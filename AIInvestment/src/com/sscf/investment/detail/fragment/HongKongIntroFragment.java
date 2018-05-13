package com.sscf.investment.detail.fragment;

import BEC.*;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.utils.DengtaConst;

import java.util.ArrayList;

/**
 * Created by liqf on 2015/10/31.
 */
public class HongKongIntroFragment extends Fragment implements DataSourceProxy.IRequestCallback, OnReloadDataListener {
    private CompanyDesc mCompanyDesc;
    private TextView mTextCompanyName;
    private TextView mTextListingDate;
    private TextView mTextIssuingPrice;
    private TextView mTextIssuingAmount;
    private TextView mTextIndustry;
    private TextView mTextLotSize;
    private TextView mTextMainBusiness;
    private LinearLayout mBonusLayout;

    private Handler mUiHandler = new Handler();
    private String mDtSecCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_intro_hongkong, container, false);

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
        mTextIssuingPrice = (TextView) contextView.findViewById(R.id.issuing_price);
        mTextIssuingAmount = (TextView) contextView.findViewById(R.id.issuing_amount);
        mTextIndustry = (TextView) contextView.findViewById(R.id.industry);
        mTextLotSize = (TextView) contextView.findViewById(R.id.lot_size);
        mTextMainBusiness = (TextView) contextView.findViewById(R.id.main_business);

        mBonusLayout = (LinearLayout) contextView.findViewById(R.id.intro_bonus_layout);
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

        String issuePrice = companyProfile.getSIssuePrice();//发行价格
        if (!TextUtils.isEmpty(issuePrice)) {
            mTextIssuingPrice.setText(issuePrice);
        }

        String issuanceNumber = companyProfile.getSIssuanceNumber();//发行数量
        if (!TextUtils.isEmpty(issuanceNumber)) {
            mTextIssuingAmount.setText(issuanceNumber);
        }

        String lotSize = companyProfile.getSTradingUnit(); //每手股数
        if (!TextUtils.isEmpty(lotSize)) {
            mTextLotSize.setText(lotSize);
        }

        String industry = companyProfile.getSIndustry();//所属行业
        if (!TextUtils.isEmpty(industry)) {
            mTextIndustry.setText(industry);
        }

        String mainBusiness = companyProfile.getSMainBusiness();//主营业务
        if (!TextUtils.isEmpty(mainBusiness)) {
            mTextMainBusiness.setText(mainBusiness);
        }

        //分红送配
        ArrayList<DividendPayingPlacing> bonusList = mCompanyDesc.getVDividendPayingPlacing();
        for (DividendPayingPlacing bonus : bonusList) {
            LinearLayout bonusItemView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.intro_bonus_item, null);
            ((TextView) bonusItemView.findViewById(R.id.bonus_year)).setText(bonus.getSYear());//年份
            ((TextView) bonusItemView.findViewById(R.id.bonus_content)).setText(bonus.getSDetail());//方案
            ((TextView) bonusItemView.findViewById(R.id.bonus_date)).setText(bonus.getSDate());//日期
            mBonusLayout.addView(bonusItemView);
        }

    }

    @Override
    public void callback(boolean success, EntityObject entity) {
        if (!success || !isAdded()) {
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
