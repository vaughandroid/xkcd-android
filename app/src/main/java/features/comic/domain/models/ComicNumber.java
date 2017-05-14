package features.comic.domain.models;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class ComicNumber implements Serializable, Comparable<ComicNumber> {

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

    @NonNull
    public List<ComicNumber> nextPage(int size) {
        ArrayList<ComicNumber> page = new ArrayList<>();
        for (int i = intVal(); i < intVal() + size; i++) {
            page.add(of(i));
        }
        return page;
    }

    @NonNull
    public List<ComicNumber> previousPage(int size) {
        ArrayList<ComicNumber> page = new ArrayList<>();
        for (int i = intVal(); i > intVal() - size; i--) {
            page.add(of(i));
        }
        return page;
    }

    @Override
    public int compareTo(@NonNull ComicNumber other) {
        return intVal() - other.intVal();
    }
}
