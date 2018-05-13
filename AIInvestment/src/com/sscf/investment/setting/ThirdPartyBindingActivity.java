package com.sscf.investment.setting;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.thoth.MapProtoLite;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.sscf.investment.setting.manager.AccountRequestManager;
import com.sscf.investment.setting.widgt.BaseLogoutActivity;
import com.sscf.investment.socialize.UmengSocialSDKUtils;
import com.sscf.investment.widget.ConfirmDialog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import BEC.E_ACCOUNT_RET;
import BEC.E_LOGIN_TYPE;

/**
 * davidwei
 * 第三方帐号绑定界面
 */
public final class ThirdPartyBindingActivity extends BaseLogoutActivity implements View.OnClickListener,
        UMAuthListener, DataSourceProxy.IRequestCallback {
    private static final String TAG = ThirdPartyBindingActivity.class.getSimpleName();

    private String mBindOpenId;
    private int mBindType;

    private String mUnbindOpenId;
    private int mUnbindType;

    private Dialog mConfirmDialog;

    private TextView mCellphoneView;
    private TextView mModifyPasswordView;

    private TextView mThirdPartyWechatBindingStateView;
    private TextView mThirdPartyQqBindingStateView;
    private TextView mThirdPartySinaWeiboBindingStateView;

    private int mStateColor;

    private BroadcastReceiver mAccountReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
        if (accountInfo == null) {
            finish();
            return;
        }

        setContentView(R.layout.activity_setting_third_party_binding);
        initViews();
        StatisticsUtil.reportAction(StatisticsConst.SETTING_THIRD_PARTY_BINDING_DISPLAY);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.setting_account);

        findViewById(R.id.modifyCellphone).setOnClickListener(this);
        mCellphoneView = (TextView) findViewById(R.id.cellphone);

        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        findViewById(R.id.thirdPartyQqBinding).setOnClickListener(this);
        findViewById(R.id.thirdPartyWechatBinding).setOnClickListener(this);
        findViewById(R.id.thirdPartySinaWeiboBinding).setOnClickListener(this);

        mModifyPasswordView = (TextView) findViewById(R.id.modifyPassword);
        mModifyPasswordView.setOnClickListener(this);

        mThirdPartyWechatBindingStateView = (TextView) findViewById(R.id.thirdPartyWechatBindingState);
        mThirdPartyQqBindingStateView = (TextView) findViewById(R.id.thirdPartyQqBindingState);
        mThirdPartySinaWeiboBindingStateView = (TextView) findViewById(R.id.thirdPartySinaWeiboBindingState);

        mStateColor = ContextCompat.getColor(this, R.color.default_text_color_60);

        updateViews();
        updateCellphone();

        registerAccountBroadcast();
    }

    private void updateViews() {
        final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
        if (accountInfo != null) {
            final Resources res = getResources();
            setTextState(mThirdPartyWechatBindingStateView, accountInfo.isWechatBinded(), res);
            setTextState(mThirdPartyQqBindingStateView, accountInfo.isQqBinded(), res);
            setTextState(mThirdPartySinaWeiboBindingStateView, accountInfo.isSinaWeiboBinded(), res);
        }
    }

    private void updateCellphone() {
        final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
        if (accountInfo != null) {
            String cellphone = accountInfo.cellphone;
            if (TextUtils.isEmpty(cellphone)) {
                mModifyPasswordView.setVisibility(View.GONE);
            } else {
                // TODO 有没有更好的转换方式
                final StringBuilder builder = new StringBuilder(cellphone.substring(0,3));
                builder.append(' ').append('*').append('*').append('*').append('*').append(' ')
                        .append(cellphone.substring(7, cellphone.length()));
                cellphone = builder.toString();
                mCellphoneView.setText(cellphone);
                mModifyPasswordView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setTextState(final TextView textView, final boolean binded, final Resources res) {
        if (binded) {
            textView.setText(R.string.binded);
            textView.setTextColor(mStateColor);
        } else {
            textView.setText(R.string.unbind);
            textView.setTextColor(res.getColor(R.color.stock_red_color));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterLoginBroadcast();
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        String openId = null;
        switch (v.getId()) {
            case R.id.modifyCellphone:
                if (TextUtils.isEmpty(DengtaApplication.getApplication().getAccountManager().getAccountInfo().cellphone)) {
                    CommonBeaconJump.showBindCellphone(this);
                    StatisticsUtil.reportAction(StatisticsConst.SETTING_BIND_CELLPHONE_DISPLAY);
                } else {
                    // 更换手机号
                    CommonBeaconJump.showActivity(this, ModifyCellphone1VerifyPasswordActivity.class);
                    StatisticsUtil.reportAction(StatisticsConst.SETTING_MODIFY_CELLPHONE_DISPLAY);
                }
                break;
            case R.id.modifyPassword://修改密码
                CommonBeaconJump.showModifyPassword(this);
                StatisticsUtil.reportAction(StatisticsConst.SETTING_MODIFY_PASSWORD_DISPLAY);
                break;
            case R.id.thirdPartyQqBinding:
                if (!NetUtil.isNetWorkConnected(this)) {
                    // TODO 先toast提醒
                    DengtaApplication.getApplication().showToast(R.string.error_tips_network_error);
                    return;
                }
                openId = accountInfo.getQqOpenId();
                if (TextUtils.isEmpty(openId)) {
                    // 绑定
                    showLoadingDialog();
                    UmengSocialSDKUtils.loginByThirdParty(this, SHARE_MEDIA.QQ, this);
                } else {
                    // 唯一绑定第三方的账户不允许解绑
                    if (accountInfo.isUniqueThirdPartyBinded()) {
                        // TODO 先toast提醒
                        DengtaApplication.getApplication().showToast(R.string.unbind_faild_unique_third_party);
                        return;
                    }

                    // 解绑确认框
                    mUnbindOpenId = openId;
                    mUnbindType = E_LOGIN_TYPE.E_QQ_LOGIN;
                    showConfirmDialog();

                }
                break;
            case R.id.thirdPartyWechatBinding:
                if (!NetUtil.isNetWorkConnected(this)) {
                    // TODO 先toast提醒
                    DengtaApplication.getApplication().showToast(R.string.error_tips_network_error);
                    return;
                }
                openId = accountInfo.getWechatOpenId();
                if (TextUtils.isEmpty(openId)) {
                    // 绑定
                    showLoadingDialog();
                    UmengSocialSDKUtils.loginByThirdParty(this, SHARE_MEDIA.WEIXIN, this);
                } else {
                    // 唯一绑定第三方的账户不允许解绑
                    if (accountInfo.isUniqueThirdPartyBinded()) {
                        // TODO 先toast提醒
                        DengtaApplication.getApplication().showToast(R.string.unbind_faild_unique_third_party);
                        return;
                    }

                    // 解绑确认框
                    mUnbindOpenId = openId;
                    mUnbindType = E_LOGIN_TYPE.E_WEIXIN_LOGIN;
                    showConfirmDialog();
                }
                break;
            case R.id.thirdPartySinaWeiboBinding:
                if (!NetUtil.isNetWorkConnected(this)) {
                    // TODO 先toast提醒
                    DengtaApplication.getApplication().showToast(R.string.error_tips_network_error);
                    return;
                }
                openId = accountInfo.getSinaWeiboOpenId();
                if (TextUtils.isEmpty(openId)) {
                    // 绑定
                    showLoadingDialog();
                    UmengSocialSDKUtils.loginByThirdParty(this, SHARE_MEDIA.SINA, this);
                } else {
                    // 唯一绑定第三方的账户不允许解绑
                    if (accountInfo.isUniqueThirdPartyBinded()) {
                        // TODO 先toast提醒
                        DengtaApplication.getApplication().showToast(R.string.unbind_faild_unique_third_party);
                        return;
                    }

                    // 解绑确认框
                    mUnbindOpenId = openId;
                    mUnbindType = E_LOGIN_TYPE.E_WEIBO_LOGIN;
                    showConfirmDialog();
                }
                break;
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.ok: // 点击解绑确认键
                dismissConfirmDialog();
                if (!NetUtil.isNetWorkConnected(this)) {
                    // TODO 先toast提醒
                    DengtaApplication.getApplication().showToast(R.string.error_tips_network_error);
                    return;
                }

                showLoadingDialog();
                AccountRequestManager.unbindThirdPartyAccount(accountInfo.ticket, accountInfo.id, mUnbindType,
                        mUnbindOpenId, new UnbindCallback());
                break;
            default:
                break;
        }
    }

    private void showConfirmDialog() {
        if (mConfirmDialog == null) {
            final ConfirmDialog dialog = new ConfirmDialog(this);
            dialog.setMessage(R.string.third_party_unbinding_msg);
            dialog.setOkButton(R.string.ok, this);
            mConfirmDialog = dialog;
        }
        mConfirmDialog.show();
    }

    private void dismissConfirmDialog() {
        if (mConfirmDialog != null) {
            mConfirmDialog.dismiss();
        }
    }

    @Override
    public void onStart(SHARE_MEDIA plat) {
        DtLog.d(TAG, "onStart plat : " + plat);
    }

    @Override
    public void onComplete(SHARE_MEDIA plat, int i, Map<String, String> map) {
        dismissLoadingDialog();
        if (!NetUtil.isNetWorkConnected(this)) {
            DengtaApplication.getApplication().showToast(R.string.error_tips_network_connect_error);
            return;
        }

        // 第三方登录成功
        int type = -1;
        String openId = map.get("openid");
        if (SHARE_MEDIA.WEIXIN.equals(plat)) {
            type = E_LOGIN_TYPE.E_WEIXIN_LOGIN;
        } else if (SHARE_MEDIA.QQ.equals(plat)) {
            type = E_LOGIN_TYPE.E_QQ_LOGIN;
        } else if (SHARE_MEDIA.SINA.equals(plat)) {
            type = E_LOGIN_TYPE.E_WEIBO_LOGIN;
            openId = map.get("uid"); // 潜规则
        }
        if (type < 0) { // 非法登录类型
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DengtaApplication.getApplication().showToast(R.string.login_faild_please_retry);
                }
            });
        } else {
            showLoadingDialog();
            // 第三方登录成功
            final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
            final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
            String accessToken = map.get("access_token");
            if (TextUtils.isEmpty(accessToken)) {
                accessToken = map.get("accessToken");
            }
            mBindOpenId = openId;
            mBindType = type;

            switch (type) {
                case E_LOGIN_TYPE.E_WEIXIN_LOGIN:
                    AccountRequestManager.bindWechatAccount(accountInfo.ticket, accountInfo.id, map.get(SettingConst.EXTRA_WECHAT_UNIONID), openId, accessToken, this);
                    break;
                default:
                    AccountRequestManager.bindThirdPartyAccount(accountInfo.ticket, accountInfo.id, type, openId, accessToken, this);
                    break;
            }
        }
    }

    @Override
    public void onError(SHARE_MEDIA plat, int i, Throwable t) {
        DtLog.d(TAG, "onError share_media" + plat);
        if (plat != null && (SHARE_MEDIA.WEIXIN.equals(plat) || SHARE_MEDIA.WEIXIN_CIRCLE.equals(plat))
                && t instanceof UmengSocialSDKUtils.WeChatNotInstalledException) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DengtaApplication.getApplication().showToast(R.string.not_install_wechat);
                    dismissLoadingDialog();
                }
            });
        }
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
//        DtLog.d(TAG, "onCancel plat" + plat);
        dismissLoadingDialog();
    }

    private final class UnbindCallback implements DataSourceProxy.IRequestCallback {
        @Override
        public void callback(boolean success, EntityObject data) {
            DtLog.d(TAG, "UnbindCallback callback success : " + success);
            if (success) {
                DtLog.d(TAG, "UnbindCallback callback data.getEntityType() : " + data.getEntityType());
                if (data.getEntityType() != EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO) {
                    return;
                }

                final Object entity = data.getEntity();
                DtLog.d(TAG, "UnbindCallback callback entity : " + entity);
                if (entity == null || !(entity instanceof MapProtoLite)) {
                    return;
                }

                final MapProtoLite packet = (MapProtoLite) entity;
                final int code = packet.read("", -1);
                DtLog.d(TAG, "UnbindCallback callback code : " + code);
                switch (code) {
                    case 0:
                        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
                        final AccountInfoExt accountInfoExt = accountManager.getAccountInfoExt();
                        if (accountInfoExt != null) {
                            accountInfoExt.accountInfo.removeThirdPartyInfo(mUnbindType);
                            accountManager.updateLocalAccountInfo(accountInfoExt);
                        }
                        switch (mUnbindType) {
                            case E_LOGIN_TYPE.E_QQ_LOGIN:
                                StatisticsUtil.reportAction(StatisticsConst.A_ME_INSTALL_QQ_RELIEVE_BINDING);
                                UmengSocialSDKUtils.removeAccount(ThirdPartyBindingActivity.this, SHARE_MEDIA.QQ);

                                break;
                            case E_LOGIN_TYPE.E_WEIXIN_LOGIN:
                                StatisticsUtil.reportAction(StatisticsConst.A_ME_INSTALL_WECHAT_RELIEVE_BINDING);
                                UmengSocialSDKUtils.removeAccount(ThirdPartyBindingActivity.this, SHARE_MEDIA.WEIXIN);
                                break;
                            case E_LOGIN_TYPE.E_WEIBO_LOGIN:
                                UmengSocialSDKUtils.removeAccount(ThirdPartyBindingActivity.this, SHARE_MEDIA.SINA);
                                break;
                            default:
                                break;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DengtaApplication.getApplication().showToast(R.string.unbind_success);
                                updateViews();
                            }
                        });
                        StatisticsUtil.reportAction(StatisticsConst.SETTING_THIRD_PARTY_UNBINDING_SUCCESS);
                        break;
                    // ticket验证不过，删除用户信息，重新登录
                    case E_ACCOUNT_RET.E_AR_TICKET_LENGTH_ERROR:
                    case E_ACCOUNT_RET.E_AR_TICKET_EXPIRED:
                    case E_ACCOUNT_RET.E_AR_TICKET_SIGN_ERROR:
                        DengtaApplication.getApplication().getAccountManager().removeAccountInfo();

                        startActivity(new Intent(ThirdPartyBindingActivity.this, LoginActivity.class));

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
                                DengtaApplication.getApplication().showToast(R.string.unbind_faild);
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
            dismissLoadingDialog();
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        DtLog.d(TAG, "ThirdPartyBindingActivity callback success : " + success);
        if (success) {
            if (data.getEntityType() != EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO) {
                return;
            }

            final Object entity = data.getEntity();
            DtLog.d(TAG, "ThirdPartyBindingActivity callback entity : " + entity);
            if (entity == null || !(entity instanceof MapProtoLite)) {
                return;
            }

            final MapProtoLite packet = (MapProtoLite) entity;
            final int code = packet.read("", -1);
            DtLog.d(TAG, "ThirdPartyBindingActivity callback code : " + code);
            switch (code) { // 绑定成功
                case 0:
                    final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
                    final AccountInfoExt accountInfoExt = accountManager.getAccountInfoExt();
                    if (accountInfoExt != null) {
                        accountInfoExt.accountInfo.addThirdPartyInfo(mBindType, mBindOpenId);
                        accountManager.updateLocalAccountInfo(accountInfoExt);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.bind_success);
                            updateViews();
                        }
                    });
                    StatisticsUtil.reportAction(StatisticsConst.SETTING_THIRD_PARTY_BINDING_SUCCESS);
                    break;

                case E_ACCOUNT_RET.E_AR_THIRD_HAS_BIND:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.already_bind_please_unbind);
                        }
                    });
                    break;
                case E_ACCOUNT_RET.E_AR_THIRD_VERFIY_ERROR:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.third_account_login_faild);
                        }
                    });
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
                            DengtaApplication.getApplication().showToast(getString(R.string.bind_faild) + ":" + code);
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
        dismissLoadingDialog();
    }

    private void registerAccountBroadcast() {
        if (mAccountReceiver == null) {
            mAccountReceiver = new AccountBroadcastReceiver();
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mAccountReceiver,
                    new IntentFilter(SettingConst.ACTION_UPDATE_ACCOUNT_INFO));
        }
    }

    private void unregisterLoginBroadcast() {
        if (mAccountReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mAccountReceiver);
        }
    }

    private final class AccountBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_UPDATE_ACCOUNT_INFO.equals(action)) {
                updateCellphone();
            }
        }
    }
}