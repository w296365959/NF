package com.dengtacj.component.managers;

import com.dengtacj.component.callback.OnGetDataCallback;
import java.util.ArrayList;
import BEC.AHExtendInfo;
import BEC.AHPlateDesc;
import BEC.CapitalDetailDesc;
import BEC.CapitalMainFlowDesc;
import BEC.ChangeStatRsp;
import BEC.PlateQuoteDesc;
import BEC.PrivInfo;
import BEC.SecSimpleQuote;

/**
 * Created by davidwei on 2017-09-29
 */
public interface IMarketManager {

    // ------------------- 涨跌幅榜 -----------------------------
    void requestIndustryPlateList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestConceptPlateList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestIndexFuturesList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestStockIncreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestStockDecreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestStockTurnoverRateList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestHKMainStockIncreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestHKMainStockDecreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestHKGEMStockIncreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestHKGEMStockDecreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestSHConnectStockIncreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestSHConnectStockDecreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestHKConnectStockIncreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestHKConnectStockDecreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestCCSStockIncreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestCCSStockDecreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestSPXStockIncreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    void requestSPXStockDecreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback);
    // ------------------- 涨跌幅榜 -----------------------------

    // ------------------- 资金流 -----------------------------
    void requestMultiStockCapitalFlow(final ArrayList<String> dtSecCodeList, OnGetDataCallback<ArrayList<CapitalMainFlowDesc>> callback);
    void requestStockCapitalFlowIncreaseList(final int num, OnGetDataCallback<ArrayList<CapitalDetailDesc>> callback);
    void requestStockCapitalFlowDecreaseList(final int num, OnGetDataCallback<ArrayList<CapitalDetailDesc>> callback);
    void requestIndustryCapitalFlowIncreaseList(final int num, OnGetDataCallback<ArrayList<CapitalDetailDesc>> callback);
    void requestIndustryCapitalFlowDecreaseList(final int num, OnGetDataCallback<ArrayList<CapitalDetailDesc>> callback);
    void requestConceptCapitalFlowIncreaseList(final int num, OnGetDataCallback<ArrayList<CapitalDetailDesc>> callback);
    void requestConceptCapitalFlowDecreaseList(final int num, OnGetDataCallback<ArrayList<CapitalDetailDesc>> callback);
    // ------------------- 资金流 -----------------------------

    // ------------------- ah列表 -----------------------------
    void requestAHStockIncreaseList(final int num, OnGetDataCallback<ArrayList<AHPlateDesc>> callback);
    void requestAHStockDecreaseList(final int num, OnGetDataCallback<ArrayList<AHPlateDesc>> callback);
    // ------------------- ah列表 -----------------------------

    /**
     * 获得涨跌分布
     * @param callback
     */
    void requestChangeStat(OnGetDataCallback<ChangeStatRsp> callback);
    void requestStockListInPlate(final String plateCode, OnGetDataCallback<ArrayList<SecSimpleQuote>> callback);

    /**
     * 美股私有化列表
     * @param num
     * @param callback
     */
    void requestPrivatizationTrackingList(final int num, OnGetDataCallback<ArrayList<PrivInfo>> callback);

    /**
     * 沪港通余额
     * @param callback
     */
    void requestSHHKConnectBalance(OnGetDataCallback<AHExtendInfo> callback);
    float getHKDollarsExchangeRate();
}
