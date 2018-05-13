package com.sscf.investment.market;

import BEC.E_SEC_ATTR;
import BEC.PlateQuoteDesc;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.RefreshButton;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.sdk.utils.NetUtil;
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
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.*;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.widget.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by davidwei on 2015/9/15.
 * 涨幅榜，跌幅榜的列表
 */
public final class StockTurnoverRateListActivity extends BaseFragmentActivity implements View.OnClickListener, Handler.Callback,
        PtrHandler, Runnable, OnGetDataCallback<ArrayList<PlateQuoteDesc>> {
    private static final int STOCK_MAX_COUNT = 100;

    private static final int MSG_UPDATE_LIST = 1;
    public static final int MSG_HANDLE_FAILED = 2;

    private PtrFrameLayout mPullRefreshLayout;
    private ListView mListView;
    private StockListAdapter mAdapter;
    private Handler mHandler;
    private RefreshButton mRefreshButton;

    private PeriodicHandlerManager mPeriodicHandlerManager;

    private ArrayList<SecListItem> mSecList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.market_stock_turnover_rate_list);

        mHandler = new Handler(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    public void initViews() {
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        mRefreshButton = (RefreshButton) findViewById(R.id.refresh_button);
        mRefreshButton.setOnClickListener(this);
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.market_turnover_rate);

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
        mRefreshButton.startLoadingAnim();
        requestData();
        mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
    }

    private void requestData() {
        final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance().getManager(IMarketManager.class.getName());
        if (marketManager != null) {
            marketManager.requestStockTurnoverRateList(STOCK_MAX_COUNT, this);
        }
    }

    @Override
    public void onGetData(ArrayList<PlateQuoteDesc> stockList) {
        if (stockList != null) {
            mSecList = SecListItemUtils.getSecListFromPlateQuoteDescList(stockList);
            mHandler.obtainMessage(MSG_UPDATE_LIST, stockList).sendToTarget();
        } else {
            mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_LIST:
                mPullRefreshLayout.refreshComplete();
                mRefreshButton.stopLoadingAnim();
                updateStockList((ArrayList<PlateQuoteDesc>) msg.obj);
                break;
            case MSG_HANDLE_FAILED:
                mPullRefreshLayout.refreshComplete();
                mRefreshButton.stopLoadingAnim();
                break;
            default:
                break;
        }
        return false;
    }

    private void updateStockList(final ArrayList<PlateQuoteDesc> stockList) {
        final Context context = this;

        StockListAdapter adapter = mAdapter;
        if (adapter == null) {
            adapter = new StockListAdapter(context, stockList, R.layout.market_stock_list_item);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(adapter);
            mAdapter = adapter;
        } else {
            adapter.setData(stockList);
            adapter.notifyDataSetChanged();
        }
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
    public void onRefreshBegin(PtrFrameLayout frame) {
        refresh();
    }

    private final class StockListAdapter extends CommonAdapter<PlateQuoteDesc> implements AdapterView.OnItemClickListener {
        public StockListAdapter(Context context, List<PlateQuoteDesc> data, int itemLayoutId) {
            super(context, data, itemLayoutId);
        }

        @Override
        public void convert(CommonViewHolder holder, PlateQuoteDesc item, int position) {
            final TextView title = holder.getView(R.id.title);
            title.setText(item.sSecName);

            final TextView code = holder.getView(R.id.code);
            code.setText(StockUtil.convertSecInfo(item.sDtSecCode).getSSecCode());

            final TextView price = holder.getView(R.id.price);
            price.setText(StringUtil.getFormattedFloat(item.fNow, item.iTpFlag));
            final TextView delta = holder.getView(R.id.delta_percent);
            delta.setText(StringUtil.getFormattedFloat(item.fFhsl * 100, item.iTpFlag) + '%');

            // 是否显示次新股的tag
            if (item.stSecAttr != null && item.stSecAttr.mSecAttr != null) {
                final MultiTagView multiTagView = holder.getView(R.id.multiTagView);
                Map<Integer, String> map = item.stSecAttr.mSecAttr;
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
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 因为有headerview，所以position位置不对
            final CommonViewHolder holder = (CommonViewHolder) view.getTag();
            final PlateQuoteDesc item = getItem(holder.getPosition());
            final ArrayList<SecListItem> secList = mSecList;
            CommonBeaconJump.showSecurityDetailActivity(StockTurnoverRateListActivity.this, item.getSDtSecCode(), item.getSSecName(), secList);
        }
    }
}
