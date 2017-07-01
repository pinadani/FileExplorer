package pinadani.filemanager.di;

import javax.inject.Singleton;

import dagger.Component;
import pinadani.filemanager.mvp.presenter.BrowserPresenter;
import pinadani.filemanager.mvp.presenter.MainPresenter;


/**
 * Main application component
 * Created by Daniel.Pina on 1.7.2017.
 **/
@Singleton
@Component(modules = {
        AppModule.class})
public interface AppComponent {

    void inject(MainPresenter mainPresenter);

    void inject(BrowserPresenter browserPresenter);

}
