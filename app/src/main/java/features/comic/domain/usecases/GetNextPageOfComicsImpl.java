package features.comic.domain.usecases;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        List<ComicNumber> comicNumbers = first.numbersForNextPage(pageSize, sortOrder);
        return Observable.fromIterable(comicNumbers)
                .flatMapSingle(comicNumber ->
                        getComic.asSingle(comicNumber)
                                .map(ComicResult::of)
                                .onErrorReturnItem(ComicResult.of(MissingComic.of(comicNumber)))
                )
                .collectInto(
                        new HashMap<ComicNumber, ComicResult>(),
                        (map, comicResult) -> map.put(comicResult.number(), comicResult)
                )
                .map(resultMap -> createSortedResultsList(resultMap, comicNumbers))
                .map(comicResults -> PagedComics.of(comicResults, ComicNumber.of(first.intVal() + pageSize)));
    }

    @NonNull
    private List<ComicResult> createSortedResultsList(HashMap<ComicNumber, ComicResult> resultMap,
                                                      List<ComicNumber> desiredOrder) {
        List<ComicResult> sortedResults = new ArrayList<>(pageSize);
        for (ComicNumber comicNumber : desiredOrder) {
            sortedResults.add(resultMap.get(comicNumber));
        }
        return sortedResults;
    }

}
