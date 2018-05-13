package com.sscf.investment.portfolio;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseActivity;
import com.sscf.investment.sdk.main.manager.CallbackManager;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;

import BEC.BEACON_STAT_TYPE;

/**
 * Created by xuebinliu on 2015/7/22.
 *
 * 自选主界面界面
 */
public final class TabFragmentPortfolio extends PortfolioOrMarketSubFragment implements View.OnClickListener, Handler.Callback {
    private static final String TAG = TabFragmentPortfolio.class.getSimpleName();


    private int mCurrentIndex = -1;
    private static final int EMPTY_FRAGMENT_ID = 0;
    private static final int STOCK_FRAGMENT_ID = 1;
    private Fragment[] mFragments = new Fragment[2];



    public static final int MSG_REFESH_FRAGMENT = 3;        // 更新Fragment

    // 用于刷新行情
    private Handler mUpdateHandler;

    // 自选数据重新加载通知，场景:启动、用户登录、从服务器拉到自选数据
    private CallbackManager.DtCallback mPortfolioChangeCallback = new CallbackManager.DtCallback() {
        @Override
        public void onDataChange(Object obj) {
            DtLog.d(TAG, "mPortfolioChangeCallback onDataChange");
            if (mUpdateHandler != null) {
                mUpdateHandler.sendEmptyMessage(MSG_REFESH_FRAGMENT);
            }
        }
    };

    /**
     * 行情刷新
     */
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_REFESH_FRAGMENT:
                DtLog.d(TAG, "handleMessage MSG_REFESH_FRAGMENT");
                if (getUserVisibleHint()) {
                    displayFragment();
                }
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DtLog.d(TAG, "onCreate");
        mUpdateHandler = new Handler(Looper.getMainLooper(), this);

        mFragments[EMPTY_FRAGMENT_ID] = new PortfolioEmptyFragment();
        mFragments[STOCK_FRAGMENT_ID] = new PortfolioStockFragment();
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        return new TimeStatHelper(BEACON_STAT_TYPE.E_BST_PORTFOLIO_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DtLog.d(TAG, "onCreateView");
        final View root = inflater.inflate(R.layout.tab_fragment_portfolio, null, false);
        CallbackManager.getInstance().regCallback(CallbackManager.CM_TYPE_PORTFOLIO_DATA_CHANGE, mPortfolioChangeCallback);

        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setChildUserVisibleHint(!hidden);
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        displayFragment();
        setChildUserVisibleHint(true);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        displayFragment();
    }

    public void displayFragment() {
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        String groupName = "";
        boolean isEmpty = true;
        if (portfolioDataManager != null) {
            groupName = portfolioDataManager.getCurrentGroupName();
            isEmpty = portfolioDataManager.isEmptyFromCurrentGroup();
        }

        setGroupTitle(groupName);

        if (isEmpty) {
            switchFragment(EMPTY_FRAGMENT_ID);
        } else {
            switchFragment(STOCK_FRAGMENT_ID);
            StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_DISPLAY);
        }
    }

    private void switchFragment(final int index) {
        if (mCurrentIndex != index) {
            showFragment(index, mCurrentIndex);
            mCurrentIndex = index;
        }
    }

    private void showFragment(int currentIndex, int lastIndex) {
        DtLog.d(TAG, "showFragment : currentIndex = " + currentIndex + " , lastIndex = " + lastIndex);
        final BaseActivity activity = (BaseActivity) getActivity();
        if (activity == null || activity.isDestroy()) {
            DtLog.d(TAG, "showFragment : activity destory");
            return;
        }
        final FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null) {
            // monkey 跑出来这里会为null
            DtLog.w(TAG, "getFragmentManager null");
            return;
        }

        final FragmentTransaction ft = fragmentManager.beginTransaction();

        if (lastIndex >= 0) {
            final Fragment lastFragment = mFragments[lastIndex];
            if (lastFragment.isAdded()) {
                ft.hide(lastFragment);
            }
        }

        final Fragment currentFragment = mFragments[currentIndex];
        if (currentFragment.isAdded()) {
            ft.show(currentFragment);
        } else {
            ft.add(R.id.portfolio_content, currentFragment);
            currentFragment.setUserVisibleHint(true);
        }
        ft.commitAllowingStateLoss();
    }

    private void setChildUserVisibleHint(boolean isVisibleToUser) {
        if (mCurrentIndex > -1 && mCurrentIndex < mFragments.length) {
            final Fragment currentFragment = mFragments[mCurrentIndex];
            if (currentFragment.isAdded()) {
                currentFragment.setUserVisibleHint(isVisibleToUser);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DtLog.d(TAG, "onDestroyView");
        CallbackManager.getInstance().unRegCallback(CallbackManager.CM_TYPE_PORTFOLIO_DATA_CHANGE, mPortfolioChangeCallback);
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
//            case R.id.group_title_layout:
//                PortfolioGroupManagerActivity.show(activity);
//                break;
//            case R.id.actionbar_portfolio_search:
//                CommonBeaconJump.showSearch(activity);
//                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_CLICK_SEARCH);
//                break;
//            case R.id.actionbar_live:
//                WebBeaconJump.showPortfolioLive(activity);
//                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_CLICK_LIVE);
//                break;
//            case R.id.intelligent_answer_layout:
//                final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
//                final boolean state = redDotManager.getIntelligentAnswerRedDotState();
//                if (state) {
//                    redDotManager.setIntelligentAnswerRedDotState(false);
//                }
//                WebBeaconJump.showIntelligentAnswer(activity, state);
//                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_INTELLIGENT_ANSWER_CLICKED);
//                break;
            default:
                break;
        }
    }
}
