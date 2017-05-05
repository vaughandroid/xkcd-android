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
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.threeten.bp.LocalDate;

import app.XKCDroidApp;
import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.usecases.ComicUseCases.GetComic;
import io.reactivex.Single;
import rx.AndroidSchedulerProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ViewComicActivityTest {

    @Rule public ActivityTestRule<ViewComicActivity> activityTestRule = new ActivityTestRule<>(ViewComicActivity.class, false, false);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock GetComic getComicStub;

    private ViewComicActivityRobot robot;
    private Context targetContext;

    @Before
    public void setUp() {
        targetContext = InstrumentationRegistry.getTargetContext();

        DaggerTestViewComicComponent.builder()
                .module(new TestViewComicComponent.Module(instance -> instance.inject(
                        getComicStub,
                        new AndroidSchedulerProvider())))
                .build()
                .inject((XKCDroidApp) targetContext.getApplicationContext());

        robot = new ViewComicActivityRobot();
    }

    // TODO: Test image is loaded.

    @Test
    public void showAndHideAltText() throws Exception {
        Comic comic =  Comic.builder()
                .number(123)
                .title("Comic title")
                .date(LocalDate.now())
                .imageUri(Uri.EMPTY)
                .altText("Alt text")
                .build();
        when(getComicStub.asSingle(any())).thenReturn(Single.just(comic));
        activityTestRule.launchActivity(ViewComicActivity.intent(ComicNumber.of(123), targetContext));

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