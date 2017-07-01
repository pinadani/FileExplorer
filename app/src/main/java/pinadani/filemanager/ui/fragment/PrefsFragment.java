package pinadani.filemanager.ui.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import pinadani.filemanager.R;

/**
 * TODO
 * Created by Daniel.Pina on 1.7.2017.
 */
public class PrefsFragment extends PreferenceFragmentCompat{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }
}
