package features.comic.domain.usecases;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import features.comic.data.ComicRepository;
import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import io.reactivex.Single;

public class GetComicUseCase {

    private final ComicRepository repository;

    @Inject public GetComicUseCase(ComicRepository repository) {
        this.repository = repository;
    }

    public Single<Comic> single(@NonNull ComicNumber comicNumber) {
        return repository.getComic(comicNumber);
    }
}
