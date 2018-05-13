package com.sscf.investment.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;

/**
 * davidwei
 * 消息通知设置界面
 */
public final class SettingMessageActivity extends BaseFragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private CheckBox mPushSwitch;
    private CheckBox mImportantNewsSwitch;
    private CheckBox mNewSharesSwitch;

    private View mPushSubSwitchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_message);
        initViews();
        StatisticsUtil.reportAction(StatisticsConst.SETTING_PUSH_MESSAGE_DISPLAY);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.setting_message);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        mPushSwitch = (CheckBox) findViewById(R.id.settingPushSwitch);
        final boolean isChecked = SettingPref.getIBoolean(SettingConst.KEY_SETTING_PUSH_SWITCH, true);
        mPushSwitch.setChecked(isChecked);
        mPushSwitch.setOnCheckedChangeListener(this);

        mPushSubSwitchLayout = findViewById(R.id.settingMessageSubSwitchLayout);
        mPushSubSwitchLayout.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);

        mImportantNewsSwitch = (CheckBox) findViewById(R.id.settingPushImportantNews);
        mImportantNewsSwitch.setChecked(SettingPref.getIBoolean(SettingConst.KEY_SETTING_PUSH_IMPORTANT_NEWS_SWITCH, true));
        mImportantNewsSwitch.setOnCheckedChangeListener(this);
        mNewSharesSwitch = (CheckBox) findViewById(R.id.settingPushNewShares);
        mNewSharesSwitch.setChecked(SettingPref.getIBoolean(SettingConst.KEY_SETTING_PUSH_NEW_SHARES_SWITCH, true));
        mNewSharesSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.settingPushSwitch:
                SettingPref.putIBoolean(SettingConst.KEY_SETTING_PUSH_SWITCH, isChecked);
                mPushSubSwitchLayout.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                mImportantNewsSwitch.setChecked(isChecked);
                mNewSharesSwitch.setChecked(isChecked);
                break;
            case R.id.settingPushImportantNews:
                SettingPref.putIBoolean(SettingConst.KEY_SETTING_PUSH_IMPORTANT_NEWS_SWITCH, isChecked);
                if (!isChecked) {
                    StatisticsUtil.reportAction(StatisticsConst.SETTING_PUSH_IMPORTANT_NEWS_SWITCH_OFF);
                }
                break;
            case R.id.settingPushNewShares:
                SettingPref.putIBoolean(SettingConst.KEY_SETTING_PUSH_NEW_SHARES_SWITCH, isChecked);
                if (!isChecked) {
                    StatisticsUtil.reportAction(StatisticsConst.SETTING_PUSH_NEW_SHARES_SWITCH_OFF);
                }
                break;
            default:
                break;
        }
    }
}
