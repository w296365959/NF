package com.sscf.investment.detail.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;

/**
 * Created by yorkeehuang on 2017/6/30.
 */

public class IndustryBenchmarkItemBar extends View {

    private float mPercent = 0f;

    private int mBarColor;
    private int mBackgroundColor;

    private Paint mPaint;

    public IndustryBenchmarkItemBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mBarColor = ContextCompat.getColor(getContext(), R.color.industry_benchmark_default_bar_color);
        mBackgroundColor = ContextCompat.getColor(getContext(), R.color.default_content_bg);
    }

    public void setBarColorRes(int barColorRes) {
        setBarColor(ContextCompat.getColor(getContext(), barColorRes));
    }

    public void setBarColor(int barColor) {
        if(mBarColor != barColor) {
            mBarColor = barColor;
            invalidate();
        }
    }

    public void setPercent(float percent) {
        mPercent = percent;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if(width > 0 && height > 0) {
            if(mPercent > 0) {
                mPaint.setColor(mBarColor);
                canvas.drawRect(0, 0, mPercent * width, height, mPaint);
                mPaint.setColor(mBackgroundColor);
                canvas.drawRect(mPercent * width, 0, width, height, mPaint);
            } else {
                mPaint.setColor(mBackgroundColor);
                canvas.drawRect(0, 0, width, height, mPaint);
            }
        }
    }
}
