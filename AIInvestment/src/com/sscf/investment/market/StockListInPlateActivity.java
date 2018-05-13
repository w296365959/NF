package com.sscf.investment.market;

import BEC.*;
import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.dengtacj.component.managers.IQuoteManager;
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
import com.sscf.investment.comparator.SecSimpleQuoteUpdownComparator;
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
 * Created by davidwei on 2015/9/14.
 * 板块关联个股的列表
 */
@Route("StockListInPlateActivity")
public final class StockListInPlateActivity extends BaseFragmentActivity implements Handler.Callback,
        View.OnClickListener, PtrHandler, Runnable, StockSortTitleView.OnSortStateChangeListener,
        OnGetDataCallback<ArrayList<SecSimpleQuote>> {
    private static final String TAG = StockListInPlateActivity.class.getSimpleName();
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

    private String mPlateCode;
    private String mPlateName;

    private int mSortType = StockSortTitleView.STATE_SORT_DESCEND;

    private Comparator mDescendComparator;
    private Comparator mAscendComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final String plateCode = intent.getStringExtra(DengtaConst.KEY_SEC_CODE);
        if (TextUtils.isEmpty(plateCode)) {
            finish();
        }

        mPlateName = intent.getStringExtra(DengtaConst.KEY_SEC_NAME);
        mPlateCode = plateCode;

        setContentView(R.layout.market_stock_list_in_plate);

        initViews();

        mHandler = new Handler(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
    }

    private void initViews() {
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        mRefreshButton = (RefreshButton) findViewById(R.id.refresh_button);
        mRefreshButton.setOnClickListener(this);
        ((TextView) findViewById(R.id.actionbar_title)).setText(mPlateName);

        findViewById(R.id.plateLayout).setOnClickListener(this);
        mPlateNameView = (TextView) findViewById(R.id.plateName);
        mPlateCodeView = (TextView) findViewById(R.id.plateCode);
        mPlateUpdownView = (TextView) findViewById(R.id.plateUpdown);

        mPlateNameView.setText(mPlateName);
        mPlateCodeView.setText(StockUtil.convertSecInfo(mPlateCode).getSSecCode());

        ((StockSortTitleView)findViewById(R.id.deltaTitle)).setStateListener(this);

        mPullRefreshLayout = (PtrFrameLayout) findViewById(R.id.ptr);
        mPullRefreshLayout.setPtrHandler(this);

        mListView = (ListView) mPullRefreshLayout.findViewById(R.id.list);
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
    public void onResume() {
        super.onResume();
        mPeriodicHandlerManager.runPeriodic();
    }

    @Override
    public void onPause() {
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
            quoteManager.requestSimpleQuote(mPlateCode, mGetQuoteCallback);
        }
        final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance().getManager(IMarketManager.class.getName());
        if (marketManager != null) {
            marketManager.requestStockListInPlate(mPlateCode, this);
        }
    }

    private OnGetDataCallback<SecSimpleQuote> mGetQuoteCallback = (SecSimpleQuote quote) -> {
        if (quote != null) {
            mHandler.obtainMessage(MSG_UPDATE_PLATE_INFO, quote).sendToTarget();
        } else {
            mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
        }
    };

    @Override
    public void onGetData(ArrayList<SecSimpleQuote> stockList) {
        DtLog.d(TAG, "onGetStockList : " + stockList);
        if (stockList != null) {
            switch (mSortType) {
                case StockSortTitleView.STATE_SORT_DESCEND:
                    Collections.sort(stockList, getDescendComparator());
                    break;
                case StockSortTitleView.STATE_SORT_ASCEND:
                    Collections.sort(stockList, getAscendComparator());
                    break;
                default:
                    break;
            }

            mHandler.obtainMessage(MSG_UPDATE_STOCK_LIST, stockList).sendToTarget();
        } else {
            mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_PLATE_INFO:
                final SecSimpleQuote simpleQuote = (SecSimpleQuote) msg.obj;
                mPlateUpdownView.setText(StringUtil.getUpDownStringSpannable(simpleQuote));
                break;
            case MSG_UPDATE_STOCK_LIST:
                mPullRefreshLayout.refreshComplete();
                mRefreshButton.stopLoadingAnim();
                updateStockList((ArrayList<SecSimpleQuote>) msg.obj);
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

    private void updateStockList(final ArrayList<SecSimpleQuote> stockList) {
        mPullRefreshLayout.refreshComplete();

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

    private final class StockListAdapter extends CommonAdapter<SecSimpleQuote> implements AdapterView.OnItemClickListener {
        public StockListAdapter(Context context, List<SecSimpleQuote> data, int itemLayoutId) {
            super(context, data, itemLayoutId);
        }

        @Override
        public void convert(CommonViewHolder holder, SecSimpleQuote item, int position) {
            final TextView title = holder.getView(R.id.title);
            title.setText(item.sSecName);

            final TextView code = holder.getView(R.id.code);
            code.setText(StockUtil.convertSecInfo(item.sDtSecCode).getSSecCode());

            final TextView price = holder.getView(R.id.price);
            final TextView delta = holder.getView(R.id.delta_percent);

            if (item.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
                // 停牌
                delta.setText(R.string.stock_stop);
                delta.setTextColor(StringUtil.getColorBase());
            } else {
                delta.setText(StringUtil.getUpDownStringSpannable(item.fNow, item.fClose));
            }
            price.setText(StringUtil.getFormattedFloat(item.fNow, item.iTpFlag));

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
            // 因为有headerview，所以position位置不对
            final CommonViewHolder holder = (CommonViewHolder) view.getTag();
            final SecSimpleQuote item = getItem(holder.getPosition());
            ArrayList<SecSimpleQuote> stockList = (ArrayList<SecSimpleQuote>) mAdapter.getData();
            final ArrayList<SecListItem> secList = SecListItemUtils.getSecListFromSecSimpleQuoteList(stockList);
            CommonBeaconJump.showSecurityDetailActivity(mContext, item.getSDtSecCode(), item.getSSecName(), secList);
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

        ArrayList<SecSimpleQuote> stockList = (ArrayList<SecSimpleQuote>) mAdapter.getData();
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
            mDescendComparator = new SecSimpleQuoteUpdownComparator(StockSortTitleView.STATE_SORT_DESCEND);
        }
        return mDescendComparator;
    }

    private Comparator getAscendComparator() {
        if (mAscendComparator == null) {
            mAscendComparator = new SecSimpleQuoteUpdownComparator(StockSortTitleView.STATE_SORT_ASCEND);
        }
        return mAscendComparator;
    }
}
