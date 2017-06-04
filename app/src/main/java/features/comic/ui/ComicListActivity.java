package features.comic.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import app.CLEActivity;
import butterknife.BindView;
import dagger.android.AndroidInjection;
import features.comic.domain.SortOrder;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.usecases.ComicUseCases;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import me.vaughandroid.xkcdreader.R;
import rx.SchedulerProvider;
import timber.log.Timber;
import util.IntentUtils;
import util.annotations.NeedsTests;

import static features.comic.domain.SortOrder.NEWEST_TO_OLDEST;
import static features.comic.domain.SortOrder.OLDEST_TO_NEWEST;

@NeedsTests
public class ComicListActivity extends CLEActivity {

    public static Intent intent(Context context) {
        return new Intent(context, ComicListActivity.class);
    }

    private ComicUseCases.GetNextPageOfComics getNextPageOfComics;
    private ComicUseCases.GetLatestComicNumber getLatestComicNumber;

    private SchedulerProvider schedulerProvider;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.comic_list_recyclerview) RecyclerView recyclerView;
    private ComicAdapter adapter;

    // TODO: Default to NEWEST_TO_OLDEST
    private SortOrder sortOrder = OLDEST_TO_NEWEST;

    @Inject
    void inject(ComicUseCases.GetNextPageOfComics getNextPageOfComics,
                ComicUseCases.GetLatestComicNumber getLatestComicNumber,
                SchedulerProvider schedulerProvider) {
        this.getNextPageOfComics = getNextPageOfComics;
        this.getLatestComicNumber = getLatestComicNumber;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        initViews();

        showLoading();
        fetchNextPageOfComics(ComicNumber.of(1));
    }

    private void initViews() {
        setContentView(R.layout.activity_comic_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ComicAdapter(
                this,
                comic -> startActivity(ViewComicActivity.intent(comic.number(), this)),
                missingComic -> startActivity(IntentUtils.browserIntent(missingComic.uri())),
                nextComicNumber -> fetchNextPageOfComics(nextComicNumber)
        );
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comic_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_newest_to_oldest:
                setSortOrder(NEWEST_TO_OLDEST);
                return true;
            case R.id.action_oldest_to_newest:
                setSortOrder(OLDEST_TO_NEWEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSortOrder(SortOrder sortOrder) {
        if (this.sortOrder != sortOrder) {
            this.sortOrder = sortOrder;

            Single<ComicNumber> firstNumberSingle;
            switch (sortOrder) {
                case OLDEST_TO_NEWEST:
                    firstNumberSingle = Single.just(ComicNumber.of(1));
                    break;
                case NEWEST_TO_OLDEST:
                    firstNumberSingle = getLatestComicNumber.asSingle();
                    break;
                default:
                    throw new IllegalStateException("Unsupported sort order: " + sortOrder);
            }

            showLoading();
            firstNumberSingle
                    .flatMap(comicNumber -> getNextPageOfComics.asSingle(comicNumber, sortOrder))
                    .observeOn(schedulerProvider.ui())
                    .subscribe(
                            pagedComics -> {
                                adapter.clear();
                                adapter.addPage(pagedComics);
                                showContent();
                            },
                            error -> {
                                Timber.e(error);
                                showError();
                            }
                    );
        }
    }

    private void fetchNextPageOfComics(ComicNumber first) {
        Disposable d = getNextPageOfComics.asSingle(first, sortOrder)
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        pagedComics -> {
                            adapter.addPage(pagedComics);
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
