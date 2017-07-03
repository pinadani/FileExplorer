package pinadani.filemanager.di;

import javax.inject.Singleton;

import dagger.Component;
import pinadani.filemanager.mvp.presenter.BrowserPresenter;
import pinadani.filemanager.ui.fragment.PrefsFragment;


/**
 * Main application component
 * Created by Daniel.Pina on 1.7.2017.
 **/
@Singleton
@Component(modules = {
        AppModule.class})
public interface AppComponent {

    void inject(BrowserPresenter browserPresenter);

    void inject(PrefsFragment prefsFragment);

}
