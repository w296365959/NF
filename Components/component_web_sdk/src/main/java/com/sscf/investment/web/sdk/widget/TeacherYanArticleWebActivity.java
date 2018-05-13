package com.sscf.investment.web.sdk.widget;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.ShareParams;
import com.dengtacj.component.entity.ShareType;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IShareManager;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.component.ui.widget.TwoTabSelectorView;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.web.sdk.JsProxy;
import com.sscf.investment.web.sdk.NativeProxy;
import com.sscf.investment.web.sdk.R;
import com.sscf.investment.web.sdk.WebConst;
import com.sscf.investment.web.sdk.WebSettingPref;
import com.sscf.investment.web.sdk.manager.Scheme;
import com.sscf.investment.web.sdk.utils.WebUtils;
import com.tencent.smtt.sdk.WebView;

import BEC.AccountInfo;
import BEC.AccountTicket;
import BEC.E_INFO_TYPE;
import BEC.SscfInfoLikeReq;
import BEC.SscfInfoLikeRsp;
import BEC.SscfInfoLikeUserReq;
import BEC.SscfInfoLikeUserRsp;
import BEC.UserInfo;

/**
 * Created by LEN on 2018/5/4.
 */
@Route("TeacherYanArticleWebActivity")
public class TeacherYanArticleWebActivity extends BaseWebActivity implements DataSourceProxy.IRequestCallback{

    private static final String TAG = TeacherYanArticleWebActivity.class.getSimpleName();

    private TwoTabSelectorView mChangeFontSizeView;

    private ImageView mFavorButton;

    public static final String KEY_WEB_FONT_SIZE_BIG = "key_web_font_size_big";

    private String title;

    private int sscfInfoId;

    private boolean isLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getIntent().getStringExtra(WebConst.KEY_SHARE_TITLE);
        String id = getIntent().getStringExtra(WebConst.KEY_ARTICLE_ID);
        if (!TextUtils.isEmpty(id))
            sscfInfoId = Integer.valueOf(id);
        handleIntent();

        if (TextUtils.isEmpty(mUrl)) {
            finish();
            return;
        }

        try {
            setContentView(R.layout.activity_teacher_yan_article_web);
        } catch (Exception e) {
            e.printStackTrace(); // 处理webview的java.lang.NullPointerException
            finish();
            return;
        }

        initViews();

        WebUtils.setCookies(this, mUrl);
        final String url = mSupportTheme ? WebUtils.addThemeParam(mUrl) : mUrl;
        mWebView.loadUrl(url);
        getLikeInfo();
        DtLog.d(TAG, "loadUrl url : " + url);
    }

    @Override
    protected void initViews() {
        findViewById(R.id.webViewShare).setOnClickListener(this);

        mFavorButton = (ImageView) findViewById(R.id.webViewFavor);
        mFavorButton.setOnClickListener(this);
        mChangeFontSizeView = (TwoTabSelectorView)findViewById(R.id.actionbar_webactivity_selector);
        super.initViews();
        initChangeFontSizeView();
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

    public void updateFontSizeIfNeeded() {
        if (WebSettingPref.getIBoolean(KEY_WEB_FONT_SIZE_BIG, false)) {
            JsProxy.adjustFontSizeBig(mWebView);
            mChangeFontSizeView.switchTab(1);
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

    @Override
    protected void initWebView() {
        final DtWebView webView = (DtWebView)findViewById(R.id.dengta_web_view);
        webView.init();
        webView.setBackgroundColor(Color.TRANSPARENT);

        final DtBaseWebViewClient webViewClient = new TeacherYanWebViewClient(this);
        webView.setWebViewClient(webViewClient);

        final DtWebChromeClient webChromeClient = new DtWebChromeClient();
        webView.setWebChromeClient(webChromeClient);
        mWebChromeClient = webChromeClient;

        final NativeProxy nativeProxy = new NativeProxy(getHandler());
        webView.addJavascriptInterface(nativeProxy, NativeProxy.TAG);
        webView.getX5WebViewExtension();

        mWebView = webView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.webViewShare) {
            final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
                    .getManager(IShareManager.class.getName());
            if (shareManager == null) {
                return;
            }
            shareManager.showShareDialog(this, getShareParams(), ShareType.MOMENTS);
        }else if (id == R.id.webViewFavor) {
            clickFavorButton();
        }
    }

    private void clickFavorButton() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo != null) {
            doLike();
        } else {
            CommonBeaconJump.showLogin(this);
        }
    }

    private IAccountManager getAccountManager() {
        return (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
    }

    private AccountInfoEntity getAccountInfo() {
        final IAccountManager accountManager = getAccountManager();
        if (accountManager == null) {
            return null;
        }
        return accountManager.getAccountInfo();
    }

    private UserInfo getUserInfo() {
        final IAccountManager accountManager = getAccountManager();
        if (accountManager == null) {
            return null;
        }
        return accountManager.getUserInfo();
    }

    private void setFavorButtonState() {
        runOnUiThread(() ->{
            mFavorButton.setImageResource(isLiked ? R.drawable.icon_praised : R.drawable.icon_praise);
        });
    }

    private void doLike() {
        SscfInfoLikeReq req = new SscfInfoLikeReq();
        req.setEInfoType(E_INFO_TYPE.E_IT_TEACH);
        req.setIsAdd(!isLiked);
        req.setISscfInfoID(sscfInfoId);
        AccountInfoEntity entity = getAccountInfo();
        if (null != entity) {
            AccountTicket accountTicket = new AccountTicket();
            accountTicket.setVtTicket(entity.ticket);
            req.setStAccountTicket(accountTicket);
            req.setStUserInfo(getUserInfo());
        }
        DataEngine.getInstance().request(EntityObject.ET_DO_LIKE_INFO, req, this);
    }

    private void getLikeInfo() {
        SscfInfoLikeUserReq req = new SscfInfoLikeUserReq();
        req.eInfoType = E_INFO_TYPE.E_IT_TEACH;
        req.setISscfInfoID(sscfInfoId);
        AccountInfoEntity entity = getAccountInfo();
        if (null != entity) {
            AccountTicket accountTicket = new AccountTicket();
            accountTicket.setVtTicket(entity.ticket);
            req.setStAccountTicket(accountTicket);
            req.setStUserInfo(getUserInfo());
        }
        DataEngine.getInstance().request(EntityObject.ET_GET_LIKE_INFO, req, this);
    }

    private ShareParams mShareParams;

    private ShareParams getShareParams() {
        ShareParams params = mShareParams;
        if (params == null) {
            final String msg = getString(R.string.share_news_msg);
            final String url = Scheme.getSharedUrl(mUrl);
            params = ShareParams.createShareParams(title, msg, url, getShareIconUrl());
            mShareParams = params;
        }
        return params;
    }

    private String getShareIconUrl() {
        return WebUrlManager.getInstance().getShareIconUrl();
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success) {
            switch (data.getEntityType()) {
                case EntityObject.ET_GET_LIKE_INFO:
                    getLikeInfoRsp((SscfInfoLikeUserRsp) data.getEntity());
                    break;
                case EntityObject.ET_DO_LIKE_INFO:
                    getLikeInfo();
                    break;
            }
        }
    }

    private void getLikeInfoRsp(SscfInfoLikeUserRsp rsp) {
        isLiked = rsp.getIsLike();
        setFavorButtonState();
    }

    final class TeacherYanWebViewClient extends DtBaseWebViewClient {
        private TeacherYanArticleWebActivity mWebActivity;

        TeacherYanWebViewClient(final TeacherYanArticleWebActivity webActivity) {
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
}
