package com.sscf.investment.detail.entity;

/**
 * Created by LEN on 2018/3/28.
 */

public class Gap {

    private int index;

    private float high;

    private float low;

    private boolean enable = true;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Gap() {
    }

    public Gap(int index, float high, float low) {
        this.index = index;
        this.high = high;
        this.low = low;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public boolean isEnable() {
        return enable;
    }
}
