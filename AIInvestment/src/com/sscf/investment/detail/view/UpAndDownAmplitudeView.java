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
 * Created by liqf on 2016/4/13.
 */
public class UpAndDownAmplitudeView extends View {
    private Paint mPaintAxis;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private int mContentMarginTop;
    private int mRightTextAreaWidth;
    private int mBottomTextAreaHeight;
    private Paint mPaintDashLine;
    private int mBorderRectColor;
    private int mBorderStrokeWidth;
    private float mLineStrokeWidth;
    private int mTextSize;
    private TextDrawer mTextDrawer;
    private Typeface mTypeface;
    private int mTextColor;
    private int mTextHeight;
    private ArrayList<MarketStatDesc> mMarketStatDescList;
    private int mTradingMinutes = 240;

    private int mColorRiseBig;
    private int mColorRise;
    private int mColorDown;
    private int mColorDownBig;

    public UpAndDownAmplitudeView(Context context, AttributeSet attrs) {
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
        mLineStrokeWidth = resources.getDimensionPixelSize(R.dimen.up_and_down_line_stroke_width);

        mLineStrokeWidth = resources.getDimensionPixelSize(R.dimen.line_chart_time_strokeWidth);

        mColorRise = resources.getColor(R.color.dengta_a_rise);
        mColorRiseBig = resources.getColor(R.color.dengta_a_big_rise);
        mColorDown = resources.getColor(R.color.dengta_a_down);
        mColorDownBig = resources.getColor(R.color.dengta_a_big_down);

        TypedArray a = getContext().obtainStyledAttributes(new int[] {
            R.attr.line_chart_border_rect_color,
        });
        mBorderRectColor = a.getColor(0, Color.GRAY);
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

        mPaintAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();

        drawAxes(canvas);

        drawCoordinates(canvas);

        if (mMarketStatDescList == null || mMarketStatDescList.size() == 0) {
            return;
        }

        drawLines(canvas);
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

            int change5 = marketStatDesc.getIChange5();
            int change10 = marketStatDesc.getIChange10();
            int changeN5 = marketStatDesc.getIChangeN5();
            int changeN10 = marketStatDesc.getIChangeN10();
            int total = change5 + change10 + changeN5 + changeN10;

            int change5Next = marketStatDescNext.getIChange5();
            int change10Next = marketStatDescNext.getIChange10();
            int changeN5Next = marketStatDescNext.getIChangeN5();
            int changeN10Next = marketStatDescNext.getIChangeN10();
            int totalNext = change5Next + change10Next + changeN5Next + changeN10Next;

            float value = 0;
            float valueNext = 0;

            value = (float) change5 / total;
            startY = getDrawingHeightByValue(value);
            valueNext = (float) change5Next / totalNext;
            stopY = getDrawingHeightByValue(valueNext);
            mPaintAxis.setColor(mColorRise);
            canvas.drawLine(startX, startY, startX + itemWidth, stopY, mPaintAxis);

            value = (float) change10 / total;
            startY = getDrawingHeightByValue(value);
            valueNext = (float) change10Next / totalNext;
            stopY = getDrawingHeightByValue(valueNext);
            mPaintAxis.setColor(mColorRiseBig);
            canvas.drawLine(startX, startY, startX + itemWidth, stopY, mPaintAxis);

            value = (float) changeN10 / total;
            startY = getDrawingHeightByValue(value);
            valueNext = (float) changeN10Next / totalNext;
            stopY = getDrawingHeightByValue(valueNext);
            mPaintAxis.setColor(mColorDownBig);
            canvas.drawLine(startX, startY, startX + itemWidth, stopY, mPaintAxis);

            value = (float) changeN5 / total;
            startY = getDrawingHeightByValue(value);
            valueNext = (float) changeN5Next / totalNext;
            stopY = getDrawingHeightByValue(valueNext);
            mPaintAxis.setColor(mColorDown);
            canvas.drawLine(startX, startY, startX + itemWidth, stopY, mPaintAxis);

            startX += itemWidth;
        }
    }

    private float getDrawingHeightByValue(float value) {
        float drawingHeight = 0;

        float startY = mContentMarginTop;
        float contentHeight = getChartHeight();
        drawingHeight = (1 - value) * contentHeight + startY;

        return drawingHeight;
    }

    private void drawCoordinates(Canvas canvas) {
        drawXCoordinates(canvas);
        drawYCoordinates(canvas);
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
        int drawValue = 5;
        int itemHeight = y / drawValue;
        y = mContentMarginTop;
        mPaintAxis.setColor(mTextColor);
        while (drawValue >= 0) { //从上往下画
            int showValue = drawValue * 20;
            String showValueStr = String.valueOf(showValue) + "%";
            canvas.drawText(showValueStr, x + 10, y + mTextHeight / 2, mPaintAxis);
            drawValue--;
            y += itemHeight;
        }
    }

    private int getChartHeight() {
        return mDrawingHeight - mBottomTextAreaHeight - mContentMarginTop;
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

        //画横向虚线
        int drawValue = 5;
        int itemHeight = getChartHeight() / drawValue;
        y = mContentMarginTop;
        while (drawValue > 0) { //从上往下画
            canvas.drawLine(0, y, x, y, mPaintDashLine);
            drawValue--;
            y += itemHeight;
        }
    }

    public void setData(ArrayList<MarketStatDesc> marketStatDesc) {
        mMarketStatDescList = marketStatDesc;

        invalidate();
    }
}
