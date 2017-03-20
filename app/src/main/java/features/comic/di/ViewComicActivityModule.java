package features.comic.di;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import features.comic.ui.ViewComicActivity;

@Module(subcomponents = ViewComicSubcomponent.class)
public abstract class ViewComicActivityModule {

    @Binds @IntoMap @ActivityKey(ViewComicActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bind(ViewComicSubcomponent.Builder builder);
}
