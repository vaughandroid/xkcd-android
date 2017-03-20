package features.comic.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class GetNextPageOfComicsUseCase {

    private final GetMaximumComicNumberUseCase getMaximumComicNumberUseCase;
    private final GetComicUseCase getComicUseCase;

    @Inject public GetNextPageOfComicsUseCase(GetMaximumComicNumberUseCase getMaximumComicNumberUseCase,
                                              GetComicUseCase getComicUseCase) {
        this.getMaximumComicNumberUseCase = getMaximumComicNumberUseCase;
        this.getComicUseCase = getComicUseCase;
    }

    public Single<List<Comic>> single(@NonNull ComicNumber first, int pageSize) {
        return getMaximumComicNumberUseCase.single()
                .flatMapObservable(maxNumber -> {
                    List<ComicNumber> comicNumbers = new ArrayList<>();
                    ComicNumber currentNumber = first;
                    for (int i = 0; i < pageSize && currentNumber.compareTo(maxNumber) <= 0; i++) {
                        comicNumbers.add(currentNumber);
                        currentNumber = currentNumber.next();
                    }
                    return Observable.fromIterable(comicNumbers);
                })
                .flatMapMaybe((comicNum) -> getComicUseCase.single(comicNum).toMaybe().onErrorComplete())
                .toSortedList(Comic.ascendingComparator())
                .map(comics -> {
                    if (comics.isEmpty()) {
                        throw new RuntimeException("Failed to fetch any comics");
                    }
                    return comics;
                });
    }
}
