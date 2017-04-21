package features.comic.ui;

import javax.inject.Inject;

import features.comic.domain.models.Comic;
import features.comic.domain.usecases.ComicUseCases;
import io.reactivex.Single;
import me.vaughandroid.xkcdreader.R;
import testutils.espresso.CustomAssertions;
import testutils.espresso.CustomMatchers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ViewComicActivityRobot {

    private final ComicUseCases.GetComic getComicMock;

    @Inject public ViewComicActivityRobot(ComicUseCases.GetComic getComicMock) {
        this.getComicMock = getComicMock;
    }

    public Setup setup() {
        return new Setup();
    }

    public Perform perform() {
        return new Perform();
    }

    public Check check() {
        return new Check();
    }

    public class Setup {

        public Setup comic(Comic comic) {
            when(getComicMock.asSingle(any())).thenReturn(Single.just(comic));
            return this;
        }

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
                    .check(CustomAssertions.isDisplayed(isShown));
            return this;
        }

        public Check altText(String text) {
            onView(withId(R.id.comic_alt_text))
                    .check(matches(withText(text)));
            return this;
        }

        public Check altTextIsShown(boolean isShown) {
            onView(withId(R.id.comic_alt_text))
                    .check(CustomAssertions.isDisplayed(isShown));
            return this;
        }

        public Check imageFile(String filePath) {
            onView(withId(R.id.comic_photoview))
                    .check(matches(CustomMatchers.imageViewWithBitmap(filePath)));
            return this;
        }
    }

}
