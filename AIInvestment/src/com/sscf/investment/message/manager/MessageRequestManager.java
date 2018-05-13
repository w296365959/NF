package com.sscf.investment.message.manager;

import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import java.util.Map;
import BEC.AccountTicket;
import BEC.AlertMsgClassDetailReq;
import BEC.AlertMsgClassListReq;
import BEC.E_MSG_CLASS;

/**
 * davidwei
 * 消息中心
 */
public final class MessageRequestManager {

    /**
     * 消息中心分类列表
     */
    public static void getMsgClassListRequest(final Map<Integer, Integer> endPushTime, final DataSourceProxy.IRequestCallback observer) {
        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        final AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        long accountId = 0L;
        byte[] ticket = null;
        if (accountInfoEntity != null) {
            accountId = accountInfoEntity.id;
            ticket = accountInfoEntity.ticket;
        }

        final AlertMsgClassListReq req = new AlertMsgClassListReq();
        req.mEndPushTime = endPushTime;

        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.iAccountId = accountId;
        req.stUserInfo = accountManager.getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_MSG_CLASS_LIST, req, observer);
    }

    /**
     * 消息中心，消息详情列表
     */
    public static void getMsgDetailListRequest(final int classId, final int start, final int num, final DataSourceProxy.IRequestCallback observer) {
        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        final AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        long accountId = 0L;
        byte[] ticket = null;
        if (accountInfoEntity != null) {
            accountId = accountInfoEntity.id;
            ticket = accountInfoEntity.ticket;
        }

        final AlertMsgClassDetailReq req = new AlertMsgClassDetailReq();
        req.iClassID = classId;
        req.iStart = start;
        req.iNum = num;

        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.iAccountId = accountId;
        req.stUserInfo = accountManager.getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_MSG_DETAIL_LIST, req, observer);
    }

    /**
     * 消息中心，股价提醒消息列表
     */
    public static void getStockPriceRemindMsgList(final int start, final int num, final DataSourceProxy.IRequestCallback observer) {
        getMsgDetailListRequest(E_MSG_CLASS.E_MC_SEC_PRICE, start, num, observer);
    }

    /**
     * 消息中心，自选日报消息列表
     */
    public static void getPortfolioDailyRemindMsgList(final int start, final int num, final DataSourceProxy.IRequestCallback observer) {
        getMsgDetailListRequest(E_MSG_CLASS.E_MC_DAILY_REPORT, start, num, observer);
    }

    /**
     * 消息中心，新股消息列表
     */
    public static void getNewShareRemindMsgList(final int start, final int num, final DataSourceProxy.IRequestCallback observer) {
        getMsgDetailListRequest(E_MSG_CLASS.E_MC_NEW_STOCK, start, num, observer);
    }

}