package com.dengtacj.component.managers;

import android.app.Activity;

/**
 * Created by davidwei on 2017-09-04.
 */

public interface IScreenShotManager {
    void init();
    void startListen(Activity activity);
    void stopListen();
}
