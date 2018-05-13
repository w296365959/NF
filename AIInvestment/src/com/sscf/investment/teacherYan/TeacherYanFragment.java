package com.sscf.investment.teacherYan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.TabLayout;
import com.sscf.investment.detail.FragmentFactory;
import com.sscf.investment.information.view.ImageBannerView;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.teacherYan.presenter.TeacherYanBannerPresent;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.BeaconPtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import BEC.BannerInfo;
import BEC.E_SCENE_TYPE;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by LEN on 2018/4/19.
 */

public class TeacherYanFragment extends BaseFragment implements PtrHandler,
        TeacherYanBannerPresent.OnGetBannerCallback, ImageBannerView.OnBannerClickListener{

    private final static String TAG = "TeacherYanFragment";

    private TabLayout mArticleTabLayout;

    private ArrayList<Fragment> mTabFragmentList = new ArrayList<>();

    private BeaconPtrFrameLayout mPtrFrame;
    private NestedScrollView mScrollView;

    private String mCurrentTag;

    private TeacherYanBannerPresent mPresent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_teacher_yan, container, false);
        mPresent = new TeacherYanBannerPresent(E_SCENE_TYPE.E_ST_YYYDS, this);
        initView(contextView);
        return contextView;
    }

    @SuppressLint("NewApi")
    private void initView(View view){
        mBannerView = (ImageBannerView) view.findViewById(R.id.image_banner_layout);
        mArticleTabLayout = (TabLayout) view.findViewById(R.id.article_tabs);
        initArticleTabLayout(getChildFragmentManager(), mArticleTabLayout);
        mArticleTabLayout.switchTab(0);
        mPtrFrame = (BeaconPtrFrameLayout) view.findViewById(R.id.ptr);
        mPtrFrame.setPtrHandler(this);
        mPtrFrame.setSupportDisallowInterceptTouchEvent(true);
        mScrollView = (NestedScrollView) view.findViewById(R.id.scrollview);
        mScrollView.setNestedScrollingEnabled(false);
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getScrollY() == 0){
                    mPtrFrame.requestDisallowInterceptTouchEvent(false);
                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    ((OnPullRefresh)getDisplayFragment()).onScrollToBottom();
                }
            }
        });
    }

    private void initArticleTabLayout(final FragmentManager fragmentManager, final TabLayout articleTabLayout) {
        articleTabLayout.initWithTitles(
                new int[]{R.string.tab_yan_say, R.string.tab_yan_answer, R.string.tab_yan_curse, R.string.tab_yan_teacher},

                new String[]{FragmentFactory.TEACHER_YAN_WORD, FragmentFactory.TEACHER_YAN_TALKFREE,
                        FragmentFactory.TEACHER_YAN_CURSE, FragmentFactory.TEACHER_YAN_ARTICLE});
        articleTabLayout.setOnTabSelectionListener((index) ->{

                String tag = articleTabLayout.getTagByIndex(index);
                if (tag.equals(FragmentFactory.TEACHER_YAN_WORD)) {
                    StatisticsUtil.reportAction(StatisticsConst.A_NEWS_YANDASHI_SPEECH_CLICK);
                }else if (tag.equals(FragmentFactory.TEACHER_YAN_TALKFREE)) {
                    StatisticsUtil.reportAction(StatisticsConst.A_NEWS_YANDASHI_ANSWER_CLICK);
                }else if (tag.equals(FragmentFactory.TEACHER_YAN_CURSE)) {
                    StatisticsUtil.reportAction(StatisticsConst.A_NEWS_YANDASHI_COURSE_CLICK);
                }else if (tag.equals(FragmentFactory.TEACHER_YAN_ARTICLE)) {
                    StatisticsUtil.reportAction(StatisticsConst.A_NEWS_YANDASHI_EDUCATION_CLICK);
                }
                replaceFragmentByTag(fragmentManager, tag, articleTabLayout.getCurrentTag(), R.id.article_fragment_container);
        });

        articleTabLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                articleTabLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }

    private void replaceFragmentByTag(FragmentManager fragmentManager, String tag, String currentTag, int containerId) {
        if (!isAdded() || (getActivity() != null && ((BaseFragmentActivity) getActivity()).isDestroy())) {
            return;
        }

        mCurrentTag = currentTag;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment currentFragment = findFragmentByTag(mCurrentTag);
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = findFragmentByTag(tag);
        if (fragment == null) {
            fragment = FragmentFactory.createFragment(tag);
            mTabFragmentList.add(fragment);
            if (!fragment.isAdded()) {
                fragment.setUserVisibleHint(getUserVisibleHint());
                fragmentTransaction.add(containerId, fragment, tag);
            }
        }

        showTab(fragmentTransaction, tag, mTabFragmentList);

        fragmentTransaction.commitAllowingStateLoss();
    }

    private Fragment getDisplayFragment() {
        return findFragmentByTag(mCurrentTag);
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
    public void onUserVisible() {
        super.onUserVisible();
        if (null != mBannerView) {
            mBannerView.startLooperPic();
        }
        final Fragment currentFragment = getDisplayFragment();
        currentFragment.setUserVisibleHint(true);
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        if (null != mBannerView) {
            mBannerView.stopLooperPic();
        }
        final Fragment currentFragment = getDisplayFragment();
        currentFragment.setUserVisibleHint(false);
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        mPresent.requestData();
        final Fragment currentFragment = getDisplayFragment();
        if (null != currentFragment)
            currentFragment.setUserVisibleHint(true);
    }

    @Override
    public void onFirstUserInvisible() {
        super.onFirstUserInvisible();
        final Fragment currentFragment = getDisplayFragment();
        if (null != currentFragment)
            currentFragment.setUserVisibleHint(false);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(final PtrFrameLayout frame) {
        doRefresh();
    }

    private void onLoadComplete() {
        mPtrFrame.refreshComplete();
    }

    private void doRefresh() {
        mPresent.requestData();
        ((OnPullRefresh)findFragmentByTag(mCurrentTag)).onPullRefresh();
    }

    @Override
    public void onGetBannerCallback(ArrayList<BannerInfo> bannerInfos) {
        DtLog.e(TAG, "onGetBannerCallback");
        onLoadComplete();
        final Activity activity = getActivity();
        if (activity == null || bannerInfos.size() == 0) {
            return;
        }

        mBannerInfos = bannerInfos;
        if (mBannerView != null) {
            final ViewGroup.LayoutParams params = mBannerView.getLayoutParams();
            params.height = DeviceUtil.dip2px(activity, 144);
            mBannerView.setLayoutParams(params);
        }
        mBannerView.setData(getImageUrls(bannerInfos));
        mBannerView.setOnBannerClickListener(this);
    }

    public interface OnPullRefresh {
        void onPullRefresh();
        void onScrollToBottom();
    }

    private ArrayList<BannerInfo> mBannerInfos;
    private ImageBannerView mBannerView;

    String[] getImageUrls(final ArrayList<BannerInfo> bannerInfos) {
        String[] imageUrls = null;
        if (bannerInfos != null) {
            final int size = bannerInfos.size();
            imageUrls = new String[size];
            BannerInfo bannerInfo = null;
            for (int i = 0; i < size; i++) {
                bannerInfo = bannerInfos.get(i);
                if (bannerInfo != null) {
                    imageUrls[i] = bannerInfo.sImgUrl;
                }
            }
        }
        return imageUrls;
    }

    @Override
    public void onBannerClick(int position) {
        final int size = mBannerInfos == null ? 0 : mBannerInfos.size();
        if (position < size) {
            final BannerInfo bannerInfo = mBannerInfos.get(position);
            if (bannerInfo != null) {
                final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                if (scheme != null) {
                    scheme.handleUrl(getContext(), bannerInfo.sSkippUrl);
                }
                StatisticsUtil.reportAction(StatisticsConst.INFORMATION_MARKET_INFO_BANNER_CLICKED);
            }
        }
    }
}
