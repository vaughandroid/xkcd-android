package features.comic.ui;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;

import javax.inject.Inject;

import me.vaughandroid.xkcdreader.R;
import testutils.RecyclerViewMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

public class ComicListActivityRobot {

    @Inject public ComicListActivityRobot() {
    }

    public Check check() {
        return new Check();
    }

    public class Check {
        public ItemCheck item(int idx) {
            return new ItemCheck(idx);
        }

        public class ItemCheck {

            private final int idx;

            public ItemCheck(int idx) {
                this.idx = idx;
            }

            public ItemCheck title(String text) {
                getViewInteraction()
                        .check(matches(hasDescendant(allOf(
                                withId(R.id.comic_title),
                                withText(text)
                        ))));
                return this;
            }

            public ItemCheck number(String text) {
                getViewInteraction()
                        .check(matches(hasDescendant(allOf(
                                withId(R.id.comic_number),
                                withText(text)
                        ))));
                return this;
            }

            public ItemCheck date(String text) {
                getViewInteraction()
                        .check(matches(hasDescendant(allOf(
                                        withId(R.id.comic_date),
                                        withText(text)
                        ))));
                return this;
            }

            private ViewInteraction getViewInteraction() {
                onView(withId(R.id.comic_list_recyclerview))
                        .perform(RecyclerViewActions.scrollToPosition(idx));
                return onView(new RecyclerViewMatcher(R.id.comic_list_recyclerview).atPosition(idx));
            }

        }
    }

}
