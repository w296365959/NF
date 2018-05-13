package com.sscf.investment.stare.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sscf.investment.sdk.utils.DtLog;

/**
 * Created by xiangcheng on 17/8/22.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = SpaceItemDecoration.class.getSimpleName();

    private boolean ignoreFirstLeftSpace = false;
    private int top;
    private int left;
    private int right;
    private int bottom;

    public SpaceItemDecoration(int space) {
        this.top = space;
        this.left = space;
        this.right = space;
        this.bottom = space;
    }

    public SpaceItemDecoration(int top, int left, int right, int bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
    }

    public void ignoreFirstLeftSpace() {
        ignoreFirstLeftSpace = true;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = top;
        if (ignoreFirstLeftSpace && parent.getChildAdapterPosition(view) == 0) {
            outRect.left = 0;
        } else {
            outRect.left = left;
        }
        outRect.right = right;
        outRect.bottom = bottom;
        DtLog.d(TAG, "outRect.top = " + outRect.top + ", outRect.left = " + outRect.left + ", outRect.right = " + outRect.right + ", outRect.bottom = " + outRect.bottom);
    }
}