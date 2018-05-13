package com.sscf.investment.market;

import BEC.CapitalDetailDesc;
import BEC.E_SEC_ATTR;
import BEC.E_SEC_STATUS;
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
import android.content.Intent;
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
import com.sscf.investment.market.view.StockSortTitleView;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.*;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.widget.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by davidwei on 2015/9/15.
 */
public final class CapitalFlowStockListActivity extends BaseFragmentActivity implements OnGetDataCallback<ArrayList<CapitalDetailDesc>>,
        View.OnClickListener, Handler.Callback, StockSortTitleView.OnSortStateChangeListener, Runnable, PtrHandler {
    private PtrFrameLayout mPullRefreshLayout;
    private ListView mListView;
    private IndustryListAdapter mAdapter;
    private TextView mTitleView;
    private RefreshButton mRefreshButton;

    private static final String EXTRA_SORT_TYPE = "extra_sort_type";

    private static final int MSG_UPDATE_STOCK_LIST = 1;
    public static final int MSG_HANDLE_FAILED = 2;

    private static final int STOCK_LIST_COUNT = 100;

    private Handler mHandler;
    private PeriodicHandlerManager mPeriodicHandlerManager;

    private int mSortType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_activity_capital_flow_stock_list);

        mHandler = new Handler(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);

        mSortType = getIntent().getIntExtra(EXTRA_SORT_TYPE, StockSortTitleView.STATE_SORT_DESCEND);

        initViews();
    }

    private void initViews() {
        mTitleView = ((TextView) findViewById(R.id.actionbar_title));
        setTitleText();
        final View backButton = findViewById(R.id.actionbar_back_button);
        backButton.setOnClickListener(this);
        mRefreshButton = (RefreshButton) findViewById(R.id.refresh_button);
        mRefreshButton.setOnClickListener(this);
        ((StockSortTitleView)findViewById(R.id.deltaTitle)).setStateListener(this);

        mPullRefreshLayout = (PtrFrameLayout) findViewById(R.id.ptr);
        mPullRefreshLayout.setPtrHandler(this);

        mListView = (ListView) mPullRefreshLayout.findViewById(R.id.list);
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

    public void setTitleText() {
        int titleId = 0;
        switch (mSortType) {
            case StockSortTitleView.STATE_SORT_DESCEND:
                titleId = R.string.market_stock_capital_flow_increase;
                break;
            case StockSortTitleView.STATE_SORT_ASCEND:
                titleId = R.string.market_stock_capital_flow_decrease;
                break;
            default:
                finish();
                return;
        }
        mTitleView.setText(titleId);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        SwipeBackLayout.attachSwipeLayout(this);
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
            switch (mSortType) {
                case StockSortTitleView.STATE_SORT_DESCEND:
                    marketManager.requestStockCapitalFlowIncreaseList(STOCK_LIST_COUNT, this);
                    break;
                case StockSortTitleView.STATE_SORT_ASCEND:
                    marketManager.requestStockCapitalFlowDecreaseList(STOCK_LIST_COUNT, this);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onGetData(ArrayList<CapitalDetailDesc> data) {
        if (data != null) {
            mHandler.obtainMessage(MSG_UPDATE_STOCK_LIST, data).sendToTarget();
        } else {
            mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_STOCK_LIST:
                mPullRefreshLayout.refreshComplete();
                mRefreshButton.stopLoadingAnim();
                updateStockLick((ArrayList<CapitalDetailDesc>) msg.obj);
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

    private void updateStockLick(final ArrayList<CapitalDetailDesc> capitalDescs) {
        final Context context = this;

        IndustryListAdapter adapter = mAdapter;
        if (adapter == null) {
            adapter = new IndustryListAdapter(context, capitalDescs, R.layout.market_capital_flow_stock_list_item);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(adapter);
            mAdapter = adapter;
        } else {
            adapter.setData(capitalDescs);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSortStateChanged(View view, int newState) {
        if (mSortType == newState) {
            return;
        }
        mSortType = newState;
        setTitleText();
        if (mAdapter == null) {
            return;
        }

        mPeriodicHandlerManager.runPeriodic();
    }

    private final class IndustryListAdapter extends CommonAdapter<CapitalDetailDesc> implements AdapterView.OnItemClickListener {
        public IndustryListAdapter(Context context, List<CapitalDetailDesc> data, int itemLayoutId) {
            super(context, data, itemLayoutId);
        }

        @Override
        public void convert(CommonViewHolder holder, CapitalDetailDesc item, int position) {
            final TextView title = holder.getView(R.id.title);
            title.setText(item.sSecName);

            final TextView code = holder.getView(R.id.code);
            code.setText(StockUtil.convertSecInfo(item.sDtSecCode).getSSecCode());

            final TextView price = holder.getView(R.id.price);
            if (item.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
                price.setText(R.string.stock_stop);
            } else {
                price.setText(StringUtil.getUpdownString(item.fChangeRate));
            }

            final TextView deltaPercent = holder.getView(R.id.delta_percent);
            deltaPercent.setText(StringUtil.getCapitalFlowSpannable(item.fZljlr));

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
            final ArrayList<SecListItem> secList = SecListItemUtils.getSecListFromCapitalDetailDescList((ArrayList<CapitalDetailDesc>) getData());
            final CapitalDetailDesc item = getItem(((CommonViewHolder)view.getTag()).getPosition());
            CommonBeaconJump.showSecurityDetailActivity(mContext, item.getSDtSecCode(), item.getSSecName(), secList);
        }
    }

    public static void showCapitalFlowStockIncreaseListActivity(final Context context) {
        show(context, StockSortTitleView.STATE_SORT_DESCEND);
    }

    public static void showCapitalFlowStockDecreaseListActivity(final Context context) {
        show(context, StockSortTitleView.STATE_SORT_ASCEND);
    }

    private static void show(final Context context, final int type) {
        final Intent intent = new Intent(context, CapitalFlowStockListActivity.class);
        intent.putExtra(EXTRA_SORT_TYPE, type);
        context.startActivity(intent);
    }
}
