package features.comic.domain.usecases;

import android.support.annotation.NonNull;

import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.models.PagedComics;
import io.reactivex.Single;

public interface ComicUseCases {

    interface GetComic {
        // TODO: Support MissingComics?
        Single<Comic> asSingle(@NonNull ComicNumber comicNumber);
    }

    interface GetLatestComic {
        Single<Comic> asSingle();
    }

    interface GetMaximumComicNumber {
        Single<ComicNumber> asSingle();
    }

    interface GetNextPageOfComics {
        Single<PagedComics> asSingle(@NonNull ComicNumber first, int pageSize);
    }
}
