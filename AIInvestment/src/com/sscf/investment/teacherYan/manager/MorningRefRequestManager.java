package com.sscf.investment.teacherYan.manager;

import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.SettingConst;

import BEC.InformationSpiderNewsListReq;

/**
 * Created by LEN on 2018/4/23.
 */

public class MorningRefRequestManager {

    private static final String TAG = MorningRefRequestManager.class.getSimpleName();

    private static final int KIND_MORNING_REF = 3;

    public static void requestMorningRefList(final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestMorningRefList callback : " + callback);
        final InformationSpiderNewsListReq req = new InformationSpiderNewsListReq();
        req.iCount = SettingConst.PAGE_COUNT;
        req.iKind = KIND_MORNING_REF;

        DataEngine.getInstance().request(EntityObject.ET_MORNING_REF, req, callback);
    }

    public static void requestMorningRefListMore(final int startId, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestMorningRefListMore lastId = " + startId + " , callback = " + callback);
        final InformationSpiderNewsListReq req = new InformationSpiderNewsListReq();
        req.iBeginID = startId;
        req.iCount = SettingConst.PAGE_COUNT;
        req.iKind = KIND_MORNING_REF;

        DataEngine.getInstance().request(EntityObject.ET_MORNING_REF, req, callback, String.valueOf(startId));
    }
}
