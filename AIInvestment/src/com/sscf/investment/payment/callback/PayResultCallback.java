package com.sscf.investment.payment.callback;

/**
 * Created by yorkeehuang on 2017/10/19.
 */

public interface PayResultCallback {
    int PAY_UNKNOWN = 0;
    int PAY_SUCCESS = 1;
    int PAY_FAIL = 2;

    void OnGetPayResult(int payType, int status);
}
