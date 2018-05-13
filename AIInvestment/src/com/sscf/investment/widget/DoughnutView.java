package com.sscf.investment.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yorkeehuang on 2017/6/28.
 */

public class DoughnutView extends View {

    private static final float FULL_CIRCLE = 360.0f;
    private static final float INITIAL_START_ANGLE = -90.0f;
    private static final int MAX_SECTOR_COUNT = 6;

    private ArrayList<Integer> mColors = new ArrayList<>(MAX_SECTOR_COUNT);

    private int mDrawingWidth;
    private int mDrawingHeight;
    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private int mInnerRadius;
    private int mColorInner;
    private int mOuterRingColor;
    private int mEmptyColor;

    private Paint mPaint;
    private RectF mDrawingRect;

    private static int[] SECTOR_COLORS = new int[] {
            R.color.doughnut_color0,
            R.color.doughnut_color1,
            R.color.doughnut_color2,
            R.color.doughnut_color3,
            R.color.doughnut_color4,
            R.color.doughnut_color5
    };

    private List<Float> mSectorDatas = Collections.EMPTY_LIST;

    public DoughnutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initSectorColors(context);
        mColorInner = ContextCompat.getColor(getContext(), R.color.default_content_bg);
        mOuterRingColor = ContextCompat.getColor(getContext(), R.color.default_background);
        mEmptyColor = ContextCompat.getColor(getContext(), R.color.doughnut_view_empty_color);
        mInnerRadius = getResources().getDimensionPixelSize(R.dimen.doughnut_inner_radius);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public static int getSectorColor(Context context, int index) {
        if(index < SECTOR_COLORS.length) {
            return context.getResources().getColor(SECTOR_COLORS[index]);
        }
        return 0;
    }

    private void initSectorColors(Context context) {
        for(int i=0, size=SECTOR_COLORS.length; i<size; i++) {
            addColor(context, SECTOR_COLORS[i]);
        }
    }

    private void addColor(Context context, int colorRes) {
        mColors.add(context.getResources().getColor(colorRes));
    }

    public void setSectorDatas(List<Float> datas) {
        while (datas.size() > MAX_SECTOR_COUNT) {
            datas.remove(datas.size() - 1);
        }
        mSectorDatas = datas;
        invalidate();
    }

    private void initDrawingPosition() {
        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();
        mCenterX = mDrawingWidth / 2;
        mCenterY = mDrawingHeight / 2;
        mRadius = mCenterX;
        mDrawingRect = new RectF(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initDrawingPosition();

        //画底板，也即外圈的灰色背景
        mPaint.setColor(mOuterRingColor);

        drawSectors(canvas);

        // 画内圈
        mPaint.setColor(mColorInner);
        canvas.drawCircle(mCenterX, mCenterY, mInnerRadius, mPaint);
    }

    private void drawSectors(Canvas canvas) {
        float startAngle = INITIAL_START_ANGLE;
        float sweepAngle;
        int count = mSectorDatas.size();
        if(count > 0) {
            for (int i = 0; i < count; i++) {
                sweepAngle = mSectorDatas.get(i) * FULL_CIRCLE;
                mPaint.setColor(mColors.get(i));
                canvas.drawArc(mDrawingRect, startAngle, sweepAngle, true, mPaint);
                startAngle += sweepAngle;
            }
        } else {
            mPaint.setColor(mEmptyColor);
            canvas.drawArc(mDrawingRect, 0, FULL_CIRCLE, true, mPaint);
        }
    }
}
