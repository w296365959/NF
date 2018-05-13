package com.sscf.investment.detail.model;

import android.text.TextUtils;

import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.detail.presenter.TimeLineComparePresenter;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.EntityUtil;

import BEC.TrendRsp;

/**
 * Created by yorkeehuang on 2017/12/13.
 */

public class TimeLineCompareModel implements DataSourceProxy.IRequestCallback {

    private static final String TAG = TimeLineCompareModel.class.getSimpleName();

    private TimeLineComparePresenter mPresenter;

    public TimeLineCompareModel(final TimeLineComparePresenter presenter) {
        mPresenter = presenter;
    }

    public void requestQuote(final String dtSecCode) {
        DtLog.d(TAG, "requestQuote() : " + dtSecCode);
        QuoteRequestManager.getQuoteRequest(dtSecCode, this, null);
    }

    public void loadTimeLineData(String dtSecCode, int start) {
        DtLog.d(TAG, "loadTimeLineData() : " + dtSecCode + ", start = " + start);
        if (start <= 0) {
            QuoteRequestManager.getTimeLineDataRequest(dtSecCode, this);
        } else {
            QuoteRequestManager.getTimeLineDataRequest(dtSecCode, start, this);
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_QUOTE:
                mPresenter.onGetQuote(EntityUtil.entityToSecQuote(success, data));
                break;
            case EntityObject.ET_GET_TREND:
                if(success && data.getEntity() != null) {
                    final TrendRsp trendRsp = (TrendRsp) data.getEntity();
                    final boolean isFull = TextUtils.isEmpty((String) data.getExtra());
                    mPresenter.onGetTimeLineData(isFull, trendRsp);
                }
                break;
            default:
                break;
        }
    }
}
