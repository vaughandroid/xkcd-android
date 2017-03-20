package di;

import javax.inject.Singleton;

import app.XKCDroidApp;
import dagger.Component;
import features.comic.data.ComicRepository;
import rx.SchedulerProvider;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(XKCDroidApp app);

    // Exports for sub-graphs.
    XKCDroidApp app();
    ComicRepository comicRepository();
    SchedulerProvider schedulerProvider();

    // Sub-graphs.
    ActivityComponent plus(ActivityModule activityModule);
}
