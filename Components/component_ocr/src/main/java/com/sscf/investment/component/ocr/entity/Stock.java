package com.sscf.investment.component.ocr.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yorkeehuang on 2017/5/24.
 */

public class Stock implements Parcelable {
    private String mDtSecCode;
    private String mSecName;

    public Stock(String dtSecCode, String secName) {
        mDtSecCode = dtSecCode;
        mSecName = secName;
    }


    protected Stock(Parcel in) {
        mDtSecCode = in.readString();
        mSecName = in.readString();
    }

    public static final Parcelable.Creator<Stock> CREATOR = new Parcelable.Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

    public String getDtSecCode() {
        return mDtSecCode;
    }

    public void setDtSecCode(String dtSecCode) {
        mDtSecCode = dtSecCode;
    }

    public String getSecName() {
        return mSecName;
    }

    public void setName(String secname) {
        mSecName = secname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDtSecCode);
        dest.writeString(mSecName);
    }
}
