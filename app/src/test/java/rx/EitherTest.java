package rx;

import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;

public class EitherTest {

    @Rule public JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Test
    public void continued_left() throws Exception {
        Object value = new Object();
        Either<Object, Object> either = Either.left(value);

        either.continued(left -> assertThat(left).isSameAs(value), right -> fail());
    }

    @Test
    public void continued_right() throws Exception {
        Object value = new Object();
        Either<Object, Object> either = Either.right(value);

        either.continued(left -> fail(), right -> assertThat(right).isSameAs(value));
    }

    @Test
    public void join_left() throws Exception {
        Either<Object, Object> either = Either.left(new Object());

        assertThat(either.<Object>join(left -> "left", right -> "right")).isEqualTo("left");
    }

    @Test
    public void join_right() throws Exception {
        Either<Object, Object> either = Either.right(new Object());

        assertThat(either.<Object>join(left -> "left", right -> "right")).isEqualTo("right");
    }
}