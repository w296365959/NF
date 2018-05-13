package com.sscf.investment.setting;

import BEC.E_ACCOUNT_RET;
import BEC.FinishRegisterAccountRsp;
import android.os.Bundle;
import com.sscf.investment.sdk.net.NetworkConst;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.sscf.investment.setting.widgt.ErrorTipsView;
import com.sscf.investment.sdk.utils.DtLog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.setting.manager.AccountRequestManager;
import com.sscf.investment.setting.manager.CheckTextFormatUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.dengtacj.thoth.MapProtoLite;

/**
 * davidwei
 * 注册流程里的设置用户信息界面
 */
public final class Register2SetUserInfoActivity extends BaseFragmentActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback {
    private static final String TAG = Register2SetUserInfoActivity.class.getSimpleName();
    private EditText mSetUserInfoPasswordValueEdit;
    private View mSwitchPasswordVisibleButton;
    private ErrorTipsView mErrorTipsView;

    private String mCellphone;
    private boolean mPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_register2_set_user_info);
        initView();
        mCellphone = getIntent().getStringExtra("cellphone");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initView() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.account_set_password);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        findViewById(R.id.finishRegister).setOnClickListener(this);
        mSetUserInfoPasswordValueEdit = (EditText) findViewById(R.id.setUserInfoPasswordValue);
        mSwitchPasswordVisibleButton = findViewById(R.id.switchPasswordVisibleButton);
        mSwitchPasswordVisibleButton.setOnClickListener(this);
        mErrorTipsView = (ErrorTipsView) findViewById(R.id.errorTips);
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
            case R.id.finishRegister:
                clickFinishRegister();
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

    private void clickFinishRegister() {
        final String password = mSetUserInfoPasswordValueEdit.getText().toString();
        if (!CheckTextFormatUtil.checkPasswordFormat(mErrorTipsView, password)) {
            return;
        }

        if (!NetUtil.isNetWorkConnected(this)) {
            mErrorTipsView.showErrorTips(getString(R.string.error_tips_network_error));
            return;
        }

        showLoadingDialog();

        AccountRequestManager.finishRegister(mCellphone, password, this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success) {
            DtLog.d(TAG, "callback data.getEntityType() : " + data.getEntityType());
            DtLog.d(TAG, "callback data.getEntity() :" + data.getEntity());
            if (data.getEntityType() != EntityObject.ET_ACCOUNT_FINISH_RESIGTER) {
                // 如果不是注册完成的请求就直接退出
                return;
            }

            final Object entity = data.getEntity();
            if (entity == null || !(entity instanceof MapProtoLite)) {
                return;
            }

            final MapProtoLite packet = (MapProtoLite) entity;
            final int code = packet.read("", -1);
            final FinishRegisterAccountRsp rsp = packet.read(NetworkConst.RSP, new FinishRegisterAccountRsp());

            DtLog.d(TAG, "callback code :" + code);
            switch (code) {
                case 0:// 注册成功
                    final AccountInfoExt accountInfoExt = new AccountInfoExt();
                    final AccountInfoEntity accountInfoEntity = new AccountInfoEntity();
                    accountInfoExt.accountInfo = accountInfoEntity;
                    accountInfoEntity.id = rsp.stAccountInfo.iAccountId;
                    accountInfoEntity.ticket = rsp.stAccountTicket.vtTicket;
                    accountInfoEntity.nickname = rsp.stAccountInfo.sUserName;
                    accountInfoEntity.cellphone = rsp.stAccountInfo.sPhoneNum;
                    DengtaApplication.getApplication().getAccountManager().saveAccountInfo(accountInfoExt);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.register_success);
                            finish();
                        }
                    });
                    break;
                case E_ACCOUNT_RET.E_AR_USERNAME_USED:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_nickname_occupied));
                    break;
                case E_ACCOUNT_RET.E_AR_USERNAME_FORMAT_ERROR:
                    mErrorTipsView.showErrorTips(R.string.error_tips_nickname_format_error);
                    break;
                case E_ACCOUNT_RET.E_AR_USERNAME_SENSTIVE:
                    mErrorTipsView.showErrorTips(R.string.error_tips_nickname_error);
                    break;
                case E_ACCOUNT_RET.E_AR_PHONE_EMAIL_USED:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_cellphone_occupied));
                    break;
                case E_ACCOUNT_RET.E_AR_FAILED_REGISTER:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_register_failed));
                    break;
                case E_ACCOUNT_RET.E_AR_PASSWORD_WEAK:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_password_weak));
                    break;
                case E_ACCOUNT_RET.E_AR_PASSWORD_DIFF:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_password_repead_error));
                    break;
                case E_ACCOUNT_RET.E_AR_PASSWORD_EMPTY:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_password_empty));
                    break;
                case E_ACCOUNT_RET.E_AR_PASSWORD_FORMAT_ERROR:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_password_format_invalid));
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