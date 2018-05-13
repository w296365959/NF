package com.sscf.investment.setting;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.setting.model.KlineSettingConst;
import com.sscf.investment.setting.widgt.SettingSelectAdapter;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;

import java.util.Arrays;

/**
 * davidwei
 *
 */
@Deprecated
public final class SettingSelectListActivity extends BaseFragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_select_list);
        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        final String type = getIntent().getStringExtra("type");
        int textId = 0;
        int textArrayId = 0;
        int defaultValue = 0;
        int[] values = null;

//        if (SettingConst.KEY_SETTING_RIGHTS_OFFERING.equals(type)) {
//            textId = R.string.setting_rights_offering;
//            textArrayId = R.array.setting_k_line_rights_offering_text_array;
//            values = new int[] { KlineSettingConst.K_LINE_UN_REPAIR, KlineSettingConst.K_LINE_REPAIR };
//            defaultValue = KlineSettingConst.DEFAULT_K_LINE_REPAIR_TYPE;
//        } else if (SettingConst.KEY_SETTING_INDICATORS_TYPE_0.equals(type)) {
//            textId = R.string.setting_indicators_type;
//            textArrayId = R.array.setting_k_line_indicators_type_text_array;
//            values = new int[] { SettingConst.K_LINE_INDICATOR_VOLUME, SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW, SettingConst.K_LINE_INDICATOR_MACD,
//                    SettingConst.K_LINE_INDICATOR_KDJ, SettingConst.K_LINE_INDICATOR_RSI,
//                    SettingConst.K_LINE_INDICATOR_BOLL, SettingConst.K_LINE_INDICATOR_DMI, SettingConst.K_LINE_INDICATOR_CCI,
//                    SettingConst.K_LINE_INDICATOR_ENE,SettingConst.K_LINE_INDICATOR_DMA,SettingConst.K_LINE_INDICATOR_EXPMA,
//                    SettingConst.K_LINE_INDICATOR_VR,SettingConst.K_LINE_INDICATOR_BBI,SettingConst.K_LINE_INDICATOR_OBV,
//                    SettingConst.K_LINE_INDICATOR_BIAS, SettingConst.K_LINE_INDICATOR_WR, SettingConst.K_LINE_INDICATOR_BREAK};
//            defaultValue = SettingConst.DEFAULT_K_LINE_INDICATOR_TYPE;
//        } else {
//            finish();
//            return;
//        }
//
//        ((TextView) findViewById(R.id.actionbar_title)).setText(textId);
//        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
//
//        final ListView listView = (ListView) findViewById(android.R.id.list);
//        initListViews(listView, textArrayId, values, defaultValue, type);
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