package com.sscf.investment.sdk;

/**
 * Created by free on 11/2/16.
 * 数据接口API返回值定义
 */
public class ErrorCode {

    /**
     * 调用成功
     */
    public static final int SUCCESS = 0;

    /**
     * 无授权
     */
    public static final int AUTH_FAILED = 1;

    /**
     * 无效参数
     */
    public static final int INVALID_PARAMS = 2;

    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 3;

    /**
     * 内部错误
     */
    public static final int INNER_ERROR = 4;
}
