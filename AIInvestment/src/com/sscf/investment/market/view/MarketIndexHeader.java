package com.sscf.investment.market.view;

import BEC.SecSimpleQuote;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IDataCacheManager;
import com.dengtacj.component.managers.IQuoteManager;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.utils.SecListItemUtils;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import java.util.ArrayList;

/**
 * Created by davidwei on 2015/9/23.
 * 市场行情港股，美股界面的指数的HeaderView
 */
public final class MarketIndexHeader extends LinearLayout implements OnGetDataCallback<ArrayList<SecSimpleQuote>>,
        View.OnClickListener, Handler.Callback {
    private ArrayList<String> mIndexDtSecCodes;

    private static final int MSG_UPDATE_INDEX_INFO = 1;

    private static final int INDEX_COUNT = 3;

    private final Handler mHandler;

    private ArrayList<SecListItem> mSecList;

    public static final int TYPE_HONGKONG = 1;
    public static final int TYPE_AMERICAN = 2;

    /**
     * 用于统计
     */
    private int mType;

    public MarketIndexHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new Handler(this);
    }

    public void setIndexDtSecCodes(final int arrayId, final int type) {
        final String[] dtSecCodes = getResources().getStringArray(arrayId);
        final int length = dtSecCodes.length;
        mIndexDtSecCodes = new ArrayList<String>(length);
        arrayToList(dtSecCodes, mIndexDtSecCodes);
        final IDataCacheManager dataCacheManager = (IDataCacheManager) ComponentManager.getInstance()
                .getManager(IDataCacheManager.class.getName());
        if (dataCacheManager != null) {
            final ArrayList<SecSimpleQuote> quotes = new ArrayList<>(length);
            for (String dtSecCode : dtSecCodes) {
                quotes.add(dataCacheManager.getSecSimpleQuote(dtSecCode));
            }
            updateIndexInfos(quotes);
        }
        mType = type;
    }

    private void arrayToList(String[] array, ArrayList<String> list) {
        for (String dtSecCode : array) {
            list.add(dtSecCode);
        }
    }

    public void requestData() {
        final IQuoteManager quoteManager = (IQuoteManager) ComponentManager.getInstance()
                .getManager(IQuoteManager.class.getName());
        if (quoteManager != null) {
            quoteManager.requestSimpleQuote(mIndexDtSecCodes, this);
        }
    }

    @Override
    public void onGetData(ArrayList<SecSimpleQuote> data) {
        if (data != null) {
            if (mSecList == null) {
                mSecList = SecListItemUtils.getSecListFromSecSimpleQuoteList(data);
            }
            mHandler.obtainMessage(MSG_UPDATE_INDEX_INFO, data).sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_INDEX_INFO:
                updateIndexInfos((ArrayList<SecSimpleQuote>) msg.obj);
                break;
            default:
                break;
        }
        return false;
    }

    private void updateIndexInfos(final ArrayList<SecSimpleQuote> quotes) {
        final int size = Math.min(quotes.size(), INDEX_COUNT);

        MarketIndexInfoView infoView;
        SecSimpleQuote secQuote;
        for (int i = 0; i < size; i++) {
            infoView = (MarketIndexInfoView) getChildAt(i);
            secQuote = quotes.get(i);
            if (secQuote != null) {
                infoView.updateInfos(secQuote);
                infoView.setTag(secQuote);
                infoView.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        final SecSimpleQuote secQuote = (SecSimpleQuote) v.getTag();
        if (secQuote == null) {
            return;
        }

        CommonBeaconJump.showSecurityDetailActivity(getContext(), secQuote.getSDtSecCode(), secQuote.getSSecName(), mSecList);
        switch (mType) {
            case TYPE_HONGKONG:
                StatisticsUtil.reportAction(StatisticsConst.MARKET_HONGKONG_CLICK_INDEX);
                break;
            case TYPE_AMERICAN:
                StatisticsUtil.reportAction(StatisticsConst.MARKET_AMERICAN_CLICK_INDEX);
                break;
            default:
                break;
        }
    }
}
