package com.sscf.investment.market;

import BEC.*;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.RefreshButton;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.market.view.StockSortTitleView;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.utils.SecListItemUtils;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.widget.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by davidwei on 2015/9/11.
 * 涨幅榜，跌幅榜的列表
 */
public final class StockUpDownListActivity extends BaseFragmentActivity implements View.OnClickListener, Handler.Callback, Runnable,
        PtrHandler, OnGetDataCallback<ArrayList<PlateQuoteDesc>>, StockSortTitleView.OnSortStateChangeListener{
    private static final String EXTRA_SORT_TYPE = "extra_sort_type";
    private static final String EXTRA_LIST_TYPE = "extra_list_type";

    private int mSortType;

    private RefreshButton mRefreshButton;

    private ArrayList<SecListItem> mSecList;

    /**
     * A股
     */
    private static final int LIST_TYPE_A_SHARES = 1;
    /**
     * 港股主板
     */
    private static final int LIST_TYPE_H_SHARES_MAIN = 2;
    /**
     * 港股创业板
     */
    private static final int LIST_TYPE_H_SHARES_GEM = 3;
    /**
     * 沪股通
     */
    private static final int LIST_TYPE_SH_CONNECT = 4;
    /**
     * 港股通
     */
    private static final int LIST_TYPE_HK_CONNECT = 5;
    /**
     * 中概股
     */
    private static final int LIST_TYPE_CCS = 6;
    /**
     * 标普500
     */
    private static final int LIST_TYPE_SPX = 7;

    private int mStockListType;

    private static final int STOCK_MAX_COUNT = 100;

    private static final int MSG_UPDATE_LIST = 1;
    public static final int MSG_HANDLE_FAILED = 2;

    private PtrFrameLayout mPullRefreshLayout;
    private ListView mListView;
    private TextView mTitleView;
    private StockListAdapter mAdapter;
    private Handler mHandler;
    private PeriodicHandlerManager mPeriodicHandlerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSortType = getIntent().getIntExtra(EXTRA_SORT_TYPE, -1);
        if (mSortType < 0) {
            finish();
            return;
        }

        mStockListType = getIntent().getIntExtra(EXTRA_LIST_TYPE, -1);
        if (mStockListType < 0) {
            finish();
            return;
        }

        setContentView(R.layout.market_stock_list);

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
        mRefreshButton = (RefreshButton) findViewById(R.id.refresh_button);
        mRefreshButton.setOnClickListener(this);
        mTitleView = ((TextView) findViewById(R.id.actionbar_title));
        setTitleText();

        ((StockSortTitleView)findViewById(R.id.deltaTitle)).setStateListener(this);

        mPullRefreshLayout = (PtrFrameLayout) findViewById(R.id.ptr);
        mPullRefreshLayout.setPtrHandler(this);
        mListView = (ListView) mPullRefreshLayout.findViewById(R.id.list);
    }

    public void setTitleText() {
        switch (mStockListType) {
            case LIST_TYPE_A_SHARES:
                mTitleView.setText("");
                break;
            case LIST_TYPE_H_SHARES_MAIN:
                mTitleView.setText(R.string.market_main_broad);
                break;
            case LIST_TYPE_H_SHARES_GEM:
                mTitleView.setText(R.string.market_gem);
                break;
            case LIST_TYPE_SH_CONNECT:
                mTitleView.setText(R.string.market_sh_connect);
                break;
            case LIST_TYPE_HK_CONNECT:
                mTitleView.setText(R.string.market_hk_connect);
                break;
            case LIST_TYPE_CCS:
                mTitleView.setText(R.string.market_ccs);
                break;
            case LIST_TYPE_SPX:
                mTitleView.setText(R.string.market_spx);
                break;
            default:
                finish();
                return;
        }

        int titleId = 0;
        switch (mSortType) {
            case StockSortTitleView.STATE_SORT_DESCEND:
                titleId = R.string.market_increase;
                break;
            case StockSortTitleView.STATE_SORT_ASCEND:
                titleId = R.string.market_decrease;
                break;
            default:
                finish();
                return;
        }
        mTitleView.append(getString(titleId));
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
        if (marketManager == null) {
            return;
        }
        if (mStockListType == LIST_TYPE_A_SHARES) {
            switch (mSortType) {
                case StockSortTitleView.STATE_SORT_DESCEND:
                    marketManager.requestStockIncreaseList(STOCK_MAX_COUNT, this);
                    break;
                case StockSortTitleView.STATE_SORT_ASCEND:
                    marketManager.requestStockDecreaseList(STOCK_MAX_COUNT, this);
                    break;
                default:
                    return;
            }
        } else if (mStockListType == LIST_TYPE_H_SHARES_MAIN) {
            switch (mSortType) {
                case StockSortTitleView.STATE_SORT_DESCEND:
                    marketManager.requestHKMainStockIncreaseList(STOCK_MAX_COUNT, this);
                    break;
                case StockSortTitleView.STATE_SORT_ASCEND:
                    marketManager.requestHKMainStockDecreaseList(STOCK_MAX_COUNT, this);
                    break;
                default:
                    return;
            }
        } else if (mStockListType == LIST_TYPE_H_SHARES_GEM) {
            switch (mSortType) {
                case StockSortTitleView.STATE_SORT_DESCEND:
                    marketManager.requestHKGEMStockIncreaseList(STOCK_MAX_COUNT, this);
                    break;
                case StockSortTitleView.STATE_SORT_ASCEND:
                    marketManager.requestHKGEMStockDecreaseList(STOCK_MAX_COUNT, this);
                    break;
                default:
                    return;
            }
        } else if (mStockListType == LIST_TYPE_SH_CONNECT) {
            switch (mSortType) {
                case StockSortTitleView.STATE_SORT_DESCEND:
                    marketManager.requestSHConnectStockIncreaseList(STOCK_MAX_COUNT, this);
                    break;
                case StockSortTitleView.STATE_SORT_ASCEND:
                    marketManager.requestSHConnectStockDecreaseList(STOCK_MAX_COUNT, this);
                    break;
                default:
                    return;
            }
        } else if (mStockListType == LIST_TYPE_HK_CONNECT) {
            switch (mSortType) {
                case StockSortTitleView.STATE_SORT_DESCEND:
                    marketManager.requestHKConnectStockIncreaseList(STOCK_MAX_COUNT, this);
                    break;
                case StockSortTitleView.STATE_SORT_ASCEND:
                    marketManager.requestHKConnectStockDecreaseList(STOCK_MAX_COUNT, this);
                    break;
                default:
                    return;
            }
        } else if (mStockListType == LIST_TYPE_CCS) {
            switch (mSortType) {
                case StockSortTitleView.STATE_SORT_DESCEND:
                    marketManager.requestCCSStockIncreaseList(STOCK_MAX_COUNT, this);
                    break;
                case StockSortTitleView.STATE_SORT_ASCEND:
                    marketManager.requestCCSStockDecreaseList(STOCK_MAX_COUNT, this);
                    break;
                default:
                    return;
            }
        } else if (mStockListType == LIST_TYPE_SPX) {
            switch (mSortType) {
                case StockSortTitleView.STATE_SORT_DESCEND:
                    marketManager.requestSPXStockIncreaseList(STOCK_MAX_COUNT, this);
                    break;
                case StockSortTitleView.STATE_SORT_ASCEND:
                    marketManager.requestSPXStockDecreaseList(STOCK_MAX_COUNT, this);
                    break;
                default:
                    return;
            }
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
    public void onSortStateChanged(View view, int newState) {
        if (mSortType == newState) {
            return;
        }
        mSortType = newState;
        mPeriodicHandlerManager.runPeriodic();
        setTitleText();
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
            delta.setText(StringUtil.getUpDownStringSpannable(item.fNow, item.fClose));

            // 是否显示次新股的tag
            if (item.stSecAttr != null && item.stSecAttr.mSecAttr != null) {
                final MultiTagView multiTagView = holder.getView(R.id.multiTagView);
                switch (mStockListType) {
                    case LIST_TYPE_A_SHARES:
                    case LIST_TYPE_SH_CONNECT:
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
                        break;
                    default:
                        multiTagView.clearAllTags();
                        break;
                }
            }

            StockUtil.updateStockTagIcon((ImageView) holder.getView(R.id.stock_tag_icon), item.sDtSecCode);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 因为有headerview，所以position位置不对
            final CommonViewHolder holder = (CommonViewHolder) view.getTag();
            final PlateQuoteDesc item = getItem(holder.getPosition());
            final ArrayList<SecListItem> secList = mSecList;
            CommonBeaconJump.showSecurityDetailActivity(StockUpDownListActivity.this, item.getSDtSecCode(), item.getSSecName(), secList);
        }
    }

    /**
     * A股涨幅榜
     * @param context
     */
    public static void showASharesIncreaseList(final Context context) {
        show(context, StockSortTitleView.STATE_SORT_DESCEND, LIST_TYPE_A_SHARES);
    }

    /**
     * A股跌幅榜
     * @param context
     */
    public static void showASharesDecreaseList(final Context context) {
        show(context, StockSortTitleView.STATE_SORT_ASCEND, LIST_TYPE_A_SHARES);
    }

    /**
     * H股主板涨幅榜
     * @param context
     */
    public static void showHKMainIncreaseList(final Context context) {
        show(context, StockSortTitleView.STATE_SORT_DESCEND, LIST_TYPE_H_SHARES_MAIN);
    }

    /**
     * H股创业板涨幅榜
     * @param context
     */
    public static void showHKGEMIncreaseList(final Context context) {
        show(context, StockSortTitleView.STATE_SORT_DESCEND, LIST_TYPE_H_SHARES_GEM);
    }

    /**
     * 沪股通涨幅榜
     * @param context
     */
    public static void showSHConnectStockIncreaseList(final Context context) {
        show(context, StockSortTitleView.STATE_SORT_DESCEND, LIST_TYPE_SH_CONNECT);
    }

    /**
     * 港股通涨幅榜
     * @param context
     */
    public static void showHKConnectStockIncreaseList(final Context context) {
        show(context, StockSortTitleView.STATE_SORT_DESCEND, LIST_TYPE_HK_CONNECT);
    }

    /**
     * 中概股涨幅榜
     * @param context
     */
    public static void showCCSIncreaseList(final Context context) {
        show(context, StockSortTitleView.STATE_SORT_DESCEND, LIST_TYPE_CCS);
    }

    /**
     * 中概股跌幅榜
     * @param context
     */
    public static void showCCSDecreaseList(final Context context) {
        show(context, StockSortTitleView.STATE_SORT_ASCEND, LIST_TYPE_CCS);
    }

    /**
     * 标普500涨幅榜
     * @param context
     */
    public static void showSPXIncreaseList(final Context context) {
        show(context, StockSortTitleView.STATE_SORT_DESCEND, LIST_TYPE_SPX);
    }

    /**
     * 标普500跌幅榜
     * @param context
     */
    public static void showSPXDecreaseList(final Context context) {
        show(context, StockSortTitleView.STATE_SORT_ASCEND, LIST_TYPE_SPX);
    }

    private static void show(final Context context, final int type, final int listType) {
        final Intent intent = new Intent(context, StockUpDownListActivity.class);
        intent.putExtra(EXTRA_SORT_TYPE, type);
        intent.putExtra(EXTRA_LIST_TYPE, listType);
        context.startActivity(intent);
    }

}
