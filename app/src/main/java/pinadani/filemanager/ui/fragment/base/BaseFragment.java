/**
 * BaseFragment.java
 *
 * @project Ulek
 * @package cz.eman.ulek.fragment.base.BaseFragment
 * @author eMan s.r.o.
 * @since 19.11.13 14:13
 */

package pinadani.filemanager.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import butterknife.ButterKnife;
import pinadani.filemanager.ui.activity.base.BaseFragmentActivity;


public abstract class BaseFragment extends Fragment implements IBaseFragment {
    public static String TAG = BaseFragment.class.getName();

    private FragmentDelegate delegate;

    public BaseFragment() {
        if (getArguments() == null) {
            setArguments(new Bundle());
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate = new FragmentDelegate();
        delegate.onCreate(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAB();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            setTitle(getTitle());
        }
    }

    @Override
    public void onDestroy() {
        delegate.onDestroy();
        delegate = null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onUpButtonClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when back button is pressed
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }

    /**
     * Called when up is clicked
     */
    public void onUpButtonClicked() {
    }

    public void setTitle(int title) {
        delegate.setTitle(title);
    }

    protected BaseFragmentActivity getFragmentActivity() {
        return (BaseFragmentActivity) getActivity();
    }

    protected abstract String getTitle();

    public void setTitle(String title) {
        delegate.setTitle(title);
    }

    protected abstract void initAB();

    protected void baseSettingsAB() {
        delegate.baseSettingsAB();
    }
}
