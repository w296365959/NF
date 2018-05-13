package com.sscf.investment.web.sdk.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.db.FavorItem;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IFavorManager;
import com.dengtacj.component.managers.IShareManager;
import com.dengtacj.component.router.BeaconJump;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.CommonToast;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.component.ui.widget.TwoTabSelectorView;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DownloadUtils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.NumberUtil;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.dengtacj.component.entity.ShareParams;
import com.sscf.investment.web.sdk.JsProxy;
import com.sscf.investment.web.sdk.NativeProxy;
import com.sscf.investment.web.sdk.R;
import com.sscf.investment.web.sdk.manager.Scheme;
import com.sscf.investment.web.sdk.WebConst;
import com.sscf.investment.web.sdk.WebSettingPref;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.web.sdk.utils.WebUtils;
import com.tencent.smtt.sdk.WebView;
import org.json.JSONObject;
import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by xuebinliu on 2015/8/4.
 * 新功能请不要添加到此类
 */
@Route("WebActivity")
@Deprecated
public final class WebActivity extends BaseFragmentActivity implements View.OnClickListener, DtWebView.OnScrollListener,
        Handler.Callback, TwoTabSelectorView.OnTabSelectedListener {
    private static final String TAG = WebActivity.class.getSimpleName();

    /**
     * 是否设置了web大字体
     */
    public static final String KEY_WEB_FONT_SIZE_BIG = "key_web_font_size_big";

    private String mUrl;

    private TextView mFavorButton;
    private FavorItem mFavorItem;
    private FavorItem mWebFavorItem;

    private TwoTabSelectorView mChangeFontSizeView;
    private View mWebViewBottomBar;
    private TextView mActionBarTitleView;

    private DtWebView mWebView;

    // web页面类型定义
    private int mCurrentWebType;

    private int mSwipeBackType = WebConst.DEFAULT_SWIPE_BACK_TYPE;

    private String mTitle = "";

    private boolean mWebBackEnable;

    // JS交互的回调
    @Override
    public boolean handleMessage(Message msg) {
        DtLog.d(TAG, "handleMessage msg.what =" + msg.what);
        if (isDestroy()) {
            return true;
        }
        switch (msg.what) {
            case WebConst.MSG_UPDATE_TITLE:
                mTitle = (String)msg.obj;
                mActionBarTitleView.setText(mTitle);
                break;
            case WebConst.MSG_SET_LONG_CLICK_ENABLED:
                // setOnLongClickListener可能会导致WebView卡死
                // mWebView.setLongClickEnable((boolean) msg.obj);
                break;
            case WebConst.MSG_FINISH_PAGE:
                finish();
                break;
            case WebConst.MSG_DOWNLOAD_FILE:
                final String downloadUrl = (String) msg.obj;
                final File pdfFile = FileUtil.getPdfFileByUrl(downloadUrl);
                final WeakReference<DtWebView> webviewRef = new WeakReference<DtWebView>(mWebView);
                SDKManager.getInstance().getDefaultExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        DownloadUtils.httpDownload(downloadUrl, pdfFile, new DownloadUtils.HttpDownloadCallback() {
                            @Override
                            public void onProgressUpdated(String url, float progress) {
                                DtWebView webView = webviewRef.get();
                                if (null != webView) {
                                    JsProxy.updateDownloadProgress(webView, url, progress);
                                }
                            }

                            @Override
                            public void onDownloadComplete(String url, boolean success) {
                                DtWebView webView = webviewRef.get();
                                if (null != webView) {
                                    JsProxy.downloadComplete(webView, url, success);
                                }
                            }
                        });
                    }
                });
                break;
            case WebConst.MSG_OPEN_PDF_BY_SYSTEM:
                String url = (String) msg.obj;
                final File file = FileUtil.getPdfFileByUrl(url);
                if (file.exists()) {
                    BeaconJump.showPdfViewer(this, Uri.fromFile(file).toString());
                }
                break;
            case WebConst.MSG_SEND_INFO_TO_NATIVE:
                getInfoFromWeb((JSONObject) msg.obj);
                break;
            case WebConst.MSG_SET_BACK_BUTTON_LISTENER_ENABLE:
                mWebBackEnable = msg.arg1 == 1;
                break;
            default:
                break;
        }
        return true;
    }

    private void getInfoFromWeb(JSONObject jsonObject) {
        final String type = jsonObject.optString("type");
        if ("newsDetail".equals(type)) { // 获得新闻信息
            jsonObject = jsonObject.optJSONObject("data");

            final String favorId = jsonObject.optString("sNewsID");
            final int favorType = jsonObject.optInt("eNewsType");
            final String title = jsonObject.optString("sTitle");
            final String infoUrl = jsonObject.optString("sDtInfoUrl");
            final long publishTime = CommonConst.MILLIS_FOR_SECOND * jsonObject.optInt("iTime");
            long accountId = 0;
            final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                    .getManager(IAccountManager.class.getName());
            if (accountManager != null) {
                accountId = accountManager.getAccountId();
            }
            final FavorItem favorItem = new FavorItem(accountId, favorId, favorType, title, infoUrl, publishTime);

            // 潜规则，web会传两次值，第一次默认值，第二次是真实值
            if (mWebFavorItem == null) { // 第一次
                mWebFavorItem = favorItem;
                if (mFavorItem == null) { // 如果没有从上一个界面传值进来，就先使用默认值
                    mFavorItem = favorItem;
                }
            } else { // 第二次
                mWebFavorItem = null;
                mFavorItem = favorItem;
            }

            boolean isFavor = false;
            final IFavorManager favorManager = (IFavorManager) ComponentManager.getInstance()
                    .getManager(IFavorManager.class.getName());
            if (favorManager != null) {
                isFavor = favorManager.isFavor(favorItem);
            }
            setFavorButtonState(isFavor);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DtLog.d(TAG, "onCreate");

        try {
            setContentView(R.layout.web_activity_layout);
        } catch (Exception e) {
            e.printStackTrace(); // 处理webview的java.lang.NullPointerException
            finish();
            return;
        }

        handleIntent(getIntent());

        String url = mUrl;
        if (TextUtils.isEmpty(url)) {
            finish();
            return;
        }

        initViews();

        initWebview();

        WebUtils.setCookies(this, url);
        url = WebUtils.addThemeParam(url);
        DtLog.d(TAG, "handleIntent url final =" + url);
        mWebView.loadUrl(url);

        registerLocalBroadcast();
    }

    private void initViews() {
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        mActionBarTitleView = (TextView)findViewById(R.id.actionbar_title);
        mChangeFontSizeView = (TwoTabSelectorView)findViewById(R.id.actionbar_webactivity_selector);
        mWebViewBottomBar = findViewById(R.id.webViewBottomBar);
        mFavorButton = (TextView) findViewById(R.id.webViewFavor);

        DtLog.d(TAG, "initViews mCurrentWebType=" + mCurrentWebType);
        switch (mCurrentWebType) {
            case WebConst.WT_NEWS:// 新闻
            case WebConst.WT_REPORT:// 研报
            case WebConst.WT_ANNONCEMENT:// 公告
            case WebConst.WT_DISCOVERY_MARKET: // 发现情报
                initBottomBar();
                mWebViewBottomBar.setVisibility(View.VISIBLE);
                if (!isPdfConvert(mUrl)) {
                    initChangeFontSizeView();
                    mChangeFontSizeView.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    private void initBottomBar() {
        mFavorButton.setOnClickListener(this);

        findViewById(R.id.webViewShare).setOnClickListener(this);

        mFavorItem = (FavorItem) getIntent().getSerializableExtra(WebConst.EXTRA_NEWS);
        if (mFavorItem != null) {
            long accountId = 0;
            final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                    .getManager(IAccountManager.class.getName());
            if (accountManager != null) {
                accountId = accountManager.getAccountId();
            }

            boolean isFavor = false;
            final IFavorManager favorManager = (IFavorManager) ComponentManager.getInstance()
                    .getManager(IFavorManager.class.getName());
            if (favorManager != null) {
                isFavor = favorManager.isFavor(mFavorItem);
            }

            mFavorItem.setAccountId(accountId);
            setFavorButtonState(isFavor);
        }
    }

    private void initChangeFontSizeView() {
        mChangeFontSizeView.setResouce(R.color.web_actionbar_two_tab_text_color_selector, R.drawable.web_actionbar_two_tab_selector_bg,
                R.drawable.web_actionbar_two_tab_left_bg_selected, R.drawable.web_actionbar_two_tab_right_bg_selected,
                R.drawable.web_actionbar_two_tab_middle_bg_selected);
        mChangeFontSizeView.setTabTitles(R.string.small, R.string.big);
        mChangeFontSizeView.setTabTitlesSize(R.dimen.font_size_12, R.dimen.font_size_16);
        if (WebSettingPref.getIBoolean(KEY_WEB_FONT_SIZE_BIG, false)) {
            mChangeFontSizeView.switchTab(1);
        }
        mChangeFontSizeView.setOnTabSelectedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterLocalBroadcast();
        if (mWebView != null) {
            mWebView.destroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        JsProxy.onWebViewShow(mWebView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JsProxy.onWebViewHide(mWebView);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        switch (mSwipeBackType) {
            case WebConst.SWIPE_BACK_TYPE_DISALLOWED:
                DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));
                break;
            case WebConst.SWIPE_BACK_TYPE_FROM_LEFT_EDGE:
                SwipeBackLayout.attachSwipeLayout(this, true);
                break;
            case WebConst.SWIPE_BACK_TYPE_FULLSCREEN:
                SwipeBackLayout.attachSwipeLayout(this);
                break;
            default:
                break;
        }
    }

    private void initWebview() {
        mWebView = (DtWebView)findViewById(R.id.dengta_web_view);
        mWebView.init();
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.setOnScrollListener(this);


        DtBaseWebViewClient webViewClient = null;
        switch (mCurrentWebType) {
            case WebConst.WT_NEWS:// 新闻
            case WebConst.WT_REPORT:// 研报
            case WebConst.WT_ANNONCEMENT:// 公告
            case WebConst.WT_DISCOVERY_MARKET: // 发现情报
                webViewClient = new NewsWebViewClient(this);
                break;
            default:
                webViewClient = new DtBaseWebViewClient();
                break;
        }

        try {
            mWebView.setWebViewClient(webViewClient);
        } catch (Exception e) {
            e.printStackTrace();
            StatisticsUtil.reportError(e);
        }

        final DtWebChromeClient webChromeClient = new DtWebChromeClient();
        mWebView.setWebChromeClient(webChromeClient);

        final NativeProxy nativeProxy = new NativeProxy(new Handler(getMainLooper(), this));
        mWebView.addJavascriptInterface(nativeProxy, NativeProxy.TAG);
    }

    /**
     * 页面加载完成，如果设置了大字体则调整为大字体
     */
    public void updateFontSizeIfNeeded() {
        if (mCurrentWebType == WebConst.WT_NEWS || mCurrentWebType == WebConst.WT_REPORT ||
                mCurrentWebType == WebConst.WT_ANNONCEMENT || mCurrentWebType == WebConst.WT_DISCOVERY_MARKET) {
            if (WebSettingPref.getIBoolean(KEY_WEB_FONT_SIZE_BIG, false)) {
                JsProxy.adjustFontSizeBig(mWebView);
                mChangeFontSizeView.switchTab(1);
            }
        }
    }

    @Override
    public void onFirstTabSelected() {
        DtLog.d(TAG, "onFirstTabSelected set small font size");
        WebSettingPref.putIBoolean(KEY_WEB_FONT_SIZE_BIG, false);
        JsProxy.adjustFontSizeSmall(mWebView);
    }

    @Override
    public void onSecondTabSelected() {
        DtLog.d(TAG, "onSecondTabSelected set big font size");
        WebSettingPref.putIBoolean(KEY_WEB_FONT_SIZE_BIG, true);
        JsProxy.adjustFontSizeBig(mWebView);
    }

    @Override
    public void onThirdTabSelected() {
    }

    private void setFavorButtonState(final boolean isFavor) {
        mFavorButton.setActivated(isFavor);
    }

    private void handleIntent(Intent intent) {
        // 加载url
        final String url = intent.getStringExtra(WebConst.URL_ADDR);
        DtLog.d(TAG, "handleIntent url=" + url);
        mUrl = url;
        if (TextUtils.isEmpty(url)) {
            return;
        }

        int swipeBackType = intent.getIntExtra(WebConst.SWIPE_BACK_TYPE, WebConst.DEFAULT_SWIPE_BACK_TYPE);

        final Uri uri = Uri.parse(url);
        if (uri.isHierarchical()) {
            swipeBackType = NumberUtil.parseInt(uri.getQueryParameter(Scheme.TAG_SWIPE_BACK_TYPE), swipeBackType);
        }
        mSwipeBackType = swipeBackType;

        // 取出web page的类型
        mCurrentWebType = intent.getIntExtra(WebConst.WEB_TYPE, -1);
    }

    /**
     * 潜规则逻辑
     * @param url
     * @return
     */
    private boolean isPdfConvert(String url) {
        final Uri uri = Uri.parse(url);
        final String isPdfConvert = uri.getQueryParameter("isPdfConvert");
        return NumberUtil.parseInt(isPdfConvert, 0) == 1;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            if(mWebView.canGoBack()) {
                // 返回上一页面
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        final int id = v.getId();
        if (id == R.id.webViewFavor) {
            clickFavorButton();
        } else if (id == R.id.actionbar_back_button) {
            back();
        } else if (id == R.id.webViewShare) {
            if (mFavorItem == null) {
                return;
            }
            final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
                    .getManager(IShareManager.class.getName());
            if (shareManager == null) {
                return;
            }
            shareManager.showShareDialog(this, getNewsShareParams());
        }
    }

    private ShareParams mShareParams;

    private ShareParams getNewsShareParams() {
        ShareParams params = mShareParams;
        if (params == null) {
            final String msg = getString(R.string.share_news_msg);
            final String url = Scheme.getSharedUrl(mFavorItem.getInfoUrl());
            params = ShareParams.createShareParams(mFavorItem.getTitle(), msg, url, getShareIconUrl());
            mShareParams = params;
        }
        return params;
    }

    /**
     * 查找不同页面类型的分享icon url
     * @return
     */
    private String getShareIconUrl() {
        return WebUrlManager.getInstance().getShareIconUrl();
    }

    private void clickFavorButton() {
        final IFavorManager favorManager = (IFavorManager) ComponentManager.getInstance()
                .getManager(IFavorManager.class.getName());
        if (favorManager == null) {
            return;
        }

        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }

        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo != null) {
            final FavorItem favorItem = mFavorItem;
            if (favorItem == null) {
                return;
            }

            // 登录状态可能会变
            favorItem.setAccountId(accountInfo.id);

            final boolean isFavor = !favorManager.isFavor(favorItem);

            if (isFavor) {
                favorManager.addFavor(favorItem);
                CommonToast.showToast(R.string.setting_favor_already);
            } else {
                favorManager.deleteFavor(favorItem);
            }
            setFavorButtonState(isFavor);
        } else {
            CommonBeaconJump.showLogin(this);
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (mWebBackEnable) {
            JsProxy.clickButton(mWebView, BaseWebActivity.VALUE_BACK_BUTTON);
            return;
        }
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onScrollUp() {
        if (mFavorItem == null) {
            return;
        }

        mWebViewBottomBar.setVisibility(View.GONE);
    }

    @Override
    public void onScrollDown() {
        if (mFavorItem == null) {
            return;
        }

        mWebViewBottomBar.setVisibility(View.VISIBLE);
    }

    private BroadcastReceiver mLoginReceiver;

    private void registerLocalBroadcast() {
        if (mLoginReceiver == null) {
            mLoginReceiver = new LocalBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter(CommonConst.ACTION_LOGIN_SUCCESS);
            intentFilter.addAction(CommonConst.ACTION_LOGOUT_SUCCESS);
            intentFilter.addAction(CommonConst.ACTION_UPDATE_ACCOUNT_INFO);
            intentFilter.addAction(CommonConst.ACTION_BEACON_PROTOCAL_ERROR);
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mLoginReceiver, intentFilter);
        }
    }

    private void unregisterLocalBroadcast() {
        if (mLoginReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mLoginReceiver);
        }
    }

    private final class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (CommonConst.ACTION_LOGIN_SUCCESS.equals(action)) {
                WebUtils.setCookies(context, mWebView.getUrl());
                JsProxy.loginSuccess(mWebView);
            } else if (CommonConst.ACTION_LOGOUT_SUCCESS.equals(action) || CommonConst.ACTION_UPDATE_ACCOUNT_INFO.equals(action)) {
                WebUtils.setCookies(context, mWebView.getUrl());
            } else if (CommonConst.ACTION_BEACON_PROTOCAL_ERROR.equals(action)) {
                JsProxy.beaconOpenFail(mWebView, 1);
            }
        }
    }
}

final class NewsWebViewClient extends DtBaseWebViewClient {
    private WebActivity mWebActivity;

    NewsWebViewClient(final WebActivity webActivity) {
        mWebActivity = webActivity;
    }

    @Override
    public void onPageFinished(final WebView view, String url) {
        super.onPageFinished(view, url);
        if (mWebActivity != null) {
            mWebActivity.updateFontSizeIfNeeded();
        }
    }
}