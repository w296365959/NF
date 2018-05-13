package com.sscf.investment.detail.entity;

import java.util.ArrayList;
import BEC.SecQuote;

/**
 * Created by liqf on 2015/8/27.
 */
public final class BuySellEntity {
    public float[] getBuyValues() {
        return mBuyValues;
    }

    private float[] mBuyValues;

    public long[] getBuyVolumes() {
        return mBuyVolumes;
    }

    public float[] getSellValues() {
        return mSellValues;
    }

    public long[] getSellVolumes() {
        return mSellVolumes;
    }

    private long[] mBuyVolumes;
    private float[] mSellValues;
    private long[] mSellVolumes;

    private float mYesterdayClose;

    private int iTpFlag;

    public BuySellEntity(final SecQuote quote) {
        iTpFlag = quote.iTpFlag;
        mYesterdayClose = quote.fClose;
        final int LENGTH = 5;
        mBuyValues = floatListToArray(quote.vBuyp, new float[LENGTH]);
        mBuyVolumes = longListToArray(quote.vBuyv, new long[LENGTH]);
        mSellValues = floatListToArray(quote.vSellp, new float[LENGTH]);
        mSellVolumes = longListToArray(quote.vSellv, new long[LENGTH]);
    }

    private float[] floatListToArray(ArrayList<Float> list, final float[] arr) {
        final int size = Math.min(list == null ? 0 : list.size(), arr == null ? 0 : arr.length);
        for (int i = 0; i < size; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    private long[] longListToArray(ArrayList<Long> list, final long[] arr) {
        final int size = Math.min(list == null ? 0 : list.size(), arr == null ? 0 : arr.length);
        for (int i = 0; i < size; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public float getYesterdayClose() {
        return mYesterdayClose;
    }

    public int getTpFlag() {
        return iTpFlag;
    }
}
