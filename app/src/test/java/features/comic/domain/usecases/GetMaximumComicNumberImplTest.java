package features.comic.domain.usecases;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import features.comic.domain.models.ComicNumber;
import io.reactivex.Single;
import testutil.StubFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GetMaximumComicNumberImplTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock ComicUseCases.GetLatestComic getLatestComicUseCase;

    @Test public void returnsNumberOfLatestComic() throws Exception {
        GetMaximumComicNumberImpl it = new GetMaximumComicNumberImpl(getLatestComicUseCase);
        when(getLatestComicUseCase.asSingle()).thenReturn(Single.just(StubFactory.comic(1234)));

        ComicNumber maxNumber = it.asSingle().blockingGet();

        assertThat(maxNumber).isEqualTo(ComicNumber.create(1234));
    }

}