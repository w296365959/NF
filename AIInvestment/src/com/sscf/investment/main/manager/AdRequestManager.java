package com.sscf.investment.main.manager;

import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;

import BEC.DT_ACTIVITY_TYPE;
import BEC.DT_AD_TYPE;
import BEC.GetDtActivityListReq;

/**
 * Created by davidwei on 2017/08/16
 */

public final class AdRequestManager {

    public static void requestStockPickFlowAd(final DataSourceProxy.IRequestCallback callback) {
        requestAd(DT_ACTIVITY_TYPE.T_ACTIVITY_STOCK_PICK_AD, DT_AD_TYPE.E_AD_ACTIVITY, callback);
    }

    public static void requestInfoFlowAd(final DataSourceProxy.IRequestCallback callback) {
        requestAd(DT_ACTIVITY_TYPE.T_ACTIVITY_NEWS_AD, DT_AD_TYPE.E_AD_ACTIVITY, callback);
    }

    public static void requestDtActivityList(int type, DataSourceProxy.IRequestCallback callback) {
        requestAd(type, DT_AD_TYPE.E_AD_ACTIVITY, callback, String.valueOf(type));
    }

    public static void requestDtActivityList(int type, DataSourceProxy.IRequestCallback callback, String extra) {
        requestAd(type, DT_AD_TYPE.E_AD_ACTIVITY, callback, extra);
    }

    public static void requestPayAdList(final int subjectType, DataSourceProxy.IRequestCallback callback) {
        requestAd(subjectType, DT_AD_TYPE.E_AD_PAY, callback);
    }

    private static void requestAd(final int eType, final int iAdType, final DataSourceProxy.IRequestCallback callback) {
        final GetDtActivityListReq req = new GetDtActivityListReq();

        req.eType = eType;
        req.iAdType = iAdType;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_GET_DT_ACTIVITY_LIST, req, callback);
    }

    private static void requestAd(final int eType, final int iAdType, final DataSourceProxy.IRequestCallback callback, String extra) {
        final GetDtActivityListReq req = new GetDtActivityListReq();

        req.eType = eType;
        req.iAdType = iAdType;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_GET_DT_ACTIVITY_LIST, req, callback, extra);
    }
}
