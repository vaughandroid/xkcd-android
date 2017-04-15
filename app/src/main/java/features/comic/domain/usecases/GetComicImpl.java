package features.comic.domain.usecases;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import features.comic.data.ComicRepository;
import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import io.reactivex.Single;

public class GetComicImpl implements ComicUseCases.GetComic {

    private final ComicRepository repository;

    @Inject public GetComicImpl(ComicRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<Comic> asSingle(@NonNull ComicNumber comicNumber) {
        return repository.getComic(comicNumber);
    }
}
