package com.sscf.investment.search.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.db.SearchHistoryItem;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.portfolio.PortfolioGroupManagerActivity;
import com.dengtacj.component.entity.db.GroupEntity;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.sdk.utils.SDkStringUtils;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.search.SearchActivity;
import com.sscf.investment.search.presenter.SearchHistoryPresenter;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StockUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.CommonAdapter;
import com.sscf.investment.widget.CommonViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidwei on 2015-08-12.
 *
 */
public final class SearchHistoryFragment extends BaseFragment implements View.OnClickListener {

    private ListView mListView;
    private SearchHistoryAdapter mAdapter;
    private SearchHistoryPresenter mPresenter;

    // header
    private View mHeader;
    private RecyclerView mRecyclerView;
    private HeaderAdapter mRecyclerViewAdapter;
    private View mHistoryTitle;
    private View mHistoryClear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.fragment_search_history, null);

        initHeader(inflater);

        mPresenter = new SearchHistoryPresenter(this);
        mPresenter.setType(SearchHistoryPresenter.TYPE_HOT_STOCK);
        return mListView;
    }

    private void initHeader(final LayoutInflater inflater) {
        final View header = inflater.inflate(R.layout.fragment_search_history_header, null);
        mHeader = header;
        mHistoryTitle = header.findViewById(R.id.title);
        mHistoryClear = header.findViewById(R.id.clear);
        mHistoryClear.setOnClickListener(this);
        final RecyclerView recyclerView = (RecyclerView) header.findViewById(R.id.recyclerview);
        mRecyclerView = recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserInvisible();
        mPresenter.requestData();
        mPresenter.getHistoryList();
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        mPresenter.getHistoryList();
        if (mRecyclerViewAdapter != null) {
            if (mRecyclerViewAdapter.updateMemberState()) {
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public void updateHeaderList(final List<SecListItem> headerList, int type) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        HeaderAdapter adapter = mRecyclerViewAdapter;
        if (adapter == null) {
            adapter = new HeaderAdapter(activity, headerList);
            mRecyclerViewAdapter = adapter;
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter.setData(headerList);
            adapter.notifyDataSetChanged();
        }
        adapter.setType(type);
    }

    public void updateHistory(List<SearchHistoryItem> searchHistoryItems) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        SearchHistoryAdapter adapter = mAdapter;
        if (adapter == null) {
            mListView.addHeaderView(mHeader);
            adapter = new SearchHistoryAdapter(activity, searchHistoryItems);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(adapter);
            mAdapter = adapter;
        } else {
            adapter.setData(searchHistoryItems);
            adapter.notifyDataSetChanged();
        }

        final int size = searchHistoryItems == null ? 0 : searchHistoryItems.size();
        if (size > 0) {
            mHistoryTitle.setVisibility(View.VISIBLE);
            mHistoryClear.setVisibility(View.VISIBLE);
        } else {
            mHistoryTitle.setVisibility(View.GONE);
            mHistoryClear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear:
                updateHistory(null);
                DengtaApplication.getApplication().defaultExecutor.execute(() -> {
                    StatisticsUtil.reportAction(StatisticsConst.SEARCH_CLEAR_HISTORY);
                    SearchHistoryItem.clearAllItemFromDb();
                });
                break;
            default:
                break;
        }
    }
}

final class HeaderAdapter extends CommonBaseRecyclerViewAdapter<SecListItem> {
    private int mType = SearchHistoryPresenter.TYPE_HOT_STOCK;
    private static final int MAX_COUNT = 8;
    private boolean mLogin;
    private boolean mMember;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_MORE = 1;

    HeaderAdapter(Context context, List<SecListItem> data) {
        super(context, data, R.layout.fragment_search_history_header_item);
        setItemClickable(true);
        updateMemberState();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == MAX_COUNT - 1) ? TYPE_MORE : TYPE_NORMAL;
    }

    void setType(int type) {
        mType = type;
    }

    boolean updateMemberState() {
        boolean changeState = false;
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager != null) {
            final boolean login = accountManager.isLogined();
            final boolean member = accountManager.isMember();
            if (login != mLogin) {
                mLogin = login;
                changeState = true;
            }
            if (member != mMember) {
                mMember = member;
                changeState = true;
            }
        }
        return changeState;
    }

    @Override
    public int getItemCount() {
        final int count = super.getItemCount();
        return count < MAX_COUNT - 1 ? count : MAX_COUNT;
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, SecListItem item, int position) {
        final int type = getItemViewType(position);
        if (type == TYPE_MORE) { // 更多
            final TextView textView = holder.getView(R.id.text);
            textView.setTextColor(0xff929292);
            textView.setText(R.string.more_2);
        } else {
            String name = item.getName();
            if (mType != SearchHistoryPresenter.TYPE_HOT_STOCK && !mMember) {
                name = SDkStringUtils.hideSecName(name);
            }
            holder.setText(R.id.text, name);
        }
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        final int type = getItemViewType(position);
        if (type == TYPE_MORE) { // 更多
            switch (mType) {
                case SearchHistoryPresenter.TYPE_HOT_STOCK:
                    WebBeaconJump.showHotStockRank(mContext);
                    break;
                default:
                    break;
            }
        } else {
            final SecListItem item = getItem(position);
            CommonBeaconJump.showSecurityDetailActivity(mContext, item.getDtSecCode(), item.getName(), (ArrayList<SecListItem>) mData);
            StatisticsUtil.reportAction(StatisticsConst.SEARCH_CLICK_HOT_STOCK);
        }
    }
}

final class SearchHistoryAdapter extends CommonAdapter<SearchHistoryItem> implements AdapterView.OnItemClickListener, View.OnClickListener {
    private final IPortfolioDataManager mPortfolioDataManager;

    SearchHistoryAdapter(Context context, List<SearchHistoryItem> datas) {
        super(context, datas, R.layout.fragment_search_result_stock_item);
        mPortfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
    }

    @Override
    public void convert(CommonViewHolder holder, SearchHistoryItem item, int position) {
        final String dtSecCode = item.getUnicode();

        final TextView tagView = holder.getView(R.id.tag);
        final String tagText = StockUtil.getSearchTagText(dtSecCode);
        if (TextUtils.isEmpty(tagText)) {
            tagView.setVisibility(View.INVISIBLE);
        } else {
            tagView.setVisibility(View.VISIBLE);
            tagView.setText(tagText);
        }

        holder.setText(R.id.name, item.getName());

        final TextView codeView = holder.getView(R.id.code);
        codeView.setText(StockUtil.getSecCode(dtSecCode));

        final boolean isPortfolio = mPortfolioDataManager != null && mPortfolioDataManager.isPortfolio(dtSecCode);
        final ImageView button = holder.getView(R.id.button);
        button.setOnClickListener(this);
        button.setImageResource(isPortfolio ? R.drawable.search_remove_icon : R.drawable.search_add_icon);
        button.setTag(holder);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final CommonViewHolder holder = (CommonViewHolder) view.getTag();
        if (holder == null) {
            return;
        }

        final SearchHistoryItem item = getItem(holder.getPosition());
        if (item == null) {
            return;
        }

        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        final String dtSecCode = item.getUnicode();
        final String secName = item.getName();
        if (CommonBeaconJump.showSecurityDetailActivity(mContext, dtSecCode, secName)) {
            final SearchActivity activity = (SearchActivity) mContext;
            LocalBroadcastManager.getInstance(dengtaApplication).sendBroadcast(new Intent(DengtaConst.ACTION_SEARCH_TO_SECURITY_DETTAIL));
            activity.finish();
            dengtaApplication.defaultExecutor.execute(() -> SearchHistoryItem.addItemToDb(secName, dtSecCode));
        }
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        if (mPortfolioDataManager == null) {
            return;
        }

        final CommonViewHolder holder = (CommonViewHolder) v.getTag();
        final SearchHistoryItem item = getItem(holder.getPosition());

        if(item == null) {
            return;
        }

        final String dtCode = item.getUnicode();
        final boolean isPortfolio = mPortfolioDataManager.isPortfolio(dtCode);

        if (isPortfolio) {
            final ArrayList<String> dtSecCodes = new ArrayList<>(1);
            dtSecCodes.add(dtCode);
            mPortfolioDataManager.deleteStockFromAllGroup(dtSecCodes, true);
            ((ImageView) v).setImageResource(R.drawable.search_add_icon);
        } else {
            StatisticsUtil.reportAction(StatisticsConst.SEARCH_CLICK_ADD);
            final List<StockDbEntity> allStockList = mPortfolioDataManager.getAllStockList(false, false);
            final int size = allStockList == null ? 0 : allStockList.size();
            if (size >= DengtaConst.MAX_PORTFOLIO_COUNT) {
                DengtaApplication.getApplication().showToast(R.string.no_more_portfolio);
                return;
            }

            ((ImageView) v).setImageResource(R.drawable.search_remove_icon);
            final List<GroupEntity> groupList = mPortfolioDataManager.getAllGroup(false, false);
            final int groupSize = groupList == null ? 0 : groupList.size();

            final String secName = item.getName();
            if (groupSize > 0) { // 分组界面添加
                PortfolioGroupManagerActivity.show(v.getContext(), dtCode, secName);
            } else { // 直接添加到默认分组
                mPortfolioDataManager.addStock(dtCode, secName);
            }
        }
    }
}
