package testutils;

import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * With credit to <a href="https://github.com/dannyroa/espresso-samples">Danny Roa</a>
 */
// TODO: Make this easier to use - e.g. helpers for validating specific child/descendant views?
public class RecyclerViewMatcher {

    private final int recyclerViewId;
    public RecyclerViewMatcher(int recyclerViewId) {
        this.recyclerViewId = recyclerViewId;
    }

    public Matcher<View> atPosition(final int position) {
        return new TypeSafeMatcher<View>() {
            boolean cachedChildView;
            View childView;

            public void describeTo(Description description) {
                description.appendText("with id: " + getResourceName());
            }

            private String getResourceName() {
                Resources resources = InstrumentationRegistry.getTargetContext().getResources();
                String name;
                try {
                    name = resources.getResourceName(recyclerViewId);
                } catch (Resources.NotFoundException e) {
                    name = String.format("%s (resource name not found)", recyclerViewId);
                }
                return name;
            }

            public boolean matchesSafely(View view) {
                return view == getChildView(view);
            }

            private View getChildView(View view) {
                if (!cachedChildView) {
                    RecyclerView recyclerView = (RecyclerView) view.getRootView().findViewById(recyclerViewId);
                    if (recyclerView != null) {
                        childView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
                    }
                    cachedChildView = true;
                }
                return childView;
            }
        };
    }

}