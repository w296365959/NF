package com.sscf.investment.pay;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.sscf.investment.socialize.UmengSocialSDKUtils;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by davidwei on 2017/02/07
 */
public final class PayManager {

	private static final String TAG = PayManager.class.getSimpleName();

    private static final String ALI_SUCCESS_RESULT = "9000";    // 订单支付成功
    private static final String ALI_PAYING_RESULT = "8000";     // 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
    private static final String ALI_UNKNOWN_RESULT = "6004";     // 支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
    private static final String ALI_CANCEL_RESULT = "6001";     // 用户中途取消

    private static PayCallback sPayCallback;

    private static IWXAPI getWXAPI(final Context context) {
        final String APP_ID = UmengSocialSDKUtils.WECHAT_APP_ID;
        final IWXAPI api = WXAPIFactory.createWXAPI(context.getApplicationContext(), APP_ID);
        api.registerApp(APP_ID);
        return api;
    }

	public static void payByWechat(final Context context, final PayReq req, final PayCallback callback) {
        sPayCallback = callback;
		final IWXAPI api = getWXAPI(context);
		try{
            api.sendReq(req);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

    public static boolean isWechatInstalled(final Context context) {
        return getWXAPI(context).isWXAppInstalled();
    }

    public static boolean isWechatPaySupported(final Context context) {
        return getWXAPI(context).getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

	public static void setAliSanboxEnable(boolean enable) {
		if(enable) {
			EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
		}
	}

    /**
     * 非ui线程调用
     * @param sign
     * @param activity
     * @param callback
     */
	public static void payByAlipay(final String sign, final Activity activity, final PayCallback callback) {
        Log.d(TAG, "payByAlipay() sign = " + sign);
        final PayTask alipay = new PayTask(activity);
        Map<String, String> result = alipay.payV2(sign, true);
        Log.i(TAG, "payByAlipay : " + result);
        if (callback != null) {
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            if (result == null) { // 避免NullPointException
                result = Collections.EMPTY_MAP;
            }
            final String resultStatus = result.get("resultStatus");
            final String resultStr = result.get("result");
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, ALI_SUCCESS_RESULT)) {
                callback.onAlipaySuccess(TextUtils.isEmpty(resultStr) ? null : result);
            } else if(TextUtils.equals(resultStatus, ALI_PAYING_RESULT)
                    || TextUtils.equals(resultStatus, ALI_UNKNOWN_RESULT)){
                callback.onAlipayUnknown(TextUtils.isEmpty(resultStr) ? null : result);
            } else if (TextUtils.equals(resultStatus, ALI_CANCEL_RESULT)) {
                callback.onAlipayCancel();
            } else {
                callback.onAlipayError(TextUtils.isEmpty(resultStr) ? null : result);
            }
        }
    }

    public static void handleWechatPayCallback(final BaseResp resp) {
        final PayCallback callback = sPayCallback;
        if (callback != null) {
            sPayCallback = null;
            final Map<String, String> result = baseRespToMap(resp);
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    callback.onWechatPaySuccess(result);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    callback.onWechatPayCancel(result);
                    break;
                default:
                    callback.onWechatPayError(result);
                    break;
            }
        }
    }

    private static Map<String, String> baseRespToMap(final BaseResp resp) {
        final Map<String, String> result = new HashMap<>(3);
        result.put("errCode", String.valueOf(resp.errCode));
        if (!TextUtils.isEmpty(resp.errStr)) {
            result.put("errStr", resp.errStr);
        }
        return result;
    }

	public interface PayCallback {
		void onAlipaySuccess(final Map<String, String> result);
        void onAlipayError(final Map<String, String> result);
        void onAlipayUnknown(final Map<String, String> result);
        void onAlipayCancel();
        void onWechatPaySuccess(final Map<String, String> result);
        void onWechatPayError(final Map<String, String> result);
        void onWechatPayCancel(final Map<String, String> result);
	}
}
