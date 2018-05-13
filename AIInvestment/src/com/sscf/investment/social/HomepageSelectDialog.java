package com.sscf.investment.social;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.interpolator.ExpoEaseIn;
import com.sscf.investment.interpolator.ExpoEaseOut;
import com.sscf.investment.sdk.utils.DeviceUtil;

import BEC.E_FEED_TYPE;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liqf on 2016/9/19.
 */
public class HomepageSelectDialog extends Dialog {
    private static final String TAG = "HomepageSelectDialog";
    private static final long ANIMATION_DURATION = 200;

    @BindView(R.id.dialogContentView)
    LinearLayout mDialogContentView;
    @BindView(R.id.dialogFrame)
    FrameLayout mDialogFrame;

    @BindView(R.id.follow)
    TextView mFollow;
    @BindView(R.id.share)
    TextView mShare;

    @OnClick(R.id.follow)
    public void doFollow() {
        if (mHomepageSelectCallback != null) {
            mHomepageSelectCallback.onFeedTypeSelected(E_FEED_TYPE.E_FT_ALL);
        }
        doDismiss();
    }

    @OnClick(R.id.share)
    public void doShare() {
        if (mHomepageSelectCallback != null) {
            mHomepageSelectCallback.onFeedTypeSelected(E_FEED_TYPE.E_FT_INVEST_MARKET_AND_STOCK);
        }
        doDismiss();
    }

    @OnClick(R.id.cancel)
    public void doCancel() {
        doDismiss();
    }

    private void doDismiss() {
        dismissWithAnimation(mDialogFrame, mDialogContentView);
    }

    public HomepageSelectDialog(Context context) {
        super(context, R.style.dialog_share_theme);
        DeviceUtil.enableDialogTranslucentStatus(getWindow(), getContext().getResources().getColor(R.color.black_40));

        setContentView(R.layout.dialog_homepage_select);
        ButterKnife.bind(this);

        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RectF frame = new RectF(
                    mDialogContentView.getX(),
                    mDialogContentView.getY(),
                    mDialogContentView.getX() + mDialogContentView.getWidth(),
                    mDialogContentView.getY() + mDialogContentView.getHeight());
                if (!frame.contains(event.getX(), event.getY())) {
                    doDismiss();
                }
                return false;
            }
        });

        setCanceledOnTouchOutside(true);

        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ValueAnimator animatorExpand = ObjectAnimator.ofFloat(mDialogContentView, "translationY", mDialogContentView.getMeasuredHeight(), 0);
                animatorExpand.setDuration(ANIMATION_DURATION);
                animatorExpand.setEvaluator(new ExpoEaseIn(ANIMATION_DURATION));
                animatorExpand.start();

                ValueAnimator alphaAnimator = ValueAnimator.ofFloat(0, 255).setDuration(ANIMATION_DURATION);
                alphaAnimator.setEvaluator(new ExpoEaseIn(ANIMATION_DURATION));
                alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        mDialogFrame.getBackground().setAlpha((int) value);
                    }
                });
                alphaAnimator.start();
            }
        });
    }

    private void dismissWithAnimation(final View contentFrame, View contentView) {
        ValueAnimator animatorCollapse = ObjectAnimator.ofFloat(contentView, "translationY", 0, contentView.getMeasuredHeight());
        animatorCollapse.setDuration(ANIMATION_DURATION);
        animatorCollapse.setEvaluator(new ExpoEaseOut(ANIMATION_DURATION));
        animatorCollapse.start();

        animatorCollapse.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(255, 0).setDuration(ANIMATION_DURATION);
        alphaAnimator.setEvaluator(new ExpoEaseOut(ANIMATION_DURATION));
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                contentFrame.getBackground().setAlpha((int) value);
            }
        });
        alphaAnimator.start();
    }

    public interface HomepageSelectCallback {
        void onFeedTypeSelected(int feedType);
    }

    public void setHomepageSelectCallback(HomepageSelectCallback homepageSelectCallback) {
        mHomepageSelectCallback = homepageSelectCallback;
    }

    private HomepageSelectCallback mHomepageSelectCallback;
}
