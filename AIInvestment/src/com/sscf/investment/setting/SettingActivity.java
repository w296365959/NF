package com.sscf.investment.setting;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IIntelligentShakeManager;
import com.dengtacj.component.managers.IThemeManager;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.PackageUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.manager.AccountRequestManager;
import com.sscf.investment.setting.manager.RedDotManager;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.utils.ViewUtils;

/**
 * davidwei
 * 设置界面
 */
public final class SettingActivity extends BaseFragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        CommonDialog.OnDialogButtonClickListener, DataSourceProxy.IRequestCallback {
    private View mAboutRetDot;

    private View mAccountView;
    private View mLogoutView;
    private CheckBox mNightModeSwitch;

    private TextView mClearSizeView;

    private BroadcastReceiver mLocalReceiver;

    private Dialog mLogoutConfirmDialog;

    private long mCacheLength;

    private IIntelligentShakeManager mShakeManager;

    private CheckBox mShakeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final IIntelligentShakeManager shakeManager = (IIntelligentShakeManager) ComponentManager.getInstance()
                .getManager(IIntelligentShakeManager.class.getName());
        if (shakeManager == null) {
            finish();
            return;
        }
        mShakeManager = shakeManager;
        setContentView(R.layout.activity_setting_setting_detail);
        initViews();
        registerLocalBroadcast();
        StatisticsUtil.reportAction(StatisticsConst.SETTING_DETAIL_DISPLAY);

        new GetCacheSizeTask().executeOnExecutor(DengtaApplication.getApplication().defaultExecutor);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.setting_detail_settings);
        mShakeSwitch = (CheckBox) findViewById(R.id.settingSwitchShake);
        final boolean checked = mShakeManager.isEnable();
        mShakeSwitch.setChecked(checked);
        mShakeSwitch.setOnCheckedChangeListener(this);

        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        // 账户安全
        mAccountView = ViewUtils.initSettingItem(this, R.id.settingAccount, R.string.setting_account, this);
        // 行情频率设置
        ViewUtils.initSettingItem(this, R.id.settingRefreshFrequency, R.string.setting_refresh_frequency, this);
        // K线设置
        ViewUtils.initSettingItem(this, R.id.settingKLine, R.string.setting_k_line_settings, this);
        // 灯塔直播设置
        ViewUtils.initSettingItem(this, R.id.settingLive, R.string.setting_live, this);
        // 灯塔表哥设置
//        ViewUtils.initSettingItem(this, R.id.settingShake, R.string.setting_shake, this);
        // 消息通知设置
        ViewUtils.initSettingItem(this, R.id.settingMessage, R.string.setting_message, this);
        // 意见反馈
        ViewUtils.initSettingItem(this, R.id.settingFeedback, R.string.setting_feedback, this);
        // 退出登录
        mLogoutView = findViewById(R.id.settingLogout);
        mLogoutView.setOnClickListener(this);
        // 关于
        findViewById(R.id.settingAboutLayout).setOnClickListener(this);
        // 清除缓存
        findViewById(R.id.settingClearLayout).setOnClickListener(this);
        mClearSizeView = (TextView) findViewById(R.id.settingClearSize);

        if (!DengtaApplication.getApplication().getAccountManager().isLogined()) {
            mAccountView.setVisibility(View.GONE);
            mLogoutView.setVisibility(View.GONE);
        }

        mNightModeSwitch = (CheckBox) findViewById(R.id.settingSwitchDayNightMode);
        boolean isDefaultTheme = true;
        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        if (themeManager != null) {
            isDefaultTheme = themeManager.isDefaultTheme();
        }
        mNightModeSwitch.setChecked(!isDefaultTheme);
        mNightModeSwitch.setOnCheckedChangeListener(this);

        ((TextView) findViewById(R.id.settingVersionName)).setText(getString(R.string.setting_version_name, PackageUtil.getVersionName(this)));

        mAboutRetDot = findViewById(R.id.settingAboutRetDot);
        mAboutRetDot.setVisibility(DengtaApplication.getApplication().getRedDotManager().getAboutState()
                ? View.VISIBLE : View.INVISIBLE);
    }

    private void registerLocalBroadcast() {
        if (mLocalReceiver == null) {
            mLocalReceiver = new LocalBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter(SettingConst.ACTION_RED_DOT_STATE_CHANGED);
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mLocalReceiver, intentFilter);
        }
    }

    private void unregisterLocalBroadcast() {
        if (mLocalReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mLocalReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterLocalBroadcast();
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
            case R.id.settingAccount://账号安全
                startActivity(new Intent(this, ThirdPartyBindingActivity.class));
                break;
            case R.id.settingRefreshFrequency://行情刷新
                startActivity(new Intent(this, SettingRefreshFrequencyActivity.class));
                StatisticsUtil.reportAction(StatisticsConst.SETTING_REFRESH_FREQUENCY_DISPLAY);
                break;
            case R.id.settingKLine://k线设置
                startActivity(new Intent(this, SettingKLineActivity.class));
                StatisticsUtil.reportAction(StatisticsConst.SETTING_K_LINE_DISPLAY);
                break;
            case R.id.settingMessage://消息通知
                startActivity(new Intent(this, SettingMessageActivity.class));
                break;
//            case R.id.settingShake:
//                CommonBeaconJump.showActivity(this, SettingShakeActivity.class);
//                break;
            case R.id.settingFeedback://意见反馈
                WebBeaconJump.showFeedback(this);
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_FEEDBACK);
                break;
            case R.id.settingAboutLayout://关于
                startActivity(new Intent(this, AboutActivity.class));
                StatisticsUtil.reportAction(StatisticsConst.A_ME_INSTALL_ABOUT_AI);
                break;
            case R.id.settingClearLayout://清理缓存
                clearCache();
                break;
            case R.id.settingLive://直播设置
                startActivity(new Intent(this, SettingLiveActivity.class));
                StatisticsUtil.reportAction(StatisticsConst.A_ME_INSTALL_LIVE_CLICKED);
                break;
            case R.id.settingLogout://退出登录
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_LOGOUT);
                showLogoutConfirmDialog();
                break;
            default:
                break;
        }
    }

    private void clearCache() {
        if (mCacheLength > 0L) {
            showLoadingDialog();
            new ClearCacheTask().executeOnExecutor(DengtaApplication.getApplication().defaultExecutor);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.settingSwitchDayNightMode:
                SwitchNightModeMaskActivity.show(this);
                break;
            case R.id.settingSwitchShake:
                final IIntelligentShakeManager shakeManager = mShakeManager;
                shakeManager.setEnable(isChecked);
                if (isChecked) {
                    shakeManager.registerShakeListener(this, null);
                } else {
                    shakeManager.unregisterShakeListener();
                }
                break;
            default:
                break;
        }
    }

    private void showLogoutConfirmDialog() {
        if (isDestroy()) {
            return;
        }
        if (mLogoutConfirmDialog == null) {
            final CommonDialog dialog = new CommonDialog(this);
            dialog.setMessage(R.string.logout_confirm_msg);
            dialog.addButton(R.string.cancel);
            dialog.addButton(R.string.ok);
            dialog.setButtonClickListener(this);
            dialog.setCanCancelOnTouchOutside(true);
            mLogoutConfirmDialog = dialog;
        }
        mLogoutConfirmDialog.show();
    }

    private void dismissLogoutConfirmDialog() {
        if (mLogoutConfirmDialog != null) {
            mLogoutConfirmDialog.dismiss();
        }
    }

    @Override
    public void onDialogButtonClick(CommonDialog dialog, View view, int position) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        switch (position) {
            case 0:
                dismissLogoutConfirmDialog();
                break;
            case 1: // 退出登录的确认按钮
                dismissLogoutConfirmDialog();
                showLoadingDialog();

                new AsyncTask<Object, Object, Object>() {

                    @Override
                    protected Object doInBackground(Object[] params) {
                        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
                        final AccountManager accountManager = dengtaApplication.getAccountManager();
                        final long accountId = accountManager.getAccountId();
                        if (accountId > 0) {
                            if (NetUtil.isNetWorkConnected(dengtaApplication)) {
                                AccountRequestManager.logout(accountId, SettingActivity.this);
                            }

                            accountManager.removeAccountInfo();
                            StatisticsUtil.reportAction(StatisticsConst.SETTING_USER_INFO_CLICK_LOGOUT);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        dismissLoadingDialog();
                        mAccountView.setVisibility(View.GONE);
                        mLogoutView.setVisibility(View.GONE);
                    }
                }.executeOnExecutor(DengtaApplication.getApplication().defaultExecutor);
                break;
            default:
                break;
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
    }

    private final class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_RED_DOT_STATE_CHANGED.equals(action)) {
                final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
                mAboutRetDot.setVisibility(redDotManager.getAboutState() ? View.VISIBLE : View.INVISIBLE);
            }
        }
    }

    private final class GetCacheSizeTask extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object[] params) {
            long length = FileUtil.getFileLength(FileUtil.getPdfDir());
            length += FileUtil.getFileLength(FileUtil.getOldPdfDir());
            mCacheLength = length;
            return StringUtil.sizeToStringM(length);
        }

        @Override
        protected void onPostExecute(String length) {
            mClearSizeView.setText(length);
        }
    }

    private final class ClearCacheTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                FileUtil.cleanDirectory(FileUtil.getPdfDir());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                FileUtil.cleanDirectory(FileUtil.getOldPdfDir());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            mCacheLength = 0L;
            dismissLoadingDialog();
            mClearSizeView.setText(StringUtil.sizeToStringM(0));
        }
    }
}