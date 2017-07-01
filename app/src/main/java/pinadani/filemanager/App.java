package pinadani.filemanager;

import android.app.Application;
import android.support.annotation.NonNull;

import com.facebook.stetho.Stetho;

import pinadani.filemanager.di.DaggerAppComponent;
import pinadani.filemanager.di.AppComponent;
import pinadani.filemanager.di.AppModule;


/**
 * Application class for this application
 * Created by Daniel.Pina on 1.7.2017.
 */
public class App extends Application {

    public static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    private static AppComponent mAppComponent;

    private static App INSTANCE;

    public static App getInstance() {
        return INSTANCE;
    }

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static void setAppComponent(@NonNull AppComponent appComponent) {
        mAppComponent = appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        Stetho.initializeWithDefaults(this);

    }
}
