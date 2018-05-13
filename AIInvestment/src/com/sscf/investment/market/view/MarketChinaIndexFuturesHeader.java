package com.sscf.investment.market.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IDataCacheManager;
import com.dengtacj.component.managers.IQuoteManager;
import com.sscf.investment.R;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.market.IndexFuturesListActivity;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.component.ui.widget.CommonMultiViewTypeRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.widget.RecyclerViewViewPager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import BEC.SecSimpleQuote;

/**
 * Created by davidwei on 2015/9/23.
 * 市场行情沪深界面的股指期货的HeaderView
 */
public final class MarketChinaIndexFuturesHeader extends RecyclerViewViewPager {
    static final int COUNT_PER_PAGE = 3;
    private int mPage;
    private IndexAdapter[] mAdapters;

    public MarketChinaIndexFuturesHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        final int pageCount = 3;
        final IndexAdapter[] adapters = new IndexAdapter[pageCount];
        mAdapters = adapters;

        // 保存左右滑动的list
        final ArrayList<SecListItem> secList = new ArrayList<SecListItem>(8);
        IndexAdapter adapter;
        final String[] dtSecCodes0 = getResources().getStringArray(R.array.market_china_index_dtseccode_0);
        final String[] names0 = getResources().getStringArray(R.array.market_china_index_name_0);
        adapter = new IndexAdapter(getContext(), null);
        adapter.setData(dtSecCodes0, names0, secList);
        adapters[0] = adapter;

        final String[] dtSecCodes1 = getResources().getStringArray(R.array.market_china_index_dtseccode_1);
        final String[] names1 = getResources().getStringArray(R.array.market_china_index_name_1);
        adapter = new IndexAdapter(getContext(), null);
        adapter.setData(dtSecCodes1, names1, secList);
        adapters[1] = adapter;

        final String[] dtSecCodes2 = getResources().getStringArray(R.array.market_china_index_dtseccode_2);
        final String[] names2 = getResources().getStringArray(R.array.market_china_index_name_2);
        adapter = new IndexAdapter(getContext(), null);
        adapter.setData(dtSecCodes2, names2, secList);
        adapters[2] = adapter;

        init(COUNT_PER_PAGE, adapters);
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        mPage = position;
        requestData();
        StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_INDEXED_SCROLL);
    }

    public void requestData() {
        mAdapters[mPage].requestData();
    }
}

final class IndexAdapter extends CommonMultiViewTypeRecyclerViewAdapter<String>
        implements OnGetDataCallback<ArrayList<SecSimpleQuote>>, Handler.Callback {
    private final HashMap<String, SecSimpleQuote> mQuotes;
    private ArrayList<SecListItem> mItemList;
    private final ArrayList<String> mDtSecCodeList;
    private final Handler mHandler;

    IndexAdapter(Context context, List<String> data) {
        super(context, data);
        mQuotes = new HashMap<>(MarketChinaIndexFuturesHeader.COUNT_PER_PAGE);
        mDtSecCodeList = new ArrayList<>(MarketChinaIndexFuturesHeader.COUNT_PER_PAGE);
        setItemClickable(true);
        mHandler = new Handler(this);
    }

    void setData(final String[] dtSecCodes, final String[] names, final ArrayList<SecListItem> secList) {
        final int length = dtSecCodes.length;
        final ArrayList<String> data = new ArrayList<String>(length);
        final IDataCacheManager dataCacheManager = (IDataCacheManager) ComponentManager.getInstance()
                .getManager(IDataCacheManager.class.getName());
        SecSimpleQuote quote;
        String dtSecCode;
        String secName;
        mItemList = secList;
        SecListItem secItem;
        for (int i = 0; i < length; i++) {
            dtSecCode = dtSecCodes[i];
            secName = names[i];
            quote = dataCacheManager != null ? dataCacheManager.getSecSimpleQuote(dtSecCodes[i]) : null;
            if (quote == null) {
                quote = new SecSimpleQuote();
                quote.sDtSecCode = dtSecCode;
                quote.sSecName = secName;
            }
            mQuotes.put(dtSecCode, quote);
            secItem = new SecListItem();
            secItem.setDtSecCode(dtSecCode);
            secItem.setName(secName);
            mItemList.add(secItem);
            mDtSecCodeList.add(dtSecCode);
            data.add(dtSecCode);
        }

        if (length < 3) { // 添加更多指数
            data.add(null);
        }
        setData(data);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        switch (viewType) {
            case 1:
                return R.layout.market_index_item;
            default: // 更多指数
                return R.layout.market_index_more_item;
        }
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, String item, int position) {
        switch (getItemViewType(position)) {
            case 1:
                final MarketIndexInfoView itemView = holder.getView(R.id.item);
                itemView.updateInfos(mQuotes.get(item));
                break;
            default: // 更多指数
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) == null ? 0 : 1;
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        final String dtSecCode = getItem(position);
        if (dtSecCode != null) {
            CommonBeaconJump.showSecurityDetailActivity(mContext, dtSecCode, mQuotes.get(dtSecCode).sSecName, mItemList);
            if (DengtaConst.DENGTA_DT_CODE.equals(dtSecCode)) {
                StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_DENGTA_INDEX);
            } else {
                StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_INDEX_FUTURES);
            }
        } else { // 更多指数
            CommonBeaconJump.showActivity(mContext, IndexFuturesListActivity.class);
            StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_MORE_INDEXES);
        }
    }

    void requestData() {
        final IQuoteManager quoteManager = (IQuoteManager) ComponentManager.getInstance()
                .getManager(IQuoteManager.class.getName());
        if (quoteManager != null) {
            quoteManager.requestSimpleQuote(mDtSecCodeList, this);
        }
    }

    @Override
    public void onGetData(ArrayList<SecSimpleQuote> data) {
        if (data != null) {
            mHandler.obtainMessage(0, data).sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        final ArrayList<SecSimpleQuote> quotes = (ArrayList<SecSimpleQuote>) msg.obj;
        String dtSecCode;
        for (SecSimpleQuote quote : quotes) {
            dtSecCode = quote.sDtSecCode;
            mQuotes.put(dtSecCode, quote);
        }
        notifyDataSetChanged();
        return true;
    }
}
