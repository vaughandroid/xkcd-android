package features.comic.ui;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
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
import features.comic.domain.usecases.ComicUseCases.GetLatestComicNumber;
import features.comic.domain.usecases.ComicUseCases.GetNextPageOfComics;
import io.reactivex.Single;
import rx.AndroidSchedulerProvider;
import testutils.TestAppRule;

import static features.comic.domain.SortOrder.NEWEST_TO_OLDEST;
import static features.comic.domain.SortOrder.OLDEST_TO_NEWEST;
import static junit.framework.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static testutil.TestModelFactory.comicResult;
import static testutil.TestModelFactory.comicsPage;
import static testutil.TestModelFactory.missingComicResult;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ComicListActivityTest {

    @Rule public TestAppRule testAppRule = new TestAppRule();
    @Rule public IntentsTestRule<ComicListActivity> activityTestRule =
            new IntentsTestRule<>(ComicListActivity.class, false, false);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock GetNextPageOfComics getNextPageOfComicsStub;
    @Mock GetLatestComicNumber getLatestComicNumberStub;

    private ComicListActivityRobot robot;
    private Context targetContext;

    // TODO: Test images are loaded correctly

    @Before
    public void setUp() {
        targetContext = InstrumentationRegistry.getTargetContext();

        TestApp.activityInjector = instance -> ((ComicListActivity) instance).inject(
                getNextPageOfComicsStub,
                getLatestComicNumberStub,
                new AndroidSchedulerProvider()
        );

        robot = new ComicListActivityRobot();
    }

    @Test
    public void itemsAreShown() throws Exception {
        PagedComics comics = comicsPage(1001, "2017-01-01", 10, false);
        when(getNextPageOfComicsStub.asSingle(any(), any())).thenReturn(Single.just(comics));

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
    public void changeSortOrder() throws Exception {
        when(getLatestComicNumberStub.asSingle())
                .thenReturn(Single.just(ComicNumber.of(100)));
        when(getNextPageOfComicsStub.asSingle(ComicNumber.of(1), OLDEST_TO_NEWEST))
                .thenReturn(Single.just(comicsPage(1, "2017-01-01", 10, true)));
        when(getNextPageOfComicsStub.asSingle(ComicNumber.of(100), NEWEST_TO_OLDEST))
                .thenReturn(Single.just(comicsPage(100, "2018-01-01", -10, true)));

        launchActivity();

        robot.check().itemCheck(0).number("# 1");
        robot.check().itemCheck(9).number("# 10");

        robot.perform().sortNewestToOldest();

        robot.check().itemCheck(0).number("# 100");
        robot.check().itemCheck(9).number("# 91");

        robot.perform().sortOldestToNewest();

        robot.check().itemCheck(0).number("# 1");
        robot.check().itemCheck(9).number("# 10");
    }

    @Test
    public void paging() throws Exception {
        //noinspection unchecked
        when(getNextPageOfComicsStub.asSingle(any(), any())).thenAnswer(
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
        when(getNextPageOfComicsStub.asSingle(any(), any())).thenReturn(Single.just(comics));

        launchActivity();

        robot.setup().willOpenViewComicActivity();

        robot.perform().item(7).open();

        robot.check().openedViewComicActivity(1007);
    }

    @Test
    public void clickOnMissingComicLaunchesBrowser() throws Exception {
        List<ComicResult> items = Arrays.asList(
                comicResult(123),
                missingComicResult(124),
                comicResult(125)
        );
        PagedComics pagedComics = PagedComics.of(items);
        when(getNextPageOfComicsStub.asSingle(any(), any())).thenReturn(Single.just(pagedComics));

        launchActivity();

        robot.setup().willOpenBrowser();

        robot.perform().item(1).open();

        robot.check().openedBrowser("https://xkcd.com/124/");
    }

    private void launchActivity() {
        activityTestRule.launchActivity(ComicListActivity.intent(targetContext));
    }

}