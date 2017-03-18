package features.comic.domain;

import android.support.annotation.NonNull;

import features.comic.data.ComicRepository;
import io.reactivex.Single;

public class GetComicUseCase {

    private final ComicRepository repository;

    public GetComicUseCase(@NonNull ComicRepository repository) {
        this.repository = repository;
    }

    public Single<Comic> single(@NonNull ComicNumber comicNumber) {
        return repository.getComic(comicNumber);
    }
}
