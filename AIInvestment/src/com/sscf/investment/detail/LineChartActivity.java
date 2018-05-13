package com.sscf.investment.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.animation.DecelerateInterpolator;

import com.sscf.investment.R;
import com.sscf.investment.detail.fragment.LandscapeLineChartFragment;
import com.sscf.investment.detail.view.LineChartTextureView;
import com.sscf.investment.main.DataPref;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.DepthPageTransformer;
import com.sscf.investment.widget.FixedSpeedScroller;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by liqf on 2015/8/25.
 */
public class LineChartActivity extends BaseFragmentActivity {
    private static final String TAG = LineChartActivity.class.getSimpleName();
    public static final String KEY_INDEX_IN_LIST = "index_in_list";
    public static final String KEY_LINE_TYPE = "line_type";
    public static final String KEY_SEC_CODE = "sec_code";
    public static final int DEFAULT_KLINE_LOAD_NUM = 310;
    public static final int DEFAULT_TIME_LINE_CAPITAL_NUM = 250;
    private FragmentManager mFragmentManager;
    private SparseArray<Fragment> mTabFragmentList = new SparseArray<>();
    private ViewPager mViewPager;
    private FragmentPagerAdapter mPagerAdapter;
    private int mLineType;
    private ArrayList<SecListItem> mSecItems;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);

        StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_SHOW_FULLSCREEN_CHART);

        DengtaApplication.getApplication().mKLineZoomRatioLandscape = 1.0f;

        Intent intent = getIntent();
        mSecItems = intent.getParcelableArrayListExtra(DengtaConst.KEY_SEC_LIST);

        setContentView(R.layout.main_activity_line_chart);

        mLineType = getLineType();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setViewPagerScroller();
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        mFragmentManager = getSupportFragmentManager();

        mPagerAdapter = new FragmentPagerAdapter(mFragmentManager) {
            @Override
            public Fragment getItem(int i) {
                DtLog.d(TAG, "getItem i = " + i);
                if (mSecItems == null) {
                    return null;
                }

                SecListItem secItem = mSecItems.get(i);
                String tagByIndex = secItem.getDtSecCode();
                Fragment fragment = mFragmentManager.findFragmentByTag(tagByIndex);
                if (fragment == null) {
                    fragment = createFragmentBySecItem(secItem, i);
                    mTabFragmentList.put(i, fragment);
                }

                return fragment;
            }

            @Override
            public int getCount() {
                return mSecItems == null ? 0 : mSecItems.size();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(getInitialItem());

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                DtLog.d(TAG, "onPageSelected: i = " + i);
                DtLog.d(TAG, "onPageSelected: mViewPager.getChildCount() = " + mViewPager.getChildCount());
                DtLog.d(TAG, "onPageSelected: mPagerAdapter.getCount() = " + mPagerAdapter.getCount());

                for (int index = 0; index < mTabFragmentList.size(); index++) {
                    int key = mTabFragmentList.keyAt(index);
                    LandscapeLineChartFragment fragment = (LandscapeLineChartFragment) mTabFragmentList.get(key);
                    fragment.onPageChanged(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    protected int[] getThemeResourceIds() {
        int[] themeIds = new int[2];
        themeIds[0] = R.style.theme_default_fullscreen;
        themeIds[1] = R.style.theme_night_fullscreen;
        return themeIds;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DengtaApplication.getApplication().mKLineZoomRatioLandscape = 1.0f;
    }

    private int getInitialItem() {
        if (mSecItems == null) {
            return 0;
        }

        for (int i = 0; i < mSecItems.size(); i++) {
            SecListItem secItem = mSecItems.get(i);
            if (TextUtils.equals(secItem.getDtSecCode(), getDtSecCode())) {
                return i;
            }
        }
        return 0;
    }

    public int getCurrentItem() {
        return mViewPager.getCurrentItem();
    }

    public int getPageCount() {
        return mPagerAdapter.getCount();
    }

    private Fragment createFragmentBySecItem(final SecListItem secItem, int index) {
        LandscapeLineChartFragment fragment = new LandscapeLineChartFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DengtaConst.KEY_SEC_CODE, secItem.getDtSecCode());
        bundle.putString(DengtaConst.KEY_SEC_NAME, secItem.getName());
        bundle.putInt(KEY_LINE_TYPE, mLineType);
        bundle.putInt(KEY_INDEX_IN_LIST, index);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onBackPressed() {
        int currentItem = mViewPager.getCurrentItem();
        Fragment fragment = mTabFragmentList.get(currentItem);
        if (fragment instanceof LandscapeLineChartFragment) {
            LandscapeLineChartFragment currentFragment = (LandscapeLineChartFragment) fragment;
            int lineType = currentFragment.getCurrentLineType();
            Intent intent = new Intent();
            intent.putExtra(KEY_LINE_TYPE, lineType);
            SecListItem secItem = mSecItems.get(currentItem);
            intent.putExtra(KEY_SEC_CODE, secItem.getDtSecCode());
            setResult(0, intent);
        }

        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void toLeft() {
        int currentItem = mViewPager.getCurrentItem();
        LandscapeLineChartFragment fragment = (LandscapeLineChartFragment) mTabFragmentList.get(currentItem);
        mLineType = fragment.getCurrentLineType();
        int leftItem = currentItem > 0 ? currentItem - 1 : 0;
        mViewPager.setCurrentItem(leftItem);
    }

    public void toRight() {
        int currentItem = mViewPager.getCurrentItem();
        LandscapeLineChartFragment fragment = (LandscapeLineChartFragment) mTabFragmentList.get(currentItem);
        mLineType = fragment.getCurrentLineType();
        int count = mPagerAdapter.getCount();
        int rightItem = currentItem >= count - 1 ? count - 1 : currentItem + 1;
        mViewPager.setCurrentItem(rightItem);
    }

    public String getDtSecCode() {
        Intent intent = getIntent();
        String secCode = intent.getStringExtra(DengtaConst.KEY_SEC_CODE);
        Log.d(TAG, "getDtSecCode secCode=" + secCode);
        return secCode;
    }

    public int getMarketType() {
        return StockUtil.convertSecInfo(getDtSecCode()).getEMarketType();
    }

    private int getLineType() {
        Intent intent = getIntent();
        return intent.getIntExtra(KEY_LINE_TYPE, LineChartTextureView.TYPE_TIME);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DengtaConst.REQUEST_SEARCH_PICK) {
            if(resultCode == RESULT_OK) {
                String dtSecCode = data.getStringExtra(CommonConst.KEY_SEC_CODE);
                if(!TextUtils.isEmpty(dtSecCode)) {
                    DataPref.setTimeLineCompareType(SettingConst.TIMELINE_COMPARE_TYPE_CUSTOM);
                    DataPref.setTimeLineCompareSecCode(dtSecCode);
                    for(int i=0, size=mTabFragmentList.size(); i<size; i++) {
                        Fragment fragment = mTabFragmentList.valueAt(i);
                        if(fragment != null && fragment instanceof LandscapeLineChartFragment) {
                            ((LandscapeLineChartFragment)fragment).selectCustomStock(dtSecCode);
                        }
                    }
                }
            }
        }
    }
}
