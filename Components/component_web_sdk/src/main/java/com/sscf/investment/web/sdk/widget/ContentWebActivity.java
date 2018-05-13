package com.sscf.investment.web.sdk.widget;

import android.os.Bundle;
import android.text.TextUtils;

import com.chenenyu.router.annotation.Route;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.web.sdk.R;
import com.sscf.investment.web.sdk.WebConst;

/**
 * Created by yorkeehuang on 2017/10/24.
 */
@Route("ContentWebActivity")
public class ContentWebActivity extends BaseWebActivity {

    private static final String TAG = ContentWebActivity.class.getSimpleName();
    private String mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent();

        if (TextUtils.isEmpty(mContent)) {
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

        mWebView.getSettings().setDefaultTextEncodingName("UTF-8") ;
        mWebView.loadData(mContent, "text/html", "UTF-8") ;
        DtLog.d(TAG, "loadData : " + mContent);
    }

    protected void handleIntent() {
        super.handleIntent();
        mContent = getIntent().getStringExtra(WebConst.CONTENT);
    }
}
