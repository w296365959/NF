package com.sscf.investment.setting;

import BEC.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import com.chenenyu.router.annotation.Route;
import com.sscf.investment.component.ui.widget.PicPickerDialog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.sdk.net.NetworkConst;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.sscf.investment.setting.widgt.ErrorTipsView;
import com.sscf.investment.socialize.UmengSocialSDKUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.setting.manager.AccountRequestManager;
import com.sscf.investment.setting.manager.CheckTextFormatUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.ClearButtonEditTextListener;
import com.sscf.investment.widget.PhoneNumberEditText;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.dengtacj.thoth.MapProtoLite;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * davidwei
 * 登录界面
 */
@Route("LoginActivity")
public final class LoginActivity extends BaseFragmentActivity implements View.OnClickListener,
        UMAuthListener, DataSourceProxy.IRequestCallback {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private PhoneNumberEditText mLoginCellphoneView;
    private EditText mLoginPasswordView;
    private BroadcastReceiver mLoginReceiver;

    private ErrorTipsView mErrorTipsView;

    private int mLoginType;
    private Map<String, String> mLoginInfos;

    private byte[] mAvatarData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_login);
        initViews();
        registerLoginBroadcast();
        StatisticsUtil.reportAction(StatisticsConst.SETTING_LOGIN_DISPLAY);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.login_title);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        findViewById(R.id.qqLoginButton).setOnClickListener(this);
        findViewById(R.id.wechatLoginButton).setOnClickListener(this);
        findViewById(R.id.weiboLoginButton).setOnClickListener(this);
        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.loginRegister).setOnClickListener(this);
        findViewById(R.id.forgetPassword).setOnClickListener(this);

        mLoginCellphoneView = (PhoneNumberEditText) findViewById(R.id.loginCellphone);
        mLoginPasswordView = (EditText) findViewById(R.id.loginPassword);

        final View loginCellphoneClearButton = findViewById(R.id.loginCellphoneClearButton);
        new ClearButtonEditTextListener(mLoginCellphoneView, loginCellphoneClearButton);
        final View loginPasswordClearButton = findViewById(R.id.loginPasswordClearButton);
        new ClearButtonEditTextListener(mLoginPasswordView, loginPasswordClearButton);

        mErrorTipsView = (ErrorTipsView) findViewById(R.id.errorTips);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterLoginBroadcast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissLoadingDialog();
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.qqLoginButton:
                showLoadingDialog();
                UmengSocialSDKUtils.getPlatformInfo(this, SHARE_MEDIA.QQ, this);
                StatisticsUtil.reportAction(StatisticsConst.SETTING_LOGIN_CLICK_QQ_LOGIN);
                break;
            case R.id.wechatLoginButton:
                showLoadingDialog();
                UmengSocialSDKUtils.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, this);
                StatisticsUtil.reportAction(StatisticsConst.SETTING_LOGIN_CLICK_WECHAT_LOGIN);
                break;
            case R.id.weiboLoginButton:
                showLoadingDialog();
                UmengSocialSDKUtils.getPlatformInfo(this, SHARE_MEDIA.SINA, this);
                StatisticsUtil.reportAction(StatisticsConst.SETTING_LOGIN_CLICK_WEIBO_LOGIN);
                break;
            case R.id.loginButton:
                clickLoginButton();
                StatisticsUtil.reportAction(StatisticsConst.SETTING_LOGIN_CLICK_LOGIN_BUTTON);
                break;
            case R.id.loginRegister:
                startActivity(new Intent(this, Register1VerifySmsCodeActivity.class));
                StatisticsUtil.reportAction(StatisticsConst.SETTING_LOGIN_CLICK_REGISTER);
                break;
            case R.id.forgetPassword:
                final Intent intent = new Intent(this, VerifySmsCodeActivity.class);
                intent.putExtra(SettingConst.EXTRA_FUNCTION_TYPE, SettingConst.FUNCTION_FORGET_PASSWORD);
                startActivity(intent);
                StatisticsUtil.reportAction(StatisticsConst.SETTING_LOGIN_CLICK_FORGET_PASSWORD);
                break;
            case R.id.actionbar_back_button:
                finish();
                break;
            default:
                break;
        }
    }

    private void clickLoginButton() {
        String cellphone = mLoginCellphoneView.getPhoneNumber();
        if (!CheckTextFormatUtil.checkCellphoneFormat(mErrorTipsView, cellphone)) {
            return;
        }

        final String password = mLoginPasswordView.getText().toString();
        if (!CheckTextFormatUtil.checkPasswordFormat(mErrorTipsView, password)) {
            return;
        }

        if (!NetUtil.isNetWorkConnected(this)) {
            mErrorTipsView.showErrorTips(R.string.error_tips_network_error);
            return;
        }

        showLoadingDialog();
        AccountRequestManager.login(cellphone, password, this);
    }

    @Override
    public void onStart(SHARE_MEDIA plat) {
        DtLog.d(TAG, "onStart plat : " + plat);
    }

    @Override
    public void onComplete(SHARE_MEDIA plat, int i, Map<String, String> map) {
        DtLog.d(TAG, "onComplete plat : " + plat);
        DtLog.d(TAG, "onComplete map : " + map);
        dismissLoadingDialog();

        if (map == null) {
            DengtaApplication.getApplication().showToast(R.string.error_tips_third_party_error);
            finish();
            return;
        }

        if (!NetUtil.isNetWorkConnected(this)) {
            mErrorTipsView.showErrorTips(R.string.error_tips_network_error);
            return;
        }

        mLoginInfos = map;

        // 第三方登录成功
        int type = -1;
        String openId = map.get(SettingConst.EXTRA_OPEN_ID);
        if (SHARE_MEDIA.WEIXIN.equals(plat)) {
            type = E_LOGIN_TYPE.E_WEIXIN_LOGIN;
        } else if (SHARE_MEDIA.QQ.equals(plat)) {
            type = E_LOGIN_TYPE.E_QQ_LOGIN;
        } else if (SHARE_MEDIA.SINA.equals(plat)) {
            type = E_LOGIN_TYPE.E_WEIBO_LOGIN;
            openId = map.get(SettingConst.EXTRA_SINA_UID); // 潜规则
        }

        if (type < 0) { // 非法登录类型
            mErrorTipsView.showErrorTips(R.string.login_faild_please_retry);
        } else {
            showLoadingDialog();

            final String accessToken = map.get(SettingConst.EXTRA_ACCESS_TOKEN);

            mLoginType = type;

            switch (type) {
                case E_LOGIN_TYPE.E_WEIXIN_LOGIN:
                    String unionid = map.get(SettingConst.EXTRA_WECHAT_UNIONID);
                    if (unionid == null) {
                        unionid = "";
                    }
                    AccountRequestManager.wechatLogin(openId, accessToken, unionid, this);
                    break;
                case E_LOGIN_TYPE.E_QQ_LOGIN:
                default:
                    AccountRequestManager.thirdPartyLogin(openId, accessToken, type, this);
                    break;
            }
        }
    }

    @Override
    public void onError(final SHARE_MEDIA plat, int i, final Throwable t) {
        DtLog.d(TAG, "onError share_media : " + plat + " t : " + t);
        if (isDestroy()) {
            return;
        }

        final boolean isWeChatNotInstalledException = isWeChatNotInstalledException(plat, t);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
                mErrorTipsView.showErrorTips(isWeChatNotInstalledException ? R.string.not_install_wechat : R.string.third_account_login_faild);
            }
        });
    }

    private boolean isWeChatNotInstalledException(final SHARE_MEDIA plat, final Throwable t) {
        return plat != null && (SHARE_MEDIA.WEIXIN.equals(plat) || SHARE_MEDIA.WEIXIN_CIRCLE.equals(plat))
                && t instanceof UmengSocialSDKUtils.WeChatNotInstalledException;
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        dismissLoadingDialog();
    }

    private void loginCallback(final boolean success, final MapProtoLite packet) {
        if (success) {
            final int code = packet.read("", -1);
            switch (code) {
                case E_ACCOUNT_RET.E_AR_SUCC: // 登录成功
                    final LoginRsp rsp = packet.read(NetworkConst.RSP, new LoginRsp());
                    final AccountInfoExt accountInfoEntity = AccountRequestManager.parseAccountInfo(rsp.stAccountTicket.vtTicket,
                            rsp.stAccountInfo, rsp.vtThirdLoginInfo);
                    // 保存用户信息
                    DengtaApplication.getApplication().getAccountManager().saveAccountInfo(accountInfoEntity);
                    finish();
                    break;
                case E_ACCOUNT_RET.E_AR_ACCOUNT_NO_EXIST:
                    mErrorTipsView.showErrorTips(R.string.error_tips_user_not_exsit);
                    break;

                case E_ACCOUNT_RET.E_AR_PHONE_FORMAT_ERROR:
                    mErrorTipsView.showErrorTips(R.string.error_tips_phone_number_error);
                    break;
                case E_ACCOUNT_RET.E_AR_PASSWORD_ERROR:
                case E_ACCOUNT_RET.E_AR_PASSWORD_FORMAT_ERROR:
                    mErrorTipsView.showErrorTips(R.string.error_tips_password_error);
                    break;
                default:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_error_code, code));
                    break;
            }
        } else {
            mErrorTipsView.showErrorTips(R.string.error_tips_login_failed);
        }
    }

    private void thirdPartyLoginCallback(final boolean success, final MapProtoLite packet) {
        if (success) {
            final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
            DtLog.d(TAG, "thirdPartyLoginCallback code " + packet.read("", -1));
            final int code = packet.read("", -1);
            switch (code) {
                case 0: // 登录成功
                    final VerfiyThirdAccountRsp rsp = packet.read(NetworkConst.RSP, new VerfiyThirdAccountRsp());
                    final AccountInfoExt accountInfoEntity = AccountRequestManager.parseAccountInfo(rsp.stAccountTicket.vtTicket,
                            rsp.stAccountInfo, rsp.vtThirdLoginInfo);
                    // 保存用户信息
                    dengtaApplication.getAccountManager().saveAccountInfo(accountInfoEntity);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dengtaApplication.showToast(R.string.login_success);
                            finish();
                        }
                    });
                    break;
                case E_ACCOUNT_RET.E_AR_THIRD_ACCOUNT_NO_EXIST:
                    // 未注册的第三方用户 先拉取头像
                    // 清除以前的头像数据
                    mAvatarData = null;
                    final String url = mLoginInfos.get(SettingConst.EXTRA_PROFILE_IMAGE_URL);
                    if (TextUtils.isEmpty(url) || url.length() < 7) {
                        registerThirdParty();
                    } else {
                        ImageLoaderUtils.getImageLoader().loadImage(url, new AvatarImageListener(url));
                    }
                    return;
                default:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_error_code, code));
                    break;
            }
        } else {
            mErrorTipsView.showErrorTips(R.string.error_tips_login_failed);
        }
        dismissLoadingDialog();
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (!success || data == null) {
            dismissLoadingDialog();
            mErrorTipsView.showErrorTips(R.string.error_tips_login_failed);
            return;
        }

        if (isDestroy()) { // 界面退出了就不做任何逻辑
            return;
        }

        final Object entity = data.getEntity();
        if (entity == null || !(entity instanceof MapProtoLite)) {
            return;
        }

        final MapProtoLite packet = (MapProtoLite) data.getEntity();
        switch (data.getEntityType()) {
            case EntityObject.ET_ACCOUNT_LOGIN:
                loginCallback(success, packet);
                dismissLoadingDialog();
                break;
            case EntityObject.ET_ACCOUNT_THIRD_PARTY_LOGIN:
                thirdPartyLoginCallback(success, packet);
                break;
            default:
                dismissLoadingDialog();
                break;
        }
    }

    private void registerLoginBroadcast() {
        if (mLoginReceiver == null) {
            mLoginReceiver = new LoginBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter(SettingConst.ACTION_LOGIN_SUCCESS);
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mLoginReceiver, intentFilter);
        }
    }

    private void unregisterLoginBroadcast() {
        if (mLoginReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mLoginReceiver);
        }
    }

    private final class LoginBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_LOGIN_SUCCESS.equals(action)) {
                finish();
            }
        }
    }

    private final class AvatarImageListener implements ImageLoadingListener, Runnable {
        private Bitmap mBitmap;

        private AvatarImageListener(final String url) {
        }

        @Override
        public void onLoadingStarted(String s, View view) {
        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {
            DtLog.d(TAG, "onLoadingFailed : " + failReason);
            // 未获得图片直接注册
            registerThirdParty();
        }

        @Override
        public void onLoadingComplete(String s, View view, final Bitmap bitmap) {
            if (bitmap != null) {
                mBitmap = bitmap;
                DengtaApplication.getApplication().defaultExecutor.execute(this);
            }
        }

        @Override
        public void onLoadingCancelled(String s, View view) {
        }

        @Override
        public void run() {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            mAvatarData = out.toByteArray();
            FileUtil.closeStream(null, out);
            registerThirdParty();
        }
    }

    private void registerThirdParty() {
        String openId = mLoginInfos.get(SettingConst.EXTRA_OPEN_ID);
        final String accessToken = mLoginInfos.get(SettingConst.EXTRA_ACCESS_TOKEN);
        final String nickname = mLoginInfos.get(SettingConst.EXTRA_SCREEN_NAME);
        switch (mLoginType) {
            case E_LOGIN_TYPE.E_QQ_LOGIN:
                AccountRequestManager.thirdPartyRegister(openId, accessToken, mLoginType, nickname,
                        mAvatarData, PicPickerDialog.IMAGE_TYPE, new ThirdPartyRegisterCallback());
                break;
            case E_LOGIN_TYPE.E_WEIXIN_LOGIN:
                String unionid = mLoginInfos.get(SettingConst.EXTRA_WECHAT_UNIONID);
                if (unionid == null) {
                    unionid = "";
                }
                AccountRequestManager.wechatRegister(openId, accessToken, unionid, nickname, mAvatarData,
                        PicPickerDialog.IMAGE_TYPE, new ThirdPartyRegisterCallback());
                break;
            case E_LOGIN_TYPE.E_WEIBO_LOGIN:
                openId = mLoginInfos.get(SettingConst.EXTRA_SINA_UID); // 潜规则
                AccountRequestManager.thirdPartyRegister(openId, accessToken, mLoginType, nickname, mAvatarData,
                        PicPickerDialog.IMAGE_TYPE, new ThirdPartyRegisterCallback());
                break;
            default:
                break;
        }
    }

    private void thirdPartyRegisterCallback(final boolean success, final MapProtoLite packet) {
        if (success) {
            final int code = packet.read("", -1);
            switch (code) {
                case E_ACCOUNT_RET.E_AR_SUCC: // 登录成功
                    final LoginRsp rsp = packet.read(NetworkConst.RSP, new LoginRsp());
                    final AccountInfoExt accountInfoEntity = AccountRequestManager.parseAccountInfo(rsp.stAccountTicket.vtTicket, rsp.stAccountInfo, rsp.vtThirdLoginInfo);
                    // 保存用户信息
                    DengtaApplication.getApplication().getAccountManager().saveAccountInfo(accountInfoEntity);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.register_success);
                            finish();
                        }
                    });
                    break;
                default:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_error_code, code));
                    break;
            }
        } else {
            mErrorTipsView.showErrorTips(getString(R.string.error_tips_login_failed));
        }
    }

    private final class ThirdPartyRegisterCallback implements DataSourceProxy.IRequestCallback {

        @Override
        public void callback(boolean success, EntityObject data) {
            if (!success || data == null) {
                dismissLoadingDialog();
                mErrorTipsView.showErrorTips(R.string.error_tips_login_failed);
                return;
            }

            if (isDestroy()) { // 界面退出了就不做任何逻辑
                return;
            }

            final Object entity = data.getEntity();
            if (entity == null || !(entity instanceof MapProtoLite)) {
                return;
            }

            final MapProtoLite packet = (MapProtoLite) data.getEntity();
            switch (data.getEntityType()) {
                case EntityObject.ET_ACCOUNT_LOGIN: // 第三方账户注册的回调
                    thirdPartyRegisterCallback(success, packet);
                    break;
                default:
                    break;
            }
            dismissLoadingDialog();
        }
    }
}