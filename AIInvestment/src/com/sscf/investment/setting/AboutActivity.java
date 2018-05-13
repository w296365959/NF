package com.sscf.investment.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.manager.RedDotManager;
import com.sscf.investment.setting.widgt.DeveloperSettingPref;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.utils.ViewUtils;
import com.sscf.investment.web.CommonWebConst;

import BEC.UpgradeInfo;

/**
 * davidwei
 * 关于界面
 */
public final class AboutActivity extends BaseFragmentActivity implements View.OnClickListener, Handler.Callback {
    private UpgradeInfo mUpgradeInfo;
    private BroadcastReceiver mLocalReceiver;
    private View mCheckUpgradeRetDot;

    private Handler mHandler;
    private int mClickAboutCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_about);
        initViews();
        registerLocalBroadcast();

        mHandler = new Handler(this);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterLocalBroadcast();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.aboutTitle)).setText(R.string.setting_about);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        findViewById(R.id.settingCheckUpgradeLayout).setOnClickListener(this);
        findViewById(R.id.aboutBgLayout).setOnClickListener(this);

        // 功能介绍
        ViewUtils.initSettingItem(this, R.id.settingIntro, R.string.setting_about_intro, this);
        // 用户协议
        ViewUtils.initSettingItem(this, R.id.settingUserProtocal, R.string.setting_user_protocal,this);
        if (DeveloperSettingPref.getShowDeveloperSettings()) {
            findViewById(R.id.setting_about_developer_settings_layout_id).setVisibility(View.VISIBLE);
            findViewById(R.id.setting_about_developer_settings_id).setOnClickListener(this);
        } else {
            findViewById(R.id.setting_about_developer_settings_layout_id).setVisibility(View.GONE);
        }

        mCheckUpgradeRetDot = findViewById(R.id.settingCheckUpgradeRetDot);
        final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
        mCheckUpgradeRetDot.setVisibility(redDotManager.getUpgradeState() ? View.VISIBLE : View.INVISIBLE);

        updateUpgradeHintText();
    }

    private void updateUpgradeHintText() {
        final UpgradeInfo upgradeInfo = DengtaApplication.getApplication().getUpgradeManager().getUpgradeInfo();
        if (upgradeInfo != null) {
            final SpannableString hintText = new SpannableString(getString(R.string.setting_upgrade_hint, upgradeInfo.sVersionName));
            hintText.setSpan(StringUtil.getUpStyle(), 0, hintText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ((TextView) findViewById(R.id.settingCheckHint)).setText(hintText);
        }
        mUpgradeInfo = upgradeInfo;
    }

    private void registerLocalBroadcast() {
        if (mLocalReceiver == null) {
            mLocalReceiver = new LocalBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter(SettingConst.ACTION_UPGRADE_INFO_CHANGED);
            intentFilter.addAction(SettingConst.ACTION_RED_DOT_STATE_CHANGED);
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mLocalReceiver, intentFilter);
        }
    }

    private void unregisterLocalBroadcast() {
        if (mLocalReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mLocalReceiver);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.settingIntro:
                if (TimeUtils.isFrequentOperation()) {
                    return;
                }
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_INTRODUCE);
                WebBeaconJump.showWebActivity(this, CommonWebConst.URL_FAQ_FUND_INTRODUCE, CommonWebConst.WT_FUNCTION_INTRODUCE);
                break;
            case R.id.settingUserProtocal:
                if (TimeUtils.isFrequentOperation()) {
                    return;
                }
                WebBeaconJump.showWebActivity(this, DengtaApplication.getApplication().getUrlManager().getUserAgreementUrl(), CommonWebConst.WT_USER_AGREEMENT);
                break;
            case R.id.setting_about_developer_settings_id:
                Intent developerSettingsIntent = new Intent(this, DeveloperSettingsActivity.class);
                startActivity(developerSettingsIntent);
                break;
            case R.id.settingCheckUpgradeLayout:
                if (TimeUtils.isFrequentOperation()) {
                    return;
                }
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_CHECK_UPGRADE);
                if (mUpgradeInfo != null) {
                    CommonBeaconJump.showUpgrade(this);
                } else {
                    if (!NetUtil.isNetWorkConnected(this)) {
                        DengtaApplication.getApplication().showToast(R.string.error_tips_network_error);
                        return;
                    }
                    DengtaApplication.getApplication().showToast(R.string.setting_about_checking_upgrade);
                    DengtaApplication.getApplication().getUpgradeManager().reqUpdate(0);
                }
                break;
            case R.id.aboutBgLayout:
                if (!DeveloperSettingPref.getShowDeveloperSettings()) {
                    mClickAboutCount++;
                    if (mClickAboutCount >= 7) {
                        mClickAboutCount = 0;
                        findViewById(R.id.setting_about_developer_settings_layout_id).setVisibility(View.VISIBLE);
                        findViewById(R.id.setting_about_developer_settings_id).setOnClickListener(this);
                        DeveloperSettingPref.setShowDeveloperSettings(true);
                        DengtaApplication.getApplication().showToast(R.string.setting_about_developer_settings_used);
                    }
                    mHandler.removeMessages(0);
                    mHandler.sendEmptyMessageDelayed(0, 1000L);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        mClickAboutCount = 0;
        return true;
    }

    private final class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_RED_DOT_STATE_CHANGED.equals(action)) {
                final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
                mCheckUpgradeRetDot.setVisibility(redDotManager.getUpgradeState() ? View.VISIBLE : View.INVISIBLE);
            } else if (SettingConst.ACTION_UPGRADE_INFO_CHANGED.equals(action)) {
                updateUpgradeHintText();
                if (mUpgradeInfo == null) {
                    DengtaApplication.getApplication().showToast(R.string.setting_about_newest);
                } else {
                    DengtaApplication.getApplication().showToast(R.string.upgrade_new_version);
                }
            }
        }
    }
}