package pinadani.filemanager.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;

import pinadani.filemanager.App;
import pinadani.filemanager.Constants;
import pinadani.filemanager.R;

/**
 * Class for working with files and filesystem
 * Created by Daniel.Pina on 1.7.2017.
 */
public class FileUtils {
    private static final String BYTES_POSTFIX = " bytes";
    private static final String KILOBYTES_POSTFIX = " KB";
    private static final String MEGABYTES_POSTFIX = " MB";
    private static final String GIGABYTES_POSTFIX = " GB";

    public static final int INTERNAL_STORAGE = 0;
    public static final int EXTERNAL_STORAGE = 1;
    public static final int IMAGES_STORAGE = 2;
    public static final int MUSIC_STORAGE = 3;
    public static final int DOWNLOADS_STORAGE = 4;

    public static final FileFilter DEFAULT_FILE_FILTER = pathname -> !pathname.isHidden();

    /**
     * Gets extension of the file name excluding the . character
     */
    public static String getFileExtension(String fileName) {
        if (fileName.contains("."))
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        else
            return "";
    }

    public static boolean isInternalHomeDir(File file) {
        return TextUtils.equals(Environment.getRootDirectory().getAbsolutePath(), file.getAbsolutePath());
    }

    public static boolean isExternalHomeDir(File file) {
        return TextUtils.equals(Environment.getExternalStorageDirectory().getAbsolutePath(), file.getAbsolutePath());
    }

    public static boolean isInternalOrSDCard(File file) {
        return (isInternalHomeDir(file) || isExternalHomeDir(file));
    }

    public static String getShortPath(File dir) {
        if (!isInternalOrSDCard(dir)) {
            String path = dir.getAbsolutePath();
            if (path.contains("/")) {
                return ("..." + path.substring(path.lastIndexOf('/')));
            }
        } else {
            if (isExternalHomeDir(dir)) {
                return App.getInstance().getString(R.string.root);
            }
        }
        return Constants.DEFAULT_HOME_FOLDER;
    }

    public static File getDirByType(int storageType) {
        switch (storageType) {
            case EXTERNAL_STORAGE:
                return getExternalSDCard();
            case IMAGES_STORAGE:
                return getImagesDir();
            case MUSIC_STORAGE:
                return getMusicDir();
            case DOWNLOADS_STORAGE:
                return getDownloadDir();
            default:
                return null;
        }
    }

    /**
     * Compares files by name
     * Directories come always first
     */
    public static class FileNameComparator implements Comparator<File> {
        private final static int FIRST = -1;
        private final static int SECOND = 1;

        @Override
        public int compare(File file1, File file2) {
            if (file1.isDirectory() || file2.isDirectory()) {
                if (file1.isDirectory() == file2.isDirectory()) {
                    return file1.getName().compareToIgnoreCase(file2.getName());
                } else {
                    if (file1.isDirectory()) {
                        return FIRST;
                    } else {
                        return SECOND;
                    }
                }
            }
            return file1.getName().compareToIgnoreCase(file2.getName());
        }
    }


    /**
     * File size formatting.
     *
     * @param file File where we format its size.
     * @return Formatted file size.
     */
    public static String formatFileSize(File file) {
        long size = file.length();

        if (size < Constants.KILOBYTE) {
            return getFileSizeString((float) size, BYTES_POSTFIX);
        } else {
            if (size < Constants.MEGABYTE) {
                return getFileSizeString((float) size / Constants.KILOBYTE, KILOBYTES_POSTFIX);
            } else {
                if (size < Constants.GIGABYTE) {
                    return getFileSizeString((float) size / Constants.MEGABYTE, MEGABYTES_POSTFIX);
                } else {
                    return getFileSizeString((float) size / Constants.GIGABYTE, GIGABYTES_POSTFIX);
                }
            }
        }
    }

    private static String getFileSizeString(float size, String postfix) {
        return String.format(Locale.US, "%.2f", size) + postfix;
    }

    /**
     * Getting the number of subfiles and subfolders.
     *
     * @param folder The file where we find the number of subfiles and subfolders.
     * @return number of subfiles and subfolders
     */
    public static int getNumFilesInFolder(File folder) {
        if (!folder.isDirectory()) {
            return 0;
        }
        File[] files = folder.listFiles();
        return files == null ? 0 : files.length;
    }

    /**
     * Delete collection of files and folders including all subfolders.
     *
     * @param directories Collection of files and folders to delete.
     */
    public static void deleteFoldersOrFolder(Collection<File> directories) {
        for (File file : directories) {
            if (file.isDirectory()) {
                deleteFoldersOrFolder(Arrays.asList(file.listFiles()));
            }
            file.delete();
        }
    }

    public static File getExternalSDCard() {
        return Environment.getExternalStorageDirectory();
    }

    public static File getMusicDir() {
        return getExternalStorageDir(Environment.DIRECTORY_MUSIC);
    }

    public static File getImagesDir() {
        return getExternalStorageDir(Environment.DIRECTORY_DCIM);
    }

    public static File getDownloadDir() {
        return getExternalStorageDir(Environment.DIRECTORY_DOWNLOADS);
    }

    private static File getExternalStorageDir(String dir) {
        return Environment.getExternalStoragePublicDirectory(dir);
    }
}
