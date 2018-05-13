package com.dengtacj.component.entity.payment;

import java.io.Serializable;

/**
 * Created by davidwei on 2017-02-20.
 */

public final class OrderInfo implements Serializable {
    public String id;
    public String name;
    public int amount;
    /**
     * 取值请查看E_DT_SUBJECT_TYPE
     */
    public int type;
    /**
     * 取值请查看E_THIRD_PAY_SOURCE
     */
    public int thirdPaySource;
    /**
     * 取值请查看E_H5_PAY_OPEN_TYPE
     */
    public int h5OpenType;
    /**
     * 取值请查看E_DT_PAY_STATUS
     */
    public int status;
    /**
     * 取值请查看E_DT_PAY_TYPE
     */
    public int payType;

    public OrderInfo(String id, String name, int amount, int type, int thirdPaySource, int h5OpenType) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.type = type;
        this.thirdPaySource = thirdPaySource;
        this.h5OpenType = h5OpenType;
    }
}
