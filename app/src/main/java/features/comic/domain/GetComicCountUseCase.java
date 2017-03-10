package features.comic.domain;

import android.support.annotation.NonNull;

import io.reactivex.Single;

public class GetComicCountUseCase {

    private final GetLatestComicUseCase getLatestComicUseCase;

    public GetComicCountUseCase(@NonNull GetLatestComicUseCase getLatestComicUseCase) {
        this.getLatestComicUseCase = getLatestComicUseCase;
    }

    public Single<Integer> single() {
        return getLatestComicUseCase.single()
                .map(comic -> comic.id().intVal());
    }
}
