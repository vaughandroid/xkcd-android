package features.comic.ui;

import android.app.Activity;

import javax.inject.Singleton;

import app.XKCDroidApp;
import dagger.Component;
import dagger.Provides;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import util.annotations.AwaitingLibaryRelease;

@AwaitingLibaryRelease(
        library = "com.google.dagger:dagger:2.11",
        comment = "Can be removed, see: https://github.com/google/dagger/issues/648"
)
@Singleton
@Component(modules = { TestViewComicComponent.Module.class, AndroidInjectionModule.class })
public interface TestViewComicComponent {

    void inject(XKCDroidApp myApp);

    @dagger.Module
    class Module {

        private final AndroidInjector<ViewComicActivity> injector;

        public Module(AndroidInjector<ViewComicActivity> injector) {
            this.injector = injector;
        }

        @Provides @IntoMap @ActivityKey(ViewComicActivity.class)
        AndroidInjector.Factory<? extends Activity> factory() {
            return new AndroidInjector.Factory<ViewComicActivity>() {
                @Override
                public AndroidInjector<ViewComicActivity> create(ViewComicActivity instance) {
                    return injector;
                }
            };
        }
    }
}
