package com.sscf.investment.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IMarketWarningManager;
import com.sscf.investment.R;
import com.sscf.investment.utils.StringUtil;

import BEC.GetSecBsInfoRsp;
import BEC.RealMarketQRRsp;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by davidwei on 2017/03/21
 */
public class BeaconPullToRefreshWarningHeader extends LinearLayout implements PtrUIHandler,
        ValueAnimator.AnimatorUpdateListener {
    private static final String TAG = BeaconPullToRefreshWarningHeader.class.getSimpleName();
    private View mPointer;
    private TextView mTextView;

    private ValueAnimator mAnimator;

    private float mRotate;

    public BeaconPullToRefreshWarningHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mPointer = findViewById(R.id.pointer);
        mTextView = (TextView) findViewById(R.id.text);
    }

    public void startAnim(final float rotate) {
        stopAnim();

        if (rotate <= 0) {
            return;
        }

        final ValueAnimator animator = ValueAnimator.ofFloat(0, rotate);
        animator.setDuration(800L);
        animator.addUpdateListener(this);
        animator.start();
        mAnimator = animator;
    }

    public void stopAnim() {
        final ValueAnimator animator = mAnimator;
        if (animator != null) {
            animator.cancel();
            animator.removeUpdateListener(this);
            mAnimator = null;
        }
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mPointer.setRotation(0);
        final IMarketWarningManager marketWarningManager = (IMarketWarningManager) ComponentManager.getInstance()
                .getManager(IMarketWarningManager.class.getName());
        RealMarketQRRsp rsp = null;
        if (marketWarningManager != null) {
            rsp = marketWarningManager.getMainBoardWarningInfo();
        }

        float score = 0f;
        String text = "";
        if (rsp != null) {
            score = rsp.fPoint * 100;
            text = rsp.sTypeText + " " + StringUtil.getFormatedFloat(rsp.fPoint*100) + "%";
        }

        final float rotate = score / 100 * 180;
        mRotate = rotate;
        mTextView.setText(R.string.market_signal);
        final Resources res = getContext().getResources();
        final SpannableString spannableString;
        if (TextUtils.isEmpty(text)) {
            text = res.getString(R.string.value_null);
        }
        spannableString = new SpannableString(text);
        final int grade = (int)(score / 20);
        int textColor;
        switch (grade) {
            case 0:
                textColor = 0xff34ac0a;
                break;
            case 1:
                textColor = 0xfff2c02b;
                break;
            case 2:
                textColor = 0xffff9141;
                break;
            case 3:
                textColor = 0xffff5047;
                break;
            case 4:
            case 5:
                textColor = 0xfff10c00;
                break;
            default:
                textColor = res.getColor(R.color.default_text_color_100);
                break;
        }
        spannableString.setSpan(new ForegroundColorSpan(textColor), 0,
                spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTextView.append(spannableString);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        startAnim(mRotate);
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int currentPos = ptrIndicator.getCurrentPosY();
        if (currentPos != 0) {
            setTranslationY(-currentPos + getMeasuredHeight());
        } else {
            setTranslationY(0);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        final float value = (Float) animation.getAnimatedValue();
        mPointer.setRotation(value);
    }
}
