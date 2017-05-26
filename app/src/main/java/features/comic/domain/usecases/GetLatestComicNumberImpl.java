package features.comic.domain.usecases;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import io.reactivex.Single;

public class GetLatestComicNumberImpl implements ComicUseCases.GetLatestComicNumber {

    private final ComicUseCases.GetLatestComic getLatestComic;

    @Inject public GetLatestComicNumberImpl(@NonNull ComicUseCases.GetLatestComic getLatestComic) {
        this.getLatestComic = getLatestComic;
    }

    @Override
    public Single<ComicNumber> asSingle() {
        return getLatestComic.asSingle()
                .map(Comic::number);
    }
}
