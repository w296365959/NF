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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.sscf.investment.R;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.market.AHPremiumStockListActivity;
import com.sscf.investment.market.StockUpDownListActivity;
import com.sscf.investment.market.view.MarketSHHKConnectBalanceHeader;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.DataUtils;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.utils.SecListItemUtils;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.MultiTagView;
import java.util.ArrayList;
import java.util.Map;
import BEC.AHPlateDesc;
import BEC.BEACON_STAT_TYPE;
import BEC.E_MARKET_TYPE;
import BEC.E_SEC_ATTR;
import BEC.PlateQuoteDesc;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by davidwei on 2015/10/13.
 * 沪港通行情界面
 */
public final class MarketSHHKConnectFragment extends BaseFragment implements Handler.Callback,
        Runnable, PtrHandler, OnGetDataCallback<ArrayList<AHPlateDesc>> {
    private ExpandableListView mListView;
    private MarketSHHKConnectListAdapter mAdapter;
    private PtrFrameLayout mPtrFrame;
    private MarketSHHKConnectBalanceHeader mBalanceHeader;

    private static final int TYPE_COUNT = 2;

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_AH_STOCK = 1;
    private static final int MSG_HANDLE_FAILED = 2;

    private Handler mHandler;
    private PeriodicHandlerManager mPeriodicHandlerManager;

    public static final int DEFAULT_STOCK_LIST_COUNT = 10;

    private GetDataCallback mGetSHConnectStockIncreaseListCallback;
    private GetDataCallback mGetHKConnectStockIncreaseListCallback;

    public static final int GET_DATA_TYPE_SH_CONNECT_STOCK_INCREASE_LIST = 1;
    public static final int GET_DATA_TYPE_HK_CONNECT_STOCK_INCREASE_LIST = 2;

    //设置组视图的显示文字
    private String[] mGroupTitles;
    private ArrayList<ArrayList<PlateQuoteDesc>> mInfoArray;
    private ArrayList<AHPlateDesc> mAHPremiumArray;
    private ArrayList<SecListItem>[] mSecList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.ptr_expandable_list, container, false);
        mHandler = new Handler(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        mGroupTitles = getResources().getStringArray(R.array.market_sh_hk_group_title);
        mInfoArray = new ArrayList<ArrayList<PlateQuoteDesc>>(mGroupTitles.length);
        mAHPremiumArray = new ArrayList<AHPlateDesc>(0);
        mSecList = new ArrayList[mGroupTitles.length];
        mGetSHConnectStockIncreaseListCallback = new GetDataCallback(GET_DATA_TYPE_SH_CONNECT_STOCK_INCREASE_LIST);
        mGetHKConnectStockIncreaseListCallback = new GetDataCallback(GET_DATA_TYPE_HK_CONNECT_STOCK_INCREASE_LIST);
        initViews(root);

        requestData();
        registerBroadcastReceiver();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcastReceiver();
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_MARKET_QUOTE_PAGE);
        helper.setKey(StatConsts.SHANGHAI_HONGKONG);
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

        initSHHKConnectBalanceHeader();

        mAdapter = new MarketSHHKConnectListAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnChildClickListener(mAdapter);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                StatisticsUtil.reportAction(StatisticsConst.A_MARKET_HUGANGTONG_CHNINDEX_SHOW);
                return false;
            }
        });
        expandAllGroup();
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
    }

    private void initSHHKConnectBalanceHeader() {
        mBalanceHeader = (MarketSHHKConnectBalanceHeader) View.inflate(getActivity(), R.layout.market_sh_hk_connect_balance_header, null);
        mListView.addHeaderView(mBalanceHeader);
    }

    private void expandAllGroup() {
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mListView.expandGroup(i);
        }
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        final TradingStateManager tradingStateManager = TradingStateManager.getInstance();
        mTrading = tradingStateManager.isTradingOrCallauction(E_MARKET_TYPE.E_MT_SH)
                || tradingStateManager.isTradingOrCallauction(E_MARKET_TYPE.E_MT_HK);
        mPeriodicHandlerManager.runPeriodic();
        StatisticsUtil.reportAction(StatisticsConst.MARKET_SH_HK_DISPLAY);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        mPeriodicHandlerManager.runPeriodic();
        StatisticsUtil.reportAction(StatisticsConst.MARKET_SH_HK_DISPLAY);
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
        mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
        if (!mTrading) {
            mPeriodicHandlerManager.stop();
        }
    }

    private void requestData() {
        final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance().getManager(IMarketManager.class.getName());
        if (marketManager != null) {
            mBalanceHeader.requestData(marketManager);
            marketManager.requestSHConnectStockIncreaseList(DEFAULT_STOCK_LIST_COUNT, mGetSHConnectStockIncreaseListCallback);
            marketManager.requestHKConnectStockIncreaseList(DEFAULT_STOCK_LIST_COUNT, mGetHKConnectStockIncreaseListCallback);
            marketManager.requestAHStockIncreaseList(DEFAULT_STOCK_LIST_COUNT, this);
        }
    }

    /**
     * AH股溢价请求的回调
     */
    @Override
    public void onGetData(ArrayList<AHPlateDesc> stockList) {
        if (stockList != null) {
            mHandler.obtainMessage(TYPE_AH_STOCK, stockList).sendToTarget();
        }
    }

    /**
     * 沪股通与港股通的请求的回调
     */
    private final class GetDataCallback implements OnGetDataCallback<ArrayList<PlateQuoteDesc>> {
        private final int mType;
        GetDataCallback(int type) {
            mType = type;
        }

        @Override
        public void onGetData(ArrayList<PlateQuoteDesc> stockList) {
            if (stockList != null) {
                mHandler.obtainMessage(TYPE_NORMAL, mType, 0, stockList).sendToTarget();
                // 给切换个股详情页用的
                int index = -1;
                switch (mType) {
                    case GET_DATA_TYPE_SH_CONNECT_STOCK_INCREASE_LIST:
                        index = 0;
                        break;
                    case GET_DATA_TYPE_HK_CONNECT_STOCK_INCREASE_LIST:
                        index = 1;
                        break;
                    default:
                        return;
                }
                mSecList[index] = SecListItemUtils.getSecListFromPlateQuoteDescList(stockList);
            } else {
                mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case TYPE_NORMAL:
                mPtrFrame.refreshComplete();
                updateData((ArrayList<PlateQuoteDesc>) msg.obj, msg.arg1);
                break;
            case TYPE_AH_STOCK:
                mPtrFrame.refreshComplete();
                updateAHPremiumData((ArrayList<AHPlateDesc>) msg.obj);
                break;
            case MSG_HANDLE_FAILED:
                mPtrFrame.refreshComplete();
                break;
            default:
                break;
        }

        return false;
    }

    private void updateAHPremiumData(ArrayList<AHPlateDesc> data) {
        mAHPremiumArray = data;
        mAdapter.notifyDataSetChanged();
    }

    private void updateData(ArrayList<PlateQuoteDesc> data, int type) {
        switch (type) {
            case GET_DATA_TYPE_SH_CONNECT_STOCK_INCREASE_LIST:
                mInfoArray.remove(0);
                mInfoArray.add(0, data);
                mAdapter.notifyDataSetChanged();
                break;
            case GET_DATA_TYPE_HK_CONNECT_STOCK_INCREASE_LIST:
                mInfoArray.remove(1);
                mInfoArray.add(1, data);
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private final class MarketSHHKConnectListAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener, View.OnClickListener {
        private final LayoutInflater mInflater;

        public MarketSHHKConnectListAdapter() {
            final Activity activity = getActivity();
            mInflater = LayoutInflater.from(activity);
            final ArrayList<PlateQuoteDesc> empty = new ArrayList<PlateQuoteDesc>(0);

            mInfoArray.add(empty);
            mInfoArray.add(empty);
        }

        @Override
        public int getGroupCount() {
            return mGroupTitles.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (getChildType(groupPosition, 0)) {
                case TYPE_NORMAL:
                    return mInfoArray.get(groupPosition).size();
                case TYPE_AH_STOCK:
                    return mAHPremiumArray.size();
                default:
                    return 0;
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroupTitles[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (getChildType(groupPosition, childPosition)) {
                case TYPE_NORMAL:
                    return mInfoArray.get(groupPosition).get(childPosition);
                case TYPE_AH_STOCK:
                    return mAHPremiumArray.get(childPosition);
                default:
                    return null;
            }
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
        public int getGroupType(int groupPosition) {
            return groupPosition == 2 ? TYPE_AH_STOCK : TYPE_NORMAL;
        }

        @Override
        public int getGroupTypeCount() {
            return TYPE_COUNT;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String group = (String) getGroup(groupPosition);
            LinearLayout groupView = (LinearLayout) convertView;
            final int groupType = getGroupType(groupPosition);
            if (convertView == null) {
                groupView = new LinearLayout(getActivity());
                groupView.setOrientation(LinearLayout.VERTICAL);
                switch (groupType) {
                    case TYPE_NORMAL:
                        groupView.addView(mInflater.inflate(R.layout.market_listview_group, null));
                        break;
                    case TYPE_AH_STOCK:
                        groupView.addView(mInflater.inflate(R.layout.market_listview_group, null));
                        final View header = mInflater.inflate(R.layout.market_ah_stock_list_header, null);
                        header.setBackgroundDrawable(null);
                        groupView.addView(header);
                        break;
                }
                convertView = groupView;
            }

            TextView title = (TextView) groupView.findViewById(R.id.group_title);
            ImageView expandImage = (ImageView) groupView.findViewById(R.id.group_expand_image);
            if (isExpanded) {
                expandImage.setImageResource(R.drawable.list_group_expanded);
                groupView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.market_block_margin), 0, 0);
                if (groupType == TYPE_AH_STOCK) {
                    groupView.findViewById(R.id.marketAHStockListHeader).setVisibility(View.VISIBLE);
                }
            } else {
                expandImage.setImageResource(R.drawable.list_group_collapsed);
                groupView.setPadding(0, 0, 0, 0);
                if (groupType == TYPE_AH_STOCK) {
                    groupView.findViewById(R.id.marketAHStockListHeader).setVisibility(View.GONE);
                }
            }
            title.setText(group);
            final View moreView = groupView.findViewById(R.id.group_more);
            moreView.setOnClickListener(this);
            moreView.setTag(groupPosition);
            return convertView;
        }

        @Override
        public int getChildType(int groupPosition, int childPosition) {
            return groupPosition == 2 ? TYPE_AH_STOCK : TYPE_NORMAL;
        }

        @Override
        public int getChildTypeCount() {
            return TYPE_COUNT;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            switch (getChildType(groupPosition, childPosition)) {
                case TYPE_NORMAL:
                    return getNormalChildView(groupPosition, childPosition, isLastChild, convertView, parent);
                case TYPE_AH_STOCK:
                    return getAHStockChildView(groupPosition, childPosition, isLastChild, convertView, parent);
                default:
                    return null;
            }
        }

        public View getNormalChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView title, code, price, deltaPercent;
            ImageView tagView;
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
                tagView = (ImageView) convertView.findViewById(R.id.stock_tag_icon);
                viewHolder.mTagView = tagView;
                multiTagView = (MultiTagView) convertView.findViewById(R.id.multiTagView);
                viewHolder.mMultiTagView = multiTagView;
                viewHolder.mItem = childItem;
            } else {
                ViewHolder viewHolder = (ViewHolder) convertView.getTag();
                title = viewHolder.mTitle;
                code = viewHolder.mCode;
                price = viewHolder.mPrice;
                deltaPercent = viewHolder.mDeltaPercent;
                tagView = viewHolder.mTagView;
                multiTagView = viewHolder.mMultiTagView;
                viewHolder.mItem = childItem;
            }

            title.setText(childItem.sSecName);

            code.setText(StockUtil.convertSecInfo(childItem.sDtSecCode).getSSecCode());

            price.setText(childItem.fNow > 0 ? StringUtil.getFormattedFloat(childItem.fNow, childItem.iTpFlag) : "--");

            if (groupPosition == 2) {
                deltaPercent.setText(DataUtils.rahToStr(childItem.fFhsl * 100) + '%');
            } else {
                deltaPercent.setText(StringUtil.getUpDownStringSpannable(childItem.fNow, childItem.fClose));
            }

            StockUtil.updateStockTagIcon(tagView, childItem.sDtSecCode);

            // 是否显示次新股的tag
            if (groupPosition == 0) {
                if (childItem.stSecAttr != null && childItem.stSecAttr.mSecAttr != null) {
                    Map<Integer, String> map = childItem.stSecAttr.mSecAttr;
                    if (map.size() > 0) {
                        final ArrayList<String> tags = new ArrayList<String>(1);
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
            } else {
                multiTagView.clearAllTags();
            }

            return convertView;
        }

        public View getAHStockChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView title, aSharesInfoView, hSharesInfoView, premiumInfoView;
            final AHPlateDesc childItem = (AHPlateDesc) getChild(groupPosition, childPosition);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.market_ah_stock_list_item, null);
                AHPremiumViewHolder viewHolder = new AHPremiumViewHolder();
                convertView.setTag(viewHolder);
                title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.title = title;
                hSharesInfoView = (TextView) convertView.findViewById(R.id.hSharesInfo);
                viewHolder.hSharesInfoView = hSharesInfoView;
                aSharesInfoView = (TextView) convertView.findViewById(R.id.aSharesInfo);
                viewHolder.aSharesInfoView = aSharesInfoView;
                premiumInfoView = (TextView) convertView.findViewById(R.id.premiumInfo);
                viewHolder.premiumInfoView = premiumInfoView;
                viewHolder.item = childItem;
            } else {
                AHPremiumViewHolder viewHolder = (AHPremiumViewHolder) convertView.getTag();
                title = viewHolder.title;
                hSharesInfoView = viewHolder.hSharesInfoView;
                aSharesInfoView = viewHolder.aSharesInfoView;
                premiumInfoView = viewHolder.premiumInfoView;
                viewHolder.item = childItem;
            }

            title.setText(childItem.sASecName);

            hSharesInfoView.setText(StringUtil.getAHInfoStringSpannable(childItem.fHKNow, childItem.fHKIncrease / 100));

            aSharesInfoView.setText(StringUtil.getAHInfoStringSpannable(childItem.fANow, childItem.fAIncrease));

            float exchangeRate = 0.8f;
            final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance().getManager(IMarketManager.class.getName());
            if (marketManager != null) {
                exchangeRate = marketManager.getHKDollarsExchangeRate();
            }
            premiumInfoView.setText(StringUtil.getAHPremiumStringSpannable(childItem.fHKNow, childItem.fANow, exchangeRate));

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            switch (getChildType(groupPosition, childPosition)) {
                case TYPE_NORMAL:
                    clickNormalChild(v, groupPosition);
                    break;
                case TYPE_AH_STOCK:
                    clickAhChild(v);
                    break;
                default:
                    break;
            }
            return true;
        }

        private void clickNormalChild(View v, int groupPosition) {
            final Activity activity = getActivity();
            if (activity == null) {
                return;
            }

            final PlateQuoteDesc item = ((ViewHolder) v.getTag()).mItem;
            final ArrayList<SecListItem> secList = mSecList[groupPosition];
            final String dtCode = item.sDtSecCode;
            final String name = item.sSecName;
            CommonBeaconJump.showSecurityDetailActivity(activity, dtCode, name, secList);

            switch (groupPosition) {
                case 0:
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_SH_HK_CONNECT_CLICK_SH_INCREASE_LIST_STOCK);
                    break;
                case 1:
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_SH_HK_CONNECT_CLICK_HK_INCREASE_LIST_STOCK);
                    break;
                default:
                    break;
            }
        }

        private void clickAhChild(View v) {
            final Activity activity = getActivity();
            if (activity == null) {
                return;
            }

            final AHPlateDesc ahItem = ((AHPremiumViewHolder) v.getTag()).item;
            WebBeaconJump.showAhPremiumDetail(activity, ahItem.sADtSecCode, ahItem.sASecName, ahItem.sHKDtSecCode, ahItem.sHKSecName);
            StatisticsUtil.reportAction(StatisticsConst.MARKET_SH_HK_CONNECT_CLICK_AH_STOCK);
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
                    StockUpDownListActivity.showSHConnectStockIncreaseList(activity);
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_SH_HK_CONNECT_CLICK_MORE_SH_INCREASE_LIST);
                    break;
                case 1:
                    StockUpDownListActivity.showHKConnectStockIncreaseList(activity);
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_SH_HK_CONNECT_CLICK_MORE_HK_INCREASE_LIST);
                    break;
                case 2:
                    final Intent intent = new Intent(activity, AHPremiumStockListActivity.class);
                    activity.startActivity(intent);
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_SH_HK_CONNECT_CLICK_MORE_AH_LIST);
                    break;
                default:
                    break;
            }
        }
    }

    private static final class ViewHolder {
        public TextView mTitle;
        public TextView mCode;
        public TextView mPrice;
        public TextView mDeltaPercent;
        public ImageView mTagView;
        public MultiTagView mMultiTagView;
        public PlateQuoteDesc mItem;
    }

    private static final class AHPremiumViewHolder {
        public TextView title;
        public TextView hSharesInfoView;
        public TextView aSharesInfoView;
        public TextView premiumInfoView;
        public AHPlateDesc item;
    }

    private BroadcastReceiver mReceiver;
    private boolean mTrading;
    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new MarketSHHKConnectReceiver();
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

    private final class MarketSHHKConnectReceiver extends BroadcastReceiver {
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
        final TradingStateManager tradingStateManager = TradingStateManager.getInstance();
        final boolean trading = tradingStateManager.isTradingOrCallauction(E_MARKET_TYPE.E_MT_SH)
                || tradingStateManager.isTradingOrCallauction(E_MARKET_TYPE.E_MT_HK);
        if (mTrading != trading) {
            mTrading = trading;
            if (trading) {
                mPeriodicHandlerManager.runPeriodic();
            }
        }
    }
}
