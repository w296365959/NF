package com.sscf.investment.detail.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.DeviceUtil;

/**
 * Created by yorkeehuang on 2017/8/9.
 */

public class DetailFunctionRecyclerView extends RecyclerView {

    private int mDividerWidth;
    private int mDividerHeight;
    private int mDividerMarginTopBottom;

    private ScrollView mParentScrollView;

    public DetailFunctionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDividerWidth = DeviceUtil.dip2px(getContext().getApplicationContext(), 0.5f);
        mDividerHeight = DeviceUtil.dip2px(getContext().getApplicationContext(), 8f);
        mDividerMarginTopBottom = DeviceUtil.dip2px(getContext().getApplicationContext(), 12f);
        addItemDecoration(new SpaceItemDecoration());
    }

    public void setParentScrollView(ScrollView scrollView) {
        mParentScrollView = scrollView;
    }

    private void setParentScrollable(boolean scrollable) {
        if (mParentScrollView != null) {
            boolean disallowParent = !scrollable;
            mParentScrollView.requestDisallowInterceptTouchEvent(disallowParent);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        setParentScrollable(false);
        return super.dispatchTouchEvent(ev);
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration{


        private Paint mPaint;

        private SpaceItemDecoration() {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(ContextCompat.getColor(getContext(), R.color.time_line_indicator_color));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if(parent.getChildAdapterPosition(view) != 0) {
                outRect.left = mDividerWidth;
            }
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, State state) {
            super.onDraw(c, parent, state);
            drawVerticalLine(c, parent, state);
        }

        //画竖线
        public void drawVerticalLine(Canvas c, RecyclerView parent, RecyclerView.State state){
            int top = parent.getPaddingTop() + mDividerMarginTopBottom;
            int bottom = top + mDividerHeight;
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++){
                final View child = parent.getChildAt(i);

                final int left = child.getRight();
                final int right = left + mDividerWidth;
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
