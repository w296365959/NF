package com.sscf.investment.discover.manager;

import BEC.*;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IAccountManager;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.entity.AccountInfoEntity;

/**
 * Created by davidwei on 2015/10/23.
 */
public final class DiscoverRequestManager {

    /**
     * 删除某个订阅策略
     */
    public static void removeSubscriptionRequest(final String strategyId, final DataSourceProxy.IRequestCallback observer) {
        final ActStrategySubReq req = new ActStrategySubReq();

        req.iActType = E_STRATEGY_SUB_ACT_TYPE.E_STRATEGY_SUB_ACT_CANCEL;
        req.sStrategyId = strategyId;

        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        final AccountInfoEntity accountInfo = accountManager != null ? accountManager.getAccountInfo() : null;
        final AccountTicket accountTicket = new AccountTicket();
        if (accountInfo != null) {
            accountTicket.vtTicket = accountInfo.ticket;
        }
        req.stAccountTicket = accountTicket;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_STOCK_PICK_REMOVE_SUBSCRIPTION, req, observer);
    }

    /**
     * 获得用户订阅策略列表
     */
    public static void getSubscriptionListRequest(final DataSourceProxy.IRequestCallback observer) {
        final GetStrategySubListReq req = new GetStrategySubListReq();

        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        final AccountInfoEntity accountInfo = accountManager != null ? accountManager.getAccountInfo() : null;
        final AccountTicket accountTicket = new AccountTicket();
        if (accountInfo != null) {
            accountTicket.vtTicket = accountInfo.ticket;
        }
        req.stAccountTicket = accountTicket;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_STOCK_PICK_GET_SUBSCRIPTION_LIST, req, observer);
    }

    /**
     * 获得智能选股里的策略列表
     * @param observer
     */
    public static void getStrategyListRequest(final DataSourceProxy.IRequestCallback observer) {
        final GetCategoryListReq req = new GetCategoryListReq();
        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_DISCOVER_STRATEGY_LIST, req, observer);
    }

    /**
     * 智能选股
     * @param startId
     * @param observer
     */
    public static void getIntelligentPickStockRequest(final String startId, final DataSourceProxy.IRequestCallback observer) {
        final IntelliPickStockReq req = new IntelliPickStockReq();
        req.sStartId = startId;
        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_DISCOVER_INTELLIGENT_PICK_STOCK, req, observer, startId);
    }

    /**
     * 选股页面推荐的增值服务列表
     */
    public static void getValueAddedListRequest(final DataSourceProxy.IRequestCallback observer) {
        final RecommValueAddedListReq req = new RecommValueAddedListReq();

        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        final AccountInfoEntity accountInfo = accountManager != null ? accountManager.getAccountInfo() : null;
        final AccountTicket accountTicket = new AccountTicket();
        if (accountInfo != null) {
            accountTicket.vtTicket = accountInfo.ticket;
        }
        req.stAccountTicket = accountTicket;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_STOCK_PICK_VALUEADDED_LIST, req, observer);
    }

    /**
     * 精选策略列表
     */
    public static void getSelectedStragetyList(final DataSourceProxy.IRequestCallback observer) {
        final ConditionPickStrategyListReq req = new ConditionPickStrategyListReq();
        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_SELECTED_STRATEGY_LIST, req, observer);
    }
}
