package com.sscf.investment.component.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import com.sscf.investment.component.ui.widget.BaseActivity;

/**
 * Created by davidwei on 2017-12-06
 *
 */
public final class ActivityUtils {

    public static boolean isDestroy(final Activity activity) {
        if (activity == null) {
            return true;
        } else if(activity instanceof BaseActivity) {
            return ((BaseActivity)activity).isDestroy();
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return activity.isDestroyed() || activity.isFinishing();
        } else {
            return activity.isFinishing();
        }
    }

    public static boolean isActivityDestroy(final Context context) {
        if (context instanceof Activity) {
            return isDestroy((Activity) context);
        } else if (context instanceof android.view.ContextThemeWrapper) {
            final Context baseContext = ((android.view.ContextThemeWrapper) context).getBaseContext();
            if (baseContext != null && baseContext instanceof Activity) {
                return isDestroy((Activity) baseContext);
            }
        }
        return true;
    }
}
