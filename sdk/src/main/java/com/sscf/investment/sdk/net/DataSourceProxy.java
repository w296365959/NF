package com.sscf.investment.sdk.net;


import com.sscf.investment.sdk.utils.DtLog;

import java.util.*;

/**
 * Created by xuebinliu on 2015/7/30.
 */
public abstract class DataSourceProxy {
    private static final String TAG = "DataEngine";

    // 请求数据
    private Object REQUEST_MAP_LOCK = new Object();
    private Map<Integer, ReqParams> mRequestMap = new HashMap<>();

    // 请求id
    private Object REQ_ID_LOCK = new Object();
    private int reqId = 0;

    /**
     * 请求参数保存
     */
    public class ReqParams {
        public ReqParams(int reqType, IRequestCallback callback, Object extra) {
            this.callback = callback;
            this.reqType = reqType;
            this.extra = extra;
            this.timeout = 0;
        }

        public IRequestCallback callback;
        public int reqType;
        public int timeout;
        public Object extra;
        public int openApiType;
    }

    /**
     * 产生唯一请求id
     * @return
     */
    public int generateReqId(){
        synchronized (REQ_ID_LOCK) {
            return reqId++;
        }
    }

    /**
     * 获取所有请求id
     * @return
     */
    public List<Integer> getAllRequestId() {
        List<Integer> reqIdList = new ArrayList<>();
        synchronized (REQUEST_MAP_LOCK) {
            Set<Integer> keys = mRequestMap.keySet();
            Iterator<Integer> keyIterator = keys.iterator();
            while (keyIterator.hasNext()) {
                reqIdList.add(keyIterator.next());
            }
        }
        return reqIdList;
    }

    /**
     * 根据请求id获取请求记录
     * @param reqId
     * @return
     */
    public ReqParams getReqParams(int reqId) {
        synchronized (REQUEST_MAP_LOCK) {
            if (mRequestMap.containsKey(reqId)) {
                return mRequestMap.get(reqId);
            }
        }
        return null;
    }

    /**
     * 清空所有请求记录
     */
    public void clearAllRequest() {
        synchronized (REQUEST_MAP_LOCK) {
            mRequestMap.clear();
        }
    }

    /**
     * 通知所有请求失败，并清空请求记录
     */
    public void cancelAllRequest() {
        try {
            List<Integer> keys = getAllRequestId();
            for (Integer key : keys) {
                ReqParams reqParams;
                synchronized (REQUEST_MAP_LOCK) {
                    reqParams = mRequestMap.get(key);
                }
                if (reqParams != null && reqParams.callback != null) {
                    DtLog.w(TAG, "cancel request reqType=" + reqParams.reqType + ", reqId=" + key);
                    reqParams.callback.callback(false, new EntityObject(reqParams.reqType, null, reqParams.extra));
                }
            }
        } catch (Exception e) {
            DtLog.e(TAG, "cancelAllRequest e=" + e.getMessage());
        }
        clearAllRequest();
    }

    /**
     * 为请求递增超时
     * @param reqId
     */
    public void addTimeoutCount(int reqId) {
        synchronized (REQUEST_MAP_LOCK) {
            if (mRequestMap.containsKey(reqId)) {
                mRequestMap.get(reqId).timeout++;
            }
        }
    }

    /**
     * 记录请求
     * @param reqId
     * @param reqParams
     */
    public void putCallback(int reqId, ReqParams reqParams) {
        synchronized (REQUEST_MAP_LOCK) {
            mRequestMap.put(reqId, reqParams);
        }
    }

    /**
     * 取得请求记录，同时移除
     * @param reqId
     * @return
     */
    public ReqParams popCallback(int reqId) {
        synchronized (REQUEST_MAP_LOCK) {
            return mRequestMap.remove(reqId);
        }
    }

    /**
     * 普通请求，一个请求对应一个响应
     * @param reqType 请求操作的类型，在EntityObject中定义
     * @param reqData 实际请求数据
     * @param observer 回调
     * @return true，请求已入队列
     */
    public abstract boolean request(int reqType, Object reqData, IRequestCallback observer, Object extra);

    /**
     * 停止数据源，释放资源
     */
    public abstract void destroy();

    /**
     * 请求回调接口
     */
    public interface IRequestCallback {
        /**
         * 回调方法
         * @param success true代表请求成功
         * @param data 请求成功时，返回的数据
         */
        void callback(boolean success, EntityObject data);
    }
}
