package di;

import javax.inject.Singleton;

import app.XKCDroidApp;
import dagger.Module;
import dagger.Provides;
import features.comic.data.ComicRepository;
import features.comic.data.ComicRepositoryImpl;
import features.comic.data.ComicService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.AndroidSchedulerProvider;
import rx.SchedulerProvider;

@Module
public class AppModule {

    private final XKCDroidApp app;

    public AppModule(XKCDroidApp app) {
        this.app = app;
    }

    @Provides @Singleton XKCDroidApp app() {
        return app;
    }

    @Provides @Singleton Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides @Singleton SchedulerProvider schedulerProvider() {
        return new AndroidSchedulerProvider();
    }

    @Provides @Singleton ComicService comicService(Retrofit retrofit) {
        return retrofit.create(ComicService.class);
    }

    @Provides @Singleton ComicRepository comicRepository(ComicRepositoryImpl implementation) {
        return implementation;
    }
}
