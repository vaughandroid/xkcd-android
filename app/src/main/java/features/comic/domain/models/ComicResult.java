package features.comic.domain.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.Comparator;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

@AutoValue
public abstract class ComicResult {

    public static Comparator<ComicResult> ascendingComparator() {
        return (c1, c2) -> c1.number().compareTo(c2.number());
    }

    public static ComicResult of(Comic comic) {
        return new AutoValue_ComicResult(comic, null);
    }

    public static ComicResult of(MissingComic missingComic) {
        return new AutoValue_ComicResult(null, missingComic);
    }

    @Nullable
    abstract Comic comic();

    @Nullable
    abstract MissingComic missingComic();

    public ComicNumber number() {
        return join(Comic::number, MissingComic::number);
    }

    public void continued(Consumer<Comic> continuationLeft, Consumer<MissingComic> continuationRight) {
        try {
            if (comic() != null) {
                continuationLeft.accept(comic());
            } else {
                continuationRight.accept(missingComic());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T join(Function<Comic, T> joinLeft, Function<MissingComic, T> joinRight) {
        T result;
        try {
            if (comic() != null) {
                result = joinLeft.apply(comic());
            } else {
                result = joinRight.apply(missingComic());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
