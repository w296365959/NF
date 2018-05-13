package com.dengtacj.component.managers;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by davidwei on 2017-09-04.
 */

public interface IScanManager {
    void handleResult(Activity activity, Intent intent);
}
