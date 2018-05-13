package com.dengtacj.request;

import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;

import java.util.ArrayList;

import BEC.CapitalDDZMultiReq;
import BEC.CapitalDDZReq;
import BEC.CapitalFlowReq;
import BEC.ConsultReq;
import BEC.E_CAPITAL_DDZ_TYPE;
import BEC.E_TREND_REQ_TYPE;
import BEC.HisChipDistReq;
import BEC.KLineReq;
import BEC.QuoteReq;
import BEC.SecBaseInfoReq;
import BEC.SecLiveMsgReq;
import BEC.TickReq;
import BEC.TrendReq;
import BEC.UserInfo;

/**
 * Created by davidwei on 2017/04/17
 */
public final class QuoteRequestManager {
    private static final String TAG = QuoteRequestManager.class.getSimpleName();

    /**
     * 个股行情信息
     */
    public static void getQuoteRequest(final String dtSecCode, final DataSourceProxy.IRequestCallback callback, final Object extra) {
        final ArrayList<String> dtSecCodeList = new ArrayList<String>(1);
        dtSecCodeList.add(dtSecCode);
        getQuoteRequest(dtSecCodeList, callback, extra);
    }

    /**
     * 个股行情信息
     */
    public static void getQuoteRequest(final ArrayList<String> dtSecCodeList, final DataSourceProxy.IRequestCallback callback, final Object extra) {
        DtLog.d(TAG, "getQuoteRequest : dtSecCodeList.size = " + dtSecCodeList.size() + " , dtSecCodeList = " + dtSecCodeList + " , callback = " + callback);
        final QuoteReq req = new QuoteReq();
        req.vDtSecCode = dtSecCodeList;

        final UserInfo userInfo = SDKManager.getInstance().getUserInfo();
        if (userInfo != null) {
            req.vGuid = userInfo.vGUID;
        }

        DataEngine.getInstance().request(EntityObject.ET_GET_QUOTE, req, callback, extra);
    }

    /**
     * 获取筹码详情
     */
    public static void getChipDistRequest(final String dtSecCode, final int wantNum, final DataSourceProxy.IRequestCallback callback) {
        final HisChipDistReq req = new HisChipDistReq();
        req.setStUserInfo(SDKManager.getInstance().getUserInfo());
        req.setSDtSecCode(dtSecCode);
        req.setIWantnum(wantNum);

        DataEngine.getInstance().request(EntityObject.ET_GET_HISTORY_CHIP_DIST_SIMPLE, req, callback);
    }

    /**
     * 个股简版行情信息
     */
    public static void getSimpleQuoteRequest(final String dtSecCode, final DataSourceProxy.IRequestCallback callback, final Object extra) {
        final ArrayList<String> dtSecCodeList = new ArrayList<String>(1);
        dtSecCodeList.add(dtSecCode);
        getSimpleQuoteRequest(dtSecCodeList, callback, extra);
    }

    /**
     * 个股简版行情信息
     */
    public static void getSimpleQuoteRequest(final ArrayList<String> dtSecCodeList, final DataSourceProxy.IRequestCallback callback, final Object extra) {
        DtLog.d(TAG, "getSimpleQuoteRequest : dtSecCodeList.size = " + dtSecCodeList.size() + " , dtSecCodeList = " + dtSecCodeList + " , callback = " + callback);
        final QuoteReq req = new QuoteReq();
        req.vDtSecCode = dtSecCodeList;

        final UserInfo userInfo = SDKManager.getInstance().getUserInfo();
        if (userInfo != null) {
            req.vGuid = userInfo.vGUID;
        }

        DataEngine.getInstance().request(EntityObject.ET_GET_SIMPLE_QUOTE, req, callback, extra);
    }

    /**
     * K线数据
     */
    public static void getKLineDataRequest(final String dtSecCode, final int kLineType, final int start, final int num,
                                           final boolean isRepair, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "getKLineDataRequest : dtSecCode = " + dtSecCode + " , kLineType = " + kLineType
                + " , start = " + start + " , num = " + num + " , isRepair = " + isRepair + " , callback = " + callback);
        final KLineReq req = new KLineReq();
        req.sDtSecCode = dtSecCode;
        req.eKLineType = kLineType;
        req.iStartxh = start;
        req.iWantnum = num;
        req.bTg = isRepair;

        final UserInfo userInfo = SDKManager.getInstance().getUserInfo();
        if (userInfo != null) {
            req.vGuid = userInfo.vGUID;
        }

        DataEngine.getInstance().request(EntityObject.ET_GET_KLINE_DATA, req, callback);
    }

    /**
     * 个股日K资金流向
     */
    public static void getKLineCapitalDDZDataRequest(final String dtSecCode, final int start, final int num,
                                           final DataSourceProxy.IRequestCallback callback) {
        getCapitalDDZDataRequest(dtSecCode, E_CAPITAL_DDZ_TYPE.E_CDT_DAY, start, num, callback);
    }

    /**
     * 个股分时资金流向
     */
    public static void getTimeLineCapitalDDZDataRequest(final String dtSecCode, final int start, final int num,
                                                     final DataSourceProxy.IRequestCallback callback) {
        getCapitalDDZDataRequest(dtSecCode, E_CAPITAL_DDZ_TYPE.E_CDT_MIN, start, num, callback);
    }

    /**
     * 五日资金流向,资金博弈
     * @param dtSecCode
     * @param callback
     */
    public static void getFiveDayTimeLineCapitalDDZDataRequest(final String dtSecCode,
                                                               final DataSourceProxy.IRequestCallback callback) {
        CapitalDDZMultiReq req = new CapitalDDZMultiReq();
        req.setSDtSecCode(dtSecCode);
        DataEngine.getInstance().request(EntityObject.ET_GET_FIVEDAY_CAPITAL_DDZ, req, callback);
    }

    /**
     * 个股资金流向
     */
    private static void getCapitalDDZDataRequest(final String dtSecCode, final int type, final int start, final int num,
                                                     final DataSourceProxy.IRequestCallback callback) {
        DtLog.i(TAG, "getCapitalDDZDataRequest : dtSecCode = " + dtSecCode + " , type = " + type
                + " , start = " + start + " , num = " + num + " , callback = " + callback);
        final CapitalDDZReq req = new CapitalDDZReq();
        req.sDtSecCode = dtSecCode;
        req.eCapitalDDZType = type;
        req.iStartxh = start;
        req.iNum = num;

        final UserInfo userInfo = SDKManager.getInstance().getUserInfo();
        if (userInfo != null) {
            req.vGuid = userInfo.vGUID;
        }

        DataEngine.getInstance().request(EntityObject.ET_GET_CAPITAL_DDZ, req, callback, String.valueOf(type));
    }

    /**
     * 诊断个股评分
     * @param dtSecCode
     * @param callback
     */
    public static void getStockScoreDetailRequest(final String dtSecCode, final DataSourceProxy.IRequestCallback callback) {
        final ConsultReq req = new ConsultReq();

        final ArrayList<String> secCodes = new ArrayList<String>(1);
        secCodes.add(dtSecCode);
        req.vtDtSecCode = secCodes;

        final UserInfo userInfo = SDKManager.getInstance().getUserInfo();
        if (userInfo != null) {
            req.vGuid = userInfo.vGUID;
        }

        DataEngine.getInstance().request(EntityObject.ET_GET_SCORE_DETAIL, req, callback);
    }

    /**
     * 成交明细
     * @param dtSecCode
     * @param callback
     */
    public static void getTickDataRequest(final String dtSecCode, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "getTickDataRequest : dtSecCode = " + dtSecCode + " , callback = " + callback);
        final TickReq req = new TickReq();
        req.sDtSecCode = dtSecCode;
        req.iStartxh = 0;
        req.iWantnum = 50;

        final UserInfo userInfo = SDKManager.getInstance().getUserInfo();
        if (userInfo != null) {
            req.vGuid = userInfo.vGUID;
        }

        DataEngine.getInstance().request(EntityObject.ET_QUERY_STOCK_TICK, req, callback);
    }

    /**
     * 个股直播消息
     * @param dtSecCode
     * @param lastId
     * @param callback
     */
    public static void getLiveMsgRequest(final String dtSecCode, final String lastId, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "getLiveMsgRequest : dtSecCode = " + dtSecCode + " , lastId = " + lastId + " , callback = " + callback);
        final SecLiveMsgReq req = new SecLiveMsgReq();
        req.sDtSecCode = dtSecCode;
        req.sId = lastId;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_GET_LIVE_MESSAGE, req, callback);
    }

    /**
     * 个股基本信息
     */
    public static void getBaseStockInfoRequest(final String dtSecCode, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "getBaseStockInfoRequest : dtSecCode = " + dtSecCode + " , callback = " + callback);
        final SecBaseInfoReq req = new SecBaseInfoReq();

        final ArrayList<String> secCodes = new ArrayList<String>(1);
        secCodes.add(dtSecCode);
        req.vDtSecCode = secCodes;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_QUERY_STOCK_DETAIL_INFO, req, callback);
    }

    /**
     * 资金
     */
    public static void getCapitalDataRequest(final String dtSecCode, final DataSourceProxy.IRequestCallback callback) {
        getCapitalDataRequest(dtSecCode, 1, callback);
    }

    /**
     * 资金
     */
    public static void getCapitalDataRequest(final String dtSecCode, final int num, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "getCapitalDataRequest : dtSecCode = " + dtSecCode + " , callback = " + callback);
        final CapitalFlowReq req = new CapitalFlowReq();
        req.sDtSecCode = dtSecCode;
        req.iPeriod = 1;
        req.iNum = num;

        final UserInfo userInfo = SDKManager.getInstance().getUserInfo();
        if (userInfo != null) {
            req.vGuid = userInfo.vGUID;
        }

        DataEngine.getInstance().request(EntityObject.ET_GET_FUND, req, callback);
    }

    /**
     * 增量拉取分时数据
     */
    public static void getTimeLineDataRequest(final String dtSecCode, final int start, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "getTimeLineDataRequest : dtSecCode = " + dtSecCode + " , start = " + start + " , callback = " + callback);
        final TrendReq req = new TrendReq();
        req.sDtSecCode = dtSecCode;
        req.eTrendReqType = E_TREND_REQ_TYPE.E_TRT_INCRE;
        req.iStartxh = start;

        final UserInfo userInfo = SDKManager.getInstance().getUserInfo();
        if (userInfo != null) {
            req.vGuid = userInfo.vGUID;
        }

        DataEngine.getInstance().request(EntityObject.ET_GET_TREND, req, callback, String.valueOf(start));
    }

    /**
     * 拉取分时数据
     */
    public static void getTimeLineDataRequest(final String dtSecCode, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "getTimeLineDataRequest : dtSecCode = " + dtSecCode + " , callback = " + callback);
        final TrendReq req = new TrendReq();
        req.sDtSecCode = dtSecCode;
        req.eTrendReqType = E_TREND_REQ_TYPE.E_TRT_NORMAL;

        final UserInfo userInfo = SDKManager.getInstance().getUserInfo();
        if (userInfo != null) {
            req.vGuid = userInfo.vGUID;
        }

        DataEngine.getInstance().request(EntityObject.ET_GET_TREND, req, callback);
    }
}
