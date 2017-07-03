package pinadani.filemanager.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public interface IBaseFragment {
    FragmentActivity getActivity();

    /**
     * Called when back button is pressed
     */
    boolean onBackPressed();

    /**
     * Get fragment arguments
     * @return
     */
    Bundle getArguments();

    /**
     * Called when UP button is clicked
     */
    void onUpButtonClicked();
}
