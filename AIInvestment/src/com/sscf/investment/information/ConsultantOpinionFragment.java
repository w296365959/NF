package com.sscf.investment.information;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.information.view.ConsultantOpinionHeader;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.social.FeedRecyclerViewAdapter;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.OnRefreshStateChangeListener;
import com.sscf.investment.widget.recyclerview.CommonFooterView;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;
import com.sscf.investment.widget.recyclerview.EndlessRecyclerOnScrollListener;
import com.sscf.investment.widget.recyclerview.RecyclerViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import BEC.BEACON_STAT_TYPE;
import BEC.E_FEED_GROUP_TYPE;
import BEC.E_FEED_INVEST_ATTI_TYPE;
import BEC.E_FEED_TYPE;
import BEC.E_FEED_USER_TYPE;
import BEC.FeedContent;
import BEC.FeedExtendInfo;
import BEC.FeedItem;
import BEC.FeedUserBaseInfo;
import BEC.GetFeedListReq;
import BEC.GetFeedListRsp;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by liqf on 2015/10/20.
 */
public class ConsultantOpinionFragment extends BaseFragment implements DataSourceProxy.IRequestCallback, OnReloadDataListener {
    private static final String TAG = ConsultantOpinionFragment.class.getSimpleName();

    public static final int REFRESH_TYPE_UP = 1;
    public static final int REFRESH_TYPE_DOWN = 2;

    private static final int DIRECTION_PULL_NEWER = 0;
    private static final int DIRECTION_PULL_OLDER = 1;

    private static final int STATUS_NO_MORE = 0;
    private static final int STATUS_HAS_MORE = 1;

    public static final int UPDATE_INTERVAL = 15 * 60 * 1000; //页面切回来时如果超过15分钟就刷新一次

    private PtrFrameLayout mPtrFrame;

    private Handler mUiHandler = new Handler();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ConsultantRecyclerViewAdapter mAdapter;

    @BindView(R.id.empty_view) View mEmptyView;
    @BindView(R.id.loading_layout) View mLoadingLayoutCenter;
    @BindView(R.id.fail_retry) View mFailRetryLayoutCenter;

    private ConsultantOpinionHeader mHeaderView;
    private CommonFooterView mFooterView;

    private int mViewState = DengtaConst.UI_STATE_NORMAL;

    private String mDtSecCode;

    private int mType = TYPE_SEC;

    /**
     * 拉取个股的投顾解读
     */
    public static final int TYPE_SEC = 0;

    /**
     * 拉取大盘的投顾解读
     */
    public static final int TYPE_MARKET = 1;

    private OnRefreshStateChangeListener mRefreshStateChangeListener;
    private long mLastUpdateTime;

    private ArrayList<FeedItem> mFeedList = new ArrayList<>();
    private ArrayList<FeedItem> mTopFeedItems;
    private Set<String> mTopFeedIds = new HashSet<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mDtSecCode = arguments.getString(DengtaConst.KEY_SEC_CODE);
            if (TextUtils.isEmpty(mDtSecCode)) {
                mType = TYPE_MARKET;
            }
        }

        final View contextView = inflater.inflate(R.layout.fragment_consultant_opinion, container, false);
        ButterKnife.bind(this, contextView);

        initViews(contextView);

        return contextView;
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_MARKET_NEWS);
        helper.setKey(StatConsts.MARKET_NEWS_CONSULTANT_OPINION);
        return helper;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        DtLog.d(TAG, "onResume");
        doReloadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initViews(View contextView) {
        mRecyclerView = (RecyclerView) contextView.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        mAdapter = new ConsultantRecyclerViewAdapter(getContext(), mRecyclerView);
//        mAdapter.setSecInfo(mDtSecCode, mSecName);
        mRecyclerView.setAdapter(mAdapter);

        if (mType == TYPE_MARKET) {
            initHeaderView();
        }
        initFooterView();

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                //加载更多功能的代码
                DtLog.d(TAG, "onScroll: onScrollStateChanged !!!need bottom refresh!!!");
                if (mViewState != DengtaConst.UI_STATE_NO_MORE_CONTENT) {
                    loadFeedList(REFRESH_TYPE_DOWN);
                }
            }
        });

        mPtrFrame = (PtrFrameLayout) contextView.findViewById(R.id.ptr);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (mRecyclerView.getVisibility() == View.VISIBLE) {
                    return RecyclerViewHelper.isOnTop(mRecyclerView);
                }
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                DtLog.d(TAG, "onRefreshBegin: onUIRefresh");
                doRefresh();
            }
        });
    }

    private void initHeaderView() {
        mHeaderView = (ConsultantOpinionHeader) View.inflate(getActivity(), R.layout.fragment_consultant_opinion_header, null);
        mHeaderView.setParentAdapter(mAdapter);
    }

    private void initFooterView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mFooterView = (CommonFooterView) inflater.inflate(R.layout.common_footer_view_layout, mRecyclerView, false);
        mFooterView.setFailRetryListener(() -> loadFeedList(REFRESH_TYPE_DOWN));
        mAdapter.setFooterView(mFooterView);
    }

    private void doRefresh() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            onLoadComplete(false);
            DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
            return;
        }

        loadFeedList(REFRESH_TYPE_UP);
        if (mHeaderView != null) {
            mHeaderView.requestData();
        }
    }

    @Override
    public void onFirstUserVisible() {
        DtLog.d(TAG, "onFirstUserVisible");
        super.onFirstUserVisible();
        doReloadData();
        StatisticsUtil.reportAction(StatisticsConst.INFORMATION_TAB_CONSULTANT_DISPLAYED);
    }

    @Override
    public void onUserVisible() {
        DtLog.d(TAG, "onUserVisible");
        super.onUserVisible();
        doReloadData();
        StatisticsUtil.reportAction(StatisticsConst.INFORMATION_TAB_CONSULTANT_DISPLAYED);
    }

    @Override
    public void onFirstUserInvisible() {
        DtLog.d(TAG, "onFirstUserInvisible");
        super.onFirstUserInvisible();
    }

    @Override
    public void onUserInvisible() {
        DtLog.d(TAG, "onUserInvisible");
        super.onUserInvisible();
    }

    private void doReloadData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            onLoadComplete(false);
            showViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
            return;
        }

        mUiHandler.postDelayed(() -> {
            long realtime = SystemClock.elapsedRealtime();
            if (realtime - mLastUpdateTime > UPDATE_INTERVAL) {
                goToTopAndDoRefresh();
            }
        }, 0); //必须延时，否则不能触发onRefresh执行
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
//            doReloadData();
        }
    }

    public void setOnRefreshCompleteListener(OnRefreshStateChangeListener listener) {
        mRefreshStateChangeListener = listener;
    }

    private void loadFeedList(final int refreshType) {
        DtLog.d(TAG, "loadFeedList: refreshType = " + refreshType);

        mLastUpdateTime = SystemClock.elapsedRealtime();

        if (mRefreshStateChangeListener != null) {
            mRefreshStateChangeListener.onRefreshStart();
        }

        if (refreshType == REFRESH_TYPE_DOWN) {
            showViewByState(DengtaConst.UI_STATE_LOADING);
        }

        String startId = "", newestId = "", oldestId = "";
        int count = mFeedList.size();
        if (count > 0) {
            newestId = mFeedList.get(0).getStFeedContent().getSFeedId();
            oldestId = mFeedList.get(count - 1).getStFeedContent().getSFeedId();
        }

        int direction = DIRECTION_PULL_NEWER;
        switch (refreshType) {
            case REFRESH_TYPE_UP:
                startId = /*newestId*/""; //如果顶部刷新不清缓存只需注释掉此行
                direction = DIRECTION_PULL_NEWER;
                break;
            case REFRESH_TYPE_DOWN:
                startId = oldestId;
                direction = DIRECTION_PULL_OLDER;
                break;
            default:
                break;
        }

        GetFeedListReq req = new GetFeedListReq();
        req.setESelfType(mType == TYPE_SEC ? E_FEED_TYPE.E_FT_INVEST_STOCK : E_FEED_TYPE.E_FT_INVEST_MARKET);
        req.setEFeedGroupType(E_FEED_GROUP_TYPE.E_FGT_SEC);
        req.setStUserInfo(DengtaApplication.getApplication().getAccountManager().getUserInfo());
        req.setSStartFeedId(startId);
        req.setIDirection(direction);
        req.setSDtSecCode(mType == TYPE_SEC ? mDtSecCode : DengtaConst.DENGTA_DT_CODE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(DengtaConst.KEY_REFRESH_TYPE, refreshType);
            jsonObject.put(DengtaConst.KEY_LAST_OLDEST_ID, oldestId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DataEngine.getInstance().request(EntityObject.ET_GET_FEED_LIST, req, this, jsonObject.toString());
    }

    @Override
    public void callback(boolean success, final EntityObject entity) {
        if (!success) {
            DtLog.w(TAG, "callback requestBriefInfo faild");
            if (entity != null) {
                int entityType = entity.getEntityType();
                if (entityType == EntityObject.ET_GET_FEED_LIST) {
                    onLoadComplete(false);
                }
            }
            return;
        }

        switch (entity.getEntityType()) {
            case EntityObject.ET_GET_FEED_LIST:
                onLoadComplete(true);

                final GetFeedListRsp rsp = (GetFeedListRsp) entity.getEntity();
                final String extra = (String) entity.getExtra();
                mUiHandler.post(() -> handleGetFeedList(rsp, extra));
                break;
            default:
                break;
        }
    }

    private void onLoadComplete(final boolean success) {
        mUiHandler.post(() -> {
            if (mRefreshStateChangeListener != null) {
                mRefreshStateChangeListener.onRefreshComplete();
            }

            mPtrFrame.refreshComplete();

            final boolean hasContent = mAdapter.getNormalItemCount() > 0;
            int viewState;
            if (hasContent) {
                viewState = success ? DengtaConst.UI_STATE_NORMAL : DengtaConst.UI_STATE_FAILED_RETRY;
            } else {
                viewState = success ? DengtaConst.UI_STATE_NO_CONTENT : DengtaConst.UI_STATE_FAILED_RETRY;
            }
            showViewByState(viewState);
        });
    }

    @UiThread
    private void handleGetFeedList(GetFeedListRsp rsp, String extra) {
        Integer refreshType = REFRESH_TYPE_DOWN;
        String lastOldestId = "";
        try {
            JSONObject jsonObject = new JSONObject(extra);
            refreshType = jsonObject.getInt(DengtaConst.KEY_REFRESH_TYPE);
            lastOldestId = jsonObject.getString(DengtaConst.KEY_LAST_OLDEST_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<FeedItem> feedItems = rsp.getVFeedItem();
        if (refreshType == REFRESH_TYPE_UP) {
            mTopFeedItems = rsp.getVTopFeedItem();
            filterDuplicatedItems(feedItems, mTopFeedItems);
            feedItems.addAll(0, mTopFeedItems);
        } else {
            filterDuplicatedItems(feedItems, mTopFeedItems);
        }
        int status = rsp.getIStatus();

        if (feedItems != null && feedItems.size() == 0) {
            if (refreshType == REFRESH_TYPE_DOWN) {
                showViewByState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
            } else if (refreshType == REFRESH_TYPE_UP) {
                if (mAdapter.getNormalItemCount() == 0) {
                    showViewByState(DengtaConst.UI_STATE_NO_CONTENT);
                }
            }
            return;
        }

        if (status == STATUS_NO_MORE) {
            showViewByState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
        }

        Map<Long, FeedUserBaseInfo> feedUserBaseInfos = rsp.getMFeedUserBaseInfo();
        mAdapter.mFeedUserBaseInfos.putAll(feedUserBaseInfos);

        int firstVisiblePosition = mLayoutManager.findFirstVisibleItemPosition();
        View v = mRecyclerView.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();

        if (refreshType == REFRESH_TYPE_UP) {
            if (status == STATUS_HAS_MORE) {
                mFeedList.clear();
                mFeedList.addAll(feedItems);
            } else if (status == STATUS_NO_MORE) {
                mFeedList.clear(); //如果顶部刷新不清缓存只需注释掉此行
                mFeedList.addAll(0, feedItems);
            }
            mAdapter.setListData(mFeedList);
            mRecyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        } else if (refreshType == REFRESH_TYPE_DOWN) { //下拉刷新，或者上拉刷新且新数据和缓存数据能衔接上时
            String oldestId = mFeedList.size() > 0 ? mFeedList.get(mFeedList.size() - 1).getStFeedContent().getSFeedId() : "";
            if (TextUtils.equals(lastOldestId, oldestId)) {
                mFeedList.addAll(feedItems);
                mAdapter.setListData(mFeedList);
                mAdapter.notifyDataSetChanged();
                mLayoutManager.scrollToPositionWithOffset(firstVisiblePosition, top);
            }
        }

        showViewByState(DengtaConst.UI_STATE_NORMAL);
    }

    private void filterDuplicatedItems(ArrayList<FeedItem> feedItems, ArrayList<FeedItem> topFeedItems) {
        if (topFeedItems == null || topFeedItems.size() == 0) {
            return;
        }
        mTopFeedIds.clear();
        ArrayList<String> topIds = new ArrayList<>();
        for (FeedItem topFeedItem : topFeedItems) {
            topIds.add(topFeedItem.getStFeedContent().getSFeedId());
            mTopFeedIds.add(topFeedItem.getStFeedContent().getSFeedId());
        }
        for (String topId : topIds) {
            for (Iterator iterator = feedItems.iterator();iterator.hasNext();) {
                FeedItem feedItem = (FeedItem) iterator.next();
                String feedId = feedItem.getStFeedContent().getSFeedId();
                if (TextUtils.equals(feedId, topId)) {
                    feedItems.remove(feedItem);
                    break;
                }
            }
        }
    }

    private void showViewByState(int state) {
        DtLog.d(TAG, "showViewByState: state = " + state);
        final boolean hasContent = mAdapter.getNormalItemCount() > 0;

        switch (state) {
            case DengtaConst.UI_STATE_NORMAL:
                if (mViewState != DengtaConst.UI_STATE_NORMAL) {
                    mPtrFrame.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mFailRetryLayoutCenter.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.GONE);
                    mLoadingLayoutCenter.setVisibility(View.GONE);
                    if (hasContent) {
                        mFooterView.setState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
                    } else {
                        mFooterView.setState(DengtaConst.UI_STATE_NO_CONTENT);
                    }
                }
                break;
            case DengtaConst.UI_STATE_LOADING:
                mEmptyView.setVisibility(View.GONE);
                mFailRetryLayoutCenter.setVisibility(View.GONE);
                if (hasContent) {
                    mFooterView.setState(DengtaConst.UI_STATE_LOADING);
                    mRecyclerView.scrollToPosition(mAdapter.getNormalItemCount() - 1);
                } else {
                    mPtrFrame.setVisibility(View.GONE);
                    mLoadingLayoutCenter.setVisibility(View.VISIBLE);
                }
                break;
            case DengtaConst.UI_STATE_NO_MORE_CONTENT:
                mPtrFrame.setVisibility(View.VISIBLE);
                mFooterView.setState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
                mEmptyView.setVisibility(View.GONE);
                break;
            case DengtaConst.UI_STATE_FAILED_RETRY:
                mEmptyView.setVisibility(View.GONE);
                mLoadingLayoutCenter.setVisibility(View.GONE);
                if (hasContent) {
                    mFooterView.setState(DengtaConst.UI_STATE_FAILED_RETRY);
                } else {
                    mFailRetryLayoutCenter.setVisibility(View.VISIBLE);
                }
                break;
            case DengtaConst.UI_STATE_NO_CONTENT:
                mEmptyView.setVisibility(View.VISIBLE);
                mPtrFrame.setVisibility(View.GONE);
                mFailRetryLayoutCenter.setVisibility(View.GONE);
                mLoadingLayoutCenter.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        mViewState = state;
    }

    @OnClick(R.id.fail_retry)
    public void onFailRetryClicked() {
        loadFeedList(REFRESH_TYPE_UP);
        if (mHeaderView != null) {
            mHeaderView.requestData();
        }
    }

    @Override
    public void onReloadData() {
        goToTopAndDoRefresh();
    }

    private void goToTopAndDoRefresh() {
        mLayoutManager.scrollToPosition(0);
        mPtrFrame.autoRefresh();
    }

    public class ConsultantRecyclerViewAdapter extends CommonRecyclerViewAdapter {
        public static final int MAX_SUMMARY_LENGTH = 60;
        private ArrayList<String> mClickedNewsIDs = new ArrayList<>();

        private final Context mContext;
        private LayoutInflater mInflater;

        private int mColorBase;
        private int mColorTitle;
        private int mColorSummary;
        @BindDrawable(R.drawable.attitude_rise) Drawable mAttiDrawableGood;
        @BindDrawable(R.drawable.attitude_down) Drawable mAttiDrawableBad;
        private DisplayImageOptions mDefaultOptions;

        private RecyclerView mRecyclerView;
        public Map<Long, FeedUserBaseInfo> mFeedUserBaseInfos = new HashMap<>();

        private String mTopicTagStr;
        private ArrayList<String> mTagList = new ArrayList<>();

        public ConsultantRecyclerViewAdapter(final Context context, RecyclerView recyclerView) {
            super(context);
            mContext = context;
            mRecyclerView = recyclerView;
            ButterKnife.bind(this, mRecyclerView);
            mDefaultOptions = ImageLoaderUtils.buildDisplayImageOptions(R.drawable.default_consultant_face);

            initResources();
        }

        private void initResources() {
            mColorTitle = ContextCompat.getColor(mContext, R.color.default_text_color_100);
            mColorSummary = ContextCompat.getColor(mContext, R.color.default_text_color_60);
            mColorBase = ContextCompat.getColor(mContext, R.color.default_text_color_60);

            mTopicTagStr = getResources().getString(R.string.market_info_tag_topic);
            mTagList.add(mTopicTagStr);
        }

        @Override
        protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(mContext);
            }
            return new ViewHolder(mInflater.inflate(R.layout.consultant_opinion_item, parent, false));
        }

        class ViewHolder extends CommonViewHolder {
            @BindView(R.id.user_icon) public ImageView mUserIcon;
            @BindView(R.id.user_icon_v) ImageView mUserIconV;
            @BindView(R.id.title) public TextView mTitle;
            @BindView(R.id.summary) public TextView mSummary;
            @BindView(R.id.name) public TextView mUserName;
            @BindView(R.id.time) public TextView mTime;
            @BindView(R.id.page_view_count) public TextView mPageViewCount;
            @BindView(R.id.tag) public TextView mTag;

            public ViewHolder(View itemView) {
                super(itemView);
            }

            @Override
            public void bindData(Object itemData) {
                super.bindData(itemData);
                FeedItem feedItem = (FeedItem) itemData;
                FeedContent feedContent = feedItem.getStFeedContent();
                String feedId = feedContent.getSFeedId();
                long pubAccountId = feedContent.getIPubAccountId();
                FeedUserBaseInfo userBaseInfo = mFeedUserBaseInfos.get(pubAccountId);
                FeedExtendInfo feedExtendInfo = feedItem.getStFeedExtendInfo();

                mTitle.setText(feedContent.getSTitle());

                SpannableString summaryString = getSummaryString(feedContent);
                mSummary.setText(summaryString);
                mSummary.setVisibility(TextUtils.isEmpty(summaryString) ? View.GONE : View.VISIBLE);

                if (mClickedNewsIDs.contains(feedId)) {
                    mTitle.setTextColor(mColorBase);
                } else {
                    mTitle.setTextColor(mColorTitle);
                }

                if (userBaseInfo != null) {
                    mUserName.setText(userBaseInfo.getSNickName());
                    String faceUrl = userBaseInfo.getSFaceUrl();
                    ImageLoaderUtils.getImageLoader().displayImage(faceUrl, mUserIcon, mDefaultOptions);
                    int userType = userBaseInfo.getEUserType();
                    if (userType == E_FEED_USER_TYPE.E_FEED_USER_INVEST_V) {
                        mUserIconV.setVisibility(View.VISIBLE);
                    } else {
                        mUserIconV.setVisibility(View.GONE);
                    }
                } else {
                    mUserName.setText("");
                    mUserIcon.setImageResource(R.drawable.default_consultant_face);
                    mUserIconV.setVisibility(View.GONE);
                }

                mTime.setText(TimeUtils.timeStamp2Date(feedContent.getIPubTime() * 1000L));

                int accessCount = feedExtendInfo.getIAccessCount();
                mPageViewCount.setText(getString(R.string.consultant_page_view_count, accessCount));

                if(mTopFeedIds.contains(feedItem.getStFeedContent().getSFeedId())) {
                    mTag.setVisibility(View.VISIBLE);
                } else {
                    mTag.setVisibility(View.GONE);
                }
            }

            @Override
            public void onItemClicked() {
                super.onItemClicked();
                FeedItem feedItem = (FeedItem) mItemData;
                FeedContent feedContent = feedItem.getStFeedContent();
                FeedExtendInfo feedExtendInfo = feedItem.getStFeedExtendInfo();

                mClickedNewsIDs.add(feedContent.getSFeedId());
                mTitle.setTextColor(mColorBase);
                mSummary.setTextColor(mColorBase);

                int accessCount = feedExtendInfo.getIAccessCount();
                accessCount++;
                feedExtendInfo.setIAccessCount(accessCount);
                mPageViewCount.setText(getString(R.string.consultant_page_view_count, accessCount));

                //打开投顾详情页面
                WebBeaconJump.showCommonWebActivity(mContext, DengtaApplication.getApplication().getUrlManager().getOpinionDetailUrl(feedContent.getSFeedId()));
                StatisticsUtil.reportAction(StatisticsConst.INFORMATION_TAB_CONSULTANT_CLICK_ITEM);
            }

            private SpannableString getSummaryString(FeedContent feedContent) {
                int attiType = feedContent.getEAttiType();
//            attiType = E_FEED_INVEST_ATTI_TYPE.E_FIAT_GOOD; //for test
                String content = attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_NEUTRAL ? feedContent.getSDescription() : "??" + " " + feedContent.getSDescription();
                Drawable attiDrawable = null;
                if (attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_GOOD) {
                    attiDrawable = mAttiDrawableGood;
                } else if (attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_BAD) {
                    attiDrawable = mAttiDrawableBad;
                }
                if (attiDrawable != null) {
                    attiDrawable.setBounds(0, 0, attiDrawable.getIntrinsicWidth(), attiDrawable.getIntrinsicHeight());
                }

                SpannableString spannableString = new SpannableString(StringUtil.getStringWithMaxLength(content, FeedRecyclerViewAdapter.MAX_CONSULTANT_CONTENT_LENGTH));
                if (attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_GOOD || attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_BAD) {
                    spannableString.setSpan(new ImageSpan(attiDrawable, ImageSpan.ALIGN_BASELINE), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                return spannableString;
            }
        }
    }
}
