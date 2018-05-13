package com.sscf.investment.widget.linechart;

/**
 * Created by yorkeehuang on 2017/5/10.
 */

public class TimeLineInfo {
    private float mYesterdayClose;
    private float mHigh;
    private float mLow;
    private boolean mIsRefYesterdayClose = true;
    private boolean mIsSuspended = false;

    public void setIsRefYesterdayClose(boolean isRefYesterdayClose) {
        mIsRefYesterdayClose = isRefYesterdayClose;
    }

    public boolean isRefYesterdayClose() {
        return mIsRefYesterdayClose;
    }

    public void setYesterdayClose(float yesterdayClose) {
        mYesterdayClose = yesterdayClose;
    }

    public float getYesterdayClose() {
        return mYesterdayClose;
    }

    public void setHigh(float high) {
        mHigh = high;
    }

    public float getHigh() {
        return mHigh;
    }

    public void setLow(float low) {
        mLow = low;
    }

    public float getLow() {
        return mLow;
    }

    public void setIsSuspended(boolean isSuspended) {
        mIsSuspended = isSuspended;
    }

    public boolean isSuspended() {
        return mIsSuspended;
    }
}
