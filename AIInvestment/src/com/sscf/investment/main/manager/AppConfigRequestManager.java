package com.sscf.investment.main.manager;

import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import java.util.ArrayList;
import BEC.E_CONFIG_TYPE;
import BEC.GenGuidReq;
import BEC.GetConfigReq;
import BEC.ReportUserInfoReq;
import BEC.UserInfo;

/**
 * Created by davidwei on 2016/1/13.
 * appConfig服务相关的request
 */
public final class AppConfigRequestManager {

    /**
     * 拉取guid
     */
    public static void genGuidRequest(final UserInfo userInfo, final DataSourceProxy.IRequestCallback observer) {
        final GenGuidReq guidReq = new GenGuidReq();
        guidReq.stUserInfo = userInfo;

        DataEngine.getInstance().request(EntityObject.ET_GUID, guidReq, observer);
    }

    /**
     * 请求设置里新股的红点
     * @param observer
     */
    public static void getConfigNewShareInfoRequest(final DataSourceProxy.IRequestCallback observer) {
        final GetConfigReq req = new GetConfigReq();
        final ArrayList<Integer> types = new ArrayList<Integer>(1);
        types.add(E_CONFIG_TYPE.E_CONFIG_NEW_STOCK);
        req.vType = types;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_CONFIG_GET_NEW_SHARE, req, observer);
    }

    /**
     * 请求投顾的红点
     * @param observer
     */
    public static void getIntelligentAnswerInfoRequest(final DataSourceProxy.IRequestCallback observer) {
        final GetConfigReq req = new GetConfigReq();
        final ArrayList<Integer> types = new ArrayList<Integer>(1);
        types.add(E_CONFIG_TYPE.E_CONFIG_INTELI_INVEST);
        req.vType = types;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_CONFIG_GET_INTELLIGENT_ANSWER, req, observer);
    }

    /**
     * 请求设置里活动的提醒文字和红点信息
     * @param observer
     */
    public static void getConfigActivitiesAndOpenAccountInfoRequest(final DataSourceProxy.IRequestCallback observer) {
        final GetConfigReq req = new GetConfigReq();
        final ArrayList<Integer> types = new ArrayList<Integer>(2);
        types.add(E_CONFIG_TYPE.E_CONFIG_NEW_ACTIVITY);
        types.add(E_CONFIG_TYPE.E_CONFIG_OPEN_ACCOUNT);
        req.vType = types;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_CONFIG_GET_NEW_ACTIVITIES, req, observer);
    }

    /**
     * 上报UserInfo
     * @param observer
     */
    public static void reportUserInfoRequest(final UserInfo userInfo, final DataSourceProxy.IRequestCallback observer) {
        final ReportUserInfoReq req = new ReportUserInfoReq();
        req.stUserInfo = userInfo;

        DataEngine.getInstance().request(EntityObject.ET_REPORT_USER_INFO, req, observer);
    }

    /**
     * 自选股上面指数的列表
     * @param observer
     */
    public static void getPortofolioHeaderIndexesRequest(final DataSourceProxy.IRequestCallback observer) {
        final GetConfigReq req = new GetConfigReq();
        final ArrayList<Integer> types = new ArrayList<Integer>(1);
        types.add(E_CONFIG_TYPE.E_CONFIG_OPTIONAL_INDEX);
        req.vType = types;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_CONFIG_PORTFOLIO_HEADER_INDEXES, req, observer);
    }

    /**
     * 获得push的sdk的开关，如是否使用华为，友盟的push
     * @param observer
     */
    public static void getPushSwitchRequest(final DataSourceProxy.IRequestCallback observer) {
        final GetConfigReq req = new GetConfigReq();
        final ArrayList<Integer> types = new ArrayList<Integer>(1);
        types.add(E_CONFIG_TYPE.E_CONFIG_PUSH_SWITCH);
        req.vType = types;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_PUSH_SWITCH, req, observer);
    }

    /**
     * 插件信息
     * @param observer
     */
    public static void getPluginInfoRequest(final DataSourceProxy.IRequestCallback observer) {
        final GetConfigReq req = new GetConfigReq();
        final ArrayList<Integer> types = new ArrayList<Integer>(1);
        types.add(E_CONFIG_TYPE.E_CONFIG_PLUGIN);
        req.vType = types;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_PLUGIN, req, observer);
    }

    /**
     *
     * @param observer
     */
    public static void requestAnnoucementType(final DataSourceProxy.IRequestCallback observer) {
        final GetConfigReq req = new GetConfigReq();
        final ArrayList<Integer> types = new ArrayList<Integer>(1);
        types.add(E_CONFIG_TYPE.E_ANNOUCEMWNT_TYPE_LIST);
        req.vType = types;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_CONFIG_GET_ANNOUCEMENT_TYPE, req, observer);
    }
}
