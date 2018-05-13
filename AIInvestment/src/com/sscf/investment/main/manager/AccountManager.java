package com.sscf.investment.main.manager;

import BEC.AccuPointTaskType;
import BEC.E_DEVICE_TOKEN_TYPE;
import BEC.E_DT_MEMBER_TYPE;
import BEC.FeedUserBaseInfo;
import BEC.GenGuidRsp;
import BEC.UpdateTicketRsp;
import BEC.UserInfo;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.dengtacj.component.managers.IAccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.MainActivity;
import com.sscf.investment.push.UmengPushManager;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.message.manager.MessageCenterManager;
import com.sscf.investment.sdk.net.IPManager;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.*;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.sscf.investment.setting.manager.AccountRequestManager;
import com.sscf.investment.setting.manager.RedDotManager;
import com.sscf.investment.socialize.UmengSocialSDKUtils;
import com.sscf.investment.utils.*;
import com.umeng.message.PushAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuebinliu on 2015/8/13.
 */
public final class AccountManager implements DataSourceProxy.IRequestCallback, IAccountManager {
    private static final String TAG = "AccountManager";

    public static final String KEY_CHANNEL_ID = "UMENG_CHANNEL";  // 渠道号
    private static final String DUA_LCID = "";
    private static final String DUA_VERSION_BUILD = "0";
    private static final String DUA_VERSION_TYPE = "_GA";   // 版本类型 DD、DR、B、GA

    private AccountInfoExt mAccountInfo;

    private UserInfo mUserInfo;

    // guid，16个字节的字节串
    private byte[] mGuid = null;

    private String mGuidString = "";

    // dua
    private String mDUA;

    private boolean mInitGuiding;
    private boolean mUmengReport;

    private MainActivity mMainActivity;

    public AccountManager() {
        DtLog.d(TAG, "AccountManager constructor");
        init();
        DtLog.d(TAG, "AccountManager constructor complete");
    }

    private void init() {
        DtLog.d(TAG, "loadAccountInfo");
        initAccountInfo();

        initGuid();

        // init dua
        getDUA();
    }

    private void initAccountInfo() {
        final File accountInfoFile = FileUtil.getAccountInfoFile(DengtaApplication.getApplication());
        DtLog.d(TAG, "loadAccountInfo accountInfoFile : " + accountInfoFile.exists());
        final Object o = FileUtil.getObjectFromFile(accountInfoFile);

        AccountInfoExt accountInfoExt = null;
        AccountInfoEntity accountInfoEntity = null;
        if (o != null) {
            if (o instanceof AccountInfoExt) {
                accountInfoExt = (AccountInfoExt) o;
                accountInfoEntity = accountInfoExt.accountInfo;
            } else if (o instanceof AccountInfoEntity) { // 兼容老版本
                accountInfoExt = new AccountInfoExt();
                accountInfoEntity = (AccountInfoEntity) o;
                accountInfoExt.accountInfo = accountInfoEntity;
            }
        }

        if (accountInfoEntity != null && accountInfoEntity.id > 0 && accountInfoEntity.ticket != null) {
            DtLog.d(TAG, "loadAccountInfo accountInfo.id : " + accountInfoEntity.id);
            mAccountInfo = accountInfoExt;
        } else {
            accountInfoFile.delete();
        }
    }

    public void setMainActivity(MainActivity mMainActivity) {
        this.mMainActivity = mMainActivity;
    }

    /**
     * DUA示例
     * SN=ADRCJPH10_GA&VN=10141225&BN=220&VC=HTC&MO=M8T&RL=1080_1920&CHID=10000_10000&LCID=1200&RV=&OS=Android4.4.2&DV=V1
     * @return
     */
    public String getDUA() {
        if (mDUA == null) {
            final DengtaApplication application = DengtaApplication.getApplication();
            final StringBuilder stringBuilder = new StringBuilder();
            final String versionCode = String.valueOf(PackageUtil.getVersionCode(application));
            String duaVersionMain = null;
            if (versionCode.length() >= 2) {
                duaVersionMain = versionCode.substring(0,2);
            } else {
                duaVersionMain = versionCode;
            }
            stringBuilder.append("SN=").append("ADR" + "CJ" + "PH").append(duaVersionMain).append(DUA_VERSION_TYPE);
            stringBuilder.append("&VN=").append(versionCode);
            stringBuilder.append("&BN=").append(DUA_VERSION_BUILD);              // db编号
            stringBuilder.append("&VC=").append(Build.MANUFACTURER);  // 厂商名
            stringBuilder.append("&MO=").append(Build.MODEL);         // 设备名
            stringBuilder.append("&RL=").append(DeviceUtil.getScreenWidth(application)).
                    append("_").append(DeviceUtil.getScreenHeight(application));  // 屏幕分辨率
            stringBuilder.append("&CHID=").append(
                    DengtaApplication.getChannelIDFromManifest() + "_" +
                    SettingPref.getString(SettingConst.KEY_FIRST_SETUP_CHANNEL_ID, ""));    // 渠道号
            stringBuilder.append("&LCID=").append(DUA_LCID);             // LCID
            stringBuilder.append("&RV=");                     // 保留
            stringBuilder.append("&OS=").append("Android" + Build.VERSION.RELEASE);      // 系统版本
            stringBuilder.append("&DV=").append("V1");

            mDUA = stringBuilder.toString();
            DtLog.d(TAG, "DUA=" + mDUA);
        }
        return mDUA;
    }

    /**
     * @param guid
     * 保存guid信息
     */
    public void saveGuid(byte[] guid) {
        if (guid != null && guid.length == 16) {
            mGuid = guid;
            String guidString = DataUtils.bytesToHexString(guid);
            mGuidString = guidString;
            DtLog.d(TAG, "save guid string=" + guidString);
            if (mUserInfo != null) {
                mUserInfo.setVGUID(mGuid);
            }
            SettingPref.putString(SettingConst.SETTING_APP_GUID, guidString);
        }
    }

    /**
     * 获取guid信息
     * @return
     */
    public byte[] getGuid() {
        if (mGuid == null) {
            // 发起guid拉取
            initGuid();
        }
        return mGuid;
    }

    public String getGuidString() {
        if (mGuid == null) {
            // 发起guid拉取
            initGuid();
        }
        return mGuidString;
    }

    /**
     * 后台拉取guid
     */
    public void initGuid() {
        if (mInitGuiding) {
            return;
        }

        mInitGuiding = true;
        DtLog.d(TAG, "init guid");

        // 读取guid
        String guidString = SettingPref.getString(SettingConst.SETTING_APP_GUID, "");
        if (!TextUtils.isEmpty(guidString)) {
            mGuid = DataUtils.hexStringToBytes(guidString);
            mGuidString = guidString;
            DtLog.d(TAG, "init guid string=" + guidString);
        }

        mUmengReport = TextUtils.isEmpty(PushAgent.getInstance(DengtaApplication.getApplication()).getRegistrationId());
        final UserInfo userInfo = getUserInfo();
        AppConfigRequestManager.genGuidRequest(userInfo, this);
    }

    public boolean isUmengReport() {
        return mUmengReport;
    }

    public AccountInfoEntity getAccountInfo() {
        return mAccountInfo == null ? null : mAccountInfo.accountInfo;
    }

    public AccountInfoExt getAccountInfoExt() {
        return mAccountInfo;
    }

    public boolean isMember() {
        if(mAccountInfo != null) {
            return mAccountInfo.isMember();
        }
        return false;
    }

    public boolean isExpired() {
        if(mAccountInfo != null) {
            return mAccountInfo.isExpired();
        }
        return false;
    }

    public long getAccountId() {
        final AccountInfoEntity accountInfo = getAccountInfo();
        return accountInfo == null ? 0 : accountInfo.id;
    }

    public UserInfo getUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = new UserInfo();
            mUserInfo.setSDUA(getDUA());
            mUserInfo.setSIMEI(DeviceUtil.getImei(DengtaApplication.getApplication()));
            mUserInfo.setSPackageName(DengtaApplication.getApplication().getPackageName());
        }

        if (mGuid == null || mGuid.length < 1) {
            // 避免guid为null的时候会crash的问题
            mUserInfo.vGUID = new byte[0];
        } else {
            mUserInfo.vGUID = mGuid;
        }

        long accountId = 0;
        final AccountInfoEntity accountInfo = getAccountInfo();
        int memberType = E_DT_MEMBER_TYPE.E_DT_MEMBER;
        if (accountInfo != null) {
            accountId = accountInfo.id;
            memberType = getAccountInfoExt().memberType;
        }
        mUserInfo.iAccountId = accountId;
        mUserInfo.iMember = memberType;

        setDeviceTokens();

        // 供组件使用
        final SDKManager sdkManager = SDKManager.getInstance();
        sdkManager.setUserInfo(mUserInfo);
        sdkManager.setGuid(getGuidString());

        return mUserInfo;
    }

    private void setDeviceTokens() {
        Map<Integer, String> deviceTokens = mUserInfo.mpDeviceTokens;
        if (deviceTokens == null) {
            deviceTokens = new HashMap<Integer, String>(2);
            mUserInfo.mpDeviceTokens = deviceTokens;
        }

        mUserInfo.sTag = UmengPushManager.PUSH_VERSION;

        String umengDeviceToken = deviceTokens.get(E_DEVICE_TOKEN_TYPE.E_DTT_UMENG);
        if (TextUtils.isEmpty(umengDeviceToken)) {
            umengDeviceToken = PushAgent.getInstance(DengtaApplication.getApplication()).getRegistrationId();
            if (umengDeviceToken == null) {
                umengDeviceToken = "";
            }
            deviceTokens.put(E_DEVICE_TOKEN_TYPE.E_DTT_UMENG, umengDeviceToken);
        }
        DtLog.d(TAG, "report umeng push deviceTokens=" + umengDeviceToken);
    }

    /**
     * 判断用户是否登录了
     * @return
     */
    public boolean isLogined() {
        return mAccountInfo != null;
    }

    /**
     * 用户账户信息保存到本地，非ui线程调用
     */
    public void saveAccountInfo(final AccountInfoExt accountInfo) {
        DtLog.d(TAG, "saveAccountInfo");
        mAccountInfo = accountInfo;

        LocalBroadcastManager.getInstance(DengtaApplication.getApplication())
                .sendBroadcast(new Intent(SettingConst.ACTION_LOGIN_SUCCESS));

        saveAccountInfoFile(accountInfo);

        onLogin();
    }

    public void saveAccountInfoFile(final AccountInfoExt accountInfo) {
        final File accountInfoFile = FileUtil.getAccountInfoFile(DengtaApplication.getApplication());
        FileUtil.saveObjectToFile(accountInfo, accountInfoFile);
    }

    private void onLogin() {
        UmengPushManager.initUmengTag();
        AppConfigRequestManager.reportUserInfoRequest(getUserInfo(), this);

        final MessageCenterManager messageCenterManager = DengtaApplication.getApplication().getMessageCenterManager();
        messageCenterManager.reset();
        messageCenterManager.requestMessageCenter();
        DengtaApplication.getApplication().getBonusPointManager().reportFinishedTask(AccuPointTaskType.E_ACCU_POINT_TASK_DAILY_SIGN);
    }

    /**
     * 规则
     * accountId|env_formal|version
     */
    public static String getUmengAlias(long accountId, int version) {
        final StringBuilder alias = new StringBuilder();
        alias.append(accountId).append('|');
        if (IPManager.getInstance().isTest()) {
            alias.append(DengtaConst.ALIAS_ENV_LOCAL);
        } else {
            alias.append(DengtaConst.ALIAS_ENV_FORMAL);
        }
        alias.append('|').append(version);
        return alias.toString();
    }

    /**
     * 用户账户信息保存到本地，非ui线程调用
     */
    public void updateLocalAccountInfo(final AccountInfoExt accountInfo) {
        DtLog.d(TAG, "updateLocalAccountInfo");
        mAccountInfo = accountInfo;

        LocalBroadcastManager.getInstance(DengtaApplication.getApplication())
                .sendBroadcast(new Intent(SettingConst.ACTION_UPDATE_ACCOUNT_INFO));

        final File accountInfoFile = FileUtil.getAccountInfoFile(DengtaApplication.getApplication());
        FileUtil.saveObjectToFile(accountInfo, accountInfoFile);
    }

    /**
     * 删除本地用户信息，非ui线程调用
     */
    public void removeAccountInfo() {
        DtLog.d(TAG, "removeAccountInfo");
        synchronized (this) {
            if (mAccountInfo == null) {
                return;
            }
            mAccountInfo = null;
        }

        LocalBroadcastManager.getInstance(DengtaApplication.getApplication())
                .sendBroadcast(new Intent(SettingConst.ACTION_LOGOUT_SUCCESS));

        final File accountInfoFile = FileUtil.getAccountInfoFile(DengtaApplication.getApplication());
        if (accountInfoFile.isFile()) {
            accountInfoFile.delete();
        }

        onLogout();
    }

    private void onLogout() {
        final MainActivity mainActivity = mMainActivity;
        if (mainActivity != null && !mainActivity.isDestroy()) {
            UmengSocialSDKUtils.removeAccount(mainActivity, SHARE_MEDIA.WEIXIN);
            UmengSocialSDKUtils.removeAccount(mainActivity, SHARE_MEDIA.QQ);
            UmengSocialSDKUtils.removeAccount(mainActivity, SHARE_MEDIA.SINA);
        }

        // 去掉提醒的红点
        final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
        redDotManager.setPortfolioLiveState(false);

        UmengPushManager.initUmengTag();
        AppConfigRequestManager.reportUserInfoRequest(getUserInfo(), this);

        final MessageCenterManager messageCenterManager = DengtaApplication.getApplication().getMessageCenterManager();
        messageCenterManager.reset();
        messageCenterManager.requestMessageCenter();
        // 注销后，清除掉当前缓存的积分信息
        DengtaApplication.getApplication().getBonusPointManager().clear();
    }

    @Override
    public void callback(final boolean success, final EntityObject data) {
        if (data == null) {
            return;
        }

        switch (data.getEntityType()) {
            case EntityObject.ET_GUID:
                getGuidCallback(success, data);
                break;
            case EntityObject.ET_ACCOUNT_UPDATE_TICKET:
                updateTicketCallback(success, data);
                break;
            case EntityObject.ET_REPORT_USER_INFO:
            default:
                break;
        }
    }

    public void getGuidCallback(final boolean success, final EntityObject data) {
        mInitGuiding = false;
        if (success) {
            DtLog.d(TAG, "initGuid callback, data=" + data.toString());
            GenGuidRsp genGuidRsp = (GenGuidRsp) data.getEntity();
            saveGuid(genGuidRsp.getVGuid());

            AppConfigRequestManager.reportUserInfoRequest(getUserInfo(), AccountManager.this);
        } else {
            DtLog.d(TAG, "get guid requestBriefInfo failed");
        }
    }

    public void updateTicketCallback(final boolean success, final EntityObject data) {
        if (success && data.getEntity() != null) {
            final UpdateTicketRsp rsp = (UpdateTicketRsp) data.getEntity();
            final AccountInfoExt accountInfoEntity = AccountRequestManager.parseAccountInfo(rsp.stAccountTicket.vtTicket,
                    rsp.stAccountInfo, rsp.vtThirdLoginInfo);
            mAccountInfo = accountInfoEntity;
            saveAccountInfoFile(accountInfoEntity);
        }
    }

    /**
     * 更新ticket和其他用户信息
     */
    @Override
    public void updateAccountInfoFromWeb() {
        final AccountInfoEntity accountInfo = getAccountInfo();
        if (accountInfo != null) {
            AccountRequestManager.updateTicketRequest(accountInfo.ticket, accountInfo.id, getUserInfo(), this);
        }
    }

    public static boolean isMember(FeedUserBaseInfo userBaseInfo) {
        if(userBaseInfo != null && userBaseInfo.getStMember() != null) {
            return userBaseInfo.getStMember().getIMemberType() == E_DT_MEMBER_TYPE.E_DT_MEMBER;
        }
        return false;
    }
}
