package com.sscf.investment.sdk;

import BEC.UserInfo;
import android.content.Context;
import com.sscf.investment.sdk.core.AuthManager;
import com.sscf.investment.sdk.core.DataProvider;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.IPManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by free on 11/1/16.
 *
 * SDK操作入口，核心库，精简高效稳定
 *
 * 单利操作，获取实例方法 SDKManager.getInstance()
 *
 * 重要方法：
 *
 * 1）init(Context context, String appId, String appKey)
 *      初始化SDK
 * 2)
 */
public class SDKManager {
    public static final String TAG = SDKManager.class.getSimpleName();

    private ExecutorService defaultExecutor;

    private Context context;
    private int appId;
    private String appKey;

    private DataProvider dataProvider;

    private UserInfo userInfo;

    private String guid;

    private static SDKManager instance;

    private SDKManager() {
    }

    /**
     * 获取sdk实例
     * @return instance
     */
    public static synchronized SDKManager getInstance() {
        if (instance == null) {
            instance = new SDKManager();
        }
        return instance;
    }

    public void setContext(final Context context) {
        this.context = context;
    }

    /**
     * SDK初始化
     * @param context   应用程序上下文
     * @param appId     灯塔分配的appId，用于鉴权
     * @param appKey    灯塔分配的appKey，用于鉴权
     * @param authComplete 授权结果回调对象，授权检测完成后调用此对象的callback方法，isAuth代表是否获得授权
     */
    public void init(final Context context, int appId, String appKey,
                     AuthManager.IAuthComplete authComplete) {
        this.context = context;
        this.appId = appId;
        this.appKey = appKey;

        // 初始化线程池
        defaultExecutor = Executors.newFixedThreadPool(10);

        // 初始化数据接口提供器
        dataProvider = new DataProvider();

        IPManager.getInstance().init();
        // 初始化数据引擎
        DataEngine.getInstance().init();

        // 初始化鉴权
//        AuthManager.getInstance().init(authComplete);
    }

    /**
     * 释放sdk资源
     */
    public void destroy() {
        DataEngine.getInstance().release();
//        ServerIPManager.getInstance().destroy();
        if (defaultExecutor != null) {
            defaultExecutor.shutdown();
            defaultExecutor = null;
        }
    }

    /**
     * 获取线程池对象
     * @return
     */
    public ExecutorService getDefaultExecutor() {
        return defaultExecutor;
    }

    public Context getContext() {
        return context;
    }

    public int getAppId() {
        return appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public byte[] getGuidBytes() {
        return userInfo == null ? null : userInfo.vGUID;
    }

    //------------------------数据接口 返回JSON数据----------------------------//

    /**
     * 获取股票行情
     * @param secCodeList 灯塔股票代码列表
     * @param callback
     * @return 返回码见ErrorCode
     */
    public int getSecQuote(ArrayList<String> secCodeList, DataSourceProxy.IRequestCallback callback) {
        return dataProvider.getSecQuote(secCodeList, callback);
    }

    /**
     * 获取筹码分布数据
     * @param dtSecCode 灯塔股票代码
     * @param callback  回调对象，用于异步接收请求数据
     * @return 返回码见ErrorCode
     */
    public int getChipDist(String dtSecCode, DataSourceProxy.IRequestCallback callback) {
        return dataProvider.getChipDist(dtSecCode, callback);
    }

    /**
     * 获取相似K线数据
     * @param dtSecCode 灯塔股票代码
     * @param callback  回调对象，用于异步接收请求数据
     * @return 返回码见ErrorCode
     */
    public int getSimulateKLine(String dtSecCode, DataSourceProxy.IRequestCallback callback) {
        return dataProvider.getSimulateKLine(dtSecCode, callback);
    }

    /**
     * 获取个股画像行情
     * @param dtSecCode 灯塔股票代码
     * @param callback  回调对象，用于异步接收请求数据
     * @return 返回码见ErrorCode
     */
    public int getStockPortrait(String dtSecCode, PORTRAIT_DATA_TYPE type, DataSourceProxy.IRequestCallback callback) {
        return dataProvider.getStockPortrait(dtSecCode, type, callback);
    }

    /**
     * 个股画像分类数据请求
     */
    public enum PORTRAIT_DATA_TYPE {
        MARKETTREND,            // 画像相关分时行情
        RELASECUD,              // 关联股票
        SECPERFORMANCE,         // 营业收入
        RELASECPERFORMANCE,     // 利润
        SECVAL,                 // 市盈率市净率
        SECRATE,                // 机构评级
    }
}
