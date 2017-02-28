package util;

public final class Assertions {

    private Assertions() {
        throw new AssertionError("No instances.");
    }

    public static void isNull(Object value, String message, Object... messageArgs) {
        if (value != null) {
            throw new AssertionError(String.format(message, messageArgs));
        }
    }

    public static void notNull(Object value, String message, Object... messageArgs) {
        if (value == null) {
            throw new AssertionError(String.format(message, messageArgs));
        }
    }
}
