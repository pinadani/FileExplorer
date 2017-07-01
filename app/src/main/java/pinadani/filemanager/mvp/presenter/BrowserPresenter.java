package pinadani.filemanager.mvp.presenter;

import android.os.AsyncTask;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import nucleus.presenter.RxPresenter;
import pinadani.filemanager.App;
import pinadani.filemanager.interactor.ISPInteractor;
import pinadani.filemanager.mvp.view.IBrowserView;
import pinadani.filemanager.ui.adapter.FileAdapter;
import pinadani.filemanager.utils.FileUtils;

/**
 * Presenter managing actions on the main activity.
 * Created by Daniel.Pina on 1.7.2017.
 */

public class BrowserPresenter extends RxPresenter<IBrowserView> {
    @Inject
    ISPInteractor mSPInteractor;

    private FileAdapter mAdapter;

    private File mCurrentDir;
    private List<File> mFiles = null;

    private AsyncTask loadFilesTask = null;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        App.getAppComponent().inject(this);
        mCurrentDir = mSPInteractor.getHomeFolder();
        if (mAdapter == null) {
            mAdapter = new FileAdapter(null);
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

                mFiles = result == null ? new ArrayList<>() : Arrays.asList(result);
                mAdapter.setData(new ArrayList<>(Arrays.asList(result)));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mCurrentDir);
    }

    @Override
    public void onDestroy() {
        if (loadFilesTask != null)
            loadFilesTask.cancel(true);
        super.onDestroy();
    }

}
