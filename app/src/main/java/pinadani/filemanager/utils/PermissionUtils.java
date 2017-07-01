package pinadani.filemanager.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Utility class for permissions
 * Created by Daniel.Pina on 1.7.2017.
 **/
public class PermissionUtils {
    public static final String TAG = PermissionUtils.class.getName();

    public static boolean areStoragePermissionsGranted(Context ctx) {
        String accessFineLocation = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        return ActivityCompat.checkSelfPermission(ctx, accessFineLocation) == PackageManager.PERMISSION_GRANTED;
    }
}
