package app;

import android.app.Activity;

import dagger.android.AndroidInjector;


public class TestApp extends XKCDroidApp {

    public static AndroidInjector<Activity> activityInjector;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector != null ? activityInjector : super.activityInjector();
    }
}
