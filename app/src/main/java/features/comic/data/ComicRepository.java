package features.comic.data;

import android.support.annotation.NonNull;

import features.comic.domain.Comic;
import features.comic.domain.ComicNumber;
import io.reactivex.Single;

public interface ComicRepository {

    Single<Comic> getLatestComic();
    Single<Comic> getComic(@NonNull ComicNumber id);
}
