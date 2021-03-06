package pinadani.filemanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import pinadani.filemanager.App;
import pinadani.filemanager.R;
import pinadani.filemanager.mvp.presenter.BrowserPresenter;
import pinadani.filemanager.mvp.view.IBrowserView;
import pinadani.filemanager.ui.LayoutManager.GridAutoFitLayoutManager;
import pinadani.filemanager.ui.activity.MainActivity;
import pinadani.filemanager.ui.adapter.FileAdapter;
import pinadani.filemanager.ui.fragment.base.BaseNucleusFragment;

/**
 * Browser Fragment connect to its presenter.
 * <p>
 * Created by Daniel Pina on 1.7.2017.
 **/
@RequiresPresenter(BrowserPresenter.class)
public class BrowserFragment extends BaseNucleusFragment<BrowserPresenter> implements IBrowserView {
    public static final String TAG = BrowserFragment.class.getName();

    @Bind(R.id.recycler_file)
    RecyclerView mRecyclerFile;

    @Bind(R.id.txt_empty_folder)
    TextView mEmptyFolderText;

    private ActionMode mActionMode = null;

    private GridAutoFitLayoutManager mGridAutoFitLayoutManager =
            new GridAutoFitLayoutManager(App.getInstance(),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200,
                            App.getInstance().getResources().getDisplayMetrics()));
    /**
     * Double tap back to leaving
     */
    private Toast mLeavingToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Toast shown when user presses back button first time
        mLeavingToast = Toast.makeText(getActivity(), R.string.main_leave, Toast.LENGTH_SHORT);
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
        inflater.inflate(R.menu.menu_browser, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                getPresenter().openSettings();
                break;
            case R.id.refresh:
                getPresenter().refresh();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerFile.setLayoutManager(mGridAutoFitLayoutManager);
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

    @Override
    public void switchActionMode(boolean isOn) {
        // Start the CAB using the ActionMode.Callback defined above
        if (isOn) {
            mActionMode = getActivity().startActionMode(mActionModeCallback);
        } else {
            if (mActionMode != null) {

                mActionMode.finish();
            }
        }
    }

    /**
     * Callback wirk with action mode to delete items from recyclerview
     */
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; switchActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            ((MainActivity) getActivity()).getSupportActionBar().hide();
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_delete, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    getPresenter().deleteSelectedItems();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            ((MainActivity) getActivity()).getSupportActionBar().show();
            mActionMode = null;
            getPresenter().deselectItems();
        }
    };


    @Override
    public boolean onBackPressed() {
        if (getPresenter().onBackPressed()) {
            // Press back button two times before leaving the app.
            if (mLeavingToast.getView().getWindowVisibility() != View.VISIBLE) {
                mLeavingToast.show();
            } else {
                getActivity().finish();
            }
        }
        return false;
    }

    public void showEmptyFolder(boolean showEmptyFolder) {
        mEmptyFolderText.setVisibility(showEmptyFolder ? View.VISIBLE : View.GONE);
    }
}
