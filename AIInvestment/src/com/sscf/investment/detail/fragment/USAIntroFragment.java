package com.sscf.investment.detail.fragment;

import BEC.CompanyDesc;
import BEC.CompanyProfile;
import BEC.CompanyReq;
import BEC.CompanyRsp;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
 * Created by liqf on 2015/10/31.
 */
public class USAIntroFragment extends Fragment implements DataSourceProxy.IRequestCallback, OnReloadDataListener {
    private CompanyDesc mCompanyDesc;
    private TextView mTextCompanyName;
    private TextView mTextListingDate;
    private TextView mTextListingAddr;
    private TextView mTextSecCategory;
    private TextView mTextCountry;
    private TextView mTextBrief;

    private Handler mUiHandler = new Handler();
    private String mDtSecCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_intro_usa, container, false);

        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
        }

        findViews(contextView);

        loadData();

        return contextView;
    }

    private void loadData() {
        CompanyReq companyReq = new CompanyReq();
        companyReq.setSDtSecCode(mDtSecCode);
        companyReq.setStUserInfo(DengtaApplication.getApplication().getAccountManager().getUserInfo());
        DataEngine.getInstance().request(EntityObject.ET_GET_BRIEF_INFO, companyReq, this);
    }

    private void findViews(View contextView) {
        mTextCompanyName = (TextView) contextView.findViewById(R.id.company_name);
        mTextListingDate = (TextView) contextView.findViewById(R.id.company_listing_date);
        mTextListingAddr = (TextView) contextView.findViewById(R.id.listing_addr);
        mTextSecCategory = (TextView) contextView.findViewById(R.id.sec_category);
        mTextCountry = (TextView) contextView.findViewById(R.id.country);
        mTextBrief = (TextView) contextView.findViewById(R.id.company_brief);
    }

    private void setData(CompanyDesc companyDesc) {
        //公司简介
        mCompanyDesc = companyDesc;

        CompanyProfile companyProfile = mCompanyDesc.getStCompanyProfile();

        String name = companyProfile.getSName();
        if (!TextUtils.isEmpty(name)) {
            mTextCompanyName.setText(name);//公司名称
        }

        String listingDate = companyProfile.getSListingDate();//上市日期
        if (!TextUtils.isEmpty(listingDate)) {
            mTextListingDate.setText(listingDate);
        }

        String listedAddr = companyProfile.getSListedAddr(); //交易市场
        if (!TextUtils.isEmpty(listedAddr)) {
            mTextListingAddr.setText(listedAddr);
        }

        String secCategory = companyProfile.getSSecCategory(); //证券类型
        if (!TextUtils.isEmpty(secCategory)) {
            mTextSecCategory.setText(secCategory);
        }

        String country = companyProfile.getSCountry(); //所属国家
        if (!TextUtils.isEmpty(country)) {
            mTextCountry.setText(country);
        }

        String mainBusiness = companyProfile.getSMainBusiness(); //公司简介
        if (!TextUtils.isEmpty(mainBusiness)) {
            mTextBrief.setText(mainBusiness);
        }
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
            case EntityObject.ET_GET_BRIEF_INFO:
                CompanyRsp companyRsp = (CompanyRsp) entity.getEntity();
                final CompanyDesc companyDesc = companyRsp.getStCompanyDesc();
                mUiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isAdded()) {
                            setData(companyDesc);
                        }
                    }
                }, 0);
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
