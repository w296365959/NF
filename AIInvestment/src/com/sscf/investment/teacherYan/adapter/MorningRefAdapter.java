package com.sscf.investment.teacherYan.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.teacherYan.manager.CountNumUtil;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.SecListItemUtils;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import BEC.E_SEC_STATUS;
import BEC.InformationSpiderNews;
import BEC.SecBaseInfo;
import BEC.SecSimpleQuote;
import BEC.TopicListItem;
import butterknife.BindView;

/**
 * Created by LEN on 2018/4/23.
 */

public class MorningRefAdapter extends CommonRecyclerViewAdapter {

    private final Map<String, SecSimpleQuote> mStockInfoMap;

    public MorningRefAdapter(Context context) {
        super(context);
        mStockInfoMap = new HashMap<>();
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        return new MorningRefHolder(mInflater.inflate(R.layout.item_morning_ref, parent, false));
    }

    public ArrayList<String> getVisibleDtSecCodes(final int positionStart, final int positionEnd) {
        if (positionStart < 0 || positionEnd < 0) {
            return null;
        }

        final HashSet<String> dtSecCodeSet = new HashSet<>();
        for (int i = positionStart; i <= positionEnd; i++) {
            final InformationSpiderNews item = (InformationSpiderNews) getItemData(i);
            if (item != null) {
                final String[] stockList = item.getSShares().split("\\|");
                int stockSize = stockList == null ? 0 : stockList.length;
                if (stockSize > 0) {
                    stockSize = Math.min(2, stockSize); // 只取前4个股票
                    for (int j = 0; j < stockSize; j++) {
                        if (!TextUtils.isEmpty(stockList[j])) {
                            dtSecCodeSet.add(stockList[j]);
                        }
                    }
                }
            }
        }

        return new ArrayList<>(dtSecCodeSet);
    }

    public void updateQuotesData(ArrayList<SecSimpleQuote> quotes) {
        String dtSecCode;
        for (SecSimpleQuote quote : quotes) {
            dtSecCode = quote.sDtSecCode;
            mStockInfoMap.put(dtSecCode, quote);
        }
        notifyDataSetChanged();
    }

    final class MorningRefHolder extends CommonRecyclerViewAdapter.CommonViewHolder implements View.OnClickListener {
        @BindView(R.id.tvMoringRefAbstract)
        TextView mTitle;
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.time)
        TextView mTime;

        @BindView(R.id.name0)
        TextView mName0;
        @BindView(R.id.value0)
        TextView mValue0;
        @BindView(R.id.name1)
        TextView mName1;
        @BindView(R.id.value1)
        TextView mValue1;

        MorningRefHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            final InformationSpiderNews item = (InformationSpiderNews) itemData;
            if (item != null) {
                mTitle.setText(item.getSAbstracts());
                mName.setText(item.getSSource());
                mTime.setText(TimeUtils.transForDate(item.getSInformationTime()));
                if (!TextUtils.isEmpty(item.getSShares())) {
                    String[] symbols = item.getSShares().split("\\|");
                    updateQuotes(symbols);
                }else{
                    mName0.setVisibility(View.GONE);
                    mValue0.setVisibility(View.GONE);
                    mName1.setVisibility(View.GONE);
                    mValue1.setVisibility(View.GONE);
                }

            }
        }

        private void updateQuotes(String[] symbols) {
            final SecSimpleQuote[] quotes = getRelatedQuotes(symbols);

            mName0.setOnClickListener(this);
            mValue0.setOnClickListener(this);
            mName1.setOnClickListener(this);
            mValue1.setOnClickListener(this);

            if (quotes.length > 0)
                updateStockQuote(mName0, mValue0, quotes[0]);
            if (quotes.length > 1)
                updateStockQuote(mName1, mValue1, quotes[1]);
        }

        SecSimpleQuote[] getRelatedQuotes(final String[] stockList) {
            SecSimpleQuote[] quotes = new SecSimpleQuote[stockList.length];
            final int QUOTES_LENGTH = stockList.length;
            mTag = quotes;

            for (int i = 0; i < QUOTES_LENGTH; i++) {
                quotes[i] = getQuote(i, stockList);
            }

            return quotes;
        }

        private void updateStockQuote(final TextView nameView, final TextView valueView, final SecSimpleQuote quote) {
            if (quote == null) {
                nameView.setVisibility(View.GONE);
                valueView.setVisibility(View.GONE);
                return;
            }

            nameView.setVisibility(View.VISIBLE);
            valueView.setVisibility(View.VISIBLE);
            int nameBgRes = R.drawable.hot_spot_stock_name_base_bg;
            int valueBgRes = R.drawable.hot_spot_stock_value_base_bg;
            if (quote.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
                valueView.setText(R.string.suspended);
            } else {
                final float now = quote.fNow;
                final float close = quote.fClose;
                if (now > 0 && close > 0) {
                    final float delta = now - close;
                    valueView.setText(StringUtil.getUpdownString(quote));
                    if (delta > 0) {
                        nameBgRes = R.drawable.hot_spot_stock_name_red_bg;
                        valueBgRes = R.drawable.hot_spot_stock_value_red_bg;
                    } else if (delta < 0) {
                        nameBgRes = R.drawable.hot_spot_stock_name_green_bg;
                        valueBgRes = R.drawable.hot_spot_stock_value_green_bg;
                    }
                } else {
                    valueView.setText(R.string.value_null);
                }
            }
            nameView.setText(quote.sSecName);
            nameView.setBackgroundResource(nameBgRes);
            valueView.setBackgroundResource(valueBgRes);
            nameView.setTag(quote);
            valueView.setTag(quote);
        }

        SecSimpleQuote getQuote(final int index, final String[] stockList) {
            SecSimpleQuote quote = null;
            String dtSecCode;
            if (index < stockList.length) {
                final String stock = stockList[index];
                if (!TextUtils.isEmpty(stock)) {
                    dtSecCode = stock;
                    if (!TextUtils.isEmpty(dtSecCode)) {
                        quote = mStockInfoMap.get(dtSecCode);
                        if (quote == null) {
                            quote = DengtaApplication.getApplication().getDataCacheManager().getSecSimpleQuote(dtSecCode);
                            if (quote == null) {
                                quote = new SecSimpleQuote();
                                quote.sDtSecCode = dtSecCode;
                            }
                        }
                    }
                }
            }
            return quote;
        }

        @Override
        public void onItemClicked() {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            StatisticsUtil.reportAction(StatisticsConst.A_NEWS_BREAKER_PLATE_CLICK);
            final InformationSpiderNews item = (InformationSpiderNews) mItemData;
            if (item != null) {
                CountNumUtil.readMorningRef(item.getIID());
                WebBeaconJump.showCommonWebActivity(mContext,
                        WebUrlManager.getInstance().getMorningRefUrl(String.valueOf(item.getIID())));
            }
        }

        @Override
        public void onClick(View v) {
            final SecSimpleQuote quote = (SecSimpleQuote) v.getTag();
            if (quote != null) {
                final SecSimpleQuote[] quotes = (SecSimpleQuote[]) mTag;
                CommonBeaconJump.showSecurityDetailActivity(mContext, quote.sDtSecCode, quote.sSecName,
                        SecListItemUtils.getSecListFromSecSimpleQuoteList(quotes));
                StatisticsUtil.reportAction(StatisticsConst.A_NEWS_BREAKER_INDIVIDUAL_CLICK);
            }
        }
    }
}
