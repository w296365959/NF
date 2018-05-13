package com.sscf.investment.detail.fragment;

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
import com.sscf.investment.sdk.utils.DtLog;
import java.util.ArrayList;
import BEC.CapitalStructure;
import BEC.CompanyDesc;
import BEC.CompanyProfile;
import BEC.CompanyReq;
import BEC.CompanyRsp;
import BEC.DividendPayingPlacing;
import BEC.SecSimpleQuote;

/**
 * Created by liqf on 2015/8/10.
 */
public class NewThirdBoardIntroFragment extends Fragment implements DataSourceProxy.IRequestCallback, OnReloadDataListener {
    private static final String TAG = NewThirdBoardIntroFragment.class.getSimpleName();
    private Handler mUiHandler = new Handler();
    private CompanyDesc mCompanyDesc;
    private View mContentView;
    private TextView mTextCompanyName;
    private TextView mTextListingDate;
    private TextView mTextMainBusiness;
    private LinearLayout mBonusLayout;

    private String mDtSecCode;

    private boolean mHasData = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_intro_new_third_board, container, false);
        mContentView = contextView;

        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
        }

        initResources();

        findViews(contextView);

        return contextView;
    }

    private void loadData() {
        if (mHasData) {
            return;
        }

        CompanyReq companyReq = new CompanyReq();
        companyReq.setSDtSecCode(mDtSecCode);
        companyReq.setStUserInfo(DengtaApplication.getApplication().getAccountManager().getUserInfo());
        DataEngine.getInstance().request(EntityObject.ET_GET_BRIEF_INFO, companyReq, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        DtLog.d(TAG, "onResume");

        loadData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        DtLog.d(TAG, "onHiddenChanged: hidden = " + hidden);

        if (hidden) {
        } else {
            loadData();
        }
    }

    private void initResources() {
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
                            if (companyDesc != null) {
                                mHasData = true;
                                mCompanyDesc = companyDesc;
                                setData(companyDesc);
                            }
                        }
                    }
                }, 0);
                break;
            default:
                break;
        }
    }

    private SecSimpleQuote getQuote(String dtSecCode, ArrayList<SecSimpleQuote> secQuotes) {
        for (SecSimpleQuote secQuote : secQuotes) {
            String secCode = secQuote.getSDtSecCode();
            if (TextUtils.equals(dtSecCode, secCode)) {
                return secQuote;
            }
        }
        return null;
    }

    private void findViews(View contextView) {
        mTextCompanyName = (TextView) contextView.findViewById(R.id.company_name);
        mTextListingDate = (TextView) contextView.findViewById(R.id.company_listing_date);
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

        String mainBusiness = companyProfile.getSMainBusiness();//主营业务
        if (!TextUtils.isEmpty(mainBusiness)) {
            mTextMainBusiness.setText(mainBusiness);
        }

        //分红送配
        ArrayList<DividendPayingPlacing> bonusList = mCompanyDesc.getVDividendPayingPlacing();
        mBonusLayout.removeAllViews();
        for (DividendPayingPlacing bonus : bonusList) {
            LinearLayout bonusItemView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.intro_bonus_item, null);
            ((TextView) bonusItemView.findViewById(R.id.bonus_year)).setText(bonus.getSYear());//年份
            ((TextView) bonusItemView.findViewById(R.id.bonus_content)).setText(bonus.getSDetail());//方案
            ((TextView) bonusItemView.findViewById(R.id.bonus_date)).setText(bonus.getSDate());//日期
            mBonusLayout.addView(bonusItemView);
        }

        //股本结构
        CapitalStructure capitalStructure = mCompanyDesc.getStCapitalStructure();
        String equity = capitalStructure.getSEquity();
        setTextById(R.id.struct_total_share, equity);//总股本
        String circulationStock = capitalStructure.getSCirculationStock();
        setTextById(R.id.struct_circulating, circulationStock);//流通A股
        setTextById(R.id.struct_circulating_stock, capitalStructure.getSFloatShare());//流通股
    }

    private void setTextById(int textId, String text) {
        TextView textView = (TextView) mContentView.findViewById(textId);
        textView.setText(text);
    }

    @Override
    public void onReloadData() {
        loadData();
    }
}
