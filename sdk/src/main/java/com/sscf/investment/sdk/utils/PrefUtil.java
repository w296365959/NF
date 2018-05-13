package com.sscf.investment.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.sscf.investment.sdk.SDKManager;

/**
 * Created by liqf on 2015/12/10.
 */
public class PrefUtil {
    public static final String DENGTACJ_STOCK_SDK_PREF = "dengtacj_stock_sdk_pref";

    /**
     * GUID
     */
    public static final String KEY_GUID = "KEY_GUID";


    public static SharedPreferences getSharedPreferences() {
        return SDKManager.getInstance().getContext().getSharedPreferences(DENGTACJ_STOCK_SDK_PREF, Context.MODE_MULTI_PROCESS);
    }

    public static boolean putString(String key, String value) {
        return getSharedPreferences().edit().putString(key, value).commit();
    }

    public static boolean putBoolean(String key, Boolean value) {
        return getSharedPreferences().edit().putBoolean(key, value).commit();
    }

    public static boolean putInt(String key, int value) {
        return getSharedPreferences().edit().putInt(key, value).commit();
    }

    public static String getString(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    public static boolean getBoolean(String key, Boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }
}
