package com.sscf.investment.payment.model;

import android.text.TextUtils;

import com.sscf.investment.payment.PaymentRequestManager;
import com.sscf.investment.payment.callback.H5PayCallback;
import com.sscf.investment.payment.presenter.H5PayPresenter;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;

import BEC.E_DT_PAY_ERROR_CODE;
import BEC.E_DT_PAY_STATUS;
import BEC.GetH5PayUrlRsp;
import BEC.GetOrderPayResultRsp;

/**
 * Created by yorkeehuang on 2017/10/19.
 */

public class H5PayModel extends PayModel implements DataSourceProxy.IRequestCallback {

    private static final String TAG = H5PayModel.class.getSimpleName();

    private H5PayPresenter mPresenter;

    public H5PayModel(H5PayPresenter presenter) {
        mPresenter = presenter;
    }

    public boolean requestAliH5Prepay(String orderId) {
        return PaymentRequestManager.requestAliH5PayUrl(orderId, mPresenter.getThirdPartySource(), this);
    }

    public boolean requestWxH5Prepay(String orderId) {
        return PaymentRequestManager.requestWxH5PayUrl(orderId, mPresenter.getThirdPartySource(), this);
    }

    public boolean requestOrderResult(String orderId, int payType) {
        return PaymentRequestManager.requestRefreshOrderResult(orderId, payType, this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_H5_PAY_URL:
                handleH5PayCallback(success, data);
                break;
            case EntityObject.ET_GET_ORDER_RESULT:
                handleGetOrderResultCallback(success, data);
                break;
            default:
        }
    }

    private void handleH5PayCallback(final boolean success, final EntityObject data) {
        final int payType = getPayType(data);
        if(success && data.getEntity() != null) {
            GetH5PayUrlRsp rsp = (GetH5PayUrlRsp) data.getEntity();
            if(isOrderError(rsp.getIReturnCode())){
                mPresenter.OnGetH5PayUrlError(payType, rsp.getIReturnCode());
            } else {
                final String payUrl = rsp.getSPayUrl();
                DtLog.d(TAG, "handleH5PayCallback() payUrl = " + payUrl);
                if(!TextUtils.isEmpty(payUrl)) {
                    mPresenter.OnGetH5PayUrlSuccess(payType, payUrl);
                } else {
                    mPresenter.OnGetH5PayUrlError(payType, H5PayCallback.URL_EXTRA_ERROR);
                }
            }
        } else {
            mPresenter.OnGetH5PayUrlError(payType, H5PayCallback.URL_EXTRA_ERROR);
        }
    }

    private void handleGetOrderResultCallback(final boolean success, final EntityObject data) {
        final int payType = getPayType(data);
        if(success && data.getEntity() != null) {
            GetOrderPayResultRsp rsp = (GetOrderPayResultRsp) data.getEntity();
            if(rsp.getIReturnCode() == E_DT_PAY_ERROR_CODE.E_DT_PAY_SUCC) {
                switch (rsp.getIPayStatus()) {
                    case E_DT_PAY_STATUS.E_DT_PAY_SUCCESS:
                        mPresenter.OnGetPayResult(payType, H5PayCallback.PAY_SUCCESS);
                        break;
                    case E_DT_PAY_STATUS.E_DT_PAY_FAIL:
                        mPresenter.OnGetPayResult(payType, H5PayCallback.PAY_FAIL);
                        break;
                    default:
                        mPresenter.OnGetPayResult(payType, H5PayCallback.PAY_UNKNOWN);
                }
            } else {
                mPresenter.OnGetPayResult(payType, H5PayCallback.PAY_FAIL);
            }
        } else {
            mPresenter.OnGetPayResult(payType, H5PayCallback.PAY_UNKNOWN);
        }
    }
}
