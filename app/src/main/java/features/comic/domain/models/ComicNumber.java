package features.comic.domain.models;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class ComicNumber implements Serializable, Comparable<ComicNumber> { // XXX no longer needs to be Comparable?

    public static ComicNumber of(int intVal) {
        return new AutoValue_ComicNumber(intVal);
    }

    public abstract int intVal();

    @NonNull
    public ComicNumber next() {
        return ComicNumber.of(intVal() + 1);
    }

    @NonNull
    public ComicNumber previous() {
        return ComicNumber.of(intVal() - 1);
    }

    @Override
    public int compareTo(@NonNull ComicNumber other) {
        return intVal() - other.intVal();
    }
}
