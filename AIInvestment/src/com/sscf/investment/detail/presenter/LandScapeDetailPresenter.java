package com.sscf.investment.detail.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.sscf.investment.detail.fragment.LandscapeLineChartFragment;
import com.sscf.investment.detail.model.LandScapeDetailModel;
import com.sscf.investment.detail.view.LineChartTextureView;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.utils.StockUtil;
import java.util.ArrayList;
import BEC.CapitalFlow;
import BEC.HisChipDistRsp;
import BEC.SecLiveMsg;
import BEC.SecLiveMsgRsp;
import BEC.SecQuote;
import BEC.TickRsp;
import BEC.TrendDesc;
import BEC.TrendRsp;

/**
 * Created by davidwei on 2017/05/11
 */
public final class LandScapeDetailPresenter implements Runnable, Handler.Callback {
    private static final int MSG_UPDATE_QUOTE_DATA = 1;
    private static final int MSG_UPDATE_TIME_LINE_FULL_DATA = 2;
    private static final int MSG_UPDATE_TIME_LINE_INCREMENT_DATA = 3;
    private static final int MSG_UPDATE_TICK_VIEW = 4;
    private static final int MSG_UPDATE_LIVE_MSG = 5;
    private static final int MSG_UPDATE_CAPITAL_FLOW = 6;
    private static final int MSG_UPDATE_CHIP_DIST = 7;

    // V
    private final LandscapeLineChartFragment mFragment;
    // M
    private final LandScapeDetailModel mModel;

    protected final Handler mHandler;
    protected final PeriodicHandlerManager mPeriodicHandlerManager;

    private boolean mIsTrading;

    private final String mDtSecCode;
    private String mLastMsgId;

    private ArrayList<TrendDesc> mTimeLineDatas;

    public LandScapeDetailPresenter(LandscapeLineChartFragment fragment, String dtSecCode) {
        mFragment = fragment;
        mModel = new LandScapeDetailModel(this);

        mHandler = new Handler(Looper.getMainLooper(), this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);

        mDtSecCode = dtSecCode;
    }

    public void setTrading(boolean isTrading) {
        mIsTrading = isTrading;
    }

    public void refresh() {
        mPeriodicHandlerManager.runPeriodic();

        if(StockUtil.supportChipEntrance(mDtSecCode) && DengtaApplication.getApplication().getAccountManager().isMember()) {
            mModel.requestChipDist(mDtSecCode);
        }
    }

    public void stopRefresh() {
        mPeriodicHandlerManager.stop();
    }

    @Override
    public void run() {
        final String dtSeccode = mDtSecCode;
        mFragment.reloadDataByIndex();
        mModel.requestQuote(dtSeccode);
        requestTick();
        requestLiveMsg();

        if (mIsTrading) {
            mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
        } else {
            mPeriodicHandlerManager.stop();
        }
    }

    public void requestTimeLineData() {
        mModel.requestTimeLineData(mDtSecCode, getTimeLineDatasStart());
    }

    /**
     * @return 如果是0，全量拉取
     */
    private int getTimeLineDatasStart() {
        final ArrayList<TrendDesc> timeLineDatas = mTimeLineDatas;
        final int size = timeLineDatas == null ? 0 : timeLineDatas.size();
        return size - 2;
    }

    public void onGetTimeLineFullData(final TrendRsp trendRsp) {
        if (trendRsp != null) {
            mHandler.obtainMessage(MSG_UPDATE_TIME_LINE_FULL_DATA, trendRsp).sendToTarget();
        }
    }

    public void onGetTimeLineIncrementData(final TrendRsp trendRsp) {
        if (trendRsp != null) {
            mHandler.obtainMessage(MSG_UPDATE_TIME_LINE_INCREMENT_DATA, trendRsp).sendToTarget();
        }
    }

    public void onGetQuote(final SecQuote quote) {
        if (quote != null) {
            mHandler.obtainMessage(MSG_UPDATE_QUOTE_DATA, quote).sendToTarget();
        }// TODO 失败
    }

    public void onGetChipDistRsp(final HisChipDistRsp chipDistRsp) {
        if (chipDistRsp != null) {
            mHandler.obtainMessage(MSG_UPDATE_CHIP_DIST, chipDistRsp).sendToTarget();
        }
    }

    public void requestTick() {
        if (StockUtil.hasTransaction(mDtSecCode)) {
            mModel.requestTick(mDtSecCode);
        }
    }

    public void onGetTick(final TickRsp rsp) {
        if (rsp != null) {
            mHandler.obtainMessage(MSG_UPDATE_TICK_VIEW, rsp).sendToTarget();
        }
    }

    public void requestLiveMsg() {
        if (StockUtil.supportSecLiveMsg(mDtSecCode)) {
            mModel.requestLiveMsg(mDtSecCode, mLastMsgId);
        }
    }

    public void onGetLiveMsg(final SecLiveMsgRsp rsp) {
        if (rsp != null) {
            mHandler.obtainMessage(MSG_UPDATE_LIVE_MSG, rsp).sendToTarget();
        }
    }

    public void requestCapitalData() {
        mModel.requestCapitalData(mDtSecCode);
    }

    public void onGetCapitalFlow(final CapitalFlow capitalFlow) {
        if (capitalFlow != null) {
            mHandler.obtainMessage(MSG_UPDATE_CAPITAL_FLOW, capitalFlow).sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(final Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_QUOTE_DATA:
                mFragment.showViewByState(DengtaConst.UI_STATE_NORMAL);
                mFragment.updateQuoteData((SecQuote) msg.obj);
                break;
            case MSG_UPDATE_TIME_LINE_FULL_DATA:
                mFragment.showViewByState(DengtaConst.UI_STATE_NORMAL);
                handleUpdateTimeLineFullData((TrendRsp) msg.obj);
                break;
            case MSG_UPDATE_TIME_LINE_INCREMENT_DATA:
                handleUpdateTimeLineIncrementData((TrendRsp) msg.obj);
                break;
            case MSG_UPDATE_TICK_VIEW:
                mFragment.setTicksData((TickRsp) msg.obj);
                break;
            case MSG_UPDATE_LIVE_MSG:
                final SecLiveMsgRsp rsp = (SecLiveMsgRsp) msg.obj;
                final ArrayList<SecLiveMsg> secLiveMsgs = rsp.getVSecLiveMsg();
                if (secLiveMsgs != null && secLiveMsgs.size() > 0) {
                    mLastMsgId = secLiveMsgs.get(0).sId;
                    mFragment.updateLiveMsgView(secLiveMsgs);
                }
                break;
            case MSG_UPDATE_CAPITAL_FLOW:
                mFragment.setCapitalData((CapitalFlow) msg.obj);
                break;
            case MSG_UPDATE_CHIP_DIST:
                final HisChipDistRsp chipDistRsp = (HisChipDistRsp) msg.obj;
                mFragment.setChipDist(chipDistRsp);
                break;
            default:
                break;
        }
        return true;
    }

    private void handleUpdateTimeLineFullData(final TrendRsp trendRsp) {
        mFragment.setSupportAverage(trendRsp.bSupport);
        final ArrayList<TrendDesc> timeLineDatas = trendRsp.vTrendDesc;
        if (timeLineDatas != null && timeLineDatas.size() > 0) { // 一直没数据就一直拉取全量数据
            mFragment.updateTimeLineData(timeLineDatas);
            mTimeLineDatas = timeLineDatas;
        }
        mFragment.refreshValueView(LineChartTextureView.TYPE_TIME);
    }

    private void handleUpdateTimeLineIncrementData(final TrendRsp trendRsp) {
        final ArrayList<TrendDesc> incrementTimeLineData = trendRsp.vTrendDesc;
        if (incrementTimeLineData == null || incrementTimeLineData.size() == 0) { // 没有增量数据就不处理
            return;
        }

        final ArrayList<TrendDesc> oldTimeLineDatas = mTimeLineDatas;
        final int size = oldTimeLineDatas == null ? 0 : oldTimeLineDatas.size();
        if (size == 0) { // 如果没有全量数据，就直接拉取全量数据
            return;
        }

        int index = -1;
        TrendDesc oldData;
        final TrendDesc firstIncrementData = incrementTimeLineData.get(0);
        // 查询最后一个数据在新数据里的位置，以便连接上
        for (int i = size - 1; i >= 0; i--) {
            oldData = oldTimeLineDatas.get(i);
            if (oldData.iMinute == firstIncrementData.iMinute) {
                index = i;
                break;
            }
        }

        ArrayList<TrendDesc> newTimeLineDatas;
        if (index < 0) {// 未找到重复的数据，就直接连接
            oldTimeLineDatas.addAll(incrementTimeLineData);
            newTimeLineDatas = oldTimeLineDatas;
        } else { // 有部分重复的数据
            newTimeLineDatas = new ArrayList<TrendDesc>(oldTimeLineDatas.subList(0, index));
            newTimeLineDatas.addAll(incrementTimeLineData);
        }
        mFragment.updateTimeLineData(newTimeLineDatas);
        mTimeLineDatas = newTimeLineDatas;
        mFragment.refreshValueView(LineChartTextureView.TYPE_TIME);
    }
}
