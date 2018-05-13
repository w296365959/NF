package com.sscf.investment.detail.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;

/**
 * Created by davidwei on 2016/7/14.
 */
public final class ShakeGuideDialog extends Dialog implements Runnable, View.OnClickListener, DialogInterface.OnShowListener, DialogInterface.OnDismissListener {
    private final Handler mHandler;
    private AnimationDrawable mDrawable;
    private BaseFragmentActivity mActivity;

    public ShakeGuideDialog(final BaseFragmentActivity activity) {
        super(activity, R.style.dialog_center_theme);
        mActivity = activity;
        setContentView(R.layout.dialog_shake_guide);

        findViewById(R.id.content).setOnClickListener(this);
        setOnShowListener(this);
        setOnDismissListener(this);

        mHandler = new Handler();
    }

    @Override
    public void onShow(DialogInterface dialog) {
        final ImageView imageView = (ImageView) findViewById(R.id.shakeGuideImageView);
        final AnimationDrawable drawable = (AnimationDrawable) getContext().getResources().getDrawable(R.drawable.shake_guide_anim);
        mDrawable = drawable;
        imageView.setImageDrawable(drawable);
        drawable.start();

        // 5秒后自动消失
        mHandler.postDelayed(this, 5000L);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mDrawable != null) {
            mDrawable.stop();
        }
        mHandler.removeCallbacks(this);
    }

    @Override
    public void onClick(View v) {
        mHandler.removeCallbacks(this);
        dismiss();
    }

    @Override
    public void run() {
        if (mActivity.isDestroy()) {
            return;
        }
        dismiss();
    }
}
