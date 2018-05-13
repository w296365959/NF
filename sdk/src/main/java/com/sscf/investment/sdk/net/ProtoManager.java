package com.sscf.investment.sdk.net;

import com.dengtacj.thoth.BaseDecodeStream;
import com.dengtacj.thoth.MapProtoLite;
import com.dengtacj.thoth.Message;
import com.sscf.investment.sdk.utils.DtLog;

import BEC.*;

/**
 * Created by davidwei on 2015/10/30.
 * 协议管理
 */
public final class ProtoManager {
    private static final String TAG = DataEngine.TAG;

    public static MapProtoLite getRequestProto(final int reqType, final Object reqData) {
        MapProtoLite uniPacket = null;
        switch (reqType) {
            case EntityObject.ET_OPEN_API:
                // 分时数据
                uniPacket = genMapEntryLite("open", "open", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_TREND:
                // 分时数据
                uniPacket = genMapEntryLite("quote", "getTrend", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_ENLARGE_TREND:
                // 交易时段分时数据放大
                uniPacket = genMapEntryLite("rtquote", "getTrendSec", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_CALLAUCTION_TREND:
                // 集合竞价时段分时数据放大
                uniPacket = genMapEntryLite("rtquote", "getTrendSec", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_NEWS:
                // 新闻
                uniPacket = genMapEntryLite("news", "getNews", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_RECOMMEND:
                // 获取推荐列表
                uniPacket = genMapEntryLite("intelliRecom", "getRecomList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_REPORT_READ_RECOMMEND:
                // 收集用户阅读信息，用作关联推荐
                uniPacket = genMapEntryLite("intelliRecom", "reportRead", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_HISTORY_CHIP_DIST_SIMPLE:
                // 获取筹码信息
                uniPacket = genMapEntryLite("chipDist", "getHisChipDistSimple", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_ANNOUNCEMENT:
                break;
            case EntityObject.ET_GET_RESEARCH_REPORT:
                break;
            case EntityObject.ET_GET_FUND:
                // 资金
                uniPacket = genMapEntryLite("quote", "getCapitalFlow", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_FINANCE:
                // 财务
                uniPacket = genMapEntryLite("base", "getFinance", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_BRIEF_INFO:
                // 公司简介
                uniPacket = genMapEntryLite("base", "getCompany", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_CONSULTANT:
                // 个股投顾
                uniPacket = genMapEntryLite("news", "getInvestAdviseInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_CONSULTANT_STOCK_INFO:
                // 获取个股压力位、支撑位等数据
                uniPacket = genMapEntryLite("base", "getConsultStockInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_INDEX_STOCK_RANK:
                // 获取指数的关联个股榜单
                uniPacket = genMapEntryLite("singal", "getIndexStocks", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_DISCOVER_BANNER:
                //获取市场情报的轮播图片banner
                uniPacket = genMapEntryLite("singal", "getDiscBanner", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_DT_ACTIVITY_LIST:
                // 获取灯塔活动列表
                uniPacket = genMapEntryLite("singal", "getDtActivityList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_HOTFIX_PATCH:
                // 获取在线热修复的补丁
                uniPacket = genMapEntryLite("hotpatch", "getPatch", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_KLINE_DATA:
                // K线
                uniPacket = genMapEntryLite("quote", "getKLine", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_SIMPLE_QUOTE:
                // 请求简版行情
                uniPacket = genMapEntryLite("quote", "getSimpleQuote", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_QUOTE:
                // 请求行情
                uniPacket = genMapEntryLite("quote", "getQuote", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_UPDOWN_LIST:
                // 板块/大盘涨跌幅榜单
                uniPacket = genMapEntryLite("quote", "getPlateQuote", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_SUBJECT_LIST:
                uniPacket = genMapEntryLite("quote", "getConcQuote", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_CAPITAL_FLOW_LIST:
                uniPacket = genMapEntryLite("quote", "getCapitalDetail", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_CAPITAL_FLOW_MULTI_STOCK:
                uniPacket = genMapEntryLite("quote", "getCapitalMainFlow", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_SH_HK_CONNECT_BALANCE:
                uniPacket = genMapEntryLite("quote", "getAHExtend", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_AH_STOCK:
                uniPacket = genMapEntryLite("quote", "getAHPlate", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_ACCOUNT_SEND_PHONE_VERIFY_CODE:
                uniPacket = genMapEntryLite("account", "doSendPhoneVerifyCode", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_ACCOUNT_VERIFY_ACCOUNT_INFO:
                uniPacket = genMapEntryLite("account", "doVerifyAccountInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_ACCOUNT_FINISH_RESIGTER:
                uniPacket = genMapEntryLite("account", "doFinishResigter", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_ACCOUNT_LOGIN:
                uniPacket = genMapEntryLite("account", "doLogin", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_ACCOUNT_THIRD_PARTY_LOGIN:
                uniPacket = genMapEntryLite("account", "doVerfiyThirdAccount", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_ACCOUNT_LOGOUT:
                uniPacket = genMapEntryLite("account", "logoutAccount", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_ACCOUNT_RESET_PASSWORD:
                uniPacket = genMapEntryLite("account", "resetPassword", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO:
                uniPacket = genMapEntryLite("account", "modifyAccountInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_ACCOUNT_UPDATE_TICKET:
                uniPacket = genMapEntryLite("account", "updateTicket", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_CLOUD_REPORT_SCAN:
                uniPacket = genMapEntryLite("yunconn", "reportScan", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_CLOUD_CONFIRM_LOGIN:
                uniPacket = genMapEntryLite("yunconn", "reportAckLogin", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_SEARCH:
                uniPacket = genMapEntryLite("search", "doCommonSearch", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_HOT_STOCK:
                uniPacket = genMapEntryLite("accuPoint", "getRealTimeHotStock", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_BS_TOP:
                uniPacket = genMapEntryLite("quote", "getSecBsTop", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_CHANGE_STAT:
                uniPacket = genMapEntryLite("statinfo", "getChangeStatDesc", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_COMPANY_INDUSTRIAL_CHAIN:
                uniPacket = genMapEntryLite("investGraph", "getCompanyIndustrialChain", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_DONGMI_QA_LIST:
                uniPacket = genMapEntryLite("jlnews", "getDongmiQaList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_MOCK_UP_LIST:
                uniPacket = genMapEntryLite("similarKLine", "getSimilarKLineStatistics", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_UPGRADE:
                uniPacket = genMapEntryLite("upgradeApp", "doUpgradeApp", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_UPGRADE_DB:
                uniPacket = genMapEntryLite("upgradeAppDB", "doUpgradeApp", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_SAVE_PORTFOLIO:
                // 保存自选股列表
                uniPacket = genMapEntryLite("portfo", "savePortfolio", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_QUERY_PORTFOLIO:
                // 保存自选股列表
                uniPacket = genMapEntryLite("portfo", "queryPortfolio", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_QUERY_STARE_STRATEGY:
                // 智能盯盘策略分组列表
                uniPacket = genMapEntryLite("portfo", "queryStareStrategy", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_KLINE_CONDITION:
                // 日K满足上涨条件
                uniPacket = genMapEntryLite("similarKLine", "getKLineCondition", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_QUERY_DT_LIVE_MSG:
                // 获取灯塔直播消息
                uniPacket = genMapEntryLite("dtlive", "getBoxLive", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_QUERY_STOCK_DETAIL_INFO:
                // 证券info，名称等
                uniPacket = genMapEntryLite("base", "getSecBaseInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_QUERY_SUBJECT_DETAIL_INFO:
                // 主题info，名称等
                uniPacket = genMapEntryLite("base", "getConcBaseInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_STOCK_LIST_IN_INDUSTRY:
                uniPacket = genMapEntryLite("quote", "getPlateStockList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_FAVOR_UPDATE:
                uniPacket = genMapEntryLite("favor", "saveFavor", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_FAVOR_QUERY:
                uniPacket = genMapEntryLite("favor", "queryFavor", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_FAVOR_CANDICATOR_QUERY:
                uniPacket = genMapEntryLite("favor", "queryIndFavor", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_FAVOR_CANDICATOR_SAVE:
                uniPacket = genMapEntryLite("favor", "saveIndFavor", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_QUERY_SUBJECT_RELATED_STOCKS:
                // 主题关联个股
                uniPacket = genMapEntryLite("base", "getConcStockList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_LIVE_MESSAGE:
                // 大单等提醒消息
                uniPacket = genMapEntryLite("base", "getSecLiveMsg", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_FUND_INFO:
                // 基金
                uniPacket = genMapEntryLite("base", "getFundBaseInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_QUERY_STOCK_TICK:
                // 个股成交明细
                uniPacket = genMapEntryLite("quote", "getTick", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_QUERY_CONCEPT_HEAT:
                // 主题热度曲线
                uniPacket = genMapEntryLite("quote", "getConcIndex", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_QUERY_TRADING_TIME:
                // 交易时间
                uniPacket = genMapEntryLite("quote", "getTradingTime", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_MARKET_STAT:
                // 灯塔全A指数统计数据
                uniPacket = genMapEntryLite("quote", "getMarketStat", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_QUERY_H5_RES_ZIP_UPGRADE:
                // H5资源包升级
                uniPacket = genMapEntryLite("zipStore", "getZip", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_DISCOVER_NEWS_LIST:
                // 发现模块资讯列表
                uniPacket = genMapEntryLite("news", "getDiscoveryNewsList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_DISCOVER_FLASH_NEWS:
                // 发现模块滚动直播
                uniPacket = genMapEntryLite("news", "getFlashNewsList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_DISCOVER_INTELLIGENT_PICK_STOCK:
                // 发现模块智能选股
                uniPacket = genMapEntryLite("singal", "getIntelliPickStockExV2", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_STOCK_PICK_VALUEADDED_LIST:
                uniPacket = genMapEntryLite("accuPoint", "getRecommValueAddedList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_DISCOVER_HOT_STOCK:
                // 发现模块智能选股，热门股票
                uniPacket = genMapEntryLite("singal", "getHotStockList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_DISCOVER_STRATEGY_LIST:
                // 发现模块智能选股里获得策略列表
                uniPacket = genMapEntryLite("singal", "getCategoryList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_SELECTED_STRATEGY_LIST:
                uniPacket = genMapEntryLite("singal", "RecoConditionPickStrategyList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_STOCK_PICK_GET_SUBSCRIPTION_LIST:
                uniPacket = genMapEntryLite("singal", "getStrategySubList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_STOCK_PICK_REMOVE_SUBSCRIPTION:
                uniPacket = genMapEntryLite("singal", "actStrategySub", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_MARKET_INFO_AD:
                uniPacket = genMapEntryLite("singal", "getMarketAdList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_MARGIN_TRADING_INFO:
                uniPacket = genMapEntryLite("quote", "getMarginTrade", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_CAPITAL_DDZ:
                // 实时资金流
                uniPacket = genMapEntryLite("quote", "getCapitalDDZ", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_USA_PRIVATIZATION_BRIEF:
                // 中概股私有化简要信息
                uniPacket = genMapEntryLite("singal", "getPrivBreifInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_USA_PRIVATIZATION_TRACKING_LIST:
                // 中概股私有化列表
                uniPacket = genMapEntryLite("singal", "getPrivTopList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_SEC_BS_INFO:
                uniPacket = genMapEntryLite("quote", "getSecBsInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_STOCK_MARGIN_TRADE:
                uniPacket = genMapEntryLite("marginTrade", "getStockMarginTrade", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_REMIND_QUERY_LIST:
                uniPacket = genMapEntryLite("alert", "getAlertMessageList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_REMIND_CLEAR_LIST:
                uniPacket = genMapEntryLite("alert", "clearAlertMessageList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_MSG_CLASS_LIST:
                uniPacket = genMapEntryLite("alert", "getAlertMsgClassList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_MSG_DETAIL_LIST:
                uniPacket = genMapEntryLite("alert", "getAlertMsgClassDetail", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_STATISTIC:
                // 统计
                uniPacket = genMapEntryLite("stat", "report", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_QUERY_SHARE_URL:
                // 分享URL
                uniPacket = genMapEntryLite("appConfig", "getShareUrl", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GUID:
                uniPacket = genMapEntryLite("appConfig", "genGuid", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_CONFIG_GET_IPLIST:
                uniPacket = genMapEntryLite("appConfig", "getIpList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_REPORT_USER_INFO:
                uniPacket = genMapEntryLite("appConfig", "reportUserInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_SPLASH_UPDATE:// 闪屏更新
            case EntityObject.ET_GET_PUSH_SWITCH:// Push开关
            case EntityObject.ET_CONFIG_PORTFOLIO_HEADER_INDEXES: // 自选股指数列表
            case EntityObject.ET_CONFIG_GET_NEW_ACTIVITIES: // 新活动红点，文字提示
            case EntityObject.ET_CONFIG_GET_NEW_SHARE: // 设置里新股的红点
            case EntityObject.ET_CONFIG_GET_INTELLIGENT_ANSWER: // 投顾的红点
            case EntityObject.ET_GET_EMPTY_SERVICE_STATE:// 空服务状态
            case EntityObject.ET_CONFIG_GET_OPEN_BLESSING_PACK:// 福包
            case EntityObject.ET_CONFIG_ACCU_POINT_DESC:// 积分任务描述文字
            case EntityObject.ET_PAY_USER_AGREEMENT:// 支付订单界面用户协议
            case EntityObject.ET_GET_PLUGIN:// 开户插件信息
            case EntityObject.ET_CONFIG_GET_ANNOUCEMENT_TYPE: // 公告的分类信息
                uniPacket = genMapEntryLite("appConfig", "getConfigList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_MARKET_WARNING: // 大盘预警
                uniPacket = genMapEntryLite("marketAlert", "getMarketAlertDetail", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_LOG_REPORT: // 上报log
                uniPacket = genMapEntryLite("logreport", "reportLog", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_USER_POINT_INFO:
                uniPacket = genMapEntryLite("accuPoint", "getUserPointInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_POINT_TASK_LIST:
                uniPacket = genMapEntryLite("accuPoint", "getPointTaskList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_REPORT_TASK_FINISHED:
                uniPacket = genMapEntryLite("accuPoint", "reportFinishTask", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_OPEN_POINT_PRIVILEGE:
                uniPacket = genMapEntryLite("accuPoint", "openAccuPointPrivi", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_EXCHANGE_PRIVILEGE_LIST:
                uniPacket = genMapEntryLite("accuPoint", "getExChangePriviList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_LIVE_VIDEO_LIST:
                uniPacket = genMapEntryLite("videocenter", "getLiveRoomList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_RECOMM_VIDEO_LIST:
                uniPacket = genMapEntryLite("videocenter", "recommVideoList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_UPDATE_FAVOR_VIDEO:
                uniPacket = genMapEntryLite("videocenter", "updateFaverateVideo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_FAVOR_VIDEO:
                uniPacket = genMapEntryLite("videocenter", "getFaverateVideo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_FEED_LIST:
                uniPacket = genMapEntryLite("feed", "getFeedList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_FEED:
                uniPacket = genMapEntryLite("feed", "getFeed", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_FEED_USER_INFO:
                uniPacket = genMapEntryLite("feed", "getFeedUserInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_RELATION_BATCH:
                uniPacket = genMapEntryLite("feed", "getRelationBatch", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_POST_FEED:
                uniPacket = genMapEntryLite("feed", "poFeed", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_DELETE_FEED:
                uniPacket = genMapEntryLite("feed", "delFeed", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_POST_COMMENT:
                uniPacket = genMapEntryLite("feed", "poComment", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_DELETE_COMMENT:
                uniPacket = genMapEntryLite("feed", "delComment", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_FEED_DO_LIKE:
                uniPacket = genMapEntryLite("feed", "doLike", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_FEED_FRIEND_LIST:
                uniPacket = genMapEntryLite("feed", "getUserRelation", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_FEED_SET_ATTENTION:
                uniPacket = genMapEntryLite("feed", "setUserRelation", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_INVESTMENT_ADVISER_RECOMMENDED_LIST:
                uniPacket = genMapEntryLite("feed", "getInvestRecommendList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_INVESTMENT_ADVISER_LIST:
                uniPacket = genMapEntryLite("feed", "getInvestList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_CHIP_DIST:
                uniPacket = genMapEntryLite("chipDist", "getChipDistDetail", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_STAT_REPORT:
                uniPacket = genMapEntryLite("stat", "report", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_SCORE_DETAIL:
                uniPacket = genMapEntryLite("consultstock", "getScoreDetail", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_RTMIN_DATA_EX:
                // 5日分时
                uniPacket = genMapEntryLite("kline", "getRtMinEx", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_MEMBER_FEE_LIST:
                // 获取灯塔开通会员费用列表
                uniPacket = genMapEntryLite("dtpay", "getMemberFeeList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_PAY_ORDER_ID:
                // 生成灯塔内部订单号
                uniPacket = genMapEntryLite("dtpay", "getPayOrderId", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_ALI_PAY_SIGN:
                // 支付宝支付-生成签名
                uniPacket = genMapEntryLite("dtpay", "getAliPaySign", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_WX_PAY_PREPAY_ID:
                // 微信支付-生成预支付交易会话ID
                uniPacket = genMapEntryLite("dtpay", "getWxPayPrepayId", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_QUERY_PAY_RESULT:
                // 终端上报支付同步通知结果
                uniPacket = genMapEntryLite("dtpay", "reportPayResult", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_ORDER_RESULT:
                // 查询订单支付结果
                uniPacket = genMapEntryLite("dtpay", "getOrderPayResult", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_CHECK_USER_COUPON:
                // 查询用户是否有可用优惠券
                uniPacket = genMapEntryLite("dtpay", "checkUserCoupon", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_H5_PAY_URL:
                // 获取H5支付URL
                uniPacket = genMapEntryLite("dtpay", "getH5PayUrl", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_USER_COUPON_NUM:
                // 查询用户优惠券个数
                uniPacket = genMapEntryLite("accuPoint", "getUserCouponNum", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_USER_EVAL_RESULT:
                // 获取用户风险评估结果
                uniPacket = genMapEntryLite("accuPoint", "getUserEvalResult", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_ACCUMULATE_POINTS_COMMIT_CODE:
                uniPacket = genMapEntryLite("accuPoint", "commitAccuPointCode", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_HOT_SPOT_LIST:
                uniPacket = genMapEntryLite("dttemp", "getTopicList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_INTELLIGENT_INPUT_DEFAULT_TEXT:
                uniPacket = genMapEntryLite("investInterface", "getInputBoxDefault", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_INTELLIGENT_INPUT_SUGGESTION_TEXT:
                uniPacket = genMapEntryLite("investInterface", "getInputSuggestion", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_STOCK_MAP_GET_SEC_BY_COORDS:
                uniPacket = genMapEntryLite("secmap", "getSecByCoords", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_STOCK_MAP_GET_SALEDEPART_BY_COORDS:
                uniPacket = genMapEntryLite("secmap", "getSaleDepartByCoords", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_STOCK_MAP_GET_SALEDEPT_DETAIL:
                uniPacket = genMapEntryLite("secmap", "getSaleDepTradeDetail", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_STOCK_MAP_GET_COMPANY_BY_COORDS:
                uniPacket = genMapEntryLite("secmap", "getCompanyByCoords", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_TEACHER_YAN_WORD:
            case EntityObject.ET_TEACHER_YAN_CURSE:
                uniPacket = genMapEntryLite("sscfInfo", "getWxWalkRecord", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_TALK_FREE:
                uniPacket = genMapEntryLite("sscfInfo", "getWxTalkFree", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_TEACHER_YAN_ARTICLE:
                uniPacket = genMapEntryLite("sscfInfo", "getWxTeachList", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_ANALYSIS_MARKET:
            case EntityObject.ET_MORNING_REF:
                uniPacket = genMapEntryLite("sscfInfo", "getInformationSpiderNews", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_REQUEST_TEACHER_VIDEO:
                uniPacket = genMapEntryLite("sscfInfo", "getVideoInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_REQUEST_MAKE_MONEY_STATE:
                uniPacket = genMapEntryLite("quotecalc", "getRealMarketQRDes", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_TEACHERYAN_COUNT_NUM:
                uniPacket = genMapEntryLite("sscfInfo", "doRead", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_LIKE_INFO:
                uniPacket = genMapEntryLite("sscfInfo", "getLikeInfo", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_DO_LIKE_INFO:
                uniPacket = genMapEntryLite("sscfInfo", "doLike", NetworkConst.REQ, reqData);
                break;
            case EntityObject.ET_GET_FIVEDAY_CAPITAL_DDZ:
                //五日资金博弈
                DtLog.e(TAG, "genMapEntryLite ");
                uniPacket = genMapEntryLite("quantdata", "getCapitalDDZ5Days", NetworkConst.REQ, reqData);
            default:
                DtLog.w(TAG, "requestCommonData reqType=" + reqType);
                break;
        }

        return uniPacket;
    }

    public static Object getResponseObject(final int reqType, final MapProtoLite packet) {
        Object resObject = null;
        switch (reqType) {
            case EntityObject.ET_OPEN_API:
                // 分时数据
                resObject = packet.read(NetworkConst.RSP, new OpenApiRsp());
                break;
            case EntityObject.ET_GET_TREND:
                // 新闻
                resObject = packet.read(NetworkConst.RSP, new TrendRsp());
                break;
            case EntityObject.ET_GET_ENLARGE_TREND:
                // 交易时段分时数据放大
                resObject = packet.read(NetworkConst.RSP, new TrendRsp());
                break;
            case EntityObject.ET_GET_CALLAUCTION_TREND:
                // 集合竞价时段分时数据放大
                resObject = packet.read(NetworkConst.RSP, new TrendRsp());
                break;
            case EntityObject.ET_GET_NEWS:
                // 新闻
                resObject = packet.read(NetworkConst.RSP, new NewsRsp());
                break;
            case EntityObject.ET_GET_RECOMMEND:
                // 获取推荐列表
                resObject = packet.read(NetworkConst.RSP, new RecomListRsp());
                break;
            case EntityObject.ET_REPORT_READ_RECOMMEND:
                // 收集用户阅读信息，用作关联推荐
                resObject = packet;
                break;
            case EntityObject.ET_GET_HISTORY_CHIP_DIST_SIMPLE:
                // 获取筹码信息
                resObject = packet.read(NetworkConst.RSP, new HisChipDistRsp());
                break;
            case EntityObject.ET_GET_ANNOUNCEMENT:
                // 公告
                break;
            case EntityObject.ET_GET_RESEARCH_REPORT:
                // 研报
                break;
            case EntityObject.ET_GET_FUND:
                // 资金
                resObject = packet.read(NetworkConst.RSP, new CapitalFlowRsp());
                break;
            case EntityObject.ET_GET_FINANCE:
                // 财务
                resObject = packet.read(NetworkConst.RSP, new FinanceRsp());
                break;
            case EntityObject.ET_GET_BRIEF_INFO:
                // 公司简况
                resObject = packet.read(NetworkConst.RSP, new CompanyRsp());
                break;
            case EntityObject.ET_GET_CONSULTANT:
                // 个股投顾
                resObject = packet.read(NetworkConst.RSP, new GetInvestAdvisorListRsp());
                break;
            case EntityObject.ET_GET_CONSULTANT_STOCK_INFO:
                // 获取个股压力位、支撑位等数据
                resObject = packet.read(NetworkConst.RSP, new GetConsultStockInfoRsp());
                break;
            case EntityObject.ET_GET_INDEX_STOCK_RANK:
                // 获取指数的关联个股榜单
                resObject = packet.read(NetworkConst.RSP, new GetIndexStocksRsp());
                break;
            case EntityObject.ET_GET_DISCOVER_BANNER:
                //获取市场情报的轮播图片banner
                resObject = packet.read(NetworkConst.RSP, new GetDiscBannerRsp());
                break;
            case EntityObject.ET_GET_DT_ACTIVITY_LIST:
                //活动列表
                resObject = packet.read(NetworkConst.RSP, new GetDtActivityListRsp());
                break;
            case EntityObject.ET_GET_HOTFIX_PATCH:
                // 获取在线热修复的补丁
                resObject = packet.read(NetworkConst.RSP, new PatchUrlRsp());
                break;
            case EntityObject.ET_GET_KLINE_DATA:
                // K线
                resObject = packet.read(NetworkConst.RSP, new KLineRsp());
                break;
            case EntityObject.ET_GET_RTMIN_DATA_EX:
                // 5日分时
                resObject = packet.read(NetworkConst.RSP, new RtMinRsp());
                break;
            case EntityObject.ET_GET_MEMBER_FEE_LIST:
                // 获取灯塔开通会员费用列表
                resObject = packet.read(NetworkConst.RSP, new GetMemberFeeListRsp());
                break;
            case EntityObject.ET_GET_PAY_ORDER_ID:
                // 生成灯塔内部订单号
                resObject = packet.read(NetworkConst.RSP, new GetPayOrderIdRsp());
                break;
            case EntityObject.ET_GET_ALI_PAY_SIGN:
                // 支付宝支付-生成签名
                resObject = packet.read(NetworkConst.RSP, new GetAliPaySignRsp());
                break;
            case EntityObject.ET_GET_WX_PAY_PREPAY_ID:
                // 微信支付-生成预支付交易会话ID
                resObject = packet.read(NetworkConst.RSP, new GetWxPayPrepayIdRsp());
                break;
            case EntityObject.ET_QUERY_PAY_RESULT:
                // 终端上报支付同步通知结果
                resObject = packet.read(NetworkConst.RSP, new ReportPayResultRsp());
                break;
            case EntityObject.ET_GET_ORDER_RESULT:
                // 查询订单支付结果
                resObject = packet.read(NetworkConst.RSP, new GetOrderPayResultRsp());
                break;
            case EntityObject.ET_CHECK_USER_COUPON:
                // 查询用户是否有可用优惠券
                resObject = packet.read(NetworkConst.RSP, new CheckUserCouponRsp());
                break;
            case EntityObject.ET_GET_H5_PAY_URL:
                // 获取H5支付URL
                resObject = packet.read(NetworkConst.RSP, new GetH5PayUrlRsp());
                break;
            case EntityObject.ET_GET_USER_COUPON_NUM:
                // 查询用户优惠券个数
                resObject = packet.read(NetworkConst.RSP, new GetUserCouponNumRsp());
                break;
            case EntityObject.ET_GET_USER_EVAL_RESULT:
                // 获取用户风险评估结果
                resObject = packet.read(NetworkConst.RSP, new GetUserEvalResultRsp());
                break;
            case EntityObject.ET_GET_SIMPLE_QUOTE:
                // 请求简版行情
                resObject = packet.read(NetworkConst.RSP, new QuoteSimpleRsp());
                break;
            case EntityObject.ET_GET_QUOTE:
                // 请求行情
                resObject = packet.read(NetworkConst.RSP, new QuoteRsp());
                break;
            case EntityObject.ET_GET_UPDOWN_LIST:
                // 板块/大盘涨跌幅榜单
                resObject = packet.read(NetworkConst.RSP, new PlateQuoteRsp());
                break;
            case EntityObject.ET_GET_SUBJECT_LIST:
                resObject = packet.read(NetworkConst.RSP, new ConcQuoteRsp());
                break;
            case EntityObject.ET_GET_CAPITAL_FLOW_LIST:
                resObject = packet.read(NetworkConst.RSP, new CapitalDetailRsp());
                break;
            case EntityObject.ET_GET_CAPITAL_FLOW_MULTI_STOCK:
                resObject = packet.read(NetworkConst.RSP, new CapitalMainFlowRsp());
                break;
            case EntityObject.ET_GET_SH_HK_CONNECT_BALANCE:
                resObject = packet.read(NetworkConst.RSP, new AHExtendRsp());
                break;
            case EntityObject.ET_GET_AH_STOCK:
                resObject = packet.read(NetworkConst.RSP, new AHPlateRsp());
                break;
            case EntityObject.ET_ACCOUNT_SEND_PHONE_VERIFY_CODE:
            case EntityObject.ET_ACCOUNT_VERIFY_ACCOUNT_INFO:
                resObject = packet;
                break;
            case EntityObject.ET_ACCOUNT_LOGIN:
            case EntityObject.ET_ACCOUNT_FINISH_RESIGTER:
            case EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO:
            case EntityObject.ET_ACCOUNT_LOGOUT:
            case EntityObject.ET_ACCOUNT_THIRD_PARTY_LOGIN:
            case EntityObject.ET_FAVOR_QUERY:
                resObject = packet;
                break;
            case EntityObject.ET_ACCOUNT_UPDATE_TICKET:
                resObject = packet.read(NetworkConst.RSP, new UpdateTicketRsp());
                break;
            case EntityObject.ET_CLOUD_REPORT_SCAN:
                resObject = packet;
                break;
            case EntityObject.ET_CLOUD_CONFIRM_LOGIN:
                resObject = packet;
                break;
            case EntityObject.ET_FAVOR_UPDATE:
                resObject = packet.read(NetworkConst.RSP, new SaveFavorNewsRsp());
                break;
            case EntityObject.ET_FAVOR_CANDICATOR_QUERY:
                resObject = packet.read(NetworkConst.RSP, new QueryFavorIndRsp());
                break;
            case EntityObject.ET_FAVOR_CANDICATOR_SAVE:
                resObject = packet.read(NetworkConst.RSP, new SaveFavorIndRsp());
                break;
            case EntityObject.ET_GUID:
                // guid
                resObject = packet.read(NetworkConst.RSP, new GenGuidRsp());
                break;
            case EntityObject.ET_CONFIG_GET_IPLIST:
                resObject = packet.read(NetworkConst.RSP, new IpListRsp());
                break;
            case EntityObject.ET_REPORT_USER_INFO:
                // 暂时不处理
                resObject = packet.read("", -1);
                break;
            case EntityObject.ET_STATISTIC:
                // 统计，暂时不处理
                resObject = packet.read("", -1);
                break;
            case EntityObject.ET_SEARCH:
                // 搜索
                resObject = packet.read(NetworkConst.RSP, new CommonSearchRsp());
                break;
            case EntityObject.ET_GET_HOT_STOCK:
                resObject = packet.read(NetworkConst.RSP, new GetRealTimeHotStockRsp());
                break;
            case EntityObject.ET_GET_BS_TOP:
                resObject = packet.read(NetworkConst.RSP, new GetSecBsTopRsp());
                break;
            case EntityObject.ET_GET_CHANGE_STAT:
                resObject = packet.read(NetworkConst.RSP, new ChangeStatRsp());
                break;
            case EntityObject.ET_GET_COMPANY_INDUSTRIAL_CHAIN:
                resObject = packet.read(NetworkConst.RSP, new CompanyIndustrialChainRsp());
                break;
            case EntityObject.ET_GET_DONGMI_QA_LIST:
                resObject = packet.read(NetworkConst.RSP, new DongmiQaListRsp());
                break;
            case EntityObject.ET_GET_MOCK_UP_LIST:
                resObject = packet.read(NetworkConst.RSP, new SKLStatisticsRsp());
                break;
            case EntityObject.ET_UPGRADE:
            case EntityObject.ET_UPGRADE_DB:
                // 更新
                resObject = packet.read(NetworkConst.RSP, new UpgradeRsp());
                break;
            case EntityObject.ET_SAVE_PORTFOLIO:
                // 上传自选
                resObject = packet;
                break;
            case EntityObject.ET_QUERY_PORTFOLIO:
                // 上传自选
                resObject = packet;
                break;
            case EntityObject.ET_QUERY_STARE_STRATEGY:
                // 获取智能盯盘策略分组列表
                resObject = packet.read(NetworkConst.RSP, new QueryStareStrategyRsp());
                break;
            case EntityObject.ET_KLINE_CONDITION:
                // 日K满足上涨条件
                resObject = packet.read(NetworkConst.RSP, new KLineConditionRsp());
                break;
            case EntityObject.ET_QUERY_DT_LIVE_MSG:
                // 获取灯塔直播消息
                resObject = packet;
                break;
            case EntityObject.ET_QUERY_STOCK_DETAIL_INFO:
                // 个股信息
                resObject = packet.read(NetworkConst.RSP, new SecBaseInfoRsp());
                break;
            case EntityObject.ET_QUERY_SUBJECT_DETAIL_INFO:
                // 主题info，名称等
                resObject = packet.read(NetworkConst.RSP, new ConcBaseInfoRsp());
                break;
            case EntityObject.ET_QUERY_SUBJECT_RELATED_STOCKS:
                // 主题关联个股
                resObject = packet.read(NetworkConst.RSP, new ConcStockListRsp());
                break;
            case EntityObject.ET_GET_LIVE_MESSAGE:
                // 大单等提醒消息
                resObject = packet.read(NetworkConst.RSP, new SecLiveMsgRsp());
                break;
            case EntityObject.ET_GET_STOCK_LIST_IN_INDUSTRY:
                resObject = packet.read(NetworkConst.RSP, new PlateStockListRsp());
                break;
            case EntityObject.ET_GET_FUND_INFO:
                // 基金
                resObject = packet.read(NetworkConst.RSP, new FundBaseInfoRsp());
                break;
            case EntityObject.ET_QUERY_STOCK_TICK:
                // 个股成交明细
                resObject = packet.read(NetworkConst.RSP, new TickRsp());
                break;
            case EntityObject.ET_QUERY_CONCEPT_HEAT:
                // 主题热度曲线
                resObject = packet.read(NetworkConst.RSP, new ConcIndexRsp());
                break;
            case EntityObject.ET_QUERY_TRADING_TIME:
                // 交易时间
                resObject = packet.read(NetworkConst.RSP, new TradingTimeRsp());
                break;
            case EntityObject.ET_GET_MARKET_STAT:
                // 灯塔全A指数统计数据
                resObject = packet.read(NetworkConst.RSP, new MarketStatRsp());
                break;
            case EntityObject.ET_QUERY_H5_RES_ZIP_UPGRADE:
                // H5资源包升级
                resObject = packet.read(NetworkConst.RSP, new ZipStoreRsp());
                break;
            case EntityObject.ET_QUERY_SHARE_URL:
                // 分享URL
                resObject = packet.read(NetworkConst.RSP, new ShareUrlRsp());
                break;
            case EntityObject.ET_GET_DISCOVER_NEWS_LIST:
                // 发现模块资讯列表
                resObject = packet.read(NetworkConst.RSP, new DiscoveryNewsContentRsp());
                break;
            case EntityObject.ET_GET_DISCOVER_FLASH_NEWS:
                // 发现模块滚动直播
                resObject = packet.read(NetworkConst.RSP, new FlashNewsListRsp());
                break;
            case EntityObject.ET_GET_DISCOVER_INTELLIGENT_PICK_STOCK:
                // 发现模块智能选股
                resObject = packet.read(NetworkConst.RSP, new IntelliPickStockRspEx());
                break;
            case EntityObject.ET_GET_STOCK_PICK_VALUEADDED_LIST:
                resObject = packet.read(NetworkConst.RSP, new RecommValueAddedListRsp());
                break;
            case EntityObject.ET_GET_DISCOVER_HOT_STOCK:
                // 发现模块智能选股，热门股票
                resObject = packet.read(NetworkConst.RSP, new HotStockListRsp());
                break;
            case EntityObject.ET_GET_DISCOVER_STRATEGY_LIST:
                // 发现模块智能选股里获得策略列表
                resObject = packet.read(NetworkConst.RSP, new GetCategoryListRsp());
                break;
            case EntityObject.ET_SELECTED_STRATEGY_LIST:
                resObject = packet.read(NetworkConst.RSP, new ConditionPickStrategyListRsp());
                break;
            case EntityObject.ET_STOCK_PICK_GET_SUBSCRIPTION_LIST:
                resObject = packet.read(NetworkConst.RSP, new GetStrategySubListRsp());
                break;
            case EntityObject.ET_STOCK_PICK_REMOVE_SUBSCRIPTION:
                resObject = packet.read(NetworkConst.RSP, new ActStrategySubRsp());
                break;
            case EntityObject.ET_MARKET_INFO_AD:
                resObject = packet.read(NetworkConst.RSP, new MarketAdListRsp());
                break;
            case EntityObject.ET_GET_MARGIN_TRADING_INFO:
                resObject = packet.read(NetworkConst.RSP, new MarginTradeRsp());
                break;
            case EntityObject.ET_GET_CAPITAL_DDZ:
                // 实时资金流
                resObject = packet.read(NetworkConst.RSP, new CapitalDDZRsp());
                break;
            case EntityObject.ET_GET_USA_PRIVATIZATION_BRIEF:
                // 中概股私有化简要信息
                resObject = packet.read(NetworkConst.RSP, new GetPrivBreifInfoRsp());
                break;
            case EntityObject.ET_GET_USA_PRIVATIZATION_TRACKING_LIST:
                // 中概股私有化列表
                resObject = packet.read(NetworkConst.RSP, new GetPrivTopListRsp());
                break;
            case EntityObject.ET_GET_SEC_BS_INFO:
                resObject = packet.read(NetworkConst.RSP, new GetSecBsInfoRsp());
                break;
            case EntityObject.ET_GET_STOCK_MARGIN_TRADE:
                resObject = packet.read(NetworkConst.RSP, new StockMarginTradeRsp());
                break;
            case EntityObject.ET_REMIND_QUERY_LIST:
                // 查询提醒列表
                resObject = packet;
                break;
            case EntityObject.ET_REMIND_CLEAR_LIST:
                // 清空提醒列表
                resObject = packet.read("", -1);
                break;
            case EntityObject.ET_GET_MSG_CLASS_LIST:
                resObject = packet.read(NetworkConst.RSP, new AlertMsgClassListRsp());
                break;
            case EntityObject.ET_GET_MSG_DETAIL_LIST:
                resObject = packet.read(NetworkConst.RSP, new AlertMsgClassDetailRsp());
                break;
            case EntityObject.ET_GET_SPLASH_UPDATE: // 闪屏更新
            case EntityObject.ET_GET_PUSH_SWITCH: // Push开关
            case EntityObject.ET_CONFIG_PORTFOLIO_HEADER_INDEXES: // 自选股指数列表
            case EntityObject.ET_CONFIG_GET_NEW_ACTIVITIES: // 新活动红点，文字提示
            case EntityObject.ET_CONFIG_GET_NEW_SHARE: // 设置里新股的红点
            case EntityObject.ET_CONFIG_GET_INTELLIGENT_ANSWER: // 投顾的红点
            case EntityObject.ET_CONFIG_GET_OPEN_BLESSING_PACK: // 福包
            case EntityObject.ET_CONFIG_ACCU_POINT_DESC: // 积分任务描述文字
            case EntityObject.ET_PAY_USER_AGREEMENT: // 支付订单页面用户协议
            case EntityObject.ET_GET_PLUGIN:// 开户插件信息
            case EntityObject.ET_GET_EMPTY_SERVICE_STATE: //空服务的状态
            case EntityObject.ET_CONFIG_GET_ANNOUCEMENT_TYPE: //空服务的状态
                resObject = packet.read(NetworkConst.RSP, new GetConfigRsp());
                break;
            case EntityObject.ET_GET_MARKET_WARNING: // 大盘预警
                resObject = packet.read(NetworkConst.RSP, new MarketAlertRsp());
                break;
            case EntityObject.ET_LOG_REPORT: // 上报log
                resObject = packet.read(NetworkConst.RSP, new ReportLogRsp());
                break;
            case EntityObject.ET_GET_USER_POINT_INFO:
                resObject = packet.read(NetworkConst.RSP, new GetUserPointInfoRsp());
                break;
            case EntityObject.ET_GET_POINT_TASK_LIST:
                resObject = packet.read(NetworkConst.RSP, new GetPointTaskListRsp());
                break;
            case EntityObject.ET_GET_EXCHANGE_PRIVILEGE_LIST:
                resObject = packet.read(NetworkConst.RSP, new GetExChangePriviListRsp());
                break;
            case EntityObject.ET_REPORT_TASK_FINISHED:
                resObject = packet;
                break;
            case EntityObject.ET_OPEN_POINT_PRIVILEGE:
                resObject = packet;
                break;
            case EntityObject.ET_GET_LIVE_VIDEO_LIST:
                resObject = packet.read(NetworkConst.RSP, new LiveRoomListRsp());
                break;
            case EntityObject.ET_GET_RECOMM_VIDEO_LIST:
                resObject = packet.read(NetworkConst.RSP, new RecommVideoListRsp());
                break;
            case EntityObject.ET_UPDATE_FAVOR_VIDEO:
                resObject = packet.read(NetworkConst.RSP, new UpdateFaverateVideoRsp());
                ;
                break;
            case EntityObject.ET_GET_FAVOR_VIDEO:
                resObject = packet.read(NetworkConst.RSP, new VideoBlockList());
                break;
            case EntityObject.ET_GET_FEED_LIST:
                resObject = packet.read(NetworkConst.RSP, new GetFeedListRsp());
                break;
            case EntityObject.ET_GET_FEED:
                resObject = packet.read(NetworkConst.RSP, new GetFeedRsp());
                break;
            case EntityObject.ET_GET_FEED_USER_INFO:
                resObject = packet.read(NetworkConst.RSP, new GetFeedUserInfoRsp());
                break;
            case EntityObject.ET_GET_RELATION_BATCH:
                resObject = packet.read(NetworkConst.RSP, new GetRelationBatchRsp());
                break;
            case EntityObject.ET_POST_FEED:
                resObject = packet.read(NetworkConst.RSP, new PoFeedRsp());
                break;
            case EntityObject.ET_DELETE_FEED:
                resObject = packet;
                break;
            case EntityObject.ET_POST_COMMENT:
                resObject = packet.read(NetworkConst.RSP, new PoCommentRsp());
                break;
            case EntityObject.ET_DELETE_COMMENT:
                resObject = packet;
                break;
            case EntityObject.ET_FEED_DO_LIKE:
                resObject = packet.read(NetworkConst.RSP, new LikeRsp());
                break;
            case EntityObject.ET_FEED_FRIEND_LIST:
                resObject = packet.read(NetworkConst.RSP, new GetUserRelationRsp());
                break;
            case EntityObject.ET_FEED_SET_ATTENTION:
                resObject = packet.read(NetworkConst.RSP, new SetUserRelationRsp());
                break;
            case EntityObject.ET_GET_INVESTMENT_ADVISER_RECOMMENDED_LIST:
                resObject = packet.read(NetworkConst.RSP, new GetInvestRecommendRsp());
                break;
            case EntityObject.ET_GET_INVESTMENT_ADVISER_LIST:
                resObject = packet.read(NetworkConst.RSP, new GetInvestListRsp());
                break;
            case EntityObject.ET_GET_CHIP_DIST:
                resObject = packet.read(NetworkConst.RSP, new ChipDistRsp());
                break;
            case EntityObject.ET_STAT_REPORT:
                resObject = packet;
                break;
            case EntityObject.ET_GET_SCORE_DETAIL:
                resObject = packet.read(NetworkConst.RSP, new ConsultScoreRsp());
                break;
            case EntityObject.ET_ACCUMULATE_POINTS_COMMIT_CODE:
                resObject = packet;
                break;
            case EntityObject.ET_GET_HOT_SPOT_LIST:
                resObject = packet.read(NetworkConst.RSP, new TopicListRsp());
                break;
            case EntityObject.ET_INTELLIGENT_INPUT_DEFAULT_TEXT:
                resObject = packet.read(NetworkConst.RSP, new InputBoxDefaultRsp());
                break;
            case EntityObject.ET_INTELLIGENT_INPUT_SUGGESTION_TEXT:
                resObject = packet.read(NetworkConst.RSP, new InputSuggestionRsp());
                break;
            case EntityObject.ET_STOCK_MAP_GET_SEC_BY_COORDS:
                resObject = packet.read(NetworkConst.RSP, new GetSecByCoordsRsp());
                break;
            case EntityObject.ET_STOCK_MAP_GET_SALEDEPART_BY_COORDS:
                resObject = packet.read(NetworkConst.RSP, new GetSaleDepartByCoordsRsp());
                break;
            case EntityObject.ET_STOCK_MAP_GET_SALEDEPT_DETAIL:
                resObject = packet.read(NetworkConst.RSP, new GetSaleDepTradeDetailRsp());
                break;
            case EntityObject.ET_STOCK_MAP_GET_COMPANY_BY_COORDS:
                resObject = packet.read(NetworkConst.RSP, new GetCompanyByCoordsRsp());
                break;
            case EntityObject.ET_TEACHER_YAN_WORD:
            case EntityObject.ET_TEACHER_YAN_CURSE:
                resObject = packet.read(NetworkConst.RSP, new WxWalkRecordListRsp());
                break;
            case EntityObject.ET_TALK_FREE:
                resObject = packet.read(NetworkConst.RSP, new WxTalkFreeListRsp());
                break;
            case EntityObject.ET_TEACHER_YAN_ARTICLE:
                resObject = packet.read(NetworkConst.RSP, new WxTeachListRsp());
                break;
            case EntityObject.ET_MORNING_REF:
            case EntityObject.ET_ANALYSIS_MARKET:
                resObject = packet.read(NetworkConst.RSP, new InformationSpiderNewsListRsp());
                break;
            case EntityObject.ET_REQUEST_TEACHER_VIDEO:
                resObject = packet.read(NetworkConst.RSP, new VideoInfoListRsp());
                break;
            case EntityObject.ET_REQUEST_MAKE_MONEY_STATE:
                resObject = packet.read(NetworkConst.RSP, new RealMarketQRRsp());
                break;
            case EntityObject.ET_TEACHERYAN_COUNT_NUM:
                resObject = packet.read(NetworkConst.RSP, new SscfInfoReadRsp());
                break;
            case EntityObject.ET_GET_LIKE_INFO:
                resObject = packet.read(NetworkConst.RSP, new SscfInfoLikeUserRsp());
                break;
            case EntityObject.ET_DO_LIKE_INFO:
                resObject = packet.read(NetworkConst.RSP, new SscfInfoLikeRsp());
                break;
            case EntityObject.ET_GET_FIVEDAY_CAPITAL_DDZ:
                //五日资金博弈
                resObject = packet.read(NetworkConst.RSP, new CapitalDDZMultiRsp());
            default:
                DtLog.e(TAG, "requestCommonData reqType=");
                break;
        }

        return resObject;
    }

    public static boolean decode(final Message struct, final byte[] bytes) {
        if (struct == null || bytes == null || bytes.length == 0) {
            return false;
        }

        try {
            BaseDecodeStream is = new BaseDecodeStream(bytes);
            is.setCharset("UTF8");
            struct.read(is);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static MapProtoLite genMapEntryLite(String serverName, String functionName, String paraFlg, Object data) {
        if (data == null) {
            return null;
        }

        final MapProtoLite packet = new MapProtoLite();
        packet.setHandleName(serverName);
        packet.setFuncName(functionName);
        packet.write(paraFlg, data);

        return packet;
    }
}
