package com.sscf.investment.setting;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.widgt.SettingSelectAdapter;

import java.util.Arrays;

/**
 * davidwei
 * 行情刷新频率设置的界面
 */
public final class SettingRefreshFrequencyActivity extends BaseFragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_refresh_frequency);
        initViews();
        StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_MORE_KLINE_SETTINGS_DETAILS);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.setting_refresh_frequency);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        initRefreshFrequencyMobileNetList();

        initRefreshFrequencyWifiList();
    }

    private void initRefreshFrequencyMobileNetList() {
        // 初始化2G/3G/4G的刷新频率
        final int[] values = getResources().getIntArray(R.array.setting_refresh_frequency_mobile_net_seconds_array);
        final ListView listView = (ListView) findViewById(R.id.refreshFrequencyMobileNetList);

        initListViews(listView, R.array.setting_refresh_frequency_mobile_net_text_array, values,
                SettingConst.DEFAULT_REFRESH_FREQUENCY_MOBILE_NET_SECONDS, SettingConst.KEY_REFRESH_FREQUENCY_MOBILE_NET_SECONDS);
    }

    private void initRefreshFrequencyWifiList() {
        // 初始化wifi的刷新频率
        final int[] values = getResources().getIntArray(R.array.setting_refresh_frequency_wifi_seconds_array);
        final ListView listView = (ListView) findViewById(R.id.refreshFrequencyWifiList);

        initListViews(listView, R.array.setting_refresh_frequency_wifi_text_array, values,
                SettingConst.DEFAULT_REFRESH_FREQUENCY_WIFI_SECONDS, SettingConst.KEY_REFRESH_FREQUENCY_WIFI_SECONDS);
    }

    private void initListViews(ListView listView, int textArrayId, int[] values, int defaultValue, String key) {
        final Resources res = getResources();
        final String[] textArray = res.getStringArray(textArrayId);
        final int value = SettingPref.getInt(key, defaultValue);

        final SettingSelectAdapter adapter = new SettingSelectAdapter(this, Arrays.asList(textArray),
                values, R.layout.activity_setting_list_item, value, key);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);
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
}