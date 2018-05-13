package com.sscf.investment.message.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.sscf.investment.R;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;
import com.sscf.investment.widget.recyclerview.EndlessRecyclerOnScrollListener;

/**
 * davidwei
 * RecyclerView管理器，管理各种loading，分页等状态
 */
public final class RecyclerViewManager extends EndlessRecyclerOnScrollListener implements Handler.Callback, View.OnClickListener {
    static final int MSG_SHOW_RECYCLER_VIEW_BY_STATE = 1;
    static final int MSG_SHOW_FOOTER_VIEW_BY_STATE = 2;
    final RecyclerView mRecyclerView;
    final CommonRecyclerViewAdapter mAdapter;

    final View mLoadingLayoutCenter;
    final View mFailRetryLayoutCenter;
    final View mEmptyView;

    // 分页相关layout
    final FrameLayout mFooterView;
    final View mLoadingLayout;
    final View mNoMoreLayout;
    final View mFooterFailRetryLayout;

    int mRecyclerViewState = DengtaConst.UI_STATE_NORMAL;
    int mBottomViewState = DengtaConst.UI_STATE_NORMAL;

    final OnLoadMoreListener mLoadMoreListener;

    final Handler mHandler;

    private boolean movePosition = true;

    public void setMovePosition(boolean movePosition) {
        this.movePosition = movePosition;
    }

    public RecyclerViewManager(Context context, RecyclerView recyclerView, LinearLayoutManager linearLayoutManager,
                               CommonRecyclerViewAdapter adapter, View loadingLayoutCenter, View failRetryLayoutCenter, View emptyView,
                               OnLoadMoreListener loadMoreListener) {
        super(linearLayoutManager);
        final LayoutInflater inflater = LayoutInflater.from(context);
        mRecyclerView = recyclerView;
        mAdapter = adapter;

        recyclerView.addOnScrollListener(this);

        mLoadingLayoutCenter = loadingLayoutCenter;
        mFailRetryLayoutCenter = failRetryLayoutCenter;
        mEmptyView = emptyView;

        mFooterView = new FrameLayout(context);
        adapter.setFooterView(mFooterView);

        mLoadingLayout = inflater.inflate(R.layout.list_item_loading, null);
        mFooterView.addView(mLoadingLayout);
        mNoMoreLayout = inflater.inflate(R.layout.no_more_text, null);
        mFooterFailRetryLayout = inflater.inflate(R.layout.fail_retry_item, null);
        mFooterFailRetryLayout.setOnClickListener(this);

        mLoadMoreListener = loadMoreListener;

        mHandler = new Handler(this);
    }

    @Override
    public void onLoadMore(int currentPage) {
        if (mBottomViewState != DengtaConst.UI_STATE_NO_MORE_CONTENT && mBottomViewState != DengtaConst.UI_STATE_NO_FOOTER) {
            showFooterViewByState(DengtaConst.UI_STATE_LOADING);
            mLoadMoreListener.onLoadMore();
        }
    }

    @Override
    public void onClick(View v) {
        if (mBottomViewState != DengtaConst.UI_STATE_NO_MORE_CONTENT && mBottomViewState != DengtaConst.UI_STATE_NO_FOOTER) {
            showFooterViewByState(DengtaConst.UI_STATE_LOADING);
            mLoadMoreListener.onLoadMore();
        }
    }

    public void showRecyclerViewByState(int state) {
        if (ThreadUtils.isMainThread()) {
            showRecyclerViewByStateOnUi(state);
        } else {
            mHandler.obtainMessage(MSG_SHOW_RECYCLER_VIEW_BY_STATE, state, 0).sendToTarget();
        }
    }

    private void showRecyclerViewByStateOnUi(int state) {
        switch (state) {
            case DengtaConst.UI_STATE_NORMAL:
                if (mRecyclerViewState != DengtaConst.UI_STATE_NORMAL) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mLoadingLayoutCenter.setVisibility(View.GONE);
                    mFailRetryLayoutCenter.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.GONE);
                }
                break;
            case DengtaConst.UI_STATE_LOADING:
                if (mRecyclerViewState != DengtaConst.UI_STATE_LOADING) {
                    mRecyclerView.setVisibility(View.GONE);
                    mLoadingLayoutCenter.setVisibility(View.VISIBLE);
                    mFailRetryLayoutCenter.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.GONE);
                }
                break;
            case DengtaConst.UI_STATE_NO_CONTENT:
                if (mRecyclerViewState != DengtaConst.UI_STATE_NO_CONTENT) {
                    mRecyclerView.setVisibility(View.GONE);
                    mLoadingLayoutCenter.setVisibility(View.GONE);
                    mFailRetryLayoutCenter.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                }
                break;
            case DengtaConst.UI_STATE_FAILED_RETRY:
                if (mRecyclerViewState != DengtaConst.UI_STATE_NORMAL) {
                    mRecyclerView.setVisibility(View.GONE);
                    mLoadingLayoutCenter.setVisibility(View.GONE);
                    mFailRetryLayoutCenter.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
        mRecyclerViewState = state;
    }

    public void showFooterViewByState(int state) {
        if (ThreadUtils.isMainThread()) {
            showFooterViewByStateOnUi(state);
        } else {
            mHandler.obtainMessage(MSG_SHOW_FOOTER_VIEW_BY_STATE, state, 0).sendToTarget();
        }
    }

    private void showFooterViewByStateOnUi(int state) {
        if (mBottomViewState == state) {
            return;
        }
        mBottomViewState = state;
        switch (state) {
            case DengtaConst.UI_STATE_NORMAL:
                mFooterView.removeAllViews();
                mFooterView.addView(mLoadingLayout);
                break;
            case DengtaConst.UI_STATE_LOADING:
                mFooterView.removeAllViews();
                mFooterView.addView(mLoadingLayout);
                if (movePosition) {
                    final int position = mAdapter.getNormalItemCount() - 1;
                    if (position > 0) {
                        mRecyclerView.scrollToPosition(position);
                    }
                }

                break;
            case DengtaConst.UI_STATE_NO_MORE_CONTENT:
                mFooterView.removeAllViews();
                mFooterView.addView(mNoMoreLayout);
                break;
            case DengtaConst.UI_STATE_FAILED_RETRY:
                mFooterView.removeAllViews();
                mFooterView.addView(mFooterFailRetryLayout);
                break;
            case DengtaConst.UI_STATE_NO_FOOTER:
                mFooterView.removeAllViews();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SHOW_RECYCLER_VIEW_BY_STATE:
                showRecyclerViewByState(msg.arg1);
                break;
            case MSG_SHOW_FOOTER_VIEW_BY_STATE:
                showFooterViewByStateOnUi(msg.arg1);
                break;
        }
        return true;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
