package com.sscf.investment.detail.model;

import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.detail.presenter.HandicapPresenter;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.EntityUtil;
import java.util.ArrayList;
import BEC.ConsultScore;
import BEC.ConsultScoreRsp;

/**
 * Created by davidwei on 2017/04/24
 */
public final class HandicapModel implements DataSourceProxy.IRequestCallback {
    private final HandicapPresenter mHandicapPresenter;

    private DataSourceProxy.IRequestCallback mIndustryPlateQuoteCallback;
    private DataSourceProxy.IRequestCallback mConceptPlateQuoteCallback;

    public HandicapModel(final HandicapPresenter handicapPresenter) {
        mHandicapPresenter = handicapPresenter;
    }

    public void requestPlateQuote(final String industryPlateCode, final ArrayList<String> conceptPlateCodes) {
        QuoteRequestManager.getSimpleQuoteRequest(industryPlateCode, getIndustryPlateQuoteCallback(), null);
        QuoteRequestManager.getSimpleQuoteRequest(conceptPlateCodes, getConceptQuoteCallback(), null);
    }

    private DataSourceProxy.IRequestCallback getIndustryPlateQuoteCallback() {
        if (mIndustryPlateQuoteCallback == null) {
            mIndustryPlateQuoteCallback = new DataSourceProxy.IRequestCallback() {
                @Override
                public void callback(boolean success, EntityObject data) {
                    mHandicapPresenter.onGetIndustryPlateQuote(EntityUtil.entityToSecSimpleQuote(success, data));
                }
            };
        }
        return mIndustryPlateQuoteCallback;
    }

    private DataSourceProxy.IRequestCallback getConceptQuoteCallback() {
        if (mConceptPlateQuoteCallback == null) {
            mConceptPlateQuoteCallback = new DataSourceProxy.IRequestCallback() {
                @Override
                public void callback(boolean success, EntityObject data) {
                    mHandicapPresenter.onGetConceptPlateQuote(EntityUtil.entityToSecSimpleQuoteList(success, data));
                }
            };
        }
        return mConceptPlateQuoteCallback;
    }

    public void requestSimpleQuote(final String dtSecCode) {
        QuoteRequestManager.getSimpleQuoteRequest(dtSecCode, this, null);
    }

    public void requestStockScore(final String dtSecCode) {
        QuoteRequestManager.getStockScoreDetailRequest(dtSecCode, this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_SIMPLE_QUOTE:
                handleSimpleQuote(success, data);
                break;
            case EntityObject.ET_GET_SCORE_DETAIL:
                handleScoreDetail(success, data);
                break;
            default:
                break;
        }
    }

    private void handleSimpleQuote(boolean success, EntityObject data) {
        mHandicapPresenter.onGetRelatedHSimpleQuote(EntityUtil.entityToSecSimpleQuote(success, data));
    }

    private void handleScoreDetail(boolean success, EntityObject data) {
        if (success) {
            final ConsultScoreRsp rsp = (ConsultScoreRsp) data.getEntity();
            if (rsp != null) {
                ArrayList<ConsultScore> scores = rsp.vtConsultScore;
                final int size = scores == null ? 0 : scores.size();
                if (size > 0) {
                    mHandicapPresenter.onGetStockScore(scores.get(0));
                    return;
                }
            }
        }
        // TODO fail
    }
}
