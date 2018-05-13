package com.dengtacj.component.entity;

import java.io.Serializable;

/**
 * Created by yorkeehuang on 2017/5/13.
 */

public class CouponInfo implements Serializable {
    public int type;
    public String code;
    public int value;

    public CouponInfo(int type, String code, int value) {
        this.type = type;
        this.code = code;
        this.value = value;
    }
}
