package features.comic.ui;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;

import javax.inject.Inject;

import me.vaughandroid.xkcdreader.R;
import testutils.CustomViewAssertions;
import testutils.CustomViewMatchers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static testutils.CustomViewMatchers.recyclerViewItem;

public class ComicListActivityRobot {

    @Inject public ComicListActivityRobot() {
    }

    public Check check() {
        return new Check();
    }

    public Perform perform() {
        return new Perform();
    }

    private ViewInteraction scrollToAndGetItemInteraction(int idx) {
        onView(withId(R.id.comic_list_recyclerview))
                .perform(RecyclerViewActions.scrollToPosition(idx));
        return onView(recyclerViewItem(R.id.comic_list_recyclerview, idx));
    }

    public class Check {

        public Check itemCount(int count) {
            onView(withId(R.id.comic_list_recyclerview))
                    .check(matches(CustomViewMatchers.recyclerViewWithItemCount(count)));
            return this;
        }

        public ItemCheck item(int idx) {
            return new ItemCheck(idx);
        }

        public class ItemCheck {

            private final int idx;

            public ItemCheck(int idx) {
                this.idx = idx;
            }

            public ItemCheck title(String text) {
                checkTextView(text, R.id.comic_title);
                return this;
            }

            public ItemCheck number(String text) {
                checkTextView(text, R.id.comic_number);
                return this;
            }

            public ItemCheck date(String text) {
                checkTextView(text, R.id.comic_date);
                return this;
            }

            private void checkTextView(String text, int textViewId) {
                scrollToAndGetItemInteraction(idx)
                        .check(matches(hasDescendant(allOf(withId(textViewId), withText(text)))));
            }

        }
    }

    public class Perform {

        public ItemPerform item(int idx) {
            return new ItemPerform(idx);
        }

        public class ItemPerform {

            private final int idx;

            ItemPerform(int idx) {
                this.idx = idx;
            }

            public ItemPerform click() {
                scrollToAndGetItemInteraction(idx)
                        .perform(ViewActions.click());
                return this;
            }

            public ItemPerform scrollTo() {
                scrollToAndGetItemInteraction(idx);
                return this;
            }
        }
    }

}
