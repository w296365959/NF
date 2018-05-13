package com.sscf.investment.privilege;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chenenyu.router.annotation.Route;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.privilege.manager.AccumulatePointsRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.NetworkConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.ClearButtonEditTextListener;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.dengtacj.thoth.MapProtoLite;
import BEC.AccuPointErrCode;
import BEC.CommitAccuPointCodeRsp;

/**
 * Created by davidwei on 2016/12/21
 */
@Route("CommitInviteCodeActivity")
public final class CommitInviteCodeActivity extends BaseFragmentActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback,
        TextWatcher, DialogInterface.OnDismissListener {
    private EditText mEditText;
    private TextView mRightButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privilege_exchange);
        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.input_invite_code);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        mRightButton = (TextView) findViewById(R.id.actionbar_right_button);
        mRightButton.setText(R.string.commit);
        mRightButton.setOnClickListener(this);
        mRightButton.setEnabled(false);

        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.setHint(R.string.commit_invite_code_hint);
        final View clearButton = findViewById(R.id.clearInputButton);
        new ClearButtonEditTextListener(mEditText, clearButton);
        mEditText.addTextChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceUtil.hideInputMethod(mEditText);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.actionbar_right_button:
                requestData();
                StatisticsUtil.reportAction(StatisticsConst.COMMIT_INVIT_CODE_CLICK_COMMIT);
                break;
            default:
                break;
        }
    }

    private void requestData() {
        final String code = getCode();
        if (!TextUtils.isEmpty(code)) {
            showLoadingDialog();
            AccumulatePointsRequestManager.commitInviteCodeRequest(code, this);
        }
    }

    @Override
    public void callback(final boolean success, final EntityObject data) {
        dismissLoadingDialog();
        if (success) {
            final Object obj = data.getEntity();
            if (obj != null) {
                final MapProtoLite packet = (MapProtoLite) obj;
                final int res = packet.read("", -1);
                switch (res) {
                    case AccuPointErrCode.E_ACCU_POINT_SUCC:
                        final CommitAccuPointCodeRsp rsp = packet.read(NetworkConst.RSP, new CommitAccuPointCodeRsp());
                        final String text = getResources().getString(R.string.commit_invite_code_success_tips,
                                String.valueOf(rsp.iGetPoints));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showSuccessDialog(text);
                            }
                        });
                        StatisticsUtil.reportAction(StatisticsConst.COMMIT_INVIT_CODE_COMMIT_INVITE_CODE_SUCCESS);
                        break;
                    case AccuPointErrCode.E_ACCU_POINT_COMMIT_SELF_CODE:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DengtaApplication.getApplication().showToast(R.string.commit_invite_code_failed_tips_1);
                            }
                        });
                        break;
                    case AccuPointErrCode.E_ACCU_POINT_REPEATED_COMMIT:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DengtaApplication.getApplication().showToast(R.string.commit_invite_code_failed_tips_2);
                            }
                        });
                        break;
                    case AccuPointErrCode.E_ACCU_POINT_OLD_USER_COMMIT_CODE:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DengtaApplication.getApplication().showToast(R.string.commit_invite_code_failed_tips_3);
                            }
                        });
                        break;
                    case AccuPointErrCode.E_ACCU_POINT_INVALID_CODE:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DengtaApplication.getApplication().showToast(R.string.commit_invite_code_failed_tips_4);
                            }
                        });
                        break;
                    default:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DengtaApplication.getApplication().showToast(R.string.commit_invite_code_failed_tips);
                            }
                        });
                        break;
                }
                return;
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
            }
        });
    }

    private void showSuccessDialog(String msg) {
        final CommonDialog dialog = new CommonDialog(this);
        dialog.setCanCancelOnTouchOutside(false);
        dialog.setTitle(R.string.dialog_title_tips);
        dialog.setMessage(msg);
        dialog.addButton(R.string.ok);
        dialog.setOnDismissListener(this);
        dialog.show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mRightButton.setEnabled(!TextUtils.isEmpty(getCode()));
    }

    private String getCode() {
        return mEditText.getText().toString().trim();
    }
}
