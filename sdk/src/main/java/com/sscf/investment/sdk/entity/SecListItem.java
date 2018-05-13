package com.sscf.investment.sdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liqf on 2015/11/2.
 */
public class SecListItem implements Parcelable {
    private String mDtSecCode;
    private String mName;

    public SecListItem() {

    }

    protected SecListItem(Parcel in) {
        mDtSecCode = in.readString();
        mName = in.readString();
    }

    public static final Creator<SecListItem> CREATOR = new Creator<SecListItem>() {
        @Override
        public SecListItem createFromParcel(Parcel in) {
            return new SecListItem(in);
        }

        @Override
        public SecListItem[] newArray(int size) {
            return new SecListItem[size];
        }
    };

    public String getDtSecCode() {
        return mDtSecCode;
    }

    public void setDtSecCode(String dtSecCode) {
        mDtSecCode = dtSecCode;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDtSecCode);
        dest.writeString(mName);
    }
}
