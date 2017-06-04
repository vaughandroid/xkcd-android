package features.comic.domain.models;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import features.comic.domain.SortOrder;

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

    public List<ComicNumber> numbersForNextPage(int size, SortOrder sortOrder) {
        switch(sortOrder) {
            case OLDEST_TO_NEWEST:
                return numbersForNextPage(size);
            case NEWEST_TO_OLDEST:
                return numbersForPreviousPage(size);
            default:
                throw new IllegalArgumentException("Unsupported sort order: " + sortOrder);
        }
    }

    @NonNull
    public List<ComicNumber> numbersForNextPage(int size) {
        ArrayList<ComicNumber> page = new ArrayList<>();
        for (int i = intVal(); i < intVal() + size; i++) {
            page.add(of(i));
        }
        return page;
    }

    @NonNull
    public List<ComicNumber> numbersForPreviousPage(int size) {
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
