package com.sscf.investment.web.sdk.widget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IRedDotManager;
import com.sscf.investment.component.ui.widget.BaseActivity;
import com.sscf.investment.web.CommonWebConst;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.web.sdk.R;
import com.sscf.investment.web.sdk.utils.WebUtils;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import BEC.DtLiveType;

/**
 * Created by liqf on 2016/5/9.
 */
@Route("LiveMsgActivity")
public class LiveMsgActivity extends BaseActivity {

    private TabbedWebViewContainer mTabbedWebViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatisticsUtil.reportAction(StatisticsConst.LIVE_MSG_PAGE_DISPLAYED);

        setContentView(R.layout.activity_web_live_msg);

        Intent intent = getIntent();
        final int liveType = intent.getIntExtra(CommonWebConst.KEY_LIVE_TYPE, 0);

        ImageView backBtn = (ImageView) findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView faqBtn = (ImageView) findViewById(R.id.faq_button);
        faqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebBeaconJump.showLiveMsgFAQ(LiveMsgActivity.this);
            }
        });

        mTabbedWebViewContainer = (TabbedWebViewContainer) findViewById(R.id.tabbed_webview_container);
        final WebUrlManager urlManager = WebUrlManager.getInstance();
        mTabbedWebViewContainer.initWithTitlesAndUrls(new int[]{R.string.live_msg_portfolio, R.string.live_msg_market},
            new String[]{WebUtils.addThemeParam(urlManager.getLiveMsgPortfolioUrl()), WebUtils.addThemeParam(urlManager.getLiveMsgMarketUrl())});

        switchToTab(liveType);

        final IRedDotManager redDotManager = (IRedDotManager) ComponentManager.getInstance()
                .getManager(IRedDotManager.class.getName());
        if (redDotManager != null) {
            redDotManager.setPortfolioLiveState(false);
            redDotManager.setMainBoardLiveState(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTabbedWebViewContainer.destroy();
    }

    private void switchToTab(int liveType) {
        int tabIndex = 0;
        switch (liveType) {
            case DtLiveType.E_LIVE_PORTFOLIO:
            case DtLiveType.E_LIVE_ALL:
                tabIndex = 0;
                StatisticsUtil.reportAction(StatisticsConst.LIVE_MSG_PORTFOLIO_TAB_DISPLAYED);
                break;
            case DtLiveType.E_LIVE_MARKET:
                tabIndex = 1;
                StatisticsUtil.reportAction(StatisticsConst.LIVE_MSG_MARKET_TAB_DISPLAYED);
                break;
            default:
                break;
        }
        mTabbedWebViewContainer.switchTab(tabIndex);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this, true);
    }
}
