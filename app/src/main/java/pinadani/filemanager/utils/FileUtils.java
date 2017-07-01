package pinadani.filemanager.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;

import pinadani.filemanager.Constants;

/**
 * Created by Daniel.Pina on 1.7.2017.
 */
public class FileUtils {
    private static final String BYTES_POSTFIX = " bytes";
    private static final String KILOBYTES_POSTFIX = " kb";
    private static final String MEGABYTES_POSTFIX = " mb";
    private static final String GIGABYTES_POSTFIX = " gb";

    public static final FileFilter DEFAULT_FILE_FILTER = pathname -> !pathname.isHidden();

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
     * TODO
     * TODO
     *
     * @param file
     * @return
     */
    public static String formatFileSize(File file) {
        long size = file.length();

        if (size < Constants.KILOBYTE) {
            return getFileSizeString(size, BYTES_POSTFIX);
        } else {
            if (size < Constants.MEGABYTE) {
                return getFileSizeString(size / Constants.KILOBYTE, KILOBYTES_POSTFIX);
            } else {
                if (size < Constants.GIGABYTE) {
                    return getFileSizeString(size / Constants.MEGABYTE, MEGABYTES_POSTFIX);
                } else {
                    return getFileSizeString(size / Constants.GIGABYTE, GIGABYTES_POSTFIX);
                }
            }
        }
    }

    private static String getFileSizeString(long size, String postfix) {
        return String.format(Locale.US, "%.2f", (float) size) + postfix;
    }

    /**
     * TODO
     *
     * @param folder
     * @return
     */
    public static int getNumFilesInFolder(File folder) {
        if (!folder.isDirectory()) {
            return 0;
        }
        File[] files = folder.listFiles();
        return files == null ? 0 : files.length;
    }

    /**
     * TODO
     *
     * @param directories
     */
    public static void deleteFoldersOrFolder(Collection<File> directories) {
        for (File file : directories) {
            if (file.isDirectory()) {
                deleteFoldersOrFolder(Arrays.asList(file.listFiles()));
            }
            file.delete();
        }
    }


}
