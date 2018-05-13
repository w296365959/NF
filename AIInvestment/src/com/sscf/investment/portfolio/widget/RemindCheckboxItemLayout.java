package com.sscf.investment.portfolio.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;

public final class RemindCheckboxItemLayout extends LinearLayout
        implements CompoundButton.OnCheckedChangeListener {
    private TextView mTextView;
    private CheckBox mCheckbox;

    private final int mFocusColor;
    private final int mUnfocusColor;

    public RemindCheckboxItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mFocusColor = ContextCompat.getColor(getContext(), R.color.default_text_color_100);
        mUnfocusColor = ContextCompat.getColor(getContext(), R.color.default_text_color_60);
    }

    @Override
    protected void onFinishInflate() {
        mTextView = (TextView) findViewById(R.id.portfolioRemindTextView);
        mCheckbox = (CheckBox) findViewById(R.id.portfolioRemindCheckbox);
        mCheckbox.setOnCheckedChangeListener(this);
    }

    public void setValue(final int stringId, final boolean checked) {
        mTextView.setText(stringId);
        mCheckbox.setChecked(checked);
    }

    public boolean isChecked() {
        return mCheckbox.isChecked();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mTextView.setTextColor(isChecked ? mFocusColor : mUnfocusColor);
    }
}
