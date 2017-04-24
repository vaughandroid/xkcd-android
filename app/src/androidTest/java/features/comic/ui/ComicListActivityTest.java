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

import java.util.ArrayList;
import java.util.List;

import app.XKCDroidApp;
import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.usecases.ComicUseCases.GetNextPageOfComics;
import io.reactivex.Single;
import rx.AndroidSchedulerProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ComicListActivityTest {

    @Rule public ActivityTestRule<ComicListActivity> activityTestRule = new ActivityTestRule<>(ComicListActivity.class, false, false);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock GetNextPageOfComics getNextPageOfComicsStub;

    private ComicListActivityRobot robot;
    private Context targetContext;

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
    public void resultsAreShownInOrder() throws Exception {
        List<Comic> comics = createComicsList(
                ComicNumber.create(1234),
                LocalDate.parse("2017-04-24"),
                10
        );
        when(getNextPageOfComicsStub.asSingle(any(), anyInt())).thenReturn(Single.just(comics));

        activityTestRule.launchActivity(ComicListActivity.intent(targetContext));

        robot.check().item(0)
                .title("title 1234")
                .number("# 1234")
                .date("2017-04-24");

        robot.check().item(5)
                .title("title 1239")
                .number("# 1239")
                .date("2017-04-19");

        robot.check().item(9)
                .title("title 1243")
                .number("# 1243")
                .date("2017-04-15");
    }

    // TODO: Test clicking on views -> ViewComicActivity
    // TODO: Test paging
    // TODO: Test imageviews?

    private static List<Comic> createComicsList(ComicNumber comicNumber, LocalDate localDate, int count) {
        List<Comic> comics = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            comics.add(createComic(comicNumber, localDate));
            comicNumber = comicNumber.next();
            localDate = localDate.minusDays(1);
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