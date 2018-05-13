package com.sscf.investment.teacherYan.manager;

import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.SettingConst;

import BEC.E_MEDIA_TYPE;
import BEC.WxWalkRecordListReq;


/**
 * Created by LEN on 2018/4/19.
 */

public class TeacherYanWordRequestManager {

    private static final String TAG = TeacherYanWordRequestManager.class.getSimpleName();


    public static void requestTeacherYanWordList(final DataSourceProxy.IRequestCallback callback) {
        DtLog.e(TAG, "requestTeacherYanWordList");
        WxWalkRecordListReq req = new WxWalkRecordListReq();
        req.iCount = SettingConst.PAGE_COUNT;
        req.eType = E_MEDIA_TYPE.E_MT_AUDIO;
        DataEngine.getInstance().request(EntityObject.ET_TEACHER_YAN_WORD, req, callback);
    }


    public static void requestTeacherYanWordListMore(final int mLastId, final DataSourceProxy.IRequestCallback callback) {
        DtLog.e(TAG, "requestTeacherYanWordListMore");
        WxWalkRecordListReq req = new WxWalkRecordListReq();
        req.iCount = SettingConst.PAGE_COUNT;
        req.iBeginID = mLastId;
        req.eType = E_MEDIA_TYPE.E_MT_AUDIO;
        DataEngine.getInstance().request(EntityObject.ET_TEACHER_YAN_WORD, req, callback, String.valueOf(mLastId));
    }
}
