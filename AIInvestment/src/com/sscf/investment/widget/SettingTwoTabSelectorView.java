package com.sscf.investment.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sscf.investment.R;

/**
 * Created by LEN on 2018/4/4.
 */

public class SettingTwoTabSelectorView extends LinearLayout implements View.OnClickListener{
    private TextView mLeftTab;
    private TextView mRightTab;

    private SettingTwoTabSelectorView.OnTabSelectedListener mOnTabSelectedListener;

    public SettingTwoTabSelectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.setting_two_tab_selector, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setBackgroundResource(R.drawable.setting_two_tab_selector_bg);
        mLeftTab = (TextView) findViewById(R.id.left_tab_text);
        mLeftTab.setOnClickListener(this);
        mLeftTab.setSelected(true);

        mRightTab = (TextView) findViewById(R.id.right_tab_text);
        mRightTab.setOnClickListener(this);
        mRightTab.setSelected(false);
    }

    public void setTabTitles(int leftTitle, int rightTitle) {
        final Resources res = getResources();
        setTabTitles(res.getString(leftTitle), res.getString(rightTitle));
    }

    public void setTabTitles(String leftTitle, String rightTitle) {
        mLeftTab.setText(leftTitle);
        mRightTab.setText(rightTitle);
    }

    public void setLeftTitle(String leftTitle) {
        mLeftTab.setText(leftTitle);
    }

    public void setRightTitle(String rightTitle) {
        mRightTab.setText(rightTitle);
    }

    @Override
    public void onClick(View v) {
        if(v == mLeftTab) {
            switchToLeft();
        } else if(v == mRightTab) {
            switchToRight();
        }
    }

    public void switchToIndex(int index){
        if (index == 0){
            switchToLeft();
        }else {
            switchToRight();
        }
    }

    public void switchToLeft() {
        if (!mLeftTab.isSelected()) {
            mLeftTab.setSelected(true);
            mRightTab.setSelected(false);
            if (mOnTabSelectedListener != null) {
                mOnTabSelectedListener.onLeftTabSelected();
            }
        }
    }

    public void switchToRight() {
        if (!mRightTab.isSelected()) {
            mRightTab.setSelected(true);
            mLeftTab.setSelected(false);

            if (mOnTabSelectedListener != null) {
                mOnTabSelectedListener.onRightTabSelected();
            }
        }
    }

    public void setOnTabSelectedListener(SettingTwoTabSelectorView.OnTabSelectedListener listener) {
        mOnTabSelectedListener = listener;
    }

    public interface OnTabSelectedListener {
        void onLeftTabSelected();
        void onRightTabSelected();
    }
}
