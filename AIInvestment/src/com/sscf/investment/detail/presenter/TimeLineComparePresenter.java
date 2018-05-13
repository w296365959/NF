package com.sscf.investment.detail.presenter;

import android.os.Handler;
import android.os.Looper;

import com.sscf.investment.detail.model.TimeLineCompareModel;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.ThreadUtils;

import java.util.ArrayList;

import BEC.SecQuote;
import BEC.TrendDesc;
import BEC.TrendRsp;

/**
 * Created by yorkeehuang on 2017/12/13.
 */

public class TimeLineComparePresenter {

    private static final String TAG = TimeLineComparePresenter.class.getSimpleName();

    private static final int LOADING_TIMELINE_DATA_INTERVAL = 1000;

    private final String mDtSecCode;

    private TimeLineCompareModel mModel;

    protected final Handler mHandler;

    private ITimeLineCompareDisplayer mDisplayer;

    private ArrayList<TrendDesc> mTimeLineDatas;

    private SecQuote mSecQuote;

    private long loadTimeLineTimeStamp = 0L;

    public TimeLineComparePresenter(ITimeLineCompareDisplayer displayer, String dtSecCode) {
        mDisplayer = displayer;
        mModel = new TimeLineCompareModel(this);
        mDtSecCode = dtSecCode;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public String getDtSecCode() {
        return mDtSecCode;
    }

    public void releaseDiplayer() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDisplayer = null;
            }
        });
    }

    /**
     * @return 如果小于等于0，全量拉取
     */
    private int getTimeLineDatasStart() {
        final ArrayList<TrendDesc> timeLineDatas = mTimeLineDatas;
        final int size = timeLineDatas == null ? 0 : timeLineDatas.size();
        return size - 2;
    }

    private void loadTimeLineData() {
        if(mSecQuote != null) {
            long now = System.currentTimeMillis();
            if(now - loadTimeLineTimeStamp > LOADING_TIMELINE_DATA_INTERVAL) {
                loadTimeLineTimeStamp = now;
                int start = getTimeLineDatasStart();
                DtLog.d(TAG, "loadTimeLineData() secName = " + mSecQuote.getSSecName() + ", start = " + start);
                mModel.loadTimeLineData(mDtSecCode, start);
            }
        }
    }

    public void loadQuote() {
        if(mSecQuote == null) {
            mModel.requestQuote(mDtSecCode);
        } else {
            mModel.requestQuote(mDtSecCode);
            loadTimeLineData();
        }
    }

    private void handleUpdateTimeLineFullData(final TrendRsp trendRsp) {
        final ArrayList<TrendDesc> timeLineDatas = trendRsp.vTrendDesc;
        if (timeLineDatas != null && timeLineDatas.size() > 0) { // 一直没数据就一直拉取全量数据
            mDisplayer.updateCompareTimeEntities(timeLineDatas);
            mTimeLineDatas = timeLineDatas;
        }
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
            newTimeLineDatas = new ArrayList<>(oldTimeLineDatas.subList(0, index));
            newTimeLineDatas.addAll(incrementTimeLineData);
        }
        mDisplayer.updateCompareTimeEntities(newTimeLineDatas);
        mTimeLineDatas = newTimeLineDatas;
    }

    private boolean checkDisplayer() {
        return mDisplayer != null;
    }

    public void onGetTimeLineData(boolean isFull, TrendRsp trendRsp) {
        ThreadUtils.runOnUiThread(() -> {
            if(checkDisplayer()) {
                if (isFull) { // 全量分时数据
                    handleUpdateTimeLineFullData(trendRsp);
                } else { // 增量分时数据
                    handleUpdateTimeLineIncrementData(trendRsp);
                }
            }
        });
    }

    public void onGetQuote(SecQuote secQuote) {
        ThreadUtils.runOnUiThread(() -> {
            if(checkDisplayer()) {
                if(secQuote != null) {
                    mDisplayer.updateCompareQuote(secQuote);
                    DtLog.d(TAG, "onGetQuote() mSecQuote name = " + (mSecQuote != null ? mSecQuote.getSSecName() : "null")
                            + ", secQuote name = " + secQuote.getSSecName());
                    if(mSecQuote == null || mSecQuote.getFClose() != secQuote.getFClose()) {
                        mSecQuote = secQuote;
                        mTimeLineDatas = null;
                        loadTimeLineData();
                    } else {
                        mSecQuote = secQuote;
                        if(mTimeLineDatas != null) {
                            mDisplayer.updateCompareTimeEntities(mTimeLineDatas);
                        }
                    }
                }
            } else {
                DtLog.d(TAG, "onGetQuote() mDisplayer == null");
            }
        });
    }

    public interface ITimeLineCompareDisplayer {
        void updateCompareTimeEntities(ArrayList<TrendDesc> timeLineDatas);

        void updateCompareQuote(SecQuote quote);
    }
}
