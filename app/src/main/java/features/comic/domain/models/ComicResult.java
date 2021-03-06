package features.comic.domain.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

@AutoValue
public abstract class ComicResult {

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

    public void continued(Consumer<Comic> continuationComic,
                          Consumer<MissingComic> continuationMissing) {
        try {
            if (comic() != null) {
                continuationComic.accept(comic());
            } else {
                continuationMissing.accept(missingComic());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T join(Function<Comic, T> joinComic, Function<MissingComic, T> joinMissing) {
        T result;
        try {
            if (comic() != null) {
                result = joinComic.apply(comic());
            } else {
                result = joinMissing.apply(missingComic());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
