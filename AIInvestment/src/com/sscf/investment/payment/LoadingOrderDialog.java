package com.sscf.investment.payment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseActivity;
import com.sscf.investment.main.DengtaApplication;
import com.dengtacj.component.entity.payment.CommodityInfo;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.PaymentInfoUtils;
import com.dengtacj.component.router.CommonBeaconJump;

import BEC.CheckUserCouponRsp;
import BEC.GetPayOrderIdRsp;
import BEC.UserRiskEvalResult;

/**
 * Created by yorkeehuang on 2017/5/13.
 */
public class LoadingOrderDialog extends PaymentLoadingDialog implements DialogInterface.OnShowListener, DataSourceProxy.IRequestCallback {

    private CommodityInfo mCommodityInfo;
    private UserRiskEvalResult mRiskEval;
    private int mNeedSign;
    private BaseActivity mActivity;
    private boolean mIsDismiss = false;

    public LoadingOrderDialog(BaseActivity activity) {
        super(activity);
        mActivity = activity;
        setOnShowListener(this);
    }

    public void setCommodityInfo(CommodityInfo commodityInfo) {
        mCommodityInfo = commodityInfo;
    }

    @Override
    public void onShow(DialogInterface dialog) {
        PaymentRequestManager.requestCheckUserCoupon(this, mCommodityInfo.number,
                mCommodityInfo.type, mCommodityInfo.unit, mCommodityInfo.extra);

        DengtaApplication.getApplication().getPaymentInfoManager().requestUserAgreement();
        startAnim();
    }

    private void runOnUiThread(Runnable runnable) {
        BaseActivity activity = mActivity;
        if(activity != null) {
            if(!activity.isDestroy()) {
                activity.runOnUiThread(runnable);
            }
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_CHECK_USER_COUPON:
                handleCheckUserCoupon(success, data);
                break;
            case EntityObject.ET_GET_PAY_ORDER_ID:
                handleGetPayOrderId(success, data);
                break;
            default:
        }
    }

    private boolean shouldConfirm(CheckUserCouponRsp rsp) {
        return PaymentInfoUtils.shouldRiskEval(rsp) || PaymentInfoUtils.hasCoupon(rsp);
    }

    private void handleCheckUserCoupon(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            CheckUserCouponRsp rsp = (CheckUserCouponRsp) data.getEntity();
            mRiskEval = rsp.getStRiskResult();
            mNeedSign = rsp.getINeedSign();
            if(shouldConfirm(rsp)) {
                mCommodityInfo.couponNum = rsp.getICouponNum();
                runOnUiThread(new UITask() {
                    @Override
                    void runTask() {
                        dismiss();
                        showConfirmOrderActivity();
                    }
                });
            } else {
                PaymentRequestManager.requestPayOrderId(this, mCommodityInfo.number, mCommodityInfo.unit, mCommodityInfo.type, mCommodityInfo.extra);
            }
        } else {
            runOnUiThread(new UITask() {
                @Override
                public void runTask() {
                    dismiss();
                    DengtaApplication.getApplication().showToast(R.string.create_pay_order_error_text);
                }
            });
        }
    }

    @Override
    public void dismiss() {
        mIsDismiss = true;
        stopAnim();
        super.dismiss();
    }

    private void showConfirmOrderActivity() {
        Activity activity = getActivity();
        if(activity != null) {
            dismiss();
            Intent intent = new Intent(activity, ConfirmOrderActivity.class);
            intent.putExtra(DengtaConst.EXTRA_COMMODITY_INFO, mCommodityInfo);
            intent.putExtra(DengtaConst.EXTRA_RISK_EVAL, mRiskEval);
            intent.putExtra(DengtaConst.EXTRA_NEED_SIGN, mNeedSign);
            activity.startActivity(intent);
        }
    }

    private void handleGetPayOrderId(boolean success, EntityObject data) {
        UITask task;
        if(success && data.getEntity() != null) {
            GetPayOrderIdRsp payOrderIdRsp = (GetPayOrderIdRsp) data.getEntity();
            task = new UITask() {
                @Override
                public void runTask() {
                    dismiss();
                    pay(payOrderIdRsp);
                }
            };
        } else {
            task = new UITask() {
                @Override
                public void runTask() {
                    dismiss();
                    DengtaApplication.getApplication().showToast(R.string.create_pay_order_error_text);
                }
            };
        }
        runOnUiThread(task);
    }

    private void pay(GetPayOrderIdRsp data) {
        Activity activity = getActivity();
        if(activity != null) {
            CommonBeaconJump.showPayOrder(activity, PaymentInfoUtils.itemToOrderInfo(data.stDtPayItem), mRiskEval, mNeedSign);
            dismiss();
        }
    }

    private BaseActivity getActivity() {
        BaseActivity activity = mActivity;
        if(activity != null) {
            if(!activity.isDestroy()) {
                return activity;
            }
        }
        return null;
    }

    private abstract class UITask implements Runnable {

        abstract void runTask();

        @Override
        public void run() {
            if(mIsDismiss) {
                return;
            }
            runTask();
        }
    }
}
