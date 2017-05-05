package features.comic.domain.usecases;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import features.comic.domain.models.ComicNumber;
import features.comic.domain.models.PagedComics;
import io.reactivex.Single;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static testutil.TestModelFactory.comic;

public class GetNextPageOfComicsImplTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock ComicUseCases.GetMaximumComicNumber getMaximumComicNumberUseCase;
    @Mock ComicUseCases.GetComic getComicUseCase;

    @Test public void withinRange_FetchesExpectedNumberOfComics() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.just(ComicNumber.of(10)));
        when(getComicUseCase.asSingle(ComicNumber.of(1))).thenReturn(Single.just(comic(1)));
        when(getComicUseCase.asSingle(ComicNumber.of(2))).thenReturn(Single.just(comic(2)));
        when(getComicUseCase.asSingle(ComicNumber.of(3))).thenReturn(Single.just(comic(3)));

        PagedComics expected = PagedComics.of(asList(comic(1), comic(2), comic(3)), ComicNumber.of(4));

        PagedComics actual = it.asSingle(ComicNumber.of(1), 3).blockingGet();

        assertThat(actual).isEqualTo(expected);
    }

    @Test public void reachingEndOfRange_FetchesExpectedComics() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.just(ComicNumber.of(2)));
        when(getComicUseCase.asSingle(ComicNumber.of(1))).thenReturn(Single.just(comic(1)));
        when(getComicUseCase.asSingle(ComicNumber.of(2))).thenReturn(Single.just(comic(2)));

        PagedComics expected = PagedComics.of(asList(comic(1), comic(2)), ComicNumber.of(3));

        PagedComics actual = it.asSingle(ComicNumber.of(1), 10).blockingGet();

        assertThat(actual).isEqualTo(expected);
    }

    @Test public void pastEndOfRange_RaisesError() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.just(ComicNumber.of(100)));

        it.asSingle(ComicNumber.of(101), 50)
                .subscribe(
                        comics -> fail(),
                        throwable -> assertThat(throwable).isInstanceOf(RuntimeException.class)
                );
    }

    @Test public void comicsAreOrdered() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.just(ComicNumber.of(100)));
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
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.error(new RuntimeException()));

        it.asSingle(ComicNumber.of(1), 1)
                .subscribe(
                        comics -> fail(),
                        throwable -> assertThat(throwable).isInstanceOf(RuntimeException.class)
                );
    }

    @Test public void getComic_SomeErrors_SkipsThoseComics() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.just(ComicNumber.of(100)));
        //noinspection unchecked
        when(getComicUseCase.asSingle(any())).thenReturn(
                Single.just(comic(1)),
                Single.error(new RuntimeException()),
                Single.just(comic(3)),
                Single.error(new RuntimeException()),
                Single.just(comic(5))
        );

        PagedComics expected = PagedComics.of(asList(comic(1), comic(3), comic(5)), ComicNumber.of(6));

        PagedComics actual = it.asSingle(ComicNumber.of(1), 5).blockingGet();

        assertThat(actual).isEqualTo(expected);
    }

    @Test public void getComic_AllErrors_RaisesError() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.just(ComicNumber.of(100)));
        when(getComicUseCase.asSingle(any())).thenReturn(Single.error(new RuntimeException()));

        it.asSingle(ComicNumber.of(1), 1)
                .subscribe(
                        comics -> fail(),
                        throwable -> assertThat(throwable).isInstanceOf(RuntimeException.class)
                );
    }
}