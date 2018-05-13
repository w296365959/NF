package com.sscf.investment.web.sdk.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ILocalH5ResourceManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.UrlUtils;
import com.sscf.investment.web.sdk.manager.Scheme;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import java.io.File;

/**
 * Created by xuebinliu on 2015/8/4.
 */
public class DtBaseWebViewClient extends WebViewClient {
    private static final String TAG = DtBaseWebViewClient.class.getSimpleName();

    @Override
    public void onLoadResource(WebView webView, String url) {
        DtLog.d(TAG, "onLoadResource url = " + url);
        super.onLoadResource(webView, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
        DtLog.d(TAG, "shouldInterceptRequest url = " + url);
        final ILocalH5ResourceManager localH5ResourceManager = (ILocalH5ResourceManager) ComponentManager.getInstance().getManager(ILocalH5ResourceManager.class.getName());
        if (localH5ResourceManager != null) {
            final File file = localH5ResourceManager.getWebResourceFile(url);
            DtLog.d(TAG, "shouldInterceptRequest matched file = " + file);

            if (file != null) {
                if (file.exists()) {
                    final String mimeType = UrlUtils.getMimeTypeByFilePath(file.getName());
                    try {
                        return new WebResourceResponse(mimeType, "utf-8", FileUtil.openInputStream(file));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Give the host application a chance to take over the control when a new
     * url is about to be loaded in the current WebView. If WebViewClient is not
     * provided, by default WebView will ask Activity Manager to choose the
     * proper handler for the url. If WebViewClient is provided, return true
     * means the host application handles the url, while return false means the
     * current WebView handles the url.
     * This method is not called for requests using the POST "method".
     *
     * @param view The WebView that is initiating the callback.
     * @param url The url to be loaded.
     * @return True if the host application wants to leave the current WebView
     *         and handle the url itself, otherwise return false.
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        DtLog.d(TAG, "shouldOverrideUrlLoading url=" + url + ", host=" + view.getUrl());
        //http://sec.wedengta.com/InvestAdvise/faqIntelligentAnswer.html?dt_page_type=11&webviewType=userActivitesType&dt_from=app
        final Context context = view.getContext();
//        url = url.replace("http://sec.wedengta.com", "https://sec.gushi.com");
//        url = url.replace("https://sec.wedengta.com", "https://sec.gushi.com");
        final ISchemeManager schemeManager = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
        if (!TextUtils.equals(view.getUrl(), url) && schemeManager != null && schemeManager.handleWebViewUrl(context, url)
                != CommonConst.PROTOCAL_NOT_HANDLE) {
            DtLog.d(TAG, "shouldOverrideUrlLoading scheme handle successful");
            return true;
        }

        final Uri uri = Uri.parse(url);
        final String scheme = uri.getScheme();
        if(TextUtils.isEmpty(scheme)) {
            view.loadUrl(url);
            return false;
        } else if(Scheme.HTTP.equalsIgnoreCase(scheme) || Scheme.HTTPS.equalsIgnoreCase(scheme)) {
            if(Scheme.isDtUrl(url)) {
                view.loadUrl(url);
                return true;
            } else {
                return false;
            }
        } else {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    /**
     * 开始载入页面
     * @param view
     * @param url
     * @param favicon
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        DtLog.d(TAG, "onPageStarted url=" + url);
    }

    /**
     * 页面加载结束
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(final WebView view, String url) {
        super.onPageFinished(view, url);
        DtLog.d(TAG, "onPageFinished url=" + url);
    }

    @Override
    public void onReceivedError(WebView webView, int i, String s, String s1) {
        super.onReceivedError(webView, i, s, s1);
        DtLog.d(TAG, "onReceivedError s" + s + ", s1=" + s1);
    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler handler, SslError error) {
        DtLog.d(TAG, "onReceivedSslError error=" + error);
        handler.proceed();
    }
}
