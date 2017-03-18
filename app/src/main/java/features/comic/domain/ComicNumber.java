package features.comic.domain;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.io.Serializable;

@AutoValue
public abstract class ComicNumber implements Serializable, Comparable<ComicNumber> {

    public static ComicNumber create(int intVal) {
        return new AutoValue_ComicNumber(intVal);
    }

    public abstract int intVal();

    public ComicNumber next() {
        return ComicNumber.create(intVal() + 1);
    }

    public ComicNumber previous() {
        return ComicNumber.create(intVal() - 1);
    }

    @Override
    public int compareTo(@NonNull ComicNumber other) {
        return intVal() - other.intVal();
    }
}
