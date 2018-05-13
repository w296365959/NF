package com.sscf.investment.main;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IIntelligentShakeManager;
import com.dengtacj.component.managers.IScreenShotManager;
import com.sscf.investment.component.ui.widget.BaseActivity;
import com.sscf.investment.sdk.utils.DtLog;

/**
 * Created by davidwei on 2017-09-25.
 */

public final class DengtaActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks{
    private static final String TAG = DengtaActivityLifecycleCallbacks.class.getSimpleName();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        DtLog.d(TAG, "onActivityCreated : activity = " + activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        DtLog.d(TAG, "onActivityDestroyed : activity = " + activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        DtLog.d(TAG, "onActivityResumed : activity = " + activity);
        if (activity instanceof BaseActivity) {
            startScreenShotListen(activity); // 截屏分享
        }
        if (activity instanceof IIntelligentShakeManager.OnGetStockInfoCallback) {
            registerShakeListener(activity, (IIntelligentShakeManager.OnGetStockInfoCallback) activity);// 摇一摇
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        DtLog.d(TAG, "onActivityPaused : activity = " + activity);
        if (activity instanceof BaseActivity) {
            stopScreenShotListen(); // 截屏分享
        }
        if (activity instanceof IIntelligentShakeManager.OnGetStockInfoCallback) {
            unregisterShakeListener(); // 摇一摇
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        DtLog.d(TAG, "onActivityStarted : activity = " + activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        DtLog.d(TAG, "onActivityStopped : activity = " + activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        DtLog.d(TAG, "onActivitySaveInstanceState : activity = " + activity);
    }

    private void startScreenShotListen(final Activity activity) {
        final IScreenShotManager screenShotManager = (IScreenShotManager) ComponentManager.getInstance()
                .getManager(IScreenShotManager.class.getName());
        if (screenShotManager != null) {
            screenShotManager.startListen(activity);
        }
    }

    private void stopScreenShotListen() {
        final IScreenShotManager screenShotManager = (IScreenShotManager) ComponentManager.getInstance()
                .getManager(IScreenShotManager.class.getName());
        if (screenShotManager != null) {
            screenShotManager.stopListen();
        }
    }

    private void registerShakeListener(final Activity activity, IIntelligentShakeManager.OnGetStockInfoCallback callback) {
        if (callback.isShakeEnable()) {
            final IIntelligentShakeManager shakeManager = (IIntelligentShakeManager) ComponentManager.getInstance()
                    .getManager(IIntelligentShakeManager.class.getName());
            if (shakeManager != null && shakeManager.isEnable()) {
                shakeManager.registerShakeListener(activity, callback);
            }
        }
    }

    private void unregisterShakeListener() {
        final IIntelligentShakeManager shakeManager = (IIntelligentShakeManager) ComponentManager.getInstance()
                .getManager(IIntelligentShakeManager.class.getName());
        if (shakeManager != null) {
            shakeManager.unregisterShakeListener();
        }
    }
}
