package com.sscf.investment.portfolio;

import android.content.Intent;
import android.os.Bundle;
import com.sscf.investment.main.MainActivity;
import com.sscf.investment.sdk.utils.DtLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sscf.investment.R;
import com.sscf.investment.search.SearchActivity;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.widget.BaseFragment;

/**
 * Created by xuebinliu on 2015/8/10.
 *
 * 没有自选时显示的空页面
 */
public final class PortfolioEmptyFragment extends BaseFragment implements View.OnClickListener, Runnable {
    private static final String TAG = PortfolioEmptyFragment.class.getSimpleName();
    // 分割线
    private View mDividerView;
    // 提示语
    private TextView mAddTips;
    // 登录状态
    private LoginStatusLayout mLoginLayout;
    // 大盘界面
    private PortfolioHeaderMarket mPortfolioHeaderMarket;
    private PeriodicHandlerManager mPeriodicHandlerManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DtLog.d(TAG, "onCreateView");
        final View root = inflater.inflate(R.layout.portfolio_empty_stock, null, false);
        initViews(root);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        return root;
    }

    private void initViews(View root) {
        root.findViewById(R.id.portfolio_empty_add_button).setOnClickListener(this);
        mAddTips = (TextView) root.findViewById(R.id.portfolio_empty_add_tips);

        // 指数行情窗口
        mPortfolioHeaderMarket = (PortfolioHeaderMarket) root.findViewById(R.id.portfolio_header_market_id);
        mDividerView = root.findViewById(R.id.divider);

        root.findViewById(R.id.portfolio_empty_discory_button).setOnClickListener(this);

        // 登录状态界面
        mLoginLayout = (LoginStatusLayout) root.findViewById(R.id.loginStatusLayout);
        mLoginLayout.updateUserInfo();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        updateStatus();
        mPeriodicHandlerManager.runPeriodicDelay(100); // 延迟100ms，保证能立刻刷新自选行情
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        updateStatus();
        mPeriodicHandlerManager.runPeriodicDelay(100); // 延迟100ms，保证能立刻刷新自选行情
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
        mPeriodicHandlerManager.stop();
    }

    @Override
    public void run() {
        requestData();
        mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
    }

    public void requestData() {
        mPortfolioHeaderMarket.requestData();
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.portfolio_empty_add_button:
                handleAddButton();
                break;
            case R.id.portfolio_empty_discory_button:
                final MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.switchTab(1);
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_EMPTY_CLICK_DISCORY_BUTTON);
                break;
            default:
                break;
        }
    }

    private void handleAddButton() {
        DtLog.d(TAG, "handleAddButton");
                // 点击了股票的加号，跳转到搜索
        startActivity(new Intent(getActivity(), SearchActivity.class));
        StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_CLICK_ADD);
    }

    public void updateStatus() {
        DtLog.d(TAG, "updateStatus");
        if (!isAdded()) {
            return;
        }

        DtLog.d(TAG, "updateStatus update left stock");
        mPortfolioHeaderMarket.setVisibility(View.VISIBLE);
        mLoginLayout.setVisibility(View.VISIBLE);
        mDividerView.setVisibility(View.VISIBLE);
        mAddTips.setText(R.string.portfolio_empty_add_stock);
    }
}
