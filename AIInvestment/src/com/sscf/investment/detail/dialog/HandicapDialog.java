package com.sscf.investment.detail.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sscf.investment.R;
import com.sscf.investment.detail.presenter.HandicapPresenter;
import com.sscf.investment.detail.presenter.SecurityDetailPresenter;
import com.sscf.investment.detail.view.HandicapIntelligentDiagnosisView;
import com.sscf.investment.detail.view.HandicapMarketView;
import com.sscf.investment.detail.view.HandicapPlateView;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.WebBeaconJump;
import java.util.ArrayList;
import BEC.ConsultScore;
import BEC.SecQuote;
import BEC.SecSimpleQuote;

/**
 * Created by davidwei on 2017/04/26
 */
public final class HandicapDialog extends Dialog implements View.OnClickListener,
        DialogInterface.OnDismissListener, IQuoteDialog {

    private HandicapPresenter mHandicapPresenter;
    private final SecurityDetailPresenter mPresenter;
    private final FrameLayout mDataLayout;
    private HandicapMarketView mMarketView;
    private HandicapPlateView mPlateView;
    private HandicapIntelligentDiagnosisView mIntelligentDiagnosisView;
    private View mCurrentView;
    private View mMarketButton;
    private View mPlateButton;
    private View mIntelligentDiagnosisButton;
    private final ImageView mTriangleView;
    private final ConstraintLayout.LayoutParams mTriangleParams;

    public HandicapDialog(@NonNull Context context, SecurityDetailPresenter presenter) {
        super(context, R.style.dialog_center_theme);
        setContentView(R.layout.dialog_handicap);
        mPresenter = presenter;
        final ConstraintLayout content = (ConstraintLayout) findViewById(R.id.content);
        getWindow().getDecorView().setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                final RectF frame = new RectF(
                        content.getX(),
                        content.getY(),
                        content.getX() + content.getWidth(),
                        content.getY() + content.getHeight());
                if (!frame.contains(event.getX(), event.getY())) {
                    cancel();
                }
            }
            return false;
        });
        findViewById(R.id.close_button).setOnClickListener(this);
        mDataLayout = (FrameLayout) findViewById(R.id.dataLayout);
        mMarketButton = findViewById(R.id.marketButton);
        mMarketButton.setOnClickListener(this);
        mPlateButton = findViewById(R.id.plateButton);
        mPlateButton.setOnClickListener(this);
        mIntelligentDiagnosisButton = findViewById(R.id.intelligentDiagnosisButton);
        mIntelligentDiagnosisButton.setOnClickListener(this);
        mTriangleView = new ImageView(context);
        mTriangleView.setImageResource(R.drawable.handicap_triangle);
        mTriangleParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        content.addView(mTriangleView, mTriangleParams);

        StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_SHOW_HANCICAP);
    }

    public void setHandicapPresenter(final HandicapPresenter presenter) {
        mHandicapPresenter = presenter;
    }

    private void setButtonSelected(final View view, final boolean selected) {
        view.setBackgroundResource(selected ? R.color.default_background : R.color.main_tab_bg);
    }

    private void setTrianglePosition(final View relativeView) {
        mTriangleParams.rightToLeft = relativeView.getId();
        mTriangleParams.topToTop = relativeView.getId();
        mTriangleParams.bottomToBottom = relativeView.getId();
        mTriangleView.setLayoutParams(mTriangleParams);
    }

    public void showMarketData() {
        if (mCurrentView != null && mCurrentView == mMarketView) {
            return;
        }

        setButtonSelected(mMarketButton, true);
        setButtonSelected(mPlateButton, false);
        setButtonSelected(mIntelligentDiagnosisButton, false);
        setTrianglePosition(mMarketButton);

        if (mMarketView == null) {
            mMarketView = (HandicapMarketView) View.inflate(getContext(), R.layout.dialog_handicap_market, null);
            mMarketView.findViewById(R.id.ahLayout).setOnClickListener(this);
        }
        mDataLayout.removeView(mCurrentView);
        mDataLayout.addView(mMarketView);
        mCurrentView = mMarketView;
    }

    @Override
    public void updateQuote(final SecQuote quote) {
        mMarketView.updateQupte(quote);
    }

    public void updateAHpremuim(final SecSimpleQuote quote) {
        mMarketView.updateAHpremuim(quote);
    }

    public void showBelongedPlate() {
        if (mCurrentView != null && mCurrentView == mPlateView) {
            return;
        }

        setButtonSelected(mMarketButton, false);
        setButtonSelected(mPlateButton, true);
        setButtonSelected(mIntelligentDiagnosisButton, false);
        setTrianglePosition(mPlateButton);

        if (mPlateView == null) {
            mPlateView = (HandicapPlateView) View.inflate(getContext(), R.layout.dialog_handicap_plate, null);
            mPlateView.setDialog(this);
            mPlateView.findViewById(R.id.showPlateButton).setOnClickListener(this);
        }
        mDataLayout.removeView(mCurrentView);
        mDataLayout.addView(mPlateView);
        mCurrentView = mPlateView;

        StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_HANDICAP_PLATE);
    }

    public void updateIndustryPlateData(final SecSimpleQuote quote) {
        mPlateView.updateIndustryPlateData(quote);
    }

    public void updateConceptPlateData(final ArrayList<SecSimpleQuote> quotes) {
        mPlateView.updateConceptPlateData(quotes);
    }

    public void showIntelligentDiagnosis() {
        if (mCurrentView != null && mCurrentView == mIntelligentDiagnosisView) {
            return;
        }

        setButtonSelected(mMarketButton, false);
        setButtonSelected(mPlateButton, false);
        setButtonSelected(mIntelligentDiagnosisButton, true);
        setTrianglePosition(mIntelligentDiagnosisButton);

        if (mIntelligentDiagnosisView == null) {
            mIntelligentDiagnosisView = (HandicapIntelligentDiagnosisView) View.inflate(getContext(),
                    R.layout.dialog_handicap_intelligent_diagnosis, null);
            mIntelligentDiagnosisView.findViewById(R.id.showIntelligentDiagnosisButton).setOnClickListener(this);
        }
        mDataLayout.removeView(mCurrentView);
        mDataLayout.addView(mIntelligentDiagnosisView);
        mCurrentView = mIntelligentDiagnosisView;

        StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_INTELLIGENT_DIAGNOSIS);
    }

    public void updateIntelligentDiagnosis(final ConsultScore score) {
        mIntelligentDiagnosisView.setData(score);
    }

    @Override
    public void onClick(View v) {
        final Context context = getContext();
        switch (v.getId()) {
            case R.id.marketButton:
                mHandicapPresenter.swichToMarket();
                break;
            case R.id.ahLayout:
                mHandicapPresenter.clickShowAHPremium(context);
                StatisticsUtil.reportAction(StatisticsConst.AH_PREMIUM_CLICKED);
                dismiss();
                break;
            case R.id.plateButton:
                mHandicapPresenter.swichToPlate();
                break;
            case R.id.intelligentDiagnosisButton:
                mHandicapPresenter.swichToIntelligentDiagnosis();
                break;
            case R.id.showPlateButton:
                WebBeaconJump.showIndustryPlateList(context);
                dismiss();
                break;
            case R.id.showIntelligentDiagnosisButton:
                mHandicapPresenter.clickShowIntelligentDiagnosisButton(context);
                dismiss();
                break;
            case R.id.close_button:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandicapPresenter.release();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mHandicapPresenter.release();
        mPresenter.releaseDialog();
    }
}
