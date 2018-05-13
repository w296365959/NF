package com.sscf.investment.payment.presenter;

import android.app.Activity;
import android.content.Intent;

import com.dengtacj.component.entity.H5PayResult;
import com.dengtacj.component.entity.payment.OrderInfo;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.payment.PayOrderActivity;
import com.sscf.investment.payment.callback.H5PayCallback;
import com.sscf.investment.payment.model.H5PayModel;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.utils.DengtaConst;

import BEC.E_DT_PAY_STATUS;
import BEC.E_H5_PAY_OPEN_TYPE;

/**
 * Created by yorkeehuang on 2017/10/19.
 */

public class H5PayPresenter extends PayPresenter implements H5PayCallback {

    private static final String TAG = H5PayPresenter.class.getSimpleName();

    private H5PayModel mModel;

    public H5PayPresenter(PayOrderActivity activity, OrderInfo orderInfo) {
        super(activity, orderInfo);
        mModel = new H5PayModel(this);
    }

    public int getThirdPartySource() {
        return mOrderInfo.thirdPaySource;
    }

    @Override
    void payByAli(String orderId) {
        if(mModel.requestAliH5Prepay(orderId)) {
            showLoadingDialog();
        }
    }

    @Override
    void payByWechat(String orderId) {
        if(mModel.requestWxH5Prepay(orderId)) {
            showLoadingDialog();
        }
    }

    public void handleH5PayResult(final int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && data != null) {
            H5PayResult h5PayResult = (H5PayResult) data.getSerializableExtra(DengtaConst.EXTRA_H5_PAY_RESULT);
            if(h5PayResult != null) {
                mOrderInfo.payType = h5PayResult.payType;
                switch (h5PayResult.result) {
                    case E_DT_PAY_STATUS.E_DT_PAY_SUCCESS:
                        showLoadingDialog();
                        mModel.requestOrderResult(mOrderInfo.id, mOrderInfo.payType);
                        break;
                    case E_DT_PAY_STATUS.E_DT_PAY_FAIL:
                        showErrorDialog();
                        break;
                    case E_DT_PAY_STATUS.E_DT_PAY_WATING_PAY:
                        goOrderUnknown();
                        break;
                    default:
                }
            }
        }
    }

    @Override
    public void OnGetH5PayUrlSuccess(int payType, String payUrl) {
        DtLog.d(TAG, "OnGetH5PayUrlSuccess() payType = " + payType + ", payUrl = " + payUrl);
        dismissLoadingDialog();
        switch (mOrderInfo.h5OpenType) {
            case E_H5_PAY_OPEN_TYPE.E_H5_PAY_OPEN_BY_URL:
                WebBeaconJump.showCommonWebActivityForResult(getActivity(), PayOrderActivity.H5_PAY, false, payUrl);
                break;
            case E_H5_PAY_OPEN_TYPE.E_H5_PAY_OPEN_BY_HTML:
                WebBeaconJump.showContentWebActivityForResult(getActivity(), PayOrderActivity.H5_PAY, payUrl);
                break;
            default:
        }
    }

    @Override
    public void OnGetH5PayUrlError(int payType, int error) {
        DtLog.d(TAG, "OnGetH5PayUrlError() payType = " + payType + ", error = " + error);
        dismissLoadingDialog();
        showErrorDialog();
    }

    @Override
    public void OnGetPayResult(int payType, int status) {
        handlePayResult(payType, status);
    }
}
