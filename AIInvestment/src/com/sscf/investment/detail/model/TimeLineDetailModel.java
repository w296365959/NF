package com.sscf.investment.detail.model;

import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.detail.presenter.TimelineDetailPresenter;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.linechart.TimeLineChartViewHelper;
import BEC.E_TREND_REQ_TYPE;
import BEC.RtMinReq;
import BEC.TickRsp;
import BEC.TrendReq;

/**
 * Created by yorkeehuang on 2017/5/11.
 */

public class TimeLineDetailModel implements DataSourceProxy.IRequestCallback {

    private static final String TAG = TimeLineDetailModel.class.getSimpleName();
    private TimelineDetailPresenter mPresenter;

    public TimeLineDetailModel(TimelineDetailPresenter presenter) {
        mPresenter = presenter;
    }

    public void loadHistoryTimeLineData(String dtSecCode, String date) {
        DtLog.d(TAG, "loadHistoryTimeLineData: " + date);
        RtMinReq req = new RtMinReq(dtSecCode, 1, date, 0);
        DataEngine.getInstance().request(EntityObject.ET_GET_RTMIN_DATA_EX, req, this);
    }

    public void loadEnlargeTimeLineData(String dtSecCode, int minute, String openTimeStr, String closeTimeStr) {
        DtLog.d(TAG, "loadEnlargeTimeLineData: " + minute);
        TrendReq req = new TrendReq();
        req.setSDtSecCode(dtSecCode);
        int openTime = StringUtil.time2Minutes(openTimeStr);
        int closeTime = StringUtil.time2Minutes(closeTimeStr);
        int adaptedMinite = TimeLineChartViewHelper.adaptMinute(minute, openTime, closeTime);
        req.setIMinute(StringUtil.timeMinuteToEnlargeMinute(adaptedMinite));
        req.setETrendReqType(E_TREND_REQ_TYPE.E_TRT_TRADING);
        req.setVGuid(DengtaApplication.getApplication().getAccountManager().getGuid());
        DataEngine.getInstance().request(EntityObject.ET_GET_ENLARGE_TREND, req, this);
    }

    public void loadEarlyEnlargeTimeLineData(String dtSecCode, String openTimeStr, String closeTimeStr) {
        loadEnlargeTimeLineData(dtSecCode, 0, openTimeStr, closeTimeStr);
    }

    public void loadCallauctionData(String dtSecCode) {
        DtLog.d(TAG, "loadCallauctionData");
        TrendReq req = new TrendReq();
        req.setSDtSecCode(dtSecCode);
        req.setETrendReqType(E_TREND_REQ_TYPE.E_TRT_CALLAUCTION);
        req.setVGuid(DengtaApplication.getApplication().getAccountManager().getGuid());
        DataEngine.getInstance().request(EntityObject.ET_GET_CALLAUCTION_TREND, req, this);
    }

    public void requestTick(String dtSecCode) {
        QuoteRequestManager.getTickDataRequest(dtSecCode, this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_RTMIN_DATA_EX:
                mPresenter.handleHistoryTimeLine(success, data);
                break;
            case EntityObject.ET_GET_ENLARGE_TREND:
                mPresenter.handleEnlargeTimeLine(success, data);
                break;
            case EntityObject.ET_GET_CALLAUCTION_TREND:
                mPresenter.handleCallauction(success, data);
                break;
            case EntityObject.ET_QUERY_STOCK_TICK:
                final TickRsp tickRsp = success ? (TickRsp) data.getEntity() : null;
                mPresenter.onGetTickData(tickRsp);
            default:
                break;
        }
    }

}
