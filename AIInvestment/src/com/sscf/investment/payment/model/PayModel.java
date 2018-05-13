package com.sscf.investment.payment.model;

import com.sscf.investment.sdk.net.EntityObject;

import BEC.E_DT_PAY_TYPE;

/**
 * Created by yorkeehuang on 2017/10/20.
 */

public class PayModel {

    protected int getPayType(final EntityObject data) {
        if(data != null && data.getExtra() != null && data.getExtra() instanceof Integer) {
            return (Integer) data.getExtra();
        }
        return E_DT_PAY_TYPE.E_DT_PAY_ALI;
    }

    protected boolean isOrderError(final int returnCode) {
        return returnCode != 0;
    }
}
