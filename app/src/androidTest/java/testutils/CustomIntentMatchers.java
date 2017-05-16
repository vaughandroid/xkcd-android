package testutils;


import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.intent.matcher.IntentMatchers;

import org.hamcrest.Matcher;

import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static org.hamcrest.CoreMatchers.allOf;

public final class CustomIntentMatchers {

    private CustomIntentMatchers() {
        throw new AssertionError("No instances.");
    }

    public static Matcher<Intent> forActivityClass(Class clazz) {
        return IntentMatchers.hasComponent(clazz.getName());
    }

    public static Matcher<Intent> forBrowser(String uri) {
        return allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(uri))
        );
    }
}
