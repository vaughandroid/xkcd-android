package features.comic.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import app.CLEActivity;
import butterknife.BindView;
import dagger.android.AndroidInjection;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.usecases.GetComicUseCase;
import io.reactivex.disposables.Disposable;
import me.vaughandroid.xkcdreader.R;
import rx.SchedulerProvider;
import timber.log.Timber;
import util.Assertions;

public class ViewComicActivity extends CLEActivity {

    private static final String COMIC_ID = "COMIC_ID";

    public static Intent intent(@NonNull ComicNumber comicNumber, @NonNull Context context) {
        return new Intent(context, ViewComicActivity.class).putExtra(COMIC_ID, comicNumber);
    }

    @Inject GetComicUseCase getComicUseCase;

    @Inject SchedulerProvider schedulerProvider;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.view_comic_imageview) ImageView comicImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comic);

        initToolbar();
        fetchComic();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    @NonNull
    private ComicNumber getComicId() {
        ComicNumber comicNumber = (ComicNumber) getIntent().getSerializableExtra(COMIC_ID);
        Assertions.notNull(comicNumber, "Comic ID not present.");
        return comicNumber;
    }

    private void fetchComic() {
        Disposable d = getComicUseCase.single(getComicId())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        comic -> {
                            toolbar.setTitle(comic.title());
                            Picasso.with(this).load(comic.imageUri()).into(comicImageView);
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
