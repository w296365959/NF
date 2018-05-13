package com.sscf.investment.main.manager;

import BEC.UpgradeInfo;
import BEC.UpgradeReq;
import BEC.UpgradeRsp;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.sscf.investment.BuildConfig;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.PackageUtil;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.utils.StringUtil;

/**
 * Created by xuebinliu on 2015/8/21.
 *
 * 升级管理
 */
public final class UpgradeManager implements DataSourceProxy.IRequestCallback{
    public static final String TAG = UpgradeManager.class.getSimpleName();

    private UpgradeInfo mUpgradeInfo;

    private boolean mRequesting;

    public UpgradeManager() {
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        DtLog.d(TAG, "callback success=" + success);
        if (success) {
            if (data != null) {
                final UpgradeRsp upgradeRsp = (UpgradeRsp)data.getEntity();
                DtLog.d(TAG, "rsp=" + upgradeRsp);
                handleUpgrade(upgradeRsp);
            }
        }

        if (mUpgradeInfo == null){ // 没有新版本就去掉红点
            DengtaApplication.getApplication().getRedDotManager().setUpgradeState(false);
        }

        // 访问成功或失败都发广播
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(new Intent(SettingConst.ACTION_UPGRADE_INFO_CHANGED));
        mRequesting = false;
    }

    /**
     * 版本升级红点提示处理
     * 策略：
     * 1. 当前版本提示过一次就不在提示了
     * 2. 协议中有弹窗提示要求，则弹窗提示
     */
    private void handleUpgrade(UpgradeRsp upgradeRsp) {
        if (upgradeRsp != null) {
            final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
            final UpgradeInfo upgradeInfo = upgradeRsp.stUpgradeInfo;
            final int currentVersionCode = PackageUtil.getVersionCode(dengtaApplication);
            if (upgradeInfo.iVersion > currentVersionCode) {
                DtLog.d(TAG, "handleUpgrade new version=" + upgradeInfo.iVersion + ", oldversion=" + currentVersionCode);

                // 加入对于下载信息的检查，如果没有有效的下载信息，不认为有更新数据
                if(!checkDownloadInfo(upgradeInfo)) {
                    return;
                }

                switch (upgradeInfo.iStatus) {
                    case 0: // 不更新
                        dengtaApplication.getRedDotManager().setUpgradeState(false);
                        return;
                    case 1: // 提示更新
                        dengtaApplication.getRedDotManager().setUpgradeState(true);
                        if (upgradeInfo.isFreqControl == 0) {
                            LocalBroadcastManager.getInstance(dengtaApplication).sendBroadcast(new Intent(SettingConst.ACTION_UPGRADE_TIPS));
                        }
                        // 升级提示框
                        break;
                    case 2: // 强制升级
                        LocalBroadcastManager.getInstance(dengtaApplication).sendBroadcast(new Intent(SettingConst.ACTION_UPGRADE_TIPS));
                        dengtaApplication.getRedDotManager().setUpgradeState(true);
                        break;
                    case 3: // 检查更新
                        dengtaApplication.getRedDotManager().setUpgradeState(true);
                        break;
                    default:
                        return;
                }
                mUpgradeInfo = upgradeInfo;
            }
        }
    }

    private boolean checkDownloadInfo(UpgradeInfo upgradeInfo) {
        if(upgradeInfo != null) {
            final long fileLength = StringUtil.parseLong(upgradeInfo.sFileSize, 0);

            if(!TextUtils.isEmpty(upgradeInfo.getSURL())
                    && fileLength > 0
                    && !TextUtils.isEmpty(upgradeInfo.getSMd5())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取升级相关信息
     * @return
     */
    public UpgradeInfo getUpgradeInfo() {
        return mUpgradeInfo;
    }

    /**
     * 请求更新
     * @param checkFreq 是否检测频率,1:为是
     */
    public void reqUpdate(final int checkFreq) {
        if (mRequesting) {
            return;
        }

        mRequesting = true;

        UpgradeReq req = new UpgradeReq();
        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();
        req.sClientMd5 = "";
        req.iCheckFreq = checkFreq;
        if (req.stUserInfo.getVGUID() == null || req.stUserInfo.getVGUID().length < 1) {
            DtLog.e(TAG, "reqUpdate invalid guid");
            return;
        }

        int reqType = EntityObject.ET_UPGRADE;
        if (BuildConfig.BUILD_TYPE.equals("release")) {
            reqType = EntityObject.ET_UPGRADE_DB;
        }

        try {
            DataEngine.getInstance().request(reqType, req, this);
        } catch (Exception e) {
            DtLog.e(TAG, "reqUpdate Exception e=" + e.getMessage());
            e.printStackTrace();
        }
    }


}
