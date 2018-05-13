package com.sscf.investment.market;

import BEC.AHPlateDesc;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.dengtacj.component.managers.IShareManager;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.comparator.AHPremiumComparator;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.market.view.StockSortTitleView;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.DengtaSettingPref;
import com.dengtacj.component.entity.ShareParams;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StringUtil;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by davidwei on 2015/9/11.
 * AH股溢价的列表
 */
public final class AHPremiumStockListActivity extends BaseFragmentActivity implements View.OnClickListener, Handler.Callback, Runnable,
        PtrHandler, StockSortTitleView.OnSortStateChangeListener, OnGetDataCallback<ArrayList<AHPlateDesc>> {
    private static final String TAG = AHPremiumStockListActivity.class.getSimpleName();

    private static final int STOCK_MAX_COUNT = 200;

    public static final int MSG_UPDATE_DATA = 1;
    public static final int MSG_HANDLE_FAILED = 2;

    private int mSortType = StockSortTitleView.STATE_SORT_DESCEND;

    private PtrFrameLayout mPullRefreshLayout;
    private ListView mListView;
    private StockListAdapter mAdapter;
    private Handler mHandler;
    private PeriodicHandlerManager mPeriodicHandlerManager;

    private AHPremiumComparator mDescendAHPremiumComparator;
    private AHPremiumComparator mAscendAHPremiumComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_ah_premium_stock_list);

        mHandler = new Handler(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        findViewById(R.id.actionbar_share).setOnClickListener(this);
        findViewById(R.id.actionbar_faq).setOnClickListener(this);
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.market_ah);

        ((StockSortTitleView)findViewById(R.id.premiumTitle)).setStateListener(this);

        mPullRefreshLayout = (PtrFrameLayout) findViewById(R.id.ptr);
        mPullRefreshLayout.setPtrHandler(this);

        mListView = (ListView) mPullRefreshLayout.findViewById(R.id.list);
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
        requestData();
        mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
    }

    private void requestData() {
        final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance().getManager(IMarketManager.class.getName());
        if (marketManager == null) {
            return;
        }
        switch (mSortType) {
            case StockSortTitleView.STATE_SORT_DESCEND:
                marketManager.requestAHStockIncreaseList(STOCK_MAX_COUNT, this);
                break;
            case StockSortTitleView.STATE_SORT_ASCEND:
                marketManager.requestAHStockDecreaseList(STOCK_MAX_COUNT, this);
                break;
            default:
                return;
        }
    }

    @Override
    public void onGetData(ArrayList<AHPlateDesc> stockList) {
        final int size = stockList == null ? 0 : stockList.size();
        DtLog.d(TAG, "onGetData : size = " + size);
        if (stockList != null && stockList.size() > 0) {
            mHandler.obtainMessage(MSG_UPDATE_DATA, stockList).sendToTarget();
        } else {
            mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_DATA:
                mPullRefreshLayout.refreshComplete();
                updateStockList((ArrayList<AHPlateDesc>) msg.obj);
                break;
            case MSG_HANDLE_FAILED:
                mPullRefreshLayout.refreshComplete();
                break;
            default:
                break;
        }
        return true;
    }

    private void updateStockList(final ArrayList<AHPlateDesc> stockList) {
        Comparator comparator = getDescendAHPremiumComparator();
        if (mSortType == StockSortTitleView.STATE_SORT_ASCEND) {
            comparator = getAscendAHPremiumComparator();
        }

        Collections.sort(stockList, comparator);

        StockListAdapter adapter = mAdapter;
        if (adapter == null) {
            adapter = new StockListAdapter(this, stockList, R.layout.market_ah_stock_list_item);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(adapter);
            mAdapter = adapter;
        } else {
            adapter.setData(stockList);
            adapter.notifyDataSetChanged();
        }
    }

    private AHPremiumComparator getDescendAHPremiumComparator() {
        if (mDescendAHPremiumComparator == null) {
            mDescendAHPremiumComparator = new AHPremiumComparator(StockSortTitleView.STATE_SORT_DESCEND);
        }
        return mDescendAHPremiumComparator;
    }

    private AHPremiumComparator getAscendAHPremiumComparator() {
        if (mAscendAHPremiumComparator == null) {
            mAscendAHPremiumComparator = new AHPremiumComparator(StockSortTitleView.STATE_SORT_ASCEND);
        }
        return mAscendAHPremiumComparator;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.actionbar_share:
                shareList();
                break;
            case R.id.actionbar_faq:
                WebBeaconJump.showCommonWebActivity(this, WebUrlManager.getInstance().getAHPremiumFaqUrl());
                break;
            default:
                break;
        }
    }

    private void shareList() {
        if (isDestroy()) {
            return;
        }

        final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
                .getManager(IShareManager.class.getName());
        if (shareManager == null) {
            return;
        }

        final Resources resources = getResources();
        final WebUrlManager urlManager = DengtaApplication.getApplication().getUrlManager();
        final ShareParams shareParams = ShareParams.createShareParams(resources.getString(R.string.share_ah_list_title),
                resources.getString(R.string.share_ah_list_msg), urlManager.getAHPremiumListShareUrl(), urlManager.getShareIconUrl());

        shareManager.showShareDialog(this, shareParams);

        StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_SHARE_APP);
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
    public void onRefreshBegin(PtrFrameLayout frame) {
        refresh();
    }

    @Override
    public void onSortStateChanged(View view, int newState) {
        if (mSortType == newState) {
            return;
        }
        mSortType = newState;

        if (mAdapter != null) {
            updateStockList((ArrayList<AHPlateDesc>) mAdapter.getData());
        }
    }

    private final class StockListAdapter extends CommonAdapter<AHPlateDesc> implements AdapterView.OnItemClickListener {

        public StockListAdapter(Context context, List<AHPlateDesc> data, int itemLayoutId) {
            super(context, data, itemLayoutId);
        }

        @Override
        public void convert(CommonViewHolder holder, AHPlateDesc item, int position) {
            final TextView title = holder.getView(R.id.title);
            final TextView hSharesInfoView = holder.getView(R.id.hSharesInfo);
            final TextView aSharesInfoView = holder.getView(R.id.aSharesInfo);
            final TextView premiumInfoView = holder.getView(R.id.premiumInfo);

            title.setText(item.sASecName);

            hSharesInfoView.setText(StringUtil.getAHInfoStringSpannable(item.fHKNow, item.fHKIncrease / 100));

            aSharesInfoView.setText(StringUtil.getAHInfoStringSpannable(item.fANow, item.fAIncrease));

            float exchangeRate = 0.8f;
            final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance().getManager(IMarketManager.class.getName());
            if (marketManager != null) {
                exchangeRate = marketManager.getHKDollarsExchangeRate();
            }
            premiumInfoView.setText(StringUtil.getAHPremiumStringSpannable(item.fHKNow, item.fANow, exchangeRate));
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AHPremiumStockListActivity activity = AHPremiumStockListActivity.this;
            if (activity.isDestroy()) {
                return;
            }

            // 因为有headerview，所以position位置不对
            final CommonViewHolder holder = (CommonViewHolder) view.getTag();
            final AHPlateDesc ahItem = getItem(holder.getPosition());
            WebBeaconJump.showAhPremiumDetail(AHPremiumStockListActivity.this, ahItem.sADtSecCode, ahItem.sASecName, ahItem.sHKDtSecCode, ahItem.sHKSecName);
        }
    }
}
