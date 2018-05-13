package com.sscf.investment.detail.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TwoTabSelectorView;
import com.sscf.investment.detail.entity.BuySellEntity;
import com.sscf.investment.detail.presenter.TimelineDetailPresenter;
import com.sscf.investment.detail.view.BuySellView;
import com.sscf.investment.detail.view.IndicatorAnimationView;
import com.sscf.investment.detail.view.InnerScrollView;
import com.sscf.investment.detail.view.TicksView;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.widget.linechart.TimeLineChartViewHelper;
import com.sscf.investment.widget.linechart.TimeLineConsts;

import java.util.ArrayList;

import BEC.E_TREND_REQ_TYPE;
import BEC.SecQuote;
import BEC.TrendDesc;
import BEC.TrendReq;
import BEC.TrendRsp;

/**
 * Created by yorkeehuang on 2017/4/25.
 */

public class CallauctionDialog extends BottomDialog implements Runnable, DialogInterface.OnDismissListener, DataSourceProxy.IRequestCallback {

    private static final String TAG = CallauctionDialog.class.getSimpleName();

    private static final int STATE_STOP = 0;
    private static final int STATE_LOADING_DATA = 1;
    private static final int STATE_ANIMATOR = 2;

    private int mStartTime;
    private int mEndTime;

    private TextView mPriceView;
    private TextView mChangeValueView;
    private TextView mChangeRateView;

    private TwoTabSelectorView mTabSelector;
    private BuySellView mBuySellView;
    private TicksView mTicksView;
    private InnerScrollView mTicksScrollView;
    private IndicatorAnimationView mTimeLineIndicator;
    private FrameLayout mTicksFrameLayout;
    private PeriodicHandlerManager mPeriodicHandlerManager;
    private final TimelineDetailPresenter mPresenter;

    public CallauctionDialog(@NonNull Context context, TimelineDetailPresenter presenter) {
        super(context, CALLAUCTION_LINE);
        mPresenter = presenter;
        DeviceUtil.enableDialogTranslucentStatus(getWindow(), context.getResources().getColor(R.color.black_40));
        initTimeLineInfo();
        initView();
        setOnDismissListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_callauction;
    }

    private void initTimeLineInfo() {
        mTimeLineInfo.setIsRefYesterdayClose(false);
    }

    private void initView() {
        mTimeLineIndicator = (IndicatorAnimationView) findViewById(R.id.line_chart_indicator);
        mSurface.setTimeValueChangeListener(point ->
                runOnUiThread(() -> mTimeLineIndicator.updateCenterPoint(point)));
        mPriceView = (TextView) findViewById(R.id.price);
        mChangeValueView = (TextView) findViewById(R.id.change_value);
        mChangeRateView = (TextView) findViewById(R.id.change_rate);

        mTabSelector = (TwoTabSelectorView) findViewById(R.id.tab_selector);
        mTabSelector.setAttributes(R.attr.transaction_two_tab_textColor, R.attr.transaction_two_tab_selector_view_bg_drawable,
                R.attr.transaction_two_tab_selector_selected_bg_left_drawable, R.attr.transaction_two_tab_selector_selected_bg_right_drawable,
                R.attr.transaction_two_tab_selector_selected_bg_middle_drawable);
        mTabSelector.setTabTitles(R.string.transaction_buy_sell, R.string.transaction_ticks);
        mTabSelector.setTabTitlesSize(R.dimen.font_size_14, R.dimen.font_size_14);
        mTabSelector.setOnTabSelectedListener(new TwoTabSelectorView.OnTabSelectedListener() {
            @Override
            public void onFirstTabSelected() {
                mBuySellView.setVisibility(View.VISIBLE);
                mTicksScrollView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSecondTabSelected() {
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TICKS_CLICKED);
                mBuySellView.setVisibility(View.INVISIBLE);
                mTicksScrollView.setVisibility(View.VISIBLE);

                // 切换成交明细，手动更新一次
                if (mSecQuote != null) {
                    mPresenter.requestTick(mSecQuote.sDtSecCode);
                }
            }

            @Override
            public void onThirdTabSelected() {
            }
        });
        mBuySellView = (BuySellView) findViewById(R.id.buy_sell);
        mBuySellView.setOnClickListener(this);

        mTicksScrollView = (InnerScrollView) findViewById(R.id.ticks_layout);
        mTicksFrameLayout = (FrameLayout) findViewById(R.id.ticks_container);
        mTicksFrameLayout.setOnClickListener(this);
        mTicksView = (TicksView) findViewById(R.id.ticks);
    }

    public void onResume() {
        DtLog.d(TAG, "onResume()");
        super.onResume();
        resume();
    }

    public void onPause() {
        DtLog.d(TAG, "onPause()");
        super.onPause();
        stop(false);
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

    public void setTicksData(ArrayList<BEC.TickDesc> tickData) {
        mTicksView.setTicksData(tickData);
    }

    public void setTrendRsp(TrendRsp trendRsp) {
        runOnUiThread(() -> {
            if(StockUtil.isSuspended(mSecQuote)) {
                return;
            }
            if(trendRsp != null) {
                SecDetailInfo detailInfo = getDetailInfo();
                if(detailInfo != null) {
                    final int startTime = trendRsp.getIStartTime();
                    final int endTime = trendRsp.getIEndTime();
                    ArrayList<TrendDesc> entities = trendRsp.getVTrendDesc();
                    if(entities != null) {
                        setStartEndTime(startTime, endTime);
                        updateTimeLineInfo();
                        setTimeLineEntities(mTimeLineInfo, TimeLineChartViewHelper.adaptEntites(entities,
                                detailInfo.getYesterdayClose(), startTime, endTime));
                        loadDelayIfNeed();
                    }
                }
            }
        });
    }

    private void updateTimeLineInfo() {
        SecDetailInfo detailInfo = getDetailInfo();
        if(detailInfo != null) {
            mTimeLineInfo.setHigh(detailInfo.getHigh());
            mTimeLineInfo.setLow(detailInfo.getLow());
            mTimeLineInfo.setIsSuspended(detailInfo.isSuspended());
            mTimeLineInfo.setYesterdayClose(detailInfo.getYesterdayClose());
        }
    }

    private void setStartEndTime(int startTime, int endTime) {
        mStartTime = startTime;
        mEndTime = endTime;
        if(mSurface != null) {
            int tradingMinutes = (StringUtil.enlargeMinuteToHms(endTime)
                    - StringUtil.enlargeMinuteToHms(startTime)) / TimeLineConsts.TIME_INTERVAL + 1;
            DtLog.d(TAG, "setTradingMinutes() tradingMinutes = " + tradingMinutes);
            mSurface.setTradingMinutes(tradingMinutes);
            mSurface.setTradingTime(StringUtil.enlargeMinuteToHmStr(startTime), "", StringUtil.enlargeMinuteToHmStr(endTime));
        }
    }

    private void updateView(SecQuote secQuote) {
        if(secQuote != null) {
            SecDetailInfo info = getDetailInfo();
            updateByQuote(info, mSecQuote);
            boolean isSuspended = StockUtil.isSuspended(secQuote);
            int color;
            String price;
            float delta = secQuote.getFNow() - secQuote.getFClose();
            if(isSuspended) {
                price = "停牌";
                color = mGrayColor;
            } else {
                if (delta > 0) {
                    color = mRedColor;
                } else if (delta == 0) {
                    color = mGrayColor;
                } else {
                    color = mGreenColor;
                }
                price = StringUtil.getFormattedFloat(secQuote.getFNow(), secQuote.getITpFlag());
            }
            mPriceView.setTextColor(color);
            mPriceView.setText(price);

            String deltaValue;
            if(isSuspended) {
                deltaValue = "";
            } else {
                deltaValue = StringUtil.getFormattedFloat(delta, secQuote.getITpFlag());
                if (delta > 0) {
                    deltaValue = "+" + deltaValue;
                }
            }
            mChangeValueView.setTextColor(color);
            mChangeValueView.setText(deltaValue);

            String deltaPercent;
            if(isSuspended || secQuote.getFClose() > 0) {
                deltaPercent = "";
            } else {
                deltaPercent = StringUtil.getChangePercentString(delta / secQuote.getFClose()).toString();
            }
            mChangeRateView.setTextColor(color);
            mChangeRateView.setText(deltaPercent);

            mTicksView.setYesterdayClose(secQuote.getFClose());
            setBuySellData(secQuote);
        }
    }

    @Override
    public boolean updateQuote(SecQuote secQuote) {
        if(super.updateQuote(secQuote)) {
            if (secQuote != null) {
                mTicksView.setTpFlag(secQuote.iTpFlag);
            }
            updateByQuote(initDetailInfo(), secQuote);
            updateView(secQuote);
            return true;
        }
        return false;
    }

    private void updateByQuote(SecDetailInfo info, SecQuote quote) {
        if(info != null && quote != null) {
            info.setIsSuspended(StockUtil.isSuspended(mSecQuote));
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

    private void setBuySellData(SecQuote quote) {
        final BuySellEntity buySellEntity = new BuySellEntity(quote);
        if (mBuySellView != null) {
            mBuySellView.setData(buySellEntity);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.buy_sell:
                mTabSelector.switchTab(1);
                break;
            case R.id.ticks_container:
                mTabSelector.switchTab(2);
                break;
            default:
        }
    }

    private PeriodicHandlerManager getPeriodicHandlerManager() {
        if(mPeriodicHandlerManager == null) {
            mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        }
        return mPeriodicHandlerManager;
    }

    private void loadDelayIfNeed() {
        loadDelayIfNeed(false);
    }

    private void loadDelayIfNeed(boolean forceLoad) {
        PeriodicHandlerManager manager = getPeriodicHandlerManager();
        int refreshState = getRefreshState(forceLoad);
        DtLog.d(TAG, "loadDelayIfNeed() refreshState = " + refreshState);
        switch (refreshState) {
            case STATE_LOADING_DATA:
                manager.runPeriodicDelay(3000);
                break;
            case STATE_ANIMATOR:
                mTimeLineIndicator.startAnimation();
                manager.runPeriodicDelay(3000);
                break;
            default:
                mTimeLineIndicator.stopAnimation();
                manager.stop();
        }
    }

    public int getRefreshState(boolean forceLoad) {
        if(mTimeLineEntities != null && !mTimeLineEntities.isEmpty() && mSecQuote != null) {
            int marketType = StockUtil.getMarketType(mSecQuote.getSDtSecCode());
            boolean isCallauction = DengtaApplication.getApplication().getTradingStateManager().isInCallauction(marketType);
            TrendDesc entity = mTimeLineEntities.get(mTimeLineEntities.size() - 1);
            DtLog.d(TAG, "needRefresh(), isCallauction = " + isCallauction + ", forceLoad = " + forceLoad
                    + ", entity.getIMinute() = " + entity.getIMinute() + ", mEndTime = " + mEndTime);
            if(isCallauction) {
                return entity.getIMinute() < mEndTime ? STATE_ANIMATOR : STATE_STOP;
            } else if(forceLoad) {
                return entity.getIMinute() < mEndTime ? STATE_LOADING_DATA : STATE_STOP;
            }
        }
        return STATE_STOP;
    }

    @Override
    public void run() {
        runOnUiThread(() -> {
            mTimeLineIndicator.startAnimation();
            loadLatestData();
            loadDelayIfNeed();
        });
    }

    private void loadLatestData() {
        if(mSecQuote != null) {
            DtLog.d(TAG, "loadLatestData");
            if(mTimeLineEntities != null && !mTimeLineEntities.isEmpty()) {
                TrendDesc lastEntity = mTimeLineEntities.get(mTimeLineEntities.size() - 1);
                TrendReq req = new TrendReq();
                req.setSDtSecCode(mSecQuote.getSDtSecCode());
                req.setIReqDataMinute(lastEntity.getIMinute());
                req.setETrendReqType(E_TREND_REQ_TYPE.E_TRT_CALLAUCTION);
                req.setVGuid(DengtaApplication.getApplication().getAccountManager().getGuid());
                DataEngine.getInstance().request(EntityObject.ET_GET_CALLAUCTION_TREND, req, this);
            }
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_CALLAUCTION_TREND:
                runOnUiThread(() -> handleTrendRsp(success, data));
                break;
            default:
        }
    }

    private void handleTrendRsp(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            TrendRsp trendRsp = (TrendRsp) data.getEntity();
            final int startTime = trendRsp.getIStartTime();
            final int endTime = trendRsp.getIEndTime();
            if(startTime == mStartTime && endTime == mEndTime) {
                ArrayList<TrendDesc> newEntites = trendRsp.getVTrendDesc();
                if(newEntites != null && !newEntites.isEmpty()) {
                    TrendDesc firstNewEntity = newEntites.get(0);
                    TrendDesc lastOldEntiry = mTimeLineEntities.get(mTimeLineEntities.size() - 1);
                    if(firstNewEntity.getIMinute() == lastOldEntiry.getIMinute()) {
                        mTimeLineEntities.remove(mTimeLineEntities.size() - 1);
                        mTimeLineEntities.addAll(newEntites);
                        SecDetailInfo detailInfo = getDetailInfo();
                        if(detailInfo != null) {
                            mSurface.setTimeLineEntities(mTimeLineInfo,
                                    TimeLineChartViewHelper.adaptEntites(mTimeLineEntities,
                                            detailInfo.getYesterdayClose(), startTime, endTime));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        DtLog.d(TAG, "onDismiss()");
        stop(true);
    }
}
