package features.comic.domain.usecases;

import javax.inject.Inject;

import features.comic.data.ComicRepository;
import features.comic.domain.models.Comic;
import io.reactivex.Single;

public class GetLatestComicUseCase {

    private final ComicRepository comicRepository;

    @Inject public GetLatestComicUseCase(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    public Single<Comic> single() {
        return comicRepository.getLatestComic();
    }
}
