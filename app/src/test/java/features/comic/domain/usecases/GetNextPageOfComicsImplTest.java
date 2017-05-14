package features.comic.domain.usecases;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import features.comic.domain.models.ComicNumber;
import features.comic.domain.models.PagedComics;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static testutil.TestModelFactory.comic;

public class GetNextPageOfComicsImplTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock ComicUseCases.GetComic getComicUseCase;

    private final TestObserver<PagedComics> testObserver = new TestObserver<>();

    @Test public void fetchesExpectedComics() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getComicUseCase);
        when(getComicUseCase.asSingle(ComicNumber.of(1))).thenReturn(Single.just(comic(1)));
        when(getComicUseCase.asSingle(ComicNumber.of(2))).thenReturn(Single.just(comic(2)));
        when(getComicUseCase.asSingle(ComicNumber.of(3))).thenReturn(Single.just(comic(3)));

        PagedComics expected = PagedComics.of(asList(comic(1), comic(2), comic(3)), ComicNumber.of(4));

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

        PagedComics expected = PagedComics.of(asList(comic(1), comic(2), comic(3)), ComicNumber.of(4));

        PagedComics actual = it.asSingle(ComicNumber.of(1), 3).blockingGet();

        assertThat(actual).isEqualTo(expected);
    }

    @Test public void getComicCount_Error_RaisesError() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getComicUseCase);

        it.asSingle(ComicNumber.of(1), 1).subscribe(testObserver);

        testObserver.assertError(RuntimeException.class);
    }

    @Test public void getComic_SomeErrors_SkipsThoseComics() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getComicUseCase);
        //noinspection unchecked
        when(getComicUseCase.asSingle(any())).thenReturn(
                Single.just(comic(100)),
                Single.error(new RuntimeException()),
                Single.just(comic(102)),
                Single.error(new RuntimeException()),
                Single.just(comic(104))
        );

        PagedComics expected = PagedComics.of(asList(comic(100), comic(102), comic(104)), ComicNumber.of(105));

        PagedComics actual = it.asSingle(ComicNumber.of(100), 5).blockingGet();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getComic_ErrorOnFinalComic_NextIsCorrect() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getComicUseCase);
        //noinspection unchecked
        when(getComicUseCase.asSingle(any())).thenReturn(
                Single.just(comic(100)),
                Single.just(comic(101)),
                Single.error(new RuntimeException())
        );

        PagedComics expected = PagedComics.of(asList(comic(100), comic(101)), ComicNumber.of(103));

        PagedComics actual = it.asSingle(ComicNumber.of(100), 3).blockingGet();

        assertThat(actual).isEqualTo(expected);
    }
}