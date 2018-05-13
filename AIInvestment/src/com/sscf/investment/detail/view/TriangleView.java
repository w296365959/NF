package com.sscf.investment.detail.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;

/**
 * Created by liqf on 2016/9/8.
 */
public class TriangleView extends View {
    private Paint mPaint;
    private Path mPath;

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int contentColor = ContextCompat.getColor(context, R.color.default_background);
        mPaint.setColor(contentColor);

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(0, height);
        mPath.lineTo(width, height);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }
}
