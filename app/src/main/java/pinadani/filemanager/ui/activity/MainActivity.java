package pinadani.filemanager.ui.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import pinadani.filemanager.R;
import pinadani.filemanager.mvp.presenter.MainPresenter;
import pinadani.filemanager.mvp.view.IMainView;
import pinadani.filemanager.ui.activity.base.BaseNucleusActivity;
import pinadani.filemanager.ui.fragment.BrowserFragment;
import pinadani.filemanager.ui.fragment.dialog.AccessStoragePermissionDialogFragment;

/**
 * Main activity
 * Created by Daniel.Pina on 1.7.2017.
 */
@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BaseNucleusActivity<MainPresenter> implements IMainView {
    public static final String TAG = MainActivity.class.getName();

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private Toast mLeavingToast;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        // Toast shown when user presses back button first time
        mLeavingToast = Toast.makeText(this, R.string.main_leave, Toast.LENGTH_SHORT);

        //requesting storage permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkForPermissions(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AccessStoragePermissionDialogFragment.REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish();
                }
            }
        }
    }

    @Override
    protected String getFragmentName() {
        return BrowserFragment.class.getName();
    }

    @Override
    public void onBackPressed() {
        // Press back button two times before leaving the app.
        if (mLeavingToast.getView().getWindowVisibility() != View.VISIBLE) {
            mLeavingToast.show();
        } else {
            super.onBackPressed();
        }
    }
}
