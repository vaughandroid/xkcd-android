package features.comic.domain.models;

import org.junit.Test;

import java.util.List;

import static features.comic.domain.models.ComicNumber.of;
import static org.assertj.core.api.Assertions.assertThat;

public class ComicNumberTest {

    @Test
    public void intValue_MatchesConstructorValue() {
        ComicNumber number = of(123);

        assertThat(number.intVal()).isEqualTo(123);
    }

    @Test
    public void next() throws Exception {
        assertThat(of(123).next()).isEqualTo(of(124));
    }

    @Test
    public void previous() throws Exception {
        assertThat(of(100).previous()).isEqualTo(of(99));
    }

    @Test
    public void compareTo_Equal() throws Exception {
        assertThat(of(1).compareTo(of(1))).isEqualTo(0);
    }

    @Test
    public void compareTo_Higher() throws Exception {
        assertThat(of(78).compareTo(of(77))).isGreaterThan(0);
    }

    @Test
    public void compareTo_Lower() throws Exception {
        assertThat(of(13).compareTo(of(14))).isLessThan(0);
    }
}