package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;

/**
 * Created by liqf on 2015/8/4.
 */
public class CircleIndicatorView extends View {
    private int mDrawingWidth;
    private int mDrawingHeight;
    private Paint mPaint;
    private float mRadius;
    private int mColor;

    public CircleIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CircleIndicatorView);
        mColor = array.getColor(R.styleable.CircleIndicatorView_circleColor, Color.GRAY);
        mRadius = array.getDimensionPixelSize(R.styleable.CircleIndicatorView_circleRadius, 0);
        array.recycle();

        initResources();
    }

    private void initResources() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();
        canvas.drawCircle(mDrawingWidth / 2, mDrawingHeight / 2, mRadius, mPaint);
    }
}
