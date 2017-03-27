package features.comic.data;

import android.support.annotation.NonNull;

import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import io.reactivex.Single;

public interface ComicRepository {

    Single<Comic> getLatestComic();
    Single<Comic> getComic(@NonNull ComicNumber id);
}
