package features.comic.domain.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class PagedComics {

    public static PagedComics of(List<ComicResult> items) {
        return of(items, null);
    }

    public static PagedComics of(List<ComicResult> items, @Nullable ComicNumber nextComicNumber) {
        return new AutoValue_PagedComics(items, nextComicNumber);
    }

    public abstract List<ComicResult> items();

    @Nullable
    public abstract ComicNumber nextComicNumber();
}
