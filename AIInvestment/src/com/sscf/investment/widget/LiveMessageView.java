package com.sscf.investment.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.interpolator.ExpoEaseIn;
import com.sscf.investment.interpolator.ExpoEaseOut;
import com.sscf.investment.main.DataPref;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import BEC.E_SEC_LIVE_MSG_TYPE;

/**
 * Created by liqf on 2016/5/5.
 */
public class LiveMessageView extends RelativeLayout {
    private static final String TAG = LiveMessageView.class.getSimpleName();
    private static final long ANIMATION_DURATION = 200;
    private static final long DISPLAY_DURATION = 4000;
    public static final int MAX_TIME_GAP = 60 * 1000;
    private String mMsgText = "";
    private int mMsgType = E_SEC_LIVE_MSG_TYPE.E_SLMT_NORMAL;
    private int mLiveType = 0; //0:自选股直播；1：大盘直播
    private int mMsgTime = -1;
    private ImageView mLeftIcon;
    private TextView mRightIndicator;
    private TextView mMsgTextView;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private TextDrawer mTextDrawer;
    private int mTextSize;
    public static final int LIVE_TYPE_SEC_DETAIL = -1;
    private Runnable mShowMsgRunnable;

    public void setFullscreen(boolean fullscreen) {
        mIsFullscreen = fullscreen;
    }

    private boolean mIsFullscreen = false;

    //估计值：mTextExtraWidth + 中间文字宽度 = 整个View的宽度
    private int mTextExtraWidth;

    private RectF mRectBg = new RectF();
    private int mColorBg;
    private Paint mPaint;

    public LiveMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(getContext()).inflate(R.layout.live_message, this, true);

        initResources(context);

        initViews();
    }

    private void initViews() {
        mLeftIcon = (ImageView) findViewById(R.id.left_icon);
        mRightIndicator = (TextView) findViewById(R.id.right_indicator);
        mMsgTextView = (TextView) findViewById(R.id.content_text);
    }

    private void initResources(Context context) {
        Resources resources = context.getResources();
        mTextDrawer = new TextDrawer();
        mTextSize = resources.getDimensionPixelSize(R.dimen.font_size_12);
        mTextExtraWidth = resources.getDimensionPixelOffset(R.dimen.live_message_text_extraWidth);
        mColorBg = resources.getColor(R.color.black_80);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();

        mRectBg.set(0, 0, mDrawingWidth, mDrawingHeight);
        int radius = mDrawingHeight / 2;
        mPaint.setColor(mColorBg);
        canvas.drawRoundRect(mRectBg, radius, radius, mPaint);
    }

    public int getLiveType() {
        return mLiveType;
    }

    public boolean setData(String msg, int msgType, int msgTime, int liveType) {
        if (msgTime == mMsgTime || TextUtils.isEmpty(msg)) {
            return false;
        }

        removeCallbacks(mShowMsgRunnable);

        mMsgText = msg.trim();
        mMsgType = msgType;
        mLiveType = liveType;
        mMsgTime = msgTime;
        mLeftIcon.setImageResource(getLeftIconByType(mMsgType));

        //如果time在最近的MAX_TIME_GAP时间以内就显示，否则什么都不做
        if (liveType == LIVE_TYPE_SEC_DETAIL) {
            long time = msgTime * 1000L;
            long currentTimeMillis = System.currentTimeMillis();
            DtLog.d(TAG, "setData: msgTime = " + time + ", currentTimeMillis = " + currentTimeMillis + ", delta = " + (currentTimeMillis - time));
            if (!(Math.abs(currentTimeMillis - time) < MAX_TIME_GAP)) {
                return false;
            }
            if (!mIsFullscreen) {
                StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_LIVE_MSG_DISPLAYED);
            } else {
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_KLINE_LIVE_MSG_DISPLAYED);
            }
        } else {
            DataPref.setLastLiveMsgTime(msgTime);
            StatisticsUtil.reportAction(StatisticsConst.LIVE_MSG_DISPLAYED);
        }

        setVisibility(INVISIBLE);
        mMsgTextView.setText(mMsgText);

        initShowMsgRunnable();

        removeCallbacks(mShowMsgRunnable);
        postDelayed(mShowMsgRunnable, 200);
        return true;
    }

    private void initShowMsgRunnable() {
        if (mShowMsgRunnable == null) {
            mShowMsgRunnable = new Runnable() {
                @Override
                public void run() {
                    int lineCount = mMsgTextView.getLineCount();
                    DtLog.d(TAG, "setData: lineCount = " + lineCount);
                    ViewGroup.LayoutParams lp = getLayoutParams();
                    if (lineCount == 1) {
                        int textWidth = mTextDrawer.measureSingleTextWidth(mMsgTextView.getText(), mTextSize, null);
                        lp.width = textWidth + mTextExtraWidth;
                    } else {
                        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    }
                    setLayoutParams(lp);
                    show();
                }
            };
        }
    }

    private int getLeftIconByType(int msgType) {
        switch (msgType) {
            case E_SEC_LIVE_MSG_TYPE.E_SLMT_BAD:
                return R.drawable.reminder_bad;
            case E_SEC_LIVE_MSG_TYPE.E_SLMT_GOOD:
                return R.drawable.reminder_good;
            default:
                return R.drawable.reminder_normal;
        }
    }

    public void show() {
        ValueAnimator animatorShow = ObjectAnimator.ofFloat(this, "alpha", 0, 1);
        animatorShow.setDuration(ANIMATION_DURATION);
        animatorShow.setEvaluator(new ExpoEaseIn(ANIMATION_DURATION));
        animatorShow.start();
        setVisibility(VISIBLE);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                hide();
            }
        }, DISPLAY_DURATION);
    }

    public void hide() {
        ValueAnimator animatorHide = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        animatorHide.setDuration(ANIMATION_DURATION);
        animatorHide.setEvaluator(new ExpoEaseOut(ANIMATION_DURATION));
        animatorHide.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorHide.start();
    }

}
