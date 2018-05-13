package com.sscf.investment.widget.linechart;

import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Surface;

import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import BEC.TrendDesc;

/**
 * Created by yorkeehuang on 2017/3/31.
 */

public class TimeLineChartDrawingThread extends HandlerThread implements Handler.Callback {

    private static final String TAG = TimeLineChartDrawingThread.class.getSimpleName();

    private static final int MSG_REFRESH = 101;
    private static final int MSG_CLEAR = 102;

    public static final float AMOUNT_MAX_HEIGHT_RATIO = 0.99f;

    public static final float TIME_LINE_HEIGHT_RATIO = 0.97f;

    private static final int VERTICAL_REFERENCE_LINE_COUNT = 3;

    private int mDrawingWidth, mDrawingHeight;

    private Surface mDrawingSurface;

    private TimeLineChartSurfaceTextureListener mListener;

    private TimeLineChartTextureView.TimeValueChangeListener mTimeValueChangeListener;

    private Point mTimeNowPoint = new Point();

    LineChartResource mRes;

    private Handler mReceiver;

    private boolean mRequestRender;

    /**
     * 上部内容区的高度动态计算得来，以适应不同的屏幕尺寸，例如全屏和非全屏
     */
    private int mChartContentHeight;

    private float mTimeValueMax;
    private float mTimeValueMin;
    private float mTimeAmountMax;


    private TimeLineInfo mInfo;
    private float mClose;

    int mTpFlag = DengtaConst.DEFAULT_TP_FLAG;

    private List<TrendDesc> mTimeEntities;

    int mStrokeWidth;

    public TimeLineChartDrawingThread(TimeLineChartSurfaceTextureListener listener, final LineChartResource res) {
        super("DrawingThread");
        mListener = listener;
        mRes = res;
    }

    public void init(Surface surface, int width, int height) {
        mDrawingSurface = surface;
        mDrawingWidth = width;
        mDrawingHeight = height;
        mChartContentHeight = mDrawingHeight - mRes.mTradingTimeAreaHeight - mRes.mBottomRectHeight;
        DtLog.d(TAG, "DrawingThread, width = " + width + ", height = " + height);
    }

    void clearBackground() {
        Canvas c = null;
        try {
            c = mDrawingSurface.lockCanvas(null);

            //画背景色，否则会显示为全黑背景
            c.drawColor(mRes.mBgColor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    mDrawingSurface.unlockCanvasAndPost(c);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getBottomRectTop() {
        return mDrawingHeight - mRes.mBottomRectHeight;
    }

    void initHandler() {
        mReceiver = new Handler(getLooper(), this);
    }

    public void release() {
        mReceiver.removeCallbacksAndMessages(null);
        mListener = null;
        if (mDrawingSurface != null) {
            mDrawingSurface.release();
            mDrawingSurface = null;
        }
        mInfo = null;
        mTimeEntities = null;
        mRes = null;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_REFRESH:
                synchronized (this) {
                    if (!readyToDraw()) {
                        break;
                    }

                    mRequestRender = false;

                    Canvas c = null;
                    try {
                        c = mDrawingSurface.lockCanvas(/*mSurfaceRect*/null);
                        if (c == null) {
                            break;
                        }

                        //画背景色，否则会显示为全黑背景
                        c.drawColor(mRes.mBgColor);

                        //画外围边框
                        drawBorderRect(c);
                        drawTimeChart(c);

                        DtLog.d(TAG, "after draw a frame");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (c != null) {
                                mDrawingSurface.unlockCanvasAndPost(c);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
            case MSG_CLEAR:
                synchronized (this) {
                    Canvas c = null;
                    try {
                        c = mDrawingSurface.lockCanvas(null);

                        //画背景色，否则会显示为全黑背景
                        c.drawColor(mRes.mBgColor);

                        drawBorderRect(c);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (c != null) {
                                mDrawingSurface.unlockCanvasAndPost(c);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
            default:
                break;
        }
        return true;
    }

    private boolean readyToDraw() {
        return mRequestRender;
    }


    private void drawBorderRect(Canvas c) {
        //画上面分时线的矩形外框
        c.drawRect(mRes.mBorderStrokeWidth, mRes.mBorderStrokeWidth, mDrawingWidth - mRes.mBorderStrokeWidth,
                mChartContentHeight - mRes.mBorderStrokeWidth, mRes.mPaintRect);

        //画上面分时线的中间虚线
        //            c.drawLine(0, mChartContentHeight / 2, mDrawingWidth, mChartContentHeight / 2, mPaintDashLine);

        //画下面成交量的矩形边框
        c.drawRect(mRes.mBorderStrokeWidth, mDrawingHeight - mRes.mBottomRectHeight + mRes.mBorderStrokeWidth,
                mDrawingWidth - mRes.mBorderStrokeWidth, mDrawingHeight - mRes.mBorderStrokeWidth, mRes.mPaintRect);
    }

    private void drawTimeReferenceLines(Canvas canvas) {
        float gap = mDrawingWidth / (VERTICAL_REFERENCE_LINE_COUNT + 1);
        float indicatorTop = mDrawingHeight - mRes.mBottomRectHeight + mRes.mBorderStrokeWidth;
        float indicatorBottom = mDrawingHeight - mRes.mBorderStrokeWidth;
        float indicatorMiddle = (indicatorTop + indicatorBottom) / 2;
        for(int i=0; i<VERTICAL_REFERENCE_LINE_COUNT; i++) {
            float x = gap * (i + 1);
            canvas.drawLine(x, 0, x, mChartContentHeight, mRes.mPaintDashLine);
        }
        canvas.drawLine(0, indicatorMiddle, mDrawingWidth, indicatorMiddle, mRes.mPaintDashLine);
        //画上面分时线的中间虚线
        canvas.drawLine(0, mChartContentHeight / 2, mDrawingWidth, mChartContentHeight / 2, mRes.mPaintDashLine);
    }

    private void drawTimeChart(Canvas canvas) {
        DtLog.d(TAG, "canvas drawTimeChart: " + Thread.currentThread().getId());

        drawTradingTime(canvas);

        if (mTimeEntities == null || mTimeEntities.size() == 0) {
            DtLog.d(TAG, "canvas drawTimeChart mTimeEntities has no data!");
            if (mTimeValueChangeListener != null) {
                mTimeValueChangeListener.onValuePositionChanged(null);
            }
            return;
        }

        if ( mClose == 0
                || mTimeValueMin == 0
                || mTimeValueMax == 0
                || mListener.mTradingMinutes == 0) { //没有昨日收盘价，也就没有分时图的中线，此时不画
            DtLog.d(TAG, "canvas drawTimeChart wrong data: mClose = " + mClose + ", secStatus = " + mListener.mSecStatus +
                    ", mTimeValueMin = " + mTimeValueMin + ", mTimeValueMax = " + mTimeValueMax +
                    ", mTradingMinutes = " + mListener.mTradingMinutes);
            return;
        }

        DtLog.d(TAG, "canvas drawTimeChart start");
        drawTimeReferenceLines(canvas);
        drawCornerValues(canvas);

        //先复位所有Path的起始点
        int strokeWidth = mStrokeWidth;
        mRes.mTimeHorizontalGap = (float) (mDrawingWidth - strokeWidth) / mListener.mTradingMinutes;
        mRes.mShadowPath.reset();
        mRes.mShadowPath.moveTo(0, mChartContentHeight);
        mRes.mTimePath.reset();
        mRes.mTimeAveragePath.reset();

        //从第i个点(start)，向第i+1个点(stop)画线。包括分时线，分时均线和成交量
        float startX = strokeWidth / 2, startY = 0, stopX = 0, stopY = 0;
        for (int i = 0; i < mTimeEntities.size() - 1; i++) {
            //取第i个点的数值
            TrendDesc entity = mTimeEntities.get(i);
            float value = entity.getFNow();
            float average = entity.getFAverage();
            if (average == 0) {
                average = value;
            }

            //取第i+1个点的数值
            TrendDesc nextEntity = mTimeEntities.get(i+1);
            float nextValue = nextEntity.getFNow();
            float nextAverage = nextEntity.getFAverage();
            if (nextAverage == 0) {
                nextAverage = nextValue;
            }

            //获取数值对应的绘图坐标位置
            startY = getPriceDrawingHeight(value);
            stopY = getPriceDrawingHeight(nextValue);
            stopX = startX + mRes.mTimeHorizontalGap;
            //                DtLog.d("liqf", "now price startX=" + startX + ", startY=" + startY + ", stopX=" + stopX + ", stopY = " + stopY);

            //阴影path向前移动
            mRes.mShadowPath.lineTo(startX, startY);
            mRes.mShadowPath.lineTo(stopX, stopY);

            if (i == 0) { //安置分时均线Path的起始点
                mRes.mTimePath.moveTo(startX, startY);
                mRes.mTimeAveragePath.moveTo(startX, getPriceDrawingHeight(average));
            }

            if (i > 0) { //分时线和均线都向前移动
                mRes.mTimePath.lineTo(stopX, stopY);
                mRes.mTimeAveragePath.lineTo(stopX, getPriceDrawingHeight(nextAverage));
            }

            if (i + 1 == mTimeEntities.size() - 1) { //分时列表的最后一个点指示符闪烁
                mTimeNowPoint.x = (int) stopX;
                mTimeNowPoint.y = (int) stopY;
                if (mTimeValueChangeListener != null) {
                    mTimeValueChangeListener.onValuePositionChanged(new Point(mTimeNowPoint));
                }
            }

            startX += mRes.mTimeHorizontalGap; //横坐标起始点移动到下一个时间片
        }

        if (mTimeEntities.size() == 1) { //只有一个点的时候以上循环进不去，在此处画闪烁的点
            TrendDesc entity = mTimeEntities.get(0);
            float value = entity.getFNow();
            mTimeNowPoint.x = (int) 0;
            mTimeNowPoint.y = (int) getPriceDrawingHeight(value);
            if (mTimeValueChangeListener != null) {
                mTimeValueChangeListener.onValuePositionChanged(new Point(mTimeNowPoint));
            }
        }

        //close阴影Path
        mRes.mShadowPath.lineTo(stopX, mChartContentHeight);
        mRes.mShadowPath.close();

        //画分时阴影
        canvas.drawPath(mRes.mShadowPath, mRes.mPaintShadow);

        //画分时线
        mRes.mPaintLine.setColor(mRes.mTimeLineColor);
        canvas.drawPath(mRes.mTimePath, mRes.mPaintLine);
        mRes.mPaintLine.setColor(mRes.mTimeAverageLineColor);
        canvas.drawPath(mRes.mTimeAveragePath, mRes.mPaintLine);

        drawTimeLineIndicatorVolume(canvas);

        DtLog.d(TAG, "canvas drawTimeChart complete");
    }

    private void drawTradingTime(Canvas canvas) {
        float x = 0;
        float y = mChartContentHeight + mRes.mTextHeight;
        String text = "";
        int textWidth = 0;

        mRes.mKLineTextPaint.setColor(mRes.mKColorGray);
        mRes.mKLineTextPaint.setTypeface(TextDrawer.getTypeface());

        text = mListener.mOpenTimeStr;
        canvas.drawText(text, x, y, mRes.mKLineTextPaint); //画开盘时间，如"09:30"

        text = mListener.mMiddleTimeStr;
        textWidth = mRes.mTextDrawer.measureSingleTextWidth(text, mRes.mTextSize, TextDrawer.getTypeface());
        x = mDrawingWidth / 2 - textWidth / 2;
        canvas.drawText(text, x, y, mRes.mKLineTextPaint); //画午间休市时间，如"11:30/13:00"

        text = mListener.mCloseTimeStr;
        textWidth = mRes.mTextDrawer.measureSingleTextWidth(text, mRes.mTextSize, TextDrawer.getTypeface());
        x = mDrawingWidth - textWidth;
        canvas.drawText(text, x, y, mRes.mKLineTextPaint); //画收盘时间，如"15:00"
    }

    private void drawTimeLineIndicatorVolume(Canvas canvas) {
        if (mTimeEntities == null || mTimeEntities.size() == 0) {
            return;
        }

        if (mClose == 0
                || mTimeValueMin == 0 || mTimeValueMax == 0
                || mListener.mTradingMinutes == 0) { //没有昨日收盘价，也就没有分时图的中线，此时不画
            return;
        }

        //从第i个点(start)，向第i+1个点(stop)画线。包括分时线，分时均线和成交量
        int strokeWidth = mStrokeWidth;
        float startX = strokeWidth / 2, stopX = 0;
        for (int i = 0; i < mTimeEntities.size() - 1; i++) {
            //取第i个点的数值
            TrendDesc entity = mTimeEntities.get(i);
            float value = entity.getFNow();
            float amount = entity.getLNowvol();

            //画第i个点的成交量
            float prevValue = mClose;
            float delta = 0;
            if (i > 0) {
                prevValue = mTimeEntities.get(i - 1).getFNow();
            }
            delta = value - prevValue;
            if (delta > 0) {
                mRes.mPaintAmount.setColor(mRes.mKColorRed);
            } else if (delta < 0) {
                mRes.mPaintAmount.setColor(mRes.mKColorGreen);
            } else {
                mRes.mPaintAmount.setColor(mRes.mTimeAmountColor);
            }
            mRes.mPaintAmount.setStrokeWidth(strokeWidth);
            canvas.drawLine(startX, mDrawingHeight, startX, mDrawingHeight - getAmountDrawingHeight(amount), mRes.mPaintAmount);

            //获取数值对应的绘图坐标位置
            stopX = startX + mRes.mTimeHorizontalGap;

            //画最后一个点的成交量
            if (i + 1 == mTimeEntities.size() - 1) { //分时列表的最后一个点指示符闪烁
                //取第i+1个点的数值
                TrendDesc nextEntity = mTimeEntities.get(i + 1);
                float nextValue = nextEntity.getFNow();
                delta = nextValue - value;
                if (delta > 0) {
                    mRes.mPaintAmount.setColor(mRes.mKColorRed);
                } else if (delta < 0) {
                    mRes.mPaintAmount.setColor(mRes.mKColorGreen);
                } else {
                    mRes.mPaintAmount.setColor(mRes.mTimeAmountColor);
                }
                float nextAmount = nextEntity.getLNowvol();
                canvas.drawLine(stopX, mDrawingHeight, stopX, mDrawingHeight - getAmountDrawingHeight(nextAmount), mRes.mPaintAmount);
            }

            startX += mRes.mTimeHorizontalGap; //横坐标起始点移动到下一个时间片
        }

    }

    private void drawCornerValues(Canvas canvas) {
        if(mInfo == null) {
            return;
        }
        float upLeft = mTimeValueMax, downLeft = mTimeValueMin;
        float middle = mInfo.getYesterdayClose();
        float upRight = (upLeft - middle) / middle, downRight = (downLeft - middle) / middle;

        mRes.mKLineTextPaint.setTypeface(TextDrawer.getTypeface());

        int color = (upLeft - middle) > 0 ? mRes.mKColorRed : mRes.mKColorGreen;
        float x = 0;
        float y = mRes.mTextHeight;
        mRes.mKLineTextPaint.setColor(color);
        canvas.drawText(StringUtil.getFormattedFloat(upLeft, mTpFlag), x, y, mRes.mKLineTextPaint);

        x = 0;
        y = mChartContentHeight / 2 + (mRes.mTextHeight - mRes.mTextBottom) / 2;
        mRes.mKLineTextPaint.setColor(mRes.mDefaultGrayTextColor);
        canvas.drawText(StringUtil.getFormattedFloat((upLeft + downLeft) / 2, mTpFlag), x, y, mRes.mKLineTextPaint);

        color = (downLeft - middle) > 0 ? mRes.mKColorRed : mRes.mKColorGreen;
        x = 0;
        y = mChartContentHeight - mRes.mTextBottom;
        mRes.mKLineTextPaint.setColor(color);
        canvas.drawText(StringUtil.getFormattedFloat(downLeft, mTpFlag), x, y, mRes.mKLineTextPaint);

        String text = StringUtil.getPercentString(upRight);
        int textWidth = mRes.mTextDrawer.measureSingleTextWidth(text, mRes.mTextSize, TextDrawer.getTypeface());
        color = upRight > 0 ? mRes.mKColorRed : mRes.mKColorGreen;
        x = mDrawingWidth - textWidth;
        y = mRes.mTextHeight;
        mRes.mKLineTextPaint.setColor(color);
        canvas.drawText(text, x, y, mRes.mKLineTextPaint);

        text = StringUtil.getPercentString(downRight);
        textWidth = mRes.mTextDrawer.measureSingleTextWidth(text, mRes.mTextSize, TextDrawer.getTypeface());
        color = downRight > 0 ? mRes.mKColorRed : mRes.mKColorGreen;
        x = mDrawingWidth - textWidth;
        y = mChartContentHeight - mRes.mTextBottom;
        mRes.mKLineTextPaint.setColor(color);
        canvas.drawText(text, x, y, mRes.mKLineTextPaint);
    }

    private float getAmountDrawingHeight(float amount) {
        return amount / mTimeAmountMax * mRes.mBottomRectHeight * AMOUNT_MAX_HEIGHT_RATIO;
    }

    private float getPriceDrawingHeight(float value) {
        float y = 0;
        float deltaV = 0;
        float deltaY = 0;

        float close = mClose;
        if (value > close) {
            deltaV = value - close;
            deltaY = deltaV * mChartContentHeight / 2 / (mTimeValueMax - close) * TIME_LINE_HEIGHT_RATIO;
            y = mChartContentHeight / 2 - deltaY;
        } else if (value < close) {
            deltaV = close - value;
            deltaY = deltaV * mChartContentHeight / 2 / (close - mTimeValueMin) * TIME_LINE_HEIGHT_RATIO;
            y = mChartContentHeight / 2 + deltaY;
        } else {
            y = mChartContentHeight / 2;
        }


        //            DtLog.d("liqf", "now price drawing height is " + y);
        return y;
    }

    synchronized void setTimeEntities(TimeLineInfo info, List<TrendDesc> timeEntities) {
        if(info != null) {
            mInfo = info;
            mTimeEntities = timeEntities;
            updateMinMaxTimeValue(mTimeEntities);
            requestRender();
        }
    }

    synchronized void setInfo(TimeLineInfo info) {
        mInfo = info;
    }

    synchronized void updateSize(int width, int height) {
        mDrawingWidth = width;
        mDrawingHeight = height;
        mRes.mSurfaceRect.set(0, 0, mDrawingWidth, mDrawingHeight);

        requestRender();
    }

    void requestRender() {
        requestRenderDelayed(0L);
    }

    void requestRenderDelayed(final long delay) {
        synchronized (this) {
            mRequestRender = true;
        }

        if (mReceiver != null) {
            mReceiver.removeCallbacksAndMessages(null);
            mReceiver.sendEmptyMessageDelayed(MSG_REFRESH, delay);
        }
    }

    void requestClear() {
        if (mReceiver != null) {
            mReceiver.removeCallbacksAndMessages(null);
            mReceiver.sendEmptyMessage(MSG_CLEAR);
        }
    }

    private void updateMinMaxTimeValue(List<TrendDesc> entities) {
        mTimeValueMax = 0;
        mTimeValueMin = Float.MAX_VALUE;
        mTimeAmountMax = 0;

        if(mInfo == null) {
            return;
        }

        if (mInfo.isRefYesterdayClose() && mInfo.getYesterdayClose() == 0) {
            return;
        }
        if (entities == null) {
            return;
        }

        for (TrendDesc entity : entities) {
            float now = entity.getFNow();
            if(now > 0) {
                if (now > mTimeValueMax) {
                    mTimeValueMax = now;
                } else if (now < mTimeValueMin) {
                    mTimeValueMin = now;
                }
            }

            float average = entity.getFAverage();
            if(average > 0) {
                if (average > mTimeValueMax) {
                    mTimeValueMax = average;
                } else if (average < mTimeValueMin) {
                    mTimeValueMin = average;
                }
            }

            float amount = entity.getLNowvol();
            if (amount > mTimeAmountMax) {
                mTimeAmountMax = amount;
            }
        }

        if (mInfo.isRefYesterdayClose()) {
            float min = mInfo.getLow();
            float max = mInfo.getHigh();
            if (min > 0 && min < mTimeValueMin) {
                mTimeValueMin = min;
            }
            if (max > 0 && max > mTimeValueMax) {
                mTimeValueMax = max;
            }
        }

        if(mTimeValueMin == Float.MAX_VALUE || mTimeValueMax == 0) {
            mTimeValueMin = mInfo.getYesterdayClose();
            mTimeValueMax = mInfo.getYesterdayClose();
        }

        float close;
        if(mInfo.isRefYesterdayClose()) {
            close = mInfo.getYesterdayClose();
        } else {
            close = (mTimeValueMin + mTimeValueMax) / 2;
        }

        float d1 = Math.abs(mTimeValueMax - close);
        float d2 = Math.abs(mTimeValueMin - close);
        float maxDelta = d1 > d2 ? d1 : d2;
        mTimeValueMax = close + maxDelta;
        mTimeValueMin = close - maxDelta;
        mClose = close;
    }

    public static float computeMin(ArrayList<TrendDesc> entities, float yestedayClose) {
        float timeValueMax = 0;
        float timeValueMin = Float.MAX_VALUE;
        if (entities == null) {
            return 0f;
        }

        for (TrendDesc entity : entities) {
            float now = entity.getFNow();
            if(now > 0) {
                if (now > timeValueMax) {
                    timeValueMax = now;
                } else if (now < timeValueMin) {
                    timeValueMin = now;
                }
            }

            float average = entity.getFAverage();
            if(average > 0) {
                if (average > timeValueMax) {
                    timeValueMax = average;
                } else if (average < timeValueMin) {
                    timeValueMin = average;
                }
            }
        }

        if(timeValueMax == Float.MAX_VALUE || timeValueMin == 0) {
            timeValueMax = yestedayClose;
            timeValueMin = yestedayClose;
        }

        float close = (timeValueMin + timeValueMax) / 2;

        float d1 = Math.abs(timeValueMax - close);
        float d2 = Math.abs(timeValueMin - close);
        float maxDelta = d1 > d2 ? d1 : d2;
        timeValueMin = close - maxDelta;
        return timeValueMin;
    }

    public void setTimeValueChangeListener(TimeLineChartTextureView.TimeValueChangeListener listener) {
        mTimeValueChangeListener = listener;
    }
}
