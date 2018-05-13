package com.sscf.investment.web.sdk;

import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.web.sdk.widget.DtWebView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xuebinliu on 2015/9/7.
 *
 * 本地代码调用JS代码代理
 */
public final class JsProxy {
    private static final String TAG = JsProxy.class.getSimpleName();

    /**
     * 调小页面字体
     * @param webView
     */
    public static void adjustFontSizeSmall(DtWebView webView) {
        if (webView == null) {
            return;
        }
        webView.loadUrl("javascript:ReqWeb('adjustFontSize', {size:'small'})");
    }

    /**
     * 调大页面字体
     * @param webView
     */
    public static void adjustFontSizeBig(DtWebView webView) {
        if (webView == null) {
            return;
        }
        DtLog.e(TAG, "adjustFontSizeBig");
        webView.loadUrl("javascript:ReqWeb('adjustFontSize', {size:'big'})");
    }

    /**
     * 调大页面字体
     * @param webView
     */
    public static void updateDownloadProgress(final DtWebView webView, final String url, final float progress) {
        if (webView == null) {
            return;
        }
        DtLog.d(TAG, "updateDownloadProgress: progress = " + progress + ", url = " + url);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("url", url);
            jsonObject.put("progress", progress);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webView.loadUrl("javascript:ReqWeb('updateDownloadProgress'," + jsonObject.toString() + ")");
    }

    /**
     * 调大页面字体
     * @param webView
     */
    public static void downloadComplete(final DtWebView webView, final String url, final boolean success) {
        if (webView == null) {
            return;
        }

        DtLog.d(TAG, "downloadComplete: success = " + success + ", url = " + url);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("url", url);
            jsonObject.put("success", success);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webView.loadUrl("javascript:ReqWeb('downloadComplete'," + jsonObject.toString() + ")");
    }

    /**
     * 点击左上角的后退按钮
     * @param webView
     */
    public static void clickBackButton(final DtWebView webView) {
        if (webView == null) {
            return;
        }

        webView.loadUrl("javascript:ReqWeb('historyBack', {})");
    }

    /**
     * 点击右上角的分享按钮
     * @param webView
     */
    public static void clickShareButton(final DtWebView webView) {
        if (webView == null) {
            return;
        }

        webView.loadUrl("javascript:ReqWeb('share', {})");
    }

    /**
     * 点击右上角的textButton
     * @param webView
     */
    public static void clickRightButton(final DtWebView webView) {
        if (webView == null) {
            return;
        }

        webView.loadUrl("javascript:ReqWeb('clickRightButton', {})");
    }

    /**
     * 点击右上角的刷新按钮
     * @param webView
     */
    public static void clickRefreshButton(final DtWebView webView) {
        if (webView == null) {
            return;
        }

        webView.loadUrl("javascript:ReqWeb('clickRefreshButton', {})");
    }

    /**
     * 点击右上角的faq按钮
     * @param webView
     */
    public static void clickFaqButton(final DtWebView webView) {
        if (webView == null) {
            return;
        }

        webView.loadUrl("javascript:ReqWeb('clickFaqButton', {})");
    }

    /**
     * 登录成功回调给js
     * @param webView
     */
    public static void loginSuccess(final DtWebView webView) {
        if (webView == null) {
            return;
        }
        webView.loadUrl("javascript:ReqWeb('loginSuccess', {})");
    }

    /**
     * 分享成功失败的状态回调给js
     * @param webView
     */
    public static void shareState(final DtWebView webView, int state, String plat) {
        if (webView == null) {
            return;
        }

        final JSONObject params = new JSONObject();
        try {
            params.put("state", state);
            params.put("plat", plat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webView.loadUrl(String.format("javascript:ReqWeb('shareInfo', %s)", params));
    }

    /**
     * @param webView
     */
    public static void onWebViewShow(final DtWebView webView) {
        if (webView == null) {
            return;
        }

        webView.loadUrl("javascript:ReqWeb('webviewShow', {})");
    }

    /**
     * @param webView
     */
    public static void onWebViewHide(final DtWebView webView) {
        if (webView == null) {
            return;
        }

        webView.loadUrl("javascript:ReqWeb('webviewHide', {})");
    }

    /**
     * @param webView
     */
    public static void startLoad(final DtWebView webView) {
        if (webView == null) {
            return;
        }

        webView.loadUrl("javascript:ReqWeb('startLoad', {})");
    }

    /**
     * @param webView
     */
    public static void entryWords(final DtWebView webView, final String words) {
        if (webView == null) {
            return;
        }

        webView.loadUrl(String.format("javascript:ReqWeb('entryWords',{words:'%s'})", words));
    }

    /**
     * @param webView
     */
    public static void entryWords(final DtWebView webView, final String secName, final String dtCode) {
        if (webView == null) {
            return;
        }
        webView.loadUrl(String.format("javascript:ReqWeb('entryWords',{words:'%s', realWords:'%s'})", secName, dtCode));
    }

    public static void switchTab(final DtWebView webView, final int index) {
        if (webView == null) {
            return;
        }

        webView.loadUrl(String.format("javascript:ReqWeb('naviTabSwitchAction',{tabIndex:%s})", index));
    }

    /**
     * @param status 1.没有此地址，2.传参错误
     */
    public static void beaconOpenFail(final DtWebView webView, final int status) {
        if (webView == null) {
            return;
        }

        webView.loadUrl(String.format("javascript:ReqWeb('beaconOpenFail',{status:%s})", status));
    }

    /**
     * 点击按钮
     * @param type
     */
    public static void clickButton(final DtWebView webView, final String type) {
        if (webView == null) {
            return;
        }

        webView.loadUrl(String.format("javascript:ReqWeb('clickBtn',{type:'%s'})", type));
    }

    public static void clickPopupButton(final DtWebView webView, final String type, final String id) {
        if (webView == null) {
            return;
        }

        webView.loadUrl(String.format("javascript:ReqWeb('clickBtn',{type:'%s', id:'%s'})", type, id));
    }

    public static void clickButton(final DtWebView webView, final String type, final String input) {
        if (webView == null) {
            return;
        }

        webView.loadUrl(String.format("javascript:ReqWeb('clickBtn',{type:'%s', input:'%s'})", type, input));
    }

    public static void removeAd(final DtWebView webView) {
        if (webView == null) {
            return;
        }

        webView.loadUrl(String.format("javascript:$('#download').attr('style','display:none');$('#bg').attr('style','display:none');"));
        webView.loadUrl(String.format("javascript:var script = document.createElement(\"script\"); script.type = \"text/javascript\"; script.src = \"//idtcdn.oss-cn-hangzhou.aliyuncs.com/beacon/lib/removeAds.js\"; document.body.appendChild(script);"));
    }

    public static void onPostCommentComplete(final DtWebView webView, final boolean success, final String feedId) {
        if (webView == null) {
            return;
        }

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("success", success);
            jsonObject.put("feedId", feedId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webView.loadUrl("javascript:ReqWeb('onPostCommentComplete'," + jsonObject.toString() + ")");
    }

    public static void onDeleteComment(final DtWebView webView, final String feedId) {
        if (webView == null) {
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("feedId", feedId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webView.loadUrl("javascript:ReqWeb('onDeleteComment'," + jsonObject.toString() + ")");
    }

    /**
     * @param webView
     */
    public static void  onGetLocation(final DtWebView webView, final double longitude, final double  latitude) {
        if (webView == null) {
            return;
        }

        final JSONObject jsonObject = new JSONObject();
        if (longitude != Double.MAX_VALUE && latitude != Double.MAX_VALUE) {
            try {
                jsonObject.put("longitude", longitude);
                jsonObject.put("latitude", latitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        webView.loadUrl("javascript:ReqWeb('onGetLocation'," + jsonObject.toString() + ")");
    }
}
