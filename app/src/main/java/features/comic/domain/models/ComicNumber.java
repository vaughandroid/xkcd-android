package features.comic.domain.models;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.io.Serializable;

@AutoValue
public abstract class ComicNumber implements Serializable, Comparable<ComicNumber> {

    public static ComicNumber of(int intVal) {
        return new AutoValue_ComicNumber(intVal);
    }

    public abstract int intVal();

    public ComicNumber next() {
        return ComicNumber.of(intVal() + 1);
    }

    public ComicNumber previous() {
        return ComicNumber.of(intVal() - 1);
    }

    @Override
    public int compareTo(@NonNull ComicNumber other) {
        return intVal() - other.intVal();
    }
}
