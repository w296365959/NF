package com.sscf.investment.component.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by xuebinliu on 08/04/2017.
 */
public class EmptyReceiver2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(KeepAlive.TAG, "EmptyReceiver2 onReceive action=" + intent.getAction());
    }
}
