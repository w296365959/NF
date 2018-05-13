package com.sscf.investment.detail.model;

import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.detail.presenter.SecurityDetailPresenter;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.EntityUtil;

import BEC.HisChipDistRsp;
import BEC.SecBaseInfoRsp;
import BEC.SecLiveMsgRsp;

/**
 * Created by davidwei on 2017/04/24
 */
public final class SecurityDetailModel implements DataSourceProxy.IRequestCallback {
    private final SecurityDetailPresenter mPresenter;

    public SecurityDetailModel(final SecurityDetailPresenter presenter) {
        mPresenter = presenter;
    }

    public void requestQuote(final String dtSecCode) {
        QuoteRequestManager.getQuoteRequest(dtSecCode, this, null);
    }

    public void requestBaseInfo(final String dtSecCode) {
        QuoteRequestManager.getBaseStockInfoRequest(dtSecCode, this);
    }

    public void requestLiveMsg(final String dtSecCode, final String lastId) {
        QuoteRequestManager.getLiveMsgRequest(dtSecCode, lastId, this);
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
            case EntityObject.ET_QUERY_STOCK_DETAIL_INFO:
                mPresenter.onGetBaseInfo((SecBaseInfoRsp) data.getEntity());
                break;
            case EntityObject.ET_GET_LIVE_MESSAGE:
                mPresenter.onGetLiveMsg((SecLiveMsgRsp) data.getEntity());
                break;
            case EntityObject.ET_GET_HISTORY_CHIP_DIST_SIMPLE:
                mPresenter.onGetChipDistRsp((HisChipDistRsp) data.getEntity());
                break;
            default:
                break;
        }
    }
}
