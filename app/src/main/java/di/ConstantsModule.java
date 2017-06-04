package di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ConstantsModule {

    @Provides @Singleton @Named("comics_page_size")
    public int pageSize() {
        return 20;
    }
}
