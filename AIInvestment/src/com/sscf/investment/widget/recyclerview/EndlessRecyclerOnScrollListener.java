package com.sscf.investment.widget.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sscf.investment.sdk.utils.DtLog;

/**
 * Created by liqf on 2016/9/1.
 */
public abstract class EndlessRecyclerOnScrollListener extends
        RecyclerView.OnScrollListener {
    private static final String TAG = "EndlessRecyclerOnScrollListener";

    private int previousTotal = 0;
    private boolean loading = true;

    private int currentPage = 1;

    private LinearLayoutManager mLinearLayoutManager;
    private int lastVisibleItem;

    public EndlessRecyclerOnScrollListener(
            LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
        DtLog.d(TAG, "onScrolled: lastVisibleItem = " + lastVisibleItem);
        DtLog.d(TAG, "onScrolled: mLinearLayoutManager.getItemCount() = " + mLinearLayoutManager.getItemCount());

        DtLog.d(TAG, "onScrolled: scrollState = " + recyclerView.getScrollState());
        if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mLinearLayoutManager.getItemCount()) {
            DtLog.d(TAG, "onScrolled: onLoadMore");
            onLoadMore(0);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

    }

    public abstract void onLoadMore(int currentPage);
}
