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
 * Created by yorkeehuang on 2018/1/3.
 */

public class PortfolioTwoTabSelectorView extends LinearLayout implements View.OnClickListener {

    private View mLeftTabContainer;
    private TextView mLeftTab;
    private TextView mRightTab;

    private OnTabSelectedListener mOnTabSelectedListener;

    public PortfolioTwoTabSelectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.portfolio_market_two_tab_selector, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeftTabContainer = findViewById(R.id.left_tab_text_container);
        mLeftTab = (TextView) findViewById(R.id.left_tab_text);
        mLeftTabContainer.setOnClickListener(this);
        mLeftTab.setSelected(true);
        mLeftTabContainer.setSelected(true);

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
        if(v == mLeftTabContainer) {
            switchToLeft();
        } else if(v == mRightTab) {
            switchToRight();
        }
    }

    public void switchToLeft() {
        if (!mLeftTab.isSelected()) {
            mLeftTabContainer.setSelected(true);
            mLeftTab.setSelected(true);
            mRightTab.setSelected(false);
            if (mOnTabSelectedListener != null) {
                mOnTabSelectedListener.onLeftTabSelected();
            }
        } else if (mOnTabSelectedListener != null) {
            mOnTabSelectedListener.onLeftTabReSelected();
        }
    }

    public void switchToRight() {
        if (!mRightTab.isSelected()) {
            mRightTab.setSelected(true);
            mLeftTab.setSelected(false);
            mLeftTabContainer.setSelected(false);

            if (mOnTabSelectedListener != null) {
                mOnTabSelectedListener.onRightTabSelected();
            }
        } else if (mOnTabSelectedListener != null) {
            mOnTabSelectedListener.onRightTabReSelected();
        }
    }

    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        mOnTabSelectedListener = listener;
    }

    public interface OnTabSelectedListener {
        void onLeftTabSelected();
        void onLeftTabReSelected();
        void onRightTabSelected();
        void onRightTabReSelected();
    }
}
