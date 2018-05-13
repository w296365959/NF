package com.sscf.investment.teacherYan.manager;

import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.SettingConst;

import BEC.InformationSpiderNewsListReq;
import BEC.VideoInfoListReq;

/**
 * Created by LEN on 2018/4/24.
 */

public class TeacherVideoRequestManager {

    private static final String TAG = TeacherVideoRequestManager.class.getSimpleName();

    private static final int KIND_MORNING_REF = 3;

    public static void requestTeacherVideoList(final DataSourceProxy.IRequestCallback callback) {
        final VideoInfoListReq req = new VideoInfoListReq();
        req.iCount = SettingConst.PAGE_COUNT;
        DataEngine.getInstance().request(EntityObject.ET_REQUEST_TEACHER_VIDEO, req, callback);
    }

    public static void requestTeacherVideoListMore(final int startId, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestTeacherVideoListMore startId = " + startId + " , callback = " + callback);
        final VideoInfoListReq req = new VideoInfoListReq();
        req.iBeginID = startId;
        req.iCount = SettingConst.PAGE_COUNT;

        DataEngine.getInstance().request(EntityObject.ET_REQUEST_TEACHER_VIDEO, req, callback, String.valueOf(startId));
    }
}
