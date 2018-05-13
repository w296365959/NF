package com.sscf.investment.payment.callback;

/**
 * Created by yorkeehuang on 2017/10/19.
 */

public interface H5PayCallback extends PayResultCallback {

    int URL_EXTRA_ERROR = -1001;

    void OnGetH5PayUrlSuccess(int payType, String url);

    void OnGetH5PayUrlError(int payType, int error);

    void OnGetPayResult(int payType, int status);
}
