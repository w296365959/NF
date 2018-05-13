package com.sscf.investment.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import com.chenenyu.router.annotation.Route;
import com.sscf.investment.R;
import com.sscf.investment.detail.fragment.DengtaAIndexDetailFragment;
import com.sscf.investment.detail.fragment.FundDetailFragment;
import com.sscf.investment.detail.fragment.HongKongDetailFragment;
import com.sscf.investment.detail.fragment.IndexDetailFragment;
import com.sscf.investment.detail.fragment.IndexFutureDetailFragment;
import com.sscf.investment.detail.fragment.NewThirdBoardDetailFragment;
import com.sscf.investment.detail.fragment.PlateDetailFragment;
import com.sscf.investment.detail.fragment.SecurityDetailFragment;
import com.sscf.investment.detail.fragment.StockDetailFragment;
import com.sscf.investment.detail.fragment.USADetailFragment;
import com.sscf.investment.detail.view.ShakeGuideDialog;
import com.sscf.investment.main.DataPref;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.FixedSpeedScroller;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import java.lang.reflect.Field;
import java.util.ArrayList;
import BEC.E_MARKET_TYPE;
import BEC.E_SEC_TYPE;

/**
 * Created by liqf on 2015/11/4.
 */
@Route("SecurityDetailActivity")
public final class SecurityDetailActivity extends BaseFragmentActivity {
    private static final String TAG = SecurityDetailActivity.class.getSimpleName();

    private ViewPager mViewPager;

    public boolean mClickSearch;

    private String mCurrentDtSecCode;
    private String mCurrentDtSecName;
    private ArrayList<SecListItem> mSecItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
        DtLog.d(TAG, "onCreate");
        final Intent intent = getIntent();

        final String dtSecCode = intent.getStringExtra(DengtaConst.KEY_SEC_CODE);
        if (TextUtils.isEmpty(dtSecCode)) {
            finish();
            return;
        }

        mSecItems = intent.getParcelableArrayListExtra(DengtaConst.KEY_SEC_LIST);
        if (mSecItems == null) {
            finish();
            return;
        }

        final String secName = intent.getStringExtra(DengtaConst.KEY_SEC_NAME);

        DengtaApplication.getApplication().mKLineZoomRatioPortrait = 1.0f;

        mCurrentDtSecCode = dtSecCode;
        mCurrentDtSecName = secName;

        setContentView(R.layout.main_activity_security);
        initViews(dtSecCode);
        registerSearchReceiver();
    }

    private void initViews(String dtSecCode) {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setViewPagerScroller();

        final SecurityDetailFragmentManager adapter = new SecurityDetailFragmentManager(
                getSupportFragmentManager(), mSecItems, findViewById(R.id.detail_action_bar_flow));
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(getInitialItem(dtSecCode));
        mViewPager.addOnPageChangeListener(adapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DtLog.d(TAG, "onPostCreate");
        SwipeBackLayout.attachSwipeLayout(this, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DtLog.d(TAG, "onResume");
        mClickSearch = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        DtLog.d(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DtLog.d(TAG, "onDestroy");
        if (mViewPager != null) {
            mViewPager.clearOnPageChangeListeners();
        }
        DeviceUtil.fixInputMethodManagerLeak(this);

        DengtaApplication.getApplication().mKLineZoomRatioPortrait = 1.0f;
        unregisterSearchReceiver();
    }

    @Override
    public String getDtCode() {
        if (mCurrentDtSecCode == null) {
            return null;
        }

        boolean isChineseStock = isChineseStock();
        if (isChineseStock) {
            return mCurrentDtSecCode;
        } else {
            return null;
        }
    }

    @Override
    public String getSecName() {
        if (mCurrentDtSecCode == null) {
            return null;
        }

        boolean isChineseStock = isChineseStock();
        if (isChineseStock) {
            return mCurrentDtSecName;
        } else {
            return null;
        }
    }

    public ArrayList<SecListItem> getSecItems() {
        return mSecItems;
    }

    private boolean isChineseStock() {
        return StockUtil.isChineseMarket(mCurrentDtSecCode) && StockUtil.isStock(mCurrentDtSecCode);
    }

    private int getInitialItem(final String dtSecCode) {
        for (int i = 0; i < mSecItems.size(); i++) {
            SecListItem secItem = mSecItems.get(i);
            if (TextUtils.equals(secItem.getDtSecCode(), dtSecCode)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        switch (requestCode) {
            case DengtaConst.REQUEST_FULLSCREEN_LINECHART:
                String dtSecCode = data.getStringExtra(LineChartActivity.KEY_SEC_CODE);
                int secIndex = getSecIndex(dtSecCode);
                if (secIndex != -1) {
                    mViewPager.setCurrentItem(secIndex);
                }
                break;
            case DengtaConst.REQUEST_SEARCH_PICK:
                if(resultCode == RESULT_OK) {
                    String selectedDtSecCode = data.getStringExtra(CommonConst.KEY_SEC_CODE);
                    Bundle extra = data.getBundleExtra(DengtaConst.EXTRA_SEARCH_PICK);
                    if(extra != null) {
                        if(!TextUtils.isEmpty(selectedDtSecCode)) {
                            DataPref.setTimeLineCompareType(SettingConst.TIMELINE_COMPARE_TYPE_CUSTOM);
                            DataPref.setTimeLineCompareSecCode(selectedDtSecCode);
                            SecurityDetailFragmentManager fragmentManager = ((SecurityDetailFragmentManager) mViewPager.getAdapter());
                            if(fragmentManager != null) {
                                SparseArray<SecurityDetailFragment> fragments = fragmentManager.getFragments();
                                if(fragments != null) {
                                    for(int i=0, size=fragments.size(); i<size; i++) {
                                        SecurityDetailFragment fragment = fragments.valueAt(i);
                                        fragment.selectCustomStock(selectedDtSecCode);
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default:
        }
    }

    private int getSecIndex(String dtSecCode) {
        if(mSecItems != null && !mSecItems.isEmpty()) {
            for (int i = 0; i < mSecItems.size(); i++) {
                SecListItem item = mSecItems.get(i);
                String code = item.getDtSecCode();
                if (TextUtils.equals(code, dtSecCode)) {
                    return i;
                }
            }
        }

        return -1;
    }

    private void setViewPagerScroller() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(),
                new DecelerateInterpolator());
            field.set(mViewPager, scroller);
            scroller.setmDuration(100);
        } catch (Exception e) {
            DtLog.e(TAG, "", e);
        }
    }

    private BroadcastReceiver mReceiver;
    private void registerSearchReceiver() {
        if (mReceiver == null) {
            mReceiver = new SearchProcessReceiver();
            final IntentFilter intentFilter = new IntentFilter(DengtaConst.ACTION_SEARCH_TO_SECURITY_DETTAIL);
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, intentFilter);
        }
    }

    private void unregisterSearchReceiver() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private final class SearchProcessReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (DengtaConst.ACTION_SEARCH_TO_SECURITY_DETTAIL.equals(action)) {
                if (mClickSearch) {
                    finish();
                }
            }
        }
    }

    private final class SecurityDetailFragmentManager extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
        private final ArrayList<SecListItem> mSecItems;
        private final SparseArray<SecurityDetailFragment> mTabFragmentList;
        private final View mDetailActionBar;

        public SecurityDetailFragmentManager(FragmentManager fm, ArrayList<SecListItem> secItems, View detailActionBar) {
            super(fm);
            mSecItems = secItems;
            mTabFragmentList = new SparseArray<>(secItems.size());
            mDetailActionBar = detailActionBar;
        }

        @Override
        public Fragment getItem(int i) {
            final SecurityDetailFragment fragment = createFragment(mSecItems, i);
            mTabFragmentList.put(i, fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return mSecItems.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mTabFragmentList.remove(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset == 0) {
                mDetailActionBar.setVisibility(View.GONE);
            } else {
                mDetailActionBar.setVisibility(View.VISIBLE);
            }

            SecurityDetailFragment fragment = mTabFragmentList.get(position);
            if (fragment != null) {
                fragment.onPageScrolled(true, positionOffset);
            }

            fragment =  mTabFragmentList.get(position + 1);
            if (fragment != null) {
                fragment.onPageScrolled(false, -1 + positionOffset);
            }
        }

        public SparseArray<SecurityDetailFragment> getFragments() {
            return mTabFragmentList;
        }

        @Override
        public void onPageSelected(int index) {
            final int size = mTabFragmentList.size();
            DtLog.d(TAG, "onPageSelected: index = " + index);
            DtLog.d(TAG, "onPageSelected: mTabFragmentList.size() = " + size);

            final SecListItem item = mSecItems.get(index);
            mCurrentDtSecCode = item.getDtSecCode();
            mCurrentDtSecName = item.getName();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                final int size = mTabFragmentList.size();
                DtLog.d(TAG, "onPageScrollStateChanged: mTabFragmentList.size() = " + size);
                mDetailActionBar.setVisibility(View.GONE);
                for (int i = 0; i < size; i++) {
                    mTabFragmentList.valueAt(i).onPageScrollStateIdle();
                }
            }
        }
    }

    private static SecurityDetailFragment createFragment(ArrayList<SecListItem> secItems, int index) {
        final SecListItem secItem = secItems.get(index);
        final String dtSecCode = secItem.getDtSecCode();
        int secType = StockUtil.convertSecInfo(dtSecCode).getESecType();
        int marketType = StockUtil.getMarketType(dtSecCode);

        SecurityDetailFragment fragment;
        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
                // 股票
                switch (marketType) {
                    case E_MARKET_TYPE.E_MT_HK: //港股
                        fragment = new HongKongDetailFragment();
                        break;
                    case E_MARKET_TYPE.E_MT_NASDAQ: //纳斯达克
                    case E_MARKET_TYPE.E_MT_NYSE: //纽交所
                    case E_MARKET_TYPE.E_MT_AMEX: //美交所
                        fragment = new USADetailFragment();
                        break;
                    case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
                    case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                        fragment = new StockDetailFragment();
                        break;
                    case E_MARKET_TYPE.E_MT_TB:
                        fragment = new NewThirdBoardDetailFragment();
                        break;
                    default:
                        fragment = new StockDetailFragment();
                        break;
                }

                break;
            case E_SEC_TYPE.E_ST_FUND:
                // 基金
                fragment = new FundDetailFragment();
                break;
            case E_SEC_TYPE.E_ST_INDEX:                // 指数
                switch (marketType) {
                    case E_MARKET_TYPE.E_MT_DT:
                        fragment = new DengtaAIndexDetailFragment();
                        break;
                    default:
                        fragment = new IndexDetailFragment();
                        break;
                }
                break;
            case E_SEC_TYPE.E_ST_PLATE:                // 行业板块
                fragment = new PlateDetailFragment();
                break;
            case E_SEC_TYPE.E_ST_FUTURES:
                // 股指期货
                fragment = new IndexFutureDetailFragment();
                break;
            default:
                fragment = new StockDetailFragment();
                break;
        }

        final Bundle bundle = new Bundle();
        bundle.putString(DengtaConst.KEY_SEC_CODE, secItem.getDtSecCode());
        bundle.putString(DengtaConst.KEY_SEC_NAME, secItem.getName());
        fragment.setArguments(bundle);

        return fragment;
    }
}

