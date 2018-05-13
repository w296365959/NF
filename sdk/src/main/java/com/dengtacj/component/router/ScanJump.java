package com.dengtacj.component.router;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.chenenyu.router.Router;
import com.sscf.investment.sdk.utils.CommonConst;

/**
 * Created by davidwei on 2017/09/25
 */
public final class ScanJump {

    public static void showScan(final Activity activity) {
        showCaptureActivity(activity, CommonConst.REQUEST_CODE_SCAN, CommonConst.MODE_QR_CODE);
    }

    public static void showTakePicture(final Activity activity) {
        showCaptureActivity(activity, CommonConst.REQUEST_CODE_SCAN, CommonConst.MODE_TAKE_PICTURE);
    }

    private static void showCaptureActivity(final Context context, final int requestCode, final int mode) {
        final Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.EXTRA_CAPTURE_MODE, mode);
        try {
            Router.build("CaptureActivity").requestCode(requestCode).with(bundle).go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
