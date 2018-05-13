package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;

/**
 * Created by liqf on 2016/4/12.
 */
public class IndicatorGroupView extends View {
    private String[] mTextList;
    private int[] mColorList;

    private Paint mPaint;
    private TextDrawer mTextDrawer;
    private int mTextSize;
    private int mTextHeight;
    private int mTextBottom;
    private int mTextGroupGap;
    private int mIconTextGap;
    private int mCircleRadius;

    public IndicatorGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initResources();
    }

    private void initResources() {
        Resources resources = getResources();
        mTextDrawer = new TextDrawer();
        mTextSize = resources.getDimensionPixelSize(R.dimen.font_size_10);
        mTextGroupGap = resources.getDimensionPixelSize(R.dimen.indicator_group_gap);
        mIconTextGap = resources.getDimensionPixelSize(R.dimen.indicator_group_icon_gap);
        mCircleRadius = resources.getDimensionPixelSize(R.dimen.indicator_group_circle_radius);
        mTextHeight = mTextDrawer.measureSingleTextHeight(mTextSize, null);
        mTextBottom = mTextDrawer.measureSingleTextBottom(mTextSize, null);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int drawingHeight = getMeasuredHeight();

        if (mTextList == null || mTextList.length == 0) {
            return;
        }

        float startX = 0;
        float startY = (drawingHeight + mTextHeight - mTextBottom) / 2;
        for (int i = 0; i < mTextList.length; i++) {
            mPaint.setColor(mColorList[i]);
            canvas.drawCircle(startX + mCircleRadius, drawingHeight / 2, mCircleRadius,  mPaint);
            startX += mCircleRadius * 2 + mIconTextGap;
            canvas.drawText(mTextList[i], startX, startY, mPaint);
            int textWidth = mTextDrawer.measureSingleTextWidth(mTextList[i], mTextSize, null);
            startX += textWidth + mTextGroupGap;
        }
    }

    public void setIndicators(String[] textList) {
        mTextList = textList;
        invalidate();
    }

    public void setColorList(int[] colorList) {
        if (colorList == null || colorList.length == 0) {
            return;
        }

        Resources resources = getResources();
        mColorList = new int[colorList.length];
        for (int i = 0; i < colorList.length; i++) {
            mColorList[i] = resources.getColor(colorList[i]);
        }

        invalidate();
    }
}
