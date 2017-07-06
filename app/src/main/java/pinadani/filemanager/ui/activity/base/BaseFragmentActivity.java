package pinadani.filemanager.ui.activity.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;

import pinadani.filemanager.R;
import pinadani.filemanager.ui.fragment.base.IBaseFragment;
import pinadani.filemanager.ui.fragment.dialog.AccessStoragePermissionDialogFragment;
import pinadani.filemanager.utils.PermissionUtils;


public class BaseFragmentActivity extends AppCompatActivity {
    public static final String TAG = "BaseFragmentActivity";

    private static final String EXTRA_FRAGMENT_NAME = "fragment";
    private static final String EXTRA_ARGUMENTS = "arguments";
    public static final int CONTENT_VIEW_ID = R.id.fragmentContainer;
    private static final String EXTRA_RESULT = "result";
    private View mContentView;

    /**
     * gets intent for starting new activity with fragment defined by fragment name and passes extras to the starting intent
     *
     * @param ctx
     * @param fragmentName fragment to instantiate
     * @param args         to pass to the instantiated fragment
     */
    public static Intent generateIntent(Context ctx, String fragmentName, Bundle args) {
        return new Intent(ctx, BaseFragmentActivity.class).putExtra(EXTRA_FRAGMENT_NAME, fragmentName).putExtra(EXTRA_ARGUMENTS, args);
    }

    /**
     * Create result intent with parcelable result saved in extras
     *
     * @param result Parcelable to be stored
     * @return intent
     */
    @NonNull
    public static Intent generateResultIntent(@NonNull Parcelable result) {
        return new Intent().putExtra(EXTRA_RESULT, result);
    }

    /**
     * Get result from intent's extras
     *
     * @param intent result intent
     * @return result as parcelable
     */
    @Nullable
    public static Parcelable getResultFromResultIntent(@NonNull Intent intent) {
        if (intent.getExtras() != null) {
            return intent.getExtras().getParcelable(EXTRA_RESULT);
        }
        return null;
    }

    /**
     * gets intent for starting new activity with fragment defined by fragment name and passes extras to the starting intent
     *
     * @param ctx
     * @param fragmentName fragment to instantiate
     * @param args         to pass to the instantiated fragment
     */
    public static Intent generateIntent(Context ctx, String fragmentName, Bundle args, Class<?> activityClass) {
        return new Intent(ctx, activityClass).putExtra(EXTRA_FRAGMENT_NAME, fragmentName).putExtra(EXTRA_ARGUMENTS, args);
    }

    public static void startActivity(Context ctx, String fragmentName) {
        Intent intent = new Intent(ctx, BaseFragmentActivity.class).putExtra(EXTRA_FRAGMENT_NAME, fragmentName);
        ctx.startActivity(intent);
    }

    /**
     * Start specific activity and open fragment defined by name
     *
     * @param ctx
     * @param fragmentName
     * @param activityClass
     */
    public static void startActivity(Context ctx, String fragmentName, Class<?> activityClass) {
        Intent intent = new Intent(ctx, activityClass).putExtra(EXTRA_FRAGMENT_NAME, fragmentName);
        ctx.startActivity(intent);
    }


    /**
     * Start specific activity and open fragment defined by name
     *
     * @param ctx
     * @param fragmentName
     * @param activityClass
     */
    public static void startActivityForResult(Context ctx, String fragmentName, Class<?> activityClass, int requestCode) {
        Intent intent = new Intent(ctx, activityClass).putExtra(EXTRA_FRAGMENT_NAME, fragmentName);
        if (ctx instanceof Activity) {
            ((Activity) ctx).startActivityForResult(intent, requestCode);
        }
    }

    /**
     * Start specific activity and open fragment defined by name
     *
     * @param ctx
     * @param fragmentName
     * @param activityClass
     */
    public static void startActivity(Context ctx, String fragmentName, Class<?> activityClass, Bundle args) {
        Intent intent = new Intent(ctx, activityClass).putExtra(EXTRA_FRAGMENT_NAME, fragmentName).putExtra(EXTRA_ARGUMENTS, args);
        ctx.startActivity(intent);
    }

    /**
     * starts new activity with fragment defined by fragment name and passes extras to the starting intent
     *
     * @param ctx
     * @param fragmentName fragment to instantiate
     * @param args         to pass to the instantiated fragment
     */
    public static void startActivity(Context ctx, String fragmentName, Bundle args) {
        ctx.startActivity(generateIntent(ctx, fragmentName, args));
    }

    /**
     * starts new activity with fragment defined by fragment name and passes extras to the starting intent
     *
     * @param ctx
     * @param fragmentName fragment to instantiate
     * @param extras       to copy to the new intent
     */
    public static void startActivity(Context ctx, String fragmentName, Intent extras) {
        Intent intent = new Intent(ctx, BaseFragmentActivity.class).putExtra(EXTRA_FRAGMENT_NAME, fragmentName).putExtras(extras);
        ctx.startActivity(intent);
    }


    public View getContentView() {
        return mContentView;
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(CONTENT_VIEW_ID);
    }


    /**
     * returns the name of the fragment to be instantiated
     *
     * @return
     */

    protected String getFragmentName() {
        return getIntent().getStringExtra(EXTRA_FRAGMENT_NAME);
    }

    /**
     * instantiates the fragment
     *
     * @return
     */
    protected Fragment instantiateFragment(String fragmentName) {
        return Fragment.instantiate(this, fragmentName);
    }

    /**
     * Method that creates content view with fragment container. Container must have id set to R.id.fragmentContainer
     */

    protected View onCreateContentView() {
        return getLayoutInflater().inflate(R.layout.activity_base, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentView = onCreateContentView();

        setContentViewInternal(mContentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        initToolbar();
        String fragmentName = getFragmentName();
        if (fragmentName == null) {
            finish();
            return;
        }

        Bundle args = getIntent().getBundleExtra(EXTRA_ARGUMENTS);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentName);
        if ((fragment == null) && (savedInstanceState == null)) {
            fragment = instantiateFragment(fragmentName);
            if (args != null) {
                fragment.setArguments(args);
            }
            getSupportFragmentManager().beginTransaction().add(CONTENT_VIEW_ID, fragment, fragment.getClass().getName()).commit();
        }

    }

    /**
     * Check for storage permissions.
     *
     * @param forceDialog Boolean whether to forcefully open dialog about why should the user
     *                    allow storage permission.
     */
    public boolean checkForPermissionsAndShowDialog(boolean forceDialog) {
        // Here, thisActivity is the current activity
        String accessStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (!PermissionUtils.areStoragePermissionsGranted(this)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, accessStorage) || forceDialog) {
                new AccessStoragePermissionDialogFragment().show(getSupportFragmentManager(), AccessStoragePermissionDialogFragment.TAG);
            } else {
                String[] permissions = {accessStorage};
                ActivityCompat.requestPermissions(this, permissions, AccessStoragePermissionDialogFragment.REQUEST_CODE);
            }
            return false;
        }
        return true;
    }

    /**
     * Init activity actionbar as toolbar from layout. Child activity should override this if it dont want actionbar present
     */
    protected void initToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));
    }


    /**
     * replace fragment with a new fragment, add it to the back stack and use fragment name as a
     * transaction tag
     *
     * @param fragment for container to be replaced with
     */
    public void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, fragment.getClass().getName(), true);
    }

    /**
     * replaces fragment with a new fragment and uses fragment name as a
     * transaction tag
     *
     * @param fragment for container to be replaced with
     */
    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        replaceFragment(fragment, fragment.getClass().getName(), addToBackStack);
    }

    /**
     * @param fragment       fragment for container to be replaced with
     * @param name           of the transaction, null if not needed
     * @param addToBackStack
     */
    public void replaceFragment(Fragment fragment, String name, boolean addToBackStack) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(CONTENT_VIEW_ID, fragment, fragment.getClass().getName());
            if (addToBackStack) {
                transaction.addToBackStack(name);
            }

            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        } catch (Exception e) {//java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
            e.printStackTrace();
        }

    }

    protected void setContentViewInternal(View view, ViewGroup.LayoutParams params) {
        setContentView(view, params);
    }

    ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getCurrentFragment() != null) {
            getCurrentFragment().onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() == null || (!((IBaseFragment) getCurrentFragment()).onBackPressed())) {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getCurrentFragment() != null) {
                    ((IBaseFragment) getCurrentFragment()).onUpButtonClicked();
                }
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void showHomeButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
