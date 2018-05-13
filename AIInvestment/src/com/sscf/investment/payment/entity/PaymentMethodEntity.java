package com.sscf.investment.payment.entity;

import com.sscf.investment.R;

/**
 * Created by yorkeehuang on 2017/10/19.
 */

public class PaymentMethodEntity {
    public int iconId;
    public int nameId;
    public int introId;

    public static PaymentMethodEntity createAlipayEntity() {
        final PaymentMethodEntity entity = new PaymentMethodEntity();
        entity.iconId = R.drawable.alipay;
        entity.nameId = R.string.payment_method_alipay;
        entity.introId = R.string.payment_method_alipay_intro;
        return entity;
    }

    public static PaymentMethodEntity createWechatEntity() {
        final PaymentMethodEntity entity = new PaymentMethodEntity();
        entity.iconId = R.drawable.wechat_pay;
        entity.nameId = R.string.payment_method_wechat;
        entity.introId = R.string.payment_method_wechat_intro;
        return entity;
    }
}
