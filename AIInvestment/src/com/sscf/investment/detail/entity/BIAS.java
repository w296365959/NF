package com.sscf.investment.detail.entity;

/**
 * Created by LEN on 2018/3/21.
 */

public class BIAS {

    private float value1;

    private float value2;

    private float value3;

    public BIAS(float value1, float value2, float value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    public float getValue1() {
        return value1;
    }

    public void setValue1(float value1) {
        this.value1 = value1;
    }

    public float getValue2() {
        return value2;
    }

    public void setValue2(float value2) {
        this.value2 = value2;
    }

    public float getValue3() {
        return value3;
    }

    public void setValue3(float value3) {
        this.value3 = value3;
    }
}
