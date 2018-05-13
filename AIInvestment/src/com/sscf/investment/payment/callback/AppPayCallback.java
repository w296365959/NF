package com.sscf.investment.payment.callback;

import com.sscf.investment.payment.entity.WxPrepayId;

/**
 * Created by yorkeehuang on 2017/10/19.
 */

public interface AppPayCallback extends PayResultCallback {
    int SIGN_EXTRA_ERROR = -1000;

    void OnGetAlipaySignSuccess(String sign);

    void OnGetAlipaySignError(int error);

    void OnGetWxpayPrepayIdSuccess(WxPrepayId prepayId);

    void OnGetWxpayPrepayIdError(int error);
}
