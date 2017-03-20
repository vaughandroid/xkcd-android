package features.comic.di;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import features.comic.ui.ComicListActivity;

@Module(subcomponents = ComicListSubcomponent.class)
public abstract class ComicListActivityModule {

    @Binds @IntoMap @ActivityKey(ComicListActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bind(ComicListSubcomponent.Builder builder);
}
