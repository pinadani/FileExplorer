package pinadani.filemanager.model;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * TODO
 * Created by Daniel Pina on 7/1/2017.
 */

public class FileOrFolder extends File{
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
}
