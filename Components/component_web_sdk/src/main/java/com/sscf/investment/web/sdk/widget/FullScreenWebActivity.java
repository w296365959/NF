package com.sscf.investment.web.sdk.widget;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chenenyu.router.annotation.Route;
import com.sscf.investment.sdk.utils.BitmapUtils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NumberUtil;
import com.sscf.investment.web.sdk.R;
import com.sscf.investment.web.sdk.utils.WebUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by davidwei on 2017/11/30
 * 全屏的webview
 */
@Route("FullScreenWebActivity")
public final class FullScreenWebActivity extends BaseWebActivity {
    private static final String TAG = FullScreenWebActivity.class.getSimpleName();

    private View mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent();

        if (TextUtils.isEmpty(mUrl)) {
            finish();
            return;
        }

        try {
            setContentView(R.layout.activity_web_fullscreen);
        } catch (Exception e) {
            e.printStackTrace(); // 处理webview的java.lang.NullPointerException
            finish();
            return;
        }

        findViews();
        initWebView();

        WebUtils.setCookies(this, mUrl);
        final String url = mSupportTheme ? WebUtils.addThemeParam(mUrl) : mUrl;
        mWebView.loadUrl(url);
        DtLog.d(TAG, "loadUrl url : " + url);

        final Resources res = getResources();
        mBackButton.setImageBitmap(BitmapUtils.modifyBitmapColor(res, R.drawable.back_normal, 0xff000000));
    }

    private void findViews() {
        mActionBar = findViewById(R.id.actionBar);
        mBackButton = (ImageView) findViewById(R.id.actionbar_back_button);
        mBackButton.setOnClickListener(this);
        mCloseButton = (TextView) findViewById(R.id.actionbar_close_button);
        mCloseButton.setOnClickListener(this);
        mTitleView = (TextView) findViewById(R.id.actionbar_title);
        mRightContainer = (LinearLayout) findViewById(R.id.rightContainer);
    }

    @Override
    protected void setLeftViews(JSONArray jsonArray) {
        final int length = jsonArray.length();
        JSONObject jsonObject = null;
        String viewType = null;

        final Resources res = getResources();
        int visibility;
        int color;
        for (int i = 0; i < length; i++) {
            jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject != null) {
                viewType = jsonObject.optString(KEY_VIEW_TYPE);
                visibility = jsonObject.optInt(KEY_VISIBILITY, -1);
                color = parseColor(jsonObject.optString(KEY_COLOR, ""));
                if (VALUE_BACK_BUTTON.equals(viewType)) { // 返回按钮
                    if (visibility == 1) {
                        mBackButton.setVisibility(View.VISIBLE);
                        mBackButton.setImageBitmap(BitmapUtils.modifyBitmapColor(res, R.drawable.back_normal, color));
                    } else {
                        mBackButton.setVisibility(View.INVISIBLE);
                    }
                } else if (VALUE_CLOSE_BUTTON.equals(viewType)) { // 关闭按钮
                    if (visibility == 1) {
                        mCloseButton.setVisibility(View.VISIBLE);
                        mCloseButton.setTextColor(color);
                    } else {
                        mCloseButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    @Override
    protected void setRightViews(JSONArray jsonArray) {
        JSONObject jsonObject = null;
        String viewType = null;

        final LinearLayout rightContainer = mRightContainer;
        rightContainer.removeAllViews();

        final int length = jsonArray.length();

        int visibility = -1;
        final int padding = getResources().getDimensionPixelSize(R.dimen.actionbar_margin);
        final int paddingHalf = padding / 2;
        int paddingRight;
        int color;

        final Resources res = getResources();

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
                visibility = jsonObject.optInt(KEY_VISIBILITY, -1);
                if (visibility != 1) {
                    continue;
                }

                viewType = jsonObject.optString(KEY_VIEW_TYPE);
                color = parseColor(jsonObject.optString(KEY_COLOR, ""));
                if (VALUE_SHARE_BUTTON.equals(viewType)) { // 分享按钮
                    final ImageView button = addRightButton(paddingHalf, paddingRight, params);
                    button.setId(R.id.actionbar_share);
                    button.setImageBitmap(BitmapUtils.modifyBitmapColor(res, R.drawable.actionbar_share_normal, color));
                } else if (VALUE_REFRESH_BUTTON.equals(viewType)) { // 刷新按钮
//                    final View refreshButton = addRightButton(R.layout.actionbar_refresh_button, paddingHalf, paddingRight, params);
//                    if (mRefreshButton != null) {
//                        mRefreshButton.stopLoadingAnim();
//                    }
//                    mRefreshButton = (RefreshButton) refreshButton.findViewById(R.id.refresh_button);
                } else if(VALUE_FAQ_BUTTON.equals(viewType)) { // faq的button
//                    addRightButton(R.layout.actionbar_faq_button, paddingHalf, paddingRight, params);
                } else if (VALUE_FILTER_BUTTON.equals(viewType)) { // 条件选股button
//                    addRightButton(R.layout.actionbar_filter_button, paddingHalf, paddingRight, params);
                } else if (VALUE_RIGHT_TEXT_BUTTON.equals(viewType)) {
//                    final TextView rightTextButton = (TextView) View.inflate(this, R.layout.actionbar_right_text_button, null);
//                    rightTextButton.setText(jsonObject.optString("word"));
//                    rightTextButton.setOnClickListener(this);
//                    rightTextButton.setPadding(paddingHalf, 0, paddingRight, 0);
//                    rightContainer.addView(rightTextButton, params);
                } else if (VALUE_BROTHER_SCHOOL_VIEW.equals(viewType)) { // 表哥学堂
//                    addRightButton(R.layout.actionbar_school_button, paddingHalf, paddingRight, params);
                }
            }
        }
    }

    private ImageView addRightButton(final int paddingLeft, final int paddingRight, final LinearLayout.LayoutParams params) {
        final ImageView button = new ImageView(this);
        button.setOnClickListener(this);
        button.setPadding(paddingLeft, 0, paddingRight, 0);
        mRightContainer.addView(button, params);
        return button;
    }

    @Override
    protected void setCenterViews(JSONArray jsonArray) {
        final int length = jsonArray.length();
        JSONObject jsonObject = null;
        String viewType;

        int visibility;
        int color;
        for (int i = 0; i < length; i++) {
            jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject != null) {
                viewType = jsonObject.optString(KEY_VIEW_TYPE);
                visibility = jsonObject.optInt(KEY_VISIBILITY, -1);
                color = parseColor(jsonObject.optString(KEY_COLOR, ""));
                if (VALUE_TITLE_BAR_VIEW.equals(viewType)) { // titlebar
                    if (visibility == 1) {
                        mActionBar.setVisibility(View.VISIBLE);
                        mActionBar.setBackgroundColor(color);
                    } else {
                        mActionBar.setVisibility(View.INVISIBLE);
                    }
                } else if (VALUE_TITLE_TXT_VIEW.equals(viewType)) { // titletxt
                    if (visibility == 1) {
                        mTitleView.setVisibility(View.VISIBLE);
                        mTitleView.setTextColor(color);
                    } else {
                        mTitleView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    private static int parseColor(String color) {
        if (!TextUtils.isEmpty(color)) {
            final String[] colorRGBA = color.split(",");
            if (colorRGBA != null && colorRGBA.length == 4) {
                final int red = NumberUtil.parseInt(colorRGBA[0], 0);
                final int green = NumberUtil.parseInt(colorRGBA[1], 0);
                final int blue = NumberUtil.parseInt(colorRGBA[2], 0);
                final int alpha = (int) Math.round(255 * NumberUtil.parseDouble(colorRGBA[3], 0d));
                return Color.argb(alpha, red, green, blue);
            }
        }
        return 0;
    }
}