package com.sscf.investment.information.manager;

import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import BEC.DiscoveryNewsReq;
import BEC.E_SCENE_TYPE;
import BEC.GetDiscBannerReq;
import BEC.MarketAdListReq;

/**
 * Created by davidwei on 2017-05-15.
 */
public final class MarketInfoRequestManager {
    private static final String TAG = MarketInfoRequestManager.class.getSimpleName();

    /**
     * 资讯要闻列表
     */
    public static void requestNewsList(final String startId, final String endId, final int newsFlag,
                                       final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestNewsList: startId = " + startId + ", endId = " + endId + ", newsFlag = " + newsFlag);
        final DiscoveryNewsReq req = new DiscoveryNewsReq();
        req.sStartId = startId;
        req.sEndId = endId;
        req.eNewsFlag = newsFlag;
        req.iSupportTop = 1;
        req.iSupportTopic = 1;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_GET_DISCOVER_NEWS_LIST, req, callback, String.valueOf(startId));
    }

    /**
     * 资讯要闻里插入的广告
     */
    public static void requestAdList(final DataSourceProxy.IRequestCallback callback) {
        final MarketAdListReq req = new MarketAdListReq();
        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_MARKET_INFO_AD, req, callback);
    }

    public static void requestBannerList(final DataSourceProxy.IRequestCallback callback, int bannerType) {
        final GetDiscBannerReq req = new GetDiscBannerReq();
        req.eSceneType = bannerType;
        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_GET_DISCOVER_BANNER, req, callback);
    }
    /**
     * 资讯要闻banner广告
     */
    public static void requestBannerList(final DataSourceProxy.IRequestCallback callback) {
        final GetDiscBannerReq req = new GetDiscBannerReq();
        req.eSceneType = E_SCENE_TYPE.E_ST_ADR;
        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_GET_DISCOVER_BANNER, req, callback);
    }
}
