package features.comic.ui;

import android.app.Instrumentation.ActivityResult;
import android.content.Context;
import android.content.Intent;
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

import java.util.Arrays;
import java.util.List;

import app.TestApp;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.models.ComicResult;
import features.comic.domain.models.PagedComics;
import features.comic.domain.usecases.ComicUseCases.GetNextPageOfComics;
import io.reactivex.Single;
import rx.AndroidSchedulerProvider;
import testutils.TestAppRule;

import static android.app.Activity.RESULT_OK;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static testutil.TestModelFactory.comicResult;
import static testutil.TestModelFactory.comicsPage;
import static testutil.TestModelFactory.missingComicResult;
import static testutils.CustomIntentMatchers.forActivityClass;
import static testutils.CustomIntentMatchers.forBrowser;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ComicListActivityTest {

    @Rule public TestAppRule testAppRule = new TestAppRule();
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

        TestApp.activityInjector = instance -> ((ComicListActivity) instance).inject(
                getNextPageOfComicsStub,
                new AndroidSchedulerProvider()
        );

        robot = new ComicListActivityRobot();
    }

    @Test
    public void itemsAreShownInOrder() throws Exception {
        PagedComics comics = comicsPage(1001, "2017-01-01", 10, false);
        when(getNextPageOfComicsStub.asSingle(any(), anyInt())).thenReturn(Single.just(comics));

        launchActivity();

        robot.check().itemCheck(0)
                .title("title 1001")
                .number("# 1001")
                .date("2017-01-01");

        robot.check().itemCheck(5)
                .title("title 1006")
                .number("# 1006")
                .date("2017-01-06");

        robot.check().itemCheck(9)
                .title("title 1010")
                .number("# 1010")
                .date("2017-01-10");
    }

    @Test
    public void paging() throws Exception {
        //noinspection unchecked
        when(getNextPageOfComicsStub.asSingle(any(), anyInt())).thenAnswer(
                invocation -> {
                    ComicNumber comicNumber = invocation.getArgument(0);
                    switch (comicNumber.intVal()) {
                        case 1:
                            return Single.just(comicsPage(1, "2017-01-01", 10, true));
                        case 11:
                            return Single.just(comicsPage(11, "2017-01-11", 10, false));
                        default:
                            fail("Unexpected comic number requested: " + comicNumber);
                    }
                    return null;
                }
        );

        launchActivity();

        robot.check()
                .itemCount(11)
                .itemCheck(0)
                        .title("title 1")
                        .number("# 1")
                        .date("2017-01-01");

        robot.perform().item(10).scrollTo();

        robot.check()
                .itemCount(20)
                .itemCheck(10)
                        .title("title 11")
                        .number("# 11")
                        .date("2017-01-11");
    }

    @Test
    public void clickOnComicItemShowsComic() throws Exception {
        PagedComics comics = comicsPage(1000, "2017-01-01", 10, false);
        when(getNextPageOfComicsStub.asSingle(any(), anyInt())).thenReturn(Single.just(comics));

        launchActivity();

        intending(forActivityClass(ViewComicActivity.class))
                .respondWith(new ActivityResult(RESULT_OK, null));

        robot.perform().item(7).click();

        intended(allOf(
                forActivityClass(ViewComicActivity.class),
                IntentMatchers.hasExtra(ViewComicActivity.EXTRA_COMIC_ID, ComicNumber.of(1007))
        ));
    }

    @Test
    public void clickOnMissingComicLaunchesBrowser() throws Exception {
        List<ComicResult> items = Arrays.asList(
                comicResult(123),
                missingComicResult(124),
                comicResult(125)
        );
        PagedComics pagedComics = PagedComics.of(items);
        when(getNextPageOfComicsStub.asSingle(any(), anyInt())).thenReturn(Single.just(pagedComics));

        launchActivity();

        intending(hasAction(Intent.ACTION_VIEW))
                .respondWith(new ActivityResult(RESULT_OK, null));

        robot.perform().item(1).click();

        intended(forBrowser("https://xkcd.com/124/"));
    }

    private void launchActivity() {
        activityTestRule.launchActivity(ComicListActivity.intent(targetContext));
    }

}