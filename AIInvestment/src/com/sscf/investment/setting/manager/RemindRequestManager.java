package com.sscf.investment.setting.manager;

import android.text.TextUtils;
import BEC.*;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.ProtoManager;
import com.dengtacj.component.entity.RemindedMessageItem;
import com.sscf.investment.utils.DengtaConst;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * davidwei
 *  提醒相关请求
 */
public final class RemindRequestManager {

    public static void getRemindList(final byte[] ticket, final long accountId, final int start, final int num,
                                     final DataSourceProxy.IRequestCallback observer) {
        final AlertMessageListReq req = new AlertMessageListReq();
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.iAccountId = accountId;
        req.iStart = start;
        req.iNum = num;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_REMIND_QUERY_LIST, req, observer);
    }

    public static void clearClassRemindList(final byte[] ticket, final long accountId, final int classId,final DataSourceProxy.IRequestCallback observer) {
        final ClearAlertMessageListReq req = new ClearAlertMessageListReq();
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.iAccountId = accountId;
        req.iClassID = classId;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_REMIND_CLEAR_LIST, req, observer);
    }

    public static void clearRemindList(final byte[] ticket, final long accountId, final DataSourceProxy.IRequestCallback observer) {
        final ClearAlertMessageListReq req = new ClearAlertMessageListReq();
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.iAccountId = accountId;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_REMIND_CLEAR_LIST, req, observer);
    }

    public static void decodePushDataListWithDate(ArrayList<PushData> datas, ArrayList items, HashSet<RemindedMessageItem> itemsSet, int... types) {
        RemindedMessageItem item = null;
        final int size = datas == null ? 0 : datas.size();
        String currentDate = "";
        final int itemSize = items == null ? 0 : items.size();
        if (itemSize > 0) {
            item = (RemindedMessageItem) items.get(itemSize - 1);
            if (item != null) {
                currentDate = item.getPublishDateString();
            }
        }
        String date = null;
        for (int i = 0; i < size; i++) {
            item = decodePushData(datas.get(i));
            if (item != null && !itemsSet.contains(item) && filterMessageItemType(item, types)) {
                date = item.getPublishDateString();
                if (!TextUtils.equals(currentDate, date)) {
                    currentDate = date;
                    items.add(date);
                }
                items.add(item);
                itemsSet.add(item);
            }
        }
    }

    private static boolean filterMessageItemType(RemindedMessageItem item, int... types) {
        final int length = types == null ? 0 : types.length;
        boolean b = false;
        if (length > 0) {
            final int messageType = item.messageType;
            for (int i = 0; i < length; i++) {
                if (types[i] == messageType) {
                    b = true;
                    break;
                }
            }
        } else {
            b = true;
        }
        return b;
    }

    public static RemindedMessageItem decodePushData(final PushData pushData) {
        RemindedMessageItem item = null;
        if (pushData == null) {
            return item;
        }

        switch (pushData.stPushType.ePushDataType) {
            case E_PUSH_DATA_TYPE.E_SEC_REMIND: // 股价提醒
                item = decodeSecRemind(pushData);
                break;
            case E_PUSH_DATA_TYPE.E_NEWS_REMIND: // 重大新闻，新闻，公告，研报，日报
                item = decodeNewsRemind(pushData);
                break;
            case E_PUSH_DATA_TYPE.E_NNT_ACTIVITY: // 活动
            case E_PUSH_DATA_TYPE.E_PDT_NEW_STOCK_REMIND: // 新股提醒
            case E_PUSH_DATA_TYPE.E_VALUE_ADDED_SERVICE: // 增值服务
                item = decodeCommonRemind(pushData);
                break;
            case E_PUSH_DATA_TYPE.E_USER_INTERACTION: // 互动消息
                item = decodeInteractMessage(pushData);
                break;
            case E_PUSH_DATA_TYPE.E_TG_ATTITUDE: // 投顾观点
                item = decodeInvestmentAdviserEvents(pushData);
                break;
            default:
                break;
        }
        return item;
    }

    public static RemindedMessageItem decodeInteractMessage(final PushData pushData) {
        final InteractionNotify notify = new InteractionNotify();
        ProtoManager.decode(notify, pushData.vtData);
        final RemindedMessageItem item = new RemindedMessageItem();
        item.id = pushData.stPushType.sMsgId;
        item.nickname = notify.sUserName;
        item.content = pushData.sDescription;
        item.publishTime = DengtaConst.MILLIS_FOR_SECOND * pushData.iPushTime;
        item.messageType = RemindedMessageItem.TYPE_INTERACT_MESSAGE;
        item.accountId = notify.iAccountId;
        item.newsId = notify.sFeedId;
        item.userIconUrl = notify.sIcon;
        item.userType = notify.bVerify;
        item.message = notify.sMsg;
        item.feedType = notify.iFeedType;
        return item;
    }

    public static RemindedMessageItem decodeInvestmentAdviserEvents(final PushData pushData) {
        final TgAttitudeNotify notify = new TgAttitudeNotify();
        ProtoManager.decode(notify, pushData.vtData);
        final RemindedMessageItem item = new RemindedMessageItem();
        item.id = pushData.stPushType.sMsgId;
        item.nickname = notify.sUserName;
        item.content = pushData.sDescription;
        item.publishTime = DengtaConst.MILLIS_FOR_SECOND * pushData.iPushTime;
        item.messageType = RemindedMessageItem.TYPE_INVESTMENT_ADVISER_EVENTS;
        item.accountId = notify.iAccountId;
        item.newsId = notify.sFeedId;
        item.userIconUrl = notify.sIcon;
        item.userType = notify.bVerify;
        item.message = notify.sNotifyMsg;
        item.feedType = notify.iFeedType;
        return item;
    }

    public static RemindedMessageItem decodeNewsRemind(final PushData pushData) {
        RemindedMessageItem item = null;

        final NewsNotify newsNotify = new NewsNotify();
        ProtoManager.decode(newsNotify, pushData.vtData);

        int messageType = 0;

        if (pushData.stControl != null && pushData.stControl.iRealPushType == E_PUSH_DATA_TYPE.E_PDT_DAILY_REPORT) {
            messageType = RemindedMessageItem.TYPE_DAILY_REPORT;
        } else {
            if (newsNotify.eNotifyNewType == E_NOTIFY_NEW_TYPE.E_NNT_IMPORTANT) {
                // 重大新闻
                messageType = RemindedMessageItem.TYPE_IMPORTANT_NEWS;
            } else {
                // 新闻公告提醒
                switch (newsNotify.eNewsType) {
                    case E_NEWS_TYPE.NT_ANNOUNCEMENT:
                        messageType = RemindedMessageItem.TYPE_ANNOUNCEMENT;
                        break;
                    case E_NEWS_TYPE.NT_REPORT:
                        messageType = RemindedMessageItem.TYPE_RESEARCH;
                        break;
                    default:
                        return item;
                }
            }
        }

        item = new RemindedMessageItem(pushData.stPushType.sMsgId, newsNotify.sTitle, newsNotify.sNotifyMsg,
                DengtaConst.MILLIS_FOR_SECOND * pushData.iPushTime,
                messageType, newsNotify.sDtSecCode, newsNotify.sDtInfoUrl, newsNotify.sNewsID, newsNotify.eNewsType);
        item.tagInfos = newsNotify.vtTagInfo;
        return item;
    }

    public static RemindedMessageItem decodeSecRemind(final PushData pushData) {
        final SecNotify secNotify = new SecNotify();
        ProtoManager.decode(secNotify, pushData.vtData);
        final SecInfo secInfo = secNotify.stSecInfo;
        if (secInfo == null) {
            return null;
        }
        return new RemindedMessageItem(pushData.stPushType.sMsgId, secInfo.sCHNShortName,
                secNotify.sNotifyMsg, DengtaConst.MILLIS_FOR_SECOND * pushData.iPushTime, RemindedMessageItem.TYPE_SEC, secInfo.sDtSecCode);
    }

    public static RemindedMessageItem decodeCommonRemind(final PushData pushData) {
        final ActivityNotify activityNotify = new ActivityNotify();
        ProtoManager.decode(activityNotify, pushData.vtData);
        int messageType = 0;
        switch (pushData.stPushType.ePushDataType) {
            case E_PUSH_DATA_TYPE.E_NNT_ACTIVITY: // 活动
                messageType = RemindedMessageItem.TYPE_ACTIVITY;
                break;
            case E_PUSH_DATA_TYPE.E_PDT_NEW_STOCK_REMIND: // 新股提醒
                messageType = RemindedMessageItem.TYPE_NEW_SHARE;
                break;
            case E_PUSH_DATA_TYPE.E_VALUE_ADDED_SERVICE:
                messageType = RemindedMessageItem.TYPE_VALUEADDED_SERVICES;
                break;
            default:
                return null;
        }
        final RemindedMessageItem item = new RemindedMessageItem(pushData.stPushType.sMsgId, activityNotify.sTitle,
                activityNotify.sMsg, DengtaConst.MILLIS_FOR_SECOND * pushData.iPushTime, messageType);
        item.infoUrl = activityNotify.sUrl;
        return item;
    }
}