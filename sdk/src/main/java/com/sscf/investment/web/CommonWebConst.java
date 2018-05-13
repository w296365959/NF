package com.sscf.investment.web;

/**
 * Created by xuebinliu on 10/16/15.
 */
public class CommonWebConst {

    // 打开web activity的参数
    public static final String URL_ADDR = "url_addr";
    public static final String EXTRA_NEWS = "extra_news";
    public static final String WEB_TYPE = "web_type";
    public static final String SUPPORT_THEME = "support_theme";
    public static final String CONTENT = "content";

    //滑动返回手势类型
    public static final String SWIPE_BACK_TYPE = "swipe_back_type";

    public static final int SWIPE_BACK_TYPE_DISALLOWED = 0;
    public static final int SWIPE_BACK_TYPE_FULLSCREEN = 1;
    public static final int SWIPE_BACK_TYPE_FROM_LEFT_EDGE = 2;
    public static final int DEFAULT_SWIPE_BACK_TYPE = SWIPE_BACK_TYPE_FULLSCREEN;

    /**
     * 请使用WT_ACTIVITIES的类型
     */
    @Deprecated
    public static final int WT_PORTRAIT = 3;    // 个股画像
    @Deprecated
    public static final int WT_DISCOVERY_RINTELLI = 6;   //发现智能选股
    @Deprecated
    public static final int WT_USER_AGREEMENT = 7;   // 用户协议,服务协议
    @Deprecated
    public static final int WT_FEATRUE_INTRODUCE = 8;   // 特性介绍
    @Deprecated
    public static final int WT_FUNCTION_INTRODUCE = 9;   // 功能介绍

    public static final int WT_FEEDBACK = 10;   // 用户反馈
    public static final int WT_COMMON_WEB = 11;   // 通用的web
    public static final int WT_FULL_SCREEN_WEB = 12;   // 全屏的webview

    public static final int WT_NEWS = 0;        // 新闻
    public static final int WT_REPORT = 1;      // 研报
    public static final int WT_ANNONCEMENT = 2; // 公告
    public static final int WT_DISCOVERY = 4;   // 发现
    public static final int WT_DISCOVERY_MARKET = 5;   // 发现情报
    public static final int WT_TEACHER_YAN = 6;//

    public static final String URL_FAQ_DT_INDEX = "file:///android_asset/faqDtIndex.html";
    public static final String URL_FAQ_FUND_EXP = "file:///android_asset/faqFundExp.html";
    public static final String URL_FAQ_FUND_INTRODUCE = "file:///android_asset/function_introduce/faqFunctionIntro.html";
    public static final String URL_FAQ_MARGIN_TRADING_EXP = "file:///android_asset/faqMarginTradingExp.html";
    public static final String URL_FEEDBACK = "file:///android_asset/feedback/feedback.html";

    public static final String ACCOUNT_DT_ID = "dtid";
    public static final String ACCOUNT_DT_TICKET = "dtticket";

    public static final String KEY_LIVE_TYPE = "key_live_type";

    public static final String KEY_SHARE_TITLE = "key_share_title";

    public static final String KEY_ARTICLE_ID = "key_article_id";
}
