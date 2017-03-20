package features.comic.ui;

import dagger.Component;
import di.ActivityComponent;
import di.ActivityModule;
import di.AppComponent;
import di.PerActivity;

@PerActivity
@Component(
        dependencies = AppComponent.class,
        modules = ActivityModule.class
)
public interface ComicListComponent extends ActivityComponent {

    void inject(ComicListActivity activity);
}
