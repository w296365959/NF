package com.sscf.investment.payment;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.widget.ImageView;

import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;

/**
 * Created by yorkeehuang on 2017/5/15.
 */

public class PaymentLoadingDialog extends Dialog implements ValueAnimator.AnimatorUpdateListener {

    private static final int MAX_PROGRESS = 10000;
    private TypedArray mRefreshArray;
    private ValueAnimator mAnimator;
    private ImageView mImageView;

    public PaymentLoadingDialog(Context context) {
        super(context, R.style.dialog_center_theme);
        setContentView(R.layout.dialog_payment_loading);
        initAnimation(context);
    }

    private void initAnimation(Context context) {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        final String packageName = dengtaApplication.getPackageName();
        final Resources resources = dengtaApplication.getResources();
        final int REFRESH_LENGTH = 28;

        final int[] refreshDrawalbeIds = new int[REFRESH_LENGTH];

        for (int i = 0; i < REFRESH_LENGTH; i++) {
            refreshDrawalbeIds[i] = resources.getIdentifier("refresh_frame_2_" + String.format("%02d", i), "attr", packageName);
        }
        mRefreshArray = context.obtainStyledAttributes(refreshDrawalbeIds);

        mImageView = (ImageView) findViewById(R.id.loading_img);
        mImageView.setImageResource(mRefreshArray.getResourceId(0, getDefaultDrawableResId()));
        final ValueAnimator animator = ValueAnimator.ofInt(0, MAX_PROGRESS);
        animator.setDuration(800L);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(this);
        mAnimator = animator;
    }

    protected int getDefaultDrawableResId() {
        return R.drawable.refresh_frame_1_00;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        final Integer progress = (Integer) animation.getAnimatedValue();
        final int length = mRefreshArray.length();
        final int frame = progress * (length - 1) / MAX_PROGRESS;
        mImageView.setImageResource(mRefreshArray.getResourceId(frame, getDefaultDrawableResId()));
    }

    public void startAnim() {
        stopAnim();
        if (mAnimator != null) {
            mAnimator.start();
        }
    }

    public void stopAnim() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }
}
