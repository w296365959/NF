package com.sscf.investment.information;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.information.presenter.HotSpotPresenter;
import com.sscf.investment.main.DengtaApplication;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.message.manager.RecyclerViewManager;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.SecListItemUtils;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StringUtil;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import BEC.BEACON_STAT_TYPE;
import BEC.E_SEC_STATUS;
import BEC.SecBaseInfo;
import BEC.SecSimpleQuote;
import BEC.StockRela;
import BEC.TopicListItem;
import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by davidwei on 2017/05/13.
 * 热点
 */
public final class HotSpotFragment extends BaseFragment implements PtrHandler, RecyclerViewManager.OnLoadMoreListener, View.OnClickListener {
    private static final String TAG = HotSpotFragment.class.getSimpleName();

    private HotSpotPresenter mPresenter;
    private PtrFrameLayout mPtrLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerViewManager mRecyclerViewManager;
    private HotSpotAdapter mAdapter;

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    mPresenter.refresh();
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DtLog.d(TAG, "onCreateView");
        final View root = inflater.inflate(R.layout.common_ptr_recycler_view_with_state, null);
        initViews(root);

        mPresenter = new HotSpotPresenter(this);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_MARKET_NEWS);
        helper.setKey(StatConsts.MARKET_NEWS_HOT_SPOT);
        return helper;
    }

    private void initViews(final View root) {
        DtLog.d(TAG, "initViews");
        final Activity activity = getActivity();
        mPtrLayout = (PtrFrameLayout) root.findViewById(R.id.ptr);
        mPtrLayout.setPtrHandler(this);

        mRecyclerView = (RecyclerView) mPtrLayout.findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new GapItemDecoration(
                activity.getResources().getDimensionPixelSize(R.dimen.list_market_divider_height)));
        final HotSpotAdapter adapter = new HotSpotAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;

        final View loadingLayoutCenter = root.findViewById(R.id.loading_layout);
        final View failRetryLayoutCenter = root.findViewById(R.id.fail_retry);
        failRetryLayoutCenter.setOnClickListener(this);
        final TextView emptyView = (TextView) root.findViewById(R.id.emptyView);
        emptyView.setText(R.string.no_content);

        mRecyclerViewManager = new RecyclerViewManager(activity, mRecyclerView, mLinearLayoutManager,
                adapter, loadingLayoutCenter, failRetryLayoutCenter, emptyView, this);
        showLoadingLayout();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        mPresenter.requestListData();
        mRecyclerView.addOnScrollListener(mScrollListener);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        mPresenter.refresh();
        mRecyclerView.addOnScrollListener(mScrollListener);
    }

    @Override
    public void onFirstUserInvisible() {
        super.onFirstUserInvisible();
        if (mPresenter != null) {
            mPresenter.stopRefresh();
        }
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        mPresenter.stopRefresh();
        mRecyclerView.removeOnScrollListener(mScrollListener);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mPresenter.requestListData();
    }

    @Override
    public void onLoadMore() {
        mPresenter.requestListMoreData();
    }

    public void refreshComplete() {
        mPtrLayout.refreshComplete();
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

    public void showFooterNormalLayout() {
        mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_NORMAL);
    }

    @Override
    public void onClick(View v) {
        showLoadingLayout();
        mPresenter.requestListData();
    }

    public void updateList( final ArrayList<TopicListItem> topicList) {
        DtLog.d(TAG, "updateList topicList.size() : " + topicList.size());
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NORMAL);
        final HotSpotAdapter adapter = mAdapter;
        adapter.setListData(topicList);
        adapter.notifyDataSetChanged();
        mFirstPosition = -1;
        mLastPosition = -1;
    }

    private int mFirstPosition = -1;
    private int mLastPosition = -1;
    private ArrayList<String> mDtSecCodes;

    public ArrayList<String> getVisibleDtSecCodes() {
        if (mAdapter != null) {
            final int firstPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
            final int lastPosition = mLinearLayoutManager.findLastVisibleItemPosition();
            DtLog.d(TAG, "getVisibleDtSecCodes firstPosition : " + firstPosition);
            DtLog.d(TAG, "getVisibleDtSecCodes lastPosition : " + lastPosition);
            DtLog.d(TAG, "getVisibleDtSecCodes mFirstPosition : " + mFirstPosition);
            DtLog.d(TAG, "getVisibleDtSecCodes mLastPosition : " + mLastPosition);
            if (firstPosition != mFirstPosition || lastPosition != mLastPosition) { // 位置变化，更新list
                mFirstPosition = firstPosition;
                mLastPosition = lastPosition;
                mDtSecCodes = mAdapter.getVisibleDtSecCodes(firstPosition, lastPosition);
            }
            final int size = mDtSecCodes == null ? 0 : mDtSecCodes.size();
            DtLog.d(TAG, "getVisibleDtSecCodes mDtSecCodes.size() : " + size);
            return mDtSecCodes;
        }
        return null;
    }

    public void updateQuotesData(final ArrayList<SecSimpleQuote> quotes) {
        DtLog.d(TAG, "updateQuotesData quotes.size() : " + quotes.size());
        mAdapter.updateQuotesData(quotes);
    }
}

final class HotSpotAdapter extends CommonRecyclerViewAdapter {
    private final Map<String, SecSimpleQuote> mStockInfoMap;

    HotSpotAdapter(Context context) {
        super(context);
        mStockInfoMap = new HashMap<>();
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        return new HotSpotHolder(mInflater.inflate(R.layout.fragment_info_hot_spot_item, parent, false));
    }

    ArrayList<String> getVisibleDtSecCodes(final int positionStart, final int positionEnd) {
        if (positionStart < 0 || positionEnd < 0) {
            return null;
        }

        final HashSet<String> dtSecCodeSet = new HashSet<>();
        for (int i = positionStart; i <= positionEnd; i++) {
            final TopicListItem item = (TopicListItem) getItemData(i);
            if (item != null) {
                final ArrayList<BEC.StockRela> stockList = item.vStockRela;
                int stockSize = stockList == null ? 0 : stockList.size();
                if (stockSize > 0) {
                    stockSize = Math.min(4, stockSize); // 只取前4个股票
                    for (int j = 0; j < stockSize; j++) {
                        final SecBaseInfo stock = stockList.get(j).stSecBaseInfo;
                        if (stock != null) {
                            dtSecCodeSet.add(stock.sDtSecCode);
                        }
                    }
                }
            }
        }

        return new ArrayList<>(dtSecCodeSet);
    }

    void updateQuotesData(ArrayList<SecSimpleQuote> quotes) {
        String dtSecCode;
        for (SecSimpleQuote quote : quotes) {
            dtSecCode = quote.sDtSecCode;
            mStockInfoMap.put(dtSecCode, quote);
        }
        notifyDataSetChanged();
    }

    final class HotSpotHolder extends CommonRecyclerViewAdapter.CommonViewHolder implements View.OnClickListener {
        @BindView(R.id.title) TextView mTitle;
        @BindView(R.id.name) TextView mName;
        @BindView(R.id.time) TextView mTime;

        @BindView(R.id.name0) TextView mName0;
        @BindView(R.id.value0) TextView mValue0;
        @BindView(R.id.name1) TextView mName1;
        @BindView(R.id.value1) TextView mValue1;
        @BindView(R.id.name2) TextView mName2;
        @BindView(R.id.value2) TextView mValue2;
        @BindView(R.id.name3) TextView mName3;
        @BindView(R.id.value3) TextView mValue3;

        HotSpotHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            final TopicListItem item = (TopicListItem) itemData;
            if (item != null) {
                mTitle.setText(item.stTopicMessage.sTitle);
                mName.setText(item.sName);
                mTime.setText(TimeUtils.timeStamp2Date(item.iTimestamp * DengtaConst.MILLIS_FOR_SECOND));
                updateQuotes(item.vStockRela);
            }
        }

        private void updateQuotes(final ArrayList<StockRela> stockList) {
            final SecSimpleQuote[] quotes = getRelatedQuotes(stockList);

            mName0.setOnClickListener(this);
            mValue0.setOnClickListener(this);
            mName1.setOnClickListener(this);
            mValue1.setOnClickListener(this);
            mName2.setOnClickListener(this);
            mValue2.setOnClickListener(this);
            mName3.setOnClickListener(this);
            mValue3.setOnClickListener(this);

            updateStockQuote(mName0, mValue0, quotes[0]);
            updateStockQuote(mName1, mValue1, quotes[1]);
            updateStockQuote(mName2, mValue2, quotes[2]);
            updateStockQuote(mName3, mValue3, quotes[3]);
        }

        SecSimpleQuote[] getRelatedQuotes(final ArrayList<StockRela> stockList) {
            SecSimpleQuote[] quotes = (SecSimpleQuote[]) mTag;
            final int QUOTES_LENGTH = 4;
            if (quotes == null) {
                quotes = new SecSimpleQuote[QUOTES_LENGTH];
                mTag = quotes;
            }

            for (int i = 0; i < QUOTES_LENGTH; i++) {
                quotes[i] = getQuote(i, stockList);
            }

            return quotes;
        }

        private void updateStockQuote(final TextView nameView, final TextView valueView, final SecSimpleQuote quote) {
            if (quote == null) {
                nameView.setVisibility(View.GONE);
                valueView.setVisibility(View.GONE);
                return;
            }

            nameView.setVisibility(View.VISIBLE);
            valueView.setVisibility(View.VISIBLE);
            int nameBgRes = R.drawable.hot_spot_stock_name_base_bg;
            int valueBgRes = R.drawable.hot_spot_stock_value_base_bg;
            if (quote.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
                valueView.setText(R.string.suspended);
            } else {
                final float now = quote.fNow;
                final float close = quote.fClose;
                if (now > 0 && close > 0) {
                    final float delta = now - close;
                    valueView.setText(StringUtil.getUpdownString(quote));
                    if (delta > 0) {
                        nameBgRes = R.drawable.hot_spot_stock_name_red_bg;
                        valueBgRes = R.drawable.hot_spot_stock_value_red_bg;
                    } else if (delta < 0) {
                        nameBgRes = R.drawable.hot_spot_stock_name_green_bg;
                        valueBgRes = R.drawable.hot_spot_stock_value_green_bg;
                    }
                } else {
                    valueView.setText(R.string.value_null);
                }
            }
            nameView.setText(quote.sSecName);
            nameView.setBackgroundResource(nameBgRes);
            valueView.setBackgroundResource(valueBgRes);
            nameView.setTag(quote);
            valueView.setTag(quote);
        }

        SecSimpleQuote getQuote(final int index, final ArrayList<StockRela> stockList) {
            SecSimpleQuote quote = null;
            String dtSecCode;
            if (index < stockList.size()) {
                final StockRela stock = stockList.get(index);
                if (stock != null) {
                    dtSecCode = stock.stSecBaseInfo.sDtSecCode;
                    if (!TextUtils.isEmpty(dtSecCode)) {
                        quote = mStockInfoMap.get(dtSecCode);
                        if (quote == null) {
                            quote = DengtaApplication.getApplication().getDataCacheManager().getSecSimpleQuote(dtSecCode);
                            if (quote == null) {
                                quote = new SecSimpleQuote();
                                quote.sSecName = stock.stSecBaseInfo.sCHNShortName;
                                quote.sDtSecCode = dtSecCode;
                            }
                        }
                    }
                }
            }
            return quote;
        }

        @Override
        public void onItemClicked() {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }

            final TopicListItem item = (TopicListItem) mItemData;
            if (item != null) {
                WebBeaconJump.showCommonWebActivity(mContext,
                        WebUrlManager.getInstance().getHotSpotDetailUrl(item.sId));
                StatisticsUtil.reportAction(StatisticsConst.A_NEWS_HOTSPOT_DETAIL);
            }
        }

        @Override
        public void onClick(View v) {
            final SecSimpleQuote quote = (SecSimpleQuote) v.getTag();
            if (quote != null) {
                final SecSimpleQuote[] quotes = (SecSimpleQuote[]) mTag;
                CommonBeaconJump.showSecurityDetailActivity(mContext, quote.sDtSecCode, quote.sSecName,
                        SecListItemUtils.getSecListFromSecSimpleQuoteList(quotes));
                StatisticsUtil.reportAction(StatisticsConst.HOT_SPOT_CLICK_STOCK);
            }
        }
    }
}

final class GapItemDecoration extends RecyclerView.ItemDecoration{
    private final int mGap;

    GapItemDecoration(final int gap) {
        mGap = gap;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mGap);
    }
}
