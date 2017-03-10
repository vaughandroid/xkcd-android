package features.comic.domain;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.Single;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GetComicCountUseCaseTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock GetLatestComicUseCase getLatestComicUseCase;
    @Mock Comic comic;

    @Test public void returnsNumberOfLatestComic() throws Exception {
        GetComicCountUseCase it = new GetComicCountUseCase(getLatestComicUseCase);
        when(getLatestComicUseCase.single()).thenReturn(Single.just(comic));
        when(comic.id()).thenReturn(ComicId.create(1234));

        int count = it.single().blockingGet();

        assertThat(count).isEqualTo(1234);
    }

}