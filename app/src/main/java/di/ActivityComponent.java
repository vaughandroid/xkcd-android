package di;

import android.app.Activity;

import dagger.Component;

@PerActivity
@Component(
        dependencies = AppComponent.class,
        modules = ActivityModule.class
)
public interface ActivityComponent {

    // Exports for sub-graphs.
    Activity activity();
}
