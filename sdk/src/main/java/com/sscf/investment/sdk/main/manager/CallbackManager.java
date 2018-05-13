package com.sscf.investment.sdk.main.manager;

import com.sscf.investment.sdk.utils.DtLog;

import java.lang.ref.SoftReference;
import java.util.*;

/**
 * Created by xuebinliu on 2015/8/11.
 * 单例
 */
public class CallbackManager {
    private static final String TAG = CallbackManager.class.getSimpleName();

    public static final int CM_TYPE_PORTFOLIO_DATA_CHANGE = 1;  // 服务器拉到自选数据，需要更新UI

    /**
     * 回调对象管理, modeType<-->List<SoftReference<DtCallback>>
     */
    private Map<Integer, List<SoftReference<DtCallback>>> mCallbackMap = new HashMap<Integer, List<SoftReference<DtCallback>>>();
    private Object CALLBACK_MAP_LOCK = new Object();

    private static CallbackManager instance;

    private CallbackManager() {
    }

    public static synchronized  CallbackManager getInstance() {
        if (instance == null) {
            instance = new CallbackManager();
        }
        return instance;
    }

    /**
     * 注册回调，用于监听模块变化
     * @param modeType 要监听的模块类型
     * @param dtCallback 数据变化时的回调
     */
    public void regCallback(int modeType, DtCallback dtCallback) {
        if (modeType < 0 || dtCallback == null) {
            return;
        }

        synchronized (CALLBACK_MAP_LOCK) {
            if (mCallbackMap.containsKey(modeType)) {
                mCallbackMap.get(modeType).add(new SoftReference(dtCallback));
            } else {
                List<SoftReference<DtCallback>> callbackList = new ArrayList<SoftReference<DtCallback>>();
                callbackList.add(new SoftReference(dtCallback));
                mCallbackMap.put(modeType, callbackList);
            }

        }
    }

    /**
     * 反注册
     * @param modeType
     * @param dtCallback
     */
    public void unRegCallback(int modeType, DtCallback dtCallback) {
        DtLog.d(TAG, "unRegCallback modeType=" + modeType + ", dtCallback=" + dtCallback);
        if (modeType < 0 || dtCallback == null) {
            return;
        }

        synchronized (CALLBACK_MAP_LOCK) {
            if (mCallbackMap.containsKey(modeType)) {
                List<SoftReference<DtCallback>> callbackList = mCallbackMap.get(modeType);
                for (int i = 0; i < callbackList.size(); i++) {
                    SoftReference<DtCallback> softCallback = callbackList.get(i);
                    if (softCallback.get() == dtCallback) {
                        callbackList.remove(i);
                        DtLog.d(TAG, "unRegCallback remove modeType=" + modeType + ", dtCallback=" + dtCallback);
                        break;
                    }
                }
            }
        }
    }

    public void notify(int modeType, Object obj) {
        DtLog.d(TAG, "notify modeType=" + modeType);

        DtCallback dtCallback = null;
        synchronized (CALLBACK_MAP_LOCK) {
            if (mCallbackMap.containsKey(modeType)) {
                List<SoftReference<DtCallback>> callbackList = mCallbackMap.get(modeType);
                for (int i = 0; i < callbackList.size(); i++) {
                    SoftReference<DtCallback> softCallback = callbackList.get(i);
                    if (softCallback != null && softCallback.get() != null) {
                        dtCallback = softCallback.get();
                    }
                }
            }
        }

        if (dtCallback != null) {
            DtLog.d(TAG, "notify callback modeType=" + modeType);
            dtCallback.onDataChange(obj);
        } else {
            // 打印队列信息
            dumpCallbackList(modeType);
        }
    }

    private void dumpCallbackList(int modeType) {
        DtLog.w(TAG, "dumpCallbackList modeType=" + modeType);
        synchronized (CALLBACK_MAP_LOCK) {
            if (mCallbackMap.containsKey(modeType)) {
                List<SoftReference<DtCallback>> callbackList = mCallbackMap.get(modeType);
                for (int i = 0; i < callbackList.size(); i++) {
                    SoftReference<DtCallback> softCallback = callbackList.get(i);
                    DtLog.d(TAG, "dumpCallbackList callback=" + (softCallback != null ? softCallback.get() : "null"));
                }
            } else {
                DtLog.w(TAG, "dumpCallbackList not containsKey modeType=" + modeType);
            }
        }
    }

    public interface DtCallback {
        void onDataChange(Object obj);
    }
}
