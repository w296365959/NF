package com.sscf.investment.teacherYan.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.teacherYan.MorningRefFragment;
import com.sscf.investment.teacherYan.manager.MorningRefRequestManager;
import com.sscf.investment.utils.PeriodicHandlerManager;

import java.util.ArrayList;

import BEC.E_MARKET_TYPE;
import BEC.InformationSpiderNews;
import BEC.InformationSpiderNewsListRsp;
import BEC.SecSimpleQuote;

/**
 * Created by LEN on 2018/4/23.
 */

public class MorningRefPresent extends BroadcastReceiver implements
        Runnable, DataSourceProxy.IRequestCallback, Handler.Callback {

    private static final String TAG = MorningRefPresent.class.getSimpleName();

    private static final int MSG_UPDATE_LIST = 1;
    private static final int MSG_UPDATE_LIST_FAILED = 2;
    private static final int MSG_UPDATE_QUOTES = 3;
    private static final int MSG_UPDATE_LIST_MORE = 4;
    private static final int MSG_UPDATE_LIST_NO_MORE = 5;
    private static final int MSG_UPDATE_LIST_MORE_FAILED = 6;
    private final MorningRefFragment mFragment;
    private final Handler mHandler;
    private final PeriodicHandlerManager mPeriodicHandlerManager;


    private ArrayList<InformationSpiderNews> mInformations;
    private int mLastId; // 分页使用
    private int mTotalCount;

    public MorningRefPresent(final MorningRefFragment fragment) {
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

        MorningRefRequestManager.requestMorningRefList(this);
    }

    public void requestListMoreData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_MORE_FAILED);
            return;
        }
        MorningRefRequestManager.requestMorningRefListMore(mLastId, this);
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
            case EntityObject.ET_MORNING_REF:
                getMorningRefCallback(success, data);
                break;
            default:
                break;
        }
    }

    private void getMorningRefCallback(boolean success, EntityObject data) {
        final String endId = (String) data.getExtra();
        if (TextUtils.isEmpty(endId)) { // 首页数据
            if (success) {
                final InformationSpiderNewsListRsp rsp = (InformationSpiderNewsListRsp) data.getEntity();
                mTotalCount = rsp.getITotalCount();
                final ArrayList<InformationSpiderNews> informations = rsp.getVtInformationSpiderNews();
                final int size = informations == null ? 0 : informations.size();
                if (size > 0) {
                    mLastId = informations.get(size - 1).getIID();
                }
                mHandler.obtainMessage(MSG_UPDATE_LIST, informations).sendToTarget();
                if (rsp.getITotalCount() == informations.size()) {
                    mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                } else if (size > 0) {
                    mFragment.showFooterNormalLayout();
                }
            } else {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            }
        } else { // 分页数据
            if (success) {
                if (endId.equals(String.valueOf(mLastId))) { // 过滤重复数据
                    final InformationSpiderNewsListRsp rsp = (InformationSpiderNewsListRsp) data.getEntity();
                    mTotalCount = rsp.getITotalCount();
                    final ArrayList<InformationSpiderNews> informations = rsp.getVtInformationSpiderNews();
                    final int size = informations == null ? 0 : informations.size();
                    if (size > 0) {
                        mLastId = informations.get(size - 1).getIID();
                        mHandler.obtainMessage(MSG_UPDATE_LIST_MORE, informations).sendToTarget();
                    }

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
                final ArrayList<InformationSpiderNews> informations = (ArrayList<InformationSpiderNews>) msg.obj;
                final int size = informations == null ? 0 : informations.size();
                if (size > 0) {
                    mInformations = informations;
                    mFragment.updateList(informations);
                    mPeriodicHandlerManager.runPeriodicDelay(100); // 解决mFragment.getVisibleDtSecCodes()为0的问题
                } else {
                    mFragment.showEmptyLayout();
                }
                break;
            case MSG_UPDATE_LIST_FAILED:
                mFragment.refreshComplete();
                final ArrayList<InformationSpiderNews> list = mInformations;
                if (list != null && list.size() > 0) {
                    DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                } else {
                    mFragment.showRetryLayout();
                }
                break;
            case MSG_UPDATE_LIST_MORE:
                mInformations.addAll((ArrayList<InformationSpiderNews>) msg.obj);
                if (mInformations.size() >= mTotalCount) {
                    mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                }
                mFragment.updateList(mInformations);
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
