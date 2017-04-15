package features.comic.domain.usecases;

import javax.inject.Inject;

import features.comic.data.ComicRepository;
import features.comic.domain.models.Comic;
import io.reactivex.Single;

public class GetLatestComicImpl implements ComicUseCases.GetLatestComic {

    private final ComicRepository comicRepository;

    @Inject public GetLatestComicImpl(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    @Override
    public Single<Comic> asSingle() {
        return comicRepository.getLatestComic();
    }
}
