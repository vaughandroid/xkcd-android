package features.comic.domain.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MissingComic {

    public static MissingComic of(ComicNumber comicNumber) {
        return new AutoValue_MissingComic(comicNumber);
    }

    public abstract ComicNumber number();
}
