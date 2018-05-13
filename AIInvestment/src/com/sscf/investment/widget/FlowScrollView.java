package com.sscf.investment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.sscf.investment.sdk.utils.DtLog;

/**
 * Created by liqf on 2016/8/13.
 */
public class FlowScrollView extends ScrollView implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = FlowScrollView.class.getSimpleName();

    private View mFlowView1;
    private View mOriginalView;

    private int mTop;
    private OnScrollListener mOnScrollListener;

    public FlowScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollChanged(t);
        }

        if (mFlowView1 == null) {
            return;
        }

        int top1 = mTop;

        if (t > top1) {
            mFlowView1.setVisibility(VISIBLE);
            mFlowView1.setTranslationY(t);
        } else {
            mFlowView1.setVisibility(GONE);
        }
    }

    public void setFlowLayers(View flowView1, View originalView) {
        mFlowView1 = flowView1;
        mTop = originalView.getTop();
        mOriginalView = originalView;

        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public void setOnScrollListener(final OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    @Override
    public void onGlobalLayout() {
        int top = mOriginalView.getTop();
        DtLog.d(TAG, "onGlobalLayout: top = " + top);
        if (top != 0) {
            mTop = top;
        }
    }

    public interface OnScrollListener {
        void onScrollChanged(int scollY);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
