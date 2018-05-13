package com.sscf.investment.market;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TabLayout;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.market.fragment.*;
import com.sscf.investment.setting.manager.RedDotManager;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.CommonFragmentPagerAdapter;

/**
 * Created by xuebinliu on 2015/7/22.
 *
 * 行情界面
 */
public final class TabFragmentMarket extends BaseFragment implements View.OnClickListener,
        CommonFragmentPagerAdapter.FragmentFactoryCallback{
    private static final String MARKET_CHINA = "market_china";
    private static final String MARKET_HONGKONG = "market_hongkong";
    private static final String MARKET_SHANGHAI_HONGKONG = "market_shanghai_hongkong";
    private static final String MARKET_AMERICA = "market_america";

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private CommonFragmentPagerAdapter mPagerAdapter;
    private int[] mTitles;
    private String[] mTags;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.tab_fragment_market, container, false);
        mTitles = new int[]{R.string.market_china, R.string.market_hongkong, R.string.market_shanghai_hongkong, R.string.market_america};
        mTags = new String[]{MARKET_CHINA, MARKET_HONGKONG, MARKET_SHANGHAI_HONGKONG, MARKET_AMERICA};

        mViewPager = (ViewPager) contextView.findViewById(R.id.market_viewpager);

        mPagerAdapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), mTitles.length, this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                mTabLayout.refreshSelectedTab(i);
            }
        });

        mTabLayout = (TabLayout) contextView.findViewById(R.id.market_tab_layout);
        mTabLayout.initWithTitles(mTitles, mTags);
        mTabLayout.setOnTabSelectionListener(new TabLayout.OnTabSelectionListener() {
            @Override
            public void onTabSelected(int index) {
                mViewPager.setCurrentItem(index);
            }
        });
        mTabLayout.switchTab(0);

        return contextView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewPager.clearOnPageChangeListeners();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mPagerAdapter.setChildUserVisibleHint(!hidden);
    }

    @Override
    public Fragment createFragment(final int index) {
        Fragment fragment = null;
        switch (index) {
            case 0:
                fragment = new MarketChinaFragment();
                break;
            case 1:
                fragment = new MarketHongKongFragment();
                break;
            case 2:
                fragment = new MarketSHHKConnectFragment();
                break;
            case 3:
                fragment = new MarketAmericaFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.actionbar_live:
                WebBeaconJump.showDtLive(activity);
                StatisticsUtil.reportAction(StatisticsConst.MARKET_CLICK_LIVE);
                break;
            case R.id.intelligent_answer_layout:
                final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
                final boolean state = redDotManager.getIntelligentAnswerRedDotState();
                if (state) {
                    redDotManager.setIntelligentAnswerRedDotState(false);
                }
                WebBeaconJump.showIntelligentAnswer(activity, state);
                StatisticsUtil.reportAction(StatisticsConst.MARKET_CLICK_INTELLIGENT_ANSWER);
                break;
            default:
                break;
        }
    }
}