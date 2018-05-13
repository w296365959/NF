package com.sscf.investment.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by yorkeehuang on 2017/1/6.
 */

public class CropView extends View {

    private  static final String TAG = CropView.class.getSimpleName();

    private Drawable mDrawable;

    private boolean mIsEnableCrop = false;


    private int mCropWidth = 0;
    private int mCropHeight= 0;

    private Matrix mMatrix = new Matrix();

    public CropView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIsEnableCrop(boolean isEnableCrop) {
        mIsEnableCrop = isEnableCrop;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
        invalidate();
    }

    public void setBitmap(Resources res, Bitmap bitmap) {
        setDrawable(new BitmapDrawable(res, bitmap));
    }

    public void setCropWidth(int cropWidth) {
        if(cropWidth >= 0) {
            mCropWidth = cropWidth;
        }
    }

    public void setCropHeight(int cropHeight) {
        if(cropHeight >= 0) {
            mCropHeight = cropHeight;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw()");
        super.onDraw(canvas);
        if(mDrawable != null) {
            final int boundwidth = mDrawable.getIntrinsicWidth();
            final int boundheight = mDrawable.getIntrinsicHeight();
            final int cropWidth;
            final int cropHeight;
            if(mIsEnableCrop) {
                cropWidth = mCropWidth;
                cropHeight = mCropHeight;
            } else {
                cropWidth = 0;
                cropHeight = 0;
            }

            mDrawable.setBounds(0, 0, boundwidth, boundheight);

            final int dwidth = boundwidth - cropWidth;
            final int dheight = boundheight - cropHeight;
            final int vwidth = getWidth();
            final int vheight = getHeight();

            float scale;
            float dx = 0, dy = 0;

            if (dwidth * vheight > vwidth * dheight) {
                scale = (float) vheight / (float) dheight;
                dx = (vwidth - dwidth * scale) * 0.5f;
            } else {
                scale = (float) vwidth / (float) dwidth;
                dy = (vheight - dheight * scale) * 0.5f;
            }
            mMatrix.reset();
            mMatrix.setScale(scale, scale);
            mMatrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));

            int saveCount = canvas.getSaveCount();
            canvas.save();
            canvas.concat(mMatrix);
            mDrawable.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }
}
