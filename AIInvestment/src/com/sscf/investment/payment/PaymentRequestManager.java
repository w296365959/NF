package com.sscf.investment.payment;

import android.text.TextUtils;

import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.socialize.UmengSocialSDKUtils;
import java.util.ArrayList;
import java.util.Map;
import BEC.AccountTicket;
import BEC.CheckUserCouponReq;
import BEC.E_CONFIG_TYPE;
import BEC.E_DT_PAY_TYPE;
import BEC.GetAliPaySignReq;
import BEC.GetConfigReq;
import BEC.GetH5PayUrlReq;
import BEC.GetMemberFeeListReq;
import BEC.GetOrderPayResultReq;
import BEC.GetPayOrderIdReq;
import BEC.GetUserCouponNumReq;
import BEC.GetUserEvalResultReq;
import BEC.GetWxPayPrepayIdReq;
import BEC.ReportPayResultReq;
import BEC.UserInfo;

/**
 * Created by yorkeehuang on 2017/2/8.
 */

public class PaymentRequestManager {

    private static final String TAG = PaymentRequestManager.class.getSimpleName();

    public static void requestMemberFeeList(DataSourceProxy.IRequestCallback callback) {
        GetMemberFeeListReq req = new GetMemberFeeListReq();
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        UserInfo userInfo = accountManager.getUserInfo();
        req.setStUserInfo(userInfo);
        DataEngine.getInstance().request(EntityObject.ET_GET_MEMBER_FEE_LIST, req, callback);
    }

    /**
     * 获取用户优惠券信息
     */
    public static boolean requestCheckUserCoupon(DataSourceProxy.IRequestCallback callback, int number, int subjectType, int unit, String extra) {
        DtLog.d(TAG, "requestCheckUserCoupon()");
        CheckUserCouponReq req = new CheckUserCouponReq();
        req.setINumber(number);
        req.setISubjectType(subjectType);
        req.setINumberUnit(unit);
        req.setSCommExtraJson(extra);
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();

        UserInfo userInfo = accountManager.getUserInfo();
        AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        if(userInfo != null && accountInfoEntity != null && accountInfoEntity.ticket != null) {
            AccountTicket accountTicket = new AccountTicket();
            accountTicket.setVtTicket(accountInfoEntity.ticket);
            req.setStAccountTicket(accountTicket);
            req.setStUserInfo(userInfo);
            req.setINumber(number);
            req.setISubjectType(subjectType);
            return DataEngine.getInstance().request(EntityObject.ET_CHECK_USER_COUPON, req, callback);
        }
        return false;
    }

    public static boolean requestPayOrderId(DataSourceProxy.IRequestCallback callback, int number, int numberUnit, int subjectType, String extra) {
        return requestPayOrderId(callback, number, numberUnit, subjectType, extra, null);
    }

    public static boolean requestPayOrderId(DataSourceProxy.IRequestCallback callback, int number, int numberUnit, int subjectType, String extra, ArrayList<String> couponCodes) {
        DtLog.d(TAG, "requestPayOrderId()");
        GetPayOrderIdReq req = new GetPayOrderIdReq();
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        UserInfo userInfo = accountManager.getUserInfo();
        AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        if(userInfo != null && accountInfoEntity != null && accountInfoEntity.ticket != null) {
            AccountTicket accountTicket = new AccountTicket();
            accountTicket.setVtTicket(accountInfoEntity.ticket);
            req.setStAccountTicket(accountTicket);
            req.setStUserInfo(userInfo);
            req.setINumber(number);
            req.setISubjectType(subjectType);
            req.setVCouponCode(couponCodes);
            req.setINumberUnit(numberUnit);
            req.setSCommExtraJson(extra);
            return DataEngine.getInstance().request(EntityObject.ET_GET_PAY_ORDER_ID, req, callback);
        }
        return false;
    }

    public static boolean requestGetUserCouponNum( DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestGetUserCouponNum()");
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        UserInfo userInfo = accountManager.getUserInfo();
        AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        if(userInfo != null && accountInfoEntity != null && accountInfoEntity.ticket != null) {
            GetUserCouponNumReq req = new GetUserCouponNumReq();
            AccountTicket accountTicket = new AccountTicket();
            accountTicket.setVtTicket(accountInfoEntity.ticket);
            req.setStAccountTicket(accountTicket);
            req.setStUserInfo(userInfo);
            req.setICouponStatus(0);
            return DataEngine.getInstance().request(EntityObject.ET_GET_USER_COUPON_NUM, req, callback);
        }
        return false;
    }

    public static boolean requestEvalResult(final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestEvalResult()");
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        UserInfo userInfo = accountManager.getUserInfo();
        AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        AccountTicket accountTicket = null;
        if(userInfo != null && accountInfoEntity != null && accountInfoEntity.ticket != null) {
            accountTicket = new AccountTicket();
            accountTicket.setVtTicket(accountInfoEntity.ticket);
            GetUserEvalResultReq req = new GetUserEvalResultReq();
            req.setStAccountTicket(accountTicket);
            req.setStUserInfo(userInfo);
            return DataEngine.getInstance().request(EntityObject.ET_GET_USER_EVAL_RESULT, req, callback);
        }
        return false;
    }

    public static boolean requestAliPaySign(final String orderId, DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestAliPaySign()");
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        UserInfo userInfo = accountManager.getUserInfo();
        AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        if (userInfo != null && accountInfoEntity != null && accountInfoEntity.ticket != null) {
            GetAliPaySignReq req = new GetAliPaySignReq();
            AccountTicket accountTicket = new AccountTicket();
            accountTicket.setVtTicket(accountInfoEntity.ticket);
            req.setStAccountTicket(accountTicket);
            req.setStUserInfo(userInfo);
            req.setSDtPayOrderId(orderId);
            req.setSAppId(UmengSocialSDKUtils.ALI_APPID);
            return DataEngine.getInstance().request(EntityObject.ET_GET_ALI_PAY_SIGN, req, callback);
        }
        return false;
    }

    public static boolean requestWechatPayPrepayId(final String orderId, DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestWechatPayPrepayId()");
        final GetWxPayPrepayIdReq req = new GetWxPayPrepayIdReq();
        req.sAppId = UmengSocialSDKUtils.WECHAT_APP_ID;
        req.sDtPayOrderId = orderId;

        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        final AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfoEntity == null ? null : accountInfoEntity.ticket);
        req.setStAccountTicket(accountTicket);
        req.stUserInfo = accountManager.getUserInfo();
        return DataEngine.getInstance().request(EntityObject.ET_GET_WX_PAY_PREPAY_ID, req, callback);
    }

    public static boolean requestAliH5PayUrl(final String orderId, final int thirdPaySource, final DataSourceProxy.IRequestCallback callback) {
        return requestH5PayUrl(orderId, thirdPaySource, E_DT_PAY_TYPE.E_DT_PAY_ALI, callback);
    }

    public static boolean requestWxH5PayUrl(final String orderId, final int thirdPaySource, final DataSourceProxy.IRequestCallback callback) {
        return requestH5PayUrl(orderId, thirdPaySource, E_DT_PAY_TYPE.E_DT_PAY_WX, callback);
    }

    private static boolean requestH5PayUrl(final String orderId, final int thirdPaySource, final int payType, final DataSourceProxy.IRequestCallback callback) {
        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        final AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        if(!TextUtils.isEmpty(orderId) && accountInfoEntity != null) {
            GetH5PayUrlReq req = new GetH5PayUrlReq();
            req.setSDtPayOrderId(orderId);
            AccountTicket accountTicket = new AccountTicket();
            accountTicket.setVtTicket(accountInfoEntity.ticket);
            req.setStAccountTicket(accountTicket);
            req.stUserInfo = accountManager.getUserInfo();
            req.setIPayType(payType);
            req.setIThirdPaySource(thirdPaySource);
            return DataEngine.getInstance().request(EntityObject.ET_GET_H5_PAY_URL, req, callback);
        }
        return false;
    }

    public static boolean requestQueryAlipayResult(final Map<String, String> result, final DataSourceProxy.IRequestCallback callback) {
        return requestQuerypayResult(E_DT_PAY_TYPE.E_DT_PAY_ALI, result, callback);
    }

    public static boolean requestQueryWechatPayResult(final Map<String, String> result, final DataSourceProxy.IRequestCallback callback) {
        return requestQuerypayResult(E_DT_PAY_TYPE.E_DT_PAY_WX, result, callback);
    }

    private static boolean requestQuerypayResult(final int type, final Map<String, String> result, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestQuerypayResult() dtpayUpUrl");
        final ReportPayResultReq req = new ReportPayResultReq();
        req.mPayResult = result;
        req.iPayType = type;

        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        final AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfoEntity == null ? null : accountInfoEntity.ticket);
        req.setStAccountTicket(accountTicket);
        req.stUserInfo = accountManager.getUserInfo();
        return DataEngine.getInstance().request(EntityObject.ET_QUERY_PAY_RESULT, req, callback, type);
    }

    public static boolean requestRefreshOrderResult(final String orderId, final int payType, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestRefreshOrderResult()");
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        UserInfo userInfo = accountManager.getUserInfo();
        AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        if (userInfo != null && accountInfoEntity != null && accountInfoEntity.ticket != null) {
            GetOrderPayResultReq req = new GetOrderPayResultReq();
            req.iPayType = payType;
            AccountTicket accountTicket = new AccountTicket();
            accountTicket.setVtTicket(accountInfoEntity.ticket);
            req.setStAccountTicket(accountTicket);
            req.setStUserInfo(userInfo);
            req.setSInnerOrderId(orderId);
            return DataEngine.getInstance().request(EntityObject.ET_GET_ORDER_RESULT, req, callback, payType);
        }
        return false;
    }

    public static boolean requestUserAgreement(final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "requestUserAgreement()");
        final GetConfigReq req = new GetConfigReq();
        final ArrayList<Integer> types = new ArrayList<Integer>(1);
        types.add(E_CONFIG_TYPE.E_CONFIG_PAY_USER_AGREEMENT);
        req.vType = types;
        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();
        return DataEngine.getInstance().request(EntityObject.ET_PAY_USER_AGREEMENT, req, callback);
    }
}
