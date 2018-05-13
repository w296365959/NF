package com.sscf.investment.payment.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.dengtacj.component.entity.payment.OrderInfo;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.pay.PayManager;
import com.sscf.investment.payment.PayOrderActivity;
import com.sscf.investment.payment.callback.PayResultCallback;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.TimeUtils;

import BEC.E_DT_PAY_ERROR_CODE;
import BEC.E_DT_PAY_TYPE;

/**
 * Created by yorkeehuang on 2017/10/18.
 */

public abstract class PayPresenter {

    private static final String TAG = PayPresenter.class.getSimpleName();

    protected final OrderInfo mOrderInfo;
    private final PayOrderActivity mActivity;

    public PayPresenter(PayOrderActivity activity, OrderInfo orderInfo) {
        mActivity = activity;
        mOrderInfo = orderInfo;
    }

    protected final Activity getActivity() {
        return mActivity;
    }

    public boolean pay(int payType) {
        if (TimeUtils.isFrequentOperation()) {
            return false;
        }

        if(!NetUtil.isNetWorkConnected(mActivity)) {
            DengtaApplication.getApplication().showToast(R.string.network_error);
            return false;
        }

        if (!DengtaApplication.getApplication().getAccountManager().isLogined()) {
            CommonBeaconJump.showLogin(mActivity);
            return false;
        }

        final String orderId = mOrderInfo.id;
        if(TextUtils.isEmpty(orderId)) {
            DengtaApplication.getApplication().showToast(R.string.order_invalid);
            mActivity.finish();
            return false;
        }

        switch (payType) {
            case E_DT_PAY_TYPE.E_DT_PAY_ALI: // 支付宝
                payByAli(orderId);
                return true;
            case E_DT_PAY_TYPE.E_DT_PAY_WX: // 微信
                if (!PayManager.isWechatInstalled(getActivity())) {
                    DengtaApplication.getApplication().showToast(R.string.wechat_pay_not_installed);
                } else if (!PayManager.isWechatPaySupported(getActivity())) {
                    DengtaApplication.getApplication().showToast(R.string.wechat_pay_not_supported);
                } else {
                    payByWechat(orderId);
                }
                return true;
            default:
                return false;
        }
    }

    abstract void payByAli(String orderId);

    abstract void payByWechat(String orderId);

    private boolean checkActivity() {
        return mActivity != null && !mActivity.isDestroy();
    }

    protected void goOrderFinish() {
        if(checkActivity()) {
            mActivity.goOrderFinish();
        }
    }

    protected void goOrderUnknown() {
        if(checkActivity()) {
            mActivity.goOrderUnknown();
        }
    }

    public void showLoadingDialog() {
        if(checkActivity()) {
            mActivity.showLoadingDialog();
        }
    }

    public void dismissLoadingDialog() {
        if(checkActivity()) {
            mActivity.dismissLoadingDialog();
        }
    }

    protected void showErrorDialog() {
        if(checkActivity()) {
            mActivity.showErrorDialog();
        }
    }

    protected void showOverTimeDialog() {
        if(checkActivity()) {
            mActivity.showOverTimeDialog();
        }
    }

    protected boolean isOrderOverTime(final int returnCode) {
        return returnCode == E_DT_PAY_ERROR_CODE.E_DT_PAY_INNER_ORDER_TIMEOUT;
    }

    public void handlePayResult(int payType, int status) {
        DtLog.d(TAG, "handlePayResult() payType = " + payType + ", status = " + status);
        dismissLoadingDialog();
        mOrderInfo.payType = payType;
        switch (status) {
            case PayResultCallback.PAY_UNKNOWN:
                goOrderUnknown();
                break;
            case PayResultCallback.PAY_SUCCESS:
                goOrderFinish();
                break;
            case PayResultCallback.PAY_FAIL:
                showErrorDialog();
                break;
            default:
        }
    }
}
