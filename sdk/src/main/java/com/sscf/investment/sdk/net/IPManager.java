package com.sscf.investment.sdk.net;

import BEC.*;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.SettingPref;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by xuebinliu on 14/08/2017.
 * 1. 内置业务和行情域名
 * 2. 首次启动使用域名请求ip列表
 * 3. 非首次启动，如果存在ip列表则使用ip列表，不存在则使用域名联网
 * 4. 业务和行情ip列表及内置域名分开，请求也分开为两条链路
 * 5. ip失败后轮换ip
 */
public class IPManager implements DataSourceProxy.IRequestCallback{
    private static final String TAG = "IPManager";

    private static final String KEY_PREF_IP_LIST = "KEY_PREF_IP_LIST";
    private static final String KEY_PREF_QUOTE_IP_LIST = "KEY_PREF_QUOTE_IP_LIST";
    // test
    private static final String KEY_PREF_IP_LIST_TEST = "KEY_PREF_IP_LIST_TEST";
    private static final String KEY_PREF_QUOTE_IP_LIST_TEST = "KEY_PREF_QUOTE_IP_LIST_TEST";

    // 正式环境http域名
    public static final String DEFAULT_HTTP_DOMAIN = "http://http.wedengta.com:55555";

    // 业务：正式环境域名和IP列表
    private final String DEFAULT_DOMAIN = "47.96.141.250:55556";
    private ArrayList<String> defaultIpList;

    // 行情：正式环境域名和IP列表
    private final String DEFAULT_QUOTE_DOMAIN = "47.96.141.250:55556";
    private ArrayList<String> defaultQuoteIpList;

    // 测试环境ip
    private boolean isTest = false;
    //    private static final String TEST_IP = "58.49.133.142:55556";
    private static final String TEST_IP = "47.97.111.88:55556";
    private ArrayList<String> testIpList;
    private ArrayList<String> testQuoteIpList;

    private static IPManager instance;
    private IPManager() {
    }

    public synchronized static IPManager getInstance() {
        if (instance == null) {
            instance = new IPManager();
        }
        return instance;
    }

    /**
     * 初始化ip列表
     */
    public void init() {
        // 初始化业务正式环境ip
        defaultIpList = new ArrayList<>();
        Set<String> ipSet = SettingPref.getStringSet(KEY_PREF_IP_LIST);
        if (ipSet == null) {
            defaultIpList.add(DEFAULT_DOMAIN);
            DtLog.d(TAG, "init default ip=" + DEFAULT_DOMAIN);
        } else {
            Iterator<String> iterator = ipSet.iterator();
            while (iterator.hasNext()) {
                String ip = iterator.next();
                defaultIpList.add(ip);
                DtLog.d(TAG, "init last update ip=" + ip);
            }
            // 把域名加入ip列表尾部，轮询用
            defaultIpList.add(DEFAULT_DOMAIN);
        }

        // 初始化行情正式环境ip
        defaultQuoteIpList = new ArrayList<>();
        Set<String> ipQuoteSet = SettingPref.getStringSet(KEY_PREF_QUOTE_IP_LIST);
        if (ipQuoteSet == null) {
            defaultQuoteIpList.add(DEFAULT_QUOTE_DOMAIN);
            DtLog.d(TAG, "init quote default ip=" + DEFAULT_QUOTE_DOMAIN);
        } else {
            Iterator<String> iterator = ipQuoteSet.iterator();
            while (iterator.hasNext()) {
                String ip = iterator.next();
                defaultQuoteIpList.add(ip);
                DtLog.d(TAG, "init quote update ip=" + ip);
            }
            defaultQuoteIpList.add(DEFAULT_QUOTE_DOMAIN);
        }

        // 初始化业务测试环境ip
        testIpList = new ArrayList<>();
        Set<String> testIpSet = SettingPref.getStringSet(KEY_PREF_IP_LIST_TEST);
        if (testIpSet == null) {
            testIpList.add(TEST_IP);
            DtLog.d(TAG, "init default test ip=" + TEST_IP);
        } else {
            Iterator<String> iterator = testIpSet.iterator();
            while (iterator.hasNext()) {
                String ip = iterator.next();
                testIpList.add(ip);
                DtLog.d(TAG, "init update test ip=" + ip);
            }
            testIpList.add(TEST_IP);
        }

        // 初始化行情测试环境ip
        testQuoteIpList = new ArrayList<>();
        Set<String> testQuoteIpSet = SettingPref.getStringSet(KEY_PREF_QUOTE_IP_LIST_TEST);
        if (testQuoteIpSet == null) {
            testQuoteIpList.add(TEST_IP);
            DtLog.d(TAG, "init default quote test ip=" + TEST_IP);
        } else {
            Iterator<String> iterator = testQuoteIpSet.iterator();
            while (iterator.hasNext()) {
                String ip = iterator.next();
                testQuoteIpList.add(ip);
                DtLog.d(TAG, "init quote update test ip=" + ip);
            }
            testQuoteIpList.add(TEST_IP);
        }
    }

    /**
     * 更新ip列表
     */
    public void update() {
        if (!NetUtil.isNetWorkConnected(SDKManager.getInstance().getContext())) {
            DtLog.w(TAG, "update no network return");
            return;
        }

        ArrayList<Integer> ipTypes = new ArrayList<>();
        ipTypes.add(E_IP_TYPE.E_THOTH_PROXY);
        ipTypes.add(E_IP_TYPE.E_THOTH_SOCKET_PROXY);
        ipTypes.add(E_IP_TYPE.E_QUOTE_SOCKET_PROXY);

        final IpListReq req = new IpListReq();
        req.eApnType = E_APN_TYPE.E_APN_UNKNOWN;
        req.sApn = "";
        req.vtIPType = ipTypes;

        DtLog.d(TAG, "start request ip list");
        DataEngine.getInstance().request(EntityObject.ET_CONFIG_GET_IPLIST, req, this);
    }

    /**
     * 处理ip更新
     * @param success true代表请求成功
     * @param data 请求成功时，返回的数据
     */
    @Override
    public void callback(boolean success, EntityObject data) {
        DtLog.d(TAG, "callback update ip list, success=" + success);
        if (success && data.getEntity() != null) {
            IpListRsp rsp = (IpListRsp) data.getEntity();
            ArrayList<IpInfo> ipInfoList = rsp.vtIpInfo;
            if (ipInfoList != null && ipInfoList.size() > 0) {
                for (IpInfo ipInfo : ipInfoList) {
                    if (ipInfo.eIPType == E_IP_TYPE.E_THOTH_SOCKET_PROXY) {
                        // 保存业务ip
                        Set<String> ipSet = new HashSet<>();
                        for (String ip : ipInfo.vtIPList) {
                            ipSet.add(ip);
                            DtLog.d(TAG, "save new default ip=" + ip + ", isTest=" + isTest);
                        }
                        if (isTest) {
                            SettingPref.putStringSet(KEY_PREF_IP_LIST_TEST, ipSet);
                        } else {
                            SettingPref.putStringSet(KEY_PREF_IP_LIST, ipSet);
                        }
                    } else if (ipInfo.eIPType == E_IP_TYPE.E_QUOTE_SOCKET_PROXY) {
                        // 保存行情ip
                        Set<String> ipSet = new HashSet<>();
                        for (String ip : ipInfo.vtIPList) {
                            ipSet.add(ip);
                            DtLog.d(TAG, "save new quote default ip=" + ip + ", isTest=" + isTest);
                        }
                        if (isTest) {
                            SettingPref.putStringSet(KEY_PREF_QUOTE_IP_LIST_TEST, ipSet);
                        } else {
                            SettingPref.putStringSet(KEY_PREF_QUOTE_IP_LIST, ipSet);
                        }
                    } else {
                        DtLog.d(TAG, "ignore ip type=" + ipInfo.eIPType);
                    }
                }
            } else {
                DtLog.w(TAG, "callback failed ipInfoList no data");
            }
        } else {
            DtLog.w(TAG, "callback failed");
        }
    }

    /**
     * 获取ip
     * @param isQuote 是否取行情的ip
     * @return
     */
    public synchronized InetSocketAddress getIp(boolean isQuote) {
        String ip = null;
        if (isTest) {
            // 测试ip
            ip = isQuote ? testQuoteIpList.get(0) : testIpList.get(0);
            DtLog.d(TAG, "get test ip=" + ip + ", isQuote=" + isQuote);
            return NetUtil.ipToSocketAdress(ip);
        }

        try {
            // 正式环境ip
            ip = isQuote ? defaultQuoteIpList.get(0) : defaultIpList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ip == null || ip.length() < 1) {
            // 注意：如果获取ip出现异常，业务和行情都使用DEFAULT_DOMAIN
            ip = isQuote ? DEFAULT_QUOTE_DOMAIN : DEFAULT_DOMAIN;
            DtLog.e(TAG, "get default error isQuote=" + isQuote + ", ip=" + ip);
        } else {
            DtLog.d(TAG, "get default ip=" + ip + ", isQuote=" + isQuote);
        }

        return NetUtil.ipToSocketAdress(ip);
    }

    public synchronized String getIpString(boolean isQuote) {
        String ip = "";
        if (isTest) {
            // 测试ip
            ip =  isQuote ? testQuoteIpList.get(0) : testIpList.get(0);
        } else {
            // 正式ip
            ip = isQuote ? defaultQuoteIpList.get(0) : defaultIpList.get(0);
        }
        DtLog.d(TAG, "getIpString ip=" + ip + ", isQuote=" + isQuote);
        return ip;
    }

    /**
     * 标记ip失败
     * @param isQuote
     */
    public synchronized void failed(boolean isQuote) {
        if (isTest) {
            if (isQuote) {
                String ip = testQuoteIpList.remove(0);
                testQuoteIpList.add(ip);
                DtLog.w(TAG, "failed test quote ip=" + ip);
            } else {
                String ip = testIpList.remove(0);
                testIpList.add(ip);
                DtLog.w(TAG, "failed test ip=" + ip);
            }
        } else {
            if (isQuote) {
                String ip = defaultQuoteIpList.remove(0);
                defaultQuoteIpList.add(ip);
                DtLog.w(TAG, "failed quote ip=" + ip);
            } else {
                String ip = defaultIpList.remove(0);
                defaultIpList.add(ip);
                DtLog.w(TAG, "failed ip=" + ip);
            }
        }
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(int test) {
        isTest = test == 1;
    }
}
