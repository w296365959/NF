package com.sscf.investment.market.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.sscf.investment.R;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.market.StockUpDownListActivity;
import com.sscf.investment.market.view.MarketIndexHeader;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.SecListItemUtils;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.widget.BaseFragment;
import java.util.ArrayList;
import BEC.BEACON_STAT_TYPE;
import BEC.E_MARKET_TYPE;
import BEC.PlateQuoteDesc;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by davidwei on 2015/10/09.
 * 港股行情界面
 */
public final class MarketHongKongFragment extends BaseFragment implements Handler.Callback, Runnable, PtrHandler {
    private ExpandableListView mListView;
    private MarketHongKongListAdapter mAdapter;
    private PtrFrameLayout mPtrFrame;
    private MarketIndexHeader mIndexHeader;

    private Handler mHandler;
    private PeriodicHandlerManager mPeriodicHandlerManager;

    public static final int DEFAULT_STOCK_LIST_COUNT = 10;

    public static final int MSG_UPDATE_DATA = 1;
    public static final int MSG_HANDLE_FAILED = 2;

    public static final int GET_DATA_TYPE_HK_MAIN_STOCK_INCREASE_LIST = 1;
    public static final int GET_DATA_TYPE_HK_GEM_STOCK_INCREASE_LIST = 2;

    private GetDataCallback mGetHKMainIncreaseListCallback;
    private GetDataCallback mGetHKGEMIncreaseListCallback;

    //设置组视图的显示文字
    private String[] mGroupTitles;
    private ArrayList<ArrayList<PlateQuoteDesc>> mInfoArray;
    private ArrayList<SecListItem>[] mSecList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.ptr_expandable_list, container, false);
        mHandler = new Handler(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        mGroupTitles = getResources().getStringArray(R.array.market_hongkong_group_title);
        mInfoArray = new ArrayList<ArrayList<PlateQuoteDesc>>(mGroupTitles.length);
        mSecList = new ArrayList[mGroupTitles.length];
        initViews(root);

        mGetHKMainIncreaseListCallback = new GetDataCallback(GET_DATA_TYPE_HK_MAIN_STOCK_INCREASE_LIST);
        mGetHKGEMIncreaseListCallback = new GetDataCallback(GET_DATA_TYPE_HK_GEM_STOCK_INCREASE_LIST);

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
        helper.setKey(StatConsts.HONGKONG);
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

        initIndexesHeader();

        mAdapter = new MarketHongKongListAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnChildClickListener(mAdapter);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                StatisticsUtil.reportAction(StatisticsConst.A_MARKET_GANGGU_CHNSTOCK_SHOW);
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

    private void initIndexesHeader() {
        mIndexHeader = (MarketIndexHeader) View.inflate(getActivity(), R.layout.market_index_header, null);
        mIndexHeader.setIndexDtSecCodes(R.array.market_hongkong_index_unicode, MarketIndexHeader.TYPE_HONGKONG);
        mListView.addHeaderView(mIndexHeader);
    }

    private void expandAllGroup() {
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mListView.expandGroup(i);
        }
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        mTrading = TradingStateManager.getInstance().isTradingOrCallauction(E_MARKET_TYPE.E_MT_HK);
        mPeriodicHandlerManager.runPeriodic();
        StatisticsUtil.reportAction(StatisticsConst.MARKET_HONGKONG_DISPLAY);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        mPeriodicHandlerManager.runPeriodic();
        StatisticsUtil.reportAction(StatisticsConst.MARKET_HONGKONG_DISPLAY);
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
        mIndexHeader.requestData();
        final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance().getManager(IMarketManager.class.getName());
        if (marketManager != null) {
            marketManager.requestHKMainStockIncreaseList(DEFAULT_STOCK_LIST_COUNT, mGetHKMainIncreaseListCallback);
            marketManager.requestHKGEMStockIncreaseList(DEFAULT_STOCK_LIST_COUNT, mGetHKGEMIncreaseListCallback);
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
            case GET_DATA_TYPE_HK_MAIN_STOCK_INCREASE_LIST:
                mInfoArray.remove(0);
                mInfoArray.add(0, data);
                mAdapter.notifyDataSetChanged();
                break;
            case GET_DATA_TYPE_HK_GEM_STOCK_INCREASE_LIST:
                mInfoArray.remove(1);
                mInfoArray.add(1, data);
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private final class MarketHongKongListAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener, View.OnClickListener {
        private final LayoutInflater mInflater;

        public MarketHongKongListAdapter() {
            final Activity activity = getActivity();
            mInflater = LayoutInflater.from(activity);
            final ArrayList<PlateQuoteDesc> empty = new ArrayList<PlateQuoteDesc>(0);

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
            FrameLayout groupView = (FrameLayout) convertView;
            if (convertView == null) {
                groupView = new FrameLayout(getActivity());
                groupView.addView(mInflater.inflate(R.layout.market_listview_group, null));
                convertView = groupView;
            }

            TextView title = (TextView) groupView.findViewById(R.id.group_title);
            ImageView expandImage = (ImageView) groupView.findViewById(R.id.group_expand_image);
            if (isExpanded) {
                expandImage.setImageResource(R.drawable.list_group_expanded);
                groupView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.market_block_margin), 0, 0);
            } else {
                expandImage.setImageResource(R.drawable.list_group_collapsed);
                groupView.setPadding(0, 0, 0, 0);
            }
            title.setText(group);
            final View moreView = groupView.findViewById(R.id.group_more);
            moreView.setOnClickListener(this);
            moreView.setTag(groupPosition);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView title, code, price, deltaPercent;
            ImageView tagView;
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
                viewHolder.mItem = childItem;
            } else {
                ViewHolder viewHolder = (ViewHolder) convertView.getTag();
                title = viewHolder.mTitle;
                code = viewHolder.mCode;
                price = viewHolder.mPrice;
                deltaPercent = viewHolder.mDeltaPercent;
                tagView = viewHolder.mTagView;
                viewHolder.mItem = childItem;
            }

            title.setText(childItem.sSecName);

            code.setText(StockUtil.convertSecInfo(childItem.sDtSecCode).getSSecCode());

            price.setText(childItem.fNow > 0 ? StringUtil.getFormattedFloat(childItem.fNow, childItem.iTpFlag) : "--");

            deltaPercent.setText(StringUtil.getUpDownStringSpannable(childItem.fNow, childItem.fClose));

            StockUtil.updateStockTagIcon(tagView, childItem.sDtSecCode);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            final ArrayList<SecListItem> secList = mSecList[groupPosition];
            final ViewHolder viewHolder = (ViewHolder) v.getTag();
            final PlateQuoteDesc item = viewHolder.mItem;
            CommonBeaconJump.showSecurityDetailActivity(getActivity(), item.getSDtSecCode(), item.getSSecName(), secList);

            switch (groupPosition) {
                case 0:
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_HONGKONG_CLICK_MAIN_INCREASE_STOCK);
                    break;
                case 1:
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_HONGKONG_CLICK_GEM_INCREASE_STOCK);
                    break;
                default:
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
                    StockUpDownListActivity.showHKMainIncreaseList(activity);
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_HONGKONG_CLICK_MORE_MAIN_INCREASE_LIST);
                    break;
                case 1:
                    StockUpDownListActivity.showHKGEMIncreaseList(activity);
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_HONGKONG_CLICK_MORE_GEM_INCREASE_LIST);
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
        public PlateQuoteDesc mItem;
    }

    /**
     * 获得主板，创业板的数据的callback
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
                int index = -1;
                switch (mType) {
                    case GET_DATA_TYPE_HK_MAIN_STOCK_INCREASE_LIST:
                        index = 0;
                        break;
                    case GET_DATA_TYPE_HK_GEM_STOCK_INCREASE_LIST:
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

    private BroadcastReceiver mReceiver;
    private boolean mTrading;
    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new MarketHongKongReceiver();
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

    private final class MarketHongKongReceiver extends BroadcastReceiver {
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
        final boolean trading = TradingStateManager.getInstance().isTradingOrCallauction(E_MARKET_TYPE.E_MT_HK);
        if (mTrading != trading) {
            mTrading = trading;
            if (trading) {
                mPeriodicHandlerManager.runPeriodic();
            }
        }
    }
}