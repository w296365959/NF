package com.sscf.investment.component.ui.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.sscf.investment.sdk.utils.CommonConst;

final class BaseSwitchThemeBroadcastReceiver extends BroadcastReceiver {
    private final BaseActivity mActivity;

    BaseSwitchThemeBroadcastReceiver(final BaseFragmentActivity activity) {
        this.mActivity = activity;
    }

    void registerSwitchThemeReceiver() {
        LocalBroadcastManager.getInstance(mActivity.getApplicationContext())
                .registerReceiver(this, new IntentFilter(CommonConst.ACTION_SWITCH_THEME));
    }

    void unregisterSwitchThemeReceiver() {
        LocalBroadcastManager.getInstance(mActivity.getApplicationContext()).unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (CommonConst.ACTION_SWITCH_THEME.equals(action)) {
            final BaseActivity activity = this.mActivity;
            if (!activity.isDestroy()) {
                activity.recreate();
            }
        }
    }
}
