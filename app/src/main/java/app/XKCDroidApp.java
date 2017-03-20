package app;

import android.app.Activity;
import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.jakewharton.threetenabp.BuildConfig;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasDispatchingActivityInjector;
import di.AppModule;
import di.DaggerAppComponent;
import retrofit2.Retrofit;
import rx.SchedulerProvider;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class XKCDroidApp extends Application implements HasDispatchingActivityInjector {

    @Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject Retrofit retrofit;
    @Inject SchedulerProvider schedulerProvider;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        }

        AndroidThreeTen.init(this);

        DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build()
                .inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
