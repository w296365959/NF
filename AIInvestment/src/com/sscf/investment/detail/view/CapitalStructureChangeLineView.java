package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;

import java.util.ArrayList;

import BEC.CapitalStructureDetail;

/**
 * Created by liqf on 2016/2/17.
 */
public class CapitalStructureChangeLineView extends View {
    private static final int MAX_COUNT = 75;
    private ArrayList<CapitalStructureDetail> mDataList = new ArrayList<>();
    private String mLastValueStr1;
    private String mLastValueStr2;

    private float mMinHot;
    private float mMaxHot;
    private int mValueUnit;
    private int mMaxShowValue;
    private boolean mInited;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private int mRightTextWidth;
    private int mBottomTextAreaHeight;

    /**
     * 顶部留一点空余，避免有时候最后一个点的值的文字标注在顶部时画到canvas之外显示不出来了
     */
    private int mTopPadding;

    private int mTextMargin;
    private Paint mPaintAxis;
    private int mCircleRadius;
    private int mBorderStrokeWidth;
    private int mBorderRectColor;
    private Paint mPaintDashLine;
    private int mTextSize;
    private TextDrawer mTextDrawer;
    private Typeface mTypeface;
    private int mTextColor;
    private int mTextColorDarker;
    private int mTextHeight;
    private Path mPath;
    private Paint mPaintLine;
    private int mLineColorShareholderNumber;
    private int mLineColorPerCapitaHoldings;
    private float mLineStrokeWidth;

    private Path mShadowPath;
    private Paint mShadowPaint;
    private int mShadowStartColor;
    private int mBgColor;

    private float mLastDrawingHeight1;
    private float mLastDrawingHeight2;

    public CapitalStructureChangeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        initResources();
    }

    private void initResources() {
        Resources resources = getResources();
        mRightTextWidth = resources.getDimensionPixelSize(R.dimen.heat_line_right_text_width);
        mBottomTextAreaHeight = resources.getDimensionPixelSize(R.dimen.capital_structure_chart_bottom_text_height);
        mTopPadding = resources.getDimensionPixelSize(R.dimen.capital_structure_chart_top_padding);
        mTextMargin = resources.getDimensionPixelSize(R.dimen.capital_structure_chart_text_margin);

        mBorderStrokeWidth = resources.getDimensionPixelSize(R.dimen.line_chart_divider_strokeWidth);

        mLineStrokeWidth = resources.getDimensionPixelSize(R.dimen.line_chart_time_strokeWidth);

        mCircleRadius = resources.getDimensionPixelSize(R.dimen.capital_structure_circle_radius);

        TypedArray a = getContext().obtainStyledAttributes(new int[] {
                R.attr.heat_line_color,
                R.attr.heat_shadow_color,
                R.attr.line_chart_border_rect_color
        });
        mLineColorShareholderNumber = a.getColor(0, Color.GRAY);
        mShadowStartColor = a.getColor(1, Color.GRAY);
        mBorderRectColor = a.getColor(2, Color.GRAY);
        a.recycle();
        mTextColor = ContextCompat.getColor(getContext(), R.color.default_text_color_60);
        mTextColorDarker = ContextCompat.getColor(getContext(), R.color.default_text_color_80);
        mBgColor = ContextCompat.getColor(getContext(), R.color.default_content_bg);

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

        mPath = new Path();

        mLineColorPerCapitaHoldings = resources.getColor(R.color.k_line_average_3);
        mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(mLineColorShareholderNumber);
        mPaintLine.setStrokeWidth(mLineStrokeWidth);

        mShadowPath = new Path();
        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();

        canvas.translate(0, mTopPadding);

        drawAxes(canvas);

        if (mDataList == null || mDataList.size() < 2) {
            return;
        }

        drawCoordinates(canvas);

        drawShareholderNumberLine(canvas);

        drawPerCapitaHoldingsLine(canvas);

        drawLastPointValues(canvas);

        canvas.translate(0, -mTopPadding);
    }

    private void drawLastPointValues(Canvas canvas) {
        int textWidth1 = mTextDrawer.measureSingleTextWidth(mLastValueStr1, mTextSize, mTypeface);
        int textWidth2 = mTextDrawer.measureSingleTextWidth(mLastValueStr2, mTextSize, mTypeface);
        int offset = mCircleRadius * 4;
        if (mLastDrawingHeight1 <= mLastDrawingHeight2) { //股东人数位置在上
            mPaintAxis.setColor(mLineColorShareholderNumber);
            canvas.drawText(mLastValueStr1, mDrawingWidth - textWidth1, mLastDrawingHeight1 - offset, mPaintAxis);
            mPaintAxis.setColor(mLineColorPerCapitaHoldings);
            canvas.drawText(mLastValueStr2, mDrawingWidth - textWidth2, mLastDrawingHeight2 + mTextHeight + offset, mPaintAxis);
        } else {
            mPaintAxis.setColor(mLineColorShareholderNumber);
            canvas.drawText(mLastValueStr1, mDrawingWidth - textWidth1, mLastDrawingHeight1 + mTextHeight + offset - mTextMargin, mPaintAxis);
            mPaintAxis.setColor(mLineColorPerCapitaHoldings);
            canvas.drawText(mLastValueStr2, mDrawingWidth - textWidth2, mLastDrawingHeight2 - offset, mPaintAxis);
        }
    }

    private void drawAxes(Canvas canvas) {
        int x = mDrawingWidth;
        int y = getChartHeight();
        //画横轴
//        canvas.drawLine(0, y, x, y, mPaintAxis);
        //画纵轴
//        canvas.drawLine(x, mContentMarginTop, x, y, mPaintAxis);

        mPaintAxis.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, x, y, mPaintAxis);

        if (mDataList == null || mDataList.size() < 2) {
            return;
        }

        //画横向虚线
        int drawValue = mMaxShowValue / mValueUnit;
        int itemHeight = getChartHeight() / drawValue;
        y = 0;
        while (drawValue > 0) { //从上往下画
            canvas.drawLine(0, y, x, y, mPaintDashLine);
            drawValue--;
            y += itemHeight;
        }
    }

    private void drawCoordinates(Canvas canvas) {
        drawXCoordinates(canvas);
//        drawYCoordinates(canvas);
    }

    private void drawXCoordinates(Canvas canvas) {
        int size = mDataList.size();
        CapitalStructureDetail dataBegin = mDataList.get(size - 1);
        CapitalStructureDetail dataEnd = mDataList.get(0);
        String beginYmd = dataBegin.getSDate();
        String endYmd = dataEnd.getSDate();
        float x = 0;
        float y = getChartHeight() + mTextHeight;
        y += mTextMargin;

        mPaintAxis.setColor(mTextColor);
        mPaintAxis.setStyle(Paint.Style.FILL);
        canvas.drawText(beginYmd, x + mTextMargin, y, mPaintAxis);

        int textWidth = mTextDrawer.measureSingleTextWidth(endYmd, mTextSize, mTypeface);
        x = mDrawingWidth - textWidth;
        canvas.drawText(endYmd, x - mTextMargin, y, mPaintAxis);
    }

    private void drawShareholderNumberLine(Canvas canvas) {
        mPath.reset();

        float x = mDrawingWidth;
        int count = MAX_COUNT - 1;
        count = Math.min(count, mDataList.size() - 1);
        float itemWidth = x / (count);

        float startX = 0;
        int size = mDataList.size();

        mPaintLine.setColor(mLineColorShareholderNumber);
        mShadowPath.reset();
        int y = getChartHeight();
        mShadowPath.moveTo(startX, y);
        for (int i = size - 1; i >= 0; i--) {
            CapitalStructureDetail detail = mDataList.get(i);
            float hot = detail.getIShareholderNumber();
            float drawingHeight = getDrawingHeight(hot);

            mShadowPath.lineTo(startX, drawingHeight);

            if (i == size - 1) {
                mPath.moveTo(startX, drawingHeight);
            } else if (i == 0) {
                mPath.lineTo(startX - mCircleRadius * 2, drawingHeight);
            } else {
                mPath.lineTo(startX, drawingHeight);
            }

            if (i == 0) {
                mShadowPath.lineTo(startX, y);
            }

            if (i == size - 1) { //first node
                //draw nothing
            } else if (i == 0) { //last node
                mLastDrawingHeight1 = drawingHeight;
            } else { //middle node
//                mPaintLine.setStyle(Paint.Style.FILL);
//                canvas.drawCircle(startX, drawingHeight, mCircleRadius, mPaintLine);
            }

            startX += itemWidth;
        }

        mShadowPath.close();
        LinearGradient gradient = new LinearGradient(0, 0, 0, y, mShadowStartColor, mBgColor, Shader.TileMode.CLAMP);
        mShadowPaint.setShader(gradient);
        canvas.drawPath(mShadowPath, mShadowPaint);

        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(mLineColorShareholderNumber);
        canvas.drawPath(mPath, mPaintLine);

        mPaintLine.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x - mCircleRadius * 2, mLastDrawingHeight1, mCircleRadius * 2, mPaintLine);
        mPaintLine.setColor(mBgColor);
        canvas.drawCircle(x - mCircleRadius * 2, mLastDrawingHeight1, mCircleRadius, mPaintLine);
    }

    private float getDrawingHeight(float hot) {
        int y = getChartHeight();
        float height = hot / mMaxShowValue * y;
        return y - height;
    }

    /**
     * 获取框图的矩形区域高度，不含底部的文字标注区域
     * @return
     */
    private int getChartHeight() {
        return mDrawingHeight - mBottomTextAreaHeight - mTopPadding;
    }

    private void drawPerCapitaHoldingsLine(Canvas canvas) {
        mPath.reset();

        float max = -1, min = Float.MAX_VALUE;
        for (CapitalStructureDetail capitalStructureDetail : mDataList) {
            float holdings = capitalStructureDetail.getFPerCapitaHoldings();
            if (holdings > max) {
                max = holdings;
            }
            if (holdings < min) {
                min = holdings;
            }
        }
        float range = max - min;

        float x = mDrawingWidth;
        int count = MAX_COUNT - 1;
        count = Math.min(count, mDataList.size() - 1);
        float itemWidth = x / (count);

        float startX = 0;
        int size = mDataList.size();

        mPaintLine.setColor(mLineColorPerCapitaHoldings);
        for (int i = size - 1; i >= 0; i--) {
            CapitalStructureDetail detail = mDataList.get(i);
            float hot = detail.getFPerCapitaHoldings();

            float normalizedValue = range != 0 ? (hot - min) / range : 0.0f;
            //把归一化的值从0到1映射到0.3到0.7的区间
            normalizedValue = 0.4f * normalizedValue + 0.3f;
            float drawingHeight = (1 - normalizedValue) * getChartHeight();

            if (i == size - 1) {
                mPath.moveTo(startX, drawingHeight);
            } else if (i == 0) {
                mPath.lineTo(startX - mCircleRadius * 2, drawingHeight);
            } else {
                mPath.lineTo(startX, drawingHeight);
            }

            if (i == size - 1) { //first node
                //draw nothing
            } else if (i == 0) { //last node
                mLastDrawingHeight2 = drawingHeight;
            } else { //middle node
//                mPaintLine.setStyle(Paint.Style.FILL);
//                canvas.drawCircle(startX, drawingHeight, mCircleRadius, mPaintLine);
            }

            startX += itemWidth;
        }

        mPaintLine.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, mPaintLine);

        mPaintLine.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x - mCircleRadius * 2, mLastDrawingHeight2, mCircleRadius * 2, mPaintLine);
        mPaintLine.setColor(mBgColor);
        canvas.drawCircle(x - mCircleRadius * 2, mLastDrawingHeight2, mCircleRadius, mPaintLine);
    }

    public void setData(ArrayList<CapitalStructureDetail> capitalStructureDetails, String sShareholderNumber, String sPerCapitaHoldings) {
        if (capitalStructureDetails == null) {
            mDataList = null;
            return;
        }

        mDataList.clear();
        if (capitalStructureDetails.size() > MAX_COUNT) {
            mDataList.addAll(capitalStructureDetails.subList(0, MAX_COUNT));
        } else {
            mDataList.addAll(capitalStructureDetails);
        }
        mLastValueStr1 = sShareholderNumber;
        mLastValueStr2 = sPerCapitaHoldings;

        updateValues();

        invalidate();
    }

    private void updateValues() {
        updateMinMaxValue();
        updateValueUnit();
        updateMaxShowValue();
    }

    private void updateMinMaxValue() {
        mMinHot = Float.MAX_VALUE;
        mMaxHot = 0;
        for (CapitalStructureDetail detail : mDataList) {
            float hot = detail.getIShareholderNumber();
            if (hot < mMinHot) {
                mMinHot = hot;
            }
            if (hot > mMaxHot) {
                mMaxHot = hot;
            }
        }
    }

    private void updateValueUnit() {
        float max = mMaxHot;
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
        float maxUnits = mMaxHot / mValueUnit;
        int ceil = (int) Math.ceil(maxUnits);
        mMaxShowValue = ceil * mValueUnit;
    }
}
