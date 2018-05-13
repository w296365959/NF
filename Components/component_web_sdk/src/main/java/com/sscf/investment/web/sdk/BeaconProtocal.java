package com.sscf.investment.web.sdk;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.dengtacj.component.router.BeaconJump;
import com.dengtacj.component.router.ScanJump;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.sdk.utils.BaseStockUtil;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.NumberUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.tencent.smtt.sdk.TbsVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by davidwei on 2016/07/27
 * 方法定义参考
 * public static void xxx(final Context context, final Uri uri) {}
 * 方法名为beacon://xxx?里的host
 */
public final class BeaconProtocal {

    private static final String KEY_ID = "id";
    private static final String KEY_VIDEO_KEY = "videoKey";
    private static final String KEY_VIDEO_TYPE = "videoType";
    private static final String KEY_NAME = "name";
    private static final String KEY_VIDEO_SOURCE = "source";
    // 证券列表（由code和name的列表组成，形如“000001@平安银行|000002@万科A”）
    public static final String TAG_SORT_TYPE = "sort_type";

    private static JSONObject getParams(final Uri uri) {
        final String json = uri.getQueryParameter("body");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    // ----------老协议----------
    /**
     * 股票详情页面
     */
    public static void stock(final Context context, final Uri uri) {
        final String secCode = uri.getQueryParameter(KEY_ID);
        final String secName = uri.getQueryParameter(KEY_NAME);
        final String secListStr = uri.getQueryParameter(CommonConst.KEY_SEC_LIST);
        ArrayList<SecListItem> secListItems = null;
        if (!TextUtils.isEmpty(secListStr)) {
            final String[] list = secListStr.split("\\|");
            final int length = list == null ? 0 : list.length;
            if (length > 0) {
                secListItems = new ArrayList<>();
                for (String itemStr : list) {
                    String[] strings = itemStr.split("@");
                    SecListItem item = new SecListItem();
                    item.setDtSecCode(strings[0]);
                    item.setName(strings[1]);
                    secListItems.add(item);
                }
            }
        }

        CommonBeaconJump.showSecurityDetailActivity(context, secCode, secName, secListItems);
    }

    /**
     * 股票新闻列表页面
     */
    public static void secNewsList(final Context context, final Uri uri) {
        final String secCode = uri.getQueryParameter(KEY_ID);
        final String secName = uri.getQueryParameter(KEY_NAME);
        final String type = uri.getQueryParameter("type");

        CommonBeaconJump.showArticleList(context, secCode, secName, type);
    }

    /**
     * 板块关联个股的列表
     */
    public static void stockListInPlate(final Context context, final Uri uri) {
        final String secCode = uri.getQueryParameter(KEY_ID);
        final String secName = uri.getQueryParameter(KEY_NAME);
        CommonBeaconJump.showStockListInPlate(context, secCode, secName);
    }

    /**
     * 板块关联个股资金流的列表
     */
    public static void capitalFlowStockListInPlate(final Context context, final Uri uri) {
        final String secCode = uri.getQueryParameter(KEY_ID);
        final String secName = uri.getQueryParameter(KEY_NAME);
        final String sortType = uri.getQueryParameter(TAG_SORT_TYPE);
        CommonBeaconJump.showCapitalFlowStockListInPlate(context, secCode, secName,  NumberUtil.parseInt(sortType, 1));
    }
    // ----------老协议----------

    // ----------首页----------
    /**
     * 首页自选
     */
    public static void optional(final Context context, final Uri uri) {
    }

    /**
     * 首页行情
     */
    public static void quotation(final Context context, final Uri uri) {
    }

    /**
     * 首页发现
     */
    public static void discover(final Context context, final Uri uri) {
    }

    /**
     * 首页市场情报
     */
    public static void news(final Context context, final Uri uri) {
    }

    /**
     * 首页我的
     */
    public static void me(final Context context, final Uri uri) {
    }
    // ----------首页----------

    // ----------首页入口----------
    /**
     * 搜索
     */
    public static void search(final Context context, final Uri uri) {
        CommonBeaconJump.showSearch(context);
    }

    /**
     * 表哥
     */
    public static void brother(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String words = params.optString("words");
        final String realWords = params.optString("realWords");
        WebBeaconJump.showIntelligentAnswer(context, realWords, words);
    }
    // ----------首页入口----------

    // ----------我的----------
    /**
     * 登录
     */
    public static void login(final Context context, final Uri uri) {
        CommonBeaconJump.showLogin(context);
    }

    /**
     * 提醒列表
     */
    public static void remind(final Context context, final Uri uri) {
        CommonBeaconJump.showRemindList(context);
    }

    /**
     * 扫一扫
     */
    public static void scan(final Context context, final Uri uri) {
        if (context instanceof Activity) {
            ScanJump.showScan((Activity) context);
        }
    }

    /**
     * 收藏
     */
    public static void collect(final Context context, final Uri uri) {
        CommonBeaconJump.showFavor(context);
    }
    // ----------我的----------

    // ----------个股详情里----------

    /**
     * 个股直播
     */
    public static void stockLive(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String secCode = params.optString(CommonConst.KEY_SEC_CODE);
        final String secName = params.optString(CommonConst.KEY_SEC_NAME);
        WebBeaconJump.showStockLive(context, secCode, secName);
    }

    /**
     * 个股画像
     */
    public static void stockPortrait(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String secCode = params.optString(CommonConst.KEY_SEC_CODE);
        final String secName = params.optString(CommonConst.KEY_SEC_NAME);
        WebBeaconJump.showStockPortrait(context, secCode, secName);
    }

    /**
     * 个股大事件提醒
     */
    public static void bigEventRemind(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String secCode = params.optString(CommonConst.KEY_SEC_CODE);
        final String secName = params.optString(CommonConst.KEY_SEC_NAME);
        WebBeaconJump.showStockBigEventRemind(context, secCode, secName);
    }

    /**
     * 个股相似k线
     */
    public static void similarKlineDetail(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String secCode = params.optString(CommonConst.KEY_SEC_CODE);
        final String secName = params.optString(CommonConst.KEY_SEC_NAME);
        WebBeaconJump.showSimilarKLine(context, secCode, secName);
    }

    /**
     * 个股历史回看
     */
    public static void historyDetail(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String secCode = params.optString(CommonConst.KEY_SEC_CODE);
        final String secName = params.optString(CommonConst.KEY_SEC_NAME);
        WebBeaconJump.showSecHistory(context, secCode, secName);
    }

    /**
     * 个股筹码分布
     */
    public static void gamblingChipDetail(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String secCode = params.optString(CommonConst.KEY_SEC_CODE);
        final String secName = params.optString(CommonConst.KEY_SEC_NAME);
        WebBeaconJump.showCYQ(context, secCode, secName);
    }

    /**
     * 个股私有化追踪
     */
    public static void privateTrackDetail(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String secCode = params.optString(CommonConst.KEY_SEC_CODE);
        final String secName = params.optString(CommonConst.KEY_SEC_NAME);
        WebBeaconJump.showPrivatizationTrackingDetail(context, secCode, secName);
    }

    /**
     * 个股定增
     */
    public static void privatePlacementDetail(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String secCode = params.optString(CommonConst.KEY_SEC_CODE);
        final String secName = params.optString(CommonConst.KEY_SEC_NAME);
        WebBeaconJump.showDirectionAddDetail(context, secCode, secName);
    }

    /**
     * 个股停复牌详情页
     */
    public static void suspentionDetail(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String secCode = params.optString(CommonConst.KEY_SEC_CODE);
        final String secName = params.optString(CommonConst.KEY_SEC_NAME);
        WebBeaconJump.showSuspensionDetail(context, secCode, secName);
    }

    /**
     * 个股ah溢价详情
     */
    public static void ahDetail(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String secCodeA = params.optString("seccodeA");
        final String secNameA = params.optString("secnameA");
        final String secCodeH = params.optString("seccodeH");
        final String secNameH = params.optString("secnameH");
        WebBeaconJump.showAhPremiumDetail(context, secCodeA, secNameA, secCodeH, secNameH);
    }
    // ----------个股详情里----------
    // ----------行情----------
    /**
     * 大盘预警
     */
    public static void marketWarning(final Context context, final Uri uri) {
        WebBeaconJump.showMarketWarning(context);
    }

    /**
     * 资金流
     */
    public static void capitalFlows(final Context context, final Uri uri) {
        CommonBeaconJump.showCapitalFlow(context);
    }

    /**
     * 板块列表
     */
    public static void plateList(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        WebBeaconJump.showPlateList(context, params.optString("ranktype"));
    }
    // ----------行情----------

    // ----------小工具区----------
    /**
     * 相似k线
     */
    public static void similarKline(final Context context, final Uri uri) {
        WebBeaconJump.showSimilarKLine(context, "", "");
    }

    /**
     * 历史回看
     */
    public static void history(final Context context, final Uri uri) {
        WebBeaconJump.showSecHistory(context, "", "");
    }

    /**
     * 新股中心
     */
    public static void newStock(final Context context, final Uri uri) {
        WebBeaconJump.showNewShare(context);
    }
    /**
     * 龙虎榜
     */
    public static void lhb(final Context context, final Uri uri) {
        WebBeaconJump.showDragonTigerBillboard(context);
    }

    /**
     * 灯塔直播
     */
    public static void dtLive(final Context context, final Uri uri) {
        WebBeaconJump.showDtLive(context);
    }

    /**
     * 筹码分布
     */
    public static void gamblingChip(final Context context, final Uri uri) {
        WebBeaconJump.showCYQ(context, "", "");
    }

    /**
     * 私有化追踪列表
     */
    public static void privateTrack(final Context context, final Uri uri) {
        WebBeaconJump.showPrivatizationTrackingList(context);
    }

    /**
     * 定向增发列表
     */
    public static void privatePlacement(final Context context, final Uri uri) {
        WebBeaconJump.showDirectionAddListList(context);
    }

    // ----------小工具区----------

    // ----------直接跳转到h5----------
    /**
     * 龙虎榜单明细
     */
    public static void lhbDetail(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        WebBeaconJump.showCommonWebActivity(context, params.optString("url"));
    }

    /**
     * 新股中心 申购详情
     */
    public static void newStockDetail(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        WebBeaconJump.showCommonWebActivity(context, params.optString("url"));
    }

    /**
     * 策略榜单
     */
    public static void recommend(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        WebBeaconJump.showCommonWebActivity(context, params.optString("url"));
    }

    /**
     * 策略详情
     */
    public static void recommendDetail(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        WebBeaconJump.showCommonWebActivity(context, params.optString("url"));
    }

    /**
     * 文章内容页
     */
    public static void newsDetail(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        WebBeaconJump.showWebActivity(context, params.optString("url"), WebConst.WT_NEWS);
    }

    /**
     * 文章专题
     */
    public static void specialSubject(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        WebBeaconJump.showCommonWebActivity(context, params.optString("url"));
    }

    /**
     * 意见反馈
     */
    public static void feedback(final Context context, final Uri uri) {
        WebBeaconJump.showFeedback(context);
    }

    /**
     * 活动
     */
    public static void activity(final Context context, final Uri uri) {
        WebBeaconJump.showActivities(context);
    }

    // ----------直接跳转到h5----------

    // ----------2.0.0 新增接口----------
    /**
     * 打开个人主页
     */
    public static void homepage(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String accountIdStr = params.optString(CommonConst.EXTRA_ACCOUNT_ID);
        long accountId = NumberUtil.parseLong(accountIdStr, 0);
        CommonBeaconJump.showHomepage(context, accountId);
    }

    public static void playVideo(final Context context, final Uri uri) {
        String sources = uri.getQueryParameter(KEY_VIDEO_SOURCE);
        TbsVideo.openVideo(context, sources);
    }

    /**
     * 打开评论编辑页面
     */
    public static void commentEdit(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String dtSecCode = params.optString(CommonConst.KEY_SEC_CODE);
        final String secName = params.optString(CommonConst.KEY_SEC_NAME);
        final String feedId = params.optString(CommonConst.KEY_FEED_ID);
        final int feedType = params.optInt(CommonConst.KEY_FEED_TYPE);
        final String replyAccountIdStr = params.optString(CommonConst.KEY_REPLY_ACCOUNT_ID);
        final String replyCommentId = params.optString(CommonConst.KEY_REPLY_COMMENT_ID);
        final String replyNickName = params.optString(CommonConst.KEY_REPLY_NICK_NAME);

        long replyAccountId = Long.parseLong(replyAccountIdStr);
        CommonBeaconJump.showCommentEditpage(context, dtSecCode, secName, feedId, feedType, replyAccountId, replyCommentId, replyNickName);
    }
    // ----------2.0.0 新增接口----------

    // ----------2.0.1 新增接口----------
    /**
     * 消息中心
     */
    public static void messageCenter(final Context context, final Uri uri) {
        CommonBeaconJump.showMessageCenter(context);
    }

    /**
     * 评论列表
     */
    public static void commentList(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String dtSecCode = params.optString(CommonConst.KEY_SEC_CODE);
        final String secName = params.optString(CommonConst.KEY_SEC_NAME);
        CommonBeaconJump.showCommentList(context, dtSecCode, secName);
    }
    // ----------2.0.1 新增接口----------

    // ----------2.1.0 新增接口----------
    public static void exchangePrivilege(final Context context, final Uri uri) {
        CommonBeaconJump.showExchangePrivilege(context);
    }

    public static void commitInviteCode(final Context context, final Uri uri) {
        CommonBeaconJump.showCommitInviteCode(context);
    }

    /**
     * 积分兑换
     */
    public static void accuPointExchange(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        int[] types = null;
        try {
            JSONArray array = params.optJSONArray(CommonConst.KEY_PRIVILEGE_TYPE);
            if(array != null) {
                int len = array.length();
                if(len > 0) {
                    types = new int[array.length()];
                    for(int i=0; i<len; i++) {
                        Object obj = array.get(i);
                        Integer type = Integer.valueOf(obj.toString());
                        types[i] = type;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CommonBeaconJump.showOpenPrivilege(context, types);
    }
    // ----------2.1.0 新增接口----------

    // ----------2.2.0 新增接口----------
    public static void bindCellphone(final Context context, final Uri uri) {
        CommonBeaconJump.showBindCellphone(context);
    }
    // ----------2.2.0 新增接口----------


    // ----------2.3.0 新增接口----------
    public static void openMember(final Context context, final Uri uri) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager != null && accountManager.isLogined()) {
            CommonBeaconJump.showOpenMember(context);
        } else {
            CommonBeaconJump.showLogin(context);
        }
    }

    public static void payOrder(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final String orderId = params.optString("orderId");
        final String orderName = params.optString("orderName");
        final int orderAmount = params.optInt("orderAmount");
        final int orderType = params.optInt("orderType");
        CommonBeaconJump.showPayOrder(context, orderId, orderName, orderAmount, orderType, 0, 0);
    }

    public static void createOrder(final Context context, final Uri uri) {
        final JSONObject params = getParams(uri);
        final int type = params.optInt("type", -1);
        final int number = params.optInt("number", -1);
        final String desc = params.optString("name");
        final int value = params.optInt("value", -1);
        final int unit = params.optInt("unit", 0);
        final String extra = params.optString("extra");
       BeaconJump.showLoadingOrder(context, type, number, desc, value, unit, extra);
    }

    public static void investmentAdviserList(final Context context, final Uri uri) {
        CommonBeaconJump.showInvestmentAdviserList(context);
    }

    /**
     * 积分任务
     */
    public static void bonusPointsTask(final Context context, final Uri uri) {
        CommonBeaconJump.showBonusPoints(context);
    }
    // ----------2.3.0 新增接口----------

    // ----------2.9.1 新增接口----------
    public static void smartStockStare(final Context context, final Uri uri) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }

        if (accountManager.isLogined()) {
            final JSONObject params = getParams(uri);
            final String secCode = params.optString(CommonConst.KEY_SEC_CODE);
            final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                    .getManager(IPortfolioDataManager.class.getName());
            if (portfolioDataManager != null) {
                final String secName = params.optString(CommonConst.KEY_SEC_NAME);
                if (BaseStockUtil.isAStock(secCode)) {
                    BeaconJump.showSmartStockStareActivity(context, secCode, secName);
                } else {
                    BeaconJump.showPortfolioRemindActivity(context, secCode);
                }
            }
        } else {
            CommonBeaconJump.showLogin(context);
        }
    }
    // ----------2.9.1 新增接口----------
}
