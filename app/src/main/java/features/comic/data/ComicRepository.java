package features.comic.data;

import android.support.annotation.NonNull;

import features.comic.domain.Comic;
import features.comic.domain.ComicId;
import io.reactivex.Single;

public interface ComicRepository {

    Single<Comic> getComic(@NonNull ComicId id);
}
