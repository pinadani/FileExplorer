package pinadani.filemanager.interactor;

import java.io.File;

/**
 * Interactor that stores data to shared preferences
 * Created by Daniel.Pina on 1.7.2017.
 **/
public interface ISPInteractor {

    File getHomeFolder();

    void saveHomeFolder(String homeFolder);


}
