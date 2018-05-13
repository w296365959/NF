package com.sscf.investment.setting;

import BEC.E_ACCOUNT_RET;
import BEC.ModifyAccountInfoRsp;
import android.content.Intent;
import android.os.Bundle;
import com.sscf.investment.sdk.net.NetworkConst;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.sscf.investment.setting.widgt.BaseLogoutActivity;
import com.sscf.investment.setting.widgt.ErrorTipsView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.manager.AccountRequestManager;
import com.sscf.investment.setting.manager.CheckTextFormatUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.dengtacj.thoth.MapProtoLite;

/**
 * davidwei
 * 设置用户密码界面
 */
public final class SetUserPasswordActivity extends BaseLogoutActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback {
    private EditText mSetUserInfoPasswordValueEdit;
    private View mSwitchPasswordVisibleButton;
    private ErrorTipsView mErrorTipsView;
    private String mCellphone;
    private String mSmsCode;
    private int mFunctionType;
    private boolean mPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_set_user_password);

        final Intent intent = getIntent();
        mCellphone = intent.getStringExtra(SettingConst.EXTRA_CELLPHONE);
        mSmsCode = intent.getStringExtra(SettingConst.EXTRA_SMS_CODE);
        mFunctionType = intent.getIntExtra(SettingConst.EXTRA_FUNCTION_TYPE, -1);

        initView();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initView() {
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        final Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        mSetUserInfoPasswordValueEdit = (EditText) findViewById(R.id.setUserInfoPasswordValue);
        mSwitchPasswordVisibleButton = findViewById(R.id.switchPasswordVisibleButton);
        mSwitchPasswordVisibleButton.setOnClickListener(this);
        mErrorTipsView = (ErrorTipsView) findViewById(R.id.errorTips);

        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.account_set_password);

        switch (mFunctionType) {
            case SettingConst.FUNCTION_BIND_CELLPHONE:
                nextButton.setText(R.string.finish_bind);
                break;
            case SettingConst.FUNCTION_FORGET_PASSWORD:
                nextButton.setText(R.string.finish);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            case R.id.nextButton:
                clickNextButton();
                break;
            case R.id.switchPasswordVisibleButton:
                mPasswordVisible = !mPasswordVisible;
                final int selection = mSetUserInfoPasswordValueEdit.getSelectionEnd();
                if (mPasswordVisible) {
                    mSetUserInfoPasswordValueEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mSwitchPasswordVisibleButton.setBackgroundResource(R.drawable.password_visible);
                } else {
                    mSetUserInfoPasswordValueEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mSwitchPasswordVisibleButton.setBackgroundResource(R.drawable.password_invisible);
                }
                mSetUserInfoPasswordValueEdit.setSelection(selection);
                break;
            default:
                break;
        }
    }

    private void clickNextButton() {
        final String password = mSetUserInfoPasswordValueEdit.getText().toString();
        if (!CheckTextFormatUtil.checkPasswordFormat(mErrorTipsView, password)) {
            return;
        }

        if (!NetUtil.isNetWorkConnected(this)) {
            mErrorTipsView.showErrorTips(getString(R.string.error_tips_network_error));
            return;
        }

        switch (mFunctionType) {
            case SettingConst.FUNCTION_BIND_CELLPHONE:
                showLoadingDialog();
                final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
                AccountRequestManager.bindCellphoneinfo(accountInfo.ticket, accountInfo.id, mCellphone, password, mSmsCode, this);
                break;
            case SettingConst.FUNCTION_FORGET_PASSWORD:
                showLoadingDialog();
                AccountRequestManager.setForgottenPassword(mCellphone, mSmsCode, password, this);
                break;
            default:
                break;
        }
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
                    if (mFunctionType == SettingConst.FUNCTION_BIND_CELLPHONE) {// 绑定成功
                        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
                        final AccountInfoExt accountInfoExt = accountManager.getAccountInfoExt();
                        accountInfoExt.accountInfo.cellphone = mCellphone;
                        accountManager.updateLocalAccountInfo(accountInfoExt);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DengtaApplication.getApplication().showToast(R.string.bind_phone_number_success);
                                finish();
                            }
                        });
                        StatisticsUtil.reportAction(StatisticsConst.SETTING_BIND_CELLPHONE_SUCCESS);
                    } else if (mFunctionType == SettingConst.FUNCTION_FORGET_PASSWORD) { // 忘记密码设置成功
                        final ModifyAccountInfoRsp rsp = packet.read(NetworkConst.RSP, new ModifyAccountInfoRsp());
                        final AccountInfoExt accountInfoEntity = AccountRequestManager.parseAccountInfo(rsp.stAccountTicket.vtTicket, rsp.stAccountInfo, rsp.vtThirdLoginInfo);
                        DengtaApplication.getApplication().getAccountManager().saveAccountInfo(accountInfoEntity);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DengtaApplication.getApplication().showToast(R.string.set_password_success);
                                finish();
                            }
                        });
                    }
                    break;
                case E_ACCOUNT_RET.E_AR_PASSWORD_FORMAT_ERROR:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_password_format_invalid));
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