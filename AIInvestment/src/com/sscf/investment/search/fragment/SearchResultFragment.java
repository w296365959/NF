package com.sscf.investment.search.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.portfolio.PortfolioGroupManagerActivity;
import com.dengtacj.component.entity.db.GroupEntity;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.sscf.investment.sdk.utils.NumberUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StockUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.widget.CommonAdapter;
import com.sscf.investment.widget.CommonViewHolder;
import com.dengtacj.thoth.Message;
import java.util.ArrayList;
import java.util.List;
import BEC.CommonSearchRsp;
import BEC.PlateInfo;
import BEC.SecInfo;

/**
 * Created by davidwei on 2015-08-12.
 */
public final class SearchResultFragment extends Fragment {
    private static final String TAG = SearchResultFragment.class.getSimpleName();
    private String mInputWord;
    private CommonSearchRsp mSearchRsp;
    private ListView mResultList;
    private SearchResultAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DtLog.d(TAG, "onCreateView");
        final View root = inflater.inflate(R.layout.fragment_search_result, null);
        initViews(root);
        // TODO 写法有点问题，需优化
        updateResult(mInputWord, mSearchRsp);
        return root;
    }

    @Override
    public void onResume() {
        DtLog.d(TAG, "onResume");
        super.onResume();
        // TODO 是否可以优化，在其他地方添加了自选在此刷新
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initViews(final View root) {
        mResultList = (ListView) root.findViewById(R.id.searchResultStockList);
        mResultList.setEmptyView(root.findViewById(R.id.searchResultEmpty));
    }

    public void updateResult(final String inputWord, final CommonSearchRsp searchRsp) {
        mInputWord = inputWord;
        mSearchRsp = searchRsp;
        if (searchRsp != null && mResultList != null) {
            if (searchRsp.sShowOrder == null) {
                return;
            }

            // TODO 每次的搜索结果都回到开头
            final String[] order = searchRsp.sShowOrder.split(",");
            final int length = order.length;
            int type = 0;
            final ArrayList<Message> stockItems = new ArrayList<Message>();
            for (int i = 0; i < length; i++) {
                type = NumberUtil.parseInt(order[i], 0);
                switch (type) {
                    case 1: //1:代表股票
                        stockItems.addAll(searchRsp.stSecResult.vtSecInfoResultItem);
                        break;
                    case 2: // 2:代表行业
                        stockItems.addAll(searchRsp.stPlateResult.vtPlateResultItem);
                        break;
                    case 3: // 主题
                        break;
                    default:
                        break;
                }
            }
            updateStockResult(stockItems);
        }
    }

    private void updateStockResult(ArrayList<Message> stockInfoItems) {
        DtLog.d(TAG, "updateStockResult");
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        SearchResultAdapter adapter = mAdapter;
        if (adapter == null) {
            adapter = new SearchResultAdapter(activity, stockInfoItems, R.layout.fragment_search_result_stock_item);
            mAdapter = adapter;
            mResultList.setAdapter(adapter);
            mResultList.setOnItemClickListener(adapter);
        } else {
            adapter.setData(stockInfoItems);
            adapter.notifyDataSetChanged();
        }
    }

    private static final class SearchResultAdapter extends CommonAdapter<Message> implements AdapterView.OnItemClickListener, View.OnClickListener {
        private final Activity mActivity;
        private final IPortfolioDataManager mPortfolioDataManager;

        public SearchResultAdapter(Activity activity, ArrayList<Message> data, int itemLayoutId) {
            super(activity, data, itemLayoutId);
            mActivity = activity;
            mPortfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                    .getManager(IPortfolioDataManager.class.getName());;
        }

        @Override
        public void convert(CommonViewHolder holder, Message item, int position) {
            String name = "";
            String secCode = "";
            String dtSecCode = "";
            if (item instanceof SecInfo) {
                final SecInfo secInfo = (SecInfo) item;
                name = secInfo.sCHNShortName;
                secCode = StockUtil.getSecCode(secInfo.sDtSecCode);
                dtSecCode = secInfo.sDtSecCode;
            } else if (item instanceof PlateInfo) {
                final PlateInfo plateInfo = (PlateInfo) item;
                name = plateInfo.sPlateName;
                secCode = StockUtil.getSecCode(plateInfo.sDtSecCode);
                dtSecCode = plateInfo.sDtSecCode;
            }

            final TextView tagView = holder.getView(R.id.tag);
            final String tagText = StockUtil.getSearchTagText(dtSecCode);
            if (TextUtils.isEmpty(tagText)) {
                tagView.setVisibility(View.INVISIBLE);
            } else {
                tagView.setVisibility(View.VISIBLE);
                tagView.setText(tagText);
            }

            final TextView nameView = holder.getView(R.id.name);
            nameView.setText(name);
            final TextView codeView = holder.getView(R.id.code);
            codeView.setText(secCode);

            final boolean isPortfolio = mPortfolioDataManager != null && mPortfolioDataManager.isPortfolio(dtSecCode);
            final ImageView button = holder.getView(R.id.button);
            button.setOnClickListener(this);
            button.setImageResource(isPortfolio ? R.drawable.search_remove_icon : R.drawable.search_add_icon);
            button.setTag(holder);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Message item = getItem(position);
            if (item == null) {
                return;
            }

            String name = "";
            String unicode = "";
            if (item instanceof SecInfo) {
                final SecInfo secInfo = (SecInfo) item;
                name = secInfo.sCHNShortName;
                unicode = secInfo.sDtSecCode;
            } else if (item instanceof PlateInfo) {
                final PlateInfo plateInfo = (PlateInfo) item;
                name = plateInfo.sPlateName;
                unicode = plateInfo.sDtSecCode;
            }

            final String finalName = name;
            final String finalUnicode = unicode;

            final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
            if (CommonBeaconJump.showSecurityDetailActivity(mActivity, unicode, name)) {
                dengtaApplication.defaultExecutor.execute(() -> SearchHistoryItem.addItemToDb(finalName, finalUnicode));
                LocalBroadcastManager.getInstance(dengtaApplication).sendBroadcast(new Intent(DengtaConst.ACTION_SEARCH_TO_SECURITY_DETTAIL));
                mActivity.finish();
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
            final Message item = getItem(holder.getPosition());

            if (item == null) {
                return;
            }

            String dtCode = null;
            String secName = null;
            if (item instanceof SecInfo) {
                final SecInfo secInfo = (SecInfo) item;
                dtCode = secInfo.sDtSecCode;
                secName = secInfo.sCHNShortName;
            } else if (item instanceof PlateInfo) {
                final PlateInfo secInfo = (PlateInfo) item;
                dtCode = secInfo.sDtSecCode;
                secName = secInfo.sPlateName;
            } else {
                return;
            }

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

                if (groupSize > 0) { // 分组界面添加
                    PortfolioGroupManagerActivity.show(v.getContext(), dtCode, secName);
                } else { // 直接添加到默认分组
                    mPortfolioDataManager.addStock(dtCode, secName);
                }
            }
        }
    }
}
