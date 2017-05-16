package features.comic.domain.models;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import me.vaughandroid.xkcdreader.BuildConfig;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MissingComicTest {

    @Test
    public void uri() throws Exception {
        MissingComic missingComic = MissingComic.of(ComicNumber.of(987));
        Uri expected = Uri.parse("https://xkcd.com/987/");

        assertThat(missingComic.uri()).isEqualTo(expected);
    }
}