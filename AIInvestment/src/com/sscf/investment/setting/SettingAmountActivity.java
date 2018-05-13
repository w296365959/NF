package com.sscf.investment.setting;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.setting.manager.KLineSettingManager;
import com.sscf.investment.setting.model.IndicatorConfigure;
import com.sscf.investment.setting.widgt.SeekBarLayout;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;

/**
 * davidwei
 * AMOUNT均线设置
 */
public final class SettingAmountActivity extends BaseFragmentActivity implements View.OnClickListener {
    private static final int DEFAULT_AMOUNT_COUNT = 2;
    private static final int AMOUNT_MAX_COUNT = 3;
    private int mAMOUNTCount;
    private final SeekBarLayout[] mSeekBarLayouts = new SeekBarLayout[AMOUNT_MAX_COUNT];
    private final int[] mDefaultValues = new int[AMOUNT_MAX_COUNT];

    private LinearLayout mSettingAMOUNTLayout;
    private View mAddAMOUNTButton;

    /**
     * 统计用的
     */
    private int[] mSrcValues = new int[AMOUNT_MAX_COUNT];

    private KLineSettingManager mKlineSettingManager;

    private IndicatorConfigure mConfigure;

    private String oldValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_amount);
        mKlineSettingManager = DengtaApplication.getApplication().getKLineSettingManager();
        mConfigure = mKlineSettingManager.getConfigureByName("成交量");
        oldValues = mConfigure.values;
        initDefaultData();
        initViews();
    }

    private void initDefaultData() {
        mDefaultValues[0] = SettingConst.DEFAULT_SETTING_AMOUNT_1;
        mDefaultValues[1] = SettingConst.DEFAULT_SETTING_AMOUNT_2;
        mDefaultValues[2] = SettingConst.DEFAULT_SETTING_AMOUNT_3;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceUtil.hideInputMethod(mSettingAMOUNTLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StringBuffer sb = new StringBuffer();
        for (int i = 0 ; i < mSeekBarLayouts.length ; i++){
            if (null != mSeekBarLayouts[i]){
                sb.append(mSeekBarLayouts[i].getValue() + (i == mSeekBarLayouts.length - 1 ? "" : ","));
            }
        }
        if (!sb.toString().equals(oldValues)) {
            mConfigure.values = sb.toString();
            mKlineSettingManager.saveConfigure();
            StatisticsUtil.reportAction(StatisticsConst.SETTING_K_LINE_SETTING_CHANGED);
        }
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.stock_detail_volume);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        final TextView rightButton = ((TextView) findViewById(R.id.actionbar_right_button));
        rightButton.setText(R.string.setting_recovery_default);
        rightButton.setOnClickListener(this);

        final Resources res = getResources();

        final int MIN_SETTING_AMOUNT = 1;
        final int AMOUNTX_SETTING_AMOUNT = 250;

        final String textLeft = "";
        final int maxLength = textLeft.length();
        final String textRight = res.getString(R.string.setting_ma_text_right);

        mSettingAMOUNTLayout = (LinearLayout) findViewById(R.id.settingAMOUNTLayout);
        SeekBarLayout seekBarLayout = null;
        LinearLayout.LayoutParams params = null;
        int value = 0;
        String[] strValues = oldValues.split(",");
        for (int i = 0; i < AMOUNT_MAX_COUNT; i++) {
            value = i < strValues.length ? Integer.valueOf(strValues[i]) : 0;
            mSrcValues[i] = value;
            if (value > 0) {
                seekBarLayout = (SeekBarLayout) View.inflate(this, R.layout.activity_setting_seekbar_layout, null);
                mSeekBarLayouts[i] = seekBarLayout;
                seekBarLayout.setText(textLeft, maxLength, textRight);
                seekBarLayout.setValueRange(MIN_SETTING_AMOUNT, AMOUNTX_SETTING_AMOUNT);
                seekBarLayout.setValue(value);
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mSettingAMOUNTLayout.addView(seekBarLayout, params);
                mAMOUNTCount++;
            }
        }

        mAddAMOUNTButton = findViewById(R.id.addAMOUNTButton);
        mAddAMOUNTButton.setOnClickListener(this);
        if (mAMOUNTCount == AMOUNT_MAX_COUNT) {
            mAddAMOUNTButton.setVisibility(View.GONE);
        }

        ((TextView) findViewById(R.id.title)).setText(R.string.stock_detail_volume);
        new Handler().postDelayed(new Runnable() { // 解决三星s6 edge更换字体后字体被截断的问题
            @Override
            public void run() {
                ((TextView) findViewById(R.id.intro)).setText(R.string.setting_amount_intro);
            }
        }, 0L);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.actionbar_right_button:
                SeekBarLayout seekBarLayout = null;
                for (int i = 0; i < DEFAULT_AMOUNT_COUNT; i++) {
                    mSeekBarLayouts[i].setValue(mDefaultValues[i]);
                }
                for (int i = DEFAULT_AMOUNT_COUNT; i < AMOUNT_MAX_COUNT; i++) {
                    mSettingAMOUNTLayout.removeView(mSeekBarLayouts[i]);
                    mSeekBarLayouts[i] = null;
                }
                mAMOUNTCount = DEFAULT_AMOUNT_COUNT;
                mAddAMOUNTButton.setVisibility(View.VISIBLE);
                DeviceUtil.hideInputMethod(mSettingAMOUNTLayout);
                break;
            case R.id.addAMOUNTButton:
                int value = 0;
                switch (mAMOUNTCount) {
                    case 2:
                        value = 20;
                        break;
                    default:
                        return;
                }

                final Resources res = getResources();

                final int MIN_SETTING_AMOUNT = 1;
                final int MAX_SETTING_AMOUNT = 250;

                final String textLeft = "";
                final int maxLength = textLeft.length();
                final String textRight = res.getString(R.string.setting_ma_text_right);

                seekBarLayout = (SeekBarLayout) View.inflate(this, R.layout.activity_setting_seekbar_layout, null);
                mSeekBarLayouts[mAMOUNTCount] = seekBarLayout;
                seekBarLayout.setText(textLeft, maxLength, textRight);
                seekBarLayout.setValueRange(MIN_SETTING_AMOUNT, MAX_SETTING_AMOUNT);
                seekBarLayout.setValue(mDefaultValues[mAMOUNTCount]);
                seekBarLayout.setValue(value);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mSettingAMOUNTLayout.addView(seekBarLayout, params);
                seekBarLayout.showInputMethod();
                mAMOUNTCount++;

                if (mAMOUNTCount == AMOUNT_MAX_COUNT) {
                    mAddAMOUNTButton.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }
}