package features.comic.ui;

import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.threeten.bp.LocalDate;

import app.XKCDroidApp;
import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.usecases.ComicUseCases;
import rx.AndroidSchedulerProvider;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ViewComicActivityTest {

    @Rule public ActivityTestRule<ViewComicActivity> activityTestRule = new ActivityTestRule<>(ViewComicActivity.class, false, false);

    private ViewComicActivityRobot robot;
    private Context targetContext;

    @Before
    public void setUp() {
        targetContext = InstrumentationRegistry.getTargetContext();

        ComicUseCases.GetComic getComicMock = Mockito.mock(ComicUseCases.GetComic.class);
        DaggerTestViewComicComponent.builder()
                .module(
                        new TestViewComicComponent.Module(instance ->
                                instance.inject(getComicMock, new AndroidSchedulerProvider())
                        )
                )
                .build()
                .inject((XKCDroidApp) targetContext.getApplicationContext());

        robot = new ViewComicActivityRobot(getComicMock);
    }

    @Test
    public void showAndHideAltText() throws Exception {
        robot.setup().comic(
                Comic.builder()
                        .number(123)
                        .title("Comic title")
                        .date(LocalDate.now())
                        .imageUri(Uri.EMPTY)
                        .altText("Alt text")
                        .build()
        );
        activityTestRule.launchActivity(ViewComicActivity.intent(ComicNumber.create(123), targetContext));

        robot.check()
                .fabIsShown(true)
                .altTextIsShown(false);

        robot.perform().pressFab();

        robot.check()
                .altText("Alt text")
                .altTextIsShown(true)
                .fabIsShown(false);

        robot.perform().pressAltText();

        robot.check()
                .fabIsShown(true)
                .altTextIsShown(false);
    }

}