package com.sscf.investment.detail.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.interpolator.ExpoEaseOut;
import com.sscf.investment.interpolator.QuadEaseIn;

import java.util.ArrayList;

import BEC.E_SEC_LIVE_MSG_TYPE;
import BEC.SecLiveMsg;

/**
 * Created by liqf on 2016/2/26.
 */
public class CapitalFlowReminderView extends View {
    private static final long ANIMATION_DURATION = 500;
    private static final long MSG_DISPLAY_DURATION = 5000;
    private static final String TAG = CapitalFlowReminderView.class.getSimpleName();
    private Paint mPaintText;
    private Paint mPaintBg;
    private Paint mPaintIcon;
    private int mTextSize;
    private int mTextHeight;
    private int mTextWidth;
    private int mTextBottom;
    private int mTextPadding;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private TextDrawer mTextDrawer;
    private int mColorRed;
    private int mColorGreen;
    private int mColorDefault;
    private int mColorBase;
    private int mTextColor;
    private Bitmap mBitmapReminder;
    private Bitmap mBitmapReminderNoContent;
    private RectF mRectBitmap = new RectF();
    private String mText;
    private int mMsgType = E_SEC_LIVE_MSG_TYPE.E_SLMT_NORMAL;
    private RectF mRectBg = new RectF();
    private AnimatorSet mAnimatorSet;

    public float getBgStartX() {
        return bgStartX;
    }

    public void setBgStartX(float bgStartX) {
        this.bgStartX = bgStartX;

        invalidate();
    }

    private float bgStartX;

    public CapitalFlowReminderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initResources();
    }

    private void initResources() {
        Resources resources = getContext().getResources();

        mTextSize = resources.getDimensionPixelSize(R.dimen.capital_flow_reminder_textSize);
        mTextDrawer = new TextDrawer();
        mTextHeight = mTextDrawer.measureSingleTextHeight(mTextSize, null);
        mTextBottom = resources.getDimensionPixelSize(R.dimen.capital_flow_reminder_textBottom);
        mTextPadding = resources.getDimensionPixelSize(R.dimen.capital_flow_reminder_textPadding);
        mBitmapReminder = ((BitmapDrawable)resources.getDrawable(R.drawable.capital_flow_reminder)).getBitmap();
        mBitmapReminderNoContent = ((BitmapDrawable)resources.getDrawable(R.drawable.capital_flow_reminder_no_content)).getBitmap();

        mColorRed = resources.getColor(R.color.stock_red_color);
        mColorGreen = resources.getColor(R.color.stock_green_color);
        mColorDefault = resources.getColor(R.color.actionbar_bg_color);

        mColorBase = ContextCompat.getColor(getContext(), R.color.default_background);

        mTextColor = mColorDefault;
        
        mPaintBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBg.setColor(mColorBase);
        mPaintIcon = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setTextSize(mTextSize);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();

//        DtLog.d(TAG, "onDraw: bgStartX = " + bgStartX);

        int iconSize = mDrawingHeight;
        float x = bgStartX;
        float y = mDrawingHeight - (mDrawingHeight - mTextHeight) / 2 - mTextBottom;

        //画圆角矩形的外边框背景
        mRectBg.set(x, 0, mDrawingWidth, mDrawingHeight);
        if (mShowType != SHOW_TYPE_NO_CONTENT) {
            mPaintBg.setAlpha(255);
        } else {
            mPaintBg.setAlpha((int) (0.8f * 255));
        }
        canvas.drawRoundRect(mRectBg, mDrawingHeight / 2, mDrawingHeight / 2, mPaintBg);

        //画显示的文字
        if (!TextUtils.isEmpty(mText)) {
            canvas.save();
            canvas.clipRect(x + mTextPadding, 0, mDrawingWidth - iconSize - mTextPadding, mDrawingHeight);
            mPaintText.setColor(mTextColor);
            canvas.drawText(mText, x + mTextPadding, y, mPaintText);
            canvas.restore();
        }

        //画右侧的小喇叭
        mRectBitmap.left = mDrawingWidth - iconSize;
        mRectBitmap.top = 0;
        mRectBitmap.right = mDrawingWidth;
        mRectBitmap.bottom = mDrawingHeight;
        if (mShowType != SHOW_TYPE_NO_CONTENT) {
            mPaintIcon.setAlpha((255));
            canvas.drawBitmap(mBitmapReminder, null, mRectBitmap, mPaintIcon);
        } else {
            mPaintIcon.setAlpha((int) (0.8f * 255));
            canvas.drawBitmap(mBitmapReminderNoContent, null, mRectBitmap, mPaintIcon);
        }
    }

    private CapitalFlowReminderLayout mParentLayout;
    private int mShowType = SHOW_TYPE_NO_CONTENT;
    public static final int SHOW_TYPE_NO_CONTENT = 0;
    public static final int SHOW_TYPE_HAS_CONTENT = 1;
    public static final int SHOW_TYPE_DISPLAY_CONTENT = 2;

    public void setData(ArrayList<SecLiveMsg> secLiveMsgs, int showType) {
        //输入真实的数据结构
        mShowType = showType;

        if (secLiveMsgs != null && secLiveMsgs.size() > 0) {
            SecLiveMsg liveMsg = secLiveMsgs.get(0);
            mText = liveMsg.getSMsg();
            mMsgType = liveMsg.getESecLiveMsgType();
            mTextColor = getMsgColorByType(mMsgType);
        }

        //for test
//        mText = "10:50 大单买入205万，成交均价13.36元";

        if (TextUtils.isEmpty(mText)) {
            invalidate();
            return;
        }

        if (mParentLayout == null) {
            mParentLayout = (CapitalFlowReminderLayout) getParent();
        }

        if (mShowType == SHOW_TYPE_DISPLAY_CONTENT) {
            playAnimation();
        } else {
            invalidate();
        }
    }

    private void playAnimation() {
        stopAnimation();

        final int iconSize = mDrawingHeight;
        mTextWidth = mTextDrawer.measureSingleTextWidth(mText, mTextSize, null);
        int bgWidth = getBgWidth();
        mParentLayout.setWidth(bgWidth);
        int right = mDrawingWidth - iconSize;
        int left = mDrawingWidth - bgWidth;

        ValueAnimator animatorExpand = ObjectAnimator.ofFloat(this, "bgStartX", right, left);
        animatorExpand.setDuration(ANIMATION_DURATION);
        animatorExpand.setEvaluator(new QuadEaseIn(ANIMATION_DURATION));

        ValueAnimator animatorCollapse = ObjectAnimator.ofFloat(this, "bgStartX", left, right);
        animatorCollapse.setStartDelay(ANIMATION_DURATION + MSG_DISPLAY_DURATION);
        animatorCollapse.setDuration(ANIMATION_DURATION / 5);
        animatorCollapse.setEvaluator(new ExpoEaseOut(ANIMATION_DURATION / 5));
        animatorCollapse.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mParentLayout.setWidth(CapitalFlowReminderView.this.mDrawingHeight);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(animatorExpand).with(animatorCollapse);
        mAnimatorSet.start();
    }

    public void stopAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
            mAnimatorSet = null;
        }
    }

    private int getMsgColorByType(int msgType) {
        switch (msgType) {
            case E_SEC_LIVE_MSG_TYPE.E_SLMT_BAD:
                return mColorGreen;
            case E_SEC_LIVE_MSG_TYPE.E_SLMT_GOOD:
                return mColorRed;
            default:
                return mColorDefault;
        }
    }

    private int getBgWidth() {
        final int iconSize = mDrawingHeight;
        int width = 0;

        if (mTextWidth != 0) {
            width = iconSize + mTextWidth + mTextPadding * 2;
        } else {
            width = iconSize;
        }

        int parentWidth = mParentLayout.getFullWidth();
        if (width > parentWidth) {
            width = parentWidth;
        }

        return width;
    }
}
