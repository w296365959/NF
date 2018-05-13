package com.sscf.investment.scan.camera;

import android.app.Activity;
import android.hardware.Camera;
import android.view.OrientationEventListener;
import android.view.Surface;

import com.sscf.investment.sdk.utils.DtLog;

/**
 * Created by yorkeehuang on 2017/6/1.
 */

public class Util {
    private static final String TAG = Util.class.getSimpleName();

    // Orientation hysteresis amount used in rounding, in degrees
    public static final int ORIENTATION_HYSTERESIS = 5;

    public static int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0: return 0;
            case Surface.ROTATION_90: return 90;
            case Surface.ROTATION_180: return 180;
            case Surface.ROTATION_270: return 270;
        }
        return 0;
    }

    public static int roundOrientation(int orientation, int orientationHistory) {
       boolean changeOrientation = false;
       if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
                changeOrientation = true;
            } else {
                int dist = Math.abs(orientation - orientationHistory);
                dist = Math.min( dist, 360 - dist );
                changeOrientation = ( dist >= 45 + ORIENTATION_HYSTERESIS);
            }
        if (changeOrientation) {
                return ((orientation + 45) / 90 * 90) % 360;
        }
        return orientationHistory;
    }

    public static void setRotationParameter(Camera.Parameters parameters, int orientation) {
        int rotation = 0;
        if (orientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
            Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
            rotation = (info.orientation + orientation + 90) % 360;
            DtLog.d(TAG, "setRotationParameter() info.orientation = " + info.orientation + ", orientation = " + orientation);
        }
        DtLog.d(TAG, "setRotationParameter() rotation = " + rotation);
        parameters.setRotation(rotation);
    }
}
