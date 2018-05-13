package com.sscf.investment.market;

import BEC.*;
import com.chenenyu.router.annotation.Route;
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
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.market.view.StockSortTitleView;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.*;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.widget.*;
import java.util.*;

/**
 * Created by davidwei on 2015/9/16.
 */
@Route("CapitalFlowStockListInPlateActivity")
public final class CapitalFlowStockListInPlateActivity extends BaseFragmentActivity implements OnGetDataCallback<ArrayList<CapitalMainFlowDesc>>, Runnable, Handler.Callback,
        View.OnClickListener, StockSortTitleView.OnSortStateChangeListener, PtrHandler {
    private static final String TAG = CapitalFlowStockListInPlateActivity.class.getSimpleName();

    private static final String EXTRA_SORT_TYPE = "extra_sort_type";

    private PtrFrameLayout mPullRefreshLayout;
    private ListView mListView;
    private StockListAdapter mAdapter;

    private RefreshButton mRefreshButton;
    private TextView mPlateNameView;
    private TextView mPlateCodeView;
    private TextView mPlateUpdownView;

    private Handler mHandler;
    private PeriodicHandlerManager mPeriodicHandlerManager;

    public static final int MSG_UPDATE_STOCK_LIST = 1;
    public static final int MSG_HANDLE_FAILED = 2;
    public static final int MSG_UPDATE_PLATE_INFO = 3;

    private Comparator mDescendComparator;
    private Comparator mAscendComparator;

    private int mSortType;

    private String mPlateCode;
    private String mPlateName;
    private ArrayList<String> mPlateCodes;
    private ArrayList<String> mStockUnicodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!initData()) {
            return;
        }

        setContentView(R.layout.market_activity_capital_flow_stock_list_in_plate);

        mHandler = new Handler(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        initViews();
    }

    private boolean initData() {
        final Intent intent = getIntent();
        final String plateCode = intent.getStringExtra(DengtaConst.KEY_SEC_CODE);
        if (TextUtils.isEmpty(plateCode)) {
            finish();
            return false;
        }

        mPlateName = intent.getStringExtra(DengtaConst.KEY_SEC_NAME);
        mPlateCode = plateCode;
        mPlateCodes = new ArrayList<String>(1);
        mPlateCodes.add(plateCode);
        int sortType = intent.getIntExtra(EXTRA_SORT_TYPE, StockSortTitleView.STATE_SORT_DESCEND);
        if (sortType != StockSortTitleView.STATE_SORT_DESCEND && sortType != StockSortTitleView.STATE_SORT_ASCEND) {
            sortType = StockSortTitleView.STATE_SORT_DESCEND;
        }
        mSortType = sortType;
        return true;
    }

    private void initViews() {
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        mRefreshButton = (RefreshButton) findViewById(R.id.refresh_button);
        mRefreshButton.setOnClickListener(this);
        ((TextView) findViewById(R.id.actionbar_title)).setText(mPlateName);

        final StockSortTitleView sortTitleView = (StockSortTitleView)findViewById(R.id.deltaTitle);
        sortTitleView.setSortState(mSortType);
        sortTitleView.setStateListener(this);

        findViewById(R.id.plateLayout).setOnClickListener(this);
        mPlateNameView = (TextView) findViewById(R.id.plateName);
        mPlateCodeView = (TextView) findViewById(R.id.plateCode);
        mPlateUpdownView = (TextView) findViewById(R.id.plateUpdown);

        mPlateNameView.setText(mPlateName);
        mPlateCodeView.setText(StockUtil.convertSecInfo(mPlateCode).getSSecCode());

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
            case R.id.plateLayout:
                CommonBeaconJump.showSecurityDetailActivity(this, mPlateCode, mPlateName);
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
            marketManager.requestMultiStockCapitalFlow(mPlateCodes, mPlateCallback);
            if (mStockUnicodeList != null) {
                marketManager.requestMultiStockCapitalFlow(mStockUnicodeList, this);
            } else {
                marketManager.requestStockListInPlate(mPlateCode, (ArrayList<SecSimpleQuote> data) -> {
                    if (data != null && data.size() > 0) {
                        ArrayList<String> stockUnicodeList = new ArrayList<String>(data.size());
                        for (SecSimpleQuote stockInfo : data) {
                            stockUnicodeList.add(stockInfo.sDtSecCode);
                        }
                        mStockUnicodeList = stockUnicodeList;
                        mPeriodicHandlerManager.runPeriodic();
                    } else {
                        mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
                    }
                });
            }
        }
    }

    private final OnGetDataCallback<ArrayList<CapitalMainFlowDesc>> mPlateCallback = (ArrayList<CapitalMainFlowDesc> data) -> {
        if (data != null && data.size() > 0) {
            final CapitalMainFlowDesc plateInfo = data.get(0);
            mHandler.obtainMessage(MSG_UPDATE_PLATE_INFO, plateInfo).sendToTarget();
        } else {
            mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
        }
    };

    @Override
    public void onGetData(ArrayList<CapitalMainFlowDesc> data) {
        DtLog.d(TAG, "onGetData data : " + data);
        if (data != null) {
            switch (mSortType) {
                case StockSortTitleView.STATE_SORT_DESCEND:
                    Collections.sort(data, getDescendComparator());
                    break;
                case StockSortTitleView.STATE_SORT_ASCEND:
                    Collections.sort(data, getAscendComparator());
                    break;
                default:
                    break;
            }
            mHandler.obtainMessage(MSG_UPDATE_STOCK_LIST, data).sendToTarget();
        } else {
            mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_PLATE_INFO:
                final CapitalMainFlowDesc plateInfo = (CapitalMainFlowDesc) msg.obj;
                mPlateUpdownView.setText(StringUtil.getCapitalFlowSpannable(plateInfo.fZljlr));
                break;
            case MSG_UPDATE_STOCK_LIST:
                mPullRefreshLayout.refreshComplete();
                mRefreshButton.stopLoadingAnim();
                updateStockList((ArrayList<CapitalMainFlowDesc>) msg.obj);
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

    private void updateStockList(final ArrayList<CapitalMainFlowDesc> stockList) {
        final Context context = this;

        StockListAdapter adapter = mAdapter;
        if (adapter == null) {
            adapter = new StockListAdapter(context, stockList, R.layout.market_capital_flow_stock_list_item);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(adapter);
            mAdapter = adapter;
        } else {
            adapter.setData(stockList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSortStateChanged(View view, int newState) {
        if (mSortType == newState) {
            return;
        }
        mSortType = newState;
        if (mAdapter == null) {
            return;
        }

        ArrayList<CapitalMainFlowDesc> stockList = (ArrayList<CapitalMainFlowDesc>) mAdapter.getData();
        switch (newState) {
            case StockSortTitleView.STATE_SORT_DESCEND:
                Collections.sort(stockList, getDescendComparator());
                mAdapter.notifyDataSetChanged();
                break;
            case StockSortTitleView.STATE_SORT_ASCEND:
                Collections.sort(stockList, getAscendComparator());
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private Comparator getDescendComparator() {
        if (mDescendComparator == null) {
            mDescendComparator = new CapitalComparator(StockSortTitleView.STATE_SORT_DESCEND);
        }
        return mDescendComparator;
    }

    private Comparator getAscendComparator() {
        if (mAscendComparator == null) {
            mAscendComparator = new CapitalComparator(StockSortTitleView.STATE_SORT_ASCEND);
        }
        return mAscendComparator;
    }

    private static final class CapitalComparator implements Comparator<CapitalMainFlowDesc> {
        private int mSortType;

        private CapitalComparator(int sortType) {
            this.mSortType = sortType;
        }

        @Override
        public int compare(CapitalMainFlowDesc lhs, CapitalMainFlowDesc rhs) {
            int res = 0;
            if (lhs.eSecStatus != E_SEC_STATUS.E_SS_SUSPENDED && rhs.eSecStatus != E_SEC_STATUS.E_SS_SUSPENDED) {
                if (lhs.fZljlr > rhs.fZljlr) {
                    res = -1;
                } else if(lhs.fZljlr < rhs.fZljlr) {
                    res = 1;
                }

                if (mSortType == StockSortTitleView.STATE_SORT_ASCEND) {
                    res = -res;
                }
            } else if (lhs.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED && rhs.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
                // 停牌的显示到最后
                if (lhs.fNow < rhs.fNow) {
                    res = -1;
                } else if(lhs.fNow > rhs.fNow) {
                    res = 1;
                }
            } else if (lhs.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
                res = 1;
            } else {
                res = -1;
            }

            return res;
        }
    }

    private final class StockListAdapter extends CommonAdapter<CapitalMainFlowDesc> implements AdapterView.OnItemClickListener {

        public StockListAdapter(Context context, List<CapitalMainFlowDesc> data, int itemLayoutId) {
            super(context, data, itemLayoutId);
        }

        @Override
        public void convert(CommonViewHolder holder, CapitalMainFlowDesc item, int position) {
            final TextView title = holder.getView(R.id.title);
            title.setText(item.sSecName);

            final TextView code = holder.getView(R.id.code);
            code.setText(StockUtil.convertSecInfo(item.sDtSecCode).getSSecCode());

            final TextView price = holder.getView(R.id.price);

            if (item.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
                // 停牌
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
                    final ArrayList<String> tags = new ArrayList<String>(2);
                    final Resources resources = getResources();
                    if (map.containsKey(E_SEC_ATTR.E_SEC_ATTR_SUB_NEW)) {
                        tags.add(resources.getString(R.string.sub_new));
                    } else if (map.containsKey(E_SEC_ATTR.E_SEC_ATTR_NEW)) {
                        tags.add(resources.getString(R.string.new_stock));
                    }

                    if (map.containsKey(E_SEC_ATTR.E_SEC_ATTR_PLATE_FAUCET)) {
                        tags.add(resources.getString(R.string.tap));
                    }

                    multiTagView.addTags(tags, R.drawable.tag_round_rect_bg_position, R.color.tab_indicatorColor);
                } else {
                    multiTagView.clearAllTags();
                }
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final ArrayList<SecListItem> secList = SecListItemUtils.getSecListFromCapitalMainFlowDescList((ArrayList<CapitalMainFlowDesc>) getData());
            // 因为有headerview，所以position位置不对
            final CommonViewHolder holder = (CommonViewHolder) view.getTag();
            final CapitalMainFlowDesc item = getItem(holder.getPosition());
            CommonBeaconJump.showSecurityDetailActivity(mContext, item.getSDtSecCode(), item.getSSecName(), secList);
        }
    }

    public static void show(final Context context, final String unicode, final String name, final int sortType) {
        Intent intent = new Intent(context, CapitalFlowStockListInPlateActivity.class);
        intent.putExtra(DengtaConst.KEY_SEC_CODE, unicode);
        intent.putExtra(DengtaConst.KEY_SEC_NAME, name);
        intent.putExtra(EXTRA_SORT_TYPE, sortType);
        context.startActivity(intent);
    }
}
