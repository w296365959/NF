package com.sscf.investment.payment;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.ProtoManager;
import com.sscf.investment.sdk.utils.DtLog;

import java.util.ArrayList;
import java.util.Map;

import BEC.ConfigDetail;
import BEC.E_CONFIG_TYPE;
import BEC.GetConfigRsp;
import BEC.GetUserCouponNumRsp;
import BEC.GetUserEvalResultRsp;
import BEC.PayUserAgreement;
import BEC.PayUserAgreementList;

/**
 * Created by yorkeehuang on 2017/5/15.
 */

public class PaymentInfoManager implements DataSourceProxy.IRequestCallback {

    private static final String TAG = PaymentInfoManager.class.getSimpleName();

    public static final String USER_COUPON_NUM_CHANGED = "user_coupon_num_changed";
    public static final String USER_RISK_EVAL_CHANGED = "user_risk_eval_changed";

    private GetUserCouponNumRsp mGetUserCouponNumRsp;

    private GetUserEvalResultRsp mGetUserEvalResultRsp;

    private Map<Integer, PayUserAgreement> mSubjectUserAgreementMap;

    public void requsetCouponNumber() {
        PaymentRequestManager.requestGetUserCouponNum(this);
    }

    public void requestEvalResult() {
        if(!PaymentRequestManager.requestEvalResult(this)) {
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(new Intent(USER_RISK_EVAL_CHANGED));
        }
    }

    public void requestUserAgreement() {
        if(mSubjectUserAgreementMap == null || mSubjectUserAgreementMap.isEmpty()) {
           PaymentRequestManager.requestUserAgreement(this);
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_USER_COUPON_NUM:
                handleGetUserCouponNum(success, data);
                break;
            case EntityObject.ET_GET_USER_EVAL_RESULT:
                handleGetEvalResult(success, data);
                break;
            case EntityObject.ET_PAY_USER_AGREEMENT:
                handleUserAgreement(success, data);
                break;
            default:
        }
    }

    private void handleGetUserCouponNum(boolean success, EntityObject data) {
        DtLog.d(TAG, "handleGetUserCouponNum() : success = " + success + ", data.getEntity() = " + data.getEntity());
        if(success && data.getEntity() != null) {
            mGetUserCouponNumRsp = (GetUserCouponNumRsp) data.getEntity();
        }
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(new Intent(USER_COUPON_NUM_CHANGED));
    }

    private void handleGetEvalResult(boolean success, EntityObject data) {
        DtLog.d(TAG, "handleGetEvalResult() : success = " + success + ", data.getEntity() = " + data.getEntity());
        if(success && data.getEntity() != null) {
            mGetUserEvalResultRsp = (GetUserEvalResultRsp) data.getEntity();
        }
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(new Intent(USER_RISK_EVAL_CHANGED));
    }

    private void handleUserAgreement(boolean success, EntityObject data) {
        DtLog.d(TAG, "handleUserAgreement() : success = " + success + ", data.getEntity() = " + data.getEntity());
        if(success && data.getEntity() != null) {
            GetConfigRsp rsp = (GetConfigRsp) data.getEntity();
            ArrayList<ConfigDetail> cfgDetails = rsp.getVList();
            for (ConfigDetail config : cfgDetails) {
                if (config.iType == E_CONFIG_TYPE.E_CONFIG_PAY_USER_AGREEMENT) {
                    PayUserAgreementList payUserAgreementList = new PayUserAgreementList();
                    if (ProtoManager.decode(payUserAgreementList, config.vData)) {
                        if(payUserAgreementList != null && payUserAgreementList.getMSubjectUserAgreement() != null && !payUserAgreementList.getMSubjectUserAgreement().isEmpty()) {
                            mSubjectUserAgreementMap = payUserAgreementList.getMSubjectUserAgreement();
                        }
                    }
                    break;
                }
            }
        }
    }

    public PayUserAgreement getPayUserAgreement(int type) {
        if(mSubjectUserAgreementMap != null && !mSubjectUserAgreementMap.isEmpty()) {
            return mSubjectUserAgreementMap.get(type);
        }
        return null;
    }

    public int getUserCouponNum() {
        boolean isLogined = DengtaApplication.getApplication().getAccountManager().isLogined();
        if(isLogined && mGetUserCouponNumRsp != null) {
            return mGetUserCouponNumRsp.getICouponNum();
        }
        return -1;
    }

    public String getUserEvalResult() {
        boolean isLogined = DengtaApplication.getApplication().getAccountManager().isLogined();
        if(isLogined && mGetUserEvalResultRsp != null) {
            return mGetUserEvalResultRsp.getSRiskType();
        }
        return "未完成";
    }
}
