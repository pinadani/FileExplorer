package pinadani.filemanager.ui.fragment.dialog;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;

import pinadani.filemanager.R;

/**
 * Dialog shown to explain why we need access fine location permission
 * Created by Daniel.Pina on 1.7.2017.
 */
public class AccessStoragePermissionDialogFragment extends DialogFragment {
    public static final String TAG = AccessStoragePermissionDialogFragment.class.getName();

    public static final int REQUEST_CODE = 18;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.permission_access_storage_expanation);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        });
        builder.setNegativeButton(R.string.alert_dialog_default_negative, (dialogInterface, i) -> {
            getActivity().finish();
        });
        builder.setCancelable(false);
        setCancelable(false);
        return builder.create();
    }
}

