package com.dengtacj.component.entity.payment;

import java.io.Serializable;

/**
 * Created by yorkeehuang on 2017/5/13.
 */

public class CommodityInfo implements Serializable {
    /**
     * 取值请查看E_DT_SUBJECT_TYPE
     */
    public int type;
    /**
     * 商品数量
     */
    public int number;
    /**
     * 商品描述
     */
    public String desc;
    /**
     * 商品价格
     */
    public int value;
    /**
     * 优惠券数量
     */
    public int couponNum;

    /**
     * 商品单位
     */
    public int unit;
    /**
     * 附加信息
     */
    public String extra;

    public CommodityInfo(int type, int number, String desc, int value, int unit, String extra) {
        this.type = type;
        this.number = number;
        this.desc = desc;
        this.value = value;
        this.unit = unit;
        this.extra = extra;
    }
}
