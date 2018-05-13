package com.sscf.investment.social;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LongSparseArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chenenyu.router.annotation.Route;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TabLayout;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.social.manager.InvestmentAdviserListManager;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import java.util.ArrayList;
import BEC.FeedInvestListHead;
import BEC.GetInvestListRsp;

/**
 * Created by davidwei on 2016-12-06.
 * 投顾榜单
 */
@Route("InvestmentAdviserListActivity")
public final class InvestmentAdviserListActivity extends BaseFragmentActivity implements View.OnClickListener, Handler.Callback,
        ViewPager.OnPageChangeListener, TabLayout.OnTabSelectionListener, DataSourceProxy.IRequestCallback {
    private static final int MSG_UPDATE_LIST = 1;
    private static final int MSG_SHOW_VIEW_BY_STATE = 2;
    private long mAccountId;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private InvestmentAdviserListManager[] mAdapters;

    private View mLoadingLayout;
    private View mRetryLayout;

    private int mState;

    private final LongSparseArray<Boolean> mAttentionsStates = new LongSparseArray<Boolean>();

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_investment_adviser_list);
        mAccountId = DengtaApplication.getApplication().getAccountManager().getAccountId();
        mHandler = new Handler(this);
        initViews();
        requestData();
        StatisticsUtil.reportAction(StatisticsConst.INVESTMENT_ADVISER_LIST_DISPLAY);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this, true);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.investment_adviser_list);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        mLoadingLayout = findViewById(R.id.loading_layout);
        mRetryLayout = findViewById(R.id.fail_retry);
        mRetryLayout.setOnClickListener(this);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setOnTabSelectionListener(this);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final long accountId = DengtaApplication.getApplication().getAccountManager().getAccountId();
        if (mAccountId != accountId) {
            mAccountId = accountId;
            final InvestmentAdviserListManager[] adapters = mAdapters;
            final int length = adapters == null ? 0 : adapters.length;
            for (int i = 0; i < length; i++) {
                adapters[i].requestData("");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.fail_retry:
                requestData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabSelected(int index) {
        mViewPager.setCurrentItem(index);
        if (mAdapters != null) {
            mAdapters[index].notifyDataSetChanged();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mTabLayout.switchTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void requestData() {
        showViewByState(DengtaConst.UI_STATE_LOADING);
        FeedRequestManager.getInvestmentAdviserListTitleRequest(this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success && data.getEntity() != null) {
            final GetInvestListRsp rsp = (GetInvestListRsp) data.getEntity();
            mHandler.obtainMessage(MSG_UPDATE_LIST, rsp).sendToTarget();
        } else {
            showViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_LIST:
                showViewByState(DengtaConst.UI_STATE_NORMAL);
                updateTitle((GetInvestListRsp) msg.obj);
                break;
            case MSG_SHOW_VIEW_BY_STATE:
                showViewByState(msg.arg1);
                break;
            default:
                break;
        }

        return true;
    }

    private void updateTitle(final GetInvestListRsp rsp) {
        final ArrayList<FeedInvestListHead> heads = rsp.vHead;
        final int length = heads == null ? 0 : heads.size();
        if (length <= 0) {
            return;
        }

        if (mAdapters == null) {
            final String[] titles = new String[length];
            for (int i = 0; i < length; i++) {
                titles[i] = heads.get(i).sListName;
            }

            final InvestmentAdviserListManager[] adapters = new InvestmentAdviserListManager[length];
            InvestmentAdviserListManager adapter = null;
            for (int i = 0; i < length; i++) {
                adapter = new InvestmentAdviserListManager(this, mAttentionsStates, rsp.vHead.get(i).iListType, mAccountId);
                adapters[i] = adapter;
                if (i == 0) {
                    final EntityObject data = new EntityObject();
                    data.setExtra("");
                    data.setEntity(rsp);
                    adapters[i].updateList(data);
                } else {
                    adapter.requestData();
                }
            }

            mViewPager.setAdapter(new InvestmentAdviserPagerAdapter(adapters));
            mTabLayout.initWithTitles(titles, titles);
            mTabLayout.switchTab(0);
            mAdapters = adapters;
        }
    }

    public void showViewByState(int state) {
        if (ThreadUtils.isMainThread()) {
            showViewByStateOnUi(state);
        } else {
            mHandler.obtainMessage(MSG_SHOW_VIEW_BY_STATE, state, 0).sendToTarget();
        }
    }

    private void showViewByStateOnUi(int state) {
        if (mState == state) {
            return;
        }
        mState = state;
        switch (state) {
            case DengtaConst.UI_STATE_NORMAL:
                mLoadingLayout.setVisibility(View.GONE);
                mRetryLayout.setVisibility(View.GONE);
                mTabLayout.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.VISIBLE);
                break;
            case DengtaConst.UI_STATE_LOADING:
                mLoadingLayout.setVisibility(View.VISIBLE);
                mRetryLayout.setVisibility(View.GONE);
                mTabLayout.setVisibility(View.GONE);
                mViewPager.setVisibility(View.GONE);
                break;
            case DengtaConst.UI_STATE_FAILED_RETRY:
                mLoadingLayout.setVisibility(View.GONE);
                mRetryLayout.setVisibility(View.VISIBLE);
                mTabLayout.setVisibility(View.GONE);
                mViewPager.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}

final class InvestmentAdviserPagerAdapter extends PagerAdapter {
    final InvestmentAdviserListManager[] mManagers;
    InvestmentAdviserPagerAdapter(final InvestmentAdviserListManager[] managers) {
        mManagers = managers;
    }

    @Override
    public int getCount() {
        return mManagers.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View pager = mManagers[position].getRootView();
        container.addView(pager);
        return pager;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mManagers[position].getRootView());
    }
}
