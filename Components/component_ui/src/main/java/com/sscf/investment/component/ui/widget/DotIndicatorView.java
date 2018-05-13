package com.sscf.investment.component.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.sscf.investment.component.ui.R;
import com.sscf.investment.sdk.utils.DeviceUtil;

/**
 * Created by davidwei on 2015/9/23.
 */
public final class DotIndicatorView extends LinearLayout {
    private int mIndicatorCount;
    private View[] mIndicators;

    private int mCurrentIndex;

    private final Drawable mSelectedDrawable;
    private final Drawable mNormalDrawable;

    public DotIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mSelectedDrawable = ContextCompat.getDrawable(context, R.drawable.dot_red);
        mNormalDrawable = ContextCompat.getDrawable(context, R.drawable.dot_gray);
    }

    public void setIndicatorCount(int count) {
        setIndicatorCount(count, 0);
    }

    public void setIndicatorCount(int count, int selectedIndex) {
        if (mIndicatorCount > 0 || count <= 0) {
            // 如果已经初始化就不在初始化了
            return;
        }

        this.mIndicatorCount = count;
        mIndicators = new View[count];

        final Context context = getContext();
        final Resources res = context.getResources();
        final int indicatorSize = DeviceUtil.dip2px(context, 4.5f);
        final int indicatorMargin = DeviceUtil.dip2px(context, 8);
        LayoutParams params = null;
        View indicator = null;
        mCurrentIndex = selectedIndex;
        for (int i = 0; i < count; i++) {
            params = new LayoutParams(indicatorSize, indicatorSize);
            if (i < count - 1) {
                params.rightMargin = indicatorMargin;
            }
            indicator = new View(context);
            if (i == selectedIndex) {
                indicator.setBackgroundDrawable(mSelectedDrawable);
            } else {
                indicator.setBackgroundDrawable(mNormalDrawable);
            }
            addView(indicator, params);
            mIndicators[i] = indicator;
        }
    }

    public void switchIndex(final int index) {
        if (mCurrentIndex != index) {
            mIndicators[index].setBackgroundDrawable(mSelectedDrawable);
            mIndicators[mCurrentIndex].setBackgroundDrawable(mNormalDrawable);
            mCurrentIndex = index;
        }
    }
}
