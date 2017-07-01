package pinadani.filemanager.ui.fragment.base;

import android.support.v7.app.ActionBar;
import android.text.TextUtils;

import pinadani.filemanager.ui.activity.base.BaseFragmentActivity;

/**
 * Common class for all types of fragments
 */
public class FragmentDelegate {
    static final String TAG = "FragmentDelegate";
    private IBaseFragment fragment;

    public void onDestroy() {
        fragment = null;
    }

    public void onCreate(IBaseFragment baseFragment) {
        fragment = baseFragment;
    }

    /**
     * Basic settings of actionbar
     */
    protected void baseSettingsAB() {
        ActionBar ab = getActivity().getSupportActionBar();
        ab.setDisplayShowTitleEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private BaseFragmentActivity getActivity() {
        return (BaseFragmentActivity) fragment.getActivity();
    }

    void setTitle(int title) {
        setTitle(getActivity().getString(title));

    }

    void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        getActivity().setTitle(title);
    }
}
