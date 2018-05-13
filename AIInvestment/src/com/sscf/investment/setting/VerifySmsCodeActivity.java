package com.sscf.investment.setting;

import BEC.E_ACCOUNT_RET;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chenenyu.router.annotation.Route;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.sscf.investment.setting.manager.AccountRequestManager;
import com.sscf.investment.setting.manager.CheckTextFormatUtil;
import com.sscf.investment.setting.widgt.BaseLogoutActivity;
import com.sscf.investment.setting.widgt.ErrorTipsView;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.widget.ClearButtonEditTextListener;
import com.sscf.investment.widget.PhoneNumberEditText;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.dengtacj.thoth.MapProtoLite;

/**
 * davidwei
 * 短信验证码界面，更换手机号，绑定手机号，忘记密码的需要经过此步骤
 */
@Route("VerifySmsCodeActivity")
public final class VerifySmsCodeActivity extends BaseLogoutActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback, Handler.Callback {
    private Button mNextButton;
    private PhoneNumberEditText mCellphoneEdit;
    private EditText mSmsCodeEdit;
    private Button mGetVerifiedCodeButton;
    private ErrorTipsView mErrorTipsView;

    private String mCellphone;
    private String mSmsCode;
    private int mFunctionType;

    private BroadcastReceiver mSmsReceiver;

    private Handler mMainHandler;
    private int mCountDown = 60;

    private static final int MSG_SET_GET_VERIFIED_CODE_BUTTON_DISABLE = 1;
    private static final int MSG_SET_GET_VERIFIED_CODE_BUTTON_ENABLE = 2;
    private static final int MSG_GET_VERIFIED_CODE_BUTTON_COUNT_DOWN = 3;

    // TODO 没找到常量
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_verify_sms_code);
        initViews();
        mMainHandler = new Handler(this);
        registerSmsReceiver();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        TextView titleView = ((TextView) findViewById(R.id.actionbar_title));
        mCellphoneEdit = (PhoneNumberEditText) findViewById(R.id.cellphone);
        mSmsCodeEdit = (EditText) findViewById(R.id.smsCode);
        mGetVerifiedCodeButton = (Button) findViewById(R.id.getVerifiedCode);
        mNextButton = (Button) findViewById(R.id.nextButton);

        final View cellphoneClearButton = findViewById(R.id.cellphoneClearButton);
        new ClearButtonEditTextListener(mCellphoneEdit, cellphoneClearButton);
        final View verifiedCodeClearButton = findViewById(R.id.verifiedCodeClearButton);
        new ClearButtonEditTextListener(mSmsCodeEdit, verifiedCodeClearButton);

        final Intent intent = getIntent();
        final boolean showClose = intent.getBooleanExtra(CommonConst.EXTRA_SHOW_CLOSE, false);
        final View backButton = findViewById(R.id.actionbar_back_button);
        backButton.setOnClickListener(this);
        backButton.setVisibility(showClose ? View.INVISIBLE : View.VISIBLE);
        final View closeButton = findViewById(R.id.actionbar_close_button);
        closeButton.setOnClickListener(this);
        closeButton.setVisibility(showClose ? View.VISIBLE : View.INVISIBLE);

        mNextButton.setOnClickListener(this);
        mGetVerifiedCodeButton.setOnClickListener(this);

        final int functionType = intent.getIntExtra(SettingConst.EXTRA_FUNCTION_TYPE, 0);
        mFunctionType = functionType;
        switch (functionType) {
            case SettingConst.FUNCTION_MODIFY_CELLPHONE:
                titleView.setText(R.string.account_modify_cellphone);
                mCellphoneEdit.setHint(R.string.modify_new_cellphone_hint);
                mNextButton.setText(R.string.commit);

                break;
            case SettingConst.FUNCTION_BIND_CELLPHONE:
                titleView.setText(R.string.account_bind_cellphone);
                mNextButton.setText(R.string.commit);
                findViewById(R.id.tips).setVisibility(View.VISIBLE);
                StatisticsUtil.reportAction(StatisticsConst.SETTING_BIND_CELLPHONE_DISPLAY_2);
                break;
            case SettingConst.FUNCTION_FORGET_PASSWORD:
                titleView.setText(R.string.forget_password);
                mNextButton.setText(R.string.commit);
                break;
            default:
                break;
        }
        mErrorTipsView = (ErrorTipsView) findViewById(R.id.errorTips);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterSmsReceiver();
        mMainHandler.removeMessages(MSG_GET_VERIFIED_CODE_BUTTON_COUNT_DOWN);
    }

    private void registerSmsReceiver() {
        if (mSmsReceiver == null) {
            mSmsReceiver = new SMSReceiver();
            registerReceiver(mSmsReceiver, new IntentFilter(SMS_RECEIVED_ACTION));
        }
    }

    private void unregisterSmsReceiver() {
        if (mSmsReceiver != null) {
            unregisterReceiver(mSmsReceiver);
            mSmsReceiver = null;
        }
    }

    @Override
    public void onBackPressed() {
        clickBack();
    }

    private void clickBack() {
        if (mFunctionType == SettingConst.FUNCTION_BIND_CELLPHONE) {
            final CommonDialog dialog = new CommonDialog(this);
            dialog.setCanCancelOnTouchOutside(false);
            dialog.setMessage(R.string.bind_cellphone_close_tips);
            dialog.addButton(R.string.later);
            dialog.addButton(R.string.continue_bind_cellphone);
            dialog.setButtonClickListener((commonDialog, view, position) -> {
                commonDialog.dismiss();
                switch (position) {
                    case 0:
                        finish();
                        break;
                    case 1:
                    default:
                        break;
                }
            });
            dialog.show();
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_button:
            case R.id.actionbar_close_button:
                clickBack();
                break;
            case R.id.nextButton:
                clickNextButton();
                switch (mFunctionType) {
                    case SettingConst.FUNCTION_MODIFY_CELLPHONE:
                        break;
                    case SettingConst.FUNCTION_BIND_CELLPHONE:
                        StatisticsUtil.reportAction(StatisticsConst.SETTING_BIND_CELLPHONE_1_CLICK_NEXT_BUTTON);
                        break;
                    case SettingConst.FUNCTION_FORGET_PASSWORD:
                        StatisticsUtil.reportAction(StatisticsConst.SETTING_FORGET_PASSWORD_1_CLICK_NEXT_BUTTON);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.getVerifiedCode:
                clickGetVerifiedCode();
                break;
            default:
                break;
        }
    }

    private void clickGetVerifiedCode() {
        final String cellphone = mCellphoneEdit.getPhoneNumber();
        if (!CheckTextFormatUtil.checkCellphoneFormat(mErrorTipsView, cellphone)) {
            return;
        }

        if (!NetUtil.isNetWorkConnected(this)) {
            mErrorTipsView.showErrorTips(getString(R.string.error_tips_network_error));
            return;
        }

        switch (mFunctionType) {
            case SettingConst.FUNCTION_MODIFY_CELLPHONE: // 修改手机号
            case SettingConst.FUNCTION_BIND_CELLPHONE: // 绑定手机号，验证短信验证码
                showLoadingDialog();
                // 需要验证手机号的唯一性.
                AccountRequestManager.sendVerifedCode(cellphone,
                        AccountRequestManager.STATE_ACCOUNT_EXIST_NOT_SEND_CODE, this);
                break;
            case SettingConst.FUNCTION_FORGET_PASSWORD: // 忘记密码
                showLoadingDialog();
                // 应该需要验证手机号是否被注册，如果有就发短信，如果没有就不应该发
                AccountRequestManager.sendVerifedCode(cellphone,
                        AccountRequestManager.STATE_ACCOUNT_EXIST_SEND_CODE, this);
                break;
            default:
                break;
        }
    }

    public void clickNextButton() {
        final String cellphone = mCellphoneEdit.getPhoneNumber();
        if (!CheckTextFormatUtil.checkCellphoneFormat(mErrorTipsView, cellphone)) {
            return;
        }

        final String smsCode = mSmsCodeEdit.getText().toString();
        if (!CheckTextFormatUtil.checkSmsCodeFormat(mErrorTipsView, smsCode)) {
            return;
        }

        if (!NetUtil.isNetWorkConnected(this)) {
            mErrorTipsView.showErrorTips(getString(R.string.error_tips_network_error));
            return;
        }

        mCellphone = cellphone;
        switch (mFunctionType) {
            case SettingConst.FUNCTION_MODIFY_CELLPHONE: // 修改手机号
                showLoadingDialog();
                final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
                final String password = getIntent().getStringExtra(SettingConst.EXTRA_PASSWORD);
                AccountRequestManager.modifyCellphoneinfo(accountInfo.ticket, accountInfo.id, cellphone, password, smsCode, this);
                break;
            case SettingConst.FUNCTION_BIND_CELLPHONE: // 绑定手机号，验证短信验证码
            case SettingConst.FUNCTION_FORGET_PASSWORD: // 忘记密码
                showLoadingDialog();
                mSmsCode = smsCode;
                AccountRequestManager.verifySmsCode(cellphone, smsCode, this);
                break;
            default:
                break;
        }
    }

    private void sendSmsCodeCallback(boolean success, final int code) {
        if (success) {
            switch (code) {
                case 0:
                    // 手机验证码发送成功
                    mMainHandler.sendEmptyMessage(MSG_SET_GET_VERIFIED_CODE_BUTTON_DISABLE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.send_verify_code_success);
                        }
                    });
                    break;
                case E_ACCOUNT_RET.E_AR_PHONE_EMAIL_USED:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_cellphone_occupied));
                    break;
                case E_ACCOUNT_RET.E_AR_ACCOUNT_NO_EXIST:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_cellphone_not_register));
                default:
                    break;
            }
        } else {
            mErrorTipsView.showErrorTips(getString(R.string.error_tips_request_failed));
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (!success) {
            return;
        }
        final Object entity = data.getEntity();
        if (entity == null) {
            return;
        }

        if (!(entity instanceof MapProtoLite)) {
            return;
        }
        final MapProtoLite packet = (MapProtoLite) entity;
        final int code = packet.read("", -1);
        switch (data.getEntityType()) {
            case EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO: // 修改手机号的callback
                modifyCellphoneInfoCallback(success, code);
                break;
            case EntityObject.ET_ACCOUNT_SEND_PHONE_VERIFY_CODE:
                sendSmsCodeCallback(success, code);
                break;
            case EntityObject.ET_ACCOUNT_VERIFY_ACCOUNT_INFO:
                processVerifySmsCodeCallback(success, code);
                break;
            default:
                break;
        }

        dismissLoadingDialog();
    }

    private void processVerifySmsCodeCallback(final boolean success, final int code) {
        if (success) {
            switch (code) {
                case 0:
                    // 短信验证码验证通过
                    Intent intent = null;
                    switch (mFunctionType) {
                        case SettingConst.FUNCTION_BIND_CELLPHONE: // 绑定手机号，验证短信验证码，跳转到设置密码界面
                            break;
                        case SettingConst.FUNCTION_FORGET_PASSWORD: // 忘记密码，跳转到重新设置密码界面
                            break;
                        default:
                            return;
                    }
                    intent = new Intent(this, SetUserPasswordActivity.class);
                    intent.putExtra(SettingConst.EXTRA_CELLPHONE, mCellphone);
                    intent.putExtra(SettingConst.EXTRA_SMS_CODE, mSmsCode);
                    intent.putExtra(SettingConst.EXTRA_FUNCTION_TYPE, mFunctionType);
                    startActivity(intent);
                    finish();
                    break;
                case E_ACCOUNT_RET.E_AR_PHONE_CODE_ERROR:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_sms_code_error));
                    break;
                case E_ACCOUNT_RET.E_AR_PHONE_CODE_EXPIRED:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_sms_code_invalid));
                    break;
                default:
                    break;
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DengtaApplication.getApplication().showToast(R.string.error_tips_request_failed);
                }
            });
        }
    }

    private void modifyCellphoneInfoCallback(final boolean success, final int code) {
        if (success) {
            switch (code) {
                case 0:
                    // 手机验证通过
                    final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
                    final AccountInfoExt accountInfoExt = accountManager.getAccountInfoExt();
                    accountInfoExt.accountInfo.cellphone = mCellphone;
                    accountManager.updateLocalAccountInfo(accountInfoExt);
                    // TODO
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.modify_phone_number_success);
                            finish();
                        }
                    });
                    break;
                case E_ACCOUNT_RET.E_AR_PHONE_EMAIL_USED:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_cellphone_occupied));
                    break;
                case E_ACCOUNT_RET.E_AR_PHONE_CODE_ERROR: //手机的验证码错误
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_sms_code_error));
                    break;
                case E_ACCOUNT_RET.E_AR_PHONE_CODE_EXPIRED: // 手机的验证码已经过期失效
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_sms_code_invalid));
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(String.format(getString(R.string.error_tips_error_code), String.valueOf(code)));
                        }
                    });
                    break;
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DengtaApplication.getApplication().showToast(R.string.error_tips_request_failed);
                }
            });
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SET_GET_VERIFIED_CODE_BUTTON_DISABLE:
                mGetVerifiedCodeButton.setEnabled(false);
                mMainHandler.sendEmptyMessage(MSG_GET_VERIFIED_CODE_BUTTON_COUNT_DOWN);
                break;
            case MSG_SET_GET_VERIFIED_CODE_BUTTON_ENABLE:
                mGetVerifiedCodeButton.setEnabled(true);
                mGetVerifiedCodeButton.setText(R.string.code_send_repeat);
                break;
            case MSG_GET_VERIFIED_CODE_BUTTON_COUNT_DOWN: // 倒数
                if (mCountDown < 0) {
                    mCountDown = 60;
                    mMainHandler.sendEmptyMessage(MSG_SET_GET_VERIFIED_CODE_BUTTON_ENABLE);
                } else {
                    mGetVerifiedCodeButton.setText(getString(R.string.code_count_down, mCountDown));
                    mCountDown--;
                    mMainHandler.sendEmptyMessageDelayed(MSG_GET_VERIFIED_CODE_BUTTON_COUNT_DOWN, DengtaConst.MILLIS_FOR_SECOND);
                }
                break;
        }
        return true;
    }

    /**
     * 自动填写短信验证码使用的Receiver
     * // TODO 与注册界面的代码重复，可以复用
     */
    public final class SMSReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            final Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];
            for (int n = 0; n < messages.length; n++) {
                smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
            }
            final String smsContent = smsMessage[0].getMessageBody();
            // TODO 临时写死
            if (!TextUtils.isEmpty(smsContent) && smsContent.contains(getString(R.string.sscf)) && smsContent.contains(getString(R.string.verified_code))) {
                int startIndex = smsContent.indexOf(getString(R.string.verified_code)) + 5;
                int endIndex = startIndex + 4;
                mSmsCodeEdit.setText(smsContent.substring(startIndex, endIndex));
            }
        }
    }
}