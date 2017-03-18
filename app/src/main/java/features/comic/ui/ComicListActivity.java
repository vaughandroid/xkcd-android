package features.comic.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import app.CLEActivity;
import app.XKCDroidApp;
import butterknife.BindView;
import butterknife.OnClick;
import features.comic.data.ComicRepository;
import features.comic.data.ComicRepositoryImpl;
import features.comic.domain.ComicId;
import features.comic.domain.GetComicCountUseCase;
import features.comic.domain.GetComicUseCase;
import features.comic.domain.GetLatestComicUseCase;
import features.comic.domain.GetNextPageOfComicsUseCase;
import io.reactivex.disposables.Disposable;
import me.vaughandroid.xkcdreader.R;
import rx.AndroidSchedulerProvider;
import rx.SchedulerProvider;
import timber.log.Timber;

public class ComicListActivity extends CLEActivity {

    @Inject GetNextPageOfComicsUseCase getNextPageOfComicsUseCase;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.comic_list_recyclerview) RecyclerView recyclerView;
    private ComicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();

        // TODO: Inject these
        ComicRepository comicRepository = new ComicRepositoryImpl();
        GetLatestComicUseCase getLatestComicUseCase = new GetLatestComicUseCase(comicRepository);
        GetComicCountUseCase getComicCountUseCase = new GetComicCountUseCase(getLatestComicUseCase);
        GetComicUseCase getComicUseCase = new GetComicUseCase(comicRepository);
        getNextPageOfComicsUseCase = new GetNextPageOfComicsUseCase(getComicCountUseCase, getComicUseCase);

        fetchComics();
    }

    private void initViews() {
        setContentView(R.layout.activity_comic_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ComicAdapter(this);
        adapter.setOnComicClickedListener(
                comic -> startActivity(ViewComicActivity.intent(comic.id(), this))
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

    @OnClick(R.id.fab)
    void onClickFab() {
        startActivity(ViewComicActivity.intent(ComicId.create(123), this));
    }

    private void fetchComics() {
        showLoading();
        Disposable d = getNextPageOfComicsUseCase.single(ComicId.create(1), 20)
                .observeOn(XKCDroidApp.schedulerProvider().ui())
                .subscribe(
                        comics -> {
                            adapter.setComics(comics);
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
