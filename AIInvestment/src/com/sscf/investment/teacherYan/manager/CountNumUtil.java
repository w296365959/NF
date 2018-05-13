package com.sscf.investment.teacherYan.manager;

import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;

import BEC.E_INFO_TYPE;
import BEC.SscfInfoReadReq;

/**
 * Created by LEN on 2018/5/3.
 */

public class CountNumUtil {

    private static DataSourceProxy.IRequestCallback mCallback = new DataSourceProxy.IRequestCallback() {
        @Override
        public void callback(boolean success, EntityObject data) {

        }
    };

    public static void readTeacherYanArticle(int iid) {
        SscfInfoReadReq req = new SscfInfoReadReq();
        req.setEInfoType(E_INFO_TYPE.E_IT_TEACH);
        req.setISscfInfoID(iid);
        DataEngine.getInstance().request(EntityObject.ET_TEACHERYAN_COUNT_NUM, req, mCallback);
    }

    public static void readMorningRef(int iid) {
        SscfInfoReadReq req = new SscfInfoReadReq();
        req.setEInfoType(E_INFO_TYPE.E_IT_ZC);
        req.setISscfInfoID(iid);
        DataEngine.getInstance().request(EntityObject.ET_TEACHERYAN_COUNT_NUM, req, mCallback);
    }

    public static void readAnalysisMarket(int iid) {
        SscfInfoReadReq req = new SscfInfoReadReq();
        req.setEInfoType(E_INFO_TYPE.E_IT_JP);
        req.setISscfInfoID(iid);
        DataEngine.getInstance().request(EntityObject.ET_TEACHERYAN_COUNT_NUM, req, mCallback);
    }

    public static void readTeacherVideo(int iid) {
        SscfInfoReadReq req = new SscfInfoReadReq();
        req.setEInfoType(E_INFO_TYPE.E_IT_VIDEO);
        req.setISscfInfoID(iid);
        DataEngine.getInstance().request(EntityObject.ET_TEACHERYAN_COUNT_NUM, req, mCallback);
    }

    public static void readAudioRecord(int iid) {
        SscfInfoReadReq req = new SscfInfoReadReq();
        req.setEInfoType(E_INFO_TYPE.E_IT_YL);
        req.setISscfInfoID(iid);
        DataEngine.getInstance().request(EntityObject.ET_TEACHERYAN_COUNT_NUM, req, mCallback);
    }


    public static void readTeacherYanVideo(int iid) {
        SscfInfoReadReq req = new SscfInfoReadReq();
        req.setEInfoType(E_INFO_TYPE.E_IT_KC);
        req.setISscfInfoID(iid);
        DataEngine.getInstance().request(EntityObject.ET_TEACHERYAN_COUNT_NUM, req, mCallback);
    }

    public static void readTalkFree(int iid) {
        SscfInfoReadReq req = new SscfInfoReadReq();
        req.setEInfoType(E_INFO_TYPE.E_IT_DY);
        req.setISscfInfoID(iid);
        DataEngine.getInstance().request(EntityObject.ET_TEACHERYAN_COUNT_NUM, req, mCallback);
    }
}
