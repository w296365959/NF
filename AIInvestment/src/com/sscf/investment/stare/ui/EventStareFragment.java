package com.sscf.investment.stare.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dengtacj.component.entity.db.StockDbEntity;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.portfolio.widget.RemindEditItemLayout;
import com.sscf.investment.stare.ui.widget.SubmitableFragment;

/**
 * Created by yorkeehuang on 2017/9/11.
 */

public class EventStareFragment extends SubmitableFragment implements View.OnClickListener {

    private CheckBox mNewAnnouncementCheckbox;
    private CheckBox mResearchReportCheckbox;
    private RemindEditItemLayout mHoldingPriceAboveView;
    private RemindEditItemLayout mHoldingPriceBelowView;
//    private RemindEditItemLayout mMainPriceAboveView;
//    private RemindEditItemLayout mMainPriceBelowView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_event_stare, container, false);
        initView(view);
        return view;
    }

    private void initView(View rootView){
        mNewAnnouncementCheckbox = initCheckboxItem(rootView, R.id.new_announcement,
                R.string.smart_stock_stare_new_announcement_title);
        mResearchReportCheckbox = initCheckboxItem(rootView, R.id.research_report,
                R.string.smart_stock_stare_research_report_title);

        mHoldingPriceAboveView = (RemindEditItemLayout) rootView.findViewById(R.id.holding_price_above);
        mHoldingPriceAboveView.setValue(R.string.holding_price_above,
                R.string.portfolio_remind_stock_price_hint, 0);

        mHoldingPriceBelowView = (RemindEditItemLayout) rootView.findViewById(R.id.holding_price_below);
        mHoldingPriceBelowView.setValue(R.string.holding_price_below,
                R.string.portfolio_remind_stock_price_hint, 0);

        rootView.findViewById(R.id.help_icon).setOnClickListener(this);

//        mMainPriceAboveView = (RemindEditItemLayout) rootView.findViewById(R.id.main_price_above);
//        mMainPriceAboveView.findViewById(R.id.member).setVisibility(View.VISIBLE);
//        mMainPriceAboveView.setValue(R.string.main_price_above,
//                R.string.portfolio_remind_stock_ratio_up_hint, 0);
//
//        mMainPriceBelowView = (RemindEditItemLayout) rootView.findViewById(R.id.main_price_below);
//        mMainPriceBelowView.findViewById(R.id.member).setVisibility(View.VISIBLE);
//        mMainPriceBelowView.setValue(R.string.main_price_below,
//                R.string.portfolio_remind_stock_ratio_down_hint, 0);
    }

    private void updateCurrentPrice(float currentPrice) {
        if(getView() != null) {
            updateView(currentPrice);
        }
    }

    private void updateView(float currentPrice) {
        if(currentPrice >= 0) {
            mHoldingPriceAboveView.setData(currentPrice, RemindEditItemLayout.TYPE_NORMAL);
            mHoldingPriceBelowView.setData(currentPrice, RemindEditItemLayout.TYPE_NORMAL);
//            mMainPriceAboveView.setData(currentPrice, RemindEditItemLayout.TYPE_NORMAL);
//            mMainPriceBelowView.setData(currentPrice, RemindEditItemLayout.TYPE_NORMAL);
        }
    }

    public void notifyCurrentPriceChange() {
        updateCurrentPrice(mPresenter.getCurrentPrice());
    }

    private CheckBox initCheckboxItem(View rootView, int itemId, int title) {
        View item = rootView.findViewById(itemId);
        TextView titleView = (TextView) item.findViewById(R.id.smart_stock_stare_title);
        titleView.setText(title);
        CheckBox checkBox = (CheckBox) item.findViewById(R.id.smart_stock_stare_checkbox);
        return checkBox;
    }

    @Override
    public void initFragmentValue(StockDbEntity stockEntity) {
        mNewAnnouncementCheckbox.setChecked(stockEntity.isPushAnnouncement());
        mResearchReportCheckbox.setChecked(stockEntity.isPushResearch());

        mHoldingPriceAboveView.setValue(R.string.holding_price_above,
                R.string.portfolio_remind_stock_price_hint, stockEntity.getChipHighPrice());

        mHoldingPriceBelowView.setValue(R.string.holding_price_below,
                R.string.portfolio_remind_stock_price_hint, stockEntity.getChipLowPrice());

//        mMainPriceAboveView.setValue(R.string.main_price_above,
//                R.string.portfolio_remind_stock_ratio_up_hint, stockEntity.getMainHighPrice());
//
//        mMainPriceBelowView.setValue(R.string.main_price_below,
//                R.string.portfolio_remind_stock_ratio_down_hint, stockEntity.getMainLowPrice());

        updateView(mPresenter.getCurrentPrice());
    }


    @Override
    public int checkFragment(StockDbEntity stockEntity) {
        int result = Submitable.RESULT_NOCHANGE;

        boolean isPushAnnouncement = mNewAnnouncementCheckbox.isChecked();
        if(stockEntity.isPushAnnouncement() != isPushAnnouncement) {
            result = Submitable.RESULT_SHOULD_SUBMIT;
        }

        boolean isPushResearch = mResearchReportCheckbox.isChecked();
        if(stockEntity.isPushResearch() != isPushResearch) {
            result = Submitable.RESULT_SHOULD_SUBMIT;
        }

        if (!floatEquals(stockEntity.getChipHighPrice(), mHoldingPriceAboveView.getValue())) {
            result = Submitable.RESULT_SHOULD_SUBMIT;
        }

        if (!floatEquals(stockEntity.getChipLowPrice(), mHoldingPriceBelowView.getValue())) {
            result = Submitable.RESULT_SHOULD_SUBMIT;
        }

//        if (stockEntity.getMainHighPrice() != mMainPriceAboveView.getValue()) {
//            result = Submitable.RESULT_SHOULD_SUBMIT;
//        }
//
//        if (stockEntity.getMainLowPrice() != mMainPriceBelowView.getValue()) {
//            result = Submitable.RESULT_SHOULD_SUBMIT;
//        }

        return result;
    }

    @Override
    public void submitFragment(StockDbEntity stockEntity) {
        stockEntity.setPushAnnouncement(mNewAnnouncementCheckbox.isChecked());
        stockEntity.setPushResearch(mResearchReportCheckbox.isChecked());
        stockEntity.setChipHighPrice(mHoldingPriceAboveView.getValue());
        stockEntity.setChipLowPrice(mHoldingPriceBelowView.getValue());

//          stockEntity.setMainHighPrice(mMainPriceAboveView.getValue());
//          stockEntity.setMainLowPrice(mMainPriceAboveView.getValue());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.help_icon:
                final String url = DengtaApplication.getApplication().getUrlManager().getChipAvgCostFaqUrl();
                WebBeaconJump.showCommonWebActivity(getContext(), url);
                break;
        }
    }
}
