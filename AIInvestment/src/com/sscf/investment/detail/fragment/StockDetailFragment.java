package com.sscf.investment.detail.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.ShareParams;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.dengtacj.component.router.BeaconJump;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.sscf.investment.detail.FragmentFactory;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.detail.SecurityDetailActivity;
import com.sscf.investment.detail.presenter.SecurityDetailPresenter;
import com.sscf.investment.detail.view.DetailActionBar;
import com.sscf.investment.detail.view.DetailFunctionRecyclerView;
import com.sscf.investment.detail.view.KLineInfosView;
import com.sscf.investment.detail.view.MemoLayout;
import com.sscf.investment.detail.view.OperationBar;
import com.sscf.investment.detail.view.ScrollableTabLayout;
import com.sscf.investment.detail.view.StockStatusTipsLayout;
import com.sscf.investment.main.DataPref;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.socialize.ShareDialog;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.utils.ViewUtils;
import com.sscf.investment.widget.BeaconPtrFrameLayout;
import com.sscf.investment.widget.FlowScrollView;
import com.sscf.investment.widget.LiveMessageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import BEC.BEACON_STAT_TYPE;
import BEC.E_NEMS_GET_SOURCE;
import BEC.E_NEWS_TYPE;
import BEC.E_SEC_ATTR;
import BEC.SecBaseInfo;
import BEC.SecBaseInfoRsp;
import BEC.SecInfo;
import BEC.SecLiveMsg;
import BEC.SecLiveMsgRsp;
import BEC.SecQuote;
import BEC.SecStatusInfo;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 个股详情
 */
public final class StockDetailFragment extends SecurityDetailFragment implements OperationBar.OnOperationListener,
        DetailActionBar.OnOperationListener, View.OnClickListener {

    private static final String TAG = StockDetailFragment.class.getSimpleName();

    private static final int FUNCTION_DIRECTION_ADD_NUGGETS = 0;
    private static final int FUNCTION_MARGIN_TRACKING = 1;
    private static final int FUNCTION_DK = 2;
    private static final int FUNCTION_CHART_FORM_SEARCH = 3;
    private static final int FUNCTION_SIMILAR_K = 4;
    private static final int FUNCTION_HISTORY = 5;
    private static final int FUNCTION_BULLBEAR = 6;

    private View mContextView;
    private SecurityDetailActivity mActivity;

    private BeaconPtrFrameLayout mPtrFrame;
    private FlowScrollView mScrollView;
    private OperationBar mOperationBar;

    private DetailActionBar mActionBar;

    // ---------- quote区域 ----------
    private TextView mText11;
    private TextView mText13;
    private TextView mText15;
    private TextView mText17;
    private TextView mText21;
    private TextView mText23;
    private TextView mText25;
    private TextView mText26;
    private TextView mText27;
    // ---------- quote区域 ----------

    private LiveMessageView mLiveMsgView;
    private String mLastLiveMsgId = "";

    private final Handler mUiHandler = new Handler();

    private ArrayList<Fragment> mTabFragmentList = new ArrayList<>();

    private View mGuideLayout;

    private boolean mTabInited;

    private ScrollableTabLayout mArticleTabLayout;
    private ScrollableTabLayout mArticleTabLayoutSticky;
    private FunctionItemAdapter mFunctionsItemAdapter;
    private boolean mTabFlowViewVisible;

    private StockStatusTipsLayout mStockStatusTipsLayout;
    private MemoLayout mMemoLayout;

    private View mQuoteLayout;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED.equals(action)) {
                int marketType = intent.getIntExtra(TradingStateManager.KEY_MARKET_TYPE, -1);
                if (marketType == mMarketType) {
                    int tradingState = intent.getIntExtra(TradingStateManager.KEY_TRADING_STATE, -1);
                    mIsTrading = tradingState == TradingStateManager.TRADING_STATE_TRADING;
                    mIsCallauction = tradingState == TradingStateManager.TRADING_STATE_CALLAUCTION;
                    DtLog.d(TAG, "onReceive: TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED tradingState = " + tradingState);
                    queryTradingValuesAndRefresh(false);
                }
            } else if (TradingStateManager.ACTION_TRADING_STATE_UPDATED.equals(action)){
                queryTradingValuesAndRefresh(true);
            }
        }
    };
    private ViewTreeObserver.OnScrollChangedListener mTabLayoutScrollListener;
    private ViewTreeObserver.OnScrollChangedListener mTabLayoutStickyScrollListener;
    private ViewTreeObserver.OnScrollChangedListener mScrollListener;

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_SEC_DETAIL_PAGE);
        helper.setKey(StatConsts.CHINA_STOCK);
        return helper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DtLog.d(TAG, "onCreateView: mDtSecCode = " + mDtSecCode + ", mName = " + mName);
        mActivity = (SecurityDetailActivity) getActivity();
        final View contextView = getActivity().getLayoutInflater().inflate(R.layout.main_stock_detail, container, false);
        mContextView = contextView;

        initViews();

        preloadQuoteData();

        mPresenter = new SecurityDetailPresenter(this, mLineChartFragment, mDtSecCode);
        // 预加载使用
        mPresenter.refreshDelay(100);
        return contextView;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceivers();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceivers();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        doOnResume();
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        doOnResume();
    }

    @Override
    public void onFirstUserInvisible() {
        super.onFirstUserInvisible();
        doOnPause();
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        doOnPause();
    }

    private void doOnResume() {
        // 数据刷新
        queryTradingValuesAndRefresh(true);
        if (!mIsTrading && !mIsCallauction) { //即使不在交易时间，无论如何要拉取一次历史的行情数据
            mPresenter.refresh();
        }
        // ui 刷新
        if (mTabInited) {
            switchTabIfNeeded(false);
        }
        mActionBar.setContentScrollX(0);
        refreshAlarmBar();
        centerSelectedTab();
        updateMemo();

        StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_SHOW_CHINA_STOCK);
    }

    private void updateMemo() {
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        final String comment = portfolioDataManager == null ? null : portfolioDataManager.getComment(mDtSecCode);
        if (TextUtils.isEmpty(comment)) {
            if (mMemoLayout != null) {
                mMemoLayout.setVisibility(View.GONE);
            }
        } else {
            if (mMemoLayout == null) {
                final ViewStub viewStub = (ViewStub) mContextView.findViewById(R.id.memoLayoutStub);
                if (viewStub != null) {
                    mMemoLayout = (MemoLayout) viewStub.inflate();
                }
            }
            if (mMemoLayout != null) {
                mMemoLayout.setDtSecCode(mDtSecCode, mName);
                mMemoLayout.setComment(comment);
            }
        }
    }

    private void centerSelectedTab() {
        String stockTabTag = DataPref.getStockTabTag();
        mArticleTabLayout.setCenterByTag(stockTabTag);
        mArticleTabLayoutSticky.setCenterByTag(stockTabTag);
    }

    private void switchTabIfNeeded(final boolean force) {
        String tabTag = DataPref.getStockTabTag();
        final ScrollableTabLayout articleTabLayout = (ScrollableTabLayout) mContextView.findViewById(R.id.article_tabs);
        if (!TextUtils.equals(tabTag, articleTabLayout.getCurrentTag()) || force) {
            articleTabLayout.switchTab(tabTag);
        }
    }

    @Override
    public void onPageScrollStateIdle() {
        super.onPageScrollStateIdle();

        mActionBar.setContentScrollX(0);
    }

    @Override
    public void onPageScrolled(boolean isLeft, float positionOffset) {
        super.onPageScrolled(isLeft, positionOffset);

        if (isLeft) {
            mActionBar.setContentScrollX(positionOffset);
        } else {
            mActionBar.setContentScrollX(positionOffset);
        }
    }

    private void registerReceivers() {
        IntentFilter filter = new IntentFilter(TradingStateManager.ACTION_TRADING_STATE_UPDATED);
        filter.addAction(TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED);
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mStockStatusTipsLayout != null) {
            mStockStatusTipsLayout.stopSwitchAnimation();
            mStockStatusTipsLayout = null;
        }

        mArticleTabLayout.getViewTreeObserver().removeOnScrollChangedListener(mTabLayoutScrollListener);
        mArticleTabLayoutSticky.getViewTreeObserver().removeOnScrollChangedListener(mTabLayoutStickyScrollListener);
        mScrollView.getViewTreeObserver().removeOnScrollChangedListener(mScrollListener);
        mPresenter.release();
    }

    private void doOnPause() {
        if (mPresenter != null) {
            mPresenter.stopRefresh();
        }
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).unregisterReceiver(mBroadcastReceiver);
    }

    private void initViews() {
        mPtrFrame = (BeaconPtrFrameLayout) mContextView.findViewById(R.id.ptr);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                doRefresh();
            }
        });
        mPtrFrame.setSupportDisallowInterceptTouchEvent(true);

        mScrollView = (FlowScrollView) mContextView.findViewById(R.id.scrollview);

        initFlowView();

        initQuoteView();

        mScrollView.setOnScrollListener((scrollY) ->  {
                if (scrollY == 0) {
                    mPtrFrame.requestDisallowInterceptTouchEvent(false);
                }
                mActionBar.showSubTitleAlternative(scrollY >= mQuoteLayout.getBottom());
        });

        initLineChartFragment();

        final FragmentManager fragmentManager = getChildFragmentManager();

        initFunctionView(mDtSecCode);

        mArticleTabLayout = (ScrollableTabLayout) mContextView.findViewById(R.id.article_tabs);
        mArticleTabLayout.setParentScrollView(mScrollView);
        mArticleTabLayoutSticky = (ScrollableTabLayout) mContextView.findViewById(R.id.article_tabs_sticky);
        mArticleTabLayoutSticky.setParentScrollView(mScrollView);
        mUiHandler.postDelayed(() -> {
            initArticleTabLayout(fragmentManager, mArticleTabLayout, mArticleTabLayoutSticky);
            initArticleTabLayout(fragmentManager, mArticleTabLayoutSticky, mArticleTabLayout);
            switchTabIfNeeded(true);
            mTabInited = true;
        }, INIT_TAB_DELAY);

        mTabLayoutScrollListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (!mTabFlowViewVisible) {
                    mArticleTabLayoutSticky.setScrollX(mArticleTabLayout.getScrollX());
                }
            }
        };
        mArticleTabLayout.getViewTreeObserver().addOnScrollChangedListener(mTabLayoutScrollListener);
        mTabLayoutStickyScrollListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (mTabFlowViewVisible) {
                    mArticleTabLayout.setScrollX(mArticleTabLayoutSticky.getScrollX());
                }
            }
        };
        mArticleTabLayoutSticky.getViewTreeObserver().addOnScrollChangedListener(mTabLayoutStickyScrollListener);
        mScrollListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mTabFlowViewVisible = mScrollView.getScrollY() > getFlowViewTop();
            }
        };
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(mScrollListener);

        mActionBar = (DetailActionBar) mContextView.findViewById(R.id.detail_actionbar);
        mActionBar.setDtSecCode(mDtSecCode, mName);
        mActionBar.setOnOperationListener(this);
        mActionBar.setHasSubTitle(true);
        mActionBar.setTitleText(StringUtil.getActionBarTitle(mName, mShowSecCode));
        mOperationBar = (OperationBar) mContextView.findViewById(R.id.operation_bar);
        mOperationBar.setDtSecCode(mDtSecCode, mName);
        mOperationBar.setOnOperationListener(this);
        mOperationBar.showLiveMsgRedDot(DengtaApplication.getApplication().getDataCacheManager().hasUnreadLiveMsg(mDtSecCode));

        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        final boolean isAdded = portfolioDataManager == null ? false : portfolioDataManager.isPortfolio(mDtSecCode);
        mOperationBar.setIsAdded(isAdded);

        mLiveMsgView = (LiveMessageView) mContextView.findViewById(R.id.live_message);
        mLiveMsgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_LIVE_MSG_CLICKED);
                mLiveMsgView.setVisibility(View.GONE);
                openSecLiveMsgPage();
            }
        });
    }

    private SparseArray<Integer> mFunctions = new SparseArray<>();

    private SparseArray<Integer> getFunctions() {
        return mFunctions;
    }

    private void updateFunctionBySecInfo(SecInfo secInfo) {
        SparseArray<Integer> functions = getFunctions();
        boolean hasChange = false;

        if(StockUtil.isDirectorAddStock(secInfo)) {
            Integer function = functions.get(FUNCTION_DIRECTION_ADD_NUGGETS);
            if(function == null) {
                functions.put(FUNCTION_DIRECTION_ADD_NUGGETS, R.string.direction_add_nuggets);
                hasChange = true;
            }
        }

        if(StockUtil.isMarginStock(secInfo)) {
            Integer function = functions.get(FUNCTION_MARGIN_TRACKING);
            if(function == null) {
                functions.put(FUNCTION_MARGIN_TRACKING, R.string.operation_margin_tracking);
                hasChange = true;
            }
        }

        if(hasChange) {
            mFunctionsItemAdapter.setData(functions);
        }
    }

    private void initFunctionView(String dtSecCode) {
        SparseArray<Integer> functions = getFunctions();

        if(StockUtil.supportBSPoint(dtSecCode)) {
            functions.put(FUNCTION_CHART_FORM_SEARCH, R.string.operation_dk);
        }

        if(StockUtil.supportSimilarK(dtSecCode)) {
            functions.put(FUNCTION_SIMILAR_K, R.string.operation_similar_k);
        }

        if(StockUtil.supportSecHistory(dtSecCode)) {
            functions.put(FUNCTION_HISTORY, R.string.operation_history);
        }

        if (StockUtil.supportBullbearPoint(dtSecCode)) {
            functions.put(FUNCTION_BULLBEAR, R.string.operation_bullbear);
        }

        DetailFunctionRecyclerView recyclerView = (DetailFunctionRecyclerView) mContextView.findViewById(R.id.detail_function_recycler_view);
        recyclerView.setParentScrollView(mScrollView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mFunctionsItemAdapter = new FunctionItemAdapter(getContext(), R.layout.detail_function_button);
        recyclerView.setAdapter(mFunctionsItemAdapter);
        mFunctionsItemAdapter.setData(functions);

        TextView settingBtn = (TextView) mContextView.findViewById(R.id.setting);
        settingBtn.setOnClickListener(this);
    }

    private void  initQuoteView() {
        final View quoteLayout = mContextView.findViewById(R.id.quoteLayout);
        quoteLayout.setOnClickListener(this);
        mQuoteLayout = quoteLayout;
        mText11 = (TextView) quoteLayout.findViewById(R.id.text11);
        mText13 = (TextView) quoteLayout.findViewById(R.id.text13);
        mText15 = (TextView) quoteLayout.findViewById(R.id.text15);
        mText17 = (TextView) quoteLayout.findViewById(R.id.text17);
        mText21 = (TextView) quoteLayout.findViewById(R.id.text21);
        mText23 = (TextView) quoteLayout.findViewById(R.id.text23);
        mText25 = (TextView) quoteLayout.findViewById(R.id.text25);
        mText26 = (TextView) quoteLayout.findViewById(R.id.text26);
        mText27 = (TextView) quoteLayout.findViewById(R.id.text27);
    }

    private void doRefresh() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mPtrFrame.refreshComplete();
            DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
            return;
        }

        mPresenter.refresh();
        mLineChartFragment.onReloadData();

        Fragment currentFragment = findFragmentByTag(mArticleTabLayout.getCurrentTag());
        if (currentFragment != null && currentFragment.isAdded() && currentFragment instanceof OnReloadDataListener) {
            ((OnReloadDataListener) currentFragment).onReloadData();
        }
    }

    private void preloadQuoteData() {
        final SecQuote quote = DengtaApplication.getApplication().getDataCacheManager().getSecQuote(mDtSecCode);
        updateQuoteView(quote);
    }

    private void initFlowView() {
        final View articleTabLayoutStickyContainer = mContextView.findViewById(R.id.article_tabs_sticky_container);
        final View articleTabLayoutContainer = mContextView.findViewById(R.id.article_tabs_container);
        mScrollView.setFlowLayers(articleTabLayoutStickyContainer, articleTabLayoutContainer);
    }

    private int getFlowViewTop() {
        final View articleTabLayoutContainer = mContextView.findViewById(R.id.article_tabs_container);
        return articleTabLayoutContainer.getTop();
    }

    private void initStatusTipsLayout() {
        ViewStub statusTipsViewStub = (ViewStub) mContextView.findViewById(R.id.viewstub_status_tips);
        if (statusTipsViewStub == null) {
            return;
        }
        statusTipsViewStub.inflate();
        mStockStatusTipsLayout = (StockStatusTipsLayout) mContextView.findViewById(R.id.stock_status_tips);
    }

    private void openFigureActivity() {
        WebBeaconJump.showStockPortrait(mActivity, mDtSecCode, mName);
    }

    private void initArticleTabLayout(final FragmentManager fragmentManager, final ScrollableTabLayout articleTabLayout, final ScrollableTabLayout stickyLayout) {
        articleTabLayout.initWithTitles(
            new int[]{R.string.tab_news, R.string.tab_capital, R.string.tab_diagnosis,
                    R.string.tab_interaction, R.string.tab_notice, R.string.tab_report, R.string.tab_finance, R.string.tab_intro},
            new String[]{FragmentFactory.NEWS, FragmentFactory.CAPITAL, FragmentFactory.DIAGNOSIS, FragmentFactory.INTERACTION, FragmentFactory.NOTICE, FragmentFactory.REPORT, FragmentFactory.FINANCE, FragmentFactory.INTRO});
        articleTabLayout.setOnTabSelectionListener(new ScrollableTabLayout.OnTabSelectionListener() {
            @Override
            public void onTabSelected(int index) {
                if (index == 0) {
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TAB_NEWS_CLICKED);
                } else if (index == 1) {
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TAB_CAPITAL_CLICKED);
                } else if (index == 2) {
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TAB_DIAGNOSIS_CLICKED);
                } else if(index == 3) {

                } else if (index == 4) {
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TAB_ANNOUNCEMENT_CLICKED);
                } else if (index == 5) {
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TAB_REPORT_CLICKED);
                }  else if (index == 6) {
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TAB_FINANCE_CLICKED);
                } else if (index == 7) {
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TAB_INTRO_CLICKED);
                }

                String tag = articleTabLayout.getTagByIndex(index);
                DataPref.setStockTabTag(tag);

                replaceFragmentByTag(fragmentManager, tag, articleTabLayout.getCurrentTag(), R.id.article_fragment_container);
                stickyLayout.refreshSelectedTab(index);

                int flowViewTop = getFlowViewTop();
                int scrollY = mScrollView.getScrollY();
                if (scrollY > flowViewTop) {
                    mScrollView.setScrollY(flowViewTop);
                }
            }
        });

        articleTabLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                articleTabLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                articleTabLayout.setCenterByTag(DataPref.getStockTabTag());
                return false;
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        DtLog.d(TAG, "setUserVisibleHint mName = " + mName + ", isVisibleToUser = " + isVisibleToUser);
        Fragment fragment = getCurrentArticleFragment();
        if(fragment != null) {
           fragment.setUserVisibleHint(isVisibleToUser);
        }
    }

    private Fragment getCurrentArticleFragment() {
        if(mArticleTabLayout != null) {
            String tag = mArticleTabLayout.getCurrentTag();
            if(!TextUtils.isEmpty(tag)) {
                return findFragmentByTag(tag);
            }
        }
        return null;
    }

    private void replaceFragmentByTag(FragmentManager fragmentManager, String tag, String currentTag, int containerId) {
        if (!isAdded() || (getActivity() != null && ((BaseFragmentActivity) getActivity()).isDestroy())) {
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment currentFragment = findFragmentByTag(currentTag);
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = findFragmentByTag(tag);
        if (fragment == null) {
            Bundle bundle;
            // 特别注意：a股的公告特殊处理
            if (FragmentFactory.NOTICE.equals(tag)) {
                fragment = new AStockNoticeFragment();
                bundle = new Bundle();
                bundle.putString(CommonConst.KEY_SEC_CODE, mDtSecCode);
                bundle.putInt(CommonConst.KEY_NEWS_TYPE, E_NEWS_TYPE.NT_ANNOUNCEMENT);
                bundle.putInt(CommonConst.KEY_GET_SOURCE, E_NEMS_GET_SOURCE._E_STOCK_MARKET_GET);
                bundle.putString(CommonConst.KEY_SEC_NAME, mName);
                fragment.setArguments(bundle);
                mTabFragmentList.add(fragment);
            } else {
                fragment = FragmentFactory.createFragment(tag);
                if (fragment instanceof NewsFragment) {
                    if (FragmentFactory.NEWS.equals(tag)) {
                        bundle = new Bundle();
                        bundle.putString(CommonConst.KEY_SEC_CODE, mDtSecCode);
                        bundle.putInt(CommonConst.KEY_NEWS_TYPE, E_NEWS_TYPE.NT_NEWS);
                        bundle.putInt(CommonConst.KEY_GET_SOURCE, E_NEMS_GET_SOURCE._E_STOCK_MARKET_GET);
                        bundle.putString(CommonConst.KEY_SEC_NAME, mName);
                        fragment.setArguments(bundle);
                    }  else if (FragmentFactory.REPORT.equals(tag)) {
                        bundle = new Bundle();
                        bundle.putString(CommonConst.KEY_SEC_CODE, mDtSecCode);
                        bundle.putInt(CommonConst.KEY_NEWS_TYPE, E_NEWS_TYPE.NT_REPORT);
                        bundle.putInt(CommonConst.KEY_GET_SOURCE, E_NEMS_GET_SOURCE._E_STOCK_MARKET_GET);
                        bundle.putString(CommonConst.KEY_SEC_NAME, mName);
                        fragment.setArguments(bundle);
                    }
                    mTabFragmentList.add(fragment);
                } else {
                    bundle = new Bundle();
                    bundle.putString(CommonConst.KEY_SEC_CODE, mDtSecCode);
                    bundle.putString(CommonConst.KEY_SEC_NAME, mName);
                    fragment.setArguments(bundle);
                    mTabFragmentList.add(fragment);
                }
            }
            if (!fragment.isAdded()) {
                DtLog.d(TAG, "mName = " + mName);
                fragment.setUserVisibleHint(getUserVisibleHint());
                fragmentTransaction.add(containerId, fragment, tag);
            }
        }

        showTab(fragmentTransaction, tag, mTabFragmentList);

        fragmentTransaction.commitAllowingStateLoss();
    }

    private void showTab(FragmentTransaction ft, String currentTag, List<Fragment> fragmentList){
        for (Fragment fragment : fragmentList) {
            String tag = fragment.getTag();

            if (TextUtils.equals(currentTag, tag)) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
        }
    }

    private Fragment findFragmentByTag(String tag) {
        for (Fragment fragment : mTabFragmentList) {
            if (TextUtils.equals(tag, fragment.getTag())) {
                return fragment;
            }
        }
        return null;
    }

    @Override
    public void onLiveMsg() {
        openSecLiveMsgPage();
    }

    @Override
    public void onShare() {
        DtLog.d(TAG, "onShare");

        if (isAdded()) {
            getShareDialog().showShareDialog(getShareParams());
            StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_SHARE_CLICKED);
        }
    }

    @Override
    public void onFigure() {
        openFigureActivity();
    }

    @Override
    public void onChip() {
        WebBeaconJump.showCYQ(getActivity(), mDtSecCode, mName);
        StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_CHIP_ENTRANCE_CLICKED);
    }

    private ShareDialog mShareDialog;
    private ShareParams mShareParams;

    private ShareDialog getShareDialog() {
        if (mShareDialog == null) {
            mShareDialog = new ShareDialog(mActivity);
        }
        return mShareDialog;
    }

    public ShareParams getShareParams() {
        ShareParams params = mShareParams;
        if (params == null) {
            params = new ShareParams();

            final StringBuilder builder = new StringBuilder(mName);
            builder.append('(').append(mShowSecCode).append(')');
            params.title = builder.toString();

            final String stockDetailUrl = DengtaApplication.getApplication().getUrlManager().getStockDetailUrl(mDtSecCode, mName);
            params.url = stockDetailUrl;
            params.imageUrl = DengtaApplication.getApplication().getUrlManager().getShareIconUrl();

            mShareParams = params;
        }

        // 分享时刻最新价/涨跌幅
        final SecQuote quote = mPresenter.getQuote();
        params.msg = getString(R.string.share_stock_msg, mText11.getText().toString(), StringUtil.getUpdownString(quote));
        return params;
    }

    @Override
    public void onAlarm() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }
        if (accountManager.isLogined()) {
            final String dtSecCode = mDtSecCode;
            final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                    .getManager(IPortfolioDataManager.class.getName());
            if (portfolioDataManager != null && portfolioDataManager.isPortfolio(dtSecCode)) {
                if(StockUtil.isChineseStockB(dtSecCode)) {
                    BeaconJump.showPortfolioRemindActivity(mActivity, dtSecCode);
                } else {
                    BeaconJump.showSmartStockStareActivity(mActivity, dtSecCode, mName);
                }
            }
        } else {
            CommonBeaconJump.showLogin(mActivity);
        }
    }

    @Override
    public void onAdd() {
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager != null) {
            portfolioDataManager.addStock(mDtSecCode, mName);
        }

        refreshAlarmBar();
    }

    @Override
    public void onRemove() {
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager != null) {
            portfolioDataManager.modifyDeletePortfolio(mDtSecCode, true);
        }
        refreshAlarmBar();
    }

    @Override
    public void onMore() {
        if (mGuideLayout != null) {
            mGuideLayout.setVisibility(View.GONE);
            DataPref.setMoreOperationGuideClicked(true);
        }
    }

    @Override
    public void onBack() {
        mActivity.finish();
    }

    private void refreshAlarmBar() {
        mOperationBar.refreshAlarmBar();
    }

    private void openSecLiveMsgPage() {
        DengtaApplication.getApplication().getDataCacheManager().setHasUnreadLiveMsg(mDtSecCode, false);
        mOperationBar.showLiveMsgRedDot(false);

        WebBeaconJump.showStockLive(getActivity(), mDtSecCode, mName);

        StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_PAGE_DISPLAYED);
    }

    @Override
    public void updateQuoteView(final SecQuote quote) {
        if (quote == null) {
            return;
        }

        final int tpFlag = quote.iTpFlag;
        final float close = quote.fClose;
        ViewUtils.setQuoteView(mText11, mText21, quote);
        ViewUtils.setQuoteValueText(mText13, quote.fMax, close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, quote.fOpen, close, tpFlag);
        mText17.setText(StringUtil.getTurnOverRateString(quote.fFhsl));
        ViewUtils.setQuoteValueText(mText23, quote.fMin, close, tpFlag);
        mText25.setText(StringUtil.getAmountString(quote));
        mText26.setText(R.string.volume_ratio);
        mText27.setText(StringUtil.getVolumeRatioString(quote));

        if (!TextUtils.equals(mName, quote.getSSecName().trim())) {
            mName = quote.getSSecName();
            mActionBar.setTitleText(StringUtil.getActionBarTitle(mName, mShowSecCode));
        }
        mActionBar.setSubTitleText(StringUtil.getSecActionBarQuoteTime(quote.getITime()));
        mActionBar.setSubTitleAlternativeText(StringUtil.getActionBarSubTitleAlternative(quote));
    }

    @Override
    public void onLoadComplete() {
        mPtrFrame.refreshComplete();
    }

    @Override
    public void updateBaseInfoView(final SecBaseInfoRsp secBaseInfoRsp) {
        if (secBaseInfoRsp == null) {
            return;
        }

        if (!isAdded()) {
            return;
        }

        ArrayList<SecInfo> stockList = secBaseInfoRsp.getVSecInfo();
        if (stockList == null || stockList.size() == 0) {
            return;
        }

        SecInfo secInfo = stockList.get(0);

        Map<Integer, String> attrs = secInfo.getMSecAttr();
        Set<Integer> keySet = attrs.keySet();
        List<String> attrTags = new ArrayList<>();
        for (Integer attr : keySet) {
            if (!(attr == E_SEC_ATTR.E_SEC_ATTR_SHHK || attr == E_SEC_ATTR.E_SEC_ATTR_MARGIN || attr == E_SEC_ATTR.E_SEC_ATTR_SZHK)) {
                continue; //actionbar上只显示沪港通和融资融券的tag
            }

            //增加TAG的显示
            String attrStr = StringUtil.getAttrStr(attr);
            if (attrStr != null) {
                attrTags.add(attrStr);
            }
        }
        mActionBar.setAttrTags(attrTags);
        mOperationBar.setSecInfo(secInfo);
        updateFunctionBySecInfo(secInfo);

        ArrayList<SecBaseInfo> relatedSecBaseInfos = secInfo.getVRelateSecInfo();
        if (relatedSecBaseInfos == null || relatedSecBaseInfos.size() == 0) {
        } else {
            boolean isRelatedMarket = false;
            for (SecBaseInfo relatedSecBaseInfo : relatedSecBaseInfos) {
                String dtSecCode = relatedSecBaseInfo.getSDtSecCode();
                isRelatedMarket = StockUtil.isHongKongMarket(dtSecCode);
                if (isRelatedMarket) {
                    break;
                }
            }
            if (isRelatedMarket) {
                StatisticsUtil.reportAction(StatisticsConst.AH_PREMIUM_DISPLAYED);
            }
        }

        ArrayList<SecStatusInfo> statusInfos = secInfo.getVStatusInfo();
        if (statusInfos != null && statusInfos.size() > 0) {
            if (mStockStatusTipsLayout == null) {
                initStatusTipsLayout();
            }
            mStockStatusTipsLayout.setData(statusInfos, mDtSecCode, mName);
        }
    }

    @Override
    public void updateLiveMsgView(final SecLiveMsgRsp rsp) {
        final ArrayList<SecLiveMsg> secLiveMsgs = rsp.getVSecLiveMsg();
        final int liveMsgStatus = rsp.getILiveMsgStatus();
        DtLog.d(TAG, "updateLiveMsgView: secName = " + mName);

        if (liveMsgStatus == 1) { //当返回1且有小红点时，终端要清除小红点
            DengtaApplication.getApplication().getDataCacheManager().setHasUnreadLiveMsg(mDtSecCode, false);
            mOperationBar.showLiveMsgRedDot(false);
            return;
        }

        if (secLiveMsgs == null || secLiveMsgs.size() <= 0) {
            return;
        }

        final SecLiveMsg liveMsg = secLiveMsgs.get(0);
        mLastLiveMsgId = liveMsg.getSId();
        DtLog.d(TAG, "updateLiveMsgView: msg = " + liveMsg.getSMsg() + ", id = " + liveMsg.getSId() + ", secName = " + mName);

        DengtaApplication application = DengtaApplication.getApplication();
        String cachedLastMsgId = application.getDataCacheManager().getLastLiveMsgId(mDtSecCode);
        DtLog.d(TAG, "updateLiveMsgView: currentId = " + mLastLiveMsgId + ", cachedId = " + cachedLastMsgId);
        if (!TextUtils.equals(mLastLiveMsgId, cachedLastMsgId)) {
            application.getDataCacheManager().setLastLiveMsgId(mDtSecCode, mLastLiveMsgId);
            if (DengtaSettingPref.isStockLiveMsgEnabled()) {
                mUiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLiveMsgView.setData(liveMsg.getSMsg(), liveMsg.getESecLiveMsgType(), liveMsg.getITime(), LiveMessageView.LIVE_TYPE_SEC_DETAIL);
                    }
                }, 500);
            }

            application.getDataCacheManager().setHasUnreadLiveMsg(mDtSecCode, true);
            mOperationBar.showLiveMsgRedDot(true);
        }
    }

    @Override
    public void updateKLineQuoteView(final KLineInfosView.KLineLineTouchEvent event, final SecQuote quote) {
        if (quote == null) {
            return;
        }
        final int tpFlag = quote.iTpFlag;
        final float close = event.getYesterdayClose();
        final float now = event.getClose();
        if (now <= 0) {
            return;
        }
        ViewUtils.setQuoteView(mText11, mText21, close, now, tpFlag);
        ViewUtils.setQuoteValueText(mText13, event.getHigh(), close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, event.getOpen(), close, tpFlag);
        mText17.setText(StringUtil.getKLineTurnOverRateString(event.getVolume(), quote));
        ViewUtils.setQuoteValueText(mText23, event.getLow(), close, tpFlag);
        mText25.setText(StringUtil.getAmountString(event.getAmount()));
        String time;
        if (!event.isMinuteK()) {
            time = StringUtil.getFormattedDateString(event.getTimeStr());
        } else {
            time = StringUtil.getFormattedDateString(event.getTimeStr()) + " " + StringUtil.minuteToTime((int) event.getMinute());
        }
        mText26.setText(time);
        mText27.setText("");
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
            case R.id.quoteLayout:
                mPresenter.showHandicapDialog(activity, mName);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MORE_QUOTE_CLICKED);
                break;
            case R.id.setting:
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_FUNCTION_KLINE_SETTINGS);
                CommonBeaconJump.showKLineSetting(getContext());
                break;
            default:
                break;
        }
    }

    final class FunctionItemAdapter extends CommonBaseRecyclerViewAdapter<Integer> {

        public FunctionItemAdapter(Context context, int itemLayoutId) {
            super(context, null, itemLayoutId);

            setItemClickable(true);
        }

        public void setData(SparseArray<Integer> data) {
            super.setData(convertData(data));
            notifyDataSetChanged();
        }

        private List<Integer> convertData(SparseArray<Integer> datas) {
            List<Integer> list = new ArrayList<>(datas.size());
            for(int i=0; i<= 6; i++) {
                Integer data = datas.get(i);
                if(data != null) {
                    list.add(data);
                }
            }
            return list;
        }

        @Override
        public void convert(CommonRecyclerViewHolder holder, Integer textRes, int position) {
            final TextView textView = holder.getView(R.id.text);
            textView.setText(textRes);
        }

        @Override
        protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
            Integer textRes = getItem(position);
            switch (textRes) {
                case R.string.direction_add_nuggets:
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_FUNCTION_INVESTMENT_DIRECTION_ADD_NUGGETS);
                    WebBeaconJump.showDirectionAddDetail(mContext, mDtSecCode, mName);
                    break;

                case R.string.operation_margin_tracking:
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_FUNCTION_MARGIN_TRACKING);
                    WebBeaconJump.showMarginTracking(mContext, mDtSecCode, mName);
                    break;

                case R.string.operation_dk:
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_FUNCTION_BS_SIGNAL);
                    WebBeaconJump.showBS(mContext, mDtSecCode, mName);
                    break;

                case R.string.operation_similar_k:
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_FUNCTION_SIMILAR_KLINE);
                    WebBeaconJump.showSimilarKLine(mContext, mDtSecCode, mName);
                    break;

                case R.string.operation_history:
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_FUNCTION_SEC_HISTORY);
                    WebBeaconJump.showSecHistory(mContext, mDtSecCode, mName);
                    break;
                case R.string.operation_bullbear:
                    break;
            }
        }
    }
}
