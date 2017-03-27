package features.comic.domain.usecases;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import io.reactivex.Single;

public class GetMaximumComicNumberUseCase {

    private final GetLatestComicUseCase getLatestComicUseCase;

    @Inject public GetMaximumComicNumberUseCase(@NonNull GetLatestComicUseCase getLatestComicUseCase) {
        this.getLatestComicUseCase = getLatestComicUseCase;
    }

    public Single<ComicNumber> single() {
        return getLatestComicUseCase.single()
                .map(Comic::number);
    }
}
