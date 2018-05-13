package com.sscf.investment.setting;

import BEC.E_ACCOUNT_RET;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.sscf.investment.setting.manager.AccountRequestManager;
import com.sscf.investment.setting.manager.CheckTextFormatUtil;
import com.sscf.investment.setting.widgt.BaseLogoutActivity;
import com.sscf.investment.setting.widgt.ErrorTipsView;
import com.sscf.investment.widget.ClearButtonEditTextListener;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.dengtacj.thoth.MapProtoLite;

/**
 * davidwei
 * 修改昵称的界面
 */
public final class ModifyNicknameActivity extends BaseLogoutActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback {
    private EditText mModifyNicknameEditText;
    private String mNickname;

    private ErrorTipsView mErrorTipsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_modify_nickname);
        initViews();
        StatisticsUtil.reportAction(StatisticsConst.SETTING_MODIFY_NICKNAME_DISPLAY);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.account_modify_nickname);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        TextView saveButton = (TextView) findViewById(R.id.actionbar_right_button);
        saveButton.setText(R.string.save);
        saveButton.setOnClickListener(this);
        mModifyNicknameEditText = (EditText) findViewById(R.id.modifyNicknameEditText);
        final Resources resources = getResources();
        final int NICKNAME_MIN_LENGTH = resources.getInteger(R.integer.nickname_min_length);
        final int NICKNAME_MAX_LENGTH = resources.getInteger(R.integer.nickname_max_length);
        mModifyNicknameEditText.setHint(getResources().getString(R.string.error_tips_nickname_length_error,
                NICKNAME_MIN_LENGTH, NICKNAME_MAX_LENGTH));

        final View modifyNicknameClearButton = findViewById(R.id.modifyNicknameClearButton);
        new ClearButtonEditTextListener(mModifyNicknameEditText, modifyNicknameClearButton);

        final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
        if (accountInfo == null) {
            finish();
            return;
        }
        mModifyNicknameEditText.setText(accountInfo.nickname);

        mErrorTipsView = (ErrorTipsView) findViewById(R.id.errorTips);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.actionbar_right_button:
                clickSaveButton();
                break;
            default:
                break;
        }
    }

    private void clickSaveButton() {
        final String nickname = mModifyNicknameEditText.getText().toString().trim();
        if (!CheckTextFormatUtil.checkNicknameFormat(mErrorTipsView, nickname)) {
            return;
        }

        final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
        if (nickname.equals(accountInfo.nickname)) { // 昵称没修改就直接修改成功
            DengtaApplication.getApplication().showToast(R.string.modify_nick_name_success);
            finish();
            return;
        }

        if (!NetUtil.isNetWorkConnected(this)) {
            mErrorTipsView.showErrorTips(R.string.error_tips_network_error);
            return;
        }

        showLoadingDialog();

        mNickname = nickname;
        AccountRequestManager.modifyNickname(accountInfo.ticket, accountInfo.id, nickname, this);
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
                case 0: // 修改成功
                    final AccountInfoExt accountInfoExt = DengtaApplication.getApplication().getAccountManager().getAccountInfoExt();
                    accountInfoExt.accountInfo.nickname = mNickname;
                    DengtaApplication.getApplication().getAccountManager().updateLocalAccountInfo(accountInfoExt);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.modify_nick_name_success);
                            finish();
                        }
                    });
                    StatisticsUtil.reportAction(StatisticsConst.SETTING_MODIFY_NICKNAME_SUCCESS);
                    break;
                case E_ACCOUNT_RET.E_AR_USERNAME_USED:
                    mErrorTipsView.showErrorTips(R.string.error_tips_nickname_occupied);
                    break;
                case E_ACCOUNT_RET.E_AR_USERNAME_FORMAT_ERROR:
                    mErrorTipsView.showErrorTips(R.string.error_tips_nickname_format_error);
                    break;
                case E_ACCOUNT_RET.E_AR_USERNAME_SENSTIVE:
                    mErrorTipsView.showErrorTips(R.string.error_tips_nickname_error);
                    break;
                case E_ACCOUNT_RET.E_AR_USERNAME_LENGTH_ERROR:
                    mErrorTipsView.showErrorTips(R.string.error_tips_nickname_format_error);
                    final Resources resources = DengtaApplication.getApplication().getResources();
                    final int NICKNAME_MIN_LENGTH = resources.getInteger(R.integer.nickname_min_length);
                    final int NICKNAME_MAX_LENGTH = resources.getInteger(R.integer.nickname_max_length);
                    mErrorTipsView.showErrorTips(DengtaApplication.getApplication().getString(R.string.error_tips_nickname_length_error,
                                NICKNAME_MIN_LENGTH, NICKNAME_MAX_LENGTH));
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
            mErrorTipsView.showErrorTips(R.string.error_tips_request_failed);
        }

        dismissLoadingDialog();
    }

}