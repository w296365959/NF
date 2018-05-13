package com.sscf.investment.detail.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sscf.investment.R;
import com.sscf.investment.main.DataPref;
import com.sscf.investment.sdk.utils.DtLog;

import java.util.ArrayList;

/**
 * Created by liqf on 2016/2/16.
 */
public class HorizontalPopupEntranceGroup extends FrameLayout implements View.OnClickListener {
    public static final int ANIMATOR_EXPAND_DURATION = 200;
    public static final int ANIMATOR_COLLAPSE_DURATION = 150;
    private static final String TAG = "HorizontalPopupEntranceGroup";
    private Context mContext;
    private boolean mIsExpanded = false;
    private boolean mIsAnimating = false;

    private ImageView mGroupEntrance;

    private AnimatorSet mAnimatorSet;

    private int mTranslateDistance = 150;

    private OnEntranceClickedListener mOnEntranceListener;
    private boolean mIsInFullscreen;

    private int mCount = 0;

    public HorizontalPopupEntranceGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        initResources();

        initGroupEntrance();
    }

    private void initGroupEntrance() {
        mGroupEntrance = new ImageView(mContext);
        mGroupEntrance.setImageResource(R.drawable.entrance_group);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.LEFT|Gravity.BOTTOM);
        addView(mGroupEntrance, lp);

        mGroupEntrance.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsAnimating) {
                    DtLog.d(TAG, "mIsAnimating return");
                    return;
                }

                if (!mIsExpanded) {
                    expandChildrenWithAnimation(true);
                } else {
                    expandChildrenWithAnimation(false);
                }
            }
        });
    }

    public void setIsInFullscreen(boolean isInFullscreen) {
        mIsInFullscreen = isInFullscreen;
    }

    public void setOnEntranceListener(OnEntranceClickedListener listener) {
        mOnEntranceListener = listener;
    }

    private void initResources() {
        Resources resources = mContext.getResources();
        mTranslateDistance = resources.getDimensionPixelSize(R.dimen.entrance_translation_distance);
    }

    public void addChildIcon(final int childIconId, final int tag) {
        mCount++;
        final ImageView child = new ImageView(mContext);
        child.setImageResource(childIconId);
        child.setTag(tag);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.LEFT | Gravity.BOTTOM);
        lp.leftMargin = mTranslateDistance * mCount;
        addView(child, lp);
        child.setOnClickListener(this);
    }

    public void expandChildrenWithAnimation(boolean needExpand) {
        if (mIsAnimating) {
            return;
        }

        mIsAnimating = true;

        if (needExpand) {
            if (!mIsExpanded) {
                mAnimatorSet = new AnimatorSet();
                ArrayList<Animator> animators = new ArrayList<>();
                int delay = 0;
                for (int i = 1; i <= mCount; i++) {
                    final View child = getChildAt(i);
                    FrameLayout.LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                    int leftMargin = layoutParams.leftMargin;
                    child.setTranslationX(-leftMargin);
                    child.setAlpha(0);
                    ValueAnimator animatorTranslate = ObjectAnimator.ofFloat(child, "translationX", -leftMargin, 0);
                    animatorTranslate.setInterpolator(new OvershootInterpolator());
                    animatorTranslate.setDuration(ANIMATOR_EXPAND_DURATION);
                    animatorTranslate.setStartDelay(delay);
                    animatorTranslate.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            child.setVisibility(VISIBLE);
                            mGroupEntrance.setRotation(180);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    ValueAnimator animatorAlpha = ObjectAnimator.ofFloat(child, "alpha", 0, 1.0f);
                    animatorAlpha.setDuration(ANIMATOR_EXPAND_DURATION);
                    animatorAlpha.setStartDelay(delay);

                    animators.add(animatorTranslate);
                    animators.add(animatorAlpha);

                    delay += ANIMATOR_EXPAND_DURATION / 2;
                }
                mAnimatorSet.playTogether(animators);

                mIsExpanded = true;
            }
        } else { //播放收拢来的动画
            if (mIsExpanded) {
                mAnimatorSet = new AnimatorSet();
                ArrayList<Animator> animators = new ArrayList<>();
                int delay = 0;
                for (int i = 1; i <= mCount; i++) {
                    final View child = getChildAt(i);
                    FrameLayout.LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                    int leftMargin = layoutParams.leftMargin;
                    ValueAnimator animatorTranslate = ObjectAnimator.ofFloat(child, "translationX", 0, -leftMargin);
                    animatorTranslate.setInterpolator(new AccelerateDecelerateInterpolator());
                    animatorTranslate.setDuration(ANIMATOR_COLLAPSE_DURATION);
                    animatorTranslate.setStartDelay(delay);
                    animatorTranslate.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mGroupEntrance.setRotation(0);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            child.setVisibility(GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    ValueAnimator animatorAlpha = ObjectAnimator.ofFloat(child, "alpha", 1.0f, 0);
                    animatorAlpha.setDuration(ANIMATOR_COLLAPSE_DURATION);
                    animatorAlpha.setStartDelay(delay);

                    animators.add(animatorTranslate);
                    animators.add(animatorAlpha);

                    delay += ANIMATOR_COLLAPSE_DURATION / 2;
                }
                mAnimatorSet.playTogether(animators);

                mIsExpanded = false;
            }
        }

        DataPref.setLineChartGroupEntranceExpanded(mIsExpanded);
        if (mAnimatorSet == null) {
            return;
        }
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsAnimating = false;
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

    @Override
    public void onClick(View v) {
        final Object tag = v.getTag();
        if (mOnEntranceListener != null && !mIsAnimating && tag != null) {
            mOnEntranceListener.onEntranceClicked((Integer) tag);
        }
    }

    public void setExpanded(boolean expanded) {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
            mAnimatorSet = null;
        }

        mIsExpanded = expanded;

        for (int i = 1; i <= mCount; i++) {
            ImageView child = (ImageView) getChildAt(i);
            child.setVisibility(expanded ? VISIBLE : GONE);
        }

        if (expanded) {
            for (int i = 1; i <= mCount; i++) {
                ImageView child = (ImageView) getChildAt(i);
                child.setTranslationX(0);
                child.setAlpha(1.0f);
            }

            mGroupEntrance.setRotation(180);
        } else {
            mGroupEntrance.setRotation(0);
        }
    }

    public interface OnEntranceClickedListener {
        void onEntranceClicked(int index);
    }
}
