package com.sscf.investment.portfolio;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.widget.PortfolioTwoTabSelectorView;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.market.TabFragmentMarket;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.setting.manager.RedDotManager;
import com.sscf.investment.widget.BaseFragment;

/**
 * Created by yorkeehuang on 2018/1/3. 自选
 */

public class TabFragmentPortfolioOrMarket extends BaseFragment implements PortfolioOrMarketDisplayer, View.OnClickListener,  PortfolioTwoTabSelectorView.OnTabSelectedListener {

    private PortfolioTwoTabSelectorView mTabSelector;
    private View mIntellgentAnswerRedDotView;
    private View mLiveRedDotView;

    private static final int TYPE_PORTFOLIO = 1;//自选
    private static final int TYPE_MARKET = 2;//行情

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_portfolio_or_market, null);
        enableStatusSet();
        initActionbar(rootView);
        switchFragment(TYPE_PORTFOLIO);
        registerBroadcastReceiver();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcastReceiver();
    }

    private void initActionbar(View rootView) {
        mTabSelector = (PortfolioTwoTabSelectorView) rootView.findViewById(R.id.tab_selector);
        mTabSelector.setTabTitles(R.string.portfolio_group_default, R.string.main_tab_name_market);
        mTabSelector.setOnTabSelectedListener(this);
        rootView.findViewById(R.id.actionbar_portfolio_search).setOnClickListener(this);


        rootView.findViewById(R.id.actionbar_live).setOnClickListener(this);
        mLiveRedDotView = rootView.findViewById(R.id.liveRedDot);

        rootView.findViewById(R.id.intelligent_answer_layout).setOnClickListener(this);
        mIntellgentAnswerRedDotView = rootView.findViewById(R.id.intellgentAnswerRedDot);
    }

    public Fragment switchFragment(int type) {
        if(type <= 0) {
            return null;
        }
        FragmentManager manager = getActivity().getSupportFragmentManager();

        String tag = String.valueOf(type);
        Fragment fragment = manager.findFragmentByTag(tag);
        FragmentTransaction transaction = manager.beginTransaction();

        if(fragment == null) {
            fragment = createFragment(type);
        }

        if(fragment != null) {
            transaction.replace(R.id.container, fragment, tag);
            fragment.setUserVisibleHint(true);
            if(fragment instanceof PortfolioOrMarketSubFragment) {
                ((PortfolioOrMarketSubFragment) fragment).setParentDisplayer(this);
            }
            transaction.commitAllowingStateLoss();
        }
        return fragment;
    }

    private Fragment createFragment(int type) {
        switch (type) {
            case TYPE_PORTFOLIO:
                return new TabFragmentPortfolio();
            case TYPE_MARKET:
                return new TabFragmentMarket();
        }
        return null;
    }

    @Override
    public void onLeftTabSelected() {
        switchFragment(TYPE_PORTFOLIO);
    }

    @Override
    public void onLeftTabReSelected() {
        PortfolioGroupManagerActivity.show(getActivity());
    }

    @Override
    public void onRightTabSelected() {
        switchFragment(TYPE_MARKET);
     }

    @Override
    public void onRightTabReSelected() {

    }

    @Override
    public void onClick(View v) {
        Activity activity = getActivity();
        if(activity == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_portfolio_search:
                CommonBeaconJump.showSearch(activity);
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_CLICK_SEARCH);
                break;
            case R.id.intelligent_answer_layout:
                final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
                final boolean state = redDotManager.getIntelligentAnswerRedDotState();
                if (state) {
                    redDotManager.setIntelligentAnswerRedDotState(false);
                }
                WebBeaconJump.showIntelligentAnswer(activity, state);
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_INTELLIGENT_ANSWER_CLICKED);
                break;
            case R.id.actionbar_live:
                WebBeaconJump.showPortfolioLive(activity);
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_CLICK_LIVE);
                break;
            default:
        }
    }

    private LocalBroadcastReceiver mReceiver;

    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new LocalBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter(SettingConst.ACTION_RED_DOT_STATE_CHANGED);
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).registerReceiver(mReceiver, intentFilter);
        }
    }

    private void unregisterBroadcastReceiver() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }


    @Override
    public void setGroupTitle(String groupTitle) {
        mTabSelector.setLeftTitle(groupTitle);
    }

    private final class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_RED_DOT_STATE_CHANGED.equals(action)) {
                final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
                mIntellgentAnswerRedDotView.setVisibility(redDotManager.getIntelligentAnswerRedDotState() ? View.VISIBLE : View.GONE);
                mLiveRedDotView.setVisibility(redDotManager.getMainBoardLiveState() ? View.VISIBLE : View.GONE);
            }
        }
    }
}
