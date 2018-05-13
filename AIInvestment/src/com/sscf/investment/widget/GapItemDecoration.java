package com.sscf.investment.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by LEN on 2018/4/23.
 */

public class GapItemDecoration extends RecyclerView.ItemDecoration {

    private final int mGap;

    public GapItemDecoration(final int gap) {
        mGap = gap;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mGap);
    }
}
