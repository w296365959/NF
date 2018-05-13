package com.sscf.investment.teacherYan.manager;

import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.SettingConst;

import BEC.WxTalkFreeListReq;

/**
 * Created by LEN on 2018/4/20.
 */

public class TalkFreeRequestManager {
    private static final String TAG = TalkFreeRequestManager.class.getSimpleName();

    public static void requestTalkFreeData(DataSourceProxy.IRequestCallback callback){
        WxTalkFreeListReq req = new WxTalkFreeListReq();
        req.iCount = SettingConst.PAGE_COUNT;
        DataEngine.getInstance().request(EntityObject.ET_TALK_FREE, req, callback);
    }

    public static void requestTalkFreeDataMore(DataSourceProxy.IRequestCallback callback, int id){
        DtLog.e(TAG, "requestTalkFreeDataMore");
        WxTalkFreeListReq req = new WxTalkFreeListReq();
        req.iCount = SettingConst.PAGE_COUNT;
        req.iBeginID = id;
        DataEngine.getInstance().request(EntityObject.ET_TALK_FREE, req, callback, String.valueOf(id));
    }
}
