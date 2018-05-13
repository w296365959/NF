package com.sscf.investment.sdk.utils;

/**
 * Created by yorkeehuang on 2017/6/9.
 */

public class CommonConst {
    // 常用key
    public static final String KEY_SEC_NAME = "secName";
    public static final String KEY_SEC_CODE = "secCode";
    public static final String KEY_SEC_LIST = "secList";

    /**
     * 最大可以添加的自选股个数
     */
    public static final int MAX_PORTFOLIO_COUNT = 200;

    // 通用extra
    /**
     * account ID
     */
    public static final String EXTRA_ACCOUNT_ID = "accountId";

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_PUSH_REMIND = "extra_push_remind";
    public static final String EXTRA_FROM = "extra_from";
    public static final String EXTRA_TITLE = "extra_title";

    /**
     * 拍照类型
     */
    public static final String EXTRA_CAPTURE_MODE = "capture_mode";
    /**
     * 日K数据
     */
    public static final String EXTRA_DAILAY_KLINE_DATA = "day_kline_data";
    /**
     * 日K数据开始索引
     */
    public static final String EXTRA_DAILAY_KLINE_START = "day_kline_start";
    /**
     * 灯塔特权类型
     */
    public static final String EXTRA_PRIVILEGE_TYPE = "privilege_type";

    public static final String EXTRA_DATA = "extra_data";

    // request code
    public static final int REQUEST_CODE_SCAN = 1;
    public static final int REQUEST_FULLSCREEN_LINECHART = 2;
    public static final int REQUEST_SEARCH_PICK = 3;

    // capture mode
    public static final int MODE_QR_CODE = 1;
    public static final int MODE_TAKE_PICTURE = 2;

    // 好友界类型
    public static final int TYPE_ATTENTION = 0;
    public static final int TYPE_FANS = 1;

    public static final int PROTOCAL_NOT_HANDLE = -1;
    public static final int PROTOCAL_HANDLE_SUCCESS = 0;
    public static final int PROTOCAL_ERROR_BEACON_NOT_SUPPORT = 1;
    public static final int PROTOCAL_ERROR_URL_BLANK = 2;

    public static final String ACTION_BEACON_PROTOCAL_ERROR = "action_beacon_protocal_error";

    public static final String CHARTSET_UTF8 = "UTF8";

    /**
     * 短信验证码主要用于什么功能，修改手机号，绑定手机号，忘记密码
     */
    public final static String EXTRA_FUNCTION_TYPE = "sms_code_function_type";
    public final static String EXTRA_SHOW_CLOSE = "extra_show_close";
    public final static String EXTRA_PASSWORD = "password";
    public final static String EXTRA_CELLPHONE = "cellphone";
    public final static String EXTRA_SMS_CODE = "sms_code";
    public final static String EXTRA_OPEN_ID = "openid";
    public final static String EXTRA_ACCESS_TOKEN = "access_token";
    public final static String EXTRA_WECHAT_UNIONID = "unionid";
    public static final String EXTRA_SINA_UID = "uid";
    public static final String EXTRA_SCREEN_NAME = "screen_name";
    public static final String EXTRA_PROFILE_IMAGE_URL = "profile_image_url";

    /**
     * 修改手机号需要短信验证码
     */
    public final static int FUNCTION_MODIFY_CELLPHONE = 1;
    /**
     * 绑定手机号需要短信验证码
     */
    public final static int FUNCTION_BIND_CELLPHONE = 2;
    /**
     * 忘记密码需要短信验证码
     */
    public final static int FUNCTION_FORGET_PASSWORD = 3;

    public static final String KEY_NEWS_TYPE = "news_type";
    public static final String KEY_GET_SOURCE = "get_source";

    public static final String KEY_PRIVILEGE_TYPE = "privilegeType";

    public static final String KEY_FEED_ID = "feedId";
    public static final String KEY_FEED_TYPE = "feedType";
    public static final String KEY_REPLY_COMMENT_INFO = "replyCommentInfo";
    public static final String KEY_REPLY_ACCOUNT_ID = "replyAccountId";
    public static final String KEY_REPLY_COMMENT_ID = "replyCommentId";
    public static final String KEY_REPLY_NICK_NAME = "replyNickName";

    /**
     * 订单信息
     */
    public static final String EXTRA_ORDER_INFO = "orderInfo";
    /**
     * 风险评测
     */
    public static final String EXTRA_RISK_EVAL = "risk_eval";
    /**
     * 数字签名
     */
    public static final String EXTRA_NEED_SIGN = "need_sign";

    // 登录成功
    public static final String ACTION_LOGIN_SUCCESS = "ACTION_LOGIN_SUCCESS";
    // 注销成功
    public static final String ACTION_LOGOUT_SUCCESS = "ACTION_LOGOUT_SUCCESS";
    // 账户信息改变
    public static final String ACTION_UPDATE_ACCOUNT_INFO = "ACTION_UPDATE_ACCOUNT_INFO";

    /**
     * 优惠券信息
     */
    public static final String EXTRA_COUPON_INFO = "coupon_info";

    /**
     * H5支付结果
     */
    public static final String EXTRA_H5_PAY_RESULT = "h5_pay_result";

    /**
     * 支付签名结果
     */
    public static final String EXTRA_SIGN_RESULT = "sign_result";

    // 时间单位定义
    public static long MILLIS_FOR_SECOND = 1000L;

    // 时间单位定义
    public static long DAY_FOR_SECOND = 24L * 60 * 60 * MILLIS_FOR_SECOND;

    // 收藏更新
    public static final String ACTION_FAVOR_CHANGED = "action_favor_changed";
    // ticket过期
    public static final String ACTION_TICKET_ERROR = "action_ticket_error";
    // 视频收藏更新
    public static final String ACTION_VIDEO_FAVOR_CHANGED = "action_video_favor_changed";

    /**
     * 切换主题的action
     */
    public static final String ACTION_SWITCH_THEME = "action_switch_theme";

    public static final String DENGTA_DT_CODE = "2005888001";
    public static final String SHANGHAI_INDEX_DT_CODE = "0105000001";
    public static final String SHENZHEN_INDEX_DT_CODE = "0005399001";
    public static final String SMALL_AND_MEDIUM_SIZED_BOARD_INDEX_DT_CODE = "0005399005";
    public static final String GROWTH_ENTERPRISE_INDEX_DT_CODE = "0005399006";

    public static final String EXTRA_SEARCH_PICK = "search_pick_extra";

    public static final String EXTRA_PLAY_AUDIO = "extra_play_audio";

}
