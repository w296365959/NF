package com.sscf.investment.web.sdk.widget;

import android.os.Bundle;
import android.text.TextUtils;
import com.chenenyu.router.annotation.Route;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.web.sdk.R;
import com.sscf.investment.web.sdk.utils.WebUtils;

/**
 * Created by davidwei on 2016/01/04.
 * 通用的webactivity
 */
@Route("CommonWebActivity")
public class CommonWebActivity extends BaseWebActivity {
    private static final String TAG = CommonWebActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent();

        if (TextUtils.isEmpty(mUrl)) {
            finish();
            return;
        }

        try {
            setContentView(R.layout.activity_common_web);
        } catch (Exception e) {
            e.printStackTrace(); // 处理webview的java.lang.NullPointerException
            finish();
            return;
        }

        initViews();

        WebUtils.setCookies(this, mUrl);
        final String url = mSupportTheme ? WebUtils.addThemeParam(mUrl) : mUrl;
        mWebView.loadUrl(url);
        DtLog.d(TAG, "loadUrl url : " + url);
    }
}