package com.sscf.investment.sdk.net;

import com.dengtacj.thoth.Message;
import com.dengtacj.json.JSONException;

/**
 * Created by xuebinliu on 2015/7/31.
 */
public final class EntityObject {
    // 请求数据额外数据类型名称，用作KEY
    public static final String REQ_EXT_TYPE = "REQ_EXT_TYPE";

    // 请求数据数据类型名称
    public static final int ET_GET_TREND = 0;                 // 分时数据
    public static final int ET_GET_KLINE_INCRCEMENT = 1;       // 分时数据，增量更新用
    public static final int ET_GET_NEWS = 2;                  // 个股新闻
    public static final int ET_GET_ANNOUNCEMENT = 3;           // 个股公告
    public static final int ET_GET_RESEARCH_REPORT = 4;        // 个股研报
    public static final int ET_GET_FUND = 5;                  // 个股资金
    public static final int ET_GET_FINANCE = 6;                // 个股财务
    public static final int ET_GET_BRIEF_INFO = 7;             // 公司简介
    public static final int ET_GET_KLINE_DATA = 8;            // K线数据请求
    public static final int ET_GET_SIMPLE_QUOTE = 9;           // 请求简版行情
    public static final int ET_GET_QUOTE = 10;                  // 请求行情
    public static final int ET_GET_UPDOWN_LIST = 11;            // 板块/大盘涨跌幅榜单
    public static final int ET_GET_STOCK_LIST_IN_INDUSTRY = 12; // 某板块里的所有股票
    public static final int ET_GET_SUBJECT_LIST = 13;            // 主题列表
    public static final int ET_GET_CAPITAL_FLOW_LIST = 14;            // 资金流列表
    public static final int ET_GET_CAPITAL_FLOW_MULTI_STOCK = 15;            // 多股资金流信息
    public static final int ET_GET_SH_HK_CONNECT_BALANCE = 16;            // 沪港通余额
    public static final int ET_GET_AH_STOCK = 17;            // AH股溢价
    public static final int ET_GET_DISCOVER_NEWS_LIST = 18;            // 发现模块资讯列表
    public static final int ET_GET_DISCOVER_FLASH_NEWS = 19;            // 发现模块滚动直播
    public static final int ET_GET_MARGIN_TRADING_INFO = 20; // 获得个股的融资融券数据
    public static final int ET_GET_CAPITAL_DDZ = 21;                 // 实时资金流
    public static final int ET_GET_SPLASH_UPDATE = 22;                 // 闪屏更新
    public static final int ET_GET_PUSH_SWITCH = 23;                 // Push开关
    public static final int ET_GET_LIVE_MESSAGE = 24;                 // 大单等提醒消息
    public static final int ET_GET_MARKET_STAT = 25;                 // 灯塔全A指数统计数据
    public static final int ET_GET_MARKET_WARNING = 26;                 // 灯塔全A指数统计数据
    public static final int ET_GET_USA_PRIVATIZATION_BRIEF = 27;                 // 中概股私有化简要信息
    public static final int ET_GET_USA_PRIVATIZATION_TRACKING_LIST = 28;                 // 中概股私有化列表
    public static final int ET_GET_CONSULTANT = 29;         // 个股投顾
    public static final int ET_GET_CONSULTANT_STOCK_INFO = 30;     // 获取个股压力位、支撑位等数据
    public static final int ET_GET_INDEX_STOCK_RANK = 31;     // 获取指数的关联个股榜单
    public static final int ET_GET_HOTFIX_PATCH = 32;       // 获取在线热修复的补丁
    public static final int ET_GET_EMPTY_SERVICE_STATE = 33;       // 获取空服务的状态
    public static final int ET_GET_DISCOVER_BANNER = 34;    //获取市场情报的轮播图片banner
    public static final int ET_GET_DT_ACTIVITY_LIST = 35;   // 获取灯塔活动列表
    public static final int ET_GET_SEC_BS_INFO = 36;   // 获取个股BS点信息
    public static final int ET_GET_STOCK_MARGIN_TRADE = 37;   // 获取个股最近一个月的两融数据
    public static final int ET_GET_ENLARGE_TREND = 38;        // 交易时段分时数据放大
    public static final int ET_GET_CALLAUCTION_TREND = 39;    // 集合竞价时段分时数据放大
    public static final int ET_GET_RECOMMEND = 40;    // 获取推荐列表
    public static final int ET_REPORT_READ_RECOMMEND = 41;    // 收集用户阅读信息，用作关联推荐
    public static final int ET_GET_HISTORY_CHIP_DIST_SIMPLE = 42;    // 获取历史筹码数据
    public static final int ET_GET_CHANGE_STAT = 43;    // 获取涨跌分布
    public static final int ET_GET_COMPANY_INDUSTRIAL_CHAIN = 44;             // 公司产业链信息
    public static final int ET_GET_DONGMI_QA_LIST = 45;             // 董秘QA

    // account服务
    public static final int ET_ACCOUNT_SEND_PHONE_VERIFY_CODE = 101;      // 发送手机验证码
    public static final int ET_ACCOUNT_VERIFY_ACCOUNT_INFO = 102;         // 验证账户信息，比如短信验证码,用户名字，用户手机号码或电子邮箱等
    public static final int ET_ACCOUNT_FINISH_RESIGTER = 103;             // 完成注册，完成真正的用户名，密码等设置
    public static final int ET_ACCOUNT_LOGIN = 104;                     // 登录
    public static final int ET_ACCOUNT_RESET_PASSWORD = 105;             // 重置密码
    public static final int ET_ACCOUNT_MODIFY_ACCOUNT_INFO = 106;        // 修改用户信息
    public static final int ET_ACCOUNT_LOGOUT= 107;                     // 用户登出注销
    public static final int ET_ACCOUNT_THIRD_PARTY_LOGIN = 108;         // 第三方帐号登录
    public static final int ET_ACCOUNT_UPDATE_TICKET = 109;         // 第三方帐号登录
    public static final int ET_CLOUD_REPORT_SCAN = 110;         // 上报扫描的二维码
    public static final int ET_CLOUD_CONFIRM_LOGIN = 111;         // 确认登录

    public static final int ET_GUID = 200;               // 获取GUID
    public static final int ET_SAVE_PORTFOLIO = 202;   // 上传自选
    public static final int ET_QUERY_PORTFOLIO = 203;   // 拉取自选
    public static final int ET_QUERY_STOCK_DETAIL_INFO = 204;         // 获取个股详细信息
    public static final int ET_QUERY_SUBJECT_DETAIL_INFO = 205;       // 获取主题详细信息
    public static final int ET_QUERY_SUBJECT_RELATED_STOCKS = 206;   // 获取主题详细信息
    public static final int ET_GET_FUND_INFO = 208;                 // 获取基金信息
    public static final int ET_QUERY_STOCK_TICK = 209;   // 获取个股成交明细
    public static final int ET_QUERY_CONCEPT_HEAT = 210;   // 获取主题热度曲线
    public static final int ET_QUERY_TRADING_TIME = 211; //获取交易时间
    public static final int ET_QUERY_H5_RES_ZIP_UPGRADE = 212; //获取H5资源包的升级
    public static final int ET_QUERY_DT_LIVE_MSG = 213; //获取灯塔直播消息
    public static final int ET_UPGRADE = 214;            // 请求升级
    public static final int ET_UPGRADE_DB = 215;            // 请求升级，db用的
    public static final int ET_QUERY_STARE_STRATEGY = 216;  // 获取智能盯盘策略分组列表
    public static final int ET_KLINE_CONDITION = 217;            // 日K满足上涨条件


    public static final int ET_SEARCH = 301;                    // 搜索
    public static final int ET_GET_HOT_STOCK = 302;                    // 查询实时热门个股
    public static final int ET_GET_BS_TOP = 303;                    // 查询BS点榜单列表
    public static final int ET_GET_MOCK_UP_LIST = 304;                    // 查询模拟列表

    public static final int ET_FAVOR_UPDATE = 401;             // 添加或删除收藏
    public static final int ET_FAVOR_QUERY = 402;             // 查询收藏
    public static final int ET_FAVOR_CANDICATOR_QUERY = 403;  //查询K线配置
    public static final int ET_FAVOR_CANDICATOR_SAVE = 404;     //保存K线配置

    public static final int ET_REMIND_QUERY_LIST = 501;             // 查询提醒列表
    public static final int ET_REMIND_CLEAR_LIST = 502;             // 清空提醒列表
    public static final int ET_GET_MSG_CLASS_LIST = 503;             // 消息中心列表
    public static final int ET_GET_MSG_DETAIL_LIST = 504;             // 消息中心，消息详情列表

    // app config服务
    public static final int ET_CONFIG_PORTFOLIO_HEADER_INDEXES = 601;             // 自选股里的指数列表
    public static final int ET_CONFIG_GET_IPLIST = 602;                    // 获取iplist
    public static final int ET_STATISTIC = 603;                    // 统计
    public static final int ET_REPORT_USER_INFO = 604;                    // 上报userinfo
    public static final int ET_CONFIG_GET_NEW_ACTIVITIES = 605;                    // 设置里活动的提醒文字和红点信息
    public static final int ET_QUERY_SHARE_URL = 606;                   // 查询分享URL
    public static final int ET_CONFIG_GET_NEW_SHARE = 607;                    // 设置里新股的红点
    public static final int ET_CONFIG_GET_INTELLIGENT_ANSWER = 608;                    // 投顾的红点
    public static final int ET_CONFIG_GET_OPEN_BLESSING_PACK = 609;                    // 开红包动画
    public static final int ET_CONFIG_ACCU_POINT_DESC = 610;                    // 设置界面积分任务的文字描述
    public static final int ET_CONFIG_GET_ANNOUCEMENT_TYPE = 611;                 // 公告的分类信息
    // 智能选股
    public static final int ET_GET_DISCOVER_INTELLIGENT_PICK_STOCK = 701; // 发现模块智能选股
    public static final int ET_GET_DISCOVER_HOT_STOCK = 702; // 发现模块智能选股里获得热门股票
    public static final int ET_GET_DISCOVER_STRATEGY_LIST = 703; // 发现模块智能选股里获得策略列表
    public static final int ET_SELECTED_STRATEGY_LIST = 704; // 策略选股里策略精选列表
    public static final int ET_GET_STOCK_PICK_VALUEADDED_LIST = 705; // 选股里，增值服务列表

    // 选股用户订阅
    public static final int ET_STOCK_PICK_GET_SUBSCRIPTION_LIST = 721; // 获得用户订阅策略列表
    public static final int ET_STOCK_PICK_REMOVE_SUBSCRIPTION = 722; // 删除某个订阅策略

    public static final int ET_MARKET_INFO_AD = 730; // 资讯要闻界面的广告

    // reportLog服务
    public static final int ET_LOG_REPORT = 801; // 上报log

    // 积分
    public static final int ET_ACCUMULATE_POINTS_COMMIT_CODE = 820; // 提交兑换码
    public static final int ET_GET_USER_POINT_INFO = 831; // 获取用户积分信息
    public static final int ET_GET_POINT_TASK_LIST = 832; // 获取积分任务列表
    public static final int ET_REPORT_TASK_FINISHED = 833; // 完成任务上报
    public static final int ET_OPEN_POINT_PRIVILEGE = 834; // 开通特权
    public static final int ET_GET_EXCHANGE_PRIVILEGE_LIST = 835; // 查询积分兑换特权列表

    // 新版视频
    public static final int ET_GET_LIVE_VIDEO_LIST = 853; // 直播室列表
    public static final int ET_GET_RECOMM_VIDEO_LIST = 854; // 投资学堂
    public static final int ET_UPDATE_FAVOR_VIDEO = 855; // 收藏、删除收藏视频
    public static final int ET_GET_FAVOR_VIDEO = 856; // 获取已经收藏的视频

    // 社交
    public static final int ET_GET_FEED_LIST = 901; // 获取用户的消息列表
    public static final int ET_POST_FEED = 902; // 发布消息
    public static final int ET_DELETE_FEED = 903; // 删除消息
    public static final int ET_POST_COMMENT = 904; // 发布评论
    public static final int ET_DELETE_COMMENT = 905; // 删除消息
    public static final int ET_FEED_DO_LIKE = 906; // 点赞
    public static final int ET_FEED_FRIEND_LIST = 907; // 粉丝与关注的好友列表
    public static final int ET_FEED_SET_ATTENTION = 908; // 设置关注，包括添加与取消关注
    public static final int ET_GET_FEED_USER_INFO = 909; // 获取用户的基本信息
    public static final int ET_GET_RELATION_BATCH = 910; // 查询关注关系
    public static final int ET_GET_FEED = 911; // 查询单条feed的详情
    public static final int ET_GET_INVESTMENT_ADVISER_RECOMMENDED_LIST = 912; // 投顾推荐列表
    public static final int ET_GET_INVESTMENT_ADVISER_LIST = 913; // 投顾榜单列表

    // 对外接口
    public static final int ET_GET_CHIP_DIST = 1000; // 获取筹码分布

    // 统计上报接口
    public static final int ET_STAT_REPORT = 1100; // 统计上报


    // 诊股
    public static final int ET_GET_SCORE_DETAIL = 1120; // 获取分值


    // kline K线扩展接口
    public static final int ET_GET_RTMIN_DATA_EX = 1200; // 5日分时数据请求

    // 支付相关
    public static final int ET_GET_MEMBER_FEE_LIST = 1210; // 获取灯塔开通会员费用列表
    public static final int ET_GET_PAY_ORDER_ID = 1211; // 生成灯塔内部订单号
    public static final int ET_GET_ALI_PAY_SIGN = 1212; // 支付宝支付-生成签名
    public static final int ET_GET_WX_PAY_PREPAY_ID = 1213; // 微信支付-生成预支付交易会话ID
    public static final int ET_QUERY_PAY_RESULT = 1214; // 查询支付结果
    public static final int ET_GET_ORDER_RESULT = 1215; // 查询订单支付结果
    public static final int ET_CHECK_USER_COUPON = 1216; // 查询用户是否有可用优惠券
    public static final int ET_GET_USER_COUPON_NUM = 1217; // 查询用户优惠券个数
    public static final int ET_PAY_USER_AGREEMENT = 1218; // 支付订单页面用户协议
    public static final int ET_GET_USER_EVAL_RESULT = 1219; // 获取用户风险评估结果
    public static final int ET_GET_H5_PAY_URL = 1220; // 获取H5支付URL

    public static final int ET_GET_HOT_SPOT_LIST = 1300; // 资讯界面，热点列表

    public static final int ET_STOCK_MAP_GET_SEC_BY_COORDS = 1400; // 股市地图，查询坐标内的个股列表
    public static final int ET_STOCK_MAP_GET_SALEDEPART_BY_COORDS = 1401; // 股市地图，查询坐标内的营业部
    public static final int ET_STOCK_MAP_GET_SALEDEPT_DETAIL = 1402; // 股市地图，查询个股营业部买卖详情
    public static final int ET_STOCK_MAP_GET_COMPANY_BY_COORDS = 1403; // 股市地图，查询坐标内的营业部
    public static final int ET_TEACHER_YAN_WORD = 1044;
    public static final int ET_TALK_FREE = 1045;
    public static final int ET_TEACHER_YAN_CURSE = 1046;
    public static final int ET_TEACHER_YAN_ARTICLE = 1047;
    public static final int ET_MORNING_REF = 1048;
    public static final int ET_ANALYSIS_MARKET = 1049;
    public static final int ET_REQUEST_TEACHER_VIDEO = 1050;
    public static final int ET_REQUEST_MAKE_MONEY_STATE = 1051;
    public static final int ET_TEACHERYAN_COUNT_NUM = 1052;
    public static final int ET_GET_LIKE_INFO = 1053;
    public static final int ET_DO_LIKE_INFO = 1054;
    public static final int ET_GET_FIVEDAY_CAPITAL_DDZ = 1055;   //五日资金博弈

    public static final int ET_GET_PLUGIN = 1500; // 开户插件信息

    // 表哥输入框引导语句
    public static final int ET_INTELLIGENT_INPUT_DEFAULT_TEXT = 2100;
    // 表哥输入输入联想语句
    public static final int ET_INTELLIGENT_INPUT_SUGGESTION_TEXT = 2101;

    // 第三方请求
    public static final int ET_OPEN_API = 2000;

    private Object extra;
    private int entityType;
    private Object entity;

    public EntityObject() {
    }

    public EntityObject(int entityType, Object entity, Object extra) {
        this.entityType = entityType;
        this.entity = entity;
        this.extra = extra;
    }

    /**
     * 取得JSON形式的结果
     * @return
     */
    public String getJSONString() {
        Message msg = (Message)entity;
        if (msg == null) {
            return "";
        } else {
            try {
                return msg.writeToJsonString();
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "entityType=" + entityType + ", entity=" + entity.toString();
    }
}
