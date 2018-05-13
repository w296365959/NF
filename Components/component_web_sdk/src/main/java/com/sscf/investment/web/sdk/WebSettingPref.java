package com.sscf.investment.web.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.sscf.investment.sdk.SDKManager;

/**
 * @author davidwei
 */
public final class WebSettingPref {
	private static final String LOCK_PREF_NAME = "dengta_setting_web_pref";

	private static SharedPreferences getSharedPreferences() {
		return SDKManager.getInstance().getContext().getSharedPreferences(LOCK_PREF_NAME, Context.MODE_PRIVATE);
	}

	public static void putString(String key, String value) {
		final Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
	}

	public static String getString(String key, String defValue) {
		return getSharedPreferences().getString(key, defValue);
	}

	public static void putIBoolean(String key, boolean value) {
		putInt(key, value ? 1 : 0);
	}

	public static boolean getIBoolean(String key, boolean defValue) {
		return getInt(key, defValue ? 1 : 0) == 1;
	}

	public static void putInt(String key, int value) {
		Editor editor = getSharedPreferences().edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getInt(String key, int defValue) {
		return getSharedPreferences().getInt(key, defValue);
	}
}
