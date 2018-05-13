package com.sscf.investment.web.sdk.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import com.sscf.investment.component.ui.widget.CommonViewPager;
import com.sscf.investment.component.ui.widget.TabLayout;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.web.sdk.JsProxy;
import com.sscf.investment.web.sdk.NativeProxy;
import com.sscf.investment.web.sdk.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by liqf on 2016/5/11.
 */
public final class TabbedWebViewContainer extends LinearLayout implements TabLayout.OnTabSelectionListener, ViewPager.OnPageChangeListener {
    private static final String TAG = "TabbedWebViewContainer";
    private CommonViewPager mViewPager;
    private DtWebView[] mWebViews;
    private TabLayout mTabLayout;

    public TabbedWebViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        View.inflate(context, R.layout.web_tabbed_webview_container, this);
        initViews();
    }

    private void initViews() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (CommonViewPager) findViewById(R.id.view_pager);
    }

    public void switchTab(int liveType) {
        if (mWebViews == null) {
            return;
        }
        mTabLayout.switchTab(liveType);
    }

    public void destroy() {
        mViewPager.removeOnPageChangeListener(this);
    }

    public void initWithTitlesAndUrls(int[] titles, String[] urls) {
        mTabLayout.initWithTitles(titles, null);
        mTabLayout.switchTab(0);
        mTabLayout.setOnTabSelectionListener(this);

        final int length = urls.length;
        final DtWebView[] webViews = new DtWebView[length];
        DtWebView webView;
        for (int i = 0; i < length; i++) {
            webView = initWebView(urls[i]);
            if (webView == null) {
                return;
            }
            webViews[i] = webView;
        }

        mWebViews = webViews;
        final WebViewPagerAdapter pagerAdapter = new WebViewPagerAdapter(webViews);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    private DtWebView initWebView(final String url) {
        View root = null;
        try {
            root = View.inflate(getContext(), R.layout.ptr_webview, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (root == null) {
            DtLog.d(TAG, "inflate webview failed");
            return null;
        }

        final PtrFrameLayout ptrFrame = (PtrFrameLayout) root;
        final DtWebView webView = (DtWebView) ptrFrame.findViewById(R.id.webView);
        ptrFrame.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                DtLog.d(TAG, "checkCanDoRefresh: webview scrollY = " + webView.getWebScrollY());
                return webView.getWebScrollY() == 0;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                JsProxy.startLoad(webView);
                ptrFrame.refreshComplete();
            }
        });

        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.init();

        if (Build.VERSION.SDK_INT >= 21) {
            // 5.0及以上系统，指定可以HTTPS跨域访问HTTP
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        final DtBaseWebViewClient webViewClient = new DtBaseWebViewClient();
        webView.setWebViewClient(webViewClient);

        final DtWebChromeClient webChromeClient = new DtWebChromeClient();
        webView.setWebChromeClient(webChromeClient);

        final NativeProxy nativeProxy = new NativeProxy(new Handler(Looper.getMainLooper(), null));
        webView.addJavascriptInterface(nativeProxy, NativeProxy.TAG);

        webView.loadUrl(url);
        return webView;
    }

    @Override
    public void onTabSelected(int index) {
        DtLog.d(TAG, "onTabSelected: index = " + index);
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        DtLog.d(TAG, "onPageSelected: position = " + position);
        mTabLayout.switchTabNoCallback(position);
        final DtWebView webView = mWebViews[position];
        final int length = mWebViews.length;
        for (int i = 0; i < length; i++) {
            if (i == position) {
                JsProxy.onWebViewShow(webView);
            } else {
                JsProxy.onWebViewHide(webView);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}

final class WebViewPagerAdapter extends PagerAdapter {
    private final DtWebView[] mWebViews;

    WebViewPagerAdapter(final DtWebView[] webViews) {
        mWebViews = webViews;
    }

    @Override
    public int getCount() {
        return mWebViews.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View parent = (View) mWebViews[position].getParent();
        container.addView(parent);
        return parent;
    }
}