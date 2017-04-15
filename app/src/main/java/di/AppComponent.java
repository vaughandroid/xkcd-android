package di;

import javax.inject.Singleton;

import app.XKCDroidApp;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import features.comic.di.ComicListActivityModule;
import features.comic.di.ComicUseCasesModule;
import features.comic.di.ViewComicActivityModule;

@Singleton
@Component(
        modules = {
                AppModule.class,
                AndroidInjectionModule.class,
                ComicUseCasesModule.class,
                ComicListActivityModule.class,
                ViewComicActivityModule.class
        }
)
public interface AppComponent {

    void inject(XKCDroidApp app);

}
