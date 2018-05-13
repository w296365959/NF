package com.sscf.investment.social;

import android.text.TextUtils;

import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import java.util.ArrayList;
import BEC.AccountTicket;
import BEC.E_FEED_USER_RELATION_SET_TYPE;
import BEC.E_FEED_USER_RELATION_TYPE;
import BEC.GetFeedUserInfoReq;
import BEC.GetRelationBatchReq;
import BEC.GetUserRelationReq;
import BEC.SetUserRelationReq;

/**
 * Created by davidwei on 2016/09/22
 */
public final class FriendsRequestManager {

    /**
     * 获得关注的列表
     */
    public static void getAttentionsListRequest(final long userId, final String startId, final DataSourceProxy.IRequestCallback observer) {
        getFriendsListRequest(userId, startId, E_FEED_USER_RELATION_TYPE.E_FURT_FOLLOWER, observer);
    }

    /**
     * 获得粉丝的列表
     */
    public static void getFansListRequest(final long userId, final String startId, final DataSourceProxy.IRequestCallback observer) {
        getFriendsListRequest(userId, startId, E_FEED_USER_RELATION_TYPE.E_FURT_FANS, observer);
    }

    /**
     * 获得粉丝的列表
     */
    private static void getFriendsListRequest(final long userId, final String startId, final int type, final DataSourceProxy.IRequestCallback observer) {
        final GetUserRelationReq req = new GetUserRelationReq();
        req.iOtherAccountId = (int) userId;
        req.eFeedUserRelationType = type;
        req.sStartId = startId;
        req.iDirection = TextUtils.isEmpty(startId) ? 0 : 1;

        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        final AccountTicket ticket = new AccountTicket();
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo != null) {
            ticket.vtTicket = accountInfo.ticket;
        }

        req.stAccountTicket = ticket;
        req.stUserInfo = accountManager.getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_FEED_FRIEND_LIST, req, observer, startId);
    }

    /**
     * 查询关注关系
     */
    public static void getRelation(final long accountId, final DataSourceProxy.IRequestCallback observer) {
        GetRelationBatchReq req = new GetRelationBatchReq();
        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        req.setStUserInfo(accountManager.getUserInfo());
        AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfo != null ? accountInfo.ticket : null);
        req.setStAccountTicket(accountTicket);
        ArrayList<Long> accountIds = new ArrayList<>();
        accountIds.add(accountId);
        req.setVAccountId(accountIds);
        DataEngine.getInstance().request(EntityObject.ET_GET_RELATION_BATCH, req, observer);
    }

    /**
     * 添加关注
     */
    public static void attendRequest(final long userId, final DataSourceProxy.IRequestCallback observer) {
        final SetUserRelationReq req = new SetUserRelationReq();
        req.iDstAccountId = userId;
        req.eSetType = E_FEED_USER_RELATION_SET_TYPE.E_FURST_FOLLOW;

        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        final AccountTicket ticket = new AccountTicket();
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo != null) {
            ticket.vtTicket = accountInfo.ticket;
        }

        req.stAccountTicket = ticket;
        req.stUserInfo = accountManager.getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_FEED_SET_ATTENTION, req, observer);
    }

    /**
     * 取消关注
     */
    public static void cancelAttendRequest(final long userId, final DataSourceProxy.IRequestCallback observer) {
        final SetUserRelationReq req = new SetUserRelationReq();
        req.iDstAccountId = userId;
        req.eSetType = E_FEED_USER_RELATION_SET_TYPE.E_FURST_CANCEL_FOLLOW;

        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        final AccountTicket ticket = new AccountTicket();
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo != null) {
            ticket.vtTicket = accountInfo.ticket;
        }

        req.stAccountTicket = ticket;
        req.stUserInfo = accountManager.getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_FEED_SET_ATTENTION, req, observer);
    }

    public static void getFeedUserInfo(final long userId, final DataSourceProxy.IRequestCallback observer) {
        final GetFeedUserInfoReq req = new GetFeedUserInfoReq();

        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        final AccountTicket ticket = new AccountTicket();
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo != null) {
            ticket.vtTicket = accountInfo.ticket;
        }

        req.stAccountTicket = ticket;
        req.stUserInfo = accountManager.getUserInfo();

        req.iOtherAccountId = userId;
        DataEngine.getInstance().request(EntityObject.ET_GET_FEED_USER_INFO, req, observer);
    }
}
