package com.sscf.investment.setting.widgt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;

public class BaseLogoutActivity extends BaseFragmentActivity {

    private LogoutBroadcastReceiver mLogoutReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLogoutReceiver = new LogoutBroadcastReceiver(this);
        mLogoutReceiver.registerLogoutThemeReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLogoutReceiver.unregisterLogoutThemeReceiver();
        mLogoutReceiver = null;
    }

}

final class LogoutBroadcastReceiver extends BroadcastReceiver {
    private BaseLogoutActivity mActivity;

    public LogoutBroadcastReceiver(final BaseLogoutActivity activity) {
        this.mActivity = activity;
    }

    public void registerLogoutThemeReceiver() {
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication())
                .registerReceiver(this, new IntentFilter(SettingConst.ACTION_LOGOUT_SUCCESS));
    }

    public void unregisterLogoutThemeReceiver() {
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (SettingConst.ACTION_LOGOUT_SUCCESS.equals(action)) {
            final BaseLogoutActivity activity = this.mActivity;
            if (activity == null || activity.isDestroy()) {
                return;
            }
            activity.finish();
        }
    }
}
