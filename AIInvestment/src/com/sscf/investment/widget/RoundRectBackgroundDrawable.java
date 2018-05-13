package com.sscf.investment.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;

/**
 * Created by liqf on 2015/8/14.
 */
public class RoundRectBackgroundDrawable extends Drawable {
    private Paint mPaint;
    private RectF mRectF = new RectF();
    private float mRadius;

    public RoundRectBackgroundDrawable(final Context context) {
        Resources resources = DengtaApplication.getApplication().getResources();
        int defaultStrokeWidth = resources.getDimensionPixelSize(R.dimen.default_round_rect_bg_strokeWidth);

        int defaultColor = ContextCompat.getColor(context, R.color.tab_indicatorColor);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(defaultColor);
        mPaint.setStrokeWidth(defaultStrokeWidth);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setStrokeWidth(float strokeWidth) {
        mPaint.setStrokeWidth(strokeWidth);
    }

    public void setStyle(Paint.Style style) {
        mPaint.setStyle(style);
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    @Override
    public void draw(Canvas canvas) {
        mRectF.set(getBounds());
//        float radius = (mRectF.bottom - mRectF.top) / 2;
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
