package com.sscf.investment.sdk.utils;

import android.text.TextUtils;
import BEC.E_MARKET_TYPE;
import BEC.E_SEC_TYPE;
import BEC.SecCode;

/**
 * Created by xuebinliu on 9/7/15.
 */
public class BaseStockUtil {

    /**
     * 灯塔唯一编码转换成证券信息
     * @param dtSecCode
     * @return
     */
    public static SecCode convertSecInfo(final String dtSecCode) {
        final SecCode secInfo = new SecCode();
        secInfo.eMarketType = getMarketType(dtSecCode);
        secInfo.eSecType = getSecType(dtSecCode);
        secInfo.sSecCode = getSecCode(dtSecCode);
        return secInfo;
    }

    public static int getMarketType(final String dtSecCode) {
        if (!TextUtils.isEmpty(dtSecCode) && dtSecCode.length() >= 2) {
            return NumberUtil.parseInt(dtSecCode.substring(0, 2), E_MARKET_TYPE.E_MT_NO);
        } else {
            return E_MARKET_TYPE.E_MT_NO;
        }
    }

    public static int getSecType(final String dtSecCode) {
        if (!TextUtils.isEmpty(dtSecCode) && dtSecCode.length() >= 4) {
            return NumberUtil.parseInt(dtSecCode.substring(2, 4), E_SEC_TYPE.E_ST_UNKNOWN);
        } else {
            return E_SEC_TYPE.E_ST_UNKNOWN;
        }
    }

    public static String getSecCode(final String dtSecCode) {
        if (!TextUtils.isEmpty(dtSecCode) && dtSecCode.length() > 4) {
            return dtSecCode.substring(4);
        } else {
            return "";
        }
    }

    /**
     * 是否是A股的个股
     * @return
     */
    public static boolean isAStock(final String dtSecCode) {
        if (getSecType(dtSecCode) == E_SEC_TYPE.E_ST_STOCK) {
            switch (getMarketType(dtSecCode)) {
                case E_MARKET_TYPE.E_MT_SH: {
                    final String secCode = getSecCode(dtSecCode);
                    if (!TextUtils.isEmpty(secCode) && secCode.startsWith("60")) {
                        return true;
                    }
                    break;
                }
                case E_MARKET_TYPE.E_MT_SZ: {
                    final String secCode = getSecCode(dtSecCode);
                    if (!TextUtils.isEmpty(secCode)) {
                        if (secCode.startsWith("00")) {
                            return true;
                        } else if (secCode.startsWith("30")) {
                            return true;
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
        return false;
    }

    public static boolean isUsaMarket(final String dtSecCode) {
        switch (getMarketType(dtSecCode)) {
            case E_MARKET_TYPE.E_MT_NASDAQ: //纳斯达克
            case E_MARKET_TYPE.E_MT_NYSE: //纽交所
            case E_MARKET_TYPE.E_MT_AMEX: //美交所
            case E_MARKET_TYPE.E_MT_USI:
                return true;
            default:
                return false;
        }
    }


    public static boolean isChineseMarket(final String dtSecCode) {
        switch (getMarketType(dtSecCode)) {
            case E_MARKET_TYPE.E_MT_SH:
            case E_MARKET_TYPE.E_MT_SZ:
            case E_MARKET_TYPE.E_MT_DT:
                return true;
            default:
                return false;
        }
    }

    public static boolean isHongKongMarket(final String dtSecCode) {
        switch (getMarketType(dtSecCode)) {
            case E_MARKET_TYPE.E_MT_HK:
            case E_MARKET_TYPE.E_MT_HIS:
                return true;
            default:
                return false;
        }
    }
}
