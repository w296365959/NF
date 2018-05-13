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
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.widgt.ErrorTipsView;
import com.sscf.investment.sdk.utils.DtLog;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.setting.manager.AccountRequestManager;
import com.sscf.investment.setting.manager.CheckTextFormatUtil;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.web.CommonWebConst;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.ClearButtonEditTextListener;
import com.sscf.investment.widget.PhoneNumberEditText;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.dengtacj.thoth.MapProtoLite;

/**
 * davidwei
 * 注册流程里验证短信验证码界面
 */
public final class Register1VerifySmsCodeActivity extends BaseFragmentActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback, Handler.Callback {
    private static final String TAG = Register1VerifySmsCodeActivity.class.getSimpleName();
    private View mRegisterContractRightView;
    private EditText mRegisterVerifiedCodeView;
    private PhoneNumberEditText mRegisterCellphoneView;
    private Button mGetVerifiedCodeButton;
    private ErrorTipsView mErrorTipsView;
    private BroadcastReceiver mSmsReceiver;
    private Handler mMainHandler;
    private int mCountDown = 60;

    private static final int MSG_SET_GET_VERIFIED_CODE_BUTTON_DISABLE = 1;
    private static final int MSG_SET_GET_VERIFIED_CODE_BUTTON_ENABLE = 2;
    private static final int MSG_GET_VERIFIED_CODE_BUTTON_COUNT_DOWN = 3;
    private static final int MSG_SHOW_TOAST = 4;

    // TODO 没找到常量
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_register1_verify_sms_code);
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
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.register_title);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        findViewById(R.id.nextButton).setOnClickListener(this);
        mGetVerifiedCodeButton = (Button) findViewById(R.id.getVerifiedCode);
        mGetVerifiedCodeButton.setOnClickListener(this);
        mRegisterContractRightView = findViewById(R.id.registerContractRight);
        mRegisterContractRightView.setOnClickListener(this);
        mRegisterCellphoneView = (PhoneNumberEditText) findViewById(R.id.registerCellphone);
        mRegisterVerifiedCodeView = (EditText) findViewById(R.id.registerVerifiedCode);

        View registerCellphoneClearButton = findViewById(R.id.registerCellphoneClearButton);
        new ClearButtonEditTextListener(mRegisterCellphoneView, registerCellphoneClearButton);
        View registerVerifiedCodeClearButton = findViewById(R.id.registerVerifiedCodeClearButton);
        new ClearButtonEditTextListener(mRegisterVerifiedCodeView, registerVerifiedCodeClearButton);

        mErrorTipsView = (ErrorTipsView) findViewById(R.id.errorTips);
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterSmsReceiver();
        mMainHandler.removeMessages(MSG_GET_VERIFIED_CODE_BUTTON_COUNT_DOWN);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        switch (v.getId()) {
            case R.id.getVerifiedCode:
                clickGetVerifiedCode();
                break;
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.nextButton:
                clickNextButton();
                StatisticsUtil.reportAction(StatisticsConst.SETTING_REGISTER_1_CLICK_NEXT_BUTTON);
                break;
            case R.id.registerContractRight:
                WebBeaconJump.showWebActivity(this, DengtaApplication.getApplication().getUrlManager().getUserAgreementUrl(), CommonWebConst.WT_USER_AGREEMENT);
                break;
            default:
                break;
        }
    }

    private void clickGetVerifiedCode() {
        final String cellphone = mRegisterCellphoneView.getPhoneNumber();
        if (!CheckTextFormatUtil.checkCellphoneFormat(mErrorTipsView, cellphone)) {
            return;
        }

        if (!NetUtil.isNetWorkConnected(this)) {
            mErrorTipsView.showErrorTips(getString(R.string.error_tips_network_error));
            return;
        }

        showLoadingDialog();
        AccountRequestManager.sendVerifedCode(cellphone,
                AccountRequestManager.STATE_ACCOUNT_EXIST_NOT_SEND_CODE, this);
    }

    private void clickNextButton() {
        final String cellphone = mRegisterCellphoneView.getPhoneNumber();
        if (!CheckTextFormatUtil.checkCellphoneFormat(mErrorTipsView, cellphone)) {
            return;
        }

        final String smsCode = mRegisterVerifiedCodeView.getText().toString();
        if (!CheckTextFormatUtil.checkSmsCodeFormat(mErrorTipsView, smsCode)) {
            return;
        }

        if (!NetUtil.isNetWorkConnected(this)) {
            mErrorTipsView.showErrorTips(getString(R.string.error_tips_network_error));
            return;
        }

        showLoadingDialog();
        AccountRequestManager.verifySmsCode(cellphone, smsCode, this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success) {
            DtLog.d(TAG, "callback data.getEntityType() : " + data.getEntityType());
            DtLog.d(TAG, "callback data.getEntity() :" + data.getEntity());
            final Object entity = data.getEntity();
            if (entity == null || !(entity instanceof MapProtoLite)) {
                return;
            }

            MapProtoLite uniPacket = (MapProtoLite)entity;
            final int code = uniPacket.read("", -1);
            switch (data.getEntityType()) {
                case EntityObject.ET_ACCOUNT_SEND_PHONE_VERIFY_CODE: // 向后台要求发送验证码的请求的处理
                    // TODO 手机号已经被注册过了,临时toast提示
                    if (code == E_ACCOUNT_RET.E_AR_PHONE_EMAIL_USED) {
                        mErrorTipsView.showErrorTips(getString(R.string.error_tips_cellphone_occupied));
                    } else if (code == 0) { // 手机验证码发送成功
                        mMainHandler.sendEmptyMessage(MSG_SET_GET_VERIFIED_CODE_BUTTON_DISABLE);
                        mMainHandler.obtainMessage(MSG_SHOW_TOAST, getString(R.string.send_verify_code_success)).sendToTarget();
                    }
                    break;
                case EntityObject.ET_ACCOUNT_VERIFY_ACCOUNT_INFO: // 向后台验证短信验证码的请求的处理
                    if (code == E_ACCOUNT_RET.E_AR_PHONE_CODE_ERROR) { // 手机的验证码错误
                        mErrorTipsView.showErrorTips(getString(R.string.error_tips_sms_code_error));
                    } else if (code == E_ACCOUNT_RET.E_AR_PHONE_CODE_EXPIRED) { // T手机的验证码已经过期失效
                        mErrorTipsView.showErrorTips(getString(R.string.error_tips_sms_code_invalid));
                    } else if (code == 0) { // 手机验证通过
                        final Intent intent = new Intent(this, Register2SetUserInfoActivity.class);
                        intent.putExtra("cellphone", mRegisterCellphoneView.getPhoneNumber());
                        startActivity(intent);
                        finish();
                    }
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
            case MSG_SHOW_TOAST:
                DengtaApplication.getApplication().showToast((String) msg.obj);
                break;
        }
        return true;
    }

    /**
     * 自动填写短信验证码使用的Receiver
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
            // TODO 临时写死  判断验证码中是否含有灯塔字段
            if (!TextUtils.isEmpty(smsContent) && smsContent.contains(getString(R.string.sscf)) && smsContent.contains(getString(R.string.verified_code))) {
                int startIndex = smsContent.indexOf(getString(R.string.verified_code)) + 5;
                int endIndex = startIndex + 4;
                mRegisterVerifiedCodeView.setText(smsContent.substring(startIndex, endIndex));
            }
        }
    }
}