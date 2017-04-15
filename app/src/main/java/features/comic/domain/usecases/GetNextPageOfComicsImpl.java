package features.comic.domain.usecases;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import io.reactivex.Observable;
import io.reactivex.Single;

public class GetNextPageOfComicsImpl implements ComicUseCases.GetNextPageOfComics {

    private final ComicUseCases.GetMaximumComicNumber getMaximumComicNumber;
    private final ComicUseCases.GetComic getComic;

    @Inject public GetNextPageOfComicsImpl(ComicUseCases.GetMaximumComicNumber getMaximumComicNumber,
                                           ComicUseCases.GetComic getComic) {
        this.getMaximumComicNumber = getMaximumComicNumber;
        this.getComic = getComic;
    }

    @Override
    public Single<List<Comic>> asSingle(@NonNull ComicNumber first, int pageSize) {
        return getMaximumComicNumber.asSingle()
                .flatMapObservable(maxNumber -> {
                    List<ComicNumber> comicNumbers = new ArrayList<>();
                    ComicNumber currentNumber = first;
                    for (int i = 0; i < pageSize && currentNumber.compareTo(maxNumber) <= 0; i++) {
                        comicNumbers.add(currentNumber);
                        currentNumber = currentNumber.next();
                    }
                    return Observable.fromIterable(comicNumbers);
                })
                .flatMapMaybe((comicNum) -> getComic.asSingle(comicNum).toMaybe().onErrorComplete())
                .toSortedList(Comic.ascendingComparator())
                .map(comics -> {
                    if (comics.isEmpty()) {
                        throw new RuntimeException("Failed to fetch any comics");
                    }
                    return comics;
                });
    }
}
