package com.sscf.investment.market.view;

import BEC.MarginTradeInfo;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.utils.StringUtil;

import java.util.ArrayList;

/**
 * Created by davidwei on 2015/11/04.
 */
public class MarginTradingBalanceView extends View {
    private static final int MAX_COUNT = 100;
    private ArrayList<MarginTradeInfo> mHeatList = new ArrayList<>();

    private float mMinHot;
    private float mMaxHot;
    private long mValueUnit;
    private long mMaxShowValue;
    private boolean mInited;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private int mContentMarginTop;
    private int mRightTextAreaWidth;
    private int mRightTextWidth;
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
    private Path mPath;
    private Paint mPaintLine;
    private int mLineColor;
    private float mLineStrokeWidth;

    private Path mShadowPath;
    private Paint mShadowPaint;
    private int mShadowStartColor;
    private int mBgColor;

    public MarginTradingBalanceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        initResources();
    }

    private void initResources() {
        Resources resources = getResources();
        mContentMarginTop = resources.getDimensionPixelSize(R.dimen.heat_line_content_marginTop);
        mRightTextAreaWidth = resources.getDimensionPixelSize(R.dimen.heat_line_right_text_area_width);
        mRightTextWidth = resources.getDimensionPixelSize(R.dimen.heat_line_right_text_width);
        mBottomTextAreaHeight = resources.getDimensionPixelSize(R.dimen.heat_line_bottom_text_height);

        mBorderStrokeWidth = resources.getDimensionPixelSize(R.dimen.line_chart_divider_strokeWidth);

        mLineStrokeWidth = resources.getDimensionPixelSize(R.dimen.line_chart_time_strokeWidth);

        TypedArray a = getContext().obtainStyledAttributes(new int[] {
            R.attr.heat_line_color,
            R.attr.heat_shadow_color,
            R.attr.line_chart_border_rect_color,
        });
        mLineColor = a.getColor(0, Color.GRAY);
        mShadowStartColor = a.getColor(1, Color.GRAY);
        mBorderRectColor = a.getColor(2, Color.GRAY);
        a.recycle();
        mTextColor = ContextCompat.getColor(getContext(), R.color.default_text_color_60);
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

        mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(mLineColor);
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

        drawAxes(canvas);

        if (mHeatList == null || mHeatList.size() < 2) {
            return;
        }

        drawCoordinates(canvas);

        drawHeatLine(canvas);
    }

    private void drawAxes(Canvas canvas) {
        mPaintAxis.setColor(mBorderRectColor);

        int x = mDrawingWidth - mRightTextAreaWidth;
        int y = getChartHeight() + mContentMarginTop;
        //画横轴
        canvas.drawLine(0, y, x, y, mPaintAxis);
        //画纵轴
        canvas.drawLine(x, mContentMarginTop, x, y, mPaintAxis);

        if (mHeatList == null || mHeatList.size() < 2) {
            return;
        }

        //画横向虚线
        long drawValue = mMaxShowValue / mValueUnit;
        int itemHeight = (int) (getChartHeight() / drawValue);
        y = mContentMarginTop;
        while (drawValue > 0) { //从上往下画
            canvas.drawLine(0, y, x, y, mPaintDashLine);
            drawValue--;
            y += itemHeight;
        }
    }

    private void drawCoordinates(Canvas canvas) {
        drawXCoordinates(canvas);
        drawYCoordinates(canvas);
    }

    private void drawXCoordinates(Canvas canvas) {
        int size = mHeatList.size();
        MarginTradeInfo heatBegin = mHeatList.get(size - 1);
        MarginTradeInfo heatEnd = mHeatList.get(0);
        float x = 0;
        float y = getChartHeight() + mContentMarginTop + mTextHeight;

        mPaintAxis.setColor(mTextColor);
        canvas.drawText(heatBegin.sDate, x, y, mPaintAxis);

        String endYmdStr = heatEnd.sDate;
        int textWidth = mTextDrawer.measureSingleTextWidth(endYmdStr, mTextSize, mTypeface);
        x = mDrawingWidth - mRightTextAreaWidth - textWidth;
        canvas.drawText(endYmdStr, x, y, mPaintAxis);
    }

    private void drawYCoordinates(Canvas canvas) {
        int x = mDrawingWidth - mRightTextAreaWidth;
        int y = getChartHeight();

        //画纵坐标轴上的值
        long drawValue = mMaxShowValue / mValueUnit;
        int itemHeight = (int) (y / drawValue);
        y = mContentMarginTop;
        mPaintAxis.setColor(mTextColor);
        while (drawValue >= 0) { //从上往下画
            long showValue = drawValue * mValueUnit;
            String showValueStr = StringUtil.getMarginTradingBalance(showValue);
            int textWidth = mTextDrawer.measureSingleTextWidth(showValueStr, mTextSize, mTypeface);
            canvas.drawText(showValueStr, x + (mRightTextWidth - textWidth), y + mTextHeight / 2, mPaintAxis);
            drawValue--;
            y += itemHeight;
        }
    }

    private void drawHeatLine(Canvas canvas) {
        mPath.reset();

        float x = mDrawingWidth - mRightTextAreaWidth;
        int count = MAX_COUNT - 1;
        count = Math.min(count, mHeatList.size() - 1);
        float itemWidth = x / (count);

        float startX = 0;
        int size = mHeatList.size();

        mShadowPath.reset();
        mShadowPath.moveTo(startX, mDrawingHeight - mBottomTextAreaHeight);
        for (int i = size - 1; i >= 0; i--) {

            MarginTradeInfo heat = mHeatList.get(i);
            float hot = heat.fBuyBalance - heat.fSellBalance;
            float drawingHeight = getDrawingHeight(hot);

            mShadowPath.lineTo(startX, drawingHeight);

            if (i == size - 1) {
                mPath.moveTo(startX, drawingHeight);
            } else {
                mPath.lineTo(startX, drawingHeight);
            }

            if (i == 0) {
                mShadowPath.lineTo(startX, mDrawingHeight - mBottomTextAreaHeight);
            }

            startX += itemWidth;
        }

        canvas.drawPath(mPath, mPaintLine);

        mShadowPath.close();

        LinearGradient gradient = new LinearGradient(0, 0, 0, mDrawingHeight - mBottomTextAreaHeight, mShadowStartColor, mBgColor, Shader.TileMode.CLAMP);
        mShadowPaint.setShader(gradient);
        canvas.drawPath(mShadowPath, mShadowPaint);
    }

    private float getDrawingHeight(float hot) {
        int y = getChartHeight();
        float height = hot / mMaxShowValue * y;
        return y - height + mContentMarginTop;
    }

    private int getChartHeight() {
        return mDrawingHeight - mBottomTextAreaHeight - mContentMarginTop;
    }

    public void setHeatData(ArrayList<MarginTradeInfo> indexDescs) {
        if (indexDescs == null) {
            mHeatList = null;
            return;
        }

        mHeatList.clear();
        if (indexDescs.size() > MAX_COUNT) {
            mHeatList.addAll(indexDescs.subList(0, MAX_COUNT));
        } else {
            mHeatList.addAll(indexDescs);
        }

        updateValues();

        postInvalidate();
    }

    private void updateValues() {
        updateMinMaxValue();
        updateValueUnit();
        updateMaxShowValue();
    }

    private void updateMinMaxValue() {
        mMinHot = Float.MAX_VALUE;
        mMaxHot = 0;
        for (MarginTradeInfo heat : mHeatList) {
            float hot = heat.fBuyBalance - heat.fSellBalance;
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
        long valueUnit = 1;
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
        long ceil = (long) Math.ceil(maxUnits);
        mMaxShowValue = ceil * mValueUnit;
    }
}
