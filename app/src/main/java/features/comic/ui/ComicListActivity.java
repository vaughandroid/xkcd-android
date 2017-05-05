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
import features.comic.domain.models.ComicNumber;
import features.comic.domain.usecases.ComicUseCases;
import io.reactivex.disposables.Disposable;
import me.vaughandroid.xkcdreader.R;
import rx.SchedulerProvider;
import timber.log.Timber;
import util.annotations.NeedsTests;

@NeedsTests
public class ComicListActivity extends CLEActivity {

    public static Intent intent(Context context) {
        return new Intent(context, ComicListActivity.class);
    }

    public static final int PAGE_SIZE = 20;

    private ComicUseCases.GetNextPageOfComics getNextPageOfComics;

    private SchedulerProvider schedulerProvider;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.comic_list_recyclerview) RecyclerView recyclerView;
    private ComicAdapter adapter;

    @Inject
    void inject(ComicUseCases.GetNextPageOfComics getNextPageOfComics, SchedulerProvider schedulerProvider) {
        this.getNextPageOfComics = getNextPageOfComics;
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
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchNextPageOfComics(ComicNumber first) {
        Disposable d = getNextPageOfComics.asSingle(first, PAGE_SIZE)
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
