package com.sscf.investment.component.keepalive;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by xuebinliu on 08/04/2017.
 */
public class EmptyService1 extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(KeepAlive.TAG, "EmptyService1 onBind");
        return null;
    }
}
