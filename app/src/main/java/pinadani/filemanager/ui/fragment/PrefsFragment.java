package pinadani.filemanager.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import javax.inject.Inject;

import pinadani.filemanager.App;
import pinadani.filemanager.R;
import pinadani.filemanager.interactor.ISPInteractor;
import pinadani.filemanager.ui.activity.base.BaseFragmentActivity;
import pinadani.filemanager.ui.fragment.base.IBaseFragment;

/**
 * Settings fragment to set default folder
 * Created by Daniel.Pina on 1.7.2017.
 */
public class PrefsFragment extends PreferenceFragmentCompat implements IBaseFragment, Preference.OnPreferenceClickListener {
    public static final String CURRENT_FOLDER = "current_folder";

    @Inject
    ISPInteractor mSPInteractor;

    private AlertDialog mAlertDialog;
    private Preference mDefaultFolderPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        mDefaultFolderPreference = findPreference("default_folder_key");
        mDefaultFolderPreference.setOnPreferenceClickListener(this);
        mDefaultFolderPreference.setSummary(mSPInteractor.getHomeFolder().getAbsolutePath());

        initSetDefaultFolderDialog(getArguments().getString(CURRENT_FOLDER));

        ActionBar actionBar = ((BaseFragmentActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().compareTo("default_folder_key") == 0) {
            mAlertDialog.show();
            return true;
        }
        return false;
    }

    private void initSetDefaultFolderDialog(String currentFolder) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // set title
        alertDialogBuilder.setTitle(getString(R.string.set_default_folder));

        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.set_actual_folder_as_default,currentFolder))
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, id) -> {
                    mSPInteractor.saveHomeFolder(currentFolder);
                    mDefaultFolderPreference.setSummary(currentFolder);
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());

        // create alert dialog
        mAlertDialog = alertDialogBuilder.create();
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onUpButtonClicked() {

    }
}
