package com.sscf.investment.utils;

import BEC.*;
import com.sscf.investment.sdk.entity.SecListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidwei on 2015/11/07.
 */
public final class SecListItemUtils {

    public static ArrayList<SecListItem> getSecListFromSecInfoList(final ArrayList<SecInfo> stockList) {
        final int size = stockList != null ? stockList.size() : 0;
        if (size == 0) {
            return null;
        }

        final ArrayList<SecListItem> secList = new ArrayList<SecListItem>(size);
        SecListItem item = null;
        for (SecInfo secInfo : stockList) {
            item = new SecListItem();
            item.setDtSecCode(secInfo.sDtSecCode);
            item.setName(secInfo.sCHNShortName);
            secList.add(item);
        }
        return  secList;
    }

    public static ArrayList<SecListItem> getSecListFromHotStockDescList(final List<HotStockDesc> stockList) {
        final int size = stockList != null ? stockList.size() : 0;
        if (size == 0) {
            return null;
        }

        final ArrayList<SecListItem> secList = new ArrayList<SecListItem>(size);
        SecListItem item = null;
        for (HotStockDesc secInfo : stockList) {
            if (secInfo == null) {
                continue;
            }
            item = new SecListItem();
            item.setDtSecCode(secInfo.sDtSecCode);
            item.setName(secInfo.sSecName);
            secList.add(item);
        }
        return  secList;
    }

    public static ArrayList<SecListItem> getSecListFromSecQuoteList(final List<SecQuote> stockList) {
        final int size = stockList != null ? stockList.size() : 0;
        if (size == 0) {
            return null;
        }

        final ArrayList<SecListItem> secList = new ArrayList<SecListItem>(size);
        SecListItem item = null;
        for (SecQuote secInfo : stockList) {
            if (secInfo == null) {
                continue;
            }
            item = new SecListItem();
            item.setDtSecCode(secInfo.sDtSecCode);
            item.setName(secInfo.sSecName);
            secList.add(item);
        }
        return  secList;
    }

    public static ArrayList<SecListItem> getSecListFromSecSimpleQuoteList(final List<SecSimpleQuote> stockList) {
        final int size = stockList != null ? stockList.size() : 0;
        if (size == 0) {
            return null;
        }

        final ArrayList<SecListItem> secList = new ArrayList<SecListItem>(size);
        SecListItem item = null;
        for (SecSimpleQuote secInfo : stockList) {
            if (secInfo == null) {
                continue;
            }
            item = new SecListItem();
            item.setDtSecCode(secInfo.sDtSecCode);
            item.setName(secInfo.sSecName);
            secList.add(item);
        }
        return  secList;
    }

    public static ArrayList<SecListItem> getSecListFromSecSimpleQuoteList(final SecSimpleQuote[] stockList) {
        final int size = stockList != null ? stockList.length : 0;
        if (size == 0) {
            return null;
        }

        final ArrayList<SecListItem> secList = new ArrayList<SecListItem>(size);

        SecListItem item = null;
        for (SecSimpleQuote secInfo : stockList) {
            if (secInfo == null) {
                continue;
            }
            item = new SecListItem();
            item.setDtSecCode(secInfo.sDtSecCode);
            item.setName(secInfo.sSecName);
            secList.add(item);
        }
        return  secList;
    }

    public static ArrayList<SecListItem> getSecListFromPlateQuoteDescList(final ArrayList<PlateQuoteDesc> stockList) {
        final int size = stockList != null ? stockList.size() : 0;
        if (size == 0) {
            return null;
        }

        final ArrayList<SecListItem> secList = new ArrayList<SecListItem>(size);
        SecListItem item = null;
        for (PlateQuoteDesc secInfo : stockList) {
            item = new SecListItem();
            item.setDtSecCode(secInfo.sDtSecCode);
            item.setName(secInfo.sSecName);
            secList.add(item);
        }
        return  secList;
    }

    public static ArrayList<SecListItem> getSecListFromCapitalDetailDescList(final ArrayList<CapitalDetailDesc> stockList) {
        final int size = stockList != null ? stockList.size() : 0;
        if (size == 0) {
            return null;
        }

        final ArrayList<SecListItem> secList = new ArrayList<SecListItem>(size);
        SecListItem item = null;
        for (CapitalDetailDesc secInfo : stockList) {
            item = new SecListItem();
            item.setDtSecCode(secInfo.sDtSecCode);
            item.setName(secInfo.sSecName);
            secList.add(item);
        }
        return  secList;
    }

    public static ArrayList<SecListItem> getSecListFromCapitalMainFlowDescList(final ArrayList<CapitalMainFlowDesc> stockList) {
        final int size = stockList != null ? stockList.size() : 0;
        if (size == 0) {
            return null;
        }

        final ArrayList<SecListItem> secList = new ArrayList<SecListItem>(size);
        SecListItem item = null;
        for (CapitalMainFlowDesc secInfo : stockList) {
            item = new SecListItem();
            item.setDtSecCode(secInfo.sDtSecCode);
            item.setName(secInfo.sSecName);
            secList.add(item);
        }
        return  secList;
    }

    public static ArrayList<SecListItem> getSecListFromAHPlateDescList(final ArrayList<AHPlateDesc> stockList) {
        final int size = stockList != null ? stockList.size() : 0;
        if (size == 0) {
            return null;
        }

        final ArrayList<SecListItem> secList = new ArrayList<SecListItem>(size);
        SecListItem item = null;
        for (AHPlateDesc secInfo : stockList) {
            item = new SecListItem();
            item.setDtSecCode(secInfo.sHKDtSecCode);
            item.setName(secInfo.sHKSecName);
            secList.add(item);
        }
        return secList;
    }

    public static ArrayList<SecListItem> getSecListFromRealTimeStockItemList(final ArrayList<RealTimeStockItem> stockList) {
        final int size = stockList != null ? stockList.size() : 0;
        if (size == 0) {
            return null;
        }

        final ArrayList<SecListItem> secList = new ArrayList<SecListItem>(size);
        SecListItem item = null;
        for (RealTimeStockItem secInfo : stockList) {
            item = new SecListItem();
            item.setDtSecCode(secInfo.sDtSecCode);
            item.setName(secInfo.sSecName);
            secList.add(item);
        }
        return secList;
    }

    public static ArrayList<SecListItem> getSecListFromSKLStatisticsInfoList(final ArrayList<SKLStatisticsInfo> stockList) {
        final int size = stockList != null ? stockList.size() : 0;
        if (size == 0) {
            return null;
        }

        final ArrayList<SecListItem> secList = new ArrayList<SecListItem>(size);
        SecListItem item = null;
        for (SKLStatisticsInfo secInfo : stockList) {
            item = new SecListItem();
            item.setDtSecCode(secInfo.sDtSecCode);
            item.setName(secInfo.sSecName);
            secList.add(item);
        }
        return secList;
    }
}
