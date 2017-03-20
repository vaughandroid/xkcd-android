package features.comic.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import di.PerActivity;
import features.comic.ui.ViewComicActivity;

@PerActivity
@Subcomponent
public interface ViewComicSubcomponent extends AndroidInjector<ViewComicActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<ViewComicActivity> {}
}
