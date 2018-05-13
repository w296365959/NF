package com.sscf.investment.teacherYan.manager;

import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;

import BEC.E_MEDIA_TYPE;
import BEC.WxWalkRecordListReq;

/**
 * Created by LEN on 2018/4/23.
 */

public class TeacherYanCoureseRequestManager {

    private static final String TAG = TeacherYanCoureseRequestManager.class.getSimpleName();

    private static final int PAGE_COUNT = 20;

    public static void requestTeacherYanCurseList(final DataSourceProxy.IRequestCallback callback) {
        WxWalkRecordListReq req = new WxWalkRecordListReq();
        req.iCount = PAGE_COUNT;
        req.eType = E_MEDIA_TYPE.E_MT_VIDEO;
        DataEngine.getInstance().request(EntityObject.ET_TEACHER_YAN_CURSE, req, callback);
    }


    public static void requestTeacherYanWordListMore(final int lastId, final DataSourceProxy.IRequestCallback callback) {
        WxWalkRecordListReq req = new WxWalkRecordListReq();
        req.iCount = E_MEDIA_TYPE.E_MT_VIDEO;
        req.iBeginID = lastId;
        DataEngine.getInstance().request(EntityObject.ET_TEACHER_YAN_CURSE, req, callback, lastId);
    }
}
