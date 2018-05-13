package com.sscf.investment.information;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.sscf.investment.R;
import com.sscf.investment.information.presenter.MarketInfoPresenter;
import com.sscf.investment.message.manager.RecyclerViewManager;
import com.sscf.investment.information.view.ImageBannerView;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.dengtacj.component.entity.db.FavorItem;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.AttitudeColorView;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;
import com.sscf.investment.widget.recyclerview.RecyclerViewHelper;
import java.util.ArrayList;
import java.util.HashSet;
import BEC.BEACON_STAT_TYPE;
import BEC.BannerInfo;
import BEC.E_NEWS_FLAG;
import BEC.E_NEWS_TYPE;
import BEC.MarketAd;
import BEC.NewsDesc;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by liqf on 2015/10/20.
 */
public final class MarketInfoFragment extends BaseFragment implements View.OnClickListener,
        RecyclerViewManager.OnLoadMoreListener, PtrHandler, ImageBannerView.OnBannerClickListener {
    private static final String TAG = MarketInfoFragment.class.getSimpleName();

    public static final int UPDATE_INTERVAL = 15 * 60 * 1000; //页面切回来时如果超过15分钟就刷新一次

    private PtrFrameLayout mPtrLayout;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private NewsRecyclerViewAdapter mAdapter;

    private FrameLayout mHeaderView;
    private ImageBannerView mBannerView;

    private long mLastUpdateTime;

    private MarketInfoPresenter mPresenter;

    private RecyclerViewManager mRecyclerViewManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.common_ptr_recycler_view_with_state, container, false);
        initViews(root);
        mPresenter = new MarketInfoPresenter(this);
        return root;
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_MARKET_NEWS);
        helper.setKey(StatConsts.MARKET_NEWS_INFO);
        return helper;
    }

    private void initViews(final View root) {
        final Activity activity = getActivity();
        mPtrLayout = (PtrFrameLayout) root.findViewById(R.id.ptr);
        mPtrLayout.setPtrHandler(this);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new NewsRecyclerViewAdapter(activity);
        mRecyclerView.setAdapter(mAdapter);

        mHeaderView = new FrameLayout(activity);
        mAdapter.setHeaderView(mHeaderView);

        final View loadingLayoutCenter = root.findViewById(R.id.loading_layout);
        final View failRetryLayoutCenter = root.findViewById(R.id.fail_retry);
        failRetryLayoutCenter.setOnClickListener(this);
        final TextView emptyView = (TextView) root.findViewById(R.id.emptyView);
        emptyView.setText(R.string.no_content);

        mRecyclerViewManager = new RecyclerViewManager(activity, mRecyclerView, mLayoutManager,
                mAdapter, loadingLayoutCenter, failRetryLayoutCenter, emptyView, this);
        showLoadingLayout();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if (mRecyclerView.getVisibility() == View.VISIBLE) {
            return RecyclerViewHelper.isOnTop(mRecyclerView);
        }
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(final PtrFrameLayout frame) {
        requestData();
    }

    @Override
    public void onLoadMore() {
        mPresenter.requestMoreData();
    }

    @Override
    public void onFirstUserVisible() {
        DtLog.d(TAG, "onFirstUserVisible");
        super.onFirstUserVisible();
        requestData();
        StatisticsUtil.reportAction(StatisticsConst.INFORMATION_TAB_MARKET_INFO_DISPLAYED);
    }

    @Override
    public void onUserVisible() {
        DtLog.d(TAG, "onUserVisible");
        super.onUserVisible();
        final long now = SystemClock.elapsedRealtime();
        if (now - mLastUpdateTime > UPDATE_INTERVAL) {
            new Handler().postDelayed(() -> goToTopAndDoRefresh(), 50);
        }
        StatisticsUtil.reportAction(StatisticsConst.INFORMATION_TAB_MARKET_INFO_DISPLAYED);
        if (mBannerView != null) {
            mBannerView.startLooperPic();
        }
    }

    private void goToTopAndDoRefresh() {
        if (mAdapter.getNormalItemCount() > 0) {
            mLayoutManager.scrollToPosition(0);
            mPtrLayout.autoRefresh();
        } else {
            showLoadingLayout();
            requestData();
        }
    }

    @Override
    public void onFirstUserInvisible() {
        DtLog.d(TAG, "onFirstUserInvisible");
        super.onFirstUserInvisible();
        if (mBannerView != null) {
            mBannerView.stopLooperPic();
        }
    }

    @Override
    public void onUserInvisible() {
        DtLog.d(TAG, "onUserInvisible");
        super.onUserInvisible();
        if (mBannerView != null) {
            mBannerView.stopLooperPic();
        }
    }

    private void requestData() {
        mLastUpdateTime = SystemClock.elapsedRealtime();
        mPresenter.requestData();
    }

    public void showLoadingLayout() {
        DtLog.d(TAG, "showLoadingLayout");
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_LOADING);
    }

    public void showRetryLayout() {
        DtLog.d(TAG, "showRetryLayout");
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
    }

    public void showEmptyLayout() {
        DtLog.d(TAG, "showEmptyLayout");
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NO_CONTENT);
    }

    public void showFooterRetryLayout() {
        DtLog.d(TAG, "showFooterRetryLayout");
        mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
    }

    public void showFooterNoMoreLayout() {
        DtLog.d(TAG, "showFooterNoMoreLayout");
        mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
    }

    public void refreshComplete() {
        mPtrLayout.refreshComplete();
    }

    public void updateList(final ArrayList<Object> list) {
        DtLog.d(TAG, "updateList list : " + list);
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NORMAL);
        mAdapter.setListData(list);
        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<BannerInfo> mBannerInfos;

    public void refreshBannerLayout(ArrayList<BannerInfo> bannerInfos) {
        DtLog.d(TAG, "refreshBannerLayout bannerInfos : " + bannerInfos);
        final Activity activity = getActivity();
        if (activity == null || bannerInfos.size() == 0) {
            return;
        }

        mBannerInfos = bannerInfos;
        ImageBannerView bannerView = mBannerView;
        if (bannerView == null) {
            LayoutInflater.from(activity).inflate(R.layout.image_banner_layout, mHeaderView, true);
            bannerView = (ImageBannerView) mHeaderView.findViewById(R.id.image_banner_layout);
            final ViewGroup.LayoutParams params = bannerView.getLayoutParams();
            params.height = DeviceUtil.dip2px(activity, 144);
            bannerView.setLayoutParams(params);
            mBannerView = bannerView;
        }
        bannerView.setData(getImageUrls(bannerInfos));
        bannerView.setOnBannerClickListener(this);
        StatisticsUtil.reportAction(StatisticsConst.INFORMATION_MARKET_INFO_BANNER_DISPLAYED);
    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fail_retry:
                showLoadingLayout();
                requestData();
                break;
            default:
                break;
        }
    }
}

class NewsRecyclerViewAdapter extends CommonRecyclerViewAdapter {
    private HashSet<String> mClickedNewsIDs = new HashSet<>();
    private HashSet<String> mClickedAds = new HashSet<>();

    @BindColor(R.color.default_text_color_60) int mColorBase;
    @BindColor(R.color.default_text_color_100) int mColorTitle;
    @BindColor(R.color.default_text_color_60) int mColorSummary;

    private static final int TYPE_NEWS = 1;
    private static final int TYPE_AD_1 = 2;
    private static final int TYPE_AD_2 = 3;

    public NewsRecyclerViewAdapter(final Activity activity) {
        super(activity);
        ButterKnife.bind(this, activity);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case TYPE_AD_1:
                return new AdHolder(mInflater.inflate(R.layout.infomation_market_info_ad_item_1, parent, false));
            case TYPE_AD_2:
                return new AdHolder(mInflater.inflate(R.layout.infomation_market_info_ad_item_2, parent, false));
            case TYPE_NEWS:
            default:
                return new ViewHolder(mInflater.inflate(R.layout.discover_news_item, parent, false));
        }
    }

    @Override
    protected int getNormalViewType(int position) {
        final Object item = getItemData(position);
        if (item instanceof MarketAd) {
            final int adType = ((MarketAd)item).iStyle;
            return adType == 1 ? TYPE_AD_1 : TYPE_AD_2;
        }
        return TYPE_NEWS;
    }

    private static void openNormalNewsPage(final Context context, final NewsDesc item) {
        final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
        long accountId = 0;
        if (accountInfo != null) {
            accountId = accountInfo.id;
        }

        final FavorItem favorItem = new FavorItem(accountId, item.sNewsID, E_NEWS_TYPE.NT_DISC_NEWS, item.sTitle,
                item.sDtInfoUrl, DengtaConst.MILLIS_FOR_SECOND * item.iTime);

        WebBeaconJump.showWebActivity(context, item.getSDtInfoUrl(), favorItem);
    }

    final class AdHolder extends CommonViewHolder {
        @BindView(R.id.title) TextView mTitle;
        @BindView(R.id.image) ImageView mImage;

        public AdHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            final MarketAd ad = (MarketAd) mItemData;
            final String title = ad.sTitle;
            mTitle.setText(title);
            mTitle.setTextColor(mClickedAds.contains(title) ? mColorBase : mColorTitle);
            final String imageUrl = ad.sImgUrl;
            mImage.setImageResource(R.color.default_background);
            if (!TextUtils.isEmpty(imageUrl)) {
                ImageLoaderUtils.getImageLoader().displayImage(imageUrl, mImage);
            }
        }

        @Override
        public void onItemClicked() {
            final MarketAd marketAd = ((MarketAd) mItemData);
            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            if (scheme != null) {
                scheme.handleUrl(mContext, marketAd.sUrl);
            }
            mClickedAds.add(marketAd.sTitle);
            mTitle.setTextColor(mColorBase);
            StatisticsUtil.reportAction(StatisticsConst.INFORMATION_MARKET_INFO_AD_CLICKED);
        }
    }

    final class ViewHolder extends CommonViewHolder {
        @BindView(R.id.title) TextView mTitle;
        @BindView(R.id.summary) TextView mSummary;
        @BindView(R.id.news_time) TextView mTime;
        @BindView(R.id.news_source) TextView mSource;
        @BindView(R.id.attitude_view) AttitudeColorView mAttitude;
        @BindView(R.id.tag) TextView mTagView;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            NewsDesc newsDesc = (NewsDesc) itemData;

            mTitle.setText(newsDesc.getSTitle());
            if (mClickedNewsIDs.contains(newsDesc.getSNewsID())) {
                mTitle.setTextColor(mColorBase);
                mSummary.setTextColor(mColorBase);
            } else {
                mTitle.setTextColor(mColorTitle);
                mSummary.setTextColor(mColorSummary);
            }

            String description = newsDesc.getSDescription();
            if (!TextUtils.isEmpty(description)) {
                mSummary.setText(description);
                mSummary.setVisibility(View.VISIBLE);
            } else {
                mSummary.setVisibility(View.GONE);
            }

            mSource.setText(newsDesc.getSFrom());

            if (newsDesc.eNewsFlag == E_NEWS_FLAG.E_NF_TOPIC) {
                mAttitude.setVisibility(View.GONE);
                mTagView.setVisibility(View.VISIBLE);
                mTagView.setBackgroundResource(R.drawable.tag_topic_round_rect_bg_position);
                mTagView.setTextColor(mContext.getResources().getColor(R.color.white_100));
                mTagView.setText(R.string.market_info_tag_topic);
                mTime.setVisibility(View.GONE);
                mSource.setText(R.string.market_info_topic_source);
            } else if(newsDesc.eNewsFlag == E_NEWS_FLAG.E_NF_TOP) {
                mAttitude.setVisibility(View.VISIBLE);
                mAttitude.setData(newsDesc.getVtTagInfo());
                mTagView.setBackgroundResource(R.drawable.tag_top_round_rect_bg_position);
                mTagView.setTextColor(mContext.getResources().getColor(R.color.link_color));
                mTagView.setText(R.string.market_info_tag_top);
                mTagView.setVisibility(View.VISIBLE);
                mTime.setVisibility(View.GONE);
            } else {
                mAttitude.setVisibility(View.VISIBLE);
                mAttitude.setData(newsDesc.getVtTagInfo());
                mTagView.setVisibility(View.GONE);
                mTime.setVisibility(View.VISIBLE);
                mTime.setText(TimeUtils.timeStamp2Date(newsDesc.getITime() * 1000L));
            }
        }

        @Override
        public void onItemClicked() {
            final NewsDesc item = (NewsDesc) mItemData;
            if (item.eNewsFlag == E_NEWS_FLAG.E_NF_TOPIC) {
                WebBeaconJump.showCommonWebActivity(mContext, item.sDtInfoUrl);
            } else {
                openNormalNewsPage(mContext, item);
            }

            mClickedNewsIDs.add(item.sNewsID);
            mTitle.setTextColor(mColorBase);
            mSummary.setTextColor(mColorBase);

            StatisticsUtil.reportAction(StatisticsConst.INFORMATION_MARKET_INFO_NEWS_CLICKED);
        }
    }
}