package com.sscf.investment.detail.fragment;

import BEC.*;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.R;
import com.sscf.investment.market.view.StockMarginTradeView;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.detail.entity.DailyCapitalChangeEntity;
import com.sscf.investment.detail.view.CapitalFlowChangeView;
import com.sscf.investment.detail.view.CapitalFlowView;
import com.sscf.investment.main.DengtaApplication;
import com.dengtacj.request.MarketRequestManager;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.web.CommonWebConst;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.BaseFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqf on 2015/7/21
 *
 */
public class CapitalFragment extends BaseFragment implements DataSourceProxy.IRequestCallback, OnReloadDataListener, Runnable, View.OnClickListener {
    private static final String TAG = CapitalFragment.class.getSimpleName();
    private TextView mCapitalOfFiveDaysText;
    private TextView mMainIn;
    private TextView mMainOut;
    private TextView mNetIn;
    private CapitalFlowView mCapitalFlowViewPie;
    private CapitalFlowChangeView mCapitalFlowView;
    private CapitalFlowChangeView mCaptitalFlowChangeView;
    private View mMarginTradingBalanceLayout;
    private TextView mMarginTradingInfo;
    private StockMarginTradeView mStockMarginTradeView;

    private String mTitleStr;
    private String mDtSecCode;
    private String mSecName;

    private int mColorRed;
    private int mColorGreen;
    private int mColorBase;
    private String mCapitalSuperStr;
    private String mCapitalBigStr;
    private String mCapitalMidStr;
    private String mCapitalSmallStr;

    private ArrayList<StockDateMarginTrade> mStockDateMarginTradeList;

    private int mMarketType;

    private PeriodicHandlerManager mPeriodicHandlerManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DtLog.d(TAG, "onCreateView");
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_capital, container, false);

        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
            mSecName = args.getString(DengtaConst.KEY_SEC_NAME);
            mMarketType = StockUtil.getMarketType(mDtSecCode);
        }

        initResources();

        mCapitalOfFiveDaysText = (TextView) contextView.findViewById(R.id.capital_five_days);

        mMarginTradingBalanceLayout = contextView.findViewById(R.id.marginTradingBalanceLayout);
        mMarginTradingInfo = (TextView) contextView.findViewById(R.id.marginTradingInfo);
        mStockMarginTradeView = (StockMarginTradeView) contextView.findViewById(R.id.marginTradingBalance);
        mTitleStr = getResources().getString(R.string.capital_deta);

        mMainIn = (TextView) contextView.findViewById(R.id.main_in_value);
        mMainOut = (TextView) contextView.findViewById(R.id.main_out_value);
        mNetIn = (TextView) contextView.findViewById(R.id.capital_net_value);
        mCapitalFlowViewPie = (CapitalFlowView) contextView.findViewById(R.id.capital_flow_pie);
        mCapitalFlowViewPie.setType(CapitalFlowView.TYPE_DETAIL_TAB_PIE);
        mCapitalFlowView = (CapitalFlowChangeView) contextView.findViewById(R.id.capital_flow);
        contextView.findViewById(R.id.help_icon).setOnClickListener(this);

        mCaptitalFlowChangeView = (CapitalFlowChangeView) contextView.findViewById(R.id.capital_change);

        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        return contextView;
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_SEC_NEWS);
        helper.setKey(StatConsts.STOCK_INFO_CAPITAL);
        return helper;
    }

    private void setMore(View root, int textRes, View.OnClickListener listener) {
        View moreView = root.findViewById(R.id.more);
        moreView.setOnClickListener(listener);
        ((TextView) moreView.findViewById(R.id.more_text)).setText(textRes);
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        if (mPeriodicHandlerManager != null) {
            mPeriodicHandlerManager.runPeriodic();
        }
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        mPeriodicHandlerManager.runPeriodic();
    }

    @Override
    public void onFirstUserInvisible() {
        super.onFirstUserInvisible();
        if (mPeriodicHandlerManager != null) {
            mPeriodicHandlerManager.stop();
        }
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        if (mPeriodicHandlerManager != null) {
            mPeriodicHandlerManager.stop();
        }
    }

    @Override
    public void run() {
        DtLog.d(TAG, "mPeriodicHandlerManager run : mSecName = " + mSecName);
        QuoteRequestManager.getCapitalDataRequest(mDtSecCode, 5, this);
        if (mStockDateMarginTradeList == null) {
            MarketRequestManager.getStockMarginTrade(mDtSecCode, StockMarginTradeView.GET_DATA_COUNT, this);
        }

        final boolean isTrading = DengtaApplication.getApplication().getTradingStateManager().isTrading(mMarketType);
        if (getUserVisibleHint() && isTrading) {
            mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
        } else {
            mPeriodicHandlerManager.stop();
        }
    }

    @Override
    public void callback(boolean success, EntityObject entity) {
        if (!isAdded()) {
            return;
        }

        switch (entity.getEntityType()) {
            case EntityObject.ET_GET_FUND:
                final ArrayList<CapitalFlow> capitalFlowList = EntityUtil.entityToCapitalFlowList(success, entity);
                if (capitalFlowList != null && capitalFlowList.size() > 0) {
                    ThreadUtils.runOnUiThread(() -> {
                        if (CapitalFragment.this.isAdded()) {
                            setData(capitalFlowList);
                        }
                    });
                }
                break;
            case EntityObject.ET_GET_STOCK_MARGIN_TRADE:
                handleStockMarginTrade(success, entity);
                break;
            default:
                break;
        }
    }

    private void setData(ArrayList<CapitalFlow> capitalFlowList) {
        setCapitalFlowData(capitalFlowList.get(0));
        setFiveDaysData(capitalFlowList);
    }

    private void handleStockMarginTrade(boolean success, EntityObject entity) {
        if(success && entity.getEntity() != null) {
            StockMarginTradeRsp rsp = (StockMarginTradeRsp) entity.getEntity();
            ArrayList<StockDateMarginTrade> stockDateMarginTradeList = rsp.getVtStockDateMarginTrade();
            mStockDateMarginTradeList = stockDateMarginTradeList;
            if(stockDateMarginTradeList != null && stockDateMarginTradeList.size() > 2) {
                ThreadUtils.runOnUiThread(() -> {
                    if (getActivity() == null) {
                        return;
                    }
                    setMarginTradeData(rsp);
                });
            }
        }
    }

    private void setMarginTradeData(StockMarginTradeRsp rsp) {
        mMarginTradingBalanceLayout.setVisibility(View.VISIBLE);
        ArrayList<StockDateMarginTrade> stockDateMarginTradeList = rsp.getVtStockDateMarginTrade();
        final StockDateMarginTrade todayMarginTrade = stockDateMarginTradeList.get(0);
        final StockDateMarginTrade yestodayMarginTrade = stockDateMarginTradeList.get(1);
        String marginBalance = StringUtil.getMarginTradingBalance((long)(todayMarginTrade.getFMarginBalance()));
        float updownPercent = (todayMarginTrade.getFMarginBalance() - yestodayMarginTrade.getFMarginBalance()) / yestodayMarginTrade.getFMarginBalance();

        String updownTextPre = updownPercent >= 0 ? "上升" : "下降";
        IndustryRank rank = rsp.getStBalanceRank();
        String rankText = rank.getIPosition() + "/" + rank.getITotal();
        final String text = getResources().getString(R.string.capital_margin_trading_balance_info,
                marginBalance, rankText, updownTextPre + StringUtil.getPercentString(Math.abs(updownPercent)));
        mMarginTradingInfo.setText(text);

        mStockMarginTradeView.setData(stockDateMarginTradeList);
    }

    private boolean isMember() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        return accountManager != null && accountManager.isMember();
    }

    private void setFiveDaysData(ArrayList<CapitalFlow> capitalFlowList) {
        List<DailyCapitalChangeEntity> entities = new ArrayList<>();

        for (CapitalFlow capitalFlow : capitalFlowList) {
            long mainFundIn = (long) (capitalFlow.getFSuperin() + capitalFlow.getFBigin());
            long mainFundOut = (long) (capitalFlow.getFSuperout() + capitalFlow.getFBigout());
            long delta = mainFundIn - mainFundOut;

            final String day = TimeUtils.getTimeString("MM-dd", capitalFlow.getLTime() * 1000L);
            entities.add(0, new DailyCapitalChangeEntity(day, delta));
        }
        mCaptitalFlowChangeView.setFiveDayValues(entities);

        setTitleText();
    }

    private void setTitleText() {
        float maxAbsValue = mCaptitalFlowChangeView.getMaxAbsValue();
        String unitStr = maxAbsValue >= StringUtil.YI ?
                getResources().getString(R.string.yi) :
                getResources().getString(R.string.wan);
        mCapitalOfFiveDaysText.setText(mTitleStr + "(" + unitStr + getResources().getString(R.string.yuan) + ")");
    }

    private void initResources() {
        Resources resources = getResources();
        mColorRed = ContextCompat.getColor(getContext(), R.color.stock_red_color);
        mColorGreen = ContextCompat.getColor(getContext(), R.color.stock_green_color);
        mColorBase = ContextCompat.getColor(getContext(), R.color.default_text_color_60);

        mCapitalSuperStr = resources.getString(R.string.capital_super);
        mCapitalBigStr = resources.getString(R.string.capital_big);
        mCapitalMidStr = resources.getString(R.string.capital_mid);
        mCapitalSmallStr = resources.getString(R.string.capital_small);
    }

    private void setCapitalFlowData(CapitalFlow capitalFlow) {
        float mainFundIn = capitalFlow.getFSuperin() + capitalFlow.getFBigin();
        float mainFundOut = capitalFlow.getFSuperout() + capitalFlow.getFBigout();

        mCapitalFlowViewPie.setData(capitalFlow);

        List<DailyCapitalChangeEntity> entities = new ArrayList<>();
        entities.add(new DailyCapitalChangeEntity(mCapitalSuperStr, capitalFlow.getFSuperin() - capitalFlow.getFSuperout()));
        entities.add(new DailyCapitalChangeEntity(mCapitalBigStr, capitalFlow.getFBigin() - capitalFlow.getFBigout()));
        entities.add(new DailyCapitalChangeEntity(mCapitalMidStr, capitalFlow.getFMidin() - capitalFlow.getFMidout()));
        entities.add(new DailyCapitalChangeEntity(mCapitalSmallStr, capitalFlow.getFSmallin() - capitalFlow.getFSmallout()));

        mCapitalFlowView.setFiveDayValues(entities);

        mMainIn.setText(StringUtil.getAmountStringWan(mainFundIn));
        mMainOut.setText(StringUtil.getAmountStringWan(mainFundOut));
        float netIn = mainFundIn - mainFundOut;
        mNetIn.setText(StringUtil.getAmountStringWan(netIn));

        if (netIn > 0) {
            mNetIn.setTextColor(mColorRed);
        } else if (netIn < 0) {
            mNetIn.setTextColor(mColorGreen);
        } else {
            mNetIn.setTextColor(mColorBase);
        }
    }

    @Override
    public void onReloadData() {
        // 资金
        QuoteRequestManager.getCapitalDataRequest(mDtSecCode, 5, this);
    }

    @Override
    public void onClick(View v) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.help_icon:
                if (TimeUtils.isFrequentOperation()) {
                    return;
                }

                WebBeaconJump.showWebActivity(activity, CommonWebConst.URL_FAQ_FUND_EXP, CommonWebConst.WT_FEATRUE_INTRODUCE);
                break;
            default:
                break;
        }
    }
}
