package com.sscf.investment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.sscf.investment.R;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.utils.DengtaConst;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liqf on 2016/11/9.
 */

public class StateFrameLayout extends FrameLayout {
    private int mViewState = DengtaConst.UI_STATE_LOADING;

    private View mContentView;
    @BindView(R.id.loading_layout) FrameLayout mLoadingLayout;
    @BindView(R.id.no_more_text) FrameLayout mNoMoreText;
    @BindView(R.id.fail_retry) FrameLayout mFailRetry;
    @BindView(R.id.empty_view) FrameLayout mEmptyView;

    private OnReloadDataListener mOnReloadDataListener;

    public StateFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount != 1) {
            throw new IllegalArgumentException("StateFrameLayout should have only one child!");
        }

        mContentView = getChildAt(0);

        LayoutInflater.from(getContext()).inflate(R.layout.state_views, this, true);
        ButterKnife.bind(this);

        mFailRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetryClicked();
            }
        });
    }

    public void setOnReloadDataListener(OnReloadDataListener listener) {
        mOnReloadDataListener = listener;
    }

    public int getState() {
        return mViewState;
    }

    public void setState(final int viewState) {
        mViewState = viewState;

        switch (mViewState) {
            case DengtaConst.UI_STATE_LOADING:
                mContentView.setVisibility(GONE);
                mLoadingLayout.setVisibility(VISIBLE);
                mNoMoreText.setVisibility(GONE);
                mFailRetry.setVisibility(GONE);
                mEmptyView.setVisibility(GONE);
                break;
            case DengtaConst.UI_STATE_FAILED_RETRY:
                mContentView.setVisibility(GONE);
                mLoadingLayout.setVisibility(GONE);
                mNoMoreText.setVisibility(GONE);
                mFailRetry.setVisibility(VISIBLE);
                mEmptyView.setVisibility(GONE);
                break;
            case DengtaConst.UI_STATE_NORMAL:
                mContentView.setVisibility(VISIBLE);
                mLoadingLayout.setVisibility(GONE);
                mNoMoreText.setVisibility(GONE);
                mFailRetry.setVisibility(GONE);
                mEmptyView.setVisibility(GONE);
                break;
            case DengtaConst.UI_STATE_NO_CONTENT:
                mContentView.setVisibility(GONE);
                mLoadingLayout.setVisibility(GONE);
                mNoMoreText.setVisibility(GONE);
                mFailRetry.setVisibility(GONE);
                mEmptyView.setVisibility(VISIBLE);
                break;
            default:
                throw new IllegalArgumentException("The state is not supported yet.");
        }
    }

    @OnClick(R.id.fail_retry)
    public void onRetryClicked() {
        if (mOnReloadDataListener != null) {
            mOnReloadDataListener.onReloadData();
        }
    }
}
