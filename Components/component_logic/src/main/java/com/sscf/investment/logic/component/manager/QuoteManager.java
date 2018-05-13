package com.sscf.investment.logic.component.manager;

import android.text.TextUtils;
import android.util.SparseArray;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IDataCacheManager;
import com.dengtacj.component.managers.IQuoteManager;
import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.EntityUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import BEC.SecQuote;
import BEC.SecSimpleQuote;

public final class QuoteManager implements IQuoteManager, DataSourceProxy.IRequestCallback {

    private final SparseArray mCallbacks = new SparseArray();
    /**
     * 保存dtSecCode顺序使用的，返回时候的顺序与请求时的顺序一样
     */
    private final SparseArray<ArrayList<String>> mDtSecCodeListMap = new SparseArray<ArrayList<String>>();
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

    private Integer putDtSecCodeList(final int callbackId, final ArrayList<String> dtSecCodeList) {
        mDtSecCodeListMap.put(callbackId, dtSecCodeList);
        return callbackId;
    }

    private ArrayList<String> getDtSecCodeList(final int callbackId) {
        final ArrayList<String> dtSecCodeList = mDtSecCodeListMap.get(callbackId);
        mDtSecCodeListMap.remove(callbackId);
        return dtSecCodeList;
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        final Integer callbackId = (Integer) data.getExtra();
        if (callbackId == null) {
            return;
        }
        final Object callback = getCallback(callbackId);
        if (callback == null) {
            return;
        }

        switch (data.getEntityType()) {
            case EntityObject.ET_GET_SIMPLE_QUOTE:
                if (callback instanceof OnGetDataCallback) {
                    final ArrayList<String> dtSecCodeList = getDtSecCodeList(callbackId);
                    if (dtSecCodeList == null) {
                        final SecSimpleQuote quote = EntityUtil.entityToSecSimpleQuote(success, data);
                        final IDataCacheManager dataCacheManager = (IDataCacheManager) ComponentManager.getInstance()
                                .getManager(IDataCacheManager.class.getName());
                        if (dataCacheManager != null) {
                            dataCacheManager.setSecSimpleQuote(quote);
                        }
                        ((OnGetDataCallback<SecSimpleQuote>) callback).onGetData(quote);
                    } else {
                        final ArrayList<SecSimpleQuote> quotes = EntityUtil.entityToSecSimpleQuoteList(success, data);
                        ((OnGetDataCallback<ArrayList<SecSimpleQuote>>) callback).onGetData(getSortSecSimpleQuoteList(dtSecCodeList, quotes));
                    }
                }
                break;
            case EntityObject.ET_GET_QUOTE:
                if (callback instanceof OnGetDataCallback) {
                    final ArrayList<String> dtSecCodeList = getDtSecCodeList(callbackId);
                    if (dtSecCodeList == null) {
                        final SecQuote quote = EntityUtil.entityToSecQuote(success, data);
                        final IDataCacheManager dataCacheManager = (IDataCacheManager) ComponentManager.getInstance()
                                .getManager(IDataCacheManager.class.getName());
                        if (dataCacheManager != null) {
                            dataCacheManager.setSecQuote(quote);
                        }
                        ((OnGetDataCallback<SecQuote>) callback).onGetData(quote);
                    } else {
                        final ArrayList<SecQuote> quotes = EntityUtil.entityToSecQuoteList(success, data);
                        ((OnGetDataCallback<ArrayList<SecQuote>>) callback).onGetData(getSortSecQuoteList(dtSecCodeList, quotes));
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void requestQuote(String dtSecCode, OnGetDataCallback<SecQuote> callback) {
        final int callbackId = putCallback(callback);
        QuoteRequestManager.getQuoteRequest(dtSecCode, this, callbackId);
    }

    @Override
    public void requestQuote(ArrayList<String> dtSecCodeList, OnGetDataCallback<ArrayList<SecQuote>> callback) {
        final int callbackId = putCallback(callback);
        putDtSecCodeList(callbackId, dtSecCodeList);
        QuoteRequestManager.getQuoteRequest(dtSecCodeList, this, callbackId);
    }

    @Override
    public void requestSimpleQuote(String dtSecCode, OnGetDataCallback<SecSimpleQuote> callback) {
        final int callbackId = putCallback(callback);
        QuoteRequestManager.getSimpleQuoteRequest(dtSecCode, this, callbackId);
    }

    @Override
    public void requestSimpleQuote(ArrayList<String> dtSecCodeList, OnGetDataCallback<ArrayList<SecSimpleQuote>> callback) {
        final int callbackId = putCallback(callback);
        putDtSecCodeList(callbackId, dtSecCodeList);
        QuoteRequestManager.getSimpleQuoteRequest(dtSecCodeList, this, callbackId);
    }

    /**
     * 重新按照写死的顺序排列
     * @param dtSecCodes
     * @param secList
     */
    private static ArrayList<SecSimpleQuote> getSortSecSimpleQuoteList(ArrayList<String> dtSecCodes, ArrayList<SecSimpleQuote> secList) {
        if (secList == null) {
            return null;
        }
        final ArrayList<SecSimpleQuote> stockList = new ArrayList<SecSimpleQuote>(dtSecCodes.size());
        for (String dtsecCode : dtSecCodes) {
            stockList.add(findSimpleQuote(dtsecCode, secList));
        }
        return stockList;
    }

    private static SecSimpleQuote findSimpleQuote(String dtsecCode, ArrayList<SecSimpleQuote> secList) {
        final IDataCacheManager dataCacheManager = (IDataCacheManager) ComponentManager.getInstance()
                .getManager(IDataCacheManager.class.getName());
        final Iterator<SecSimpleQuote> it = secList.iterator();
        SecSimpleQuote quote;
        while (it.hasNext()) {
            quote = it.next();
            if (TextUtils.equals(dtsecCode, quote.sDtSecCode)) {
                it.remove();
                if (dataCacheManager != null) {
                    dataCacheManager.setSecSimpleQuote(quote);
                }
                return quote;
            }
        }
        // 没有拉取到的行情先从cache里获得
        quote = null;
        if (dataCacheManager != null) {
            quote = dataCacheManager.getSecSimpleQuote(dtsecCode);
        }
        if (quote == null) {
            quote = new SecSimpleQuote();
            quote.sDtSecCode = dtsecCode;
        }
        return quote;
    }

    /**
     * 重新按照写死的顺序排列
     * @param dtSecCodes
     * @param secList
     */
    private static ArrayList<SecQuote> getSortSecQuoteList(ArrayList<String> dtSecCodes, ArrayList<SecQuote> secList) {
        if (secList == null) {
            return null;
        }
        final ArrayList<SecQuote> stockList = new ArrayList<SecQuote>(dtSecCodes.size());
        for (String dtsecCode : dtSecCodes) {
            stockList.add(findQuote(dtsecCode, secList));
        }
        return stockList;
    }

    private static SecQuote findQuote(String dtsecCode, ArrayList<SecQuote> secList) {
        final IDataCacheManager dataCacheManager = (IDataCacheManager) ComponentManager.getInstance()
                .getManager(IDataCacheManager.class.getName());
        final Iterator<SecQuote> it = secList.iterator();
        SecQuote quote;
        while (it.hasNext()) {
            quote = it.next();
            if (TextUtils.equals(dtsecCode, quote.sDtSecCode)) {
                it.remove();
                if (dataCacheManager != null) {
                    dataCacheManager.setSecQuote(quote);
                }
                return quote;
            }
        }
        // 没有拉取到的行情先从cache里获得
        quote = null;
        if (dataCacheManager != null) {
            quote = dataCacheManager.getSecQuote(dtsecCode);
        }
        if (quote == null) {
            quote = new SecQuote();
            quote.sDtSecCode = dtsecCode;
        }

        return quote;
    }
}
