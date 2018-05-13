package com.sscf.investment.detail.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.entity.SecListItem;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import BEC.E_INDEX_STOCKS_REQ_TYPE;
import BEC.GetIndexStocksReq;
import BEC.GetIndexStocksRsp;
import BEC.SecQuote;

/**
 * Created by liqf on 2015/9/1.
 */
public class RankFragment extends Fragment implements DataSourceProxy.IRequestCallback, Runnable, OnReloadDataListener {
    private static final String TAG = RankFragment.class.getSimpleName();
    private View mContentView;

    private Handler mUiHandler = new Handler();
    private PeriodicHandlerManager mPeriodicHandlerManager;
    private View mContentLayout;
    private ListView mListView;
    private View mEmptyView;
    private View mFailRetryLayout;
    private View mLoadingLayout;

    private String mDtSecCode;
    private int mReqType;

    public static final String KEY_REQ_TYPE = "key_req_type";

    private ArrayList<SecListItem> mSecList;
    private StockListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_rank, container, false);
        mContentView = contextView;

        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
            mReqType = args.getInt(KEY_REQ_TYPE);
        }

        mPeriodicHandlerManager = new PeriodicHandlerManager(this);

        initViews();

        return mContentView;
    }

    private void initViews() {
        mContentLayout = mContentView.findViewById(R.id.content_layout);
        mListView = (ListView) mContentView.findViewById(R.id.listview);
        mEmptyView = mContentView.findViewById(R.id.empty_view);
        mListView.setEmptyView(mEmptyView);

        mFailRetryLayout = mContentView.findViewById(R.id.fail_retry);
        mFailRetryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNetworkData();
            }
        });

        mLoadingLayout = mContentView.findViewById(R.id.loading_layout);
    }

    private void loadNetworkData() {
        if (mSecList == null) {
            showViewByState(DengtaConst.UI_STATE_LOADING);
        }

        GetIndexStocksReq req = new GetIndexStocksReq();
        req.setStUserInfo(DengtaApplication.getApplication().getAccountManager().getUserInfo());
        req.setSIndexDtCode(mDtSecCode);
        req.setIReqType(mReqType);
        DataEngine.getInstance().request(EntityObject.ET_GET_INDEX_STOCK_RANK, req, this);
    }

    private void showViewByState(int state) {
        switch (state) {
            case DengtaConst.UI_STATE_NORMAL:
                mContentLayout.setVisibility(View.VISIBLE);
                mLoadingLayout.setVisibility(View.GONE);
                mFailRetryLayout.setVisibility(View.GONE);
                break;
            case DengtaConst.UI_STATE_LOADING:
                mContentLayout.setVisibility(View.GONE);
                mLoadingLayout.setVisibility(View.VISIBLE);
                mFailRetryLayout.setVisibility(View.GONE);
                break;
            case DengtaConst.UI_STATE_FAILED_RETRY:
                mContentLayout.setVisibility(View.GONE);
                mLoadingLayout.setVisibility(View.GONE);
                mFailRetryLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void callback(boolean success, EntityObject entity) {
        if (!success) {
            DtLog.w(TAG, "callback requestBriefInfo faild");
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    showViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
                }
            });
            return;
        }

        switch (entity.getEntityType()) {
            case EntityObject.ET_GET_INDEX_STOCK_RANK:
                GetIndexStocksRsp rsp = (GetIndexStocksRsp) entity.getEntity();
                final Map<Integer, ArrayList<SecQuote>> stocksMap = rsp.getMStocks();
                final ArrayList<SecQuote> stocks = stocksMap.get(mReqType);

                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!isAdded()) {
                            return;
                        }
                        setData(stocks);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void setData(ArrayList<SecQuote> stocks) {
        showViewByState(DengtaConst.UI_STATE_NORMAL);
        mListView.setFocusable(false); //这一行可以解决listview抢焦点把自己置于屏幕中央的问题

        if (stocks == null) {
            return;
        }

        mSecList = SecListItemUtils.getSecListFromSecQuoteList(stocks);

        mAdapter = new StockListAdapter(getContext(), stocks, R.layout.market_stock_list_item);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
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
        loadNetworkData();
        mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
    }

    @Override
    public void onReloadData() {
        loadNetworkData();
    }

    private final class StockListAdapter extends CommonAdapter<SecQuote> implements AdapterView.OnItemClickListener {

        public StockListAdapter(Context context, List<SecQuote> data, int itemLayoutId) {
            super(context, data, itemLayoutId);
        }

        @Override
        public void convert(CommonViewHolder holder, SecQuote item, int position) {
            final TextView title = holder.getView(R.id.title);
            title.setText(item.sSecName);

            final TextView code = holder.getView(R.id.code);
            code.setText(StockUtil.convertSecInfo(item.sDtSecCode).getSSecCode());

            final TextView price = holder.getView(R.id.price);
            price.setText(StringUtil.getFormattedFloat(item.fNow, item.iTpFlag));

            final TextView delta = holder.getView(R.id.delta_percent);
            if (mReqType == E_INDEX_STOCKS_REQ_TYPE.E_UP_RATE_REQ || mReqType == E_INDEX_STOCKS_REQ_TYPE.E_DOWN_RATE_REQ) {
                delta.setText(StringUtil.getUpDownStringSpannable(item.fNow, item.fClose));
            } else if (mReqType == E_INDEX_STOCKS_REQ_TYPE.E_TURNOVER_RATE_REQ) {
                delta.setText(StringUtil.getPercentString(item.getFFhsl()));
            }

            StockUtil.updateStockTagIcon(holder.getView(R.id.stock_tag_icon), item.sDtSecCode);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 因为有headerview，所以position位置不对
            final CommonViewHolder holder = (CommonViewHolder) view.getTag();
            final SecQuote item = getItem(holder.getPosition());
            final ArrayList<SecListItem> secList = mSecList;
            CommonBeaconJump.showSecurityDetailActivity(getActivity(), item.getSDtSecCode(), item.getSSecName(), secList);
        }
    }
}
