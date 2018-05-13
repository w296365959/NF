package com.sscf.investment.market.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.MathUtil;
import com.sscf.investment.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import BEC.StockDateMarginTrade;

/**
 * Created by yorkeehuang on 2017/4/15.
 */

public class StockMarginTradeView extends View {

    private  static final String TAG = StockMarginTradeView.class.getSimpleName();
    private static final int COLUMN_COUNT = 30;

    public static final int GET_DATA_COUNT = COLUMN_COUNT + 1;

    private List<ColumnData> mColumnDataList = new ArrayList<>(COLUMN_COUNT);

    private int mColumnWidth;
    private float mDayWidth;
    private int mTextAreaWidth;
    private int mTextPadding;
    private int mContentPadding;
    private int mBottomTextAreaHeight;
    private int mBorderStrokeWidth;

    private int mTextColor;

    private int mMa10Color;
    private int mColumnColor;
    private int mLastColumnColor;


    private int mDrawingWidth;
    private int mDrawingHeight;
    private int mContentMarginTop;

    private int mMarkLength;

    private int mTextSize;
    private int mTextHeight;
    private int mTextBottom;
    private TextDrawer mTextDrawer;
    private Typeface mTypeface;

    private DataRange mBalanceRange;
    private DataRange mUpRateRange;

    private int mAvgStrokeWidth;

    private Paint mPaintAxis;
    private Paint mPaintMark;
    private Paint mPaintLine;
    private Paint mPaintColumn;

    private Path mPath = new Path();

    public StockMarginTradeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        initResources();
    }

    private void initResources() {
        Resources resources = getResources();
        initDimens(resources);
        initColor(resources);
        initPaint(resources);
    }

    private void initDimens(final Resources resources) {
        mContentMarginTop = resources.getDimensionPixelSize(R.dimen.margin_trade_content_marginTop);
        mTextAreaWidth = resources.getDimensionPixelSize(R.dimen.margin_trade_text_area_width);
        mTextPadding = resources.getDimensionPixelSize(R.dimen.margin_trade_text_padding);
        mContentPadding = resources.getDimensionPixelSize(R.dimen.margin_trade_content_padding);
        mBottomTextAreaHeight = resources.getDimensionPixelSize(R.dimen.heat_line_bottom_text_height);
        mColumnWidth = resources.getDimensionPixelSize(R.dimen.column_width);
        mAvgStrokeWidth = resources.getDimensionPixelSize(R.dimen.avg_stroke_width);
        mBorderStrokeWidth = resources.getDimensionPixelSize(R.dimen.margin_trade_border_stroke_width);
        mMarkLength = resources.getDimensionPixelSize(R.dimen.margin_trade_mark_length);
    }

    private void initColor(final Resources resources) {
        mTextColor = ContextCompat.getColor(getContext(), R.color.default_text_color_60);
        mMa10Color = ContextCompat.getColor(getContext(), R.color.up_rate_color);
        mColumnColor = resources.getColor(R.color.column_color);
        mLastColumnColor = resources.getColor(R.color.last_column_color);
    }

    private void initPaint(final Resources resources) {
        mPaintAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAxis.setStrokeWidth(mBorderStrokeWidth);
        mTextSize = resources.getDimensionPixelSize(R.dimen.font_size_10);
        mPaintAxis.setTextSize(mTextSize);
        mTextDrawer = new TextDrawer();
        mTypeface = TextDrawer.getTypeface();
        mPaintAxis.setTypeface(mTypeface);
        mTextHeight = mTextDrawer.measureSingleTextHeight(mTextSize, mTypeface);
        mTextBottom = mTextDrawer.measureSingleTextBottom(mTextSize, mTypeface);

        mPaintMark = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintMark.setStrokeWidth(DeviceUtil.dip2px(getContext(), 0.5f));
        mPaintMark.setStyle(Paint.Style.FILL);
        mPaintMark.setColor(mTextColor);

        mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLine.setStyle(Paint.Style.STROKE);

        mPaintColumn = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintColumn.setStyle(Paint.Style.FILL);
    }

    public void setData(ArrayList<StockDateMarginTrade> stockDateMarginTradeList) {
        DtLog.d(TAG, "setData() stockDateMarginTradeList.size() = " + stockDateMarginTradeList.size());
        mBalanceRange = new DataRange(Float.MAX_VALUE, 0);
        mUpRateRange = new DataRange(Float.MAX_VALUE, Float.MIN_VALUE);
        compute(stockDateMarginTradeList);
        mBalanceRange.valueUnit = computeUnit(2, mBalanceRange.max, mBalanceRange.min);
        mUpRateRange.valueUnit = computeUnit(1, mUpRateRange.max, mUpRateRange.min);
        updateMinMaxShowValue(mBalanceRange);
        updateMinMaxShowValue(mUpRateRange);
        postInvalidate();
    }

    private void compute(ArrayList<StockDateMarginTrade> stockDateMarginTradeList) {
        mColumnDataList.clear();

        int entityCount = stockDateMarginTradeList.size();

        int columnCount = entityCount < COLUMN_COUNT ? entityCount : COLUMN_COUNT;

        for(int i=0; i<columnCount; i++) {
            ColumnData column = new ColumnData();

            StockDateMarginTrade current = stockDateMarginTradeList.get(i);
            column.margin = current;
            column.balance = current.getFMarginBalance();

            if(i + 1 < entityCount) {
                StockDateMarginTrade pre = stockDateMarginTradeList.get(i + 1);
                column.upRate = computeRiseRate(current, pre);
            }

            if(column.upRate != ColumnData.INVALID_RISE_RATE) {
                mUpRateRange.max = MathUtil.getMaxVal(column.upRate, mUpRateRange.max);
                mUpRateRange.min = MathUtil.getMinVal(column.upRate, mUpRateRange.min);
            }

            if(column.balance > 0) {
                mBalanceRange.max = MathUtil.getMaxVal(column.balance, mBalanceRange.max);
            }

            if(column.balance >= 0) {
                mBalanceRange.min = MathUtil.getMinVal(column.balance, mBalanceRange.min);
            }

            mColumnDataList.add(column);
        }
    }

    private float computeRiseRate(StockDateMarginTrade current, StockDateMarginTrade pre) {
        return (current.getFMarginBalance() - pre.getFMarginBalance()) * 100 / pre.getFMarginBalance();
    }

    private void updateDayWidth() {
        int count = mColumnDataList.size();
        float gap = (getChartWidth() - mColumnWidth * count) / (count - 1);
        mDayWidth = gap + mColumnWidth;
    }

    private long computeUnit(long minUnit, float max, float min) {
        float margin = max - min;
        long valueUnit = minUnit;
        do {
            if (margin >= 2) {
                margin /= 2;
                if (margin >= 1) {
                    valueUnit *= 2;
                }
            }

            if(margin / 5 >= 2) {
                margin /= 5;
                if (margin >= 2) {
                    valueUnit *= 5;
                }
            }
        } while (margin >= 10);

        return valueUnit;
    }

    private void updateMinMaxShowValue(DataRange range) {
        float maxUnits = range.max / range.valueUnit;
        float minUnits = range.min / range.valueUnit;
        long ceil = (long) Math.ceil(maxUnits);
        long floor = (long) Math.floor(minUnits);
        range.maxShowValue = ceil * range.valueUnit;
        range.minShowValue = floor * range.valueUnit;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();

        drawAxes(canvas);

        if (mColumnDataList == null || mColumnDataList.size() < 2) {
            return;
        }

        drawCoordinates(canvas);
        updateDayWidth();
        drawColumn(canvas);
    }

    private void drawXCoordinates(Canvas canvas) {
        int size = mColumnDataList.size();
        int centerIndex = -1;
        if(mColumnDataList.size() > 2){
            centerIndex = (int)Math.floor(mColumnDataList.size()*0.5f);
        }

        StockDateMarginTrade marginBegin = mColumnDataList.get(size - 1).margin;

        StockDateMarginTrade marginCenter = null;
        if(centerIndex > 0) {
            marginCenter = mColumnDataList.get(centerIndex).margin;
        }

        StockDateMarginTrade marginEnd = mColumnDataList.get(0).margin;

        float x = getContentLeft();
        float y = getChartHeight() + mContentMarginTop + mTextHeight;

        mPaintAxis.setColor(mTextColor);
        canvas.drawText(marginBegin.getSOpDate(), x, y, mPaintAxis);

        if(marginCenter != null) {
            String centerYmdStr = marginCenter.getSOpDate();
            int textWidth = mTextDrawer.measureSingleTextWidth(centerYmdStr, mTextSize, mTypeface);
            x = (int)(getChartLeft() + (getChartWidth() - textWidth) * 0.5f);
            canvas.drawText(centerYmdStr, x, y, mPaintAxis);
        }

        String endYmdStr = marginEnd.getSOpDate();
        int textWidth = mTextDrawer.measureSingleTextWidth(endYmdStr, mTextSize, mTypeface);
        x = getContentRight() - textWidth;
        canvas.drawText(endYmdStr, x, y, mPaintAxis);
    }

    private void drawLeftYCoordinates(Canvas canvas) {
        float x = getContentLeft();
        float y = getChartHeight();

        //画纵坐标轴上的值
        long drawValue = (mBalanceRange.maxShowValue - mBalanceRange.minShowValue) / mBalanceRange.valueUnit;
        if(drawValue > 0) {
            float itemHeight = y / drawValue;
            y = mContentMarginTop;
            mPaintAxis.setColor(mColumnColor);
            while (drawValue >= 0) { //从上往下画
                long showValue = drawValue * mBalanceRange.valueUnit + mBalanceRange.minShowValue;
                String showValueStr = StringUtil.getMarginTradingBalance(showValue);
                int textWidth = mTextDrawer.measureSingleTextWidth(showValueStr, mTextSize, mTypeface);
                canvas.drawText(showValueStr, x - mTextPadding - textWidth, y + (mTextHeight - mTextBottom) / 2, mPaintAxis);
                canvas.drawLine(x, y, x - mMarkLength, y, mPaintMark);
                drawValue--;
                y += itemHeight;
            }
        }
    }

    private void drawRightYCoordinates(Canvas canvas) {
        float x = getContentRight();
        float y = getChartHeight();

        //画纵坐标轴上的值
        long drawValue = (mUpRateRange.maxShowValue - mUpRateRange.minShowValue) / mUpRateRange.valueUnit;
        if(drawValue > 0) {
            float itemHeight =  y / drawValue;
            y = mContentMarginTop;
            mPaintAxis.setColor(mMa10Color);
            while (drawValue >= 0) { //从上往下画
                long showValue = drawValue * mUpRateRange.valueUnit + mUpRateRange.minShowValue;
                String showValueStr = showValue + "%";
                canvas.drawText(showValueStr, x + mTextPadding, y + (mTextHeight - mTextBottom) / 2, mPaintAxis);

                canvas.drawLine(x, y, x + mMarkLength, y, mPaintMark);
                drawValue--;
                y += itemHeight;
            }
        }
    }

    private float getContentLeft() {
        return mTextAreaWidth;
    }

    private float getContentRight() {
        return mDrawingWidth - mTextAreaWidth;
    }

    private float getChartLeft() {
        return mTextAreaWidth + mContentPadding;
    }

    private float getChartRight() {
        return mDrawingWidth - mTextAreaWidth - mContentPadding;
    }

    private float getChartWidth() {
        return mDrawingWidth - 2 * mTextAreaWidth - 2 * mContentPadding;
    }

    private int getChartHeight() {
        return mDrawingHeight - mBottomTextAreaHeight - mContentMarginTop;
    }

    private void drawAxes(Canvas canvas) {
        mPaintAxis.setColor(mTextColor);

        int left = mTextAreaWidth;
        int right = mDrawingWidth - mTextAreaWidth;
        int y = getChartHeight() + mContentMarginTop;

        canvas.drawLine(left, mContentMarginTop, right, mContentMarginTop, mPaintAxis);
        canvas.drawLine(left, y, right, y, mPaintAxis);
        canvas.drawLine(mTextAreaWidth, mContentMarginTop, mTextAreaWidth, mContentMarginTop + getChartHeight(), mPaintAxis);
        canvas.drawLine(mDrawingWidth - mTextAreaWidth, mContentMarginTop, mDrawingWidth - mTextAreaWidth, mContentMarginTop + getChartHeight(), mPaintAxis);

        if (mColumnDataList == null || mColumnDataList.size() < 2) {
            return;
        }
    }

    private void drawCoordinates(Canvas canvas) {
        drawXCoordinates(canvas);
        drawLeftYCoordinates(canvas);
        drawRightYCoordinates(canvas);
    }

    private void drawColumn(Canvas canvas) {
        float left = getChartLeft();
        int bottom = mDrawingHeight - mBottomTextAreaHeight;

        mPath.reset();

        int beginIndex = mColumnDataList.size() - 1;

        int pathBeginIndex;

        if(mColumnDataList.get(beginIndex).upRate == ColumnData.INVALID_RISE_RATE) {
            pathBeginIndex = beginIndex - 1;
        } else {
            pathBeginIndex = beginIndex;
        }

        for(int i=mColumnDataList.size() - 1; i>= 0; i--) {
            ColumnData column = mColumnDataList.get(i);
            DtLog.d(TAG, "upRate = " + StringUtil.getFormatedFloat(column.upRate) + "%");
            if(i == 0) {
                mPaintColumn.setColor(mLastColumnColor);
            } else {
                mPaintColumn.setColor(mColumnColor);
            }
            int top = (int)getValueHeight(mBalanceRange, column.balance);
            int right = (int)left + mColumnWidth;
            canvas.drawRect(left, top, right, bottom, mPaintColumn);


            float drawingHeight = getValueHeight(mUpRateRange, column.upRate);

            if (i == pathBeginIndex) {
                mPath.moveTo(left + mColumnWidth / 2, drawingHeight);
            } else if(i < pathBeginIndex) {
                mPath.lineTo(left + mColumnWidth / 2, drawingHeight);
            }

            left += mDayWidth;
        }
        mPaintLine.setStrokeWidth(mAvgStrokeWidth);
        mPaintLine.setColor(mMa10Color);
        canvas.drawPath(mPath, mPaintLine);
    }

    private float getValueHeight(DataRange range, float value) {
        int y = getChartHeight();
        float height = (value - range.minShowValue) / (range.maxShowValue - range.minShowValue) * y;
        return y - height + mContentMarginTop;
    }

    private class DataRange {
        private float min;
        private float max;
        private long valueUnit;
        private long minShowValue;
        private long maxShowValue;

        DataRange(float min, float max) {
            this.min = min;
            this.max = max;
        }
    }

    private class ColumnData {
        static final float INVALID_RISE_RATE = -2f;
        float upRate = INVALID_RISE_RATE;
        float balance;

        StockDateMarginTrade margin;
    }
}
