package com.sscf.investment.stare.ui.model;

import android.support.annotation.NonNull;

import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.stare.ui.presenter.StarePresenter;

import java.util.ArrayList;

import BEC.QuoteSimpleRsp;

/**
 * Created by yorkeehuang on 2017/9/11.
 */

public class StareModel implements DataSourceProxy.IRequestCallback {

    private StarePresenter mPresenter;


    public StareModel(@NonNull StarePresenter presenter) {
        mPresenter = presenter;
    }

    public void requestSimpleQuote(String unicode) {
        final ArrayList<String> unicodes = new ArrayList<String>(1);
        unicodes.add(unicode);
        QuoteRequestManager.getSimpleQuoteRequest(unicodes, this, null);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_SIMPLE_QUOTE:
                handleSimpleQuote(success, data);
                break;
        }
    }

    private void handleSimpleQuote(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            QuoteSimpleRsp rsp = (QuoteSimpleRsp) data.getEntity();
            if(rsp.getVSecSimpleQuote() != null && !rsp.getVSecSimpleQuote().isEmpty()) {
                mPresenter.onGetSimpleQuote(rsp.getVSecSimpleQuote().get(0));
            }
        }
    }
}
