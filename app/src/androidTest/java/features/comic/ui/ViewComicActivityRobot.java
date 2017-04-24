package features.comic.ui;

import me.vaughandroid.xkcdreader.R;
import testutils.CustomViewAssertions;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ViewComicActivityRobot {

    public Perform perform() {
        return new Perform();
    }

    public Check check() {
        return new Check();
    }

    public class Perform {

        public Perform pressFab() {
            onView(withId(R.id.fab))
                    .perform(click());
            return this;
        }

        public Perform pressAltText() {
            onView(withId(R.id.comic_alt_text))
                    .perform(click());
            return this;
        }
    }

    public class Check {

        public Check fabIsShown(boolean isShown) {
            onView(withId(R.id.fab))
                    .check(CustomViewAssertions.isDisplayed(isShown));
            return this;
        }

        public Check altText(String text) {
            onView(withId(R.id.comic_alt_text))
                    .check(matches(withText(text)));
            return this;
        }

        public Check altTextIsShown(boolean isShown) {
            onView(withId(R.id.comic_alt_text))
                    .check(CustomViewAssertions.isDisplayed(isShown));
            return this;
        }
    }

}
