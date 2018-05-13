package com.sscf.investment.setting;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.setting.manager.KLineSettingManager;
import com.sscf.investment.setting.model.IndicatorConfigure;
import com.sscf.investment.setting.model.KLineSettingConfigure;
import com.sscf.investment.setting.widgt.SeekBarLayout;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;

/**
 * davidwei
 * KDJ设置
 */
public final class SettingKDJActivity extends BaseFragmentActivity implements View.OnClickListener {
    private SeekBarLayout mSeekBarLayout1;
    private SeekBarLayout mSeekBarLayout2;
    private SeekBarLayout mSeekBarLayout3;

    private KLineSettingManager mKlineSettingManager;

    private IndicatorConfigure mIndicatorConfigure;

    private String[] strValues;

    private String oldValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_kdj);
        mKlineSettingManager = DengtaApplication.getApplication().getKLineSettingManager();
        mIndicatorConfigure = mKlineSettingManager.getConfigureByName("KDJ");
        oldValues = mIndicatorConfigure.values;
        strValues = mIndicatorConfigure.values.split(",");
        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StringBuffer sb = new StringBuffer();
        sb.append(mSeekBarLayout1.getValue());
        sb.append(",");
        sb.append(mSeekBarLayout2.getValue());
        sb.append(",");
        sb.append(mSeekBarLayout3.getValue());

        if (!sb.toString().equals(oldValues)) {
            mIndicatorConfigure.values = sb.toString();
            StatisticsUtil.reportAction(StatisticsConst.SETTING_K_LINE_SETTING_CHANGED);
            mKlineSettingManager.saveConfigure();
        }
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.index_type_kdj);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        final TextView rightButton = ((TextView) findViewById(R.id.actionbar_right_button));
        rightButton.setText(R.string.setting_recovery_default);
        rightButton.setOnClickListener(this);

        final String textLeft = "";
        final int maxLength = textLeft.length();
        final String textRight = getString(R.string.subject_time_daily);

        mSeekBarLayout1 = (SeekBarLayout) findViewById(R.id.settingKDJ1);
        mSeekBarLayout1.setText(textLeft, maxLength, textRight);
        mSeekBarLayout1.setValueRange(1, 100);
        mSeekBarLayout1.setValue(Integer.valueOf(strValues[0]));

        mSeekBarLayout2 = (SeekBarLayout) findViewById(R.id.settingKDJ2);
        mSeekBarLayout2.setText(textLeft, maxLength, textRight);
        mSeekBarLayout2.setValueRange(2, 40);
        mSeekBarLayout2.setValue(Integer.valueOf(strValues[1]));

        mSeekBarLayout3 = (SeekBarLayout) findViewById(R.id.settingKDJ3);
        mSeekBarLayout3.setText(textLeft, maxLength, textRight);
        mSeekBarLayout3.setValueRange(2, 40);
        mSeekBarLayout3.setValue(Integer.valueOf(strValues[2]));

        ((TextView) findViewById(R.id.title)).setText(R.string.index_type_kdj);

        new Handler().postDelayed(new Runnable() { // 解决三星s6 edge更换字体后字体被截断的问题
            @Override
            public void run() {
                ((TextView) findViewById(R.id.intro)).setText(R.string.setting_kdj_intro);
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
                mSeekBarLayout1.setValue(SettingConst.DEFAULT_SETTING_KDJ_1);
                mSeekBarLayout2.setValue(SettingConst.DEFAULT_SETTING_KDJ_2);
                mSeekBarLayout3.setValue(SettingConst.DEFAULT_SETTING_KDJ_3);
                break;
            default:
                break;
        }
    }
}