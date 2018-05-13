package com.sscf.investment.utils;

import com.sscf.investment.sdk.utils.CommonConst;

/**
 * Created by xuebinliu on 2015/7/24.
 *
 * 灯塔常量定义
 */
public class DengtaConst extends CommonConst {

    /**
     * 最大可以添加的自选分组个数
     */
    public static final int MAX_GROUP_COUNT = 10;

    // 时间单位定义，超时时间
    public static long REFRESH_TIME_OUT = 5L * MILLIS_FOR_SECOND;

    public static final int DEFAULT_TP_FLAG = 2;

    public static final String KEY_NEW_ACTIVITY_VERSION = "key_new_activity_version";
    public static final String KEY_OPEN_ACCOUNT_VERSION = "key_open_account_version";

    public static final String KEY_INTELLIGENT_ANSWER_VERSION = "key_intelligent_answer_version";

    public static final String KEY_SUBSCRIPTION_UPDATE_TIME = "key_subscription_update_time";

    public static final String KEY_STOCK_LIST = "stock_list";

    public static final int UI_STATE_NORMAL = 0;
    public static final int UI_STATE_LOADING = 1;
    public static final int UI_STATE_NO_CONTENT = 2;
    public static final int UI_STATE_NO_MORE_CONTENT = 3;
    public static final int UI_STATE_FAILED_RETRY = 4;
    public static final int UI_STATE_NO_FOOTER = 5;

    // 友盟的tag
    public static final String ALIAS_ENV_FORMAL = "env_formal";
    public static final String ALIAS_ENV_LOCAL = "env_local";
    public static final String ALIAS_TYPE_ID_ENV = "dtid_env";

    public static final String PUSH_FROM_UMENG = "umeng";
    public static final String PUSH_FROM_HUAWEI = "huawei";

    public static final String ACTION_MESSAGE_TOTAL_UNREAD_COUNT_CHANGED = "action_message_total_unread_count_changed";

    /**
     * 请求feed列表时传递给网络模块以便callback时进行数据拼接
     */
    public static final String KEY_REFRESH_TYPE = "refresh_type";
    public static final String KEY_LAST_OLDEST_ID = "last_oldest_id";

    /**
     * 商品数量
     */
    public static final String EXTRA_COMMODITY_INFO = "commodity_info";

    /**
     * 搜索跳转到个股详情
     */
    public static final String ACTION_SEARCH_TO_SECURITY_DETTAIL = "action_search_to_security_dettail";
}
