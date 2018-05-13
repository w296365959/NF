package com.sscf.investment.detail.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import android.view.animation.LinearInterpolator;

import com.sscf.investment.BuildConfig;
import com.sscf.investment.R;
import com.sscf.investment.detail.LineChartActivity;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.detail.entity.BBI;
import com.sscf.investment.detail.entity.BIAS;
import com.sscf.investment.detail.entity.BOLL;
import com.sscf.investment.detail.entity.Break;
import com.sscf.investment.detail.entity.BreakPoint;
import com.sscf.investment.detail.entity.BullBear;
import com.sscf.investment.detail.entity.CCI;
import com.sscf.investment.detail.entity.DMA;
import com.sscf.investment.detail.entity.DMI;
import com.sscf.investment.detail.entity.ENE;
import com.sscf.investment.detail.entity.EXPMA;
import com.sscf.investment.detail.entity.Gap;
import com.sscf.investment.detail.entity.KDJ;
import com.sscf.investment.detail.entity.KZJBY;
import com.sscf.investment.detail.entity.MACD;
import com.sscf.investment.detail.entity.MagicNine;
import com.sscf.investment.detail.entity.OBV;
import com.sscf.investment.detail.entity.RSI;
import com.sscf.investment.detail.entity.VR;
import com.sscf.investment.detail.entity.WR;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.MathUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.manager.KLineSettingManager;
import com.sscf.investment.setting.model.KLineSettingConfigure;
import com.sscf.investment.setting.model.KlineSettingConst;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import BEC.CapitalDDZDesc;
import BEC.KLineDesc;
import BEC.RtMinDesc;
import BEC.SecBsInfo;
import BEC.SecQuote;
import BEC.TrendDesc;

/**
 * 作为内部类，只负责绘制图像，尽量不在此类中存储分时或者K线的原始数据。
 * 其中定义的变量常量都是绘制过程相关的图形变量
 */
public class LineChartDrawingThread extends HandlerThread implements Handler.Callback {
    private final String TAG = LineChartDrawingThread.class.getSimpleName();

    private static final int MSG_REFRESH = 101;
    private static final int MSG_CLEAR = 102;
    public static final int DEFAULT_KLINE_COUNT = 60;
    private int MA_1 = SettingConst.DEFAULT_SETTING_MA_1;
    private int MA_2 = SettingConst.DEFAULT_SETTING_MA_2;
    private int MA_3 = SettingConst.DEFAULT_SETTING_MA_3;
    private int MA_4 = SettingConst.DEFAULT_SETTING_MA_4;
    private int MA_5 = SettingConst.DEFAULT_SETTING_MA_5;
    private int MA_6 = SettingConst.DEFAULT_SETTING_MA_6;
    private int AMOUNT_MA_1 = SettingConst.DEFAULT_SETTING_AMOUNT_1;
    private int AMOUNT_MA_2 = SettingConst.DEFAULT_SETTING_AMOUNT_2;
    private int AMOUNT_MA_3 = SettingConst.DEFAULT_SETTING_AMOUNT_3;

    private int K_MACD_1 = SettingConst.DEFAULT_SETTING_MACD_1;
    private int K_MACD_2 = SettingConst.DEFAULT_SETTING_MACD_2;
    private int K_MACD_3 = SettingConst.DEFAULT_SETTING_MACD_3;

    private int K_KDJ_1 = SettingConst.DEFAULT_SETTING_KDJ_1;// 9日rsv
    private int K_KDJ_2 = SettingConst.DEFAULT_SETTING_KDJ_2;
    private int K_KDJ_3 = SettingConst.DEFAULT_SETTING_KDJ_3;

    private int K_RSI_1 = SettingConst.DEFAULT_SETTING_RSI_1;
    private int K_RSI_2 = SettingConst.DEFAULT_SETTING_RSI_2;
    private int K_RSI_3 = SettingConst.DEFAULT_SETTING_RSI_3;

    private int K_BBI_1 = SettingConst.DEFAULT_SETTING_BBI_1;
    private int K_BBI_2 = SettingConst.DEFAULT_SETTING_BBI_2;
    private int K_BBI_3 = SettingConst.DEFAULT_SETTING_BBI_3;
    private int K_BBI_4 = SettingConst.DEFAULT_SETTING_BBI_4;

    private int K_DMI_1 = SettingConst.DEFAULT_SETTING_DMI_1;
    private int K_DMI_2 = SettingConst.DEFAULT_SETTING_DMI_2;

    private int K_CCI_1 = SettingConst.DEFAULT_SETTING_CCI_1;
    private int K_CCI_2 = SettingConst.DEFAULT_SETTING_CCI_2;

    private int K_ENE_1 = SettingConst.DEFAULT_SETTING_ENE_1;
    private int K_ENE_2 = SettingConst.DEFAULT_SETTING_ENE_2;
    private int K_ENE_3 = SettingConst.DEFAULT_SETTING_ENE_3;

    private int K_DMA_1 = SettingConst.DEFAULT_SETTING_DMA_1;
    private int K_DMA_2 = SettingConst.DEFAULT_SETTING_DMA_2;
    private int K_DMA_3 = SettingConst.DEFAULT_SETTING_DMA_3;

    private int K_EXPMA_1 = SettingConst.DEFAULT_SETTING_EXPMA_1;
    private int K_EXPMA_2 = SettingConst.DEFAULT_SETTING_EXPMA_2;
    private int K_EXPMA_3 = SettingConst.DEFAULT_SETTING_EXPMA_3;
    private int K_EXPMA_4 = SettingConst.DEFAULT_SETTING_EXPMA_4;

    private int K_VR = SettingConst.DEFAULT_SETTING_VR;

    private int K_BIAS_1 = SettingConst.DEFAULT_SETTING_BIAS_1;
    private int K_BIAS_2 = SettingConst.DEFAULT_SETTING_BIAS_2;
    private int K_BIAS_3 = SettingConst.DEFAULT_SETTING_BIAS_3;

    private int K_WR = SettingConst.DEFAULT_SETTING_WR;

    private int K_MAGIC_NINE = SettingConst.DEFAULT_MAGIC_NICE;

    private int K_BREAK_1 = SettingConst.DEFAULT_SETTING_BREAK_1;
    private int K_BREAK_2 = SettingConst.DEFAULT_SETTING_BREAK_2;
    private int K_BREAK_3 = SettingConst.DEFAULT_SETTING_BREAK_3;
    private int K_BREAK_4 = SettingConst.DEFAULT_SETTING_BREAK_4;

    private int K_BULL_BEAR_PERIOD = 60;
    private int K_STANDARd_MACD_1 = 12;
    private int K_STANDARd_MACD_2 = 26;
    private int K_STANDARd_MACD_3 = 9;

    private int K_DK_POINT_1 = 4;
    private int K_DK_POINT_2 = 20;
    private int K_DK_POINT_3 = 10;
    private int K_DK_POINT_4 = 35;

    private int DEFAULT_BOLL_MA_COUNT = SettingConst.DEFAULT_SETTING_BOLL_1;
    private int DEFAULT_BOLL_FACTOR = SettingConst.DEFAULT_SETTING_BOLL_2;

    public static final float PILLAR_WIDTH_RATIO = 7f / 11f;
    public static final float AMOUNT_MAX_HEIGHT_RATIO = 0.99f;
    public static final float KLINE_MAX_HEIGHT_RATIO = 0.92f;
    public static final float MAX_ZOOM_RATIO = 2.0f;
    public static final float MIN_ZOOM_RATIO = 0.3f;

    private static final int VERTICAL_REFERENCE_LINE_COUNT = 3;
    private static final int VERTICAL_REFERENCE_LINE_COUNT_FIVE_DAY = 4;

    public static final float TIME_LINE_HEIGHT_RATIO = 0.97f;

    private int mDrawingWidth, mDrawingHeight;

    private Surface mDrawingSurface;

    private Handler mReceiver;

    private boolean mRequestRender;

    private float mTouchX = LineChartTextureView.INVALID_TOUCH_X;

    /**
     * 上部内容区的高度动态计算得来，以适应不同的屏幕尺寸，例如全屏和非全屏
     */
    private int mChartContentHeight;

    private float mTimeAmountMax;
    private float mTimeCapitalDDZMax;
    private float mTimeCapitalDDZMin;
    private float mTimeFiveDayCapitalDDZMax;
    private float mTimeFiveDayCapitalDDZMin;
    private float[] mTimeVolumeAverage;
    private float mTimeMaxVolumeAverage;
    private float mTimeMinVolumeAverage;
    private Point mTimeNowPoint = new Point();

    private TimeLineDataCache mTimeLineDataCache = new TimeLineDataCache();

    //日K线
    private List<KLineDesc> mKLineEntities;
    private HashMap<String, SecBsInfo> mBSInfos;
    private int mKLineStart;
    private int mKLineEnd;
    private int mKlineCount;

    private float[] mKLineAverage1;
    private float[] mKLineAverage2;
    private float[] mKLineAverage3;
    private float[] mKLineAverage4;
    private float[] mKLineAverage5;
    private float[] mKLineAverage6;
    private float[] mCloses;
    private float[] mHighs;
    private float[] mLows;
    private double[] ema12s;
    private double[] ema26s;
    private double[] ma60s;
    private ArrayList<MACD> mStandardMacdEntitys;//标准的macd指数
    private MInteger outBegIdx, outNBElement;

    private float mKLineMax = 0;
    private float mKLineMin = Float.MAX_VALUE;
    private float mKLineValueMax = 0;
    private float mKLineValueMin = Float.MAX_VALUE;
    private int mKLineMaxIndex;
    private int mKLineMinIndex;
    private float mKLineVolumeMax;
    float mZoomRatio = 1.0f;
    float mLastZoomRatio = 1.0f;
    private float mTranslation = 0;
    private float mKLineMaxCapitalDDZ;
    private float mKLineMinCapitalDDZ;

    /**
     * 用于全屏K线，表示左移时是否已经拉到了从上市之日起全部的K线数据
     */
    private boolean mHasOldestKLineData = false;

    private List<KZJBY> zjbylist = null;

    private long[] mKVolumeAverage1, mKVolumeAverage2, mKVolumeAverage3;
    private ArrayList<MACD> mTimeLineMacdEntitys;
    private ArrayList<MACD> mMacdEntitys;
    private ArrayList<KDJ> mKdjEntitys;
    private ArrayList<RSI> mRsiList;
    private ArrayList<BOLL> mBollEntities;
    private ArrayList<BBI> mBBIEntities;
    private ArrayList<DMI> mDMIEntities;
    private ArrayList<CCI> mCCIEntities;
    private ArrayList<ENE> mENEEntities;
    private ArrayList<DMA> mDMAEntities;
    private ArrayList<EXPMA> mEXPMAEntities;
    private ArrayList<VR> mVREntities;
    private ArrayList<OBV> mOBVEntities;
    private ArrayList<BIAS> mBIASEntities;
    private ArrayList<WR> mWREntities;
    private ArrayList<MagicNine> mMagicNineEntities;
    private ArrayList<Break> mBreakEntities;
    private ArrayList<BreakPoint> mBreakPointEntities;
    private ArrayList<BullBear> mBullBearEntities;
    private double[] emaBuy, emaSell;

    private float[] mKCapitalFlowAverageFiveDays;
    private float[] mKCapitalFlowAverageTenDays;

    private ValueAnimator mBSAnimator;
    private float mBSAnimOffset;
    private AtomicInteger mBSAnimatorState = new AtomicInteger(BS_ANIMATOR_STATE_NOT_INIT);
    private static final int BS_ANIMATOR_STATE_NOT_INIT = 0;
    private static final int BS_ANIMATOR_STATE_READY = 1;
    private static final int BS_ANIMATOR_STATE_ANIMATING = 2;
    private static final int BS_ANIMATOR_STATE_END = 3;

    int mStrokeWidth;

    private int mIndicatorNum;
    private int[] mIndicatorsRectBottom;
    private int[] mIndicatorsTextTop;
    private RectF[] mShareRect;

    private Core core;

    public float getTouchX() {
        return mTouchX;
    }

    public synchronized void setTouchX(float touchX) {
        float prevTouchX = mTouchX;
        mTouchX = touchX;

        if (touchX == LineChartTextureView.INVALID_TOUCH_X) {
            rememberLastZoomRatio();
        }
        drawValueInfos(prevTouchX);
    }

    public synchronized void rememberLastZoomRatio() {
        mLastZoomRatio = mZoomRatio; //松开手指时记住此时的zoom比例
    }

    private void drawValueInfos(float prevTouchX) {
        if (mKLineEventListener == null) {
            return;
        }

        List<TrendDesc> timeEntities = mTimeLineDataCache.getTimeLineEntities(0);

        if (mListener.mLineType == LineChartTextureView.TYPE_TIME || mListener.mLineType == LineChartTextureView.TYPE_FIVE_DAY) {
            if (timeEntities == null) { //停牌的股票不显示分时图的触摸线
                return;
            }

            TimeLineInfosView.TimeLineTouchEvent event = new TimeLineInfosView.TimeLineTouchEvent();
            if (prevTouchX == LineChartTextureView.INVALID_TOUCH_X && mTouchX != LineChartTextureView.INVALID_TOUCH_X) {
                event.setAction(TimeLineInfosView.TimeLineTouchEvent.ACTION_DOWN);
            } else if (prevTouchX != LineChartTextureView.INVALID_TOUCH_X && mTouchX != LineChartTextureView.INVALID_TOUCH_X) {
                event.setAction(TimeLineInfosView.TimeLineTouchEvent.ACTION_MOVE);
            } else {
                event.setAction(TimeLineInfosView.TimeLineTouchEvent.ACTION_UP);
            }

            int index = getTimeTouchEntityIndex(mTouchX);
            if(index >= 0 && index < timeEntities.size()) {
                TrendDesc entity = timeEntities.get(index);
                event.setMinute(entity.getIMinute());
                event.setNow(entity.getFNow());
                event.setDelta((entity.getFNow() - mListener.getYesterdayClose()) / mListener.getYesterdayClose());
                event.setVolume(entity.getLNowvol());
                event.setAverage(getTouchAverageValue(entity));
                event.setYesterdayClose(mListener.getYesterdayClose());
                event.setIndex(index);
                event.setTimeLineStart(0);
                event.setTimeLineEnd(timeEntities.size() - 1);
            }

            mKLineEventListener.onTimeLineTouchChanged(event);
        } else { //K线事件
            if (mKLineEntities == null) {
                return;
            }

            KLineInfosView.KLineLineTouchEvent event = new KLineInfosView.KLineLineTouchEvent();
            if (prevTouchX == LineChartTextureView.INVALID_TOUCH_X && mTouchX != LineChartTextureView.INVALID_TOUCH_X) {
                event.setAction(KLineInfosView.KLineLineTouchEvent.ACTION_DOWN);
            } else if (prevTouchX != LineChartTextureView.INVALID_TOUCH_X && mTouchX != LineChartTextureView.INVALID_TOUCH_X) {
                event.setAction(KLineInfosView.KLineLineTouchEvent.ACTION_MOVE);
            } else {
                event.setAction(KLineInfosView.KLineLineTouchEvent.ACTION_UP);
            }

            KLineDesc entity = getKLineTouchEntity(mTouchX, event);
            if (entity != null) {
                event.setTimeStr(String.valueOf(entity.getLYmd()));
                event.setMinute(entity.getLMinute());
                event.setMinuteK(mListener.isMinuteKLineType());
                event.setHigh(entity.getFHigh());
                event.setLow(entity.getFLow());
                event.setOpen(entity.getFOpen());
                event.setClose(entity.getFClose());
            }
            mKLineEventListener.onKLineTouchChanged(event);
        }
    }

    /**
     * 如果当前类型是指数，就把触摸点对应的average设为0，不画出对应的均值
     * @param entity 触摸点对应的entity
     * @return 触摸点对应的均值，为0表示不要画出均值文字
     */
    private float getTouchAverageValue(TrendDesc entity) {
        float average = 0;
        if (mListener.mQuote != null) {
            average = entity.getFAverage();
        }
        return average;
    }

    private int getTimeTouchEntityIndex(float touchX) {
        int index = -1;
        int strokeWidth = mStrokeWidth;
        float startX = strokeWidth / 2;
        List<TrendDesc> timeEntities = mTimeLineDataCache.getTimeLineEntities(0);
        int size = timeEntities == null ? 0 : timeEntities.size();

        if (size == 0) {
            return -1;
        }

        if (touchX < 0) {
            index = 0;
        } else if (touchX > mRes.mTimeHorizontalGap * size) {
            index = size - 1;
        } else {
            for (int i = 0; i < size; i++) {
                if (touchX != LineChartTextureView.INVALID_TOUCH_X) {
                    if (touchX >= startX && touchX < startX + mRes.mTimeHorizontalGap) {
                        index = i;
                        break;
                    }
                }

                startX += mRes.mTimeHorizontalGap; //横坐标起始点移动到下一个时间片
            }
        }

        return index;
    }

    private KLineDesc getKLineTouchEntity(float touchX, KLineInfosView.KLineLineTouchEvent event) {
        int entityCount = mKLineEnd - mKLineStart;
        float itemWidth = getKLineItemWidth();
        float startX = mRes.mKLineStrokeWidth / 2;

        int index = -1;
        if (touchX < 0) {
            index = 0;
        } else if (touchX > itemWidth * entityCount) {
            index = mKLineEnd - mKLineStart - 1;
        } else {
            for (int i = 0; i < entityCount; i++) {
                //画触摸指示线
                if (touchX != LineChartTextureView.INVALID_TOUCH_X) {
                    if (touchX >= startX - mRes.mKLineStrokeWidth / 2 && touchX <= startX + itemWidth) {
                        index = i;
                        break;
                    }
                }

                //移动到下一个格子起点
                startX += itemWidth;
            }
        }

        KLineDesc entity = null;
        if (index != -1 && index + mKLineStart >= 0 && index + mKLineStart < mKLineEntities.size()) {
            entity = mKLineEntities.get(index + mKLineStart);
            float close = entity.getFClose();

            event.setIndex(index + mKLineStart);
            event.setKLineStart(mKLineStart);
            event.setKLineEnd(mKLineEnd - 1);

            float delta;
            float yesterdayClose;
            int prevIndex = index + mKLineStart - 1;
            if (prevIndex < 0) {
                yesterdayClose = entity.getFOpen();
                delta = (close - yesterdayClose) / yesterdayClose;
            } else {
                KLineDesc prevEntity = mKLineEntities.get(prevIndex);
                yesterdayClose = prevEntity.getFClose();
                delta = (close - yesterdayClose) / yesterdayClose;
            }
            event.setDelta(delta);
            event.setYesterdayClose(yesterdayClose);
            event.setAmount(entity.getLAmout());
            event.setVolume(entity.getLVolume());
        }
        return entity;
    }

    private KLineSettingManager mKlineSettingManager;
    private KLineSettingConfigure mKlineSettingConfigure;
    private boolean isSharedForBreak;

    public void setSharedForBreak(boolean sharedForBreak) {
        isSharedForBreak = sharedForBreak;
    }

    private LineChartSurfaceTextureListener mListener;

    LineChartTextureView.TimeValueChangeListener mTimeValueChangeListener;
    LineChartTextureView.KLineEventListener mKLineEventListener;
    LineChartResource mRes;//K线资源

    public LineChartDrawingThread(LineChartSurfaceTextureListener listener, final LineChartResource res) {
        super("DrawingThread");
        mListener = listener;
        mRes = res;
        core = new Core();
        outNBElement = new MInteger();
        outBegIdx = new MInteger();
    }

    public void init(Surface surface, int width, int height, boolean isFullScreen) {
        mDrawingSurface = surface;
        mDrawingWidth = width;
        mDrawingHeight = height;
        mBSAnimOffset = mRes.mBSMinGap;

        mIsFullScreen = isFullScreen;
        mKlineSettingManager = DengtaApplication.getApplication().getKLineSettingManager();
        mKlineSettingConfigure = mKlineSettingManager.getKlineSettingConfigure();
        refreshIndicatorNum();
    }

    public void refreshIndicatorNum() {
        Resources resources = DengtaApplication.getApplication().getResources();
        if (!mIsFullScreen && !mListener.isKLineType(mListener.mLineType)) {//不是全屏绘制两个技术指标
            mRes.mIndicatorRectHeight = resources.getDimensionPixelSize(R.dimen.line_chart_bottom_rect_height_2);
            mIndicatorNum = 2;
        } else if (mIsFullScreen){//全屏绘制一次技术指标
            mRes.mIndicatorRectHeight = resources.getDimensionPixelSize(R.dimen.line_chart_bottom_rect_height_fullscreen);
            mIndicatorNum = 1;
        } else {
            mIndicatorNum = mKlineSettingConfigure.indicatorNum;
            mRes.mIndicatorRectHeight = getIndicatorHeightByIndicatorNum(resources, mIndicatorNum);
        }
        mStrokeWidth = mIsFullScreen ? 4 : 2;
        mRes.mPaintAmount.setStrokeWidth(mStrokeWidth);

        updateHeight();
    }

    private int getIndicatorHeightByIndicatorNum(Resources res, int indicatorNum) {
        if (indicatorNum == 1) {
            return res.getDimensionPixelSize(R.dimen.line_chart_bottom_rect_height_1);
        }else if (indicatorNum == 2) {
            return res.getDimensionPixelSize(R.dimen.line_chart_bottom_rect_height_2);
        }else if (indicatorNum == 3) {
            return res.getDimensionPixelSize(R.dimen.line_chart_bottom_rect_height_3);
        }else if (indicatorNum == 4) {
            return res.getDimensionPixelSize(R.dimen.line_chart_bottom_rect_height_4);
        }
        return res.getDimensionPixelSize(R.dimen.line_chart_bottom_rect_height_2);
    }

    private void updateHeight() {
        mChartContentHeight = mDrawingHeight - mRes.mTradingTimeAreaHeight -
                mRes.mIndicatorRectHeight * mIndicatorNum - mRes.mIndicatorRectMarginTop * (mIndicatorNum - 1);
        if (null != mKLineEventListener) {
            mKLineEventListener.onKLineHeightUpdate(mChartContentHeight);
        }
        mIndicatorsTextTop = new int[mIndicatorNum];
        for (int i = 0; i < mIndicatorNum; i++) {
            mIndicatorsTextTop[i] = mChartContentHeight + mRes.mTradingTimeAreaHeight + mRes.mIndicatorRectHeight * i + mRes.mIndicatorRectMarginTop * (mIndicatorNum - 1);
        }
        mIndicatorsRectBottom = new int[mIndicatorNum];
        for (int i = 0; i < mIndicatorNum; i++) {
            mIndicatorsRectBottom[i] = mDrawingHeight - (mRes.mIndicatorRectMarginTop + mRes.mIndicatorRectHeight) * (mIndicatorNum - 1 - i);
        }
    }

    public int getIndicatorHeight(){
        return mRes.mIndicatorRectHeight;
    }

    public void setTimeValueChangeListener(LineChartTextureView.TimeValueChangeListener listener) {
        mTimeValueChangeListener = listener;
    }

    public void setKLineEventListener(LineChartTextureView.KLineEventListener listener) {
        mKLineEventListener = listener;
    }

    public int getBottomRectTop() {
        return mDrawingHeight - mRes.mIndicatorRectHeight;
    }

    static final int AREA_UNKNOWN = 0;
    static final int AREA_CHART_CONTENT = 1;
    static final int AREA_1_INDICATOR_TEXT = 2;
    static final int AREA_1_INDICATOR = 3;
    static final int AREA_2_INDICATOR_TEXT = 5;
    static final int AREA_2_INDICATOR = 6;
    static final int AREA_3_SHARE = 7;
    static final int AREA_3_INDICATOR_TEXT = 8;
    static final int AREA_3_INDICATOR = 9;
    static final int AREA_4_INDICATOR_TEXT = 10;
    static final int AREA_4_INDICATOR = 11;

    final int clickArea(float x, float y) {
        int area = AREA_UNKNOWN;
        if (y > 0 && y <= mChartContentHeight) {
            area = AREA_CHART_CONTENT;//点击K线部分
        } else {
            if (mIndicatorNum > 0) {
                int clickAreaNum = 0;
                if(y <= mIndicatorsRectBottom[0]) {//点击最上面的区域
                    clickAreaNum = 0;
                    if(y <= mIndicatorsTextTop[0] + mRes.mTradingTimeAreaHeight + 20 && x < mRes.mIndicatorClickArea) {
                        area = AREA_1_INDICATOR_TEXT;
                    } else {
                        area = AREA_1_INDICATOR;
                    }
                } else if(mIndicatorNum >= 1  && y <= mIndicatorsRectBottom[1]) {//点击第二个区域
                    clickAreaNum = 1;
                    if(y <= mIndicatorsTextTop[1] + mRes.mTradingTimeAreaHeight + 20 && x < mRes.mIndicatorClickArea) {
                        area = AREA_2_INDICATOR_TEXT;
                    } else {
                        area = AREA_2_INDICATOR;
                    }
                } else if(mIndicatorNum >= 2  && y <= mIndicatorsRectBottom[2]) {//点击第二个区域
                    clickAreaNum = 2;
                    if(y <= mIndicatorsTextTop[2] + mRes.mTradingTimeAreaHeight + 20 && x < mRes.mIndicatorClickArea) {
                        area = AREA_3_INDICATOR_TEXT;
                    } else {
                        area = AREA_3_INDICATOR;
                    }
                } else if(mIndicatorNum >= 3  && y <= mIndicatorsRectBottom[3]) {//点击第二个区域
                    clickAreaNum = 3;
                    if(y <= mIndicatorsTextTop[3] + mRes.mTradingTimeAreaHeight + 20 && x < mRes.mIndicatorClickArea) {
                        area = AREA_4_INDICATOR_TEXT;
                    } else {
                        area = AREA_4_INDICATOR;
                    }
                }

                if (getIndicatorType(clickAreaNum) == SettingConst.K_LINE_INDICATOR_BREAK){
                    if (null != mShareRect && mShareRect[clickAreaNum].contains(x, y) && !isSharedForBreak){
                        return AREA_3_SHARE;
                    }
                }
            }
        }
        DtLog.e(TAG, "click area = " + area);
        return area;
    }

    private int getIndicatorType(final int indicatorIndex) {
        if(mListener.isKLineType(mListener.mLineType)) {
            if(mListener.mTradingIndicatorTypes.length > indicatorIndex) {
                return mListener.mTradingIndicatorTypes[indicatorIndex];
            }
        } else {
            if(mListener.mTimeIndicatorTypes.length > indicatorIndex) {
                return mListener.mTimeIndicatorTypes[indicatorIndex];
            }
        }
        return -1;
    }

    void initHandler() {
        mReceiver = new Handler(getLooper(), this);
    }

    public void release() {
        mReceiver.removeCallbacksAndMessages(null);
        final ValueAnimator valueAnimator = mBSAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            mBSAnimator = null;
        }
        mListener = null;
        if (mDrawingSurface != null) {
            mDrawingSurface.release();
            mDrawingSurface = null;
        }
        mKLineEventListener = null;
        mTimeValueChangeListener = null;
        mKLineEntities = null;
        mTimeLineDataCache = null;
        mBSInfos = null;
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

                        //画触摸十字线，以后可能需要
                        //                            drawTouchLine(c);

                        switch (mListener.mLineType) {
                            case LineChartTextureView.TYPE_TIME:
                                //画分时图
                                drawTimeChart(c);
                                break;
                            case LineChartTextureView.TYPE_FIVE_DAY:
                                //画5日分时图
                                drawFiveDayTimeChart(c);
                                break;
                            case LineChartTextureView.TYPE_DAILY_K:
                            case LineChartTextureView.TYPE_WEEKLY_K:
                            case LineChartTextureView.TYPE_MONTHLY_K:
                            case LineChartTextureView.TYPE_ONE_MIN_K:
                            case LineChartTextureView.TYPE_FIVE_MIN_K:
                            case LineChartTextureView.TYPE_FIFTEEN_MIN_K:
                            case LineChartTextureView.TYPE_THIRTY_MIN_K:
                            case LineChartTextureView.TYPE_SIXTY_MIN_K:
                                //画日K线图
                                initKLineSettingValues();
                                drawKChart(c);
                                break;
                            default:
                                break;
                        }

//                                mDrawingSurface.unlockCanvasAndPost(c);

                        DtLog.d("liqf", "after draw a frame");
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
                    clearBackground();
                }
                break;
            default:
                break;
        }
        return true;
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

    void initKLineSettingValues() {
        if (mIndicatorNum != mKlineSettingConfigure.indicatorNum) {
            mIndicatorNum = mKlineSettingConfigure.indicatorNum;
            refreshIndicatorNum();
        }
        for (int i = 0 ; i < mIndicatorNum ; i++) {
            checkIndicatorDisplayed(i);
        }
        if (!mKlineSettingManager.isIndicatorDisplay(mListener.mTradingIndicatorTypes[0])){
            mListener.mTradingIndicatorTypes[0] = mKlineSettingManager.getFirstDisplay();
        }
        if (mIndicatorNum > 1 && !mKlineSettingManager.isIndicatorDisplay(mListener.mTradingIndicatorTypes[1])){
            mListener.mTradingIndicatorTypes[1] = mKlineSettingManager.getFirstDisplay();
        }

        if (mIndicatorNum > 2 && !mKlineSettingManager.isIndicatorDisplay(mListener.mTradingIndicatorTypes[2])){
            mListener.mTradingIndicatorTypes[2] = mKlineSettingManager.getFirstDisplay();
        }

        if (mIndicatorNum > 3 && !mKlineSettingManager.isIndicatorDisplay(mListener.mTradingIndicatorTypes[3])){
            mListener.mTradingIndicatorTypes[3] = mKlineSettingManager.getFirstDisplay();
        }

        String[] maConfigure = mKlineSettingConfigure.MAConfigure.split(",");
        MA_1 = Integer.valueOf(maConfigure[0]);
        MA_2 = Integer.valueOf(maConfigure[1]);
        MA_3 = Integer.valueOf(maConfigure[2]);
        MA_4 = maConfigure.length > 3 ? Integer.valueOf(maConfigure[3]) : 0;
        MA_5 = maConfigure.length > 4 ? Integer.valueOf(maConfigure[4]) : 0;
        MA_6 = maConfigure.length > 5 ? Integer.valueOf(maConfigure[5]) : 0;

        String[] amoutConfigure = mKlineSettingManager.getConfigureByName("成交量").values.split(",");
        AMOUNT_MA_1 = Integer.valueOf(amoutConfigure[0]);
        AMOUNT_MA_2 = Integer.valueOf(amoutConfigure[1]);;
        AMOUNT_MA_3 = amoutConfigure.length > 2 ? Integer.valueOf(amoutConfigure[2]) : 0;

        String[] macdConfigure = mKlineSettingManager.getConfigureByName("MACD").values.split(",");
        K_MACD_1 = Integer.valueOf(macdConfigure[0]);
        K_MACD_2 = Integer.valueOf(macdConfigure[1]);
        K_MACD_3 = Integer.valueOf(macdConfigure[2]);

        String[] kdjConfigure = mKlineSettingManager.getConfigureByName("KDJ").values.split(",");
        K_KDJ_1 = Integer.valueOf(kdjConfigure[0]);
        K_KDJ_2 = Integer.valueOf(kdjConfigure[1]);
        K_KDJ_3 = Integer.valueOf(kdjConfigure[2]);

        String[] rsiConfigure = mKlineSettingManager.getConfigureByName("RSI").values.split(",");
        K_RSI_1 = Integer.valueOf(rsiConfigure[0]);
        K_RSI_2 = Integer.valueOf(rsiConfigure[1]);
        K_RSI_3 = Integer.valueOf(rsiConfigure[2]);

        String[] bbiConfigure = mKlineSettingManager.getConfigureByName("BBI").values.split(",");
        K_BBI_1 = Integer.valueOf(bbiConfigure[0]);
        K_BBI_2 = Integer.valueOf(bbiConfigure[1]);
        K_BBI_3 = Integer.valueOf(bbiConfigure[2]);
        K_BBI_4 = Integer.valueOf(bbiConfigure[3]);

        String[] dmiConfigure = mKlineSettingManager.getConfigureByName("DMI").values.split(",");
        K_DMI_1 = Integer.valueOf(dmiConfigure[0]);
        K_DMI_2 = Integer.valueOf(dmiConfigure[1]);

        String[] cciConfigure = mKlineSettingManager.getConfigureByName("CCI").values.split(",");
        K_CCI_1 = Integer.valueOf(cciConfigure[0]);
        K_CCI_2 = Integer.valueOf(cciConfigure[1]);

        String[] eneConfigure = mKlineSettingManager.getConfigureByName("ENE").values.split(",");
        K_ENE_1 = Integer.valueOf(eneConfigure[0]);
        K_ENE_2 = Integer.valueOf(eneConfigure[1]);
        K_ENE_3 = Integer.valueOf(eneConfigure[2]);

        String[] dmaConfigure = mKlineSettingManager.getConfigureByName("DMA").values.split(",");
        K_DMA_1 = Integer.valueOf(dmaConfigure[0]);
        K_DMA_2 = Integer.valueOf(dmaConfigure[1]);
        K_DMA_3 = Integer.valueOf(dmaConfigure[2]);

        String[] emaConfigure = mKlineSettingManager.getConfigureByName("EXPMA").values.split(",");
        K_EXPMA_1 = Integer.valueOf(emaConfigure[0]);
        K_EXPMA_2 = Integer.valueOf(emaConfigure[1]);
        K_EXPMA_3 = emaConfigure.length > 2 ? Integer.valueOf(emaConfigure[2]) : 0;
        K_EXPMA_4 = emaConfigure.length > 3 ? Integer.valueOf(emaConfigure[3]) : 0;

        String[] vrConfigure = mKlineSettingManager.getConfigureByName("VR").values.split(",");
        K_VR = Integer.valueOf(vrConfigure[0]);

        String[] biasConfigure = mKlineSettingManager.getConfigureByName("BIAS").values.split(",");
        K_BIAS_1 = Integer.valueOf(biasConfigure[0]);
        K_BIAS_2 = Integer.valueOf(biasConfigure[1]);
        K_BIAS_3 = Integer.valueOf(biasConfigure[2]);

        String[] wrConfigure = mKlineSettingManager.getConfigureByName("WR").values.split(",");
        K_WR = Integer.valueOf(wrConfigure[0]);

        String[] bollConfigure = mKlineSettingManager.getConfigureByName("BOLL").values.split(",");
        DEFAULT_BOLL_MA_COUNT = Integer.valueOf(bollConfigure[0]);
        DEFAULT_BOLL_FACTOR = Integer.valueOf(bollConfigure[1]);
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        mZoomRatio = mIsFullScreen ? dengtaApplication.mKLineZoomRatioLandscape : dengtaApplication.mKLineZoomRatioPortrait;

        isSharedForBreak = SettingPref.getIBoolean(SettingConst.KEY_SHARED_FOR_BREAK, false);
    }

    private void checkIndicatorDisplayed(int index) {
        if (mIndicatorNum > index && !mKlineSettingManager.isIndicatorDisplay(mListener.mTradingIndicatorTypes[index])){
            mListener.mTradingIndicatorTypes[index] = mKlineSettingManager.getFirstDisplay();
        }
    }

    private void drawBorderRect(Canvas c) {
        //画上面分时线的矩形外框
        c.drawRect(mRes.mBorderStrokeWidth, mRes.mBorderStrokeWidth, mDrawingWidth - mRes.mBorderStrokeWidth,
                mChartContentHeight - mRes.mBorderStrokeWidth, mRes.mPaintRect);


        for(int rectBottom : mIndicatorsRectBottom) {
            c.drawRect(mRes.mBorderStrokeWidth, rectBottom - mRes.mIndicatorRectHeight,
                    mDrawingWidth - mRes.mBorderStrokeWidth, rectBottom, mRes.mPaintRect);
        }
   }

   private void drawTimeReferenceLines(Canvas canvas) {
       float gap = mDrawingWidth / (VERTICAL_REFERENCE_LINE_COUNT + 1);
       for(int i=0; i<VERTICAL_REFERENCE_LINE_COUNT; i++) {
           float x = gap * (i + 1);
           canvas.drawLine(x, 0, x, mChartContentHeight, mRes.mPaintDashLine);
       }
       //画上面分时线的中间虚线
       canvas.drawLine(0, mChartContentHeight / 2, mDrawingWidth, mChartContentHeight / 2, mRes.mPaintDashLine);
   }

   private boolean hasOriginalTimeData() {
       TimeLineData timeLineData = mTimeLineDataCache.getTimeLineData(0);
       return timeLineData != null && timeLineData.hasTimeEntities();
   }

   private boolean checkOriginalTimeData() {
       TimeLineData timeLineData = mTimeLineDataCache.getTimeLineData(0);
       if(timeLineData != null && mListener != null) {
           DtLog.d(TAG, "checkOriginalTimeData() canvas drawTimeChart wrong data: nowPrice = " + mListener.getNowPrice() +
                   ", yesterdayClose = " + mListener.getYesterdayClose() + ", secStatus = " + mListener.mSecStatus +
                   ", mTimeValueMin = " + timeLineData.minValue + ", mTimeValueMax = " + timeLineData.maxValue +
                   ", mTradingMinutes = " + mListener.mTradingMinutes);
           return timeLineData.checkData() && mListener.mTradingMinutes != 0;
       }
       return false;
   }

   /**
    *
    * 绘制分时图
    * */
    private void drawTimeChart(Canvas canvas) {
        DtLog.i(TAG, "canvas drawTimeChart: " + Thread.currentThread().getId());
        drawTimeReferenceLines(canvas);
        drawTradingTime(canvas);

        if (!hasOriginalTimeData()) {
            DtLog.i(TAG, "canvas drawTimeChart mTimeEntities has no data!");
            if (mTimeValueChangeListener != null) {
                mTimeValueChangeListener.onValuePositionChanged(null);
            }
            return;
        }

        if (!checkOriginalTimeData()) { //没有昨日收盘价，也就没有分时图的中线，此时不画
            if (mTimeValueChangeListener != null) {
                mTimeValueChangeListener.onValuePositionChanged(null);
            }
            return;
        }

        DtLog.i(TAG, "canvas drawTimeChart start");
        Point point = drawTimeLine(canvas, 0);
        if(point != null) {
            mTimeNowPoint.set(point.x, point.y);
            mTimeValueChangeListener.onValuePositionChanged(point);
        }
        drawCornerValues(canvas);

        drawTimeLine(canvas, 1);

        for (int i = 0; i < mIndicatorNum; i++) {
            drawTimeLineIndicators(canvas, i);
        }

        drawTimeTouchLine(canvas);

        drawEntrance(canvas, "放大查看分时");

        DtLog.i(TAG, "canvas drawTimeChart complete");
    }

    private Point drawTimeLine(Canvas canvas, int index) {
        TimeLineData timeLineData = mTimeLineDataCache.getTimeLineData(index);
        if(timeLineData == null || !timeLineData.check()) {
            return null;
        }
        List<TrendDesc> timeEntities = timeLineData.timeEntities;
        //先复位所有Path的起始点
        int strokeWidth = mStrokeWidth;
        mRes.mTimeHorizontalGap = (float) (mDrawingWidth - strokeWidth) / mListener.mTradingMinutes;
        mRes.mShadowPath.reset();
        mRes.mShadowPath.moveTo(0, mChartContentHeight);
        mRes.mTimePath.reset();
        mRes.mTimeAveragePath.reset();

        //从第i个点(start)，向第i+1个点(stop)画线。包括分时线，分时均线和成交量
        float startX = strokeWidth / 2, startY = 0, stopX = 0, stopY = 0;
        Point point = null;
        for (int i = 0; i < timeEntities.size() - 1; i++) {
            //取第i个点的数值
            TrendDesc entity = timeEntities.get(i);
            float value = entity.getFNow();
            float average = entity.getFAverage();
            if (average == 0) {
                average = value;
            }

            //取第i+1个点的数值
            TrendDesc nextEntity = timeEntities.get(i+1);
            float nextValue = nextEntity.getFNow();
            float nextAverage = nextEntity.getFAverage();
            if (nextAverage == 0) {
                nextAverage = nextValue;
            }

            //获取数值对应的绘图坐标位置
            startY = getPriceDrawingHeight(value, timeLineData);
            stopY = getPriceDrawingHeight(nextValue, timeLineData);
            stopX = startX + mRes.mTimeHorizontalGap;
            //                DtLog.d("liqf", "now price startX=" + startX + ", startY=" + startY + ", stopX=" + stopX + ", stopY = " + stopY);

            //阴影path向前移动
            mRes.mShadowPath.lineTo(startX, startY);
            mRes.mShadowPath.lineTo(stopX, stopY);

            if (i == 0) { //安置分时均线Path的起始点
                mRes.mTimePath.moveTo(startX, startY);
                mRes.mTimeAveragePath.moveTo(startX, getPriceDrawingHeight(average, timeLineData));
            }

            if (i > 0) { //分时线和均线都向前移动
                mRes.mTimePath.lineTo(stopX, stopY);
                mRes.mTimeAveragePath.lineTo(stopX, getPriceDrawingHeight(nextAverage, timeLineData));
            }

            if (i + 1 == timeEntities.size() - 1) { //分时列表的最后一个点指示符闪烁
                point = new Point((int) stopX, (int) stopY);
            }

            startX += mRes.mTimeHorizontalGap; //横坐标起始点移动到下一个时间片
        }

        if (timeEntities.size() == 1) { //只有一个点的时候以上循环进不去，在此处画闪烁的点
            TrendDesc entity = timeEntities.get(0);
            float value = entity.getFNow();
            point = new Point(0, (int) getPriceDrawingHeight(value, timeLineData));
        }

        //close阴影Path
        mRes.mShadowPath.lineTo(stopX, mChartContentHeight);
        mRes.mShadowPath.close();

        if(index == 0) {
            //画分时阴影
            canvas.drawPath(mRes.mShadowPath, mRes.mPaintShadow);
            //画分时线
            mRes.mPaintLine.setColor(mRes.mTimeLineColor);
            canvas.drawPath(mRes.mTimePath, mRes.mPaintLine);
            //画分时均线
            if (mListener.supportAverage()) {
                mRes.mPaintLine.setColor(mRes.mTimeAverageLineColor);
                canvas.drawPath(mRes.mTimeAveragePath, mRes.mPaintLine);
            }
        } else {
            //画分时线
            mRes.mPaintLine.setColor(mRes.mCompareButtonColor);
            canvas.drawPath(mRes.mTimePath, mRes.mPaintLine);
        }

        return point;
    }

    private void drawFiveDayTimeChart(Canvas canvas) {
        DtLog.d(TAG, "canvas drawTimeChart: " + Thread.currentThread().getId());
        if (mListener.mRtMinDescs == null || mListener.mRtMinDescs.size() == 0) {
            DtLog.d(TAG, "canvas drawTimeChart mTimeEntities has no data!");
            if (mTimeValueChangeListener != null) {
                mTimeValueChangeListener.onValuePositionChanged(null);
            }
            return;
        }

        if (!checkOriginalTimeData()) { //没有昨日收盘价，也就没有分时图的中线，此时不画
            if (mTimeValueChangeListener != null) {
                mTimeValueChangeListener.onValuePositionChanged(null);
            }
            return;
        }

        DtLog.i(TAG, "canvas drawTimeChart start");

        if(StockUtil.unSupportFiveDayTimeLine(mListener.mDtSecCode)) {
            drawUnsupportChart(canvas);
            drawUnsupportIndicatior(canvas, "", -1);
        } else {
            drawCornerValues(canvas);
            drawFiveDayLine(canvas);
            drawFiveDayTradingDate(canvas);
            //先复位所有Path的起始点
            int strokeWidth = mStrokeWidth;

            mRes.mTimeHorizontalGap = (float) (mDrawingWidth - strokeWidth) / (5 * (mListener.mTradingMinutes + 1) - 1);

            float startX = strokeWidth / 2, startY = 0, stopX = 0, stopY = 0;
            //从第i个点(start)，向第i+1个点(stop)画线。包括分时线，分时均线和成交量
            int size = mListener.mRtMinDescs.size();

            for(int dayIndex=size - 1; dayIndex>=0; dayIndex--) {
                mRes.mShadowPath.reset();
                mRes.mTimePath.reset();
                mRes.mTimeAveragePath.reset();
                RtMinDesc rtMinDesc = mListener.mRtMinDescs.get(dayIndex);
                ArrayList<TrendDesc> dayTimeEntities = rtMinDesc.getVTrendDesc();
                mRes.mShadowPath.moveTo(startX - strokeWidth / 2, mChartContentHeight);
                for (int timeIndex = 0, dayCount = dayTimeEntities.size();
                     timeIndex < dayCount - 1; timeIndex++) {
                    //取第i个点的数值
                    TrendDesc entity = dayTimeEntities.get(timeIndex);
                    float value = entity.getFNow();
                    float average = entity.getFAverage();
                    if (average == 0) {
                        average = value;
                    }

                    //取第i+1个点的数值
                    TrendDesc nextEntity = dayTimeEntities.get(timeIndex+1);
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

                    if (timeIndex == 0) { //安置分时均线Path的起始点
                        mRes.mTimePath.moveTo(startX, startY);
                        mRes.mTimeAveragePath.moveTo(startX, getPriceDrawingHeight(average));
                    }

                    if (timeIndex > 0) { //分时线和均线都向前移动
                        mRes.mTimePath.lineTo(stopX, stopY);
                        mRes.mTimeAveragePath.lineTo(stopX, getPriceDrawingHeight(nextAverage));
                    }

                    if (dayIndex == 0 && timeIndex + 1 == dayTimeEntities.size() - 1) { //分时列表的最后一个点指示符闪烁
                        mTimeNowPoint.x = (int) stopX;
                        mTimeNowPoint.y = (int) stopY;
                        if (mTimeValueChangeListener != null) {
                            mTimeValueChangeListener.onValuePositionChanged(new Point(mTimeNowPoint));
                        }
                    }

                    startX += mRes.mTimeHorizontalGap; //横坐标起始点移动到下一个时间片
                }
                startX += mRes.mTimeHorizontalGap;
                if(dayIndex == 0) {
                    if (dayTimeEntities.size() == 1) { //只有一个点的时候以上循环进不去，在此处画闪烁的点
                        TrendDesc entity = dayTimeEntities.get(0);
                        float value = entity.getFNow();
                        mTimeNowPoint.x = (int) 0;
                        mTimeNowPoint.y = (int) getPriceDrawingHeight(value);
                        if (mTimeValueChangeListener != null) {
                            mTimeValueChangeListener.onValuePositionChanged(new Point(mTimeNowPoint));
                        }
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
                //画分时均线
                if (mListener.supportAverage()) {
                    mRes.mPaintLine.setColor(mRes.mTimeAverageLineColor);
                    canvas.drawPath(mRes.mTimeAveragePath, mRes.mPaintLine);
                }

            }
            drawTimeTouchLine(canvas);
            for (int i = 0; i < mIndicatorNum; i++) {
                drawTimeLineIndicators(canvas, i);
            }
        }

        DtLog.d(TAG, "canvas drawTimeChart complete");
    }

    private void drawFiveDayLine(Canvas canvas) {
        //画上面分时线的中间虚线
        canvas.drawLine(0, mChartContentHeight / 2,
                mDrawingWidth, mChartContentHeight / 2, mRes.mPaintDashLine);
        int strokeWidth = mStrokeWidth;
        float width = (mDrawingWidth - strokeWidth)/(float) LineChartTextureView.FIVE_DAY_COUNT;
        float startX = strokeWidth / 2;
        for(int i=0; i< LineChartTextureView.FIVE_DAY_COUNT-1; i++) {
            startX += width;
            canvas.drawLine(startX, 0, startX, mChartContentHeight, mRes.mPaintDashLine);
        }
    }

    /**
     * 画指标
     * @param canvas
     * @param index
     */
    private void drawTimeLineIndicators(Canvas canvas, int index) {
        switch (mListener.mTimeIndicatorTypes[index]) {
            case SettingConst.TIME_LINE_INDICATOR_VOLUME:
                drawTimeLineIndicatorVolume(canvas, index);
                break;
            case SettingConst.TIME_LINE_INDICATOR_CAPITAL_DDZ://资金博弈
                drawTimeLineIndicatorCapitalDDZ(canvas, index);
                break;
            case SettingConst.TIME_LINE_INDICATOR_MACD:
                drawTimeLineIndicatorMACD(canvas, index);
                break;
            case SettingConst.TIME_LINE_INDICATOR_VOLUME_RATIO:
                drawTimeLineIndicatorVolumeRatio(canvas, index);
                break;
            default:
                break;
        }
    }

    private float SUM(List<Float> list, int i, int n) {
        if (i < 0 || n < 0)
            return 0;
        float sum = 0;
        if (n == 0) {
            for (int j = 0; j < list.size(); j++) {
                sum += list.get(j);
            }
            return sum;
        }
        int start = i - n + 1;
        if (start < 0) {
            start = 0;
        }
        for (int j = start; j <= i; j++) {
            sum += list.get(j);
        }
        return sum;
    }

    private void drawUnsupportChart(Canvas canvas) {
        mRes.mKLineTextPaint.setTypeface(null);
        mRes.mKLineTextPaint.setTextSize(mRes.mTextSize);
        mRes.mKLineTextPaint.setColor(mRes.mKColorGray);
        String placeHolderStr = "暂不支持";
        float textWidth = mRes.mTextDrawer.measureSingleTextWidth(placeHolderStr, mRes.mTextSize, null);
        int textHeight = mRes.mTextDrawer.measureSingleTextHeight(mRes.mTextSize, null);
        float y = (mChartContentHeight + textHeight) / 2;
        float x = (mDrawingWidth - textWidth) / 2;
        canvas.drawText(placeHolderStr, x, y, mRes.mKLineTextPaint);
    }

    /**
     * 不支持该指标
     * @param canvas
     * @param title
     * @param index
     */
    private void drawUnsupportIndicatior(Canvas canvas, String title, int index) {
        if(index < 0) {
            return;
        }
        int indicatorBottom = mIndicatorsRectBottom[index];
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float x = 0, y = bottomLimit + mRes.mTextHeight;
        mRes.mKLineTextPaint.setTypeface(null);
        mRes.mKLineTextPaint.setTextSize(mRes.mTextSize);
        mRes.mKLineTextPaint.setColor(mRes.mKColorGray);
        if(!TextUtils.isEmpty(title)) {
            drawIndicatorPreText(canvas, title, bottomLimit);
//            canvas.drawText(title, x, y, mRes.mKLineTextPaint);
        }
        String placeHolderStr = mRes.mIndicatorNotSupportPlaceHolder;
        float textWidth = mRes.mTextDrawer.measureSingleTextWidth(placeHolderStr, mRes.mTextSize, null);
        int textHeight = mRes.mTextDrawer.measureSingleTextHeight(mRes.mTextSize, null);
        x = (mDrawingWidth - textWidth) / 2;
        y = (mRes.mIndicatorRectHeight - textHeight) / 2 + textHeight + bottomLimit;
        canvas.drawText(placeHolderStr, x, y, mRes.mKLineTextPaint);
    }

    /**
     * 五日下资金博弈指标画线
     */
    private void drawFiveDayIndicatorCapitalDDZ(Canvas canvas,float bottomLimit, int index,int indicatorBottom){
        Log.i(TAG, "drawTimeLineIndicatorCapitalDDZ: 画五日的资金博弈");
        drawIndicatorPreText(canvas, getCapitalDDZPreText(), bottomLimit);
        drawIndicatorReferenceLine(canvas, index, true);
//        canvas.drawText("资金博弈", x, y, mRes.mKLineTextPaint);
        if (mListener.mFiveDayCapitalDDZEntities == null || mListener.mFiveDayCapitalDDZEntities.size() == 0) {
            Log.i(TAG, "drawFiveDayIndicatorCapitalDDZ: 没有五日数据 所以不画指标");
            return;
        }

        TimeLineData timeLineData = mTimeLineDataCache.getTimeLineData(0);
        if (mListener.getYesterdayClose() == 0
                || timeLineData== null
                || timeLineData.minValue == 0 || timeLineData.maxValue == 0
                || mListener.mTradingMinutes == 0
                || mListener.mQuote == null
                || mTimeFiveDayCapitalDDZMax == Float.MIN_VALUE || mTimeFiveDayCapitalDDZMin == Float.MAX_VALUE) { //没有昨日收盘价，也就没有分时图的中线，此时不画
            Log.i(TAG, "drawFiveDayIndicatorCapitalDDZ: 没有昨日收盘价，也就没有分时图的中线，此时不画");
            return;
        }

        //先复位所有Path的起始点
        mRes.mTimeCapitalDDZPathSuper.reset();
        mRes.mTimeCapitalDDZPathBig.reset();
        mRes.mTimeCapitalDDZPathMid.reset();
        mRes.mTimeCapitalDDZPathSmall.reset();

        int strokeWidth = mStrokeWidth;
        float startX = strokeWidth / 2;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float middleY = bottomLimit + bottomHeight / 2;
//        canvas.drawLine(startX, middleY, startX + mDrawingWidth, middleY, mRes.mPaintRect);

        List<Float> sups = new ArrayList<Float>();
        List<Float> bigs = new ArrayList<Float>();
        List<Float> mids = new ArrayList<Float>();
        List<Float> smalls = new ArrayList<Float>();
        List<Float> ssups = new ArrayList<Float>();
        List<Float> sbigs = new ArrayList<Float>();
        List<Float> smids = new ArrayList<Float>();
        List<Float> ssmalls = new ArrayList<Float>();
        zjbylist = new ArrayList<KZJBY>();
        int n = 0;
        int firstIndex = mListener.mFiveDayCapitalDDZEntities.size() - 1;
        for (int i = firstIndex; i >= 0; i--) {
            CapitalDDZDesc b = mListener.mFiveDayCapitalDDZEntities.get(i);
            float sup = b.getFSuperInAmt() - b.getFSuperOutAmt();
            float big = b.getFBigInAmt() - b.getFBigOutAmt();
            float mid = b.getFMidInAmt() - b.getFMidOutAmt();
            float small = b.getFSmallInAmt() - b.getFSmallOutAmt();
            ssups.add(sup);
            sbigs.add(big);
            smids.add(mid);
            ssmalls.add(small);
            float ssup = SUM(ssups, i, n);
            float sbig = SUM(sbigs, i, n);
            float smid = SUM(smids, i, n);
            float ssmall = SUM(ssmalls, i, n);
            sups.add(ssup);
            bigs.add(sbig);
            mids.add(smid);
            smalls.add(ssmall);
            zjbylist.add(new KZJBY(ssup, sbig, smid, ssmall));
        }

        float maxsup = 0, minsup = 0, maxbig = 0, minbig = 0, maxmid = 0, minmid = 0, maxsmall = 0, minsmall = 0;
        for (int i = 0; i < zjbylist.size(); i++) {
            float sup = sups.get(i);
            float big = bigs.get(i);
            float mid = mids.get(i);
            float small = smalls.get(i);
            if (i == 0) {
                minsup = sup;
                minbig = big;
                minmid = mid;
                minsmall = small;
            }
            if (maxsup < sup) maxsup = sup;
            if (minsup > sup) minsup = sup;
            if (maxbig < big) maxbig = big;
            if (minbig > big) minbig = big;
            if (maxmid < mid) maxmid = mid;
            if (minmid > mid) minmid = mid;
            if (maxsmall < small) maxsmall = small;
            if (minsmall > small) minsmall = small;
        }
        float max = maxsup, min = minsup;
        if (max < maxbig) max = maxbig;
        if (max < maxmid) max = maxmid;
        if (max < maxsmall) max = maxsmall;
        if (min > minbig) min = minbig;
        if (min > minmid) min = minmid;
        if (min > minsmall) min = minsmall;

        float superDelta, bigDelta, midDelta, smallIn, smallOut, smallDelta;
        mTimeFiveDayCapitalDDZMax = max;
        mTimeFiveDayCapitalDDZMin = min;
        for (int i = 0; i < zjbylist.size(); i++) {
            KZJBY zjby = zjbylist.get(i);
            superDelta = zjby.getSsup();
            bigDelta = zjby.getSbig();
            midDelta = zjby.getSmid();
            smallDelta = zjby.getSsmall();
            if (i == 0) {
                mRes.mTimeCapitalDDZPathSuper.moveTo(startX, getFiveDayCapitalDDZDrawingHeight(superDelta, indicatorBottom));
                mRes.mTimeCapitalDDZPathBig.moveTo(startX, getFiveDayCapitalDDZDrawingHeight(bigDelta, indicatorBottom));
                mRes.mTimeCapitalDDZPathMid.moveTo(startX, getFiveDayCapitalDDZDrawingHeight(midDelta, indicatorBottom));
                mRes.mTimeCapitalDDZPathSmall.moveTo(startX, getFiveDayCapitalDDZDrawingHeight(smallDelta, indicatorBottom));
            } else {
                mRes.mTimeCapitalDDZPathSuper.lineTo(startX, getFiveDayCapitalDDZDrawingHeight(superDelta, indicatorBottom));
                mRes.mTimeCapitalDDZPathBig.lineTo(startX, getFiveDayCapitalDDZDrawingHeight(bigDelta, indicatorBottom));
                mRes.mTimeCapitalDDZPathMid.lineTo(startX, getFiveDayCapitalDDZDrawingHeight(midDelta, indicatorBottom));
                mRes.mTimeCapitalDDZPathSmall.lineTo(startX, getFiveDayCapitalDDZDrawingHeight(smallDelta, indicatorBottom));
            }

            startX += mRes.mTimeHorizontalGap; //横坐标起始点移动到下一个时间片
        }

        //画分时线
        mRes.mPaintLine.setColor(mRes.mKColorRed);
        canvas.drawPath(mRes.mTimeCapitalDDZPathSuper, mRes.mPaintLine);
        mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
        canvas.drawPath(mRes.mTimeCapitalDDZPathBig, mRes.mPaintLine);
        mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
        canvas.drawPath(mRes.mTimeCapitalDDZPathMid, mRes.mPaintLine);
        mRes.mPaintLine.setColor(mRes.mKColorGreen);
        canvas.drawPath(mRes.mTimeCapitalDDZPathSmall, mRes.mPaintLine);

    }

    /**
     * 画资金博弈指标
     * @param canvas
     * @param index
     */
    private void drawTimeLineIndicatorCapitalDDZ(Canvas canvas, int index) {
        final int indicatorBottom = mIndicatorsRectBottom[index];
        //画左上角的指标参数
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float x = 0, y = bottomLimit + mRes.mTextHeight;
        mRes.mKLineTextPaint.setTypeface(null);
        mRes.mKLineTextPaint.setTextSize(mRes.mTextSize);
        mRes.mKLineTextPaint.setColor(mRes.mKColorGray);

        if (!TextUtils.isEmpty(mListener.mDtSecCode)) {
            if (mListener.mLineType == LineChartTextureView.TYPE_FIVE_DAY ||
                    !StockUtil.supportCapitalDDZ(mListener.mDtSecCode)) {  //五日的资金博弈
               // drawUnsupportIndicatior(canvas, getCapitalDDZPreText(), index);
                //五日的资金博弈指标
                drawFiveDayIndicatorCapitalDDZ(canvas,bottomLimit,index,indicatorBottom);
                return;
            }
        }
        //下面是分时的资金博弈指标
        Log.i(TAG, "drawTimeLineIndicatorCapitalDDZ: 画分时的资金博弈");
        drawIndicatorPreText(canvas, getCapitalDDZPreText(), bottomLimit);
        drawIndicatorReferenceLine(canvas, index, true);
//        canvas.drawText("资金博弈", x, y, mRes.mKLineTextPaint);
        if (mListener.mCapitalDDZEntities == null || mListener.mCapitalDDZEntities.size() == 0) {
            return;
        }

        TimeLineData timeLineData = mTimeLineDataCache.getTimeLineData(0);
        if (mListener.getYesterdayClose() == 0
                || timeLineData== null
                || timeLineData.minValue == 0 || timeLineData.maxValue == 0
                || mListener.mTradingMinutes == 0
                || mListener.mQuote == null
                || mTimeCapitalDDZMax == Float.MIN_VALUE || mTimeCapitalDDZMin == Float.MAX_VALUE) { //没有昨日收盘价，也就没有分时图的中线，此时不画
            return;
        }

        //先复位所有Path的起始点
        mRes.mTimeCapitalDDZPathSuper.reset();
        mRes.mTimeCapitalDDZPathBig.reset();
        mRes.mTimeCapitalDDZPathMid.reset();
        mRes.mTimeCapitalDDZPathSmall.reset();

        int strokeWidth = mStrokeWidth;
        float startX = strokeWidth / 2;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float middleY = bottomLimit + bottomHeight / 2;
//        canvas.drawLine(startX, middleY, startX + mDrawingWidth, middleY, mRes.mPaintRect);

        List<Float> sups = new ArrayList<Float>();
        List<Float> bigs = new ArrayList<Float>();
        List<Float> mids = new ArrayList<Float>();
        List<Float> smalls = new ArrayList<Float>();
        List<Float> ssups = new ArrayList<Float>();
        List<Float> sbigs = new ArrayList<Float>();
        List<Float> smids = new ArrayList<Float>();
        List<Float> ssmalls = new ArrayList<Float>();
        zjbylist = new ArrayList<KZJBY>();
        int n = 0;
        int firstIndex = mListener.mCapitalDDZEntities.size() - 1;
        for (int i = firstIndex; i >= 0; i--) {
            CapitalDDZDesc b = mListener.mCapitalDDZEntities.get(i);
            float sup = b.getFSuperInAmt() - b.getFSuperOutAmt();
            float big = b.getFBigInAmt() - b.getFBigOutAmt();
            float mid = b.getFMidInAmt() - b.getFMidOutAmt();
            float small = b.getFSmallInAmt() - b.getFSmallOutAmt();
            ssups.add(sup);
            sbigs.add(big);
            smids.add(mid);
            ssmalls.add(small);
            float ssup = SUM(ssups, i, n);
            float sbig = SUM(sbigs, i, n);
            float smid = SUM(smids, i, n);
            float ssmall = SUM(ssmalls, i, n);
            sups.add(ssup);
            bigs.add(sbig);
            mids.add(smid);
            smalls.add(ssmall);
            zjbylist.add(new KZJBY(ssup, sbig, smid, ssmall));
        }

        float maxsup = 0, minsup = 0, maxbig = 0, minbig = 0, maxmid = 0, minmid = 0, maxsmall = 0, minsmall = 0;
        for (int i = 0; i < zjbylist.size(); i++) {
            float sup = sups.get(i);
            float big = bigs.get(i);
            float mid = mids.get(i);
            float small = smalls.get(i);
            if (i == 0) {
                minsup = sup;
                minbig = big;
                minmid = mid;
                minsmall = small;
            }
            if (maxsup < sup) maxsup = sup;
            if (minsup > sup) minsup = sup;
            if (maxbig < big) maxbig = big;
            if (minbig > big) minbig = big;
            if (maxmid < mid) maxmid = mid;
            if (minmid > mid) minmid = mid;
            if (maxsmall < small) maxsmall = small;
            if (minsmall > small) minsmall = small;
        }
        float max = maxsup, min = minsup;
        if (max < maxbig) max = maxbig;
        if (max < maxmid) max = maxmid;
        if (max < maxsmall) max = maxsmall;
        if (min > minbig) min = minbig;
        if (min > minmid) min = minmid;
        if (min > minsmall) min = minsmall;

        float superDelta, bigDelta, midDelta, smallIn, smallOut, smallDelta;
        mTimeCapitalDDZMax = max;
        mTimeCapitalDDZMin = min;
        for (int i = 0; i < zjbylist.size(); i++) {
            KZJBY zjby = zjbylist.get(i);
            superDelta = zjby.getSsup();
            bigDelta = zjby.getSbig();
            midDelta = zjby.getSmid();
            smallDelta = zjby.getSsmall();
            if (i == 0) {
                mRes.mTimeCapitalDDZPathSuper.moveTo(startX, getCapitalDDZDrawingHeight(superDelta, indicatorBottom));
                mRes.mTimeCapitalDDZPathBig.moveTo(startX, getCapitalDDZDrawingHeight(bigDelta, indicatorBottom));
                mRes.mTimeCapitalDDZPathMid.moveTo(startX, getCapitalDDZDrawingHeight(midDelta, indicatorBottom));
                mRes.mTimeCapitalDDZPathSmall.moveTo(startX, getCapitalDDZDrawingHeight(smallDelta, indicatorBottom));
            } else {
                mRes.mTimeCapitalDDZPathSuper.lineTo(startX, getCapitalDDZDrawingHeight(superDelta, indicatorBottom));
                mRes.mTimeCapitalDDZPathBig.lineTo(startX, getCapitalDDZDrawingHeight(bigDelta, indicatorBottom));
                mRes.mTimeCapitalDDZPathMid.lineTo(startX, getCapitalDDZDrawingHeight(midDelta, indicatorBottom));
                mRes.mTimeCapitalDDZPathSmall.lineTo(startX, getCapitalDDZDrawingHeight(smallDelta, indicatorBottom));
            }

            startX += mRes.mTimeHorizontalGap; //横坐标起始点移动到下一个时间片
        }

        //画分时线
        mRes.mPaintLine.setColor(mRes.mKColorRed);
        canvas.drawPath(mRes.mTimeCapitalDDZPathSuper, mRes.mPaintLine);
        mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
        canvas.drawPath(mRes.mTimeCapitalDDZPathBig, mRes.mPaintLine);
        mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
        canvas.drawPath(mRes.mTimeCapitalDDZPathMid, mRes.mPaintLine);
        mRes.mPaintLine.setColor(mRes.mKColorGreen);
        canvas.drawPath(mRes.mTimeCapitalDDZPathSmall, mRes.mPaintLine);
    }

    private void drawTimeLineIndicatorVolume(Canvas canvas, int index) {
        TimeLineData timeLineData = mTimeLineDataCache.getTimeLineData(0);
        if (timeLineData == null || !timeLineData.hasTimeEntities()) {
            return;
        }
        List<TrendDesc> timeEntities = timeLineData.timeEntities;
        final int indicatorBottom = mIndicatorsRectBottom[index];

        //画左上角的指标参数
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        drawIndicatorPreText(canvas, getVolumePreText(), bottomLimit);
        drawIndicatorReferenceLine(canvas, index, false);
        drawIndicatorReferenceLine(canvas, index, true);

        if (!checkOriginalTimeData()) { //没有昨日收盘价，也就没有分时图的中线，此时不画
            return;
        }

        //从第i个点(start)，向第i+1个点(stop)画线。包括分时线，分时均线和成交量
        int strokeWidth = mStrokeWidth;
        float startX = strokeWidth / 2, stopX = 0;
        for (int i = 0; i < timeEntities.size() - 1; i++) {
            //取第i个点的数值
            TrendDesc entity = timeEntities.get(i);
            float value = entity.getFNow();
            float amount = entity.getLNowvol();

            //画第i个点的成交量
            float prevValue = mListener.getYesterdayClose();
            float delta = 0;
            if (i > 0) {
                prevValue = timeEntities.get(i - 1).getFNow();
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
            canvas.drawLine(startX, indicatorBottom, startX, indicatorBottom - getAmountDrawingHeight(amount), mRes.mPaintAmount);

            //获取数值对应的绘图坐标位置
            stopX = startX + mRes.mTimeHorizontalGap;

            //画最后一个点的成交量
            if (i + 1 == timeEntities.size() - 1) { //分时列表的最后一个点指示符闪烁
                //取第i+1个点的数值
                TrendDesc nextEntity = timeEntities.get(i + 1);
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
                canvas.drawLine(stopX, indicatorBottom, stopX, indicatorBottom - getAmountDrawingHeight(nextAmount), mRes.mPaintAmount);
            }

            startX += mRes.mTimeHorizontalGap; //横坐标起始点移动到下一个时间片
        }
    }

    private void updateVolumnAverage(final List<TrendDesc> timeEntities) {
        float totalVolume = 0;
        float maxVolumeAverage = 0f;
        float minVolumeAverage = Float.MAX_VALUE;
        final int size = timeEntities != null ? timeEntities.size() : 0;
        if (size == 0) {
            mTimeVolumeAverage = null;
            return;
        }
        final float[] volumeAvg = new float[size];
        int i = 0;
        float avg;
        for (TrendDesc entity : timeEntities) {
            totalVolume += entity.lNowvol;
            avg = totalVolume / (i + 1);
            volumeAvg[i] = avg;
            maxVolumeAverage = Math.max(maxVolumeAverage, avg);
            minVolumeAverage = Math.min(minVolumeAverage, avg);
            i++;
        }
        mTimeVolumeAverage = volumeAvg;
        mTimeMaxVolumeAverage = maxVolumeAverage;
        mTimeMinVolumeAverage = minVolumeAverage;
    }

    // 量比
    private void drawTimeLineIndicatorVolumeRatio(Canvas canvas, int index) {
        List<TrendDesc> timeEntities = mTimeLineDataCache.getTimeLineEntities(0);
        if (timeEntities == null || timeEntities.size() == 0) {
            return;
        }

        final int indicatorBottom = mIndicatorsRectBottom[index];

        //画左上角的指标参数
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        drawIndicatorPreText(canvas, getVolumeRatioPreText(), bottomLimit);
        if (!StockUtil.supportVolumeRatio(mListener.mDtSecCode)) {//量比
            Log.i(TAG, "drawTimeLineIndicatorVolumeRatio: 不支持量比的"+getVolumeRatioPreText());
            drawUnsupportIndicatior(canvas, getVolumeRatioPreText(), index);
            return;
        }
        if(mListener.getLineType() == LineChartTextureView.TYPE_FIVE_DAY) {//五日下是否画量比的指标
            drawUnsupportIndicatior(canvas, getVolumeRatioPreText(), index);//不画，显示不支持该类型
            Log.i(TAG, "drawTimeLineIndicatorVolumeRatio: 五日下量比暂不支持"+getVolumeRatioPreText());
            return;
        } else {
            drawIndicatorReferenceLine(canvas, index, true);
            drawIndicatorReferenceLine(canvas, index, false);
        }

        final SecQuote quote = mListener.mQuote;
        TimeLineData timeLineData = mTimeLineDataCache.getTimeLineData(0);
        if (/*getNowPrice() == 0 || */mListener.getYesterdayClose() == 0
                || timeLineData == null
                || timeLineData.minValue == 0 || timeLineData.maxValue == 0
                || mListener.mTradingMinutes == 0 || quote == null) { //没有昨日收盘价，也就没有分时图的中线，此时不画
            return;
        }
        // 当前最新的量比，从quote里取得
        final float currentVolumnRatio = quote.fVolumeRatio;
        if (currentVolumnRatio <= 0f) {
            return;
        }

        final float[] volumeAvg = mTimeVolumeAverage;
        final int length = volumeAvg == null ? 0 : volumeAvg.length;
        if (length == 0) {
            return;
        }

        final Path path = mRes.mTimeCapitalDDZPathSmall;
        path.reset();

        final float day5AverageVolume = volumeAvg[length - 1] / currentVolumnRatio;
        final float maxVolumeAverage = mTimeMaxVolumeAverage;
        final float maxVolumeRatio = maxVolumeAverage / day5AverageVolume;
        final float minVolumeAverage = mTimeMinVolumeAverage;
        final float minVolumeRatio = minVolumeAverage / day5AverageVolume;
        final float itemWidth = mRes.mTimeHorizontalGap;
        float ratio;
        float startX = mRes.mKLineStrokeWidth / 2;
        for (int i = 0; i < length; i++) {
            ratio = volumeAvg[i] / day5AverageVolume;
            DtLog.d(TAG, "ratio = " + ratio);
            if (i == 0) {
                path.moveTo(startX, getVolumeRatioHeight(ratio, maxVolumeRatio, minVolumeRatio, indicatorBottom));
            } else {
                path.lineTo(startX, getVolumeRatioHeight(ratio, maxVolumeRatio, minVolumeRatio, indicatorBottom));
            }

            startX += itemWidth;
        }

        final Paint paint = mRes.mPaintLine;
        paint.setColor(mRes.mTimeLineColor);
        canvas.drawPath(path, paint);
    }

    private float getVolumeRatioHeight(final float value, final float maxValue, final float minValue, final float indicatorBottom) {
        return indicatorBottom - mRes.mIndicatorRectHeight * 0.9f * (value - minValue) / (maxValue - minValue);
    }

    private void drawTimeTouchLine(Canvas canvas) {
        if (!mIsTouching) {
            return;
        }

        if (mTouchX == LineChartTextureView.INVALID_TOUCH_X) {
            return;
        }

        if (!hasOriginalTimeData()) {
            return;
        }
        List<TrendDesc> timeEntities = mTimeLineDataCache.getTimeLineEntities(0);
//                DtLog.d(TAG, "drawTimeTouchLine: mTouchX = " + mTouchX);
        //先是触摸位置超过有值区域的处理
        final Paint paint = mRes.mPaintTouchLine;
        paint.setStyle(Paint.Style.STROKE);
        float touchLineHeight;
        int index = -1;
        int strokeWidth = mStrokeWidth;
        float startX = strokeWidth / 2;

        if (mTouchX < 0) {
            canvas.drawLine(startX, 0, startX, mDrawingHeight, paint);
            touchLineHeight = getPriceDrawingHeight(timeEntities.get(0).getFNow());
            updateEntraceRect((int) startX);
            canvas.drawLine(0, touchLineHeight, mDrawingWidth, touchLineHeight, paint);
            index = 0;
        } else if (mTouchX >= startX + mRes.mTimeHorizontalGap * (timeEntities.size() - 1)) {
            float x = startX + mRes.mTimeHorizontalGap * (timeEntities.size() - 1)/*mDrawingWidth - strokeWidth / 2*/;
            updateEntraceRect((int) x);
            canvas.drawLine(x, 0, x, mDrawingHeight, paint);
            touchLineHeight = getPriceDrawingHeight(timeEntities.get(timeEntities.size() - 1).getFNow());
            canvas.drawLine(0, touchLineHeight, mDrawingWidth, touchLineHeight, paint);
            index = timeEntities.size() - 1;
        } else {
            float touchPositionValue;
            for (int i = 0; i < timeEntities.size() - 1; i++) {
                //取第i个点的数值
                TrendDesc entity = timeEntities.get(i);
                if(entity == null) {
                    continue;
                }
                float value = entity.getFNow();

                //得出当前触摸线的位置对应的值
                if (mTouchX != LineChartTextureView.INVALID_TOUCH_X) {
                    if (mTouchX >= startX && mTouchX < startX + mRes.mTimeHorizontalGap) {
                        touchPositionValue = value;
                        touchLineHeight = getPriceDrawingHeight(touchPositionValue);
                        updateEntraceRect((int) startX);
                        canvas.drawLine(startX, 0, startX, mDrawingHeight, paint);
                        canvas.drawLine(0, touchLineHeight, mDrawingWidth, touchLineHeight, paint);
                        index = i;
                        break;
                    }
                }

                startX += mRes.mTimeHorizontalGap;
            }
        }

        for (int i = 0; i < mIndicatorNum; i++) {
            drawTimeLineIndicatorValues(canvas, index, i);
        }
    }

    private void drawCornerValues(Canvas canvas) {
        TimeLineData data0 = mTimeLineDataCache.getTimeLineData(0);
        if(data0 != null && data0.check()) {
            float maxUpRate = mTimeLineDataCache.computeMaxUpRate();
            float minUpRate = mTimeLineDataCache.computeMinUpRate();
            float upLeft = data0.getClose() * (1 + maxUpRate);
            float downLeft = data0.getClose() * (1 + minUpRate);
            float upRight = maxUpRate;
            float downRight = minUpRate;

            mRes.mKLineTextPaint.setTextSize(mRes.mTextSize);
            mRes.mKLineTextPaint.setTypeface(TextDrawer.getTypeface());

            float x = 0;
            float y = mRes.mTextHeight;
            mRes.mKLineTextPaint.setColor(mRes.mKColorRed);
            canvas.drawText(StringUtil.getFormattedFloat(upLeft, mListener.mTpFlag), x, y, mRes.mKLineTextPaint);

            x = 0;
            y = mChartContentHeight / 2 + (mRes.mTextHeight - mRes.mTextBottom) / 2;
            mRes.mKLineTextPaint.setColor(mRes.mDefaultGrayTextColor);
            canvas.drawText(StringUtil.getFormattedFloat((upLeft + downLeft) / 2, mListener.mTpFlag), x, y, mRes.mKLineTextPaint);

            x = 0;
            y = mChartContentHeight - mRes.mTextBottom;
            mRes.mKLineTextPaint.setColor(mRes.mKColorGreen);
            canvas.drawText(StringUtil.getFormattedFloat(downLeft, mListener.mTpFlag), x, y, mRes.mKLineTextPaint);

            String text = StringUtil.getPercentString(upRight);
            int textWidth = mRes.mTextDrawer.measureSingleTextWidth(text, mRes.mTextSize, TextDrawer.getTypeface());
            x = mDrawingWidth - textWidth;
            y = mRes.mTextHeight;
            mRes.mKLineTextPaint.setColor(mRes.mKColorRed);
            canvas.drawText(text, x, y, mRes.mKLineTextPaint);

            text = StringUtil.getPercentString(downRight);
            textWidth = mRes.mTextDrawer.measureSingleTextWidth(text, mRes.mTextSize, TextDrawer.getTypeface());
            x = mDrawingWidth - textWidth;
            y = mChartContentHeight - mRes.mTextBottom;
            mRes.mKLineTextPaint.setColor(mRes.mKColorGreen);
            canvas.drawText(text, x, y, mRes.mKLineTextPaint);
        }
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

    private void drawFiveDayTradingDate(Canvas canvas) {
        ArrayList<RtMinDesc> rtMinDescs = mListener.mRtMinDescs;
        if(rtMinDescs != null && !rtMinDescs.isEmpty()) {
            mRes.mKLineTextPaint.setColor(mRes.mKColorGray);
            mRes.mKLineTextPaint.setTypeface(TextDrawer.getTypeface());
            int textWidth = 0;
            float left = 0;
            float x = 0;
            float y = mChartContentHeight + mRes.mTextHeight;
            int size = rtMinDescs.size();
            float dayWidth = mDrawingWidth / LineChartTextureView.FIVE_DAY_COUNT;
            for(int i=size-1; i>=0; i--) {
                RtMinDesc rtMinDesc = rtMinDescs.get(i);
                String date = adaptDateText(rtMinDesc.getSDate());
                textWidth = mRes.mTextDrawer.measureSingleTextWidth(date, mRes.mTextSize, TextDrawer.getTypeface());
                x = left + dayWidth / 2 - textWidth / 2;
                canvas.drawText(date, x, y, mRes.mKLineTextPaint);
                left += dayWidth;
            }
        }
    }

    private String adaptDateText(String date) {
        if(date != null && date.length() >= 8) {
            return date.substring(4, 6) + "-" + date.substring(6, 8);
        }
        return date;
    }

    private float getPriceDrawingHeight(float value) {
        TimeLineData timeLineData = mTimeLineDataCache.getTimeLineData(0);
        if(timeLineData != null) {
            return getPriceDrawingHeight(value, timeLineData);
        }
        return 0f;
    }

    private float getPriceDrawingHeight(float value, TimeLineData timeLineData) {
        float y = 0;
        float deltaV = 0;
        float deltaY = 0;

        float close = timeLineData.getClose();
        float max = timeLineData.maxValue;
        float min = timeLineData.minValue;
        float totalMaxRate = mTimeLineDataCache.getMaxUpRate();
        float totalMinRate = Math.abs(mTimeLineDataCache.getMinDownRate());

        float contentHeight;

        if (value > close) {
            float maxRate = (max - close) / close;
            contentHeight = maxRate < totalMaxRate ? mChartContentHeight * maxRate / totalMaxRate : mChartContentHeight;
            deltaV = value - close;
            deltaY = deltaV * contentHeight / 2 / (max - close) * TIME_LINE_HEIGHT_RATIO;
            y = mChartContentHeight / 2 - deltaY;
        } else if (value < close) {
            float minRate = (close - min) / close;
            contentHeight = minRate < totalMinRate ? mChartContentHeight * minRate / totalMinRate : mChartContentHeight;
            deltaV = close - value;
            deltaY = deltaV * contentHeight / 2 / (close - min) * TIME_LINE_HEIGHT_RATIO;
            y = mChartContentHeight / 2 + deltaY;
        } else {
            y = mChartContentHeight / 2;
        }
        //            DtLog.d("liqf", "now price drawing height is " + y);
        return y;
    }

    private float getKLineCapitalFlowDrawingHeight(float value, int indicatorBottom) {
        float maxval = mKLineMaxCapitalDDZ;
        float minval = mKLineMinCapitalDDZ;
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float height = bottomLimit + ((maxval - value) / (maxval - minval)) * bottomHeight;
        return height;
    }

    private float getKLineDrawingHeight(float value) { //上下各保留等量的空白，同时整体向下偏移mKLineAverageTextAreaHeight以便在顶部画出均线值
        float y = 0;
        if (mKLineMin != mKLineMax) {
            y = KLINE_MAX_HEIGHT_RATIO * mChartContentHeight
                    + (1 - 2 * KLINE_MAX_HEIGHT_RATIO) * (mChartContentHeight - mRes.mKLineAverageTextAreaHeight)
                    * (value - mKLineMin) / (mKLineMax - mKLineMin);
        } else {
            y = mChartContentHeight / 2;
        }
        return y;
    }

    private float getAmountDrawingHeight(float amount) {
        return amount / mTimeAmountMax * mRes.mIndicatorRectHeight * AMOUNT_MAX_HEIGHT_RATIO;
    }

    private float getCapitalDDZDrawingHeight(float value, float indicatorBottom) {
        float maxval = mTimeCapitalDDZMax;
        float minval = mTimeCapitalDDZMin;
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float height = bottomLimit + ((maxval - value) / (maxval - minval)) * bottomHeight;
        return height;
    }
   private float getFiveDayCapitalDDZDrawingHeight(float value, float indicatorBottom) {
        float maxval = mTimeFiveDayCapitalDDZMax;
        float minval = mTimeFiveDayCapitalDDZMin;
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float height = bottomLimit + ((maxval - value) / (maxval - minval)) * bottomHeight;
        return height;
    }

    private float getKLineVolumeDrawingHeight(float amount) {
        return amount / mKLineVolumeMax * mRes.mIndicatorRectHeight * AMOUNT_MAX_HEIGHT_RATIO;
    }

    private void drawKChart(Canvas canvas) {
        if (mTimeValueChangeListener != null) {
            mTimeValueChangeListener.onValuePositionChanged(null);
        }

        if (mKLineEntities == null || mKLineEntities.size() == 0) {
            return;
        }

        //在顶部画均线文字标注
        if (mTouchX == LineChartTextureView.INVALID_TOUCH_X) {
            drawKLineAverageText(canvas, -1);
        }

        if (!mListener.isMinuteKLineType()) {//绘制Y轴上的竖虚线，包括文字哦
            drawHorizontalCoordinates(canvas);
        }
        drawVerticalCoordinates(canvas);

        int entityCount = mKLineEnd - mKLineStart;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;
        float startX = mRes.mKLineStrokeWidth / 2;

        final HashMap<String, SecBsInfo> bsInfos = mBSInfos;
        boolean hasBS = false;
        boolean hasLastBS = false; // 是否有最后一个k线的bs点，判断是否需要不停做动画
        for (int i = 0; i < entityCount; i++) {
            int index = i + mKLineStart;
            KLineDesc entity = mKLineEntities.get(index);

            float begin = entity.getFOpen();
            float end = entity.getFClose();
            float high = entity.getFHigh();
            float low = entity.getFLow();

            float beginHeight = getKLineDrawingHeight(begin);
            float endHeight = getKLineDrawingHeight(end);
            float highHeight = getKLineDrawingHeight(high);
            float lowHeight = getKLineDrawingHeight(low);

            mRes.mPaintLine.setStrokeWidth(mRes.mKLineStrokeWidth);
            if (end > begin) {
                mRes.mPaintKLine.setStyle(getKLineStyleRiseLine());
                mRes.mPaintKLine.setStrokeWidth(mRes.mKLineStrokeWidth);
                mRes.mPaintKLine.setColor(mRes.mKColorRed);
                mRes.mPaintLine.setColor(mRes.mKColorRed);
            } else if (end == begin) {
                float yesterdayEnd = 0;
                if (i > 0) {
                    yesterdayEnd = mKLineEntities.get(i + mKLineStart - 1).getFClose();
                }
                if (end < yesterdayEnd) {
                    mRes.mPaintKLine.setStyle(Paint.Style.FILL_AND_STROKE);
                    mRes.mPaintKLine.setColor(mRes.mKColorGreen);
                    mRes.mPaintLine.setColor(mRes.mKColorGreen);
                } else {
                    mRes.mPaintKLine.setStyle(getKLineStyleRiseLine());
                    mRes.mPaintKLine.setStrokeWidth(mRes.mKLineStrokeWidth);
                    mRes.mPaintKLine.setColor(mRes.mKColorRed);
                    mRes.mPaintLine.setColor(mRes.mKColorRed);
                }
            } else {
                mRes.mPaintKLine.setStyle(Paint.Style.FILL_AND_STROKE);
                mRes.mPaintKLine.setColor(mRes.mKColorGreen);
                mRes.mPaintLine.setColor(mRes.mKColorGreen);
            }

            //画K线的均线
            float averageStartX = startX + pillarWidth / 2;
            if (mKlineSettingConfigure.isMAOpen){
                if (i + 1 < entityCount) {
                    if (i + mKLineStart + 1 >= MA_1) {
                        mRes.mPaintLine.setColor(mRes.mKLineAverageColor1);
                        canvas.drawLine(averageStartX, getKLineDrawingHeight(mKLineAverage1[i]), averageStartX + itemWidth,
                                getKLineDrawingHeight(mKLineAverage1[i + 1]), mRes.mPaintLine);
                    }
                    if (i + mKLineStart + 1 >= MA_2) {
                        mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
                        canvas.drawLine(averageStartX, getKLineDrawingHeight(mKLineAverage2[i]), averageStartX + itemWidth, getKLineDrawingHeight(mKLineAverage2[i + 1]), mRes.mPaintLine);
                    }
                    if (i + mKLineStart + 1 >= MA_3) {
                        mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
                        canvas.drawLine(averageStartX, getKLineDrawingHeight(mKLineAverage3[i]), averageStartX + itemWidth, getKLineDrawingHeight(mKLineAverage3[i + 1]), mRes.mPaintLine);
                    }
                    if (i + mKLineStart + 1 >= MA_4 && MA_4 != 0) {
                        mRes.mPaintLine.setColor(mRes.mKLineAverageColor4);
                        canvas.drawLine(averageStartX, getKLineDrawingHeight(mKLineAverage4[i]), averageStartX + itemWidth, getKLineDrawingHeight(mKLineAverage4[i + 1]), mRes.mPaintLine);
                    }
                    if (i + mKLineStart + 1 >= MA_5 && MA_5 != 0) {
                        mRes.mPaintLine.setColor(mRes.mKLineAverageColor5);
                        canvas.drawLine(averageStartX, getKLineDrawingHeight(mKLineAverage5[i]), averageStartX + itemWidth, getKLineDrawingHeight(mKLineAverage5[i + 1]), mRes.mPaintLine);
                    }
                    if (i + mKLineStart + 1 >= MA_6 && MA_6 != 0) {
                        mRes.mPaintLine.setColor(mRes.mKLineAverageColor6);
                        canvas.drawLine(averageStartX, getKLineDrawingHeight(mKLineAverage6[i]), averageStartX + itemWidth, getKLineDrawingHeight(mKLineAverage6[i + 1]), mRes.mPaintLine);
                    }
                }
            }


            //画K线红绿柱子
            float left = startX;
            float right = startX + pillarWidth;
            canvas.drawRect(left, endHeight, right, beginHeight, mRes.mPaintKLine);

            //画上下影线
            float max = Math.max(beginHeight, endHeight);
            float min = Math.min(beginHeight, endHeight);
            float lineStartX = startX + pillarWidth / 2;
            if (end >= begin) {
                canvas.drawLine(lineStartX, lowHeight, lineStartX, max, mRes.mPaintKLine);
                canvas.drawLine(lineStartX, min, lineStartX, highHeight, mRes.mPaintKLine);
            } else {
                canvas.drawLine(lineStartX, lowHeight, lineStartX, highHeight, mRes.mPaintKLine);
            }

//            if (mListener.mLineType == LineChartTextureView.TYPE_DAILY_K && mKlineSettingConfigure.isDKOpen) {
//                if (bsInfos != null) {
//                    final String date = entity.lYmd + " 00:00:00"; // 潜规则
//                    final SecBsInfo bsInfo = bsInfos.get(date);
//                    if (bsInfo != null) {
//                        if (i == entityCount - 1) { // 最后一个bs点特殊处理，不停做动画
//                            drawBSPoint(canvas, bsInfo.iBs, lineStartX, highHeight, lowHeight, mBSAnimOffset);
//                            hasLastBS = true;
//                        } else if (mAnimatorCount < 3) { // 3次以内都需要做动画
//                            drawBSPoint(canvas, bsInfo.iBs, lineStartX, highHeight, lowHeight, mBSAnimOffset);
//                        } else { // 停止做动画，在起始点绘制bs点
//                            drawBSPoint(canvas, bsInfo.iBs, lineStartX, highHeight, lowHeight, mRes.mBSMinGap);
//                        }
//                        hasBS = true;
//                    }
//                }
//            }

            //移动到下一个格子起点
            startX += itemWidth;
        }

        //画K线指标
        for (int i = 0; i < mIndicatorNum; i++) {
            drawKLineTradingIndicators(canvas, i);
        }

        drawKTouchLine(canvas);
        //画最大最小值的指示框
        drawKLineMinMaxIndicators(canvas);

        drawEntrance(canvas, "查看历史分时");

        drawKLineGap(canvas);//绘制缺口

        drawMagicNineIndicator(canvas);//绘制神奇九转

        drawBullBearIndicator(canvas);

        drawDKIndicator(canvas);

        DtLog.d(TAG, "canvas drawKChart complete");

    }

    private Rect mEntranceRect = new Rect();

    boolean isTouchEntrance(int x, int y) {
        if(supportEntrance()) {
            Rect rect = mEntranceRect;
            return x >= rect.left - mRes.mEntranceTouchExpand
                    && x <= rect.right + mRes.mEntranceTouchExpand
                    && y>= rect.top - mRes.mEntranceTouchExpand
                    && y <= rect.bottom + mRes.mEntranceTouchExpand;
        }
        return false;
    }

    private boolean supportEntrance() {
        return mIsTouching && mListener != null && mListener.supportEntrance();
    }

    private void updateEntraceRect(int x) {
        if(supportEntrance()) {
            final int entranceWidth = mRes.mEntranceWidth;
            final int entranceHeight = mRes.mEntranceHeight;
            final int finalX;
            if(x - entranceWidth / 2 < 0) {
                finalX = 0;
            } else if(x + entranceWidth / 2 > mDrawingWidth) {
                finalX = mDrawingWidth - entranceWidth;
            } else {
                finalX = x - entranceWidth / 2;
            }
            mEntranceRect.left = finalX;
            mEntranceRect.top = mRes.mKLineAverageTextAreaHeight;
            mEntranceRect.right = finalX + entranceWidth;
            mEntranceRect.bottom = mRes.mKLineAverageTextAreaHeight + entranceHeight;
        }
    }

    private void drawDKIndicator(Canvas canvas) {
        if (null == emaBuy || emaBuy.length != mKlineCount || null == emaSell || emaSell.length != mKlineCount) {
            emaBuy = new double[mKlineCount];//买线,计算结果有3个数无效
            core.ema(0, mKlineCount - 1, mCloses, K_DK_POINT_1, outBegIdx, outNBElement, emaBuy);

            double[] slopeValue = new double[mKlineCount];//slope值，计算结果19个数无效
            core.linearRegSlope(0, mKlineCount - 1, mCloses, K_DK_POINT_2, outBegIdx, outNBElement, slopeValue);
            emaSell = new double[mKlineCount];
            double[] tempClose = new double[mKlineCount];
            for (int i = 0 ; i < mKlineCount ; i++) {
                tempClose[i] = slopeValue[(i + mKlineCount - K_DK_POINT_2 + 1) % mKlineCount] * K_DK_POINT_3
                        + mKLineEntities.get(i).getFClose();
            }
            core.ema(0, mKlineCount - 1, tempClose, K_DK_POINT_4, outBegIdx, outNBElement, emaSell);
            DtLog.e(TAG, "calculateDkIndicator");
        }

        float itemWidth = getKLineItemWidth();
        float startX = mRes.mKLineStrokeWidth / 2;
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;
        boolean hasBS = false, hasLastBS = false;
        for (int i = mKLineStart; i < mKLineEnd; i++) {
            if (i < K_DK_POINT_4 - 1) {
                startX += itemWidth;
                continue;
            }
            float lineStartX = startX + pillarWidth / 2;
            float highHeight = getKLineDrawingHeight(mKLineEntities.get(i).getFHigh());
            float lowHeight = getKLineDrawingHeight(mKLineEntities.get(i).getFLow());
            int dkStatus = -1;

            int buyPreIndex = ((i - 1) + mKlineCount - K_DK_POINT_1 + 1) % mKlineCount;
            int buyCurrentIndex = (i + mKlineCount - K_DK_POINT_1 + 1) % mKlineCount;
            int sellPreIndex = ((i - 1) + mKlineCount - K_DK_POINT_4 + 1) % mKlineCount;
            int sellCurrentIndex = (i + mKlineCount - K_DK_POINT_4 + 1) % mKlineCount;

            boolean isEMABuy = emaBuy[buyPreIndex] < emaSell[sellPreIndex]
                    && emaBuy[buyCurrentIndex] > emaSell[sellCurrentIndex];

            boolean isEMASell = emaBuy[buyPreIndex] > emaSell[sellPreIndex]
                    && emaBuy[buyCurrentIndex] < emaSell[sellCurrentIndex];
            if (isEMABuy) {
                dkStatus = 1;
            }else if (isEMASell) {
                dkStatus = 0;
            }
            if (dkStatus != -1) {
                if (i == mKlineCount - 1) { // 最后一个bs点特殊处理，不停做动画
                    hasLastBS = true;
                    drawBSPoint(canvas, dkStatus, lineStartX, highHeight, lowHeight, mBSAnimOffset);
                } else if (mAnimatorCount < 3) { // 3次以内都需要做动画
                    drawBSPoint(canvas, dkStatus, lineStartX, highHeight, lowHeight, mBSAnimOffset);
                } else { // 停止做动画，在起始点绘制bs点
                    drawBSPoint(canvas, dkStatus, lineStartX, highHeight, lowHeight, mRes.mBSMinGap);
                }
                hasBS = true;
            }
            startX += itemWidth;
        }
        processBSAnimator(hasBS, hasLastBS);
    }

    private void drawBullBearIndicator(Canvas canvas){
        if (!isSupportBullBear()){
            return;
        }
        if (null == mBullBearEntities || mBullBearEntities.isEmpty() || mBullBearEntities.size() != mKlineCount){
            mBullBearEntities = new ArrayList<>();
            for (int i = 0 ; i < mKlineCount ; i++){
                if (i < K_BULL_BEAR_PERIOD){
                    mBullBearEntities.add(new BullBear(-1));
                    continue;
                }
                float end = mKLineEntities.get(i).getFClose();
                double currentMa60 = ma60s[(i + mKlineCount - K_BULL_BEAR_PERIOD + 1) % mKlineCount];
                double preMa60 = ma60s[(i + mKlineCount - K_BULL_BEAR_PERIOD + 1) % mKlineCount - 1];
                if (end >= currentMa60 && currentMa60 >= preMa60 && mStandardMacdEntitys.get(i).getDif() >= 0){
                    //牛
                    mBullBearEntities.add(new BullBear(1));
                }else {
                    //熊
                    mBullBearEntities.add(new BullBear(0));
                }
            }
        }
        float startX = mRes.mKLineStrokeWidth / 2;
        float itemWidth = getKLineItemWidth();
        int entityCount = mKLineEnd - mKLineStart;
        for (int i = 0; i < entityCount; i++) {
            int index = i + mKLineStart;
            RectF rect = new RectF(startX - mRes.mKLineStrokeWidth / 2, 0,
                    itemWidth + startX - mRes.mKLineStrokeWidth / 2, mChartContentHeight);
            BullBear currentBullBear = mBullBearEntities.get(index);

            if (index != 0 && index != mKlineCount - 1){
                BullBear preBullBear = mBullBearEntities.get(index - 1);
                BullBear nextBullBear = mBullBearEntities.get(index + 1);
                if (currentBullBear.getBullBear() != preBullBear.getBullBear() &&
                        currentBullBear.getBullBear() != nextBullBear.getBullBear()){
                    currentBullBear.setBullBear(nextBullBear.getBullBear());
                }
            }
            mRes.mPaintKLine.setColor(getBullBearColor(currentBullBear.getBullBear()));
            canvas.drawRect(rect, mRes.mPaintKLine);
            startX += itemWidth;
        }
    }

    private boolean isSupportBullBear(){
        return (mListener.getLineType() != LineChartTextureView.TYPE_TIME
                || mListener.getLineType() != LineChartTextureView.TYPE_FIVE_DAY)
                && mKlineSettingConfigure.isBullBearOpen
                && mKlineCount > K_BULL_BEAR_PERIOD
                && StockUtil.supportBullbearPoint(mListener.mDtSecCode);
    }

    private int getBullBearColor(int bullBear) {
        if (bullBear == 1){
            return mRes.mKColorAlphaRed;
        }else if (bullBear == 0){
            return mRes.mKColorAlphaGreen;
        }else {
            return Color.TRANSPARENT;
        }
    }

    private void drawMagicNineIndicator(Canvas canvas) {
        if (!isSupportMagicNine())
            return;

        float itemWidth = getKLineItemWidth();
        if (itemWidth + 2 < mRes.mPaintKLine.measureText(String.valueOf(MagicNine.NINE))){//设计说可以多2个像素
            return;
        }//设计说9为最宽的数字

        int count = mKLineEntities.size();
        if (null == mMagicNineEntities || mMagicNineEntities.isEmpty() || mMagicNineEntities.size() != count){
            mMagicNineEntities = new ArrayList<>();
            for (int i = 0 ; i < count ; i++){
                if (i < K_MAGIC_NINE){
                    mMagicNineEntities.add(new MagicNine(false));
                    continue;
                }
                MagicNine magicNine = new MagicNine();
                MagicNine preMagicNine = mMagicNineEntities.get(mMagicNineEntities.size() - 1);
                KLineDesc preDesc = mKLineEntities.get(i - K_MAGIC_NINE);
                KLineDesc desc = mKLineEntities.get(i);
                magicNine.setStatus(desc.getFClose() >= preDesc.getFClose() ? 1 : -1);
                if (magicNine.getStatus() == preMagicNine.getStatus()){
                    magicNine.setNumber(preMagicNine.getNumber() + 1);
                    if (magicNine.getNumber() == MagicNine.NINE){
                        magicNine.setEnable(true);
                        for (int j = 0 ; j < MagicNine.NINE - 1 ; j++){
                            mMagicNineEntities.get(mMagicNineEntities.size() - 1 - j).setEnable(true);
                        }
                    }
                }else {
                    magicNine.setNumber(1);
                }
                mMagicNineEntities.add(magicNine);
            }

            for (int i = mMagicNineEntities.size() - 1 ; i >= 0 ; i--){//向前查找最近的连续数字
                MagicNine magicNine = mMagicNineEntities.get(i);
                if (magicNine.getNumber() > MagicNine.NINE){
                    magicNine.setEnable(false);
                    break;
                }
                magicNine.setEnable(true);
                MagicNine preMagicNine = mMagicNineEntities.get(i - 1);
                if (magicNine.getStatus() == preMagicNine.getStatus()){
                    preMagicNine.setEnable(true);
                }else{
                    break;
                }
            }
        }

        int entityCount = mKLineEnd - mKLineStart;

        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;
        float startX = mRes.mKLineStrokeWidth / 2;
        for (int i = 0; i < entityCount; i++) {
            KLineDesc desc = mKLineEntities.get(i + mKLineStart);
            MagicNine magicNine = mMagicNineEntities.get(i + mKLineStart);
            if (!magicNine.isEnable() || magicNine.getNumber() > 9){// ||
                startX += itemWidth;
                continue;
            }
            float high = desc.getFHigh();
            float low = desc.getFLow();
            float highHeight = getKLineDrawingHeight(high);
            float lowHeight = getKLineDrawingHeight(low);
            String number = String.valueOf(magicNine.getNumber());
            Rect rect = new Rect();
            mRes.mPaintKLine.getTextBounds(number,0,number.length(), rect);
            float with = rect.width();
            if (magicNine.getStatus() > 0){
                mRes.mPaintKLine.setColor(mRes.mKColorRed);
                canvas.drawText(number, startX + pillarWidth / 2 - with/2f, lowHeight + 10 + rect.height(), mRes.mPaintKLine);
                if (number.equals(String.valueOf(MagicNine.NINE))){//绘制压力线
                    mRes.mPaintDashPressureLine.setColor(mRes.mKColorGreen);
                    canvas.drawLine(startX + pillarWidth / 2, highHeight, mDrawingWidth, highHeight, mRes.mPaintDashPressureLine);
                }
            }else{
                mRes.mPaintKLine.setColor(mRes.mKColorGreen);//考虑到字体的高度
                canvas.drawText(number, startX + pillarWidth / 2 - with/2f, highHeight - 10, mRes.mPaintKLine);
                if (number.equals(String.valueOf(MagicNine.NINE))){//绘制支撑线
                    mRes.mPaintDashPressureLine.setColor(mRes.mKColorRed);
                    canvas.drawLine(startX + pillarWidth / 2, lowHeight, mDrawingWidth, lowHeight, mRes.mPaintDashPressureLine);
                }
            }
            startX += itemWidth;
        }
    }

    private boolean isSupportMagicNine(){
        return mKLineEntities.size() > K_MAGIC_NINE && mListener.getLineType() == LineChartTextureView.TYPE_DAILY_K
                && mKlineSettingConfigure.isMagicNineOpen && StockUtil.isAStock(mListener.mDtSecCode);
    }

    private void drawKLineGap(Canvas canvas){
        if (!mKlineSettingConfigure.isGapOpen || mKLineEntities.size() <= 1){
            return;
        }
        ArrayList<Gap> gaps = new ArrayList<>();
        float currentHighPrice = mKLineEntities.get(mKLineEntities.size() - 1).getFHigh();
        float currentLowPrice = mKLineEntities.get(mKLineEntities.size() - 1).getFLow();
        for (int i = mKLineStart + 1; i < mKLineEnd ; i++){
            KLineDesc preDesc = mKLineEntities.get(i - 1);
            KLineDesc desc = mKLineEntities.get(i);

            if (desc.getFLow() > preDesc.getFHigh()){//高开产生一个缺口
                gaps.add(new Gap(i - 1, desc.getFLow(), preDesc.getFHigh()));
            }

            if (desc.getFHigh() < preDesc.getFLow()){//低开产生一个缺口
                gaps.add(new Gap(i - 1, preDesc.getFLow(), desc.getFHigh()));
            }

            for (int j = 0 ; j < gaps.size() ; j++){
                Gap gap = gaps.get(j);
                if (desc.getFHigh() >= gap.getHigh() && desc.getFLow() <= gap.getLow()){//全部填满缺口
                    gap.setEnable(false);
                }else if (desc.getFLow() > gap.getLow() && desc.getFLow() < gap.getHigh()){//最低价填补缺口
                    gap.setHigh(desc.getFLow());
                }else if (desc.getFHigh() > gap.getLow() && desc.getFHigh() < gap.getHigh()){//最高价填补缺口
                    gap.setLow(desc.getFHigh());
                }
            }
        }

        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;
        Gap upGap = new Gap();
        Gap lowGap = new Gap();
        for (int i = 0 ; i < gaps.size(); i++){
            Gap gap = gaps.get(i);
            if (gap.isEnable()){
                if (gap.getLow() >= currentHighPrice && gap.getIndex() > upGap.getIndex()){
                    upGap = gap;
                }else if (gap.getHigh() <= currentLowPrice && gap.getIndex() > lowGap.getIndex()){
                    lowGap = gap;
                }
            }
        }
        if (upGap.getIndex() > 0){
            canvas.drawRect((upGap.getIndex() - mKLineStart)*itemWidth + mRes.mKLineStrokeWidth / 2 + pillarWidth / 2,
                    getKLineDrawingHeight(upGap.getHigh()), mDrawingWidth, getKLineDrawingHeight(upGap.getLow()), mRes.mFillPaint);
        }
        if (lowGap.getIndex() > 0){
            canvas.drawRect((lowGap.getIndex() - mKLineStart)*itemWidth + mRes.mKLineStrokeWidth / 2 + pillarWidth / 2,
                    getKLineDrawingHeight(lowGap.getHigh()), mDrawingWidth, getKLineDrawingHeight(lowGap.getLow()), mRes.mFillPaint);
        }
    }

    private void drawEntrance(Canvas canvas, String text) {
        if(supportEntrance()) {
            final int entranceHeight = mRes.mEntranceHeight;
            Paint paint = mRes.mPaintEntrance;
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mRes.mEntranceBgColor);

            float radius = entranceHeight / 2f;
            RectF rectF = new RectF();
            rectF.left = mEntranceRect.left;
            rectF.top = mEntranceRect.top;
            rectF.right = mEntranceRect.left + 2 * radius;
            rectF.bottom = mEntranceRect.bottom;
            canvas.drawArc(rectF, 90, 180, false, paint);

            rectF.left = mEntranceRect.right - 2 * radius;
            rectF.top = mEntranceRect.top;
            rectF.right = mEntranceRect.right;
            rectF.bottom = mEntranceRect.bottom;
            canvas.drawArc(rectF, -90, 180, false, paint);

            canvas.drawRect(mEntranceRect.left + radius, mEntranceRect.top,  mEntranceRect.right - radius, mEntranceRect.bottom, paint);
            paint.setTextSize(mRes.mEntranceTextSize);

            float textWidth = paint.measureText(text);
            float textLeft = mEntranceRect.left + (mEntranceRect.right - mEntranceRect.left - textWidth) / 2;
            float textTop = mEntranceRect.bottom - (mEntranceRect.bottom - mEntranceRect.top - mRes.mEntranceTextHeight + mRes.mEntranceTextBottom) / 2;
            paint.setColor(mRes.mEntranceTextColor);
            canvas.drawText(text, textLeft, textTop, paint);
        }
    }

    private int mAnimatorCount = 0;
    private Handler mUIHandler;

    private void processBSAnimator(final boolean hasBS, final boolean hasLastBS) {
        if (mUIHandler == null) {
            mUIHandler = new Handler(Looper.getMainLooper(), new BSAnimatorManager());
        }
        // 主线程处理，防止出现java.lang.Throwable: Explicit termination method 'dispose' not called
        mUIHandler.obtainMessage(0, hasBS ? 1 : 0, hasLastBS ? 1 : 0).sendToTarget();
    }

    final class BSAnimatorManager implements Handler.Callback, Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            mBSAnimOffset = (Float) valueAnimator.getAnimatedValue();
        }

        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            mBSAnimatorState.set(BS_ANIMATOR_STATE_END);
            if (animator instanceof ValueAnimator) {
                final ValueAnimator valueAnimator = (ValueAnimator) animator;
                if (valueAnimator != null) {
                    valueAnimator.removeAllUpdateListeners();
                    valueAnimator.removeAllListeners();
                }
            }
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            mBSAnimatorState.set(BS_ANIMATOR_STATE_END);
            if (animator instanceof ValueAnimator) {
                final ValueAnimator valueAnimator = (ValueAnimator) animator;
                if (valueAnimator != null) {
                    valueAnimator.removeAllUpdateListeners();
                    valueAnimator.removeAllListeners();
                }
            }
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            mAnimatorCount++;
        }

        @Override
        public boolean handleMessage(Message msg) {
            final boolean hasBS = msg.arg1 == 1;
            final boolean hasLastBS = msg.arg2 == 1;

            if (hasBS && mBSAnimatorState.get() == BS_ANIMATOR_STATE_NOT_INIT) {
                mBSAnimatorState.set(BS_ANIMATOR_STATE_READY);
            }
            switch (mBSAnimatorState.get()) {
                case BS_ANIMATOR_STATE_ANIMATING:
                    requestRenderDelayed(35L);
                    break;
                case BS_ANIMATOR_STATE_READY:
                    final long delay = 800L;
                    ValueAnimator valueAnimator = mBSAnimator;
                    if (valueAnimator == null) {
                        if (mRes == null) {
                            return true;
                        }
                        valueAnimator = ValueAnimator.ofFloat(mRes.mBSMinGap, mRes.mBSMaxGap, mRes.mBSMinGap);
                        valueAnimator.setInterpolator(new LinearInterpolator());
                        valueAnimator.setRepeatCount(hasLastBS ? ValueAnimator.INFINITE : 3);
                        valueAnimator.setDuration(800L);
                        valueAnimator.setStartDelay(delay);
                        mBSAnimator = valueAnimator;
                    }
                    valueAnimator.addUpdateListener(this);
                    valueAnimator.addListener(this);
                    requestRenderDelayed(delay);
                    valueAnimator.start();
                    mBSAnimatorState.set(BS_ANIMATOR_STATE_ANIMATING);
                    break;
                case BS_ANIMATOR_STATE_NOT_INIT:
                case BS_ANIMATOR_STATE_END:
                    break;
            }
            return true;
        }
    }

    private void drawBSPoint(Canvas canvas, int bs, float lineStartX, float highHeight, float lowHeight, float offset) {
        // BS点
        switch (bs) {
            case 0: // 卖点
                canvas.drawBitmap(mRes.mSPointBitmap, lineStartX - mRes.mSOffsetX, highHeight - mRes.mSPointBitmap.getHeight() - offset, null);
                break;
            case 1: // 买点
                canvas.drawBitmap(mRes.mBPointBitmap, lineStartX - mRes.mBOffsetX, lowHeight + offset, null);
                break;
            default:
                break;
        }
    }

    private float getKLineItemWidth() {
        int entityCount = mKLineEnd - mKLineStart;
        int visibleCount = (int) (DEFAULT_KLINE_COUNT / mZoomRatio);
        float itemWidth;
        if (entityCount < visibleCount) {
            itemWidth = (float) mDrawingWidth / visibleCount;
        } else {
            itemWidth = (float) mDrawingWidth / entityCount;
        }

        return itemWidth;
    }

    private Paint.Style getKLineStyleRiseLine() {
        switch (mKlineSettingConfigure.klineStyle) {
            case KlineSettingConst.K_LINE_STYLE_RISE_LINE_FILL:
                return Paint.Style.FILL_AND_STROKE;
            case KlineSettingConst.K_LINE_STYLE_RISE_LINE_STROKE:
                return Paint.Style.STROKE;
            default:
                return Paint.Style.STROKE;
        }
    }

    /**
     *
     * 绘制技术指标
     * */
    private void drawKLineTradingIndicators(Canvas canvas, int index) {
        switch (mListener.mTradingIndicatorTypes[index]) {
            case SettingConst.K_LINE_INDICATOR_VOLUME:
                drawKLineIndicatorVolume(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_MACD:
                drawKLineIndicatorMACD(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_KDJ:
                drawKLineIndicatorKDJ(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_RSI:
                drawKLineIndicatorRSI(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_BOLL:
                drawKLineIndicatorBOLL(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW:
                drawKLineIndicatorCapitalFlow(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_DMI:
                drawKlineIndicatorDMI(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_CCI:
                drawKlineIndicatorCCI(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_ENE:
                drawKlineIndicatorENE(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_DMA:
                drawKlineIndicatorDMA(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_EXPMA:
                drawKlineIndicatorEXPMA(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_VR:
                drawKlineIndicatorVR(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_BBI:
                drawKLineIndicatorBBI(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_OBV:
                drawKlineIndicatorOBV(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_BIAS:
                drawKlineIndicatorBIAS(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_WR:
                drawKlineIndicatorWR(canvas, index);
                break;
            case SettingConst.K_LINE_INDICATOR_BREAK:
                drawKlineIndicatorBreak(canvas, index);
                break;
            default:
                break;
        }
    }

    private String getTimeLineIndicatorPreText(int indicatorType) {
        switch (indicatorType) {
            case SettingConst.TIME_LINE_INDICATOR_CAPITAL_DDZ:
                return getCapitalDDZPreText();
            case SettingConst.TIME_LINE_INDICATOR_VOLUME:
                return getVolumePreText();
            case SettingConst.TIME_LINE_INDICATOR_MACD:
                return getMACDPreText();
            default:
                return "";
        }
    }

    private String getKLineIndicatorPreText(int indicatorType) {
        switch (indicatorType) {
            case SettingConst.K_LINE_INDICATOR_VOLUME:
                return getVolumePreText();
            case SettingConst.K_LINE_INDICATOR_MACD:
                return getMACDPreText();
            case SettingConst.K_LINE_INDICATOR_KDJ:
                return getKDJPreText();
            case SettingConst.K_LINE_INDICATOR_RSI:
                return getRSIPreText();
            case SettingConst.K_LINE_INDICATOR_BOLL:
                return getBOLLPreText();
            case SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW:
                return getCapitalFlowPreText();
            case SettingConst.K_LINE_INDICATOR_DMI:
                return getDMIPreText();
            case SettingConst.K_LINE_INDICATOR_CCI:
                return getCCIPreText();
            case SettingConst.K_LINE_INDICATOR_ENE:
                return getENEPreText();
            case SettingConst.K_LINE_INDICATOR_DMA:
                return getDMAPreText();
            case SettingConst.K_LINE_INDICATOR_EXPMA:
                return getEXPMAPreText();
            case SettingConst.K_LINE_INDICATOR_VR:
                return getVRPreText();
            case SettingConst.K_LINE_INDICATOR_BBI:
                return getBBIPreText();
            case SettingConst.K_LINE_INDICATOR_OBV:
                return getOBVPreText();
            case SettingConst.K_LINE_INDICATOR_BIAS:
                return getBIASPreText();
            case SettingConst.K_LINE_INDICATOR_WR:
                return getWRPreText();
            case SettingConst.K_LINE_INDICATOR_BREAK:
                return getBreakPreText();
            default:
                return "";
        }
    }

    private String getVolumeRatioPreText() {
        return "量比";
    }

    private String getVolumePreText() {
        return "成交量";
    }

    private String getMACDPreText() {
        return "MACD(" + K_MACD_1 + "," + K_MACD_2 + "," + K_MACD_3 + ")";
    }

    private String getKDJPreText() {
        return "KDJ(" + K_KDJ_1 + "," + K_KDJ_2 + "," + K_KDJ_3 + ")";
    }

    private String getRSIPreText() {
        return "RSI(" + K_RSI_1 + "," + K_RSI_2 + "," + K_RSI_3 + ")";
    }

    private String getBOLLPreText() {
        return "BOLL(" + DEFAULT_BOLL_MA_COUNT + "," + DEFAULT_BOLL_FACTOR + ")";
    }

    private String getCapitalFlowPreText() {
        return "主力资金";
    }

    private String getBBIPreText(){
        return "BBI(" + K_BBI_1 + "," + K_BBI_2 + "," + K_BBI_3 + "," + K_BBI_4 + ")";
    }

    private String getCCIPreText(){
        return "CCI(" + K_CCI_1 + "," + K_CCI_2 + ")";
    }

    private String getENEPreText(){
        return "ENE(" +  K_ENE_1 + "," + K_ENE_2 + "," + K_ENE_3 + ")";
    }

    private String getDMAPreText(){
        return "DMA(" +  K_DMA_1 + "," + K_DMA_2 + "," + K_DMA_3 + ")";
    }

    private String getEXPMAPreText(){
        if (K_EXPMA_3 == 0 && K_EXPMA_4 == 0) {
            return "EXPMA(" +  K_EXPMA_1 + "," + K_EXPMA_2 + ")";
        }
        if (K_EXPMA_4 == 0) {
            return "EXPMA(" +  K_EXPMA_1 + "," + K_EXPMA_2 + "," + K_EXPMA_3 + ")";
        }
        return "EXPMA(" +  K_EXPMA_1 + "," + K_EXPMA_2 + "," + K_EXPMA_3 + "," + K_EXPMA_4 + ")";
    }

    private String getVRPreText(){
        return "VR(" + K_VR + ")";
    }

    private String getOBVPreText(){
        return "OBV";
    }

    private String getBreakPreText(){
        return "横盘突破";
    }

    private String getWRPreText(){
        return "WR(" + K_WR + ")";
    }

    private String getBIASPreText(){
        return "BIAS(" +  K_BIAS_1 + "," + K_BIAS_2 + "," + K_BIAS_3 + ")";
    }

    private String getDMIPreText(){
        return "DMI(" + K_DMI_1 + "," + K_DMI_2 + ")";
    }

    private String getCapitalDDZPreText() {
        return "资金博弈";
    }

    /***
     *
     * 绘制资金流
     * */
    private void drawKLineIndicatorCapitalFlow(Canvas canvas, final int index) {
        final int indicatorBottom = mIndicatorsRectBottom[index];


        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float x = 0, y = bottomLimit + mRes.mTextHeight;
        mRes.mKLineTextPaint.setTypeface(null);
        mRes.mKLineTextPaint.setTextSize(mRes.mTextSize);
        mRes.mKLineTextPaint.setColor(mRes.mKColorGray);

        if (notSupportKLineCapitalFlow()) {
            //画左上角的指标参数,不支持主力资金指标
            drawIndicatorPreText(canvas, getCapitalFlowPreText(), bottomLimit);
            //画占位符
            String placeHolderStr = mRes.mIndicatorNotSupportPlaceHolder;
            float textWidth = mRes.mTextDrawer.measureSingleTextWidth(placeHolderStr, mRes.mTextSize, null);
            int textHeight = mRes.mTextDrawer.measureSingleTextHeight(mRes.mTextSize, null);
            x = (mDrawingWidth - textWidth) / 2;
            y = (mRes.mIndicatorRectHeight - textHeight) / 2 + textHeight + bottomLimit;
            canvas.drawText(placeHolderStr, x, y, mRes.mKLineTextPaint);
            return;
        }

        int entityCount = mKLineEnd - mKLineStart;
        //判断有没有拉取到资金流数据
        boolean hasData = false;
        for (int i = 0; i < entityCount; i++) {
            KLineDesc kLineDesc = mKLineEntities.get(i + mKLineStart);
            long ymd = kLineDesc.getLYmd();
            Float superDelta = mListener.mKLineCapitalFlowList.get((int) ymd);
            if (superDelta != null) {
                hasData = true;
                break;
            }
        }
        if (!hasData) {
            return;
        }

        //算出五日、十日均线的值
        mKCapitalFlowAverageFiveDays = new float[entityCount];
        mKCapitalFlowAverageTenDays = new float[entityCount];
        long averageVolume = 0;
        int count = 0;
        for (int i = 0; i < entityCount; i++) {
            averageVolume = 0;
            count = 0;
            for (int j = 0; j < 5 && i + mKLineStart - j >= 0; j++) {
                KLineDesc kLineDesc = mKLineEntities.get(i + mKLineStart - j);
                long ymd = kLineDesc.getLYmd();
                Float superDelta = mListener.mKLineCapitalFlowList.get((int) ymd);
                if (superDelta == null) {
                    superDelta = 0.0f;
                }
                averageVolume += superDelta;
                count++;
            }
            if (count > 0) {
                averageVolume /= count;
            }
            mKCapitalFlowAverageFiveDays[i] = averageVolume;
        }
        for (int i = 0; i < entityCount; i++) {
            averageVolume = 0;
            count = 0;
            for (int j = 0; j < 10 && i + mKLineStart - j >= 0; j++) {
                KLineDesc kLineDesc = mKLineEntities.get(i + mKLineStart - j);
                long ymd = kLineDesc.getLYmd();
                Float superDelta = mListener.mKLineCapitalFlowList.get((int) ymd);
                if (superDelta == null) {
                    superDelta = 0.0f;
                }
                averageVolume += superDelta;
                count++;
            }
            if (count > 0) {
                averageVolume /= count;
            }
            mKCapitalFlowAverageTenDays[i] = averageVolume;
        }

        //算出最大最小值
        mKLineMaxCapitalDDZ = -(Float.MAX_VALUE - 100);
        mKLineMinCapitalDDZ = Float.MAX_VALUE;
        for (int i = 0; i < entityCount; i++) {
            KLineDesc kLineDesc = mKLineEntities.get(i + mKLineStart);
            long ymd = kLineDesc.getLYmd();
            Float superDelta = mListener.mKLineCapitalFlowList.get((int) ymd);
            if (superDelta == null) {
                superDelta = 0.0f;
            }

            mKLineMaxCapitalDDZ = MathUtil.getMaxVal(mKLineMaxCapitalDDZ, superDelta, mKCapitalFlowAverageFiveDays[i], mKCapitalFlowAverageTenDays[i]);
            mKLineMinCapitalDDZ = MathUtil.getMinVal(mKLineMinCapitalDDZ, superDelta, mKCapitalFlowAverageFiveDays[i], mKCapitalFlowAverageTenDays[i]);
        }

        mRes.mKLineMACDDiffPath.reset();
        mRes.mKLineMACDDeaPath.reset();
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;
        float startX = mRes.mKLineStrokeWidth / 2;
        for (int i = 0; i < entityCount; i++) {
            KLineDesc kLineDesc = mKLineEntities.get(i + mKLineStart);
            long ymd = kLineDesc.getLYmd();
            Float superDelta = mListener.mKLineCapitalFlowList.get((int) ymd);
            if (superDelta == null) {
                superDelta = 0.0f;
            }

            if (superDelta > 0) {
                mRes.mPaintKLine.setColor(mRes.mKColorRed);
            } else if (superDelta < 0) {
                mRes.mPaintKLine.setColor(mRes.mKColorGreen);
            } else {
                mRes.mPaintKLine.setColor(mRes.mKColorGray);
            }
            mRes.mPaintKLine.setStyle(Paint.Style.FILL_AND_STROKE);

            float middle = getKLineCapitalFlowDrawingHeight(0, indicatorBottom);
            canvas.drawRect(startX, getKLineCapitalFlowDrawingHeight(superDelta, indicatorBottom), startX + pillarWidth, middle, mRes.mPaintKLine);

            float ma5Height = getKLineCapitalFlowDrawingHeight(mKCapitalFlowAverageFiveDays[i], indicatorBottom);
            float ma10Height = getKLineCapitalFlowDrawingHeight(mKCapitalFlowAverageTenDays[i], indicatorBottom);
            if (i == 0) {
                mRes.mKLineMACDDiffPath.moveTo(startX + pillarWidth / 2, ma5Height);
                mRes.mKLineMACDDeaPath.moveTo(startX + pillarWidth / 2, ma10Height);
            } else {
                mRes.mKLineMACDDiffPath.lineTo(startX + pillarWidth / 2, ma5Height);
                mRes.mKLineMACDDeaPath.lineTo(startX + pillarWidth / 2, ma10Height);
            }

            startX += itemWidth;
        }

        mRes.mPaintLine.setColor(mRes.mKLineAverageColor1);
        canvas.drawPath(mRes.mKLineMACDDiffPath, mRes.mPaintLine);
        mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
        canvas.drawPath(mRes.mKLineMACDDeaPath, mRes.mPaintLine);
        //画左上角的指标参数
        drawIndicatorPreText(canvas, getCapitalFlowPreText(), bottomLimit);
    }

    private boolean notSupportKLineCapitalFlow() {
        return mListener.mLineType != LineChartTextureView.TYPE_DAILY_K ||
                !StockUtil.supportCapitalDDZ(mListener.mDtSecCode);
    }

    private void drawKlineIndicatorDMI(Canvas canvas, final int index){
        final int indicatorBottom = mIndicatorsRectBottom[index];
        float startx = /*left*/0;
        float maxval = Float.MIN_VALUE;
        float minval = Float.MAX_VALUE;
        int count = mKLineEntities.size();
        if (null == mDMIEntities || mDMIEntities.isEmpty() || mDMIEntities.size() != count) {
            double dmiUpDMSum = 0,dmiDownDMSum = 0,dmiTRSum = 0,dmiDXSum = 0;//DMI相关
            float[] UPDMArr = new float[count];
            float[] DOWNArr = new float[count];
            float[] DXArr = new float[count];
            float[] TRArr = new float[count];
            ArrayList<Float> pdi = new ArrayList<>();
            ArrayList<Float> mdi = new ArrayList<>();
            ArrayList<Float> adx = new ArrayList<>();
            ArrayList<Float> adxr = new ArrayList<>();
            mDMIEntities = new ArrayList<>();
            for (int i = 0 ; i < count ; i++){
                KLineDesc klineVO = mKLineEntities.get(i);
                if (i == 0){
                    dmiUpDMSum = 0;
                    dmiDownDMSum = 0;
                    dmiTRSum = 0;
                    dmiDXSum = 0;
                    UPDMArr[0] = 0;
                    DOWNArr[0] = 0;
                    DXArr[0] = 0;
                    TRArr[0] = 0;
                    pdi.add(0f);
                    mdi.add(0f);
                    adx.add(0f);
                    adxr.add(0f);
                }else{
                    KLineDesc preKlineVO = mKLineEntities.get(i-1);//取出当前的前一天
                    UPDMArr[i] = klineVO.getFHigh() - preKlineVO.getFHigh();
                    if (UPDMArr[i] < 0) {
                        UPDMArr[i] = 0.0f;
                    }
                    DOWNArr[i] = preKlineVO.getFLow() - klineVO.getFLow();
                    if (DOWNArr[i] < 0) {
                        DOWNArr[i] = 0.0f;
                    }

                    if (UPDMArr[i] > DOWNArr[i]) {
                        DOWNArr[i] = 0.0f;
                    } else if (UPDMArr[i] < DOWNArr[i]) {
                        UPDMArr[i] = 0.0f;
                    } else {
                        UPDMArr[i] = 0.0f;
                        DOWNArr[i] = 0.0f;
                    }

                    if (i < K_DMI_1) {
                        dmiUpDMSum += UPDMArr[i];
                    } else {
                        dmiUpDMSum += (UPDMArr[i] - UPDMArr[i-K_DMI_1]);
                    }
                    if (i< K_DMI_1) {
                        dmiDownDMSum += DOWNArr[i];
                    } else {
                        dmiDownDMSum += (DOWNArr[i] - DOWNArr[i-K_DMI_1]);
                    }
                    TRArr[i] = MathUtil.getMaxVal(MathUtil.getMaxVal(Math.abs(klineVO.getFHigh() - klineVO.getFLow()),
                    Math.abs(klineVO.getFHigh() - preKlineVO.getFClose())),
                    Math.abs(klineVO.getFLow() -preKlineVO.getFClose()));
                    if (i < K_DMI_1) {
                        dmiTRSum += TRArr[i];
                    } else {
                        dmiTRSum += TRArr[i] - TRArr[i-K_DMI_1];
                    }
                    if (dmiTRSum != 0) {
                        pdi.add((float) (dmiUpDMSum/dmiTRSum*100));
                        mdi.add((float) (dmiDownDMSum/dmiTRSum*100));
                    } else {
                        pdi.add(0f);
                        mdi.add(0f);
                    }
                    if ((pdi.get(i) + mdi.get(i)) != 0) {
                        DXArr[i] = Math.abs(pdi.get(i) - mdi.get(i))/(pdi.get(i) + mdi.get(i))*100;
                    } else {
                        DXArr[i] = 0;
                    }
                    if (i < K_DMI_2) {
                        dmiDXSum += DXArr[i];
                        adx.add((float) (dmiDXSum/(i+1)));
                    } else {
                        dmiDXSum += DXArr[i] - DXArr[i-K_DMI_2];
                        adx.add((float) (dmiDXSum/K_DMI_2));
                    }
                    if (i<K_DMI_2) {
                        adxr.add((adx.get(i)+adx.get(i - 1))/2);
                    } else {
                        adxr.add((adx.get(i) + adx.get(i-K_DMI_2))/2);
                    }
                }
                mDMIEntities.add(new DMI(pdi.get(i), mdi.get(i), adx.get(i), adxr.get(i)));
            }
        }

        for (int m = mKLineStart; m < mKLineEnd; m++) {
            DMI entity = mDMIEntities.get(m);
            maxval = MathUtil.getMaxVal(maxval, entity.getAdx(), entity.getAdxr(), entity.getMdi(), entity.getPdi());
            minval = MathUtil.getMinVal(minval, entity.getAdx(), entity.getAdxr(), entity.getMdi(), entity.getPdi());
        }

        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;

        drawIndicatorReferenceLine(canvas, index, false);
        startx = mRes.mKLineStrokeWidth / 2 + pillarWidth / 2;

        DMI prevDMI;
        DMI dmi;
        for (int j = mKLineStart; j < mKLineEnd; j++) {
            if (j == mKLineStart) {
                startx += itemWidth;
                continue;
            }

            prevDMI = mDMIEntities.get(j - 1);
            dmi = mDMIEntities.get(j);

            float prevPDIval = prevDMI.getPdi();
            float prevMDIval = prevDMI.getMdi();
            float prevADXval = prevDMI.getAdx();
            float prevADXRval = prevDMI.getAdxr();

            float prevPDIHeight = bottomLimit + ((maxval - prevPDIval) / (maxval - minval)) * bottomHeight;
            float prevMDIHeight = bottomLimit + ((maxval - prevMDIval) / (maxval - minval)) * bottomHeight;
            float prevADXHeight = bottomLimit + ((maxval - prevADXval) / (maxval - minval)) * bottomHeight;
            float prevADXRHeight = bottomLimit + ((maxval - prevADXRval) / (maxval - minval)) * bottomHeight;

            float pdiVal = dmi.getPdi();
            float mdiVal = dmi.getMdi();
            float adxVal = dmi.getAdx();
            float adxrVal = dmi.getAdxr();
            float pdiHeight = bottomLimit + ((maxval - pdiVal) / (maxval - minval)) * bottomHeight;
            float mdiHeight = bottomLimit + ((maxval - mdiVal) / (maxval - minval)) * bottomHeight;
            float adxHeight = bottomLimit + ((maxval - adxVal) / (maxval - minval)) * bottomHeight;
            float adxrHeight = bottomLimit + ((maxval - adxrVal) / (maxval - minval)) * bottomHeight;

            mRes.mPaintLine.setColor(mRes.mKLineAverageColor1);
            canvas.drawLine(startx - itemWidth, prevPDIHeight, startx, pdiHeight, mRes.mPaintLine);// PDI
            mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
            canvas.drawLine(startx - itemWidth, prevMDIHeight, startx, mdiHeight, mRes.mPaintLine);// MDI

            mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
            canvas.drawLine(startx - itemWidth, prevADXHeight, startx, adxHeight, mRes.mPaintLine);// ADX
            mRes.mPaintLine.setColor(mRes.mKLineAverageColor4);
            canvas.drawLine(startx - itemWidth, prevADXRHeight, startx, adxrHeight, mRes.mPaintLine);// ADXR

            startx += itemWidth;
        }

        //画左上角的指标参数
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getDMIPreText(), y);
    }

    private void drawKlineIndicatorOBV(Canvas canvas, final int index){
        final int indicatorBottom = mIndicatorsRectBottom[index];
        float startx = /*left*/0;
        float maxval = Float.MIN_VALUE;
        float minval = Float.MAX_VALUE;
        int count = mKLineEntities.size();
        if (null == mOBVEntities || mOBVEntities.isEmpty() || mOBVEntities.size() != count){
            mOBVEntities = new ArrayList<>();
            float[] inVol = new float[count];
            for (int i = 0 ; i < count ; i++){
                inVol[i] = mKLineEntities.get(i).getLVolume();
            }

            double[] obv = new double[count];
            core.obv(0, count - 1, mCloses, inVol, outBegIdx, outNBElement, obv);

            for (int i = 0 ; i < count ; i++){
                mOBVEntities.add(new OBV((float) obv[i] / 10000));
            }
        }

        for (int m = mKLineStart; m < mKLineEnd; m++) {
            OBV entity = mOBVEntities.get(m);
            maxval = MathUtil.getMaxVal(maxval, entity.getValue());
            minval = MathUtil.getMinVal(minval, entity.getValue());
        }
                /* 画图start... */
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;
        drawIndicatorReferenceLine(canvas, index, false);

        startx = mRes.mKLineStrokeWidth / 2 + pillarWidth / 2;
        OBV prevOBV;
        OBV obv;
        for (int j = mKLineStart; j < mKLineEnd; j++) {
            if (j == mKLineStart) {
                startx += itemWidth;
                continue;
            }

            prevOBV = mOBVEntities.get(j - 1);
            obv = mOBVEntities.get(j);

            float prevVal = prevOBV.getValue();

            float prevHeight = bottomLimit + ((maxval - prevVal) / (maxval - minval)) * bottomHeight;

            float val = obv.getValue();
            float vrHeight = bottomLimit + ((maxval - val) / (maxval - minval)) * bottomHeight;
            mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
            canvas.drawLine(startx - itemWidth, prevHeight, startx, vrHeight, mRes.mPaintLine);

            startx += itemWidth;
        }

        //画左上角的指标参数
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getOBVPreText(), y);

    }

    private void drawKlineIndicatorWR(Canvas canvas, final int index){
        final int indicatorBottom = mIndicatorsRectBottom[index];
        float startx = /*left*/0;
        float maxval = -Float.MAX_VALUE;
        float minval = Float.MAX_VALUE;
        int count = mKLineEntities.size();
        if (null == mWREntities || mWREntities.isEmpty() || mWREntities.size() != count) {
            mWREntities = new ArrayList<>();
            double[] wrs = new double[count];
            core.willR(0, count - 1, mHighs, mLows, mCloses, K_WR, outBegIdx, outNBElement, wrs);
            for (int i = 0 ; i < count ; i++){
                mWREntities.add(new WR((float) -wrs[(i + count - K_WR + 1) % count]));//不知道为什么，算出来是一个相反的数
            }
        }


        drawIndicatorReferenceLine(canvas, index, false);
        for (int m = mKLineStart; m < mKLineEnd; m++) {
            if (m < K_WR - 1)
                continue;
            WR entity = mWREntities.get(m);
            maxval = MathUtil.getMaxVal(maxval, entity.getValue());
            minval = MathUtil.getMinVal(minval, entity.getValue());
        }

        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;

        startx = mRes.mKLineStrokeWidth / 2 + pillarWidth / 2;

        WR prevWR;
        WR wr;
        for (int j = mKLineStart; j < mKLineEnd; j++) {
            if (j == mKLineStart || j < K_CCI_1) {
                startx += itemWidth;
                continue;
            }

            prevWR = mWREntities.get(j - 1);
            wr = mWREntities.get(j);
            float prevWRVal = prevWR.getValue();
            float wrHeight = bottomLimit + ((maxval - prevWRVal) / (maxval - minval)) * bottomHeight;
            float wrVal = wr.getValue();
            float kHeight = bottomLimit + ((maxval - wrVal) / (maxval - minval)) * bottomHeight;

            mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
            canvas.drawLine(startx - itemWidth, wrHeight, startx, kHeight, mRes.mPaintLine);
            startx += itemWidth;
        }

        //画左上角的指标参数
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getWRPreText(), y);
    }

    private void drawKlineIndicatorCCI(Canvas canvas, final int index){
        final int indicatorBottom = mIndicatorsRectBottom[index];
        float startx = /*left*/0;
        float maxval = 0 - Float.MAX_VALUE;
        float minval = Float.MAX_VALUE;
        int count = mKLineEntities.size();
        if (null == mCCIEntities || mCCIEntities.isEmpty() || mCCIEntities.size() != count) {
            mCCIEntities = new ArrayList<CCI>();

            double[] cci = new double[count];
            double[] ccia = new double[count];
            core.cci(0, count - 1, mHighs, mLows, mCloses, K_CCI_1, outBegIdx, outNBElement, cci);
            core.ema(0, count - 1, cci, K_CCI_2, outBegIdx, outNBElement, ccia);

            for (int i = 0; i < count - K_CCI_1 + 1; i++){
                mCCIEntities.add(new CCI((float) cci[i]));
            }

            for (int i = 0; i < K_CCI_1 - 1; i++){//这些值是无效的
                mCCIEntities.add(0, new CCI(0));
            }

            for (int i = K_CCI_1 + K_CCI_2 - 1 ; i < count ; i++){//新增CCIA线
                mCCIEntities.get(i).setCcia((float) ccia[i - K_CCI_1 - K_CCI_2 + 1]);
            }
        }

        for (int m = mKLineStart; m < mKLineEnd; m++) {
            if (m < K_CCI_1 - 1)
                continue;
            CCI entity = mCCIEntities.get(m);
            maxval = MathUtil.getMaxVal(maxval, entity.getCCI());
            minval = MathUtil.getMinVal(minval, entity.getCCI());
        }
                /* 画图start... */
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;

        drawIndicatorReferenceLine(canvas, index, false);
        startx = mRes.mKLineStrokeWidth / 2 + pillarWidth / 2;

        CCI prevCCI;
        CCI cci;
        for (int j = mKLineStart; j < mKLineEnd; j++) {
            if (j == mKLineStart || j < K_CCI_1) {
                startx += itemWidth;
                continue;
            }

            prevCCI = mCCIEntities.get(j - 1);
            cci = mCCIEntities.get(j);
            float prevCCIVal = prevCCI.getCCI();
            float prevCCIAVal = prevCCI.getCcia();
            float cciaHeight = bottomLimit + ((maxval - prevCCIAVal) / (maxval - minval)) * bottomHeight;
            float cciHeight = bottomLimit + ((maxval - prevCCIVal) / (maxval - minval)) * bottomHeight;

            float cciVal = cci.getCCI();
            float cciaVal = cci.getCcia();
            float cciaHeight2 = bottomLimit + ((maxval - cciaVal) / (maxval - minval)) * bottomHeight;
            float kHeight = bottomLimit + ((maxval - cciVal) / (maxval - minval)) * bottomHeight;

            mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
            canvas.drawLine(startx - itemWidth, cciHeight, startx, kHeight, mRes.mPaintLine);

            if (j > K_CCI_1 + K_CCI_2 - 1){
                mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
                canvas.drawLine(startx - itemWidth, cciaHeight, startx, cciaHeight2, mRes.mPaintLine);
            }

            startx += itemWidth;
        }

        //画左上角的指标参数
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getCCIPreText(), y);
    }

    private void drawKlineIndicatorBIAS(Canvas canvas, final int index){
        final int indicatorBottom = mIndicatorsRectBottom[index];
        float startx = /*left*/0;
        float maxval = -Float.MAX_VALUE;
        float minval = Float.MAX_VALUE;
        int count = mKLineEntities.size();
        if (null == mBIASEntities || mBIASEntities.isEmpty() || mBIASEntities.size() != count){
            mBIASEntities = new ArrayList<>();
            double[] ma1 = new double[count];
            double[] ma2 = new double[count];
            double[] ma3 = new double[count];

            core.sma(0, count - 1, mCloses, K_BIAS_1, outBegIdx, outNBElement, ma1);
            core.sma(0, count - 1, mCloses, K_BIAS_2, outBegIdx, outNBElement, ma2);
            core.sma(0, count - 1, mCloses, K_BIAS_3, outBegIdx, outNBElement, ma3);

            for (int i = 0 ; i < count ; i++){
                int index1 = K_BIAS_1 > count ? 0 : (i + count - K_BIAS_1 + 1) % count;
                float value1 = (float) ((mCloses[i] - ma1[index1])/
                        ma1[index1]*100);

                int index2 = K_BIAS_2 > count ? 0 : (i + count - K_BIAS_2 + 1) % count;
                float value2 = (float) ((mCloses[i] - ma2[index2])/
                        ma2[index2]*100);

                int index3 = K_BIAS_3 > count ? 0 : (i + count - K_BIAS_3 + 1) % count;
                float value3 = (float) ((mCloses[i] - ma3[index3])/
                        ma3[index3]*100);
                mBIASEntities.add(new BIAS(value1, value2, value3));
            }
        }

        for (int m = mKLineStart; m < mKLineEnd; m++) {
            BIAS entity = mBIASEntities.get(m);
            if (m >= K_BIAS_1 - 1){
                maxval = MathUtil.getMaxVal(maxval, entity.getValue1());
                minval = MathUtil.getMinVal(minval, entity.getValue1());
            }
            if (m >= K_BIAS_2 - 1){
                maxval = MathUtil.getMaxVal(maxval, entity.getValue2());
                minval = MathUtil.getMinVal(minval, entity.getValue2());
            }
            if (m >= K_BIAS_3 - 1){
                maxval = MathUtil.getMaxVal(maxval, entity.getValue3());
                minval = MathUtil.getMinVal(minval, entity.getValue3());
            }
        }

        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;

        startx = mRes.mKLineStrokeWidth / 2 + pillarWidth / 2;

        BIAS prevBIAS;
        BIAS bias;
        for (int j = mKLineStart; j < mKLineEnd; j++) {
            if (j == mKLineStart) {
                startx += itemWidth;
                continue;
            }

            prevBIAS = mBIASEntities.get(j - 1);
            bias = mBIASEntities.get(j);
                    /* 画kdj */
            float prevVal1 = prevBIAS.getValue1();
            float prevVal2 = prevBIAS.getValue2();
            float prevVal3 = prevBIAS.getValue3();

            float prevVal1Height = bottomLimit + ((maxval - prevVal1) / (maxval - minval)) * bottomHeight;
            float prevVal2Height = bottomLimit + ((maxval - prevVal2) / (maxval - minval)) * bottomHeight;
            float prevVal3Height = bottomLimit + ((maxval - prevVal3) / (maxval - minval)) * bottomHeight;

            float val1 = bias.getValue1();
            float val2 = bias.getValue2();
            float val3 = bias.getValue3();
            float val1Height = bottomLimit + ((maxval - val1) / (maxval - minval)) * bottomHeight;
            float val2Height = bottomLimit + ((maxval - val2) / (maxval - minval)) * bottomHeight;
            float val3Height = bottomLimit + ((maxval - val3) / (maxval - minval)) * bottomHeight;

            mRes.mPaintLine.setColor(mRes.mKLineAverageColor1);
            canvas.drawLine(startx - itemWidth, prevVal1Height, startx, val1Height, mRes.mPaintLine);
            mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
            canvas.drawLine(startx - itemWidth, prevVal2Height, startx, val2Height, mRes.mPaintLine);
            mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
            canvas.drawLine(startx - itemWidth, prevVal3Height, startx, val3Height, mRes.mPaintLine);

            startx += itemWidth;
        }

        //画左上角的指标参数
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getBIASPreText(), y);
        drawIndicatorReferenceLine(canvas, index, false);

    }

    private void drawKlineIndicatorVR(Canvas canvas, final int index){
        final int indicatorBottom = mIndicatorsRectBottom[index];
        float startx = /*left*/0;
        float maxval = Float.MIN_VALUE;
        float minval = Float.MAX_VALUE;
        int count = mKLineEntities.size();
        if (null == mVREntities || mVREntities.isEmpty() || mVREntities.size() != count) {
            mVREntities = new ArrayList<>();
            double[] avses = new double[count];//上涨的集合
            double[] bvses = new double[count];//下跌的集合
            double[] cvses = new double[count];
            for (int i = 0 ; i < count ; i++) {
                if (i == 0){
                    avses[i] = 0;
                    bvses[i] = 0;
                    cvses[i] = 0;
                    continue;
                }
                KLineDesc entity = mKLineEntities.get(i);
                KLineDesc preEntity = mKLineEntities.get(i - 1);
                float preClose = preEntity.getFClose();
                float close = entity.getFClose();
                if (preClose < close){//
                    avses[i] = entity.getLVolume();
                    bvses[i] = 0;
                    cvses[i] = 0;
                }else if (preClose > close){
                    avses[i] = 0;
                    bvses[i] = entity.getLVolume();
                    cvses[i] = 0;
                }else{
                    avses[i] = 0;
                    bvses[i] = 0;
                    cvses[i] = entity.getLVolume();
                }
            }

            double[] sumAvs = new double[count];
            double[] sumBvs = new double[count];
            double[] sumCvs = new double[count];
            core.sum(0, count - 1, avses,  K_VR, outBegIdx, outNBElement, sumAvs);
            core.sum(0, count - 1, bvses,  K_VR, outBegIdx, outNBElement, sumBvs);
            core.sum(0, count - 1, cvses,  K_VR, outBegIdx, outNBElement, sumCvs);

            for (int i = 0 ; i < count ; i++){
                int tIndex = K_VR > count ? 0 : (i + count - K_VR + 1) % count;
                double tAvs = sumAvs[tIndex];
                double tBvs = sumBvs[tIndex];
                double tCvs = sumCvs[tIndex];
                mVREntities.add(new VR((float) (100 * (tAvs + 1.0f / 2.0f * tCvs)/ (tBvs + 1.0f / 2.0f * tCvs))));
            }
        }

        for (int m = mKLineStart; m < mKLineEnd; m++) {
            VR entity = mVREntities.get(m);
            maxval = MathUtil.getMaxVal(maxval, entity.getVr());
            minval = MathUtil.getMinVal(minval, entity.getVr());
        }
                /* 画图start... */
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;

        drawIndicatorReferenceLine(canvas, index, false);
        startx = mRes.mKLineStrokeWidth / 2 + pillarWidth / 2;

        VR prevVR;
        VR vr;
        for (int j = mKLineStart; j < mKLineEnd; j++) {
            if (j == mKLineStart || j < K_VR) {
                startx += itemWidth;
                continue;
            }

            prevVR = mVREntities.get(j - 1);
            vr = mVREntities.get(j);

            float prevVal = prevVR.getVr();

            float prevHeight = bottomLimit + ((maxval - prevVal) / (maxval - minval)) * bottomHeight;

            float val = vr.getVr();
            float vrHeight = bottomLimit + ((maxval - val) / (maxval - minval)) * bottomHeight;
            mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
            canvas.drawLine(startx - itemWidth, prevHeight, startx, vrHeight, mRes.mPaintLine);

            startx += itemWidth;
        }

        //画左上角的指标参数
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getVRPreText(), y);

    }

    private void drawKlineIndicatorEXPMA(Canvas canvas, final int index){
        final int indicatorBottom = mIndicatorsRectBottom[index];
        float startx = /*left*/0;
        float maxval = Float.MIN_VALUE;
        float minval = Float.MAX_VALUE;
        int count = mKLineEntities.size();
        if (null == mEXPMAEntities || mEXPMAEntities.isEmpty() || mEXPMAEntities.size() != count){
            mEXPMAEntities = new ArrayList<>();
            double[] ma1 = new double[count];
            double[] ma2 = new double[count];
            double[] ma3 = new double[count];
            double[] ma4 = new double[count];

            core.ema(0, count - 1, mCloses, K_EXPMA_1, outBegIdx, outNBElement, ma1);
            core.ema(0, count - 1, mCloses, K_EXPMA_2, outBegIdx, outNBElement, ma2);
            core.ema(0, count - 1, mCloses, K_EXPMA_3, outBegIdx, outNBElement, ma3);
            core.ema(0, count - 1, mCloses, K_EXPMA_4, outBegIdx, outNBElement, ma4);

            for (int i = 0 ; i < count ; i++){
                float tMa1 = K_EXPMA_1 > count ? 0 : (float) ma1[(i + count - K_EXPMA_1 + 1) % count];
                float tMa2 = K_EXPMA_2 > count ? 0 : (float) ma2[(i + count - K_EXPMA_2 + 1) % count];
                float tMa3 = K_EXPMA_3 > count ? 0 : (float) ma3[(i + count - K_EXPMA_3 + 1) % count];
                float tMa4 = K_EXPMA_4 > count ? 0 : (float) ma4[(i + count - K_EXPMA_4 + 1) % count];

                mEXPMAEntities.add(new EXPMA(tMa1, tMa2, tMa3, tMa4));
            }
        }

        for (int m = mKLineStart; m < mKLineEnd; m++) {
            EXPMA entity = mEXPMAEntities.get(m);
            maxval = MathUtil.getMaxVal(maxval, mKLineEntities.get(m).getFHigh());
            minval = MathUtil.getMinVal(minval, mKLineEntities.get(m).getFLow());
            if (m >= K_EXPMA_1){
                maxval = MathUtil.getMaxVal(maxval, entity.getMa1(), mKLineEntities.get(m).getFHigh());
                minval = MathUtil.getMinVal(minval, entity.getMa1(), mKLineEntities.get(m).getFLow());
            }
            if (m >= K_EXPMA_2){
                maxval = MathUtil.getMaxVal(maxval, entity.getMa2(), mKLineEntities.get(m).getFHigh());
                minval = MathUtil.getMinVal(minval, entity.getMa2(), mKLineEntities.get(m).getFLow());
            }
            if (K_EXPMA_3 > 0 && m >= K_EXPMA_3) {
                maxval = MathUtil.getMaxVal(maxval, entity.getMa3());
                minval = MathUtil.getMinVal(minval, entity.getMa3());
            }
            if (K_EXPMA_4 > 0 && m >= K_EXPMA_4) {
                maxval = MathUtil.getMaxVal(maxval, entity.getMa4());
                minval = MathUtil.getMinVal(minval, entity.getMa4());
            }
        }
                /* 画图start... */
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;

        drawIndicatorReferenceLine(canvas, index, false);
        startx = mRes.mKLineStrokeWidth / 2 + pillarWidth / 2;

        EXPMA prevEXPMA;
        EXPMA expma;
        for (int j = mKLineStart; j < mKLineEnd; j++) {
            drawSmallKline(j, bottomLimit, maxval, minval, bottomHeight, startx, canvas, pillarWidth);

            if (j == mKLineStart) {
                startx += itemWidth;
                continue;
            }

            prevEXPMA = mEXPMAEntities.get(j - 1);
            expma = mEXPMAEntities.get(j);
                    /* 画kdj */
            float prevMa1val = prevEXPMA.getMa1();
            float prevMa2val = prevEXPMA.getMa2();
            float prevMa3val = prevEXPMA.getMa3();
            float prevMa4val = prevEXPMA.getMa4();

            float prevMa1Height = bottomLimit + ((maxval - prevMa1val) / (maxval - minval)) * bottomHeight;
            float prevMa2Height = bottomLimit + ((maxval - prevMa2val) / (maxval - minval)) * bottomHeight;
            float prevMa3Height = bottomLimit + ((maxval - prevMa3val) / (maxval - minval)) * bottomHeight;
            float prevMa4Height = bottomLimit + ((maxval - prevMa4val) / (maxval - minval)) * bottomHeight;

            float ma1Val = expma.getMa1();
            float ma2Val = expma.getMa2();
            float ma3Val = expma.getMa3();
            float ma4Val = expma.getMa4();

            float ma1Height = bottomLimit + ((maxval - ma1Val) / (maxval - minval)) * bottomHeight;
            float ma2Height = bottomLimit + ((maxval - ma2Val) / (maxval - minval)) * bottomHeight;
            float ma3Height = bottomLimit + ((maxval - ma3Val) / (maxval - minval)) * bottomHeight;
            float ma4Height = bottomLimit + ((maxval - ma4Val) / (maxval - minval)) * bottomHeight;

            if (j >= K_EXPMA_1){
                mRes.mPaintLine.setColor(mRes.mKLineAverageColor1);
                canvas.drawLine(startx - itemWidth, prevMa1Height, startx, ma1Height, mRes.mPaintLine);
            }
            if (j >= K_EXPMA_2){
                mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
                canvas.drawLine(startx - itemWidth, prevMa2Height, startx, ma2Height, mRes.mPaintLine);
            }
            if (K_EXPMA_3 > 0 && j >= K_EXPMA_3) {
                mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
                canvas.drawLine(startx - itemWidth, prevMa3Height, startx, ma3Height, mRes.mPaintLine);
            }
            if (K_EXPMA_4 > 0 && j >= K_EXPMA_4) {
                mRes.mPaintLine.setColor(mRes.mKLineAverageColor4);
                canvas.drawLine(startx - itemWidth, prevMa4Height, startx, ma4Height, mRes.mPaintLine);
            }

            startx += itemWidth;
        }

        //画左上角的指标参数
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getEXPMAPreText(), y);
    }

    private void drawKlineIndicatorDMA(Canvas canvas, final int index){
        final int indicatorBottom = mIndicatorsRectBottom[index];
        float startx = /*left*/0;
        float maxval = -Float.MAX_VALUE;
        float minval = Float.MAX_VALUE;

        if (null == mDMAEntities || mDMAEntities.isEmpty() || mDMAEntities.size() != mKLineEntities.size()) {
            mDMAEntities = new ArrayList<DMA>();
            double[] shortTimeAve = new double[mKLineEntities.size()];
            double[] longTimeAve = new double[mKLineEntities.size()];
            core.movingAverage(0, mCloses.length - 1, mCloses, K_DMA_1, MAType.Sma, outBegIdx, outNBElement, shortTimeAve);
            core.movingAverage(0, mCloses.length - 1, mCloses, K_DMA_2, MAType.Sma, outBegIdx, outNBElement, longTimeAve);

            ArrayList<Float> shortTimeAves = new ArrayList<Float>();//8.3999,  8.69
            ArrayList<Float> longTimeAves = new ArrayList<Float>();

            for(int i = 0 ; i< mCloses.length - K_DMA_1 + 1 ; i++) {
                shortTimeAves.add((float) shortTimeAve[i]);
            }
            for(int i = 0 ; i < K_DMA_1 - 1 ; i++) {
                shortTimeAves.add(0, 0f);
            }

            for(int i = 0 ; i< mCloses.length - K_DMA_2 + 1 ; i++) {
                longTimeAves.add((float) longTimeAve[i]);
            }
            for(int i = 0 ; i < K_DMA_2 - 1 ; i++) {
                longTimeAves.add(0, 0f);
            }

            float[] dma = new float[mCloses.length];
            double[] ama = new double[mCloses.length];
            for (int i = 0 ; i < mCloses.length ; i++){
                if (i < K_DMA_2 - 1) {
                    dma[i] = 0;
                }else {
                    dma[i] = shortTimeAves.get(i) - longTimeAves.get(i);
                }
            }
            core.movingAverage(0, dma.length - 1, dma, K_DMA_3, MAType.Sma, outBegIdx, outNBElement, ama);
            ArrayList<Float> amas = new ArrayList<Float>();
            for(int i = 0 ; i< mCloses.length - K_DMA_3 + 1 ; i++) {
                amas.add((float) ama[i]);
            }
            for(int i = 0 ; i < K_DMA_3 - 1 ; i++) {
                amas.add(0, 0f);
            }
            for (int i = 0 ; i < mKLineEntities.size() ; i++){
                mDMAEntities.add(new DMA(amas.get(i), dma[i]));
            }
        }

        for (int m = mKLineStart; m < mKLineEnd; m++) {
            DMA entity = mDMAEntities.get(m);
            if (entity.getAma() == 0 && entity.getDma() == 0){
                continue;
            }
            maxval = MathUtil.getMaxVal(maxval, entity.getAma(), entity.getDma());
            minval = MathUtil.getMinVal(minval, entity.getAma(), entity.getDma());
        }
                /* 画图start... */
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;

        drawIndicatorReferenceLine(canvas, index, false);
        startx = mRes.mKLineStrokeWidth / 2 + pillarWidth / 2;
        DMA prevDMA;
        DMA dma;

        for (int j = mKLineStart; j < mKLineEnd; j++) {
            if (j == mKLineStart) {
                startx += itemWidth;
                continue;
            }

            //绘制ENE数据
            prevDMA = mDMAEntities.get(j - 1);
            dma = mDMAEntities.get(j);
            float prevDMAVal = prevDMA.getDma();
            float prevUpperHeightVal = bottomLimit + ((maxval - prevDMAVal) / (maxval - minval)) * bottomHeight;
            float prevAMAVal = prevDMA.getAma();
            float prevLowerHeightVal = bottomLimit + ((maxval - prevAMAVal) / (maxval - minval)) * bottomHeight;

            float dmaVal = dma.getDma();
            float upperHeightVal = bottomLimit + ((maxval - dmaVal) / (maxval - minval)) * bottomHeight;
            float amaVal = dma.getAma();
            float lowerHeightVal = bottomLimit + ((maxval - amaVal) / (maxval - minval)) * bottomHeight;

            if (j >= K_DMA_2){
                mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);//DMA
                canvas.drawLine(startx - itemWidth, prevUpperHeightVal, startx, upperHeightVal, mRes.mPaintLine);
            }

            if (j >= K_DMA_2 + K_DMA_3){
                mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);//AMA
                canvas.drawLine(startx - itemWidth, prevLowerHeightVal, startx, lowerHeightVal, mRes.mPaintLine);
            }

            startx += itemWidth;
        }


        //画左上角的指标参数
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getDMAPreText(), y);
    }

    private void drawKlineIndicatorENE(Canvas canvas, final int index){

        final int indicatorBottom = mIndicatorsRectBottom[index];
        float startx = /*left*/0;
        float maxval = Float.MIN_VALUE;
        float minval = Float.MAX_VALUE;

        if (null == mENEEntities || mENEEntities.isEmpty() || mENEEntities.size() != mKLineEntities.size()) {
            mENEEntities = new ArrayList<ENE>();
            double[] sma = new double[mKLineEntities.size()];
            core.sma(0, mKLineEntities.size() - 1 , mCloses, K_ENE_1, outBegIdx, outNBElement, sma);
            for (int i = 0 ;i < mCloses.length ; i++) {
                float upper = (float) ((1 + K_ENE_2 * 1.0f / 100f) * sma[i]);
                float lower = (float) ((1 - K_ENE_3 * 1.0f / 100f) * sma[i]);
                float ene = (upper + lower) / 2;
                if (upper == 0 && lower == 0 && ene == 0)//无法计算时会有三个0的结果
                    continue;
                mENEEntities.add(new ENE(upper, lower, ene));
            }

            for (int i = 0 ; i < K_ENE_1 - 1 ; i++){//补数据
                mENEEntities.add(0, new ENE(0, 0, 0));
            }
        }

        for (int m = mKLineStart; m < mKLineEnd; m++) {
            ENE entity = mENEEntities.get(m);
            if (entity.getUpper() != 0){
                maxval = MathUtil.getMaxVal(maxval, entity.getUpper(), mKLineEntities.get(m).getFHigh());
            }
            if (entity.getLower() != 0){
                minval = MathUtil.getMinVal(minval, entity.getLower(), mKLineEntities.get(m).getFLow());
            }
        }
                /* 画图start... */
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;

        drawIndicatorReferenceLine(canvas, index, false);
        startx = mRes.mKLineStrokeWidth / 2 + pillarWidth / 2;

        ENE prevENE;
        ENE ene;

        for (int j = mKLineStart ; j < mKLineEnd; j++) {

            drawSmallKline(j, bottomLimit, maxval, minval, bottomHeight, startx, canvas, pillarWidth);
            if (j == mKLineStart || j < K_ENE_1) {
                startx += itemWidth;
                continue;
            }
            //绘制ENE数据
            prevENE = mENEEntities.get(j - 1);
            ene = mENEEntities.get(j);
            float prevUpperVal = prevENE.getUpper();
            float prevUpperHeightVal = bottomLimit + ((maxval - prevUpperVal) / (maxval - minval)) * bottomHeight;
            float prevLowerVal = prevENE.getLower();
            float prevLowerHeightVal = bottomLimit + ((maxval - prevLowerVal) / (maxval - minval)) * bottomHeight;
            float prevENEVal = prevENE.getEne();
            float prevENEHeightVal = bottomLimit + ((maxval - prevENEVal) / (maxval - minval)) * bottomHeight;

            float upperVal = ene.getUpper();
            float upperHeightVal = bottomLimit + ((maxval - upperVal) / (maxval - minval)) * bottomHeight;
            float lowerVal = ene.getLower();
            float lowerHeightVal = bottomLimit + ((maxval - lowerVal) / (maxval - minval)) * bottomHeight;
            float eNEVal = ene.getEne();
            float eNEHeightVal = bottomLimit + ((maxval - eNEVal) / (maxval - minval)) * bottomHeight;

            mRes.mPaintLine.setColor(mRes.mKLineAverageColor1);
            canvas.drawLine(startx - itemWidth, prevUpperHeightVal, startx, upperHeightVal, mRes.mPaintLine);
            mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
            canvas.drawLine(startx - itemWidth, prevLowerHeightVal, startx, lowerHeightVal, mRes.mPaintLine);
            mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
            canvas.drawLine(startx - itemWidth, prevENEHeightVal, startx, eNEHeightVal, mRes.mPaintLine);

            startx += itemWidth;
        }

        //画左上角的指标参数
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getENEPreText(), y);

    }

    private void drawSmallKline(int j, float bottomLimit, float maxval, float minval, float bottomHeight, float startx, Canvas canvas, float pillarWidth){
        //绘制K线
        KLineDesc entity = mKLineEntities.get(j);

        float begin = entity.getFOpen();
        float end = entity.getFClose();
        float high = entity.getFHigh();
        float low = entity.getFLow();

        float beginHeight = bottomLimit + ((maxval - begin) / (maxval - minval)) * bottomHeight;
        float endHeight = bottomLimit + ((maxval - end) / (maxval - minval)) * bottomHeight;
        float highHeight = bottomLimit + ((maxval - high) / (maxval - minval)) * bottomHeight;
        float lowHeight = bottomLimit + ((maxval - low) / (maxval - minval)) * bottomHeight;

        mRes.mPaintLine.setStrokeWidth(mRes.mKLineStrokeWidth);
        if (end > begin) {
            mRes.mPaintKLine.setStyle(getKLineStyleRiseLine());
            mRes.mPaintKLine.setStrokeWidth(mRes.mKLineStrokeWidth);
            mRes.mPaintKLine.setColor(mRes.mKColorRed);
            mRes.mPaintLine.setColor(mRes.mKColorRed);
        } else if (end == begin) {
            float yesterdayEnd = 0;
            if (j > 0) {
                yesterdayEnd = mKLineEntities.get(j - 1).getFClose();
            }
            if (end < yesterdayEnd) {
                mRes.mPaintKLine.setStyle(Paint.Style.FILL_AND_STROKE);
                mRes.mPaintKLine.setColor(mRes.mKColorGreen);
                mRes.mPaintLine.setColor(mRes.mKColorGreen);
            } else {
                mRes.mPaintKLine.setStyle(getKLineStyleRiseLine());
                mRes.mPaintKLine.setStrokeWidth(mRes.mKLineStrokeWidth);
                mRes.mPaintKLine.setColor(mRes.mKColorRed);
                mRes.mPaintLine.setColor(mRes.mKColorRed);
            }
        } else {
            mRes.mPaintKLine.setStyle(Paint.Style.FILL_AND_STROKE);
            mRes.mPaintKLine.setColor(mRes.mKColorGreen);
            mRes.mPaintLine.setColor(mRes.mKColorGreen);
        }

        //画K线红绿柱子
        float left = startx - pillarWidth / 2;
        float right = startx + pillarWidth / 2;
        canvas.drawRect(left, endHeight, right, beginHeight, mRes.mPaintKLine);

        //画上下影线
        float max = Math.max(beginHeight, endHeight);
        float min = Math.min(beginHeight, endHeight);
        float lineStartX = startx;
        if (end >= begin) {
            canvas.drawLine(lineStartX, lowHeight, lineStartX, max, mRes.mPaintKLine);
            canvas.drawLine(lineStartX, min, lineStartX, highHeight, mRes.mPaintKLine);
        } else {
            canvas.drawLine(lineStartX, lowHeight, lineStartX, highHeight, mRes.mPaintKLine);
        }
    }

    private void drawKLineIndicatorKDJ(Canvas canvas, final int index) {
        final int indicatorBottom = mIndicatorsRectBottom[index];
        float startx = /*left*/0;
        float kValue = mRes.DEFALUT_KD_VALUE;
        float dValue = mRes.DEFALUT_KD_VALUE;
        float jValue = mRes.DEFALUT_KD_VALUE;
        float ln = Float.MAX_VALUE;
        float hn = Float.MIN_VALUE;
        float maxval = Float.MIN_VALUE;
        float minval = Float.MAX_VALUE;
        if (null == mKdjEntitys || mKdjEntitys.isEmpty() || mKdjEntitys.size() != mKLineEntities.size()) {
            mKdjEntitys = new ArrayList<KDJ>();
            ArrayList<Float> klist = new ArrayList<Float>();
            ArrayList<Float> dlist = new ArrayList<Float>();
            for (int i = 0; i < mKLineEntities.size(); i++) {
                if(i==0){
                    klist.add(50f);
                    dlist.add(50f);
                    mKdjEntitys.add(new KDJ(0, 0, 0));
                    continue;
                }
                KLineDesc entity = mKLineEntities.get(i);
                float close = entity.getFClose();// 收盘价
                List<KLineDesc> subentitys = mKLineEntities.subList((i - K_KDJ_1 +1) < 0 ? 0 : (i - K_KDJ_1 +1), (i+1)>mKLineEntities.size()?mKLineEntities.size():(i+1));
                for (KLineDesc item : subentitys) {
                    float low = item.getFLow();
                    float high = item.getFHigh();
                    if (low < ln)
                        ln = low;
                    if (high > hn)
                        hn = high;
                }
                float rsv = 0;
                if (hn != ln) {
                    rsv = (close - ln) / (hn - ln) * 100;
                    kValue = SMA(rsv, K_KDJ_2, 1, klist.get(i - 1));
                    dValue = SMA(kValue, K_KDJ_3, 1, dlist.get(i - 1));
                    jValue = 3 * kValue - 2 * dValue;
                } else {
                    kValue = dValue = jValue = 100;
                }
                ln = Float.MAX_VALUE;
                hn = Float.MIN_VALUE;
                klist.add(kValue);
                dlist.add(dValue);
                mKdjEntitys.add(new KDJ(kValue, dValue, jValue));
            }
        }
        for (int m = mKLineStart; m < mKLineEnd; m++) {
            KDJ entity = mKdjEntitys.get(m);
            maxval = MathUtil.getMaxVal(maxval, entity.getKvalue(), entity.getDvalue(), entity.getJvalue());
            minval = MathUtil.getMinVal(minval, entity.getKvalue(), entity.getDvalue(), entity.getJvalue());
        }
                /* 画图start... */
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;

        drawIndicatorReferenceLine(canvas, index, false);
        startx = mRes.mKLineStrokeWidth / 2 + pillarWidth / 2;

        KDJ prevKdj;
        KDJ kdj;
        for (int j = mKLineStart; j < mKLineEnd; j++) {
            if (j == mKLineStart) {
                startx += itemWidth;
                continue;
            }

            prevKdj = mKdjEntitys.get(j - 1);
            kdj = mKdjEntitys.get(j);
                    /* 画kdj */
            float prev_dkval = prevKdj.getKvalue();
            float prev_ddval = prevKdj.getDvalue();
            float prev_djval = prevKdj.getJvalue();

            float prev_kHeight = bottomLimit + ((maxval - prev_dkval) / (maxval - minval)) * bottomHeight;
            float prev_dHeight = bottomLimit + ((maxval - prev_ddval) / (maxval - minval)) * bottomHeight;
            float prev_jHeight = bottomLimit + ((maxval - prev_djval) / (maxval - minval)) * bottomHeight;

            float dkval = kdj.getKvalue();
            float ddval = kdj.getDvalue();
            float djval = kdj.getJvalue();
            if(dkval==0 && ddval==0 && djval==0){
                continue;
            }
            float kHeight = bottomLimit + ((maxval - dkval) / (maxval - minval)) * bottomHeight;
            float dHeight = bottomLimit + ((maxval - ddval) / (maxval - minval)) * bottomHeight;
            float jHeight = bottomLimit + ((maxval - djval) / (maxval - minval)) * bottomHeight;

            mRes.mPaintLine.setColor(mRes.K_KDJ_KCOLOR);
            canvas.drawLine(startx - itemWidth, prev_kHeight, startx, kHeight, mRes.mPaintLine);// k线
            mRes.mPaintLine.setColor(mRes.K_KDJ_DCOLOR);
            canvas.drawLine(startx - itemWidth, prev_dHeight, startx, dHeight, mRes.mPaintLine);// d线
            mRes.mPaintLine.setColor(mRes.K_KDJ_JCOLOR);
            canvas.drawLine(startx - itemWidth, prev_jHeight, startx, jHeight, mRes.mPaintLine);// j线

            startx += itemWidth;
        }

        //画左上角的指标参数
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getKDJPreText(), y);
    }

    private void drawKLineIndicatorBOLL(Canvas canvas, final int index) {
        final int indicatorBottom = mIndicatorsRectBottom[index];
        if (mBollEntities == null) {
            mBollEntities = new ArrayList<>();
            float bollval, upper, lower;
            for (int i = 0; i < mKLineEntities.size(); i++) {
                bollval = 0;
                upper = 0;
                lower = 0;
                float sum = 0;
                int count = 0;
                for (int j = 0; j < DEFAULT_BOLL_MA_COUNT && i - j >= 0; j++) {
                    sum += mKLineEntities.get(i - j).getFClose();
                    count++;
                }
                if (count > 0) {
                    bollval = sum / count;
                }
                BOLL boll = new BOLL(bollval, upper, lower);
                mBollEntities.add(boll);

            }
            for (int i = 0; i < mBollEntities.size(); i++) {
                float devition = getStandardDevition(i);
                BOLL boll = mBollEntities.get(i);
                upper = boll.getBollval() + DEFAULT_BOLL_FACTOR * devition;
                lower = boll.getBollval() - DEFAULT_BOLL_FACTOR * devition;
                boll.setUpper(upper);
                boll.setLower(lower);
            }
        }

        float bollval, upper, lower;
        float maxval = Float.MIN_VALUE, minval = Float.MAX_VALUE;
        for (int i = mKLineStart; i < mKLineEnd; i++) {
            BOLL boll = mBollEntities.get(i);
            upper = boll.getUpper();
            lower = boll.getLower();

            KLineDesc kLineDesc = mKLineEntities.get(i);
            float high = kLineDesc.getFHigh();
            float low = kLineDesc.getFLow();
            maxval = MathUtil.getMaxVal(maxval, high, upper);
            minval = MathUtil.getMinVal(minval, low, lower);
        }

        mRes.mKLinePathMA1.reset();
        mRes.mKLinePathMA2.reset();
        mRes.mKLinePathMA3.reset();

        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;

        drawIndicatorReferenceLine(canvas, index, false);
        float startx = mRes.mKLineStrokeWidth / 2 + pillarWidth / 2;

        for (int i = mKLineStart; i < mKLineEnd; i++) {
            BOLL boll = mBollEntities.get(i);
            bollval = boll.getBollval();
            upper = boll.getUpper();
            lower = boll.getLower();

            float upperHeight = bottomLimit + ((maxval - upper) / (maxval - minval)) * bottomHeight;
            float bollvalHeight = bottomLimit + ((maxval - bollval) / (maxval - minval)) * bottomHeight;
            float lowerHeight = bottomLimit + ((maxval - lower) / (maxval - minval)) * bottomHeight;

            if (i - mKLineStart == 0) {
                mRes.mKLinePathMA1.moveTo(startx, upperHeight);
                mRes.mKLinePathMA2.moveTo(startx, bollvalHeight);
                mRes.mKLinePathMA3.moveTo(startx, lowerHeight);
            } else {
                mRes.mKLinePathMA1.lineTo(startx, upperHeight);
                mRes.mKLinePathMA2.lineTo(startx, bollvalHeight);
                mRes.mKLinePathMA3.lineTo(startx, lowerHeight);
            }

            //画美国线
            KLineDesc kLineDesc = mKLineEntities.get(i);
            float high = kLineDesc.getFHigh();
            float low = kLineDesc.getFLow();
            float end = kLineDesc.getFClose();
            float begin = kLineDesc.getFOpen();
            float highHeight = bottomLimit + ((maxval - high) / (maxval - minval)) * bottomHeight;
            float lowHeight = bottomLimit + ((maxval - low) / (maxval - minval)) * bottomHeight;
            float endHeight = bottomLimit + ((maxval - end) / (maxval - minval)) * bottomHeight;
            float beginHeight = bottomLimit + ((maxval - begin) / (maxval - minval)) * bottomHeight;

            mRes.mPaintLine.setStrokeWidth(mRes.mKLineStrokeWidth);
            if (end > begin) {
                mRes.mPaintLine.setColor(mRes.mKColorRed);
            } else if (end == begin) {
                float yesterdayEnd = 0;
                if (i > 0) {
                    yesterdayEnd = mKLineEntities.get(i - 1).getFClose();
                }
                if (end < yesterdayEnd) {
                    mRes.mPaintLine.setColor(mRes.mKColorGreen);
                } else {
                    mRes.mPaintLine.setColor(mRes.mKColorRed);
                }
            } else {
                mRes.mPaintLine.setColor(mRes.mKColorGreen);
            }
            canvas.drawLine(startx, lowHeight, startx, highHeight, mRes.mPaintLine);
            canvas.drawLine(startx - pillarWidth / 2, beginHeight, startx, beginHeight, mRes.mPaintLine);
            canvas.drawLine(startx + pillarWidth / 2, endHeight, startx, endHeight, mRes.mPaintLine);

            startx += itemWidth;
        }

        mRes.mPaintLine.setColor(mRes.mKLineAverageColor1);
        canvas.drawPath(mRes.mKLinePathMA1, mRes.mPaintLine);
        mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
        canvas.drawPath(mRes.mKLinePathMA2, mRes.mPaintLine);
        mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
        canvas.drawPath(mRes.mKLinePathMA3, mRes.mPaintLine);

        //画左上角的指标参数
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getBOLLPreText(), y);
    }

    private void drawKlineIndicatorBreak(Canvas canvas, final int index){
        if (mKLineEntities.size() < K_BREAK_2){//数据太少无法绘制
            return;
        }

        final int indicatorBottom = mIndicatorsRectBottom[index];
        int count = mKLineEntities.size();
        float difmax = -Float.MAX_VALUE, difmin = Float.MAX_VALUE;

        if (null == mBreakEntities || mBreakEntities.isEmpty() || mBreakEntities.size() != count) {
            mBreakEntities = new ArrayList<>(count);
            mBreakPointEntities = new ArrayList<>();


            ArrayList<Float> ema12Array = new ArrayList<>(count);
            ArrayList<Float> ema26Array = new ArrayList<>(count);
            ArrayList<Float> deaArray = new ArrayList<>(count);
            ArrayList<Float> diffArray = new ArrayList<>(count);
            float[] diff26 = new float[count];

            for (int i = 0 ; i < mKlineCount ; i++){
                KLineDesc desc = mKLineEntities.get(i);
                float ema12, ema12Old, ema26, ema26Old, dea, deaOld, diff, diffOld;
                if (i > 0){
                    ema12Old = ema12Array.get(i - 1);
                    ema26Old = ema26Array.get(i - 1);
                    ema12 = (2 * desc.getFClose() + ema12Old * (K_BREAK_1 - 1))/(K_BREAK_1 + 1);
                    ema26 = (2 * desc.getFClose() + ema26Old * (K_BREAK_2 - 1))/(K_BREAK_2 + 1);
                    diff26[i] = ema12 - ema26;
                }else{
                    ema12 = desc.getFClose() * 2 / (K_BREAK_1 + 1);
                    ema26 = desc.getFClose() * 2 / (K_BREAK_2 + 1);
                    diff26[0] = 0;
                }
                ema12Array.add(ema12);
                ema26Array.add(ema26);

                if (i > 0){
                    diffOld = diffArray.get(i - 1);
                    diff = (2 * diff26[i] + diffOld * (K_BREAK_4 - 1))/(K_BREAK_4 + 1);
                }else{
                    diff = 2 * diff26[0]/(K_BREAK_4 + 1);
                }
                diffArray.add(diff);

                if (i > 0){
                    deaOld = deaArray.get(i - 1);
                    dea = (2 * diffArray.get(i) + deaOld * (K_BREAK_3 - 1)) / (K_BREAK_3 + 1);
                }else{
                    dea = diffArray.get(0)*2/(K_BREAK_3 + 1);
                }
                deaArray.add(dea);

                mBreakEntities.add(new Break(diffArray.get(i), deaArray.get(i)));
            }

            calculateBreakPoint();
            System.gc();//
        }

        for (int m = mKLineStart; m < mKLineEnd; m++) {
            Break entity = mBreakEntities.get(m);
            float diffVal = entity.getDif();
            float deaVal = entity.getDea();
            difmax = MathUtil.getMaxVal(difmax, diffVal, deaVal);
            difmin = MathUtil.getMinVal(difmin, diffVal, deaVal);
        }

        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;
        float startX = mRes.mKLineStrokeWidth / 2;

        mRes.mKLineMACDDiffPath.reset();
        mRes.mKLineMACDDeaPath.reset();
        drawIndicatorReferenceLine(canvas, index, false);
        float indicatorMiddle =  difmax * mRes.mIndicatorRectHeight / (difmax - difmin) - mRes.mIndicatorRectHeight + indicatorBottom;

        for (int i = mKLineStart; i < mKLineEnd; i++) {
            Break entity = mBreakEntities.get(i);

            float dif = entity.getDif();
            float dea = entity.getDea();
            float diffHeight = indicatorMiddle + getMACDDrawingHeight(dif, difmin, difmax);
            float deaHeight = indicatorMiddle + getMACDDrawingHeight(dea, difmin, difmax);

            if (i == mKLineStart) {
                mRes.mKLineMACDDiffPath.moveTo(startX + pillarWidth / 2, diffHeight);
                mRes.mKLineMACDDeaPath.moveTo(startX + pillarWidth / 2, deaHeight);
            } else {
                mRes.mKLineMACDDiffPath.lineTo(startX + pillarWidth / 2, diffHeight);
                mRes.mKLineMACDDeaPath.lineTo(startX + pillarWidth / 2, deaHeight);
            }

            if (i >= 120){
                BreakPoint breakPoint = mBreakPointEntities.get(i - 120);
                if (breakPoint.isBreakDown()){
                    float top = diffHeight;
                    if (top + mRes.mBreakDownPointBitmap.getHeight() > indicatorBottom){
                        canvas.drawBitmap(mRes.mBreakDownPointBitmapRotate,
                                startX + pillarWidth / 2 - mRes.mBreakDownPointBitmapRotate.getWidth()/2,
                                 top - mRes.mBreakDownPointBitmapRotate.getHeight(),
                                null);
                    }else{
                        canvas.drawBitmap(mRes.mBreakDownPointBitmap,
                                startX + pillarWidth / 2 - mRes.mBreakDownPointBitmap.getWidth()/2,
                                top,
                                null);
                    }

                }

                if (breakPoint.isBreakUp()){
                    float top = diffHeight - mRes.mBreakUpPointBitmap.getHeight();
                    if (top < indicatorBottom - mRes.mIndicatorRectHeight){
                        canvas.drawBitmap(mRes.mBreakUpPointBitmapRotate,
                                startX + pillarWidth / 2 - mRes.mBreakUpPointBitmapRotate.getWidth()/2,
                                top + mRes.mBreakUpPointBitmapRotate.getHeight(), null);
                    }else{
                        canvas.drawBitmap(mRes.mBreakUpPointBitmap,
                                startX + pillarWidth / 2 - mRes.mBreakDownPointBitmap.getWidth()/2,
                                top, null);
                    }

                }
            }
            startX += itemWidth;
        }

        mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
        canvas.drawPath(mRes.mKLineMACDDiffPath, mRes.mPaintLine);
        mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
        canvas.drawPath(mRes.mKLineMACDDeaPath, mRes.mPaintLine);

        //画左上角的指标参数
        float y = indicatorBottom - mRes.mIndicatorRectHeight;
        drawIndicatorPreText(canvas, getBreakPreText(), y);

        if (!isSharedForBreak){
            float drawWidth = mRes.mShareForMoreBitmap.getWidth() * mRes.mIndicatorRectHeight / mRes.mShareForMoreBitmap.getHeight();
            mShareRect = null == mShareRect ? new RectF[mIndicatorNum] : mShareRect;
            mShareRect[index] = new RectF(mDrawingWidth - drawWidth,indicatorBottom - mRes.mIndicatorRectHeight + 2
                    , mDrawingWidth - 2, indicatorBottom - 2);
            canvas.drawBitmap(mRes.mShareForMoreBitmap,
                    new Rect(0, 0, mRes.mShareForMoreBitmap.getWidth(), mRes.mShareForMoreBitmap.getHeight()),
                    mShareRect[index], null);
        }
    }

    private void calculateBreakPoint(){
        int count = mKLineEntities.size();
        int preBreakUpIndex = 0;
        if (count < 120){//小于120根无法计算
            return;
        }
        if (null != mBreakPointEntities && mBreakPointEntities.size() != 0){//已经计算不需要重新计算
            return;
        }
        double[] c1 = new double[count];

        for (int i = 1 ; i < count ; i++){
            c1[i] = (mKLineEntities.get(i).getFHigh() - mKLineEntities.get(i - 1).getFClose())
                    /mKLineEntities.get(i - 1).getFClose()*100;
        }
        double[] outHigh = new double[count];
        double[] outLow = new double[count];
        double[] increase = new double[count];
        double[] ma3 = new double[count];
        double[] ma5 = new double[count];
        double[] ma10 = new double[count];
        double[] ma20 = new double[count];
        double[] ma25 = new double[count];
        double[] ma60 = new double[count];
        double[] ma90 = new double[count];
        double[] ma120 = new double[count];
        double[] a1 = new double[count];
        double[] a2 = new double[count];
        double[] a3 = new double[count];
        double[] a4 = new double[count];
        double[] a5 = new double[count];
        double[] a6 = new double[count];
        double[] a7 = new double[count];
        double[] a8 = new double[count];
        double[] a9 = new double[count];
        double[] a10 = new double[count];
        double[] a11 = new double[count];
        double[] a12 = new double[count];
        double[] a13 = new double[count];
        double[] a14 = new double[count];
        double[] a15 = new double[count];
        double[] a16 = new double[count];
        double[] a17 = new double[count];
        double[] a18 = new double[count];
        double[] a19 = new double[count];
        double[] a20 = new double[count];
        boolean[] a21 = new boolean[count];
        double[] median = new double[count];
        double[] b1 = new double[count];
        double[] b2 = new double[count];
        double[] b3 = new double[count];
        double[] b4 = new double[count];
        double[] b5 = new double[count];
        double[] b6 = new double[count];
        double[] b7 = new double[count];
        double[] b8 = new double[count];
        double[] b9 = new double[count];
        double[] b10 = new double[count];
        double[] b11 = new double[count];
        double[] b12 = new double[count];
        double[] b13 = new double[count];
        double[] b14 = new double[count];
        double[] b15 = new double[count];
        double[] b16 = new double[count];
        double[] b17 = new double[count];
        boolean[] b18 = new boolean[count];
        double[] std5 = new double[count];
        double[] std10 = new double[count];
        double[] ub5 = new double[count];
        double[] ub10 = new double[count];
        boolean[] mBreakUpPoints = new boolean[count];

        core.max(0, count - 1, mHighs, 60, outBegIdx, outNBElement, outHigh);//计算60日的最高价
        core.min(0, count - 1, mLows, 60, outBegIdx, outNBElement, outLow);//计算60日的最低价
        core.sma(0, count - 1, mCloses, 3, outBegIdx, outNBElement, ma3);
        core.sma(0, count - 1, mCloses, 5, outBegIdx, outNBElement, ma5);
        core.sma(0, count - 1, mCloses, 10, outBegIdx, outNBElement, ma10);
        core.sma(0, count - 1, mCloses, 20, outBegIdx, outNBElement, ma20);
        core.sma(0, count - 1, mCloses, 25, outBegIdx, outNBElement, ma25);
        core.sma(0, count - 1, mCloses, 60, outBegIdx, outNBElement, ma60);
        core.sma(0, count - 1, mCloses, 90, outBegIdx, outNBElement, ma90);
        core.sma(0, count - 1, mCloses, 120, outBegIdx, outNBElement, ma120);
        core.stdDev(0, count - 1, mCloses, 5, 1.0, outBegIdx, outNBElement, std5);
        core.stdDev(0, count - 1, mCloses, 10, 1.0, outBegIdx, outNBElement, std10);

        for (int i = 120 ; i < count ; i++){
            KLineDesc currentDesc = mKLineEntities.get(i);
            KLineDesc pre1Desc = mKLineEntities.get(i - 1);
            KLineDesc pre6Desc = mKLineEntities.get(i - 6);
            KLineDesc pre7Desc = mKLineEntities.get(i - 7);
            KLineDesc pre8Desc = mKLineEntities.get(i - 8);
            KLineDesc pre9Desc = mKLineEntities.get(i - 9);
            KLineDesc pre10Desc = mKLineEntities.get(i - 10);
            KLineDesc pre11Desc = mKLineEntities.get(i - 11);
            KLineDesc pre12Desc = mKLineEntities.get(i - 12);
            KLineDesc pre13Desc = mKLineEntities.get(i - 13);
            KLineDesc pre14Desc = mKLineEntities.get(i - 14);
            KLineDesc pre15Desc = mKLineEntities.get(i - 15);
            KLineDesc pre16Desc = mKLineEntities.get(i - 16);
            KLineDesc pre17Desc = mKLineEntities.get(i - 17);
            KLineDesc pre18Desc = mKLineEntities.get(i - 18);
            KLineDesc pre19Desc = mKLineEntities.get(i - 19);
            KLineDesc pre20Desc = mKLineEntities.get(i - 20);
            KLineDesc pre21Desc = mKLineEntities.get(i - 21);

            a1[i] = mBreakEntities.get(i).getDif();
            a2[i] = mBreakEntities.get(i).getDea();
            median[i] = (outHigh[i] + outLow[i]) / 2;
            increase[i] = (currentDesc.getFHigh() - pre1Desc.getFClose())/pre1Desc.getFClose()*100;

            a3[i] = (ma25[(i + count - 25 + 1)%count] - ma60[(i + count - 60 + 1)%count])
                    /ma60[(i + count - 60 + 1)%count]*100;
            a4[i] = (ma25[(i + count - 25 + 1)%count] - ma90[(i + count - 90 + 1)%count])
                    /ma90[(i + count - 90 + 1)%count]*100;
            a5[i] = (ma25[(i + count - 25 + 1)%count] - ma120[(i + count - 120 + 1)%count])
                    /ma120[(i + count - 120 + 1)%count]*100;
            a6[i] = currentDesc.getFClose() >= currentDesc.getFOpen() ? currentDesc.getFClose() : currentDesc.getFOpen();
            a7[i] = (currentDesc.getFHigh() - a6[i])/a6[i]*100;
            a8[i] = (currentDesc.getFHigh() - pre10Desc.getFClose())/pre10Desc.getFClose()*100;
            a9[i] = (currentDesc.getFHigh() - pre11Desc.getFClose())/pre11Desc.getFClose()*100;
            a10[i] = (currentDesc.getFHigh() - pre12Desc.getFClose())/pre12Desc.getFClose()*100;
            a11[i] = (currentDesc.getFHigh() - pre13Desc.getFClose())/pre13Desc.getFClose()*100;
            a12[i] = (currentDesc.getFHigh() - pre14Desc.getFClose())/pre14Desc.getFClose()*100;
            a13[i] = (currentDesc.getFHigh() - pre15Desc.getFClose())/pre15Desc.getFClose()*100;
            a14[i] = (currentDesc.getFHigh() - pre16Desc.getFClose())/pre16Desc.getFClose()*100;
            a15[i] = (currentDesc.getFHigh() - pre17Desc.getFClose())/pre17Desc.getFClose()*100;
            a16[i] = (currentDesc.getFHigh() - pre18Desc.getFClose())/pre18Desc.getFClose()*100;
            a17[i] = (currentDesc.getFHigh() - pre19Desc.getFClose())/pre19Desc.getFClose()*100;
            a18[i] = (currentDesc.getFHigh() - pre20Desc.getFClose())/pre20Desc.getFClose()*100;
            a19[i] = (currentDesc.getFHigh() - pre21Desc.getFClose())/pre21Desc.getFClose()*100;
            a20[i] = 100;
            a21[i] = a8[i] < a20[i] && a9[i] < a20[i] && a10[i] < a20[i] && a11[i] < a20[i] &&
                    a12[i] < a20[i] && a13[i] < a20[i] && a14[i] < a20[i] && a15[i] < a20[i] &&
                    a16[i] < a20[i] && a17[i] < a20[i] && a18[i] < a20[i] && a19[i] < a20[i];

            b1[i] = 30;
            b2[i] = (currentDesc.getFLow() - pre6Desc.getFHigh()) / pre6Desc.getFHigh() * 100;
            b3[i] = (currentDesc.getFLow() - pre7Desc.getFHigh()) / pre7Desc.getFHigh() * 100;
            b4[i] = (currentDesc.getFLow() - pre8Desc.getFHigh()) / pre8Desc.getFHigh() * 100;
            b5[i] = (currentDesc.getFLow() - pre9Desc.getFHigh()) / pre9Desc.getFHigh() * 100;
            b6[i] = (currentDesc.getFLow() - pre10Desc.getFHigh()) / pre10Desc.getFHigh() * 100;
            b7[i] = (currentDesc.getFLow() - pre11Desc.getFHigh()) / pre11Desc.getFHigh() * 100;
            b8[i] = (currentDesc.getFLow() - pre12Desc.getFHigh()) / pre12Desc.getFHigh() * 100;
            b9[i] = (currentDesc.getFLow() - pre13Desc.getFHigh()) / pre13Desc.getFHigh() * 100;
            b10[i] = (currentDesc.getFLow() - pre14Desc.getFHigh()) / pre14Desc.getFHigh() * 100;
            b11[i] = (currentDesc.getFLow() - pre15Desc.getFHigh()) / pre15Desc.getFHigh() * 100;
            b12[i] = (currentDesc.getFLow() - pre16Desc.getFHigh()) / pre16Desc.getFHigh() * 100;
            b13[i] = (currentDesc.getFLow() - pre17Desc.getFHigh()) / pre17Desc.getFHigh() * 100;
            b14[i] = (currentDesc.getFLow() - pre18Desc.getFHigh()) / pre18Desc.getFHigh() * 100;
            b15[i] = (currentDesc.getFLow() - pre19Desc.getFHigh()) / pre19Desc.getFHigh() * 100;
            b16[i] = (currentDesc.getFLow() - pre20Desc.getFHigh()) / pre20Desc.getFHigh() * 100;
            b17[i] = (currentDesc.getFLow() - pre21Desc.getFHigh()) / pre21Desc.getFHigh() * 100;
            b18[i] = b2[i] < b1[i] && b3[i] < b1[i] && b4[i] < b1[i] && b5[i] < b1[i] &&
                    b6[i] < b1[i] && b7[i] < b1[i] && b8[i] < b1[i] && b9[i] < b1[i] &&
                    b10[i] < b1[i] && b11[i] < b1[i] && b12[i] < b1[i] && b13[i] < b1[i] &&
                    b14[i] < b1[i] && b15[i] < b1[i] && b16[i] < b1[i] && b17[i] < b1[i];

            double mid5 = ma5[(i + count - 5 + 1) % count];//取五日均线
            ub5[i] = mid5 + 2 * std5[(i + count - 5 + 1) % count];
            double mid10 = ma10[(i + count - 10 + 1) % count];
            ub10[i] = mid10 + 2 * std10[(i + count - 10 + 1) % count];

            int c1Count = 0, c1Count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0, count7 = 0, count8 = 0, count9 = 0;
            for (int j = i ; j > i - 80 ; j--){
                if (c1[j] > 4.5){
                    c1Count++;
                }
            }
            for (int j = i ; j > i - 60 ; j--){
                int index = (j + count - 60 + 1) % count;
                if (c1[j] > 3){
                    c1Count2++;
                }
                if (ma60[index] > ma60[index - 1 > 0 ? index - 1 : 0]){
                    count3++;
                }

                if (a21[j]){
                    count4++;
                }
            }
            for (int j = i ; j > i - 20 ; j--){
                if (b18[j]){
                   count5++;
                }
            }

            for (int j = i ; j > i - 90 ; j--){
                int index = (j + count - 90 + 1) % count;
                if (ma90[index] > ma90[index - 1 > 0 ? index - 1: 0]){
                    count6++;
                }
                if (a21[j]){
                    count7++;
                }
            }
            for (int j = i ; j > i - 120 ; j--){
                int index = (j + count - 120 + 1) % count;
                if (ma120[index] > ma120[index > 0 ? index - 1: 0]){
                    count8++;
                }
                if (a21[i]){
                    count9++;
                }
            }

            boolean isMACDBuy = mBreakEntities.get(i - 1).getDif() <= mBreakEntities.get(i - 1).getDea()
                    && mBreakEntities.get(i).getDif() >= mBreakEntities.get(i).getDea();
            boolean isMACDSell = mBreakEntities.get(i - 1).getDif() >= mBreakEntities.get(i - 1).getDea()
                    && mBreakEntities.get(i).getDif() <= mBreakEntities.get(i).getDea();

            boolean c4 = c1Count >= 5 && c1Count2 >= 10;
            boolean d1 = isMACDBuy && count3 >= 20 && a3[i] < 10 && c4 && count4 >= 60 && count5 >= 20 && increase[i] > 3.5
                    && currentDesc.getFLow() < ma25[(i + count - 25 + 1) % count] && currentDesc.getFHigh() > ma25[(i + count - 25 + 1) % count];

            boolean d2 = count6 >= 30 && a4[i] < 10 && c4
                    && count7 >= 90
                    && count5 >= 20
                    && increase[i] > 3.5
                    && currentDesc.getFLow() < ma25[(i + count - 25 + 1) % count] && currentDesc.getFHigh() > ma25[(i + count - 25 + 1) % count];

            boolean d3 = count8 >= 30 && a5[i] < 10 && c4 && count9 >= 120 && count5 >= 20 && increase[i] > 3.5
                    && currentDesc.getFLow() < ma25[(i + count - 25 + 1) % count] && currentDesc.getFHigh() > ma25[(i + count - 25 + 1) % count];
            boolean isBreakUpPoint = d1 || d2 || d3 && currentDesc.getFClose() > currentDesc.getFOpen();//买入点
            mBreakUpPoints[i] = isBreakUpPoint;
            double ff = (currentDesc.getFHigh() - mKLineEntities.get(preBreakUpIndex).getFClose())
                    /mKLineEntities.get(preBreakUpIndex).getFClose()*100;
            boolean e3 = preBreakUpIndex == 0 ? false : ff > 15 && i - preBreakUpIndex < 30 && isMACDSell;
            boolean e2 = preBreakUpIndex == 0 ? false : ff > 15 && i - preBreakUpIndex < 30 &&
                    ub5[i] < ub5[i - 1] && ub5[i - 1] >= ub5[i - 2] &&
                    ub5[i - 2] >= ub5[i - 3] && ub10[i] >= ub10[i - 1] && ub10[i - 1] >= ub10[i - 2];
            boolean isBreakDownPoint = e2 || e3;//减仓


            if (isBreakUpPoint){
                preBreakUpIndex = i;
            }
            int breakUpCount = 0;
            for (int j = i ; j > i - 30; j--){
                if (mBreakUpPoints[j]){
                    breakUpCount++;
                }
            }
            mBreakPointEntities.add(new BreakPoint(i, breakUpCount == 1 && mBreakUpPoints[i], isBreakDownPoint));

        }
    }

    private void drawKLineIndicatorMACD(Canvas canvas, final int index) {
        final int indicatorBottom = mIndicatorsRectBottom[index];
        float dea9 = 0, ema12 = 0, ema26 = 0, difmax = -Float.MAX_VALUE, difmin = Float.MAX_VALUE;
        if (null == mMacdEntitys || mMacdEntitys.isEmpty() || mMacdEntitys.size() != mKLineEntities.size()) {
            calculateMACD(ema12, ema26, dea9);
        }

        for (int m = mKLineStart; m < mKLineEnd; m++) {
            MACD entity = mMacdEntitys.get(m);
            float macdVal = entity.getMacd();
            float diffVal = entity.getDif();
            float deaVal = entity.getDea();
            difmax = MathUtil.getMaxVal(difmax, diffVal, deaVal, macdVal);
            difmin = MathUtil.getMinVal(difmin, diffVal, deaVal, macdVal);
        }

        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;
        float startX = mRes.mKLineStrokeWidth / 2;

        mRes.mKLineMACDDiffPath.reset();
        mRes.mKLineMACDDeaPath.reset();

        float indicatorMiddle;

        if(difmax - difmin != 0) {
            indicatorMiddle =  difmax * mRes.mIndicatorRectHeight / (difmax - difmin) - mRes.mIndicatorRectHeight + indicatorBottom;
            drawIndicatorReferenceLine(canvas, index, indicatorMiddle, false);
            for (int i = mKLineStart; i < mKLineEnd; i++) {
                MACD entity = mMacdEntitys.get(i);
                float macd = entity.getMacd();

                if (macd >= 0) {
                    mRes.mPaintKLine.setStyle(Paint.Style.FILL_AND_STROKE);
                    mRes.mPaintKLine.setStrokeWidth(mRes.mKLineStrokeWidth);
                    mRes.mPaintKLine.setColor(mRes.mKColorRed);
                    mRes.mPaintLine.setColor(mRes.mKColorRed);
                } else {
                    mRes.mPaintKLine.setStyle(Paint.Style.FILL_AND_STROKE);
                    mRes.mPaintKLine.setColor(mRes.mKColorGreen);
                }

                float height = getMACDDrawingHeight(macd, difmin, difmax);
                float top, bottom;
                if(height >= 0) {
                    top = indicatorMiddle + height;
                    bottom = indicatorMiddle;
                } else {
                    top = indicatorMiddle;
                    bottom  = indicatorMiddle + height;
                }

                canvas.drawRect(startX, top, startX + pillarWidth, bottom, mRes.mPaintKLine);

                float dif = entity.getDif();
                float dea = entity.getDea();
                float diffHeight = indicatorMiddle + getMACDDrawingHeight(dif, difmin, difmax);
                float deaHeight = indicatorMiddle + getMACDDrawingHeight(dea, difmin, difmax);

                if (i == mKLineStart) {
                    mRes.mKLineMACDDiffPath.moveTo(startX + pillarWidth / 2, diffHeight);
                    mRes.mKLineMACDDeaPath.moveTo(startX + pillarWidth / 2, deaHeight);
                } else {
                    mRes.mKLineMACDDiffPath.lineTo(startX + pillarWidth / 2, diffHeight);
                    mRes.mKLineMACDDeaPath.lineTo(startX + pillarWidth / 2, deaHeight);
                }

                startX += itemWidth;
            }

            mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
            canvas.drawPath(mRes.mKLineMACDDiffPath, mRes.mPaintLine);
            mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
            canvas.drawPath(mRes.mKLineMACDDeaPath, mRes.mPaintLine);
        } else {
            indicatorMiddle = indicatorBottom - mRes.mIndicatorRectHeight / 2;
            drawIndicatorReferenceLine(canvas, index, indicatorMiddle, false);
            if(mTimeLineMacdEntitys != null && !mTimeLineMacdEntitys.isEmpty()) {
                float endX = startX + (mTimeLineMacdEntitys.size() - 1) * itemWidth;
                mRes.mPaintLine.setColor(mRes.mTimeAverageLineColor);
                canvas.drawLine(startX, indicatorMiddle, endX, indicatorMiddle, mRes.mPaintLine);
            }
        }

        //画左上角的指标参数
        float y = indicatorBottom - mRes.mIndicatorRectHeight;
        drawIndicatorPreText(canvas, getMACDPreText(), y);
    }


    private void drawTimeLineIndicatorMACD(Canvas canvas, final int index) {
        final int indicatorBottom = mIndicatorsRectBottom[index];
        float dea9 = 0, ema12 = 0, ema26 = 0, difmax = -Float.MAX_VALUE, difmin = Float.MAX_VALUE;
        List<TrendDesc> timeEntities = mTimeLineDataCache.getTimeLineEntities(0);
        if (null == mTimeLineMacdEntitys || mTimeLineMacdEntitys.isEmpty() || mTimeLineMacdEntitys.size() != timeEntities.size()) {
            mTimeLineMacdEntitys = new ArrayList<MACD>();

            for (int i = 0; i < timeEntities.size(); i++) {
                TrendDesc entity = timeEntities.get(i);
                float close = entity.getFNow();
                if (i == 0) {
                    mTimeLineMacdEntitys.add(new MACD(0, 0, 0));
                    ema12 = ema26 = close;
                    continue;
                }
                ema12 = computeEma(ema12, close, K_MACD_1);
                ema26 = computeEma(ema26, close, K_MACD_2);

                float diff = ema12 - ema26;
                dea9 = (dea9 * (K_MACD_3 - 1) + diff * 2) / (K_MACD_3 + 1);
                float macd = 2 * (diff - dea9);
                mTimeLineMacdEntitys.add(new MACD(macd, diff, dea9));
            }
        }

        for (int m = 0; m < mTimeLineMacdEntitys.size(); m++) {
            MACD entity = mTimeLineMacdEntitys.get(m);
            float macdVal = entity.getMacd();
            float diffVal = entity.getDif();
            float deaVal = entity.getDea();
            difmax = MathUtil.getMaxVal(difmax, diffVal, deaVal, macdVal);
            difmin = MathUtil.getMinVal(difmin, diffVal, deaVal, macdVal);
        }

        float itemWidth = mRes.mTimeHorizontalGap;
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;
        float startX = mRes.mKLineStrokeWidth / 2;

        mRes.mKLineMACDDiffPath.reset();
        mRes.mKLineMACDDeaPath.reset();

        float indicatorMiddle;
        if(difmax - difmin != 0) {
            indicatorMiddle =  difmax * mRes.mIndicatorRectHeight / (difmax - difmin) - mRes.mIndicatorRectHeight + indicatorBottom;
            drawIndicatorReferenceLine(canvas, index, indicatorMiddle, true);
            for (int i = 0, size=mTimeLineMacdEntitys.size(); i < size; i++) {
                MACD entity = mTimeLineMacdEntitys.get(i);
                float macd = entity.getMacd();
                if (macd >= 0) {
                    mRes.mPaintKLine.setStyle(Paint.Style.FILL_AND_STROKE);
                    mRes.mPaintKLine.setStrokeWidth(mRes.mKLineStrokeWidth);
                    mRes.mPaintKLine.setColor(mRes.mKColorRed);
                    mRes.mPaintLine.setColor(mRes.mKColorRed);
                } else {
                    mRes.mPaintKLine.setStyle(Paint.Style.FILL_AND_STROKE);
                    mRes.mPaintKLine.setColor(mRes.mKColorGreen);
                }

                float drawingHeight = getMACDDrawingHeight(macd, difmin, difmax);
                float top, bottom;
                if(drawingHeight >= 0) {
                    top = indicatorMiddle + drawingHeight;
                    bottom = indicatorMiddle;
                } else {
                    top = indicatorMiddle;
                    bottom  = indicatorMiddle + drawingHeight;
                }
                canvas.drawRect(startX, top, startX + pillarWidth, bottom, mRes.mPaintKLine);

                float dif = entity.getDif();
                float dea = entity.getDea();
                float diffHeight = indicatorMiddle + getMACDDrawingHeight(dif, difmin, difmax);
                float deaHeight = indicatorMiddle + getMACDDrawingHeight(dea, difmin, difmax);

                if (i == 0) {
                    mRes.mKLineMACDDiffPath.moveTo(startX + pillarWidth / 2, diffHeight);
                    mRes.mKLineMACDDeaPath.moveTo(startX + pillarWidth / 2, deaHeight);
                } else {
                    mRes.mKLineMACDDiffPath.lineTo(startX + pillarWidth / 2, diffHeight);
                    mRes.mKLineMACDDeaPath.lineTo(startX + pillarWidth / 2, deaHeight);
                }

                startX += itemWidth;
            }
            mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
            canvas.drawPath(mRes.mKLineMACDDiffPath, mRes.mPaintLine);
            mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
            canvas.drawPath(mRes.mKLineMACDDeaPath, mRes.mPaintLine);
        } else {
            indicatorMiddle = indicatorBottom - mRes.mIndicatorRectHeight / 2;
            drawIndicatorReferenceLine(canvas, index, indicatorMiddle, true);
            if(mTimeLineMacdEntitys != null && !mTimeLineMacdEntitys.isEmpty()) {
                float endX = startX + (mTimeLineMacdEntitys.size() - 1) * itemWidth;
                mRes.mPaintLine.setColor(mRes.mTimeAverageLineColor);
                canvas.drawLine(startX, indicatorMiddle, endX, indicatorMiddle, mRes.mPaintLine);
            }
        }

        //画左上角的指标参数
        float y = indicatorBottom - mRes.mIndicatorRectHeight;
        drawIndicatorPreText(canvas, getMACDPreText(), y);
    }

    private void drawKLineIndicatorBBI(Canvas canvas, final int index) {
        final int indicatorBottom = mIndicatorsRectBottom[index];
        float startx = /*left*/0;
        float maxval = Float.MIN_VALUE;
        float minval = Float.MAX_VALUE;
        if (null == mBBIEntities || mBBIEntities.isEmpty() || mBBIEntities.size() != mBBIEntities.size()) {
            mBBIEntities = new ArrayList<>();
            double[] ma1 = new double[mKlineCount];
            double[] ma2 = new double[mKlineCount];
            double[] ma3 = new double[mKlineCount];
            double[] ma4 = new double[mKlineCount];

            core.sma(0, mKlineCount - 1, mCloses, K_BBI_1, outBegIdx, outNBElement, ma1);
            core.sma(0, mKlineCount - 1, mCloses, K_BBI_2, outBegIdx, outNBElement, ma2);
            core.sma(0, mKlineCount - 1, mCloses, K_BBI_3, outBegIdx, outNBElement, ma3);
            core.sma(0, mKlineCount - 1, mCloses, K_BBI_4, outBegIdx, outNBElement, ma4);
            for (int i = 0 ; i < mKlineCount ; i++){
                int tIndex1 = K_BBI_1 > mKlineCount ? 0 : (i + mKlineCount - K_BBI_1 + 1) % mKlineCount;
                int tIndex2 = K_BBI_2 > mKlineCount ? 0 : (i + mKlineCount - K_BBI_2 + 1) % mKlineCount;
                int tIndex3 = K_BBI_3 > mKlineCount ? 0 : (i + mKlineCount - K_BBI_3 + 1) % mKlineCount;
                int tIndex4 = K_BBI_4 > mKlineCount ? 0 : (i + mKlineCount - K_BBI_4 + 1) % mKlineCount;
                mBBIEntities.add(new BBI(mKLineEntities.get(i).fClose, (float) ((ma1[tIndex1] + ma2[tIndex2] + ma3[tIndex3] + ma4[tIndex4])/4.0f)));
            }
        }
        for (int m = mKLineStart; m < mKLineEnd; m++) {
            BBI entity = mBBIEntities.get(m);
            maxval = MathUtil.getMaxVal(maxval, entity.getPrice());
            minval = MathUtil.getMinVal(minval, entity.getPrice());
            if (m >= MathUtil.getMaxVal(K_BBI_1, K_BBI_2, K_BBI_3, K_BBI_4) - 1){
                maxval = MathUtil.getMaxVal(maxval, entity.getBbi());
                minval = MathUtil.getMinVal(minval, entity.getBbi());
            }
        }
                /* 画图start... */
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;

        drawIndicatorReferenceLine(canvas, index, false);
        startx = mRes.mKLineStrokeWidth / 2 + pillarWidth / 2;

        BBI prevBBIEntity;
        BBI bbiEntity;
        for (int j = mKLineStart; j < mKLineEnd; j++) {
            if (j == mKLineStart) {
                startx += itemWidth;
                continue;
            }

            prevBBIEntity = mBBIEntities.get(j - 1);
            bbiEntity = mBBIEntities.get(j);

            float prevPrice = prevBBIEntity.getPrice();
            float prevBBI = prevBBIEntity.getBbi();

            float prevPriceHeight = bottomLimit + ((maxval - prevPrice) / (maxval - minval)) * bottomHeight;
            float prevBBIHeight = bottomLimit + ((maxval - prevBBI) / (maxval - minval)) * bottomHeight;

            float price = bbiEntity.getPrice();
            float bbi = bbiEntity.getBbi();
            if(bbi==0 && price==0){
                continue;
            }
            float priceHeight = bottomLimit + ((maxval - price) / (maxval - minval)) * bottomHeight;
            float bbiHeight = bottomLimit + ((maxval - bbi) / (maxval - minval)) * bottomHeight;
            mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
            canvas.drawLine(startx - itemWidth, prevPriceHeight, startx, priceHeight, mRes.mPaintLine);// 价格线

            if (j >= MathUtil.getMaxVal(K_BBI_1, K_BBI_2, K_BBI_3, K_BBI_4)){
                mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
                canvas.drawLine(startx - itemWidth, prevBBIHeight, startx, bbiHeight, mRes.mPaintLine);// BBI线
            }

            startx += itemWidth;
        }
        //画左上角的指标参数
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getBBIPreText(), y);
    }

    private void drawKLineIndicatorRSI(Canvas canvas, final int index) {
        final int indicatorBottom = mIndicatorsRectBottom[index];
        float startx = /*left*/0;
        float maxrsi = Float.MIN_VALUE;// 最大值
        float minrsi = Float.MAX_VALUE;// 最小值
        float rsi1 = 100;
        float rsi2 = 100;
        float rsi3 = 100;
        if (null == mRsiList || mRsiList.isEmpty() || mRsiList.size() != mKLineEntities.size()) {
            mRsiList = new ArrayList<RSI>();
            ArrayList<Float> rsi1Maxlist = new ArrayList<Float>();
            ArrayList<Float> rsi1Abslist = new ArrayList<Float>();
            ArrayList<Float> rsi2Maxlist = new ArrayList<Float>();
            ArrayList<Float> rsi2Abslist = new ArrayList<Float>();
            ArrayList<Float> rsi3Maxlist = new ArrayList<Float>();
            ArrayList<Float> rsi3Abslist = new ArrayList<Float>();
            for (int i = 0; i < mKLineEntities.size(); i++) {
                float close = mKLineEntities.get(i).getFClose();
                float perclose = mKLineEntities.get(i - 1 < 0 ? 0 : i - 1).getFClose();
                float range = close - perclose;
                float rsi1max = 0;
                float rsi1abs = 0;
                float rsi2max = 0;
                float rsi2abs = 0;
                float rsi3max = 0;
                float rsi3abs = 0;
                if (i > 0) {
                    rsi1max = SMA(range > 0 ? range : 0, K_RSI_1, 1, rsi1Maxlist.get(i - 1));
                    rsi1abs = SMA(Math.abs(range), K_RSI_1, 1, rsi1Abslist.get(i - 1));
                    rsi2max = SMA(range > 0 ? range : 0, K_RSI_2, 1, rsi2Maxlist.get(i - 1));
                    rsi2abs = SMA(Math.abs(range), K_RSI_2, 1, rsi2Abslist.get(i - 1));
                    rsi3max = SMA(range > 0 ? range : 0, K_RSI_3, 1, rsi3Maxlist.get(i - 1));
                    rsi3abs = SMA(Math.abs(range), K_RSI_3, 1, rsi3Abslist.get(i - 1));
                    rsi1 = rsi1max / rsi1abs * 100;
                    rsi2 = rsi2max / rsi2abs * 100;
                    rsi3 = rsi3max / rsi3abs * 100;
                }
                rsi1Maxlist.add(rsi1max);
                rsi1Abslist.add(rsi1abs);
                rsi2Maxlist.add(rsi2max);
                rsi2Abslist.add(rsi2abs);
                rsi3Maxlist.add(rsi3max);
                rsi3Abslist.add(rsi3abs);
                mRsiList.add(new RSI(rsi1, rsi2, rsi3));
            }
        }
        for (int m = mKLineStart; m < mKLineEnd; m++) {
            maxrsi = MathUtil.getMaxVal(maxrsi, mRsiList.get(m).getRsi1(), mRsiList.get(m).getRsi2(), mRsiList.get(m).getRsi3());
            minrsi = MathUtil.getMinVal(minrsi, mRsiList.get(m).getRsi1(), mRsiList.get(m).getRsi2(), mRsiList.get(m).getRsi3());
        }
                /* 画图开始 */
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float bottomHeight = mRes.mIndicatorRectHeight;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;

        drawIndicatorReferenceLine(canvas, index, false);
        startx = mRes.mKLineStrokeWidth / 2 + pillarWidth / 2;

        RSI item, prevItem;
        float prev_rsi1;
        float prev_rsi2;
        float prev_rsi3;
        for (int j = mKLineStart; j < mKLineEnd; j++) {
            if (j == mKLineStart) {
                startx += itemWidth;
                continue;
            }

            prevItem = mRsiList.get(j - 1);
            item = mRsiList.get(j);

            prev_rsi1 = prevItem.getRsi1();
            prev_rsi2 = prevItem.getRsi2();
            prev_rsi3 = prevItem.getRsi3();
            float prev_rsi1starty = bottomLimit + (maxrsi - prev_rsi1) / (maxrsi - minrsi) * bottomHeight;
            float prev_rsi2starty = bottomLimit + (maxrsi - prev_rsi2) / (maxrsi - minrsi) * bottomHeight;
            float prev_rsi3starty = bottomLimit + (maxrsi - prev_rsi3) / (maxrsi - minrsi) * bottomHeight;
            prev_rsi1starty = prev_rsi1starty > bottomLimit ? prev_rsi1starty : bottomLimit;
            prev_rsi2starty = prev_rsi2starty > bottomLimit ? prev_rsi2starty : bottomLimit;
            prev_rsi3starty = prev_rsi3starty > bottomLimit ? prev_rsi3starty : bottomLimit;

            rsi1 = item.getRsi1();
            rsi2 = item.getRsi2();
            rsi3 = item.getRsi3();
            float rsi1starty = bottomLimit + (maxrsi - rsi1) / (maxrsi - minrsi) * bottomHeight;
            float rsi2starty = bottomLimit + (maxrsi - rsi2) / (maxrsi - minrsi) * bottomHeight;
            float rsi3starty = bottomLimit + (maxrsi - rsi3) / (maxrsi - minrsi) * bottomHeight;
            rsi1starty = rsi1starty > bottomLimit ? rsi1starty : bottomLimit;
            rsi2starty = rsi2starty > bottomLimit ? rsi2starty : bottomLimit;
            rsi3starty = rsi3starty > bottomLimit ? rsi3starty : bottomLimit;

            mRes.mPaintLine.setColor(mRes.K_RSI_1COLOR);
            canvas.drawLine(startx - itemWidth, prev_rsi1starty, startx, rsi1starty, mRes.mPaintLine);
            mRes.mPaintLine.setColor(mRes.K_RSI_2COLOR);
            canvas.drawLine(startx - itemWidth, prev_rsi2starty, startx, rsi2starty, mRes.mPaintLine);
            mRes.mPaintLine.setColor(mRes.K_RSI_3COLOR);
            canvas.drawLine(startx - itemWidth, prev_rsi3starty, startx, rsi3starty, mRes.mPaintLine);

            startx += itemWidth;
        }

        //画左上角的指标参数
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getRSIPreText(), y);
    }

    private void drawIndicatorReferenceLine(Canvas canvas, int index, boolean drawVertical) {
        drawIndicatorReferenceLine(canvas, index, 0, drawVertical);
    }

    private void drawIndicatorReferenceLine(Canvas canvas, int index, float indicatorMiddle, boolean drawVertical) {
        float bottom = mIndicatorsRectBottom[index];
        float top = bottom - mRes.mIndicatorRectHeight;

        float middle = indicatorMiddle > 0 ? indicatorMiddle : (bottom + top) / 2;

        if(drawVertical) {

            int count = mListener.getLineType() == LineChartTextureView.TYPE_FIVE_DAY ?
                    VERTICAL_REFERENCE_LINE_COUNT_FIVE_DAY : VERTICAL_REFERENCE_LINE_COUNT;
            float gap = mDrawingWidth / (count + 1);
            for(int i = 0; i < count ; i++) {
                float x = gap * (i + 1);
                canvas.drawLine(x, top, x, bottom, mRes.mPaintDashLine);
            }
        }

        canvas.drawLine(0, middle, mDrawingWidth, middle, mRes.mPaintDashLine);
    }

    private void calculateMACD(float ema12, float ema26, float dea9){
        mMacdEntitys = new ArrayList<MACD>();
        for (int i = 0; i < mKLineEntities.size(); i++) {
            KLineDesc entity = mKLineEntities.get(i);
            float close = entity.getFClose();
            if (i == 0) {
                mMacdEntitys.add(new MACD(0, 0, 0));
                ema12 = ema26 = close;
                continue;
            }
            ema12 = (ema12 * (K_MACD_1 - 1) + close * 2) / (K_MACD_1 + 1);
            ema26 = (ema26 * (K_MACD_2 - 1) + close * 2) / (K_MACD_2 + 1);
            float diff = ema12 - ema26;
            dea9 = (dea9 * (K_MACD_3 - 1) + diff * 2) / (K_MACD_3 + 1);
            float macd = 2 * (diff - dea9);
            mMacdEntitys.add(new MACD(macd, diff, dea9));
        }
    }

    private float getMACDDrawingHeight(float value, float diffMin, float diffMax) {
        //            DtLog.d("liqf", "now price drawing height is " + y);
        return value * mRes.mIndicatorRectHeight / (diffMin - diffMax);
    }

    private float getStandardDevition(final int i) {
        float sum = 0;
        int count = 0;
        float bollval = mBollEntities.get(i).getBollval();
        for (int j = i - DEFAULT_BOLL_MA_COUNT + 1; j <= i; j++) {
            if (j < 0) {
                continue;
            }

            float close = mKLineEntities.get(j).getFClose();
            sum += (close - bollval) * (close - bollval);
            count++;
        }
        if (count > 0) {
            return (float) Math.sqrt(sum / count);
        }
        return 0;
    }

    private static float computeEma(float ema, float close, int K_MACD) {
        BigDecimal temp1 = new BigDecimal(ema);
        BigDecimal temp2 = new BigDecimal(K_MACD - 1);

        temp1 = temp1.multiply(temp2);
        temp2 = new BigDecimal(close * 2);

        temp1 = temp1.add(temp2);
        return temp1.divide(new BigDecimal(K_MACD + 1), BigDecimal.ROUND_HALF_UP).floatValue();
    }

    private float SMA(float X,int N,int M,float perValue){
        return  (X*M+perValue*(N-M))/N;
    }

    private void drawKLineIndicatorVolume(Canvas canvas, int index) {
        final int indicatorBottom = mIndicatorsRectBottom[index];
        int entityCount = mKLineEnd - mKLineStart;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;
        float startX = mRes.mKLineStrokeWidth / 2;

        mKVolumeAverage1 = new long[entityCount];//成交量均线
        mKVolumeAverage2 = new long[entityCount];//成交量均线
        mKVolumeAverage3 = new long[entityCount];
        long averageVolume = 0;
        int count = 0;
        for (int i = 0; i < entityCount; i++) {
            averageVolume = 0;
            count = 0;
            for (int j = 0; j < AMOUNT_MA_1 && i + mKLineStart - j >= 0; j++) {
                KLineDesc entity = mKLineEntities.get(i + mKLineStart - j);
                long volume = entity.getLVolume();
                averageVolume += volume;
                count++;
            }
            if (count > 0) {
                averageVolume /= count;
            }
            mKVolumeAverage1[i] = averageVolume;
        }
        for (int i = 0; i < entityCount; i++) {
            averageVolume = 0;
            count = 0;
            for (int j = 0; j < AMOUNT_MA_2 && i + mKLineStart - j >= 0; j++) {
                KLineDesc entity = mKLineEntities.get(i + mKLineStart - j);
                long volume = entity.getLVolume();
                averageVolume += volume;
                count++;
            }
            if (count > 0) {
                averageVolume /= count;
            }
            mKVolumeAverage2[i] = averageVolume;
        }

        for (int i = 0; i < entityCount; i++) {
            averageVolume = 0;
            count = 0;
            for (int j = 0; j < AMOUNT_MA_3 && i + mKLineStart - j >= 0; j++) {
                KLineDesc entity = mKLineEntities.get(i + mKLineStart - j);
                long volume = entity.getLVolume();
                averageVolume += volume;
                count++;
            }
            if (count > 0) {
                averageVolume /= count;
            }
            mKVolumeAverage3[i] = averageVolume;
        }

        mKLineVolumeMax = 0;
        for (int i = 0; i < entityCount; i++) {
            KLineDesc entity = mKLineEntities.get(i + mKLineStart);
            float volume = entity.getLVolume();
            mKLineVolumeMax = MathUtil.getMaxVal(mKLineVolumeMax, volume, (float) mKVolumeAverage1[i], (float) mKVolumeAverage2[i],
                    (float) mKVolumeAverage3[i]);
        }
        drawIndicatorReferenceLine(canvas, index, false);

        mRes.mKLineMACDDiffPath.reset();
        mRes.mKLineMACDDeaPath.reset();
        mRes.mKLinePathMA1.reset();
        for (int i = 0; i < entityCount; i++) {
            KLineDesc entity = mKLineEntities.get(i + mKLineStart);

            float begin = entity.getFOpen();
            float end = entity.getFClose();

            if (end > begin) {
                mRes.mPaintKLine.setStyle(getKLineStyleRiseLine());
                mRes.mPaintKLine.setStrokeWidth(mRes.mKLineStrokeWidth);
                mRes.mPaintKLine.setColor(mRes.mKColorRed);
                mRes.mPaintLine.setColor(mRes.mKColorRed);
            } else if (end == begin) {
                float yesterdayEnd = 0;
                if (i > 0) {
                    yesterdayEnd = mKLineEntities.get(i + mKLineStart - 1).getFClose();
                }
                if (end < yesterdayEnd) {
                    mRes.mPaintKLine.setStyle(Paint.Style.FILL_AND_STROKE);
                    mRes.mPaintKLine.setColor(mRes.mKColorGreen);
                } else {
                    mRes.mPaintKLine.setStyle(getKLineStyleRiseLine());
                    mRes.mPaintKLine.setStrokeWidth(mRes.mKLineStrokeWidth);
                    mRes.mPaintKLine.setColor(mRes.mKColorRed);
                }
            } else {
                mRes.mPaintKLine.setStyle(Paint.Style.FILL_AND_STROKE);
                mRes.mPaintKLine.setColor(mRes.mKColorGreen);
            }

            final float volume = entity.getLVolume();
            canvas.drawRect(startX, indicatorBottom - getKLineVolumeDrawingHeight(volume) - mRes.mKLineStrokeWidth / 2, startX + pillarWidth, indicatorBottom - mRes.mKLineStrokeWidth / 2, mRes.mPaintKLine);

            final long volumeMA1 = mKVolumeAverage1[i];
            final long volumeMA2 = mKVolumeAverage2[i];
            final long volumeMA3 = mKVolumeAverage3[i];
            float diffHeight = indicatorBottom - getKLineVolumeDrawingHeight(volumeMA1);
            float deaHeight = indicatorBottom - getKLineVolumeDrawingHeight(volumeMA2);
            float ma3Height = indicatorBottom - getKLineVolumeDrawingHeight(volumeMA3);
            if (i == 0) {
                mRes.mKLineMACDDiffPath.moveTo(startX + pillarWidth / 2, diffHeight);
                mRes.mKLineMACDDeaPath.moveTo(startX + pillarWidth / 2, deaHeight);
                mRes.mKLinePathMA1.moveTo(startX + pillarWidth / 2, ma3Height);
            } else {
                mRes.mKLineMACDDiffPath.lineTo(startX + pillarWidth / 2, diffHeight);
                mRes.mKLineMACDDeaPath.lineTo(startX + pillarWidth / 2, deaHeight);
                mRes.mKLinePathMA1.lineTo(startX + pillarWidth / 2, ma3Height);
            }

            startX += itemWidth;
        }

        mRes.mPaintLine.setColor(mRes.mKLineAverageColor1);
        canvas.drawPath(mRes.mKLineMACDDiffPath, mRes.mPaintLine);
        mRes.mPaintLine.setColor(mRes.mKLineAverageColor2);
        canvas.drawPath(mRes.mKLineMACDDeaPath, mRes.mPaintLine);
        if (AMOUNT_MA_3 > 0){
            mRes.mPaintLine.setColor(mRes.mKLineAverageColor3);
            canvas.drawPath(mRes.mKLinePathMA1, mRes.mPaintLine);
        }

        //画左上角的指标参数
        float bottomLimit = indicatorBottom - mRes.mIndicatorRectHeight;
        float y = bottomLimit;
        drawIndicatorPreText(canvas, getVolumePreText(), y);
    }

    private void drawKTouchLine(Canvas canvas) {
        DtLog.d(TAG, "supportEntrance() mIsTouching = " + mIsTouching);
        if (!mIsTouching) {
            return;
        }

        if(mKLineEnd <= 0 || mKLineStart < 0) {
            return;
        }

        int index = -1;

        int entityCount = mKLineEnd - mKLineStart;
        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;
        float startX = mRes.mKLineStrokeWidth / 2;

        Paint paint = mRes.mPaintTouchLine;
        paint.setStyle(Paint.Style.STROKE);

        float drawingX = 0;

        if (mTouchX < 0) {
            index = 0;
            float touchValueHeight = getKLineDrawingHeight(mKLineEntities.get(0).getFClose());
            drawingX = pillarWidth / 2;
            updateEntraceRect((int) drawingX);
            canvas.drawLine(drawingX, mRes.mKLineAverageTextAreaHeight + computeEntraceHeight(), drawingX, mDrawingHeight, paint);
            canvas.drawLine(0, touchValueHeight, mDrawingWidth, touchValueHeight, paint);

            drawKLineAverageText(canvas, 0);
        } else if (mTouchX >= itemWidth * entityCount) {
            index = mKLineEnd - mKLineStart - 1;
            float touchValueHeight = getKLineDrawingHeight(mKLineEntities.get(mKLineEnd - 1).getFClose());
            drawingX = itemWidth * (entityCount - 1) + pillarWidth / 2;
            updateEntraceRect((int) drawingX);
            canvas.drawLine(drawingX, mRes.mKLineAverageTextAreaHeight + computeEntraceHeight(), drawingX, mDrawingHeight, paint);
            canvas.drawLine(0, touchValueHeight, mDrawingWidth, touchValueHeight, paint);

            drawKLineAverageText(canvas, entityCount - 1);
        } else {
            for (int i = 0; i < entityCount; i++) {
                KLineDesc entity = mKLineEntities.get(i + mKLineStart);

                float end = entity.getFClose();
                float endHeight = getKLineDrawingHeight(end);

                //画触摸指示线
                if (mTouchX != LineChartTextureView.INVALID_TOUCH_X) {
                    if (mTouchX >= startX && mTouchX <= startX + itemWidth) {
                        index = i;
                        drawingX = startX + pillarWidth / 2;
                        updateEntraceRect((int) drawingX);
                        canvas.drawLine(drawingX, mRes.mKLineAverageTextAreaHeight + computeEntraceHeight(), drawingX, mDrawingHeight, paint);
                        canvas.drawLine(0, endHeight, mDrawingWidth, endHeight, paint);

                        drawKLineAverageText(canvas, i);
                        break;
                    }
                }

                //移动到下一个格子起点
                startX += itemWidth;
            }
        }

        for (int i = 0; i < mIndicatorNum; i++) {
            drawKLineIndicatorValues(canvas, index + mKLineStart, i);
        }
        if(drawingX > 0) {
            updateEntraceRect((int) drawingX);
        }
    }

    public int getIndicatorNum() {
        return mIndicatorNum;
    }

    private int computeEntraceHeight() {
        return supportEntrance() ? (mEntranceRect.bottom - mEntranceRect.top) : 0;
    }

    private float drawIndicatorPreText(final Canvas canvas, String text, float top) {
        return drawIndicatorPreText(canvas, text, top, true);
    }

    private float drawIndicatorPreText(final Canvas canvas, String text, float top, boolean shouldDraw) {
        if(!TextUtils.isEmpty(text)) {
            int extraStart = text.indexOf("(");
            String preText;
            String extraText;
            if(extraStart >= 0) {
                preText = text.substring(0, extraStart);
                extraText = text.substring(extraStart, text.length());
            } else {
                preText = text;
                extraText = "";
            }

            float preTextWidth = mRes.mPreTextPaint.measureText(preText);
            int paddingLeftRight = mRes.mIndicatorPreTextPaddingLeftRight;
            int roundRadius = mRes.mIndicatorPreTextBgRoundRadius;
            top += mRes.mBorderStrokeWidth;
            float preleft = mRes.mBorderStrokeWidth;
            float bottom = top + mRes.mIndicatorPreTextBgHeight;
            float preright = preTextWidth + 2 * paddingLeftRight + mRes.mBorderStrokeWidth;
            float extraTextWidth = mRes.mPreTextPaint.measureText(extraText);

            float extraleft = preright + paddingLeftRight;
            float right = extraleft + extraTextWidth;
            if(shouldDraw) {
                mRes.mFillPaint.setColor(mRes.mIndicatorPreTextBgColor);
                RectF rectF = new RectF(preleft, top, preright, bottom);
                canvas.drawRoundRect(rectF , roundRadius, roundRadius, mRes.mFillPaint);
                mRes.mPreTextPaint.setColor(mRes.mIndicatorPreTextColor);
                float textBottom = bottom - ((mRes.mIndicatorPreTextBgHeight - mRes.mPreTextHeight + mRes.mPreTextBottom) / 2);
                canvas.drawText(preText, preleft + paddingLeftRight, textBottom, mRes.mPreTextPaint);
                canvas.drawText(extraText, extraleft, textBottom, mRes.mPreTextPaint);
            }
            return right;
        }
        return 0f;
    }

    private void drawKLineIndicatorValues(final Canvas canvas, final int index, final int indicatorIndex) {
        DtLog.d(TAG, "drawKLineIndicatorValues index = " + index);
        if (!mListener.isKLineType(mListener.mLineType) || index == -1) {
            return;
        }

        int textWidth;
        final int bottomLimit = mIndicatorsRectBottom[indicatorIndex] - mRes.mIndicatorRectHeight;
        float y = bottomLimit + mRes.mTextHeight;
        mRes.mKLineTextPaint.setTextSize(mRes.mTextSize);
        mRes.mKLineTextPaint.setTypeface(null);
        String gapStr = "    ";
        String shortGapStr = "  ";
        int defaultPrecision = 2;
        String preText = getKLineIndicatorPreText(mListener.mTradingIndicatorTypes[indicatorIndex]);
        float x = drawIndicatorPreText(canvas, preText, bottomLimit, false) + 30;
        mRes.mKLineTextPaint.setTypeface(null);
        switch (mListener.mTradingIndicatorTypes[indicatorIndex]) {
            case SettingConst.K_LINE_INDICATOR_VOLUME:
                if (mKVolumeAverage1 != null && mKVolumeAverage2 != null) {
                    KLineDesc kLineDesc = mKLineEntities.get(index);
                    long volume = kLineDesc.getLVolume();
                    String volumeStr = "成交量:";
                    boolean isHongKongOrUSA = StockUtil.isHongKongOrUSA(mListener.mDtSecCode);
                    boolean isHandfulUnit = StockUtil.isHandfulUnit(mListener.mDtSecCode);
                    if (!TextUtils.isEmpty(mListener.mDtSecCode)) {
                        volumeStr += StringUtil.getVolumeString(volume, isHongKongOrUSA, isHandfulUnit, false);
                        volumeStr += shortGapStr;
                    }
                    mRes.mKLineTextPaint.setColor(mRes.mKColorGray);
                    canvas.drawText(volumeStr, x, y, mRes.mKLineTextPaint);
                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(volumeStr, mRes.mTextSize, null);
                    x += textWidth;

                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor1);
                    volume = mKVolumeAverage1[index - mKLineStart];
                    volumeStr = "MA" + AMOUNT_MA_1 + ":";
                    if (!TextUtils.isEmpty(mListener.mDtSecCode)) {
                        volumeStr += StringUtil.getVolumeString(volume, isHongKongOrUSA, isHandfulUnit, false);
                        volumeStr += shortGapStr;
                    }
                    canvas.drawText(volumeStr, x, y, mRes.mKLineTextPaint);
                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(volumeStr, mRes.mTextSize, null);
                    x += textWidth;

                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                    volume = mKVolumeAverage2[index - mKLineStart];
                    volumeStr = "MA" + AMOUNT_MA_2 + ":";
                    if (!TextUtils.isEmpty(mListener.mDtSecCode)) {
                        volumeStr += StringUtil.getVolumeString(volume, isHongKongOrUSA, isHandfulUnit, false);
                        volumeStr += shortGapStr;
                    }
                    canvas.drawText(volumeStr, x, y, mRes.mKLineTextPaint);
                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(volumeStr, mRes.mTextSize, null);
                    x += textWidth;

                    if (AMOUNT_MA_3 > 0){
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor3);
                        volume = mKVolumeAverage3[index - mKLineStart];
                        volumeStr = "MA" + AMOUNT_MA_3 + ":";
                        if (!TextUtils.isEmpty(mListener.mDtSecCode)) {
                            volumeStr += StringUtil.getVolumeString(volume, isHongKongOrUSA, isHandfulUnit, false);
                            volumeStr += shortGapStr;
                        }
                        canvas.drawText(volumeStr, x, y, mRes.mKLineTextPaint);
                        textWidth = mRes.mTextDrawer.measureSingleTextWidth(volumeStr, mRes.mTextSize, null);
                        x += textWidth;
                    }
                }
                break;
            case SettingConst.K_LINE_INDICATOR_MACD:
                if (mMacdEntitys != null) {
                    MACD macd = mMacdEntitys.get(index);
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                    String diffStr = "DIF: " + StringUtil.getFormattedFloat(macd.getDif(), defaultPrecision) + gapStr;
                    canvas.drawText(diffStr, x, y, mRes.mKLineTextPaint);
                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(diffStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor3);
                    String deaStr = "DEA: " + StringUtil.getFormattedFloat(macd.getDea(), defaultPrecision) + gapStr;
                    canvas.drawText(deaStr, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(deaStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor1);
                    canvas.drawText("M: " + StringUtil.getFormattedFloat(macd.getMacd(), defaultPrecision), x, y, mRes.mKLineTextPaint);
                }
                break;
            case SettingConst.K_LINE_INDICATOR_KDJ:
                if (mKdjEntitys != null) {
                    KDJ kdj = mKdjEntitys.get(index);
                    mRes.mKLineTextPaint.setColor(mRes.K_KDJ_KCOLOR);
                    String kStr = "K: " + StringUtil.getFormattedFloat(kdj.getKvalue(), defaultPrecision) + gapStr;
                    canvas.drawText(kStr, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(kStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.K_KDJ_DCOLOR);
                    String dStr = "D: " + StringUtil.getFormattedFloat(kdj.getDvalue(), defaultPrecision) + gapStr;
                    canvas.drawText(dStr, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(dStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.K_KDJ_JCOLOR);
                    canvas.drawText("J: " + StringUtil.getFormattedFloat(kdj.getJvalue(), defaultPrecision), x, y, mRes.mKLineTextPaint);
                }
                break;
            case SettingConst.K_LINE_INDICATOR_RSI:
                if (mRsiList != null) {
                    RSI rsi = mRsiList.get(index);
                    mRes.mKLineTextPaint.setColor(mRes.K_RSI_1COLOR);
                    String rsi1Str = "RSI6: " + StringUtil.getFormattedFloat(rsi.getRsi1(), defaultPrecision) + gapStr;
                    canvas.drawText(rsi1Str, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(rsi1Str, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.K_RSI_2COLOR);
                    String rsi2Str = "RSI12: " + StringUtil.getFormattedFloat(rsi.getRsi2(), defaultPrecision) + gapStr;
                    canvas.drawText(rsi2Str, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(rsi2Str, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.K_RSI_3COLOR);
                    canvas.drawText("RSI24: " + StringUtil.getFormattedFloat(rsi.getRsi3(), defaultPrecision), x, y, mRes.mKLineTextPaint);
                }
                break;
            case SettingConst.K_LINE_INDICATOR_BOLL:
                if (mBollEntities != null) {
                    BOLL boll = mBollEntities.get(index);

                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor1);
                    String upperStr = "UP: " + StringUtil.getFormattedFloat(boll.getUpper(), defaultPrecision) + gapStr;
                    canvas.drawText(upperStr, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(upperStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor3);
                    String rsi2Str = "MID: " + StringUtil.getFormattedFloat(boll.getBollval(), defaultPrecision) + gapStr;
                    canvas.drawText(rsi2Str, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(rsi2Str, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                    canvas.drawText("LOW: " + StringUtil.getFormattedFloat(boll.getLower(), defaultPrecision), x, y, mRes.mKLineTextPaint);
                }
                break;
            case SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW:
                if (!notSupportKLineCapitalFlow()) {
                    KLineDesc kLineDesc = mKLineEntities.get(index);
                    long lYmd = kLineDesc.getLYmd();
                    Float superDelta = mListener.mKLineCapitalFlowList.get((int) lYmd);
                    if (superDelta == null) {
                        superDelta = 0.0f;
                    }
                    String netStr = "净量(亿): " + StringUtil.getAmountStringYi(superDelta) + gapStr;
                    mRes.mKLineTextPaint.setColor(mRes.mKColorGray);
                    canvas.drawText(netStr, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(netStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor1);
                    float ma5 = mKCapitalFlowAverageFiveDays[index - mKLineStart];
                    String ma5Str = "MA5:" + StringUtil.getAmountStringYi(ma5) + gapStr;
                    canvas.drawText(ma5Str, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(ma5Str, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                    float ma10 = mKCapitalFlowAverageTenDays[index - mKLineStart];
                    String ma10Str = "MA10:" + StringUtil.getAmountStringYi(ma10);
                    canvas.drawText(ma10Str, x, y, mRes.mKLineTextPaint);
                }
                break;
            case SettingConst.K_LINE_INDICATOR_DMI:
                if (null != mDMIEntities){
                    DMI dmi = mDMIEntities.get(index);
                    float pdi = dmi.getPdi();
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor1);
                    String pdiStr = "PDI: " + StringUtil.getFormattedFloat(pdi, defaultPrecision) + gapStr;
                    canvas.drawText(pdiStr, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(pdiStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                    String mdiStr = "MDI: " + StringUtil.getFormattedFloat(dmi.getMdi(), defaultPrecision) + gapStr;
                    canvas.drawText(mdiStr, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(mdiStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor3);
                    String adxStr = "ADX: " + StringUtil.getFormattedFloat(dmi.getAdx(), defaultPrecision) + gapStr;
                    canvas.drawText(adxStr, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(adxStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor4);
                    canvas.drawText("ADXR: " + StringUtil.getFormattedFloat(dmi.getAdxr(), defaultPrecision), x, y, mRes.mKLineTextPaint);
                }
                break;
            case SettingConst.K_LINE_INDICATOR_CCI:
                if (mCCIEntities != null) {
                    if (index >= K_CCI_1 - 1){
                        CCI cci = mCCIEntities.get(index);
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                        String aStr = "CCI: " + StringUtil.getFormattedFloat(cci.getCci(), defaultPrecision) + gapStr;
                        canvas.drawText(aStr, x, y, mRes.mKLineTextPaint);
                        textWidth = mRes.mTextDrawer.measureSingleTextWidth(aStr, mRes.mTextSize, null);
                        x += textWidth;
                    }

                    if (index >= K_CCI_1 + K_CCI_2 - 1){
                        CCI cci = mCCIEntities.get(index);
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor3);
                        String aStr = "CCIA: " + StringUtil.getFormattedFloat(cci.getCcia(), defaultPrecision) + gapStr;
                        canvas.drawText(aStr, x, y, mRes.mKLineTextPaint);
                        textWidth = mRes.mTextDrawer.measureSingleTextWidth(aStr, mRes.mTextSize, null);
                        x += textWidth;
                    }

                }
                break;
            case SettingConst.K_LINE_INDICATOR_ENE:
                if (null != mENEEntities){
                    if (index < K_ENE_1 - 1)
                        return;
                    ENE ene = mENEEntities.get(index);
                    float upper = ene.getUpper();
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor1);
                    String upperStr = "UPPER: " + StringUtil.getFormattedFloat(upper, defaultPrecision) + gapStr;
                    canvas.drawText(upperStr, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(upperStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor3);
                    String rsi2Str = "ENE: " + StringUtil.getFormattedFloat(ene.getEne(), defaultPrecision) + gapStr;
                    canvas.drawText(rsi2Str, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(rsi2Str, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                    canvas.drawText("LOWER: " + StringUtil.getFormattedFloat(ene.getLower(), defaultPrecision), x, y, mRes.mKLineTextPaint);
                }
                break;
            case SettingConst.K_LINE_INDICATOR_DMA:
                if (null != mDMAEntities){
                    DMA dma = mDMAEntities.get(index);
                    float dmaValue = dma.getDma();
                    if (index >= K_DMA_2 - 1){
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                        String upperStr = "DMA: " + StringUtil.getFormattedFloat(dmaValue, defaultPrecision) + gapStr;
                        canvas.drawText(upperStr, x, y, mRes.mKLineTextPaint);
                        if (index >= K_DMA_2 + K_DMA_3 - 1){
                            textWidth = mRes.mTextDrawer.measureSingleTextWidth(upperStr, mRes.mTextSize, null);
                            x += textWidth;
                            mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor3);
                            String rsi2Str = "AMA: " + StringUtil.getFormattedFloat(dma.getAma(), defaultPrecision) + gapStr;
                            canvas.drawText(rsi2Str, x, y, mRes.mKLineTextPaint);
                        }
                    }
                }
                break;
            case SettingConst.K_LINE_INDICATOR_EXPMA:
                if (null != mEXPMAEntities){
                    EXPMA expma = mEXPMAEntities.get(index);
                    float ma1 = expma.getMa1();
                    if (index >= K_EXPMA_1 - 1){
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor1);
                        String ma1Str = "MA1: " + StringUtil.getFormattedFloat(ma1, defaultPrecision) + shortGapStr;
                        canvas.drawText(ma1Str, x, y, mRes.mKLineTextPaint);
                        textWidth = mRes.mTextDrawer.measureSingleTextWidth(ma1Str, mRes.mTextSize, null);
                        x += textWidth;
                    }

                    if (index >= K_EXPMA_2 - 1){
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                        String ma2Str = "MA2: " + StringUtil.getFormattedFloat(expma.getMa2(), defaultPrecision) + shortGapStr;
                        canvas.drawText(ma2Str, x, y, mRes.mKLineTextPaint);
                        textWidth = mRes.mTextDrawer.measureSingleTextWidth(ma2Str, mRes.mTextSize, null);
                        x += textWidth;
                    }

                    if (index >= K_EXPMA_3 - 1 && K_EXPMA_3 > 0){
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor3);
                        String ma3Str = "MA3: " + StringUtil.getFormattedFloat(expma.getMa3(), defaultPrecision) + shortGapStr;
                        canvas.drawText(ma3Str, x, y, mRes.mKLineTextPaint);
                        textWidth = mRes.mTextDrawer.measureSingleTextWidth(ma3Str, mRes.mTextSize, null);
                        x += textWidth;
                    }

                    if (index >= K_EXPMA_4 - 1 && K_EXPMA_4 > 0){
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor4);
                        String ma4Str = "MA4: " + StringUtil.getFormattedFloat(expma.getMa4(), defaultPrecision) + gapStr;
                        canvas.drawText(ma4Str, x, y, mRes.mKLineTextPaint);
                    }
                }
                break;
            case SettingConst.K_LINE_INDICATOR_VR:
                if (mVREntities != null) {
                    if (index < K_VR - 1)
                        return;
                    VR vr = mVREntities.get(index);
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                    String aStr = "VR: " + StringUtil.getFormattedFloat(vr.getVr(), defaultPrecision) + gapStr;
                    canvas.drawText(aStr, x, y, mRes.mKLineTextPaint);
                }
                break;
            case SettingConst.K_LINE_INDICATOR_BBI://
                if (mBBIEntities != null) {
                    BBI bbi = mBBIEntities.get(index);

                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                    String aStr = "A: " + StringUtil.getFormattedFloat(bbi.getPrice(), defaultPrecision) + gapStr;
                    canvas.drawText(aStr, x, y, mRes.mKLineTextPaint);
                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(aStr, mRes.mTextSize, null);
                    x += textWidth;
                    if (index >= MathUtil.getMaxVal(K_BBI_1, K_BBI_2, K_BBI_3, K_BBI_4) - 1){
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor3);
                        String bbiStr = "BBI: " + StringUtil.getFormattedFloat(bbi.getBbi(), defaultPrecision);
                        canvas.drawText(bbiStr, x, y, mRes.mKLineTextPaint);
                    }
                }
                break;
            case SettingConst.K_LINE_INDICATOR_OBV:
                if (mOBVEntities != null) {
                    OBV obv = mOBVEntities.get(index);
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                    String aStr = "OBV: " + StringUtil.getFormattedFloat(obv.getValue(), defaultPrecision) + gapStr;
                    canvas.drawText(aStr, x, y, mRes.mKLineTextPaint);
                }
                break;
            case SettingConst.K_LINE_INDICATOR_BIAS:
                if (null != mBIASEntities){
                    BIAS bias = mBIASEntities.get(index);
                    if (index >= K_BIAS_1 - 1){
                        float value1 = bias.getValue1();
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor1);
                        String value1Str = "BIAS1: " + StringUtil.getFormattedFloat(value1, defaultPrecision) + gapStr;
                        canvas.drawText(value1Str, x, y, mRes.mKLineTextPaint);
                        textWidth = mRes.mTextDrawer.measureSingleTextWidth(value1Str, mRes.mTextSize, null);
                        x += textWidth;
                    }

                    if (index >= K_BIAS_2 - 1){
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                        String value2Str = "BIAS2: " + StringUtil.getFormattedFloat(bias.getValue2(), defaultPrecision) + gapStr;
                        canvas.drawText(value2Str, x, y, mRes.mKLineTextPaint);
                        textWidth = mRes.mTextDrawer.measureSingleTextWidth(value2Str, mRes.mTextSize, null);
                        x += textWidth;
                    }

                    if (index >= K_BIAS_3 - 1){
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor3);
                        String value3Str = "BIAS3: " + StringUtil.getFormattedFloat(bias.getValue3(), defaultPrecision) + gapStr;
                        canvas.drawText(value3Str, x, y, mRes.mKLineTextPaint);
                        textWidth = mRes.mTextDrawer.measureSingleTextWidth(value3Str, mRes.mTextSize, null);
                        x += textWidth;
                    }
                }
                break;
            case SettingConst.K_LINE_INDICATOR_WR:
                if (null != mWREntities){
                    WR wr = mWREntities.get(index);
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                    String aStr = "WR: +" + StringUtil.getFormattedFloat(wr.getValue(), defaultPrecision) + gapStr;
                    canvas.drawText(aStr, x, y, mRes.mKLineTextPaint);
                }
                break;
            case SettingConst.K_LINE_INDICATOR_BREAK:
                if (null != mBreakEntities){
                    Break mBreak = mBreakEntities.get(index);
                    if (index >= K_BREAK_1 - 1){
                        float dif = mBreak.getDif();
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                        String value1Str = "A1: " + StringUtil.getFormattedFloat(dif, defaultPrecision) + gapStr;
                        canvas.drawText(value1Str, x, y, mRes.mKLineTextPaint);
                        textWidth = mRes.mTextDrawer.measureSingleTextWidth(value1Str, mRes.mTextSize, null);
                        x += textWidth;
                        mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor3);
                        String value2Str = "A2: " + StringUtil.getFormattedFloat(mBreak.getDea(), defaultPrecision) + gapStr;
                        canvas.drawText(value2Str, x, y, mRes.mKLineTextPaint);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void drawTimeLineIndicatorValues(final Canvas canvas, final int index, final int indicatorIndex) {
        DtLog.d(TAG, "drawTimeLineIndicatorValues index = " + index);
        if (mListener.isKLineType(mListener.mLineType) || index == -1) {
            return;
        }

        int textWidth;

        final int bottomLimit = mIndicatorsRectBottom[indicatorIndex] - mRes.mIndicatorRectHeight;
        float y = bottomLimit + mRes.mTextHeight;

        float x = drawIndicatorPreText(canvas, getTimeLineIndicatorPreText(mListener.mTimeIndicatorTypes[indicatorIndex]), bottomLimit, false);
        mRes.mKLineTextPaint.setTextSize(mRes.mTextSize);
        mRes.mKLineTextPaint.setTypeface(null);
        String gapStr = "";

        switch (mListener.mTimeIndicatorTypes[indicatorIndex]) {
            case SettingConst.TIME_LINE_INDICATOR_VOLUME:
                List<TrendDesc> timeEntities = mTimeLineDataCache.getTimeLineEntities(0);
                if (!TextUtils.isEmpty(mListener.mDtSecCode) && timeEntities != null && index < timeEntities.size()) {
                    mRes.mKLineTextPaint.setColor(mRes.mKColorGray);
                    boolean mIsHongKongOrUsa = StockUtil.isHongKongOrUSA(mListener.mDtSecCode);
                    long nowvol = timeEntities.get(index).getLNowvol();
                    String volume = mIsHongKongOrUsa ? StringUtil.getVolumeString(nowvol, true, true) : StringUtil.getVolumeString(nowvol, false, true);
                    canvas.drawText(volume, x, y, mRes.mKLineTextPaint);
                }
                break;
            case SettingConst.TIME_LINE_INDICATOR_CAPITAL_DDZ:
                if (!TextUtils.isEmpty(mListener.mDtSecCode) &&
                        (mListener.mLineType == LineChartTextureView.TYPE_FIVE_DAY ||
                        !StockUtil.supportCapitalDDZ(mListener.mDtSecCode))) {
                    return;
                }

                if (zjbylist != null && index < zjbylist.size()) {
                    KZJBY zjby = zjbylist.get(index);
                    float ssup = zjby.getSsup();
                    float sbig = zjby.getSbig();
                    float smid = zjby.getSmid();
                    float ssmall = zjby.getSsmall();

                    mRes.mKLineTextPaint.setColor(mRes.mKColorRed);
                    String supStr = "超大:" + StringUtil.getAmountStringYi(ssup) + gapStr;
                    canvas.drawText(supStr, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(supStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                    String bigStr = "大:" + StringUtil.getAmountStringYi(sbig) + gapStr;
                    canvas.drawText(bigStr, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(bigStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor3);
                    String midStr = "中:" + StringUtil.getAmountStringYi(smid) + gapStr;
                    canvas.drawText(midStr, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(midStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKColorGreen);
                    String smallStr = "小:" + StringUtil.getAmountStringYi(ssmall) + gapStr;
                    canvas.drawText(smallStr, x, y, mRes.mKLineTextPaint);

                    String unit = "(亿)";
                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(smallStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKColorGray);
                    canvas.drawText(unit, x, y, mRes.mKLineTextPaint);
                }
                break;
            case SettingConst.TIME_LINE_INDICATOR_MACD:
                gapStr = "    ";
                int defaultPrecision = 2;
                if (mTimeLineMacdEntitys != null) {
                    MACD macd = mTimeLineMacdEntitys.get(index);
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor2);
                    String diffStr = "DIF: " + StringUtil.getFormattedFloat(macd.getDif(), defaultPrecision) + gapStr;
                    canvas.drawText(diffStr, x, y, mRes.mKLineTextPaint);
                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(diffStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor3);
                    String deaStr = "DEA: " + StringUtil.getFormattedFloat(macd.getDea(), defaultPrecision) + gapStr;
                    canvas.drawText(deaStr, x, y, mRes.mKLineTextPaint);

                    textWidth = mRes.mTextDrawer.measureSingleTextWidth(deaStr, mRes.mTextSize, null);
                    x += textWidth;
                    mRes.mKLineTextPaint.setColor(mRes.mKLineAverageColor1);
                    canvas.drawText("M: " + StringUtil.getFormattedFloat(macd.getMacd(), defaultPrecision), x, y, mRes.mKLineTextPaint);
                }
                break;
            case SettingConst.TIME_LINE_INDICATOR_VOLUME_RATIO:
                if (mListener.mLineType == LineChartTextureView.TYPE_FIVE_DAY) {
                    return;
                }
                final SecQuote quote = mListener.mQuote;
                if (quote == null) {
                    return;
                }
                final float currentVolumnRatio = quote.fVolumeRatio;
                if (currentVolumnRatio <= 0f) {
                    return;
                }
                final float[] volumeAvg = mTimeVolumeAverage;
                final int length = volumeAvg == null ? 0 : volumeAvg.length;
                if (length == 0 && index >= length) {
                    return;
                }
                final float day5AverageVolume = volumeAvg[length - 1] / currentVolumnRatio;
                float ratio = volumeAvg[index] / day5AverageVolume;
                mRes.mKLineTextPaint.setColor(mRes.mKColorGray);
                x = mRes.mTextDrawer.measureSingleTextWidth(getVolumeRatioPreText(), mRes.mTextSize, null);
                canvas.drawText(StringUtil.getFormatedFloat(ratio), x + 10, y, mRes.mKLineTextPaint);
                break;
            default:
                break;
        }
    }

    private void drawKLineAverageText(Canvas canvas, int position) {
        float x, y;
        int count = /*mKLineEntities.size()*/mKLineEnd - mKLineStart;
        if (position < 0) {
            position = count - 1;
        }

        if (position < 0) {
            return;
        }
        KLineInfosView.KLineAverageInfo averageInfo = new KLineInfosView.KLineAverageInfo();
        String invalidValue = "--";

        int index = position + mKLineStart + 1;
        int prePosition = position - 1 > 0 ? position - 1 : 0;

        float preValue1 = mKLineAverage1[prePosition];
        float currentValue1 = mKLineAverage1[position];
        String effectiveValue = index >= MA_1 ? StringUtil.getFormattedFloat(currentValue1, mListener.mTpFlag) : invalidValue;
        String orientation = index >= MA_1 ? (currentValue1 >= preValue1 ? mRes.mMaRise : mRes.mMaDown) : "";
        String text = "MA" + MA_1 + ":" + effectiveValue + orientation;
        averageInfo.addAverage(text, mRes.mKLineAverageColor1);

        float preValue2 = mKLineAverage2[prePosition];
        float currentValue2 = mKLineAverage2[position];
        effectiveValue = index >= MA_2 ? StringUtil.getFormattedFloat(currentValue2, mListener.mTpFlag) : invalidValue;
        orientation = index >= MA_2 ? (currentValue2 >= preValue2 ? mRes.mMaRise : mRes.mMaDown) : "";
        text = MA_2 + ":" + effectiveValue + orientation;
        averageInfo.addAverage(text, mRes.mKLineAverageColor2);

        float preValue3 = mKLineAverage3[prePosition];
        float currentValue3 = mKLineAverage3[position];
        effectiveValue = index >= MA_3 ? StringUtil.getFormattedFloat(currentValue3, mListener.mTpFlag) : invalidValue;
        orientation = index >= MA_3 ? (currentValue3 >= preValue3 ? mRes.mMaRise : mRes.mMaDown) : "";
        text = MA_3 + ":" + effectiveValue + orientation;
        averageInfo.addAverage(text, mRes.mKLineAverageColor3);

        if (MA_4 != 0) {
            float preValue4 = mKLineAverage4[prePosition];
            float currentValue4 = mKLineAverage4[position];
            effectiveValue = index >= MA_4 ? StringUtil.getFormattedFloat(currentValue4, mListener.mTpFlag) : invalidValue;
            orientation = index >= MA_4 ? (currentValue4 >= preValue4 ? mRes.mMaRise : mRes.mMaDown) : "";
            text = MA_4 + ":" + effectiveValue + orientation;
            averageInfo.addAverage(text, mRes.mKLineAverageColor4);
        }

        if (MA_5 != 0) {
            float preValue5 = mKLineAverage5[prePosition];
            float currentValue5 = mKLineAverage5[position];
            effectiveValue = index >= MA_5 ? StringUtil.getFormattedFloat(currentValue5, mListener.mTpFlag) : invalidValue;
            orientation = index >= MA_5 ? (currentValue5 >= preValue5 ? mRes.mMaRise : mRes.mMaDown) : "";
            text = MA_5 + ":" + effectiveValue + orientation;
            averageInfo.addAverage(text, mRes.mKLineAverageColor5);
        }

        if (MA_6 != 0) {
            float preValue6 = mKLineAverage6[prePosition];
            float currentValue6 = mKLineAverage6[position];
            effectiveValue = index >= MA_6 ? StringUtil.getFormattedFloat(currentValue6, mListener.mTpFlag) : invalidValue;
            orientation = index >= MA_6 ? (currentValue6 >= preValue6 ? mRes.mMaRise : mRes.mMaDown) : "";
            text = MA_6 + ":" + effectiveValue + orientation;
            averageInfo.addAverage(text, mRes.mKLineAverageColor6);
        }

        if (mKLineEventListener != null) {
            mKLineEventListener.onKLineAverageChanged(averageInfo);
        }
    }

    private void drawVerticalCoordinates(Canvas canvas) {
        int x = 0;
        int y = mRes.mKLineAverageTextAreaHeight;

        int count = 4;
        int itemHeight = (mChartContentHeight - mRes.mKLineAverageTextAreaHeight) / count;

        float delta = mKLineMax - mKLineMin;
        float drawingDelta = delta / KLINE_MAX_HEIGHT_RATIO;
        float minMaxDelta = (drawingDelta - delta) / 2;
        float max = mKLineMax + minMaxDelta;
        float min = mKLineMin - minMaxDelta;

        if (mKLineMax == mKLineMin) { //最大最小值相等的极端情况，min/max取偏差6%特殊处理
            max = mKLineMax * (1 + (1 - KLINE_MAX_HEIGHT_RATIO) / 2);
            min = mKLineMin * (1 - (1 - KLINE_MAX_HEIGHT_RATIO) / 2);
        }

        mRes.mKLineTextPaint.setTypeface(TextDrawer.getTypeface());
        mRes.mKLineTextPaint.setColor(mRes.mKColorRed);
        canvas.drawText(StringUtil.getFormatedFloat(max), x, y + mRes.mTextHeight, mRes.mKLineTextPaint);
        for (int i = 0; i < count; i++) {
            canvas.drawLine(x, y, mDrawingWidth, y, mRes.mPaintDashLine);
            y += itemHeight;
        }
        mRes.mKLineTextPaint.setColor(mRes.mKColorGreen);
        canvas.drawText(StringUtil.getFormatedFloat(min), x, y, mRes.mKLineTextPaint);
    }

    private void drawHorizontalCoordinates(Canvas canvas) {
        final int[] indicatorTypes = mListener.mTradingIndicatorTypes;
        boolean[] notSupportIndicators = new boolean[indicatorTypes.length];

        for(int i=0; i<indicatorTypes.length; i++) {
            int indicatorType = indicatorTypes[i];
            notSupportIndicators[i] = false;
            if(indicatorType == SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW) {
                notSupportIndicators[i] = notSupportKLineCapitalFlow();
            }
        }

        List<DateAndIndex> dateAndIndexList = new ArrayList<>();
        dateAndIndexList.add(new DateAndIndex(String.valueOf(mKLineEntities.get(mKLineStart).getLYmd()), mKLineStart));
        if (mKLineEnd != 0){
            dateAndIndexList.add(new DateAndIndex(String.valueOf(mKLineEntities.get(mKLineEnd - 1).getLYmd()), mKLineEnd - 1));
        }

        Typeface typeface = TextDrawer.getTypeface();
        mRes.mKLineTextPaint.setTypeface(typeface);
        mRes.mKLineTextPaint.setColor(mRes.mKColorGray);
        float y = mChartContentHeight + mRes.mTextHeight;
        int textWidth;
        canvas.drawText(getFormatString(dateAndIndexList.get(0).mDateStr), 0, y,mRes.mKLineTextPaint);
        textWidth = mRes.mTextDrawer.measureSingleTextWidth(getFormatString(dateAndIndexList.get(1).mDateStr), mRes.mTextSize, typeface);
        canvas.drawText(getFormatString(dateAndIndexList.get(1).mDateStr), mDrawingWidth - textWidth, y, mRes.mKLineTextPaint);
    }

    private String getFormatString(String date){
        return date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6);
    }

    synchronized void zoom(float ratio) {
        if (mKLineEntities == null) {
            return;
        }

        mZoomRatio = mLastZoomRatio * ratio;

        float maxRatio = MAX_ZOOM_RATIO;
        if (mZoomRatio >= maxRatio) {
            mZoomRatio = maxRatio;
        } else if (mZoomRatio <= MIN_ZOOM_RATIO) {
            mZoomRatio = MIN_ZOOM_RATIO;
        }

        DengtaApplication application = DengtaApplication.getApplication();
        if (mIsFullScreen) {
            application.mKLineZoomRatioLandscape = mZoomRatio;
        } else {
            application.mKLineZoomRatioPortrait = mZoomRatio;
        }

        //end位置不动，根据ratio移动start的位置
        mKLineStart = getKLineStartByEnd();

        if (mKLineStart == 0) {
            //触发下一次拉取
            if (mKLineEventListener != null && !mHasOldestKLineData) {
                mKLineEventListener.onMoreDataNeeded(mListener.mLineType, mKLineEntities.size(), LineChartActivity.DEFAULT_KLINE_LOAD_NUM);
                if ("debug".equals(BuildConfig.BUILD_TYPE)) {
                    DengtaApplication.getApplication().showToast("缩放触发拉取更多");
                }
            }
        }

        updateKLineValues();
        DtLog.d(TAG, "zoom: mLastZoomRatio = " + mLastZoomRatio + ", mZoomRatio = " + mZoomRatio + ", ratio = " + ratio + ", start = " + mKLineStart + ", end = " + mKLineEnd);

        StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_ZOOMED);

        requestRender();
    }

    public synchronized void moveLeft() {
        switch (mListener.mLineType) {
            case LineChartTextureView.TYPE_TIME:
                moveTimeLineLeft();
                break;
            case LineChartTextureView.TYPE_DAILY_K:
                moveKLineLeft();
                break;
            default:
        }
    }

    private void moveTimeLineLeft() {
        int lastIndex = getTimeTouchEntityIndex(getTouchX());

        if(lastIndex >= 0) {
            int newIndex;
            if(lastIndex - 15 > 0) {
                newIndex = lastIndex - 15;
            } else {
                newIndex = 0;
            }
            setTouchX(computeX(newIndex));
        }
    }

    private void moveKLineLeft() {
        float lastX = getTouchX();

        float maxTouchX = getMaxKLineTouchX();
        if(lastX > maxTouchX) {
            lastX = maxTouchX;
        }
        float itemWidth = getKLineItemWidth();
        if((int)lastX - (int)Math.ceil(itemWidth) > 0) {
            setTouchX(lastX - itemWidth);
        }
    }

    public synchronized void moveRight() {
        switch (mListener.mLineType) {
            case LineChartTextureView.TYPE_TIME:
                moveTimeLineRight();
                break;
            case LineChartTextureView.TYPE_DAILY_K:
                moveKLineRight();
                break;
            default:
        }
    }

    private void moveTimeLineRight() {
        int lastIndex = getTimeTouchEntityIndex(getTouchX());

        if(lastIndex >= 0) {
            int newIndex;
            List<TrendDesc> timeEntities = mTimeLineDataCache.getTimeLineEntities(0);
            if(lastIndex + 15 < timeEntities.size()) {
                newIndex = lastIndex + 15;
            } else {
                newIndex = timeEntities.size() - 1;
            }
            setTouchX(computeX(newIndex));
        }
    }

    private float computeX(int index) {
        return mStrokeWidth + mRes.mTimeHorizontalGap * index;
    }

    private void moveKLineRight() {
        float lastX = getTouchX();

        float minTouchX = getMinKLineTouchX();
        if(lastX < minTouchX) {
            lastX = minTouchX;
        }
        float itemWidth = getKLineItemWidth();
        if(lastX + itemWidth/2 < mDrawingWidth) {
            setTouchX(lastX + itemWidth);
        } else {
            setTouchX(mDrawingWidth - itemWidth/2);
        }
    }

    private float getMinKLineTouchX() {
        return getKLineItemWidth() - mRes.mKLineStrokeWidth;
    }

    private float getMaxKLineTouchX() {
        if(mKLineEntities != null && !mKLineEntities.isEmpty()) {
            int entityCount = mKLineEnd - mKLineStart;
            float itemWidth = getKLineItemWidth();
            return entityCount * itemWidth;
        }
        return 0f;
    }

    synchronized void translate(float distance) {
        if (distance == 0) {
            mTranslation = 0;
            return;
        }

        if (mKLineEntities == null) {
            return;
        }

        int prevStart = mKLineStart;
        int prevEnd = mKLineEnd;

        int entityCount = mKLineEnd - mKLineStart;
        float itemWidth = getKLineItemWidth();
        float distanceFromLastMove = distance - mTranslation;
        int offsetIndex = (int) (distanceFromLastMove / itemWidth);
        //                DtLog.d(TAG, "translate offsetIndex is " + offsetIndex);
        if (offsetIndex == 0) {
            return;
        }

        mTranslation = distance;
        int start = mKLineStart - offsetIndex;
        int end = mKLineEnd - offsetIndex;
        if (start < 0) {
            mKLineStart = 0;
            mKLineEnd = mKLineStart + entityCount;

            //触发下一次拉取
            if (mKLineEventListener != null && !mHasOldestKLineData) {
                mKLineEventListener.onMoreDataNeeded(mListener.mLineType, mKLineEntities.size(), LineChartActivity.DEFAULT_KLINE_LOAD_NUM);
                if ("debug".equals(BuildConfig.BUILD_TYPE)) {
                    DengtaApplication.getApplication().showToast("拖动触发拉取更多");
                }
            }
        } else if (end > mKLineEntities.size()) {
            mKLineEnd = mKLineEntities.size();
            mKLineStart = mKLineEnd - entityCount;
        } else {
            mKLineStart = start;
            mKLineEnd = end;
        }
//                DtLog.d(TAG, "translate mKLineStart is " + mKLineStart + ", mKLineEnd is " + mKLineEnd);

        if (prevStart != mKLineStart || prevEnd != mKLineEnd) {
            updateKLineValues();

            StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_TRANSLATED);
            requestRender();
        }
    }

    synchronized void setHasOldestKLineData(boolean hasAll) {
        mHasOldestKLineData = hasAll;
    }

    synchronized void onTradingIndicatorTypeChanged() {
        requestRender();
    }

    synchronized void onTimeIndicatorTypeChanged() {
        setTimeEntities(mListener.mTimeLineData.get(mListener.mLineType));
    }

    /**
     * 是否全屏显示，目前全屏显示的分时成交量柱子显示会宽一些
     */
    boolean mIsFullScreen;

    synchronized void onTpFlagUpdated() {
        updateKLineValues();
    }

    private static class DateAndIndex {
        private String mDateStr;
        private int mIndex;

        public DateAndIndex(String dateStr, int index) {
            mDateStr = dateStr;
            mIndex = index;
        }
    }

    private void drawKLineMinMaxIndicators(Canvas canvas) {
        int entityCount = mKLineEnd - mKLineStart;
        int visibleCount = (int) (DEFAULT_KLINE_COUNT / mZoomRatio);

        float itemWidth = getKLineItemWidth();
        float pillarWidth = itemWidth * PILLAR_WIDTH_RATIO;
        float startX = mRes.mKLineStrokeWidth / 2;

        for (int i = 0; i < entityCount; i++) {
            int index = i + mKLineStart;
            if (index != mKLineMinIndex && index != mKLineMaxIndex) {
                //移动到下一个格子起点
                startX += itemWidth;
                continue;
            }

            KLineDesc entity = mKLineEntities.get(index);

            float high = entity.getFHigh();
            float low = entity.getFLow();

            float highHeight = getKLineDrawingHeight(high);
            float lowHeight = getKLineDrawingHeight(low);

            mRes.mPaintLine.setStrokeWidth(mRes.mKLineStrokeWidth);

            //画上下影线
            float lineStartX = startX + pillarWidth / 2;

            //画最大最小值的指示框
            mRes.mPaintKLine.setStyle(Paint.Style.FILL);
            mRes.mPaintKLine.setColor(mRes.mKColorIndicatorBg);
            float indicatorLeft = 0, indicatorRight = 0, indicatorTop = 0, indicatorBottom = 0;
            float indicatorLineHeight = 0;
            String minMaxStr = "";
            if (index == mKLineMaxIndex) { //画最大值指示框
                minMaxStr = getMinMaxString(mKLineValueMax);
                float textWidth = mRes.mTextDrawer.measureSingleTextWidth(minMaxStr, mRes.mTextSize, TextDrawer.getTypeface());
                mRes.mKLineIndicatorWidth = (int) textWidth + 10;

                indicatorTop = highHeight - mRes.mKLineIndicatorHeight / 2;
                indicatorBottom = highHeight + mRes.mKLineIndicatorHeight / 2;
                indicatorLineHeight = (indicatorTop + indicatorBottom) / 2;
                if (i > visibleCount / 2) { //在右侧时
                    indicatorLeft = lineStartX - mRes.mKLineIndicatorWidth - mRes.mKLineIndicatorHorizontalGap;
                    indicatorRight = lineStartX - mRes.mKLineIndicatorHorizontalGap;
                    canvas.drawLine(indicatorRight, indicatorLineHeight, lineStartX, indicatorLineHeight, mRes.mPaintKLine);
                } else { //在左侧时
                    indicatorLeft = lineStartX + mRes.mKLineIndicatorHorizontalGap;
                    canvas.drawLine(lineStartX, indicatorLineHeight, indicatorLeft, indicatorLineHeight, mRes.mPaintKLine);
                }

                //画指示框里面的文字
                mRes.mPaintKLine.setTextSize(mRes.mTextSize);
                mRes.mPaintKLine.setColor(mRes.mKColorIndicatorText);
                canvas.drawText(minMaxStr, indicatorLeft + (mRes.mKLineIndicatorWidth - textWidth) / 2,
                        indicatorBottom - (mRes.mKLineIndicatorHeight - mRes.mTextHeight) / 2 - mRes.mTextBottom + mRes.mKLineIndicatorTextMarginTop, mRes.mPaintKLine);
            }

            mRes.mPaintKLine.setStyle(Paint.Style.FILL);
            mRes.mPaintKLine.setColor(mRes.mKColorIndicatorBg);
            if (index == mKLineMinIndex) { //画最小值指示框
                minMaxStr = getMinMaxString(mKLineValueMin);
                float textWidth = mRes.mTextDrawer.measureSingleTextWidth(minMaxStr, mRes.mTextSize, TextDrawer.getTypeface());
                mRes.mKLineIndicatorWidth = (int) textWidth + 10;

                indicatorTop = lowHeight - mRes.mKLineIndicatorHeight / 2;
                indicatorBottom = lowHeight + mRes.mKLineIndicatorHeight / 2;
                indicatorLineHeight = (indicatorTop + indicatorBottom) / 2;
                if (i > visibleCount / 2) { //在右侧时
                    indicatorLeft = lineStartX - mRes.mKLineIndicatorWidth - mRes.mKLineIndicatorHorizontalGap;
                    indicatorRight = indicatorLeft + mRes.mKLineIndicatorWidth;
                    canvas.drawLine(lineStartX, indicatorLineHeight, indicatorRight, indicatorLineHeight, mRes.mPaintKLine);
                } else { //在左侧时
                    indicatorLeft = lineStartX + mRes.mKLineIndicatorHorizontalGap;
                    canvas.drawLine(lineStartX, indicatorLineHeight, lineStartX + mRes.mKLineIndicatorHorizontalGap, indicatorLineHeight, mRes.mPaintKLine);
                }

                //画指示框里面的文字
                mRes.mPaintKLine.setTextSize(mRes.mTextSize);
                mRes.mPaintKLine.setColor(mRes.mKColorIndicatorText);
                canvas.drawText(minMaxStr, indicatorLeft + (mRes.mKLineIndicatorWidth - textWidth) / 2,
                        indicatorBottom - (mRes.mKLineIndicatorHeight - mRes.mTextHeight) / 2 - mRes.mTextBottom + mRes.mKLineIndicatorTextMarginTop, mRes.mPaintKLine);
            }

            //移动到下一个格子起点
            startX += itemWidth;
        }
    }

    private String getMinMaxString(float value) {
        return StringUtil.getFormattedFloat(value, mListener.mTpFlag);
    }

    synchronized void updateSize(int width, int height) {
        DtLog.d(TAG, "updateSize() width = " + width + ", height = " + height);
        mDrawingWidth = width;
        mDrawingHeight = height;
        mRes.mSurfaceRect.set(0, 0, mDrawingWidth, mDrawingHeight);
        updateHeight();
        requestRender();
    }

    private boolean readyToDraw() {
        return mRequestRender;
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

    synchronized void setLineType() {
        mKLineStart = 0;
        mKLineEnd = 0;
        mHasOldestKLineData = false;

        resetIndicatorValues();
    }

    synchronized void setTimeEntities(List<TrendDesc> timeEntities) {
        mTimeLineDataCache.putTimeEntities(timeEntities, 0);
        updateVolumnAverage(timeEntities);
        requestRender();
    }

    synchronized void setCompareTimeEntities(List<TrendDesc> timeEntities) {
        if (timeEntities == null) {
            return;
        }
        mTimeLineDataCache.putTimeEntities(timeEntities, 1);
        updateVolumnAverage(timeEntities);
        requestRender();
    }

    synchronized void clearCompareTimeEntities() {
        mTimeLineDataCache.removeTimeLineData(1);
        requestRender();
    }

    synchronized void setTimeCapitalDDZEntities() {
        requestRender();
    }

    synchronized void setKLineCapitalDDZEntities() {
        requestRender();
    }

    private void updateMinMaxKLineValue(List<KLineDesc> entities, int start, int end) {
        mKLineMax = 0;
        mKLineMin = Float.MAX_VALUE;
        mKLineValueMax = 0;
        mKLineValueMin = Float.MAX_VALUE;

        for (int i = start; i < end && i < entities.size() && i >= 0; i++) {
            KLineDesc entity = entities.get(i);

            float high = entity.getFHigh();
            float low = entity.getFLow();
            if (high > mKLineValueMax) {
                mKLineValueMax = high;
                mKLineMaxIndex = i;
            }

            if (low < mKLineValueMin) {
                mKLineValueMin = low;
                mKLineMinIndex = i;
            }

            float ma1 = mKLineAverage1[i - start];
            float ma2 = mKLineAverage2[i - start];
            float ma3 = mKLineAverage3[i - start];
            float ma4 = mKLineAverage4[i - start];
            float ma5 = mKLineAverage5[i - start];
            float ma6 = mKLineAverage6[i - start];

            ma4 = MA_4 == 0 ? Float.NEGATIVE_INFINITY : ma4;
            ma5 = MA_5 == 0 ? Float.NEGATIVE_INFINITY : ma5;
            ma6 = MA_6 == 0 ? Float.NEGATIVE_INFINITY : ma6;
            mKLineMax = MathUtil.getMaxVal(mKLineMax, high, ma1, ma2, ma3, ma4, ma5, ma6);
            ma4 = MA_4 == 0 ? Float.POSITIVE_INFINITY : ma4;
            ma5 = MA_5 == 0 ? Float.POSITIVE_INFINITY : ma5;
            ma6 = MA_6 == 0 ? Float.POSITIVE_INFINITY : ma6;
            mKLineMin = MathUtil.getMinVal(mKLineMin, low, ma1, ma2, ma3, ma4, ma5, ma6);
        }
    }

    synchronized void clearKLineEntities() {
        mKLineStart = 0;
        mKLineEnd = 0;
        requestRender();
    }

    public void setBSInfos(final HashMap<String, SecBsInfo> bsInfoMap) {
        mBSInfos = bsInfoMap;
        requestRender();
    }

    public synchronized void setKLineEntities(List<KLineDesc> kLineEntities) {
        final int size = kLineEntities == null ? 0 : kLineEntities.size();
        if (size == 0) {
            return;
        }

        DtLog.d(TAG, "setKLineEntities: prev mKLineStart = " + mKLineStart + ", mKLineEnd = " + mKLineEnd);
        int prevSize = 0;
        if (mKLineEntities != null) {
            prevSize = mKLineEntities.size();
            DtLog.d(TAG, "setKLineEntities: prev = " + prevSize);
        }

        mKLineEntities = kLineEntities;
        if (mKLineEnd > size) { // 数据错误的保护
            mKLineEnd = size;
            mKLineStart = getKLineStartByEnd();
        } else if (mKLineEnd == 0) {
            mKLineEnd = size;
            mKLineStart = getKLineStartByEnd();
        } else if (mKLineStart == 0) {
            int prevCount = mKLineEnd - mKLineStart;
            mKLineStart = size - prevSize;
            mKLineEnd = mKLineStart + prevCount;
        } else if (size > prevSize) {
            if (mKLineEnd == prevSize) {
                // 刷新k线的时候，显示新的k线柱子，需要重新计算start，end
                mKLineEnd = size;
                mKLineStart = getKLineStartByEnd();
            }
        }
        mKlineCount = mKLineEntities.size();
        updateKLineValues();
        initNormalIndicatorValues();

        resetIndicatorValues();

        requestRender();
    }

    private void resetIndicatorValues() {
        mMacdEntitys = null;
        mKdjEntitys = null;
        mRsiList = null;
        mBollEntities = null;
        mBBIEntities = null;
        mDMIEntities = null;
        mCCIEntities = null;
        mENEEntities = null;
        mDMAEntities = null;
        mEXPMAEntities = null;
        mVREntities = null;
        mOBVEntities = null;
        mWREntities = null;
        mMagicNineEntities = null;
        mBreakEntities = null;
        mBreakPointEntities = null;
        mBullBearEntities = null;
    }

    private void updateKLineValues() {
        if (mListener.isTimeLine(mListener.mLineType) || mKLineEntities == null) {
            return;
        }
        updateAverageKLineValue(mKLineEntities, mKLineStart, mKLineEnd);
        updateMinMaxKLineValue(mKLineEntities, mKLineStart, mKLineEnd);
    }

    private int getKLineStartByEnd() {
        mKLineStart = (int) (mKLineEnd - DEFAULT_KLINE_COUNT / mZoomRatio);
        if (mKLineStart < 0) {
            mKLineStart = 0;
            mKLineEnd = (int) (mKLineStart + DEFAULT_KLINE_COUNT / mZoomRatio);
            if (mKLineEnd > mKLineEntities.size()) {
                mKLineEnd = mKLineEntities.size();
            }
        }

        return mKLineStart;
    }

    public int getKLineStart() {
        return mKLineStart;
    }

    private void initNormalIndicatorValues(){
        mCloses = new float[mKlineCount];
        mHighs = new float[mKlineCount];
        mLows = new float[mKlineCount];
        ema12s = new double[mKlineCount];
        ema26s = new double[mKlineCount];
        ma60s = new double[mKlineCount];
        mStandardMacdEntitys = new ArrayList<>();
        float ema12 = 0, ema26 = 0, dea9 = 0;
        for (int i = 0 ; i < mKLineEntities.size() ; i++){
            KLineDesc entity = mKLineEntities.get(i);
            mCloses[i] = mKLineEntities.get(i).getFClose();
            mHighs[i] = mKLineEntities.get(i).getFHigh();
            mLows[i] = mKLineEntities.get(i).getFLow();
            float close = entity.getFClose();
            if (i == 0) {
                mStandardMacdEntitys.add(new MACD(0, 0, 0));
                ema12 = ema26 = close;
                continue;
            }
            ema12 = (ema12 * (K_STANDARd_MACD_1 - 1) + close * 2) / (K_STANDARd_MACD_1 + 1);
            ema26 = (ema26 * (K_STANDARd_MACD_2 - 1) + close * 2) / (K_STANDARd_MACD_2 + 1);
            float diff = ema12 - ema26;
            dea9 = (dea9 * (K_STANDARd_MACD_3 - 1) + diff * 2) / (K_STANDARd_MACD_3 + 1);
            float macd = 2 * (diff - dea9);
            mStandardMacdEntitys.add(new MACD(macd, diff, dea9));
        }
        core.ema(K_BREAK_1 - 1, mKlineCount - 1, mCloses, K_BREAK_1, outBegIdx, outNBElement, ema12s);
        core.ema(K_BREAK_2 - 1, mKlineCount - 1, mCloses, K_BREAK_2, outBegIdx, outNBElement, ema26s);
        core.sma(0, mKlineCount - 1, mCloses, 60, outBegIdx, outNBElement, ma60s);
    }

    private void updateAverageKLineValue(List<KLineDesc> entities, int start, int end) {
        int rangeCount = end - start;
        mKLineAverage1 = new float[rangeCount];
        mKLineAverage2 = new float[rangeCount];
        mKLineAverage3 = new float[rangeCount];
        mKLineAverage4 = new float[rangeCount];
        mKLineAverage5 = new float[rangeCount];
        mKLineAverage6 = new float[rangeCount];

        int size = entities.size();
        for (int i = start; i < end; i++) {
            float sum = 0;
            int count = 0;
            for (int j = i; j > i - MA_1 && j >= 0 && j < size; j--) {
                sum += entities.get(j).fClose;
                count++;
            }
            mKLineAverage1[i - start] = sum / count;

            sum = 0;
            count = 0;
            for (int j = i; j > i - MA_2 && j >= 0 && j < size; j--) {
                sum += entities.get(j).fClose;
                count++;
            }
            mKLineAverage2[i - start] = sum / count;

            sum = 0;
            count = 0;
            for (int j = i; j > i - MA_3 && j >= 0 && j < size; j--) {
                sum += entities.get(j).fClose;
                count++;
            }
            mKLineAverage3[i - start] = sum / count;

            if (MA_4 != 0) {
                sum = 0;
                count = 0;
                for (int j = i; j > i - MA_4 && j >= 0 && j < size; j--) {
                    sum += entities.get(j).fClose;
                    count++;
                }
                mKLineAverage4[i - start] = sum / count;
            }

            if (MA_5 != 0) {
                sum = 0;
                count = 0;
                for (int j = i; j > i - MA_5 && j >= 0 && j < size; j--) {
                    sum += entities.get(j).fClose;
                    count++;
                }
                mKLineAverage5[i - start] = sum / count;
            }

            if (MA_6 != 0) {
                sum = 0;
                count = 0;
                for (int j = i; j > i - MA_6 && j >= 0 && j < size; j--) {
                    sum += entities.get(j).fClose;
                    count++;
                }
                mKLineAverage6[i - start] = sum / count;
            }
        }
    }

    private class TimeLineDataCache {
        private SparseArray<TimeLineData> cache = new SparseArray<>(2);

        private float mMaxUpRate = 0;

        private float mMinDownRate = 0;

        public void putTimeEntities(List<TrendDesc> entities, int index) {
            TimeLineData timeLineData = cache.get(index);
            if(timeLineData == null) {
                timeLineData = new TimeLineData();
                cache.put(index, timeLineData);
            }
            timeLineData.timeEntities = entities;
            updateTotalMinMaxTimeValue();
        }

        public void removeTimeLineData(int index) {
            TimeLineData timeLineData = cache.get(index);
            if(timeLineData != null) {
                cache.remove(index);
                updateTotalMinMaxTimeValue();
            }
        }

        public void putQuote(SecQuote secQuote, int index) {
            TimeLineData timeLineData = cache.get(index);
            if(timeLineData == null) {
                timeLineData = new TimeLineData();
                cache.put(index, timeLineData);
            }
            timeLineData.setQuote(secQuote);
            updateTotalMinMaxTimeValue();
        }

        public void updateTotalMinMaxTimeValue() {
            for(int i=0, size=cache.size(); i<size; i++) {
                TimeLineData data = cache.valueAt(i);
                float amountMax = updateMinMaxTimeValue(data, i);
                if(i == 0) {
                    mTimeAmountMax = amountMax;
                }
            }
            mMaxUpRate = computeMaxUpRate(cache);
            mMinDownRate = computeMinUpRate(cache);
        }

        public void clear() {
            for(int i=0, size=cache.size(); i<size; i++) {
                TimeLineData data = cache.valueAt(i);
                data.clear();
            }
            cache.clear();
        }

        public float getMaxUpRate() {
            return mMaxUpRate;
        }

        public float getMinDownRate() {
            return mMinDownRate;
        }

        public float computeMaxUpRate() {
            return computeMaxUpRate(cache);
        }


        public float computeMinUpRate() {
            return computeMinUpRate(cache);
        }

        private float computeMaxUpRate(SparseArray<TimeLineData> datas) {
            float maxUpRate = 0;
            for(int i=0, size=datas.size(); i<size; i++) {
                TimeLineData data = datas.valueAt(i);
                if(data.check()) {
                    float upRate = (data.maxValue - data.getClose()) / data.getClose();
                    if(upRate > maxUpRate) {
                        maxUpRate = upRate;
                    }
                }
            }
            return maxUpRate;
        }

        private float computeMinUpRate(SparseArray<TimeLineData> datas) {
            float minDownRate = 0;
            for(int i=0, size=datas.size(); i<size; i++) {
                TimeLineData data = datas.valueAt(i);
                if(data.check()) {
                    float downRate = (data.minValue - data.getClose()) / data.getClose();
                    if(downRate < minDownRate) {
                        minDownRate = downRate;
                    }
                }
            }
            return minDownRate;
        }

        private float updateMinMaxTimeValue(TimeLineData timeLineData, int index) {
            List<TrendDesc> entities = timeLineData.timeEntities;
            float close = timeLineData.getClose();
            float timeValueMax = 0;
            float timeValueMin = Float.MAX_VALUE;
            float timeAmountMax = 0f;
            if(entities == null || timeLineData.secQuote == null) {
                return 0f;
            }

            for (TrendDesc entity : entities) {
                float now = entity.getFNow();
                if (now > timeValueMax) {
                    timeValueMax = now;
                } else if (now < timeValueMin) {
                    timeValueMin = now;
                }

                if(mListener.supportAverage()) {
                    float average = entity.getFAverage();
                    if (average > timeValueMax) {
                        timeValueMax = average;
                    } else if (average < timeValueMin) {
                        timeValueMin = average;
                    }
                }

                float amount = entity.getLNowvol();
                if (amount > timeAmountMax) {
                    timeAmountMax = amount;
                }
            }

            if(index == 0) {
                if(mListener.mQuote != null) {
                    float min = mListener.mQuote.getFMin();
                    float max = mListener.mQuote.getFMax();
                    if (min > 0 && min < timeValueMin) {
                        timeValueMin = min;
                    }
                    if (max > 0 && max > timeValueMax) {
                        timeValueMax = max;
                    }
                }
            }

            float d1 = Math.abs(timeValueMax - close);
            float d2 = Math.abs(timeValueMin - close);
            float maxDelta = d1 > d2 ? d1 : d2;
            timeLineData.maxValue = close + maxDelta;
            timeLineData.minValue = close - maxDelta;
            return timeAmountMax;
        }

        public TimeLineData getTimeLineData(int index) {
            return cache.get(index);
        }

        public List<TrendDesc> getTimeLineEntities(int index) {
            TimeLineData timeLineData = cache.get(index);
            if(timeLineData != null) {
                return timeLineData.timeEntities;
            }
            return null;
        }

        public float getMaxValue(int index) {
            TimeLineData timeLineData = cache.get(index);
            if(timeLineData != null) {
                return timeLineData.maxValue;
            }
            return 0;
        }

        public float getMinValue(int index) {
            TimeLineData timeLineData = cache.get(index);
            if(timeLineData != null) {
                return timeLineData.minValue;
            }
            return 0;
        }

        public float getClose(int index) {
            TimeLineData timeLineData = cache.get(index);
            if(timeLineData != null) {
                return timeLineData.getClose();
            }
            return 0;
        }
    }

    private class TimeLineData {
        float maxValue;
        float minValue;
        private SecQuote secQuote;
        List<TrendDesc> timeEntities;

        public void clear() {
            if(timeEntities != null) {
                timeEntities.clear();
            }
            timeEntities = null;
        }

        public boolean check() {
            return checkData() && hasTimeEntities();
        }

        public boolean checkData() {
            return maxValue > 0 && minValue > 0 && secQuote != null;
        }

        public void setQuote(SecQuote quote) {
            secQuote = quote;
        }

        public float getClose() {
            return secQuote != null ? secQuote.getFClose() : 0;
        }

        public float getSecQuoteMax() {
            return secQuote != null ? secQuote.getFMax() : 0;
        }

        public float getSecQuoteMin() {
            return secQuote != null ? secQuote.getFMin() : 0;
        }

        public boolean hasTimeEntities() {
            return timeEntities != null && !timeEntities.isEmpty();
        }
    }

    synchronized void onYesterdayCloseUpdated(float close) {
        if (close == 0) {
            return;
        }

        mTimeLineDataCache.updateTotalMinMaxTimeValue();

        requestRender();
    }

    synchronized void onQuoteUpdated() {
        mTimeLineDataCache.putQuote(mListener.mQuote, 0);
        mTimeLineDataCache.updateTotalMinMaxTimeValue();
    }

    synchronized void onQuoteCompareUpdated() {
        mTimeLineDataCache.putQuote(mListener.mCompareQuote, 1);
        mTimeLineDataCache.updateTotalMinMaxTimeValue();
    }

    private boolean mIsTouching = false;

    void setTouching(boolean touching) {
        mIsTouching = touching;
    }

    boolean isTouching() {
        return mIsTouching;
    }
}