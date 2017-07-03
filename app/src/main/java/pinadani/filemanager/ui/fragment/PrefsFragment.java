package pinadani.filemanager.ui.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.preference.PreferenceFragmentCompat;

import pinadani.filemanager.R;
import pinadani.filemanager.ui.activity.base.BaseFragmentActivity;
import pinadani.filemanager.ui.fragment.base.IBaseFragment;

/**
 * TODO
 * Created by Daniel.Pina on 1.7.2017.
 */
public class PrefsFragment extends PreferenceFragmentCompat implements IBaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        ActionBar actionBar = ((BaseFragmentActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }


    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onUpButtonClicked() {

    }
}
