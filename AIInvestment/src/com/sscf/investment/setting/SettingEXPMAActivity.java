package com.sscf.investment.setting;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.manager.KLineSettingManager;
import com.sscf.investment.setting.model.IndicatorConfigure;
import com.sscf.investment.setting.model.KLineSettingConfigure;
import com.sscf.investment.setting.widgt.SeekBarLayout;

/**
 * Created by LEN on 2018/3/20.
 */

public class SettingEXPMAActivity extends BaseFragmentActivity implements View.OnClickListener {
    private static final int DEFAULT_EXPMA_COUNT = 2;
    private static final int MAX_EXPMA_COUNT = 4;
    private int mMACount;
    private final SeekBarLayout[] mSeekBarLayouts = new SeekBarLayout[MAX_EXPMA_COUNT];
    private final int[] mDefaultValues = new int[MAX_EXPMA_COUNT];

    private LinearLayout mSettingMALayout;
    private View mAddMAButton;

    /**
     * 统计用的
     */
    private int[] mSrcValues = new int[MAX_EXPMA_COUNT];

    private KLineSettingManager mKlineSettingManager;

    private IndicatorConfigure mConfigure;

    private String oldValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_ma);
        mKlineSettingManager = DengtaApplication.getApplication().getKLineSettingManager();
        mConfigure = mKlineSettingManager.getConfigureByName("EXPMA");
        oldValues = mConfigure.values;
        initDefaultData();
        initViews();
    }

    private void initDefaultData() {
        mDefaultValues[0] = SettingConst.DEFAULT_SETTING_EXPMA_1;
        mDefaultValues[1] = SettingConst.DEFAULT_SETTING_EXPMA_2;
        mDefaultValues[2] = SettingConst.DEFAULT_SETTING_EXPMA_3;
        mDefaultValues[3] = SettingConst.DEFAULT_SETTING_EXPMA_4;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceUtil.hideInputMethod(mSettingMALayout);
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
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.index_type_expma);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        final TextView rightButton = ((TextView) findViewById(R.id.actionbar_right_button));
        rightButton.setText(R.string.setting_recovery_default);
        rightButton.setOnClickListener(this);

        final Resources res = getResources();

        final int MIN_SETTING_MA = 1;
        final int MAX_SETTING_MA = 250;

        final String textLeft = "";
        final int maxLength = textLeft.length();
        final String textRight = res.getString(R.string.setting_ma_text_right);

        mSettingMALayout = (LinearLayout) findViewById(R.id.settingMALayout);
        SeekBarLayout seekBarLayout = null;
        LinearLayout.LayoutParams params = null;
        int value = 0;
        String[] strValues = oldValues.split(",");
        for (int i = 0; i < MAX_EXPMA_COUNT; i++) {
            value = i < strValues.length ? Integer.valueOf(strValues[i]) : 0;
            mSrcValues[i] = value;
            if (value > 0) {
                seekBarLayout = (SeekBarLayout) View.inflate(this, R.layout.activity_setting_seekbar_layout, null);
                mSeekBarLayouts[i] = seekBarLayout;
                seekBarLayout.setText(textLeft, maxLength, textRight);
                seekBarLayout.setValueRange(MIN_SETTING_MA, MAX_SETTING_MA);
                seekBarLayout.setValue(value);
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mSettingMALayout.addView(seekBarLayout, params);
                mMACount++;
            }
        }

        mAddMAButton = findViewById(R.id.addMAButton);
        mAddMAButton.setOnClickListener(this);
        if (mMACount == MAX_EXPMA_COUNT) {
            mAddMAButton.setVisibility(View.GONE);
        }

        ((TextView) findViewById(R.id.title)).setText(R.string.index_type_expma);
        new Handler().postDelayed(new Runnable() { // 解决三星s6 edge更换字体后字体被截断的问题
            @Override
            public void run() {
                ((TextView) findViewById(R.id.intro)).setText(R.string.setting_expma_intro);
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
                for (int i = 0; i < DEFAULT_EXPMA_COUNT; i++) {
                    mSeekBarLayouts[i].setValue(mDefaultValues[i]);
                }
                for (int i = DEFAULT_EXPMA_COUNT; i < MAX_EXPMA_COUNT; i++) {
                    mSettingMALayout.removeView(mSeekBarLayouts[i]);
                    mSeekBarLayouts[i] = null;
                }
                mMACount = DEFAULT_EXPMA_COUNT;
                mAddMAButton.setVisibility(View.VISIBLE);
                DeviceUtil.hideInputMethod(mSettingMALayout);
                break;
            case R.id.addMAButton:
                int value = 0;
                switch (mMACount) {
                    case 2:
                        value = 20;
                        break;
                    case 3:
                        value = 60;
                        break;
                    default:
                        return;
                }

                final Resources res = getResources();

                final int MIN_SETTING_MA = 1;
                final int MAX_SETTING_MA = 250;

                final String textLeft = "";
                final int maxLength = textLeft.length();
                final String textRight = res.getString(R.string.setting_ma_text_right);

                seekBarLayout = (SeekBarLayout) View.inflate(this, R.layout.activity_setting_seekbar_layout, null);
                mSeekBarLayouts[mMACount] = seekBarLayout;
                seekBarLayout.setText(textLeft, maxLength, textRight);
                seekBarLayout.setValueRange(MIN_SETTING_MA, MAX_SETTING_MA);
                seekBarLayout.setValue(mDefaultValues[mMACount]);
                seekBarLayout.setValue(value);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mSettingMALayout.addView(seekBarLayout, params);
                seekBarLayout.showInputMethod();
                mMACount++;

                if (mMACount == MAX_EXPMA_COUNT) {
                    mAddMAButton.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }
}