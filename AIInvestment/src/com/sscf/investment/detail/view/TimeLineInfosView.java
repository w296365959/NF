package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;

/**
 * Created by liqf on 2015/9/9.
 * 全屏K线顶部，显示股票当前价等等随着触摸手势变化的值
 */
public class TimeLineInfosView extends View {
    private int mTextSize;
    private int mTextSizeSmall;
    private Paint mPaint;

    private int mMinute;
    private float mNow;
    private float mDelta;
    private long mVolume;
    private float mAverage;
    private float mYesterdayClose;
    private Typeface mTypeface;
    private TextDrawer mTextDrawer;
    private int mTimeAreaWidthSmall;
    private int mTimeAreaWidth;
    private int mNowPriceGapSmall;
    private int mNowPriceAreaWidthSmall;
    private int mTextAreaWidth;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private int mTextHeight;
    private int mTextBottom;
    private int mTitleColor;
    private int mTitleColorSmall;
    private int mRedColor;
    private int mGreenColor;
    private int mGrayColor;
    private int mTextMarginLeft;
    private int mTextMarginBottomSmall;
    private int mTitleMarginRightSmall;
    private String mTitlePrice;
    private String mTitleDelta;
    private String mTitleVolume;
    private String mTitleAverage;
    private int mTitlePriceWidth;
    private int mTitleDeltaWidth;
    private int mTitleVolumeWidth;
    private int mTitleAverageWidth;

    private boolean mInited = false;

    private int mTpFlag = DengtaConst.DEFAULT_TP_FLAG;

    private boolean mIsFullscreen = false;

    private String mDtSecCode;
    private boolean mIsHongKongOrUsa;

    public TimeLineInfosView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initResources();
    }

    public void setDtSecCode(String dtSecCode) {
        mDtSecCode = dtSecCode;

        mIsHongKongOrUsa = StockUtil.isHongKongOrUSA(mDtSecCode);
    }

    public void setIsFullscreen(final boolean isFullscreen) {
        mIsFullscreen = isFullscreen;
        invalidate();
    }

    private void initResources() {
        Resources resources = getResources();
        mTextSizeSmall = resources.getDimensionPixelSize(R.dimen.font_size_12);
        mTextSize = resources.getDimensionPixelSize(R.dimen.font_size_14);
        mRedColor = resources.getColor(R.color.stock_red_color);
        mGreenColor = resources.getColor(R.color.stock_green_color);

        mTitleColor = ContextCompat.getColor(getContext(), R.color.default_text_color_80);
        mTitleColorSmall = ContextCompat.getColor(getContext(), R.color.default_text_color_60);
        mGrayColor = ContextCompat.getColor(getContext(), R.color.default_text_color_100);

        mTypeface = TextDrawer.getTypeface();
        mTextDrawer = new TextDrawer();

        mTitlePrice = resources.getString(R.string.price);
        mTitlePriceWidth = mTextDrawer.measureSingleTextWidth(mTitlePrice, mTextSize, mTypeface);
        mTitleDelta = resources.getString(R.string.delta);
        mTitleDeltaWidth = mTextDrawer.measureSingleTextWidth(mTitleDelta, mTextSize, mTypeface);
        mTitleVolume = resources.getString(R.string.volume);
        mTitleVolumeWidth = mTextDrawer.measureSingleTextWidth(mTitleVolume, mTextSize, mTypeface);
        mTitleAverage = resources.getString(R.string.average);
        mTitleAverageWidth = mTextDrawer.measureSingleTextWidth(mTitleAverage, mTextSize, mTypeface);

        mTimeAreaWidth = resources.getDimensionPixelSize(R.dimen.line_chart_time_area_width);
        mTimeAreaWidthSmall = resources.getDimensionPixelSize(R.dimen.line_chart_time_area_width_small_for_time_line);
        mNowPriceGapSmall = resources.getDimensionPixelSize(R.dimen.line_chart_now_price_gap_small);
        mNowPriceAreaWidthSmall = resources.getDimensionPixelSize(R.dimen.line_chart_now_price_area_width_small);
        mTextAreaWidth = resources.getDimensionPixelSize(R.dimen.line_chart_text_area_width);
        mTextMarginLeft = resources.getDimensionPixelSize(R.dimen.line_chart_text_marginLeft);
        mTextMarginBottomSmall = resources.getDimensionPixelSize(R.dimen.line_chart_text_marginBottom_small);
        mTitleMarginRightSmall = resources.getDimensionPixelSize(R.dimen.line_chart_title_marginRight_small);

        mTextHeight = mTextDrawer.measureSingleTextHeight(mTextSize, mTypeface);
        mTextBottom = mTextDrawer.measureSingleTextBottom(mTextSize, mTypeface);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(mTextSize);
        mPaint.setTypeface(mTypeface);
    }

    public void setData(TimeLineTouchEvent event) {
        mMinute = event.mMinute;
        mNow = event.mNow;
        mDelta = event.mDelta;
        mVolume = event.mVolume;
        mAverage = event.mAverage;
        mYesterdayClose = event.mYesterdayClose;

        postInvalidate();
    }

    public void setTpFlag(int tpFlag) {
        if (mTpFlag != tpFlag) {
            mTpFlag = tpFlag;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();

        if (mIsFullscreen) {
            drawInfosForFullscreen(canvas);
        } else {
            drawInfos(canvas);
        }
    }

    private void drawInfosForFullscreen(Canvas canvas) {
        int x = 0;
        int y = (mDrawingHeight - mTextHeight) / 2 + mTextHeight - mTextBottom;

        mPaint.setColor(mTitleColor);
        x += mTimeAreaWidth;
        canvas.drawText(mTitlePrice, x, y, mPaint);
        x += mTextAreaWidth;
        canvas.drawText(mTitleDelta, x, y, mPaint);
        x += mTextAreaWidth;
        canvas.drawText(mTitleVolume, x, y, mPaint);
        if (mAverage != 0) {
            x += mTextAreaWidth;
            canvas.drawText(mTitleAverage, x, y, mPaint);
        }

        x = 0;
        String timeStr = StringUtil.minuteToTime(mMinute);
        mPaint.setColor(mGrayColor);
        canvas.drawText(timeStr, x, y, mPaint);

        int color;
        float offset;
        float deltaByYesterday;

        //价格
        x += mTimeAreaWidth;
        String price = StringUtil.getFormattedFloat(mNow, mTpFlag);
        if (mDelta > 0) {
            color = mRedColor;
        } else if (mDelta == 0) {
            color = mGrayColor;
        } else {
            color = mGreenColor;
        }
        mPaint.setColor(color);
        offset = mTitlePriceWidth + mTextMarginLeft;
        canvas.drawText(price, x + offset, y, mPaint);

        //涨幅
        x += mTextAreaWidth;
        String deltaPercent = StringUtil.getChangePercentString(mDelta).toString();
        if (mDelta > 0) {
            color = mRedColor;
        } else if (mDelta == 0) {
            color = mGrayColor;
        } else {
            color = mGreenColor;
        }
        mPaint.setColor(color);
        offset = mTitleDeltaWidth + mTextMarginLeft;
        canvas.drawText(deltaPercent, x + offset, y, mPaint);

        //成交
        x += mTextAreaWidth;
        String volume = mIsHongKongOrUsa ? StringUtil.getVolumeString(mVolume, true, true) : StringUtil.getVolumeString(mVolume, false, true);
        color = mGrayColor;
        mPaint.setColor(color);
        offset = mTitleVolumeWidth + mTextMarginLeft;
        canvas.drawText(volume, x + offset, y, mPaint);

        //均价
        if (mAverage != 0) {
            x += mTextAreaWidth;
            String average = StringUtil.getFormattedFloat(mAverage, mTpFlag);
            deltaByYesterday = mAverage - mYesterdayClose;
            if (deltaByYesterday > 0) {
                color = mRedColor;
            } else if (deltaByYesterday == 0) {
                color = mGrayColor;
            } else {
                color = mGreenColor;
            }
            mPaint.setColor(color);
            offset = mTitleAverageWidth + mTextMarginLeft;
            canvas.drawText(average, x + offset, y, mPaint);
        }
    }

    private void drawInfos(Canvas canvas) {
        int x = mTextMarginLeft, prevX = 0;
        int y = mDrawingHeight - mTextMarginBottomSmall;

        int color;
        float offset;
        int textWidth;
        float deltaByYesterday;

        mPaint.setTextSize(mTextSizeSmall);

        //时间
        String timeStr = StringUtil.minuteToTime(mMinute);
        mPaint.setColor(mGrayColor);
        canvas.drawText(timeStr, x, y, mPaint);

        //现价
        x += mTimeAreaWidthSmall;
        prevX = x;
        String price = StringUtil.getFormattedFloat(mNow, mTpFlag);
        if (mDelta > 0) {
            color = mRedColor;
        } else if (mDelta == 0) {
            color = mGrayColor;
        } else {
            color = mGreenColor;
        }
        mPaint.setColor(color);
        canvas.drawText(price, x, y, mPaint);

        //涨跌幅
        textWidth = mTextDrawer.measureSingleTextWidth(price, mTextSizeSmall, mTypeface);
        String deltaPercent = StringUtil.getChangePercentString(mDelta).toString();
        x += textWidth + mNowPriceGapSmall;
        canvas.drawText(deltaPercent, x, y, mPaint);

        //均价
        x = prevX + mNowPriceAreaWidthSmall;
        prevX = x;
        mPaint.setColor(mTitleColorSmall);
        canvas.drawText(mTitleAverage, x, y, mPaint);

        x += mTitleAverageWidth + mTitleMarginRightSmall;
        String average = StringUtil.getFormattedFloat(mAverage, mTpFlag);
        deltaByYesterday = mAverage - mYesterdayClose;
        if (deltaByYesterday > 0) {
            color = mRedColor;
        } else if (deltaByYesterday == 0) {
            color = mGrayColor;
        } else {
            color = mGreenColor;
        }
        mPaint.setColor(color);
        canvas.drawText(average, x, y, mPaint);

        //成交量
        x = prevX + mTextAreaWidth;
        mPaint.setColor(mTitleColorSmall);
        canvas.drawText(mTitleVolume, x, y, mPaint);

        String volume = mIsHongKongOrUsa ? StringUtil.getVolumeString(mVolume, true, true) : StringUtil.getVolumeString(mVolume, false, true);
        color = mGrayColor;
        mPaint.setColor(color);
        offset = mTitleVolumeWidth + mTitleMarginRightSmall;
        canvas.drawText(volume, x + offset, y, mPaint);
    }

    public static class TimeLineTouchEvent {
        public static final int ACTION_DOWN = 0;
        public static final int ACTION_UP = 1;
        public static final int ACTION_MOVE = 2;

        private int mAction;

        private int mMinute;
        private float mNow;
        private float mDelta;
        private long mVolume;
        private float mAverage;

        private int mIndex;
        private int mTimeLineStart;
        private int mTimeLineEnd;

        public float getYesterdayClose() {
            return mYesterdayClose;
        }

        public void setYesterdayClose(float yesterdayClose) {
            mYesterdayClose = yesterdayClose;
        }

        public float mYesterdayClose;

        public int getAction() {
            return mAction;
        }

        public void setAction(int action) {
            mAction = action;
        }

        public float getNow() {
            return mNow;
        }

        public void setNow(float now) {
            mNow = now;
        }

        public float getDelta() {
            return mDelta;
        }

        public void setDelta(float delta) {
            mDelta = delta;
        }

        public long getVolume() {
            return mVolume;
        }

        public void setVolume(long volume) {
            mVolume = volume;
        }

        public float getAverage() {
            return mAverage;
        }

        public void setAverage(float average) {
            mAverage = average;
        }

        public int getMinute() {
            return mMinute;
        }

        public void setMinute(int minute) {
            mMinute = minute;
        }

        public void setTimeLineStart(int start) {
            mTimeLineStart = start;
        }

        public int getTimeLineStart() {
            return mTimeLineStart;
        }

        public void setTimeLineEnd(int end) {
            mTimeLineEnd = end;
        }

        public int getTimeLineEnd() {
            return mTimeLineEnd;
        }

        public void setIndex(int index) {
            mIndex = index;
        }

        public int getIndex() {
            return mIndex;
        }
    }
}
