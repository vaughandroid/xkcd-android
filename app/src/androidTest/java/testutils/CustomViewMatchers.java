package testutils;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

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

    @NonNull
    public static Matcher<View> recyclerViewWithItemCount(int count) {
        return new TypeSafeMatcher<View>(RecyclerView.class) {
            @Override
            protected boolean matchesSafely(View item) {
                RecyclerView recyclerView = (RecyclerView) item;
                return recyclerView.getAdapter().getItemCount() == count;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(RecyclerView.class.getSimpleName() + " with item count: " + count);
            }
        };
    }

}
