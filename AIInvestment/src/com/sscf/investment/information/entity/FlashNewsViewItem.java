package com.sscf.investment.information.entity;

import BEC.NewsDesc;

/**
 * Created by liqf on 2016/3/25.
 */
public class FlashNewsViewItem {
    public static final int TYPE_ITEM_NEWS = 0;
    public static final int TYPE_ITEM_DATE = 1;

    public NewsDesc getNewsDesc() {
        return mNewsDesc;
    }

    public void setNewsDesc(NewsDesc newsDesc) {
        mNewsDesc = newsDesc;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    private NewsDesc mNewsDesc;
    private String mDate;
    private int mType;
}
