package com.sscf.investment.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chenenyu.router.annotation.Route;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.manager.AccountRequestManager;
import com.sscf.investment.setting.manager.CheckTextFormatUtil;
import com.sscf.investment.setting.widgt.ErrorTipsView;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.ClearButtonEditTextListener;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.dengtacj.thoth.MapProtoLite;
import BEC.E_ACCOUNT_RET;

/**
 * davidwei
 * 修改密码界面
 */
@Route("ModifyPasswordActivity")
public class ModifyPasswordActivity extends BaseFragmentActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback {
    private ErrorTipsView mErrorTipsView;

    private EditText mOldPasswordView;
    private EditText mNewPasswordView;
    private View mSwitchPasswordVisibleButton;

    private boolean mPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_modify_password);
        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.modify_password);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        findViewById(R.id.nextButton).setOnClickListener(this);

        mErrorTipsView = (ErrorTipsView) findViewById(R.id.errorTips);

        mOldPasswordView = (EditText) findViewById(R.id.verifyOldPassword);
        final View clearButton = findViewById(R.id.verifyOldPasswordClearButton);
        new ClearButtonEditTextListener(mOldPasswordView, clearButton);

        mNewPasswordView = (EditText) findViewById(R.id.inputNewPassword);
        mSwitchPasswordVisibleButton = findViewById(R.id.switchPasswordVisibleButton);
        mSwitchPasswordVisibleButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.nextButton:
                clickNextButton();
                break;
            case R.id.switchPasswordVisibleButton:
                mPasswordVisible = !mPasswordVisible;
                final int selection = mNewPasswordView.getSelectionEnd();
                if (mPasswordVisible) {
                    mNewPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mSwitchPasswordVisibleButton.setBackgroundResource(R.drawable.password_visible);
                } else {
                    mNewPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mSwitchPasswordVisibleButton.setBackgroundResource(R.drawable.password_invisible);
                }
                mNewPasswordView.setSelection(selection);
                break;
            default:
                break;
        }
    }

    public void clickNextButton() {
        final String oldPassword = mOldPasswordView.getText().toString();
        if (!CheckTextFormatUtil.checkPasswordFormat(mErrorTipsView, oldPassword)) {
            return;
        }

        final String newPassword = mNewPasswordView.getText().toString();
        if (!CheckTextFormatUtil.checkPasswordFormat(mErrorTipsView, newPassword)) {
            return;
        }

        if (!NetUtil.isNetWorkConnected(this)) {
            mErrorTipsView.showErrorTips(R.string.error_tips_network_error);
            return;
        }

        showLoadingDialog();

        final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
        AccountRequestManager.modifyPassword(accountInfo.ticket, accountInfo.id, oldPassword, newPassword, this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success) {
            if (data.getEntityType() != EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO) {
                return;
            }
            final Object entity = data.getEntity();
            if (entity == null || !(entity instanceof MapProtoLite)) {
                return;
            }

            final MapProtoLite packet = (MapProtoLite) entity;
            final int code = packet.read("", -1);
            switch (code) {
                case 0:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.set_password_success);
                            finish();
                        }
                    });
                    break;
                case E_ACCOUNT_RET.E_AR_PASSWORD_ERROR:
                case E_ACCOUNT_RET.E_AR_PASSWORD_FORMAT_ERROR:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_password_error));
                    break;
                // ticket验证不过，删除用户信息，重新登录
                case E_ACCOUNT_RET.E_AR_TICKET_LENGTH_ERROR:
                case E_ACCOUNT_RET.E_AR_TICKET_EXPIRED:
                case E_ACCOUNT_RET.E_AR_TICKET_SIGN_ERROR:
                    DengtaApplication.getApplication().getAccountManager().removeAccountInfo();

                    startActivity(new Intent(this, LoginActivity.class));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.ticket_expired);
                            finish();
                        }
                    });
                    break;
                default:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_error_code, code));
                    break;
            }
        } else {
            mErrorTipsView.showErrorTips(getString(R.string.error_tips_request_failed));
        }

        dismissLoadingDialog();
    }
}