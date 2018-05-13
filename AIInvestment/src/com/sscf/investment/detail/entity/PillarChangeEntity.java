package com.sscf.investment.detail.entity;

import BEC.SeasonOperatingRevenue;

import java.util.ArrayList;

/**
 * Created by liqf on 2015/7/23.
 */
public class PillarChangeEntity {
    private float mValue;
    private float mDelta;
    private String mTime;
    private ArrayList<SeasonOperatingRevenue> mSeasonIncomeList;

    public PillarChangeEntity(String time, float value, float delta) {
        this.mTime = time;
        this.mValue = value;
        this.mDelta = delta;
    }

    public PillarChangeEntity(String time, float value, float delta, ArrayList<SeasonOperatingRevenue> seasonIncomeList) {
        this.mTime = time;
        this.mValue = value;
        this.mDelta = delta;
        this.mSeasonIncomeList = seasonIncomeList;
    }

    public float getValue() {
        return mValue;
    }

    public float getDelta() {
        return mDelta;
    }

    public String getTime() {
        return mTime;
    }

    public ArrayList<SeasonOperatingRevenue> getSeasonIncomeList() {
        return mSeasonIncomeList;
    }
}
