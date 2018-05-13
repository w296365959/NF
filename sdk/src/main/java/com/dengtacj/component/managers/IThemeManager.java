package com.dengtacj.component.managers;

import android.content.Context;

/**
 * Created by davidwei on 2017-09-05.
 */

public interface IThemeManager {
    void init(int defaultStyle, int nightStyle);
    boolean isDefaultTheme();
    boolean isNightTheme();
    void switchTheme();
    void setActivityTheme(Context context, int defaultStyle, int nightStyle);
    void recoverNightModeConfiguration(Context context);
}
