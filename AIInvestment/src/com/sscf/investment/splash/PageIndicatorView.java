package com.sscf.investment.splash;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;

/**
 * Created by liqf on 2015/9/24.
 */
public class PageIndicatorView extends View {
    public static final float PILLAR_WIDTH_RATIO = 0.6f;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private int mCount;
    private Paint mPaint;
    private int mColorSelected;
    private int mColorUnselected;

    private int mHighlightIndex;

    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initResources();
    }

    private void initResources() {
        Resources resources = getResources();
        mPaint = new Paint();
        mColorSelected = resources.getColor(R.color.splash_indicator_selected);
        mColorUnselected = resources.getColor(R.color.splash_indicator_unselected);
        mPaint.setColor(mColorUnselected);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();

        if (mCount == 0) {
            return;
        }

        float startX = 0;
        float itemWidth = mDrawingWidth / mCount;
        startX += itemWidth / 2;

        for (int i = 0; i < mCount; i++) {
            if (i == mHighlightIndex) {
                mPaint.setColor(mColorSelected);
            } else {
                mPaint.setColor(mColorUnselected);
            }

            canvas.drawCircle(startX, mDrawingHeight / 2, mDrawingHeight / 2,mPaint);

            startX += itemWidth;
        }
    }

    public void setCount(int count) {
        mCount = count;
        invalidate();
    }

    public void setHighlightIndex(int index) {
        mHighlightIndex = index;

        invalidate();
    }
}
