package com.sscf.investment.discover;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.sscf.investment.R;
import com.sscf.investment.comparator.SecSimpleQuoteUpdownComparator;
import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.discover.manager.DiscoverRequestManager;
import com.sscf.investment.discover.manager.SubscriptionManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.MainActivity;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.setting.LoginActivity;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.utils.SecListItemUtils;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.CommonMultiViewTypeAdapter;
import com.sscf.investment.widget.CommonViewHolder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import BEC.ActStrategySubRsp;
import BEC.BEACON_STAT_TYPE;
import BEC.E_SEC_STATUS;
import BEC.IntelliStock;
import BEC.SecSimpleQuote;
import BEC.StrategySubItem;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by davidwei on 2015/10/22.
 */
public final class StockPickSubscriptionFragment extends BaseFragment implements DataSourceProxy.IRequestCallback, Handler.Callback,
        Runnable, View.OnClickListener, SubscriptionManager.SubscriptionCallback, OnReloadDataListener {

    private static final int MSG_UPDATE_STOCK_INFO = 1;
    private static final int MSG_UPDATE_LIST = 2;
    private static final int MSG_UPDATE_FAILED = 3;

    private View mLoginButton;
    private TextView mEmptyViewText;
    private PtrFrameLayout mPtrFrame;
    private ListView mListView;
    private StockPickCardAdapter mAdapter;

    private PeriodicHandlerManager mPeriodicHandlerManager;
    private Handler mHandler;

    private long mAccountId;

    private Map<String, SecSimpleQuote> mStockInfoMap;

    private LocalReceiver mReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        mAccountId = dengtaApplication.getAccountManager().getAccountId();
        mStockInfoMap = new HashMap<>();
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        final View root = inflater.inflate(R.layout.stock_pick_subscription_list, null);
        initViews(root);
        mHandler = new Handler(this);
        mReceiver = new LocalReceiver();
        DengtaApplication.getApplication().registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return root;
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_SMART_PICK);
        helper.setKey(StatConsts.DISCOVER_SUBSCRIPTION);
        return helper;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mReceiver != null) {
            DengtaApplication.getApplication().unregisterReceiver(mReceiver);
        }
    }

    private void initViews(final View root) {
        final Activity activity = getActivity();
        final Resources resources = activity.getResources();

        mLoginButton = root.findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(this);

        mEmptyViewText = (TextView) root.findViewById(R.id.emptyViewText);

        mPtrFrame = (PtrFrameLayout) root.findViewById(R.id.ptr);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (mAccountId > 0) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }
                return false;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                doRefresh();
            }
        });

        mListView = (ListView) root.findViewById(R.id.list);

        final int padding = resources.getDimensionPixelSize(R.dimen.discover_card_padding);

        mListView.setDivider(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.default_background)));
        // 添加分割线，避免下拉刷新动画的显示问题
        mListView.setDividerHeight(padding);

        updateView(null, mAccountId);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        final Activity activity = getActivity();
        switch (v.getId()) {
            case R.id.loginButton:
                activity.startActivity(new Intent(activity, LoginActivity.class));
                StatisticsUtil.reportAction(StatisticsConst.STOCK_PICK_SUBSCRIPTION_CLICK_LOGIN);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        updateView();
        if (NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            requestListData();
            return;
        }
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        updateView();
        mPeriodicHandlerManager.runPeriodic();
    }

    private void updateView() {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        final long accountId = dengtaApplication.getAccountManager().getAccountId();
        final SubscriptionManager subscriptionManager = dengtaApplication.getSubscriptionManager();
        if (accountId == mAccountId) {
            updateView(subscriptionManager.getSubscriptionList(), accountId);
        } else {
            updateView(null, accountId);
            mAccountId = accountId;
        }
        subscriptionManager.saveUpdateTime();
    }

    @Override
    public void onFirstUserInvisible() {
        super.onFirstUserInvisible();
        if (mPeriodicHandlerManager != null) {
            mPeriodicHandlerManager.stop();
        }
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        mPeriodicHandlerManager.stop();
    }

    @Override
    public void onReloadData() {
        if (mAccountId > 0) {
            mPtrFrame.autoRefresh();
        }
    }

    public void doRefresh() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_FAILED);
            return;
        }
        requestListData();
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_FAILED, 10000L);
    }

    @Override
    public void run() {
        mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            return;
        }

        requestData();
        if (!getUserVisibleHint()) {
            mPeriodicHandlerManager.stop(); // 拉取股票信息
        }
    }

    private void requestListData() {
        if (mAccountId > 0) {
            DengtaApplication.getApplication().getSubscriptionManager().getSubscriptionListRequest(this);
        }
    }

    private void requestData() {
        if (mAccountId > 0) {
            final ArrayList<String> dtSecCodeList = DengtaApplication.getApplication().getSubscriptionManager().getSubscriptionDtSecCodeList();
            final int size = dtSecCodeList == null ? 0 : dtSecCodeList.size();
            if (size > 0) {
                QuoteRequestManager.getSimpleQuoteRequest(dtSecCodeList, this, null);
                return;
            }
        }
        mPeriodicHandlerManager.stop();
    }

    @Override
    public void callback(final boolean success, final EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_SIMPLE_QUOTE:
                final ArrayList<SecSimpleQuote> stockList = EntityUtil.entityToSecSimpleQuoteList(success, data);
                if (stockList != null) {
                    for (SecSimpleQuote quote : stockList) {
                        mStockInfoMap.put(quote.sDtSecCode, quote);
                    }
                    mHandler.sendEmptyMessage(MSG_UPDATE_STOCK_INFO);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetSubscriptionList(final boolean success, final ArrayList<StrategySubItem> subscriptionList) {
        if (!success) {
            mHandler.sendEmptyMessage(MSG_UPDATE_FAILED);
            return;
        }

        mHandler.obtainMessage(MSG_UPDATE_LIST, subscriptionList).sendToTarget();
        mPeriodicHandlerManager.runPeriodic();
    }

    @Override
    public boolean handleMessage(Message msg) {
        final Activity activity = getActivity();
        if (activity == null) {
            return true;
        }

        switch (msg.what) {
            case MSG_UPDATE_LIST:
                mPtrFrame.refreshComplete();
                updateView((ArrayList<StrategySubItem>) msg.obj, mAccountId);
                mHandler.removeMessages(MSG_UPDATE_FAILED);
                break;
            case MSG_UPDATE_STOCK_INFO:
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case MSG_UPDATE_FAILED:
                mPtrFrame.refreshComplete();
                DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                mHandler.removeMessages(MSG_UPDATE_FAILED);
                mHandler.removeMessages(MSG_UPDATE_LIST);
                break;
            default:
                break;
        }
        return true;
    }

    private void updateView(final ArrayList<StrategySubItem> subscriptionList, final long accountId) {
        if (accountId > 0) {
            mLoginButton.setVisibility(View.INVISIBLE);
            final int size = subscriptionList == null ? 0 : subscriptionList.size();
            if (size > 0) {
                mListView.setVisibility(View.VISIBLE);
                mEmptyViewText.setVisibility(View.INVISIBLE);
            } else {
                mEmptyViewText.setVisibility(View.VISIBLE);
                mEmptyViewText.setText(R.string.stock_pick_subscription_empty);
                mListView.setVisibility(View.INVISIBLE);
            }

            StockPickCardAdapter adapter = mAdapter;
            if (adapter == null) {
                adapter = new StockPickCardAdapter(getActivity(), subscriptionList);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(adapter);
                mAdapter = adapter;
            } else {
                if (adapter.getData() != subscriptionList) {
                    adapter.setData(subscriptionList);
                    adapter.notifyDataSetChanged();
                }
            }
            mPeriodicHandlerManager.runPeriodic();
        } else {
            mLoginButton.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.INVISIBLE);
            mEmptyViewText.setVisibility(View.VISIBLE);
            mEmptyViewText.setText(R.string.stock_pick_subscription_login_tips);
        }
    }

    private final class StockPickCardAdapter extends CommonMultiViewTypeAdapter<StrategySubItem> implements View.OnClickListener,
            AdapterView.OnItemClickListener, DataSourceProxy.IRequestCallback {

        private SecSimpleQuoteUpdownComparator mComparator;

        public StockPickCardAdapter(Context context, List<StrategySubItem> data) {
            super(context, data, new int[]{R.layout.discover_stock_pick_card_item,
                    R.layout.discover_stock_pick_card_item_offline});
            mComparator = new SecSimpleQuoteUpdownComparator();
        }

        @Override
        public void convert(CommonViewHolder holder, StrategySubItem item, int position) {
            holder.setText(R.id.title, item.sTitle);
            holder.setText(R.id.stockPickAvgUpdownTitle, item.sMaxRetUpBanner);
            holder.setText(R.id.stockPickAvgUpdownValue, StringUtil.getChangePercentStringWithSign(item.fMaxRetAvgIncrease, false));
            holder.setText(R.id.source, item.sSource);

            String date = item.sDate;
            if (date != null) {
                final int length = date.length();
                if (length >= 10) {// 取后面5个字符
                    date = date.substring(length - 5, length);
                }
            }
            holder.setText(R.id.date, date);

            switch (getItemViewType(position)) {
                case 0:
                    updateStockInfos(holder, item);
                    holder.setText(R.id.subscription, mContext.getString(R.string.subscription_content, StringUtil.getAmountString(item.iSubscriptionsCount, 1)));
                    holder.setText(R.id.winningRateValue, StringUtil.getFormattedFloat(item.fSuccPercent * 100, 2) + '%');
                    break;
                case 1:
                    final View removeButton = holder.getView( R.id.removeStrategyButton);
                    removeButton.setTag(item.sId);
                    removeButton.setOnClickListener(this);
                    holder.setText(R.id.subscription, mContext.getString(R.string.subscription_content, String.valueOf(0)));
                    holder.setText(R.id.winningRateValue, "--");
                    break;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).iStatus == 1 ? 0 : 1;
        }

        private void updateStockInfos(CommonViewHolder holder, StrategySubItem item) {
            final ArrayList<IntelliStock> stockList = item.vtIntelliStock;

            SecSimpleQuote[] quotes = (SecSimpleQuote[]) holder.tag;
            final int QUOTES_LENGTH = 3;
            if (quotes == null) {
                quotes = new SecSimpleQuote[QUOTES_LENGTH];
                holder.tag = quotes;
            }

            for (int i = 0; i < QUOTES_LENGTH; i++) {
                quotes[i] = getQuote(i, stockList);
            }

            Arrays.sort(quotes, mComparator);

            updateStockInfo(holder.getView(R.id.discoverStockItem0), quotes[0], quotes);
            updateStockInfo(holder.getView(R.id.discoverStockItem1), quotes[1], quotes);
            updateStockInfo(holder.getView(R.id.discoverStockItem2), quotes[2], quotes);
        }

        public SecSimpleQuote getQuote(int index, ArrayList<IntelliStock> stockList) {
            SecSimpleQuote stockQuote = null;
            if (index < stockList.size()) {
                IntelliStock intelliStock = stockList.get(index);
                if (intelliStock != null) {
                    stockQuote = mStockInfoMap.get(intelliStock.sDtSecCode);
                    if (stockQuote == null) {
                        stockQuote = new SecSimpleQuote();
                        stockQuote.sSecName = intelliStock.sSecName;
                        stockQuote.sDtSecCode = intelliStock.sDtSecCode;
                    }
                }
            }
            return stockQuote;
        }

        private void updateStockInfo(final View stockView, final SecSimpleQuote stockQuote, final SecSimpleQuote[] quotes) {

            final TextView nameView = (TextView) stockView.findViewById(R.id.discoverStockName);
            final TextView updownView = (TextView) stockView.findViewById(R.id.discoverStockUpdown);

            CharSequence name = "";
            CharSequence updown = "";
            if (stockQuote != null) {
                name = stockQuote.sSecName;

                if (stockQuote.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
                    updown = getResources().getString(R.string.stock_stop);
                } else {
                    updown = StringUtil.getUpDownStringSpannable(stockQuote.fNow, stockQuote.fClose);
                }
                stockView.setTag(stockQuote);
                stockView.setTag(R.id.stock_items, quotes);
                stockView.setOnClickListener(this);
            } else {
                stockView.setTag(null);
                stockView.setTag(R.id.stock_items, null);
                stockView.setOnClickListener(null);
            }

            nameView.setText(name);
            updownView.setText(updown);
        }

        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }

            switch (v.getId()) {
                case R.id.removeStrategyButton:
                    removeSubscription(v);
                    break;
                default:
                    clickStockItem(v);
                    break;
            }
        }

        private void removeSubscription(final View v) {
            final MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                if (NetUtil.isNetWorkConnected(mainActivity)) {
                    mainActivity.showLoadingDialog();
                    final String id = (String) v.getTag();
                    DiscoverRequestManager.removeSubscriptionRequest(id, this);
                } else {
                    DengtaApplication.getApplication().showToast(R.string.error_tips_network_error);
                }
            }
        }

        @Override
        public void callback(boolean success, EntityObject data) {
            final MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.dismissLoadingDialog();
            }

            switch (data.getEntityType()) {
                case EntityObject.ET_STOCK_PICK_REMOVE_SUBSCRIPTION:
                    if (success && data.getEntity() != null) {
                        final ActStrategySubRsp rsp = (ActStrategySubRsp) data.getEntity();
                        if (rsp.iRetCode == 0) { // 删除成功
                            requestListData();
                            return;
                        }
                    }
                    break;
                default:
                    break;
            }
            DengtaApplication.getApplication().showToast(R.string.stock_pick_remove_subscription_error);
        }

        private void clickStockItem(final View v) {
            final SecSimpleQuote stockInfo = (SecSimpleQuote) v.getTag();
            if (stockInfo != null) {
                final SecSimpleQuote[] quotes = (SecSimpleQuote[]) v.getTag(R.id.stock_items);
                final ArrayList<SecListItem> secItems = SecListItemUtils.getSecListFromSecSimpleQuoteList(quotes);
                CommonBeaconJump.showSecurityDetailActivity(getActivity(), stockInfo.sDtSecCode, stockInfo.sSecName, secItems);
                StatisticsUtil.reportAction(StatisticsConst.STOCK_PICK_SUBSCRIPTION_CLICK_STOCK);
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Activity activity = getActivity();
            if (activity == null) {
                return;
            }

            if (TimeUtils.isFrequentOperation()) {
                return;
            }

            final CommonViewHolder holder = (CommonViewHolder) view.getTag();
            if (holder == null) {
                return;
            }

            final StrategySubItem item = getItem(holder.getPosition());
            if (item == null) {
                return;
            }

            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            if (scheme != null) {
                scheme.handleUrl(activity, item.sUrl);
            }

            StatisticsUtil.reportAction(StatisticsConst.STOCK_PICK_SUBSCRIPTION_CLICK_CARD);
        }
    }

    private final class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                if (NetUtil.isNetWorkConnected(context)) {
                    if (mAdapter == null || mAdapter.getCount() == 0) {
                        requestListData();
                    }
                }
            }
        }
    }
}
