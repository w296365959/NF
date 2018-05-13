package com.sscf.investment.market.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.sscf.investment.component.ui.widget.ToolsItem;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.market.MarketToolsItem;
import com.sscf.investment.market.StockTurnoverRateListActivity;
import com.sscf.investment.market.StockUpDownListActivity;
import com.sscf.investment.market.view.MarketChinaIndexFuturesHeader;
import com.sscf.investment.market.view.MarketChinaPlateHeader;
import com.sscf.investment.market.view.MarketChinaWarningHeader;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.utils.SecListItemUtils;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.MultiTagView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import BEC.BEACON_STAT_TYPE;
import BEC.E_MARKET_TYPE;
import BEC.E_SEC_ATTR;
import BEC.PlateQuoteDesc;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by liqf on 2015/8/10.
 * 行情沪深界面
 */
public final class MarketChinaFragment extends BaseFragment implements Handler.Callback, Runnable, PtrHandler {
    public static final int DEFAULT_STOCK_LIST_COUNT = 10;

    public static final int MSG_UPDATE_DATA = 1;
    public static final int MSG_HANDLE_FAILED = 2;

    private ExpandableListView mListView;
    private MarketChinaListAdapter mAdapter;
    private PtrFrameLayout mPtrFrame;

    private GetDataCallback mGetStockIncreaseListCallback;
    private GetDataCallback mGetStockDecreaseListCallback;
    private GetDataCallback mGetStockTurnoverRateListCallback;

    public static final int GET_DATA_TYPE_STOCK_INCREASE_LIST = 1;
    public static final int GET_DATA_TYPE_STOCK_DECREASE_LIST = 2;
    public static final int GET_DATA_TYPE_STOCK_TURNOVER_RATE_LIST = 3;

    //设置组视图的显示文字
    private String[] mGroupTitles;
    private ArrayList<ArrayList<PlateQuoteDesc>> mInfoArray;

    private ArrayList<SecListItem>[] mSecList;

    private Handler mHandler;
    private PeriodicHandlerManager mPeriodicHandlerManager;

    private MarketChinaIndexFuturesHeader mIndexFuturesHeader;
    private MarketChinaPlateHeader mIndustryPlateHeader;
    private MarketChinaPlateHeader mConceptPlateHeader;
    MarketChinaWarningHeader mWarningHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View contextView = inflater.inflate(R.layout.ptr_expandable_list, container, false);

        mGroupTitles = getResources().getStringArray(R.array.market_china_group_title);
        mInfoArray = new ArrayList<>(mGroupTitles.length);
        mSecList = new ArrayList[mGroupTitles.length];

        mHandler = new Handler(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        mGetStockIncreaseListCallback = new GetDataCallback(GET_DATA_TYPE_STOCK_INCREASE_LIST);
        mGetStockDecreaseListCallback = new GetDataCallback(GET_DATA_TYPE_STOCK_DECREASE_LIST);
        mGetStockTurnoverRateListCallback = new GetDataCallback(GET_DATA_TYPE_STOCK_TURNOVER_RATE_LIST);

        initViews(contextView);
        registerBroadcastReceiver();
        return contextView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcastReceiver();
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_MARKET_QUOTE_PAGE);
        helper.setKey(StatConsts.CHINA);
        return helper;
    }

    private void initViews(final View root) {
        mPtrFrame = (PtrFrameLayout) root.findViewById(R.id.ptr);
        mPtrFrame.setPtrHandler(this);

        mListView = (ExpandableListView) root.findViewById(R.id.list);
        mListView.setGroupIndicator(null);
        mListView.setDivider(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.list_divider)));
        mListView.setChildDivider(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.list_divider)));
        mListView.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.list_divider_height));

        initIndexFutures();
        initWarningHeader(root);
        initToolsHeader();
        initIndustryPlate();
        initConceptPlate();

        // 强烈注意：在android4.3以下的rom，添加header或footer一定要在setAdapter之前，否则容易出现crash
        mAdapter = new MarketChinaListAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnChildClickListener(mAdapter);
        expandAllGroup();
    }

    private void initIndexFutures() {
        mIndexFuturesHeader = (MarketChinaIndexFuturesHeader) View.inflate(getActivity(), R.layout.market_china_index_infos, null);
        mListView.addHeaderView(mIndexFuturesHeader);
    }

    private void initWarningHeader(final View root) {
        mWarningHeader = (MarketChinaWarningHeader) View.inflate(getActivity(), R.layout.market_china_warning_header, null);
        mListView.addHeaderView(mWarningHeader);
        mListView.setOnTouchListener(mWarningHeader);
    }

    private void initToolsHeader() {
        final Context context = getActivity();
        final RecyclerView recyclerView = (RecyclerView) View.inflate(context, R.layout.recycler_view, null);
        mListView.addHeaderView(recyclerView);
        recyclerView.setPadding(0, context.getResources().getDimensionPixelSize(R.dimen.market_block_margin), 0, 0);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        final ArrayList<ToolsItem> items = new ArrayList<>(4);
        items.add(MarketToolsItem.createCapitalFlow());
        items.add(MarketToolsItem.createDragonTigerBillboard());
        items.add(MarketToolsItem.createMarketMargin());
        items.add(MarketToolsItem.createNewShareCenter());
        recyclerView.setAdapter(new ToolsAdapter(context, items, R.layout.market_china_tools_item));
    }

    private void initIndustryPlate() {
        mIndustryPlateHeader = (MarketChinaPlateHeader) View.inflate(getActivity(), R.layout.market_china_plate_header, null);
        mIndustryPlateHeader.setPlateType(MarketChinaPlateHeader.PLATE_TYPE_INDUSTRY);
        mListView.addHeaderView(mIndustryPlateHeader);
    }

    private void initConceptPlate() {
        mConceptPlateHeader = (MarketChinaPlateHeader) View.inflate(getActivity(), R.layout.market_china_plate_header, null);
        mConceptPlateHeader.setPlateType(MarketChinaPlateHeader.PLATE_TYPE_CONCEPT);
        mListView.addHeaderView(mConceptPlateHeader);
    }

    private void expandAllGroup() {
        for(int i = 0; i < mAdapter.getGroupCount(); i++){
            mListView.expandGroup(i);

        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if (mListView.getVisibility() == View.VISIBLE) {
            boolean notOnTop = mListView.getChildCount() > 0
                    && (mListView.getFirstVisiblePosition() > 0
                    || mListView.getChildAt(0).getTop() < mListView.getPaddingTop());
            return !notOnTop;
        }
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(final PtrFrameLayout frame) {
        StatisticsUtil.reportAction(StatisticsConst.MARKET_PULL_REFRESH);
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
            mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
            return;
        }
        mPeriodicHandlerManager.runPeriodic();
        mWarningHeader.requestMarketWarning();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        mTrading = TradingStateManager.getInstance().isTradingOrCallauction(E_MARKET_TYPE.E_MT_SH);
        mWarningHeader.requestMarketWarning();
        mPeriodicHandlerManager.runPeriodic();
        StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_DISPLAY);
        ((View)getView().getParent()).setOnTouchListener(mWarningHeader);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        mWarningHeader.requestMarketWarning();
        mPeriodicHandlerManager.runPeriodic();
        StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_DISPLAY);
    }

    @Override
    public void onFirstUserInvisible() {
        super.onFirstUserInvisible();
        if (mPeriodicHandlerManager != null) {
            mPeriodicHandlerManager.stop();
        }
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        mPeriodicHandlerManager.stop();
    }

    @Override
    public void run() {
        requestData();
        mWarningHeader.requestData();
        mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
        if (!mTrading) {
            mPeriodicHandlerManager.stop();
        }
    }

    private void requestData() {
        mIndexFuturesHeader.requestData();
        final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance().getManager(IMarketManager.class.getName());
        if (marketManager != null) {
            mIndustryPlateHeader.requestData(marketManager);
            mConceptPlateHeader.requestData(marketManager);
            marketManager.requestStockIncreaseList(DEFAULT_STOCK_LIST_COUNT, mGetStockIncreaseListCallback);
            marketManager.requestStockDecreaseList(DEFAULT_STOCK_LIST_COUNT, mGetStockDecreaseListCallback);
            marketManager.requestStockTurnoverRateList(DEFAULT_STOCK_LIST_COUNT, mGetStockTurnoverRateListCallback);
        }
    }

    /**
     * 获得热门行业，涨幅榜，跌幅榜，换手率榜的数据的callback
     */
    private final class GetDataCallback implements OnGetDataCallback<ArrayList<PlateQuoteDesc>> {
        private final int mType;
        GetDataCallback(int type) {
            mType = type;
        }

        @Override
        public void onGetData(ArrayList<PlateQuoteDesc> stockList) {
            if (stockList != null) {
                mHandler.obtainMessage(MSG_UPDATE_DATA, mType, 0, stockList).sendToTarget();
                // 给切换个股详情页用的
                int index;
                switch (mType) {
                    case GET_DATA_TYPE_STOCK_INCREASE_LIST:
                        index = 0;
                        break;
                    case GET_DATA_TYPE_STOCK_DECREASE_LIST:
                        index = 1;
                        break;
                    case GET_DATA_TYPE_STOCK_TURNOVER_RATE_LIST:
                        index = 2;
                        break;
                    default:
                        return;
                }
                final ArrayList<SecListItem> secList = SecListItemUtils.getSecListFromPlateQuoteDescList(stockList);
                mSecList[index] = secList;
            } else {
                mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_DATA:
                mPtrFrame.refreshComplete();
                updateData((ArrayList<PlateQuoteDesc>) msg.obj, msg.arg1);
                break;
            case MSG_HANDLE_FAILED:
                mPtrFrame.refreshComplete();
                break;
            default:
                break;
        }
        return false;
    }

    private void updateData(ArrayList<PlateQuoteDesc> data, int type) {
        switch (type) {
            case GET_DATA_TYPE_STOCK_INCREASE_LIST:
                mInfoArray.remove(0);
                mInfoArray.add(0, data);
                mAdapter.notifyDataSetChanged();
                break;
            case GET_DATA_TYPE_STOCK_DECREASE_LIST:
                mInfoArray.remove(1);
                mInfoArray.add(1, data);
                mAdapter.notifyDataSetChanged();
                break;
            case GET_DATA_TYPE_STOCK_TURNOVER_RATE_LIST:
                mInfoArray.remove(2);
                mInfoArray.add(2, data);
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private final class MarketChinaListAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener, View.OnClickListener {
        private final LayoutInflater mInflater;

        public MarketChinaListAdapter() {
            final Activity activity = getActivity();
            mInflater = LayoutInflater.from(activity);
            final ArrayList<PlateQuoteDesc> empty = new ArrayList<>(0);

            mInfoArray.add(empty);
            mInfoArray.add(empty);
            mInfoArray.add(empty);
        }

        @Override
        public int getGroupCount() {
            return mGroupTitles.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mInfoArray.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroupTitles[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mInfoArray.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String group = (String) getGroup(groupPosition);
            if (convertView == null) {
                final ViewGroup groupView = new FrameLayout(getActivity());
                groupView.addView(mInflater.inflate(R.layout.market_listview_group, null));
                convertView = groupView;
            }
            TextView title = (TextView) convertView.findViewById(R.id.group_title);
            ImageView expandImage = (ImageView) convertView.findViewById(R.id.group_expand_image);
            if (isExpanded) {
                expandImage.setImageResource(R.drawable.list_group_expanded);
                convertView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.market_block_margin), 0, 0);
            } else {
                expandImage.setImageResource(R.drawable.list_group_collapsed);
                convertView.setPadding(0, 0, 0, 0);
            }
            title.setText(group);
            final View moreView = convertView.findViewById(R.id.group_more);
            moreView.setOnClickListener(this);
            moreView.setTag(groupPosition);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView title, code, price, deltaPercent;
            MultiTagView multiTagView;
            PlateQuoteDesc childItem = (PlateQuoteDesc) getChild(groupPosition, childPosition);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.market_stock_list_item, null);
                ViewHolder viewHolder = new ViewHolder();
                convertView.setTag(viewHolder);
                title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.mTitle = title;
                code = (TextView) convertView.findViewById(R.id.code);
                viewHolder.mCode = code;
                price = (TextView) convertView.findViewById(R.id.price);
                viewHolder.mPrice = price;
                deltaPercent = (TextView) convertView.findViewById(R.id.delta_percent);
                viewHolder.mDeltaPercent = deltaPercent;
                multiTagView = (MultiTagView) convertView.findViewById(R.id.multiTagView);
                viewHolder.mMultiTagView = multiTagView;
                viewHolder.mItem = childItem;
            } else {
                ViewHolder viewHolder = (ViewHolder) convertView.getTag();
                title = viewHolder.mTitle;
                code = viewHolder.mCode;
                price = viewHolder.mPrice;
                deltaPercent = viewHolder.mDeltaPercent;
                multiTagView = viewHolder.mMultiTagView;
                viewHolder.mItem = childItem;
            }

            title.setText(childItem.sSecName);

            code.setText(StockUtil.convertSecInfo(childItem.sDtSecCode).getSSecCode());

            price.setText(childItem.fNow > 0 ? StringUtil.getFormattedFloat(childItem.fNow, childItem.iTpFlag) : "--");

            if (groupPosition == 2) {
                deltaPercent.setText(StringUtil.getFormattedFloat(childItem.fFhsl * 100, childItem.iTpFlag) + '%');
            } else {
                deltaPercent.setText(StringUtil.getUpDownStringSpannable(childItem.fNow, childItem.fClose));
            }

            // 是否显示次新股的tag
            if (childItem.stSecAttr != null && childItem.stSecAttr.mSecAttr != null) {
                Map<Integer, String> map = childItem.stSecAttr.mSecAttr;
                if (map.size() > 0) {
                    final ArrayList<String> tags = new ArrayList<>(1);
                    final Resources resources = getResources();
                    if (map.containsKey(E_SEC_ATTR.E_SEC_ATTR_SUB_NEW)) {
                        tags.add(resources.getString(R.string.sub_new));
                    } else if (map.containsKey(E_SEC_ATTR.E_SEC_ATTR_NEW)) {
                        tags.add(resources.getString(R.string.new_stock));
                    }

                    multiTagView.addTags(tags, R.drawable.tag_round_rect_bg_position, R.color.tab_indicatorColor);
                } else {
                    multiTagView.clearAllTags();
                }
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return 0;
        }

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            final ArrayList<SecListItem> secList = mSecList[groupPosition];
            final ViewHolder viewHolder = (ViewHolder) v.getTag();
            final PlateQuoteDesc item = viewHolder.mItem;
            CommonBeaconJump.showSecurityDetailActivity(getActivity(), item.getSDtSecCode(), item.getSSecName(), secList);
            switch (groupPosition) {
                case 0:
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_A_SHARES_INCREASE_LIST_STOCK);
                    break;
                case 1:
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_A_SHARES_DECREASE_LIST_STOCK);
                    break;
                case 2:
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_TURNOVER_RATE_LIST_STOCK);
                    break;
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            Activity activity = getActivity();
            if (activity == null) {
                return;
            }
            final Integer groupPosition = (Integer) v.getTag();
            switch (groupPosition) {
                case 0:
                    StockUpDownListActivity.showASharesIncreaseList(activity);
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_MORE_A_SHARES_INCREASE_LIST);
                    break;
                case 1:
                    StockUpDownListActivity.showASharesDecreaseList(activity);
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_MORE_A_SHARES_DECREASE_LIST);
                    break;
                case 2:
                    activity.startActivity(new Intent(activity, StockTurnoverRateListActivity.class));
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_MORE_TURNOVER_RATE_LIST);
                    break;
                default:
                    break;
            }
        }
    }

    private static final class ViewHolder {
        TextView mTitle;
        TextView mCode;
        TextView mPrice;
        TextView mDeltaPercent;
        MultiTagView mMultiTagView;
        PlateQuoteDesc mItem;
    }

    private BroadcastReceiver mReceiver;
    private boolean mTrading;
    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new MarketChinaReceiver();
            final IntentFilter filter = new IntentFilter(TradingStateManager.ACTION_TRADING_STATE_UPDATED);
            filter.addAction(TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED);
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).registerReceiver(mReceiver, filter);
        }
    }

    private void unregisterBroadcastReceiver() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).unregisterReceiver(mReceiver);
        }
    }

    private final class MarketChinaReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED.equals(action)) {
                switchTradingState();
            } else if (TradingStateManager.ACTION_TRADING_STATE_UPDATED.equals(action)) {
                switchTradingState();
            }
        }
    }

    private void switchTradingState() {
        final boolean trading = TradingStateManager.getInstance().isTradingOrCallauction(E_MARKET_TYPE.E_MT_SH);
        if (mTrading != trading) {
            mTrading = trading;
            if (trading) {
                mPeriodicHandlerManager.runPeriodic();
            }
        }
    }
}

final class ToolsAdapter extends CommonBaseRecyclerViewAdapter<ToolsItem> {
    ToolsAdapter(Context context, List<ToolsItem> data, int itemLayoutId) {
        super(context, data, itemLayoutId);
        setItemClickable(true);
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, ToolsItem item, int position) {
        final TextView text = holder.getView(R.id.text);
        text.setText(item.textId);
        text.setCompoundDrawablesWithIntrinsicBounds(0, item.drawableId, 0, 0);
        holder.getView(R.id.newIcon).setVisibility(item.isNew ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        final ToolsItem item = getItem(position);
        if (item != null) {
            item.click(mContext);
        }
    }
}
