package di;

import dagger.Subcomponent;
import features.comic.ui.ComicListActivity;
import features.comic.ui.ViewComicActivity;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(ComicListActivity activity);
    void inject(ViewComicActivity activity);
}
