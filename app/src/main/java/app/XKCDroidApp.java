package app;

import android.app.Activity;
import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.jakewharton.threetenabp.BuildConfig;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import di.AppModule;
import di.DaggerAppComponent;
import io.reactivex.plugins.RxJavaPlugins;
import rx.RxUtils;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class XKCDroidApp extends Application implements HasActivityInjector {

    @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        }

        AndroidThreeTen.init(this);

        RxJavaPlugins.setErrorHandler(RxUtils.errorHandler());

        DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build()
                .inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }
}
