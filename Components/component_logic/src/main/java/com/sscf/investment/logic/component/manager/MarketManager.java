package com.sscf.investment.logic.component.manager;

import android.util.SparseArray;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.dengtacj.request.MarketRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.SettingPref;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import BEC.AHExtendInfo;
import BEC.AHPlateDesc;
import BEC.CapitalDetailDesc;
import BEC.CapitalMainFlowDesc;
import BEC.ChangeStatRsp;
import BEC.PlateQuoteDesc;
import BEC.PrivInfo;
import BEC.SecSimpleQuote;

public final class MarketManager implements IMarketManager, DataSourceProxy.IRequestCallback {

    private final SparseArray mCallbacks = new SparseArray();
    private final AtomicInteger mCounter = new AtomicInteger();

    private Integer putCallback(final Object callback) {
        final int callbackId = mCounter.incrementAndGet();
        mCallbacks.put(callbackId, callback);
        return callbackId;
    }

    private Object getCallback(final int callbackId) {
        final Object callback = mCallbacks.get(callbackId);
        mCallbacks.remove(callbackId);
        return callback;
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        final Integer callbackId = (Integer) data.getExtra();
        if (callbackId == null) {
            return;
        }
        final Object callback = getCallback(callbackId);

        switch (data.getEntityType()) {
            case EntityObject.ET_GET_UPDOWN_LIST:
                if (callback == null) {
                    return;
                }
                if (callback instanceof OnGetDataCallback) {
                    final ArrayList<PlateQuoteDesc> stockList = EntityUtil.entityToPlateQuoteDescList(success, data);
                    ((OnGetDataCallback<ArrayList<PlateQuoteDesc>>) callback).onGetData(stockList);
                }
                break;
            case EntityObject.ET_GET_AH_STOCK:
                if (callback == null) {
                    return;
                }
                if (callback instanceof OnGetDataCallback) {
                    final ArrayList<AHPlateDesc> stockList = EntityUtil.entityToAHPlateDescList(success, data);
                    ((OnGetDataCallback<ArrayList<AHPlateDesc>>) callback).onGetData(stockList);
                }
                break;
            case EntityObject.ET_GET_USA_PRIVATIZATION_TRACKING_LIST:
                if (callback == null) {
                    return;
                }
                if (callback instanceof OnGetDataCallback) {
                    final ArrayList<PrivInfo> stockList = EntityUtil.entityToPrivInfoList(success, data);
                    ((OnGetDataCallback<ArrayList<PrivInfo>>) callback).onGetData(stockList);
                }
                break;
            case EntityObject.ET_GET_STOCK_LIST_IN_INDUSTRY:
                if (callback == null) {
                    return;
                }
                if (callback instanceof OnGetDataCallback) {
                    final ArrayList<SecSimpleQuote> stockList = EntityUtil.entityToSecSimpleQuoteListInPlate(success, data);
                    ((OnGetDataCallback<ArrayList<SecSimpleQuote>>) callback).onGetData(stockList);
                }
                break;
            case EntityObject.ET_GET_SH_HK_CONNECT_BALANCE:
                final AHExtendInfo ahExtendInfo = EntityUtil.entityToAHExtendInfo(success, data);
                if (ahExtendInfo != null) {
                    float exchangeRate = ahExtendInfo.fRmbHkExchangeRate;
                    if (exchangeRate > 0) {
                        SettingPref.putFloat(KEY_HK_DOLLARS_EXCHANGE_RATE, exchangeRate);
                    }
                }
                if (callback == null) {
                    return;
                }
                if (callback instanceof OnGetDataCallback) {
                    ((OnGetDataCallback<AHExtendInfo>) callback).onGetData(ahExtendInfo);
                }
                break;
            case EntityObject.ET_GET_CAPITAL_FLOW_LIST:
                if (callback == null) {
                    return;
                }
                if (callback instanceof OnGetDataCallback) {
                    ((OnGetDataCallback<ArrayList<CapitalDetailDesc>>) callback).onGetData(
                            EntityUtil.entityToCapitalDetailDescList(success, data));
                }
                break;
            case EntityObject.ET_GET_CAPITAL_FLOW_MULTI_STOCK:
                if (callback == null) {
                    return;
                }
                if (callback instanceof OnGetDataCallback) {
                    ((OnGetDataCallback<ArrayList<CapitalMainFlowDesc>>) callback).onGetData(
                            EntityUtil.entityToCapitalMainFlowDescList(success, data));
                }
                break;
            case EntityObject.ET_GET_CHANGE_STAT:
                if (callback == null) {
                    return;
                }
                if (callback instanceof OnGetDataCallback) {
                    ((OnGetDataCallback<ChangeStatRsp>) callback).onGetData(
                            EntityUtil.entityToChangeStatRsp(success, data));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void requestIndustryPlateList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getIndustryPlateListRequest(num, this, callbackId);
    }

    @Override
    public void requestConceptPlateList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getConceptPlateListRequest(num, this, callbackId);
    }

    @Override
    public void requestStockIncreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getStockIncreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestStockDecreaseList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getStockDecreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestStockTurnoverRateList(final int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getStockTurnoverRateListRequest(num, this, callbackId);
    }

    @Override
    public void requestHKMainStockIncreaseList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getHKMainStockIncreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestHKMainStockDecreaseList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getHKMainStockDecreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestHKGEMStockIncreaseList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getHKGEMStockIncreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestHKGEMStockDecreaseList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getHKGEMStockDecreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestSHConnectStockIncreaseList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getSHConnectStockIncreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestSHConnectStockDecreaseList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getSHConnectStockDecreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestHKConnectStockIncreaseList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getHKConnectStockIncreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestHKConnectStockDecreaseList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getHKConnectStockDecreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestCCSStockIncreaseList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getCCSStockIncreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestCCSStockDecreaseList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getCCSStockDecreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestSPXStockIncreaseList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getSPXStockIncreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestSPXStockDecreaseList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getSPXStockDecreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestMultiStockCapitalFlow(ArrayList<String> dtSecCodeList, OnGetDataCallback<ArrayList<CapitalMainFlowDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getMultiStockCapitalFlowRequest(dtSecCodeList, this, callbackId);
    }

    @Override
    public void requestStockCapitalFlowIncreaseList(int num, OnGetDataCallback<ArrayList<CapitalDetailDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getStockCapitalFlowIncreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestStockCapitalFlowDecreaseList(int num, OnGetDataCallback<ArrayList<CapitalDetailDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getStockCapitalFlowDecreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestIndustryCapitalFlowIncreaseList(int num, OnGetDataCallback<ArrayList<CapitalDetailDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getIndustryCapitalFlowIncreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestIndustryCapitalFlowDecreaseList(int num, OnGetDataCallback<ArrayList<CapitalDetailDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getIndustryCapitalFlowDecreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestConceptCapitalFlowIncreaseList(int num, OnGetDataCallback<ArrayList<CapitalDetailDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getConceptCapitalFlowIncreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestConceptCapitalFlowDecreaseList(int num, OnGetDataCallback<ArrayList<CapitalDetailDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getConceptCapitalFlowDecreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestAHStockIncreaseList(int num, OnGetDataCallback<ArrayList<AHPlateDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getAHStockIncreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestAHStockDecreaseList(int num, OnGetDataCallback<ArrayList<AHPlateDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getAHStockDecreaseListRequest(num, this, callbackId);
    }

    @Override
    public void requestIndexFuturesList(int num, OnGetDataCallback<ArrayList<PlateQuoteDesc>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getIndexFuturesListRequest(num, this, callbackId);
    }

    @Override
    public void requestChangeStat(OnGetDataCallback<ChangeStatRsp> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.requestChangeStat(this, callbackId);
    }

    @Override
    public void requestStockListInPlate(String plateCode, OnGetDataCallback<ArrayList<SecSimpleQuote>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getStockListInPlateRequest(plateCode, this, callbackId);
    }

    @Override
    public void requestPrivatizationTrackingList(int num, OnGetDataCallback<ArrayList<PrivInfo>> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getPrivatizationTrackingList(num, this, callbackId);
    }

    @Override
    public void requestSHHKConnectBalance(OnGetDataCallback<AHExtendInfo> callback) {
        final int callbackId = putCallback(callback);
        MarketRequestManager.getSHHKConnectBalanceRequest(this, callbackId);
    }

    private static final String KEY_HK_DOLLARS_EXCHANGE_RATE = "hk_dollars_exchange_rate";

    @Override
    public float getHKDollarsExchangeRate() {
        final int callbackId = putCallback(null);
        MarketRequestManager.getSHHKConnectBalanceRequest(this, callbackId);
        return SettingPref.getFloat(KEY_HK_DOLLARS_EXCHANGE_RATE, 0.8f);
    }
}
