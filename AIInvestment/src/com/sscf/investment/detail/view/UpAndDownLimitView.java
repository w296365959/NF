package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;

import java.util.ArrayList;

import BEC.MarketStatDesc;

/**
 * Created by liqf on 2015/8/17.
 */
public class UpAndDownLimitView extends View {
    private int mMaxValue;
    private int mValueUnit;
    private int mMaxShowValue;
    private boolean mInited;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private int mContentMarginTop;
    private int mRightTextAreaWidth;
    private int mBottomTextAreaHeight;
    private Paint mPaintAxis;
    private int mBorderStrokeWidth;
    private int mBorderRectColor;
    private Paint mPaintDashLine;
    private int mTextSize;
    private TextDrawer mTextDrawer;
    private Typeface mTypeface;
    private int mTextColor;
    private int mTextHeight;
    private Paint mPaintLine;
    private int mLineColor;
    private float mLineStrokeWidth;

    private int mColorRiseLimit;
    private int mColorDownLimit;

    private ArrayList<MarketStatDesc> mMarketStatDescList;
    private int mTradingMinutes = 240;

    public UpAndDownLimitView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        initResources();
    }

    private void initResources() {
        Resources resources = getResources();
        mContentMarginTop = resources.getDimensionPixelSize(R.dimen.heat_line_content_marginTop);
        mRightTextAreaWidth = resources.getDimensionPixelSize(R.dimen.up_and_down_right_text_area_width);
        mBottomTextAreaHeight = resources.getDimensionPixelSize(R.dimen.heat_line_bottom_text_height);

        mBorderStrokeWidth = resources.getDimensionPixelSize(R.dimen.line_chart_divider_strokeWidth);

        mLineStrokeWidth = resources.getDimensionPixelSize(R.dimen.line_chart_time_strokeWidth);

        mColorRiseLimit = resources.getColor(R.color.dengta_a_rise_limit);
        mColorDownLimit = resources.getColor(R.color.dengta_a_down_limit);

        TypedArray a = getContext().obtainStyledAttributes(new int[] {
            R.attr.heat_line_color,
            R.attr.line_chart_border_rect_color,
        });
        mLineColor = a.getColor(0, Color.GRAY);
        mBorderRectColor = a.getColor(1, Color.GRAY);
        a.recycle();
        mTextColor = ContextCompat.getColor(getContext(), R.color.default_text_color_60);

        mTextSize = resources.getDimensionPixelSize(R.dimen.font_size_10);
        mTextDrawer = new TextDrawer();
        mTypeface = TextDrawer.getTypeface();

        mPaintAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAxis.setStrokeWidth(mBorderStrokeWidth);
        mPaintAxis.setColor(mBorderRectColor);
        mPaintAxis.setTextSize(mTextSize);
        mPaintAxis.setTypeface(mTypeface);
        mTextHeight = mTextDrawer.measureSingleTextHeight(mTextSize, mTypeface);

        mPaintDashLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintDashLine.setStyle(Paint.Style.STROKE);
        mPaintDashLine.setColor(mBorderRectColor);
        PathEffect pathEffect = new DashPathEffect(new float[]{8*2, 4*2}, 0);
        mPaintDashLine.setPathEffect(pathEffect);

        mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(mLineColor);
        mPaintLine.setStrokeWidth(mLineStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();

        drawAxes(canvas);

        if (mMarketStatDescList == null || mMarketStatDescList.size() < 2) {
            return;
        }

        drawCoordinates(canvas);

        if (mMaxShowValue != 0) {
            drawLines(canvas);
        }
    }

    private void drawAxes(Canvas canvas) {
        int x = mDrawingWidth - mRightTextAreaWidth;
        int y = getChartHeight() + mContentMarginTop;
        mPaintAxis.setColor(mBorderRectColor);
        mPaintAxis.setStrokeWidth(mBorderStrokeWidth);
        //画横轴
        canvas.drawLine(0, y, x, y, mPaintAxis);
        //画纵轴
        canvas.drawLine(x, mContentMarginTop, x, y, mPaintAxis);

        if (mMarketStatDescList == null || mMarketStatDescList.size() < 2 || mMaxShowValue == 0) {
            return;
        }

        //画横向虚线
        int drawValue = mMaxShowValue / mValueUnit;
        int itemHeight = getChartHeight() / drawValue;
        y = mContentMarginTop;
        while (drawValue > 0) { //从上往下画
            canvas.drawLine(0, y, x, y, mPaintDashLine);
            drawValue--;
            y += itemHeight;
        }
    }

    private void drawCoordinates(Canvas canvas) {
        drawXCoordinates(canvas);
        if (mMaxShowValue != 0) {
            drawYCoordinates(canvas);
        }
    }

    private void drawXCoordinates(Canvas canvas) {
        float x = 0;
        float y = getChartHeight() + mContentMarginTop + mTextHeight;

        mPaintAxis.setColor(mTextColor);
        mPaintAxis.setTextSize(mTextSize);
        mPaintAxis.setTypeface(mTypeface);

        String beginStr = "09:30";
        canvas.drawText(beginStr, x, y, mPaintAxis);

        String middleStr = "11:30/13:00";
        int textWidth = mTextDrawer.measureSingleTextWidth(middleStr, mTextSize, mTypeface);
        x = (mDrawingWidth - mRightTextAreaWidth) / 2 - textWidth / 2;
        canvas.drawText(middleStr, x, y, mPaintAxis);

        String endStr = "15:00";
        textWidth = mTextDrawer.measureSingleTextWidth(endStr, mTextSize, mTypeface);
        x = mDrawingWidth - mRightTextAreaWidth - textWidth;
        canvas.drawText(endStr, x, y, mPaintAxis);
    }

    private void drawYCoordinates(Canvas canvas) {
        int x = mDrawingWidth - mRightTextAreaWidth;
        int y = getChartHeight();

        //画纵坐标轴上的值
        int drawValue = mMaxShowValue / mValueUnit;
        int itemHeight = y / drawValue;
        y = mContentMarginTop;
        mPaintAxis.setColor(mTextColor);
        while (drawValue >= 0) { //从上往下画
            int showValue = drawValue * mValueUnit;
            String showValueStr = String.valueOf(showValue);
            canvas.drawText(showValueStr, x + 10, y + mTextHeight / 2, mPaintAxis);
            drawValue--;
            y += itemHeight;
        }
    }

    private void drawLines(Canvas canvas) {
        float itemWidth = (mDrawingWidth - mRightTextAreaWidth) / (float) mTradingMinutes;
        float startX = 0;
        float startY = 100;
        float stopY = 100;
        mPaintAxis.setStrokeWidth(mLineStrokeWidth);
        for (int i = 0; i < mMarketStatDescList.size() - 1; i++) {
            MarketStatDesc marketStatDesc = mMarketStatDescList.get(i);
            MarketStatDesc marketStatDescNext = mMarketStatDescList.get(i + 1);

            if ((marketStatDesc == null || marketStatDescNext == null)
                || marketStatDesc.getITime() == 0 || marketStatDescNext.getITime() == 0) {
                startX += itemWidth;
                continue;
            }

            int max = marketStatDesc.getIChangeMax();
            int min = marketStatDesc.getIChangeMin();

            int maxNext = marketStatDescNext.getIChangeMax();
            int minNext = marketStatDescNext.getIChangeMin();

            startY = getDrawingHeight(max);
            stopY = getDrawingHeight(maxNext);
            mPaintAxis.setColor(mColorRiseLimit);
            canvas.drawLine(startX, startY, startX + itemWidth, stopY, mPaintAxis);

            startY = getDrawingHeight(min);
            stopY = getDrawingHeight(minNext);
            mPaintAxis.setColor(mColorDownLimit);
            canvas.drawLine(startX, startY, startX + itemWidth, stopY, mPaintAxis);

            startX += itemWidth;
        }
    }

    private float getDrawingHeight(float value) {
        int y = getChartHeight();
        float height = value / mMaxShowValue * y;
        return y - height + mContentMarginTop;
    }

    private int getChartHeight() {
        return mDrawingHeight - mBottomTextAreaHeight - mContentMarginTop;
    }

    private void updateValues() {
        updateMinMaxValue();
        updateValueUnit();
        updateMaxShowValue();
    }

    private void updateMinMaxValue() {
        mMaxValue = 0;

        for (MarketStatDesc marketStatDesc : mMarketStatDescList) {
            int min = marketStatDesc.getIChangeMin();
            int max = marketStatDesc.getIChangeMax();
            if (min > mMaxValue) {
                mMaxValue = min;
            }
            if (max > mMaxValue) {
                mMaxValue = max;
            }
        }
    }

    private void updateValueUnit() {
        float max = mMaxValue;
        int valueUnit = 1;
        do {
            if (max >= 10) {
                max /= 10;
                if (max >= 1) {
                    valueUnit *= 10;
                }
            }
        } while (max >= 10);

        mValueUnit = valueUnit;
    }

    private void updateMaxShowValue() {
        float maxUnits = (float) mMaxValue / mValueUnit;
        int ceil = (int) Math.ceil(maxUnits);
        mMaxShowValue = ceil * mValueUnit;
    }

    public void setData(ArrayList<MarketStatDesc> marketStatDescList) {
        mMarketStatDescList = marketStatDescList;

        updateValues();

        invalidate();
    }
}
