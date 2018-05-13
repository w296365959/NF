package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.detail.entity.DailyCapitalChangeEntity;
import com.sscf.investment.utils.StringUtil;

import java.util.List;

/**
 * Created by liqf on 2015/7/21.
 */
public class CapitalFlowChangeView extends View {
    public static final float MAX_HEIGHT_RATIO = 0.7f;
    private List<DailyCapitalChangeEntity> mEntityList;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private int mDividerMarginTop;
    private TextDrawer mTextDrawer;
    private int mTextBottom;
    private Paint mTextPaint;
    private int mDateTextSize;
    private int mHistogramWidth;
    private Paint mPaint;

    private String mYiStr;
    private String mWanStr;
    private int mColorIn;
    private int mColorOut;
    private int mColorNoChange;
    private int mColorDate;
    private Typeface mTypeface;
    private int mValueTextSize;
    private int mDividerStrokeWidth;
    private int mColorDivider;

    public float getMaxAbsValue() {
        return mMaxAbsValue;
    }

    private float mMaxAbsValue;

    public CapitalFlowChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initResources();
    }

    private void initResources() {
        mTypeface = TextDrawer.getTypeface();
        Resources resources = getResources();
        mDividerMarginTop = resources.getDimensionPixelSize(R.dimen.capital_flow_change_divider_marginTop);
        mDividerStrokeWidth = resources.getDimensionPixelSize(R.dimen.tab_bar_divider_line_height);
        mDateTextSize = resources.getDimensionPixelSize(R.dimen.capital_flow_change_date_textSize);
        mValueTextSize = resources.getDimensionPixelSize(R.dimen.capital_flow_change_value_textSize);
        mHistogramWidth = resources.getDimensionPixelSize(R.dimen.capital_flow_change_histogram_width);
        mTextDrawer = new TextDrawer();
        mTextBottom = mTextDrawer.measureSingleTextBottom(mDateTextSize, TextDrawer.getTypeface());

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTypeface(mTypeface);
        mTextPaint.setStrokeWidth(mDividerStrokeWidth);
        mPaint = new Paint();

        mYiStr = resources.getString(R.string.yi);
        mWanStr = resources.getString(R.string.wan);

        mColorIn = resources.getColor(R.color.capital_flow_main_in);
        mColorOut = resources.getColor(R.color.capital_flow_main_out);

        mColorNoChange = ContextCompat.getColor(getContext(), R.color.default_text_color_60);
        mColorDate = mColorNoChange;
        mColorDivider = ContextCompat.getColor(getContext(), R.color.list_divider);
    }

    public void setFiveDayValues(List<DailyCapitalChangeEntity> entityList) {
        mEntityList = entityList;
        for (DailyCapitalChangeEntity entity : mEntityList) {
            float value = Math.abs(entity.getValue());
            if (value > mMaxAbsValue) {
                mMaxAbsValue = value;
            }
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();

        if (mEntityList == null || mEntityList.size() == 0) {
            return;
        }

        drawDivider(canvas);

        drawValues(canvas);

        drawHistogram(canvas);

        drawDates(canvas);
    }

    private void drawValues(Canvas canvas) {
        mTextPaint.setTextSize(mValueTextSize);

        float maxHeight = mDividerMarginTop;

        int count = mEntityList.size();
        int itemWidth = mDrawingWidth / count;
        int dateTextWidth = 0;
        int textHeight = mTextDrawer.measureSingleTextHeight(mValueTextSize, mTypeface);
        int textBottom = mTextDrawer.measureSingleTextBottom(mValueTextSize, mTypeface);
        int offsetX = 0;
        int itemStartX = 0;
        float y = 0;
        int valueColor;
        for (DailyCapitalChangeEntity entity : mEntityList) {
            float value = entity.getValue();
            float drawingHeight = getValueDrawingHeight(maxHeight, value);

            String valueStr = "";
            if (mMaxAbsValue >= StringUtil.YI) {
                if (Math.abs(value / StringUtil.YI) < 0.01f) {
                    value = 0; //避免出现"-0.00"
                }
                valueStr = StringUtil.getYiFloatString(value);
            } else {
                if (Math.abs(value / StringUtil.WAN) < 0.01f) {
                    value = 0; //避免出现"-0.00"
                }
                valueStr = StringUtil.getWanFloatString(value);
            }

            if (value < 0) {
                y = mDividerMarginTop - drawingHeight + textHeight;
                valueColor = mColorOut;
            } else {
                y = mDividerMarginTop - drawingHeight - textBottom;
                valueColor = mColorIn;
            }
            if (value == 0) {
                valueColor = mColorNoChange;
            }

            dateTextWidth = mTextDrawer.measureSingleTextWidth(valueStr, mValueTextSize, mTypeface);
            offsetX = (itemWidth - dateTextWidth) / 2;
            mTextPaint.setColor(valueColor);
            canvas.drawText(valueStr, itemStartX + offsetX, y, mTextPaint);
            itemStartX += itemWidth;
        }
    }

    private float getValueDrawingHeight(float maxHeight, float value) {
        return value / mMaxAbsValue * maxHeight * MAX_HEIGHT_RATIO;
    }

    private void drawDivider(Canvas canvas) {
        mTextPaint.setColor(mColorDivider);
        canvas.drawLine(0, mDividerMarginTop, mDrawingWidth, mDividerMarginTop, mTextPaint);
    }

    private void drawHistogram(Canvas canvas) {
        float maxHeight = mDividerMarginTop;
        int count = mEntityList.size();
        int itemWidth = mDrawingWidth / count;
        int itemStartX = (itemWidth - mHistogramWidth) / 2;
        for (DailyCapitalChangeEntity entity : mEntityList) {
            float value = entity.getValue();
            float drawingHeight = getValueDrawingHeight(maxHeight, value);
//            DtLog.d("liqf", "drawingHeight="+drawingHeight);
            if (value > 0) {
                mPaint.setColor(mColorIn);
                canvas.drawRect(itemStartX, mDividerMarginTop - drawingHeight, itemStartX + mHistogramWidth, mDividerMarginTop, mPaint);
            } else {
                mPaint.setColor(mColorOut);
                canvas.drawRect(itemStartX, mDividerMarginTop, itemStartX + mHistogramWidth, mDividerMarginTop - drawingHeight, mPaint);
            }
            itemStartX += itemWidth;
        }
    }

    private void drawDates(Canvas canvas) {
        mTextPaint.setColor(mColorDate);
        mTextPaint.setTextSize(mDateTextSize);

        int count = mEntityList.size();
        int itemWidth = mDrawingWidth / count;
        int dateTextWidth = 0;
        String date = "";
        int offsetX = 0;
        int itemStartX = 0;
        for (DailyCapitalChangeEntity entity : mEntityList) {
            date = entity.getDay();
            dateTextWidth = mTextDrawer.measureSingleTextWidth(date, mDateTextSize, mTypeface);
            offsetX = (itemWidth - dateTextWidth) / 2;
            canvas.drawText(entity.getDay(), itemStartX + offsetX, mDrawingHeight - mTextBottom, mTextPaint);
            itemStartX += itemWidth;
        }
    }
}
