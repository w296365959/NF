package com.sscf.investment.setting.widgt;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.widget.CommonAdapter;
import com.sscf.investment.widget.CommonViewHolder;

import java.util.List;

/**
 * Created by davidwei on 2015-08-07.
 */
public final class SettingSelectAdapter extends CommonAdapter<String> implements AdapterView.OnItemClickListener {
    private int mCheckedPosition;
    private final String mPrefKey;
    private final int[] mValues;

    private OnSettingChangedListener mOnSettingChangedListener;

    public SettingSelectAdapter(Context context, List<String> datas, int[] values, int itemLayoutId, int checkedValue, String key) {
        super(context, datas, itemLayoutId);
        mValues = values;
        mCheckedPosition = getPositionByValue(checkedValue);
        mPrefKey = key;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mCheckedPosition == position) {
            return;
        }

        final CheckedTextView itemView = (CheckedTextView) view;
        // TODO 为什么要手动设置checked
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ((CheckedTextView) parent.getChildAt(i)).setChecked(false);
        }
        itemView.setChecked(true);
        final int value = getValueByPosition(position);
        SettingPref.putInt(mPrefKey, value);
        mCheckedPosition = position;

        if (mOnSettingChangedListener != null) {
            mOnSettingChangedListener.onSettingChanged(value);
        }
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
        final CheckedTextView itemView = ((CheckedTextView) holder.getView(R.id.settingRefreshFrequencyListItem));
        itemView.setText(item);
        itemView.setChecked(position == mCheckedPosition);
    }

    public void setOnSettingChangedListener(OnSettingChangedListener l) {
        this.mOnSettingChangedListener = l;
    }

    public interface OnSettingChangedListener {
        void onSettingChanged(final int value);
    }
}
