package com.sscf.investment.main.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IFavorManager;
import com.dengtacj.component.managers.ILocalH5ResourceManager;
import com.dengtacj.component.managers.IMarketManager;
import com.dengtacj.component.managers.IMarketWarningManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.dengtacj.component.managers.IScreenShotManager;
import com.dengtacj.component.managers.ISmartStareManager;
import com.dengtacj.component.managers.IThemeManager;
import com.dengtacj.component.managers.IVideoFavorManager;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.BuildConfig;
import com.sscf.investment.main.DataPref;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.MainActivity;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.manager.AdManager;
import com.sscf.investment.main.model.MainModel;
import com.sscf.investment.push.UmengPushManager;
import com.sscf.investment.sdk.main.manager.IpUrlManager;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.widgt.DeveloperSettingPref;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.sdk.net.IPManager;
import com.sscf.investment.sdk.stat.StatManager;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.manager.LogRequestManager;
import com.sscf.investment.setting.manager.RedDotManager;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.umeng.analytics.MobclickAgent;
import java.util.HashMap;
import BEC.AccuPointTaskType;
import BEC.DtLiveType;
import BEC.E_MARKET_TYPE;
import BEC.GetBoxLiveRsp;

/**
 * Created by davidwei on 2017-08-11.
 */

public final class MainPresenter extends BroadcastReceiver implements Runnable, Handler.Callback {
    private static final int MSG_UPDATE_LIVE_MSG = 1;

    private final MainActivity mActivity;
    private final MainModel mModel;
    private final PeriodicHandlerManager mPeriodicHandlerManager;
    private final Handler mHandler;

    private boolean mRegisteredReceiver;
    private boolean mTrading;
    private boolean mResume;

    public MainPresenter(final MainActivity activity) {
        mActivity = activity;
        mModel = new MainModel(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        mHandler = new Handler(this);
        mHandler.postDelayed(() -> {
            DengtaApplication.getApplication().defaultExecutor.execute(() -> {
                initAsync();
            });
        }, 1000L);
        mHandler.postDelayed(() -> {
            DengtaApplication.getApplication().defaultExecutor.execute(() -> {
                initAsyncDelay();
            });
        }, 2000L);
        registerBroadcastReceiver();
    }

    private void initAsync() {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();

        final boolean networkConnected = NetUtil.isNetWorkConnected(dengtaApplication);

        if (networkConnected) {
            dengtaApplication.getTradingStateManager().loadTradingTimeData();
            final AccountManager accountManager = dengtaApplication.getAccountManager();
            accountManager.updateAccountInfoFromWeb();
        }

        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager != null) {
            portfolioDataManager.reloadData(false);
        }

        IFavorManager favorManager = (IFavorManager) ComponentManager.getInstance().getManager(IFavorManager.class.getName());
        if (favorManager != null) {
            // 初始化资讯收藏
            favorManager.init();
        }

        IVideoFavorManager videoFavorManager = (IVideoFavorManager) ComponentManager.getInstance().getManager(IVideoFavorManager.class.getName());
        if (videoFavorManager != null) {
            // 初始化视频收藏
            videoFavorManager.init();
        }

        ISmartStareManager smartStareManager = (ISmartStareManager) ComponentManager.getInstance().getManager(ISmartStareManager.class.getName());
        if(smartStareManager != null) {
            smartStareManager.init();
        }
    }

    private void initAsyncDelay() {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();

        final boolean networkConnected = NetUtil.isNetWorkConnected(dengtaApplication);

        UmengPushManager.initUmengTag();

        dengtaApplication.getMessageCenterManager().initAsync();

        if (networkConnected) {
            if (!("debug".equals(BuildConfig.BUILD_TYPE))) {
                dengtaApplication.getUpgradeManager().reqUpdate(1);
            }
            // IP更新
            IPManager.getInstance().update();
            // URL初始化
            WebUrlManager.getInstance().init(dengtaApplication);
            IpUrlManager.getInstance().init(dengtaApplication);
            // 拉取push开关状态
            dengtaApplication.getPushManager().checkPushSwitchUpdate();
            final ILocalH5ResourceManager localH5ResourceManager = (ILocalH5ResourceManager) ComponentManager.getInstance().getManager(ILocalH5ResourceManager.class.getName());
            if (localH5ResourceManager != null) {
                localH5ResourceManager.setUseH5LocalResource(DeveloperSettingPref.getUseH5LocalCache());
                localH5ResourceManager.init(dengtaApplication.isOverInstall());
            }

            final IMarketWarningManager marketWarningManager = (IMarketWarningManager) ComponentManager.getInstance()
                    .getManager(IMarketWarningManager.class.getName());
            if (marketWarningManager != null) {
                marketWarningManager.getMainBoardWarningInfoRequest(null);
            }

            final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance().getManager(IMarketManager.class.getName());
            if (marketManager != null) { // 从服务器获得港币汇率
                marketManager.getHKDollarsExchangeRate();
            }

            final RedDotManager redDotManager = dengtaApplication.getRedDotManager();
            redDotManager.requestConfigActivitiesAndOpenAccountInfoForce();
            redDotManager.requestIntelligentAnswerInfo();

            final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance().getManager(IAccountManager.class.getName());
            final AccountInfoEntity accountInfo = accountManager != null ? accountManager.getAccountInfo() : null;
            if (accountInfo != null) {
                if (TextUtils.isEmpty(accountInfo.cellphone) && !DengtaSettingPref.isBindCellphoneShown()) {
                    CommonBeaconJump.showBindCellphone(mActivity, true);
                    DengtaSettingPref.setBindCellphoneShown(true);
                }
                dengtaApplication.getSubscriptionManager().getSubscriptionListRequest();
                dengtaApplication.getBonusPointManager().reportFinishedTask(AccuPointTaskType.E_ACCU_POINT_TASK_DAILY_SIGN);
            }
            dengtaApplication.getPaymentInfoManager().requestUserAgreement();

            final AdManager adManager = dengtaApplication.getAdManager();
            adManager.reqAd();

            LogRequestManager.reportInstalledAppsToServer();
            LogRequestManager.reportSecDetailPageViewToServer();
        }

        // 设置友盟数据加密
        MobclickAgent.enableEncrypt(true);
        // 设置友盟不采集mac地址
        MobclickAgent.setCheckDevice(false);

        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        if (themeManager != null) {
            if (themeManager.isNightTheme()) {
                StatisticsUtil.reportAction(StatisticsConst.STATE_NIGHT_MODE);
            }
        }
        final IScreenShotManager screenShotManager = (IScreenShotManager) ComponentManager.getInstance()
                .getManager(IScreenShotManager.class.getName());
        if (screenShotManager != null) {
            screenShotManager.init();
        }
    }

    public void onDestroy() {
        unregisterBroadcastReceiver();

        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager != null) {
            // 计数统计
            StatisticsUtil.reportAccount(StatisticsConst.PORTFOLIO_STOCK_ACCOUNT,
                    new HashMap<String, String>() {{put("自选股票总数","");}}, portfolioDataManager.getAllStockList(false, false).size());

            StatisticsUtil.reportAccount(StatisticsConst.PORTFOLIO_STOCK_ACCOUNT,
                    new HashMap<String, String>() {{put("自选股票沪深数","");}}, portfolioDataManager.getChineseStockSize());

            StatisticsUtil.reportAccount(StatisticsConst.PORTFOLIO_STOCK_ACCOUNT,
                    new HashMap<String, String>() {{put("自选股票港股数", "");}}, portfolioDataManager.getHKStockSize());

            StatisticsUtil.reportAccount(StatisticsConst.PORTFOLIO_STOCK_ACCOUNT,
                    new HashMap<String, String>() {{put("自选股票美股数","");}}, portfolioDataManager.getUSAStockSize());

            StatisticsUtil.reportAccount(StatisticsConst.PORTFOLIO_STOCK_ACCOUNT,
                    new HashMap<String, String>() {{put("自选股票持仓数","");}}, portfolioDataManager.getPositionStockSize());

            StatisticsUtil.reportAccount(StatisticsConst.PORTFOLIO_STOCK_ACCOUNT,
                    new HashMap<String, String>() {{put("自选股票添加数","");}}, portfolioDataManager.getAddStockCount());

            StatisticsUtil.reportAccount(StatisticsConst.PORTFOLIO_STOCK_ACCOUNT,
                    new HashMap<String, String>() {{put("自选股票删除数","");}}, portfolioDataManager.getDelStockCount());
        }

        // 释放系统缓存
        DengtaApplication.getApplication().release();

        StatManager.getInstance().save();
    }

    public void setResume(boolean resume) {
        this.mResume = resume;
    }

    public void refresh() {
        mPeriodicHandlerManager.runPeriodic();
    }

    public void refreshDelay() {
        mPeriodicHandlerManager.runPeriodicDelay(2000);
    }

    public void stopRefresh() {
        mPeriodicHandlerManager.stop();
    }

    @Override
    public void run() {
        if (DengtaSettingPref.isLiveMsgEnabled()) {
            int lastLiveMsgTime = DataPref.getLastLiveMsgTime();
            int reqType = getLiveMsgRequestType();
            if (reqType != -1) {
                mModel.requestLiveMsg(lastLiveMsgTime, reqType);
            }
        }
        mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());

        if (!mTrading) {
            final TradingStateManager tradingStateManager = TradingStateManager.getInstance();
            final int closeTime = TradingStateManager.getInstance().getCloseTime(E_MARKET_TYPE.E_MT_SH);
            final int now = tradingStateManager.getServerTime();
            if (closeTime > 0 && now - closeTime > 30 * 60) { // 收盘30分钟后就停止请求直播数据
                stopRefresh();
            }
        }
    }

    private int getLiveMsgRequestType() {
        boolean mainBoardLiveMsgEnabled = DengtaSettingPref.isMainBoardLiveMsgEnabled();
        boolean portfolioLiveMsgEnabled = DengtaSettingPref.isPortfolioLiveMsgEnabled();
        int reqType = -1; //-1表示大盘和自选股直播的开关都被关闭
        if (mainBoardLiveMsgEnabled && portfolioLiveMsgEnabled) {
            reqType = DtLiveType.E_LIVE_ALL;
        } else if (mainBoardLiveMsgEnabled) {
            reqType = DtLiveType.E_LIVE_MARKET;
        } else if (portfolioLiveMsgEnabled) {
            reqType = DtLiveType.E_LIVE_PORTFOLIO;
        }
        return reqType;
    }

    public void onGetLiveMsg(final GetBoxLiveRsp rsp) {
        if (rsp != null) {
            mHandler.obtainMessage(MSG_UPDATE_LIVE_MSG, rsp).sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_LIVE_MSG:
                final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
                final RedDotManager redDotManager = dengtaApplication.getRedDotManager();
                final boolean isLogin = dengtaApplication.getAccountManager().isLogined();
                final GetBoxLiveRsp rsp = (GetBoxLiveRsp) msg.obj;
                if (rsp.iLiveMsgStatus == 1) { // 清除红点
                    redDotManager.setPortfolioLiveState(false);
                    redDotManager.setMainBoardLiveState(false);
                } else {
                    if (mActivity.updateLiveMsg(rsp)) {
                        // 如果显示了，就设置红点
                        switch (rsp.iLiveType) {
                            case DtLiveType.E_LIVE_PORTFOLIO:
                                redDotManager.setPortfolioLiveState(isLogin);
                                break;
                            case DtLiveType.E_LIVE_MARKET:
                                redDotManager.setMainBoardLiveState(true);
                                break;
                            case DtLiveType.E_LIVE_ALL:
                                redDotManager.setPortfolioLiveState(isLogin);
                                redDotManager.setMainBoardLiveState(true);
                                break;
                            default:
                                break;
                        }
                    }
                }
                break;
        }
        return true;
    }

    private void registerBroadcastReceiver() {
        if (mRegisteredReceiver) {
            return;
        }
        final IntentFilter filter = new IntentFilter(TradingStateManager.ACTION_TRADING_STATE_UPDATED);
        filter.addAction(TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED);
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).registerReceiver(this, filter);
        mRegisteredReceiver = true;
    }

    private void unregisterBroadcastReceiver() {
        if (mRegisteredReceiver) {
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).unregisterReceiver(this);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED.equals(action)) {
            switchTradingState();
        } else if (TradingStateManager.ACTION_TRADING_STATE_UPDATED.equals(action)){
            switchTradingState();
        }
    }

    private void switchTradingState() {
        final boolean trading = TradingStateManager.getInstance().isTradingOrCallauction(E_MARKET_TYPE.E_MT_SH);
        if (mTrading != trading) {
            mTrading = trading;
            if (trading && mResume) {
                refresh();
            }
        }
    }
}
