package pinadani.filemanager.ui.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import pinadani.filemanager.R;

/**
 * Universal alert dialog
 * <p>
 * Created by daniel.pina@ackee.cz
 * 5/3/2016
 */
public class UniversalAlertDialogFragment extends DialogFragment {
    public static final String TAG = UniversalAlertDialogFragment.class.getName();
    private static final String ARG_TEXT = "arg_text";
    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_POSITIVE_TEXT = "arg_positive_text";
    private static final String ARG_NEGATIVE_TEXT = "arg_negative_text";
    private static final String ARG_POSITIVE_SHOW = "arg_positive_show";
    private static final String ARG_NEGATIVE_SHOW = "arg_negative_show";
    private static final String ARG_CANCELABLE = "arg_cancelable";

    public static class DialogBuilder {
        private Bundle arguments;

        public DialogBuilder(String title, String text) {
            arguments = new Bundle();
            arguments.putString(ARG_TITLE, title);
            arguments.putString(ARG_TEXT, text);
        }

        public DialogBuilder showPositive(boolean show) {
            arguments.putBoolean(ARG_POSITIVE_SHOW, show);
            return this;
        }

        public DialogBuilder showNegative(boolean show) {
            arguments.putBoolean(ARG_NEGATIVE_SHOW, show);
            return this;
        }

        public DialogBuilder positiveText(String text) {
            arguments.putString(ARG_POSITIVE_TEXT, text);
            return this;
        }

        public DialogBuilder negativeText(String text) {
            arguments.putString(ARG_NEGATIVE_TEXT, text);
            return this;
        }

        public DialogBuilder cancelable(boolean cancelable) {
            arguments.putBoolean(ARG_CANCELABLE, cancelable);
            return this;
        }

        public UniversalAlertDialogFragment build() {
            UniversalAlertDialogFragment fragment = new UniversalAlertDialogFragment();
            // Set arguments to fragment
            fragment.setArguments(arguments);
            return fragment;
        }

        /**
         * Build DialogFragment and set target fragment with its request code.
         */
        public <T extends Fragment & IUniversalAlertDialogListener> UniversalAlertDialogFragment build(@NonNull T targetFragment, int requestCode) {
            UniversalAlertDialogFragment fragment = build();
            fragment.setTargetFragment(targetFragment, requestCode);
            return fragment;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Cast dialog listener.
        final IUniversalAlertDialogListener dialogListener;
        if (getTargetFragment() != null && getTargetFragment() instanceof IUniversalAlertDialogListener) {
            dialogListener = (IUniversalAlertDialogListener) getTargetFragment();
        } else {
            dialogListener = null;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getString(ARG_TITLE, ""))
                .setMessage(getArguments().getString(ARG_TEXT, ""));

        // Cancelable (default is TRUE)
        //setCancelable(!getArguments().getBoolean(ARG_CANCELABLE, true));

        // Positive button
        if (getArguments().getBoolean(ARG_POSITIVE_SHOW, true)) {
            builder.setPositiveButton(getArguments().getString(ARG_POSITIVE_TEXT, getString(R.string.alert_dialog_default_positive)), (dialog, which) -> {
                if (dialogListener != null) {
                    dialogListener.onPositiveButtonClicked(getTargetRequestCode());
                }
            });
        }

        // Negative button
        if (getArguments().getBoolean(ARG_NEGATIVE_SHOW, false)) {
            builder.setNegativeButton(getArguments().getString(ARG_NEGATIVE_TEXT, getString(R.string.alert_dialog_default_negative)), (dialog, which) -> {
                if (dialogListener != null) {
                    dialogListener.onNegativeButtonClicked();
                }
            });
        }

        AlertDialog alert = builder.create();
        setCancelable(getArguments().getBoolean(ARG_CANCELABLE, true));
        alert.setCanceledOnTouchOutside(getArguments().getBoolean(ARG_CANCELABLE, true));

        return alert;
    }

    /**
     * Delegate dismiss event.
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getTargetFragment() != null && getTargetFragment() instanceof IUniversalAlertDialogListener) {
            ((IUniversalAlertDialogListener) getTargetFragment()).onDismissed();
        }
    }

    /**
     * Interface for FragmentDialog actions. Interface must be implemented by Target fragment.
     */
    public interface IUniversalAlertDialogListener {
        void onPositiveButtonClicked(int requestCode);

        void onNegativeButtonClicked();

        void onDismissed();
    }
}
