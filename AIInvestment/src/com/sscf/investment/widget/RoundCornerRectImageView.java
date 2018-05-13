package com.sscf.investment.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by yorkeehuang on 2017/3/21.
 */

public class RoundCornerRectImageView extends View {

    private static final String TAG = RoundCornerRectImageView.class.getSimpleName();

    private Paint mPaint;
    private Drawable mDrawable;
    private int mWidth;
    private int mHeight;

    private int mRadius;

    public RoundCornerRectImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void setBitmap(Resources res, Bitmap bitmap) {
        setDrawable(new BitmapDrawable(res, bitmap));
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw()");
        super.onDraw(canvas);
        if(mDrawable != null) {
            if(mDrawable instanceof BitmapDrawable) {
                Bitmap bmp = createRoundConerImage(((BitmapDrawable) mDrawable).getBitmap());
                canvas.drawBitmap(bmp, 0, 0, mPaint);
            }
        }
    }

    private Bitmap createRoundConerImage(Bitmap source) {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Matrix matrix = new Matrix();
        matrix.postScale(mWidth / (float)source.getWidth(), mHeight / (float)source.getHeight());
        canvas.drawBitmap(source, matrix, paint);
        return target;
    }
}
