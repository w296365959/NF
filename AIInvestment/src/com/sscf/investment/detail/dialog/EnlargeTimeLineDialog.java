package com.sscf.investment.detail.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.detail.view.IndicatorAnimationView;
import com.sscf.investment.detail.view.TimeLineInfosView;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.linechart.TimeLineChartViewHelper;
import com.sscf.investment.widget.linechart.TimeLineConsts;
import com.sscf.investment.widget.linechart.TimeLineInfo;

import java.util.ArrayList;

import BEC.E_TREND_REQ_TYPE;
import BEC.SecQuote;
import BEC.TrendDesc;
import BEC.TrendReq;
import BEC.TrendRsp;

/**
 * Created by yorkeehuang on 2017/4/24.
 */

public class EnlargeTimeLineDialog extends BottomDoubleButtonDialog implements View.OnClickListener, Runnable, DialogInterface.OnDismissListener, DataSourceProxy.IRequestCallback {

    private static final String TAG = EnlargeTimeLineDialog.class.getSimpleName();

    private static final int DEFAULT_ENLARGE_TRADING_MINUTES = 30 * 20 + 1;

    private IndicatorAnimationView mTimeLineIndicator;
    private int mStartTime;
    private int mEndTime;
    private PeriodicHandlerManager mPeriodicHandlerManager;


    private int mOpenTime = TimeLineConsts.DEFAULT_OPEN_TIME;
    private int mCloseTime = TimeLineConsts.DEFAULT_CLOSE_TIME;

    private int mMinute = -1;

    private static final int FIRST_HALF_CLOSE_TIME = StringUtil.enlargeMinuteToHms(113000);
    private static final int SECOND_HALF_OPEN_TIME =  StringUtil.enlargeMinuteToHms(130000);

    public EnlargeTimeLineDialog(@NonNull Context context) {
        super(context, ENLARGE_TIME_LINE);
        initView();
        initTimeLineInfo();
        setOnDismissListener(this);
    }

    private void initView() {
        mTimeLineIndicator = (IndicatorAnimationView) findViewById(R.id.line_chart_indicator);
        mSurface.setTradingMinutes(DEFAULT_ENLARGE_TRADING_MINUTES);
        mSurface.setTimeValueChangeListener(point ->
                runOnUiThread(() ->
                        mTimeLineIndicator.updateCenterPoint(point)));
        mPreButton.setText(R.string.pre_15m_text);
        mNextButton.setText(R.string.next_15m_text);
        DtLog.d(TAG, "hasOnClickListeners() = " + findViewById(R.id.close_button).hasOnClickListeners());
    }

    public void setOpenCloseTime(int openTime, int closeTime) {
        mOpenTime = openTime;
        mCloseTime = closeTime;
    }

    private void initTimeLineInfo() {
        mTimeLineInfo.setIsRefYesterdayClose(false);
    }

    @Override
    public void onResume() {
        DtLog.d(TAG, "onResume()");
        super.onResume();
        resume();
    }

    @Override
    public void onPause() {
        DtLog.d(TAG, "onPause()");
        super.onPause();
        stop(false);
    }

    private PeriodicHandlerManager getPeriodicHandlerManager() {
        if(mPeriodicHandlerManager == null) {
            mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        }
        return mPeriodicHandlerManager;
    }

    @Override
    protected void updateValue() {
        SecDetailInfo info = getDetailInfo();
        if(info == null) {
            return;
        }
        updateByQuote(info, mSecQuote);
        super.updateValue();
        if(info != null) {
            boolean isSuspended = info.isSuspended();
            String deltaPercent = "--";
            int color = mGrayColor;
            float close = info.getYesterdayClose();
            if(!isSuspended && close > 0) {
                float delta = info.getClose() - close;
                deltaPercent = StringUtil.getChangePercentString(delta / close).toString();
                if (delta > 0) {
                    color = mRedColor;
                } else if (delta == 0) {
                    color = mGrayColor;
                } else {
                    color = mGreenColor;
                }
            }
            mChangeRateView.setTextColor(color);
            mChangeRateView.setText(deltaPercent);

            String turnover;
            if(isSuspended) {
                turnover = "--";
            } else {
                turnover = StringUtil.getPercentString(info.getTurnover());
            }
            mTurnoverView.setText(turnover);
        }
    }

    public int getMinute() {
        return mMinute;
    }

    public void setEvent(final TimeLineInfosView.TimeLineTouchEvent event) {
        runOnUiThread(() -> {
            if(event != null) {
                mMinute = event.getMinute();
                updateValue();
                updateButton(event.getIndex(), event.getTimeLineStart(), event.getTimeLineEnd());
            }
        });
    }

    @Override
    public boolean updateQuote(SecQuote secQuote) {
        if(super.updateQuote(secQuote)) {
            updateByQuote(getDetailInfo(), secQuote);
            return true;
        }
        return false;
    }

    private void updateByQuote(SecDetailInfo info, SecQuote quote) {
        if(info != null && quote != null) {
            info.setIsSuspended(StockUtil.isSuspended(quote));
            info.setYesterdayClose(quote.getFClose());
            info.setOpen(quote.getFOpen());
            info.setHigh(quote.getFMax());
            info.setLow(quote.getFMin());
            info.setClose(quote.getFNow());
            info.setAmount((long) quote.getFAmout());
            final float yesterdayClose = quote.getFClose() > 0 ? quote.getFClose() : quote.getFOpen();
            final float delta = (quote.getFNow() - yesterdayClose) / yesterdayClose;
            info.setDelta(delta);
            info.setVolume(quote.getLVolume());
            info.setTurnover(quote.getFFhsl());
        }
    }

    private TimeLineInfo updateTimeLineInfo() {
        mTimeLineInfo.setIsRefYesterdayClose(false);
        SecDetailInfo detailInfo = getDetailInfo();
        if(detailInfo != null) {
            mTimeLineInfo.setYesterdayClose(detailInfo.getYesterdayClose());
        }
        return mTimeLineInfo;
    }

    public void setTrendRsp(TimeLineInfosView.TimeLineTouchEvent event, TrendRsp trendRsp) {
        runOnUiThread(() -> {
            if(StockUtil.isSuspended(mSecQuote)) {
                return;
            }
            setEvent(event);
            if(trendRsp != null) {
                SecDetailInfo detailInfo = getDetailInfo();
                if(detailInfo != null) {
                    final int startTime = trendRsp.getIStartTime();
                    final int endTime = trendRsp.getIEndTime();
                    ArrayList<TrendDesc> entities = trendRsp.getVTrendDesc();
                    if(entities != null) {
                        DtLog.d(TAG, "setTrendRsp() startTime:" + startTime + ", endTime:" + endTime + ", size = " + entities.size());
                        setStartEndTime(startTime, endTime);
                        TimeLineInfo info = updateTimeLineInfo();
                        if(info != null) {
                            setTimeLineEntities(info, TimeLineChartViewHelper.adaptEntites(entities,
                                    detailInfo.getYesterdayClose(), startTime, endTime));
                            loadDelayIfNeed();
                        }
                    }
                }
            }
        });
    }


    private TrendDesc copyEntity(TrendDesc oldEntity) {
        TrendDesc newEntity = new TrendDesc();
        newEntity.setIMinute(oldEntity.getIMinute());
        newEntity.setFNow(oldEntity.getFNow());
        newEntity.setFAverage(oldEntity.getFAverage());
        return newEntity;
    }

    private void loadLatestData(int minute) {
        if(mSecQuote != null) {
            if(mTimeLineEntities != null && !mTimeLineEntities.isEmpty()) {
                TrendDesc lastEntity = mTimeLineEntities.get(mTimeLineEntities.size() - 1);
                TrendReq req = new TrendReq();
                req.setSDtSecCode(mSecQuote.getSDtSecCode());
                req.setIMinute(StringUtil.timeMinuteToEnlargeMinute(TimeLineChartViewHelper.adaptMinute(minute, mOpenTime, mCloseTime)));
                req.setIReqDataMinute(lastEntity.getIMinute());
                DtLog.d(TAG, "loadLatestData: minute = " + req.getIMinute() + ", reqDataMinute = " + req.getIReqDataMinute());
                req.setETrendReqType(E_TREND_REQ_TYPE.E_TRT_TRADING);
                req.setVGuid(DengtaApplication.getApplication().getAccountManager().getGuid());
                DataEngine.getInstance().request(EntityObject.ET_GET_ENLARGE_TREND, req, this);
            }
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_ENLARGE_TREND:
                if(StockUtil.isSuspended(mSecQuote)) {
                    return;
                }
                runOnUiThread(() -> handleTrendRsp(success, data));
                break;
            default:
        }
    }

    private void loadDelayIfNeed() {
        loadDelayIfNeed(false);
    }

    private void loadDelayIfNeed(boolean forceLoad) {
        PeriodicHandlerManager manager = getPeriodicHandlerManager();
        if(needRefresh(forceLoad)) {
            manager.runPeriodicDelay(3000);
        } else {
            mTimeLineIndicator.stopAnimation();
            manager.stop();
        }
    }

    private void handleTrendRsp(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            TrendRsp trendRsp = (TrendRsp) data.getEntity();
            if(trendRsp.getIStartTime() == mStartTime && trendRsp.getIEndTime() == mEndTime) {
                ArrayList<TrendDesc> newEntites = trendRsp.getVTrendDesc();
                if(newEntites != null && !newEntites.isEmpty()) {
                    TrendDesc firstNewEntity = newEntites.get(0);
                    TrendDesc lastOldEntiry = mTimeLineEntities.get(mTimeLineEntities.size() - 1);
                    DtLog.d(TAG, "handleTrendRsp: firstNew:" + firstNewEntity.getIMinute() + ", lastOld:" + lastOldEntiry.getIMinute());
                    if(firstNewEntity.getIMinute() == lastOldEntiry.getIMinute()) {
                        mTimeLineEntities.remove(mTimeLineEntities.size() - 1);
                        mTimeLineEntities.addAll(newEntites);
                        mSurface.setTimeLineEntities(mTimeLineInfo, mTimeLineEntities);
                    }
                }
            }
        }
    }

    private void setStartEndTime(int startTime, int endTime) {
        runOnUiThread(() -> {
            mStartTime = startTime;
            mEndTime = endTime;
            if(mSurface != null) {
                int startHms = StringUtil.enlargeMinuteToHms(startTime);
                int endHms = StringUtil.enlargeMinuteToHms(endTime);

                boolean includeHalf = (FIRST_HALF_CLOSE_TIME >= startHms && FIRST_HALF_CLOSE_TIME < endHms)
                        || (SECOND_HALF_OPEN_TIME > startHms && SECOND_HALF_OPEN_TIME <= endHms);
                int tradingMinutes = (endHms - startHms) / TimeLineConsts.TIME_INTERVAL + 1;

                if(includeHalf) {
                    tradingMinutes -= TimeLineConsts.HALF_INTERVAL;
                }
                DtLog.d(TAG, "setTradingMinutes() tradingMinutes = " + tradingMinutes);
                mSurface.setTradingMinutes(tradingMinutes);
                mSurface.setTradingTime(StringUtil.enlargeMinuteToHmStr(startTime), "", StringUtil.enlargeMinuteToHmStr(endTime));
            }
        });
    }

    private boolean needRefresh(boolean forceLoad) {
         if(mTimeLineEntities != null && !mTimeLineEntities.isEmpty()
                 && mSecQuote != null &&!StockUtil.isSuspended(mSecQuote)) {
             int marketType = StockUtil.getMarketType(mSecQuote.getSDtSecCode());
             boolean isTrading = DengtaApplication.getApplication().getTradingStateManager().isTrading(marketType);
            if(forceLoad || isTrading) {
                TrendDesc entity = mTimeLineEntities.get(mTimeLineEntities.size() - 1);
                int lastEntityHms = StringUtil.enlargeMinuteToHms(entity.getIMinute());
                int endTimeHms = StringUtil.enlargeMinuteToHms(mEndTime);
                DtLog.d(TAG, "needRefresh() lastEntityHms = " + lastEntityHms + ", endTimeHms = " + endTimeHms);
                return lastEntityHms < endTimeHms - TimeLineConsts.TIME_INTERVAL;
            }
        }
        return false;
    }

    @Override
    public void run() {
        runOnUiThread(() -> {
            mTimeLineIndicator.startAnimation();
            loadLatestData(getMinute());
            loadDelayIfNeed();
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        DtLog.d(TAG, "onDismiss()");
        stop(true);
    }

    private void stop(boolean release) {
        DtLog.d(TAG, "stop() release = " + release);
        mTimeLineIndicator.stopAnimation();
        if(mPeriodicHandlerManager != null) {
            mPeriodicHandlerManager.stop();
            if(release) {
                mPeriodicHandlerManager = null;
            }
        }
    }

    private void resume() {
        DtLog.d(TAG, "resume()");
        loadDelayIfNeed(true);
    }
}
