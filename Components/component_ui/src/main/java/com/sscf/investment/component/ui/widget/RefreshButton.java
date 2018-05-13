package com.sscf.investment.component.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import com.sscf.investment.component.ui.R;

public final class RefreshButton extends ImageView implements Handler.Callback {
    private static final int MSG_START_ANIM = 1;
    private static final int MSG_STOP_ANIM = 2;

    private Animation mLoadingAnimation;
    private final Handler mHandler;

    public RefreshButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setImageResource(R.drawable.refresh);
        mHandler = new Handler(this);
    }

    public void startLoadingAnim() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            startLoadingAnimOnUI();
        } else {
            mHandler.sendEmptyMessage(MSG_START_ANIM);
        }
    }

    public void stopLoadingAnim() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            stopLoadingAnimOnUI();
        } else {
            mHandler.sendEmptyMessage(MSG_STOP_ANIM);
        }
    }

    private void startLoadingAnimOnUI() {
        stopLoadingAnimOnUI();

        mLoadingAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_anim_actionbar);
        mLoadingAnimation.setInterpolator(new LinearInterpolator());
        setImageResource(R.drawable.loading_actionbar);
        startAnimation(mLoadingAnimation);

        mHandler.sendEmptyMessageDelayed(MSG_STOP_ANIM, 10000L); // 超时，自动停止动画
    }

    private void stopLoadingAnimOnUI() {
        mHandler.removeMessages(MSG_STOP_ANIM);
        if (mLoadingAnimation != null) {
            clearAnimation();
            setImageResource(R.drawable.refresh);
            mLoadingAnimation = null;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_START_ANIM:
                startLoadingAnimOnUI();
                break;
            case MSG_STOP_ANIM:
                stopLoadingAnimOnUI();
                break;
        }
        return true;
    }
}
