package features.comic.domain.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class PagedComics {

    public static PagedComics of(List<Comic> comics) {
        return of(comics, null);
    }

    public static PagedComics of(List<Comic> comics, @Nullable ComicNumber nextComicNumber) {
        return new AutoValue_PagedComics(comics, nextComicNumber);
    }

    public abstract List<Comic> comics();

    @Nullable
    public abstract ComicNumber nextComicNumber();
}
