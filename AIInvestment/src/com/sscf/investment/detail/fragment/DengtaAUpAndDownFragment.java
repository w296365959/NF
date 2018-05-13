package com.sscf.investment.detail.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.detail.view.IndicatorGroupView;
import com.sscf.investment.detail.view.UpAndDownLimitView;
import com.sscf.investment.detail.view.UpAndDownAmplitudeView;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import java.util.ArrayList;
import BEC.MarketStatDesc;
import BEC.MarketStatList;
import BEC.MarketStatReq;
import BEC.MarketStatRsp;

/**
 * Created by liqf on 2016/4/12.
 */
public class DengtaAUpAndDownFragment extends Fragment implements DataSourceProxy.IRequestCallback, OnReloadDataListener {

    private static final String TAG = DengtaAUpAndDownFragment.class.getSimpleName();
    private View mContextView;
    private TextView mAmplitudeDetailText;
    private IndicatorGroupView mAmplitudeIndicators;
    private UpAndDownAmplitudeView mAmplitudeView;
    private TextView mLimitDetailText;
    private IndicatorGroupView mLimitIndicators;
    private UpAndDownLimitView mLimitView;

    private Handler mUiHandler = new Handler();
    private String mDtSecCode;
    private boolean mHasData = false;
    private ArrayList<MarketStatDesc> mMarketStatDescList;

    private Handler mLoadDataHandler;
    private Runnable mLoadDataRunnable;
    private boolean mIsCurrentPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_dengta_a_up_and_down, container, false);
        mContextView = contextView;

        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
        }

        initViews();

        mLoadDataHandler = new Handler();
        mLoadDataRunnable = new Runnable() {
            @Override
            public void run() {
                loadData();
                mLoadDataHandler.postDelayed(mLoadDataRunnable, DengtaSettingPref.getRefreshDelaySenconds());
            }
        };

        return contextView;
    }

    private void initViews() {
        mAmplitudeDetailText = (TextView) mContextView.findViewById(R.id.rise_and_down_amplitude_detail);
        mAmplitudeIndicators = (IndicatorGroupView) mContextView.findViewById(R.id.amplitude_indicators);
        mAmplitudeIndicators.setIndicators(new String[]{"涨幅超5%", "涨幅0-5%", "跌幅0-5%", "跌幅超5%"});
        mAmplitudeIndicators.setColorList(new int[]{R.color.dengta_a_big_rise, R.color.dengta_a_rise, R.color.dengta_a_down, R.color.dengta_a_big_down});

        mAmplitudeView = (UpAndDownAmplitudeView) mContextView.findViewById(R.id.amplitude_view);

        mLimitDetailText = (TextView) mContextView.findViewById(R.id.rise_and_down_limit_detail);
        mLimitIndicators = (IndicatorGroupView) mContextView.findViewById(R.id.limit_indicators);
        mLimitIndicators.setIndicators(new String[]{"涨停板", "跌停板"});
        mLimitIndicators.setColorList(new int[]{R.color.dengta_a_rise_limit, R.color.dengta_a_down_limit});

        mLimitView = (UpAndDownLimitView) mContextView.findViewById(R.id.limit_view);
    }

    @Override
    public void onResume() {
        super.onResume();

        doOnResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        doOnPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            doOnPause();
        } else {
            doOnResume();
        }
    }

    private void doOnPause() {
        mLoadDataHandler.removeCallbacks(mLoadDataRunnable);
    }

    private void doOnResume() {
        doLoadData();
    }

    private void loadData() {
        if (mHasData) {
            return;
        }
        DtLog.d(TAG, "loadData()");

        MarketStatReq marketStatReq = new MarketStatReq();
        marketStatReq.setStUserInfo(DengtaApplication.getApplication().getAccountManager().getUserInfo());
        marketStatReq.setIStartxh(0);
        marketStatReq.setIWantnum(1000);
        DataEngine.getInstance().request(EntityObject.ET_GET_MARKET_STAT, marketStatReq, this);
    }

    private void doLoadData() {
        mLoadDataHandler.removeCallbacks(mLoadDataRunnable);
        mLoadDataHandler.post(mLoadDataRunnable);
    }

    private void setData(ArrayList<MarketStatDesc> marketStatDescList) {
        if (!isAdded()) {
            return;
        }

        mMarketStatDescList = marketStatDescList;

        mAmplitudeView.setData(marketStatDescList);
        mLimitView.setData(marketStatDescList);

        if (marketStatDescList == null || marketStatDescList.size() == 0) {
            return;
        }

        MarketStatDesc marketStatDesc = marketStatDescList.get(marketStatDescList.size() - 1);
        int min = marketStatDesc.getIChangeMin();
        int max = marketStatDesc.getIChangeMax();
        int change5 = marketStatDesc.getIChange5();
        int change10 = marketStatDesc.getIChange10();
        int changeN5 = marketStatDesc.getIChangeN5();
        int changeN10 = marketStatDesc.getIChangeN10();
        int riseCount = change5 + change10;
        int downCount = changeN5 + changeN10;
        String amplitudeDetail = getString(R.string.amplitude_detail, riseCount, downCount, (float) riseCount / (riseCount + downCount) * 100);
        mAmplitudeDetailText.setText(amplitudeDetail);
        String limitDetail = getString(R.string.limit_detail, max, min);
        mLimitDetailText.setText(limitDetail);

        String minStr = getContext().getString(R.string.down_limit_count, min);
        String maxStr = getContext().getString(R.string.rise_limit_count, max);
        String[] strings = {maxStr, minStr};
        mLimitIndicators.setIndicators(strings);
    }

    @Override
    public void callback(boolean success, EntityObject entity) {
        if (!isAdded()) {
            return;
        }

        if (!success || entity.getEntity() == null ) {
            return;
        }

        switch (entity.getEntityType()) {
            case EntityObject.ET_GET_MARKET_STAT:
                MarketStatRsp marketStatRsp = (MarketStatRsp) entity.getEntity();
                if (marketStatRsp != null) {
                    MarketStatList marketStatList = marketStatRsp.getStMarketStatList();
                    if (marketStatList != null) {
                        final ArrayList<MarketStatDesc> marketStatDesc = marketStatList.getVMarketStatDesc();
                        if (marketStatDesc != null && marketStatDesc.size() > 0) {
                            mUiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    setData(marketStatDesc);
                                }
                            });
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onReloadData() {
        loadData();
    }
}
