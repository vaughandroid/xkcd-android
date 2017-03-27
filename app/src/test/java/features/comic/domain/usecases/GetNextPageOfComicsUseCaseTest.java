package features.comic.domain.usecases;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.usecases.GetComicUseCase;
import features.comic.domain.usecases.GetMaximumComicNumberUseCase;
import features.comic.domain.usecases.GetNextPageOfComicsUseCase;
import io.reactivex.Single;
import testutil.StubFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetNextPageOfComicsUseCaseTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    GetMaximumComicNumberUseCase getMaximumComicNumberUseCase;
    @Mock
    GetComicUseCase getComicUseCase;

    @Test public void withinRange_FetchesExpectedNumberOfComics() throws Exception {
        GetNextPageOfComicsUseCase it = new GetNextPageOfComicsUseCase(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.single()).thenReturn(Single.just(ComicNumber.create(10)));
        when(getComicUseCase.single(ComicNumber.create(1))).thenReturn(Single.just(StubFactory.comic(1)));
        when(getComicUseCase.single(ComicNumber.create(2))).thenReturn(Single.just(StubFactory.comic(2)));
        when(getComicUseCase.single(ComicNumber.create(3))).thenReturn(Single.just(StubFactory.comic(3)));

        List<Comic> comics = it.single(ComicNumber.create(1), 3).blockingGet();

        assertThat(comics).containsExactly(StubFactory.comic(1), StubFactory.comic(2), StubFactory.comic(3));
    }

    @Test public void reachingEndOfRange_FetchesExpectedComics() throws Exception {
        GetNextPageOfComicsUseCase it = new GetNextPageOfComicsUseCase(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.single()).thenReturn(Single.just(ComicNumber.create(2)));
        when(getComicUseCase.single(ComicNumber.create(1))).thenReturn(Single.just(StubFactory.comic(1)));
        when(getComicUseCase.single(ComicNumber.create(2))).thenReturn(Single.just(StubFactory.comic(2)));

        List<Comic> comics = it.single(ComicNumber.create(1), 10).blockingGet();

        assertThat(comics).containsExactly(StubFactory.comic(1), StubFactory.comic(2));
    }

    @Test public void pastEndOfRange_RaisesError() throws Exception {
        GetNextPageOfComicsUseCase it = new GetNextPageOfComicsUseCase(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.single()).thenReturn(Single.just(ComicNumber.create(100)));

        it.single(ComicNumber.create(101), 50)
                .subscribe(
                        comics -> fail(),
                        throwable -> assertThat(throwable).isInstanceOf(RuntimeException.class)
                );
    }

    @Test public void comicsAreOrdered() throws Exception {
        GetNextPageOfComicsUseCase it = new GetNextPageOfComicsUseCase(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.single()).thenReturn(Single.just(ComicNumber.create(100)));
        //noinspection unchecked
        when(getComicUseCase.single(any())).thenReturn(
                Single.just(StubFactory.comic(3)),
                Single.just(StubFactory.comic(2)),
                Single.just(StubFactory.comic(1))
        );

        List<Comic> comics = it.single(ComicNumber.create(1), 3).blockingGet();

        assertThat(comics).containsExactly(StubFactory.comic(1), StubFactory.comic(2), StubFactory.comic(3));
    }

    @Test public void getComicCount_Error_RaisesError() throws Exception {
        GetNextPageOfComicsUseCase it = new GetNextPageOfComicsUseCase(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.single()).thenReturn(Single.error(new RuntimeException()));

        it.single(ComicNumber.create(1), 1)
                .subscribe(
                        comics -> fail(),
                        throwable -> assertThat(throwable).isInstanceOf(RuntimeException.class)
                );
    }

    @Test public void getComic_SomeErrors_SkipsThoseComics() throws Exception {
        GetNextPageOfComicsUseCase it = new GetNextPageOfComicsUseCase(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.single()).thenReturn(Single.just(ComicNumber.create(100)));
        //noinspection unchecked
        when(getComicUseCase.single(any())).thenReturn(
                Single.just(StubFactory.comic(1)),
                Single.error(new RuntimeException()),
                Single.just(StubFactory.comic(3)),
                Single.error(new RuntimeException()),
                Single.just(StubFactory.comic(5))
        );

        List<Comic> comics = it.single(ComicNumber.create(1), 5).blockingGet();

        assertThat(comics).containsExactly(StubFactory.comic(1), StubFactory.comic(3), StubFactory.comic(5));
    }

    @Test public void getComic_AllErrors_RaisesError() throws Exception {
        GetNextPageOfComicsUseCase it = new GetNextPageOfComicsUseCase(getMaximumComicNumberUseCase, getComicUseCase);
        when(getMaximumComicNumberUseCase.single()).thenReturn(Single.just(ComicNumber.create(100)));
        when(getComicUseCase.single(any())).thenReturn(Single.error(new RuntimeException()));

        it.single(ComicNumber.create(1), 1)
                .subscribe(
                        comics -> fail(),
                        throwable -> assertThat(throwable).isInstanceOf(RuntimeException.class)
                );
    }
}