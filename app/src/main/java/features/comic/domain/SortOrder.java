package features.comic.domain;

import java.util.ArrayList;
import java.util.List;

import features.comic.domain.models.ComicNumber;

public enum SortOrder {
    NEWEST_TO_OLDEST,
    OLDEST_TO_NEWEST;

    public List<ComicNumber> numbersForNextPage(ComicNumber firstNumber, int size) {
        ArrayList<ComicNumber> comicNumbers = new ArrayList<>();
        ComicNumber currentNumber = firstNumber;
        for (int i = 0; i < size; i++) {
            comicNumbers.add(currentNumber);
            currentNumber = next(currentNumber);
        }
        return comicNumbers;
    }

    public ComicNumber next(ComicNumber currentNumber) {
        switch(this) {
            case OLDEST_TO_NEWEST:
                return currentNumber.next();
            case NEWEST_TO_OLDEST:
                return currentNumber.previous();
            default:
                throw new UnsupportedOperationException("Not supported for: " + this.name());
        }
    }
}
