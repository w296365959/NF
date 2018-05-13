package com.sscf.investment.market.view;

import BEC.*;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.dengtacj.component.router.CommonBeaconJump;
import java.util.ArrayList;

/**
 * Created by davidwei on 2015/9/25.
 * 资金流界面里指数资金流入流出的HeaderView
 */
public final class MarketCapitalFlowIndexHeader extends LinearLayout implements OnGetDataCallback<ArrayList<CapitalMainFlowDesc>>,
        View.OnClickListener, Handler.Callback {

    private static final int INDEX_COUNT = 4;
    private ArrayList<MarketCapitalFlowIndexInfoView> mIndexInfoViews;
    private ArrayList<String> mIndexUnicodes;

    private Handler mHandler;

    public MarketCapitalFlowIndexHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHandler = new Handler(this);

        final String[] unicodes = getResources().getStringArray(R.array.market_capital_flow_index_unicode);
        mIndexUnicodes = new ArrayList<String>(unicodes.length);
        for (String unicode : unicodes) {
            mIndexUnicodes.add(unicode);
        }

        mIndexInfoViews = new ArrayList<MarketCapitalFlowIndexInfoView>(INDEX_COUNT);

        final Resources resources = getResources();
        MarketCapitalFlowIndexInfoView infoView = null;
        for (int i = 0; i < INDEX_COUNT; i++) {
            int identifier = resources.getIdentifier("capital_flow_index_" + i, "id", DengtaApplication.getApplication().getPackageName());
            if (identifier != 0) {
                infoView = (MarketCapitalFlowIndexInfoView) findViewById(identifier);
                infoView.setOnClickListener(this);
                mIndexInfoViews.add(infoView);
            }
        }
    }

    private void updateIndexInfos(ArrayList<CapitalMainFlowDesc> indexes) {
        MarketCapitalFlowIndexInfoView infoView = null;
        CapitalMainFlowDesc indexInfo = null;
        final int size = Math.min(INDEX_COUNT, indexes.size());
        for (int i = 0; i < size; i++) {
            infoView = mIndexInfoViews.get(i);
            indexInfo = indexes.get(i);
            infoView.updateInfos(indexes.get(i));
            infoView.setTag(indexInfo);
        }
    }

    public void requestData(IMarketManager marketManager) {
        marketManager.requestMultiStockCapitalFlow(mIndexUnicodes, this);
    }

    /**
     * 获得指数数据的callback
     */
    @Override
    public void onGetData(ArrayList<CapitalMainFlowDesc> data) {
        if (data != null) {
            mHandler.obtainMessage(0, getSortList(mIndexUnicodes, data)).sendToTarget();
        }
    }

    /**
     * 重新按照写死的顺序排列
     * @param unicodes
     * @param secList
     */
    public static ArrayList<CapitalMainFlowDesc> getSortList(ArrayList<String> unicodes, ArrayList<CapitalMainFlowDesc> secList) {
        ArrayList<CapitalMainFlowDesc> indexInfos = new ArrayList<CapitalMainFlowDesc>(unicodes.size());
        for (String unicode : unicodes) {
            inner : for (CapitalMainFlowDesc indexInfo : secList) {
                if (unicode.equals(indexInfo.sDtSecCode)) {
                    indexInfos.add(indexInfo);
                    break inner;
                }
            }
        }
        return indexInfos;
    }

    @Override
    public boolean handleMessage(Message msg) {
        updateIndexInfos((ArrayList<CapitalMainFlowDesc>) msg.obj);
        return false;
    }

    @Override
    public void onClick(View v) {
        final CapitalMainFlowDesc indexInfo = (CapitalMainFlowDesc) v.getTag();
        if (indexInfo == null) {
            return;
        }
        CommonBeaconJump.showSecurityDetailActivity(getContext(), indexInfo.sDtSecCode, indexInfo.sSecName);
    }
}
