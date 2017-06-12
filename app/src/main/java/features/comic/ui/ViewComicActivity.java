package features.comic.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import app.CLEActivity;
import butterknife.BindView;
import dagger.android.AndroidInjection;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.usecases.ComicUseCases;
import features.comic.ui.ViewComicUiEvent.AltTextClicked;
import features.comic.ui.ViewComicUiEvent.FabClicked;
import io.codetail.animation.ViewAnimationUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import me.vaughandroid.xkcdreader.R;
import rx.SchedulerProvider;
import timber.log.Timber;
import util.Assertions;
import util.annotations.NeedsTests;

import static android.support.annotation.VisibleForTesting.PRIVATE;

@NeedsTests
public class ViewComicActivity extends CLEActivity {

    @VisibleForTesting(otherwise = PRIVATE)
    static final String EXTRA_COMIC_ID = "COMIC_ID";

    public static Intent intent(@NonNull ComicNumber comicNumber, @NonNull Context context) {
        return new Intent(context, ViewComicActivity.class)
                .putExtra(EXTRA_COMIC_ID, comicNumber);
    }

    private ComicUseCases.GetComic getComic;

    private SchedulerProvider schedulerProvider;
    private Observable<ViewComicVM> viewModelObservable;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.view_comic_photoview) PhotoView photoView;
    @BindView(R.id.comic_alt_text) TextView altTextView;
    @BindView(R.id.comic_alt_text_reveal) View altTextRevealView;

    private final PublishSubject<ViewComicUiEvent> uiEventPublisher = PublishSubject.create();
    private ViewComicVM viewModel;

    @Inject
    void inject(ComicUseCases.GetComic getComic, SchedulerProvider schedulerProvider
//            , Observable<ViewComicVM> viewModelObservable
    ) {
        this.getComic = getComic;
        this.schedulerProvider = schedulerProvider;
        this.viewModelObservable = viewModelObservable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        initViews();
        initSubscriptions();
        fetchComic();
    }

    private void initViews() {
        setContentView(R.layout.activity_view_comic);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void initSubscriptions() {
        Observable.merge(
                RxView.clicks(fab).map(ignored -> new FabClicked()),
                RxView.clicks(altTextView).map(ignored -> new AltTextClicked())
        ).subscribe(uiEventPublisher);

        viewModelObservable.subscribe(this::onViewModelUpdated, Timber::e);
    }

    private void onViewModelUpdated(ViewComicVM viewModel) {
        if (viewModel.showAltText() != this.viewModel.showAltText()) {
            if (viewModel.showAltText()) {
                showAltText();
            } else {
                hideAltText();
            }
        }

        this.viewModel= viewModel;
    }

    private void showAltText() {
        fab.setClickable(false);
        animateFabToolbar(true);
    }

    private void hideAltText() {
        altTextView.setClickable(false);
        animateFabToolbar(false);
    }

    private void animateFabToolbar(boolean expand) {
        int fabX = fab.getLeft() + (fab.getWidth() / 2);
        int fabY = fab.getTop() + (fab.getHeight() / 2);
        int dX = fabX - altTextRevealView.getLeft();
        int dY = fabY - altTextRevealView.getTop();
        float collapsedRadius = (float) (fab.getWidth() / 2);
        float expandedRadius = (float) Math.hypot(dX, dY);

        Animator animator = ViewAnimationUtils.createCircularReveal(
                altTextView,
                dX,
                dY,
                expand ? collapsedRadius : expandedRadius,
                expand ? expandedRadius : collapsedRadius
                );
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (expand) {
                    altTextView.setClickable(true);
                } else {
                    fab.setVisibility(View.VISIBLE);
                    fab.setClickable(true);
                    altTextView.setVisibility(View.INVISIBLE);
                }
            }
        });
        animator.start();

        if (expand) {
            fab.setVisibility(View.INVISIBLE);
            altTextView.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    private ComicNumber getComicId() {
        ComicNumber comicNumber = (ComicNumber) getIntent().getSerializableExtra(EXTRA_COMIC_ID);
        Assertions.notNull(comicNumber, "Comic ID not found.");
        return comicNumber;
    }

    private void fetchComic() {
        Disposable d = getComic.asSingle(getComicId())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        comic -> {
                            toolbar.setTitle(comic.title());
                            altTextView.setText(comic.altText());
                            Picasso.with(this).load(comic.imageUri()).into(photoView);
                            showContent();
                        },
                        error -> {
                            Timber.e(error);
                            showError();
                        }
                );
        disposables.add(d);
    }

}
