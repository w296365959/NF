package com.sscf.investment.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.MainActivity;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.StatManager;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.dengtacj.component.entity.db.FavorItem;
import com.dengtacj.component.entity.RemindedMessageItem;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.web.CommonWebConst;
import com.dengtacj.component.router.WebBeaconJump;
import BEC.BEACON_STAT_TYPE;

/**
 * Created by davidwei on 2015/9/2.
 *
 * push管理
 */
public final class PushClickReceiver extends BroadcastReceiver implements DataSourceProxy.IRequestCallback {
    private static final String TAG = PushManager.TAG;
    public static final String ACTION_SHOW_SECURITY_DETAIL_PAGE = "action_show_security_detail_page";
    public static final String ACTION_SHOW_WEB_PAGE = "action_show_web_page";
    public static final String ACTION_SHOW_ACTIVITIES = "action_show_activities";

    @Override
    public void onReceive(Context context, Intent intent) {
        DtLog.d(TAG, "PushClickReceiver onReceive intent : " + intent);
        RemindedMessageItem item = null;
        try {
            item = (RemindedMessageItem) intent.getSerializableExtra(DengtaConst.EXTRA_PUSH_REMIND);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (item != null) {
            handleRemindClick(item, intent.getStringExtra(DengtaConst.EXTRA_FROM));
        } else { // 如果item没有，统一打开MainActivity
            DengtaApplication.getApplication().startMainActivity(null);
        }
    }

    private void startMainActivityToShowWebNews(String url, FavorItem favorItem) {
        DtLog.d(TAG, "PushClickReceiver startMainActivityToShowWebNews url : " + url);
        if (DengtaApplication.getApplication().isMainActivityInited()) {
            WebBeaconJump.showWebActivity(DengtaApplication.getApplication(), url, favorItem, Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            final Intent intent = new Intent(DengtaApplication.getApplication(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(ACTION_SHOW_WEB_PAGE);
            intent.putExtra(CommonWebConst.URL_ADDR, url);
            intent.putExtra(CommonWebConst.EXTRA_NEWS, favorItem);
            DengtaApplication.getApplication().startMainActivity(intent);
        }
    }

    private void startMainActivityToShowSecurityDetail(String dtSecCode, String secName) {
        if (DengtaApplication.getApplication().isMainActivityInited()) {
            CommonBeaconJump.showSecurityDetailActivity(DengtaApplication.getApplication(), dtSecCode, secName);
        } else {
            Intent intent = new Intent(DengtaApplication.getApplication(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(ACTION_SHOW_SECURITY_DETAIL_PAGE);
            intent.putExtra(DengtaConst.KEY_SEC_CODE, dtSecCode);
            intent.putExtra(DengtaConst.KEY_SEC_NAME, secName);
            DengtaApplication.getApplication().startMainActivity(intent);
        }
    }

    public boolean handleRemindClick(final RemindedMessageItem item, final String from) {
        DtLog.d(TAG, "PushClickReceiver handleRemindClick item : " + item);
        String key = "";
        switch (item.messageType) {
            case RemindedMessageItem.TYPE_SEC:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_NOTIFY_BAR, StatConsts.SEC_PRICE);
                startMainActivityToShowSecurityDetail(item.dtCode, item.title);
                key = StatisticsConst.NOTIFICATION_CLICK_SEC;
                break;
            case RemindedMessageItem.TYPE_ANNOUNCEMENT:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_NOTIFY_BAR, StatConsts.ANNOUNCEMENT);
                showNews(item);
                key = StatisticsConst.NOTIFICATION_CLICK_ANNOUNCEMENT;
                break;
            case RemindedMessageItem.TYPE_RESEARCH:

                showNews(item);
                key = StatisticsConst.NOTIFICATION_CLICK_RESEARCH;
                break;
            case RemindedMessageItem.TYPE_IMPORTANT_NEWS:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_NOTIFY_BAR, StatConsts.DISC_NEWS);
                final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
                long accountId = 0;
                if (accountInfo != null) {
                    accountId = accountInfo.id;
                }

                final FavorItem favorItem = new FavorItem(accountId, item);

                startMainActivityToShowWebNews(item.infoUrl, favorItem);

                key = StatisticsConst.NOTIFICATION_CLICK_IMPORTANT_NEWS;
                break;
            case RemindedMessageItem.TYPE_ACTIVITY:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_NOTIFY_BAR, StatConsts.ACTIVITY);
                showActivity(item.infoUrl);
                key = StatisticsConst.NOTIFICATION_CLICK_ACTIVITY;
                break;
            case RemindedMessageItem.TYPE_NEW_SHARE:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_NOTIFY_BAR, StatConsts.NEW_STOCK);
                showActivity(item.infoUrl);
                key = StatisticsConst.NOTIFICATION_CLICK_NEW_SHARE;
                break;
            case RemindedMessageItem.TYPE_DAILY_REPORT:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_NOTIFY_BAR, StatConsts.DAILY_REPORT);
                showActivity(item.infoUrl);
                key = StatisticsConst.NOTIFICATION_CLICK_DAILY_REPORT;
                break;
            case RemindedMessageItem.TYPE_INTERACT_MESSAGE:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_NOTIFY_BAR, StatConsts.INTERACTION);
                showActivity(DengtaApplication.getApplication().getUrlManager().getCommentDetailUrl(item.newsId));
                key = StatisticsConst.NOTIFICATION_CLICK_INTERACT_MESSAGE;
                break;
            case RemindedMessageItem.TYPE_INVESTMENT_ADVISER_EVENTS:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_NOTIFY_BAR, StatConsts.CONSULTANT);
                showActivity(DengtaApplication.getApplication().getUrlManager().getOpinionDetailUrl(item.newsId));
                key = StatisticsConst.NOTIFICATION_CLICK_INVESTMENT_ADVISER_EVENTS;
                break;
            case RemindedMessageItem.TYPE_VALUEADDED_SERVICES:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_NOTIFY_BAR, StatConsts.VALUE_ADDED_SERVICE);
                showActivity(item.infoUrl);
                break;
            default:
                break;
        }

        if (!TextUtils.isEmpty(key)) {
            StatisticsUtil.reportAction(key);
        }

        final StringBuilder pushKey = new StringBuilder(from);
        pushKey.append('_').append(key).append(':').append(item.id);
        PushManager.report(pushKey.toString(), this);

        // 去除未读标记
        DengtaApplication.getApplication().getRemindDataManager().removeUnreadRemind(item.id);

        return true;
    }

    private void showActivity(final String url) {
        DtLog.d(TAG, "showActivity url=" + url);
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        if (dengtaApplication.isMainActivityInited()) {
            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            if (scheme != null) {
                scheme.handleUrl(dengtaApplication, url);
            }
        } else {
            Intent intent = new Intent(dengtaApplication, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(ACTION_SHOW_ACTIVITIES);
            intent.putExtra(CommonWebConst.URL_ADDR, url);
            dengtaApplication.startMainActivity(intent);
        }
    }

    private void showNews(final RemindedMessageItem item) {
        final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
        long accountId = 0;
        if (accountInfo != null) {
            accountId = accountInfo.id;
        }

        final FavorItem favorItem = new FavorItem(accountId, item);

        startMainActivityToShowWebNews(item.infoUrl, favorItem);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
    }
}
