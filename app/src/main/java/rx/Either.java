package rx;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class Either<Left, Right> {

    public static <T, U> Either<T, U> left(T left) {
        return new Either<>(left, null);
    }

    public static <T, U> Either<T, U> right(U right) {
        return new Either<>(null, right);
    }

    private final Left left;
    private final Right right;

    protected Either(Left left, Right right) {
        this.left = left;
        this.right = right;
    }

    public void continued(Consumer<Left> continuationLeft, Consumer<Right> continuationRight) {
        try {
            if (left != null) {
                continuationLeft.accept(left);
            } else {
                continuationRight.accept(right);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T join(Function<Left, T> joinLeft, Function<Right, T> joinRight) {
        T result;
        try {
            if (left != null) {
                result = joinLeft.apply(left);
            } else {
                result = joinRight.apply(right);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
