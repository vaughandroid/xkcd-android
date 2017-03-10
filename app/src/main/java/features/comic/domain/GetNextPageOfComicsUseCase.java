package features.comic.domain;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

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
                    return Observable.range(firstId.intVal(), count);
                })
                .map(ComicId::create)
                .flatMapSingle(getComicUseCase::single)
                .toSortedList(Comic.idComparator());
    }
}
