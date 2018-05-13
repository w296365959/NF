package com.sscf.investment.web.sdk.manager;

import android.content.Context;
import com.dengtacj.component.managers.IX5WebViewManager;
import com.sscf.investment.sdk.utils.DtLog;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

/**
 * Created by davidwei on 2017/02/27.
 */
public final class X5WebViewManager implements IX5WebViewManager, QbSdk.PreInitCallback, TbsListener {
    private static final String TAG = X5WebViewManager.class.getSimpleName();
    private boolean isX5Inited = false;

    @Override
    public void init(final Context context) {
        DtLog.d(TAG,"init");
        QbSdk.setTbsListener(this);
        try {
            QbSdk.initX5Environment(context,  this);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isX5Inited() {
        return isX5Inited;
    }

    @Override
    public void onViewInitFinished(boolean arg0) {
        DtLog.e(TAG, " onViewInitFinished is " + arg0);
        isX5Inited = true;
    }

    @Override
    public void onCoreInitFinished() {
        DtLog.e(TAG, " onCoreInitFinished");
    }

    @Override
    public void onDownloadFinish(int i) {
        DtLog.d(TAG,"onDownloadFinish");
    }

    @Override
    public void onInstallFinish(int i) {
        DtLog.d(TAG,"onInstallFinish");
    }

    @Override
    public void onDownloadProgress(int i) {
        DtLog.d(TAG,"onDownloadProgress : " + i);
    }
}
