package com.sscf.investment.sdk.net;


/**
 * Created by xuebinliu on 2015/7/29.
 */
public class NetworkConst {
    public static final String REQ = "req";
    public static final String RSP = "rsp";

    /** 封包头，4个字节，代表封包总长度 */
    public static final int PACKET_HEAD_LENGTH = 4;
    /** 最大失败次数 */
    public static final int MAX_SERVER_FAIL_COUNT = 5;
    /** 重连延时 */
    public static final int RECONNECT_DELAY = 1000;

    // 发送线程能处理的消息
    /** 初始化并连接socket */
    public static final int SOCKET_CONNECT = 1;
    /** 处理查询信息 */
    public static final int SOCKET_QUERY = 2;
    /** 发送字节流给服务器 */
    public static final int SOCKET_SEND = 3;
    /** 关闭socket */
    public static final int SOCKET_CLOSE = 4;
    /** 尝试socket */
    public static final int SOCKET_TRY_CONNECT = 5;
    /** 关闭线程 */
    public static final int THREAD_RELEASE = 6;

    // 接收线程能处理的消息
    /** 处理接收信息 */
    public static final int SOCKET_RECEIVE = 1;
    /** 接受消息 -- 处理收发线程超时 */
    public static final int UI_RECEIVE_TIMEOUT = 5;

    // 长连接相关参数
    /** socket连接建立的超时时间(毫秒) */
    public static final int SOCKET_CONNECT_TIMEOUT = 5 * 1000;
    /** socket一次读取数据长度*/
    public static final int READ_BUFFER_LEN = 4096;
}
