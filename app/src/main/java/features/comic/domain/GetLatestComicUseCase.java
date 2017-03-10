package features.comic.domain;

import features.comic.data.ComicRepository;
import io.reactivex.Single;

public class GetLatestComicUseCase {

    private final ComicRepository comicRepository;

    public GetLatestComicUseCase(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    public Single<Comic> single() {
        return comicRepository.getLatestComic();
    }
}
