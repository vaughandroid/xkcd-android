package features.comic.domain.usecases;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Named;

import features.comic.domain.SortOrder;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.models.ComicResult;
import features.comic.domain.models.MissingComic;
import features.comic.domain.models.PagedComics;
import io.reactivex.Observable;
import io.reactivex.Single;

public class GetNextPageOfComicsImpl implements ComicUseCases.GetNextPageOfComics {

    private final ComicUseCases.GetComic getComic;
    private final int pageSize;

    @Inject public GetNextPageOfComicsImpl(ComicUseCases.GetComic getComic,
                                           @Named("comics_page_size") int pageSize) {
        this.getComic = getComic;
        this.pageSize = pageSize;
    }

    @Override
    public Single<PagedComics> asSingle(@NonNull ComicNumber first, SortOrder sortOrder) {
        return Observable.fromIterable(first.numbersForNextPage(pageSize))
                .flatMapSingle(comicNumber ->
                        getComic.asSingle(comicNumber)
                                .map(ComicResult::of)
                                .onErrorReturnItem(ComicResult.of(MissingComic.of(comicNumber)))
                )
                .toSortedList(ComicResult.ascendingComparator())
                .map(comics -> PagedComics.of(comics, ComicNumber.of(first.intVal() + pageSize)));
    }

}
