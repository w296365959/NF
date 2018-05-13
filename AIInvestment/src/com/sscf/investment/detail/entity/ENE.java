package com.sscf.investment.detail.entity;

/**
 * Created by LEN on 2018/3/19.
 */

public class ENE {

    private float upper;

    private float lower;

    private float ene;

    public float getUpper() {
        return upper;
    }

    public void setUpper(float upper) {
        this.upper = upper;
    }

    public float getLower() {
        return lower;
    }

    public void setLower(float lower) {
        this.lower = lower;
    }

    public float getEne() {
        return ene;
    }

    public void setEne(float ene) {
        this.ene = ene;
    }

    public ENE(float upper, float lower, float ene) {
        this.upper = upper;
        this.lower = lower;
        this.ene = ene;
    }
}
