package com.sscf.investment.scan;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;

public final class ScanStringResultActivity extends BaseFragmentActivity implements View.OnClickListener {
    private String mResult;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_scan_string_result);
        mResult = getIntent().getStringExtra("result");
        if (TextUtils.isEmpty(mResult)) {
            finish();
            return;
        }

        initViews();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.setting_scan);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        findViewById(R.id.duplicateButton).setOnClickListener(this);

        ((TextView) findViewById(R.id.resultContent)).setText(mResult);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.duplicateButton:
                DeviceUtil.putToSystemClipboard(this, mResult);
                DengtaApplication.getApplication().showToast(R.string.scan_string_duplicate_tips);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isShakeEnable() {
        return false;
    }
}