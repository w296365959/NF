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
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqf on 2015/9/9.
 */
public class KLineInfosView extends View {
    private int mTextSizeSmall;
    private int mTextSize;
    private Paint mPaint;

    private String mTimeStr;
    private long mMinute;
    private boolean mIsMinuteK;
    private float mHigh;
    private float mOpen;
    private float mLow;
    private float mClose;
    private float mDelta;
    private float mYesterdayClose;
    private Typeface mTypeface;
    private TextDrawer mTextDrawer;
    private int mTimeAreaWidthSmall;
    private int mTimeAreaWidth;
    private int mNowPriceGapSmall;
    private int mNowPriceAreaWidthSmall;
    private int mTextAreaWidth;
    private int mTextAreaWidthSmall;
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
    private String mTitleHigh;
    private String mTitleOpen;
    private String mTitleLow;
    private String mTitleClose;
    private String mTitleDelta;
    private int mTitleHighWidth;
    private int mTitleOpenWidth;
    private int mTitleLowWidth;
    private int mTitleCloseWidth;
    private int mTitleDeltaWidth;

    private int mTpFlag = DengtaConst.DEFAULT_TP_FLAG;

    private boolean mIsFullscreen = false;

    public KLineInfosView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initResources();
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

        mTitleHigh = resources.getString(R.string.high);
        mTitleHighWidth = mTextDrawer.measureSingleTextWidth(mTitleHigh, mTextSize, mTypeface);
        mTitleOpen = resources.getString(R.string.open);
        mTitleOpenWidth = mTextDrawer.measureSingleTextWidth(mTitleOpen, mTextSize, mTypeface);
        mTitleLow = resources.getString(R.string.low);
        mTitleLowWidth = mTextDrawer.measureSingleTextWidth(mTitleLow, mTextSize, mTypeface);
        mTitleClose = resources.getString(R.string.close);
        mTitleCloseWidth = mTextDrawer.measureSingleTextWidth(mTitleClose, mTextSize, mTypeface);
        mTitleDelta = resources.getString(R.string.delta);
        mTitleDeltaWidth = mTextDrawer.measureSingleTextWidth(mTitleDelta, mTextSize, mTypeface);

        mTimeAreaWidthSmall = resources.getDimensionPixelSize(R.dimen.line_chart_time_area_width_small);
        mTimeAreaWidth = resources.getDimensionPixelSize(R.dimen.line_chart_time_area_width);
        mNowPriceGapSmall = resources.getDimensionPixelSize(R.dimen.line_chart_now_price_gap_small);
        mNowPriceAreaWidthSmall = resources.getDimensionPixelSize(R.dimen.line_chart_now_price_area_width_small);
        mTextMarginLeft = resources.getDimensionPixelSize(R.dimen.line_chart_text_marginLeft);
        mTextMarginBottomSmall = resources.getDimensionPixelSize(R.dimen.line_chart_text_marginBottom_small);
        mTitleMarginRightSmall = resources.getDimensionPixelSize(R.dimen.line_chart_title_marginRight_small);
        mTextAreaWidthSmall = resources.getDimensionPixelSize(R.dimen.line_chart_text_area_width_small);

        mTextHeight = mTextDrawer.measureSingleTextHeight(mTextSize, mTypeface);
        mTextBottom = mTextDrawer.measureSingleTextBottom(mTextSize, mTypeface);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(mTypeface);
    }

    public void setData(KLineLineTouchEvent event) {
        mTimeStr = event.mTimeStr;
        mMinute = event.mMinute;
        mIsMinuteK = event.mIsMinuteK;
        mHigh = event.mHigh;
        mOpen = event.mOpen;
        mLow = event.mLow;
        mClose = event.mClose;
        mDelta = event.mDelta;
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

        mTextAreaWidth = (mDrawingWidth - mTimeAreaWidth - mTextMarginLeft) / 5; //自适应宽度

        if (mIsFullscreen) {
            drawInfosForFullscreen(canvas);
        } else {
            drawInfos(canvas);
        }
    }

    private void drawInfosForFullscreen(Canvas canvas) {
        int x = 0;
        int y = (mDrawingHeight - mTextHeight) / 2 + mTextHeight - mTextBottom;
        float deltaByYesterday;

        mPaint.setTextSize(mTextSize);

        //所有title
        mPaint.setColor(mTitleColor);
        x += mTimeAreaWidth;
        canvas.drawText(mTitleHigh, x, y, mPaint);
        x += mTextAreaWidth;
        canvas.drawText(mTitleOpen, x, y, mPaint);
        x += mTextAreaWidth;
        canvas.drawText(mTitleLow, x, y, mPaint);
        x += mTextAreaWidth;
        canvas.drawText(mTitleClose, x, y, mPaint);
        x += mTextAreaWidth;
        canvas.drawText(mTitleDelta, x, y, mPaint);

        //时间
        x = 0;
        String timeStr = "";
        if (!mIsMinuteK) {
            timeStr = StringUtil.getFormattedDateString(mTimeStr);
        } else {
            timeStr = StringUtil.getFormattedDateString(mTimeStr) + " " + StringUtil.minuteToTime((int) mMinute);
        }
        mPaint.setColor(mGrayColor);
        canvas.drawText(timeStr, x, y, mPaint);

        int color;
        float offset;

        //高
        x += mTimeAreaWidth;
        String high = StringUtil.getFormattedFloat(mHigh, mTpFlag);
        deltaByYesterday = mHigh - mYesterdayClose;
        if (deltaByYesterday > 0) {
            color = mRedColor;
        } else if (deltaByYesterday == 0) {
            color = mGrayColor;
        } else {
            color = mGreenColor;
        }
        mPaint.setColor(color);
        offset = mTitleHighWidth + mTextMarginLeft;
        canvas.drawText(high, x + offset, y, mPaint);

        //开
        x += mTextAreaWidth;
        String open = StringUtil.getFormattedFloat(mOpen, mTpFlag);
        deltaByYesterday = mOpen - mYesterdayClose;
        if (deltaByYesterday > 0) {
            color = mRedColor;
        } else if (deltaByYesterday == 0) {
            color = mGrayColor;
        } else {
            color = mGreenColor;
        }
        mPaint.setColor(color);
        offset = mTitleOpenWidth + mTextMarginLeft;
        canvas.drawText(open, x + offset, y, mPaint);

        //低
        x += mTextAreaWidth;
        String low = StringUtil.getFormattedFloat(mLow, mTpFlag);
        deltaByYesterday = mLow - mYesterdayClose;
        if (deltaByYesterday > 0) {
            color = mRedColor;
        } else if (deltaByYesterday == 0) {
            color = mGrayColor;
        } else {
            color = mGreenColor;
        }
        mPaint.setColor(color);
        offset = mTitleLowWidth + mTextMarginLeft;
        canvas.drawText(low, x + offset, y, mPaint);

        //收
        x += mTextAreaWidth;
        String close = StringUtil.getFormattedFloat(mClose, mTpFlag);
        deltaByYesterday = mClose - mYesterdayClose;
        if (deltaByYesterday > 0) {
            color = mRedColor;
        } else if (deltaByYesterday == 0) {
            color = mGrayColor;
        } else {
            color = mGreenColor;
        }
        mPaint.setColor(color);
        offset = mTitleCloseWidth + mTextMarginLeft;
        canvas.drawText(close, x + offset, y, mPaint);

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
        String timeStr = "";
        if (!mIsMinuteK) {
            timeStr = StringUtil.getFormattedDateString(mTimeStr);
        } else {
            timeStr = StringUtil.getFormattedDateString(mTimeStr) + " " + StringUtil.minuteToTime((int) mMinute);
        }
        mPaint.setColor(mGrayColor);
        canvas.drawText(timeStr, x, y, mPaint);

        //收盘价
        x += mTimeAreaWidthSmall;
        prevX = x;
        String close = StringUtil.getFormattedFloat(mClose, mTpFlag);
        if (mDelta > 0) {
            color = mRedColor;
        } else if (mDelta == 0) {
            color = mGrayColor;
        } else {
            color = mGreenColor;
        }
        mPaint.setColor(color);
        canvas.drawText(close, x, y, mPaint);

        textWidth = mTextDrawer.measureSingleTextWidth(close, mTextSizeSmall, mTypeface);
        x += textWidth + mNowPriceGapSmall;
        String deltaPercent = StringUtil.getChangePercentString(mDelta).toString();
        canvas.drawText(deltaPercent, x, y, mPaint);

        //所有title
        mPaint.setColor(mTitleColorSmall);
        x = prevX + mNowPriceAreaWidthSmall;
        prevX = x;
        canvas.drawText(mTitleOpen, x, y, mPaint);
        x += mTextAreaWidthSmall;
        canvas.drawText(mTitleHigh, x, y, mPaint);
        x += mTextAreaWidthSmall;
        canvas.drawText(mTitleLow, x, y, mPaint);

        //开
        x = prevX;
        String open = StringUtil.getFormattedFloat(mOpen, mTpFlag);
        deltaByYesterday = mOpen - mYesterdayClose;
        if (deltaByYesterday > 0) {
            color = mRedColor;
        } else if (deltaByYesterday == 0) {
            color = mGrayColor;
        } else {
            color = mGreenColor;
        }
        mPaint.setColor(color);
        offset = mTitleOpenWidth + mTitleMarginRightSmall;
        canvas.drawText(open, x + offset, y, mPaint);

        //高
        x += mTextAreaWidthSmall;
        String high = StringUtil.getFormattedFloat(mHigh, mTpFlag);
        deltaByYesterday = mHigh - mYesterdayClose;
        if (deltaByYesterday > 0) {
            color = mRedColor;
        } else if (deltaByYesterday == 0) {
            color = mGrayColor;
        } else {
            color = mGreenColor;
        }
        mPaint.setColor(color);
        offset = mTitleHighWidth + mTitleMarginRightSmall;
        canvas.drawText(high, x + offset, y, mPaint);

        //低
        x += mTextAreaWidthSmall;
        String low = StringUtil.getFormattedFloat(mLow, mTpFlag);
        deltaByYesterday = mLow - mYesterdayClose;
        if (deltaByYesterday > 0) {
            color = mRedColor;
        } else if (deltaByYesterday == 0) {
            color = mGrayColor;
        } else {
            color = mGreenColor;
        }
        mPaint.setColor(color);
        offset = mTitleLowWidth + mTitleMarginRightSmall;
        canvas.drawText(low, x + offset, y, mPaint);
    }

    public static class KLineLineTouchEvent {
        public static final int ACTION_DOWN = 0;
        public static final int ACTION_UP = 1;
        public static final int ACTION_MOVE = 2;

        private int mAction;

        private int mIndex;
        private int mKLineStart;
        private int mKLineEnd;

        private String mTimeStr;
        private long mMinute;
        private boolean mIsMinuteK;
        private float mHigh;
        private float mOpen;
        private float mLow;
        private float mClose;
        private float mDelta;
        private float mYesterdayClose;
        private long mAmount;
        private long mVolume;
        private float mTurnover;

        public int getAction() {
            return mAction;
        }

        public void setAction(int action) {
            mAction = action;
        }

        public int getIndex() {
            return mIndex;
        }

        public void setIndex(int index) {
            mIndex = index;
        }

        public int getKLineStart() {
            return mKLineStart;
        }

        public void setKLineStart(int start) {
            mKLineStart = start;
        }

        public int getKLineEnd() {
            return mKLineEnd;
        }

        public void setKLineEnd(int end) {
            mKLineEnd = end;
        }

        public String getTimeStr() {
            return mTimeStr;
        }

        public void setTimeStr(String timeStr) {
            mTimeStr = timeStr;
        }

        public float getHigh() {
            return mHigh;
        }

        public void setHigh(float high) {
            mHigh = high;
        }

        public float getOpen() {
            return mOpen;
        }

        public void setOpen(float open) {
            mOpen = open;
        }

        public float getLow() {
            return mLow;
        }

        public void setLow(float low) {
            mLow = low;
        }

        public float getClose() {
            return mClose;
        }

        public void setClose(float close) {
            mClose = close;
        }

        public float getDelta() {
            return mDelta;
        }

        public void setDelta(float delta) {
            mDelta = delta;
        }

        public long getMinute() {
            return mMinute;
        }

        public void setMinute(long minute) {
            mMinute = minute;
        }

        public boolean isMinuteK() {
            return mIsMinuteK;
        }

        public void setMinuteK(boolean minuteK) {
            mIsMinuteK = minuteK;
        }

        public float getYesterdayClose() {
            return mYesterdayClose;
        }

        public void setYesterdayClose(float yesterdayClose) {
            mYesterdayClose = yesterdayClose;
        }

        public long getAmount() {
            return mAmount;
        }

        public void setAmount(long amount) {
            mAmount = amount;
        }

        public long getVolume() {
            return mVolume;
        }

        public void setVolume(long volume) {
            mVolume = volume;
        }

        public float getTurnover() {
            return mTurnover;
        }

        public void setTurnover(float turnover) {
            mTurnover = turnover;
        }
    }

    public static class KLineAverageInfo {
        private List<Average> mAverageList = new ArrayList<>(6);

        public void clearAverages() {
            mAverageList.clear();
        }

        public void addAverage(String text, int color) {
            mAverageList.add(new Average(text, color));
        }

        public List<Average> getAverageList() {
            return mAverageList;
        }

        public class Average {
            public String text;
            public int color;

            Average(String text, int color) {
                this.text = text;
                this.color = color;
            }
        }
    }
}
