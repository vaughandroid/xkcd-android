package features.comic.domain.usecases;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.models.PagedComics;
import io.reactivex.Observable;
import io.reactivex.Single;

public class GetNextPageOfComicsImpl implements ComicUseCases.GetNextPageOfComics {

    private final ComicUseCases.GetComic getComic;

    @Inject public GetNextPageOfComicsImpl(ComicUseCases.GetComic getComic) {
        this.getComic = getComic;
    }

    @Override
    public Single<PagedComics> asSingle(@NonNull ComicNumber first, int pageSize) {
        return Observable.fromIterable(first.nextPage(pageSize))
                .flatMapMaybe(comicNumber ->
                        getComic.asSingle(comicNumber)
                                .toMaybe()
                                .onErrorComplete()
                )
                .toSortedList(Comic.ascendingComparator())
                .map(comics -> PagedComics.of(comics, ComicNumber.of(first.intVal() + pageSize)));
    }

}
