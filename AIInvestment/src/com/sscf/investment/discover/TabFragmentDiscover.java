package com.sscf.investment.discover;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.common.entity.AdEntity;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.component.ui.widget.TabLayout;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.manager.AdManager;
import com.sscf.investment.main.manager.AdRequestManager;
import com.sscf.investment.popup.PopupWindowNewFunction;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.search.SearchActivity;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.setting.manager.RedDotManager;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.CommonFragmentPagerAdapter;

import java.io.File;

import BEC.DtActivityDetail;

/**
 * Created by liqf on 2015/10/20. 选股
 */
public final class TabFragmentDiscover extends BaseFragment implements View.OnClickListener,
        CommonFragmentPagerAdapter.FragmentFactoryCallback, DataSourceProxy.IRequestCallback {
     static final String TAG_STOCK_PICK = "stock_pick";
    private static final String TAG_STRATEGY_STOCK_PICK = "strategy_stock_pick";
    private static final String TAG_SUBSCRIPTION = "subscription";

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private CommonFragmentPagerAdapter mPagerAdapter;
    private int[] mTitles;
    private String[] mTags;
    private View mIntellgentAnswerRedDotView;
    private ImageView mAdView;
    private AdEntity mAdEntity;
    private File mAdFile;
    private View mAnswerAI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View contextView = inflater.inflate(R.layout.tab_fragment_discover_native, container, false);
        enableStatusSet();
        contextView.findViewById(R.id.intelligent_answer_layout).setOnClickListener(this);
        contextView.findViewById(R.id.actionbar_search).setOnClickListener(this);
        mAnswerAI = contextView.findViewById(R.id.iv_answer_ai);
        mAdView = (ImageView) contextView.findViewById(R.id.ad);
        mAdView.setOnClickListener(this);

        mTitles = new int[]{R.string.stock_pick_strategy_title, R.string.stock_pick_strategy_stock_pick_title, R.string.stock_pick_subscription_title};
        mTags = new String[]{TAG_STOCK_PICK, TAG_STRATEGY_STOCK_PICK, TAG_SUBSCRIPTION};

        mViewPager = (ViewPager) contextView.findViewById(R.id.viewpager);
        mPagerAdapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), mTitles.length, this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                mTabLayout.refreshSelectedTab(i);
            }
        });

        mTabLayout = (TabLayout) contextView.findViewById(R.id.tab_layout);
        mTabLayout.initWithTitles(mTitles, mTags);
        mTabLayout.setOnTabSelectionListener(new TabLayout.OnTabSelectionListener() {
            @Override
            public void onTabSelected(int index) {
                mViewPager.setCurrentItem(index);
                if (index == 0) {//策略精选
                    StatisticsUtil.reportAction(StatisticsConst.DISCOVER_STOCK_PICK_DISPLAY);
                }else if(index==1){//策略选股
                    StatisticsUtil.reportAction(StatisticsConst.A_MACD_STRATEGIC_STOCK_BANNER_CLICK);
                }else  {//我的订阅
                    StatisticsUtil.reportAction(StatisticsConst.A_MACD_DINGYUE_CLICK);
                }
            }
        });
        mTabLayout.switchTab(0);

        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        final RedDotManager redDotManager = dengtaApplication.getRedDotManager();
        mTabLayout.setRedDotVisible(2, redDotManager.getSubscriptionState() ? View.VISIBLE : View.GONE);

        mIntellgentAnswerRedDotView = contextView.findViewById(R.id.intellgentAnswerRedDot);
        mIntellgentAnswerRedDotView.setVisibility(redDotManager.getIntelligentAnswerRedDotState() ? View.VISIBLE : View.GONE);

        registerBroadcastReceiver();

        if (dengtaApplication.isNewInstall()) {
            showGuideLayout();
        }

        return contextView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcastReceiver();
        mViewPager.clearOnPageChangeListeners();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mPagerAdapter.setChildUserVisibleHint(!hidden);
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        StatisticsUtil.reportAction(StatisticsConst.DISCOVER_SHOW);
        DengtaApplication.getApplication().getSubscriptionManager().getSubscriptionListRequest();
        if (mAdEntity == null) {
            AdRequestManager.requestStockPickFlowAd(this);
        }
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        StatisticsUtil.reportAction(StatisticsConst.DISCOVER_SHOW);
        DengtaApplication.getApplication().getSubscriptionManager().getSubscriptionListRequest();
        if (mAdEntity == null) {
            AdRequestManager.requestStockPickFlowAd(this);
        }
    }

    @Override
    public Fragment createFragment(final int index) {
        Fragment fragment = null;
        switch (index) {
            case 0://策略精选
                fragment = new DiscoverStockPickFragment();
                break;
            case 1://策略选股
                fragment = new StrategyStockPickFragment();
                break;
            case 2://我的订阅
                fragment = new StockPickSubscriptionFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public void onClick(View v) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.intelligent_answer_layout:
                final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
                final boolean state = redDotManager.getIntelligentAnswerRedDotState();
                if (state) {
                    redDotManager.setIntelligentAnswerRedDotState(false);
                }
                WebBeaconJump.showIntelligentAnswer(activity, state);
                StatisticsUtil.reportAction(StatisticsConst.DISCOVER_INTELLIGENT_ANSWER_CLICKED);
                removeGuideLayout();
                break;
            case R.id.actionbar_search:
                StatisticsUtil.reportAction(StatisticsConst.DISCOVER_SEARCH_CLICKED);
                activity.startActivity(new Intent(activity, SearchActivity.class));
                break;
            case R.id.ad:
                final AdEntity adEntity = mAdEntity;
                if (adEntity != null) {
                    final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                    if (scheme != null) {
                        scheme.handleUrl(activity, adEntity.url);
                    }
                    DengtaApplication.getApplication().defaultExecutor.execute(() -> {
                        final File adFile = getAdFile(DengtaApplication.getApplication());
                        adEntity.clickTime = System.currentTimeMillis();
                        FileUtil.saveObjectToFile(adEntity, adFile);
                    });
                }
                mAdView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private View mGuideLayout = null;

    private void showGuideLayout() {
        PopupWindow popupWindow = new PopupWindowNewFunction(getActivity(), R.string.guide_intelligent_answer);
        popupWindow.showAsDropDown(mAnswerAI, -450, 0);
    }

    private void removeGuideLayout() {
        if (mGuideLayout != null) {
            final ViewGroup root = (ViewGroup) getView();
            if (root != null) {
                root.removeView(mGuideLayout);
            }
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

    private final class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_RED_DOT_STATE_CHANGED.equals(action)) {
                final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
                mTabLayout.setRedDotVisible(2, redDotManager.getSubscriptionState() ? View.VISIBLE : View.GONE);
                mIntellgentAnswerRedDotView.setVisibility(redDotManager.getIntelligentAnswerRedDotState() ? View.VISIBLE : View.GONE);
            }
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_DT_ACTIVITY_LIST:
                final DtActivityDetail detail = EntityUtil.entityToDtActivityDetail(success, data);
                if (detail != null) {
                    final Activity activity = getActivity();
                    if (activity != null) {
                        final File adFile = getAdFile(activity);
                        AdEntity adEntity = (AdEntity) FileUtil.getObjectFromFile(adFile);
                        if (adEntity != null) {
                            // 当天点击过的广告，就不在显示
                            if (TextUtils.equals(adEntity.url, detail.sUrl) && TextUtils.equals(adEntity.picUrl, detail.sPicUrl)
                                    && AdManager.compareDate(adEntity.clickTime, System.currentTimeMillis())) {
                                mAdEntity = adEntity;
                                return;
                            } else {
                                adFile.delete();
                            }
                        }
                        adEntity = new AdEntity();
                        adEntity.url = detail.sUrl;
                        adEntity.picUrl = detail.sPicUrl;
                        mAdEntity = adEntity;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdView.setVisibility(View.VISIBLE);
                                ImageLoaderUtils.getImageLoader().displayImage(detail.sPicUrl, mAdView);
                            }
                        });
                        StatisticsUtil.reportAction(StatisticsConst.DISCOVER_FLOAT_AD_DISPLAY);
                    }
                }
                break;
            default:
                break;
        }
    }

    private File getAdFile(Context context) {
        File file = mAdFile;
        if (file == null) {
            file = new File(context.getFilesDir(), "ad/stockpick.bat");
            mAdFile = file;
        }
        return file;
    }
}
