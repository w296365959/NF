package com.sscf.investment.social;

import android.os.Bundle;
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
import com.sscf.investment.social.manager.FriendsListManager;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;

/**
 * davidwei
 * 好友界面
 */
@Route("FriendsActivity")
public final class FriendsActivity extends BaseFragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener,
        TabLayout.OnTabSelectionListener {
    private long mAccountId;

    private final int[] TITLES = new int[] {R.string.attention, R.string.fans};
    private final String[] TITLES_TAGS = new String[] {"attention", "fans"};
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FriendsListManager[] mAdapters;

    private final LongSparseArray<Boolean> mAttentionsStates = new LongSparseArray<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final long accountId = getIntent().getLongExtra(DengtaConst.EXTRA_ID, -1);
        if (accountId <= 0) {
            finish();
            return;
        }

        mAccountId = accountId;
        setContentView(R.layout.social_friends_list);
        initViews();
        requestData();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this, true);
    }

    private void initViews() {
        if (mAccountId == DengtaApplication.getApplication().getAccountManager().getAccountId()) {
            ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.my_friends);
        } else {
            ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.ta_friends);
        }
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        final int length = TITLES.length;

        final FriendsListManager[] adapters = new FriendsListManager[length];
        for (int i = 0; i < length; i++) {
            adapters[i] = new FriendsListManager(this, mAttentionsStates, i, mAccountId);
        }

        mAdapters = adapters;
        mViewPager.setAdapter(new FriendsPagerAdapter(adapters));
        mViewPager.addOnPageChangeListener(this);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.initWithTitles(TITLES, TITLES_TAGS);
        mTabLayout.setOnTabSelectionListener(this);
        mTabLayout.switchTab(getIntent().getIntExtra(DengtaConst.EXTRA_TYPE, 0));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(this);
        }
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
            default:
                break;
        }
    }

    @Override
    public void onTabSelected(int index) {
        mViewPager.setCurrentItem(index);
        mAdapters[index].notifyDataSetChanged();
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
        mAdapters[0].requestData();
        mAdapters[1].requestData();
    }
}

final class FriendsPagerAdapter extends PagerAdapter {
    final FriendsListManager[] mFriendsListManagers;
    FriendsPagerAdapter(final FriendsListManager[] friendsListManagers) {
        mFriendsListManagers = friendsListManagers;
    }

    @Override
    public int getCount() {
        return mFriendsListManagers.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View pager = mFriendsListManagers[position].getRootView();
        container.addView(pager);
        return pager;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mFriendsListManagers[position].getRootView());
    }
}
