package features.comic.domain.usecases;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import io.reactivex.Single;
import testutil.StubFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetNextPageOfComicsImplTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock ComicUseCases.GetMaximumComicNumber getMaximumComicNumberUseCase;
    @Mock ComicUseCases.GetComic getComicUseCase;

    @Test public void withinRange_FetchesExpectedNumberOfComics() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.just(ComicNumber.create(10)));
        when(getComicUseCase.asSingle(ComicNumber.create(1))).thenReturn(Single.just(StubFactory.comic(1)));
        when(getComicUseCase.asSingle(ComicNumber.create(2))).thenReturn(Single.just(StubFactory.comic(2)));
        when(getComicUseCase.asSingle(ComicNumber.create(3))).thenReturn(Single.just(StubFactory.comic(3)));

        List<Comic> comics = it.asSingle(ComicNumber.create(1), 3).blockingGet();

        assertThat(comics).containsExactly(StubFactory.comic(1), StubFactory.comic(2), StubFactory.comic(3));
    }

    @Test public void reachingEndOfRange_FetchesExpectedComics() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.just(ComicNumber.create(2)));
        when(getComicUseCase.asSingle(ComicNumber.create(1))).thenReturn(Single.just(StubFactory.comic(1)));
        when(getComicUseCase.asSingle(ComicNumber.create(2))).thenReturn(Single.just(StubFactory.comic(2)));

        List<Comic> comics = it.asSingle(ComicNumber.create(1), 10).blockingGet();

        assertThat(comics).containsExactly(StubFactory.comic(1), StubFactory.comic(2));
    }

    @Test public void pastEndOfRange_RaisesError() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.just(ComicNumber.create(100)));

        it.asSingle(ComicNumber.create(101), 50)
                .subscribe(
                        comics -> fail(),
                        throwable -> assertThat(throwable).isInstanceOf(RuntimeException.class)
                );
    }

    @Test public void comicsAreOrdered() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.just(ComicNumber.create(100)));
        //noinspection unchecked
        when(getComicUseCase.asSingle(any())).thenReturn(
                Single.just(StubFactory.comic(3)),
                Single.just(StubFactory.comic(2)),
                Single.just(StubFactory.comic(1))
        );

        List<Comic> comics = it.asSingle(ComicNumber.create(1), 3).blockingGet();

        assertThat(comics).containsExactly(StubFactory.comic(1), StubFactory.comic(2), StubFactory.comic(3));
    }

    @Test public void getComicCount_Error_RaisesError() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.error(new RuntimeException()));

        it.asSingle(ComicNumber.create(1), 1)
                .subscribe(
                        comics -> fail(),
                        throwable -> assertThat(throwable).isInstanceOf(RuntimeException.class)
                );
    }

    @Test public void getComic_SomeErrors_SkipsThoseComics() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.just(ComicNumber.create(100)));
        //noinspection unchecked
        when(getComicUseCase.asSingle(any())).thenReturn(
                Single.just(StubFactory.comic(1)),
                Single.error(new RuntimeException()),
                Single.just(StubFactory.comic(3)),
                Single.error(new RuntimeException()),
                Single.just(StubFactory.comic(5))
        );

        List<Comic> comics = it.asSingle(ComicNumber.create(1), 5).blockingGet();

        assertThat(comics).containsExactly(StubFactory.comic(1), StubFactory.comic(3), StubFactory.comic(5));
    }

    @Test public void getComic_AllErrors_RaisesError() throws Exception {
        GetNextPageOfComicsImpl it = new GetNextPageOfComicsImpl(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.asSingle()).thenReturn(Single.just(ComicNumber.create(100)));
        when(getComicUseCase.asSingle(any())).thenReturn(Single.error(new RuntimeException()));

        it.asSingle(ComicNumber.create(1), 1)
                .subscribe(
                        comics -> fail(),
                        throwable -> assertThat(throwable).isInstanceOf(RuntimeException.class)
                );
    }
}