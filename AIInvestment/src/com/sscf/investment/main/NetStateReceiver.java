package com.sscf.investment.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.sscf.investment.main.manager.AppConfigRequestManager;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.push.UmengPushManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;

/**
 * Created by liqf on 2015/10/12.
 */
public class NetStateReceiver extends BroadcastReceiver implements DataSourceProxy.IRequestCallback {

    private static final String TAG = NetStateReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        DtLog.d(TAG, "onReceive: action = " + action);

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
            // 收到网络变化通知
            // 是关闭还是打开网络
            boolean down = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (!down) {
                TradingStateManager tradingStateManager = dengtaApplication.getTradingStateManager();
                tradingStateManager.loadTradingTimeData();
            }

            // 上报友盟的tag
            if (NetUtil.isNetWorkConnected(context)) {
                dengtaApplication.defaultExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        UmengPushManager.initUmengTag();
                        AppConfigRequestManager.reportUserInfoRequest(dengtaApplication.getAccountManager().getUserInfo(), NetStateReceiver.this);
                    }
                });
            }
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
    }
}
