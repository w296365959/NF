package com.sscf.investment.detail.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import com.dengtacj.component.entity.ShareParams;
import com.dengtacj.component.entity.ShareType;
import com.dengtacj.component.managers.IShareManager;
import com.sscf.investment.R;
import com.sscf.investment.detail.dialog.IndicatorSelectDialog;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.*;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.socialize.ShareDialog;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;

import BEC.SecQuote;

/**
 * Created by liqf on 2015/7/17.
 */
public final class LineChartTextureView extends TextureView {
    private static final String TAG = LineChartTextureView.class.getSimpleName();
    public static final float INVALID_TOUCH_X = Float.MIN_VALUE;
    public static final String ACTION_K_LINE_INDICATOR_TYPE_CHANGED = "action_k_line_indicator_type_changed";
    public static final String ACTION_TIME_LINE_INDICATOR_TYPE_CHANGED = "action_time_line_indicator_type_changed";
    public static final String KEY_CHART_VIEW_HASH_CODE = "key_chart_view_hash_code";
    public static final String KEY_INDICATOR_INDEX = "key_indicator_index";
    private final GestureDetector mGesture;
    private final int mTouchSlop;

    private boolean mIsTouchMode = false;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_DETAIL = 1;
    public static final int STATE_CANCEL_DETAIL = 2;
    private int mChartState = STATE_NORMAL;
    private boolean mIsTranslating = false;

    public static final int TYPE_TIME = 0;
    public static final int TYPE_FIVE_DAY = 1;
    public static final int TYPE_DAILY_K = 2;
    public static final int TYPE_WEEKLY_K = 3;
    public static final int TYPE_MONTHLY_K = 4;
    public static final int TYPE_ONE_MIN_K = 5;
    public static final int TYPE_FIVE_MIN_K = 6;
    public static final int TYPE_FIFTEEN_MIN_K = 7;
    public static final int TYPE_THIRTY_MIN_K = 8;
    public static final int TYPE_SIXTY_MIN_K = 9;
    public static final int TYPE_COMPARE_TIME_LINE = 1000;


    static final int FIVE_DAY_COUNT = 5;

    /**
     * 当前触摸在屏幕上的手指个数
     */
    private int mode;

    /**
     * 上一次屏幕上的手指个数
     */
    private int lastMode;

    /**
     * 表示是否刚刚双指缩放过(主要用于防止竖屏双指缩放和平移手势的冲突)
     */
    private boolean justZoomed;

    /**
     * 双指落在屏幕上准备缩放时的距离
     */
    private float oldDist;

    /**
     * 第一个手指落在屏幕上的X位置
     */
    private float xDown;

    /**
     * Fling手势引发的平移动画
     */
    private ValueAnimator mTranslateAnimator;

    private GestureDetector.OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            DtLog.d(TAG, "onFling, velocityX = " + velocityX);

            if (mode == 1 && lastMode <= 1) {
                float distance = velocityX / 5;
                DtLog.d(TAG, "onFling: distance = " + distance);
                int duration = (int) (Math.abs(distance) / 1.5f);
                DtLog.d(TAG, "onFling: duration = " + duration);
                mTranslateAnimator = ValueAnimator.ofFloat(0, distance).setDuration(duration);
                mTranslateAnimator.setInterpolator(new DecelerateInterpolator());
                mTranslateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        translate(value);
                    }
                });
                mTranslateAnimator.start();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(MotionEvent event) {
            LineChartSurfaceTextureListener textureListener = getTextureListener();
            if (textureListener == null) {
                return;
            }

            try {
                if (mIsTouchMode && !isTouchEntrance(event)) {
                    setChartState(STATE_DETAIL);
                    textureListener.setTouchX(event.getX()); //首次把touch位置设为非invalid，这样后续的touch才会触发画触摸线
                    setParentScrollable(false);

                    int lineType = textureListener.getLineType();
                    if (lineType == TYPE_TIME) {
                        if (textureListener.mIsFullScreen) {
                            StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_TOUCH_LINE_SHOW_TIME);
                        } else {
                            StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_SHOW_ENLARGE_TIMELINE_ENTRANCE);
                        }
                    } else {
                        if (textureListener.mIsFullScreen) {
                            StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_TOUCH_LINE_SHOW_K);
                        } else {
                            StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TOUCH_LINE_SHOW_K);
                            if(textureListener.supportEntrance()) {
                                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_SHOW_HISTORY_TIMELINE_ENTRANCE);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            DtLog.d(TAG, "onDoubleTap: y = " + e.getY());
            float y = e.getY();
            LineChartSurfaceTextureListener textureListener = getTextureListener();
            if (textureListener != null) {
                if (y < textureListener.getBottomRectTop()) {
                    if (mDoubleTapListener != null) {
                        mDoubleTapListener.onDoubleTap();
                    }
                }
            }

            return super.onDoubleTap(e);
        }
    };

    public boolean isDetailState() {
        return mChartState == STATE_DETAIL;
    }

    public void enterDetailState() {
        LineChartSurfaceTextureListener textureListener = getTextureListener();
        if (textureListener == null) {
            return;
        }
        setChartState(STATE_DETAIL);
        textureListener.setTouchX(250);
    }

    public void exitDetailState() {
        LineChartSurfaceTextureListener textureListener = getTextureListener();
        if (textureListener == null) {
            return;
        }
        setChartState(STATE_CANCEL_DETAIL);
    }

    private boolean isTouchEntrance(MotionEvent e) {
        LineChartSurfaceTextureListener textureListener = getTextureListener();
        if(textureListener != null) {
            return textureListener.isTouchEntrance((int)e.getX(), (int)e.getY());
        }
        return false;
    }

    private OnDoubleTapListener mDoubleTapListener;
    public void setOnDoubleTapListener(final OnDoubleTapListener doubleTapListener) {
        mDoubleTapListener = doubleTapListener;
    }

    private OnEntranceClickListener mOnEnranceClickListener;
    public void setOnEntranceClickListener(final OnEntranceClickListener onEntranceClickListener) {
        mOnEnranceClickListener = onEntranceClickListener;
    }

    private OnStateChangeListener mOnStateChangeListener;
    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        mOnStateChangeListener = onStateChangeListener;
    }

    public interface OnDoubleTapListener {
        void onDoubleTap();
    }

    public interface OnEntranceClickListener {
        void onEntranceClick(int lineType);
    }

    public interface OnStateChangeListener {
        void onStateChange(int oldState, int newState);
    }

    public LineChartTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mGesture = new GestureDetector(context, mOnGesture);

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGesture.onTouchEvent(event);
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        DtLog.d(TAG, "onTouch: event is " + action);
        LineChartSurfaceTextureListener textureListener = getTextureListener();
        if (textureListener == null) {
            return super.onTouchEvent(event);
        }

        boolean justUpInTouchMode = false;
        boolean justUpFromIndicatorChange = false;
        boolean justUpFromTranslating = false;
        boolean justUpFromZoom = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mTranslateAnimator != null) {
                    mTranslateAnimator.cancel();
                }

                // 如果点击到了入口区域，则发送回调，并且不触发
                if(isTouchEntrance(event)) {
                    if(mOnEnranceClickListener != null) {
                        if(textureListener != null) {
                            mOnEnranceClickListener.onEntranceClick(textureListener.getLineType());
                        }
                    }
                    return false;
                }

                setChartState(STATE_CANCEL_DETAIL);
                textureListener.setTouchX(INVALID_TOUCH_X);
                mIsTranslating = false;
                mode = 1;
                lastMode = 0;
                justZoomed = false;
                xDown = event.getRawX();
                translate(0);
                break;
            case MotionEvent.ACTION_UP:
                mode = 0;
                lastMode = 0;

                switch (mChartState) {
                    case STATE_DETAIL:
                        justUpInTouchMode = true;
                        break;
                    case STATE_CANCEL_DETAIL:
                        justUpInTouchMode = true;
                        setChartState(STATE_NORMAL);
                        break;
                    default:
                }

//                setTouching(false);
                if (mIsTranslating) {
                    justUpFromTranslating = true;
                }
                mIsTranslating = false;
                if (justZoomed) {
                    justUpFromZoom = true;
                    justZoomed = false;
                }
//                textureListener.setTouchX(INVALID_TOUCH_X);
                setParentScrollable(true);

                if (!justUpInTouchMode && !justUpFromTranslating && !justUpFromZoom) {
                    justUpFromIndicatorChange = onClick(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                lastMode = mode;
                mode -= 1;
                textureListener.rememberLastZoomRatio();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                lastMode = mode;
                mode += 1;

                if (mode == 2) {
                    setParentScrollable(false);
                    justZoomed = true;
                    oldDist = spacing(event);
                    //                DtLog.d(TAG, "onTouch: oldDist = " + oldDist);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float rawX = event.getRawX();
                if (mode == 1) {
                    if (textureListener.getTouchX() != INVALID_TOUCH_X) { //表示已经处在显示触摸线状态
                        textureListener.setTouchX(event.getX());
                    } else {
                        if (lastMode <= 1) {
                            float distance = rawX - xDown;
                            translate(distance);
                            if (Math.abs(distance) > mTouchSlop) {
                                mIsTranslating = true;
                            }
                        }
                    }
                } else if (mode == 2) {
                    float newDist = spacing(event);
                    DtLog.d(TAG, "onTouch: newDist = " + newDist);
                    if (newDist > oldDist + 0) {
                        zoom(newDist / oldDist);
//                        oldDist = newDist;
                    }
                    if (newDist < oldDist - 0) {
                        zoom(newDist / oldDist);
//                        oldDist = newDist;
                    }
                    if (textureListener.getTouchX() != INVALID_TOUCH_X) { //表示已经处在显示触摸线状态
                        textureListener.setTouchX(event.getX(0)); //触摸十字针的线始终跟随第一个手指头
                    }
                }
                break;
            default:
                break;
        }

        if (!justUpInTouchMode && !justUpFromIndicatorChange && !justUpFromZoom) { //刚从长按状态松手时或者刚刚点击切换了指标类型时避免触发onClick
            super.onTouchEvent(event); //这样点击进入全屏的操作才能生效，因为onClick是在super里面触发执行的
        }

        return true; //这样才能接收到后续的move事件以便持续绘制触摸线
    }

    private boolean onClick(final float x, final float y) {
        final LineChartSurfaceTextureListener textureListener = getTextureListener();
        final int area = textureListener.clickArea(x, y);
        switch (area) {
            case LineChartDrawingThread.AREA_1_INDICATOR:
                if (textureListener.isKLineType(textureListener.getLineType())) {
                    changeToNextKLineIndicator(0);
                } else {
                    changeToNextTimeLineIndicator(0);

                }
                Log.i(TAG, "onClick: 区域0");
                StatisticsUtil.reportAction(StatisticsConst.K_LINE_INDICATOR_SWITCH_CLICKED);
                break;
            case LineChartDrawingThread.AREA_2_INDICATOR:
                if (textureListener.isKLineType(textureListener.getLineType())) {
                    changeToNextKLineIndicator(1);
                } else {
                    changeToNextTimeLineIndicator(1);
                }
                Log.i(TAG, "onClick: 区域1");
                StatisticsUtil.reportAction(StatisticsConst.K_LINE_INDICATOR_SWITCH_CLICKED);
                break;
            case LineChartDrawingThread.AREA_3_INDICATOR:
                if (textureListener.isKLineType(textureListener.getLineType())) {
                    changeToNextKLineIndicator(2);
                }
                Log.i(TAG, "onClick: 区域2");
                StatisticsUtil.reportAction(StatisticsConst.K_LINE_INDICATOR_SWITCH_CLICKED);
                break;
            case LineChartDrawingThread.AREA_4_INDICATOR:
                if (textureListener.isKLineType(textureListener.getLineType())) {
                    changeToNextKLineIndicator(3);
                }
                Log.i(TAG, "onClick: 区域3");
                StatisticsUtil.reportAction(StatisticsConst.K_LINE_INDICATOR_SWITCH_CLICKED);
                break;
            case LineChartDrawingThread.AREA_1_INDICATOR_TEXT:
                clickIndicatorText(0);
                Log.i(TAG, "onClick:  0");
                break;
            case LineChartDrawingThread.AREA_2_INDICATOR_TEXT:
                clickIndicatorText(1);
                Log.i(TAG, "onClick: 1");
                break;
            case LineChartDrawingThread.AREA_3_INDICATOR_TEXT:
                clickIndicatorText(2);
                Log.i(TAG, "onClick: 2");
                break;
            case LineChartDrawingThread.AREA_4_INDICATOR_TEXT:
                clickIndicatorText(3);
                Log.i(TAG, "onClick: 3");
                break;
            case LineChartDrawingThread.AREA_3_SHARE:
                if (null == getContext()){
                    return false;
                }
                getShareDialog().showShareDialog(getShareParams());
                break;
            default:
                return false;
        }
        return true;
    }

    private void clickIndicatorText(int areaNum) {
        final LineChartSurfaceTextureListener textureListener = getTextureListener();
        final IndicatorSelectDialog dialog = new IndicatorSelectDialog(getContext());
        final boolean isKLineType = textureListener.isKLineType(textureListener.getLineType());
        if(supportIndicator()) {
            dialog.init(isKLineType ? 1 : 0, isKLineType ? textureListener.mTradingIndicatorTypes[areaNum]
                            : textureListener.mTimeIndicatorTypes[areaNum],
                    (selectIndicator)-> {
                        if (isKLineType) {
                            changeToKLineIndicator(areaNum, selectIndicator);
                        } else {
                            changeToTimeLineIndicator(areaNum, selectIndicator);
                        }
                    });
            dialog.show();
            StatisticsUtil.reportAction(StatisticsConst.K_LINE_INDICATOR_SWITCH_DIALOG_SHOW);
        }
    }

    private ShareDialog mShareDialog;

    private ShareDialog getShareDialog() {
        if (mShareDialog == null) {
            mShareDialog = new ShareDialog((Activity) getContext(), ShareType.MOMENTS);
        }
        mShareDialog.setShareListener( (state, plat) ->  {
                if (state == IShareManager.STATE_SUCCESS){//分享chengg
                    getTextureListener().setIsShared(true);
                    SettingPref.putIBoolean(SettingConst.KEY_SHARED_FOR_BREAK, true);
                }
            });
        return mShareDialog;
    }
    public ShareParams getShareParams() {
        ShareParams params = new ShareParams();

        final String stockDetailUrl = DengtaApplication.getApplication().getUrlManager().getBreakUpUrl();
        params.url = stockDetailUrl;
        params.imageUrl = DengtaApplication.getApplication().getUrlManager().getShareIconUrl();
        params.title = getContext().getResources().getString(R.string.share_title_break);
        // 分享时刻最新价/涨跌幅
        return params;
    }

    private boolean supportIndicator() {
        LineChartSurfaceTextureListener textureListener = getTextureListener();
        if (textureListener != null) {
            String dtSecCode = textureListener.mDtSecCode;
            int type = textureListener.getLineType();
            if(type == TYPE_FIVE_DAY) {
                return !StockUtil.unSupportFiveDayTimeLine(dtSecCode);
            }
        } else {
            return false;
        }
        return true;
    }

    void setChartState(final int state) {
        int finalState = STATE_NORMAL;
        switch (mChartState) {
            case STATE_NORMAL :
                finalState = switchNormalState(state);
                break;
            case STATE_DETAIL :
                finalState = switchDetailState(state);
                break;
            case STATE_CANCEL_DETAIL :
                finalState = switchDetailCancelState(state);
                break;
        }
        if(mOnStateChangeListener != null) {
            mOnStateChangeListener.onStateChange(mChartState, finalState);
        }
        mChartState = finalState;
        LineChartSurfaceTextureListener textureListener = getTextureListener();
        if (textureListener != null) {
            textureListener.setTouching(mChartState == STATE_DETAIL);
        }
    }

    private int switchNormalState(int state) {
        switch (state) {
            case STATE_DETAIL:
                return STATE_DETAIL;
            default:
                return STATE_NORMAL;
        }
    }

    private int switchDetailState(int state) {
        switch (state) {
            case STATE_NORMAL:
                return STATE_NORMAL;
            case STATE_CANCEL_DETAIL:
                return STATE_CANCEL_DETAIL;
            default:
                return STATE_DETAIL;
        }
    }

    private int switchDetailCancelState(int state) {
        switch (state) {
            case STATE_NORMAL:
                return STATE_NORMAL;
            case STATE_DETAIL:
                return STATE_DETAIL;
            default:
                return STATE_CANCEL_DETAIL;
        }
    }

    public void changeToNextTimeLineIndicator(int index) {
        LineChartSurfaceTextureListener textureListener = getTextureListener();
        if (textureListener == null) {
            return;
        }

        int nextType = textureListener.getNextTimeIndicatorType(index);
        changeToTimeLineIndicator(index, nextType);
    }

    public void changeToTimeLineIndicator(int index, int type) {
        LineChartSurfaceTextureListener textureListener = getTextureListener();
        if (textureListener == null) {
            return;
        }

        switch (index) {
            case 0:
                SettingPref.putInt(SettingConst.KEY_SETTING_TIME_INDICATORS_TYPE_INDEX_0, type);
                break;
            case 1:
                SettingPref.putInt(SettingConst.KEY_SETTING_TIME_INDICATORS_TYPE_INDEX_1, type);
                break;
            default:
        }

        textureListener.onTimeIndicatorTypeChanged(type, index);

        if (!textureListener.mIsFullScreen) {
            StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TIME_LINE_INDICATOR_SWITCH_CLICKED);
        }

        Intent intent = new Intent(ACTION_TIME_LINE_INDICATOR_TYPE_CHANGED);
        intent.putExtra(KEY_CHART_VIEW_HASH_CODE, hashCode());
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(intent);
    }

    public void changeToNextKLineIndicator(int index) {
        LineChartSurfaceTextureListener textureListener = getTextureListener();
        if (textureListener == null) {
            return;
        }

        int nextType = textureListener.getNextTradingIndicatorType(index);
        changeToKLineIndicator(index, nextType);
    }

    public void changeToKLineIndicator(int index, int type) {
        LineChartSurfaceTextureListener textureListener = getTextureListener();
        if (textureListener == null) {
            return;
        }

        switch (index) {
            case 0:
                SettingPref.putInt(SettingConst.KEY_SETTING_INDICATORS_TYPE_0, type);
                break;
            case 1:
                SettingPref.putInt(SettingConst.KEY_SETTING_INDICATORS_TYPE_1, type);
                break;
            case 2:
                SettingPref.putInt(SettingConst.KEY_SETTING_INDICATORS_TYPE_2, type);
                break;
            case 3:
                SettingPref.putInt(SettingConst.KEY_SETTING_INDICATORS_TYPE_3, type);
                break;
        }
        textureListener.onTradingIndicatorTypeChanged(type, index);

        if (!textureListener.mIsFullScreen) {
            StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TIME_LINE_INDICATOR_SWITCH_CLICKED);
        }
        sendBroadCastUpdateIndicator(index);
    }

    private void sendBroadCastUpdateIndicator(int index) {
        Intent intent = new Intent(ACTION_K_LINE_INDICATOR_TYPE_CHANGED);
        intent.putExtra(KEY_CHART_VIEW_HASH_CODE, hashCode());
        intent.putExtra(KEY_INDICATOR_INDEX, index);
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(intent);
    }

    private LineChartSurfaceTextureListener getTextureListener() {
        return (LineChartSurfaceTextureListener) getSurfaceTextureListener();
    }

    private void zoom(float ratio) {
        if (mIsTouchMode) {
            DtLog.d(TAG, "zoom f is " + ratio);
            LineChartSurfaceTextureListener textureListener = getTextureListener();
            if (textureListener != null) {
                textureListener.zoom(ratio);
            }
        }
    }

    private void translate(float distance) {
        if (mIsTouchMode) {
            DtLog.d(TAG, "translate distance is " + distance);
            LineChartSurfaceTextureListener textureListener = getTextureListener();
            if (textureListener != null) {
                textureListener.translate(distance);
            }
        }
    }

    private float spacing(MotionEvent event) {
        try {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return 1f;
    }

    public void setIsTouchMode(boolean isTouchMode) {
        mIsTouchMode = isTouchMode;
    }

    private void setParentScrollable(boolean scrollable) {
        boolean disallowParent = !scrollable;
        getParent().requestDisallowInterceptTouchEvent(disallowParent);
    }

    public void stopAnimation() {
        if (mTranslateAnimator != null) {
            mTranslateAnimator.cancel();
            mTranslateAnimator = null;
        }
    }

    public void moveLeft() {
        final LineChartSurfaceTextureListener textureListener = getTextureListener();
        if(textureListener != null) {
            textureListener.moveLeft();
        }
    }

    public void moveRight() {
        final LineChartSurfaceTextureListener textureListener = getTextureListener();
        if(textureListener != null) {
            textureListener.moveRight();
        }
    }

    public interface TimeValueChangeListener {
        void onValuePositionChanged(Point point);
    }

    public interface KLineEventListener {
        void onMoreDataNeeded(int kLineType, int start, int wantNum);

        void onTimeLineTouchChanged(final TimeLineInfosView.TimeLineTouchEvent event);

        void onKLineTouchChanged(final KLineInfosView.KLineLineTouchEvent event);

        void onKLineAverageChanged(final KLineInfosView.KLineAverageInfo averageInfo);

        void onKLineHeightUpdate(final int kChartHeight);
    }
}

