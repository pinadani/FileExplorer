package pinadani.filemanager.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;

import pinadani.filemanager.Constants;

/**
 * File model for FileAdapter, which contains the variable "select".
 * Created by Daniel Pina on 7/1/2017.
 */

public class FileOrFolder extends File {

    /**
     * The variable that determines that the file is selected in the list.
     */
    private boolean selected = false;

    public FileOrFolder(@NonNull String pathname) {
        super(pathname);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isParent() {
        return TextUtils.equals(Constants.DEFAULT_HOME_FOLDER, getAbsolutePath());
    }
}
