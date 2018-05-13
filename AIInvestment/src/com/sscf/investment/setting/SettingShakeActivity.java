package com.sscf.investment.setting;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IIntelligentShakeManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.widget.CommonAdapter;
import com.sscf.investment.widget.CommonViewHolder;
import java.util.Arrays;
import java.util.List;

/**
 * davidwei
 * 灯塔表哥摇一摇设置的界面
 */
public final class SettingShakeActivity extends BaseFragmentActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    private CheckBox mShakeSwitch;
    private TextView mShakeSensorTitle;
    private ListView mListView;

    private IIntelligentShakeManager mShakeManager;

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
        setContentView(R.layout.activity_setting_shake);
        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.setting_shake);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        mShakeSwitch = (CheckBox) findViewById(R.id.settingSwitchShake);
        mShakeSensorTitle = (TextView) findViewById(R.id.settingShakeSensorTitle);
        final ListView listView = (ListView) findViewById(R.id.settingShakeSensorList);

        final boolean checked = mShakeManager.isEnable();
        mShakeSwitch.setChecked(checked);
        mShakeSwitch.setOnCheckedChangeListener(this);

        initListViews(listView);
        mListView = listView;

        if (checked) {
            mShakeSensorTitle.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.VISIBLE);
        } else {
            mShakeSensorTitle.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
        }
    }

    private void initListViews(ListView listView) {
        final Resources res = getResources();
        final String[] textArray = res.getStringArray(R.array.setting_shake_sensor_text_array);
        final int[] values = getResources().getIntArray(R.array.setting_shake_sensor_array);

        final SettingShakeAdapter adapter = new SettingShakeAdapter(this, Arrays.asList(textArray),
                values, mShakeManager.getSensorThreshold());
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.settingSwitchShake:
                final IIntelligentShakeManager shakeManager = mShakeManager;
                shakeManager.setEnable(isChecked);
                if (isChecked) {
                    shakeManager.registerShakeListener(this, null);
                    mShakeSensorTitle.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.VISIBLE);
                } else {
                    shakeManager.unregisterShakeListener();
                    mShakeSensorTitle.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }
}

final class SettingShakeAdapter extends CommonAdapter<String> implements AdapterView.OnItemClickListener {
    private int mCheckedPosition;
    private final int[] mValues;

    public SettingShakeAdapter(Context context, List<String> datas, int[] values, int checkedValue) {
        super(context, datas, R.layout.activity_setting_list_item);
        mValues = values;
        mCheckedPosition = getPositionByValue(checkedValue);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mCheckedPosition == position) {
            return;
        }

        final CheckedTextView itemView = (CheckedTextView) view;
        ((CheckedTextView) parent.getChildAt(mCheckedPosition)).setChecked(false);
        itemView.setChecked(true);
        final int value = getValueByPosition(position);
        final IIntelligentShakeManager shakeManager = (IIntelligentShakeManager) ComponentManager.getInstance()
                .getManager(IIntelligentShakeManager.class.getName());
        if (shakeManager != null) {
            shakeManager.setSensorThreshold(value);
        }
        mCheckedPosition = position;
    }

    public int getValueByPosition(final int position) {
        return mValues[position];
    }

    public int getPositionByValue(final int value) {
        return getPositionByValue(mValues, value);
    }

    public static int getPositionByValue(final int[] values, final int value) {
        final int length = values.length;
        for (int i = 0; i < length; i++) {
            if (values[i] == value) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void convert(CommonViewHolder holder, String item, int position) {
        final CheckedTextView itemView = holder.getView(R.id.settingRefreshFrequencyListItem);
        itemView.setText(item);
        itemView.setChecked(position == mCheckedPosition);
    }
}