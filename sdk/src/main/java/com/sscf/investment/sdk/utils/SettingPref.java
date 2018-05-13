package com.sscf.investment.sdk.utils;

import java.util.Set;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.sscf.investment.sdk.SDKManager;

/**
 * @author ilock
 *
 */
public class SettingPref {
	private static final String LOCK_PREF_NAME = "dengta_setting_pref";
	public static final int DEFAULT_DELAY_TIME = 5000;

	private static SharedPreferences getSharedPreferences() {
		return SDKManager.getInstance().getContext().getSharedPreferences(LOCK_PREF_NAME, Context.MODE_MULTI_PROCESS);
	}

	public static void putInt(String key, int value) {
		Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
	}

	public static void putLong(String key, long value) {
		Editor editor = getSharedPreferences().edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static void putFloat(String key, float value) {
		Editor editor = getSharedPreferences().edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public static void putString(String key, String value) {
		Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
	}

	public static void putStringSet(String key, Set<String> set) {
		Editor editor = getSharedPreferences().edit();
        editor.putStringSet(key, set);
        editor.commit();
	}

	public static void putBoolean(String key, boolean value) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return getSharedPreferences().getBoolean(key, defValue);
	}

	public static void putIBoolean(String key, boolean value) {
		putInt(key, value ? 1 : 0);
	}

	public static boolean getIBoolean(String key, boolean defValue) {
		return getInt(key, defValue ? 1 : 0) == 1;
	}

	public static long getLong(String key, long defValue) {
		return getSharedPreferences().getLong(key, defValue);
	}

	public static int getInt(String key, int defValue) {
		return getSharedPreferences().getInt(key, defValue);
	}

	public static float getFloat(String key, float defValue) {
		return getSharedPreferences().getFloat(key, defValue);
	}
	
	public static String getString(String key, String defValue) {
		return getSharedPreferences().getString(key, defValue);
	}
	
	public static Set<String> getStringSet(String key) {
		return getSharedPreferences().getStringSet(key, null);
	}
}
