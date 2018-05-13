package com.sscf.investment.setting;

import BEC.E_ACCOUNT_RET;
import BEC.LoginRsp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.NetworkConst;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.sscf.investment.setting.manager.AccountRequestManager;
import com.sscf.investment.setting.manager.CheckTextFormatUtil;
import com.sscf.investment.setting.widgt.ErrorTipsView;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.dengtacj.thoth.MapProtoLite;

/**
 * davidwei
 * 修改手机号界面
 */
public class ModifyCellphone1VerifyPasswordActivity extends BaseFragmentActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback {
    private EditText mPasswordEdit;
    private String mPassword;
    private ErrorTipsView mErrorTipsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_modify_cellphone_1_verify_password);
        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.account_modify_cellphone);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        mPasswordEdit = (EditText) findViewById(R.id.verifyPassword);
        findViewById(R.id.nextButton).setOnClickListener(this);

        mErrorTipsView = (ErrorTipsView) findViewById(R.id.errorTips);
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
            default:
                break;
        }
    }

    public void clickNextButton() {
        final String password = mPasswordEdit.getText().toString();
        if (!CheckTextFormatUtil.checkPasswordFormat(mErrorTipsView, password)) {
            return;
        }

        if (!NetUtil.isNetWorkConnected(this)) {
            mErrorTipsView.showErrorTips(R.string.error_tips_network_error);
            return;
        }

        showLoadingDialog();

        final String cellphone = DengtaApplication.getApplication().getAccountManager().getAccountInfo().cellphone;
        mPassword = password;
        AccountRequestManager.login(cellphone, password, this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success) {
            if (data.getEntityType() != EntityObject.ET_ACCOUNT_LOGIN) {
                return;
            }
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof MapProtoLite) {
                final MapProtoLite packet = (MapProtoLite) entity;
                final int code = packet.read("", -1);
                switch (code) {
                    case E_ACCOUNT_RET.E_AR_SUCC: // 密码验证成功
                        // 后台蛋疼的逻辑，验证密码还要更新ticket
                        final LoginRsp rsp = packet.read(NetworkConst.RSP, new LoginRsp());
                        final AccountInfoExt accountInfoExt = DengtaApplication.getApplication().getAccountManager().getAccountInfoExt();
                        accountInfoExt.accountInfo.ticket = rsp.stAccountTicket.vtTicket;
                        DengtaApplication.getApplication().getAccountManager().updateLocalAccountInfo(accountInfoExt);

                        final Intent intent = new Intent(ModifyCellphone1VerifyPasswordActivity.this, VerifySmsCodeActivity.class);
                        intent.putExtra(SettingConst.EXTRA_FUNCTION_TYPE, SettingConst.FUNCTION_MODIFY_CELLPHONE);
                        intent.putExtra(SettingConst.EXTRA_PASSWORD, mPassword);

                        startActivity(intent);
                        finish();
                        break;

                    case E_ACCOUNT_RET.E_AR_PASSWORD_ERROR:
                        mErrorTipsView.showErrorTips(getString(R.string.error_tips_password_error));
                        break;

                    default:
                        mErrorTipsView.showErrorTips(getString(R.string.error_tips_error_code, code));
                        break;
                }
            }
        } else {
            mErrorTipsView.showErrorTips(getString(R.string.error_tips_request_failed));
        }

        dismissLoadingDialog();
    }
}