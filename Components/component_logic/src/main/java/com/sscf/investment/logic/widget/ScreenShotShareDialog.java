package com.sscf.investment.logic.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sscf.investment.component.ui.utils.ActivityUtils;
import com.sscf.investment.logic.R;
import com.sscf.investment.sdk.utils.DeviceUtil;

/**
 * Created by yorkeehuang on 2017/3/21.
 */
public final class ScreenShotShareDialog extends Dialog implements View.OnClickListener {

    private static final int ANIMATION_DURATION = 200;
    private static final int SHOW_DURATION = 5000;
    private int mTransationY;
    private boolean mCanCancelOnTouchOutside = false;
    private ViewGroup mContentView;
    private OnDialogButtonClickListener mButtonClickListener;
    private Activity mActivity;

    public ScreenShotShareDialog(@NonNull Activity activity) {
        super(activity, R.style.dialog_center_theme);
        mActivity = activity;
        setContentView(R.layout.logic_dialog_screen_shot_share);
        final View contentView = findViewById(R.id.content);
        contentView.findViewById(R.id.share_button).setOnClickListener(this);
        mTransationY = -DeviceUtil.dip2px(getContext(), 94);
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    final RectF frame = new RectF(
                            contentView.getX(),
                            contentView.getY(),
                            contentView.getX() + contentView.getWidth(),
                            contentView.getY() + contentView.getHeight());
                    if (mCanCancelOnTouchOutside && !frame.contains(event.getX(), event.getY())) {
                        dissmissWithAnimation();
                    }
                }
                return false;
            }
        });

        mContentView = (ViewGroup) contentView;
    }

    public ViewGroup getContentView() {
        return mContentView;
    }

    public void setCanCancelOnTouchOutside(final boolean can) {
        mCanCancelOnTouchOutside = can;
    }

    public void setButtonClickListener(OnDialogButtonClickListener listener) {
        mButtonClickListener = listener;
    }

    @Override
    public void show() {
        super.show();
        mContentView.post(()->{
            ObjectAnimator animator = ObjectAnimator.ofFloat(mContentView, "translationY", mTransationY, 0f);
            animator.setDuration(ANIMATION_DURATION);
            animator.start();
        });
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> dissmissWithAnimation(), SHOW_DURATION);
    }

    private void dissmissWithAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mContentView, "translationY", 0f, mTransationY);
        animator.setDuration(ANIMATION_DURATION);
        animator.addListener(new Animator.AnimatorListener() {

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
        animator.start();
    }

    @Override
    public void dismiss() {
        if (ActivityUtils.isDestroy(mActivity)) {
            return;
        }

        if (isShowing()) {
            super.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == v.getId()) {
            if(mButtonClickListener != null) {
                mButtonClickListener.onDialogButtonClick(this, v, 0);
            }
            dismiss();
        }
    }

    public interface OnDialogButtonClickListener {
        void onDialogButtonClick(final ScreenShotShareDialog dialog, final View view, final int position);
    }
}
