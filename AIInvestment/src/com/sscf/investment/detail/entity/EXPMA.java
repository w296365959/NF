package com.sscf.investment.detail.entity;

import com.sscf.investment.sdk.utils.DtLog;

/**
 * Created by LEN on 2018/3/20.
 */

public class EXPMA {

    private float ma1;

    private float ma2;

    private float ma3;

    private float ma4;

    public float getMa1() {
        return ma1;
    }

    public void setMa1(float ma1) {
        this.ma1 = ma1;
    }

    public float getMa2() {
        return ma2;
    }

    public void setMa2(float ma2) {
        this.ma2 = ma2;
    }

    public float getMa3() {
        return ma3;
    }

    public void setMa3(float ma3) {
        this.ma3 = ma3;
    }

    public float getMa4() {
        return ma4;
    }

    public void setMa4(float ma4) {
        this.ma4 = ma4;
    }

    public EXPMA(float ma1, float ma2, float ma3, float ma4) {
        this.ma1 = ma1;
        this.ma2 = ma2;
        this.ma3 = ma3;
        this.ma4 = ma4;
    }

    @Override
    public String toString() {
        return "EXPMA{" +
                "ma1=" + ma1 +
                ", ma2=" + ma2 +
                ", ma3=" + ma3 +
                ", ma4=" + ma4 +
                '}';
    }
}
