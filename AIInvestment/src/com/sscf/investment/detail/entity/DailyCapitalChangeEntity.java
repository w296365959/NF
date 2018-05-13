package com.sscf.investment.detail.entity;

/**
 * Created by liqf on 2015/7/21
 *
 */
public class DailyCapitalChangeEntity {
    private String day;
    private float value;

    public DailyCapitalChangeEntity(String day, float value) {
        this.day = day;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public String getDay() {
        return day;
    }
}
