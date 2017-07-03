package pinadani.filemanager.utils;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import java.io.File;

import pinadani.filemanager.App;

/**
 * Intent utilities mainly gathering different purpose of user needs
 * Created on {18/12/15}
 **/
public class IntentUtils {
    public static final String TAG = IntentUtils.class.getName();

    public static Intent createFileOpenIntent(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(App.getInstance(), "pinadani.provider", file);
        intent.setDataAndType(uri, MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.getName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }
}
