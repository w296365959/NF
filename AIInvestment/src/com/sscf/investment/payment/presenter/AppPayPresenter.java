package com.sscf.investment.payment.presenter;

import com.dengtacj.component.entity.payment.OrderInfo;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.pay.PayManager;
import com.sscf.investment.payment.PayOrderActivity;
import com.sscf.investment.payment.callback.AppPayCallback;
import com.sscf.investment.payment.entity.WxPrepayId;
import com.sscf.investment.payment.model.AppPayModel;
import com.sscf.investment.sdk.utils.DtLog;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.Map;

/**
 * Created by yorkeehuang on 2017/10/18.
 */

public class AppPayPresenter extends PayPresenter implements PayManager.PayCallback, AppPayCallback {

    private static final String TAG = AppPayPresenter.class.getSimpleName();

    private final AppPayModel mModel;

    public AppPayPresenter(PayOrderActivity activity, OrderInfo orderInfo) {
        super(activity, orderInfo);
        mModel = new AppPayModel(this);
    }

    @Override
    void payByAli(String orderId) {
        if(mModel.requestAlipaySign(orderId)) {
            showLoadingDialog();
        }
    }

    @Override
    void payByWechat(String orderId) {
        if(mModel.requestWxpayPrepay(orderId)) {
            showLoadingDialog();
        }
    }

    @Override
    public void OnGetAlipaySignSuccess(String sign) {
        DtLog.d(TAG, "OnGetAlipaySignSuccess() sign = " + sign);
        DengtaApplication.getApplication().defaultExecutor.execute(()
                -> PayManager.payByAlipay(sign, getActivity(), this));
    }

    @Override
    public void OnGetAlipaySignError(int error) {
        DtLog.d(TAG, "OnGetAlipaySignError() error = " + error);
        dismissLoadingDialog();
        if(isOrderOverTime(error)) {
            showOverTimeDialog();
        } else {
            showErrorDialog();
        }
    }

    @Override
    public void OnGetWxpayPrepayIdSuccess(WxPrepayId prepayId) {
        DtLog.d(TAG, "OnGetWxpayPrepayIdSuccess() prepayId = " + prepayId.prepayId);
        final PayReq req = new PayReq();
        req.appId = prepayId.appId;
        req.partnerId = prepayId.partnerId;
        req.prepayId = prepayId.prepayId;
        req.nonceStr = prepayId.nonceStr;
        req.timeStamp = prepayId.timeStamp;
        req.packageValue = prepayId.packageValue;
        req.sign = prepayId.sign;
        PayManager.payByWechat(getActivity(), req, this);
    }

    @Override
    public void OnGetWxpayPrepayIdError(int error) {
        DtLog.d(TAG, "OnGetWxpayPrepayIdError() error = " + error);
        dismissLoadingDialog();
        if(isOrderOverTime(error)) {
            showOverTimeDialog();
        } else {
            showErrorDialog();
        }
    }

    @Override
    public void OnGetPayResult(int payType, int status) {
        DtLog.d(TAG, "OnGetPayResult() payType = " + payType + ", status = " + status);
        handlePayResult(payType, status);
    }

    // ================SDK支付回调接口 开始======================
    @Override
    public void onAlipaySuccess(Map<String, String> result) {
        DtLog.d(TAG, "onAlipaySuccess result : " + result);
        queryAliOrderResult(result);
    }

    @Override
    public void onAlipayError(Map<String, String> result) {
        DtLog.d(TAG, "onAlipayError result : " + result);
        reportAliPayError(result);
    }

    @Override
    public void onAlipayUnknown(Map<String, String> result) {
        DtLog.d(TAG, "onAlipayUnknown result : " + result);
        queryAliOrderResult(result);
    }

    @Override
    public void onAlipayCancel() {
        DtLog.d(TAG, "onAlipayCancel");
    }

    @Override
    public void onWechatPaySuccess(Map<String, String> result) {
        queryWxOrderResult(result);
    }

    @Override
    public void onWechatPayError(Map<String, String> result) {
        DtLog.d(TAG, "onWechatPayError result : " + result);
        reportWxPayError(result);
        showErrorDialog();
    }

    @Override
    public void onWechatPayCancel(Map<String, String> result) {
        DtLog.d(TAG, "onWechatPayCancel result : " + result);
        reportWxPayError(result);
    }
    // ================SDK支付回调接口 结束======================

    private void queryAliOrderResult(Map<String, String> result) {
        DtLog.d(TAG, "onAlipaySuccess result : " + result);
        if(result != null) {
            result.put("orderId", mOrderInfo.id);
            if(mModel.requestQueryAlipayResult(result)) {
                showLoadingDialog();
            }
        } else {
            goOrderUnknown();
        }
    }

    private void reportAliPayError(Map<String, String> result) {
        DtLog.d(TAG, "reportAliPayError result : " + result);
        if(result != null) {
            mModel.reportAliPayError(result);
        }
        showErrorDialog();
    }

    private void reportWxPayError(Map<String, String> result) {
        DtLog.d(TAG, "reportWxPayError result : " + result);
        if (result != null) {
            result.put("orderId", mOrderInfo.id);
            mModel.reportWxPayError(result);
        }
    }

    private void queryWxOrderResult(Map<String, String> result) {
        DtLog.d(TAG, "requestWxOrderResult result : " + result);
        if(result != null) {
            result.put("orderId", mOrderInfo.id);
            if(mModel.requestQueryWxpayResult(result)) {
                showLoadingDialog();
            }
        } else {
            goOrderUnknown();
        }
    }
}
