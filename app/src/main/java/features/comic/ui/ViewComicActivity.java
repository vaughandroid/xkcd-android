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
import features.comic.data.ComicRepositoryImpl;
import features.comic.domain.Comic;
import features.comic.domain.ComicId;
import features.comic.domain.GetComicUseCase;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.vaughandroid.xkcdreader.R;
import timber.log.Timber;
import util.Assertions;

public class ViewComicActivity extends CLEActivity {

    private static final String COMIC_ID = "COMIC_ID";

    public static Intent intent(@NonNull ComicId comicId, @NonNull Context context) {
        return new Intent(context, ViewComicActivity.class).putExtra(COMIC_ID, comicId);
    }

    @Inject GetComicUseCase getComicUseCase;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.view_comic_imageview) ImageView comicImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comic);

        // TODO: Inject this
        getComicUseCase = new GetComicUseCase(new ComicRepositoryImpl());

        initToolbar();
        fetchComic();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    @NonNull
    private ComicId getComicId() {
        ComicId comicId = (ComicId) getIntent().getSerializableExtra(COMIC_ID);
        Assertions.notNull(comicId, "Comic ID not present.");
        return comicId;
    }

    private void fetchComic() {
        Disposable d = getComicUseCase.single(getComicId())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(comic -> setComic(comic))
                .doOnError(error -> Timber.e(error))
                .subscribe();
        disposables.add(d);
    }

    private void setComic(@NonNull Comic comic) {
        toolbar.setTitle(comic.title());
        Picasso.with(this).load(comic.imageUri()).into(comicImageView);
        showContent();
    }

}
