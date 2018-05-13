package com.sscf.investment.widget;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.sscf.investment.R;

/**
 * Created by davidwei on 2016/03/16.
 */
public final class TimeLineView extends View {

    private int mLineColor;
    private float mLineWidth;
    private int mDotColor;
    private int mFirstDotColor;
    private int mFirstHaloDotColor;
    private float mDotSize;
    private float mFirstDotSize;
    private float mDotMarginTop;

    private Paint mPaint;

    public static final int TYPE_FIRST_UNKNOWN = 0;
    public static final int TYPE_NORMAL_DOT = 1;
    public static final int TYPE_FIRST_DOT = 2;
    public static final int TYPE_NO_DOT = 3;

    private int mType = TYPE_FIRST_UNKNOWN;

    public TimeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        final Resources res = getResources();

        mLineColor = res.getColor(R.color.stock_stop_color);
        mLineWidth = res.getDimension(R.dimen.list_divider_height);
        mFirstDotColor = res.getColor(R.color.actionbar_bg_color);
        mFirstHaloDotColor = res.getColor(R.color.actionbar_bg_color_20);
        mDotColor = mLineColor;

        mDotMarginTop = res.getDimension(R.dimen.time_dot_marginTop);

        mFirstDotSize = res.getDimension(R.dimen.time_first_dot_size);
        mDotSize = res.getDimension(R.dimen.time_dot_size);
    }

    public void setType(final int type) {
        if (mType != type) {
            this.mType = type;
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int width = getWidth();
        final int height = getHeight();

        final float startX = (width - mLineWidth) / 2f;
        final float cX = width / 2f;
        final float cY = mDotMarginTop + mDotSize / 2;

        switch (mType) {
            case TYPE_NORMAL_DOT:
                mPaint.setColor(mLineColor);
                canvas.drawLine(startX, 0, startX, height, mPaint);

                mPaint.setColor(mDotColor);
                canvas.drawCircle(cX, cY, mDotSize / 2f, mPaint);
                break;
            case TYPE_FIRST_DOT:
                mPaint.setColor(mLineColor);
                canvas.drawLine(startX, cY, startX, height, mPaint);

                mPaint.setColor(mFirstHaloDotColor);
                canvas.drawCircle(cX, cY, mFirstDotSize / 2f, mPaint);

                mPaint.setColor(mFirstDotColor);
                canvas.drawCircle(cX, cY, mDotSize / 2f, mPaint);
                break;
            case TYPE_NO_DOT:
                mPaint.setColor(mLineColor);
                canvas.drawLine(startX, 0, startX, height, mPaint);
                break;
            default:
                break;
        }
    }
}
