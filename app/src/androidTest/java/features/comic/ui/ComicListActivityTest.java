package features.comic.ui;

import android.app.Instrumentation.ActivityResult;
import android.content.Context;
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

import app.XKCDroidApp;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.models.PagedComics;
import features.comic.domain.usecases.ComicUseCases.GetNextPageOfComics;
import io.reactivex.Single;
import rx.AndroidSchedulerProvider;
import testutil.TestModelFactory;

import static android.app.Activity.RESULT_OK;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static org.hamcrest.CoreMatchers.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static testutil.TestModelFactory.comicsPage;
import static testutils.CustomIntentMatchers.forActivityClass;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ComicListActivityTest {

    @Rule public IntentsTestRule<ComicListActivity> activityTestRule = new IntentsTestRule<>(ComicListActivity.class, false, false);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock GetNextPageOfComics getNextPageOfComicsStub;

    private ComicListActivityRobot robot;
    private Context targetContext;

    // TODO: Test images are loaded correctly
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
        PagedComics comics = comicsPage(1001, "2017-01-01", 10, false);
        when(getNextPageOfComicsStub.asSingle(any(), anyInt())).thenReturn(Single.just(comics));

        activityTestRule.launchActivity(ComicListActivity.intent(targetContext));

        robot.check().item(0)
                .title("title 1001")
                .number("# 1001")
                .date("2017-01-01");

        robot.check().item(5)
                .title("title 1006")
                .number("# 1006")
                .date("2017-01-06");

        robot.check().item(9)
                .title("title 1010")
                .number("# 1010")
                .date("2017-01-10");
    }

    @Test
    public void paging() throws Exception {
        PagedComics pageOne = comicsPage(1000, "2017-01-01", 10, true);
        PagedComics pageTwo = comicsPage(1010, "2017-01-02", 10, false);
        // TODO: Should match comic numbers explicitly
        //noinspection unchecked
        when(getNextPageOfComicsStub.asSingle(any(), anyInt())).thenReturn(
                Single.just(pageOne),
                Single.just(pageTwo)
        );

        activityTestRule.launchActivity(ComicListActivity.intent(targetContext));

        robot.check()
                .itemCount(11)
                .item(0)
                        .title("title 1000")
                        .number("# 1000")
                        .date("2017-01-01");

        robot.perform().item(10).scrollTo();

        robot.check()
                .itemCount(20)
                .item(10)
                        .title("title 1010")
                        .number("# 1010")
                        .date("2017-01-02");
    }

    @Test
    public void clickOnItemShowsComic() throws Exception {
        PagedComics comics = comicsPage(1000, "2017-01-01", 10, false);
        when(getNextPageOfComicsStub.asSingle(any(), anyInt())).thenReturn(Single.just(comics));

        activityTestRule.launchActivity(ComicListActivity.intent(targetContext));

        intending(forActivityClass(ViewComicActivity.class))
                .respondWith(new ActivityResult(RESULT_OK, null));

        robot.perform().item(0).click();

        intended(allOf(
                forActivityClass(ViewComicActivity.class),
                IntentMatchers.hasExtra(ViewComicActivity.EXTRA_COMIC_ID, ComicNumber.of(1000))
        ));

        robot.perform().item(9).click();

        intended(allOf(
                forActivityClass(ViewComicActivity.class),
                IntentMatchers.hasExtra(ViewComicActivity.EXTRA_COMIC_ID, ComicNumber.of(1009))
        ));
    }

}