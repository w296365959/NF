package com.sscf.investment.setting.widgt;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import com.sscf.investment.R;

/**
 * Created by davidwei on 2015-08-07.
 *
 */
public class UpgradeProgressButton extends Button {

    private final Drawable mBgDrawable;
    private final Drawable mFgDrawable;

    private final Paint mPaint;

    private final Rect mBgRect;
    private final Rect mFgRect;

    private int mProgress = 100;

    public UpgradeProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        final Resources res = context.getResources();

        TypedArray a = context.obtainStyledAttributes(new int[]{ R.attr.setting_progress_button_bg });
        mBgDrawable = a.getDrawable(0);
        a.recycle();

        mFgDrawable = res.getDrawable(R.drawable.setting_progress_button_fg);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(false);

        mBgRect = new Rect();
        mFgRect = new Rect();
    }

    public void setProgress(int progress) {
        if (progress > 100) {
            progress = 100;
        }
        this.mProgress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int width = getWidth();
        if (mBgRect.right <= 0) {
            mBgRect.left = 0;
            mBgRect.top = 0;
            mBgRect.right = width;
            mBgRect.bottom = getHeight();

            mBgDrawable.setBounds(mBgRect);
            mFgRect.right = width / 2;
            mFgRect.bottom = getHeight();
        }

        if (mProgress == 0) {
            mBgDrawable.draw(canvas);
        }
        if (mProgress == 100) {
            mFgRect.right = width;
            mFgDrawable.setBounds(mFgRect);
            mFgDrawable.draw(canvas);
        } else {
            mBgDrawable.draw(canvas);
            mFgRect.right = mProgress * width / 100;
            mFgDrawable.setBounds(mFgRect);
            mFgDrawable.draw(canvas);
        }

        super.onDraw(canvas);
    }
}
