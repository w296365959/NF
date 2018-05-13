package com.sscf.investment.setting.widgt;

import android.content.Context;
import android.content.SharedPreferences;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.FileUtil;

/**
 * Created by liqf on 2015/12/10.
 */
public final class DeveloperSettingPref {
    public static final String DEVELOPER_SETTINGS = "developer_settings";

    /**
     * 是否显示开发者选项
     */
    public static final String KEY_SHOW_DEVELOPER_SETTINGS = "showDeveloperSettings";
    public static final boolean DEFAULT_SHOW_DEVELOPER_SETTINGS = false;

    /**
     * 切换服务器类型
     */
    public static final String KEY_SERVER_TYPE = "serverType";
    public static final String DEFAULT_SERVER_TYPE = "0";//defaultservertype为1的时候是test环境，需改为0代表线上

    /**
     * 是否使用本地H5资源包
     */
    public static final String KEY_USE_H5_LOCAL_CACHE = "useH5LocalCache";
    public static final boolean DEFAULT_USE_H5_LOCAL_CACHE = true;

    /**
     * 是否把日志输出到文件
     */
    public static final String KEY_SAVE_LOG_TO_FILE = "saveLogToFile";
    public static final boolean DEFAULT_SAVE_LOG_TO_FILE = false;

    /**
     * 上报日志
     */
    public static final String KEY_UPLOAD_LOG = "uploadLog";
    /**
     * 上报ANR日志
     */
    public static final String KEY_UPLOAD_ANR = "uploadANR";
    /**
     * 测试跳转地址
     */
    public static final String KEY_TEST_JUMP = "testJump";

    /**
     * 服务器IP地址
     */
    public static final String KEY_SERVER_IP = "serverIP";

    /**
     * GUID
     */
    public static final String KEY_GUID = "guid";

    /**
     * IMEI
     */
    public static final String KEY_IMEI = "imei";

    /**
     * DTID
     */
    public static final String KEY_DTID = "dtid";

    /**
     * DUA
     */
    public static final String KEY_DUA = "dua";

    /**
     * device token
     */
    public static final String KEY_DEVICE_TOKEN = "deviceToken";

    public static boolean getShowDeveloperSettings() {
        return getSharedPreferences().getBoolean(KEY_SHOW_DEVELOPER_SETTINGS, DEFAULT_SHOW_DEVELOPER_SETTINGS);
    }

    public static void setShowDeveloperSettings(final boolean show) {
        getSharedPreferences().edit().putBoolean(KEY_SHOW_DEVELOPER_SETTINGS, show).commit();
    }

    public static SharedPreferences getSharedPreferences() {
        return DengtaApplication.getApplication().getSharedPreferences(DEVELOPER_SETTINGS, Context.MODE_MULTI_PROCESS);
    }

    public static String getServerType() {
        return getSharedPreferences().getString(KEY_SERVER_TYPE, DEFAULT_SERVER_TYPE);
    }

    public static void setServerType(final int serverType) {
        getSharedPreferences().edit().putInt(KEY_SERVER_TYPE, serverType).commit();
    }

    public static boolean getUseH5LocalCache() {
        return getSharedPreferences().getBoolean(KEY_USE_H5_LOCAL_CACHE, DEFAULT_USE_H5_LOCAL_CACHE);
    }

    public static void setUseH5LocalCache(final boolean use) {
        getSharedPreferences().edit().putBoolean(KEY_USE_H5_LOCAL_CACHE, use).commit();
    }

    public static boolean getSaveLogToFile() {
        return getSharedPreferences().getBoolean(KEY_SAVE_LOG_TO_FILE, DEFAULT_SAVE_LOG_TO_FILE);
    }

    public static String getServerIP() {
        return getSharedPreferences().getString(KEY_SERVER_IP, "");
    }

    public static void setServerIP(final String serverIP) {
        getSharedPreferences().edit().putString(KEY_SERVER_IP, serverIP).commit();
    }

    public static String getDeveloperSettingsPrefPath() {
        return FileUtil.getPackageDataPath() + "/shared_prefs/" + DEVELOPER_SETTINGS + ".xml";
    }
}
