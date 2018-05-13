package com.sscf.investment.detail.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TwoTabSelectorView;
import com.sscf.investment.detail.entity.PillarChangeEntity;
import com.sscf.investment.detail.manager.SecurityDetailRequestManager;
import com.sscf.investment.detail.view.ExecutiveProfileListView;
import com.sscf.investment.detail.view.IndustryBenchmarkView;
import com.sscf.investment.detail.view.PillarChangeView;
import com.sscf.investment.detail.view.ShareholderTopItemView;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.detail.view.CapitalStructureChangeLineView;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.DoughnutView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import BEC.BEACON_STAT_TYPE;
import BEC.CapitalStructure;
import BEC.CapitalStructureDetail;
import BEC.CompanyDesc;
import BEC.CompanyIndustrialChainRsp;
import BEC.CompanyProfile;
import BEC.CompanyRsp;
import BEC.DividendPayingPlacing;
import BEC.Fundsholder;
import BEC.MainHolder;
import BEC.MainHolderDetail;
import BEC.PrimeOperatingRevenue;
import BEC.Product;
import BEC.SeniorExecutive;
import BEC.ShareholderChange;
import BEC.TopShareholder;

/**
 * Created by liqf on 2015/8/10
 * 简况
 */
public final class IntroFragment extends BaseFragment implements DataSourceProxy.IRequestCallback, View.OnClickListener {
    private static final String TAG = IntroFragment.class.getSimpleName();
    private static final int MAX_REVENUE_COUNT = 6;

    private CompanyDesc mCompanyDesc;
    private View mContentView;
    private TextView mTextCompanyName;
    private TextView mActualControllers;
    private TextView mAddress;
    private TextView mTextListingDate;
    private TextView mTextIssuingPrice;
    private TextView mTextIssuingAmount;
    private TextView mTextRegion;
    private TextView mTextIndustry;
    private TextView mTextMainBusiness;
    private LinearLayout mBonusPanel;
    private LinearLayout mBonusLayout;
    private LinearLayout mBonusEmpty;
    private RelativeLayout mRevenuePanel;
    private LinearLayout mRevenueEmpty;
    private LinearLayout mRevenueLayout;
    private DoughnutView mDoughnutView;
    private LinearLayout mExecutiveProfilePanel;
    private LinearLayout mExecutiveProfileEmpty;
    private ExecutiveProfileListView mExecutiveProfileList;
    private IndustryBenchmarkView mIndustryBenchmarkView;
    private TextView mCompanyCountView;
    private TextView mAccumulatedHoldingsView;
    private TextView mValueChangeView;
    private TextView mRatioTotalShareCapitalView;
    private LinearLayout mCompanyStructureEmpty;
    private PillarChangeView mPillarChangeView;

    private LinearLayout mBriefLayout;

    private LinearLayout mShareholderLayout;
    private LinearLayout mShareholderTopPanel;
    private LinearLayout mShareholderTopLayout;
    private LinearLayout mShareholderTopEmpty;
    private View mIndustryChainLayout;
    private TextView mUpstreamProductTextView;
    private TextView mDownstreamProductTextView;
    private LinearLayout mPartnerTopPanel;
    private LinearLayout mPartnerTopLayout;
    private LinearLayout mPartnerTopEmpty;
    private LinearLayout mFundHolderPanel;
    private LinearLayout mFundHolderLayout;
    private LinearLayout mFundHolderEmpty;

    private CapitalStructureChangeLineView mCapitalStructureLineView;
    private View mEmptyView;

    private int mColorRed;
    private int mColorGreen;
    private String mDtSecCode;
    private String mSecName;

    private boolean mHasData = false;

    private CompanyIndustrialChainRsp mIndustrialChainRsp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_intro, container, false);
        mContentView = contextView;

        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
            mSecName = args.getString(DengtaConst.KEY_SEC_NAME);
        }

        initResources();

        mShareholderLayout = (LinearLayout) contextView.findViewById(R.id.shareholder_layout);
        mBriefLayout = (LinearLayout) contextView.findViewById(R.id.brief_layout);

        TwoTabSelectorView mTabSelector = (TwoTabSelectorView) contextView.findViewById(R.id.tab_selector);
        mTabSelector.setTabTitles(R.string.intro_brief, R.string.intro_shareholder);
        mTabSelector.setOnTabSelectedListener(new TwoTabSelectorView.OnTabSelectedListener() {
            @Override
            public void onFirstTabSelected() {
                mBriefLayout.setVisibility(View.VISIBLE);
                mShareholderLayout.setVisibility(View.GONE);
            }

            @Override
            public void onSecondTabSelected() {
                mBriefLayout.setVisibility(View.GONE);
                mShareholderLayout.setVisibility(View.VISIBLE);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TAB_INTRO_SHAREHOLDER_CLICKED);
            }

            @Override
            public void onThirdTabSelected() {
            }
        });

        findViews(contextView);

        return contextView;
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_SEC_NEWS);
        helper.setKey(StatConsts.STOCK_INFO_INTRO);
        return helper;
    }

    private void loadData() {
        if (!mHasData) {
            SecurityDetailRequestManager.requestBriefInfo(mDtSecCode, this);
        }
        if (mIndustrialChainRsp == null) {
            SecurityDetailRequestManager.requestIndustrialChain(mDtSecCode, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DtLog.d(TAG, "onResume");

        loadData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        DtLog.d(TAG, "onHiddenChanged: hidden = " + hidden);
        setUserVisibleHint(!hidden);
        if (!hidden) {
            loadData();
        }
    }

    private void initResources() {
        Resources resources = getResources();
        mColorRed = resources.getColor(R.color.stock_red_color);
        mColorGreen = resources.getColor(R.color.stock_green_color);
    }

    @Override
    public void callback(boolean success, EntityObject entity) {
        if (!isAdded()) {
            return;
        }

        switch (entity.getEntityType()) {
            case EntityObject.ET_GET_BRIEF_INFO:
                handleCompanyRsp(success, entity);
                break;
            case EntityObject.ET_GET_COMPANY_INDUSTRIAL_CHAIN:
                final CompanyIndustrialChainRsp rsp = EntityUtil.entityToCompanyIndustrialChainRsp(success, entity);
                if (rsp != null) {
                    mIndustrialChainRsp = rsp;
                    ThreadUtils.runOnUiThread(() -> {
                        updateIndustralChainsView(rsp.vUpstreamProducts, rsp.vDownstreamProducts);
                    });
                }
                break;
            default:
                break;
        }
    }

    private void handleCompanyRsp(boolean success, EntityObject entity) {
        if(success && entity.getEntity() != null) {
            CompanyRsp companyRsp = (CompanyRsp) entity.getEntity();
            final CompanyDesc companyDesc = companyRsp.getStCompanyDesc();
            ThreadUtils.runOnUiThread(()-> {
                if (isAdded()) {
                    if (companyDesc != null) {
                        mHasData = true;
                        mCompanyDesc = companyDesc;
                        setData(companyDesc);
                    }
                }
            });
        } else {
            ThreadUtils.runOnUiThread(()-> {
                if (isAdded()) {
                    setNotData();
                }
            });
        }
    }

    private void updateIndustralChainsView(final ArrayList<Product> upstreamProducts, final ArrayList<Product> downstreamProducts) {
        final int upSize = upstreamProducts == null ? 0 : upstreamProducts.size();
        if (upSize > 0) {
            final StringBuilder text = new StringBuilder();
            for (Product product : upstreamProducts) {
                text.append(product.sName).append('，');
            }
            text.deleteCharAt(text.length() - 1);
            mUpstreamProductTextView.setText(text);
            mUpstreamProductTextView.setGravity(Gravity.LEFT | Gravity.TOP);
        }
        final int downSize = downstreamProducts == null ? 0 : downstreamProducts.size();
        if (downSize > 0) {
            final StringBuilder text = new StringBuilder();
            for (Product product : downstreamProducts) {
                text.append(product.sName).append('，');
            }
            text.deleteCharAt(text.length() - 1);
            mDownstreamProductTextView.setText(text);
            mDownstreamProductTextView.setGravity(Gravity.LEFT | Gravity.TOP);
        }
        if (upSize > 0 || downSize > 0) {
            mIndustryChainLayout.setVisibility(View.VISIBLE);
        }
    }

    private void findViews(View contextView) {
        mTextCompanyName = (TextView) contextView.findViewById(R.id.company_name);
        mActualControllers = (TextView) contextView.findViewById(R.id.actual_controllers);
        mAddress = (TextView) contextView.findViewById(R.id.address);
        mTextListingDate = (TextView) contextView.findViewById(R.id.company_listing_date);
        mTextIssuingPrice = (TextView) contextView.findViewById(R.id.issuing_price);
        mTextIssuingAmount = (TextView) contextView.findViewById(R.id.issuing_amount);
        mTextRegion = (TextView) contextView.findViewById(R.id.region);
        mTextIndustry = (TextView) contextView.findViewById(R.id.industry);
        mTextMainBusiness = (TextView) contextView.findViewById(R.id.main_business);

        mBonusPanel = (LinearLayout) contextView.findViewById(R.id.intro_bonus_panel);
        mBonusLayout = (LinearLayout) contextView.findViewById(R.id.intro_bonus_layout);
        mBonusEmpty = (LinearLayout) contextView.findViewById(R.id.intro_bonus_empty);
        mRevenuePanel = (RelativeLayout) contextView.findViewById(R.id.intro_revenue_panel);
        mRevenueEmpty = (LinearLayout) contextView.findViewById(R.id.intro_revenue_empty);
        mRevenueLayout = (LinearLayout) contextView.findViewById(R.id.intro_revenue_layout);
        mDoughnutView = (DoughnutView) contextView.findViewById(R.id.doughnut);

        mExecutiveProfilePanel = (LinearLayout) contextView.findViewById(R.id.executive_profile_panel);
        mExecutiveProfileEmpty = (LinearLayout) contextView.findViewById(R.id.executive_profile_empty);
        mExecutiveProfileList = (ExecutiveProfileListView) contextView.findViewById(R.id.executive_profile_list);
        mIndustryBenchmarkView = (IndustryBenchmarkView) contextView.findViewById(R.id.industry_benchmark_layout);
        mIndustryBenchmarkView.setCurrentStockInfo(mDtSecCode, mSecName);

        mCompanyCountView = (TextView) contextView.findViewById(R.id.company_count);
        mAccumulatedHoldingsView = (TextView) contextView.findViewById(R.id.accumulated_holdings);
        mValueChangeView = (TextView) contextView.findViewById(R.id.value_change);
        mRatioTotalShareCapitalView = (TextView) contextView.findViewById(R.id.ratio_total_share_capital);
        mPillarChangeView = (PillarChangeView) contextView.findViewById(R.id.company_structure);
        mCompanyStructureEmpty = (LinearLayout) contextView.findViewById(R.id.company_structure_empty);

        mShareholderTopPanel = (LinearLayout) contextView.findViewById(R.id.shareholder_top_panel);
        mShareholderTopLayout = (LinearLayout) contextView.findViewById(R.id.shareholder_top_layout);
        mShareholderTopEmpty = (LinearLayout) contextView.findViewById(R.id.shareholder_top_empty);

        mIndustryChainLayout = contextView.findViewById(R.id.industryChainLayout);
        mIndustryChainLayout.findViewById(R.id.upstreamProductTitle).setOnClickListener(this);
        mIndustryChainLayout.findViewById(R.id.downstreamProductTitle).setOnClickListener(this);
        mUpstreamProductTextView = (TextView) contextView.findViewById(R.id.upstreamProduct);
        mUpstreamProductTextView.setOnClickListener(this);
        mDownstreamProductTextView = (TextView) contextView.findViewById(R.id.downstreamProduct);
        mDownstreamProductTextView.setOnClickListener(this);
        contextView.findViewById(R.id.industrialChainMore).setOnClickListener(this);

        mPartnerTopPanel = (LinearLayout) contextView.findViewById(R.id.partner_top_panel);
        mPartnerTopLayout = (LinearLayout) contextView.findViewById(R.id.partner_top_layout);
        mPartnerTopEmpty = (LinearLayout) contextView.findViewById(R.id.partner_top_empty);

        mFundHolderPanel = (LinearLayout) contextView.findViewById(R.id.fund_holder_panel);
        mFundHolderLayout = (LinearLayout) contextView.findViewById(R.id.fund_holder_layout);
        mFundHolderEmpty = (LinearLayout) contextView.findViewById(R.id.fund_holder_empty);

        mCapitalStructureLineView = (CapitalStructureChangeLineView) contextView.findViewById(R.id.capital_structure_line);
        mEmptyView = contextView.findViewById(R.id.empty_view);
    }

    private void setCompanyAddress(String locate) {
        if(!TextUtils.isEmpty(locate)) {
            mAddress.setText(locate);
        }
    }

    private void setNotData() {
        //主营收入构成
        mRevenuePanel.setVisibility(View.GONE);
        mRevenueEmpty.setVisibility(View.VISIBLE);

        // 分红送配
        mBonusPanel.setVisibility(View.GONE);
        mBonusEmpty.setVisibility(View.VISIBLE);

        // 高管简介
        mExecutiveProfilePanel.setVisibility(View.GONE);
        mExecutiveProfileEmpty.setVisibility(View.VISIBLE);

        // 行业对比
        mIndustryBenchmarkView.setDatas(new ArrayList<>());

        // 十大流通股东
        mShareholderTopPanel.setVisibility(View.GONE);
        mShareholderTopEmpty.setVisibility(View.VISIBLE);

        // 十大股东
        mPartnerTopPanel.setVisibility(View.GONE);
        mPartnerTopEmpty.setVisibility(View.VISIBLE);

        // 主力持仓
        mPillarChangeView.setVisibility(View.GONE);
        mCompanyStructureEmpty.setVisibility(View.VISIBLE);

        // 机构持股
        mFundHolderPanel.setVisibility(View.GONE);
        mFundHolderEmpty.setVisibility(View.VISIBLE);
    }

    private void setData(CompanyDesc companyDesc) {
        //公司简介
        mCompanyDesc = companyDesc;

        CompanyProfile companyProfile = mCompanyDesc.getStCompanyProfile();

        String name = companyProfile.getSName();
        if (!TextUtils.isEmpty(name)) {
            mTextCompanyName.setText(name);//公司名称
        }

        setCompanyAddress(companyProfile.getSRegistryLocate());

        setActualControllers(companyProfile.getVController());

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

        String region = companyProfile.getSRegion();//所属地区
        if (!TextUtils.isEmpty(region)) {
            mTextRegion.setText(region);
        }

        String industry = companyProfile.getSIndustry();//所属行业
        if (!TextUtils.isEmpty(industry)) {
            mTextIndustry.setText(industry);
        }

        String mainBusiness = companyProfile.getSMainBusiness();//主营业务
        if (!TextUtils.isEmpty(mainBusiness)) {
            mTextMainBusiness.setText(mainBusiness);
        }

        //主营收入构成
        List<PrimeOperatingRevenue> primeOperatingRevenue = mCompanyDesc.getVPrimeOperatingRevenue();

        mRevenueLayout.removeAllViews();
        mDoughnutView.setSectorDatas(adaptDoughnutData(primeOperatingRevenue));
        primeOperatingRevenue = adaptPrimeOperatingRevenueList(primeOperatingRevenue);
        int size = primeOperatingRevenue.size();
        if(size > 1) {
            mRevenuePanel.setVisibility(View.VISIBLE);
            mRevenueEmpty.setVisibility(View.GONE);
            for (int i=1; i<size; i++) {
                PrimeOperatingRevenue revenue = primeOperatingRevenue.get(i);
                RelativeLayout revenueItemView = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.intro_revenue_item, null);

                ImageView icon = (ImageView) revenueItemView.findViewById(R.id.icon);
                if(revenue.getDSalesRevenue() > 0 && revenue.getFRatio() >= 0.3f) {
                    icon.setVisibility(View.VISIBLE);
                    GradientDrawable bg = (GradientDrawable)icon.getBackground();
                    if(bg != null) {
                        bg.setColor(DoughnutView.getSectorColor(getContext(), i - 1));
                    }
                } else {
                    icon.setVisibility(View.INVISIBLE);
                }
                ((TextView) revenueItemView.findViewById(R.id.revenue_type)).setText(revenue.getSTypeName());//主营业务名
                String salesRevenue = revenue.getSSalesRevenue();
                if(!TextUtils.isEmpty(salesRevenue) && salesRevenue.endsWith("元")) {
                    salesRevenue = salesRevenue.substring(0, salesRevenue.length() - 1);
                }
                ((TextView) revenueItemView.findViewById(R.id.revenue_amount)).setText(salesRevenue);//销售收入
                mRevenueLayout.addView(revenueItemView);
            }
        } else {
            mRevenuePanel.setVisibility(View.GONE);
            mRevenueEmpty.setVisibility(View.VISIBLE);
        }

        // 高管简介
        ArrayList<SeniorExecutive> seniorExecutiveList = mCompanyDesc.getVtSExecutive();
        if(seniorExecutiveList != null && !seniorExecutiveList.isEmpty()) {
            mExecutiveProfilePanel.setVisibility(View.VISIBLE);
            mExecutiveProfileEmpty.setVisibility(View.GONE);
            mExecutiveProfileList.setData(seniorExecutiveList);
        } else {
            mExecutiveProfilePanel.setVisibility(View.GONE);
            mExecutiveProfileEmpty.setVisibility(View.VISIBLE);
        }

        // 行业对比
        mIndustryBenchmarkView.setDatas(mCompanyDesc.getVtIndustryCompare());

        //分红送配
        ArrayList<DividendPayingPlacing> bonusList = mCompanyDesc.getVDividendPayingPlacing();
        mBonusLayout.removeAllViews();
        if(!bonusList.isEmpty()) {
            mBonusPanel.setVisibility(View.VISIBLE);
            mBonusEmpty.setVisibility(View.GONE);
            for (DividendPayingPlacing bonus : bonusList) {
                LinearLayout bonusItemView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.intro_bonus_item, null);
                ((TextView) bonusItemView.findViewById(R.id.bonus_year)).setText(bonus.getSYear());//年份
                ((TextView) bonusItemView.findViewById(R.id.bonus_content)).setText(bonus.getSDetail());//方案
                ((TextView) bonusItemView.findViewById(R.id.bonus_date)).setText(bonus.getSDate());//日期
                mBonusLayout.addView(bonusItemView);
            }
        } else {
            mBonusPanel.setVisibility(View.GONE);
            mBonusEmpty.setVisibility(View.VISIBLE);
        }

        //股本结构
        CapitalStructure capitalStructure = mCompanyDesc.getStCapitalStructure();
        setTextById(R.id.company_structure_date, capitalStructure.getSDate());
        String equity = capitalStructure.getSEquity();
        setTextById(R.id.struct_total_share, equity);//总股本
        String circulationStock = capitalStructure.getSCirculationStock();
        setTextById(R.id.struct_circulating, circulationStock);//流通股
        setTextById(R.id.struct_shareholder_count, capitalStructure.getSShareholderNumber());//股东人数
        String perCapitaHoldings = capitalStructure.getSPerCapitaHoldings();
        setTextById(R.id.struct_average, perCapitaHoldings);//人均持股

        //股本结构变化趋势
        setCapitalStructureData(mCompanyDesc.getStCapitalStructure());

        //十大流通股东
        setTextById(R.id.company_structure_top_ten_date, mCompanyDesc.getSTopShareholderDate());
        ArrayList<TopShareholder> topShareholders = mCompanyDesc.getVTopShareholder();
        if(topShareholders != null && !topShareholders.isEmpty()) {
            mShareholderTopPanel.setVisibility(View.VISIBLE);
            mShareholderTopEmpty.setVisibility(View.GONE);
            mShareholderTopLayout.removeAllViews();
            for (TopShareholder shareholder : topShareholders) {
                ShareholderTopItemView shareholderTopItemView = (ShareholderTopItemView) LayoutInflater.from(getActivity()).inflate(R.layout.shareholder_top_item, null);
                shareholderTopItemView.setData(shareholder);
                mShareholderTopLayout.addView(shareholderTopItemView);
            }
        } else {
            mShareholderTopPanel.setVisibility(View.GONE);
            mShareholderTopEmpty.setVisibility(View.VISIBLE);
        }

        //十大股东
        setTextById(R.id.company_partner_top_ten_date, mCompanyDesc.getSTopHolderDate());
        ArrayList<TopShareholder> topholders = mCompanyDesc.getVTopHolder();

        if(topholders != null && !topholders.isEmpty()) {
            mPartnerTopPanel.setVisibility(View.VISIBLE);
            mPartnerTopEmpty.setVisibility(View.GONE);
            mPartnerTopLayout.removeAllViews();
            for (TopShareholder topholder : topholders) {
                ShareholderTopItemView shareholderTopItemView = (ShareholderTopItemView) LayoutInflater.from(getActivity()).inflate(R.layout.shareholder_top_item, null);
                shareholderTopItemView.setData(topholder);
                mPartnerTopLayout.addView(shareholderTopItemView);
            }
        } else {
            mPartnerTopPanel.setVisibility(View.GONE);
            mPartnerTopEmpty.setVisibility(View.VISIBLE);
        }

        // 主力持仓
        MainHolder mainHolder = mCompanyDesc.getStMainHolder();
        boolean hasEntity = false;
        if(mainHolder != null) {
            mCompanyCountView.setText(mainHolder.getIHolders() + "家");
            mAccumulatedHoldingsView.setText(formatWanShares(mainHolder.getFTotalCount(), false));
            mValueChangeView.setText(formatWanShares(mainHolder.getFChange(), true));
            mRatioTotalShareCapitalView.setText(StringUtil.getFormatedFloat(mainHolder.getFRate()) + "%");
            ArrayList<MainHolderDetail> details = mainHolder.getVtMainHolderDetail();
            if(details != null && !details.isEmpty()) {
                List<PillarChangeEntity> entities = new ArrayList<>(details.size());
                for(int i=details.size()-1; i>=0; i--) {
                    MainHolderDetail detail = details.get(i);
                    float rate = detail.getFRate();
                    float price = detail.getFPrice();
                    if(price > 0) {
                        PillarChangeEntity entity = new PillarChangeEntity(detail.getSDateDesc(), price, rate);
                        entities.add(entity);
                    }
                }
                hasEntity = !entities.isEmpty();
                mPillarChangeView.setData(entities);
            }
        }

        if(hasEntity) {
            mPillarChangeView.setVisibility(View.VISIBLE);
            mCompanyStructureEmpty.setVisibility(View.GONE);
        } else {
            mPillarChangeView.setVisibility(View.GONE);
            mCompanyStructureEmpty.setVisibility(View.VISIBLE);
        }

        //机构持股
        setTextById(R.id.company_structure_change_date, mCompanyDesc.getSFundsholderDate());
        ArrayList<Fundsholder> fundsholders = mCompanyDesc.getVFundsholder();
        if(fundsholders != null && !fundsholders.isEmpty()) {
            mFundHolderPanel.setVisibility(View.VISIBLE);
            mFundHolderEmpty.setVisibility(View.GONE);
            mFundHolderLayout.removeAllViews();
            for (Fundsholder fundsholder : fundsholders) {
                LinearLayout fundHolderItemView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.shareholder_top_item, null);
                final TextView nameView = (TextView) fundHolderItemView.findViewById(R.id.shareholder_top_name);
                nameView.setText(fundsholder.sName);//姓名
                setTextById(fundHolderItemView, R.id.shareholder_top_ratio, fundsholder.getSRatio());//比例
                int shareholderChange = fundsholder.getEShareholderChange();
                setTextAndColor(fundHolderItemView, R.id.shareholder_top_change, fundsholder.getSChangeDetail(), shareholderChange);//持股变动详情
                mFundHolderLayout.addView(fundHolderItemView);
            }
        } else {
            mFundHolderPanel.setVisibility(View.GONE);
            mFundHolderEmpty.setVisibility(View.VISIBLE);
        }
    }

    private String formatWanShares(float amount, boolean withSigh) {
        String sign = ((withSigh && amount > 0) ? "+" :"");
        if(Math.abs(amount) > 10000) {
            return sign + StringUtil.getFormatedFloat(amount / 10000) + "亿股";
        }
        return sign + StringUtil.getFormatedFloat(amount) + "万股";
    }

    private void setActualControllers(ArrayList<String> controllers) {
        if(controllers != null) {
            int size = controllers.size();
            if(size > 0) {
                String text = controllers.get(0);
                if(size > 1) {
                    text = text + "等";
                }
                mActualControllers.setText(text);
            }
        }

    }

    private List<Float> adaptDoughnutData(List<PrimeOperatingRevenue> revenueList) {
        if(revenueList != null && revenueList.size() > 1) {
            List<Float> percents = new ArrayList<>(6);
            int count = 0;
            float total = 0;
            for(int i=1, size=revenueList.size(); i<size; i++) {
                PrimeOperatingRevenue revenue = revenueList.get(i);
                float ratio = revenue.getFRatio();
                double sales = revenue.getDSalesRevenue();
                if(sales > 0 && ratio > 0) {
                    if(count < MAX_REVENUE_COUNT) {
                        percents.add(ratio);
                    } else {
                        float other = percents.get(percents.size() - 1);
                        other += ratio;
                        percents.set(percents.size() - 1, other);
                    }
                    total += ratio;
                    count++;
                }
            }

            List<Float> finalPercents = new ArrayList<>(MAX_REVENUE_COUNT);
            for(int i=0, size=percents.size(); i<size; i++) {
                float percent = percents.get(i);
                finalPercents.add(percent / total);
            }
            return finalPercents;
        }
        return Collections.EMPTY_LIST;
    }

    private List<PrimeOperatingRevenue> adaptPrimeOperatingRevenueList(List<PrimeOperatingRevenue> revenueList) {
        if(revenueList != null && revenueList.size() > 1) {
            int size = revenueList.size();
            float total = 0;
            if(size > MAX_REVENUE_COUNT) {
                double other = 0;
                float ratio = 0;
                for(; revenueList.size() > MAX_REVENUE_COUNT;) {
                    PrimeOperatingRevenue revenue = revenueList.get(revenueList.size() - 1);
                    other += revenue.getDSalesRevenue();
                    ratio += revenue.getFRatio();
                    total += revenue.getFRatio();
                    revenueList.remove(revenueList.size() - 1);
                }

                for(int i=0; i< revenueList.size(); i++) {
                    PrimeOperatingRevenue revenue = revenueList.get(revenueList.size() - 1);
                    total += revenue.getFRatio();
                }

                PrimeOperatingRevenue otherRevenue = new PrimeOperatingRevenue();
                otherRevenue.setSTypeName("其他收入合计");
                otherRevenue.setFRatio(ratio * 100 / total);
                otherRevenue.setDSalesRevenue(other);
                otherRevenue.setSSalesRevenue(StringUtil.getAmountString(other));
                revenueList.add(otherRevenue);
            }

            return revenueList;
        }
        return Collections.EMPTY_LIST;
    }

    private void setTextAndColor(ViewGroup parent, int id, String text, int changeType) {
        TextView textView = (TextView) parent.findViewById(id);
        textView.setText(text);
        switch (changeType) {
            case ShareholderChange.SHC_UNCHANGE:
                break;
            case ShareholderChange.SHC_INCREASE:
                textView.setTextColor(mColorRed);
                break;
            case ShareholderChange.SHC_DECREASE:
                textView.setTextColor(mColorGreen);
                break;
            default:
                break;
        }
    }

    private void setTextById(int textId, String text) {
        TextView textView = (TextView) mContentView.findViewById(textId);
        textView.setText(text);
    }

    private void setTextById(View contextView, int textId, String text) {
        TextView textView = (TextView) contextView.findViewById(textId);
        textView.setText(text);
    }

    private void showViewByState(int state) {
        switch (state) {
            case DengtaConst.UI_STATE_NORMAL:
                mCapitalStructureLineView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                break;
            case DengtaConst.UI_STATE_LOADING:
                break;
            case DengtaConst.UI_STATE_NO_CONTENT:
                mCapitalStructureLineView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                break;
            case DengtaConst.UI_STATE_FAILED_RETRY:
                break;
            default:
                break;
        }
    }

    private void setCapitalStructureData(CapitalStructure capitalStructure) {
        boolean isNormal = false;
        if (capitalStructure != null) {
            ArrayList<CapitalStructureDetail> details = capitalStructure.getVCapitalStructureDetail();
            if (details != null && details.size() > 0) {
                isNormal = true;
                showViewByState(DengtaConst.UI_STATE_NORMAL);
                mCapitalStructureLineView.setData(details, capitalStructure.getSShareholderNumber(), capitalStructure.getSPerCapitaHoldings());
            }
        }

        if (!isNormal) {
            showViewByState(DengtaConst.UI_STATE_NO_CONTENT);
        }
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.industrialChainMore:
                WebBeaconJump.showIndustrialChain(activity, mDtSecCode, mSecName);
                break;
            case R.id.upstreamProduct:
            case R.id.upstreamProductTitle:
                WebBeaconJump.showUpstreamProduct(activity, mDtSecCode, mSecName);
                break;
            case R.id.downstreamProduct:
            case R.id.downstreamProductTitle:
                WebBeaconJump.showDownstreamProduct(activity, mDtSecCode, mSecName);
                break;
            default:
                break;
        }
    }
}
