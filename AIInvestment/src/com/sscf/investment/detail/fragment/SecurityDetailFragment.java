package com.sscf.investment.detail.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sscf.investment.R;
import com.sscf.investment.common.entity.SecDetailPageViewEntity;
import com.sscf.investment.db.DBHelper;
import com.sscf.investment.detail.presenter.SecurityDetailPresenter;
import com.sscf.investment.detail.view.ISecurityDetailView;
import com.sscf.investment.detail.view.KLineInfosView;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.widget.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import BEC.SecBaseInfoRsp;
import BEC.SecCode;
import BEC.SecLiveMsgRsp;
import BEC.SecQuote;

/**
 * Created by liqf on 2015/11/4.
 */
public class SecurityDetailFragment extends BaseFragment implements ISecurityDetailView {
    private static final String TAG = SecurityDetailFragment.class.getSimpleName();
    protected String mDtSecCode;
    protected String mShowSecCode;
    protected String mName;
    protected int mMarketType;
    public static final int INIT_TAB_DELAY = 100;

    protected boolean mIsTrading;
    protected boolean mIsCallauction;

    protected LineChartFragment mLineChartFragment;
    protected SecurityDetailPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DtLog.d(TAG, "onCreateView: " + toString());
        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
            mName = args.getString(DengtaConst.KEY_SEC_NAME).trim();
            setTimeStatHelperExtra();
        }
        SecCode secCode = StockUtil.convertSecInfo(mDtSecCode);
        mMarketType = secCode.getEMarketType();
        mShowSecCode = secCode.getSSecCode();

        DtLog.d(TAG, "onCreateView: " + mName);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void selectCustomStock(String dtSecCode) {
        if(mLineChartFragment != null) {
            mLineChartFragment.selectCustomStock(dtSecCode);
        }
    }

    private void setTimeStatHelperExtra() {
        List extra = new ArrayList(1);
        extra.add(mDtSecCode);
        if(mTimeStatHelper != null) {
            mTimeStatHelper.setExtras(extra);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DtLog.d(TAG, "onDestroy: " + mName);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mLineChartFragment != null) {
            mLineChartFragment.setUserVisibleHint(isVisibleToUser);
        }
    }

    @Override
    public void onFirstUserVisible() {
        DtLog.d(TAG, "onFirstUserVisible: " + mName);
        super.onFirstUserVisible();
        updatePageViewCountCache();
        StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_SHOW);
    }

    @Override
    public void onUserVisible() {
        DtLog.d(TAG, "onUserVisible: " + mName);
        super.onUserVisible();
        updatePageViewCountCache();
    }

    @Override
    public void onFirstUserInvisible() {
        DtLog.d(TAG, "onFirstUserInvisible: " + mName);
        super.onFirstUserInvisible();
    }

    @Override
    public void onUserInvisible() {
        DtLog.d(TAG, "onUserInvisible: " + mName);
        super.onUserInvisible();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DtLog.d(TAG, "onDestroyView: " + mName);
    }

    protected final void initLineChartFragment() {
        FragmentManager childFragmentManager = getChildFragmentManager();

        Fragment restoredFragment = childFragmentManager.findFragmentByTag(mDtSecCode);
        if(restoredFragment != null && restoredFragment instanceof LineChartFragment) {
            mLineChartFragment = (LineChartFragment) restoredFragment;
        } else {
            mLineChartFragment = new LineChartFragment();
            Bundle args = new Bundle();
            args.putString(DengtaConst.KEY_SEC_CODE, mDtSecCode);
            args.putString(DengtaConst.KEY_SEC_NAME, mName);
            mLineChartFragment.setArguments(args);
        }
        childFragmentManager.beginTransaction().replace(R.id.nested_fragment_container, mLineChartFragment, mDtSecCode).commitAllowingStateLoss();
        mLineChartFragment.setUserVisibleHint(getUserVisibleHint());
    }

    public void onPageScrolled(boolean isLeft, float positionOffset) {
    }

    public void onPageScrollStateIdle() {
        DtLog.d(TAG, "onPageScrollStateIdle: " + mName);
    }

    private void updatePageViewCountCache() {
        new UpdatePVTask().executeOnExecutor(DengtaApplication.getApplication().defaultExecutor, mDtSecCode);
    }

    @Override
    public void updateQuoteView(SecQuote quote) {
    }

    @Override
    public void onLoadComplete() {
    }

    @Override
    public void updateBaseInfoView(SecBaseInfoRsp baseInfoRsp) {
    }

    @Override
    public void updateLiveMsgView(SecLiveMsgRsp rsp) {
    }

    @Override
    public void updateKLineQuoteView(KLineInfosView.KLineLineTouchEvent event, SecQuote quote) {
    }

    protected void queryTradingValuesAndRefresh(final boolean needQueryState) {
        final TradingStateManager tradingStateManager = DengtaApplication.getApplication().getTradingStateManager();
        final int marketType = mMarketType;
        if (needQueryState) {
            int tradingState = tradingStateManager.getTradingState(marketType, false);
            if (tradingState == TradingStateManager.TRADING_STATE_NOT_INITED) {
                tradingStateManager.loadTradingTimeData();
                return;
            }

            mIsTrading = tradingState == TradingStateManager.TRADING_STATE_TRADING;
            mIsCallauction = tradingState == TradingStateManager.TRADING_STATE_CALLAUCTION;
        }

        mPresenter.setTrading(mIsTrading);
        if (mLineChartFragment != null) {
            mLineChartFragment.setIsTrading(mIsTrading);
            mLineChartFragment.setIsCallauction(mIsCallauction);
            final String openTimeStr = tradingStateManager.getOpenTimeStr(marketType);
            final String middleTimeStr = tradingStateManager.getMiddleTimeStr(marketType);
            final String closeTimeStr = tradingStateManager.getCloseTimeStr(marketType);
            mLineChartFragment.setTradingShowTime(openTimeStr, middleTimeStr, closeTimeStr);
            final int tradingMinutes = tradingStateManager.getTradingMinutes(marketType);
            mLineChartFragment.setTradingMinutes(tradingMinutes);
        }
        if (mIsTrading || mIsCallauction) {
            mPresenter.refresh();
        }
    }
}

final class UpdatePVTask extends AsyncTask<String, Object, Object> {

    @Override
    protected Object doInBackground(String[] params) {
        final String dtCode = params != null && params.length > 0 ? params[0] : null;
        if (!TextUtils.isEmpty(dtCode)) {
            final DBHelper dbHelper = DBHelper.getInstance();
            SecDetailPageViewEntity entity = dbHelper.findById(SecDetailPageViewEntity.class, dtCode);
            if (entity == null) {
                entity = new SecDetailPageViewEntity();
                entity.setDtSecCode(dtCode);
                entity.setPv(1);
                dbHelper.add(entity);
            } else {
                entity.setPv(entity.getPv() + 1);
                dbHelper.update(entity);
            }
        }
        return null;
    }
}
