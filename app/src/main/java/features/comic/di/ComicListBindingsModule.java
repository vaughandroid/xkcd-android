package features.comic.di;

import dagger.Module;
import dagger.Provides;
import di.PerActivity;
import features.comic.ui.ComicListActivity;

@Module
public class ComicListBindingsModule {

    // TODO: This is just a proof of concept and can be removed
    @Provides @PerActivity String activityName(ComicListActivity activity) {
        return activity.getClass().getSimpleName();
    }
}
