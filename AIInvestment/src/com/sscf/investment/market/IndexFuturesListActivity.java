package com.sscf.investment.market;

import BEC.*;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.dengtacj.component.managers.IQuoteManager;
import com.sscf.investment.sdk.utils.NetUtil;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.*;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.RefreshButton;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import java.util.ArrayList;

/**
 * Created by davidwei on 2015/9/16.
 * 股指期货的列表
 */
public final class IndexFuturesListActivity extends BaseFragmentActivity implements View.OnClickListener, Handler.Callback, PtrHandler, Runnable,
        OnGetDataCallback<ArrayList<SecSimpleQuote>> {
    private static final String TAG = IndexFuturesListActivity.class.getSimpleName();
    public static final int MSG_UPDATE_INDEX_DATA = 1;
    public static final int MSG_UPDATE_FUTURES_DATA = 2;
    public static final int MSG_HANDLE_FAILED = 3;
    private PtrFrameLayout mPtrFrame;
    private ExpandableListView mListView;
    private Handler mHandler;
    private PeriodicHandlerManager mPeriodicHandlerManager;

    private ArrayList<String> mIndexUnicodes;

    private IndexFuturesListAdapter mAdapter;

    private RefreshButton mRefreshButton;

    private ArrayList<SecSimpleQuote> mIndexesList;
    private ArrayList<PlateQuoteDesc> mFuturesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.market_activity_index_futures_list);

        mHandler = new Handler(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);

        initViews();

        final String[] unicodes = getResources().getStringArray(R.array.market_index_list_unicode);
        mIndexUnicodes = new ArrayList<String>(unicodes.length);
        for (String unicode : unicodes) {
            mIndexUnicodes.add(unicode);
        }

        expandAllGroup();
    }

    private void expandAllGroup() {
        for(int i = 0; i < mAdapter.getGroupCount(); i++){
            mListView.expandGroup(i);
        }
    }

    private void initViews() {
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        mRefreshButton = (RefreshButton) findViewById(R.id.refresh_button);
        mRefreshButton.setOnClickListener(this);
        ((TextView)findViewById(R.id.actionbar_title)).setText(R.string.market_index);

        mPtrFrame = (PtrFrameLayout) findViewById(R.id.ptr);
        mPtrFrame.setPtrHandler(this);

        mListView = (ExpandableListView) mPtrFrame.findViewById(R.id.list);
        mListView.setGroupIndicator(null);
        mAdapter = new IndexFuturesListAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnChildClickListener(mAdapter);
        mListView.setOnGroupClickListener(mAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        SwipeBackLayout.attachSwipeLayout(this);
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
        final IQuoteManager quoteManager = (IQuoteManager) ComponentManager.getInstance()
                .getManager(IQuoteManager.class.getName());
        if (quoteManager != null) {
            quoteManager.requestSimpleQuote(mIndexUnicodes, this);
        }

        final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance().getManager(IMarketManager.class.getName());
        if (marketManager != null) {
            marketManager.requestIndexFuturesList(100, mGetFutureListCallback);
        }
    }

    @Override
    public void onGetData(ArrayList<SecSimpleQuote> data) {
        if (data != null) {
            DtLog.d(TAG, "onGetData : " + data);
            mHandler.obtainMessage(MSG_UPDATE_INDEX_DATA, data).sendToTarget();
        } else {
            mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
        }
    }

    private OnGetDataCallback<ArrayList<PlateQuoteDesc>> mGetFutureListCallback = (ArrayList<PlateQuoteDesc> futuresList) -> {
        if (futuresList != null) {
            mHandler.obtainMessage(MSG_UPDATE_FUTURES_DATA, futuresList).sendToTarget();
        } else {
            mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
        }
    };

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_INDEX_DATA:
                mPtrFrame.refreshComplete();
                mRefreshButton.stopLoadingAnim();
                updateIndexInfos((ArrayList<SecSimpleQuote>) msg.obj);
                break;
            case MSG_UPDATE_FUTURES_DATA:
                mPtrFrame.refreshComplete();
                mRefreshButton.stopLoadingAnim();
                updateFuturesInfos((ArrayList<PlateQuoteDesc>) msg.obj);
                break;
            case MSG_HANDLE_FAILED:
                mPtrFrame.refreshComplete();
                mRefreshButton.stopLoadingAnim();
                break;
            default:
                break;
        }
        return false;
    }

    private void updateIndexInfos(ArrayList<SecSimpleQuote> indexInfos) {
        mIndexesList = indexInfos;
        mAdapter.notifyDataSetChanged();
    }

    private void updateFuturesInfos(ArrayList<PlateQuoteDesc> futuresInfos) {
        mFuturesList = futuresInfos;
        mAdapter.notifyDataSetChanged();
    }

    private final class IndexFuturesListAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {
        private final String[] mTitles;

        public IndexFuturesListAdapter() {
            mTitles = getResources().getStringArray(R.array.market_index_futures_title);
        }

        @Override
        public int getGroupCount() {
            return mTitles.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case 0:
                    return mIndexesList != null ? mIndexesList.size() : 0;
                case 1:
                    return mFuturesList != null ? mFuturesList.size() : 0;
            }
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case 0:
                    return mIndexesList.get(childPosition);
                case 1:
                    return mFuturesList.get(childPosition);
            }
            return null;
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
            TextView titleView = null;
            if (convertView == null) {
                titleView = (TextView) View.inflate(IndexFuturesListActivity.this, R.layout.market_activity_index_futures_list_title_item, null);
            } else {
                titleView = (TextView) convertView;
            }
            titleView.setText(mTitles[groupPosition]);
            return titleView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView title, code, price, deltaPercent;
            final com.dengtacj.thoth.Message childItem = (com.dengtacj.thoth.Message) getChild(groupPosition, childPosition);
            if (convertView == null) {
                convertView = View.inflate(IndexFuturesListActivity.this, R.layout.market_stock_list_item, null);
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
                viewHolder.mItem = childItem;
            } else {
                ViewHolder viewHolder = (ViewHolder) convertView.getTag();
                title = viewHolder.mTitle;
                code = viewHolder.mCode;
                price = viewHolder.mPrice;
                deltaPercent = viewHolder.mDeltaPercent;
                viewHolder.mItem = childItem;
            }

            String secName = null;
            String secCode = null;
            String secPrice = null;
            CharSequence secDelta = null;
            switch (groupPosition) {
                case 0:
                    final SecSimpleQuote indexItem = (SecSimpleQuote) childItem;
                    secName = indexItem.sSecName;
                    secCode = StockUtil.convertSecInfo(indexItem.sDtSecCode).getSSecCode();
                    secPrice = StringUtil.getFormattedFloat(indexItem.fNow, indexItem.iTpFlag);
                    secDelta = StringUtil.getUpDownStringSpannable(indexItem.fNow, indexItem.fClose);
                    break;
                case 1:
                    final PlateQuoteDesc futuresItem = (PlateQuoteDesc) childItem;
                    secName = futuresItem.sSecName;
                    secCode = StockUtil.convertSecInfo(futuresItem.sDtSecCode).getSSecCode();
                    secPrice = StringUtil.getFormattedFloat(futuresItem.fNow, futuresItem.iTpFlag);
                    secDelta = StringUtil.getUpDownStringSpannable(futuresItem.fNow, futuresItem.fClose);
                    break;
            }

            title.setText(secName);

            code.setText(secCode);

            price.setText(secPrice);
            deltaPercent.setText(secDelta);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            return true;
        }

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            final ViewHolder viewHolder = (ViewHolder) v.getTag();
            final com.dengtacj.thoth.Message childItem = viewHolder.mItem;
            String secName = null;
            String unicode = null;
            ArrayList<SecListItem> secList = null;
            switch (groupPosition) {
                case 0:
                    final SecSimpleQuote indexItem = (SecSimpleQuote) childItem;
                    secName = indexItem.sSecName;
                    unicode = indexItem.sDtSecCode;
                    secList = SecListItemUtils.getSecListFromSecSimpleQuoteList(mIndexesList);
                    break;
                case 1:
                    final PlateQuoteDesc futuresItem = (PlateQuoteDesc) childItem;
                    secName = futuresItem.sSecName;
                    unicode = futuresItem.sDtSecCode;
                    secList = SecListItemUtils.getSecListFromPlateQuoteDescList(mFuturesList);
                    break;
                default:
                    return true;
            }
            CommonBeaconJump.showSecurityDetailActivity(IndexFuturesListActivity.this, unicode, secName, secList);
            StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_INDEX_FUTURES);
            return true;
        }
    }

    private static final class ViewHolder {
        public TextView mTitle;
        public TextView mCode;
        public TextView mPrice;
        public TextView mDeltaPercent;
        public com.dengtacj.thoth.Message mItem;
    }
}
