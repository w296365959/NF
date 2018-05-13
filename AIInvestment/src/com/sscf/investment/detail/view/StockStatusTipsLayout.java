package com.sscf.investment.detail.view;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.router.WebBeaconJump;

import java.util.ArrayList;
import BEC.E_SEC_CHANGE_STATUS;
import BEC.SecStatusInfo;

/**
 * Created by liqf on 2016/6/6.
 */
public class StockStatusTipsLayout extends LinearLayout implements View.OnClickListener {
    private static final long ANIMATION_DURATION = 400;
    private static final long DISPLAY_DURATION = 2000;
    private TextView mTipsCurrent;
    private TextView mTipsNext;
    private ArrayList<SecStatusInfo> mStatusInfos;
    private int mCurrentIndex = 0;
    private ViewPropertyAnimator mAnimatorCurrent;
    private ViewPropertyAnimator mAnimatorNext;
    private String mDtSecCode;
    private String mSecName;

    public StockStatusTipsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTipsCurrent = (TextView) findViewById(R.id.currentTips);
        mTipsNext = (TextView) findViewById(R.id.nextTips);
        setOnClickListener(this);
    }

    public void setData(ArrayList<SecStatusInfo> statusInfos, String dtSecCode, String secName) {
        mStatusInfos = statusInfos;
        mDtSecCode = dtSecCode;
        mSecName = secName;
        mCurrentIndex = 0;

        setVisibility(VISIBLE);

        if (statusInfos.size() > 1) {
            doPlaySwitchAnimation();
        } else {
            SecStatusInfo statusInfo = mStatusInfos.get(mCurrentIndex);
            mTipsCurrent.setText(statusInfo.sDesc);
        }

        doStatistics(statusInfos);
    }

    private void doStatistics(ArrayList<SecStatusInfo> statusInfos) {
        for (SecStatusInfo statusInfo : statusInfos) {
            int status = statusInfo.getIStatus();
            switch (status) {
                case E_SEC_CHANGE_STATUS.E_STATUS_FIX: //定增
                    StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_DIRECT_ADD_DISPLAYED);
                    break;
                case E_SEC_CHANGE_STATUS.E_STATUS_LIFTED: // 定增破发
                    StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_DIRECT_ADD_DISPLAYED);
                    break;
                case E_SEC_CHANGE_STATUS.E_STATUS_SUSPEND: //停复牌
                    StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_SUSPENSION_DISPLAYED);
                    break;
                case E_SEC_CHANGE_STATUS.E_STATUS_PRIVATIZE: //美股私有化
                    StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_PRIVATIZATION_DISPLAYED);
                    break;
                case E_SEC_CHANGE_STATUS.E_STATUS_HOT:
                    break;
                default:
                    break;
            }
        }
    }

    private Runnable mSwitchAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            playSwitchAnimation();
        }
    };

    private void doPlaySwitchAnimation() {
        stopSwitchAnimation();
        post(mSwitchAnimationRunnable);
    }

    public void stopSwitchAnimation() {
        if (mAnimatorCurrent != null) {
            mAnimatorCurrent.cancel();
        }
        if (mAnimatorNext != null) {
            mAnimatorNext.cancel();
        }

        removeCallbacks(mSwitchAnimationRunnable);
    }

    private void playSwitchAnimation() {
        SecStatusInfo statusInfo = mStatusInfos.get(mCurrentIndex);
        mTipsCurrent.setText(statusInfo.getSDesc());
        mTipsCurrent.setTranslationY(0);

        final int nextIndex = (mCurrentIndex + 1) % mStatusInfos.size();
        statusInfo = mStatusInfos.get(nextIndex);
        mTipsNext.setText(statusInfo.getSDesc());
        mTipsNext.setTranslationY(mTipsNext.getMeasuredHeight());

        //for test
//        mTipsCurrent.setText("tips currenttips currenttips currenttips currenttips currenttips currenttips currenttips current");
//        mTipsCurrent.setTranslationY(0);
//        mTipsNext.setText("tips next");
//        mTipsNext.setTranslationY(mTextHeight);

        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

        mAnimatorCurrent = mTipsCurrent.animate();
        mAnimatorCurrent.translationY(-mTipsCurrent.getMeasuredHeight()).setInterpolator(interpolator).setDuration(ANIMATION_DURATION).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                DtLog.d("liqf", "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentIndex = nextIndex;
                StockStatusTipsLayout.this.postDelayed(mSwitchAnimationRunnable, DISPLAY_DURATION);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mAnimatorNext = mTipsNext.animate();
        mAnimatorNext.translationY(0).setInterpolator(interpolator).setDuration(ANIMATION_DURATION);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        SecStatusInfo statusInfo = mStatusInfos.get(mCurrentIndex);
        int status = statusInfo.getIStatus();
        switch (status) {
            case E_SEC_CHANGE_STATUS.E_STATUS_FIX: //定增
                StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_DIRECT_ADD_CLICKED);
                WebBeaconJump.showDirectionAddDetail(getContext(), mDtSecCode, mSecName);
                break;
            case E_SEC_CHANGE_STATUS.E_STATUS_LIFTED: // 定增破发
                WebBeaconJump.showDirectAddBreak(getContext(), statusInfo.sKey, mDtSecCode, mSecName);
                break;
            case E_SEC_CHANGE_STATUS.E_STATUS_SUSPEND: //停复牌
                StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_SUSPENSION_CLICKED);
                WebBeaconJump.showSuspensionDetail(getContext(), mDtSecCode, mSecName);
                break;
            case E_SEC_CHANGE_STATUS.E_STATUS_PRIVATIZE: //美股私有化
                StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_PRIVATIZATION_CLICKED);
                WebBeaconJump.showPrivatizationTrackingDetail(getContext(), mDtSecCode, mSecName);
                break;
            case E_SEC_CHANGE_STATUS.E_STATUS_HOT:
                WebBeaconJump.showHotStockRank(getContext());
                break;
            default:
                break;
        }
    }
}
