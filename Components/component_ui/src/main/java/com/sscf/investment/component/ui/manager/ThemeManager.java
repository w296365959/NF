package com.sscf.investment.component.ui.manager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatDelegate;
import com.dengtacj.component.managers.IThemeManager;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.SettingPref;

/**
 * Created by davidwei on 2017-09-05.
 */
public final class ThemeManager implements IThemeManager {
    private static final String TAG = ThemeManager.class.getSimpleName();

    private static final String KEY_SETTING_THEME = "setting_theme";
    private static final int THEME_NORMAL = 0;
    private static final int THEME_NIGHT = 1;
    private static final int THEME_DEFAULT = THEME_NIGHT;
    private int mDefaultStyle;
    private int mNightStyle;

    private int mTheme = -1;

    @Override
    public void init(int defaultStyle, int nightStyle) {
        mDefaultStyle = defaultStyle;
        mNightStyle = nightStyle;
        switchTheme(getTheme());
    }

    private int getTheme() {
        if (mTheme == -1) {
            mTheme = SettingPref.getInt(KEY_SETTING_THEME, THEME_DEFAULT);
        }
        return mTheme;
    }

    @Override
    public boolean isDefaultTheme() {
        return getTheme() == THEME_NORMAL;
    }

    @Override
    public boolean isNightTheme() {
        return getTheme() == THEME_NIGHT;
    }

    /**
     * google官方bug，暂时解决方案 * 手机切屏后重新设置UI_MODE
     * 模式（因为在DayNight主题下，切换横屏后UI_MODE会出错，会导致
     * 资源获取出错，需要重新设置回来）
     */
    @Override
    public void recoverNightModeConfiguration(final Context context) {
        DtLog.d(TAG, "recoverNightModeConfiguration");
        final int uiNightMode = isNightTheme() ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO;
        Configuration config = context.getResources().getConfiguration();
        config.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        config.uiMode |= uiNightMode;
        context.getResources().updateConfiguration(config, null);
    }

    @Override
    public void switchTheme() {
        DtLog.d(TAG, "switchTheme");
        //        context.setTheme(getTheme(getThemeResourceIds()));
        int theme;
        if (isDefaultTheme()) {
            theme = THEME_NIGHT;
        } else {
            theme = THEME_NORMAL;
        }
        SettingPref.putInt(KEY_SETTING_THEME, theme);
        switchTheme(theme);
        mTheme = theme;
        LocalBroadcastManager.getInstance(SDKManager.getInstance().getContext())
                .sendBroadcast(new Intent(CommonConst.ACTION_SWITCH_THEME));
    }

    @Override
    public void setActivityTheme(Context context, int defaultStyle, int nightStyle) {
        if (defaultStyle == 0) {
            defaultStyle = mDefaultStyle;
        }
        if (nightStyle == 0) {
            nightStyle = mNightStyle;
        }
        final boolean isDefaultTheme = isDefaultTheme();
        context.setTheme(isDefaultTheme ? defaultStyle : nightStyle);
        DtLog.d(TAG, "setTheme : context = " + context + " , isDefaultTheme = " + isDefaultTheme + " , defaultStyle = " + defaultStyle);
    }

    private void switchTheme(int theme) {
        if (theme == THEME_NORMAL) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (theme == THEME_NIGHT) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
}
