package com.sscf.investment.sdk.core;

import BEC.ChipDistReq;
import BEC.QuoteReq;
import android.text.TextUtils;
import com.sscf.investment.sdk.ErrorCode;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;

import java.util.ArrayList;

/**
 * Created by free on 11/2/16.
 *
 * 提供给券商的数据接口实现
 */
public class DataProvider {
    public static final String TAG = DataProvider.class.getSimpleName();

    private AjaxRequest ajaxRequest;


    public DataProvider() {
        DtLog.d(TAG, "constructor");

        ajaxRequest = new AjaxRequest();
    }

    /**
     * 获取股票行情 socket
     * @param secCodeList 灯塔股票代码列表
     * @param callback
     * @return 返回码见ErrorCode
     */
    public int getSecQuote(ArrayList<String> secCodeList, DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "getSecQuote start secCodeList=" + secCodeList);

        // 参数检测
        if (secCodeList == null || secCodeList.size() < 1 || callback == null) {
            return ErrorCode.INVALID_PARAMS;
        }

        // 网络检测
        if (!NetUtil.isNetWorkConnected(SDKManager.getInstance().getContext())) {
            return ErrorCode.NETWORK_ERROR;
        }

        // 授权检测
        if (!AuthManager.getInstance().isAuth(AuthConst.AUTH_TYPE_CHIP_DIST)) {
            return ErrorCode.AUTH_FAILED;
        }

        // 发起请求
        QuoteReq req = new QuoteReq();
        req.setVGuid(AuthManager.getInstance().getGuid());
        req.setVDtSecCode(secCodeList);
        DataEngine.getInstance().request(EntityObject.ET_GET_SIMPLE_QUOTE, req, callback);

        DtLog.d(TAG, "getSecQuote submit success, secCodeList=" + secCodeList);

        return ErrorCode.SUCCESS;
    }

    /**
     * 获取筹码分布数据 socket
     * @param dtSecCode
     * @param callback
     * @return 返回码见ErrorCode
     */
    public int getChipDist(String dtSecCode, DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "getChipDist start dtSecCode=" + dtSecCode);

        // 参数检测
        if (TextUtils.isEmpty(dtSecCode) || callback == null) {
            return ErrorCode.INVALID_PARAMS;
        }

        // 网络检测
        if (!NetUtil.isNetWorkConnected(SDKManager.getInstance().getContext())) {
            return ErrorCode.NETWORK_ERROR;
        }

        // 授权检测
        if (!AuthManager.getInstance().isAuth(AuthConst.AUTH_TYPE_CHIP_DIST)) {
            return ErrorCode.AUTH_FAILED;
        }

        // 发起请求
        ChipDistReq req = new ChipDistReq();
        req.setSDtSecCode(dtSecCode);
        req.setStUserInfo(AuthManager.getInstance().getUserInfo());
        DataEngine.getInstance().request(EntityObject.ET_GET_CHIP_DIST, req, callback);

        DtLog.d(TAG, "getChipDist submit success, dtSecCode=" + dtSecCode);

        return ErrorCode.SUCCESS;
    }

    /**
     * 获取相似K线数据 ajax
     * @param dtSecCode
     * @param callback
     * @return 返回码见ErrorCode
     */
    public int getSimulateKLine(String dtSecCode, DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "getSimulateKLine start, dtSecCode=" + dtSecCode);

        // 参数检测
        if (TextUtils.isEmpty(dtSecCode) || callback == null) {
            return ErrorCode.INVALID_PARAMS;
        }

        // 网络检测
        if (!NetUtil.isNetWorkConnected(SDKManager.getInstance().getContext())) {
            return ErrorCode.NETWORK_ERROR;
        }

        // 授权检测
        if (!AuthManager.getInstance().isAuth(AuthConst.AUTH_TYPE_SIM_KLINE)) {
            return ErrorCode.AUTH_FAILED;
        }

        // 发起请求
        ajaxRequest.getSimulateKLine(dtSecCode, callback);

        DtLog.d(TAG, "getSimulateKLine submit success, dtSecCode=" + dtSecCode);

        return ErrorCode.SUCCESS;
    }

    /**
     * 获取个股画像数据 ajax
     * @param dtSecCode
     * @param callback
     * @return 返回码见ErrorCode
     */
    public int getStockPortrait(String dtSecCode, SDKManager.PORTRAIT_DATA_TYPE type, DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "getStockPortrait start dtSecCode=" + dtSecCode);

        // 参数检测
        if (TextUtils.isEmpty(dtSecCode) || callback == null) {
            return ErrorCode.INVALID_PARAMS;
        }

        // 网络检测
        if (!NetUtil.isNetWorkConnected(SDKManager.getInstance().getContext())) {
            return ErrorCode.NETWORK_ERROR;
        }

        // 授权检测
        if (!AuthManager.getInstance().isAuth(AuthConst.AUTH_TYPE_SIM_KLINE)) {
            return ErrorCode.AUTH_FAILED;
        }

        // 发起请求
        ajaxRequest.getStockPortrait(dtSecCode, type, callback);

        DtLog.d(TAG, "getStockPortrait submit success, dtSecCode=" + dtSecCode);

        return ErrorCode.SUCCESS;
    }
}
