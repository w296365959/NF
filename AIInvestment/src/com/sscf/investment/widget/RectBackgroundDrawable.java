package com.sscf.investment.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;

/**
 * Created by liqf on 2015/8/14.
 */
public class RectBackgroundDrawable extends Drawable {
    private Paint mPaint;
    private Rect mRect;

    public RectBackgroundDrawable(final Context context) {
        Resources resources = DengtaApplication.getApplication().getResources();
        int defaultStrokeWidth = resources.getDimensionPixelSize(R.dimen.default_rect_bg_strokeWidth);

        int defaultColor = ContextCompat.getColor(context, R.color.default_text_color_60);

        mPaint = new Paint();
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

    @Override
    public void draw(Canvas canvas) {
        mRect = getBounds();
        canvas.drawRect(mRect, mPaint);
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
