package features.comic.domain;

import android.net.Uri;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.threeten.bp.LocalDate;

import java.util.List;

import io.reactivex.Single;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetNextPageOfComicsUseCaseTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock GetComicCountUseCase getComicCountUseCase;
    @Mock GetComicUseCase getComicUseCase;

    @Test public void withinRange_FetchesExpectedNumberOfComics() throws Exception {
        GetNextPageOfComicsUseCase it = new GetNextPageOfComicsUseCase(getComicCountUseCase, getComicUseCase);
        when(getComicCountUseCase.single()).thenReturn(Single.just(10));
        when(getComicUseCase.single(ComicId.create(1))).thenReturn(Single.just(stubComic(1)));
        when(getComicUseCase.single(ComicId.create(2))).thenReturn(Single.just(stubComic(2)));
        when(getComicUseCase.single(ComicId.create(3))).thenReturn(Single.just(stubComic(3)));

        List<Comic> comics = it.single(ComicId.create(1), 3).blockingGet();

        assertThat(comics).containsExactly(stubComic(1), stubComic(2), stubComic(3));
    }

    @Test public void reachingEndOfRange_FetchesExpectedComics() throws Exception {
        GetNextPageOfComicsUseCase it = new GetNextPageOfComicsUseCase(getComicCountUseCase, getComicUseCase);
        when(getComicCountUseCase.single()).thenReturn(Single.just(2));
        when(getComicUseCase.single(ComicId.create(1))).thenReturn(Single.just(stubComic(1)));
        when(getComicUseCase.single(ComicId.create(2))).thenReturn(Single.just(stubComic(2)));

        List<Comic> comics = it.single(ComicId.create(1), 10).blockingGet();

        assertThat(comics).containsExactly(stubComic(1), stubComic(2));
    }

    @Test public void pastEndOfRange_ReturnsEmptyList() throws Exception {
        GetNextPageOfComicsUseCase it = new GetNextPageOfComicsUseCase(getComicCountUseCase, getComicUseCase);
        when(getComicCountUseCase.single()).thenReturn(Single.just(100));
        when(getComicUseCase.single(any())).thenThrow(new RuntimeException());

        List<Comic> comics = it.single(ComicId.create(101), 50).blockingGet();

        assertThat(comics).isEmpty();
    }

    @Test public void comicsAreOrdered() throws Exception {
        GetNextPageOfComicsUseCase it = new GetNextPageOfComicsUseCase(getComicCountUseCase, getComicUseCase);
        when(getComicCountUseCase.single()).thenReturn(Single.just(100));
        when(getComicUseCase.single(any())).thenReturn(
                Single.just(stubComic(3)),
                Single.just(stubComic(2)),
                Single.just(stubComic(1))
        );

        List<Comic> comics = it.single(ComicId.create(1), 3).blockingGet();

        assertThat(comics).containsExactly(stubComic(1), stubComic(2), stubComic(3));
    }

    // TODO: Test failures. Skip item if it fails? Replace with placeholder?

    private Comic stubComic(int num) {
        return Comic.builder()
                .id(ComicId.create(num))
                .date(LocalDate.parse("2017-03-10"))
                .title("title " + num)
                .altText("alt text " + num)
                .imageUri(Uri.EMPTY)
                .build();
    }
}