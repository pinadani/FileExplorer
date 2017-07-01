package pinadani.filemanager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

/**
 * Utilities for UI related things like device size or conversion from dp to px
 * Created on {18/12/15}
 **/
public class UiUtils {
    public static final String TAG = UiUtils.class.getName();

    public static int dpToPx(Context ctx, int dp) {
        Resources resources = ctx.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    /**
     * Obtain host activity from given context.
     *
     * @param context Context
     * @return Activity or null.
     */
    @Nullable
    public static Activity getActivityFromContext(@NonNull Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public static int getStatusBarHeight(Context ctx) {
        int result = 0;
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            result = ctx.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
