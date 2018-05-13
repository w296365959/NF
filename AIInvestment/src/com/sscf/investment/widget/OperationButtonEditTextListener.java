package com.sscf.investment.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.WebBeaconJump;

/**
 * Created by yorkeehuang on 2017/5/26.
 */

public final class OperationButtonEditTextListener implements TextWatcher, View.OnClickListener, View.OnFocusChangeListener {
    private BaseFragmentActivity mActivity;
    private final EditText mEditText;
    private final View mOperationButton;

    public OperationButtonEditTextListener(BaseFragmentActivity activity, EditText editText, View operationButton) {
        mActivity = activity;
        mEditText = editText;
        mOperationButton = operationButton;
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);
        mOperationButton.setOnClickListener(this);
        refreshOperationBtn();
    }

    private void refreshOperationBtn() {
        int res = mEditText.getText().length() > 0 ?
                R.drawable.input_clear_button : R.drawable.search_scan_icon;
        mOperationButton.setBackgroundResource(res);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.operation:
                if (mEditText.getText().length() > 0) {
                    mEditText.setText("");
                } else if (mActivity != null && !mActivity.isDestroy()) {
                    WebBeaconJump.showImportGallery(mActivity);
                    StatisticsUtil.reportAction(StatisticsConst.SEARCH_CLICK_IMPORT_STOCK);
                }
                break;
            default:
                break;
        }
        if (mEditText.getText().length() > 0) {
            mEditText.setText("");
        } else {

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        refreshOperationBtn();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            refreshOperationBtn();
        } else {
            mOperationButton.setBackgroundResource(R.drawable.search_scan_icon);
        }
    }
}
