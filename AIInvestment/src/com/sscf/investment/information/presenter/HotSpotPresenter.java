package com.sscf.investment.information.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.sscf.investment.R;
import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.information.HotSpotFragment;
import com.sscf.investment.information.manager.HotSpotRequestManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.PeriodicHandlerManager;
import java.util.ArrayList;
import BEC.E_MARKET_TYPE;
import BEC.SecSimpleQuote;
import BEC.TopicListItem;
import BEC.TopicListRsp;

/**
 * Created by davidwei on 2017-05-13.
 */

public final class HotSpotPresenter extends BroadcastReceiver implements Runnable, DataSourceProxy.IRequestCallback, Handler.Callback {
    private static final String TAG = HotSpotPresenter.class.getSimpleName();

    private static final int MSG_UPDATE_LIST = 1;
    private static final int MSG_UPDATE_LIST_FAILED = 2;
    private static final int MSG_UPDATE_QUOTES = 3;
    private static final int MSG_UPDATE_LIST_MORE = 4;
    private static final int MSG_UPDATE_LIST_NO_MORE = 5;
    private static final int MSG_UPDATE_LIST_MORE_FAILED = 6;
    private final HotSpotFragment mFragment;
    private final Handler mHandler;
    private final PeriodicHandlerManager mPeriodicHandlerManager;

    private ArrayList<TopicListItem> mHotSpotList;
    private int mLastTime; // 分页使用

    public HotSpotPresenter(final HotSpotFragment fragment) {
        mFragment = fragment;
        mHandler = new Handler(Looper.getMainLooper(), this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        mTrading = TradingStateManager.getInstance().isTradingOrCallauction(E_MARKET_TYPE.E_MT_SH);
        registerBroadcastReceiver();
    }

    public void onDestroy() {
        unregisterBroadcastReceiver();
    }

    public void refresh() {
        mPeriodicHandlerManager.runPeriodic();
    }

    public void stopRefresh() {
        mPeriodicHandlerManager.stop();
    }

    @Override
    public void run() {
        requestQuoteData();
        mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
        if (!mTrading) {
            stopRefresh();
        }
    }

    public void requestListData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            return;
        }

        HotSpotRequestManager.requestHotSpotList(this);
    }

    public void requestListMoreData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_MORE_FAILED);
            return;
        }

        HotSpotRequestManager.requestHotSpotList(mLastTime, this);
    }

    public void requestQuoteData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            return;
        }

        final ArrayList<String> dtSecCodes = mFragment.getVisibleDtSecCodes();
        final int size = dtSecCodes == null ? 0 : dtSecCodes.size();
        if (size > 0) {
            QuoteRequestManager.getSimpleQuoteRequest(dtSecCodes, this, null);
        } else {
            stopRefresh();
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_SIMPLE_QUOTE:
                final ArrayList<SecSimpleQuote> quotes = EntityUtil.entityToSecSimpleQuoteList(success, data);
                if (quotes != null && quotes.size() > 0) {
                    mHandler.obtainMessage(MSG_UPDATE_QUOTES, quotes).sendToTarget();
                }
                break;
            case EntityObject.ET_GET_HOT_SPOT_LIST:
                getHotSpotListCallback(success, data);
                break;
            default:
                break;
        }
    }

    private void getHotSpotListCallback(boolean success, EntityObject data) {
        final String endTime = (String) data.getExtra();
        if (TextUtils.isEmpty(endTime)) { // 首页数据
            if (success) {
                final TopicListRsp rsp = (TopicListRsp) data.getEntity();
                final ArrayList<TopicListItem> hotSpotList = rsp.stTopicList;
                final int size = hotSpotList == null ? 0 : hotSpotList.size();
                if (size > 0) {
                    mLastTime = hotSpotList.get(size - 1).iTimestamp;
                }
                mHandler.obtainMessage(MSG_UPDATE_LIST, hotSpotList).sendToTarget();
                if (!rsp.bHasMore) {
                    mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                } else if (size > 0) {
                    mFragment.showFooterNormalLayout();
                }
                DtLog.d(TAG, "getHotSpotListCallback rsp.bHasMore : " + rsp.bHasMore);
                DtLog.d(TAG, "getHotSpotListCallback size : " + size);
            } else {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            }
        } else { // 分页数据
            if (success) {
                if (endTime.equals(String.valueOf(mLastTime))) { // 过滤重复数据
                    final TopicListRsp rsp = (TopicListRsp) data.getEntity();
                    final ArrayList<TopicListItem> hotSpotList = rsp.stTopicList;
                    final int size = hotSpotList == null ? 0 : hotSpotList.size();
                    if (size > 0) {
                        mLastTime = hotSpotList.get(size - 1).iTimestamp;
                        mHandler.obtainMessage(MSG_UPDATE_LIST_MORE, hotSpotList).sendToTarget();
                    }
                    if (!rsp.bHasMore || size == 0) {
                        mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                    }
                    DtLog.d(TAG, "getHotSpotListCallback rsp.bHasMore : " + rsp.bHasMore);
                    DtLog.d(TAG, "getHotSpotListCallback size : " + size);
                }
            } else {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_MORE_FAILED);
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_QUOTES:
                mFragment.updateQuotesData((ArrayList<SecSimpleQuote>) msg.obj);
                break;
            case MSG_UPDATE_LIST:
                mFragment.refreshComplete();
                final ArrayList<TopicListItem> hotSpotList = (ArrayList<TopicListItem>) msg.obj;
                final int size = hotSpotList == null ? 0 : hotSpotList.size();
                if (size > 0) {
                    mHotSpotList = hotSpotList;
                    mFragment.updateList(hotSpotList);
                    mPeriodicHandlerManager.runPeriodicDelay(100); // 解决mFragment.getVisibleDtSecCodes()为0的问题
                } else {
                    mFragment.showEmptyLayout();
                }
                break;
            case MSG_UPDATE_LIST_FAILED:
                mFragment.refreshComplete();
                final ArrayList<TopicListItem> list = mHotSpotList;
                if (list != null && list.size() > 0) {
                    DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                } else {
                    mFragment.showRetryLayout();
                }
                break;
            case MSG_UPDATE_LIST_MORE:
                mHotSpotList.addAll((ArrayList<TopicListItem>) msg.obj);
                mFragment.updateList(mHotSpotList);
                mPeriodicHandlerManager.runPeriodicDelay(100); // 解决mFragment.getVisibleDtSecCodes()为0的问题
                break;
            case MSG_UPDATE_LIST_NO_MORE:
                mFragment.showFooterNoMoreLayout();
                break;
            case MSG_UPDATE_LIST_MORE_FAILED:
                mFragment.showFooterRetryLayout();
                break;
            default:
                break;
        }
        return true;
    }


    private boolean mRegisteredReceiver;
    private boolean mTrading;

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
            if (trading) {
                refresh();
            }
        }
    }
}
