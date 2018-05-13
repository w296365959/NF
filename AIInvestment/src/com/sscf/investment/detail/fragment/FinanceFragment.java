package com.sscf.investment.detail.fragment;

import BEC.*;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TwoTabSelectorView;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.detail.entity.PillarChangeEntity;
import com.sscf.investment.detail.view.PillarChangeView;
import com.sscf.investment.detail.view.PolylineChangeView;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqf on 2015/7/22.
 */
public class FinanceFragment extends BaseFragment implements DataSourceProxy.IRequestCallback, OnReloadDataListener {
    private static final String TAG = FinanceFragment.class.getSimpleName();
    private LinearLayout mAchievementView;
    private LinearLayout mAnalysisView;
    private PillarChangeView mAchievementPillarChange;
    private PillarChangeView mProfitPillarChange;
    private PolylineChangeView mProfitabilityChange;
    private Handler mUiHandler = new Handler();
    private FinancePerformance mFinancePerformance;
    private FinancialAnalysis mFinanceAnalysis;
    private String mDtSecCode;

    private boolean mHasData = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_finance, container, false);

        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
        }

        final TwoTabSelectorView mTabSelector = (TwoTabSelectorView) contextView.findViewById(R.id.tab_selector);
        mAchievementView = (LinearLayout) contextView.findViewById(R.id.achievement);
        mAchievementPillarChange = (PillarChangeView) contextView.findViewById(R.id.achievement_pillar_change);
//        initAchievementTestData(mAchievementPillarChange);
        mProfitPillarChange = (PillarChangeView) contextView.findViewById(R.id.profit_pillar_change);
        mProfitabilityChange = (PolylineChangeView) contextView.findViewById(R.id.profitability_pillar_change);
        mAnalysisView = (LinearLayout) contextView.findViewById(R.id.analysis);
        mTabSelector.setOnTabSelectedListener(new TwoTabSelectorView.OnTabSelectedListener() {
            @Override
            public void onFirstTabSelected() {
                mAchievementView.setVisibility(View.VISIBLE);
                mAnalysisView.setVisibility(View.GONE);
            }

            @Override
            public void onSecondTabSelected() {
                mAchievementView.setVisibility(View.GONE);
                mAnalysisView.setVisibility(View.VISIBLE);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TAB_FINANCE_ANALYSIS_CLICKED);
            }

            @Override
            public void onThirdTabSelected() {

            }
        });

        contextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTabSelector.setTabTitles(R.string.finance_achievement, R.string.finance_analysis);
            }
        }, 0);

        return contextView;
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_SEC_NEWS);
        helper.setKey(StatConsts.STOCK_INFO_FINANCE);
        return helper;
    }

    private void loadData() {
        if (mHasData) {
            return;
        }

        FinanceReq financeReq = new FinanceReq();
        String unicode = mDtSecCode;
        financeReq.setSDtSecCode(unicode);
        financeReq.setStUserInfo(DengtaApplication.getApplication().getAccountManager().getUserInfo());
        DataEngine.getInstance().request(EntityObject.ET_GET_FINANCE, financeReq, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        DtLog.d(TAG, "onResume");

        loadData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        DtLog.d(TAG, "onHiddenChanged: hidden = " + hidden);
        setUserVisibleHint(!hidden);
        if (!hidden) {
            loadData();
        }
    }

    private void initAchievementTestData(PillarChangeView achievementPillarChange) {
        List<PillarChangeEntity> entityList = new ArrayList<PillarChangeEntity>();
        entityList.add(new PillarChangeEntity("2010", 360000000, 0.23f));
        entityList.add(new PillarChangeEntity("2011", 390000000, 0.15f));
        entityList.add(new PillarChangeEntity("2012", -450000000, 0.14f));
        entityList.add(new PillarChangeEntity("2013", 580000000, 0.11f));
        entityList.add(new PillarChangeEntity("2014", 622000000, 0.10f));
        achievementPillarChange.setData(entityList);
    }

    @Override
    public void callback(boolean success, EntityObject entity) {
        if (!isAdded()) {
            return;
        }

        if (!success || entity.getEntity() == null ) {
            return;
        }

        switch (entity.getEntityType()) {
            case EntityObject.ET_GET_FINANCE:
                final FinanceRsp financeRsp = (FinanceRsp) entity.getEntity();
                mUiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isAdded()) {
                            if (financeRsp != null) {
                                mHasData = true;
                                setData(financeRsp);
                            }
                        }
                    }
                }, 0);
                break;
            default:
                break;
        }
    }

    private void setData(FinanceRsp financeRsp) {
        final FinancePerformance financePerformance = financeRsp.getStFinancePerformance();
        final FinancialAnalysis financialAnalysis = financeRsp.getStFinancialAnalysis();
        mFinancePerformance = financePerformance;
        mFinanceAnalysis = financialAnalysis;

        //关键指标
        setTextById(mAnalysisView, R.id.analysis_key, mFinanceAnalysis.getSUpdateTime());
        setTextById(mAnalysisView, R.id.analysis_eps, mFinanceAnalysis.getSEPS()); //每股收益;
        setTextById(mAnalysisView, R.id.analysis_bvps, mFinanceAnalysis.getSBVPS()); //每股净资产;
        setTextById(mAnalysisView, R.id.analysis_roe, mFinanceAnalysis.getSROE()); //净资产收益率;

        //利润表
        setTextById(mAnalysisView, R.id.analysis_profit, mFinanceAnalysis.getSUpdateTime());
        setTextById(mAnalysisView, R.id.analysis_gross_revenue, mFinanceAnalysis.getSGrossRevenue()); //营业总收入;
        setTextById(mAnalysisView, R.id.analysis_operating_profit, mFinanceAnalysis.getSOperatingProfit()); //营业利润;
        setTextById(mAnalysisView, R.id.analysis_net_profit, mFinanceAnalysis.getSNetProfit()); //净利润
        setTextById(mAnalysisView, R.id.analysis_business_revenue_growth, mFinanceAnalysis.getSBusinessRevenueGrowth()); //营业总收入增长率;
        setTextById(mAnalysisView, R.id.analysis_profit_growth, mFinanceAnalysis.getSProfitGrowth()); //营业利润同比增长
        setTextById(mAnalysisView, R.id.analysis_net_growth, mFinanceAnalysis.getSNetGrowth()); //净利润同比增长

        //资产负债表
        setTextById(mAnalysisView, R.id.analysis_assets, mFinanceAnalysis.getSUpdateTime());
        setTextById(mAnalysisView, R.id.analysis_total_assets, mFinanceAnalysis.getSTotalAssets()); //资产总计
        setTextById(mAnalysisView, R.id.analysis_total_liabilities, mFinanceAnalysis.getSTotalLiabilities()); //负债总计
        setTextById(mAnalysisView, R.id.analysis_total_equities, mFinanceAnalysis.getSTotalEquities()); //所有者权益合计
        setTextById(mAnalysisView, R.id.analysis_total_lev, mFinanceAnalysis.getSLEV()); //资产负债率

        //现金流表
        setTextById(mAnalysisView, R.id.analysis_cash, mFinanceAnalysis.getSUpdateTime());
        setTextById(mAnalysisView, R.id.analysis_operating_cash_flow, mFinanceAnalysis.getSOperationalCashFlow()); //经营性现金流量
        setTextById(mAnalysisView, R.id.analysis_investment_cash_flow, mFinanceAnalysis.getSInvestmentCashFlows()); //投资性现金净量净额
        setTextById(mAnalysisView, R.id.analysis_financing_cash_flow, mFinanceAnalysis.getSFinancingCashFlows()); //融资性现金净量净额

        int opUnit = mFinancePerformance.getEOPUnit();
        String incomeWithUnit = getResources().getString(R.string.incoming_with_unit, StringUtil.getUnitString(opUnit));
        setTextById(mAchievementView, R.id.incoming_with_unit, incomeWithUnit);
        setTextById(mAchievementView, R.id.incoming_detail, mFinancePerformance.getSOperatingDesc());
        ArrayList<OperatingRevenue> operatingRevenues = mFinancePerformance.getVOperatingRevenue();
        List<PillarChangeEntity> entityList = new ArrayList<PillarChangeEntity>();
        for (OperatingRevenue revenue : operatingRevenues) {
            entityList.add(0, new PillarChangeEntity(revenue.getSYear(), revenue.getFIncome(), revenue.getFYearOnYear(), revenue.getVtSeasonOperatingRevenue()));
        }
        mAchievementPillarChange.setData(entityList);

        int profitUnit = mFinancePerformance.getEProfitUnit();
        String profitWithUnit = getResources().getString(R.string.net_profit_with_unit, StringUtil.getUnitString(profitUnit));
        setTextById(mAchievementView, R.id.net_profit_with_unit, profitWithUnit);
        setTextById(mAchievementView, R.id.profit_detail, mFinancePerformance.getSProfitDesc());
        ArrayList<Profit> profits = mFinancePerformance.getVProfit();
        entityList = new ArrayList<>();
        for (Profit profit : profits) {
            entityList.add(0, new PillarChangeEntity(profit.getSYear(), profit.getFNetProfit(), profit.getFYearOnYear()));
        }
        mProfitPillarChange.setData(entityList);

        int epsUnit = mFinancePerformance.getEEPSUnit();
        String epsWithUnit = getResources().getString(R.string.eps_with_unit, StringUtil.getUnitString(epsUnit));
        setTextById(mAchievementView, R.id.eps_with_unit, epsWithUnit);
        setTextById(mAchievementView, R.id.profitability_detail, mFinancePerformance.getSProfitabilityDesc());
        ArrayList<Profitability> profitabilities = mFinancePerformance.getVProfitability();
        entityList = new ArrayList<>();
        for (Profitability profitability : profitabilities) {
            entityList.add(0, new PillarChangeEntity(profitability.getSYear(), profitability.getFROE(), profitability.getFEPS()));
        }
        mProfitabilityChange.setData(entityList);
    }

    private void setTextById(View contextView, int textId, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        TextView textView = (TextView) contextView.findViewById(textId);
        textView.setText(text);
    }

    @Override
    public void onReloadData() {
        loadData();
    }
}
