package features.comic.domain.actions;

import com.google.auto.value.AutoValue;

import common.domain.Action;
import features.comic.domain.models.ComicNumber;

@AutoValue
public abstract class FetchComic implements Action {

    public static FetchComic create(ComicNumber comicNumber) {
        return new AutoValue_FetchComic(comicNumber);
    }

    public abstract ComicNumber comicNumber();
}
