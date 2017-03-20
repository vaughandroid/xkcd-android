package app;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import javax.inject.Inject;

import di.AppComponent;
import di.AppModule;
import di.DaggerAppComponent;
import retrofit2.Retrofit;
import rx.SchedulerProvider;
import util.Assertions;

public class XKCDroidApp extends Application {

    private static XKCDroidApp instance;

    public static AppComponent appComponent() {
        Assertions.notNull(instance, "Instance not set!");
        return instance.appComponent;
    }

    private AppComponent appComponent;

    @Inject Retrofit retrofit;
    @Inject SchedulerProvider schedulerProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        AndroidThreeTen.init(this);

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }
}
