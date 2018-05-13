package com.sscf.investment.widget.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yorkeehuang on 2017/7/26.
 */

public class CustomDividerItemDecoration extends DividerItemDecoration {
    public CustomDividerItemDecoration(Context context, int orientation) {
        super(context, orientation);
    }

    /**
     * 绘制纵向列表时的分隔线  这时分隔线是横着的
     * 每次 left相同，top根据child变化，right相同，bottom也变化
     * @param c
     * @param parent
     */
//    @Override
//    public void drawVertical(Canvas c, RecyclerView parent) {
//        final int left = parent.getPaddingLeft();
//        final int right = parent.getWidth() - parent.getPaddingRight();
//
//        final int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = parent.getChildAt(i);
//            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
//                    .getLayoutParams();
//            final int top = child.getBottom() + params.bottomMargin;
//            final int bottom = top + mDividerHeight;
//            c.drawRect(left, top, right, bottom, mPaint);
////            final int bottom = top + mDivider.getIntrinsicHeight();
////            mDivider.setBounds(left, top, right, bottom);
////            mDivider.draw(c);
//        }
//    }

    private int mNoDividerPosition = -1;

    public void setNoDividerPosition(int position) {
        mNoDividerPosition = position;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (mOrientation == VERTICAL_LIST) {
            if(position != mNoDividerPosition && position != mNoDividerPosition - 1) {
                outRect.set(0, 0, 0, mDividerHeight);
            }
        } else {
            outRect.set(0, 0, mDividerWidth, 0);
        }
    }


}
