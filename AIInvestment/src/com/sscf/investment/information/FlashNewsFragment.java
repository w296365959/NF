package com.sscf.investment.information;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.information.entity.FlashNewsViewItem;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.entity.ShareParams;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.OnRefreshStateChangeListener;
import com.sscf.investment.socialize.ShareDialog;
import com.sscf.investment.widget.recyclerview.CommonFooterView;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;
import com.sscf.investment.widget.recyclerview.EndlessRecyclerOnScrollListener;
import com.sscf.investment.widget.recyclerview.RecyclerViewHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import BEC.BEACON_STAT_TYPE;
import BEC.FlashNewsListReq;
import BEC.FlashNewsListRsp;
import BEC.NewsDesc;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by liqf on 2015/10/20.
 */
public class FlashNewsFragment extends BaseFragment implements DataSourceProxy.IRequestCallback {
    private static final String TAG = FlashNewsFragment.class.getSimpleName();

    public static final int REFRESH_TYPE_UP = 1;
    public static final int REFRESH_TYPE_DOWN = 2;
    public static final int UPDATE_INTERVAL = 15 * 60 * 1000; //页面切回来时如果超过5分钟就刷新一次

    private PtrFrameLayout mPtrFrame;

    private Handler mUiHandler = new Handler();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private NewsRecyclerViewAdapter mAdapter;

    private TextView mListViewDateHeaderSticky;
    private FrameLayout mListViewDateHeaderStickyLayout;

    private List<NewsDesc> mNewsList;

    @BindView(R.id.empty_view) View mEmptyView;
    @BindView(R.id.loading_layout) View mLoadingLayoutCenter;
    @BindView(R.id.fail_retry) View mFailRetryLayoutCenter;

    private int mViewState = DengtaConst.UI_STATE_NORMAL;

    private CommonFooterView mFooterView;

    private OnRefreshStateChangeListener mRefreshStateChangeListener;
    private long mLastUpdateTime;

    @BindDimen(R.dimen.list_item_title_bar_height) int mListItemTitleBarHeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View contextView = inflater.inflate(R.layout.flash_news_list, container, false);
        ButterKnife.bind(this, contextView);

        mRecyclerView = (RecyclerView) contextView.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        itemDecoration.setDividerColor(ContextCompat.getColor(getContext(), R.color.default_content_bg));
        mRecyclerView.addItemDecoration(itemDecoration);

        mNewsList = new ArrayList<>();
        mAdapter = new NewsRecyclerViewAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        initFooterView();

        mLoadingLayoutCenter.setVisibility(View.VISIBLE);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                //加载更多功能的代码
                DtLog.d(TAG, "onScroll: onScrollStateChanged !!!need bottom refresh!!!");
                if (mViewState != DengtaConst.UI_STATE_NO_MORE_CONTENT) {
                    showViewByState(DengtaConst.UI_STATE_LOADING);
                    loadNetworkData(REFRESH_TYPE_DOWN);
                }
            }
        });

        mAdapter.setFloatingViewCallback(mRecyclerView, FlashNewsViewItem.TYPE_ITEM_DATE, mListItemTitleBarHeight, (translationY, itemData) -> {
            if (mListViewDateHeaderStickyLayout == null) {
                FrameLayout ptrContentLayout = (FrameLayout) mPtrFrame.findViewById(R.id.ptr_content);
                View header = LayoutInflater.from(getContext()).inflate(R.layout.flash_news_header, ptrContentLayout, false);
                ptrContentLayout.addView(header);
                mListViewDateHeaderStickyLayout = (FrameLayout) header.findViewById(R.id.date_layout);
                mListViewDateHeaderSticky = (TextView) header.findViewById(R.id.date);
            }

            if (itemData != null) {
                mListViewDateHeaderSticky.setVisibility(View.VISIBLE);
                mListViewDateHeaderStickyLayout.bringToFront();
                mListViewDateHeaderStickyLayout.setTranslationY(translationY);
                FlashNewsViewItem listItem = (FlashNewsViewItem) itemData;
                String date = listItem.getDate();
                if (!TextUtils.equals(mListViewDateHeaderSticky.getText(), date)) {
                    mListViewDateHeaderSticky.setText(date);
                }
            } else {
                mListViewDateHeaderSticky.setVisibility(View.GONE);
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

        return contextView;
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_MARKET_NEWS);
        helper.setKey(StatConsts.MARKET_NEWS_FLASH_NEWS);
        return helper;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        doReloadData();
    }

    private void initFooterView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mFooterView = (CommonFooterView) inflater.inflate(R.layout.common_footer_view_layout, mRecyclerView, false);
        mFooterView.setFailRetryListener(() -> loadNetworkData(REFRESH_TYPE_DOWN));
        mAdapter.setFooterView(mFooterView);
    }

    private void doRefresh() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            onLoadComplete(false);
            DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
            return;
        }

        loadNetworkData(REFRESH_TYPE_UP);
    }

    @Override
    public void onFirstUserVisible() {
        DtLog.d(TAG, "onFirstUserVisible");
        super.onFirstUserVisible();
        doReloadData();
        StatisticsUtil.reportAction(StatisticsConst.INFORMATION_TAB_FLASH_NEWS_DISPLAYED);
    }

    @Override
    public void onUserVisible() {
        DtLog.d(TAG, "onUserVisible");
        super.onUserVisible();
        doReloadData();
        StatisticsUtil.reportAction(StatisticsConst.INFORMATION_TAB_FLASH_NEWS_DISPLAYED);
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

    private void loadNetworkData(final int refreshType) {
        DtLog.d(TAG, "loadNetworkData begin......");

        mLastUpdateTime = SystemClock.elapsedRealtime();

        if (refreshType == REFRESH_TYPE_DOWN) {
            showViewByState(DengtaConst.UI_STATE_LOADING);
        }

        if (mRefreshStateChangeListener != null) {
            mRefreshStateChangeListener.onRefreshStart();
        }

        String startId = "", endId = "";
        String newestId = "", oldestId = "";
        int count = mNewsList.size();
        if (count > 0) {
            newestId = mNewsList.get(0).getSNewsID();
            oldestId = mNewsList.get(count - 1).getSNewsID();
        }
        DtLog.d(TAG, "loadNetworkData: newestId = " + newestId + ", oldestId = " + oldestId);

        switch (refreshType) {
            case REFRESH_TYPE_UP:
                startId = "";
                endId = newestId;
                break;
            case REFRESH_TYPE_DOWN:
                startId = oldestId;
                endId = "";
                break;
            default:
                break;
        }

        if (mAdapter.getNormalItemCount() <= 0) {
            mFailRetryLayoutCenter.setVisibility(View.GONE);
            mLoadingLayoutCenter.setVisibility(View.VISIBLE);
        }

        FlashNewsListReq newsReq = new FlashNewsListReq();
        newsReq.setStUserInfo(DengtaApplication.getApplication().getAccountManager().getUserInfo());
        newsReq.setSStartId(startId);
        newsReq.setSEndId(endId);
        DtLog.d(TAG, "loadNetworkData: startId = " + startId + ", endId = " + endId);

        DataEngine.getInstance().request(EntityObject.ET_GET_DISCOVER_FLASH_NEWS, newsReq, this, String.valueOf(refreshType));
    }

    @Override
    public void callback(boolean success, EntityObject entity) {
        if (!success) {
            DtLog.d(TAG, "QRomWupManagerImpl: getData, not success");
            onLoadComplete(false);

            mUiHandler.post(() -> showViewByState(DengtaConst.UI_STATE_FAILED_RETRY));

            return;
        }

        switch (entity.getEntityType()) {
            case EntityObject.ET_GET_DISCOVER_FLASH_NEWS:
                onLoadComplete(true);

                final String extra = (String) entity.getExtra();
                FlashNewsListRsp newsRsp = (FlashNewsListRsp) entity.getEntity();
                final ArrayList<NewsDesc> newsList = newsRsp.getVtId();
                final String nextNewsID = newsRsp.getSNextNewsID();
                mUiHandler.postDelayed(() -> setData(newsList, nextNewsID, extra), 0);
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
        loadNetworkData(REFRESH_TYPE_UP);
    }

    private void setData(ArrayList<NewsDesc> newsDesc, String nextNewsID, String extra) {
        Integer refreshType = REFRESH_TYPE_DOWN;
        try {
            refreshType = Integer.valueOf(extra);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (newsDesc != null && newsDesc.size() == 0) {
            if (refreshType == REFRESH_TYPE_DOWN) {
                showViewByState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
                return;
            }
        }

        int firstVisiblePosition = mLayoutManager.findFirstVisibleItemPosition();
        View v = mRecyclerView.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();

        String nextNewestId = newsDesc.size() > 0 ? newsDesc.get(0).getSNewsID() : "";
        String nextOldestId = newsDesc.size() > 0 ? newsDesc.get(newsDesc.size() - 1).getSNewsID() : "";
        String newestId = mNewsList.size() > 0 ? mNewsList.get(0).getSNewsID() : "";
        String oldestId = mNewsList.size() > 0 ? mNewsList.get(mNewsList.size() - 1).getSNewsID() : "";

        if (refreshType == REFRESH_TYPE_DOWN || (refreshType == REFRESH_TYPE_UP && nextNewsID.compareTo(newestId) == 0)) { //下拉刷新，或者上拉刷新且新数据和缓存数据能衔接上时
            if (nextOldestId.compareTo(newestId) > 0) { //上拉刷新且数据衔接上没有断层的情况，新数据插入队列顶部
                mNewsList.addAll(0, newsDesc);
            } else if (nextNewestId.compareTo(oldestId) < 0) { //下拉刷新，新数据插入队列尾部
                mNewsList.addAll(newsDesc);
            } else {
                if (TextUtils.isEmpty(newestId) && TextUtils.isEmpty(oldestId)) { //首次拉取
                    mNewsList.addAll(newsDesc);
                }
            }
        } else { //上拉刷新并且数据出现断层的情况，废弃缓存数据
            mNewsList.clear();
            mNewsList.addAll(newsDesc);
        }

        mAdapter.setListData(mNewsList);

        mAdapter.notifyDataSetChanged();
        mLayoutManager.scrollToPositionWithOffset(firstVisiblePosition, top);

        showViewByState(DengtaConst.UI_STATE_NORMAL);
    }

    private void goToTopAndDoRefresh() {
        mRecyclerView.scrollToPosition(0);
        mPtrFrame.autoRefresh();
    }

    class NewsRecyclerViewAdapter extends CommonRecyclerViewAdapter {

        private final Context mContext;
        private SimpleDateFormat mDateFormat;
        private Date mDate;
        private List<NewsDesc> mNewsListData;
        private final LayoutInflater mInflater;

        private int mYear;
        private int mMonth;
        private int mToday;
        private StringBuilder mFormatSb;

        private int mColorSummary;
        private int mColorSummaryClicked;
        private int mColorImportant;
        private int mColorImportantClicked;

        private int mCurrentPosition = -1;
        private ShareDialog mShareDialog;
        private ShareParams mShareParams;

        public static final int MAX_LINES_COLLAPSED = 3;
        public static final int MAX_LINES_EXPANDED = 30;
        private ArrayList<String> mClickedNewsIDs = new ArrayList<>();

        public NewsRecyclerViewAdapter(Context context) {
            super(context);
            mContext = context;
            mDateFormat = new SimpleDateFormat(/*"yyyy-MM-dd HH:mm:ss"*/);
            mFormatSb = new StringBuilder();
            mInflater = LayoutInflater.from(mContext);

            initResources();
        }

        private void initResources() {
            TypedArray a = mContext.obtainStyledAttributes(new int[] {
                    R.attr.news_important, R.attr.news_important_clicked
            });
            mColorImportant = a.getColor(0, Color.GRAY);
            mColorImportantClicked = a.getColor(1, Color.GRAY);
            a.recycle();
            mColorSummary = ContextCompat.getColor(mContext, R.color.default_text_color_100);
            mColorSummaryClicked = ContextCompat.getColor(mContext, R.color.default_text_color_60);
        }

        @Override
        public void setListData(List listData) {
            mNewsListData = listData;
            setListViewData();
        }

        private void setListViewData() {
            mListData.clear();

            String curSimpleDate = "";
            for (NewsDesc newsDesc : mNewsListData) {
                int iTime = newsDesc.getITime();
                String simpleDate = timeStamp2SimpleDate(iTime * 1000L);
                if (!TextUtils.equals(curSimpleDate, simpleDate)) {
                    curSimpleDate = simpleDate;
                    FlashNewsViewItem newsViewItem = new FlashNewsViewItem();
                    newsViewItem.setDate(simpleDate);
                    newsViewItem.setType(FlashNewsViewItem.TYPE_ITEM_DATE);
                    mListData.add(newsViewItem);
                }
                FlashNewsViewItem newsViewItem = new FlashNewsViewItem();
                newsViewItem.setNewsDesc(newsDesc);
                newsViewItem.setDate(simpleDate);
                newsViewItem.setType(FlashNewsViewItem.TYPE_ITEM_NEWS);
                mListData.add(newsViewItem);
            }
        }

        private void updateDate() {
            mDate = new Date();
            mYear = mDate.getYear() + 1900;
            mMonth = mDate.getMonth();
            mToday = mDate.getDate();
        }

        public String timeStamp2SimpleDate(long timestamp) {
            updateDate();

            mFormatSb.delete(0, mFormatSb.length());
            mDate.setTime(timestamp);

            int year = mDate.getYear() + 1900;
            int month = mDate.getMonth();
            int today = mDate.getDate();
            int day = mDate.getDay();

            if (!(year == mYear && month == mMonth && today == mToday)) { //不是今天
                mFormatSb.insert(0, "yy-MM-dd");
            } else {
                return "今天";
            }

            mDateFormat.applyPattern(mFormatSb.toString());

            String date = mDateFormat.format(mDate);
            date += " 周" + TimeUtils.getDayInChinese(day);
            return date;
        }

        public String timeStamp2Minute(long timestamp) {
            updateDate();

            mFormatSb.delete(0, mFormatSb.length());
            mDate.setTime(timestamp);
            mFormatSb.append("HH:mm");

            mDateFormat.applyPattern(mFormatSb.toString());

            String date = mDateFormat.format(mDate);
            return date;
        }

        @Override
        protected int getNormalViewType(int position) {
            final int size = mListData == null ? 0 : mListData.size();
            if (position < size && position >= 0) {
                FlashNewsViewItem viewItem = (FlashNewsViewItem) mListData.get(position);
                return viewItem.getType();
            }
            return FlashNewsViewItem.TYPE_ITEM_NEWS;
        }

        @Override
        protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
            if (viewType == FlashNewsViewItem.TYPE_ITEM_NEWS) {
                return new NewsViewHolder(mInflater.inflate(R.layout.flash_news_item, parent, false));
            } else if (viewType == FlashNewsViewItem.TYPE_ITEM_DATE) {
                return new DateViewHolder(mInflater.inflate(R.layout.flash_news_item_date, parent, false));
            }
            return null;
        }

        private void refreshListItems() {
            int childCount = mRecyclerView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = mRecyclerView.getChildAt(i);
                Object tag = child.getTag();

                if (!(tag instanceof NewsViewHolder)) {
                    continue;
                }

                NewsViewHolder newsViewHolder = (NewsViewHolder) tag;
                FlashNewsViewItem listItem = (FlashNewsViewItem) getItemData(newsViewHolder.mRealPosition);

                if(listItem == null) {
                    DtLog.d(TAG, "refreshListItems listItem is null ");
                    continue;
                }

                NewsDesc newsDesc = listItem.getNewsDesc();
                if (newsDesc == null) {
                    DtLog.d(TAG, "refreshListItems newsDesc is null ");
                    continue;
                }

                int maxLines = newsViewHolder.mSummary.getMaxLines();
                boolean isExpanded = maxLines == MAX_LINES_COLLAPSED;
                if (newsViewHolder.mRealPosition == mCurrentPosition) {
                    if (isExpanded) {
                        newsViewHolder.mSummary.setMaxLines(MAX_LINES_EXPANDED);
                        newsViewHolder.mSummary.setEllipsize(null);
                    } else {
                        newsViewHolder.mSummary.setMaxLines(MAX_LINES_COLLAPSED);
                        newsViewHolder.mSummary.setEllipsize(TextUtils.TruncateAt.END);
                    }
                } else {
                    newsViewHolder.mSummary.setMaxLines(MAX_LINES_COLLAPSED);
                    newsViewHolder.mSummary.setEllipsize(TextUtils.TruncateAt.END);
                }

                int summaryColor = mColorSummary;
                int styleType = newsDesc.getIStyleType();

                if (mClickedNewsIDs.contains(newsDesc.getSNewsID())) {
                    if (styleType == 0) {
                        summaryColor = mColorSummaryClicked;
                    } else if (styleType == 1) {
                        summaryColor = mColorImportantClicked;
                    }
                    newsViewHolder.mTimeIndicator.setTextColor(mColorSummaryClicked);
                } else {
                    if (styleType == 0) {
                        summaryColor = mColorSummary;
                    } else if (styleType == 1) {
                        summaryColor = mColorImportant;
                    }
                    newsViewHolder.mTimeIndicator.setTextColor(mColorSummary);
                }

                newsViewHolder.mSummary.setTextColor(summaryColor);
            }
        }

        class NewsViewHolder extends CommonViewHolder {
            @BindView(R.id.time_line_indicator) TextView mTimeIndicator;
            @BindView(R.id.summary) TextView mSummary;
            @BindView(R.id.news_share_layout) View mShare;

            public NewsViewHolder(View itemView) {
                super(itemView);
            }

            @OnClick(R.id.news_share_layout)
            public void doShare() {
                mShareDialog = new ShareDialog((Activity) mContext);
                final Resources resources = mContext.getResources();
                final WebUrlManager urlManager = DengtaApplication.getApplication().getUrlManager();
                NewsDesc newsDesc = (NewsDesc) mShare.getTag();
                String showText = (String) mShare.getTag(R.id.share_title);
                if (showText.length() > ShareDialog.MAX_SHARE_TITLE_LENGTH) {
                    showText = showText.substring(0, ShareDialog.MAX_SHARE_TITLE_LENGTH) + "...";
                }
                mShareParams = ShareParams.createShareParams(showText, resources.getString(R.string.share_flash_news_msg),
                        newsDesc.getSDtInfoUrl(), urlManager.getShareIconUrl());

                mShareDialog.showShareDialog(mShareParams, null, null);
            }

            @Override
            public void bindData(Object itemData) {
                super.bindData(itemData);

                FlashNewsViewItem listItem = (FlashNewsViewItem) itemData;
                NewsDesc newsDesc = listItem.getNewsDesc();

                int iTime = newsDesc.getITime();
                String minute = timeStamp2Minute(iTime * 1000L);
                mTimeIndicator.setText(minute);

                mSummary.setMaxLines(MAX_LINES_COLLAPSED);
                mSummary.setEllipsize(TextUtils.TruncateAt.END);

                int summaryColor = mColorSummary;
                int styleType = newsDesc.getIStyleType();
                if (mClickedNewsIDs.contains(newsDesc.getSNewsID())) {
                    if (styleType == 0) {
                        summaryColor = mColorSummaryClicked;
                    } else if (styleType == 1) {
                        summaryColor = mColorImportantClicked;
                    }
                    mTimeIndicator.setTextColor(mColorSummaryClicked);
                } else {
                    if (styleType == 0) {
                        summaryColor = mColorSummary;
                    } else if (styleType == 1) {
                        summaryColor = mColorImportant;
                    }
                    mTimeIndicator.setTextColor(mColorSummary);
                }
                mSummary.setTextColor(summaryColor);

                String title = newsDesc.getSTitle();
                String description = newsDesc.getSDescription();
                String showText = "";
                if (!TextUtils.isEmpty(title)) {
                    showText = "【" + title + "】 " + description;
                } else {
                    showText = description;
                }
                showText = showText.trim();
                mSummary.setText(showText);

                mShare.setTag(newsDesc);
                mShare.setTag(R.id.share_title, showText);
            }

            @Override
            public void onItemClicked() {
                super.onItemClicked();
                mCurrentPosition = mRealPosition;

                FlashNewsViewItem listItem = (FlashNewsViewItem) mItemData;
                NewsDesc item = listItem.getNewsDesc();

                refreshListItems();

                String newsID = item.getSNewsID();
                if (!mClickedNewsIDs.contains(newsID)) {
                    mClickedNewsIDs.add(newsID);
                }
            }
        }

        class DateViewHolder extends CommonViewHolder {
            @BindView(R.id.date) public TextView mDate;

            public DateViewHolder(View itemView) {
                super(itemView);
            }

            @Override
            public void bindData(Object itemData) {
                super.bindData(itemData);
                FlashNewsViewItem listItem = (FlashNewsViewItem) itemData;
                String date = listItem.getDate();
                mDate.setText(date);
            }
        }
    }
}
