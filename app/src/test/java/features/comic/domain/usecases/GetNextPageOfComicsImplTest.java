package features.comic.domain.usecases;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import features.comic.domain.models.ComicNumber;
import features.comic.domain.models.ComicResult;
import features.comic.domain.models.PagedComics;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static testutil.TestModelFactory.comic;
import static testutil.TestModelFactory.comicResult;
import static testutil.TestModelFactory.missingComicResult;

public class GetNextPageOfComicsImplTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock ComicUseCases.GetComic getComicUseCase;

    private final TestObserver<PagedComics> testObserver = new TestObserver<>();

    @Test public void fetchesExpectedComics() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getComicUseCase);
        when(getComicUseCase.asSingle(ComicNumber.of(1))).thenReturn(Single.just(comic(1)));
        when(getComicUseCase.asSingle(ComicNumber.of(2))).thenReturn(Single.just(comic(2)));
        when(getComicUseCase.asSingle(ComicNumber.of(3))).thenReturn(Single.just(comic(3)));

        PagedComics expected = PagedComics.of(asList(ComicResult.of(comic(1)), ComicResult.of(comic(2)), ComicResult.of(comic(3))), ComicNumber.of(4));

        PagedComics actual = it.asSingle(ComicNumber.of(1), 3).blockingGet();

        assertThat(actual).isEqualTo(expected);
    }

    @Test public void comicsAreOrdered() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getComicUseCase);
        //noinspection unchecked
        when(getComicUseCase.asSingle(any())).thenReturn(
                Single.just(comic(3)),
                Single.just(comic(2)),
                Single.just(comic(1))
        );

        PagedComics expected = PagedComics.of(
                asList(comicResult(1), comicResult(2), comicResult(3)),
                ComicNumber.of(4)
        );

        PagedComics actual = it.asSingle(ComicNumber.of(1), 3).blockingGet();

        assertThat(actual).isEqualTo(expected);
    }

    @Test public void getComicCount_Error_RaisesError() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getComicUseCase);

        it.asSingle(ComicNumber.of(1), 1).subscribe(testObserver);

        testObserver.assertError(RuntimeException.class);
    }

    @Test public void getComic_Errors_ReturnsMissingComics() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getComicUseCase);
        //noinspection unchecked
        when(getComicUseCase.asSingle(any())).thenReturn(
                Single.just(comic(100)),
                Single.error(new RuntimeException()),
                Single.just(comic(102)),
                Single.error(new RuntimeException())
        );

        PagedComics expected = PagedComics.of(
                asList(
                        comicResult(100),
                        missingComicResult(101),
                        comicResult(102),
                        missingComicResult(103)
                ),
                ComicNumber.of(104));

        PagedComics actual = it.asSingle(ComicNumber.of(100), 4).blockingGet();

        assertThat(actual).isEqualTo(expected);
    }

}