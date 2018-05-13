package com.sscf.investment.web.sdk.widget;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IAccountManager;
import com.sscf.investment.web.sdk.WebConst;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by davidwei on 2015/9/7
 */
public final class DtWebView extends WebView {
    private VelocityTracker mTracker = null;
    private int mMinimumVelocity;
    private OnScrollListener mListener;
    private OnTouchDownListener mOnTouchDownListener;

    public DtWebView(Context context) {
        super(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity() * 3;
    }

    public DtWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity() * 3;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mOnTouchDownListener != null) {
                mOnTouchDownListener.onTouchDown();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void init() {
        //mWebView.addJavascriptInterface(mNativeProxy, NativeProxy.TAG);
        //Android4.4之前的系统会自动注入以下对象，有漏洞，需要remove掉
        try {
            removeJavascriptInterface("searchBoxJavaBridge_");
            removeJavascriptInterface("accessibility");
            removeJavascriptInterface("accessibilityTraversal");
        } catch (Exception e) { // 2.3以下机型，可能会NullPointerException
            e.printStackTrace();
        }

        final WebSettings settings = getSettings();

        //启用支持javascript
        settings.setJavaScriptEnabled(true);

        // 将图片调整到适合webview的大小
        settings.setUseWideViewPort(true);

        // 页面缩放至屏幕的大小
        settings.setLoadWithOverviewMode(true);

        // web storage支持
        settings.setDomStorageEnabled(true);
        settings.setAppCachePath(getContext().getCacheDir().getAbsolutePath());
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            // 5.0及以上系统，指定可以HTTPS跨域访问HTTP
            settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // 这一行会引起首次启动webview时卡住几秒
//        setOnLongClickListener(mLongClickListener);
    }

    @Override
    public void destroy() {
        try {
            super.destroy();
        } catch (Throwable e) {
            // java.lang.Throwable: Error: WebView.destroy() called while still attached!
            e.printStackTrace();
        }
        getSettings().setJavaScriptEnabled(false);
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mListener = l;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mListener == null) {
            return super.onInterceptTouchEvent(event);
        }
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                if(mTracker == null){
                    mTracker = VelocityTracker.obtain();
                }else{
                    mTracker.clear();
                }
                mTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mTracker.addMovement(event);
                mTracker.computeCurrentVelocity(1000);
                final int yVelocity = (int) mTracker.getYVelocity();

                if (yVelocity > mMinimumVelocity * 5) {
                    if (mListener != null) {
                        mListener.onScrollDown();
                    }
                } else if (yVelocity < -mMinimumVelocity) {
                    if (mListener != null) {
                        mListener.onScrollUp();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mTracker != null) {
                    mTracker.recycle();
                    mTracker = null;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    public interface OnScrollListener {
        void onScrollUp();
        void onScrollDown();
    }

    public void setOnTouchDownListener(OnTouchDownListener l) {
        this.mOnTouchDownListener = l;
    }

    public interface OnTouchDownListener {
        void onTouchDown();
    }

    @Override
    public void loadUrl(String s) {
        loadUrl(s, null);
    }

    @Override
    public void loadUrl(String s, Map<String, String> map) {
        if (!TextUtils.isEmpty(s) && !s.toLowerCase().startsWith("javascript:")) { // 排除javascript代码
            final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                    .getManager(IAccountManager.class.getName());
            if (accountManager != null) {
                if (map == null) {
                    map = new HashMap<String, String>(2);
                }

                map.put(WebConst.ACCOUNT_DT_GUID, accountManager.getGuidString());
                map.put(WebConst.ACCOUNT_DT_DUA, accountManager.getDUA());
            }
        }

        try {
            super.loadUrl(s, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}