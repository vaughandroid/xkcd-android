package features.comic.domain.usecases;

import org.assertj.core.api.JUnitSoftAssertions;
import org.assertj.core.api.Java6JUnitSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collections;

import features.comic.domain.models.ComicNumber;
import features.comic.domain.models.ComicResult;
import features.comic.domain.models.PagedComics;
import io.reactivex.Single;

import static features.comic.domain.SortOrder.NEWEST_TO_OLDEST;
import static features.comic.domain.SortOrder.OLDEST_TO_NEWEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static testutil.TestModelFactory.comic;
import static testutil.TestModelFactory.comicResult;
import static testutil.TestModelFactory.missingComicResult;

public class GetNextPageOfComicsImplTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock ComicUseCases.GetComic getComicUseCase;

    private GetNextPageOfComicsImpl useCase;

    @Before public void setUp() throws Exception {
        useCase = new GetNextPageOfComicsImpl(getComicUseCase, 3);

        when(getComicUseCase.asSingle(ComicNumber.of(1))).thenReturn(Single.just(comic(1)));
        when(getComicUseCase.asSingle(ComicNumber.of(2))).thenReturn(Single.just(comic(2)));
        when(getComicUseCase.asSingle(ComicNumber.of(3))).thenReturn(Single.just(comic(3)));
        when(getComicUseCase.asSingle(ComicNumber.of(4))).thenReturn(Single.just(comic(4)));
    }

    @Test public void fetchesExpectedComics() throws Exception {
        PagedComics actual = useCase.asSingle(ComicNumber.of(1), OLDEST_TO_NEWEST).blockingGet();

        assertThat(actual.items()).containsExactlyInAnyOrder(
                ComicResult.of(comic(1)),
                ComicResult.of(comic(2)),
                ComicResult.of(comic(3))
        );
    }

    @Test public void order_oldestToNewest() throws Exception {
        PagedComics actual = useCase.asSingle(ComicNumber.of(1), OLDEST_TO_NEWEST).blockingGet();

        PagedComics expected = PagedComics.of(
                Arrays.asList(
                        comicResult(1),
                        comicResult(2),
                        comicResult(3)
                ),
                ComicNumber.of(4)
        );

        assertThat(actual).isEqualTo(expected);
    }

    @Test public void order_newestToOldest() throws Exception {
        PagedComics actual = useCase.asSingle(ComicNumber.of(4), NEWEST_TO_OLDEST).blockingGet();

        PagedComics expected = PagedComics.of(
                Arrays.asList(
                        comicResult(4),
                        comicResult(3),
                        comicResult(2)
                ),
                ComicNumber.of(1)
        );

        assertThat(actual).isEqualTo(expected);
    }

    @Test public void getComic_Errors_ReturnsMissingComics() throws Exception {
        when(getComicUseCase.asSingle(ComicNumber.of(1))).thenReturn(Single.error(new RuntimeException()));
        when(getComicUseCase.asSingle(ComicNumber.of(2))).thenReturn(Single.just(comic(2)));
        when(getComicUseCase.asSingle(ComicNumber.of(3))).thenReturn(Single.error(new RuntimeException()));

        PagedComics actual = useCase.asSingle(ComicNumber.of(1), OLDEST_TO_NEWEST).blockingGet();

        assertThat(actual.items()).containsExactly(
                missingComicResult(1),
                comicResult(2),
                missingComicResult(3)
        );
    }

}