package features.comic.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import features.comic.domain.models.ComicNumber;

import static features.comic.domain.SortOrder.NEWEST_TO_OLDEST;
import static features.comic.domain.SortOrder.OLDEST_TO_NEWEST;
import static features.comic.domain.models.ComicNumber.of;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SortOrderTest {

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {OLDEST_TO_NEWEST, new ComicNumber[]{of(123), of(124), of(125), of(126)}},
                {NEWEST_TO_OLDEST, new ComicNumber[]{of(123), of(122), of(121), of(120)}},
        };
    }

    private final SortOrder sortOrder;
    private final ComicNumber[] expectedComicNumbers;

    public SortOrderTest(SortOrder sortOrder, ComicNumber[] expectedComicNumbers) {
        this.sortOrder = sortOrder;
        this.expectedComicNumbers = expectedComicNumbers;
    }

    @Test
    public void numbersForNextPage_Size4() throws Exception {
        List<ComicNumber> page = sortOrder.numbersForNextPage(of(123), 4);
        assertThat(page).containsExactly(expectedComicNumbers);
    }

    @Test
    public void numbersForNextPage_Size0() throws Exception {
        List<ComicNumber> page = sortOrder.numbersForNextPage(of(123), 0);
        assertThat(page).hasSize(0);
    }
}