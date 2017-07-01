package pinadani.filemanager.mvp.presenter;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

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
import pinadani.filemanager.ui.adapter.FileAdapter;
import pinadani.filemanager.ui.fragment.BrowserFragment;
import pinadani.filemanager.utils.FileUtils;
import pinadani.filemanager.utils.IntentUtils;

/**
 * Presenter managing actions on the main activity.
 * Created by Daniel.Pina on 1.7.2017.
 */

public class BrowserPresenter extends RxPresenter<IBrowserView> implements FileAdapter.IFileListener {
    @Inject
    ISPInteractor mSPInteractor;

    private FileAdapter mAdapter;

    private File mCurrentDir;
    private List<FileOrFolder> mFiles = null;

    private AsyncTask loadFilesTask = null;

    private boolean mSelectedMode = false;
    private int mSelectedItemCount = 0;


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        App.getAppComponent().inject(this);
        mCurrentDir = mSPInteractor.getHomeFolder();
        if (mAdapter == null) {
            mAdapter = new FileAdapter(null, this);
        }
        loadFileList();
    }

    @Override
    protected void onTakeView(IBrowserView iBrowserView) {
        super.onTakeView(iBrowserView);
        iBrowserView.initFileAdapter(mAdapter);
    }

    void loadFileList() {
        if (loadFilesTask != null) return;
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

                mFiles = new ArrayList<>();
                for (File file : result) {
                    mFiles.add(new FileOrFolder(file.getAbsolutePath()));
                }
                mAdapter.setData(mFiles);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mCurrentDir);
    }

    @Override
    public void onDestroy() {
        if (loadFilesTask != null)
            loadFilesTask.cancel(true);
        super.onDestroy();
    }

    @Override
    public void onFileClicked(FileOrFolder file, int position) {
        if (mSelectedMode) {
            if (file.isSelected()) {
                mSelectedItemCount--;
            } else {
                mSelectedItemCount++;
            }
            file.setSelected(!file.isSelected());
            mSelectedMode = mSelectedItemCount != 0;
            getView().switchActionMode(mSelectedMode);
            mAdapter.notifyItemChanged(position);
        } else {
            if (file.isDirectory()) {
                openDirectory(file);
            } else {
                openFile(file);
            }
        }
    }

    private void openFile(File file) {

        if (getView() != null) {
            Intent intent = IntentUtils.createFileOpenIntent(file);

            try {
                ((BrowserFragment) getView()).startActivity(intent);

            } catch (ActivityNotFoundException e) {
                ((BrowserFragment) getView()).startActivity(Intent.createChooser(intent, ((BrowserFragment) getView()).getString(R.string.open_file_with_, file.getName())));
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

    @Override
    public void onLongFileClicked(FileOrFolder file, int position) {
        if (!mSelectedMode) {
            mSelectedItemCount++;
            mSelectedMode = true;
            file.setSelected(true);
            getView().switchActionMode(true);
            mAdapter.notifyItemChanged(position);
        } else {
            onFileClicked(file, position);
        }
    }

    public void deleteSelectedItems() {
        Collection<File> filesToDelete = new ArrayList<>();
        for (FileOrFolder file : mFiles) {
            if (file.isSelected()) {
                filesToDelete.add(file);
            }
        }
        FileUtils.deleteFoldersOrFolder(filesToDelete);
    }

    public void deselectItems() {
        for (FileOrFolder file : mFiles) {
            file.setSelected(false);
        }
        mSelectedItemCount = 0;
        mAdapter.notifyDataSetChanged();
    }
}
