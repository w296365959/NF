package com.sscf.investment.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.sscf.investment.R;

/**
 * Created by liqf on 2016/5/16.
 */
public class EquantLayout extends ViewGroup {
    private int mItemCount;

    public EquantLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EquantLayout);
        mItemCount = a.getInteger(R.styleable.EquantLayout_itemCount, 3);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        if (measuredWidth == 0 || measuredHeight == 0) {
            return;
        }

        int itemWidth = measuredWidth / mItemCount;
        int itemHeight = measuredHeight;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                child.measure(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY));
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) {
            return;
        }

        int left = 0;
        int right = r - l;
        int itemWidth = (r - l) / mItemCount;
        int childCount = getChildCount();

        //从左往右排列
//        for (int i = 0; i < childCount; i++) {
//            View child = getChildAt(i);
//            if (child.getVisibility() == VISIBLE) {
//                child.layout(left, t, left + itemWidth, b);
//                left += itemWidth;
//            }
//        }

        //从右往左排列
        for (int i = childCount - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                child.layout(right - itemWidth, t, right, b);
                right -= itemWidth;
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }
}
