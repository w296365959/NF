package com.sscf.investment.component.ui.widget;

import android.content.Intent;
import android.os.Bundle;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IIntelligentShakeManager;
import com.dengtacj.component.managers.IScanManager;
import com.dengtacj.component.managers.IShareManager;
import com.dengtacj.component.managers.IThemeManager;
import com.sscf.investment.component.ui.R;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public class BaseFragmentActivity extends BaseActivity implements IIntelligentShakeManager.OnGetStockInfoCallback {
    private static final String TAG = BaseFragmentActivity.class.getSimpleName();

    private BaseSwitchThemeBroadcastReceiver mSwitchThemeReceiver;

    protected int getDefaultTheme() {
        return 0;
    }

    protected int getNightTheme() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DtLog.d(TAG, "onCreate");
        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        if (themeManager != null) {
            themeManager.setActivityTheme(this, getDefaultTheme(), getNightTheme());
        }

        super.onCreate(savedInstanceState);
        mSwitchThemeReceiver = new BaseSwitchThemeBroadcastReceiver(this);
        mSwitchThemeReceiver.registerSwitchThemeReceiver();

        DeviceUtil.setTaskDescription(this, getResources().getColor(R.color.actionbar_bg_color));

        // UMeng push如果不调用此方法，不仅会导致按照"几天不活跃"条件来推送失效，还将导致广播发送不成功以及设备描述红色等问题发生
        PushAgent.getInstance(getApplicationContext()).onAppStart();
    }

    @Override
    protected void onResume() {
        DtLog.d(TAG, "onResume");
        if(isRecoverNightModeConfigurationEnable()) {
            recoverNightModeConfiguration();
        }
        super.onResume();
        MobclickAgent.onResume(this);
    }

    protected boolean isRecoverNightModeConfigurationEnable() {
        return true;
    }

    private void recoverNightModeConfiguration() {
        DtLog.d(TAG, "onResume recoverNightModeConfiguration");
        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        if (themeManager != null) {
            themeManager.recoverNightModeConfiguration(this);
        }
    }

    @Override
    protected void onPause() {
        DtLog.d(TAG, "onPause");
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        DtLog.d(TAG, "onSaveInstanceState: outState = " + outState);
        super.onSaveInstanceState(outState);
    }

    /**
     * 千万要注意，此方法不能删除！！！！！！！！！！
     * 否则会出现NullPointerException
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        DtLog.d(TAG, "onRestoreInstanceState: savedInstanceState = " + savedInstanceState);
//        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        DtLog.d(TAG, "onDestroy");
        super.onDestroy();

        final IShareManager shareManager = (IShareManager) ComponentManager.getInstance().getManager(IShareManager.class.getName());
        if (shareManager != null) {
            shareManager.release(this);
        }

        mSwitchThemeReceiver.unregisterSwitchThemeReceiver();
        mSwitchThemeReceiver = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        recoverNightModeConfiguration();

        super.onActivityResult(requestCode, resultCode, data);
        // 第三方登录与分享的潜规则
        final IShareManager shareManager = (IShareManager) ComponentManager.getInstance().getManager(IShareManager.class.getName());
        if (shareManager != null) {
            shareManager.onActivityResult(this, requestCode, resultCode, data);
        }

        /// 处理扫描
        switch (requestCode) {
            case CommonConst.REQUEST_CODE_SCAN:
                final IScanManager scanManager = (IScanManager) ComponentManager.getInstance()
                        .getManager(IScanManager.class.getName());
                if (scanManager != null) {
                    scanManager.handleResult(this, data);
                }
                break;
            default:
                break;
        }
    }

    // -------------------------------------摇一摇相关功能---------------------------------------------
    /**
     * 是否开启摇一摇功能
     * @return
     */
    public boolean isShakeEnable() {
        return true;
    }

    /**
     * 传递给投顾界面的参数
     * @return
     */
    public String getDtCode() {
        return null;
    }

    /**
     * 传递给投顾界面的参数
     * @return
     */
    public String getSecName() {
        return null;
    }

    // -------------------------------------摇一摇相关功能---------------------------------------------
}
