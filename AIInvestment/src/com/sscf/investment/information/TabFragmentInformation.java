package com.sscf.investment.information;

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

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.sscf.investment.R;
import com.sscf.investment.common.entity.AdEntity;
import com.sscf.investment.detail.view.ScrollableTabLayout;
import com.sscf.investment.main.DataPref;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.manager.AdManager;
import com.sscf.investment.main.manager.AdRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.search.SearchActivity;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.setting.manager.RedDotManager;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.teacherYan.AnalysisMarketFragment;
import com.sscf.investment.teacherYan.MorningRefFragment;
import com.sscf.investment.teacherYan.TeacherVideoFragment;
import com.sscf.investment.teacherYan.TeacherYanFragment;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.CommonFragmentPagerAdapter;
import java.io.File;
import BEC.DtActivityDetail;

/**
 * Created by liqf on 2015/10/20. 资讯
 */
public final class TabFragmentInformation extends BaseFragment implements View.OnClickListener, DataSourceProxy.IRequestCallback,
        CommonFragmentPagerAdapter.FragmentFactoryCallback {
    private static final String TAG_TEACHER_YAN = "teacher_yan";
    private static final String TAG_VIDEO = "video";
    private static final String TAG_MORNING_REFERENCE = "morning_reference";
    private static final String TAG_NARRATE_MARKET = "narrate_market";
    private static final String TAG_MARKET_INFO = "market_info";
    private static final String TAG_RECOMMEND = "recommend";
    private static final String TAG_HOT_SPOT = "hot_spot";
    private static final String TAG_CONSULTANT_OPINION = "consultant_opinion";
    private static final String TAG_FLASH_NEWS = "flash_news";

    private ViewPager mViewPager;
    private ScrollableTabLayout mTabLayout;
    private CommonFragmentPagerAdapter mPagerAdapter;
    private int[] mTitles;
    private String[] mTags;
    private View mIntelligentAnswerLayout;
    private View mIntellgentAnswerRedDotView;
    private View mGuideLayout;

    private ImageView mAdView;
    private AdEntity mAdEntity;
    private File mAdFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.tab_fragment_information, container, false);
        enableStatusSet();

        mAdView = (ImageView) contextView.findViewById(R.id.ad);
        mAdView.setOnClickListener(this);
        mIntelligentAnswerLayout = contextView.findViewById(R.id.intelligent_answer_layout);
        mIntelligentAnswerLayout.setOnClickListener(this);
        final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
        mIntellgentAnswerRedDotView = contextView.findViewById(R.id.intellgentAnswerRedDot);
        mIntellgentAnswerRedDotView.setVisibility(redDotManager.getIntelligentAnswerRedDotState() ? View.VISIBLE : View.GONE);
        registerBroadcastReceiver();

        contextView.findViewById(R.id.actionbar_portfolio_search).setOnClickListener(this);

        mTitles = new int[]{R.string.discover_teacher_yan, R.string.discover_video,
                R.string.discover_market_info,R.string.hot_spot,
                R.string.discover_morning_reference, R.string.discover_narrate_market,
                R.string.discover_flash_news};
        mTags = new String[]{TAG_TEACHER_YAN, TAG_VIDEO,TAG_MARKET_INFO,
                TAG_HOT_SPOT,
                TAG_MORNING_REFERENCE,
                TAG_NARRATE_MARKET, TAG_FLASH_NEWS};

        mViewPager = (ViewPager) contextView.findViewById(R.id.viewpager);
        mPagerAdapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), mTitles.length, this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                mTabLayout.refreshSelectedTab(index);
                mTabLayout.setCenterByTag(mTags[index]);
                updateGuideState(index);
            }
        });

        mTabLayout = (ScrollableTabLayout) contextView.findViewById(R.id.tab_layout);
        mTabLayout.initWithTitles(mTitles, mTags);
        mTabLayout.switchTab(0);
        mTabLayout.setOnTabSelectionListener(index -> {
            mViewPager.setCurrentItem(index);
            updateGuideState(index);
            mTabLayout.setCenterByTag(mTags[index]);
            if (index == 0) {
                StatisticsUtil.reportAction(StatisticsConst.A_NEWS_YANDASHI_CLICK);
            }else if (index == 1) {
                StatisticsUtil.reportAction(StatisticsConst.A_NEWS_VIDEO_CLICK);
            }else if (index == 3) {
                StatisticsUtil.reportAction(StatisticsConst.A_NEWS_HOTSPOT);
            }else if (index == 4){
                StatisticsUtil.reportAction(StatisticsConst.A_NEWS_BREAKER_CLICK);
            }else if (index == 5) {
                StatisticsUtil.reportAction(StatisticsConst.A_NEWS_JIEPAN_CLICK);
            }
        });

        return contextView;
    }

    private void updateGuideState(int index) {
        if(index == 3) {
            setGuideClicked();
        }
    }

    private void setGuideClicked() {
        if(mGuideLayout != null) {
            mGuideLayout.setVisibility(View.GONE);
            DataPref.setVideoTabGuideClicked(true);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mPagerAdapter.setChildUserVisibleHint(!hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcastReceiver();
        mViewPager.clearOnPageChangeListeners();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        if (mAdEntity == null) {
            AdRequestManager.requestInfoFlowAd(this);
        }
    }


    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (mAdEntity == null) {
            AdRequestManager.requestInfoFlowAd(this);
        }
    }

    @Override
    public Fragment createFragment(final int index) {
        Fragment fragment = null;
        switch (index) {
            case 0:
                fragment = new TeacherYanFragment();
                break;
            case 1:
                fragment = new TeacherVideoFragment();
                break;
            case 2:
                fragment = new MarketInfoFragment();
                break;
            case 3:
                fragment = new HotSpotFragment();
                break;

            case 4:
                fragment = new MorningRefFragment();
                break;
            case 5:
                fragment = new AnalysisMarketFragment();
                break;
            case 6:
                fragment = new FlashNewsFragment();
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
            case R.id.intelligent_answer_layout://晓智机器人
                final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
                final boolean state = redDotManager.getIntelligentAnswerRedDotState();
                if (state) {
                    redDotManager.setIntelligentAnswerRedDotState(false);
                }
                WebBeaconJump.showIntelligentAnswer(getContext(), state);
                StatisticsUtil.reportAction(StatisticsConst.INFORMATION_INTELLIGENT_ANSWER_CLICKED);
                break;
            case R.id.actionbar_portfolio_search:
                StatisticsUtil.reportAction(StatisticsConst.INFORMATION_SEARCH_CLICKED);
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
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

    private TabFragmentInformation.LocalBroadcastReceiver mReceiver;

    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new TabFragmentInformation.LocalBroadcastReceiver();
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
                        StatisticsUtil.reportAction(StatisticsConst.INFORMATION_FLOAT_AD_DISPLAY);
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
            file = new File(context.getFilesDir(), "ad/infomation.bat");
            mAdFile = file;
        }
        return file;
    }
}
