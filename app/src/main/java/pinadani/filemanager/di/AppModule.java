package pinadani.filemanager.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pinadani.filemanager.App;

/**
 * Module that handle injecting application class (ie Context)
 * Created by Daniel.Pina on 1.7.2017.
 */
@Module(
        includes = {InteractorsModule.class,}
)
public class AppModule {
    public static final String TAG = AppModule.class.getName();
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return app;
    }
}
