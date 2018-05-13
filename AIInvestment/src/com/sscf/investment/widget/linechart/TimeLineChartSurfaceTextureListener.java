package com.sscf.investment.widget.linechart;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.support.v4.content.ContextCompat;
import android.view.Surface;
import android.view.TextureView;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.sdk.utils.DtLog;

import java.util.ArrayList;
import java.util.List;

import BEC.E_SEC_STATUS;
import BEC.TrendDesc;

/**
 * Created by yorkeehuang on 2017/3/31.
 */

public class TimeLineChartSurfaceTextureListener implements TextureView.SurfaceTextureListener {
    private static final String TAG = TimeLineChartSurfaceTextureListener.class.getSimpleName();

    public static final int TOTAL_TIME_LINE_COUNT = 4 * 60;

    private TimeLineChartDrawingThread mThread;

    private TimeLineInfo mInfo;

    private ArrayList<TrendDesc> mTimeLineEntities;

    private TimeLineChartTextureView.TimeValueChangeListener mTimeValueChangeListener;

    LineChartResource mRes;


    /**
     * 当天总的交易分钟数，用于分时图的绘制
     */
    int mTradingMinutes = TOTAL_TIME_LINE_COUNT;

    /**
     * X轴上显示的交易时间起点
     */
    String mOpenTimeStr = "";
    String mMiddleTimeStr = "";
    String mCloseTimeStr = "";

    String mDtSecCode;

    /**
     * 股票状态，是否停牌
     */
    int mSecStatus = E_SEC_STATUS.E_SS_NORMAL;

    public TimeLineChartSurfaceTextureListener(final Context context) {
        mRes = new LineChartResource(context);
    }

    public void setDtSecCode(String dtSecCode) {
        mDtSecCode = dtSecCode;
    }

    public void setTimeEntities(TimeLineInfo info, ArrayList<TrendDesc> entities) {
        mInfo = info;
        mTimeLineEntities = entities;
        if (mThread != null) {
            mThread.setTimeEntities(info, entities);
        }
    }

    public void setTradingTime(final String openTimeStr, final String middleTimeStr, final String closeTimeStr) {
        mOpenTimeStr = openTimeStr;
        mMiddleTimeStr = middleTimeStr;
        mCloseTimeStr = closeTimeStr;
    }

    public void setTimeLineInfo(TimeLineInfo info) {
        if(info != null && isInfoChanged(info)) {
            mInfo = info;
            List<TrendDesc> timeEntities = mTimeLineEntities;
            if (timeEntities != null && mThread != null) {
                mThread.setTimeEntities(mInfo, timeEntities);
            }
        }
    }

    private boolean isInfoChanged(TimeLineInfo info) {
        if(mInfo == info) {
            if(mInfo.isRefYesterdayClose() && info.isRefYesterdayClose()) {
                return mInfo.getYesterdayClose() == info.getYesterdayClose();
            }
        }
        return true;
    }

    public void setTradingMinutes(int tradingMinutes) {
        mTradingMinutes = tradingMinutes;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        DtLog.d(TAG, "onSurfaceTextureAvailable: " + toString());
        final Surface drawingSurface = new Surface(surface);
        mThread = new TimeLineChartDrawingThread(this, mRes);
        mThread.init(drawingSurface, width, height);
        mThread.setTimeValueChangeListener(mTimeValueChangeListener);
        mThread.start();
        mThread.initHandler();
        mThread.setInfo(mInfo);

        mThread.clearBackground(); //不马上画空白背景的话,在小米和华为手机上back返回时会黑一下


        List<TrendDesc> timeEntities = mTimeLineEntities;
        if (timeEntities != null) {
            mThread.setTimeEntities(mInfo, timeEntities);
        }

        requestRender(); //先画一帧，把边框画出来
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        DtLog.d(TAG, "onSurfaceTextureSizeChanged, width = " + width + ", height = " + height);
        if (mThread != null) {
            mThread.updateSize(width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        DtLog.d(TAG, "onSurfaceTextureDestroyed: " + toString());
        if (mThread != null) {
            mThread.quit();
            mThread.release();
        }
        mThread = null;
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        DtLog.d(TAG, "onSurfaceTextureUpdated: " + toString());
    }

    public void requestRender() {
        if (mThread != null) {
            mThread.requestRender();
        }
    }

    public void setTimeValueChangeListener(TimeLineChartTextureView.TimeValueChangeListener listener) {
        mTimeValueChangeListener = listener;
    }
}

final class LineChartResource {
    TextDrawer mTextDrawer;

    final int mTimeAmountColor;
    int mBgColor;
    int mTimeLineColor;
    int mTimeAverageLineColor;

    int mBorderRectColor;
    int mShadowColor;
    int mKColorGray;

    int mTouchLineColor;

    int mBorderStrokeWidth;
    int mLineStrokeWidth;
    int mBottomRectHeight;

    int mTradingTimeAreaHeight;

    int mDateTextGap;

    int mKColorRed;
    int mKColorGreen;
    int mDefaultGrayTextColor;

    int mTextSize;
    int mTextHeight;
    int mTextBottom;

    float mTimeHorizontalGap;

    String mIndicatorNotSupportPlaceHolder;

    Path mShadowPath;
    Path mTimePath;
    Path mTimeAveragePath;

    Paint mKLineTextPaint;

    Rect mSurfaceRect;
    Paint mPaintRect;
    Paint mPaintShadow;
    Paint mPaintLine;
    Paint mPaintDashLine;
    Paint mPaintKLine;
    Paint mPaintAmount;

    LineChartResource(final Context context) {
        final Resources resources = context.getResources();

        TypedArray a = context.obtainStyledAttributes(new int[] {
                R.attr.line_chart_bg, R.attr.line_chart_border_rect_color,
                R.attr.line_chart_time_line_shadow_color,
        });
        mBgColor = a.getColor(0, Color.LTGRAY);
        mBorderRectColor = a.getColor(1, Color.LTGRAY);
        mShadowColor = a.getColor(2, Color.LTGRAY);
        a.recycle();

        mKColorGray = ContextCompat.getColor(context, R.color.default_text_color_60);
        mTouchLineColor = resources.getColor(R.color.line_chart_touch_line);
        mTimeLineColor = resources.getColor(R.color.line_chart_time_line);
        mBorderStrokeWidth = resources.getDimensionPixelSize(R.dimen.line_chart_divider_strokeWidth);
        mLineStrokeWidth = resources.getDimensionPixelSize(R.dimen.line_chart_time_strokeWidth);
        mBottomRectHeight = resources.getDimensionPixelSize(R.dimen.history_time_line_chart_bottom_rect_height);
        mTradingTimeAreaHeight = resources.getDimensionPixelSize(R.dimen.line_chart_trading_time_area_height);

        mDateTextGap = resources.getDimensionPixelSize(R.dimen.line_chart_date_gap);
        mTimeAverageLineColor = resources.getColor(R.color.line_chart_time_average_line);
        mTimeAmountColor = resources.getColor(R.color.line_chart_time_amount);

        mTextSize = resources.getDimensionPixelSize(R.dimen.font_size_12);
        mKColorRed = resources.getColor(R.color.stock_red_color);
        mKColorGreen = resources.getColor(R.color.stock_green_color);
        mDefaultGrayTextColor = resources.getColor(R.color.time_line_indicator_color);
        mTextDrawer = new TextDrawer();
        mTextHeight = mTextDrawer.measureSingleTextHeight(mTextSize, TextDrawer.getTypeface());
        mTextBottom = mTextDrawer.measureSingleTextBottom(mTextSize, TextDrawer.getTypeface());

        mSurfaceRect = new Rect();
        mPaintRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintShadow.setColor(mShadowColor);
        mPaintShadow.setStyle(Paint.Style.FILL);
        mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLine.setColor(mTimeLineColor);
        mPaintLine.setStrokeWidth(mLineStrokeWidth);
        mPaintLine.setStyle(Paint.Style.STROKE);

        mPaintDashLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintDashLine.setStyle(Paint.Style.STROKE);
        mPaintDashLine.setColor(mBorderRectColor);
        PathEffect pathEffect = new DashPathEffect(new float[]{4*2, 2*2}, 0);
        mPaintDashLine.setPathEffect(pathEffect);

        mPaintKLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintKLine.setTypeface(TextDrawer.getTypeface());

        mShadowPath = new Path();
        mTimePath = new Path();
        mTimeAveragePath = new Path();

        mKLineTextPaint = new Paint();
        mKLineTextPaint.setTextSize(mTextSize);
        mKLineTextPaint.setAntiAlias(true);

        mPaintRect.setStyle(Paint.Style.STROKE);
        mPaintRect.setStrokeWidth(mBorderStrokeWidth);
        mPaintRect.setColor(mBorderRectColor);

        mIndicatorNotSupportPlaceHolder = resources.getString(R.string.indicator_not_support_placeholder);

        mPaintAmount = new Paint();
        mPaintAmount.setStyle(Paint.Style.STROKE);
        mPaintAmount.setColor(mTimeAmountColor);
    }
}
