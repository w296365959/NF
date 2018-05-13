package com.sscf.investment.detail.model;

import android.text.TextUtils;
import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.detail.presenter.LandScapeDetailPresenter;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.EntityUtil;

import BEC.HisChipDistRsp;
import BEC.SecLiveMsgRsp;
import BEC.TickRsp;
import BEC.TrendRsp;

/**
 * Created by davidwei on 2017/05/11
 */
public final class LandScapeDetailModel implements DataSourceProxy.IRequestCallback {
    private final LandScapeDetailPresenter mPresenter;

    public LandScapeDetailModel(final LandScapeDetailPresenter presenter) {
        mPresenter = presenter;
    }

    public void requestQuote(final String dtSecCode) {
        QuoteRequestManager.getQuoteRequest(dtSecCode, this, null);
    }

    public void requestTimeLineData(final String dtSecCode, final int num) {
        if (num <= 0) {
            QuoteRequestManager.getTimeLineDataRequest(dtSecCode, this);
        } else {
            QuoteRequestManager.getTimeLineDataRequest(dtSecCode, num, this);
        }
    }

    public void requestTick(final String dtSecCode) {
        QuoteRequestManager.getTickDataRequest(dtSecCode, this);
    }

    public void requestLiveMsg(final String dtSecCode, final String lastId) {
        QuoteRequestManager.getLiveMsgRequest(dtSecCode, lastId, this);
    }

    public void requestCapitalData(final String dtSecCode) {
        QuoteRequestManager.getCapitalDataRequest(dtSecCode, this);
    }

    public void requestChipDist(final String dtSecCode) {
        QuoteRequestManager.getChipDistRequest(dtSecCode, 2, this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_QUOTE:
                mPresenter.onGetQuote(EntityUtil.entityToSecQuote(success, data));
                break;
            case EntityObject.ET_GET_TREND:
                TrendRsp trendRsp = null;
                if (success) {
                    trendRsp = (TrendRsp) data.getEntity();
                }
                if (TextUtils.isEmpty((CharSequence) data.getExtra())) { // 全量分时数据
                    mPresenter.onGetTimeLineFullData(trendRsp);
                } else { // 增量分时数据
                    mPresenter.onGetTimeLineIncrementData(trendRsp);
                }
                break;
            case EntityObject.ET_QUERY_STOCK_TICK:
                mPresenter.onGetTick((TickRsp) data.getEntity());
                break;
            case EntityObject.ET_GET_LIVE_MESSAGE:
                mPresenter.onGetLiveMsg((SecLiveMsgRsp) data.getEntity());
                break;
            case EntityObject.ET_GET_FUND:
                mPresenter.onGetCapitalFlow(EntityUtil.entityToCapitalFlow(success, data));
                break;
            case EntityObject.ET_GET_HISTORY_CHIP_DIST_SIMPLE:
                mPresenter.onGetChipDistRsp((HisChipDistRsp) data.getEntity());
                break;
            default:
                break;
        }
    }
}
