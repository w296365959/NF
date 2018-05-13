package com.sscf.investment.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.DtLog;

import java.util.Random;

import BEC.DtliveErrCode;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by liqf on 2016/8/12.
 */
public class BeaconPullToRefreshHeader extends LinearLayout implements PtrUIHandler, ValueAnimator.AnimatorUpdateListener {
    private static final String TAG = BeaconPullToRefreshHeader.class.getSimpleName();
    private final ImageView mImageView;

    private final TypedArray mRefreshArray1;

    private static final int MAX_PROGRESS = 10000;

    private final ValueAnimator mAnimator;

    private int mOriginalHeight;

    private TextView mTvFamousQuotation;

    final int REFRESH_1_LENGTH = 28;

    private final int[] refresh1DrawalbeIds = new int[REFRESH_1_LENGTH];

    private static final String[] FAMOUS_QUOTATION = new String[]{
            "买入不急 卖出不贪",
            "地量地价 天量天价",
            "低位放量 上涨买入",
            "高位放天量不涨卖出",
            "反弹不是底 是底不反弹",
            "三分技术 七分心态",
            "庄家选股 散户选庄",
            "人弃我取 人取我弃",
            "长线是金 短线是银",
            "道听途说者 十之八九输",
            "只有战胜自己才可能战胜庄家",
            "横有多长 竖有多高",
            "选股不如选时 善买不如善卖",
            "宁可错过 决不做错",
            "赚钱的空间是跌出来的",
            "会买的是徒弟 会卖的是师傅",
            "股市久盘必跌",
            "股市是各行各业精英的坟墓",
            "老手多等待 新手多无耐",
            "永远不要孤注一掷",
            "波段操作是钻石",
            "强者恒强 弱者恒弱 ",
            "先知先觉者大口吃肉",
            "没有板块集体行动不买",
            "头脑不冷静 不买",
            "线乱不看 形散不买",
            "追涨不追高 追涨就是追主力",
            "价格总是向阻力最小的方向发展",
            "计划你的交易 交易你的计划"
    };

    public BeaconPullToRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER_HORIZONTAL);
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();

        refresh1DrawalbeIds[0] = R.drawable.refresh_frame_1_00;
        refresh1DrawalbeIds[1] = R.drawable.refresh_frame_1_01;
        refresh1DrawalbeIds[2] = R.drawable.refresh_frame_1_02;
        refresh1DrawalbeIds[3] = R.drawable.refresh_frame_1_03;
        refresh1DrawalbeIds[4] = R.drawable.refresh_frame_1_04;
        refresh1DrawalbeIds[5] = R.drawable.refresh_frame_1_05;
        refresh1DrawalbeIds[6] = R.drawable.refresh_frame_1_06;
        refresh1DrawalbeIds[7] = R.drawable.refresh_frame_1_07;
        refresh1DrawalbeIds[8] = R.drawable.refresh_frame_1_08;
        refresh1DrawalbeIds[9] = R.drawable.refresh_frame_1_09;
        refresh1DrawalbeIds[10] = R.drawable.refresh_frame_1_10;
        refresh1DrawalbeIds[11] = R.drawable.refresh_frame_1_11;
        refresh1DrawalbeIds[12] = R.drawable.refresh_frame_1_12;
        refresh1DrawalbeIds[13] = R.drawable.refresh_frame_1_13;
        refresh1DrawalbeIds[14] = R.drawable.refresh_frame_1_14;
        refresh1DrawalbeIds[15] = R.drawable.refresh_frame_1_15;
        refresh1DrawalbeIds[16] = R.drawable.refresh_frame_1_16;
        refresh1DrawalbeIds[17] = R.drawable.refresh_frame_1_17;
        refresh1DrawalbeIds[18] = R.drawable.refresh_frame_1_18;
        refresh1DrawalbeIds[19] = R.drawable.refresh_frame_1_19;
        refresh1DrawalbeIds[20] = R.drawable.refresh_frame_1_20;
        refresh1DrawalbeIds[21] = R.drawable.refresh_frame_1_21;
        refresh1DrawalbeIds[22] = R.drawable.refresh_frame_1_22;
        refresh1DrawalbeIds[23] = R.drawable.refresh_frame_1_23;
        refresh1DrawalbeIds[24] = R.drawable.refresh_frame_1_24;
        refresh1DrawalbeIds[25] = R.drawable.refresh_frame_1_25;
        refresh1DrawalbeIds[26] = R.drawable.refresh_frame_1_26;
        refresh1DrawalbeIds[27] = R.drawable.refresh_frame_1_27;

        mRefreshArray1 = context.obtainStyledAttributes(refresh1DrawalbeIds);

        final LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        mImageView = new ImageView(context);
        addView(mImageView, params);

        mTvFamousQuotation = new TextView(context);
        mTvFamousQuotation.setTextColor(Color.parseColor("#cecfd3"));
        mTvFamousQuotation.setGravity(Gravity.CENTER);
        mTvFamousQuotation.setText(FAMOUS_QUOTATION[new Random().nextInt(FAMOUS_QUOTATION.length - 1)]);
        LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvParams.gravity = Gravity.CENTER;
        tvParams.setMargins(20, 0, 0 ,0);
        addView(mTvFamousQuotation, tvParams);

        mImageView.setImageResource(mRefreshArray1.getResourceId(0, getDefaultDrawableResId()));
        final ValueAnimator animator = ValueAnimator.ofInt(0, MAX_PROGRESS);
        animator.setDuration(1000L);
        animator.setRepeatCount(30);
        animator.addUpdateListener(this);
        mAnimator = animator;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        final Integer progress = (Integer) animation.getAnimatedValue();
        final int length = mRefreshArray1.length();
        final int frame = progress * (length - 1) / MAX_PROGRESS;
        mImageView.setImageResource(refresh1DrawalbeIds[frame]);
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

    public void setHeight(final int height) {
        final ViewGroup.LayoutParams params = getLayoutParams();
        if (mOriginalHeight <= 0) {
            mOriginalHeight = params.height;
        }
        params.height = height;
        setLayoutParams(params);
    }

    public void setProgress(int progress) {
        DtLog.e(TAG, "setProgress progress = " + progress + " imageResource = " + mRefreshArray1.getResourceId(progress, getDefaultDrawableResId()));
        mImageView.setImageResource(refresh1DrawalbeIds[progress]);

    }

    protected int getDefaultDrawableResId() {
        return R.drawable.refresh_frame_1_27;
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        DtLog.d(TAG, "onUIReset");
        stopAnim();
        Random rand = new Random();
        mTvFamousQuotation.setText(FAMOUS_QUOTATION[rand.nextInt(FAMOUS_QUOTATION.length - 1)]);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        DtLog.d(TAG, "onUIRefreshPrepare");
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        startAnim();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        DtLog.d(TAG, "onUIRefreshComplete");
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int length = mRefreshArray1.length();
        int progress = length - (int) (currentPos / (float) mOffsetToRefresh * length);
        if (progress > length - 1) {
            progress = length - 1;
        }else if (progress < 0){
            progress = 0;
        }
        DtLog.e(TAG, "onUIPositionChange : currentPos = " + currentPos + " length = " + length + " progress = " + progress);
        setProgress(progress);
        setScaleX(1 - progress * 1.0f / (length - 1));
        setScaleY(1 - progress * 1.0f / (length - 1));
        if (currentPos != 0) {
            setTranslationY(-currentPos + getMeasuredHeight());
        } else {
            setTranslationY(0);
        }
        mTvFamousQuotation.setAlpha(1 - progress * 1.0f / (length - 1));

    }

}
