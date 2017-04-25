package features.comic.ui;

import android.app.Instrumentation.ActivityResult;
import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import app.XKCDroidApp;
import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.usecases.ComicUseCases.GetNextPageOfComics;
import io.reactivex.Single;
import rx.AndroidSchedulerProvider;

import static android.app.Activity.RESULT_OK;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static org.hamcrest.CoreMatchers.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static testutils.CustomIntentMatchers.forActivityClass;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ComicListActivityTest {

    @Rule public IntentsTestRule<ComicListActivity> activityTestRule = new IntentsTestRule<>(ComicListActivity.class, false, false);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock GetNextPageOfComics getNextPageOfComicsStub;

    private ComicListActivityRobot robot;
    private Context targetContext;

    // TODO: Test paging
    // TODO: Test ImageViews?
    // TODO: How much can/should be moved into the Robot?

    @Before
    public void setUp() {
        targetContext = InstrumentationRegistry.getTargetContext();

        DaggerTestComicListComponent.builder()
                .module(new TestComicListComponent.Module(instance -> instance.inject(
                        getNextPageOfComicsStub,
                        new AndroidSchedulerProvider()
                )))
                .build()
                .inject((XKCDroidApp) targetContext.getApplicationContext());

        robot = new ComicListActivityRobot();
    }

    @Test
    public void itemsAreShownInOrder() throws Exception {
        List<Comic> comics = createComicsList(
                ComicNumber.create(1000),
                LocalDate.parse("2017-01-01"),
                10
        );
        when(getNextPageOfComicsStub.asSingle(any(), anyInt())).thenReturn(Single.just(comics));

        activityTestRule.launchActivity(ComicListActivity.intent(targetContext));

        robot.check().item(0)
                .title("title 1000")
                .number("# 1000")
                .date("2017-01-01");

        robot.check().item(5)
                .title("title 1005")
                .number("# 1005")
                .date("2017-01-06");

        robot.check().item(9)
                .title("title 1009")
                .number("# 1009")
                .date("2017-01-10");
    }

    @Test
    public void clickOnItemShowsComic() throws Exception {
        List<Comic> comics = createComicsList(
                ComicNumber.create(1000),
                LocalDate.parse("2017-01-01"),
                10
        );
        when(getNextPageOfComicsStub.asSingle(any(), anyInt())).thenReturn(Single.just(comics));

        activityTestRule.launchActivity(ComicListActivity.intent(targetContext));

        intending(forActivityClass(ViewComicActivity.class))
                .respondWith(new ActivityResult(RESULT_OK, null));

        robot.perform().clickOnItem(0);

        intended(allOf(
                forActivityClass(ViewComicActivity.class),
                IntentMatchers.hasExtra(ViewComicActivity.EXTRA_COMIC_ID, ComicNumber.create(1000))
        ));

        robot.perform().clickOnItem(9);

        intended(allOf(
                forActivityClass(ViewComicActivity.class),
                IntentMatchers.hasExtra(ViewComicActivity.EXTRA_COMIC_ID, ComicNumber.create(1009))
        ));
    }

    private static List<Comic> createComicsList(ComicNumber comicNumber, LocalDate localDate, int count) {
        List<Comic> comics = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            comics.add(createComic(comicNumber, localDate));
            comicNumber = comicNumber.next();
            localDate = localDate.plusDays(1);
        }
        return comics;
    }

    private static Comic createComic(ComicNumber comicNumber, LocalDate localDate) {
        return Comic.builder()
                .number(comicNumber)
                .title("title " + comicNumber.intVal())
                .altText("alt text " + comicNumber.intVal())
                .date(localDate)
                .imageUri(Uri.parse("file:///android_asset/survivorship_bias.png"))
                .build();
    }
}