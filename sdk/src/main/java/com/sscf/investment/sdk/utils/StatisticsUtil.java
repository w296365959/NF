package com.sscf.investment.sdk.utils;

import com.sscf.investment.sdk.SDKManager;
import com.umeng.analytics.MobclickAgent;
import java.util.HashMap;
import java.util.Map;

/**
 * davidwei
 * 统计
 */
public final class StatisticsUtil {
    private static final String TAG = StatisticsUtil.class.getSimpleName();

    // 事件统计
    public static void reportAction(String action) {
        DtLog.d(TAG, "reportAction: action = " + action);
        MobclickAgent.onEvent(SDKManager.getInstance().getContext(), action);
    }

    // 单附加信息统计
    public static void reportAction(String action, String key, String extra) {
        HashMap m = new HashMap<String, String>(1);
        m.put(key, extra);
        DtLog.d(TAG, "reportAction: action = " + action + ", key = "  + key+ " , extra : " + extra);
        MobclickAgent.onEvent(SDKManager.getInstance().getContext(), action, m);
    }

    // 附加信息统计
    public static void reportAction(String action, Map<String,String> m) {
        DtLog.d(TAG, "reportAccount: action = " + action + " , map : " + m);
        MobclickAgent.onEvent(SDKManager.getInstance().getContext(), action, m);
    }

    // 计算统计
    public static void reportAccount(String id, Map<String,String> m,  int count) {
        DtLog.d(TAG, "reportAccount: id = " + id + ", count = " + count + " , map : " + m);
        MobclickAgent.onEventValue(SDKManager.getInstance().getContext(), id, m, count);
    }

    public static void reportError(Throwable t) {
        MobclickAgent.reportError(SDKManager.getInstance().getContext(), t);
    }
}
