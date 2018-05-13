package com.sscf.investment.payment.entity;

import BEC.WxPayPrepayInfo;

/**
 * Created by yorkeehuang on 2017/10/19.
 */

public class WxPrepayId {
    public String appId;
    public String partnerId;
    public String prepayId;
    public String nonceStr;
    public String timeStamp;
    public String packageValue;
    public String sign;

    public WxPrepayId(WxPayPrepayInfo info) {
        appId = info.sAppId;
        partnerId = info.sPartnerId;
        prepayId = info.sPrepayId;
        nonceStr = info.sNonceStr;
        timeStamp = String.valueOf(info.lTimeStamp);
        packageValue = info.sPackage;
        sign = info.sSign;
    }
}
