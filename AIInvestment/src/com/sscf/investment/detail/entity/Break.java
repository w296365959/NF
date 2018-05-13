package com.sscf.investment.detail.entity;

/**
 * Created by LEN on 2018/3/30.
 */

public class Break {

    private float dif;

    private float dea;

    public Break(float dif, float dea) {
        this.dif = dif;
        this.dea = dea;
    }

    public float getDif() {
        return dif;
    }

    public void setDif(float dif) {
        this.dif = dif;
    }

    public float getDea() {
        return dea;
    }

    public void setDea(float dea) {
        this.dea = dea;
    }
}
