package com.sscf.investment.portfolio.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.sscf.investment.sdk.utils.DtLog;

public final class HorizontalRecyclerView extends RecyclerView {
    private static final String TAG = HorizontalRecyclerView.class.getSimpleName();

    private static final int COUNT_PER_PAGE = 3;

    private OnItemScrollIndexChangedListener mOnItemScrollIndexChangedListener;
    private int mCurrentIndex = -1;
    private OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, final int newState) {
            DtLog.d(TAG, "onScrollStateChanged newState : " + newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                final LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();

                final int firstCompletelyVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
                final View firstCompletelyVisibleItem = manager.findViewByPosition(firstCompletelyVisibleItemPosition);
                if (firstCompletelyVisibleItem == null) {
                    return;
                }
                final int firstCompletelyVisibleItemLeft = manager.getDecoratedLeft(firstCompletelyVisibleItem);

                DtLog.d(TAG, "onScrollStateChanged firstCompletelyVisibleItemLeft : " + firstCompletelyVisibleItemLeft);

                int halfWidth = getWidth() / (COUNT_PER_PAGE * 2);
                int dx = 0;
                if (firstCompletelyVisibleItemLeft > halfWidth) {
                    dx = firstCompletelyVisibleItemLeft - halfWidth * 2;
                } else {
                    dx = firstCompletelyVisibleItemLeft;
                }
                if (dx == 0) {
                    if (mCurrentIndex != firstCompletelyVisibleItemPosition) {
                        mCurrentIndex = firstCompletelyVisibleItemPosition;
                        if (mOnItemScrollIndexChangedListener != null) {
                            mOnItemScrollIndexChangedListener.onItemScrollIndexChanged(firstCompletelyVisibleItemPosition);
                            mOnItemScrollIndexChangedListener.onStartItem(firstCompletelyVisibleItemPosition == 0);
                            mOnItemScrollIndexChangedListener.onEndItem(manager.findLastCompletelyVisibleItemPosition() == manager.getItemCount() - 1);
                        }
                    }
                    return;
                }
                smoothScrollBy(dx, 0);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }
    };

    public HorizontalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addOnScrollListener(mScrollListener);
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    public void setOnItemScrollIndexChangedListener(OnItemScrollIndexChangedListener l) {
        this.mOnItemScrollIndexChangedListener = l;
    }

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        mCurrentIndex = position;
        if (mOnItemScrollIndexChangedListener != null) {
            mOnItemScrollIndexChangedListener.onStartItem(position == 0);
            final LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
            final int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
            mOnItemScrollIndexChangedListener.onEndItem(lastVisibleItem == manager.getItemCount() - 1);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public interface OnItemScrollIndexChangedListener {
        void onItemScrollIndexChanged(final int index);
        void onStartItem(final boolean b);
        void onEndItem(final boolean b);
    }
}