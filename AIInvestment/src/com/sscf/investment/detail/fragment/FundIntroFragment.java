package com.sscf.investment.detail.fragment;

import BEC.FundBaseInfoReq;
import BEC.FundBaseInfoRsp;
import BEC.FundInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.utils.DengtaConst;

/**
 * Created by liqf on 2015/9/14.
 */
public class FundIntroFragment extends Fragment implements DataSourceProxy.IRequestCallback, OnReloadDataListener {
    private View mContentView;
    private Handler mUiHandler = new Handler();
    private String mDtSecCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_fund_intro, container, false);
        mContentView = contextView;

        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
        }

        return contextView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFundData();
            }
        }, 300);
    }

    private void loadFundData() {
        FundBaseInfoReq fundReq = new FundBaseInfoReq();
        fundReq.setStUserInfo(DengtaApplication.getApplication().getAccountManager().getUserInfo());
        String unicode = mDtSecCode;
        fundReq.setSDtSecCode(unicode);
        DataEngine.getInstance().request(EntityObject.ET_GET_FUND_INFO, fundReq, this);
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
            case EntityObject.ET_GET_FUND_INFO:
                final FundBaseInfoRsp fundRsp = (FundBaseInfoRsp) entity.getEntity();
                if (fundRsp != null) {
                    final FundInfo fundInfo = fundRsp.getStFundInfo();
                    if (fundInfo != null) {
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                setFundData(fundInfo);
                            }
                        });
                    }
                }
                break;
            default:
                break;
        }
    }

    private void setFundData(FundInfo fundInfo) {
        setTextById(R.id.fund_name, fundInfo.getSCHNFullName());
        setTextById(R.id.fund_type, fundInfo.getSFundType());
        setTextById(R.id.fund_invest_type, fundInfo.getSInvestType());
        setTextById(R.id.fund_invest_style, fundInfo.getSInvestStyle());
        setTextById(R.id.fund_company, fundInfo.getSFundOrgName());

        setTextById(R.id.fund_invest_target, fundInfo.getSInvestTarget());
        setTextById(R.id.fund_invest_orient, fundInfo.getSInvestOrient());
        setTextById(R.id.fund_performance_bench, fundInfo.getSPerformanceBench());
    }

    private void setTextById(int textId, String text) {
        TextView textView = (TextView) mContentView.findViewById(textId);
        textView.setText(text);
    }

    @Override
    public void onReloadData() {
        loadFundData();
    }
}
