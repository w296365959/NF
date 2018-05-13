package com.sscf.investment.setting.widgt;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.utils.StringUtil;

/**
 * Created by davidwei on 2015-08-07.
 *
 */
public final class SeekBarLayout extends LinearLayout implements SeekBar.OnSeekBarChangeListener, TextWatcher {

    private EditText mEditText;
    private SeekBar mSeekBar;

    private int mMax;
    private int mMin;
    private int mSrcValue;
    private int mValue;

    public SeekBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.addTextChangedListener(this);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    /**
     *
     * @param text1
     * @param maxLength 计算字宽用的，方便textview对齐
     * @param text2
     */
    public void setText(final String text1, final int maxLength, final String text2) {
        final TextView textView1 = (TextView) findViewById(R.id.textView11);
        final TextView textView2 = (TextView) findViewById(R.id.textView13);
        if (TextUtils.isEmpty(text1)) {
            textView1.setVisibility(GONE);
        } else {
            textView1.setText(text1);
            textView1.setMinWidth((int) Math.ceil(maxLength * getResources().getDimension(R.dimen.font_size_14)));
            textView1.setVisibility(VISIBLE);
        }
        if (TextUtils.isEmpty(text2)) {
            textView2.setVisibility(GONE);
        } else {
            textView2.setText(text2);
            textView2.setVisibility(VISIBLE);
        }
    }

    public void setValueRange(final int min, final int max) {
        mMin = min;
        mMax = max;
        mSeekBar.setMax(max - min);
        final String maxStr = String.valueOf(max);
        ((TextView) findViewById(R.id.minView)).setText(String.valueOf(min));
        ((TextView) findViewById(R.id.maxView)).setText(maxStr);
        mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxStr.length() + 1) });
    }

    public int setValue(final int value, final int defaultValue){
        mSrcValue = value;
        setValue(value);
        return value;
    }

    public int setValue(final String key, final int defaultValue) {
        final int value = SettingPref.getInt(key, defaultValue);
        mSrcValue = value;
        setValue(value);
        return value;
    }

    public void setValue(final int value) {
        mSrcValue = value;
        mValue = value;
        mSeekBar.setProgress(valueToProgress(value));
    }

    public int getValue() {
        return mValue;
    }

    public boolean isValueChanged() {
        return mSrcValue != mValue;
    }

    private int valueToProgress(final int value) {
        return value - mMin;
    }

    private int progressToValue(final int progress) {
        return progress + mMin;
    }

    public void showInputMethod() {
        mEditText.requestFocus();
        DeviceUtil.showInputMethod(mEditText);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        final int value = progressToValue(progress);
        mValue = value;

        final String text = mEditText.getText().toString();
        final String valueText = String.valueOf(value);
        if (!TextUtils.equals(text, valueText)) {
            mEditText.setText(valueText);
            mEditText.setSelection(valueText.length());
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        final String text = mEditText.getText().toString();
        if (text.startsWith("0")) { // 去掉前面的0
            mEditText.setText(text.substring(1));
            return;
        }

        final int value = StringUtil.parseInt(text, mMin);
        if (value > mMax) {
            String.valueOf(mMax);
            final String maxStr = String.valueOf(mMax);
            mEditText.setText(maxStr);
            mEditText.setSelection(maxStr.length());
        } else {
            setValue(value);
        }
    }
}
