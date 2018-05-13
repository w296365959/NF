package com.sscf.investment.detail.entity;

/**
 * Created by liqf on 2016/1/11.
 */
public class KZJBY {
    public float getSsup() {
        return ssup;
    }

    public void setSsup(float ssup) {
        this.ssup = ssup;
    }

    public float getSbig() {
        return sbig;
    }

    public void setSbig(float sbig) {
        this.sbig = sbig;
    }

    public float getSmid() {
        return smid;
    }

    public void setSmid(float smid) {
        this.smid = smid;
    }

    public float getSsmall() {
        return ssmall;
    }

    public void setSsmall(float ssmall) {
        this.ssmall = ssmall;
    }

    private float ssup;
    private float sbig;
    private float smid;
    private float ssmall;

    public KZJBY(float ssup, float sbig, float smid, float ssmall) {
        this.ssup = ssup;
        this.sbig = sbig;
        this.smid = smid;
        this.ssmall = ssmall;
    }
}
