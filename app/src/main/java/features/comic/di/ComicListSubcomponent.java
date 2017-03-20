package features.comic.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import di.PerActivity;
import features.comic.ui.ComicListActivity;

@PerActivity
@Subcomponent(modules = ComicListBindingsModule.class)
public interface ComicListSubcomponent extends AndroidInjector<ComicListActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<ComicListActivity> {}
}
