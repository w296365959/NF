package com.sscf.investment.teacherYan.manager;

import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;

import BEC.E_MEDIA_TYPE;
import BEC.WxTeachListReq;
import BEC.WxWalkRecordListReq;

/**
 * Created by LEN on 2018/4/23.
 */

public class TeacherYanArticleRequestManager {

    private static final String TAG = TeacherYanCoureseRequestManager.class.getSimpleName();

    private static final int PAGE_COUNT = 20;

    public static void requestTeacherYanArticleList(final DataSourceProxy.IRequestCallback callback) {
        WxTeachListReq req = new WxTeachListReq();
        req.iCount = PAGE_COUNT;
        DataEngine.getInstance().request(EntityObject.ET_TEACHER_YAN_ARTICLE, req, callback);
    }


    public static void requestTeacherYanArticleListMore(final int lastId, final DataSourceProxy.IRequestCallback callback) {
        WxTeachListReq req = new WxTeachListReq();
        req.iCount = E_MEDIA_TYPE.E_MT_VIDEO;
        req.iBeginID = lastId;
        DataEngine.getInstance().request(EntityObject.ET_TEACHER_YAN_ARTICLE, req, callback, lastId);
    }
}
