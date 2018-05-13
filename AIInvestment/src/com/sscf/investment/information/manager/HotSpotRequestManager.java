package com.sscf.investment.information.manager;

import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import BEC.TopicListReq;

/**
 * Created by davidwei on 2017-05-15.
 */

public final class HotSpotRequestManager {
    private static final String TAG = HotSpotRequestManager.class.getSimpleName();

    public static void requestHotSpotList(final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestHotSpotList callback : " + callback);
        final TopicListReq req = new TopicListReq();

        DataEngine.getInstance().request(EntityObject.ET_GET_HOT_SPOT_LIST, req, callback);
    }

    public static void requestHotSpotList(final int startTime, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestHotSpotList time = " + startTime + " , callback = " + callback);
        final TopicListReq req = new TopicListReq();
        req.iStartTime = startTime;

        DataEngine.getInstance().request(EntityObject.ET_GET_HOT_SPOT_LIST, req, callback, String.valueOf(startTime));
    }
}
