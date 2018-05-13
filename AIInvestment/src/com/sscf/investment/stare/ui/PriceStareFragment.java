package com.sscf.investment.stare.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dengtacj.component.entity.db.StockDbEntity;
import com.dengtacj.component.entity.stare.StareGroupInfo;
import com.dengtacj.component.entity.stare.StareItemInfo;
import com.dengtacj.component.entity.stare.StareSubGroupInfo;
import com.sscf.investment.R;
import com.sscf.investment.portfolio.widget.RemindEditItemLayout;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.stare.ui.widget.SelectedResultFlowLayout;
import com.sscf.investment.stare.ui.widget.StareSelectDialog;
import com.sscf.investment.stare.ui.widget.SubmitableFragment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yorkeehuang on 2017/9/11.
 */

public class PriceStareFragment extends SubmitableFragment implements View.OnClickListener {

    private static final String TAG = PriceStareFragment.class.getSimpleName();

    private RemindEditItemLayout mStockPriceUpView;
    private RemindEditItemLayout mStockPriceDownView;
    private RemindEditItemLayout mStockRatioUpView;
    private RemindEditItemLayout mStockRatioDownView;
    private SelectedResultFlowLayout mFlowLayout;
    private TextView mEmptyView;
    private View mTimeSelectExpand;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_price_stare, container, false);
        initView(view);
        return view;
    }

    private void initView(View rootView) {
        mStockPriceUpView = (RemindEditItemLayout) rootView.findViewById(R.id.portfolioRemindStockPriceUpLayout);

        mStockPriceDownView = (RemindEditItemLayout) rootView.findViewById(R.id.portfolioRemindStockPriceDownLayout);

        mStockRatioUpView = (RemindEditItemLayout) rootView.findViewById(R.id.portfolioRemindStockRatioUpLayout);

        mStockRatioDownView = (RemindEditItemLayout) rootView.findViewById(R.id.portfolioRemindStockRatioDownLayout);

        mTimeSelectExpand = rootView.findViewById(R.id.time_select_extand);
        mTimeSelectExpand.setOnClickListener(this);

        initCurrentPrice(mPresenter.getCurrentPrice());

        initTimeSelectList(rootView);
    }

    private void initTimeSelectList(View rootView) {
        mFlowLayout = (SelectedResultFlowLayout) rootView.findViewById(R.id.selected_result_list);
        mEmptyView = (TextView) rootView.findViewById(R.id.empty);
    }

    public void setSelectedTime(StareGroupInfo groupInfo, Set<Integer> selectedItems) {
        mFlowLayout.clearAll();

        boolean isEmpty = true;
        for(StareSubGroupInfo subGroupInfo : groupInfo.subGroupInfos) {
            for(int i=0, size=subGroupInfo.itemInfoList.size(); i<size; i++) {
                StareItemInfo itemInfo = subGroupInfo.itemInfoList.get(i);
                if(selectedItems.contains(itemInfo.value)) {
                    isEmpty = false;
                    mFlowLayout.addItem(itemInfo.value, itemInfo.name);
                }
            }
        }

        if(isEmpty) {
            mEmptyView.setVisibility(View.VISIBLE);
            mFlowLayout.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mFlowLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_select_extand:
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_STARE_BOBAO_INSTALL);
                List<StareGroupInfo> stareGroupInfoList = mPresenter.getTimeGroupList();
                if(stareGroupInfoList != null && !stareGroupInfoList.isEmpty()) {
                    mPresenter.showSelectDialog(stareGroupInfoList.get(0), mFlowLayout.getValueSet(), StareSelectDialog.LIST_LAYOUT_TYPE_FLOW);
                }
                break;
            default:
        }
    }

    private void initCurrentPrice(float currentPrice) {
        updateView(currentPrice);
    }

    private void updateCurrentPrice(float currentPrice) {
        if(getView() != null) {
            updateView(currentPrice);
        }
    }

    public void notifyCurrentPriceChange() {
        updateCurrentPrice(mPresenter.getCurrentPrice());
    }

    private void updateView(float currentPrice) {
        if(currentPrice >= 0) {
            mStockPriceUpView.setData(currentPrice, RemindEditItemLayout.TYPE_PRICE_UP);
            mStockPriceDownView.setData(currentPrice, RemindEditItemLayout.TYPE_PRICE_DOWN);
            mStockRatioUpView.setData(currentPrice, RemindEditItemLayout.TYPE_RATIO_UP);
            mStockRatioDownView.setData(currentPrice, RemindEditItemLayout.TYPE_RATIO_DOWN);
        }
    }

    @Override
    public void initFragmentValue(StockDbEntity stockEntity) {
        mStockPriceUpView.setValue(R.string.portfolio_remind_stock_price_up_title,
                R.string.portfolio_remind_stock_price_hint, stockEntity.getHighPrice());

        mStockPriceDownView.setValue(R.string.portfolio_remind_stock_price_down_title,
                R.string.portfolio_remind_stock_price_hint, stockEntity.getLowPrice());

        mStockRatioUpView.setValue(R.string.portfolio_remind_stock_ratio_up_title,
                R.string.portfolio_remind_stock_ratio_up_hint, stockEntity.getIncreasePer());

        mStockRatioDownView.setValue(R.string.portfolio_remind_stock_ratio_down_title,
                R.string.portfolio_remind_stock_ratio_down_hint, stockEntity.getDecreasesPer());

        if(mPresenter.getTimeGroupList() != null && !mPresenter.getTimeGroupList().isEmpty()) {
            StareGroupInfo groupInfo = mPresenter.getTimeGroupList().get(0);
            if(groupInfo != null && groupInfo.allItems != null) {
                List<Integer> broadcastTimeList = stockEntity.getBroadcastTimeList();
                Set<Integer> selectedItems;
                if(broadcastTimeList != null) {
                    selectedItems = new HashSet<>(broadcastTimeList);
                } else {
                    selectedItems = new HashSet<>();
                }
                setSelectedTime(groupInfo, selectedItems);
            }
        }
    }

    @Override
    public int checkFragment(StockDbEntity stockEntity) {
        if (!mStockPriceUpView.isInputValid() || !mStockPriceDownView.isInputValid()) {
            return Submitable.RESULT_INVALID;
        }

        int result = Submitable.RESULT_NOCHANGE;
        if (!floatEquals(stockEntity.getHighPrice(), mStockPriceUpView.getValue())) {
            result = Submitable.RESULT_SHOULD_SUBMIT;
        }

        if (!floatEquals(stockEntity.getLowPrice(), mStockPriceDownView.getValue())) {
            result = Submitable.RESULT_SHOULD_SUBMIT;
        }

        if (!floatEquals(stockEntity.getIncreasePer(), mStockRatioUpView.getValue())) {
            result = Submitable.RESULT_SHOULD_SUBMIT;
        }

        if (!floatEquals(stockEntity.getDecreasesPer(), mStockRatioDownView.getValue())) {
            result = Submitable.RESULT_SHOULD_SUBMIT;
        }

        String resultText = StockDbEntity.convertIntegerList2Text(mFlowLayout.getValues());
        if(!TextUtils.equals(resultText, stockEntity.getBroadcastTime())) {
            result = Submitable.RESULT_SHOULD_SUBMIT;
        }
        return result;
    }

    @Override
    public void submitFragment(StockDbEntity stockEntity) {
        stockEntity.setHighPrice(mStockPriceUpView.getValue());
        stockEntity.setLowPrice(mStockPriceDownView.getValue());
        stockEntity.setIncreasePer(mStockRatioUpView.getValue());
        stockEntity.setDecreasesPer(mStockRatioDownView.getValue());
        stockEntity.setBroadcastTime(StockDbEntity.convertIntegerList2Text(mFlowLayout.getValues()));
    }
}
