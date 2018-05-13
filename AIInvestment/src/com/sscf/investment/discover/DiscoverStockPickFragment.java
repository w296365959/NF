package com.sscf.investment.discover;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.comparator.SecSimpleQuoteUpdownComparator;
import com.sscf.investment.discover.presenter.StockPickPresenter;
import com.sscf.investment.discover.widget.StockPickHeader;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.SDkStringUtils;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.utils.SecListItemUtils;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.CommonMultiViewTypeAdapter;
import com.sscf.investment.widget.CommonViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import BEC.BEACON_STAT_TYPE;
import BEC.CategoryInfo;
import BEC.DtActivityDetail;
import BEC.E_SEC_STATUS;
import BEC.IntelliPickStockEx;
import BEC.IntelliStock;
import BEC.RecommValueAdded;
import BEC.SecBaseInfo;
import BEC.SecSimpleQuote;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by davidwei on 2015/10/22. 策略精选
 */
public final class DiscoverStockPickFragment extends BaseFragment implements AbsListView.OnScrollListener, View.OnClickListener, PtrHandler {
    private PtrFrameLayout mPtrFrame;
    private ListView mListView;
    private StockPickCardAdapter mAdapter;

    private StockPickHeader mHotStockHeader;

    private TextView mNoMoreLayout;
    private View mLoadingLayout;

    private static final int STATE_NONE = 0;
    private static final int STATE_LOADING = 1;
    private static final int STATE_HAS_NO_MORE = 2;
    private int mFooterState = STATE_NONE;

    private StockPickPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.ptr_list, container, false);
        initViews(root);
        mPresenter = new StockPickPresenter(this);
        mPresenter.registerBroadcastReceiver();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unregisterBroadcastReceiver();
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_SMART_PICK);
        helper.setKey(StatConsts.DISCOVER_STOCK_PICK);
        return helper;
    }

    private void initViews(final View root) {
        final Activity activity = getActivity();
        final Resources resources = activity.getResources();

        mPtrFrame = (PtrFrameLayout) root.findViewById(R.id.ptr);
        mPtrFrame.setPtrHandler(this);

        mListView = (ListView) root.findViewById(R.id.list);
        final int padding = resources.getDimensionPixelSize(R.dimen.discover_card_padding);

        // 添加分割线，避免下拉刷新动画的显示问题
        mListView.setDivider(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.default_background)));
        mListView.setDividerHeight(padding);
        mListView.setOnScrollListener(this);
        mListView.setHeaderDividersEnabled(true);

        mLoadingLayout = LayoutInflater.from(activity).inflate(R.layout.list_item_loading, null);
        mLoadingLayout.findViewById(R.id.line).setVisibility(View.GONE);
        mLoadingLayout.setBackgroundColor(Color.TRANSPARENT);

        initHotStockHeader(activity);
    }

    private void initHotStockHeader(Context context) {
        mHotStockHeader = (StockPickHeader) View.inflate(context, R.layout.discover_stock_pick_hot_stock, null);
        mHotStockHeader.findViewById(R.id.more).setOnClickListener(this);
        mListView.addHeaderView(mHotStockHeader);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 当不滚动时
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                if (mFooterState == STATE_LOADING) {
                    mPresenter.requestMorePageData();
                }
            }
            mPresenter.refreshQuote();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(final PtrFrameLayout frame) {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            refreshComplete();
            DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
            return;
        }
        mPresenter.requestFirstPageData();
    }

    public void showNoMore() {
        if (mFooterState == STATE_HAS_NO_MORE) {
            return;
        }
        mFooterState = STATE_HAS_NO_MORE;
        if (mNoMoreLayout == null) {
            final Activity activity = getActivity();
            final Resources resources = activity.getResources();

            mNoMoreLayout = new TextView(activity);
            mNoMoreLayout.setGravity(Gravity.CENTER);
            mNoMoreLayout.setText(R.string.no_more);

            final int textColor = ContextCompat.getColor(getContext(), R.color.default_text_color_60);
            mNoMoreLayout.setTextColor(textColor);
            mNoMoreLayout.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_size_12));
            mNoMoreLayout.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.discover_card_padding));
        }
        if (mLoadingLayout != null) {
            mListView.removeFooterView(mLoadingLayout);
        }
        if (mNoMoreLayout.getParent() == null) {
            mListView.addFooterView(mNoMoreLayout);
        }
    }

    public void showFooterLoading() {
        if (mFooterState == STATE_LOADING) {
            return;
        }
        mFooterState = STATE_LOADING;
        if (mNoMoreLayout != null) {
            mListView.removeFooterView(mNoMoreLayout);
        }
        mListView.addFooterView(mLoadingLayout);
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        mPresenter.loadDataFromLocal();
        mPresenter.refreshData();
        mHotStockHeader.onUserVisible();
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        mPresenter.refreshData();
        mHotStockHeader.onUserVisible();
    }

    @Override
    public void onFirstUserInvisible() {
        super.onFirstUserInvisible();
        if (mPresenter != null) {
            mPresenter.stopRefreshQuote();
        }
        if (mHotStockHeader != null) {
            mHotStockHeader.onUserInvisible();
        }
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        if (mPresenter != null) {
            mPresenter.stopRefreshQuote();
        }
        mHotStockHeader.onUserInvisible();
    }

    private int mFirstPosition = -1;
    private int mLastPosition = -1;
    private ArrayList<String> mDtSecCodes;

    public ArrayList<String> getVisibleDtSecCodes() {
        if (mAdapter != null) {
            final int headerCount = mListView.getHeaderViewsCount();
            int firstPosition = mListView.getFirstVisiblePosition() - headerCount;
            if (firstPosition < 0) {
                firstPosition = 0;
            }
            final int lastPosition = mListView.getLastVisiblePosition() - headerCount;
            if (firstPosition != mFirstPosition || lastPosition != mLastPosition) { // 位置变化，更新list
                mFirstPosition = firstPosition;
                mLastPosition = lastPosition;
                mDtSecCodes = mAdapter.getVisibleDtSecCodes(firstPosition, lastPosition);
            }
            return mDtSecCodes;
        }
        return null;
    }

    public void refreshComplete() {
        mPtrFrame.refreshComplete();
    }

    public void updateStrategeList(final ArrayList<CategoryInfo> strategyList) {
        mHotStockHeader.updateStrategyList(strategyList);
    }

    public void updateBannerList(final ArrayList<DtActivityDetail> bannerList) {
        mHotStockHeader.updateBannerList(bannerList);
    }

    public void updateList(final ArrayList<RecommValueAdded> valueAddedList, final ArrayList<IntelliPickStockEx> pickStockList) {
        mFirstPosition = -1;
        mLastPosition = -1;
        StockPickCardAdapter adapter = mAdapter;
        if (adapter == null) {
            adapter = new StockPickCardAdapter(getActivity(), valueAddedList, pickStockList);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(adapter);
            mAdapter = adapter;
        } else {
            adapter.setData(valueAddedList, pickStockList);
            adapter.notifyDataSetChanged();
        }
    }

    public void updateQuote(final ArrayList<SecSimpleQuote> quotes) {
        if (mAdapter != null) {
            mAdapter.updateQuote(quotes);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more:
                Activity activity = getActivity();
                if(activity != null) {
                    WebBeaconJump.showPickStockFAQ(activity);
                }
                break;
            default:
                break;
        }
    }
}

final class StockPickCardAdapter extends CommonMultiViewTypeAdapter implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final Map<String, SecSimpleQuote> mStockInfoMap;
    private SecSimpleQuoteUpdownComparator mComparator;
    private ArrayList<RecommValueAdded> mValueAddedList;
    private ArrayList<IntelliPickStockEx> mPickStockList;

    StockPickCardAdapter(final Context context, final ArrayList<RecommValueAdded> valueAddedList,
                         final ArrayList<IntelliPickStockEx> pickStockList) {
        super(context, pickStockList, new int[] {R.layout.discover_stock_pick_card_item, R.layout.discover_value_added_card_item});
        mComparator = new SecSimpleQuoteUpdownComparator();
        mStockInfoMap = new HashMap<>();
        mValueAddedList = valueAddedList;
        mPickStockList = pickStockList;
    }

    public void setData(final ArrayList<RecommValueAdded> valueAddedList, final ArrayList<IntelliPickStockEx> pickStockList) {
        mValueAddedList = valueAddedList;
        mPickStockList = pickStockList;
    }

    @Override
    public int getCount() {
        final ArrayList<RecommValueAdded> valueAddedList = mValueAddedList;
        final ArrayList<IntelliPickStockEx> pickStockList = mPickStockList;
        final int valueAddedSize = valueAddedList == null ? 0 : valueAddedList.size();
        final int pickStockSize = pickStockList == null ? 0 : pickStockList.size();
        return valueAddedSize + pickStockSize;
    }

    @Override
    public Object getItem(int position) {
        final ArrayList<RecommValueAdded> valueAddedList = mValueAddedList;
        final ArrayList<IntelliPickStockEx> pickStockList = mPickStockList;
        final int valueAddedSize = valueAddedList == null ? 0 : valueAddedList.size();
        final int pickStockSize = pickStockList == null ? 0 : pickStockList.size();
        if (position < valueAddedSize) {
            return valueAddedList.get(position);
        } else if (position < valueAddedSize + pickStockSize) {
            return pickStockList.get(position - valueAddedSize);
        } else {
            return null;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof IntelliPickStockEx ? 0 : 1;
    }

    public void updateQuote(final ArrayList<SecSimpleQuote> quotes) {
        String dtSecCode;
        for (SecSimpleQuote quote : quotes) {
            dtSecCode = quote.sDtSecCode;
            mStockInfoMap.put(dtSecCode, quote);
        }
        notifyDataSetChanged();
    }

    public ArrayList<String> getVisibleDtSecCodes(final int positionStart, final int positionEnd) {
        if (positionStart < 0 || positionEnd < 0) {
            return null;
        }

        final HashSet<String> codeSet = new HashSet<>();
        for (int i = positionStart; i <= positionEnd; i++) {
            final Object item = getItem(i);
            if (item != null) {
                if (item instanceof IntelliPickStockEx) {
                    final ArrayList<BEC.IntelliStock> stockList = ((IntelliPickStockEx) item).vtIntelliStock;
                    int stockSize = stockList == null ? 0 : stockList.size();
                    if (stockSize > 0) {
                        stockSize = Math.min(3, stockSize); // 只取前3个股票
                        for (int j = 0; j < stockSize; j++) {
                            final IntelliStock stock = stockList.get(j);
                            if (stock != null) {
                                codeSet.add(stock.sDtSecCode);
                            }
                        }
                    }
                } else if (item instanceof RecommValueAdded) {
                    final ArrayList<SecBaseInfo> stockList = ((RecommValueAdded) item).vSecBaseInfo;
                    int stockSize = stockList == null ? 0 : stockList.size();
                    if (stockSize > 0) {
                        stockSize = Math.min(3, stockSize); // 只取前3个股票
                        for (int j = 0; j < stockSize; j++) {
                            final SecBaseInfo stock = stockList.get(j);
                            if (stock != null) {
                                codeSet.add(stock.sDtSecCode);
                            }
                        }
                    }
                }
            }
        }

        return new ArrayList<>(codeSet);
    }

    @Override
    public void convert(CommonViewHolder holder, Object item, int position) {
        if (item instanceof IntelliPickStockEx) {
            final IntelliPickStockEx stockPickItem = (IntelliPickStockEx) item;
            holder.getView(R.id.newIcon).setVisibility(stockPickItem.bNew ? View.VISIBLE : View.GONE);
            holder.setText(R.id.title, stockPickItem.sTitle);
            holder.setText(R.id.winningRateValue, StringUtil.getFormattedFloat(
                    stockPickItem.fSuccPrecent * 100, 2) + '%');
            holder.setText(R.id.subscription, mContext.getString(R.string.subscription_content,
                    StringUtil.getAmountString(stockPickItem.iSubscriptionsCount, 1)));
            holder.setText(R.id.source, stockPickItem.sSource);
            String date = stockPickItem.sDate;
            if (date != null) {
                final int length = date.length();
                if (length >= 10) {// 取后面5个字符
                    date = date.substring(length - 5, length);
                }
            }
            holder.setText(R.id.date, date);

            holder.setText(R.id.stockPickAvgUpdownTitle, stockPickItem.sMaxRetUpBanner);
            holder.setText(R.id.stockPickAvgUpdownValue,
                    StringUtil.getChangePercentStringWithSign(stockPickItem.fMaxRetAvgIncrease, false));

            updateStockInfos(holder, stockPickItem);
        } else if (item instanceof RecommValueAdded) {
            final RecommValueAdded valueAddedItem = (RecommValueAdded) item;
            holder.getView(R.id.newIcon).setVisibility(valueAddedItem.iNewFlag == 1 ? View.VISIBLE : View.GONE);
            holder.setText(R.id.title, valueAddedItem.stValueAddedDesc.sName);
            holder.setText(R.id.desc, valueAddedItem.stValueAddedDesc.sDesc);
            holder.setText(R.id.subscription, mContext.getString(R.string.buy_content,
                    StringUtil.getAmountString(valueAddedItem.stValueAddedDesc.iBuyCount, 1)));
            holder.setText(R.id.source, valueAddedItem.sSource);
            String date = valueAddedItem.sUpdateTime;
            if (date != null) {
                final int length = date.length();
                if (length >= 10) {
                    date = date.substring(5, 10);
                }
            }
            holder.setText(R.id.date, date);

            final TextView button = holder.getView(R.id.button);
            button.setTag(valueAddedItem);
            button.setOnClickListener(this);
            final boolean isBuy = valueAddedItem.iBuyFlag == 1;
            button.setText(isBuy ? R.string.see_reference : R.string.unlock_view);
            button.setBackgroundResource(isBuy ? R.drawable.value_added_button_gray_bg
                    : R.drawable.value_added_button_yellow_bg);

            updateStockInfos2(holder, valueAddedItem.vSecBaseInfo, isBuy);
        }
    }

    private void updateStockInfos2(final CommonViewHolder holder, final ArrayList<SecBaseInfo> stockList, final boolean isBuy) {
        final SecSimpleQuote[] quotes = getRelatedQuotes2(holder, stockList);
        Arrays.sort(quotes, mComparator);

        updateStockInfo2(holder.getView(R.id.discoverStockItem0), quotes[0], quotes, isBuy);
        updateStockInfo2(holder.getView(R.id.discoverStockItem1), quotes[1], quotes, isBuy);
        updateStockInfo2(holder.getView(R.id.discoverStockItem2), quotes[2], quotes, isBuy);
    }

    SecSimpleQuote[] getRelatedQuotes2(final CommonViewHolder holder, final ArrayList<SecBaseInfo> stockList) {
        SecSimpleQuote[] quotes = (SecSimpleQuote[]) holder.tag;
        final int QUOTES_LENGTH = 3;
        if (quotes == null) {
            quotes = new SecSimpleQuote[QUOTES_LENGTH];
            holder.tag = quotes;
        }

        for (int i = 0; i < QUOTES_LENGTH; i++) {
            quotes[i] = getQuote2(i, stockList);
        }

        return quotes;
    }

    SecSimpleQuote getQuote2(int index, ArrayList<SecBaseInfo> stockList) {
        SecSimpleQuote quote = null;
        String dtSecCode;
        if (index < stockList.size()) {
            SecBaseInfo stock = stockList.get(index);
            if (stock != null) {
                dtSecCode = stock.sDtSecCode;
                quote = mStockInfoMap.get(dtSecCode);
                if (quote == null) {
                    quote = DengtaApplication.getApplication().getDataCacheManager().getSecSimpleQuote(dtSecCode);
                    if (quote == null) {
                        quote = new SecSimpleQuote();
                        quote.sSecName = stock.sCHNShortName;
                        quote.sDtSecCode = stock.sDtSecCode;
                    }
                }
            }
        }
        return quote;
    }

    private void updateStockInfo2(final View stockView, final SecSimpleQuote stockQuote, final SecSimpleQuote[] quotes, final boolean isBuy) {

        final TextView nameView = (TextView) stockView.findViewById(R.id.discoverStockName);
        final TextView updownView = (TextView) stockView.findViewById(R.id.discoverStockUpdown);

        CharSequence name = "";
        CharSequence updown = "";

        if (isBuy) {
            if (stockQuote != null) {
                name = stockQuote.sSecName;

                if (stockQuote.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
                    updown = mContext.getResources().getString(R.string.stock_stop);
                } else {
                    updown = StringUtil.getUpDownStringSpannable(stockQuote.fNow, stockQuote.fClose);
                }
                stockView.setTag(stockQuote);
                stockView.setTag(R.id.stock_items, quotes);
                stockView.setOnClickListener(this);

                nameView.setTextColor(ContextCompat.getColor(mContext, R.color.default_text_color_60));
                updownView.setTextColor(ContextCompat.getColor(mContext, R.color.default_text_color_60));
            } else {
                stockView.setTag(null);
                stockView.setTag(R.id.stock_items, null);
                stockView.setOnClickListener(null);
            }
        } else {
            if (stockQuote != null) {
                name = SDkStringUtils.hideSecName(stockQuote.sSecName);
                updown = "+**.**%";

                nameView.setTextColor(ContextCompat.getColor(mContext, R.color.stock_red_color));
                updownView.setTextColor(ContextCompat.getColor(mContext, R.color.stock_red_color));
            }
            stockView.setOnClickListener(null);
            stockView.setClickable(false);
        }

        nameView.setText(name);
        updownView.setText(updown);
    }

    private void updateStockInfos(final CommonViewHolder holder, final IntelliPickStockEx item) {
        final ArrayList<BEC.IntelliStock> stockList = item.vtIntelliStock;

        final SecSimpleQuote[] quotes = getRelatedQuotes(holder, stockList);
        Arrays.sort(quotes, mComparator);

        updateStockInfo(holder.getView(R.id.discoverStockItem0), quotes[0], quotes);
        updateStockInfo(holder.getView(R.id.discoverStockItem1), quotes[1], quotes);
        updateStockInfo(holder.getView(R.id.discoverStockItem2), quotes[2], quotes);
    }

    SecSimpleQuote[] getRelatedQuotes(final CommonViewHolder holder, final ArrayList<BEC.IntelliStock> stockList) {
        SecSimpleQuote[] quotes = (SecSimpleQuote[]) holder.tag;
        final int QUOTES_LENGTH = 3;
        if (quotes == null) {
            quotes = new SecSimpleQuote[QUOTES_LENGTH];
            holder.tag = quotes;
        }

        for (int i = 0; i < QUOTES_LENGTH; i++) {
            quotes[i] = getQuote(i, stockList);
        }

        return quotes;
    }

    SecSimpleQuote getQuote(int index, ArrayList<BEC.IntelliStock> stockList) {
        SecSimpleQuote quote = null;
        String dtSecCode;
        if (index < stockList.size()) {
            IntelliStock intelliStock = stockList.get(index);
            if (intelliStock != null) {
                dtSecCode = intelliStock.sDtSecCode;
                quote = mStockInfoMap.get(dtSecCode);
                if (quote == null) {
                    quote = DengtaApplication.getApplication().getDataCacheManager().getSecSimpleQuote(dtSecCode);
                    if (quote == null) {
                        quote = new SecSimpleQuote();
                        quote.sSecName = intelliStock.sSecName;
                        quote.sDtSecCode = intelliStock.sDtSecCode;
                    }
                }
            }
        }
        return quote;
    }

    private void updateStockInfo(final View stockView, final SecSimpleQuote stockQuote, final SecSimpleQuote[] quotes) {

        final TextView nameView = (TextView) stockView.findViewById(R.id.discoverStockName);
        final TextView updownView = (TextView) stockView.findViewById(R.id.discoverStockUpdown);

        CharSequence name = "";
        CharSequence updown = "";
        if (stockQuote != null) {
            name = stockQuote.sSecName;

            if (stockQuote.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
                updown = mContext.getResources().getString(R.string.stock_stop);
            } else {
                updown = StringUtil.getUpDownStringSpannable(stockQuote.fNow, stockQuote.fClose);
            }
            stockView.setTag(stockQuote);
            stockView.setTag(R.id.stock_items, quotes);
            stockView.setOnClickListener(this);
        } else {
            stockView.setTag(null);
            stockView.setTag(R.id.stock_items, null);
            stockView.setOnClickListener(null);
        }

        nameView.setText(name);
        updownView.setText(updown);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        final int id = v.getId();
        if (id == R.id.button) {
            final RecommValueAdded valueAddedItem = (RecommValueAdded) v.getTag();
            if (valueAddedItem != null) {
                final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                if (scheme != null) {
                    final String url = valueAddedItem.iBuyFlag == 1 ? valueAddedItem.sDetailUrl : valueAddedItem.sUrl;
                    scheme.handleUrl(mContext, url);
                }
                StatisticsUtil.reportAction(StatisticsConst.DISCOVER_STOCK_PICK_CLICK_VALUE_ADDED_BUTTON);
            }
        } else {
            final SecSimpleQuote stockInfo = (SecSimpleQuote) v.getTag();
            if (stockInfo != null) {
                final SecSimpleQuote[] quotes = (SecSimpleQuote[]) v.getTag(R.id.stock_items);
                final ArrayList<SecListItem> secItems = SecListItemUtils.getSecListFromSecSimpleQuoteList(quotes);
                CommonBeaconJump.showSecurityDetailActivity(mContext, stockInfo.sDtSecCode, stockInfo.sSecName, secItems);
            }
            StatisticsUtil.reportAction(StatisticsConst.DISCOVER_STOCK_PICK_CLICK_STOCK);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StatisticsUtil.reportAction(StatisticsConst.DISCOVER_STOCK_PICK_CLICK_CARD);

        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        final CommonViewHolder holder = (CommonViewHolder) view.getTag();
        if (holder == null) {
            return;
        }

        final Object item = getItem(holder.getPosition());
        if (item == null) {
            return;
        }

        final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
        if (item instanceof IntelliPickStockEx) {
            if (scheme != null) {
                scheme.handleUrl(mContext, ((IntelliPickStockEx) item).sUrl);
            }
        } else if (item instanceof RecommValueAdded) {
            if (scheme != null) {
                scheme.handleUrl(mContext, ((RecommValueAdded) item).sUrl);
            }
            StatisticsUtil.reportAction(StatisticsConst.DISCOVER_STOCK_PICK_CLICK_VALUE_ADDED_CARD);
        }
    }
}
