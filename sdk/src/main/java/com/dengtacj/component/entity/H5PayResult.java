package com.dengtacj.component.entity;

import java.io.Serializable;

/**
 * Created by yorkeehuang on 2017/10/18.
 */

public class H5PayResult implements Serializable {
    public int result;
    public int payType;

    public H5PayResult(int result, int payType) {
        this.result = result;
        this.payType = payType;
    }
}
