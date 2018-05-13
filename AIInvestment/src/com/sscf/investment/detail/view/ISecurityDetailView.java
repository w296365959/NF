package com.sscf.investment.detail.view;

import BEC.SecBaseInfoRsp;
import BEC.SecLiveMsgRsp;
import BEC.SecQuote;

/**
 * Created by davidwei on 2017-05-02
 */
public interface ISecurityDetailView {
    void onLoadComplete();
    void updateQuoteView(SecQuote quote);
    void updateBaseInfoView(SecBaseInfoRsp baseInfoRsp);
    void updateLiveMsgView(SecLiveMsgRsp rsp);
    void updateKLineQuoteView(KLineInfosView.KLineLineTouchEvent event, SecQuote quote);
    boolean getUserVisibleHint();
}
