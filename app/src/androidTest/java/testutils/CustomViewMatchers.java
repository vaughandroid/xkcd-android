package testutils;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import org.hamcrest.Matcher;

import me.vaughandroid.xkcdreader.R;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.CoreMatchers.not;

public final class CustomViewMatchers {

    private CustomViewMatchers() {
        throw new AssertionError("No instances.");
    }

    @NonNull
    public static Matcher<View> recyclerViewItem(@IdRes int recyclerViewId, int idx) {
        return new RecyclerViewMatcher(recyclerViewId).atPosition(idx);
    }

}
