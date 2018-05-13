package com.sscf.investment.widget.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sscf.investment.R;
import com.sscf.investment.utils.DengtaConst;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liqf on 2016/9/24.
 */

public class CommonFooterView extends FrameLayout {
    private static final String TAG = "CommonFooterView";

    @BindView(R.id.list_item_loading) FrameLayout mListItemLoading;
    @BindView(R.id.no_more_text) FrameLayout mNoMoreText;
    @BindView(R.id.fail_retry) FrameLayout mFailRetry;
    @BindView(R.id.empty_view) FrameLayout mEmptyView;

    private int mViewState = DengtaConst.UI_STATE_LOADING;

    private OnFailRetryListener mFailRetryListener;

    public CommonFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.common_footer_view, this, true);
        ButterKnife.bind(this);
    }

    public void setState(int viewState) {
        mViewState = viewState;

        switch (mViewState) {
            case DengtaConst.UI_STATE_LOADING:
                mListItemLoading.setVisibility(VISIBLE);
                mNoMoreText.setVisibility(GONE);
                mFailRetry.setVisibility(GONE);
                mEmptyView.setVisibility(GONE);
                break;
            case DengtaConst.UI_STATE_FAILED_RETRY:
                mListItemLoading.setVisibility(GONE);
                mNoMoreText.setVisibility(GONE);
                mFailRetry.setVisibility(VISIBLE);
                mEmptyView.setVisibility(GONE);
                break;
            case DengtaConst.UI_STATE_NO_MORE_CONTENT:
                mListItemLoading.setVisibility(GONE);
                mNoMoreText.setVisibility(VISIBLE);
                mFailRetry.setVisibility(GONE);
                mEmptyView.setVisibility(GONE);
                break;
            case DengtaConst.UI_STATE_NO_CONTENT:
                mListItemLoading.setVisibility(GONE);
                mNoMoreText.setVisibility(GONE);
                mFailRetry.setVisibility(GONE);
                mEmptyView.setVisibility(VISIBLE);
                break;
            default:
                break;
        }

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            setLayoutParams(layoutParams);
        }
    }

    @OnClick(R.id.fail_retry)
    public void OnFailRetryClicked() {
        if (mFailRetryListener != null) {
            mFailRetryListener.onFailRetry();
        }
    }

    public interface OnFailRetryListener {
        void onFailRetry();
    }

    public void setFailRetryListener(OnFailRetryListener failRetryListener) {
        mFailRetryListener = failRetryListener;
    }
}
