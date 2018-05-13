package com.sscf.investment.sdk.stat;

/**
 * Created by yorkeehuang on 2017/6/13.
 */

public class StatConsts {

    //整体操作
    // 整体使用
    /**
     * 停留时长
     */
    public static final int TOTAL_FRONT_TIME = 0;

    // 行情操作
    // 技术指标查看
    /**
     * 切换技术指标
     */
    public static final int SWITCH_INDICATOR_TYPE = 0;

    // 其他操作
    // 消息查看 - 通知栏查看类型
    /**
     * 通知要闻推送
     */
    public static final int DISC_NEWS = 0;
    /**
     * 通知股价预警
     */
    public static final int SEC_PRICE = 1;
    /**
     * 通知公告研报
     */
    public static final int ANNOUNCEMENT = 2;
    /**
     * 通知自选日报
     */
    public static final int DAILY_REPORT = 3;
    /**
     * 通知牛人动态
     */
    public static final int CONSULTANT = 4;
    /**
     * 通知互动消息
     */
    public static final int INTERACTION = 5;
    /**
     * 通知新股提醒
     */
    public static final int NEW_STOCK = 6;
    /**
     * 通知活动提醒
     */
    public static final int ACTIVITY = 7;
    /**
     * 通知增值服务
     */
    public static final int VALUE_ADDED_SERVICE = 8;

    // 行情页面访问
    /**
     * 沪深
     */
    public static final int CHINA = 0;
    /**
     * 港股
     */
    public static final int HONGKONG = 1;
    /**
     * 沪港通
     */
    public static final int SHANGHAI_HONGKONG = 2;
    /**
     * 美股
     */
    public static final int AMERICA = 3;
    /**
     * 全球
     */
    public static final int GLOBAL = 4;

    // 个股页面访问
    /**
     * 沪深个股
     */
    public static final int CHINA_STOCK = 0;
    /**
     * 其他个股
     */
    public static final int OTHER_STOCK = 1;
    /**
     * 沪深指数
     */
    public static final int CHINA_INDEX = 2;
    /**
     * 其他指数
     */
    public static final int OTHER_INDEX = 3;

    // 分时K线查看
    /**
     * 分时
     */
    public static final int TIME_LINE = 0;
    /**
     * 五日分时
     */
    public static final int FIVE_DAY = 1;
    /**
     * 日K
     */
    public static final int DAILY_KLINE = 2;
    /**
     * 其他K线
     */
    public static final int OTHER_KLINE = 3;

    // 要闻页面访问
    /**
     * 要闻
     */
    public static final int MARKET_NEWS_INFO = 0;
    /**
     * 热点
     */
    public static final int MARKET_NEWS_HOT_SPOT = 1;
    /**
     * 视频
     */
    public static final int MARKET_NEWS_VIDEO = 2;
    /**
     * 牛人
     */
    public static final int MARKET_NEWS_CONSULTANT_OPINION = 3;
    /**
     * 滚动
     */
    public static final int MARKET_NEWS_FLASH_NEWS = 4;

    // 资讯页面访问
    /**
     * 牛人
     */
    public static final int STOCK_INFO_CONSULTANT_OPINION = 0;
    /**
     * 新闻
     */
    public static final int STOCK_INFO_NEWS = 1;
    /**
     * 资金
     */
    public static final int STOCK_INFO_CAPITAL = 2;
    /**
     * 诊股
     */
    public static final int STOCK_INFO_DIAGNOSIS = 3;
    /**
     * 公告
     */
    public static final int STOCK_INFO_NOTICE = 4;
    /**
     * 研报
     */
    public static final int STOCK_INFO_REPORT = 5;
    /**
     * 财务
     */
    public static final int STOCK_INFO_FINANCE = 6;
    /**
     * 简况
     */
    public static final int STOCK_INFO_INTRO = 7;

    public static final int STOCK_MORNING_REF = 8;


    // 选股页面访问
    /**
     * 策略精选
     */
    public static final int DISCOVER_STOCK_PICK = 0;
    /**
     * 策略选股
     */
    public static final int DISCOVER_STRATEGY_STOCK_PICK = 1;
    /**
     * 我的订阅
     */
    public static final int DISCOVER_SUBSCRIPTION = 2;

    public static class Extra {
        public static final String EXTRA_START = "0";
        public static final String EXTRA_END = "1";
    }
}
