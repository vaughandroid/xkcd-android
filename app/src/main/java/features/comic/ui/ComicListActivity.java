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
import features.comic.data.ComicRepository;
import features.comic.data.ComicRepositoryImpl;
import features.comic.domain.ComicNumber;
import features.comic.domain.GetMaximumComicNumberUseCase;
import features.comic.domain.GetComicUseCase;
import features.comic.domain.GetLatestComicUseCase;
import features.comic.domain.GetNextPageOfComicsUseCase;
import io.reactivex.disposables.Disposable;
import me.vaughandroid.xkcdreader.R;
import timber.log.Timber;

public class ComicListActivity extends CLEActivity {

    public static final int PAGE_SIZE = 20;
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
        GetMaximumComicNumberUseCase getMaximumComicNumberUseCase = new GetMaximumComicNumberUseCase(getLatestComicUseCase);
        GetComicUseCase getComicUseCase = new GetComicUseCase(comicRepository);
        getNextPageOfComicsUseCase = new GetNextPageOfComicsUseCase(getMaximumComicNumberUseCase, getComicUseCase);

        showLoading();
        fetchNextPageOfComics(ComicNumber.create(1));
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
        Disposable d = getNextPageOfComicsUseCase.single(first, PAGE_SIZE)
                .observeOn(XKCDroidApp.schedulerProvider().ui())
                .subscribe(
                        comics -> {
                            adapter.addComics(comics);
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
