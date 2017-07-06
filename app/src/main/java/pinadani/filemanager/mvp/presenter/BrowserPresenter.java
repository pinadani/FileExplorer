package pinadani.filemanager.mvp.presenter;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import nucleus.presenter.RxPresenter;
import pinadani.filemanager.App;
import pinadani.filemanager.R;
import pinadani.filemanager.interactor.ISPInteractor;
import pinadani.filemanager.model.FileOrFolder;
import pinadani.filemanager.mvp.view.IBrowserView;
import pinadani.filemanager.ui.activity.base.BaseFragmentActivity;
import pinadani.filemanager.ui.adapter.FileAdapter;
import pinadani.filemanager.ui.fragment.BrowserFragment;
import pinadani.filemanager.ui.fragment.PrefsFragment;
import pinadani.filemanager.utils.FileUtils;
import pinadani.filemanager.utils.IntentUtils;
import pinadani.filemanager.utils.PermissionUtils;

/**
 * Presenter managing actions on the browser fragment.
 * Created by Daniel.Pina on 1.7.2017.
 */

public class BrowserPresenter extends RxPresenter<IBrowserView> implements FileAdapter.IFileListener {
    @Inject
    ISPInteractor mSPInteractor;

    /**
     * Adapter with dir and files to show
     */
    private FileAdapter mAdapter;

    private File mCurrentDir;

    /**
     * Currently displayed folders and files.
     */
    private List<FileOrFolder> mFiles = null;

    private AsyncTask loadFilesTask = null;

    /**
     * Am I in select mode?
     */
    private boolean mSelectedMode = false;

    /**
     * Number of selected items.
     */
    private int mSelectedItemCount = 0;


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        App.getAppComponent().inject(this);

        // Get Home folder and init FileAdapter
        mCurrentDir = mSPInteractor.getHomeFolder();
        if (mAdapter == null) {
            mAdapter = new FileAdapter(null, this);
        }
    }

    @Override
    protected void onTakeView(IBrowserView iBrowserView) {
        super.onTakeView(iBrowserView);

        // Set adapter to recycler view
        iBrowserView.initFileAdapter(mAdapter);

        // If app has permissions granted, load current dir
        if (PermissionUtils.areStoragePermissionsGranted(((BrowserFragment) iBrowserView).getActivity())) {
            loadFileList();
        }
    }

    @Override
    public void onDestroy() {
        if (loadFilesTask != null)
            loadFilesTask.cancel(true);
        super.onDestroy();
    }

    /**
     * Load current dir subfilders and subfiles
     */
    void loadFileList() {
        exitSelectedMode();

        if (loadFilesTask != null) {
            return;
        }
        this.loadFilesTask = new AsyncTask<File, Void, File[]>() {
            @Override
            protected File[] doInBackground(File... params) {
                File[] files = params[0].listFiles(FileUtils.DEFAULT_FILE_FILTER);
                try {
                    if (files == null) {
                        throw new NullPointerException("Cannot read directory");
                    }
                    if (isCancelled()) {
                        throw new Exception("Task cancelled");
                    }

                    Arrays.sort(files, new FileUtils.FileNameComparator());
                    return files;
                } catch (Exception e) {
                    return files;
                }
            }

            @Override
            protected void onCancelled(File[] result) {
                loadFilesTask = null;
            }

            @Override
            protected void onPostExecute(File[] result) {
                loadFilesTask = null;

                if(result == null) {
                    clickedOnParent();
                    return;
                }

                mFiles = new ArrayList<>();

                // if I am not in root folder show item to navigate up
                if (!FileUtils.isInternalOrSDCard(mCurrentDir)) {
                    mAdapter.setParent(true);
                    mFiles.add(new FileOrFolder(""));
                } else {
                    mAdapter.setParent(false);
                }

                for (File file : result) {
                    mFiles.add(new FileOrFolder(file.getAbsolutePath()));
                }

                mAdapter.setData(mFiles);

                // Set current file name as title
                if (getView() != null) {
                    ((BrowserFragment) getView()).setTitle(FileUtils.getShortPath(mCurrentDir));
                    ((BrowserFragment) getView()).showEmptyFolder(mFiles.size() == 1);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mCurrentDir);
    }

    /**
     * Clear select mode variables
     */
    private void exitSelectedMode() {
        mSelectedItemCount = 0;
        mSelectedMode = false;
        if (mFiles != null) {
            for (FileOrFolder file : mFiles) {
                file.setSelected(false);
            }
        }
        if (getView() != null) {
            getView().switchActionMode(false);
        }
    }

    @Override
    public void onFileClicked(FileOrFolder file, int position, ImageView imageView) {

        // If user clicked on UP-navigation, load parrent folder
        if (file.isParent()) {
            clickedOnParent();
            return;
        }

        if (mSelectedMode) {
            if (file.isSelected()) {
                mSelectedItemCount--;
            } else {
                mSelectedItemCount++;
            }
            file.setSelected(!file.isSelected());
            mSelectedMode = mSelectedItemCount != 0;
            if (!mSelectedMode) {
                exitSelectedMode();
            }
            mAdapter.notifyItemChanged(position);
        } else {
            if (file.isDirectory()) {
                openDirectoryAfterAnimation(file, imageView);
            } else {
                openFile(file);
            }
        }
    }

    /**
     * Open dir afetr animation end.
     *
     * @param file      Folder to open
     * @param imageView ImageView to animate
     */
    private void openDirectoryAfterAnimation(FileOrFolder file, ImageView imageView) {
        // dismiss animation
        Animation dismiss = AnimationUtils.loadAnimation(App.getInstance(), R.anim.dismiss);
        dismiss.setDuration(500);
        dismiss.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                openDirectory(file);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.startAnimation(dismiss);
    }

    /**
     * Open parent directory and exit select mode
     */
    private void clickedOnParent() {
        exitSelectedMode();
        openDirectory(mCurrentDir.getParentFile());
    }

    /**
     * Open file in default app
     *
     * @param file File to open
     */
    private void openFile(File file) {

        if (getView() != null) {

            Intent intent = IntentUtils.createFileOpenIntent(file);

            try {
                App.getInstance().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                App.getInstance().startActivity(Intent.createChooser(intent, App.getInstance().getString(R.string.open_file_with, file.getName())));
            } catch (Exception e) {
                new AlertDialog.Builder(((BrowserFragment) getView()).getActivity())
                        .setMessage(e.getMessage())
                        .setTitle(R.string.error)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        }
    }

    private void openDirectory(File file) {
        mCurrentDir = file;
        loadFileList();
    }

    /**
     * After first long click start select mode
     *
     * @param file     selected file
     * @param position file position
     */
    @Override
    public void onLongFileClicked(FileOrFolder file, int position) {
        if (file.isParent()) {
            clickedOnParent();
            return;
        }
        if (!mSelectedMode) {
            mSelectedItemCount++;
            mSelectedMode = true;
            file.setSelected(true);
            getView().switchActionMode(true);
            mAdapter.notifyItemChanged(position);
        } else {
            onFileClicked(file, position, null);
        }
    }

    /**
     * Delete selected files and folders
     */
    public void deleteSelectedItems() {
        Collection<File> filesToDelete = new ArrayList<>();
        for (FileOrFolder file : mFiles) {
            if (file.isSelected()) {
                filesToDelete.add(file);
            }
        }
        FileUtils.deleteFoldersOrFolder(filesToDelete);
        refresh();
    }

    /**
     * Refresh actual list of files
     */
    public void refresh() {
        loadFileList();
    }

    /**
     * Deselect all items after exit select mode
     */
    public void deselectItems() {
        for (FileOrFolder file : mFiles) {
            file.setSelected(false);
        }
        mSelectedItemCount = 0;
        mSelectedMode = false;
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Notification that permission id granted
     * start loading files
     *
     * @param granted if storage permission is granted
     */
    public void permissionsGranted(boolean granted) {
        if (granted) {
            // Now app can find home folder
            mCurrentDir = mSPInteractor.getHomeFolder();
            loadFileList();
        }
    }

    /**
     * Back pressed navigate to parent folder
     * if current folder is root, exit app
     *
     * @return current folder is root
     */
    public boolean onBackPressed() {
        if (!FileUtils.isInternalOrSDCard(mCurrentDir)) {
            mCurrentDir = mCurrentDir.getParentFile();
            loadFileList();
            return false;
        }
        return true;
    }

    /**
     * Navigate to filder selected from menu drawer
     */
    public void switchFolder(int storageType) {
        mCurrentDir = FileUtils.getDirByType(storageType, mSPInteractor.getHomeFolder());
        loadFileList();
    }

    /**
     * Open settings with current folder pushed
     */
    public void openSettings() {
        Bundle bundle = new Bundle();
        bundle.putString(PrefsFragment.CURRENT_FOLDER, mCurrentDir.getAbsolutePath());

        BaseFragmentActivity.startActivity(App.getInstance(), PrefsFragment.class.getName(), bundle);
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }
}
