package com.sscf.investment.detail.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.interpolator.ExpoEaseIn;
import com.sscf.investment.interpolator.ExpoEaseOut;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;

import java.util.ArrayList;

import BEC.E_SEC_STATUS;
import BEC.SecQuote;

/**
 * Created by liqf on 2016/3/15.
 */
public class ExpandedDetailInfosView extends FrameLayout {
    private static final String TAG = ExpandedDetailInfosView.class.getSimpleName();

    private static final long ANIMATION_DURATION = 300;
    private final View mContentView;
    private final FrameLayout mContentFrame;
    private OnAnimationListener mAnimationListener;

    private AnimatorSet mAnimatorSet;

    private String mSuspendedStr;

    public void setDtSecCode(String dtSecCode) {
        mDtSecCode = dtSecCode;
    }

    private String mDtSecCode;

    public void setSecName(String secName) {
        mSecName = secName;
    }

    private String mSecName;

    public boolean isShowing() {
        return mIsShowing;
    }

    private boolean mIsShowing = false;
    private TextView mInfoVolume;
    private TextView mInfoAmplitude;
    private TextView mInfoBillRatio;
    private TextView mInfoCirculationMarketValue;
    private TextView mInfoOutside;
    private TextView mInfoInside;
    private TextView mInfoLimitRaising;
    private TextView mInfoLimitDown;

    private int mColorRed;
    private int mColorGreen;
    private int mColorBase;
    private int mPaddingTop;

    public ExpandedDetailInfosView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initResources();

        mContentFrame = new FrameLayout(context);
        mContentFrame.setBackgroundColor(getResources().getColor(R.color.black_40));

        addView(mContentFrame);

        LayoutInflater.from(context).inflate(R.layout.stock_detail_infos_expanded, mContentFrame);
        mContentView = findViewById(R.id.expanded_detail_infos_group);

        initDetailInfos();
    }

    private void initResources() {
        Resources resources = getResources();
        mColorRed = resources.getColor(R.color.stock_red_color);
        mColorGreen = resources.getColor(R.color.stock_green_color);
        mColorBase = ContextCompat.getColor(getContext(), R.color.default_text_color_60);

        mPaddingTop = resources.getDimensionPixelSize(R.dimen.expanded_detail_infos_paddingTop);

        mSuspendedStr = resources.getString(R.string.suspended);
    }

    private void initDetailInfos() {
        mInfoVolume = (TextView) findViewById(R.id.info_item_0);
        setTextById(R.id.info_title_0, R.string.stock_detail_volume);
        mInfoAmplitude = (TextView) findViewById(R.id.info_item_1);
        setTextById(R.id.info_title_1, R.string.stock_detail_amplitude);
        mInfoBillRatio = (TextView) findViewById(R.id.info_item_2);
        setTextById(R.id.info_title_2, R.string.stock_detail_bill_ratio);
        mInfoCirculationMarketValue = (TextView) findViewById(R.id.info_item_3);
        setTextById(R.id.info_title_3, R.string.stock_detail_circulation_market_value);
        mInfoOutside = (TextView) findViewById(R.id.info_item_4);
        setTextById(R.id.info_title_4, R.string.stock_detail_outside);
        mInfoInside = (TextView) findViewById(R.id.info_item_5);
        setTextById(R.id.info_title_5, R.string.stock_detail_inside);
        mInfoLimitRaising = (TextView) findViewById(R.id.info_item_6);
        setTextById(R.id.info_title_6, R.string.stock_detail_limit_raising);
        mInfoLimitDown = (TextView) findViewById(R.id.info_item_7);
        setTextById(R.id.info_title_7, R.string.stock_detail_limit_down);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                hide();
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    public void setAQuote(final SecQuote quote) {
        if (quote == null || !mIsShowing) {
            return;
        }

        float yesterdayClose = quote.getFClose();
        float now = quote.getFNow();

        mInfoVolume.setText(StringUtil.getVolumeString(quote.getLVolume(), false, true, false));

        mInfoAmplitude.setText(StringUtil.getAmplitudeString(quote));

        int secStatus = quote.getESecStatus();
        if (secStatus == E_SEC_STATUS.E_SS_SUSPENDED || now == 0) {
            mInfoBillRatio.setText("0.00%");
            mInfoBillRatio.setTextColor(mColorBase);
        } else {
            ArrayList<Long> vBuyv = quote.getVBuyv();
            ArrayList<Long> vSellv = quote.getVSellv();
            long totalBuy = 0, totalSell = 0;
            for (Long buy : vBuyv) {
                totalBuy += buy;
            }
            for (Long sell : vSellv) {
                totalSell += sell;
            }
            long total = totalBuy + totalSell;
            String billRatioStr = "--";
            if (total != 0) {
                float billRatio = (float) (totalBuy - totalSell) / total;
                billRatioStr = StringUtil.getPercentString(billRatio);
            }
            mInfoBillRatio.setText(billRatioStr);
        }

        float circulationmarketvalue = quote.getFCirculationmarketvalue();
        mInfoCirculationMarketValue.setText(StringUtil.getAmountString(circulationmarketvalue));

        long outside = quote.getLOutside();
        mInfoOutside.setText(StringUtil.getVolumeString(outside, false, true, false));

        long inside = quote.getLInside();
        mInfoInside.setText(StringUtil.getVolumeString(inside, false, true, false));

        if (!StockUtil.isNewThirdBoard(mDtSecCode)) {
            float limitHigh = quote.getFMaxLimit();
            mInfoLimitRaising.setText(StringUtil.getFormatedFloat(limitHigh));

            float limitLow = quote.getFMinLimit();
            mInfoLimitDown.setText(StringUtil.getFormatedFloat(limitLow));

            setTextColorByValues(mInfoLimitRaising, limitHigh, yesterdayClose);
            setTextColorByValues(mInfoLimitDown, limitLow, yesterdayClose);
        } else {
            mInfoLimitRaising.setText(R.string.value_null);
            mInfoLimitDown.setText(R.string.value_null);
        }

        if (secStatus == E_SEC_STATUS.E_SS_SUSPENDED || now == 0) {
            mInfoOutside.setTextColor(mColorBase);
            mInfoInside.setTextColor(mColorBase);
        } else {
            mInfoOutside.setTextColor(mColorRed);
            mInfoInside.setTextColor(mColorGreen);
        }
    }

    public void show(int scrollY) {
        if (mIsShowing) {
            return;
        }

        cancelAnimation();

        this.setPadding(0, mPaddingTop - scrollY, 0, 0);
        setVisibility(VISIBLE);

        mContentView.measure(0, 0);
        int contentHeight = mContentView.getMeasuredHeight();
//        DtLog.d(TAG, "show(): contentHeight is " + contentHeight);
        ValueAnimator animatorExpand = ObjectAnimator.ofFloat(mContentView, "translationY", -contentHeight, 0);
        animatorExpand.setDuration(ANIMATION_DURATION);
        animatorExpand.setEvaluator(new ExpoEaseIn(ANIMATION_DURATION));

        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(0, 255).setDuration(ANIMATION_DURATION);
        alphaAnimator.setEvaluator(new ExpoEaseIn(ANIMATION_DURATION));
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mContentFrame.getBackground().setAlpha((int) value);
            }
        });

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(animatorExpand).with(alphaAnimator);
        mAnimatorSet.start();

        mIsShowing = true;
    }

    public void hide() {
        if (!mIsShowing) {
            return;
        }

        cancelAnimation();

        ValueAnimator animatorCollapse = ObjectAnimator.ofFloat(mContentView, "translationY", 0, -mContentView.getMeasuredHeight());
        animatorCollapse.setDuration(ANIMATION_DURATION);
        animatorCollapse.setEvaluator(new ExpoEaseOut(ANIMATION_DURATION));
        animatorCollapse.start();

        animatorCollapse.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);
                if (mAnimationListener != null) {
                    mAnimationListener.onHideFinished();
                }
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
                mContentFrame.getBackground().setAlpha((int) value);
            }
        });
        alphaAnimator.start();

        mIsShowing = false;
    }

    public interface OnAnimationListener {
        void onHideFinished();
    }

    public void setAnimationListener(final OnAnimationListener listener) {
        mAnimationListener = listener;
    }

    private void cancelAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
            mAnimatorSet = null;
        }
    }

    private void setTextById(int textId, int strResId) {
        TextView textView = (TextView) findViewById(textId);
        textView.setText(strResId);
    }

    private void setTextColorByValues(TextView textView, float value, float baseValue) {
        if (value > baseValue) {
            textView.setTextColor(mColorRed);
        } else if (value < baseValue) {
            textView.setTextColor(mColorGreen);
        } else {
            textView.setTextColor(mColorBase);
        }
    }
}
