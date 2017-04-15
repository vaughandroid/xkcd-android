package features.comic.domain.usecases;

import android.support.annotation.NonNull;

import java.util.List;

import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import io.reactivex.Single;

public interface ComicUseCases {

    interface GetComic {
        Single<Comic> asSingle(@NonNull ComicNumber comicNumber);
    }

    interface GetLatestComic {
        Single<Comic> asSingle();
    }

    interface GetMaximumComicNumber {
        Single<ComicNumber> asSingle();
    }

    interface GetNextPageOfComics {
        Single<List<Comic>> asSingle(@NonNull ComicNumber first, int pageSize);
    }
}