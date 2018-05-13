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
 * 直播开关设置界面
 */
public final class SettingLiveActivity extends BaseFragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private CheckBox mLiveSwitch;
    private CheckBox mPortfolioLiveSwitch;
    private CheckBox mMainBoardLiveSwitch;
    private CheckBox mStockLiveSwitch;

    private View mLiveSubSwitchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_live);
        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.setting_live);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        mLiveSwitch = (CheckBox) findViewById(R.id.settingLiveSwitch);

        final boolean isChecked = SettingPref.getIBoolean(SettingConst.KEY_SETTING_LIVE_SWITCH, true);
        mLiveSwitch.setChecked(isChecked);
        mLiveSwitch.setOnCheckedChangeListener(this);

        mLiveSubSwitchLayout = findViewById(R.id.settingLiveSubSwitchLayout);
        mLiveSubSwitchLayout.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);

        mPortfolioLiveSwitch = (CheckBox) findViewById(R.id.settingPortfolioLiveSwitch);
        mPortfolioLiveSwitch.setChecked(SettingPref.getIBoolean(SettingConst.KEY_SETTING_PORTFOLIO_LIVE_SWITCH, true));
        mPortfolioLiveSwitch.setOnCheckedChangeListener(this);

        mMainBoardLiveSwitch = (CheckBox) findViewById(R.id.settingMainBoardLiveSwitch);
        mMainBoardLiveSwitch.setChecked(SettingPref.getIBoolean(SettingConst.KEY_SETTING_MAIN_BOARD_LIVE_SWITCH, true));
        mMainBoardLiveSwitch.setOnCheckedChangeListener(this);

        mStockLiveSwitch = (CheckBox) findViewById(R.id.settingStockLiveSwitch);
        mStockLiveSwitch.setChecked(SettingPref.getIBoolean(SettingConst.KEY_STOCK_LIVE_SWITCH, true));
        mStockLiveSwitch.setOnCheckedChangeListener(this);
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
            case R.id.settingLiveSwitch:
                SettingPref.putIBoolean(SettingConst.KEY_SETTING_LIVE_SWITCH, isChecked);
                mLiveSubSwitchLayout.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                mPortfolioLiveSwitch.setChecked(isChecked);
                mMainBoardLiveSwitch.setChecked(isChecked);
                mStockLiveSwitch.setChecked(isChecked);
                if (!isChecked) {
                    StatisticsUtil.reportAction(StatisticsConst.SETTING_LIVE_SWITCH_OFF);
                }
                break;
            case R.id.settingPortfolioLiveSwitch:
                SettingPref.putIBoolean(SettingConst.KEY_SETTING_PORTFOLIO_LIVE_SWITCH, isChecked);
                if (!isChecked) {
                    StatisticsUtil.reportAction(StatisticsConst.SETTING_LIVE_PORTFOLIO_LIVE_SWITCH_OFF);
                }
                break;
            case R.id.settingMainBoardLiveSwitch:
                SettingPref.putIBoolean(SettingConst.KEY_SETTING_MAIN_BOARD_LIVE_SWITCH, isChecked);
                if (!isChecked) {
                    StatisticsUtil.reportAction(StatisticsConst.SETTING_LIVE_MAIN_BOARD_LIVE_SWITCH_OFF);
                }
                break;
            case R.id.settingStockLiveSwitch:
                SettingPref.putIBoolean(SettingConst.KEY_STOCK_LIVE_SWITCH, isChecked);
                if (!isChecked) {
                    StatisticsUtil.reportAction(StatisticsConst.SETTING_LIVE_STOCK_LIVE_SWITCH_OFF);
                }
                break;
            default:
                break;
        }
    }
}
