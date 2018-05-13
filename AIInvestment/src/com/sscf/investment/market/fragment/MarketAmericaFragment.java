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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.dengtacj.component.managers.IQuoteManager;
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
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.BaseFragment;
import java.util.ArrayList;
import java.util.HashMap;
import BEC.BEACON_STAT_TYPE;
import BEC.E_MARKET_TYPE;
import BEC.PlateQuoteDesc;
import BEC.PrivInfo;
import BEC.SecSimpleQuote;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by liqf on 2015/8/10.
 */
public final class MarketAmericaFragment extends BaseFragment implements Handler.Callback, Runnable, PtrHandler,
        OnGetDataCallback<ArrayList<SecSimpleQuote>> {
    private ExpandableListView mListView;
    private MarketAmericaListAdapter mAdapter;
    private PtrFrameLayout mPtrFrame;
    private MarketIndexHeader mIndexHeader;

    public static final int MSG_UPDATE_DATA = 1;
    public static final int MSG_HANDLE_FAILED = 2;
    public static final int MSG_UPDATE_PRIVATIZATION_TRACKING_LIST = 3;

    private Handler mHandler;
    private PeriodicHandlerManager mPeriodicHandlerManager;

    public static final int DEFAULT_STOCK_LIST_COUNT = 10;

    //设置组视图的显示文字
    private String[] mGroupTitles;
    private ArrayList<ArrayList<PlateQuoteDesc>> mInfoArray;
    private ArrayList<SecListItem>[] mSecList;
    private ArrayList<PrivInfo> mPrivatizationTrackingList;
    private ArrayList<String> mPrivatizationTrackingListDtCodes;
    private final HashMap<String, Float> mPrivatizationTrackingListPrice = new HashMap<String, Float>(DEFAULT_STOCK_LIST_COUNT);

    public static final int GET_DATA_TYPE_CCS_STOCK_INCREASE_LIST = 1;
    public static final int GET_DATA_TYPE_CCS_STOCK_DECREASE_LIST = 2;
    public static final int GET_DATA_TYPE_SPX_STOCK_INCREASE_LIST = 3;
    public static final int GET_DATA_TYPE_SPX_STOCK_DECREASE_LIST = 4;

    private GetDataCallback mGetCCSIncreaseListCallback;
    private GetDataCallback mGetCCSDecreaseListCallback;
    private GetDataCallback mGetSPXIncreaseListCallback;
    private GetDataCallback mGetSPXDecreaseListCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.ptr_expandable_list, container, false);
        mHandler = new Handler(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        mGroupTitles = getResources().getStringArray(R.array.market_america_group_title);
        mInfoArray = new ArrayList<ArrayList<PlateQuoteDesc>>(mGroupTitles.length);
        mSecList = new ArrayList[mGroupTitles.length];
        initViews(root);
        mGetCCSIncreaseListCallback = new GetDataCallback(GET_DATA_TYPE_CCS_STOCK_INCREASE_LIST);
        mGetCCSDecreaseListCallback = new GetDataCallback(GET_DATA_TYPE_CCS_STOCK_DECREASE_LIST);
        mGetSPXIncreaseListCallback = new GetDataCallback(GET_DATA_TYPE_SPX_STOCK_INCREASE_LIST);
        mGetSPXDecreaseListCallback = new GetDataCallback(GET_DATA_TYPE_SPX_STOCK_DECREASE_LIST);

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
        helper.setKey(StatConsts.AMERICA);
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

        mAdapter = new MarketAmericaListAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnChildClickListener(mAdapter);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                StatisticsUtil.reportAction(StatisticsConst.A_MARKET_MEIGU_CHNINDEX_SHOW);
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

    private void expandAllGroup() {
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mListView.expandGroup(i);
        }
    }

    private void initIndexesHeader() {
        mIndexHeader = (MarketIndexHeader) View.inflate(getActivity(), R.layout.market_index_header, null);
        mIndexHeader.setIndexDtSecCodes(R.array.market_america_index_unicode, MarketIndexHeader.TYPE_AMERICAN);
        mListView.addHeaderView(mIndexHeader);
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        mTrading = TradingStateManager.getInstance().isTrading(E_MARKET_TYPE.E_MT_NYSE);
        mPeriodicHandlerManager.runPeriodic();
        StatisticsUtil.reportAction(StatisticsConst.MARKET_AMERICA_DISPLAY);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        mPeriodicHandlerManager.runPeriodic();
        StatisticsUtil.reportAction(StatisticsConst.MARKET_AMERICA_DISPLAY);
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
            requestPrivatizationTrackingData(marketManager);
            marketManager.requestCCSStockIncreaseList(DEFAULT_STOCK_LIST_COUNT, mGetCCSIncreaseListCallback);
            marketManager.requestCCSStockDecreaseList(DEFAULT_STOCK_LIST_COUNT, mGetCCSDecreaseListCallback);
            marketManager.requestSPXStockIncreaseList(DEFAULT_STOCK_LIST_COUNT, mGetSPXIncreaseListCallback);
            marketManager.requestSPXStockDecreaseList(DEFAULT_STOCK_LIST_COUNT, mGetSPXDecreaseListCallback);
        }
    }

    private void requestPrivatizationTrackingData(final IMarketManager marketManager) {
        if (mPrivatizationTrackingListDtCodes == null) {
            marketManager.requestPrivatizationTrackingList(DEFAULT_STOCK_LIST_COUNT, mGetPrivatizationTrackingListCallback);
        } else {
            final IQuoteManager quoteManager = (IQuoteManager) ComponentManager.getInstance()
                    .getManager(IQuoteManager.class.getName());
            if (quoteManager != null) {
                quoteManager.requestSimpleQuote(mPrivatizationTrackingListDtCodes, this);
            }
        }
    }

    private OnGetDataCallback<ArrayList<PrivInfo>> mGetPrivatizationTrackingListCallback = (ArrayList<PrivInfo> stockList) -> {
        if (stockList != null) {
            final ArrayList<String> dtCodes = new ArrayList<String>(stockList.size());
            for (PrivInfo info : stockList) {
                dtCodes.add(info.sDtCode);
            }
            mPrivatizationTrackingList = stockList;
            mPrivatizationTrackingListDtCodes = dtCodes;
            mHandler.sendEmptyMessage(MSG_UPDATE_PRIVATIZATION_TRACKING_LIST);

            final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance().getManager(IMarketManager.class.getName());
            if (marketManager != null) {
                requestPrivatizationTrackingData(marketManager);
            }
        }
    };

    @Override
    public void onGetData(ArrayList<SecSimpleQuote> data) {
        if (data != null) {
            mHandler.obtainMessage(MSG_UPDATE_PRIVATIZATION_TRACKING_LIST, data).sendToTarget();
        }
    }

    /**
     * 获得中概股，标普500榜的数据的callback
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
                    case GET_DATA_TYPE_CCS_STOCK_INCREASE_LIST:
                        index = 1;
                        break;
                    case GET_DATA_TYPE_CCS_STOCK_DECREASE_LIST:
                        index = 2;
                        break;
                    case GET_DATA_TYPE_SPX_STOCK_INCREASE_LIST:
                        index = 3;
                        break;
                    case GET_DATA_TYPE_SPX_STOCK_DECREASE_LIST:
                        index = 4;
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
            case MSG_UPDATE_DATA:
                mPtrFrame.refreshComplete();
                updateData((ArrayList<PlateQuoteDesc>) msg.obj, msg.arg1);
                break;
            case MSG_HANDLE_FAILED:
                mPtrFrame.refreshComplete();
                break;
            case MSG_UPDATE_PRIVATIZATION_TRACKING_LIST:
                updatePrivatizationTrackingList((ArrayList<SecSimpleQuote>) msg.obj);
                break;
            default:
                break;
        }
        return true;
    }

    private void updatePrivatizationTrackingList(final ArrayList<SecSimpleQuote> stockList) {
        if (stockList != null) {
            for (SecSimpleQuote quote : stockList) {
                if (quote != null) {
                    mPrivatizationTrackingListPrice.put(quote.sDtSecCode, quote.fNow > 0 ? quote.fNow : quote.fClose);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void updateData(ArrayList<PlateQuoteDesc> data, int type) {
        switch (type) {
            case GET_DATA_TYPE_CCS_STOCK_INCREASE_LIST:
                mInfoArray.remove(1);
                mInfoArray.add(1, data);
                mAdapter.notifyDataSetChanged();
                break;
            case GET_DATA_TYPE_CCS_STOCK_DECREASE_LIST:
                mInfoArray.remove(2);
                mInfoArray.add(2, data);
                mAdapter.notifyDataSetChanged();
                break;
            case GET_DATA_TYPE_SPX_STOCK_INCREASE_LIST:
                mInfoArray.remove(3);
                mInfoArray.add(3, data);
                mAdapter.notifyDataSetChanged();
                break;
            case GET_DATA_TYPE_SPX_STOCK_DECREASE_LIST:
                mInfoArray.remove(4);
                mInfoArray.add(4, data);
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private final class MarketAmericaListAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener, View.OnClickListener {
        private final LayoutInflater mInflater;

        private static final int TYPE_COUNT = 2;
        private static final int TYPE_NORMAL = 0;
        private static final int TYPE_PRIVATIZATION_TRACKING = 1;

        public MarketAmericaListAdapter() {
            final Activity activity = getActivity();
            mInflater = LayoutInflater.from(activity);
            final ArrayList<PlateQuoteDesc> empty = new ArrayList<PlateQuoteDesc>(0);

            final int length = mGroupTitles.length;
            for (int i = 0; i < length; i++) {
                mInfoArray.add(empty);
            }
        }

        @Override
        public int getGroupCount() {
            return mGroupTitles.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (getGroupType(groupPosition)) {
                case TYPE_NORMAL:
                    return mInfoArray.get(groupPosition).size();
                case TYPE_PRIVATIZATION_TRACKING:
                    if (mPrivatizationTrackingList != null) {
                        return mPrivatizationTrackingList.size();
                    }
                    break;
                default:
                    break;
            }
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroupTitles[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (getGroupType(groupPosition)) {
                case TYPE_NORMAL:
                    return mInfoArray.get(groupPosition).get(childPosition);
                case TYPE_PRIVATIZATION_TRACKING:
                    if (mPrivatizationTrackingList != null) {
                        return mPrivatizationTrackingList.get(childPosition);
                    }
                    break;
                default:
                    break;
            }
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition * 100 + childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
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
                    case TYPE_PRIVATIZATION_TRACKING:
                        groupView.addView(mInflater.inflate(R.layout.market_listview_group, null));
                        final View header = mInflater.inflate(R.layout.market_privatization_tracking_list_header, null);
                        header.setBackgroundDrawable(null);
                        groupView.addView(header);
                        break;
                    default:
                        break;
                }
                convertView = groupView;
            }

            TextView title = (TextView) groupView.findViewById(R.id.group_title);
            ImageView expandImage = (ImageView) groupView.findViewById(R.id.group_expand_image);
            if (isExpanded) {
                expandImage.setImageResource(R.drawable.list_group_expanded);
                groupView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.market_block_margin), 0, 0);
                if (groupType == TYPE_PRIVATIZATION_TRACKING) {
                    groupView.findViewById(R.id.privatizationTrackingListHeader).setVisibility(View.VISIBLE);
                }
            } else {
                expandImage.setImageResource(R.drawable.list_group_collapsed);
                groupView.setPadding(0, 0, 0, 0);
                if (groupType == TYPE_PRIVATIZATION_TRACKING) {
                    groupView.findViewById(R.id.privatizationTrackingListHeader).setVisibility(View.GONE);
                }
            }
            title.setText(group);
            final View moreView = groupView.findViewById(R.id.group_more);
            moreView.setOnClickListener(this);
            moreView.setTag(groupPosition);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            switch (getGroupType(groupPosition)) {
                case TYPE_NORMAL:
                    return getNormalChildView(groupPosition, childPosition, isLastChild, convertView, parent);
                case TYPE_PRIVATIZATION_TRACKING:
                    return getPrivatizationTrackingChildView(groupPosition, childPosition, isLastChild, convertView, parent);
                default:
                    break;
            }
            return null;
        }

        public View getPrivatizationTrackingChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView title, code, offerPrice, nowPrice, currentProgress;
            ImageView tagView;
            final PrivInfo childItem = (PrivInfo) getChild(groupPosition, childPosition);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.market_privatization_tracking_list_item, null);
                PrivatizationTrackingViewHolder viewHolder = new PrivatizationTrackingViewHolder();
                convertView.setTag(viewHolder);
                title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.mTitle = title;
                code = (TextView) convertView.findViewById(R.id.code);
                viewHolder.mCode = code;
                offerPrice = (TextView) convertView.findViewById(R.id.offerPrice);
                viewHolder.mOfferPrice = offerPrice;
                nowPrice = (TextView) convertView.findViewById(R.id.nowPrice);
                viewHolder.mNowPrice = nowPrice;
                currentProgress = (TextView) convertView.findViewById(R.id.currentProgress);
                viewHolder.mCurrentProgress = currentProgress;
                tagView = (ImageView) convertView.findViewById(R.id.stock_tag_icon);
                viewHolder.mTagView = tagView;
                viewHolder.mItem = childItem;
            } else {
                PrivatizationTrackingViewHolder viewHolder = (PrivatizationTrackingViewHolder) convertView.getTag();
                title = viewHolder.mTitle;
                code = viewHolder.mCode;
                offerPrice = viewHolder.mOfferPrice;
                nowPrice = viewHolder.mNowPrice;
                currentProgress = viewHolder.mCurrentProgress;
                tagView = viewHolder.mTagView;
                viewHolder.mItem = childItem;
            }

            title.setText(childItem.sSecName);

            code.setText(childItem.sSecCode);

            offerPrice.setText(StringUtil.getFormatedFloat(childItem.fRedeemPrice));

            final Float now = mPrivatizationTrackingListPrice.get(childItem.sDtCode);
            if (now != null && now > 0) {
                nowPrice.setText(StringUtil.getFormatedFloat(now));
            }
            currentProgress.setText(childItem.sPace);

            StockUtil.updateStockTagIcon(tagView, childItem.sDtCode);

            return convertView;
        }

        public View getNormalChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
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
        public int getGroupType(int groupPosition) {
            return groupPosition == 0 ? TYPE_PRIVATIZATION_TRACKING : TYPE_NORMAL;
        }

        @Override
        public int getGroupTypeCount() {
            return TYPE_COUNT;
        }

        @Override
        public int getChildType(int groupPosition, int childPosition) {
            return groupPosition == 0 ? TYPE_PRIVATIZATION_TRACKING : TYPE_NORMAL;
        }

        @Override
        public int getChildTypeCount() {
            return TYPE_COUNT;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            switch (getGroupType(groupPosition)) {
                case TYPE_NORMAL:
                    clickNormalChild(parent, v, groupPosition, childPosition, id);
                    break;
                case TYPE_PRIVATIZATION_TRACKING:
                    clickPrivatizationTrackingChild(parent, v, groupPosition, childPosition, id);
                    break;
                default:
                    break;
            }
            return true;
        }

        private void clickPrivatizationTrackingChild(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            final PrivatizationTrackingViewHolder viewHolder = (PrivatizationTrackingViewHolder) v.getTag();
            final PrivInfo item = viewHolder.mItem;
            WebBeaconJump.showPrivatizationTrackingDetail(getActivity(), item.sDtCode, item.sSecName);
            StatisticsUtil.reportAction(StatisticsConst.MARKET_AMERICAN_CLICK_PRIVATIZATION_TRACKING_STOCK);
        }

        private void clickNormalChild(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            final ArrayList<SecListItem> secList = mSecList[groupPosition];
            final ViewHolder viewHolder = (ViewHolder) v.getTag();
            final PlateQuoteDesc item = viewHolder.mItem;
            CommonBeaconJump.showSecurityDetailActivity(getActivity(), item.getSDtSecCode(), item.getSSecName(), secList);

            switch (groupPosition) {
                case 1:
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_AMERICAN_CLICK_CCS_INCREASE_LIST_STOCK);
                    break;
                case 2:
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_AMERICAN_CLICK_CCS_DECREASE_LIST_STOCK);
                    break;
                case 3:
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_AMERICAN_CLICK_SPX_INCREASE_LIST_STOCK);
                    break;
                case 4:
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_AMERICAN_CLICK_SPX_DECREASE_LIST_STOCK);
                    break;
                default:
                    break;
            }
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
                    WebBeaconJump.showPrivatizationTrackingList(activity);
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_AMERICAN_CLICK_MORE_PRIVATIZATION_TRACKING);
                    break;
                case 1:
                    StockUpDownListActivity.showCCSIncreaseList(activity);
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_AMERICAN_CLICK_MORE_CCS_INCREASE_LIST);
                    break;
                case 2:
                    StockUpDownListActivity.showCCSDecreaseList(activity);
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_AMERICAN_CLICK_MORE_CCS_DECREASE_LIST);
                    break;
                case 3:
                    StockUpDownListActivity.showSPXIncreaseList(activity);
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_AMERICAN_CLICK_MORE_SPX_INCREASE_LIST);
                    break;
                case 4:
                    StockUpDownListActivity.showSPXDecreaseList(activity);
                    StatisticsUtil.reportAction(StatisticsConst.MARKET_AMERICAN_CLICK_MORE_SPX_DECREASE_LIST);
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

    private static final class PrivatizationTrackingViewHolder {
        public TextView mTitle;
        public TextView mCode;
        public TextView mOfferPrice;
        public TextView mNowPrice;
        public TextView mCurrentProgress;
        public ImageView mTagView;
        public PrivInfo mItem;
    }

    private BroadcastReceiver mReceiver;
    private boolean mTrading;
    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new MarketAmericaReceiver();
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

    private final class MarketAmericaReceiver extends BroadcastReceiver {
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
        final boolean trading = tradingStateManager.isTrading(E_MARKET_TYPE.E_MT_NYSE);
        if (mTrading != trading) {
            mTrading = trading;
            if (trading) {
                mPeriodicHandlerManager.runPeriodic();
            }
        }
    }
}
