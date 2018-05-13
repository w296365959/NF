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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.detail.presenter.SecurityDetailPresenter;
import com.sscf.investment.detail.view.ISecurityDetailView;
import com.sscf.investment.detail.FragmentFactory;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.detail.SecurityDetailActivity;
import com.sscf.investment.detail.view.DetailActionBar;
import com.sscf.investment.detail.view.KLineInfosView;
import com.sscf.investment.detail.view.MemoLayout;
import com.sscf.investment.detail.view.OperationBar;
import com.sscf.investment.component.ui.widget.TabLayout;
import com.sscf.investment.main.DataPref;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.portfolio.PortfolioRemindActivity;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.CommonConst;
import com.dengtacj.component.entity.ShareParams;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.utils.ViewUtils;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.BeaconPtrFrameLayout;
import com.sscf.investment.widget.FlowScrollView;
import com.sscf.investment.socialize.ShareDialog;
import java.util.ArrayList;
import BEC.BEACON_STAT_TYPE;
import BEC.E_NEMS_GET_SOURCE;
import BEC.E_NEWS_TYPE;
import BEC.SecQuote;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by liqf on 2015/9/14
 *
 */
public final class FundDetailFragment extends SecurityDetailFragment implements DetailActionBar.OnOperationListener, OperationBar.OnOperationListener,
        ISecurityDetailView, View.OnClickListener {
    private static final String TAG = FundDetailFragment.class.getSimpleName();

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

    private Handler mUiHandler = new Handler();
    private ArrayList<Fragment> mTabFragmentList = new ArrayList<>();

    private boolean mTabInited;

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
                    DtLog.d(TAG, "onReceive: TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED tradingState = " + tradingState);
                    queryTradingValuesAndRefresh(false);
                }
            } else if (TradingStateManager.ACTION_TRADING_STATE_UPDATED.equals(action)){
                queryTradingValuesAndRefresh(true);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.main_fund_detail, container, false);
        mContextView = contextView;
        mActivity = (SecurityDetailActivity) getActivity();

        initViews();

        preloadQuoteData();

        mPresenter = new SecurityDetailPresenter(this, mLineChartFragment, mDtSecCode);
        // 预加载使用
        mPresenter.refreshDelay(100);
        return contextView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.release();
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new  TimeStatHelper(BEACON_STAT_TYPE.E_BST_SEC_DETAIL_PAGE);
        helper.setKey(StatConsts.OTHER_STOCK);
        return helper;
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
        if (mTabInited) {
            switchTabIfNeeded(false);
        }

        StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_SHOW_OTHER_STOCK);

        mActionBar.setContentScrollX(0);

        queryTradingValuesAndRefresh(true);

        if (!mIsTrading) { //即使不在交易时间，无论如何要拉取一次历史的行情数据
            mPresenter.refresh();
        }

        refreshAlarmBar();

        updateMemo();
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

    private void switchTabIfNeeded(final boolean force) {
        String tabTag = DataPref.getFundTabTag();
        final TabLayout fundTabLayout = (TabLayout) mContextView.findViewById(R.id.fund_tabs);
        if (!TextUtils.equals(tabTag, fundTabLayout.getCurrentTag()) || force) {
            fundTabLayout.switchTab(tabTag);
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

        mScrollView.setOnScrollListener(new FlowScrollView.OnScrollListener() {
            @Override
            public void onScrollChanged(int scollY) {
                if (scollY == 0) {
                    mPtrFrame.requestDisallowInterceptTouchEvent(false);
                }
                mActionBar.showSubTitleAlternative(scollY >= mQuoteLayout.getBottom());
            }
        });

        initLineChartFragment();

        final FragmentManager fragmentManager = getChildFragmentManager();

        final TabLayout fundTabLayout = (TabLayout) mContextView.findViewById(R.id.fund_tabs);
        final TabLayout fundTabLayoutSticky = (TabLayout) mContextView.findViewById(R.id.fund_tabs_sticky);
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initTabLayout(fragmentManager, fundTabLayout, fundTabLayoutSticky);
                initTabLayout(fragmentManager, fundTabLayoutSticky, fundTabLayout);
                switchTabIfNeeded(true);
                mTabInited = true;
            }
        }, INIT_TAB_DELAY);

        mActionBar = (DetailActionBar) mContextView.findViewById(R.id.detail_actionbar);
        mActionBar.setDtSecCode(mDtSecCode, mName);
        mActionBar.setOnOperationListener(this);
        mActionBar.setHasSubTitle(true);
        mActionBar.setTitleText(StringUtil.getActionBarTitle(mName, mShowSecCode));

        showSecTypeTitle();

        mOperationBar = (OperationBar) mContextView.findViewById(R.id.operation_bar);
        mOperationBar.setDtSecCode(mDtSecCode, mName);
        mOperationBar.setOnOperationListener(this);

        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        final boolean isAdded = portfolioDataManager == null ? false : portfolioDataManager.isPortfolio(mDtSecCode);
        mOperationBar.setIsAdded(isAdded);
        mOperationBar.showLiveMsgRedDot(DengtaApplication.getApplication().getDataCacheManager().hasUnreadLiveMsg(mDtSecCode));
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

        final TabLayout tabLayout = (TabLayout) mContextView.findViewById(R.id.fund_tabs);
        Fragment currentFragment = findFragmentByTag(tabLayout.getCurrentTag());
        if (currentFragment != null && currentFragment.isAdded() && currentFragment instanceof OnReloadDataListener) {
            ((OnReloadDataListener) currentFragment).onReloadData();
        }
    }

    private void preloadQuoteData() {
        final SecQuote quote = DengtaApplication.getApplication().getDataCacheManager().getSecQuote(mDtSecCode);
        updateQuoteView(quote);
    }

    private void showSecTypeTitle() {
        ArrayList<String> tags = new ArrayList<>();
        String typeString = StockUtil.getTypeString(mActivity, mDtSecCode);
        if (!TextUtils.isEmpty(typeString)) {
            tags.add(typeString);
            mActionBar.setAttrTags(tags);
        }
    }

    private void initFlowView() {
        final View fundTabContainerSticky = mContextView.findViewById(R.id.fund_tabs_sticky_container);
        final View fundTabContainer = mContextView.findViewById(R.id.fund_tabs_container);
        mScrollView.setFlowLayers(fundTabContainerSticky, fundTabContainer);
    }

    private int getFlowViewTop() {
        final View articleTabLayoutContainer = mContextView.findViewById(R.id.fund_tabs_container);
        return articleTabLayoutContainer.getTop();
    }

    private void initTabLayout(final FragmentManager fragmentManager, final TabLayout tabLayout, final TabLayout stickyLayout) {
        tabLayout.initWithTitles(new int[]{R.string.tab_fund_notice, R.string.tab_fund_intro}, new String[]{FragmentFactory.NOTICE, FragmentFactory.FUND_INTRO});
        tabLayout.setOnTabSelectionListener(new TabLayout.OnTabSelectionListener() {
            @Override
            public void onTabSelected(int index) {
                String tag = tabLayout.getTagByIndex(index);
                DataPref.setFundTabTag(tag);

                replaceFragmentByTag(fragmentManager, tag, tabLayout.getCurrentTag(), R.id.fund_fragment_container);
                stickyLayout.refreshSelectedTab(index);

                int flowViewTop = getFlowViewTop();
                int scrollY = mScrollView.getScrollY();
                if (scrollY > flowViewTop) {
                    mScrollView.setScrollY(flowViewTop);
                }
            }
        });
    }

    private void replaceFragmentByTag(FragmentManager fragmentManager, String tag, String currentTag, int containerId) {
        if (!isAdded() || (getActivity() != null && ((BaseFragmentActivity) getActivity()).isDestroy())) {
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = findFragmentByTag(tag);

        if (fragment == null) {
            fragment = FragmentFactory.createFragment(tag);
            Bundle bundle;
            if (FragmentFactory.FUND_INTRO.equals(tag)) {
                bundle = new Bundle();
                bundle.putString(CommonConst.KEY_SEC_CODE, mDtSecCode);
                fragment.setArguments(bundle);
            } else if (FragmentFactory.NOTICE.equals(tag)) {
                bundle = new Bundle();
                bundle.putString(CommonConst.KEY_SEC_CODE, mDtSecCode);
                bundle.putInt(CommonConst.KEY_NEWS_TYPE, E_NEWS_TYPE.NT_ANNOUNCEMENT);
                bundle.putInt(CommonConst.KEY_GET_SOURCE, E_NEMS_GET_SOURCE._E_STOCK_MARKET_GET);
                bundle.putString(CommonConst.KEY_SEC_NAME, mName);
                fragment.setArguments(bundle);
            }
            mTabFragmentList.add(fragment);
        }

        if (!fragment.isAdded()) {
            fragmentTransaction.add(containerId, fragment, tag);
        }

        showTab(fragmentTransaction, tag);

        fragmentTransaction.commitAllowingStateLoss();
    }

    private void showTab(FragmentTransaction ft, String currentTag){
        for (Fragment fragment : mTabFragmentList) {
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
    public void onBack() {
        mActivity.finish();
    }

    @Override
    public void onLiveMsg() {
        openSecLiveMsgPage();
    }

    @Override
    public void onShare() {
        if (getShareParams() != null) {
            getShareDialog().showShareDialog(getShareParams());
        }
    }

    @Override
    public void onFigure() {

    }

    @Override
    public void onChip() {
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

            final String stockDetailUrl = DengtaApplication.getApplication().getUrlManager().getFundDetailUrl(mDtSecCode, mName);
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
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();

        if (dengtaApplication.getAccountManager().isLogined()) {
            final String unicode = mDtSecCode;
            final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                    .getManager(IPortfolioDataManager.class.getName());
            if (portfolioDataManager != null && portfolioDataManager.isPortfolio(unicode)) {
                final Intent intent = new Intent(mActivity, PortfolioRemindActivity.class);
                intent.putExtra(CommonConst.KEY_SEC_CODE, unicode);
                startActivity(intent);
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
    }

    private void refreshAlarmBar() {
        mOperationBar.refreshAlarmBar();
    }

    private void openSecLiveMsgPage() {
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
                mPresenter.showFundQuoteDialog(activity);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MORE_QUOTE_CLICKED);
                break;
            default:
                break;
        }
    }
}
