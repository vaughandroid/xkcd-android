package features.comic.domain.usecases;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import features.comic.domain.models.ComicNumber;
import features.comic.domain.usecases.GetLatestComicUseCase;
import features.comic.domain.usecases.GetMaximumComicNumberUseCase;
import io.reactivex.Single;
import testutil.StubFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GetMaximumComicNumberUseCaseTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    GetLatestComicUseCase getLatestComicUseCase;

    @Test public void returnsNumberOfLatestComic() throws Exception {
        GetMaximumComicNumberUseCase it = new GetMaximumComicNumberUseCase(getLatestComicUseCase);
        when(getLatestComicUseCase.single()).thenReturn(Single.just(StubFactory.comic(1234)));

        ComicNumber maxNumber = it.single().blockingGet();

        assertThat(maxNumber).isEqualTo(ComicNumber.create(1234));
    }

}