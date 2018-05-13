package com.sscf.investment.main;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.*;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import com.chenenyu.router.Router;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IThemeManager;
import com.dengtacj.component.managers.IX5WebViewManager;
import com.sscf.investment.BuildConfig;
import com.sscf.investment.R;
import com.sscf.investment.bonus.BonusPointManager;
import com.sscf.investment.component.keepalive.KeepAlive;
import com.sscf.investment.component.ui.widget.CommonToast;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.manager.AdManager;
import com.sscf.investment.main.manager.DataCacheManager;
import com.sscf.investment.payment.OrderManager;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.main.manager.UpgradeManager;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.setting.manager.KLineSettingManager;
import com.sscf.investment.utils.BuildUtils;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.payment.PaymentInfoManager;
import com.sscf.investment.push.PushManager;
import com.sscf.investment.push.UmengPushManager;
import com.sscf.investment.sdk.ContextHolder;
import com.sscf.investment.sdk.download.DtDownloadManager;
import com.sscf.investment.sdk.net.*;
import com.sscf.investment.discover.manager.SubscriptionManager;
import com.sscf.investment.message.manager.MessageCenterManager;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.stat.StatManager;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.setting.manager.LogRequestManager;
import com.sscf.investment.setting.manager.RedDotManager;
import com.sscf.investment.setting.manager.RemindDataManager;
import com.sscf.investment.setting.widgt.DeveloperSettingPref;
import com.sscf.investment.social.FeedRequestManager;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.socialize.UmengSocialSDKUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.sscf.investment.utils.StringUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import BEC.ELogReportRetCode;
import BEC.ReportLogRsp;
import anet.channel.util.StringUtils;

/**
 * Created by xuebinliu on 2015/7/21.
 *
 * 灯塔应用程序入库
 *
 * 管理各自业务管理模块的初始化及释放
 */
public final class DengtaApplication extends Application implements IUmengRegisterCallback {
    private static final String TAG = "DengtaApplication";
    public static final int PRELOAD_DELAY = 1000;

    private static DengtaApplication instance;

    private boolean mIsMainProcess;

    public final Executor defaultExecutor = Executors.newCachedThreadPool();

    private AccountManager mAccountManager;

    private UpgradeManager mUpgradeManager;

    private DataCacheManager mDataCacheManager;

    private TradingStateManager mTradingStateManager;

    private FeedRequestManager mFeedRequestManager;

    private RedDotManager mRedDotManager;

    private AdManager mAdManager;

    private BonusPointManager mBonusPointManager;

    private PaymentInfoManager mPaymentInfoManager;

    public static final int INSTALL_STATE_NORMAL = 0;
    public static final int INSTALL_STATE_NEW_INSTALL = 1;
    public static final int INSTALL_STATE_OVER_INSTALL = 2;
    private int mInstallState = INSTALL_STATE_NORMAL;

    private int mLastVersionCode = -1;

    public float mKLineZoomRatioPortrait = 1.0f;
    public float mKLineZoomRatioLandscape = 1.0f;

    public Handler mUiHandler = new Handler(msg -> {
        switch (msg.what) {
            case MSG_KILL_MAIN_PROCESS:
                MobclickAgent.onKillProcess(getApplication());
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            default:
                break;
        }
        return false;
    });
    public static final int MSG_KILL_MAIN_PROCESS = 1;

    private PushManager mPushManager;

    private RemindDataManager mRemindDataManager;

    private SubscriptionManager mSubscriptionManager;

    private MessageCenterManager mMessageCenterManager;

    private KLineSettingManager mKLineSettingManager;

    /**
     * 表示主界面是否已经初始化过了
     */
    private boolean mMainActivityInited = false;

    public static RefWatcher getRefWatcher() {
        return instance.refWatcher;
    }

    private RefWatcher refWatcher;

    private DtDownloadManager mDownloadManager;

    private OrderManager mOrderManager;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        long start = System.currentTimeMillis();

        ContextHolder.init(this);
        instance = this;

        MultiDex.install(this);

        mIsMainProcess = DeviceUtil.isMainProcess(this);

        KeepAlive.getInstance().start(base, "", true);

        if (mIsMainProcess) {
            SDKManager.getInstance().setContext(this);
        }

        Log.d(TAG, "DengtaApplication.attachBaseContext spend " + (System.currentTimeMillis() - start));
    }

    public void checkOldVersion(){
        mInstallState = checkOverInstallOldVersion();

        switch (mInstallState) {
            case INSTALL_STATE_NORMAL:
                break;
            case INSTALL_STATE_OVER_INSTALL:
                break;
            case INSTALL_STATE_NEW_INSTALL:
                break;
            default:
                break;
        }
    }

    // ---------- 友盟push注册回调 ----------
    @Override
    public void onSuccess(String deviceToken) {
        // 注册成功会返回device token
        if (mIsMainProcess) {
            DtLog.d(TAG, "onSuccess Umeng deviceToken=" + deviceToken);
            final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
            mUiHandler.postDelayed(()->{
                dengtaApplication.defaultExecutor.execute(()->{
                    // 获得友盟的devicetoken后在去初始化guid
                    final AccountManager accountManager = dengtaApplication.getAccountManager();
                    if (!accountManager.isUmengReport()) {
                        accountManager.initGuid();
                    }
                    UmengPushManager.initUmengTag();
                });
            }, 1000L);
        }
    }

    @Override
    public void onFailure(String s, String s1) {
        DtLog.d(TAG, "onFailure Umeng s=" + s + ", s1=" + s1);
    }
    // ---------- 友盟push注册回调 ----------

    @Override
    public void onCreate() {
        final long start = System.currentTimeMillis();

        initLog();
        DengtaCrashHandler.getInstance();

        super.onCreate();

        IPManager.getInstance().setTest(Integer.parseInt(DeveloperSettingPref.getServerType()));

        if (mIsMainProcess) {
            initComponents();
        }

        initAsync();

        if(BuildUtils.isDebug()) {
            // debug模式开启严格模式
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
        }

        if (mIsMainProcess) {
            SDKManager.getInstance().init(this, 10000, "", isAuth -> {
            });

            mUiHandler.postDelayed(this::initAsyncDelay, PRELOAD_DELAY);
        }

        DtLog.d(TAG, "DengtaApplication.onCreate End spend " + (System.currentTimeMillis() - start));
    }

    public void initAsync() {
        defaultExecutor.execute(() -> {
            final DengtaApplication dengtaApplication = DengtaApplication.this;

            if (mIsMainProcess) {
                dengtaApplication.getAccountManager();
                if (BuildUtils.isPublish()) {
                    dengtaApplication.reportCrashLog();
                }
                initComponentsAsync();
                final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                        .getManager(IThemeManager.class.getName());
                if (themeManager != null) {
                    themeManager.init(R.style.theme_default, R.style.theme_night);
                }
                final IX5WebViewManager x5WebViewManager = (IX5WebViewManager) ComponentManager.getInstance().getManager(IX5WebViewManager.class.getName());
                if (x5WebViewManager != null) {
                    x5WebViewManager.init(dengtaApplication);
                }

//                if (isNewInstall())
                {
                    UmengPushManager.setPushKeyAndSecret();
                }
                registerActivityLifecycleCallbacks(new DengtaActivityLifecycleCallbacks());
            }

            dengtaApplication.getPushManager().enableUmengPushService(true);

            final PushAgent pushAgent = PushAgent.getInstance(dengtaApplication);
            try {
                pushAgent.register(dengtaApplication);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            final boolean isDebug = BuildUtils.isDebug();
            MobclickAgent.setDebugMode(isDebug);
            pushAgent.setDebugMode(isDebug);
        });
    }

    private void initLog() {
        DtLog.init(this);
        if (!BuildConfig.BUILD_TYPE.equals("publish")) {
            DtLog.setConsoleLogEnable(true);
        }
        DtLog.setFileLogEnable(DeveloperSettingPref.getSaveLogToFile());
    }

    private void reportCrashLog() {
        DtLog.d(TAG, "reportCrashLog");
        final long now = System.currentTimeMillis();
        final long lastReportTime = SettingPref.getLong(SettingConst.KEY_LAST_REPORT_CRASH, 0);
        final long interval = now - lastReportTime;
        if (interval < 5 * 60 * 1000L && interval > 0) { // 间隔5分钟以上才上报
            return;
        }

        final File crashDir = FileUtil.getCrashDir(this);
        if (crashDir.exists() && crashDir.isDirectory()) {
            final File[] files = crashDir.listFiles();
            if (files != null && files.length > 0) {
                final File file = files[0];
                if (file.exists()) {
                    if (file.length() > 0) {
                        // 上报文件
                        DtLog.d(TAG, "reportCrashLog file : " + file);
                        SettingPref.putLong(SettingConst.KEY_LAST_REPORT_CRASH, now);
                        final byte[] crashData = FileUtil.getByteArrayFromFile(file);
                        LogRequestManager.reportLogRequest(crashData, "android_crash", "txt", new DataSourceProxy.IRequestCallback() {
                            @Override
                            public void callback(boolean success, EntityObject data) {
                                if (success && data != null) {
                                    final ReportLogRsp rsp = (ReportLogRsp) data.getEntity();
                                    if (rsp != null && rsp.iRetCode == ELogReportRetCode.E_REPORT_SUCC) {
                                        DtLog.d(TAG, "reportCrashLog success file.length() : " + file.length());
                                        file.delete();
                                    }
                                }
                            }
                        });
                    } else {
                        file.delete();
                    }
                }
            }
        }
    }

    public int getLastVersionCode() {
        return mLastVersionCode;
    }

    private int checkOverInstallOldVersion() {
        final int lastVersion = SettingPref.getInt(SettingConst.KEY_LAST_OVER_INSTALL_VESION, 0);
        final int curVersion = DeviceUtil.getVersionCode(this);

        mLastVersionCode = lastVersion;
        if (curVersion != lastVersion) {
            SettingPref.putInt(SettingConst.KEY_LAST_OVER_INSTALL_VESION, curVersion);
            if (lastVersion > 0) {
                return INSTALL_STATE_OVER_INSTALL;
            } else {
                return INSTALL_STATE_NEW_INSTALL;
            }
        }
        return INSTALL_STATE_NORMAL;
    }

    public static DengtaApplication getApplication() {
        return instance;
    }

    /**
     * 各个业务初始化
     */
    public void initAsyncDelay() {
        DtLog.d(TAG, "initAsyncDelay");

        defaultExecutor.execute(() -> {
            try {
                // router初始化
                Router.initialize(this);

                getDataCacheManager().loadCachedDataFromFile();

                getRemindDataManager().init();

                getKLineSettingManager().init();

                // 首次安装启动，保存渠道号、创建快捷方式
                if (isNewInstall()) {
                    final String channel = getChannelIDFromManifest();
                    SettingPref.putString(SettingConst.KEY_FIRST_SETUP_CHANNEL_ID, channel);
                    if (!TextUtils.equals("10016", channel) && DeviceUtil.isNeedCreateShortcut()) {
                        // 三星渠道不创建
                        createShortCut();
                    }
                }

                if (isOverInstall()) {
                    DengtaSettingPref.setBindCellphoneShown(false);
                    //覆盖显示个股详情气泡的开关
                    if (getLastVersionCode() <= 260060811) {
                        DataPref.setMoreOperationGuideClicked(false);
                    }
                }

                UmengSocialSDKUtils.initSDK(this);

                DengtaCrashHandler.getInstance();// 防止crashHandler被替换

                if (BuildUtils.isDebug()) {
                    refWatcher = LeakCanary.install(this);
                }

                // 启动push
                getPushManager().start();
            } catch (Exception e) {
                StatisticsUtil.reportError(e);
            }

            StatManager.getInstance().loadStat();
        });

        // 只能在主线程执行
        DeviceUtil.clearPreloadedDrawables();
    }

    private void initComponents() {
        ComponentManager.registerComponent("com.sscf.investment.component.ui.manager.UIComponent");
    }

    private void initComponentsAsync() {
        ComponentManager.registerComponent(DengtaComponent.class.getName());
        ComponentManager.registerComponent("com.sscf.investment.web.sdk.component.WebSdkComponent");
        ComponentManager.registerComponent("com.sscf.investment.socialize.component.SocialComponent");
        ComponentManager.registerComponent("com.sscf.investment.logic.component.LogicComponent");
    }

    /**
     * 获取Application标签内的meta data
     */
    public static String getChannelIDFromManifest() {
        String channel = "";
        // 正常发布版本，渠道号是数字，在这里读到
        ApplicationInfo appInfo;
        try {
            appInfo = DengtaApplication.getApplication().getPackageManager().getApplicationInfo(
                    DengtaApplication.getApplication().getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return channel;
        }

        String ch = appInfo.metaData.getString(AccountManager.KEY_CHANNEL_ID);
        if (TextUtils.isEmpty(ch)) {
            channel = "";
        } else {
            channel = String.valueOf(ch);
        }

        return channel;
    }

    public synchronized UpgradeManager getUpgradeManager() {
        if (mUpgradeManager == null) {
            mUpgradeManager = new UpgradeManager();
        }
        return mUpgradeManager;
    }

    public synchronized DataCacheManager getDataCacheManager() {
        if (mDataCacheManager == null) {
            mDataCacheManager = new DataCacheManager();
        }
        return mDataCacheManager;
    }

    public synchronized TradingStateManager getTradingStateManager() {
        if (mTradingStateManager == null) {
            mTradingStateManager = TradingStateManager.getInstance();
        }
        return mTradingStateManager;
    }

    public synchronized FeedRequestManager getFeedRequestManager() {
        if (mFeedRequestManager == null) {
            mFeedRequestManager = new FeedRequestManager();
        }
        return mFeedRequestManager;
    }

    public synchronized RemindDataManager getRemindDataManager() {
        if (mRemindDataManager == null) {
            mRemindDataManager = new RemindDataManager();
        }
        return mRemindDataManager;
    }

    public synchronized KLineSettingManager getKLineSettingManager() {
        if (mKLineSettingManager == null) {
            mKLineSettingManager = new KLineSettingManager();
        }
        return mKLineSettingManager;
    }

    public synchronized RedDotManager getRedDotManager() {
        if (mRedDotManager == null) {
            mRedDotManager = new RedDotManager();
        }
        return mRedDotManager;
    }

    public synchronized AdManager getAdManager() {
        if(mAdManager == null) {
            mAdManager = new AdManager();
        }
        return mAdManager;
    }

    /**
     * 释放各个业务管理模块
     */
    public void release() {
        if (mDataCacheManager != null) {
            mDataCacheManager.saveDataToFiles();
        }

        DataEngine.getInstance().release();
        DtLog.release();

        mMainActivityInited = false;
        mInstallState = INSTALL_STATE_NORMAL;
    }

    public synchronized AccountManager getAccountManager() {
        if (mAccountManager == null) {
            mAccountManager = new AccountManager();
        }
        return mAccountManager;
    }

    public WebUrlManager getUrlManager() {
        return WebUrlManager.getInstance();
    }

    public SubscriptionManager getSubscriptionManager() {
        if (mSubscriptionManager == null) {
            mSubscriptionManager = new SubscriptionManager();
        }
        return mSubscriptionManager;
    }

    public MessageCenterManager getMessageCenterManager() {
        if (mMessageCenterManager == null) {
            mMessageCenterManager = new MessageCenterManager();
        }
        return mMessageCenterManager;
    }

    public DtDownloadManager getDownloadManager() {
        if(mDownloadManager == null) {
            mDownloadManager = new DtDownloadManager(this);
        }
        return mDownloadManager;
    }

    public OrderManager getOrderManager() {
        if(mOrderManager == null) {
            mOrderManager = new OrderManager();
        }
        return mOrderManager;
    }

    public void showToast(final String text) {
        CommonToast.showToast(text);
    }

    public void showToast(final int strId) {
        CommonToast.showToast(strId);
    }

    /**
     * 首次安装启动，创建快捷方式
     */
    public void createShortCut() {
        //创建快捷方式的Intent
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //不允许重复创建
        shortcutintent.putExtra("duplicate", false);
        //需要现实的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        //快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.app_icon);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //点击快捷图片，运行的程序主入口
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext() , MainActivity.class));
        //发送广播
        sendBroadcast(shortcutintent);
    }

    private boolean mMainActivityRestarting = false;
    public void setMainActivityRestarting(boolean restarting) {
        mMainActivityRestarting = restarting;
    }

    public boolean isMainActivityRestarting() {
        return mMainActivityRestarting;
    }

    public void startMainActivity(Intent intentWithInfos) {
        Intent intent = intentWithInfos;
        if (intent == null) {
            intent = new Intent(DengtaApplication.getApplication(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        try {
            this.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isMainActivityInited() {
        return mMainActivityInited;
    }

    public void setMainActivityInited(boolean isInited) {
        mMainActivityInited = isInited;
    }

    public PushManager getPushManager() {
        if (mPushManager == null) {
            mPushManager = new PushManager(this);
        }
        return mPushManager;
    }

    public BonusPointManager getBonusPointManager() {
        if(mBonusPointManager == null) {
            mBonusPointManager = new BonusPointManager();
        }
        return mBonusPointManager;
    }

    public PaymentInfoManager getPaymentInfoManager() {
        if(mPaymentInfoManager == null) {
            mPaymentInfoManager = new PaymentInfoManager();
        }
        return mPaymentInfoManager;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        DtLog.d(TAG, "onTrimMemory: level = " + level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            getDataCacheManager().saveDataToFiles();
        }
    }

    public int getBootState() {
        return mInstallState;
    }

    public boolean isNewInstall() {
        return mInstallState == INSTALL_STATE_NEW_INSTALL;
    }

    public boolean isOverInstall() {
        return mInstallState == INSTALL_STATE_OVER_INSTALL;
    }
}
