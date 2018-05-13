package com.sscf.investment.detail.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.comparator.SecSimpleQuoteUpdownComparator;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.sdk.entity.SecListItem;
import com.dengtacj.request.MarketRequestManager;
import com.sscf.investment.market.view.StockSortTitleView;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.utils.SecListItemUtils;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.widget.CommonAdapter;
import com.sscf.investment.widget.CommonViewHolder;
import com.sscf.investment.widget.MultiTagView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import BEC.E_SEC_ATTR;
import BEC.E_SEC_STATUS;
import BEC.PlateStockListRsp;
import BEC.SecSimpleQuote;

/**
 * Created by liqf on 2016/6/15.
 */
public class RelatedStockListFragment extends Fragment implements DataSourceProxy.IRequestCallback, Runnable, Handler.Callback, StockSortTitleView.OnSortStateChangeListener, OnReloadDataListener {
    private static final String TAG = RelatedStockListFragment.class.getSimpleName();
    private static final int MAX_SHOW_COUNT = 100;

    private View mContentView;

    private int mSortType = StockSortTitleView.STATE_SORT_DESCEND;

    private Comparator mDescendComparator;
    private Comparator mAscendComparator;

    private String mDtSecCode;

    private Handler mHandler;
    private PeriodicHandlerManager mPeriodicHandlerManager;
    public static final int MSG_UPDATE_STOCK_LIST = 1;
    public static final int MSG_HANDLE_FAILED = 2;

    private ListView mListView;
    private StockListAdapter mAdapter;

    private List<SecSimpleQuote> mShowStockList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_related_stock_list, container, false);
        mContentView = contextView;

        mHandler = new Handler(this);

        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
        }

        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        mListView = (ListView) mContentView.findViewById(R.id.related_stock_listview);
        ((StockSortTitleView) mContentView.findViewById(R.id.deltaTitle)).setStateListener(this);

        return mContentView;
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

    private void requestData() {
        MarketRequestManager.getStockListInPlateRequest(mDtSecCode, this, null);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (!success) {
            mHandler.sendEmptyMessage(MSG_HANDLE_FAILED);
            return;
        }

        final Object entity = data.getEntity();
        if (entity == null) {
            return;
        }

        switch (data.getEntityType()) {
            case EntityObject.ET_GET_STOCK_LIST_IN_INDUSTRY:
                getStockListInIndustryCallback(success, (PlateStockListRsp) entity);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_STOCK_LIST:
                updateStockList((ArrayList<SecSimpleQuote>) msg.obj);
                break;
            case MSG_HANDLE_FAILED:
                break;
            default:
                break;
        }
        return false;
    }

    private void updateStockList(final ArrayList<SecSimpleQuote> stockList) {
        if (!isAdded()) {
            return;
        }

        mShowStockList.clear();
        if (stockList.size() > MAX_SHOW_COUNT) {
            mShowStockList.addAll(stockList.subList(0, MAX_SHOW_COUNT));
        } else {
            mShowStockList.addAll(stockList);
        }

        final Context context = mContentView.getContext();

        mListView.setFocusable(false); //这一行可以解决listview抢焦点把自己置于屏幕中央的问题

        StockListAdapter adapter = mAdapter;
        if (adapter == null) {
            adapter = new StockListAdapter(context, mShowStockList, R.layout.market_stock_list_item);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(adapter);
            mAdapter = adapter;
        } else {
            adapter.setData(mShowStockList);
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

    @Override
    public void onReloadData() {
        requestData();
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

    public void getStockListInIndustryCallback(boolean success, PlateStockListRsp rsp) {
        ArrayList<SecSimpleQuote> stockInfos = rsp.vSecSimpleQuote;

        DtLog.d(TAG, "stockList : " + stockInfos);
        switch (mSortType) {
            case StockSortTitleView.STATE_SORT_DESCEND:
                Collections.sort(stockInfos, getDescendComparator());
                break;
            case StockSortTitleView.STATE_SORT_ASCEND:
                Collections.sort(stockInfos, getAscendComparator());
                break;
            default:
                break;
        }

        mHandler.obtainMessage(MSG_UPDATE_STOCK_LIST, stockInfos).sendToTarget();
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

    @Override
    public void run() {
        requestData();
        mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
    }
}
