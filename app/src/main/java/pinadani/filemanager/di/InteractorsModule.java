package pinadani.filemanager.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pinadani.filemanager.interactor.ISPInteractor;
import pinadani.filemanager.interactor.SPInteractorImpl;

/**
 * Module that handles injecting Interactors
 * Created by Daniel.Pina on 1.7.2017.
 */
@Module()
public class InteractorsModule {
    public static final String TAG = InteractorsModule.class.getName();

    @Provides
    @Singleton
    public ISPInteractor provideSPInteractor(Application app) {
        return new SPInteractorImpl(app.getApplicationContext());
    }
}
