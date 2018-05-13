package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.Surface;
import android.view.TextureView;

import com.dengtacj.component.managers.IFavorManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.setting.manager.KLineSettingManager;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import BEC.CapitalDDZDesc;
import BEC.E_K_LINE_TYPE;
import BEC.E_SEC_STATUS;
import BEC.HisChipDistRsp;
import BEC.KLineDesc;
import BEC.RtMinDesc;
import BEC.RtMinReq;
import BEC.SecBsInfo;
import BEC.SecQuote;
import BEC.TrendDesc;

public class LineChartSurfaceTextureListener implements TextureView.SurfaceTextureListener {
    private static final String TAG = LineChartSurfaceTextureListener.class.getSimpleName();
    private LineChartDrawingThread mThread;
    int mTpFlag = DengtaConst.DEFAULT_TP_FLAG;
    ArrayList<RtMinDesc> mRtMinDescs;
    ArrayList<CapitalDDZDesc> mCapitalDDZEntities;
    ArrayList<CapitalDDZDesc> mFiveDayCapitalDDZEntities;
    SparseArray<List<TrendDesc>> mTimeLineData = new SparseArray<>();
    SparseBooleanArray mSupportData = new SparseBooleanArray();
    private SparseArray<List<KLineDesc>> mKLineData = new SparseArray<>();
    int mLineType = LineChartTextureView.TYPE_TIME;


    SecQuote mQuote;
    SecQuote mCompareQuote;
    HisChipDistRsp mHisChipDist;

    private CacheData mCacheData;

    /**
     * 因优品的均线数据有误，目前不显示指数的均线
     */
    boolean mIsIndex = false;

    /**
     * 当前指标类型，如成交量、MACD
     */
    int[] mTradingIndicatorTypes = new int[4];

    final int[] mTimeIndicatorTypes = new int[2];

    public static final int TOTAL_TIME_LINE_COUNT = 4 * 60;
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

    /**
     * 股票状态，是否停牌
     */
    int mSecStatus = E_SEC_STATUS.E_SS_NORMAL;

    /**
     * 是否全屏显示，目前全屏显示的分时成交量柱子显示会宽一些
     */
    boolean mIsFullScreen;

    public void setDtSecCode(String dtSecCode) {
        mDtSecCode = dtSecCode;
    }

    public void setIsShared(boolean isShared){
        mThread.setSharedForBreak(isShared);
    }

    String mDtSecCode;

    private HashMap<String, SecBsInfo> mBSInfos;

    LineChartResource mRes;

    public LineChartSurfaceTextureListener(final Context context) {
        mRes = new LineChartResource(context);
    }

    public void setDateAndIMinute(RtMinReq req) {
        if(mRtMinDescs != null && !mRtMinDescs.isEmpty()) {
            RtMinDesc rtMinDesc = mRtMinDescs.get(0);
            ArrayList<TrendDesc> trendDescList = rtMinDesc.getVTrendDesc();
            if(trendDescList != null && !trendDescList.isEmpty()) {
                int iMinute = trendDescList.get(trendDescList.size() - 1).getIMinute();
                String date = rtMinDesc.getSDate();
                DtLog.d(TAG, "setDateAndIMinute() date = " + date + ", iMinute = " + iMinute + ", mDtSecCode = " + mDtSecCode);
                req.setSDate(date);
                req.setIMinute(iMinute);
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        DtLog.d(TAG, "onSurfaceTextureAvailable: " + toString());
        final Surface drawingSurface = new Surface(surface);
        mThread = new LineChartDrawingThread(this, mRes);
        mThread.setTimeValueChangeListener(mTimeValueChangeListener);
        mThread.setKLineEventListener(mKLineEventListener);
        mThread.init(drawingSurface, width, height, mIsFullScreen);
        if(mQuote != null) {
            mThread.onQuoteUpdated();
        }

        if(mCompareQuote != null) {
            mThread.onQuoteCompareUpdated();
        }

        mThread.start();
        mThread.initHandler();

        mThread.clearBackground(); //不马上画空白背景的话,在小米和华为手机上back返回时会黑一下

        mThread.initKLineSettingValues();
        mThread.setLineType();

        mThread.onYesterdayCloseUpdated(getYesterdayClose());
        List<TrendDesc> timeEntities = mTimeLineData.get(mLineType);
        if (timeEntities != null) {
            mThread.setTimeEntities(timeEntities);
            if(mCacheData != null) {
                mThread.setTouching(mCacheData.isTouching());
                mThread.setTouchX(mCacheData.getTouchX());
            }

            if(mLineType == LineChartTextureView.TYPE_TIME) {
                List<TrendDesc> compareTimeEntities = mTimeLineData.get(LineChartTextureView.TYPE_COMPARE_TIME_LINE);
                if(compareTimeEntities != null) {
                    mThread.setCompareTimeEntities(compareTimeEntities);
                }
            }
        }


        if (mCapitalDDZEntities != null) {
            mThread.setTimeCapitalDDZEntities();
        }

        mThread.onTradingIndicatorTypeChanged();
        List<KLineDesc> kLineDescs = mKLineData.get(mLineType);
        mThread.setKLineEntities(kLineDescs);
        if(kLineDescs != null && mCacheData != null) {
            mThread.setTouching(mCacheData.isTouching());
            mThread.setTouchX(mCacheData.getTouchX());
        }
        mThread.setBSInfos(mBSInfos);

        requestRender(); //先画一帧，把边框画出来
    }

    public float getYesterdayClose() {
        switch (mLineType) {
            case LineChartTextureView.TYPE_TIME:
                return mQuote != null ? mQuote.getFClose() : 0;
            case LineChartTextureView.TYPE_FIVE_DAY:
                if(mRtMinDescs != null && !mRtMinDescs.isEmpty()) {
                    // 如果是新股，则无法通过RtMinDesc取到前一日收盘价，
                    // 所以只能通过SecQuote拿到前一日收盘价
                    if(mRtMinDescs.size() == 1) {
                        return mQuote != null ? mQuote.getFClose() : 0;
                    } else {
                        return mRtMinDescs.get(mRtMinDescs.size() - 1).getFPreClose();
                    }
                }
                break;
            default:
        }
        return 0;
    }

    float getNowPrice() {
        return mQuote != null ? mQuote.getFNow() : 0;
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
            if(mCacheData == null) {
                mCacheData = new CacheData();
            }
            mCacheData.setIsTouching(mThread.isTouching());
            mCacheData.setTouchX(mThread.getTouchX());

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

    public void setChipDist(final HisChipDistRsp hisChipDist) {
        if(hisChipDist != null) {
            mHisChipDist = hisChipDist;
            requestRender();
        }
    }

    public void setCompareQuote(final SecQuote quote) {
        if (quote == null) {
            return;
        }
        mCompareQuote = quote;
        if (mThread != null) {
            mThread.onQuoteCompareUpdated();
            mThread.requestRender();
        }
    }

    public void setQuote(final SecQuote quote) {
        if (quote == null) {
            return;
        }
        mQuote = quote;
        mSecStatus = quote.getESecStatus();
        int tpFlag = quote.getITpFlag();
        if (mTpFlag != tpFlag) {
            mTpFlag = tpFlag;
            if (mThread != null) {
                mThread.onTpFlagUpdated();
            }
        }
        if (mThread != null) {
            mThread.onQuoteUpdated();
            mThread.requestRender();
        }
    }

    public int getIndicatorHeight(){
        if (null != mThread){
            return mThread.getIndicatorHeight();
        }
        return 0;
    }

    public void setIsFullScreen(final Context context, final boolean isFullScreen) {
        mIsFullScreen = isFullScreen;
        mRes.setIsFullScreen(context, isFullScreen);
    }

    public void setTimeStr(String openTimeStr, String middleTimeStr, String closeTimeStr) {
        if (openTimeStr != null) {
            mOpenTimeStr = openTimeStr;
        }
        if (middleTimeStr != null) {
            mMiddleTimeStr = middleTimeStr;
        }
        if (closeTimeStr != null) {
            mCloseTimeStr = closeTimeStr;
        }
    }

    float getTouchX() {
        if (mThread != null) {
            return mThread.getTouchX();
        }
        return LineChartTextureView.INVALID_TOUCH_X;
    }

    void setTouchX(float touchX) {
        DtLog.d(TAG, "setTouchX is " + touchX);
        if (mThread != null) {
            mThread.setTouchX(touchX);
            mThread.requestRender();
        }
    }

    public void setCompareTimeEntities(ArrayList<TrendDesc> entities) {
        if (entities == null || entities.size() <= 0) {
            return;
        }
        mTimeLineData.put(LineChartTextureView.TYPE_COMPARE_TIME_LINE, entities);
        if (mThread != null) {
            if (mLineType == LineChartTextureView.TYPE_TIME) {
                mThread.setCompareTimeEntities(entities);
                if (getYesterdayClose() != 0) {
                    mThread.requestRender();
                }
            }
        }
    }

    public void removeCompare() {
        if (mThread != null) {
            mThread.clearCompareTimeEntities();
            mCompareQuote = null;
            if (mLineType == LineChartTextureView.TYPE_TIME) {
                if (getYesterdayClose() != 0) {
                    mThread.requestRender();
                }
            }
        }
    }

    public void setTimeEntities(ArrayList<TrendDesc> entities) {
        if (entities == null || entities.size() <= 0) {
            return;
        }
        mTimeLineData.put(LineChartTextureView.TYPE_TIME, entities);
        if (mThread != null) {
            if (mLineType == LineChartTextureView.TYPE_TIME) {
                mThread.setTimeEntities(entities);
                if (getYesterdayClose() != 0) {
                    mThread.requestRender();
                }
            }
        }
    }

    public void setSupportAverage(boolean supportAverage) {
        mSupportData.put(LineChartTextureView.TYPE_TIME, supportAverage);
    }

    public boolean supportAverage() {
        if(mLineType == LineChartTextureView.TYPE_FIVE_DAY) {
            return StockUtil.supportFiveDayTimeLine(mDtSecCode);
        } else {
            return mSupportData.get(mLineType);
        }
    }

    public void setRtMinDescs(ArrayList<RtMinDesc> rtMinDescs) {
        if(mRtMinDescs != null && !mRtMinDescs.isEmpty()) {
            updateRtMinDescs(rtMinDescs);
        } else {
            DtLog.d(TAG, "resetRtMinDesc() size = " + rtMinDescs.size() + ", mDtSecCode = " + mDtSecCode);
            resetRtMinDesc(rtMinDescs);
        }
    }

    private void updateRtMinDescs(ArrayList<RtMinDesc> rtMinDescs) {
        if(rtMinDescs != null && !rtMinDescs.isEmpty()) {
            DtLog.d(TAG, "updateRtMinDescs() size = " + rtMinDescs.size() + ", mDtSecCode = " + mDtSecCode);
            if(mRtMinDescs != null && !mRtMinDescs.isEmpty()) {
                RtMinDesc latestDesc = mRtMinDescs.get(0);
                RtMinDesc newDayDesc = rtMinDescs.get(rtMinDescs.size() - 1);
                if(TextUtils.equals(latestDesc.getSDate(), newDayDesc.getSDate())) {
                    List<TrendDesc> latestList = latestDesc.getVTrendDesc();
                    List<TrendDesc> newList = newDayDesc.getVTrendDesc();
                    if(latestList != null && !latestList.isEmpty()
                            && newList != null && !newList.isEmpty()) {
                        TrendDesc latest = latestList.get(latestList.size() - 1);
                        TrendDesc newest = newList.get(0);
                        int latestMinute = latest.getIMinute();
                        int newMinute = newest.getIMinute();
                        DtLog.d(TAG, "latestMinute = " + latestMinute + ", newMinute = " + newMinute);
                        if(latestMinute == newMinute && newList.size() > 1) {
                            newList.remove(0);
                            latestDesc.getVTrendDesc().addAll(newList);
                        }
                        resetRtMinDesc(mRtMinDescs);
                    }
                }
            }
        }
    }

    private void resetRtMinDesc(ArrayList<RtMinDesc> rtMinDescs) {
        if (rtMinDescs == null || rtMinDescs.size() <= 0) {
            return;
        }

        mRtMinDescs = rtMinDescs;

        ArrayList<TrendDesc> timeEntities = new ArrayList<>();
        int size = mRtMinDescs.size();
        for(int i=size-1; i>=0; i--) {
            timeEntities.addAll(mRtMinDescs.get(i).getVTrendDesc());
        }
        mTimeLineData.put(LineChartTextureView.TYPE_FIVE_DAY, timeEntities);

        if (mThread != null) {
            if (mLineType == LineChartTextureView.TYPE_FIVE_DAY) {
                mThread.setTimeEntities(timeEntities);
                if (getYesterdayClose() != 0) {
                    mThread.requestRender();
                }
            }
        }
    }

    public void setCapitalDDZEntities(ArrayList<CapitalDDZDesc> entities) {
        if (entities == null || entities.size() <= 0) {
            return;
        }

        mCapitalDDZEntities = entities;
        if (mThread != null) {
            mThread.setTimeCapitalDDZEntities();
        }
    }

    /**
     * 设置五日资金数据
     * @param entities
     */

    public void setFiveDayCapitalDDZEntities(ArrayList<CapitalDDZDesc> entities) {
        if (entities == null || entities.size() <= 0) {
            Log.i(TAG, "setFiveDayCapitalDDZEntities: entities == null || entities.size() <= 0"+entities);
            return;
        }

        mFiveDayCapitalDDZEntities = entities;
        if (mThread != null) {
            mThread.setTimeCapitalDDZEntities();
        }
    }

    SparseArray<Float> mKLineCapitalFlowList = new SparseArray<>();
    public void setKLineCapitalFlowData(ArrayList<CapitalDDZDesc> entities) {
        if (entities == null) {
            return;
        }

        for (CapitalDDZDesc entity : entities) {
            long time = entity.getLTime();
            int timeKey = StringUtil.timeStamp2DateYmd(time * 1000L);
            mKLineCapitalFlowList.put(timeKey, entity.getFSuperInAmt() - entity.getFSuperOutAmt() + entity.getFBigInAmt() - entity.getFBigOutAmt());
        }

        if (mThread != null) {
            if (mLineType == LineChartTextureView.TYPE_DAILY_K || mLineType == LineChartTextureView.TYPE_WEEKLY_K || mLineType == LineChartTextureView.TYPE_MONTHLY_K) {
                mThread.setKLineCapitalDDZEntities();
            }
        }
    }

    public int getLineType() {
        return mLineType;
    }

    boolean isMinuteKLineType() {
        switch (mLineType) {
            case LineChartTextureView.TYPE_ONE_MIN_K:
            case LineChartTextureView.TYPE_FIVE_MIN_K:
            case LineChartTextureView.TYPE_FIFTEEN_MIN_K:
            case LineChartTextureView.TYPE_THIRTY_MIN_K:
            case LineChartTextureView.TYPE_SIXTY_MIN_K:
                return true;
            default:
                break;
        }
        return false;
    }

    public void onTabSelected(int index) {
        DtLog.d(TAG, "onTabSelected: index " + index);
        boolean changed = index != mLineType;
        mLineType = index;
        for (int i = 0 ; i < mTradingIndicatorTypes.length ; i++) {
            if (i == 0)
                mTradingIndicatorTypes[0] = DengtaSettingPref.loadKLineIndicatorType0();
            Log.i(TAG, "onTabSelected:   mTradingIndicatorTypes[0]="+  mTradingIndicatorTypes[0]);
            if (i == 1)
                mTradingIndicatorTypes[1] = DengtaSettingPref.loadKLineIndicatorType1();
            Log.i(TAG, "onTabSelected:   mTradingIndicatorTypes[0]="+  mTradingIndicatorTypes[1]);
            if (i == 2)
                mTradingIndicatorTypes[2] = DengtaSettingPref.loadKLineIndicatorType2();
            Log.i(TAG, "onTabSelected:   mTradingIndicatorTypes[0]="+  mTradingIndicatorTypes[2]);
            if (i == 3)
                mTradingIndicatorTypes[3] = DengtaSettingPref.loadKLineIndicatorType3();
            Log.i(TAG, "onTabSelected:   mTradingIndicatorTypes[0]="+  mTradingIndicatorTypes[3]);
        }

        mTimeIndicatorTypes[0] = DengtaSettingPref.loadTimeLineIndicatorType0();
        mTimeIndicatorTypes[1] = DengtaSettingPref.loadTimeLineIndicatorType1();

        if (mThread != null) {
            mThread.setLineType();
            if(changed) {
                mThread.setTouchX(LineChartTextureView.INVALID_TOUCH_X);
                mThread.refreshIndicatorNum();
            }
            if (isKLineType(mLineType)) {
                mThread.initKLineSettingValues();
                mThread.setKLineEntities(mKLineData.get(mLineType));
                mThread.mLastZoomRatio = mThread.mZoomRatio;
            } else if(isTimeLine(mLineType)) {
                mThread.setTimeEntities(mTimeLineData.get(mLineType));
            }
        }
    }

    public static boolean isTimeLine(int type) {
        return type == LineChartTextureView.TYPE_TIME || type == LineChartTextureView.TYPE_FIVE_DAY;
    }

    /**
     *
     * 是不是K线
     *
     * */
    boolean isKLineType(int curKLineType) {
//            return curKLineType == TYPE_DAILY_K || curKLineType == TYPE_WEEKLY_K || curKLineType == TYPE_MONTHLY_K;
        return curKLineType >= LineChartTextureView.TYPE_DAILY_K;
    }

    LineChartTextureView.TimeValueChangeListener mTimeValueChangeListener;
    LineChartTextureView.KLineEventListener mKLineEventListener;

    public void setTimeValueChangeListener(LineChartTextureView.TimeValueChangeListener listener) {
        mTimeValueChangeListener = listener;
    }

    public void setKLineEventListener(LineChartTextureView.KLineEventListener listener) {
        mKLineEventListener = listener;
    }

    public void clearKLineEntities() {
        mKLineData = new SparseArray<>();
        if (mThread != null) {
            mThread.clearKLineEntities();
        }
    }

    public List<KLineDesc> getKLineEntities(int kLineType) {
        int type = getShowType(kLineType);
        return mKLineData.get(type);
    }

    public int getKLineStart() {
        if(mLineType == LineChartTextureView.TYPE_DAILY_K && mThread != null ) {
            return mThread.getKLineStart();
        }
        return -1;
    }

    public List<TrendDesc> getTimeEntities(int type) {
        return mTimeLineData.get(type);
    }

    public TrendDesc getLatestEntity(int type) {
        List<TrendDesc> entities = getTimeEntities(type);
        if(entities != null && !entities.isEmpty()) {
            return entities.get(entities.size() - 1);
        }
        return null;
    }

    public void setKLineEntities(List<KLineDesc> kLineEntities, int kLineType) {
        int type = getShowType(kLineType);
        mKLineData.put(type, kLineEntities);

        if (mThread != null) {
            if (mLineType == type) {
                mThread.setKLineEntities(mKLineData.get(mLineType));
            }
        }
    }

    public void setBSInfos(final ArrayList<SecBsInfo> bsInfos) {
        final int size = bsInfos == null ? 0 : bsInfos.size();
        if (size > 0) {
            final HashMap<String, SecBsInfo> bsInfoMap = new HashMap<>(size);
            final Iterator<SecBsInfo> it = bsInfos.iterator();
            SecBsInfo bsInfo;
            String date;
            while (it.hasNext()) {
                bsInfo = it.next();
                date = bsInfo.sDate.replaceAll("\\-", "");
                bsInfoMap.put(date, bsInfo);
            }
            mBSInfos = bsInfoMap;
        } else {
            mBSInfos = null;
        }
        if (mThread != null) {
            mThread.setBSInfos(mBSInfos);
        }
    }

    /**
     * 后台协议定义的K线类型和分时K线图当前的显示的K线类型并不一致，需要做一个映射，取得实际显示的K线类型
     * @param kLineType 后台协议定义的K线类型
     * @return 实际显示的K线类型
     */
    private int getShowType(int kLineType) {
        int type = LineChartTextureView.TYPE_DAILY_K;
        switch (kLineType) {
            case E_K_LINE_TYPE.E_KLT_DAY:
                type = LineChartTextureView.TYPE_DAILY_K;
                break;
            case E_K_LINE_TYPE.E_KLT_WEEK:
                type = LineChartTextureView.TYPE_WEEKLY_K;
                break;
            case E_K_LINE_TYPE.E_KLT_MONTH:
                type = LineChartTextureView.TYPE_MONTHLY_K;
                break;
            case E_K_LINE_TYPE.E_KLT_1_MIN:
                type = LineChartTextureView.TYPE_ONE_MIN_K;
                break;
            case E_K_LINE_TYPE.E_KLT_5_MIN:
                type = LineChartTextureView.TYPE_FIVE_MIN_K;
                break;
            case E_K_LINE_TYPE.E_KLT_15_MIN:
                type = LineChartTextureView.TYPE_FIFTEEN_MIN_K;
                break;
            case E_K_LINE_TYPE.E_KLT_30_MIN:
                type = LineChartTextureView.TYPE_THIRTY_MIN_K;
                break;
            case E_K_LINE_TYPE.E_KLT_60_MIN:
                type = LineChartTextureView.TYPE_SIXTY_MIN_K;
                break;
            default:
                break;
        }
        return type;
    }

    public int getServerKLineType(int showType) {
        int type = E_K_LINE_TYPE.E_KLT_DAY;
        switch (showType) {
            case LineChartTextureView.TYPE_DAILY_K:
                type = E_K_LINE_TYPE.E_KLT_DAY;
                break;
            case LineChartTextureView.TYPE_WEEKLY_K:
                type = E_K_LINE_TYPE.E_KLT_WEEK;
                break;
            case LineChartTextureView.TYPE_MONTHLY_K:
                type = E_K_LINE_TYPE.E_KLT_MONTH;
                break;
            case LineChartTextureView.TYPE_ONE_MIN_K:
                type = E_K_LINE_TYPE.E_KLT_1_MIN;
                break;
            case LineChartTextureView.TYPE_FIVE_MIN_K:
                type = E_K_LINE_TYPE.E_KLT_5_MIN;
                break;
            case LineChartTextureView.TYPE_FIFTEEN_MIN_K:
                type = E_K_LINE_TYPE.E_KLT_15_MIN;
                break;
            case LineChartTextureView.TYPE_THIRTY_MIN_K:
                type = E_K_LINE_TYPE.E_KLT_30_MIN;
                break;
            case LineChartTextureView.TYPE_SIXTY_MIN_K:
                type = E_K_LINE_TYPE.E_KLT_60_MIN;
                break;
            default:
                break;
        }
        return type;
    }

    public void setIsIndex(boolean isIndex) {
        mIsIndex = isIndex;
    }

    public boolean isIndex() {
        return mIsIndex;
    }

    void zoom(float ratio) {
        if (mThread != null && isKLineType(mLineType)) {
            mThread.zoom(ratio);
        }
    }

    void translate(float distance) {
        if (!mIsFullScreen) {
            return;
        }

        if (mThread != null && isKLineType(mLineType)) {
            mThread.translate(distance);
        }
    }

    public void setHasOldestKLineData(boolean hasAll) {
        if (mThread != null && isKLineType(mLineType)) {
            mThread.setHasOldestKLineData(hasAll);
        }
    }

    public void onTradingIndicatorTypeChanged(int indicatorType, int index) {
        mTradingIndicatorTypes[index] = indicatorType;
        if (mThread != null && isKLineType(mLineType)) {
            mThread.onTradingIndicatorTypeChanged();
        }
    }

    public void onTimeIndicatorTypeChanged(int indicatorType, int index) {
        mTimeIndicatorTypes[index] = indicatorType;
        if (mThread != null && !isKLineType(mLineType)) {
            mThread.onTimeIndicatorTypeChanged();
        }
    }

    public void setTradingMinutes(int tradingMinutes) {
        // 对后台异常数据进行保护，如果计算出来的tradingMinutes小于等于0，则仍然使用默认数据
        if(tradingMinutes > 0) {
            if (mTradingMinutes != tradingMinutes) {
                mTradingMinutes = tradingMinutes;
                requestRender();
            }
        }
    }

    int getNextTradingIndicatorType(int index) {
        int nextType = mTradingIndicatorTypes[index];

        nextType = DengtaApplication.getApplication().getKLineSettingManager().getNextType(nextType);
        return nextType;
    }

    int getNextTimeIndicatorType(int index) {
        return (mTimeIndicatorTypes[index] + 1) % SettingConst.TIME_LINE_INDICATOR_TYPE_COUNT;
    }

    int getBottomRectTop() {
        int height = 0;
        if (mThread != null) {
            height = mThread.getBottomRectTop();
        }
        return height;
    }

    int clickArea(final float x, final float y) {
        if (mThread != null) {
            return mThread.clickArea(x, y);
        }
        return 0;
    }

    public void rememberLastZoomRatio() {
        if (mThread != null) {
            mThread.rememberLastZoomRatio();
        }
    }

    void setTouching(boolean touching) {
        if (mThread != null) {
            mThread.setTouching(touching);
        }
    }

    boolean isTouchEntrance(int x, int y) {
        if(mThread != null) {
            return mThread.isTouchEntrance(x, y);
        }
        return false;
    }

    void moveLeft() {
        DtLog.d(TAG, "moveLeft()");
        if (mThread != null) {
            mThread.moveLeft();
            mThread.requestRender();
        }
    }

    void moveRight() {
        DtLog.d(TAG, "moveRight()");
        if (mThread != null) {
            mThread.moveRight();
            mThread.requestRender();
        }
    }

    boolean supportEntrance() {
        return !mIsFullScreen && (mLineType == LineChartTextureView.TYPE_TIME
                || mLineType == LineChartTextureView.TYPE_DAILY_K)
                && StockUtil.supportHistoryTimeLine(mDtSecCode);
    }
}

final class CacheData {
    private float mTouchX;
    private boolean mIsTouching = false;


    public void setTouchX(float touchX) {
        mTouchX = touchX;
    }

    public float getTouchX() {
        return mTouchX;
    }

    public void setIsTouching(boolean isTouching) {
        mIsTouching = isTouching;
    }

    public boolean isTouching() {
        return mIsTouching;
    }
}

final class LineChartResource {
    TextDrawer mTextDrawer;

    final int mTimeAmountColor;
    int mBgColor;
    int mTimeLineColor;
    int mTimeAverageLineColor;
    int mCompareButtonColor;

    int mKLineAverageColor1;
    int mKLineAverageColor2;
    int mKLineAverageColor3;
    int mKLineAverageColor4;
    int mKLineAverageColor5;
    int mKLineAverageColor6;
    int mEntranceBgColor;

    int mBorderRectColor;
    int mShadowColor;
    int mKColorGray;
    int mKColorIndicatorBg;
    int mKColorIndicatorText;
    int mDefaultGrayTextColor;
    int mIndicatorPreTextColor;
    int mIndicatorPreTextBgColor;
    int mIndicatorAdBgColor;
    int mEntranceTextColor;
    int mAdTextColor;

    int mTouchLineColor;

    int mBorderStrokeWidth;
    int mLineStrokeWidth;

    int mTradingTimeAreaHeight;
    int mIndicatorRectMarginTop;

    int mDateTextGap;

    int mEntranceTouchExpand;

    int mKColorRed;
    int mKColorAlphaRed;
    int mKColorGreen;
    int mKColorAlphaGreen;
    int mKLineStrokeWidth;

    int mKLineIndicatorHorizontalGap;
    int mKLineIndicatorWidth;
    int mKLineIndicatorHeight;
    int mKLineIndicatorTextMarginTop;

    int mIndicatorPreTextPaddingLeftRight;
    int mIndicatorPreTextBgHeight;
    int mIndicatorPreTextBgRoundRadius;

    int mPreTextSize;
    int mTextSize;
    int mTextHeight;
    int mTextBottom;
    int mPreTextHeight;
    int mPreTextBottom;

    int mEntranceWidth;
    int mEntranceHeight;
    int mEntranceTextSize;
    int mEntranceTextHeight;
    int mEntranceTextBottom;
    int mTouchLineStrokeWidth;
    int mAdTextSize;

    float mTimeHorizontalGap;

    int mKLineAverageTextAreaHeightPerLine;
    int mKLineAverageTextAreaHeight;
    int mKLineAverageTextGap;

    /* kdj */
    public float DEFALUT_KD_VALUE = 50;
    int K_KDJ_KCOLOR;
    int K_KDJ_DCOLOR;
    int K_KDJ_JCOLOR;
    /* rsi */
    int K_RSI_1COLOR;
    int K_RSI_2COLOR;
    int K_RSI_3COLOR;

    String mIndicatorNotSupportPlaceHolder;
    String mMaRise, mMaDown;

    Bitmap mBPointBitmap;
    float mBOffsetX;
    Bitmap mSPointBitmap;
    float mSOffsetX;
    float mBSMinGap;
    float mBSMaxGap;

    Bitmap mBreakUpPointBitmap;
    Bitmap mBreakUpPointBitmapRotate;
    Bitmap mBreakDownPointBitmap;
    Bitmap mBreakDownPointBitmapRotate;
    Bitmap mShareForMoreBitmap;

    Path mShadowPath;
    Path mTimePath;
    Path mTimeAveragePath;
    Path mTimeCapitalDDZPathSuper;
    Path mTimeCapitalDDZPathBig;
    Path mTimeCapitalDDZPathMid;
    Path mTimeCapitalDDZPathSmall;

    Path mTimeVolumeRatioPath;

    Path mKLineMACDDiffPath;
    Path mKLineMACDDeaPath;

    Path mKLinePathMA1;
    Path mKLinePathMA2;
    Path mKLinePathMA3;

    Rect mSurfaceRect;

    Paint mKLineTextPaint;
    Paint mPreTextPaint;
    Paint mFillPaint;
    Paint mPaintRect;
    Paint mPaintShadow;
    Paint mPaintLine;
    Paint mPaintDashLine;
    Paint mPaintDashPressureLine;
    Paint mPaintKLine;
    Paint mPaintTouchLine;
    Paint mPaintEntrance;
    Paint mPaintAmount;
    Paint mShaderPaint;

    // ---------- 指标相关 ----------
    int mIndicatorRectHeight;
    int mIndicatorAdWidth;
    int mIndicatorAdTextMarginLeft;
    int mIndicatorAdTextMarginBottom;
    int mIndicatorAdTextLineHeight;
    int mIndicatorAdTextLineMargin;
    // ---------- 指标相关 ----------

    int mIndicatorClickArea;

    LineChartResource(final Context context) {
        final Resources resources = context.getResources();

        TypedArray a = context.obtainStyledAttributes(new int[] {
                R.attr.line_chart_bg, R.attr.line_chart_border_rect_color,
                R.attr.line_chart_time_line_shadow_color,
                R.attr.k_line_indicator_bg_color,
                R.attr.k_line_indicator_text_color,
                R.attr.line_chart_touch_line_color
        });
        mBgColor = a.getColor(0, Color.LTGRAY);
        mBorderRectColor = a.getColor(1, Color.LTGRAY);
        mShadowColor = a.getColor(2, Color.LTGRAY);
        mKColorIndicatorBg = a.getColor(3, Color.LTGRAY);
        mEntranceTextColor = a.getColor(4, Color.LTGRAY);
        mKColorIndicatorText = 0xff929292;
        mTouchLineColor = a.getColor(5, Color.LTGRAY);
        a.recycle();

        mIndicatorClickArea = DeviceUtil.dip2px(context, 100);
        mDefaultGrayTextColor = resources.getColor(R.color.time_line_indicator_color);
        mIndicatorPreTextColor = resources.getColor(R.color.indicator_pre_text_color);
        mIndicatorPreTextBgColor = resources.getColor(R.color.indicator_pre_text_bg_color);
        mIndicatorAdBgColor = resources.getColor(R.color.indicator_ad_bg_color);
        mAdTextColor = resources.getColor(R.color.default_text_color_100);
        mCompareButtonColor = resources.getColor(R.color.select_compare_stock_button_color);

        mKColorGray = ContextCompat.getColor(context, R.color.default_text_color_60);
        mTimeLineColor = resources.getColor(R.color.line_chart_time_line);
        mBorderStrokeWidth = resources.getDimensionPixelSize(R.dimen.line_chart_divider_strokeWidth);
        mLineStrokeWidth = resources.getDimensionPixelSize(R.dimen.line_chart_time_strokeWidth);
        mTradingTimeAreaHeight = resources.getDimensionPixelSize(R.dimen.line_chart_trading_time_area_height);
        mIndicatorRectMarginTop = resources.getDimensionPixelSize(R.dimen.indicator_rect_margin_top);

        mDateTextGap = resources.getDimensionPixelSize(R.dimen.line_chart_date_gap);
        mEntranceTouchExpand = resources.getDimensionPixelSize(R.dimen.entrance_touch_expand);
        mTimeAverageLineColor = resources.getColor(R.color.line_chart_time_average_line);
        mTimeAmountColor = resources.getColor(R.color.line_chart_time_amount);

        mKLineStrokeWidth = resources.getDimensionPixelSize(R.dimen.line_chart_k_stroke_width);
        mKLineIndicatorHorizontalGap = resources.getDimensionPixelSize(R.dimen.line_chart_k_indicator_horizontal_gap);
        mKLineIndicatorWidth = resources.getDimensionPixelSize(R.dimen.line_chart_k_indicator_width);
        mKLineIndicatorHeight = resources.getDimensionPixelSize(R.dimen.line_chart_k_indicator_height);
        mKLineIndicatorTextMarginTop = resources.getDimensionPixelSize(R.dimen.line_chart_k_indicator_marginTop);

        mIndicatorPreTextPaddingLeftRight = resources.getDimensionPixelSize(R.dimen.indicator_pre_text_padding_left_right);
        mIndicatorPreTextBgHeight = resources.getDimensionPixelSize(R.dimen.indicator_pre_text_bg_height);
        mIndicatorPreTextBgRoundRadius = resources.getDimensionPixelSize(R.dimen.indicator_pre_text_bg_round_radius);

        mKColorRed = resources.getColor(R.color.stock_red_color);
        mKColorAlphaRed = resources.getColor(R.color.stock_red_color_alpha);
        mKColorGreen = resources.getColor(R.color.stock_green_color);
        mKColorAlphaGreen = resources.getColor(R.color.stock_green_color_alpha);
        mPreTextSize = resources.getDimensionPixelSize(R.dimen.font_size_10);
        mTextSize = resources.getDimensionPixelSize(R.dimen.font_size_11);
        mKLineAverageTextAreaHeightPerLine = resources.getDimensionPixelSize(R.dimen.k_line_average_text_area_height);
        mKLineAverageTextGap = resources.getDimensionPixelSize(R.dimen.k_line_average_text_gap);

        mKLineAverageColor1 = ContextCompat.getColor(context, R.color.k_line_average_1);
        mKLineAverageColor2 = ContextCompat.getColor(context, R.color.k_line_average_2);
        mKLineAverageColor3 = ContextCompat.getColor(context, R.color.k_line_average_3);
        mKLineAverageColor4 = ContextCompat.getColor(context, R.color.k_line_average_4);
        mKLineAverageColor5 = ContextCompat.getColor(context, R.color.k_line_average_5);
        mKLineAverageColor6 = ContextCompat.getColor(context, R.color.k_line_average_6);
        mEntranceBgColor = ContextCompat.getColor(context, R.color.default_orange_color);

        K_KDJ_KCOLOR = mKLineAverageColor2;
        K_KDJ_DCOLOR = mKLineAverageColor3;
        K_KDJ_JCOLOR = mKLineAverageColor1;
        K_RSI_1COLOR = mKLineAverageColor1;
        K_RSI_2COLOR = mKLineAverageColor2;
        K_RSI_3COLOR = mKLineAverageColor3;

        mKLineTextPaint = new Paint();
        mKLineTextPaint.setTextSize(mTextSize);
        mKLineTextPaint.setAntiAlias(true);

        mPreTextPaint = new Paint();
        mPreTextPaint.setTextSize(mPreTextSize);
        mPreTextPaint.setAntiAlias(true);

        mFillPaint = new Paint();
        mFillPaint.setStyle(Paint.Style.FILL);

        mTextDrawer = new TextDrawer();
        mTextHeight = mTextDrawer.measureSingleTextHeight(mTextSize, TextDrawer.getTypeface());
        mTextBottom = mTextDrawer.measureSingleTextBottom(mTextSize, TextDrawer.getTypeface());
        mPreTextHeight = mTextDrawer.measureSingleTextHeight(mPreTextSize, TextDrawer.getTypeface());
        mPreTextBottom = mTextDrawer.measureSingleTextBottom(mPreTextSize, TextDrawer.getTypeface());

        mEntranceWidth = resources.getDimensionPixelSize(R.dimen.linechart_entrance_width);
        mEntranceHeight = resources.getDimensionPixelSize(R.dimen.linechart_entrance_height);
        mEntranceTextSize = resources.getDimensionPixelSize(R.dimen.font_size_10);
        mEntranceTextHeight = mTextDrawer.measureSingleTextHeight(mEntranceTextSize, TextDrawer.getTypeface());
        mEntranceTextBottom = mTextDrawer.measureSingleTextBottom(mEntranceTextSize, TextDrawer.getTypeface());
        mTouchLineStrokeWidth = resources.getDimensionPixelSize(R.dimen.touch_line_stroke_width);

        mAdTextSize = resources.getDimensionPixelSize(R.dimen.font_size_12);

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

        mPaintDashPressureLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintDashPressureLine.setStyle(Paint.Style.STROKE);
        mPaintDashPressureLine.setColor(mBorderRectColor);
        mPaintDashPressureLine.setPathEffect(pathEffect);


        mPaintKLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintKLine.setTypeface(TextDrawer.getTypeface());

        mPaintTouchLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTouchLine.setColor(mTouchLineColor);
        mPaintTouchLine.setStrokeWidth(mTouchLineStrokeWidth);

        mPaintEntrance = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintEntrance.setStrokeWidth(mLineStrokeWidth);

        mShadowPath = new Path();
        mTimePath = new Path();
        mTimeAveragePath = new Path();
        mTimeCapitalDDZPathSuper = new Path();
        mTimeCapitalDDZPathBig = new Path();
        mTimeCapitalDDZPathMid = new Path();
        mTimeCapitalDDZPathSmall = new Path();
        mTimeVolumeRatioPath = new Path();
        mPaintRect.setStyle(Paint.Style.STROKE);
        mPaintRect.setStrokeWidth(mBorderStrokeWidth);
        mPaintRect.setColor(mBorderRectColor);

        mKLineMACDDiffPath = new Path();
        mKLineMACDDeaPath = new Path();

        mKLinePathMA1 = new Path();
        mKLinePathMA2 = new Path();
        mKLinePathMA3 = new Path();

        mMaRise = resources.getString(R.string.ma_rise);
        mMaDown = resources.getString(R.string.ma_down);
        mIndicatorNotSupportPlaceHolder = resources.getString(R.string.indicator_not_support_placeholder);

        mBSMinGap = resources.getDimension(R.dimen.k_line_bs_min_gap);
        mBSMaxGap = resources.getDimension(R.dimen.k_line_bs_max_gap);

        mBPointBitmap = BitmapFactory.decodeResource(resources, R.drawable.k_line_b_point);
        mBOffsetX = mBPointBitmap.getWidth() / 2f;
        mSPointBitmap = BitmapFactory.decodeResource(resources, R.drawable.k_line_s_point);
        mSOffsetX = mSPointBitmap.getWidth() / 2f;

        mBreakUpPointBitmap = BitmapFactory.decodeResource(resources, R.drawable.k_line_build_position);
        mBreakUpPointBitmapRotate = BitmapFactory.decodeResource(resources, R.drawable.k_line_build_position_rotate);
        mBreakDownPointBitmap = BitmapFactory.decodeResource(resources, R.drawable.k_line_sell_position);
        mBreakDownPointBitmapRotate = BitmapFactory.decodeResource(resources, R.drawable.k_line_sell_position_rotate);
        mShareForMoreBitmap = BitmapFactory.decodeResource(resources, R.drawable.share_for_more);

        mPaintAmount = new Paint();
        mPaintAmount.setStyle(Paint.Style.STROKE);
        mPaintAmount.setColor(mTimeAmountColor);

        mIndicatorRectHeight = resources.getDimensionPixelSize(R.dimen.line_chart_bottom_rect_height_2);
        mIndicatorAdWidth = resources.getDimensionPixelSize(R.dimen.line_chart_indicator_ad_width);
        mIndicatorAdTextMarginLeft = resources.getDimensionPixelSize(R.dimen.line_chart_indicator_ad_text_margin_left);
        mIndicatorAdTextMarginBottom = resources.getDimensionPixelSize(R.dimen.line_chart_indicator_ad_text_margin_bottom);
        mIndicatorAdTextLineHeight = resources.getDimensionPixelSize(R.dimen.line_chart_indicator_ad_text_line_height);
        mIndicatorAdTextLineMargin = resources.getDimensionPixelSize(R.dimen.line_chart_indicator_ad_text_line_margin);
        mShaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setIsFullScreen(Context context, boolean isFullScreen) {
        if(isFullScreen) {
            final Resources resources = context.getResources();
            mIndicatorAdWidth = resources.getDimensionPixelSize(R.dimen.line_chart_indicator_ad_width_fullscreen);
            mIndicatorAdTextMarginBottom = resources.getDimensionPixelSize(R.dimen.line_chart_indicator_ad_text_margin_bottom_fullscreen);
            mIndicatorAdTextLineHeight = resources.getDimensionPixelSize(R.dimen.line_chart_indicator_ad_text_line_height_fullscreen);
        }
    }
}
