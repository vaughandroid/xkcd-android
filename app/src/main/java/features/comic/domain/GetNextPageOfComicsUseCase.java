package features.comic.domain;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.util.ExceptionHelper;

public class GetNextPageOfComicsUseCase {

    private final GetComicCountUseCase getComicCountUseCase;
    private final GetComicUseCase getComicUseCase;

    public GetNextPageOfComicsUseCase(@NonNull GetComicCountUseCase getComicCountUseCase,
                                      @NonNull GetComicUseCase getComicUseCase) {
        this.getComicCountUseCase = getComicCountUseCase;
        this.getComicUseCase = getComicUseCase;
    }

    public Single<List<Comic>> single(@NonNull ComicId firstId, int pageSize) {
        return getComicCountUseCase.single()
                .flatMapObservable(maxCount -> {
                    int count = Math.min(pageSize, maxCount - firstId.intVal() + 1);
                    if (count > 0) {
                        return Observable.range(firstId.intVal(), count);
                    } else {
                        throw new IndexOutOfBoundsException("Past end of range");
                    }
                })
                .map(ComicId::create)
                .flatMapMaybe((comicId) -> getComicUseCase.single(comicId).toMaybe().onErrorComplete())
                .toSortedList(Comic.idComparator())
                .map(comics -> {
                    if (comics.isEmpty()) {
                        throw new RuntimeException("Failed to fetch any comics");
                    }
                    return comics;
                });
    }
}
