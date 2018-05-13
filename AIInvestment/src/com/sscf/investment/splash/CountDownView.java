package com.sscf.investment.splash;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sscf.investment.R;

/**
 * Created by davidwei on 2016/07/29
 *
 * 倒数的view
 */
public final class CountDownView extends TextView implements Runnable {
    private int mCount;

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void startCountDown(final int count) {
        mCount = count;
        setText(getContext().getString(R.string.splash_skip, count));
        postDelayed(this, 1000L);
    }

    @Override
    public void run() {
        mCount--;
        setText(getContext().getString(R.string.splash_skip, mCount));
        if (mCount > 0) {
            postDelayed(this, 1000L);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this);
    }
}
