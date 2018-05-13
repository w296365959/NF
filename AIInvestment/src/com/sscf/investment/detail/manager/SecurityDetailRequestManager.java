package com.sscf.investment.detail.manager;

import BEC.CompanyIndustrialChainReq;
import BEC.CompanyReq;
import BEC.DongmiQaListReq;
import BEC.E_FEED_GROUP_TYPE;
import BEC.E_FEED_TYPE;
import BEC.E_NEMS_GET_SOURCE;
import BEC.E_NEWS_TYPE;
import BEC.GetFeedListReq;
import BEC.NewsReq;

import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;

/**
 * Created by davidwei on 2015/03/15.
 */
public final class SecurityDetailRequestManager {
    private static final String TAG = SecurityDetailRequestManager.class.getSimpleName();

    /**
     * 请求公告大事件提醒
     */
    public static void requestAnnouncementRemind(final String dtSecCode, final DataSourceProxy.IRequestCallback callback) {
        requestNewsList(dtSecCode, E_NEWS_TYPE.NT_NEWSMARKERS, E_NEMS_GET_SOURCE._E_STOCK_MARKET_GET, callback);
    }

    /**
     * 请求分类公告列表
     */
    public static void requestNoticeList(final String dtSecCode, final String announceType, final DataSourceProxy.IRequestCallback callback) {
        final NewsReq req = new NewsReq();
        req.sAnnounceType = announceType;
        req.sDtSecCode = dtSecCode;
        req.eNewsType = E_NEWS_TYPE.NT_ANNOUNCEMENT;
        req.eGetSource = E_NEMS_GET_SOURCE._E_STOCK_MARKET_GET;
        req.sStartId = "";
        req.sEndId = "";

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_NEWS, req, callback, null);
    }

    /**
     * 请求新闻列表
     */
    public static void requestNewsList(final String dtSecCode, final int newsType, final int source, final DataSourceProxy.IRequestCallback callback) {
        requestNewsList(dtSecCode, newsType, source, "", "", callback, null);
    }

    /**
     * 请求新闻列表
     */
    public static void requestNewsList(final String dtSecCode, final int newsType, final int source,
            final String startId, final String endId, final DataSourceProxy.IRequestCallback callback, Object extra) {
        DtLog.d(TAG, String.format("requestAnnouncementRemind : dtSecCode = %s , newsType = %s , source = %s , startId = %s , endId = %s , extra = %s",
                dtSecCode, newsType, source, startId, endId, extra));
        final NewsReq req = new NewsReq();
        req.sDtSecCode = dtSecCode;
        req.eNewsType = newsType;
        req.eGetSource = source;
        req.sStartId = startId;
        req.sEndId = endId;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_NEWS, req, callback, extra);
    }

    /**
     * 请求产业链信息
     */
    public static void requestIndustrialChain(final String dtSecCode, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestIndustrialChain dtSecCode = " + dtSecCode);
        final CompanyIndustrialChainReq req = new CompanyIndustrialChainReq();
        req.sDtSecCode = dtSecCode;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_COMPANY_INDUSTRIAL_CHAIN, req, callback);
    }

    /**
     * 请求简介
     */
    public static void requestBriefInfo(final String dtSecCode, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestBriefInfo dtSecCode = " + dtSecCode);
        final CompanyReq req = new CompanyReq();
        req.sDtSecCode = dtSecCode;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_BRIEF_INFO, req, callback);
    }

    public static void requestDongmiQaList(final String dtSecCode, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestDongmiQaList dtSecCode = " + dtSecCode);
        DongmiQaListReq req = new DongmiQaListReq();
        req.setSDtSecCode(dtSecCode);
        req.setStUserInfo(SDKManager.getInstance().getUserInfo());
        DataEngine.getInstance().request(EntityObject.ET_GET_DONGMI_QA_LIST, req, callback);
    }

    public static void requestSimpleFeedList(final String dtSecCode, final DataSourceProxy.IRequestCallback callback) {
        GetFeedListReq req = new GetFeedListReq();
        req.setESelfType(E_FEED_TYPE.E_FT_STOCK_REVIEW);
        req.setEFeedGroupType(E_FEED_GROUP_TYPE.E_FGT_SEC);
        req.setStUserInfo(DengtaApplication.getApplication().getAccountManager().getUserInfo());
        req.setSDtSecCode(dtSecCode);
        DataEngine.getInstance().request(EntityObject.ET_GET_FEED_LIST, req, callback);
    }
}
