package com.sscf.investment.market;

import BEC.*;
import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.sscf.investment.sdk.utils.NetUtil;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.market.view.MarketCapitalFlowIndexHeader;
import com.sscf.investment.market.view.MarketCapitalFlowPlateHeader;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.*;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.MultiTagView;
import com.sscf.investment.component.ui.widget.RefreshButton;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by davidwei on 2015/9/14
 * 资金流主界面
 */
@Route("CapitalFlowActivity")
public final class CapitalFlowActivity extends BaseFragmentActivity implements Handler.Callback,
        PtrHandler, View.OnClickListener, Runnable {

    public static final int MSG_UPDATE_DATA = 1;
    public static final int MSG_HANDLE_FAILED = 2;

    public static final int STOCK_CAPITAL_FLOW_COUNT = 10;

    public static final int GET_DATA_TYPE_STOCK_CAPITAL_FLOW_INCREASE = 1;
    public static final int GET_DATA_TYPE_STOCK_CAPITAL_FLOW_DECREASE = 2;

    private GetDataCallback mGetStockCapitalFlowIncreaseCallback;
    private GetDataCallback mGetStockCapitalFlowDecreaseCallback;

    private PtrFrameLayout mPtrFrame;
    private ExpandableListView mListView;
    private MarketCapitalFlowListAdapter mAdapter;

    private MarketCapitalFlowPlateHeader mIndustryIncreaseHeader;
    private MarketCapitalFlowPlateHeader mIndustryDecreaseHeader;
    private MarketCapitalFlowPlateHeader mConceptIncreaseHeader;
    private MarketCapitalFlowPlateHeader mConceptDecreaseHeader;

    private RefreshButton mRefreshButton;

    private MarketCapitalFlowIndexHeader mIndexHeader;

    private String[] mGroupTitles;
    private ArrayList<ArrayList<CapitalDetailDesc>> mInfoArray;

    private Handler mHandler;

    private PeriodicHandlerManager mPeriodicHandlerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_capital_flow);

        mGroupTitles = getResources().getStringArray(R.array.capital_flow_group_title);
        mInfoArray = new ArrayList<ArrayList<CapitalDetailDesc>>(mGroupTitles.length);

        mHandler = new Handler(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);

        mGetStockCapitalFlowIncreaseCallback = new GetDataCallback(GET_DATA_TYPE_STOCK_CAPITAL_FLOW_INCREASE);
        mGetStockCapitalFlowDecreaseCallback = new GetDataCallback(GET_DATA_TYPE_STOCK_CAPITAL_FLOW_DECREASE);

        initViews();

        expandAllGroup();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        mRefreshButton = (RefreshButton) findViewById(R.id.refresh_button);
        mRefreshButton.setOnClickListener(this);
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.market_capital_flow_title);

        mPtrFrame = (PtrFrameLayout) findViewById(R.id.ptr);
        mPtrFrame.setPtrHandler(this);

        mListView = (ExpandableListView) findViewById(R.id.list);
        mListView.setGroupIndicator(null);
        mListView.setDivider(new ColorDrawable(ContextCompat.getColor(this, R.color.list_divider)));
        mListView.setChildDivider(new ColorDrawable(ContextCompat.getColor(this, R.color.list_divider)));
        mListView.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.list_divider_height));

        initCapitalFlowIndexViews();
        initCapitalFlowIndustryIncreaseViews();
        initCapitalFlowIndustryDecreaseViews();
        initCapitalFlowConceptIncreaseViews();
        initCapitalFlowConceptDecreaseViews();

        mAdapter = new MarketCapitalFlowListAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnChildClickListener(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.refresh_button:
                refresh();
                break;
            default:
                break;
        }
    }

    private void refresh() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
            mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
            return;
        }
        mPeriodicHandlerManager.runPeriodic();
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
        refresh();
    }

    private void initCapitalFlowIndexViews() {
        mIndexHeader = (MarketCapitalFlowIndexHeader) View.inflate(this, R.layout.market_capital_flow_index, null);
        mListView.addHeaderView(mIndexHeader);
    }

    private void initCapitalFlowIndustryIncreaseViews() {
        mIndustryIncreaseHeader = (MarketCapitalFlowPlateHeader) View.inflate(this, R.layout.market_capital_flow_plate_header, null);
        mIndustryIncreaseHeader.setPlateType(MarketCapitalFlowPlateHeader.PLATE_TYPE_INDUSTRY_INCREASE);
        mListView.addHeaderView(mIndustryIncreaseHeader);
    }

    private void initCapitalFlowIndustryDecreaseViews() {
        mIndustryDecreaseHeader = (MarketCapitalFlowPlateHeader) View.inflate(this, R.layout.market_capital_flow_plate_header, null);
        mIndustryDecreaseHeader.setPlateType(MarketCapitalFlowPlateHeader.PLATE_TYPE_INDUSTRY_DECREASE);
        mListView.addHeaderView(mIndustryDecreaseHeader);
    }

    private void initCapitalFlowConceptIncreaseViews() {
        mConceptIncreaseHeader = (MarketCapitalFlowPlateHeader) View.inflate(this, R.layout.market_capital_flow_plate_header, null);
        mConceptIncreaseHeader.setPlateType(MarketCapitalFlowPlateHeader.PLATE_TYPE_CONCEPT_INCREASE);
        mListView.addHeaderView(mConceptIncreaseHeader);
    }

    private void initCapitalFlowConceptDecreaseViews() {
        mConceptDecreaseHeader = (MarketCapitalFlowPlateHeader) View.inflate(this, R.layout.market_capital_flow_plate_header, null);
        mConceptDecreaseHeader.setPlateType(MarketCapitalFlowPlateHeader.PLATE_TYPE_CONCEPT_DECREASE);
        mListView.addHeaderView(mConceptDecreaseHeader);
    }

    private void expandAllGroup() {
        for(int i = 0; i < mAdapter.getGroupCount(); i++){
            mListView.expandGroup(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPeriodicHandlerManager.runPeriodic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPeriodicHandlerManager.stop();
    }

    @Override
    public void run() {
        mRefreshButton.startLoadingAnim();
        requestData();
        mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
    }

    private void requestData() {
        final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance().getManager(IMarketManager.class.getName());
        if (marketManager != null) {
            mIndexHeader.requestData(marketManager);
            mIndustryIncreaseHeader.requestData(marketManager);
            mIndustryDecreaseHeader.requestData(marketManager);
            mConceptIncreaseHeader.requestData(marketManager);
            mConceptDecreaseHeader.requestData(marketManager);
            marketManager.requestStockCapitalFlowIncreaseList(STOCK_CAPITAL_FLOW_COUNT, mGetStockCapitalFlowIncreaseCallback);
            marketManager.requestStockCapitalFlowDecreaseList(STOCK_CAPITAL_FLOW_COUNT, mGetStockCapitalFlowDecreaseCallback);
        }
    }

    /**
     * 获得热门行业，涨幅榜，跌幅榜，换手率榜的数据的callback
     */
    private final class GetDataCallback implements OnGetDataCallback<ArrayList<CapitalDetailDesc>> {
        private final int mType;
        GetDataCallback(int type) {
            mType = type;
        }

        @Override
        public void onGetData(ArrayList<CapitalDetailDesc> data) {
            if (data != null) {
                mHandler.obtainMessage(MSG_UPDATE_DATA, mType, 0, data).sendToTarget();
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
                mRefreshButton.stopLoadingAnim();
                updateData((ArrayList<CapitalDetailDesc>) msg.obj, msg.arg1);
                break;
            case MSG_HANDLE_FAILED:
                mPtrFrame.refreshComplete();
                mRefreshButton.stopLoadingAnim();
                break;
            default:
                break;
        }
        return true;
    }

    private void updateData(ArrayList<CapitalDetailDesc> data, int type) {
        switch (type) {
            case GET_DATA_TYPE_STOCK_CAPITAL_FLOW_INCREASE:
                mInfoArray.remove(0);
                mInfoArray.add(0, data);
                mAdapter.notifyDataSetChanged();
                break;
            case GET_DATA_TYPE_STOCK_CAPITAL_FLOW_DECREASE:
                mInfoArray.remove(1);
                mInfoArray.add(1, data);
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private final class MarketCapitalFlowListAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener, View.OnClickListener {
        private final LayoutInflater mInflater;

        public MarketCapitalFlowListAdapter() {
            mInflater = LayoutInflater.from(CapitalFlowActivity.this);
            ArrayList<CapitalDetailDesc> empty = new ArrayList<CapitalDetailDesc>(0);

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
                final ViewGroup groupView = new FrameLayout(CapitalFlowActivity.this);
                groupView.addView(mInflater.inflate(R.layout.market_listview_group, null));
                convertView = groupView;
            }
            final TextView title = (TextView) convertView.findViewById(R.id.group_title);
            final ImageView expandImage = (ImageView) convertView.findViewById(R.id.group_expand_image);
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
            final CapitalDetailDesc childItem = (CapitalDetailDesc) getChild(groupPosition, childPosition);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.market_capital_flow_stock_list_item, null);
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

            if (childItem.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
                // 停牌
                price.setText(R.string.stock_stop);
            } else {
                price.setText(StringUtil.getUpdownString(childItem.fChangeRate));
            }

            deltaPercent.setText(StringUtil.getCapitalFlowSpannable(childItem.fZljlr));

            // 是否显示次新股的tag
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
        public void onClick(View v) {
            Activity activity = CapitalFlowActivity.this;

            final Integer groupPosition = (Integer) v.getTag();
            switch (groupPosition) {
                case 0:
                    CapitalFlowStockListActivity.showCapitalFlowStockIncreaseListActivity(activity);
                    break;
                case 1:
                    CapitalFlowStockListActivity.showCapitalFlowStockDecreaseListActivity(activity);
                    break;
                default:
                    break;
            }
        }

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            final ArrayList<SecListItem> secList = SecListItemUtils.getSecListFromCapitalDetailDescList(mInfoArray.get(groupPosition));
            final ViewHolder viewHolder = (ViewHolder) v.getTag();
            final CapitalDetailDesc item = viewHolder.mItem;
            CommonBeaconJump.showSecurityDetailActivity(CapitalFlowActivity.this, item.getSDtSecCode(), item.getSSecName(), secList);
            return true;
        }
    }

    private final class ViewHolder {
        public TextView mTitle;
        public TextView mCode;
        public TextView mPrice;
        public TextView mDeltaPercent;
        public MultiTagView mMultiTagView;
        public CapitalDetailDesc mItem;
    }
}
