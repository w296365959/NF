package com.sscf.investment.sdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.lang.reflect.Method;

/**
 * Created by free on 11/1/16.
 */
public class SDKUtil {
    public static final String TAG = SDKUtil.class.getSimpleName();

    public static String getDtSecCode(String code) {
        return null;
    }



    /**
     * 获取屏幕宽度
     * @return px
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = SDKManager.getInstance().getContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     * @return px
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = SDKManager.getInstance().getContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取手机imei
     * @param context
     * @return
     */
    public static String getImei(Context context){
        String imei = "";
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephonyManager != null) {
                imei = mTelephonyManager.getDeviceId();
                if (TextUtils.isEmpty(imei)) {
                    imei = getSimImei();
                }
            }
            if (imei != null) {
                imei = imei.toLowerCase();
            }
        } catch (Exception e) {
        }
        return imei;
    }

    private static String getSimImei() {
        Class<?> clazz;
        try {
            clazz = Class.forName("android.telephony.TelephonyManager");
            Method method = clazz.getDeclaredMethod("getSecondary", Object.class);
            method.setAccessible(true);
            TelephonyManager telManager = (TelephonyManager) method.invoke(clazz);
            if (telManager != null) {
                return telManager.getDeviceId();
            }
        } catch (Throwable e) {
        }
        return null;
    }

    /**
     * 获取Application标签内的meta data
     */
    public static String getUMengChannelID() {
        String channel = "sdk";
        // 正常发布版本，渠道号是数字，在这里读到
        ApplicationInfo appInfo;
        try {
            Context context = SDKManager.getInstance().getContext();
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return channel;
        }

        try {
            int ch = appInfo.metaData.getInt("UMENG_CHANNEL");
            if (ch == 0) {
                channel = appInfo.metaData.getString("UMENG_CHANNEL");
            } else {
                channel = String.valueOf(ch);
            }
        } catch (Exception e) {
        }

        return channel;
    }
}
