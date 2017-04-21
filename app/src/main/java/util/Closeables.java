package util;

import android.support.annotation.Nullable;

import java.io.Closeable;
import java.io.IOException;

public class Closeables {

    public static void closeQuietly(@Nullable Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            // Intentionally ignored.
        }
    }
}
