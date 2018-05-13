package com.sscf.investment.search.manager;

import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;

import BEC.GetRealTimeHotStockReq;

/**
 * Created by davidwei on 2017-10-31
 *
 */
public final class SearchRequestManager {

    public static void requestHotStock(final int num, final DataSourceProxy.IRequestCallback callback) {
        final GetRealTimeHotStockReq req = new GetRealTimeHotStockReq();
        req.iWantNum = num;
        req.stUserInfo = SDKManager.getInstance().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_GET_HOT_STOCK, req, callback);
    }
}
