package com.sscf.investment.web.sdk.widget;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import com.chenenyu.router.annotation.Route;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.web.sdk.JsProxy;
import com.sscf.investment.web.sdk.R;
import com.sscf.investment.web.sdk.WebConst;
import com.sscf.investment.web.sdk.utils.WebUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by davidwei on 2016/01/04.
 *
 */
@Route("IntelligentAnswerWebActivity")
public final class IntelligentAnswerWebActivity extends BaseWebActivity implements DtWebView.OnTouchDownListener {

    private static final String VALUE_INPUT_LAYOUT = "inputLayout";

    private IntelligentAnswerInputLayout mInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent();

        if (TextUtils.isEmpty(mUrl)) {
            finish();
            return;
        }

        try {
            setContentView(R.layout.activity_web_intelligent_answer);
        } catch (Exception e) {
            e.printStackTrace(); // 处理webview的java.lang.NullPointerException
            finish();
            return;
        }

        initViews();

        WebUtils.setCookies(this, mUrl);
        final String url = WebUtils.addThemeParam(mUrl);
        mWebView.loadUrl(url);
    }

    @Override
    protected void initViews() {
        super.initViews();

        mWebView.setOnTouchDownListener(this);
        mInputLayout = (IntelligentAnswerInputLayout) findViewById(R.id.inputLayout);
        mInputLayout.setWebView(mWebView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mInputLayout.closeInputLayout();
    }

    protected void setBottomViews(final JSONArray jsonArray) {
        final int length = jsonArray.length();
        JSONObject jsonObject = null;
        String viewType = null;

        int visibility = -1;
        for (int i = 0; i < length; i++) {
            jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject != null) {
                viewType = jsonObject.optString(KEY_VIEW_TYPE);
                visibility = jsonObject.optInt(KEY_VISIBILITY, -1);
                if (VALUE_INPUT_LAYOUT.equals(viewType)) { // 输入框
                    mInputLayout.setVisibility(visibility == 1 ? View.VISIBLE : View.INVISIBLE);
                    final Intent intent = getIntent();
                    final String secName = intent.getStringExtra(CommonConst.KEY_SEC_NAME);
                    final String dtCode = intent.getStringExtra(CommonConst.KEY_SEC_CODE);
                    if (!TextUtils.isEmpty(secName)) {
                        JsProxy.entryWords(mWebView, secName, dtCode);
                    }
                    mInputLayout.switchInputState(IntelligentAnswerInputLayout.INPUT_STATE_DEFAULT);
                }
            }
        }
    }

    @Override
    public void onTouchDown() {
        mInputLayout.closeInputLayout();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case WebConst.MSG_SET_INPUT_VALUE:
                mInputLayout.setInputText((String) msg.obj);
                break;
            default:
                return super.handleMessage(msg);
        }
        return true;
    }

    @Override
    public boolean isShakeEnable() {
        return false;
    }
}