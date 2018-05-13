package com.sscf.investment.payment.model;

import android.text.TextUtils;

import com.sscf.investment.payment.PaymentRequestManager;
import com.sscf.investment.payment.callback.AppPayCallback;
import com.sscf.investment.payment.entity.WxPrepayId;
import com.sscf.investment.payment.presenter.AppPayPresenter;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;

import java.util.Map;

import BEC.E_DT_PAY_STATUS;
import BEC.GetAliPaySignRsp;
import BEC.GetWxPayPrepayIdRsp;
import BEC.ReportPayResultRsp;
import BEC.WxPayPrepayInfo;

/**
 * Created by yorkeehuang on 2017/10/19.
 */

public class AppPayModel extends PayModel implements DataSourceProxy.IRequestCallback {

    private static final String TAG = AppPayModel.class.getSimpleName();

    final private AppPayPresenter mPresenter;


    private DataSourceProxy.IRequestCallback mDefaultCallback = new DataSourceProxy.IRequestCallback() {

        @Override
        public void callback(boolean success, EntityObject data) {
        }
    };

    public AppPayModel(AppPayPresenter presenter) {
        mPresenter = presenter;
    }

    public boolean requestAlipaySign(String orderId) {
        return PaymentRequestManager.requestAliPaySign(orderId, this);
    }

    public boolean requestWxpayPrepay(String orderId) {
        return PaymentRequestManager.requestWechatPayPrepayId(orderId, this);
    }

    public boolean requestQueryAlipayResult(Map<String, String> result) {
        return PaymentRequestManager.requestQueryAlipayResult(result, this);
    }

    public boolean requestQueryWxpayResult(Map<String, String> result) {
        return PaymentRequestManager.requestQueryWechatPayResult(result, this);
    }

    public boolean reportAliPayError(Map<String, String> result) {
        return PaymentRequestManager.requestQueryAlipayResult(result, mDefaultCallback);
    }

    public boolean reportWxPayError(Map<String, String> result) {
        return PaymentRequestManager.requestQueryWechatPayResult(result, mDefaultCallback);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_ALI_PAY_SIGN:
                handleAlipayCallback(success, data);
                break;
            case EntityObject.ET_GET_WX_PAY_PREPAY_ID:
                handleWechatPayCallback(success, data);
                break;
            case EntityObject.ET_QUERY_PAY_RESULT:
                handleReportPayResult(success, data);
                break;
            default:
        }
    }

    private void handleAlipayCallback(final boolean success, final EntityObject data) {
        DtLog.d(TAG, "handleAlipayCallback success : " + success);
        if (success && data.getEntity() != null) {
            final GetAliPaySignRsp rsp = (GetAliPaySignRsp) data.getEntity();
            DtLog.d(TAG, "handleAlipayCallback rsp.getIReturnCode() : " + rsp.getIReturnCode());
            if(isOrderError(rsp.getIReturnCode())){
                mPresenter.OnGetAlipaySignError(rsp.getIReturnCode());
            } else {
                final String sign = rsp.sSign;
                DtLog.d(TAG, "handleAlipayCallback sign : " + sign);
                if(!TextUtils.isEmpty(sign)) {
                    mPresenter.OnGetAlipaySignSuccess(sign);
                } else {
                    mPresenter.OnGetAlipaySignError(AppPayCallback.SIGN_EXTRA_ERROR);
                }
            }
        } else {
            mPresenter.OnGetAlipaySignError(AppPayCallback.SIGN_EXTRA_ERROR);
        }
    }

    private void handleWechatPayCallback(final boolean success, final EntityObject data) {
        DtLog.d(TAG, "handleWechatPayCallback success : " + success);
        if (success && data.getEntity() != null) {
            final GetWxPayPrepayIdRsp rsp = (GetWxPayPrepayIdRsp) data.getEntity();
            DtLog.d(TAG, "handleWechatPayCallback rsp.getIReturnCode() : " + rsp.getIReturnCode());
            if(isOrderError(rsp.getIReturnCode())) {
                mPresenter.OnGetAlipaySignError(rsp.getIReturnCode());
            } else {
                final WxPayPrepayInfo prepayInfo = rsp.stPrepay;
                DtLog.d(TAG, "handleWechatPayCallback sign : " + prepayInfo.getSSign());
                if (checkWxPrepayId(prepayInfo)) {
                    mPresenter.OnGetWxpayPrepayIdSuccess(new WxPrepayId(prepayInfo));
                } else {
                    mPresenter.OnGetWxpayPrepayIdError(AppPayCallback.SIGN_EXTRA_ERROR);
                }
            }
        } else {
            mPresenter.OnGetWxpayPrepayIdError(AppPayCallback.SIGN_EXTRA_ERROR);
        }
    }

    private void handleReportPayResult(final boolean success, final EntityObject data) {
        final int payType = getPayType(data);
        if(success && data.getEntity() != null) {
            ReportPayResultRsp rsp = (ReportPayResultRsp) data.getEntity();

            switch (rsp.getIPayStatus()) {
                case E_DT_PAY_STATUS.E_DT_PAY_SUCCESS:
                    mPresenter.OnGetPayResult(payType, AppPayCallback.PAY_SUCCESS);
                    break;
                case E_DT_PAY_STATUS.E_DT_PAY_FAIL:
                    mPresenter.OnGetPayResult(payType, AppPayCallback.PAY_FAIL);
                    break;
                default:
                    mPresenter.OnGetPayResult(payType, AppPayCallback.PAY_UNKNOWN);
                    break;
            }
        } else {
            mPresenter.OnGetPayResult(payType, AppPayCallback.PAY_UNKNOWN);
        }
    }

    private boolean checkWxPrepayId(WxPayPrepayInfo prepayId) {
        return prepayId != null && !TextUtils.isEmpty(prepayId.getSAppId());
    }
}
