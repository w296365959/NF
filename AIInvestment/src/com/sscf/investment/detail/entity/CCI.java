package com.sscf.investment.detail.entity;

/**
 * Created by LEN on 2018/3/19.
 */

public class CCI {

    private float cci;

    private float ccia;

    public float getCci() {
        return cci;
    }

    public void setCci(float cci) {
        this.cci = cci;
    }

    public float getCcia() {
        return ccia;
    }

    public void setCcia(float ccia) {
        this.ccia = ccia;
    }

    public CCI(float cci, float ccia) {

        this.cci = cci;
        this.ccia = ccia;
    }

    public float getCCI() {
        return cci;
    }

    public void setValue(float value) {
        this.cci = value;
    }

    public CCI(float cci) {
        this.cci = cci;
    }
}
