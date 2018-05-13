package com.sscf.investment.detail.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import com.sscf.investment.R;
import com.sscf.investment.interpolator.SineEaseInOut;
import com.sscf.investment.sdk.utils.DtLog;

/**
 * Created by liqf on 2015/8/7.
 */
public class IndicatorAnimationView extends View implements ViewTreeObserver.OnPreDrawListener {
    private static final String TAG = IndicatorAnimationView.class.getSimpleName();
    public static final float INNER_RATIO = 3.0f;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private Paint mPaintInner;
    private float mRadiusInner;
    private Paint mPaintOuter;
    private float mRadiusOuter;
    private int mColorInner;

    public static final int INDICATOR_ANIMATION_DURATION = 3000;
    public static final float INDICATOR_EASE_IN_RATIO = 0.4f;
    private AnimatorSet mAnimatorSet;
    private boolean mIsAnimating;
    private Point mPoint = new Point(0, 0);

    public void setOuterRatio(float outerRatio) {
        this.outerRatio = outerRatio;
    }

    public float getOuterRatio() {
        return outerRatio;
    }

    private float outerRatio;

    public float getOuterAlpha() {
        return outerAlpha;
    }

    public void setOuterAlpha(float outerAlpha) {
        this.outerAlpha = outerAlpha;
    }

    private float outerAlpha;

    private Handler mHandler = new Handler();

    private Runnable mRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    public IndicatorAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initResources();

        getViewTreeObserver().addOnPreDrawListener(this);
    }

    private void initResources() {
        Resources resources = getResources();
        mColorInner = resources.getColor(R.color.indicator_animation_inner);

        mPaintInner = new Paint();
        mPaintInner.setColor(mColorInner);
        mPaintOuter = new Paint();
        mPaintOuter.setColor(mColorInner);
    }

    public void updateCenterPoint(final Point point) {
        if (!mIsAnimating || point == null) {
            if (getVisibility() == VISIBLE) {
                setVisibility(INVISIBLE);
            }
            return;
        }

        if (point.equals(mPoint)) {
            return;
        }

        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }

        mPoint.set(point.x, point.y);

        DtLog.d(TAG, "updateCenterPoint: indicator point = " + point);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
        lp.setMargins(point.x - mDrawingWidth / 2, point.y - mDrawingHeight / 2, lp.rightMargin, lp.bottomMargin);
        setLayoutParams(lp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();
        mRadiusOuter = mDrawingWidth / 2.0f;
        mRadiusInner = mRadiusOuter / INNER_RATIO;

        if (mPoint.equals(0, 0)) {
            return;
        }
//        DtLog.d(TAG, "onDraw: start");

        mRadiusOuter = mDrawingWidth / 2 * outerRatio;
        mPaintOuter.setAlpha((int) (outerAlpha * 255));
        canvas.drawCircle(mDrawingWidth / 2, mDrawingHeight / 2, mRadiusOuter, mPaintOuter);
        canvas.drawCircle(mDrawingWidth / 2, mDrawingHeight / 2, mRadiusInner, mPaintInner);

        if (mIsAnimating) {
            invalidateDelayed(100);
        }
//        DtLog.d(TAG, "onDraw: end. outerRatio = " + outerRatio + ", outerAlpha = " + outerAlpha);
    }

    private void invalidateDelayed(final long delay) {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(mRefreshRunnable, delay);
    }

    public void startAnimation() {
        if (mIsAnimating) {
            DtLog.d(TAG, "startAnimation: already started");
            return;
        }
        DtLog.d(TAG, "startAnimation: now start");

        mIsAnimating = true;

        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }

        ValueAnimator animator1 = ObjectAnimator.ofFloat(this, "outerRatio", 1.0f / INNER_RATIO, 1.0f);
        animator1.setDuration(INDICATOR_ANIMATION_DURATION);
        animator1.setEvaluator(new SineEaseInOut(INDICATOR_ANIMATION_DURATION));

        //前半段变亮
        ValueAnimator animator2 = ObjectAnimator.ofFloat(this, "outerAlpha", 0, 0.3f);
        animator2.setDuration((long) (INDICATOR_ANIMATION_DURATION * INDICATOR_EASE_IN_RATIO));
        animator2.setEvaluator(new SineEaseInOut(INDICATOR_ANIMATION_DURATION * INDICATOR_EASE_IN_RATIO));

        //后半段变淡
        ValueAnimator animator3 = ObjectAnimator.ofFloat(this, "outerAlpha", 0.3f, 0);
        animator3.setDuration((long) (INDICATOR_ANIMATION_DURATION * (1 - INDICATOR_EASE_IN_RATIO)));
        animator3.setEvaluator(new SineEaseInOut(INDICATOR_ANIMATION_DURATION * (1 - INDICATOR_EASE_IN_RATIO)));

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(animator2).with(animator1);
        mAnimatorSet.play(animator3).after(animator2);

        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mIsAnimating) {
                    mAnimatorSet.start(); //整个动画循环播放
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimatorSet.start();
    }

    public void stopAnimation() {
        DtLog.d(TAG, "stopAnimation: now stop");
        mIsAnimating = false;
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }

        setVisibility(INVISIBLE);

    }

    @Override
    public boolean onPreDraw() {
        if (mDrawingWidth == 0 || mDrawingHeight == 0) { //避免onDraw还没有执行过，导致宽高都为0
            mDrawingWidth = getMeasuredWidth();
            mDrawingHeight = getMeasuredHeight();
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnPreDrawListener(this);
    }
}
