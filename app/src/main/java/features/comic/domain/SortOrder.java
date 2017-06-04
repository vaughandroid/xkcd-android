package features.comic.domain;

import java.util.ArrayList;
import java.util.List;

import features.comic.domain.models.ComicNumber;

public enum SortOrder {
    NEWEST_TO_OLDEST,
    OLDEST_TO_NEWEST;

    public List<ComicNumber> numbersForNextPage(ComicNumber firstNumber, int size) {
        switch(this) {
            case OLDEST_TO_NEWEST:
                ArrayList<ComicNumber> page1 = new ArrayList<>();
                for (int i1 = firstNumber.intVal(); i1 < firstNumber.intVal() + size; i1++) {
                    page1.add(ComicNumber.of(i1));
                }
                return page1;
            case NEWEST_TO_OLDEST:
                ArrayList<ComicNumber> page = new ArrayList<>();
                for (int i = firstNumber.intVal(); i > firstNumber.intVal() - size; i--) {
                    page.add(ComicNumber.of(i));
                }
                return page;
            default:
                throw new IllegalArgumentException("Unsupported sort order: " + this);
        }
    }
}
