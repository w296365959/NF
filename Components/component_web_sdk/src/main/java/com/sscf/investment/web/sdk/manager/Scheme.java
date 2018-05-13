package com.sscf.investment.web.sdk.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.dengtacj.component.managers.ISchemeManager;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NumberUtil;
import com.sscf.investment.web.CommonWebConst;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.web.sdk.BeaconProtocal;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Set;

/**
 * Created by xuebinliu on 2015/8/12.
 *
 * <a href="beacon://stock?id=0101600000&name=浦发银行">浦发银行</a>
 */
public final class Scheme implements ISchemeManager {
    public static final String TAG = "Scheme";

    public static final String[] DOMAIN_LIST = new String[] {"news.wedengta.com",
    "sec.wedengta.com", "news.dengtacjw.cn", "sec.dengtacjw.cn", "node.dengtacj.cn", "59.172.4.154", "privi.wedengta.com"};

    // 自有scheme，用于从h5页面跳转native界面
    public static final String BEACON_SCHEME = "beacon://";

    public static final String HTTP = "http";
    public static final String HTTPS = "https";

    // 页面类型，用于页面内打开URL连接时检查打开页面的类型
    public static final String TAG_DT_PAGE_TYPE= "dt_page_type";

    // 页面滑动返回的手势类型
    public static final String TAG_SWIPE_BACK_TYPE = "dt_sbt";

    //通过url参数传进来的title，适用于展示第三方页面
    public static final String TAG_WEB_TITLE = "dt_title";

    /**
     * url后面加个参数告诉前端页面是从app里面打开的还是分享出去通过别的方式打开的
     * 参数设置为from=web  from=app
     * @param url
     * @return
     */
    public static String getSharedUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        return addUrlParam(url, "dt_from", "web");
    }

    public static boolean isDtUrl(String url) {
        try {
            String host = Uri.parse(url).getHost();
            DtLog.d(TAG, "isDtUrl() host = " + host);
            for(String domain : DOMAIN_LIST) {
                if(TextUtils.equals(domain, host)) {
                    DtLog.d(TAG, "isDtUrl() return true");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DtLog.d(TAG, "isDtUrl() return false");
        return false;
    }

    /**
     * 为URL添加参数
     * 如果没有？则添加？，如果key存在，则覆盖key的value
     * @param orgUrl
     * @param key
     * @param value
     * @return
     */
    public static String addUrlParam(String orgUrl, String key, String value) {
        if (!orgUrl.contains("?")) {
            return orgUrl + "?" + key + "=" + value;
        }

        String url;
        int index = orgUrl.indexOf("?");
        url = orgUrl.substring(0, index + 1);

        Uri uri = Uri.parse(orgUrl);
        Set<String> parameterNames = uri.getQueryParameterNames();
        int i = 0;
        int size = parameterNames.size();
        for (String parameterName : parameterNames) {
            if (parameterName.equalsIgnoreCase(key)) {
                url = url + parameterName + "=" + value;
            } else {
                try {
                    url = url + parameterName + "=" + URLEncoder.encode(uri.getQueryParameter(parameterName), CommonConst.CHARTSET_UTF8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return orgUrl;
                }
            }
            if (i < size - 1) {
                url += "&";
            }
            i++;
        }
        DtLog.d(TAG, "addUrlParam url=" + url);
        return url;
    }

    /**
     * 处理WebView里的URL
     * 处理特定的页面，如FAQ
     * 处理需要打开native详情页的scheme
     * @param url
     * @return 返回true，处理了url；fasle 没有处理
     */
    @Override
    public int handleWebViewUrl(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return CommonConst.PROTOCAL_ERROR_URL_BLANK;
        }

        if (url.startsWith(BEACON_SCHEME)) {
            return handleBeaconScheme(context, url);
        }

        if (handleDtPageType(context, url) == CommonConst.PROTOCAL_HANDLE_SUCCESS) {
            return CommonConst.PROTOCAL_HANDLE_SUCCESS;
        }

        return CommonConst.PROTOCAL_NOT_HANDLE;
    }

    /**
     * 处理native里的URL
     * @param url
     * @return 返回true，处理了url；fasle 没有处理
     */
    @Override
    public int handleUrl(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return CommonConst.PROTOCAL_ERROR_URL_BLANK;
        }

        if (url.startsWith(BEACON_SCHEME)) {
            return handleBeaconScheme(context, url);
        }

        if (handleDtPageType(context, url) == CommonConst.PROTOCAL_HANDLE_SUCCESS) {
            return CommonConst.PROTOCAL_HANDLE_SUCCESS;
        }

        WebBeaconJump.showCommonWebActivity(context, url);
        return CommonConst.PROTOCAL_HANDLE_SUCCESS;
    }

    /**
     * 处理自定义带有dt_page_type的地址
     */
    public static int handleDtPageType(Context context, String url) {
        if (!url.contains(TAG_DT_PAGE_TYPE)) {
            return CommonConst.PROTOCAL_NOT_HANDLE;
        }

        final Uri uri = Uri.parse(url);
        final String dtPageType = uri.getQueryParameter(TAG_DT_PAGE_TYPE);
        final int type = NumberUtil.parseInt(dtPageType, -1);

        if (type == -1) {
            return CommonConst.PROTOCAL_NOT_HANDLE;
        }

        switch (type) {
            case CommonWebConst.WT_COMMON_WEB:
                WebBeaconJump.showCommonWebActivity(context, url);
                break;
            case CommonWebConst.WT_FULL_SCREEN_WEB:
                WebBeaconJump.showFullScreenWebActivity(context, url);
                break;
            default:
                WebBeaconJump.showWebActivity(context, url, type);
                break;
        }

        return CommonConst.PROTOCAL_HANDLE_SUCCESS;
    }

    /**
     * 处理beacon协议
     */
    public int handleBeaconScheme(Context context, String url) {
        final Uri uri = Uri.parse(url);
        return handleBeaconScheme(context, uri);
    }

    /**
     * 处理beacon协议
     */
    @Override
    public int handleBeaconScheme(Context context, final Uri uri) {
        if (uri == null) {
            return CommonConst.PROTOCAL_ERROR_URL_BLANK;
        }

        final String host = uri.getHost();
        if (!TextUtils.isEmpty(host)) {
            try {
                final Method method = BeaconProtocal.class.getMethod(host, Context.class, Uri.class);
                method.invoke(null, context, uri);
                return CommonConst.PROTOCAL_HANDLE_SUCCESS;
            } catch (Exception e) {
                LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(new Intent(CommonConst.ACTION_BEACON_PROTOCAL_ERROR));
                e.printStackTrace();
            }
        } else {
            LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(new Intent(CommonConst.ACTION_BEACON_PROTOCAL_ERROR));
        }
        return CommonConst.PROTOCAL_ERROR_BEACON_NOT_SUPPORT;
    }

    @Override
    public String[] getDomainList() {
        return DOMAIN_LIST;
    }
}
