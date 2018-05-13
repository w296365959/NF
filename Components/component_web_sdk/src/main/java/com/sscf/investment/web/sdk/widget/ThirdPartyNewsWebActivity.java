package com.sscf.investment.web.sdk.widget;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IFavorManager;
import com.dengtacj.component.managers.IShareManager;
import com.sscf.investment.component.ui.widget.BaseActivity;
import com.sscf.investment.component.ui.widget.CommonToast;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.dengtacj.component.entity.db.FavorItem;
import com.dengtacj.component.entity.ShareParams;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.web.CommonWebConst;
import com.sscf.investment.web.sdk.JsProxy;
import com.sscf.investment.web.sdk.R;
import com.sscf.investment.web.sdk.manager.Scheme;
import com.sscf.investment.web.sdk.WebConst;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by davidwei on 2017/08/10
 * 打开第三方新闻的activity
 */
@Route("ThirdPartyNewsWebActivity")
public final class ThirdPartyNewsWebActivity extends BaseActivity implements View.OnClickListener, DtWebView.OnScrollListener {
    private static final String TAG = ThirdPartyNewsWebActivity.class.getSimpleName();
    private TextView mTitleView;
    private DtWebView mWebView;
    private View mBottomBar;
    private View mFavorButton;
    private View mLoadingView;
    private View mRetryView;
    private FavorItem mFavorItem;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_web_third_party_news);
        } catch (Exception e) {
            e.printStackTrace(); // 处理webview的java.lang.NullPointerException
            finish();
            return;
        }

        initViews();
        final Intent intent = getIntent();

        final String url = intent.getStringExtra(CommonWebConst.URL_ADDR);
        mWebView.loadUrl(url);
        final Uri uri = Uri.parse(url);
        if (uri.isHierarchical()) {
            final String title = uri.getQueryParameter(Scheme.TAG_WEB_TITLE);
            if (!TextUtils.isEmpty(title)) {
                mTitleView.setText(title);
            }
        }

        final FavorItem favorItem = (FavorItem) intent.getSerializableExtra(WebConst.EXTRA_NEWS);
        mFavorItem = favorItem;
        if (favorItem != null) {
            long accountId = 0;
            final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                    .getManager(IAccountManager.class.getName());
            if (accountManager != null) {
                accountId = accountManager.getAccountId();
            }
            favorItem.setAccountId(accountId);

            boolean isFavor = false;
            final IFavorManager favorManager = (IFavorManager) ComponentManager.getInstance()
                    .getManager(IFavorManager.class.getName());
            if (favorManager != null) {
                isFavor = favorManager.isFavor(mFavorItem);
            }
            setFavorButtonState(isFavor);

        }
        // 避免特殊字符被转换
        mUrl = mWebView.getUrl();
    }

    private void initViews() {
        DtLog.d(TAG, "initViews");
        findViewById(R.id.content).setOnClickListener(this);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        mTitleView = (TextView) findViewById(R.id.actionbar_title);
        mBottomBar = findViewById(R.id.webViewBottomBar);
        mBottomBar.findViewById(R.id.webViewShare).setOnClickListener(this);
        mFavorButton = mBottomBar.findViewById(R.id.webViewFavor);
        mFavorButton.setOnClickListener(this);
        mLoadingView = findViewById(R.id.loading);
        mRetryView = findViewById(R.id.retry_view);
        mRetryView.setOnClickListener(this);
        initWebView();
    }

    protected void initWebView() {
        final DtWebView webView = (DtWebView)findViewById(R.id.dengta_web_view);
        webView.init();

        webView.setWebViewClient(new ThirdPartyNewsWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.setOnScrollListener(this);
        mWebView = webView;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        final int id = v.getId();
        if (id == R.id.actionbar_back_button) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
        } else if (id == R.id.webViewShare) {
            share();
        } else if (id == R.id.webViewFavor) {
            clickFavor();
        } else if (id == R.id.content || id == R.id.retry_view) {
            if (mRetryView.getVisibility() == View.VISIBLE) {
                mWebView.reload();
            }
        }
    }

    private void setFavorButtonState(final boolean isFavor) {
        mFavorButton.setActivated(isFavor);
    }

    private void clickFavor() {
        final IFavorManager favorManager = (IFavorManager) ComponentManager.getInstance()
                .getManager(IFavorManager.class.getName());
        if (favorManager != null) {
            final FavorItem favorItem = mFavorItem;
            if (favorItem == null) {
                CommonToast.showToast(R.string.favor_not_support);
            } else {
                if (!TextUtils.equals(mWebView.getUrl(), mUrl)) {
                    CommonToast.showToast(R.string.favor_not_support);
                } else {
                    final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                            .getManager(IAccountManager.class.getName());
                    if (accountManager == null) {
                        return;
                    }
                    final long accountId = accountManager.getAccountId();
                    if (accountId == 0) { // 未登录
                        CommonBeaconJump.showLogin(this);
                    } else {
                        // 登录状态可能会变
                        favorItem.setAccountId(accountId);
                        final boolean isFavor = !favorManager.isFavor(favorItem);
                        if (isFavor) {
                            favorManager.addFavor(favorItem);
                            CommonToast.showToast(R.string.setting_favor_already);
                        } else {
                            favorManager.deleteFavor(favorItem);
                        }
                        setFavorButtonState(isFavor);
                    }
                }
            }
        }
    }

    private void share() {
        final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
                .getManager(IShareManager.class.getName());
        if (shareManager == null) {
            return;
        }
        final ShareParams params = new ShareParams();
        final Resources res = getResources();
        params.title = res.getString(R.string.share_news_title);
        params.msg = res.getString(R.string.share_news_msg);
        String newsId = "";
        if (mFavorItem != null) {
            newsId = mFavorItem.getFavorId();
        }
        params.url = "https://wedengta.com/?newsid=" + newsId;
        params.imageUrl = WebUrlManager.getInstance().getShareIconUrl();
        shareManager.showShareDialog(this, params);
    }

    @Override
    public void onScrollUp() {
        mBottomBar.setVisibility(View.GONE);
    }

    @Override
    public void onScrollDown() {
        mBottomBar.setVisibility(View.VISIBLE);
    }

    private final class ThirdPartyNewsWebViewClient extends WebViewClient {
        private boolean firstPageFinished = true;
        private boolean error = false;

        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
            error = false;
            DtLog.d(TAG, "onPageStarted : s = " + s);
            mLoadingView.setVisibility(View.VISIBLE);
            mRetryView.setVisibility(View.INVISIBLE);
            mWebView.setVisibility(View.INVISIBLE);
            mBottomBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);
            DtLog.d(TAG, "onPageFinished : url = " + url);
            if (error) {
                return;
            }
            JsProxy.removeAd((DtWebView) webView);
            if (firstPageFinished) {
                firstPageFinished = false;
                mUrl = url;
            }
            mLoadingView.setVisibility(View.INVISIBLE);
            mRetryView.setVisibility(View.INVISIBLE);
            mWebView.setVisibility(View.VISIBLE);
            mBottomBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            // 不展示默认错误界面
//            super.onReceivedError(webView, webResourceRequest, webResourceError);
            final Uri uri = webResourceRequest.getUrl();
            DtLog.d(TAG, "onReceivedError : uri = " + uri);

            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                path = path.toLowerCase();
                if (path.endsWith(".js") || path.endsWith(".css") || path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".jpeg")) {
                    return;
                }
                error = true;
                mLoadingView.setVisibility(View.INVISIBLE);
                mRetryView.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
