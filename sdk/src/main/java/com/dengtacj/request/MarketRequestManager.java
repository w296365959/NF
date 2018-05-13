package com.dengtacj.request;

import BEC.*;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.TimeUtils;
import java.util.ArrayList;

/**
 * Created by davidwei on 2015/8/22.
 */
public final class MarketRequestManager {
    private static final String TAG = MarketRequestManager.class.getSimpleName();
    private static final int TYPE_UP_DOWN = 14; // 按照涨跌幅度排序
    private static final int TYPE_TURNOVER_RATE = 36; // 按照换手率排序
    private static final int TYPE_CAPITAL_FLOW = 44; // 当日主力净流入
    private static final int TYPE_AH_STOCK = 120; // AH股溢价

    /**
     * 获得私有化列表
     */
    public static void getPrivatizationTrackingList(final int count, final DataSourceProxy.IRequestCallback observer, final Object extra) {
        final GetPrivTopListReq req = new GetPrivTopListReq();
        req.iTopN = count;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_GET_USA_PRIVATIZATION_TRACKING_LIST, req, observer, extra);
    }

    /**
     * 获得大盘预警的请求
     * @param observer
     */
    public static void getMarketWarningRequest(final DataSourceProxy.IRequestCallback observer) {
        final MarketAlertReq req = new MarketAlertReq();
        req.iStart = 0;
        req.iSize = 3;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_MARKET_WARNING, req, observer);
    }

    /**
     * 获得融资融券余额
     * @param dtSecCode
     * @param observer
     */
    public static void getMarginTradingInfo(final String dtSecCode, final DataSourceProxy.IRequestCallback observer) {
        final MarginTradeReq req = new MarginTradeReq();
        req.sDtSecCode = dtSecCode;
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_MARGIN_TRADING_INFO, req, observer);
    }

    public static void getStockMarginTrade(final String dtSecCode, final int num, final DataSourceProxy.IRequestCallback observer) {
        DtLog.d(TAG, "getStockMarginTrade : dtSecCode = " + dtSecCode);
        final StockMarginTradeReq req = new StockMarginTradeReq();
        req.sDtSecCode = dtSecCode;
        req.iWantNum = num;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_GET_STOCK_MARGIN_TRADE, req, observer);
    }

    /**
     * 获得港股主板涨幅榜股票列表
     * @param observer
     */
    public static void  getHKMainStockIncreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.eMarketType = E_MARKET_TYPE.E_MT_HK;
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_HK;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_INCEREASE;

        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得港股主板跌幅榜股票列表
     * @param observer
     */
    public static void getHKMainStockDecreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.eMarketType = E_MARKET_TYPE.E_MT_HK;
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_HK;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_DECRREASE;

        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得港股创业板涨幅榜股票列表
     * @param observer
     */
    public static void getHKGEMStockIncreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.eMarketType = E_MARKET_TYPE.E_MT_HK;
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_HK_GEM;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_INCEREASE;

        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得港股创业板跌幅榜股票列表
     * @param observer
     */
    public static void getHKGEMStockDecreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.eMarketType = E_MARKET_TYPE.E_MT_HK;
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_HK_GEM;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_DECRREASE;

        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得沪股通跌幅榜股票列表
     * @param observer
     */
    public  static void  getSHConnectStockDecreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.eMarketType = E_MARKET_TYPE.E_MT_HK;
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_HK_SH;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_DECRREASE;

        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得沪股通涨幅榜股票列表
     * @param observer
     */
    public  static void  getSHConnectStockIncreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.eMarketType = E_MARKET_TYPE.E_MT_HK;
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_HK_SH;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_INCEREASE;

        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得港股通涨幅榜股票列表
     * @param observer
     */
    public static void getHKConnectStockIncreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.eMarketType = E_MARKET_TYPE.E_MT_HK;
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_SH_HK;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_INCEREASE;

        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得港股通跌幅榜股票列表
     * @param observer
     */
    public  static void  getHKConnectStockDecreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.eMarketType = E_MARKET_TYPE.E_MT_HK;
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_SH_HK;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_DECRREASE;

        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得沪港通余额
     * @param observer
     */
    public static void getSHHKConnectBalanceRequest(final DataSourceProxy.IRequestCallback observer, Object extra) {
        final AHExtendReq req = new AHExtendReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_SH_HK_CONNECT_BALANCE, req, observer, extra);
    }

    /**
     * 获得AH股溢价股票升序列表
     * @param observer
     */
    public  static void  getAHStockIncreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, final Object extra) {
        final AHPlateReq req = new AHPlateReq();
        req.iColype = TYPE_AH_STOCK;
        req.eSortType = E_CAPITAL_DETAIL_SORT_TYPE.E_CDST_DECRREASE;
        req.iStartxh = 0;
        req.iWantnum = count;

        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_AH_STOCK, req, observer, extra);
    }

    /**
     * 获得AH股溢价股票降序列表
     * @param observer
     */
    public  static void  getAHStockDecreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, final Object extra) {
        final AHPlateReq req = new AHPlateReq();
        req.eSortType = E_CAPITAL_DETAIL_SORT_TYPE.E_CDST_INCEREASE;
        req.iStartxh = 0;
        req.iWantnum = count;

        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_AH_STOCK, req, observer, extra);
    }


    /**
     * 获得中概股涨幅榜股票列表
     * @param observer
     */
    public  static void  getCCSStockIncreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.eMarketType = E_MARKET_TYPE.E_MT_USI;
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_US_CHINA;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_INCEREASE;

        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得中概股跌幅榜股票列表
     * @param observer
     */
    public  static void  getCCSStockDecreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.eMarketType = E_MARKET_TYPE.E_MT_USI;
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_US_CHINA;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_DECRREASE;

        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得标普500涨幅榜股票列表
     * @param observer
     */
    public  static void  getSPXStockIncreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.eMarketType = E_MARKET_TYPE.E_MT_USI;
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_SPX;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_INCEREASE;

        req.vGuid = SDKManager.getInstance().getGuidBytes();
        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得标普500跌幅榜股票列表
     * @param observer
     */
    public  static void  getSPXStockDecreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.eMarketType = E_MARKET_TYPE.E_MT_USI;
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_SPX;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_DECRREASE;

        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得股指期货的列表
     * @param observer
     */
    public static void getIndexFuturesListRequest(final int count, final DataSourceProxy.IRequestCallback observer, final Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_FUTURES;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_INCEREASE;

        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得多个股资金流流入列表
     * @param observer
     */
    public static void getMultiStockCapitalFlowRequest(final ArrayList<String> stockUnicodeList, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final CapitalMainFlowReq req = new CapitalMainFlowReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.vDtSecCode = stockUnicodeList;

        DataEngine.getInstance().request(EntityObject.ET_GET_CAPITAL_FLOW_MULTI_STOCK, req, observer, extra);
    }

    /**
     * 获得个股资金流流入列表
     * @param observer
     */
    public static void getStockCapitalFlowIncreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final CapitalDetailReq req = new CapitalDetailReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.eSetType = E_CAPITAL_DETAIL_SET_TYPE.E_CDST_STOCK;
        req.eDataType = E_CAPITAL_DETAIL_DATA_TYPE.E_CDDT_1_DAY;
        req.eSortType = E_CAPITAL_DETAIL_SORT_TYPE.E_CDST_INCEREASE;
        req.iStartxh = 0;
        req.iNum = count;
        req.iColype = TYPE_CAPITAL_FLOW;

        DataEngine.getInstance().request(EntityObject.ET_GET_CAPITAL_FLOW_LIST, req, observer, extra);
    }

    /**
     * 获得个股资金流流出列表
     * @param observer
     */
    public static void getStockCapitalFlowDecreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final CapitalDetailReq req = new CapitalDetailReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.eSetType = E_CAPITAL_DETAIL_SET_TYPE.E_CDST_STOCK;
        req.eDataType = E_CAPITAL_DETAIL_DATA_TYPE.E_CDDT_1_DAY;
        req.eSortType = E_CAPITAL_DETAIL_SORT_TYPE.E_CDST_DECRREASE;
        req.iStartxh = 0;
        req.iNum = count;
        req.iColype = TYPE_CAPITAL_FLOW;

        DataEngine.getInstance().request(EntityObject.ET_GET_CAPITAL_FLOW_LIST, req, observer, extra);
    }

    /**
     * 获得行业资金流流入列表
     * @param observer
     */
    public static void getIndustryCapitalFlowIncreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final CapitalDetailReq req = new CapitalDetailReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.eSetType = E_CAPITAL_DETAIL_SET_TYPE.E_CDST_BUSS;
        req.eDataType = E_CAPITAL_DETAIL_DATA_TYPE.E_CDDT_1_DAY;
        req.eSortType = E_CAPITAL_DETAIL_SORT_TYPE.E_CDST_INCEREASE;
        req.iStartxh = 0;
        req.iNum = count;
        req.iColype = TYPE_CAPITAL_FLOW;

        DataEngine.getInstance().request(EntityObject.ET_GET_CAPITAL_FLOW_LIST, req, observer, extra);
    }

    /**
     * 获得行业资金流流出列表
     * @param observer
     */
    public static void getIndustryCapitalFlowDecreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final CapitalDetailReq req = new CapitalDetailReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.eSetType = E_CAPITAL_DETAIL_SET_TYPE.E_CDST_BUSS;
        req.eDataType = E_CAPITAL_DETAIL_DATA_TYPE.E_CDDT_1_DAY;
        req.eSortType = E_CAPITAL_DETAIL_SORT_TYPE.E_CDST_DECRREASE;
        req.iStartxh = 0;
        req.iNum = count;
        req.iColype = TYPE_CAPITAL_FLOW;

        DataEngine.getInstance().request(EntityObject.ET_GET_CAPITAL_FLOW_LIST, req, observer, extra);
    }

    /**
     * 获得概念资金流流入列表
     * @param observer
     */
    public static void getConceptCapitalFlowIncreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final CapitalDetailReq req = new CapitalDetailReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.eSetType = E_CAPITAL_DETAIL_SET_TYPE.E_CDST_CONC;
        req.eDataType = E_CAPITAL_DETAIL_DATA_TYPE.E_CDDT_1_DAY;
        req.eSortType = E_CAPITAL_DETAIL_SORT_TYPE.E_CDST_INCEREASE;
        req.iStartxh = 0;
        req.iNum = count;
        req.iColype = TYPE_CAPITAL_FLOW;

        DataEngine.getInstance().request(EntityObject.ET_GET_CAPITAL_FLOW_LIST, req, observer, extra);
    }

    /**
     * 获得概念资金流流出列表
     * @param observer
     */
    public static void getConceptCapitalFlowDecreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final CapitalDetailReq req = new CapitalDetailReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.eSetType = E_CAPITAL_DETAIL_SET_TYPE.E_CDST_CONC;
        req.eDataType = E_CAPITAL_DETAIL_DATA_TYPE.E_CDDT_1_DAY;
        req.eSortType = E_CAPITAL_DETAIL_SORT_TYPE.E_CDST_DECRREASE;
        req.iStartxh = 0;
        req.iNum = count;
        req.iColype = TYPE_CAPITAL_FLOW;

        DataEngine.getInstance().request(EntityObject.ET_GET_CAPITAL_FLOW_LIST, req, observer, extra);
    }

    /**
     * 获得概念板块列表的列表
     * @param observer
     */
    public static void getConceptPlateListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_PLATE_CONC;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_INCEREASE;

        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得行业板块的列表
     * @param observer
     */
    public static void getIndustryPlateListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_PLATE_BUSS;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_INCEREASE;

        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得涨幅榜股票列表
     * @param observer
     */
    public static void getStockIncreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_RANK;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_INCEREASE;

        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得跌幅榜股票列表
     * @param observer
     */
    public static void getStockDecreaseListRequest(final int count, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_RANK;
        req.iWantnum = count;
        req.iColype = TYPE_UP_DOWN;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_DECRREASE;

        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得换手率股票列表
     * @param observer
     */
    public static void getStockTurnoverRateListRequest(final int num, final DataSourceProxy.IRequestCallback observer, Object extra) {
        final PlateQuoteReq req = new PlateQuoteReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.ePlateQuoteReqType = E_PLATE_QUOTE_REQ_TYPE.E_PQT_RANK;
        req.iWantnum = num;
        req.iColype = TYPE_TURNOVER_RATE;
        req.ePlateQuoteSortType = E_PLATE_QUOTE_SORT_TYPE.E_PQT_INCEREASE;

        DataEngine.getInstance().request(EntityObject.ET_GET_UPDOWN_LIST, req, observer, extra);
    }

    /**
     * 获得涨跌分布
     */
    public static void requestChangeStat(final DataSourceProxy.IRequestCallback observer, final Object extra) {
        final ChangeStatReq req = new ChangeStatReq();
        req.iStart = -1;
        req.iWantNum = 1;
        req.isNeedAll = true;
        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_CHANGE_STAT, req, observer, extra);
    }

    /**
     * 获得板块的股票列表
     * @param observer
     */
    public static void getStockListInPlateRequest(final String unicode, final DataSourceProxy.IRequestCallback observer, final Object extra) {
        final PlateStockListReq req = new PlateStockListReq();
        req.vGuid = SDKManager.getInstance().getGuidBytes();
        req.sDtSecCode = unicode;

        DataEngine.getInstance().request(EntityObject.ET_GET_STOCK_LIST_IN_INDUSTRY, req, observer, extra);
    }

    public static void getBSInfoRequest(final String dtCode, final String date, final int size, final DataSourceProxy.IRequestCallback observer) {
        final GetSecBsInfoReq req = new GetSecBsInfoReq();
        req.sDtSecCode = dtCode;
        req.sDate = date;
        req.iSize = size;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_SEC_BS_INFO, req, observer);
    }

    public static void getMainBoardWarningInfoRequest(final long currentTimestamp, final DataSourceProxy.IRequestCallback observer) {
//        final String date = TimeUtils.getTimeString("yyyyMMdd", currentTimestamp);
//        getBSInfoRequest(CommonConst.SHANGHAI_INDEX_DT_CODE, date, 1, observer);
        RealMarketQRReq req = new RealMarketQRReq();
        DataEngine.getInstance().request(EntityObject.ET_REQUEST_MAKE_MONEY_STATE, req, observer);
    }
}
