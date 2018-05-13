package com.sscf.investment.sdk.net;


import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xuebinliu on 2015/7/28.
 * 数据引擎：统一请求入口，行情数据源、业务数据源管理
 */
public final class DataEngine {
    public static final String TAG = "DataEngine";

    // 请求超时检测
    private final int TIME_OUT_COUNT = 2;           // 超时次数，超过2次超时则取消请求
    public static final int TIME_INTERNAL = 2000;   // 超时检测间隔，每2s检测一次
    private Timer mTimeoutTimer;                    // 超时检测定时器

    // 业务数据源
    private DataSourceProxy businessCurrentSourceProxy;
    private SocketDataSource businessSocketDataSource;

    // 行情数据源
    private DataSourceProxy quoteCurrentSourceProxy;
    private SocketDataSource quoteSocketDataSource;

    private static DataEngine instance = null;

    private DataEngine() {
        DtLog.d(TAG, "DataEngine constructor");
    }

    public static synchronized DataEngine getInstance() {
        if (instance == null) {
            instance = new DataEngine();
        }
        return instance;
    }

    /**
     * 初始化网络连接
     */
    public synchronized void init() {
        DtLog.d(TAG, "init start");

        // 业务数据源初始化
        if (businessSocketDataSource == null) {
            DtLog.d(TAG, "init businessSocketDataSource");
            businessSocketDataSource = new SocketDataSource(false);
            businessCurrentSourceProxy = businessSocketDataSource;
        } else {
            DtLog.w(TAG, "businessCurrentSourceProxy already init");
        }

        // 行情数据源初始化
        if (quoteSocketDataSource == null) {
            DtLog.d(TAG, "init quoteSocketDataSource");
            quoteSocketDataSource = new SocketDataSource(true);
            quoteCurrentSourceProxy = quoteSocketDataSource;
        } else {
            DtLog.w(TAG, "quoteSocketDataSource already init");
        }

        if (mTimeoutTimer != null) {
            mTimeoutTimer.cancel();
            DtLog.w(TAG, "cancel mTimeoutTimer");
        }

        DtLog.d(TAG, "init timer");
        mTimeoutTimer = new Timer();
        mTimeoutTimer.scheduleAtFixedRate(new TimeoutTask(), TIME_INTERNAL, TIME_INTERNAL);
        DtLog.d(TAG, "init end");
    }

    /**
     * 释放数据引擎的资源
     */
    public void release() {
        DtLog.d(TAG, "release businessSocketDataSource");
        if (businessSocketDataSource != null) {
            businessSocketDataSource.clearAllRequest();
            businessSocketDataSource.destroy();
            businessSocketDataSource.releaseThread();
            businessSocketDataSource = null;
        }
        businessCurrentSourceProxy = null;

        DtLog.d(TAG, "release quoteSocketDataSource");
        if (quoteSocketDataSource != null) {
            quoteSocketDataSource.clearAllRequest();
            quoteSocketDataSource.destroy();
            quoteSocketDataSource.releaseThread();
            quoteSocketDataSource = null;
        }
        quoteCurrentSourceProxy = null;

        if (mTimeoutTimer != null) {
            DtLog.d(TAG, "release Timer");
            mTimeoutTimer.cancel();
            mTimeoutTimer = null;
        }
    }

    /**
     * 普通请求，一个请求对应一个响应
     * @param reqType 请求操作的类型，在EntityObject中定义
     * @param reqData 实际请求数据
     * @param callback 回调
     * @return true，请求已入队列
     */
    public boolean request(int reqType, Object reqData, DataSourceProxy.IRequestCallback callback) {
        return request(reqType, reqData, callback, null);
    }

    /**
     * 普通请求，一个请求对应一个响应
     * @param reqType 请求操作的类型，在EntityObject中定义
     * @param reqData 实际请求数据
     * @param callback 回调
     * @return true，请求已入队列
     */
    public boolean request(int reqType, Object reqData, DataSourceProxy.IRequestCallback callback, Object extra) {
        if (!NetUtil.isNetWorkConnected(SDKManager.getInstance().getContext())) {
            DtLog.w(TAG, "no network, cancel request");
            callback.callback(false, new EntityObject(reqType, null, extra));
            return false;
        }

        switch (reqType) {
            case EntityObject.ET_GET_QUOTE:
            case EntityObject.ET_GET_SIMPLE_QUOTE:
            case EntityObject.ET_GET_KLINE_DATA:
                // 走行情服务器
                if (quoteSocketDataSource == null) {
                    init();
                }
                return quoteSocketDataSource.request(reqType, reqData, callback, extra);
            default:
                if (businessCurrentSourceProxy == null) {
                    init();
                }
                // 走业务服务器
                return businessCurrentSourceProxy.request(reqType, reqData, callback, extra);
        }
    }

    /**
     * 检查当前数据源的请求队列每个请求的超时情况
     */
    public class TimeoutTask extends TimerTask {
        @Override
        public void run() {
            try {
                if (businessCurrentSourceProxy != null) {
                    List<Integer> reqIdList = businessCurrentSourceProxy.getAllRequestId();
                    for (int i = 0; i < reqIdList.size(); i++) {
                        int reqId = reqIdList.get(i);
                        DataSourceProxy.ReqParams req = businessCurrentSourceProxy.getReqParams(reqId);
                        if (req != null) {
                            if (req.timeout > TIME_OUT_COUNT) {
                                DtLog.w(TAG, "Ali request timeout, id=" + reqId  + ", type=" + req.reqType  + ", reqType=" + req.reqType + ", will cancel all request & close connection");
                                businessCurrentSourceProxy.cancelAllRequest();
                                businessCurrentSourceProxy.destroy();
                                break;
                            } else {
                                businessCurrentSourceProxy.addTimeoutCount(reqId);
                                DtLog.d(TAG, "Ali check request id=" + reqId  + ", type=" + req.reqType + ",check timeout=" + req.timeout);
                            }
                        }
                    }
                } else {
                    DtLog.w(TAG, "TimeoutTask businessCurrentSourceProxy == null");
                }
            } catch (Exception e) {
                DtLog.e(TAG, "Ali TimeoutTask exception caught: " + e.getMessage());
            }

            try {
                if (quoteCurrentSourceProxy != null) {
                    List<Integer> reqIdList = quoteCurrentSourceProxy.getAllRequestId();
                    for (int i = 0; i < reqIdList.size(); i++) {
                        int reqId = reqIdList.get(i);
                        DataSourceProxy.ReqParams req = quoteCurrentSourceProxy.getReqParams(reqId);
                        if (req != null) {
                            if (req.timeout > TIME_OUT_COUNT) {
                                DtLog.w(TAG, "Tencent request timeout, id=" + reqId  + ", type=" + req.reqType  + ", reqType=" + req.reqType + ", will cancel all request & close connection");
                                quoteCurrentSourceProxy.cancelAllRequest();
                                quoteCurrentSourceProxy.destroy();
                                break;
                            } else {
                                quoteCurrentSourceProxy.addTimeoutCount(reqId);
                                DtLog.d(TAG, "Tencent check request id=" + reqId  + ", type=" + req.reqType + ",check timeout=" + req.timeout);
                            }
                        }
                    }
                } else {
                    DtLog.w(TAG, "TimeoutTask quoteCurrentSourceProxy == null");
                }
            } catch (Exception e) {
                DtLog.e(TAG, "Tencent TimeoutTask exception caught: " + e.getMessage());
            }
        }
    }
}
