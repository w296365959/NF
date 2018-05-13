package com.sscf.investment.discover.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IQuoteManager;
import com.sscf.investment.R;
import com.sscf.investment.discover.DiscoverStockPickFragment;
import com.sscf.investment.discover.model.StockPickModel;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.PeriodicHandlerManager;
import java.io.File;
import java.util.ArrayList;
import BEC.CategoryInfo;
import BEC.DtActivityDetail;
import BEC.E_MARKET_TYPE;
import BEC.IntelliPickStockEx;
import BEC.RecommValueAdded;
import BEC.SecSimpleQuote;

/**
 * Created by davidwei on 2017-11-02
 *
 */
public final class StockPickPresenter implements Runnable, Handler.Callback, OnGetDataCallback<ArrayList<SecSimpleQuote>> {

    private static final int MSG_UPDATE_STRATEGY_LIST = 1;
    private static final int MSG_UPDATE_BANNER_LIST = 2;
    private static final int MSG_UPDATE_LIST = 3;
    private static final int MSG_UPDATE_FAILED = 4;
    private static final int MSG_UPDATE_QUOTE = 5;
    private static final int MSG_SHOW_FOOTER_LOADING = 6;
    private static final int MSG_SHOW_FOOTER_NO_MODE = 7;

    private final DiscoverStockPickFragment mFragment;
    private final StockPickModel mModel;

    public static final int UPDATE_INTERVAL = 5 * 60 * 1000;
    private long mLastUpdateTime = 0L;

    private PeriodicHandlerManager mPeriodicHandlerManager;
    private final Handler mHandler;

    private ArrayList<RecommValueAdded> mValueAddedList;
    private ArrayList<IntelliPickStockEx> mStockPickList;
    private String mLastId = "";
    private final File mStrategyDateFile;
    private final File mStockPickListFile;

    public StockPickPresenter(final DiscoverStockPickFragment fragment) {
        mFragment = fragment;
        mModel = new StockPickModel(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        mHandler = new Handler(Looper.getMainLooper(), this);
        final Context context = SDKManager.getInstance().getContext();
        mStrategyDateFile = FileUtil.getStockPickStrategyListFile(context);
        mStockPickListFile = FileUtil.getDiscoverStockPickInfoFile(context);
        mTrading = TradingStateManager.getInstance().isTradingOrCallauction(E_MARKET_TYPE.E_MT_SH);
    }

    public void loadDataFromLocal() {
        DengtaApplication.getApplication().defaultExecutor.execute(()->{
            final ArrayList<CategoryInfo> strategyList = (ArrayList<CategoryInfo>) FileUtil.getObjectFromFile(mStrategyDateFile);
            mHandler.obtainMessage(MSG_UPDATE_STRATEGY_LIST, strategyList).sendToTarget();

            final ArrayList<IntelliPickStockEx> pickStockList = (ArrayList<IntelliPickStockEx>) FileUtil.getObjectFromFile(mStockPickListFile);
            // 针对老数据数据类型不匹配的问题进行处理，如果发现缓存数据为老类型，则直接删除掉，不加载
            if (pickStockList != null && !pickStockList.isEmpty()) {
                Object pickStock = pickStockList.get(0);
                if (!(pickStock instanceof IntelliPickStockEx)) {
                    mStockPickListFile.delete();
                    return;
                }
            }

            final int size = pickStockList == null ? 0 : pickStockList.size();
            if (size > 0) {
                mLastId = pickStockList.get(size - 1).sId;
                mStockPickList = pickStockList;
                mHandler.obtainMessage(MSG_UPDATE_LIST, pickStockList).sendToTarget();
                mHandler.sendEmptyMessage(MSG_SHOW_FOOTER_LOADING);
            }
        });
    }

    public void refreshData() {
        mModel.requestValueAddedList();
        final long now = System.currentTimeMillis();
        if (now - mLastUpdateTime > UPDATE_INTERVAL) {
            if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
                mPeriodicHandlerManager.runPeriodicDelay(DengtaSettingPref.getRefreshDelaySenconds());
                return;
            }
            mModel.requestStrategyList();
            mModel.requestBannerList();
            mLastId = "";
            mModel.requestIntelligentPickStockList(mLastId);
        } else {
            refreshQuote();
        }
    }

    public void requestFirstPageData() {
        mModel.requestValueAddedList();
        mModel.requestStrategyList();
        mModel.requestBannerList();
        mLastId = "";
        mModel.requestIntelligentPickStockList(mLastId);
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_FAILED, 10000L);
    }

    public void requestMorePageData() {
        mModel.requestIntelligentPickStockList(mLastId);
    }

    public void refreshQuote() {
        mPeriodicHandlerManager.runPeriodic();
    }

    public void stopRefreshQuote() {
        mPeriodicHandlerManager.stop();
    }

    @Override
    public void run() {
        mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
        if (NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            requestData();
        }
        if (!mTrading) {
            mPeriodicHandlerManager.stop();
        }
    }

    public void requestData() {
        final ArrayList<String> dtSecCodes = mFragment.getVisibleDtSecCodes();
        if (dtSecCodes != null && dtSecCodes.size() > 0) {
            final IQuoteManager quoteManager = (IQuoteManager) ComponentManager.getInstance()
                    .getManager(IQuoteManager.class.getName());
            if (quoteManager != null) {
                quoteManager.requestSimpleQuote(dtSecCodes, this);
            }
        }
    }

    public void onGetStrategyList(ArrayList<CategoryInfo> strategyList) {
        if (strategyList != null) {
            mHandler.obtainMessage(MSG_UPDATE_STRATEGY_LIST, strategyList).sendToTarget();
            FileUtil.saveObjectToFile(strategyList, mStrategyDateFile);
        }
    }

    public void onGetBannerList(ArrayList<DtActivityDetail> bannerList) {
        if (bannerList != null) {
            mHandler.obtainMessage(MSG_UPDATE_BANNER_LIST, bannerList).sendToTarget();
        }
    }

    public void onGetValueAddedList(ArrayList<RecommValueAdded> valueAddedList) {
        if (valueAddedList != null) {
            mValueAddedList = valueAddedList;
            mHandler.obtainMessage(MSG_UPDATE_LIST, mStockPickList != null ? new ArrayList<>(mStockPickList) : null).sendToTarget();
        }
    }

    public void onGetPickStockList(final ArrayList<IntelliPickStockEx> pickStockList, final String lastId) {
        if (TextUtils.equals(mLastId, lastId)) {
            if (TextUtils.isEmpty(lastId)) { // 第一页
                if (pickStockList == null) {
                    mHandler.sendEmptyMessage(MSG_UPDATE_FAILED);
                } else {
                    final int size = pickStockList.size();
                    if (size > 0) {
                        mLastId = pickStockList.get(size - 1).sId;
                        FileUtil.saveObjectToFile(pickStockList, mStockPickListFile);
                        mStockPickList = pickStockList;
                        mHandler.obtainMessage(MSG_UPDATE_LIST, new ArrayList<>(pickStockList)).sendToTarget();
                        mHandler.sendEmptyMessage(MSG_SHOW_FOOTER_LOADING);
                        mLastUpdateTime = System.currentTimeMillis();
                    }
                }
            } else { // 下拉取后一页
                if (pickStockList == null) {
                    mHandler.sendEmptyMessage(MSG_UPDATE_FAILED);
                } else {
                    final int size = pickStockList.size();
                    if (size == 0) { // 没有更多的了
                        mHandler.sendEmptyMessage(MSG_SHOW_FOOTER_NO_MODE);
                        return;
                    }

                    final String id = pickStockList.get(size - 1).sId;

                    if (TextUtils.equals(id, lastId)) {
                        mHandler.sendEmptyMessage(MSG_SHOW_FOOTER_NO_MODE);
                        return;
                    } else {
                        mLastId = id;
                    }

                    mStockPickList.addAll(pickStockList);
                    mHandler.obtainMessage(MSG_UPDATE_LIST, new ArrayList<>(mStockPickList)).sendToTarget();
                }
            }

            if (mFragment.getUserVisibleHint()) {
                mPeriodicHandlerManager.runPeriodicDelay(100); // 拉取股票信息
            }
        }
    }

    @Override
    public void onGetData(ArrayList<SecSimpleQuote> data) {
        if (data != null) {
            mHandler.obtainMessage(MSG_UPDATE_QUOTE, data).sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        final Activity activity = mFragment.getActivity();
        if (activity == null) {
            return true;
        }

        switch (msg.what) {
            case MSG_UPDATE_QUOTE:
                mFragment.updateQuote((ArrayList<SecSimpleQuote>) msg.obj);
                break;
            case MSG_UPDATE_STRATEGY_LIST:
                mFragment.updateStrategeList((ArrayList<CategoryInfo>) msg.obj);
                break;
            case MSG_UPDATE_BANNER_LIST:
                mFragment.updateBannerList((ArrayList<DtActivityDetail>) msg.obj);
                break;
            case MSG_UPDATE_LIST:
                mFragment.refreshComplete();
                mFragment.updateList(mValueAddedList, (ArrayList<IntelliPickStockEx>) msg.obj);
                mPeriodicHandlerManager.runPeriodic();
                mHandler.removeMessages(MSG_UPDATE_FAILED);
                break;
            case MSG_UPDATE_FAILED:
                mFragment.refreshComplete();
                DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                mHandler.removeMessages(MSG_UPDATE_FAILED);
                break;
            case MSG_SHOW_FOOTER_LOADING:
                mFragment.showFooterLoading();
                break;
            case MSG_SHOW_FOOTER_NO_MODE:
                mFragment.showNoMore();
                break;
            default:
                break;
        }
        return true;
    }

    private BroadcastReceiver mReceiver;
    private boolean mTrading;

    public void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new StockPickReceiver();
            final IntentFilter filter = new IntentFilter(TradingStateManager.ACTION_TRADING_STATE_UPDATED);
            filter.addAction(TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED);
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).registerReceiver(mReceiver, filter);
        }
    }

    public void unregisterBroadcastReceiver() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).unregisterReceiver(mReceiver);
        }
    }

    private final class StockPickReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED.equals(action)) {
                switchTradingState();
            } else if (TradingStateManager.ACTION_TRADING_STATE_UPDATED.equals(action)) {
                switchTradingState();
            }
        }
    }

    private void switchTradingState() {
        final boolean trading = TradingStateManager.getInstance().isTradingOrCallauction(E_MARKET_TYPE.E_MT_SH);
        if (mTrading != trading) {
            mTrading = trading;
            if (trading) {
                mPeriodicHandlerManager.runPeriodic();
            }
        }
    }
}
