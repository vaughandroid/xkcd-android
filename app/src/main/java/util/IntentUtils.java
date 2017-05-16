package util;

import android.content.Intent;
import android.net.Uri;

public class IntentUtils {

    private IntentUtils() {
        throw new AssertionError("No instances.");
    }

    public static Intent browserIntent(Uri uri) {
        return new Intent(Intent.ACTION_VIEW, uri);
    }
}
