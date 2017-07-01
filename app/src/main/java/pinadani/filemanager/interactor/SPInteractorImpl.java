package pinadani.filemanager.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

import pinadani.filemanager.Constants;

/**
 * Implementation of shared preferences interactor
 * Created by Daniel.Pina on 1.7.2017.
 **/
public class SPInteractorImpl implements ISPInteractor {
    public static final String TAG = SPInteractorImpl.class.getName();
    private static final String SP_NAME = "sp_name";
    private static final String SP_HOME_FOLDER = "sp_home_folder";

    SharedPreferences mSharedPreferences;
    Context mCtx;

    public SPInteractorImpl(Context ctx) {
        mCtx = ctx;
        mSharedPreferences = mCtx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public File getHomeFolder() {
        String homeFolder = mSharedPreferences.getString(SP_HOME_FOLDER, null);

        if (homeFolder == null) {
            if (Environment.getExternalStorageDirectory().list() != null)
                return Environment.getExternalStorageDirectory();
            else
                return new File(Constants.DEFAULT_HOME_FOLDER);
        } else {
            return new File(homeFolder);
        }
    }

    @Override
    public void saveHomeFolder(String homeFolder) {
        mSharedPreferences.edit().putString(SP_HOME_FOLDER, homeFolder).apply();
    }
}
