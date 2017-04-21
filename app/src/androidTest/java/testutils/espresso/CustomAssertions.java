package testutils.espresso;

import android.support.annotation.NonNull;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.ViewMatchers;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.CoreMatchers.not;

public class CustomAssertions {

    private CustomAssertions() {
        throw new AssertionError("No instances.");
    }

    @NonNull
    public static ViewAssertion isDisplayed(boolean isShown) {
        return matches(isShown ? ViewMatchers.isDisplayed() : not(ViewMatchers.isDisplayed()));
    }
}
