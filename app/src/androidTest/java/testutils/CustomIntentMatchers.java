package testutils;


import android.content.Intent;
import android.support.test.espresso.intent.matcher.IntentMatchers;

import org.hamcrest.Matcher;

public final class CustomIntentMatchers {

    private CustomIntentMatchers() {
        throw new AssertionError("No instances.");
    }

    public static Matcher<Intent> forActivityClass(Class clazz) {
        return IntentMatchers.hasComponent(clazz.getName());
    }
}
