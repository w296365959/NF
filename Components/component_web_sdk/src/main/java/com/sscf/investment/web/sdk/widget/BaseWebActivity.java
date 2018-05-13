package com.sscf.investment.web.sdk.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.CouponInfo;
import com.dengtacj.component.entity.H5PayResult;
import com.dengtacj.component.entity.ShareParams;
import com.dengtacj.component.event.DeleteCommentEvent;
import com.dengtacj.component.event.PostCommentResultEvent;
import com.dengtacj.component.managers.IFeedRequestManager;
import com.dengtacj.component.managers.IShareManager;
import com.dengtacj.component.router.BeaconJump;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.sscf.investment.component.ui.widget.InputDialog;
import com.sscf.investment.component.ui.widget.RefreshButton;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.component.ui.widget.TwoTabSelectorView;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.utils.BitmapUtils;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DownloadUtils;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.NumberUtil;
import com.sscf.investment.web.sdk.JsProxy;
import com.sscf.investment.web.sdk.NativeProxy;
import com.sscf.investment.web.sdk.R;
import com.sscf.investment.web.sdk.WebConst;
import com.sscf.investment.web.sdk.manager.Scheme;
import com.sscf.investment.web.sdk.utils.WebScreenShotUtlis;
import com.sscf.investment.web.sdk.utils.WebUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by davidwei on 2016/01/04.
 * 通用的webactivity
 */
public abstract class BaseWebActivity extends BaseFragmentActivity implements View.OnClickListener, Handler.Callback,
        IShareManager.ShareListener, TwoTabSelectorView.OnTabSelectedListener,
        CommonDialog.OnDialogButtonClickListener {
    private static final String TAG = BaseWebActivity.class.getSimpleName();
    protected String mUrl;
    protected boolean mSupportTheme = true;

    private RelativeLayout mActionBar;
    protected ImageView mBackButton;
    protected TextView mCloseButton;
    protected TextView mTitleView;
    protected RefreshButton mRefreshButton;
    protected LinearLayout mRightContainer;
    protected TwoTabSelectorView mTabSelectorView;

    protected DtWebView mWebView;
    protected Handler mHandler;

    protected static final String KEY_VIEW_TYPE = "viewType";
    protected static final String KEY_VISIBILITY = "visibility";
    protected static final String KEY_COLOR = "color";

    protected static final String VALUE_BACK_BUTTON = "backButton";
    protected static final String VALUE_CLOSE_BUTTON = "closeButton";
    protected static final String VALUE_SHARE_BUTTON = "shareButton";
    protected static final String VALUE_REFRESH_BUTTON = "refreshButton";
    protected static final String VALUE_FAQ_BUTTON = "faqButton";
    protected static final String VALUE_FILTER_BUTTON = "filterButton";
    protected static final String VALUE_RIGHT_TEXT_BUTTON = "rightTextButton";
    protected static final String VALUE_TAB_SELECTOR_VIEW = "segmentButton";
    protected static final String VALUE_BROTHER_SCHOOL_VIEW = "brotherSchool";
    protected static final String VALUE_TITLE_BAR_VIEW = "titleBar";
    protected static final String VALUE_TITLE_TXT_VIEW = "titleTxt";

    private int mSwipeBackType = WebConst.SWIPE_BACK_TYPE_FROM_LEFT_EDGE;
    protected String mWebTitle = "";

    protected DtWebChromeClient mWebChromeClient;
    private boolean mWebBackEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerLocalBroadcast();
        EventBus.getDefault().register(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterLocalBroadcast();
        EventBus.getDefault().unregister(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mWebChromeClient != null) {
            mWebChromeClient.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void handleIntent() {
        final Intent intent = getIntent();
        final String url = intent.getStringExtra(WebConst.URL_ADDR);
        mUrl = url;

        if (TextUtils.isEmpty(url)) {
            return;
        }

        final Uri uri = Uri.parse(url);

        if (uri.isHierarchical()) {
            mSwipeBackType = NumberUtil.parseInt(uri.getQueryParameter(Scheme.TAG_SWIPE_BACK_TYPE),
                    WebConst.SWIPE_BACK_TYPE_FROM_LEFT_EDGE);
            final String webTitle = uri.getQueryParameter(Scheme.TAG_WEB_TITLE);
            if (webTitle != null) {
                mWebTitle = webTitle;
            }
        }

        mSupportTheme = getIntent().getBooleanExtra(WebConst.SUPPORT_THEME, true);
    }

    protected void initViews() {
        mActionBar = (RelativeLayout) findViewById(R.id.actionBar);
        mBackButton = (ImageView) mActionBar.findViewById(R.id.actionbar_back_button);
        mBackButton.setOnClickListener(this);
        mCloseButton = (TextView) mActionBar.findViewById(R.id.actionbar_close_button);
        mCloseButton.setOnClickListener(this);
        mTitleView = (TextView) mActionBar.findViewById(R.id.actionbar_title);
        mTitleView.setText(mWebTitle);

        initWebView();
    }

    protected void initWebView() {
        final DtWebView webView = (DtWebView)findViewById(R.id.dengta_web_view);
        webView.init();
        webView.setBackgroundColor(Color.TRANSPARENT);

        final DtBaseWebViewClient webViewClient = new DtBaseWebViewClient();
        webView.setWebViewClient(webViewClient);

        final DtWebChromeClient webChromeClient = new DtWebChromeClient();
        webView.setWebChromeClient(webChromeClient);
        mWebChromeClient = webChromeClient;

        final NativeProxy nativeProxy = new NativeProxy(getHandler());
        webView.addJavascriptInterface(nativeProxy, NativeProxy.TAG);
        webView.getX5WebViewExtension();

        mWebView = webView;
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(getMainLooper(), this);
        }
        return mHandler;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.actionbar_back_button) {
            back();
        } else if (id == R.id.actionbar_close_button) {
            finish();
        } else if (id == R.id.actionbar_share) {
            JsProxy.clickShareButton(mWebView);
        } else if (id == R.id.actionbar_right_button) {
            JsProxy.clickRightButton(mWebView);
        } else if (id == R.id.actionbar_refresh_button_layout) {
            JsProxy.clickRefreshButton(mWebView);
        } else if (id == R.id.actionbar_faq) {
            JsProxy.clickFaqButton(mWebView);
        } else if (id == R.id.actionbar_filter) {
            JsProxy.clickButton(mWebView, VALUE_FILTER_BUTTON);
        } else if (id == R.id.actionbar_school) {
            JsProxy.clickButton(mWebView, VALUE_BROTHER_SCHOOL_VIEW);
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (mWebBackEnable) {
            JsProxy.clickButton(mWebView, VALUE_BACK_BUTTON);
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
    public void onShareListener(int state, String plat) {
        JsProxy.shareState(mWebView, state, plat);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (isDestroy()) {
            return true;
        }
        switch (msg.what) {
            case WebConst.MSG_UPDATE_TITLE:
                mTitleView.setText((String) msg.obj);
                break;
            case WebConst.MSG_SET_LONG_CLICK_ENABLED:
                // setOnLongClickListener可能会导致WebView卡死
                // mWebView.setLongClickEnable((boolean) msg.obj);
                break;
            case WebConst.MSG_SHARE:
                final boolean shareScreenShot = msg.arg1 > 0;
                final HashMap<String, ShareParams> params = (HashMap<String, ShareParams>) msg.obj;
                if(shareScreenShot) {
                    new ScreenShotTask(params).executeOnExecutor(SDKManager.getInstance().getDefaultExecutor());
                } else {
                    showShareDialog(params);
                }
                break;
            case WebConst.MSG_SET_WEBVIEW_TITLE_BAR:
                setWebViewTitleBar((JSONArray) msg.obj);
                break;
            case WebConst.MSG_SET_REFRESH_BUTTON_ANIM:
                if (mRefreshButton != null) {
                    if (msg.arg1 == 1) {
                        mRefreshButton.startLoadingAnim();
                    } else {
                        mRefreshButton.stopLoadingAnim();
                    }
                }
                break;
            case WebConst.MSG_FINISH_PAGE:
                finish();
                break;
            case WebConst.MSG_SHOW_DIALOG:
                showCommonDialog((JSONObject) msg.obj);
                break;
            case WebConst.MSG_SHOW_DELETE_COMMENT_DIALOG:
                JSONObject jsonObject = (JSONObject) msg.obj;
                showDeleteCommentDialog(jsonObject);
                break;
            case WebConst.MSG_SHOW_INPUT_DIALOG:
                showInputDialog((JSONObject) msg.obj);
                break;
            case WebConst.MSG_SET_COUPON:
                setCoupon((JSONObject) msg.obj);
                break;
            case WebConst.MSG_H5_PAY_RESULT:
                setH5PayResult((JSONObject) msg.obj);
                break;
            case WebConst.MSG_GET_GPS_LOCATION:
//                getGPSLocation();
                break;
            case WebConst.MSG_SET_SIGN_RESULT:
                setSignResult((JSONObject) msg.obj);
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
            case WebConst.MSG_SET_BACK_BUTTON_LISTENER_ENABLE:
                mWebBackEnable = msg.arg1 == 1;
                break;
            default:
                break;
        }
        return true;
    }

    private void showDeleteCommentDialog(JSONObject jsonObject) {
        try {
            String secCode = jsonObject.getString("secCode");
            String feedId = jsonObject.getString("feedId");
            String commentId = jsonObject.getString("commentId");
            final IFeedRequestManager feedRequestManager = (IFeedRequestManager) ComponentManager.getInstance()
                    .getManager(IFeedRequestManager.class.getName());
            feedRequestManager.showFeedDeleteDialog(this, secCode, feedId, commentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setWebViewTitleBar(final JSONArray jsonArray) {
        final int length = jsonArray.length();
        JSONObject jsonObject = null;
        final String KEY_POSITION = "position";
        final String KEY_OPTION = "option";

        final String VALUE_LEFT = "left";
        final String VALUE_RIGHT = "right";
        final String VALUE_MIDDLE = "middle";
        final String VALUE_BOTTOM = "bottom";
        String position = null;
        for (int i = 0; i < length; i++) {
            jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject != null) {
                position = jsonObject.optString(KEY_POSITION);
                if (VALUE_LEFT.equals(position)) {
                    setLeftViews(jsonObject.optJSONArray(KEY_OPTION));
                } else if (VALUE_RIGHT.equals(position)) {
                    setRightViews(jsonObject.optJSONArray(KEY_OPTION));
                } else if (VALUE_MIDDLE.equals(position)) {
                    setCenterViews(jsonObject.optJSONArray(KEY_OPTION));
                } else if (VALUE_BOTTOM.equals(position)) {
                    setBottomViews(jsonObject.optJSONArray(KEY_OPTION));
                }
            }
        }
    }

    protected void setLeftViews(final JSONArray jsonArray) {
        final int length = jsonArray.length();
        JSONObject jsonObject = null;
        String viewType = null;

        int visibility = -1;
        for (int i = 0; i < length; i++) {
            jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject != null) {
                viewType = jsonObject.optString(KEY_VIEW_TYPE);
                visibility = jsonObject.optInt(KEY_VISIBILITY, -1);
                if (VALUE_BACK_BUTTON.equals(viewType)) { // 返回按钮
                    mBackButton.setVisibility(visibility == 1 ? View.VISIBLE : View.INVISIBLE);
                } else if (VALUE_CLOSE_BUTTON.equals(viewType)) { // 关闭按钮
                    mCloseButton.setVisibility(visibility == 1 ? View.VISIBLE : View.INVISIBLE);
                }
            }
        }
    }

    protected LinearLayout getRightContainer() {
        if (mRightContainer == null) {
            mRightContainer = new LinearLayout(this);
            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActionBar.addView(mRightContainer, params);
        }
        return mRightContainer;
    }

    protected void setRightViews(final JSONArray jsonArray) {
        JSONObject jsonObject = null;
        String viewType = null;

        final LinearLayout rightContainer = getRightContainer();
        rightContainer.removeAllViews();

        final int length = jsonArray.length();

        int visibility = -1;
        final int padding = getResources().getDimensionPixelSize(R.dimen.actionbar_margin);
        final int paddingHalf = padding / 2;
        int paddingRight = 0;

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        for (int i = 0; i < length; i++) {
            jsonObject = jsonArray.optJSONObject(i);
            if (i == length - 1) {
                paddingRight = padding;
            } else {
                paddingRight = paddingHalf;
            }

            if (jsonObject != null) {
                viewType = jsonObject.optString(KEY_VIEW_TYPE);
                visibility = jsonObject.optInt(KEY_VISIBILITY, -1);
                if (visibility != 1) {
                    continue;
                }

                if (VALUE_SHARE_BUTTON.equals(viewType)) { // 分享按钮
                    addRightButton(R.layout.actionbar_share_button, paddingHalf, paddingRight, params);
                } else if (VALUE_REFRESH_BUTTON.equals(viewType)) { // 刷新按钮
                    final View refreshButton = addRightButton(R.layout.actionbar_refresh_button, paddingHalf, paddingRight, params);
                    if (mRefreshButton != null) {
                        mRefreshButton.stopLoadingAnim();
                    }
                    mRefreshButton = (RefreshButton) refreshButton.findViewById(R.id.refresh_button);
                } else if(VALUE_FAQ_BUTTON.equals(viewType)) { // faq的button
                    addRightButton(R.layout.actionbar_faq_button, paddingHalf, paddingRight, params);
                } else if (VALUE_FILTER_BUTTON.equals(viewType)) { // 条件选股button
                    addRightButton(R.layout.actionbar_filter_button, paddingHalf, paddingRight, params);
                } else if (VALUE_RIGHT_TEXT_BUTTON.equals(viewType)) {
                    final TextView rightTextButton = (TextView) View.inflate(this, R.layout.actionbar_right_text_button, null);
                    rightTextButton.setText(jsonObject.optString("word"));
                    rightTextButton.setOnClickListener(this);
                    rightTextButton.setPadding(paddingHalf, 0, paddingRight, 0);
                    rightContainer.addView(rightTextButton, params);
                } else if (VALUE_BROTHER_SCHOOL_VIEW.equals(viewType)) { // 表哥学堂
                    addRightButton(R.layout.actionbar_school_button, paddingHalf, paddingRight, params);
                }
            }
        }
    }

    private View addRightButton(final int layoutId, final int paddingLeft, final int paddingRight, final LinearLayout.LayoutParams params) {
        final View button = View.inflate(this, layoutId, null);
        button.setOnClickListener(this);
        button.setPadding(paddingLeft, 0, paddingRight, 0);
        getRightContainer().addView(button, params);
        return button;
    }

    protected void setCenterViews(JSONArray jsonArray) {
        final int length = jsonArray.length();
        JSONObject jsonObject = null;
        String viewType = null;

        int visibility = -1;
        for (int i = 0; i < length; i++) {
            jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject != null) {
                viewType = jsonObject.optString(KEY_VIEW_TYPE);
                visibility = jsonObject.optInt(KEY_VISIBILITY, -1);
                if (VALUE_TAB_SELECTOR_VIEW.equals(viewType)) { // 切换的tab
                    if (visibility == 1) {
                        if (mTabSelectorView == null) {
                            mTabSelectorView = (TwoTabSelectorView) View.inflate(this, R.layout.actionbar_two_tab_selector, null);
                            mTabSelectorView.setResouce(R.color.web_actionbar_two_tab_text_color_selector, R.drawable.web_actionbar_two_tab_selector_bg,
                                    R.drawable.web_actionbar_two_tab_left_bg_selected, R.drawable.web_actionbar_two_tab_right_bg_selected,
                                    R.drawable.web_actionbar_two_tab_middle_bg_selected);
                            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                            mActionBar.addView(mTabSelectorView, params);
                        }
                        final JSONArray titleArray = jsonObject.optJSONArray("tabTitle");
                        if (titleArray != null && titleArray.length() == 2) {
                            mTabSelectorView.setTabTitles(titleArray.optString(0), titleArray.optString(1), null);
                        }
                        mTabSelectorView.switchTab(jsonObject.optInt("tabIndex"));
                        mTabSelectorView.setOnTabSelectedListener(this);
                    } else {
                        if (mTabSelectorView != null) {
                            mActionBar.removeView(mTabSelectorView);
                            mTabSelectorView = null;
                        }
                    }
                }
            }
        }
    }

    protected void setBottomViews(final JSONArray jsonArray) {
    }

    @Override
    public void onFirstTabSelected() {
        JsProxy.switchTab(mWebView, 0);
    }

    @Override
    public void onSecondTabSelected() {
        JsProxy.switchTab(mWebView, 1);
    }

    @Override
    public void onThirdTabSelected() {
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

    private void showCommonDialog(final JSONObject params) {
        final CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle(params.optString("title"));
        dialog.setMessage(params.optString("message"));
        dialog.setTag(params.optString("id"));
        final JSONArray buttonTexts = params.optJSONArray("btns");
        final int length = buttonTexts == null ? 0 : buttonTexts.length();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                dialog.addButton(buttonTexts.optString(i));
            }
        }
        dialog.setButtonClickListener(this);
        dialog.show();
    }

    @Override
    public void onDialogButtonClick(CommonDialog dialog, View view, int position) {
        if (isDestroy()) {
            return;
        }
        dialog.dismiss();
        final String buttonType = "popupBtn";
        JsProxy.clickPopupButton(mWebView, buttonType + position, dialog.getTag().toString());
    }

    private void showInputDialog(final JSONObject params) {
        final InputDialog dialog = new InputDialog(this);
        dialog.setTitle(params.optString("title"));
        dialog.setInputText(params.optString("inputText"));
        dialog.setMaxInputLength(params.optInt("inputMaxLength", 0));
        dialog.setInputHint(params.optString("placeholder"));
        final View.OnClickListener l = v -> {
            final int id = v.getId();
            if (id == R.id.ok) {
                JsProxy.clickButton(mWebView, "btnOk", dialog.getInputText());
            } else if (id == R.id.cancel) {
                JsProxy.clickButton(mWebView, "btnCancel", dialog.getInputText());
            }
            dialog.dismiss();
        };
        dialog.setCancelButton(params.optString("btnCancel"), l);
        dialog.setOkButton(params.optString("btnOk"), l);
        dialog.show();
    }

    private void setCoupon(JSONObject params) {
        int type  = params.optInt("type", -1);
        String code = params.optString("code");
        int value = params.optInt("value");
        if(type >= 0 && !TextUtils.isEmpty(code) && value >= 0) {
            Intent intent = new Intent();
            CouponInfo couponInfo = new CouponInfo(type, code, value);
            intent.putExtra(CommonConst.EXTRA_COUPON_INFO, couponInfo);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_OK);
        }
        finish();
    }

    private void setH5PayResult(JSONObject params) {
        int result  = params.optInt("result", -1);
        int payType = params.optInt("payType", -1);
        if(result >= 0 && payType >= 0) {
            Intent intent = new Intent();
            H5PayResult h5PayResult = new H5PayResult(result, payType);
            intent.putExtra(CommonConst.EXTRA_H5_PAY_RESULT, h5PayResult);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_OK);
        }
        finish();
    }

    private void showShareDialog(HashMap<String, ShareParams> map) {
        final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
                .getManager(IShareManager.class.getName());
        if (shareManager == null) {
            return;
        }
        shareManager.showShareDialog(this, map, this);
    }

    private class ScreenShotTask extends AsyncTask<Object, Object, String> {

        private HashMap<String, ShareParams> mParamsMap;

        private Bitmap mBmp;

        public ScreenShotTask(HashMap<String, ShareParams> map) {
            mParamsMap = map;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog();
            try {
                mBmp = WebScreenShotUtlis.getWebViewBitmap(getResources(), mWebView, mActionBar);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Object[] params) {
            WebScreenShotUtlis.clearAllScreenShots();
            String path = null;
            if(mBmp != null) {
                path = BitmapUtils.saveBitmap(mBmp, WebScreenShotUtlis.createScreenShotFilePath());
                mBmp.recycle();
                mBmp = null;
            }
            Runtime.getRuntime().gc();
            return path;
        }

        @Override
        protected void onPostExecute(String path) {
            if (isDestroy()) {
                return;
            }
            if (path != null) {
                for (Iterator<String> it = mParamsMap.keySet().iterator(); it.hasNext(); ) {
                    String key = it.next();
                    ShareParams param = mParamsMap.get(key);

                    ShareParams newParam;
                    if (param.shareType == ShareParams.TYPE_FILE) {
                        newParam = param;
                        newParam.putFileByPath(path);
                    } else {
                        newParam = ShareParams.createShareParams(param.title, param.msg, "", "");
                        newParam.putFileByPath(path);
                        mParamsMap.put(key, newParam);
                    }
                }
            }
            dismissLoadingDialog();
            showShareDialog(mParamsMap);
        }
    }

    private void setSignResult(JSONObject params) {
        boolean result = params.optBoolean("result", false);
        if(result) {
            Intent intent = new Intent();
            intent.putExtra(CommonConst.EXTRA_SIGN_RESULT, true);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private final class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (CommonConst.ACTION_LOGIN_SUCCESS.equals(action)) {
                WebUtils.setCookies(context, mUrl);
                JsProxy.loginSuccess(mWebView);
            } else if (CommonConst.ACTION_LOGOUT_SUCCESS.equals(action) || CommonConst.ACTION_UPDATE_ACCOUNT_INFO.equals(action)) {
                WebUtils.setCookies(context, mUrl);
            } else if (CommonConst.ACTION_BEACON_PROTOCAL_ERROR.equals(action)) {
                JsProxy.beaconOpenFail(mWebView, 1);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostCommentComplete(final PostCommentResultEvent event) {
        JsProxy.onPostCommentComplete(mWebView, event.success, event.feedId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteComment(final DeleteCommentEvent event) {
        JsProxy.onDeleteComment(mWebView, event.feedId);
    }
}