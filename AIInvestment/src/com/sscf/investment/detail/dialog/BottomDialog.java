package com.sscf.investment.detail.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.interpolator.ExpoEaseIn;
import com.sscf.investment.interpolator.ExpoEaseOut;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.widget.linechart.TimeLineChartSurfaceTextureListener;
import com.sscf.investment.widget.linechart.TimeLineChartTextureView;
import com.sscf.investment.widget.linechart.TimeLineInfo;

import java.util.ArrayList;

import BEC.SecQuote;
import BEC.TrendDesc;

import static com.sscf.investment.detail.view.DetailActionBar.ANIMATION_DURATION;

/**
 * Created by yorkeehuang on 2017/5/10.
 */

public abstract class BottomDialog extends Dialog implements View.OnClickListener, DialogInterface.OnShowListener {

    public static final int HISTORY_TIME_LINE = 1;
    public static final int ENLARGE_TIME_LINE = 2;
    public static final int CALLAUCTION_LINE = 3;

    private int mType;

    protected ArrayList<TrendDesc> mTimeLineEntities;

    private SecDetailInfo mDetailInfo;
    final protected TimeLineInfo mTimeLineInfo = new TimeLineInfo();
    private Handler mUIHanlder;

    protected SecQuote mSecQuote;
    protected TimeLineChartTextureView mSurface;
    private TimeLineChartSurfaceTextureListener mSurfaceListener;
    private OnShowListener mOnShowListener;

    protected int mRedColor;
    protected int mGreenColor;
    protected int mGrayColor;

    public BottomDialog(@NonNull Context context, int type) {
        super(context, R.style.dialog_share_theme);
        mType = type;
        setContentView(getLayout());
        DeviceUtil.enableDialogTranslucentStatus(getWindow(), context.getResources().getColor(R.color.black_40));
        initResources();
        initHandler();
        initView();
        super.setOnShowListener(this);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if(mOnShowListener != null) {
            mOnShowListener.onShow(dialog);
        }
        final View contentView = findViewById(R.id.content);
        ValueAnimator animatorExpand = ObjectAnimator.ofFloat(contentView, "translationY", contentView.getMeasuredHeight(), 0);
        animatorExpand.setDuration(ANIMATION_DURATION);
        animatorExpand.setEvaluator(new ExpoEaseIn(ANIMATION_DURATION));
        animatorExpand.start();

        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(0, 255).setDuration(ANIMATION_DURATION);
        alphaAnimator.setEvaluator(new ExpoEaseIn(ANIMATION_DURATION));
        alphaAnimator.start();
    }

    public void dismissWithAnimation() {
        final View contentView = findViewById(R.id.content);
        ValueAnimator animatorExpand = ObjectAnimator.ofFloat(contentView, "translationY", 0, contentView.getMeasuredHeight());
        animatorExpand.setDuration(ANIMATION_DURATION);
        animatorExpand.setEvaluator(new ExpoEaseOut(ANIMATION_DURATION));
        animatorExpand.start();
        animatorExpand.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(255, 0).setDuration(ANIMATION_DURATION);
        alphaAnimator.setEvaluator(new ExpoEaseOut(ANIMATION_DURATION));
        alphaAnimator.start();
    }

    @Override
    public void setOnShowListener(OnShowListener listener) {
        mOnShowListener = listener;
    }

    public void onResume() {
        refresh();
    }

    public void onPause() {

    }

    private void initHandler() {
        mUIHanlder = new Handler(Looper.getMainLooper());
    }

    public boolean updateQuote(SecQuote secQuote) {
        if(secQuote != null) {
            mSecQuote = secQuote;
            return true;
        }
        return false;
    }

    private void initResources() {
        Resources resources = getContext().getResources();
        mRedColor = resources.getColor(R.color.stock_red_color);
        mGreenColor = resources.getColor(R.color.stock_green_color);
        mGrayColor = ContextCompat.getColor(getContext(), R.color.default_text_color_100);
    }

    public int getType() {
        return mType;
    }

    protected abstract int getLayout();

    protected int getSurfaceId() {
        return R.id.time_line_chart;
    }

    private TimeLineChartTextureView getSurface() {
        View textureView = findViewById(getSurfaceId());
        if(textureView instanceof TimeLineChartTextureView) {
            return (TimeLineChartTextureView) textureView;
        }
        return null;
    }

    protected int getCloseButtonId() {
        return R.id.close_button;
    }

    private View getCloseButton() {
        return findViewById(getCloseButtonId());
    }

    protected int getBackgroundId() {
        return R.id.background;
    }

    private View getBackground() {
        return findViewById(getBackgroundId());
    }

    private void initView() {
        mSurface = getSurface();
        mSurfaceListener = new TimeLineChartSurfaceTextureListener(getContext());
        mSurface.setSurfaceTextureListener(mSurfaceListener);

        View closeButton = getCloseButton();
        if(closeButton != null) {
            closeButton.setOnClickListener(this);
        }

        View backgound = getBackground();
        if(backgound != null) {
            backgound.setOnClickListener(this);
        }
    }

    protected void runOnUiThread(Runnable runnable) {
        if(Looper.getMainLooper().getThread() == Thread.currentThread()) {
            runnable.run();
        } else {
            mUIHanlder.post(runnable);
        }
    }

    private void refresh() {
        if(isShowing() && mTimeLineEntities != null) {
            setTimeLineEntities(mTimeLineInfo, mTimeLineEntities);
        }
    }

    public void setTimeLineEntities(TimeLineInfo info, ArrayList<TrendDesc> entities) {
        mTimeLineEntities = entities;
        if(mSurface != null) {
            mSurface.setTimeLineEntities(info, entities);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == getCloseButtonId() || v.getId() == getBackgroundId()) {
            dismissWithAnimation();
        }
    }

    protected SecDetailInfo initDetailInfo() {
        if(mDetailInfo == null) {
            mDetailInfo = new SecDetailInfo();
        }
        return mDetailInfo;
    }

    protected SecDetailInfo getDetailInfo() {
        return mDetailInfo;
    }

    public class SecDetailInfo {
        private float mOpen;
        private float mClose;
        private float mYesterdayClose;
        private float mHigh;
        private float mLow;
        private float mDelta;
        private long mAmount;
        private long mVolume;
        private float mTurnover;

        private boolean mIsSuspended = false;

        public void setOpen(float open) {
            mOpen = open;
        }

        public float getOpen() {
            return mOpen;
        }

        public void setClose(float close) {
            mClose = close;
        }

        public float getClose() {
            return mClose;
        }

        public void setYesterdayClose(float yesterdayClose) {
            mYesterdayClose = yesterdayClose;
        }

        public float getYesterdayClose() {
            return mYesterdayClose;
        }

        public void setHigh(float high) {
            mHigh = high;
        }

        public float getHigh() {
            return mHigh;
        }

        public void setLow(float low) {
            mLow = low;
        }

        public float getLow() {
            return mLow;
        }

        public void setDelta(float delta) {
            mDelta = delta;
        }

        public float getDelta() {
            return mDelta;
        }

        public void setAmount(long amount) {
            mAmount = amount;
        }

        public long getAmount() {
            return mAmount;
        }

        public void setVolume(long volume) {
            mVolume = volume;
        }

        public long getVolume() {
            return mVolume;
        }

        public void setTurnover(float turnover) {
            mTurnover = turnover;
        }

        public float getTurnover() {
            return mTurnover;
        }

        public void setIsSuspended(boolean isSuspended) {
            mIsSuspended = isSuspended;
        }

        public boolean isSuspended() {
            return mIsSuspended;
        }
    }
}
