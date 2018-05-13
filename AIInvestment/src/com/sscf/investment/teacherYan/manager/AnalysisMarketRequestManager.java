package com.sscf.investment.teacherYan.manager;

import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.SettingConst;

import BEC.InformationSpiderNewsListReq;

/**
 * Created by LEN on 2018/4/24.
 */

public class AnalysisMarketRequestManager {

    private static final String TAG = AnalysisMarketRequestManager.class.getSimpleName();

    private static final int KIND_ANALYSIS_MARKET = 2;

    public static void requestAnalysisMarketList(final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestHotSpotList callback : " + callback);
        final InformationSpiderNewsListReq req = new InformationSpiderNewsListReq();
        req.iCount = SettingConst.PAGE_COUNT;
        req.iKind = KIND_ANALYSIS_MARKET;

        DataEngine.getInstance().request(EntityObject.ET_ANALYSIS_MARKET, req, callback);
    }

    public static void requestAnalysisMarketListMore(final int startId, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestHotSpotList time = " + startId + " , callback = " + callback);
        final InformationSpiderNewsListReq req = new InformationSpiderNewsListReq();
        req.iBeginID = startId;
        req.iCount = SettingConst.PAGE_COUNT;
        req.iKind = KIND_ANALYSIS_MARKET;

        DataEngine.getInstance().request(EntityObject.ET_ANALYSIS_MARKET, req, callback, String.valueOf(startId));
    }
}
