package com.sscf.investment.sdk.core;

import android.text.TextUtils;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.utils.DtLog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by free on 11/3/16.
 */
public class AjaxRequest {
    private static final String TAG = AjaxRequest.class.getSimpleName();

    private static final String URL_SIMULATE_KLINE = "https://sec.wedengta.com/getSimilarKLine?action=getSimilarKLine&seccodelist=";
    private static final String URL_STOCK_PORTRAIT = "https://sec.wedengta.com/getSecPortrait?action=%s&seccode=%s";

    // 相似K线请求
    public boolean getSimulateKLine(final String dtSecCode, final DataSourceProxy.IRequestCallback callback) {
        SDKManager.getInstance().getDefaultExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String url = URL_SIMULATE_KLINE + dtSecCode;
                String result = sendHttpRequest(url);
                if (TextUtils.isEmpty(result)) {

                } else {
//                    callback.callback(true, );
                }
            }
        });

        return true;
    }

    // 筹码分布请求
    public boolean getStockPortrait(final String dtSecCode, final SDKManager.PORTRAIT_DATA_TYPE type, final DataSourceProxy.IRequestCallback callback) {
        SDKManager.getInstance().getDefaultExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String url = getPortraitUrl(dtSecCode, type);
                String result = sendHttpRequest(url);
                if (TextUtils.isEmpty(result)) {

                } else {
//                    callback.callback(true, );
                }
            }
        });

        return true;
    }

    /**
     * 发get请求
     * @param reqUrl
     * @return
     */
    private String sendHttpRequest(String reqUrl) {
        DtLog.d(TAG, "sendHttpRequest ajax Url = " + reqUrl);
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); // 设置请求的方式
            connection.setReadTimeout(5000);    // 设置超时的时间
            connection.setConnectTimeout(5000); // 设置链接超时的时间
            connection.setRequestProperty("Content-type", "text/html");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("contentType", "utf-8");
            if (connection.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = in.readLine()) != null){
                    buffer.append(line);
                }
                String result = buffer.toString();
                is.close();
                in.close();

                DtLog.d(TAG, "sendHttpRequest ajax result=" + result);

                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getPortraitUrl(String dtSecCode, SDKManager.PORTRAIT_DATA_TYPE type) {
        String url = "";
        switch (type) {
            case MARKETTREND:            // 画像相关分时行情
                url = String.format(URL_STOCK_PORTRAIT, "MarketTrend", dtSecCode);
                break;
            case RELASECUD:              // 关联股票
                url = String.format(URL_STOCK_PORTRAIT, "RelaSecUd", dtSecCode);
                break;
            case SECPERFORMANCE:         // 营业收入
                url = String.format(URL_STOCK_PORTRAIT, "SecPerformance", dtSecCode);
                break;
            case RELASECPERFORMANCE:     // 利润
                url = String.format(URL_STOCK_PORTRAIT, "RelaSecPerformance", dtSecCode);
                break;
            case SECVAL:                 // 市盈率市净率
                url = String.format(URL_STOCK_PORTRAIT, "SecVal", dtSecCode);
                break;
            case SECRATE:                // 机构评级
                url = String.format(URL_STOCK_PORTRAIT, "SecRate", dtSecCode);
                break;
            default:
                break;
        }
        return url;
    }
}
