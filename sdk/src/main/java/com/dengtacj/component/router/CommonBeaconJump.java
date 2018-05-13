package com.dengtacj.component.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import com.chenenyu.router.IRouter;
import com.chenenyu.router.Router;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.payment.OrderInfo;
import com.dengtacj.component.managers.IAccountManager;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.NumberUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.web.CommonWebConst;
import java.util.ArrayList;
import BEC.E_NEMS_GET_SOURCE;
import BEC.E_NEWS_TYPE;
import BEC.ReplyCommentInfo;
import BEC.UserRiskEvalResult;

/**
 * Created by yorkeehuang on 2017/6/8.
 */

public class CommonBeaconJump {
    private static final String TAG = CommonBeaconJump.class.getSimpleName();

    public static Intent createIntent(final Context context, final Class cls) {
        final Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    public static void showActivity(final Context context, final Class cls) {
        context.startActivity(createIntent(context, cls));
    }

    private static void showNewTaskActivity(Context context, String routerName, Bundle bundle) {
        try {
            Router.build(routerName).with(bundle).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showActivity(Context context, String routerName, Bundle bundle) {
        try {
            IRouter router = Router.build(routerName).with(bundle);
            if (!(context instanceof Activity)) {
                router = router.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            router.go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showActivity(Context context, String routerName) {
        IRouter router = Router.build(routerName);
        if (!(context instanceof Activity)) {
            router = router.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        router.go(context);
    }

    public static void showActivityForResult(final Context context, String routerName, Bundle bundle, final int requestCode) {
        try {
            IRouter router = Router.build(routerName).requestCode(requestCode);
            if(bundle != null) {
                router.with(CommonConst.EXTRA_SEARCH_PICK, bundle);
            }
            router.go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showCommonWebActivity(final Context context, final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebConst.URL_ADDR, url);
            Router.build("CommonWebActivity").with(bundle).go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showCommonWebActivityForResult(final Context context, final int requestCode, final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebConst.URL_ADDR, url);
            Router.build("CommonWebActivity").requestCode(requestCode).with(bundle).go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showCommonUntransparentWebActivity(final Context context, final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebConst.URL_ADDR, url);
            Router.build("CommonUnTransparentWebActivity").with(bundle).go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSearch(final Context context) {
        showActivity(context, "SearchActivity");
    }

    public static void showSearchPicker(final Context context, Bundle bundle, final int requestCode) {
        showActivityForResult(context, "SearchPickerActivity", bundle, requestCode);
    }

    public static void showFavorActivity(final Context context) {
        showActivity(context, "FavorActivity");
    }

    public static void showExchangePrivilegeActivity(final Context context) {
        showActivity(context, "ExchangePrivilegeActivity");
    }

    public static void showCommitInviteCodeActivity(final Context context) {
        showActivity(context, "CommitInviteCodeActivity");
    }

    public static void showHomepage(final Context context, long accountId) {
        try {
            Bundle bundle = new Bundle();
            bundle.putLong(CommonConst.EXTRA_ACCOUNT_ID, accountId);
            showNewTaskActivity(context,  "HomepageActivity", bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showCommentList(final Context context, String dtSecCode, String secName) {
        final Bundle bundle = new Bundle();
        bundle.putString(CommonConst.KEY_SEC_CODE, dtSecCode);
        bundle.putString(CommonConst.KEY_SEC_NAME, secName);
        showActivity(context, "CommentListActivity", bundle);
    }

    public static void showLogin(final Context context) {
        showActivity(context, "LoginActivity");
    }

    public static void showMessageCenter(final Context context) {
        showActivity(context, "MessageCenterActivity");
    }

    /**
     * 提醒列表，修改进入消息中心
     */
    @Deprecated
    public static void showRemindList(final Context context) {
        showMessageCenter(context);
    }

    public static void showCapitalFlow(final Context context) {
        showActivity(context, "CapitalFlowActivity");
    }

    public static void showStockListInPlate(final Context context, final String dtSecCode, final String name) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonConst.KEY_SEC_CODE, dtSecCode);
            bundle.putString(CommonConst.KEY_SEC_NAME, name);
            showActivity(context, "StockListInPlateActivity", bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showBonusPoints(final Context context) {
        showActivity(context, "BonusPointsActivity");
    }

    public static void showTeacherYanAudioPlayer(final Context context, int audioId) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.EXTRA_PLAY_AUDIO, audioId);
        showActivity(context, "TeacherYanAudioPlayerActivity", bundle);
    }

    public static void showOpenPrivilege(final Context context, int[] types) {
        try {
            Bundle bundle = new Bundle();
            bundle.putIntArray(CommonConst.EXTRA_PRIVILEGE_TYPE, types);
            showActivity(context, "OpenPrivilegeActivity", bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAttentionList(final Context context, long accountId) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(CommonConst.EXTRA_TYPE, CommonConst.TYPE_ATTENTION);
            bundle.putLong(CommonConst.EXTRA_ID, accountId);
            showActivity(context, "FriendsActivity", bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showFansList(final Context context, long accountId) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(CommonConst.EXTRA_TYPE, CommonConst.TYPE_FANS);
            bundle.putLong(CommonConst.EXTRA_ID, accountId);
            showActivity(context, "FriendsActivity", bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showInvestmentAdviserList(final Context context) {
        showActivity(context, "InvestmentAdviserListActivity");
    }

    public static void showModifyPassword(final Context context) {
        showActivity(context, "ModifyPasswordActivity");
    }

    public static void showOpenMember(final Context context) {
        StatisticsUtil.reportAction(StatisticsConst.OPEN_MEMBER_ACTIVITY_DISPLAY);
        showActivity(context, "OpenMemberActivity");
    }

    public static void showUpgrade(final Context context) {
        showActivity(context, "UpgradeActivity");
    }

    public static void showKLineSetting(final Context context) {
        showActivity(context, "SettingKLineActivity");
    }

    public static void showBindCellphone(final Context context, boolean showClose) {
        final Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.EXTRA_FUNCTION_TYPE, CommonConst.FUNCTION_BIND_CELLPHONE);
        bundle.putBoolean(CommonConst.EXTRA_SHOW_CLOSE, showClose);
        showActivity(context, "VerifySmsCodeActivity", bundle);
    }


    public static void showBindCellphone(final Context context) {
        showBindCellphone(context, false);
    }

    public static boolean showSecurityDetailActivity(final Context context, final String dtCode, final String secName) {
        return showSecurityDetailActivity(context, dtCode, secName, null);
    }

    public static boolean showSecurityDetailActivity(final Context context, final String dtCode, final String secName, ArrayList<SecListItem> secItems) {
        if (TextUtils.isEmpty(dtCode)) {
            return false;
        }

        if (isFrequentOperation()) {
            return false;
        }

        if (secItems == null || secItems.size() == 0) {
            secItems = new ArrayList<>(1);
            SecListItem secItem = new SecListItem();
            secItem.setDtSecCode(dtCode);
            secItem.setName(secName);
            secItems.add(secItem);
        }

        final Bundle bundle = new Bundle();
        bundle.putString(CommonConst.KEY_SEC_CODE, dtCode);
        bundle.putString(CommonConst.KEY_SEC_NAME, secName);
        bundle.putParcelableArrayList(CommonConst.KEY_SEC_LIST, secItems);
        showActivity(context, "SecurityDetailActivity", bundle);
        return true;
    }

    private static long lastOperationTime;
    private static boolean isFrequentOperation() {
        final long now = SystemClock.elapsedRealtime();
        final long gap = now - lastOperationTime;
        if (gap < 2000) { //两秒之内禁止二次打开详情页，避免华为手机上启动太慢导致重叠的问题
            return true;
        } else{
            lastOperationTime = now;
            return false;
        }
    }

    public static void showArticleList(final Context context, String secCode,
                                       String secName, String type) {
        final Bundle bundle = new Bundle();
        bundle.putString(CommonConst.KEY_SEC_CODE, secCode);
        bundle.putString(CommonConst.KEY_SEC_NAME, secName);
        bundle.putInt(CommonConst.KEY_NEWS_TYPE, NumberUtil.parseInt(type, E_NEWS_TYPE.NT_NEWS));
        bundle.putInt(CommonConst.KEY_GET_SOURCE, E_NEMS_GET_SOURCE._E_STOCK_NEWSLIST_GET);
        showActivity(context, "ArticleListActivity", bundle);
    }

    public static void showCapitalFlowStockListInPlate(Context context, String secCode,
                                                       String secName, int sourType) {
        final Bundle bundle = new Bundle();
        bundle.putString(CommonConst.KEY_SEC_CODE, secCode);
        bundle.putString(CommonConst.KEY_SEC_NAME, secName);
        bundle.putInt("extra_sort_type", sourType);
        showActivity(context, "CapitalFlowStockListInPlateActivity", bundle);
    }

    public static void showExchangePrivilege(final Context context) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager != null && accountManager.isLogined()) {
            showExchangePrivilegeActivity(context);
        } else {
            showLogin(context);
        }
    }

    public static void showCommitInviteCode(final Context context) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager != null && accountManager.isLogined()) {
            showCommitInviteCodeActivity(context);
        } else {
            showLogin(context);
        }
    }


    public static void showFavor(final Context context) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager != null && accountManager.isLogined()) {
            showFavorActivity(context);
        } else {
            showLogin(context);
        }
    }


    public static void showCommentEditpage(final Context context, String dtSecCode, String secName, String feedId, int feedType, long replyAccountId, String replyCommentId, String replyNickName) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        final AccountInfoEntity accountInfo = accountManager == null ? null : accountManager.getAccountInfo();
        if (accountInfo != null) {
            if (TextUtils.isEmpty(accountInfo.cellphone)) {
                showBindCellphone(context);
            } else {
                final Bundle bundle = new Bundle();
                bundle.putString(CommonConst.KEY_SEC_NAME, secName);
                bundle.putString(CommonConst.KEY_SEC_CODE, dtSecCode);
                bundle.putString(CommonConst.KEY_FEED_ID, feedId);
                bundle.putInt(CommonConst.KEY_FEED_TYPE, feedType);
                final ReplyCommentInfo replyCommentInfo = new ReplyCommentInfo(replyCommentId, replyAccountId, replyNickName);
                bundle.putSerializable(CommonConst.KEY_REPLY_COMMENT_INFO, replyCommentInfo);
                showActivity(context, "CommentEditActivity", bundle);
            }
        } else {
            showLogin(context);
        }
    }

    public static void showPayOrder(final Context context, final String orderId, final String orderName, final int orderAmount, final int orderType, final int thirdPaySource, final int h5OpenType) {
        showPayOrder(context, new OrderInfo(orderId, orderName, orderAmount, orderType, thirdPaySource, h5OpenType), null);
    }

    public static void showPayOrder(final Context context, final OrderInfo order, final UserRiskEvalResult riskEval) {
        showPayOrder(context, order, riskEval, 0);
    }

    public static void showPayOrder(final Context context, final OrderInfo order, final UserRiskEvalResult riskEval, final int needSign) {
        final Bundle bundle = new Bundle();
        bundle.putSerializable(CommonConst.EXTRA_ORDER_INFO, order);
        bundle.putSerializable(CommonConst.EXTRA_RISK_EVAL, riskEval);
        bundle.putInt(CommonConst.EXTRA_NEED_SIGN, needSign);
        showActivity(context, "PayOrderActivity", bundle);
    }

}
