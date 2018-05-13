package com.sscf.investment.sdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.net.InetSocketAddress;
import java.util.Locale;

/**
 * Created by xuebinliu on 2015/7/24.
 *
 * 网络工具类
 */
public class NetUtil {

    public static final int NETWORK_TYPE_NONE = 0;
    public static final int NETWORK_TYPE_WIFI = 1;
    public static final int NETWORK_TYPE_2G = 2;
    public static final int NETWORK_TYPE_3G = 3;
    public static final int NETWORK_TYPE_4G = 4;

    // APN 名称
    private static final String APN_UNKNOWN = "N/A";
    private static final String APN_WIFI = "WIFI";
    private static final String APN_CMWAP = "cmwap";
    private static final String APN_CMNET = "cmnet";
    private static final String APN_3GWAP = "3gwap";
    private static final String APN_3GNET = "3gnet";
    private static final String APN_UNIWAP = "uniwap";
    private static final String APN_UNINET = "uninet";
    private static final String APN_CTWAP = "ctwap";
    private static final String APN_CTNET = "ctnet";

    /**
     * 判断是否有可用网络
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context == null) {
            return false;
        }

        try {
            ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 把形如192.168.1.1:8080的地址字符串转换成socket address
     * @param ip
     * @return
     */
    public static InetSocketAddress ipToSocketAdress(String ip) {
        final String[] strs = ip.split(":");
        if (strs.length >= 2) {
            try {
                int port = Integer.parseInt(strs[1]);
                return new InetSocketAddress(strs[0], port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 判断当前网络状态是wifi，2g，3g或无网络
     * @return
     */
    public static int getNetworkType(final Context context) {
        if (context == null) {
            return NETWORK_TYPE_NONE;
        }
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        int type = NETWORK_TYPE_NONE;

        if (networkInfo == null || !networkInfo.isConnected()) {
            // 没有网络
            type = NETWORK_TYPE_NONE;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            // wifi网络
            type = NETWORK_TYPE_WIFI;
        } else {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                int subtype = networkInfo.getSubtype();
                switch (subtype) {
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        type = NETWORK_TYPE_NONE;
                        break;

                    // 2g网络
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        type = NETWORK_TYPE_2G;
                        break;

                    case TelephonyManager.NETWORK_TYPE_LTE:
                        type = NETWORK_TYPE_4G;
                        break;

                    // 3g网络
                    default:
                        type = NETWORK_TYPE_3G;
                        break;
                }
            }

        }
        return type;
    }

    /**
     * 获得apn
     * @return
     */
    public static String getApn(final Context context) {
        String apn = APN_UNKNOWN;

        final NetworkInfo networkInfo = NetUtil.getActiveNetworkInfo(context);

        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                apn = APN_WIFI;
            } else {
                String extraInfo = networkInfo.getExtraInfo();
                if (extraInfo != null) {
                    extraInfo = extraInfo.trim().toLowerCase(Locale.getDefault());
                    if (extraInfo.contains(APN_CMNET)) {
                        apn = APN_CMNET;
                    } else if (extraInfo.contains(APN_3GNET)) {
                        apn = APN_3GNET;
                    } else if (extraInfo.contains(APN_UNINET)) {
                        apn = APN_UNINET;
                    } else if (extraInfo.contains(APN_CTNET)) {
                        apn = APN_CTNET;
                    } else if (extraInfo.contains(APN_CMWAP)) {
                        apn = APN_CMWAP;
                    } else if (extraInfo.contains(APN_3GWAP)) {
                        apn = APN_3GWAP;
                    } else if (extraInfo.contains(APN_UNIWAP)) {
                        apn = APN_UNIWAP;
                    } else if (extraInfo.contains(APN_CTWAP)) {
                        apn = APN_CTWAP;
                    }
                }
            }
        }
        return apn;
    }


    public static NetworkInfo getActiveNetworkInfo(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
            Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        try {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return networkInfo;
    }
}
