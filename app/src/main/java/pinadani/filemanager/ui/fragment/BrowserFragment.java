package pinadani.filemanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import pinadani.filemanager.R;
import pinadani.filemanager.mvp.presenter.BrowserPresenter;
import pinadani.filemanager.mvp.view.IBrowserView;
import pinadani.filemanager.ui.activity.base.BaseFragmentActivity;
import pinadani.filemanager.ui.adapter.FileAdapter;
import pinadani.filemanager.ui.fragment.base.BaseNucleusFragment;

/**
 * Browser Fragment
 * Created by Daniel Pina
 **/
@RequiresPresenter(BrowserPresenter.class)
public class BrowserFragment extends BaseNucleusFragment<BrowserPresenter> implements IBrowserView {
    public static final String TAG = BrowserFragment.class.getName();

    @Bind(R.id.recycler_file)
    RecyclerView mRecyclerFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browser, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerFile.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                BaseFragmentActivity.startActivity(getActivity(), PrefsFragment.class.getName());
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.app_name);
    }

    @Override
    protected void initAB() {
    }

    @Override
    public void initFileAdapter(FileAdapter adapter) {
        mRecyclerFile.setAdapter(adapter);
    }
}
