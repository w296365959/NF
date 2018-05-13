package com.sscf.investment.widget.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by liqf on 2016/9/5.
 */
public class RecyclerViewHelper {
    public static boolean isOnTop(final RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        boolean notOnTop = recyclerView.getChildCount() > 0
                && (layoutManager.findFirstCompletelyVisibleItemPosition() > 0
                || recyclerView.getChildAt(0).getTop() < recyclerView.getPaddingTop());
        return !notOnTop;
    }
}
