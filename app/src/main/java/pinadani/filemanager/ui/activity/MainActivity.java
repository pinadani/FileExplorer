package pinadani.filemanager.ui.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import pinadani.filemanager.R;
import pinadani.filemanager.mvp.presenter.MainPresenter;
import pinadani.filemanager.mvp.view.IMainView;
import pinadani.filemanager.ui.activity.base.BaseFragmentActivity;
import pinadani.filemanager.ui.activity.base.BaseNucleusActivity;
import pinadani.filemanager.ui.fragment.BrowserFragment;
import pinadani.filemanager.ui.fragment.PrefsFragment;
import pinadani.filemanager.ui.fragment.dialog.AccessStoragePermissionDialogFragment;
import pinadani.filemanager.utils.FileUtils;

/**
 * Main activity
 * Created by Daniel.Pina on 1.7.2017.
 */
@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BaseNucleusActivity<MainPresenter> implements IMainView {
    public static final String TAG = MainActivity.class.getName();

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        // Initialize drawer with its navigation.
        initDrawer();

        //requesting storage permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkForPermissionsAndShowDialog(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AccessStoragePermissionDialogFragment.REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ((BrowserFragment) getCurrentFragment()).getPresenter().permissionsGranted(true);
                } else {
                    finish();
                }
            }
        }
    }

    /**
     * Initialize drawer with its navigation.
     */
    private void initDrawer() {

        // Init navigation.
        mNavigationView.setNavigationItemSelectedListener(item -> {
            //Closing drawer on item click
            mDrawerLayout.closeDrawers();

            int switchTo = FileUtils.HOME_STORAGE;
            //Check to see which item was being clicked and perform appropriate action
            switch (item.getItemId()) {

                case R.id.home:
                    switchTo = FileUtils.HOME_STORAGE;
                    break;
                case R.id.root:
                    switchTo = FileUtils.EXTERNAL_STORAGE;
                    break;
                case R.id.images:
                    switchTo = FileUtils.IMAGES_STORAGE;
                    break;
                case R.id.music:
                    switchTo = FileUtils.MUSIC_STORAGE;
                    break;
                case R.id.download:
                    switchTo = FileUtils.DOWNLOADS_STORAGE;
                    break;
                case R.id.settings:
                    ((BrowserFragment) getCurrentFragment()).getPresenter().openSettings();
                    return true;
                default:
                    break;
            }
            ((BrowserFragment) getCurrentFragment()).getPresenter().switchFolder(switchTo);
            return true;
        });


        // HamburgerMenu animation.
        mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer);

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        mActionBarDrawerToggle.syncState();
    }

    @Override
    protected String getFragmentName() {
        return BrowserFragment.class.getName();
    }

    @Override
    public void onBackPressed() {
        ((BrowserFragment) getCurrentFragment()).onBackPressed();
    }
}
