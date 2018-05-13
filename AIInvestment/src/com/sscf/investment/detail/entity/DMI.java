package com.sscf.investment.detail.entity;

/**
 * Created by LEN on 2018/3/16.
 */

public class DMI {

    private float pdi;

    private float mdi;

    private float adx;

    private float adxr;

    public DMI(float pdi, float mdi, float adx, float adxr) {
        this.pdi = pdi;
        this.mdi = mdi;
        this.adx = adx;
        this.adxr = adxr;
    }

    public float getPdi() {
        return pdi;
    }

    public void setPdi(float pdi) {
        this.pdi = pdi;
    }

    public float getMdi() {
        return mdi;
    }

    public void setMdi(float mdi) {
        this.mdi = mdi;
    }

    public float getAdx() {
        return adx;
    }

    public void setAdx(float adx) {
        this.adx = adx;
    }

    public float getAdxr() {
        return adxr;
    }

    public void setAdxr(float adxr) {
        this.adxr = adxr;
    }

    public boolean isIllegal(){
        return !(pdi == 0 && mdi == 0 && adx == 0 && adxr == 0);
    }
}
