package com.sscf.investment.component.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sscf.investment.component.ui.R;

/**
 * Created by liqf on 2015/7/22.
 */
public class TwoTabSelectorView extends LinearLayout implements View.OnClickListener {
    private ColorStateList mSelectorTextColor;
    private Drawable mSelectorBg;
    private Drawable mSelectedLeftBgDrawable;
    private Drawable mSelectedRightBgDrawable;
    private Drawable mSelectedMiddleBgDrawable;
    private boolean mIsBold;

    private TextView mFirstTab;
    private TextView mSecondTab;
    private TextView mThirdTab;

    private OnTabSelectedListener mOnTabSelectedListener;

    private int mIndex;

    public TwoTabSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TwoTabSelectorView);
        mSelectorTextColor = array.getColorStateList(R.styleable.TwoTabSelectorView_selectorTextColor);
        mSelectorBg = array.getDrawable(R.styleable.TwoTabSelectorView_selectorBg);
        mSelectedLeftBgDrawable = array.getDrawable(R.styleable.TwoTabSelectorView_selectedLeftBgDrawable);
        mSelectedRightBgDrawable = array.getDrawable(R.styleable.TwoTabSelectorView_selectedRightBgDrawable);
        mSelectedMiddleBgDrawable = array.getDrawable(R.styleable.TwoTabSelectorView_selectedMiddleBgDrawable);
        mIsBold = array.getBoolean(R.styleable.TwoTabSelectorView_isBold, false);
        array.recycle();

        initResources();

        LayoutInflater.from(context).inflate(R.layout.two_tab_selector, this, true);
    }

    private void initResources() {
        TypedArray a = getContext().obtainStyledAttributes(new int[] {
            R.attr.normal_two_tab_textColor,
            R.attr.normal_two_tab_selector_view_bg_drawable,
            R.attr.normal_two_tab_selector_selected_bg_left_drawable,
            R.attr.normal_two_tab_selector_selected_bg_right_drawable,
            R.attr.normal_two_tab_selector_selected_bg_middle_drawable
        });
        ColorStateList textColor = a.getColorStateList(0);
        Drawable bgDrawable = a.getDrawable(1);
        Drawable leftBgDrawable = a.getDrawable(2);
        Drawable rightBgDrawable = a.getDrawable(3);
        Drawable middleBgDrawable = a.getDrawable(4);
        a.recycle();

        if (mSelectorTextColor == null) {
            mSelectorTextColor = textColor;
        }
        if (mSelectorBg == null) {
            mSelectorBg = bgDrawable;
        }
        if (mSelectedLeftBgDrawable == null) {
            mSelectedLeftBgDrawable = leftBgDrawable;
        }
        if (mSelectedRightBgDrawable == null) {
            mSelectedRightBgDrawable = rightBgDrawable;
        }
        if (mSelectedMiddleBgDrawable == null) {
            mSelectedMiddleBgDrawable = middleBgDrawable;
        }
    }

    public void setAttributes(int textColorAttr, int bgDrawableAttr, int leftBgDrawableAttr, int rightBgDrawableAttr, int middleBgDrawableAttr) {
        TypedArray a = getContext().obtainStyledAttributes(new int[] {
                textColorAttr, bgDrawableAttr, leftBgDrawableAttr, rightBgDrawableAttr, middleBgDrawableAttr
        });
        mSelectorTextColor = a.getColorStateList(0);
        mSelectorBg = a.getDrawable(1);
        mSelectedLeftBgDrawable = a.getDrawable(2);
        mSelectedRightBgDrawable = a.getDrawable(3);
        mSelectedMiddleBgDrawable = a.getDrawable(4);
        a.recycle();

        if (mSelectorTextColor != null) {
            mFirstTab.setTextColor(mSelectorTextColor);
            mSecondTab.setTextColor(mSelectorTextColor);
            mThirdTab.setTextColor(mSelectorTextColor);
        }

        setBackgroundDrawable(mSelectorBg);

        if (mFirstTab.isSelected()) {
            mFirstTab.setBackgroundDrawable(mSelectedLeftBgDrawable);
            mThirdTab.setBackgroundColor(Color.TRANSPARENT);
            mSecondTab.setBackgroundColor(Color.TRANSPARENT);
        } else if (mThirdTab.isSelected()) {
            mThirdTab.setBackgroundDrawable(mSelectedRightBgDrawable);
            mFirstTab.setBackgroundColor(Color.TRANSPARENT);
            mSecondTab.setBackgroundColor(Color.TRANSPARENT);
        } else if (mThirdTab.isSelected()) {
            mSecondTab.setBackgroundDrawable(mSelectedMiddleBgDrawable);
            mThirdTab.setBackgroundColor(Color.TRANSPARENT);
            mFirstTab.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void setResouce(int textColor, int bgDrawable, int leftBgDrawable, int rightBgDrawable, int middleBgDrawable) {
        final Context context = getContext();
        mSelectorTextColor =  ContextCompat.getColorStateList(context, textColor);
        mSelectorBg = ContextCompat.getDrawable(context, bgDrawable);
        mSelectedLeftBgDrawable = ContextCompat.getDrawable(context, leftBgDrawable);
        mSelectedRightBgDrawable = ContextCompat.getDrawable(context, rightBgDrawable);
        mSelectedMiddleBgDrawable = ContextCompat.getDrawable(context, middleBgDrawable);

        if (mSelectorTextColor != null) {
            mFirstTab.setTextColor(mSelectorTextColor);
            mSecondTab.setTextColor(mSelectorTextColor);
            mThirdTab.setTextColor(mSelectorTextColor);
        }

        setBackgroundDrawable(mSelectorBg);

        if (mFirstTab.isSelected()) {
            mFirstTab.setBackgroundDrawable(mSelectedLeftBgDrawable);
            mThirdTab.setBackgroundColor(Color.TRANSPARENT);
            mSecondTab.setBackgroundColor(Color.TRANSPARENT);
        } else if (mThirdTab.isSelected()) {
            mThirdTab.setBackgroundDrawable(mSelectedRightBgDrawable);
            mFirstTab.setBackgroundColor(Color.TRANSPARENT);
            mSecondTab.setBackgroundColor(Color.TRANSPARENT);
        } else if (mThirdTab.isSelected()) {
            mSecondTab.setBackgroundDrawable(mSelectedMiddleBgDrawable);
            mThirdTab.setBackgroundColor(Color.TRANSPARENT);
            mFirstTab.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setBackgroundDrawable(mSelectorBg);

        mFirstTab = (TextView) findViewById(R.id.first_tab_text);
        mFirstTab.setOnClickListener(this);
        mFirstTab.setSelected(true);

        mSecondTab = (TextView) findViewById(R.id.second_tab_text);
        mSecondTab.setOnClickListener(this);
        mSecondTab.setSelected(false);

        mThirdTab = (TextView) findViewById(R.id.third_tab_text);
        mThirdTab.setOnClickListener(this);
        mThirdTab.setSelected(false);

        if (mSelectorTextColor != null) {
            mFirstTab.setTextColor(mSelectorTextColor);
            mThirdTab.setTextColor(mSelectorTextColor);
            mSecondTab.setTextColor(mSelectorTextColor);
        }

        if (mIsBold) {
            mFirstTab.getPaint().setFakeBoldText(true);
        }

        mFirstTab.setBackgroundDrawable(mSelectedLeftBgDrawable);
        mSecondTab.setBackgroundColor(Color.TRANSPARENT);
        mThirdTab.setBackgroundColor(Color.TRANSPARENT);
    }

    public void setTabTitles(int firstTitle, int secondTitle, int thirdTitle) {
        final Resources res = getResources();
        setTabTitles(res.getString(firstTitle), res.getString(secondTitle), res.getString(thirdTitle));
    }

    public void setTabTitles(int firstTitle, int secondTitle) {
        final Resources res = getResources();
        setTabTitles(res.getString(firstTitle), res.getString(secondTitle), null);
    }

    public void setTabTitles(String firstTitle, String secondTitle, String thirdTitle) {
        if (TextUtils.isEmpty(firstTitle)) {
            mFirstTab.setVisibility(View.GONE);
        } else {
            mFirstTab.setVisibility(View.VISIBLE);
            mFirstTab.setText(firstTitle);
        }

        if (TextUtils.isEmpty(secondTitle)) {
            mSecondTab.setVisibility(View.GONE);
        } else {
            mSecondTab.setVisibility(View.VISIBLE);
            mSecondTab.setText(secondTitle);
        }

        if (TextUtils.isEmpty(thirdTitle)) {
            mThirdTab.setVisibility(View.GONE);
        } else {
            mThirdTab.setVisibility(View.VISIBLE);
            mThirdTab.setText(thirdTitle);
        }
    }

    public void setTabTitlesSize(int leftTitleSize, int rightTitleSize) {
        mFirstTab.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(leftTitleSize));
        mThirdTab.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(rightTitleSize));
        mSecondTab.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(rightTitleSize));
    }

    @Override
    public void onClick(View v) {
        if (mFirstTab == v) {
            switchToFirst();
        } else if (mSecondTab == v) {
            switchToSecond();
        } else if (mThirdTab == v) {
            switchToThird();
        }
    }

    public void switchToFirst() {
        mIndex = 0;
        if (!mFirstTab.isSelected()) {
            mFirstTab.setSelected(true);
            mSecondTab.setSelected(false);
            mThirdTab.setSelected(false);

            mFirstTab.setBackgroundDrawable(mSelectedLeftBgDrawable);
            mSecondTab.setBackgroundColor(Color.TRANSPARENT);
            mThirdTab.setBackgroundColor(Color.TRANSPARENT);

            if (mIsBold) {
                mFirstTab.getPaint().setFakeBoldText(true);
                mSecondTab.getPaint().setFakeBoldText(false);
                mThirdTab.getPaint().setFakeBoldText(false);
            }

            if (mOnTabSelectedListener != null) {
                mOnTabSelectedListener.onFirstTabSelected();
            }
        }
    }

    public void switchToSecond() {
        mIndex = 1;
        if (!mSecondTab.isSelected()) {
            mSecondTab.setSelected(true);
            mFirstTab.setSelected(false);
            mThirdTab.setSelected(false);

            if (mThirdTab.getVisibility() != View.VISIBLE) {
                mSecondTab.setBackgroundDrawable(mSelectedRightBgDrawable);
            } else {
                mSecondTab.setBackgroundDrawable(mSelectedMiddleBgDrawable);
            }

            mFirstTab.setBackgroundColor(Color.TRANSPARENT);
            mThirdTab.setBackgroundColor(Color.TRANSPARENT);

            if (mIsBold) {
                mSecondTab.getPaint().setFakeBoldText(true);
                mFirstTab.getPaint().setFakeBoldText(false);
                mThirdTab.getPaint().setFakeBoldText(false);
            }

            if (mOnTabSelectedListener != null) {
                mOnTabSelectedListener.onSecondTabSelected();
            }
        }
    }

    public void switchToThird() {
        mIndex = 2;
        if (!mThirdTab.isSelected()) {
            mThirdTab.setSelected(true);
            mFirstTab.setSelected(false);
            mSecondTab.setSelected(false);

            mThirdTab.setBackgroundDrawable(mSelectedRightBgDrawable);
            mFirstTab.setBackgroundColor(Color.TRANSPARENT);
            mSecondTab.setBackgroundColor(Color.TRANSPARENT);

            if (mIsBold) {
                mThirdTab.getPaint().setFakeBoldText(true);
                mFirstTab.getPaint().setFakeBoldText(false);
                mSecondTab.getPaint().setFakeBoldText(false);
            }

            if (mOnTabSelectedListener != null) {
                mOnTabSelectedListener.onThirdTabSelected();
            }
        }
    }

    public int getIndex() {
        return mIndex;
    }

    public void switchTab(final int index) {

        this.mIndex = index <= 2 ? 0 : index;
        if (index == 0) {
            if (mFirstTab.getVisibility() == View.VISIBLE) {
                switchToFirst();
            } else {
                switchTab(1);
            }
        } else if (index == 1) {
            if (mSecondTab.getVisibility() == View.VISIBLE) {
                switchToSecond();
            } else {
                switchTab(2);
            }
        } else if (index == 2) {
            if (mThirdTab.getVisibility() == View.VISIBLE) {
                switchToThird();
            } else {
                switchTab(0);
            }
        }
    }

    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        mOnTabSelectedListener = listener;
    }

    public interface OnTabSelectedListener {
        void onFirstTabSelected();
        void onSecondTabSelected();
        void onThirdTabSelected();
    }
}
