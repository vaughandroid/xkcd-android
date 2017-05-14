package features.comic.domain.models;

import org.junit.Test;

import testutil.TestModelFactory;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class ComicResultTest {

    private final Comic comic = TestModelFactory.comic(123);
    private final MissingComic missingComic = TestModelFactory.missingComic(456);

    @Test
    public void continued_comic() throws Exception {
        ComicResult it = ComicResult.of(comic);

        it.continued(comic -> assertThat(comic).isSameAs(this.comic), missingComic -> fail());
    }

    @Test
    public void continued_missingComic() throws Exception {
        ComicResult it = ComicResult.of(missingComic);

        it.continued(comic -> fail(), missingComic -> assertThat(missingComic).isSameAs(this.missingComic));
    }

    @Test
    public void join_comic() throws Exception {
        ComicResult comicResult = ComicResult.of(comic);

        String result = comicResult.join(comic -> "comic", missingComic -> "missing");

        assertThat(result).isEqualTo("comic");
    }

    @Test
    public void join_missingComic() throws Exception {
        ComicResult comicResult = ComicResult.of(missingComic);

        String result = comicResult.join(comic -> "comic", missingComic -> "missing");

        assertThat(result).isEqualTo("missing");
    }

    @Test
    public void number_comic() throws Exception {
        ComicResult it = ComicResult.of(comic);

        assertThat(it.number()).isEqualTo(ComicNumber.of(123));
    }

    @Test
    public void number_missingComic() throws Exception {
        ComicResult it = ComicResult.of(missingComic);

        assertThat(it.number()).isEqualTo(ComicNumber.of(456));
    }
}