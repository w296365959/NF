package com.sscf.investment.utils;

import BEC.E_MARKET_TYPE;
import BEC.E_SEC_ATTR;
import BEC.E_SEC_CHANGE_STATUS;
import BEC.E_SEC_STATUS;
import BEC.E_SEC_TYPE;
import BEC.SecCode;
import BEC.SecInfo;
import BEC.SecQuote;
import BEC.SecSimpleQuote;
import BEC.SecStatusInfo;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IThemeManager;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.sscf.investment.sdk.utils.BaseStockUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xuebinliu on 9/7/15.
 */
public final class StockUtil extends BaseStockUtil {
    private static boolean sStrInited;
    private static String sStrIndex;
    private static String sStrPlateIndex;
    private static String sStrFund;
    private static String sStrIndexFuture;

    /**
     * 更新股票标的标的icon
     * @param tagIcon
     * @param dtSecCode
     */
    public static void updateStockTagIcon(ImageView tagIcon, String dtSecCode) {
        if (tagIcon == null) {
            return;
        }
        SecCode secCode = convertSecInfo(dtSecCode);

        DengtaApplication application = DengtaApplication.getApplication();
        Resources resources = application.getResources();
        Drawable sHKTag, sUSTag;
        boolean isDefaultTheme = true;
        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        if (themeManager != null) {
            isDefaultTheme = themeManager.isDefaultTheme();
        }
        if (isDefaultTheme) {
            sHKTag = resources.getDrawable(R.drawable.stock_tag_hk);
            sUSTag = resources.getDrawable(R.drawable.stock_tag_us);
        } else {
            sHKTag = resources.getDrawable(R.drawable.stock_tag_hk_night);
            sUSTag = resources.getDrawable(R.drawable.stock_tag_us_night);
        }

        switch (secCode.getEMarketType()) {
            case E_MARKET_TYPE.E_MT_HK:
            case E_MARKET_TYPE.E_MT_HIS:
                tagIcon.setImageDrawable(sHKTag);
                tagIcon.setVisibility(View.VISIBLE);
                break;
            case E_MARKET_TYPE.E_MT_NASDAQ:
            case E_MARKET_TYPE.E_MT_NYSE:
            case E_MARKET_TYPE.E_MT_AMEX:
            case E_MARKET_TYPE.E_MT_USI:
                tagIcon.setImageDrawable(sUSTag);
                tagIcon.setVisibility(View.VISIBLE);
                break;
            default:
                tagIcon.setImageDrawable(null);
                tagIcon.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * 检查是否设置了股价提醒
     * @param stockDbEntity
     * @return
     */
    public static  boolean isSetRemind(StockDbEntity stockDbEntity) {
        if (stockDbEntity == null) {
            return false;
        }
        return stockDbEntity.getHighPrice() > 0 || stockDbEntity.getLowPrice() > 0
                || stockDbEntity.getIncreasePer() > 0 || stockDbEntity.getDecreasesPer() > 0
                || stockDbEntity.isPushAnnouncement() || stockDbEntity.isPushResearch()
                || stockDbEntity.isDkAlert() || !TextUtils.isEmpty(stockDbEntity.getBroadcastTime())
                || stockDbEntity.isPushAnnouncement() || stockDbEntity.isPushResearch()
                || stockDbEntity.getChipHighPrice() > 0 || stockDbEntity.getChipLowPrice() > 0
                || stockDbEntity.getMainHighPrice() > 0 || stockDbEntity.getMainLowPrice() > 0
                || !TextUtils.isEmpty(stockDbEntity.getStrategyId());
    }

    public static boolean hasTransaction(final String dtSecCode) {
        int marketType = getMarketType(dtSecCode);

        switch (marketType) {
            case E_MARKET_TYPE.E_MT_HK: //港股
            case E_MARKET_TYPE.E_MT_NASDAQ: //纳斯达克
            case E_MARKET_TYPE.E_MT_NYSE: //纽交所
            case E_MARKET_TYPE.E_MT_AMEX: //美交所
                return false;
            case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
            case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                break;
            default:
                break;
        }

        int secType = getSecType(dtSecCode);
        switch (secType) {
            case E_SEC_TYPE.E_ST_INDEX:
            case E_SEC_TYPE.E_ST_PLATE:
            case E_SEC_TYPE.E_ST_FUTURES:
                return false;
            default:
                break;
        }

        return true;
    }

    public static boolean hasStockRank(final String indexDtSecCode) {
        if (DengtaConst.DENGTA_DT_CODE.equals(indexDtSecCode)
            || DengtaConst.SHANGHAI_INDEX_DT_CODE.equals(indexDtSecCode)
            || DengtaConst.SHENZHEN_INDEX_DT_CODE.equals(indexDtSecCode)
            || DengtaConst.GROWTH_ENTERPRISE_INDEX_DT_CODE.equals(indexDtSecCode)) {
            return true;
        }
        return false;
    }

    public static boolean supportFiveDayTimeLine(String dtSecCode) {
        if(unSupportFiveDayTimeLine(dtSecCode)) {
            return false;
        }

        int secType = getSecType(dtSecCode);
        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
            case E_SEC_TYPE.E_ST_BOND:
            case E_SEC_TYPE.E_ST_FUND:
                return true;
            default:
        }
        return false;
    }

    public static boolean unSupportFiveDayTimeLine(String dtSecCode) {
        int marketType = getMarketType(dtSecCode);

        boolean unsupport = false;
        switch (marketType) {
            case E_MARKET_TYPE.E_MT_NASDAQ: //纳斯达克
            case E_MARKET_TYPE.E_MT_NYSE: //纽交所
            case E_MARKET_TYPE.E_MT_AMEX: //美交所
            case E_MARKET_TYPE.E_MT_USI: //美股指数
                unsupport = true;
                break;
            default:
                break;
        }
        return unsupport;
    }

    /**
     * 是否支持资金博弈
     * @param dtSecCode
     * @return
     */
    public static boolean supportCapitalDDZ(final String dtSecCode) {
        if (DengtaConst.DENGTA_DT_CODE.equals(dtSecCode)) {
            return true;
        }
        switch (getMarketType(dtSecCode)) {
            case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
            case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
            case E_MARKET_TYPE.E_MT_DT: //灯塔自定义交易所
                break;
            default:
                return false;
        }
        switch (getSecType(dtSecCode)) {
            case E_SEC_TYPE.E_ST_STOCK:
            case E_SEC_TYPE.E_ST_INDEX:
            case E_SEC_TYPE.E_ST_FUND:
            case E_SEC_TYPE.E_ST_PLATE:
            case E_SEC_TYPE.E_ST_CONC:
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * 是否支持量比
     * @param dtSecCode
     * @return
     */
    public static boolean supportVolumeRatio(final String dtSecCode) {
        switch (getMarketType(dtSecCode)) {
            case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
            case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                break;
            default:
                return false;
        }
        switch (getSecType(dtSecCode)) {
            case E_SEC_TYPE.E_ST_STOCK:
            case E_SEC_TYPE.E_ST_FUND:
                break;
            default:
                return false;
        }
        return true;
    }

    public static boolean supportSecHistory(final String dtSecCode) {
        return supportSimilarK(dtSecCode);
    }

    public static boolean supportSimilarK(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        if (DengtaConst.DENGTA_DT_CODE.equals(dtSecCode)) {
            return true;
        }

        int marketType = getMarketType(dtSecCode);
        switch (marketType) {
            case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
            case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
            case E_MARKET_TYPE.E_MT_HK: //港股
            case E_MARKET_TYPE.E_MT_NASDAQ: //纳斯达克
            case E_MARKET_TYPE.E_MT_NYSE: //纽交所
            case E_MARKET_TYPE.E_MT_AMEX: //美交所
            case E_MARKET_TYPE.E_MT_HIS: //港股指数
            case E_MARKET_TYPE.E_MT_USI: //美股指数
                break;
            default:
                return false;
        }

        int secType = getSecType(dtSecCode);
        switch (secType) {
            case E_SEC_TYPE.E_ST_INDEX:
            case E_SEC_TYPE.E_ST_STOCK:
                break;
            default:
                return false;
        }

        return true;
    }

    public static boolean isMarginStock(SecInfo secInfo) {
        if(secInfo != null) {
            Map<Integer, String> attrs = secInfo.getMSecAttr();
            if(attrs != null && !attrs.isEmpty()) {
                return !TextUtils.isEmpty(attrs.get(E_SEC_ATTR.E_SEC_ATTR_MARGIN));
            }
        }
        return false;
    }

    public static boolean isDirectorAddStock(SecInfo secInfo) {
        if(secInfo != null) {
            final ArrayList<SecStatusInfo> statusInfos = secInfo.getVStatusInfo();
            if (statusInfos != null && statusInfos.size() > 0) {
                final Iterator<SecStatusInfo> it = statusInfos.iterator();
                while (it.hasNext()) {
                    if (it.next().iStatus == E_SEC_CHANGE_STATUS.E_STATUS_FIX) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean supportBullbearPoint(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        if (isAStock(dtSecCode)) {
            return true;
        }
        SecCode secCode = convertSecInfo(dtSecCode);
        int secType = secCode.getESecType();
        switch (secType) {
            case E_SEC_TYPE.E_ST_INDEX:
            case E_SEC_TYPE.E_ST_PLATE:
                return true;
        }

        return false;
    }

    public static boolean supportBSPoint(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        int marketType = getMarketType(dtSecCode);
        switch (marketType) {
            case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
            case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                break;
            default:
                return false;
        }

        int secType = getSecType(dtSecCode);
        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
                break;
            default:
                return false;
        }

        return true;
    }

    public static boolean supportHistoryTimeLine(final String dtSecCode) {
        return supportBSPoint(dtSecCode);
    }

    public static boolean supportEnlargeTimeLine(final String dtSecCode) {
        return supportBSPoint(dtSecCode);
    }

    public static boolean supportCallauction(final String dtSecCode) {
        return supportBSPoint(dtSecCode);
    }

    public static boolean supportSecLiveMsg(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        if (hasStockRank(dtSecCode)) {
            return true;
        }

        int marketType = getMarketType(dtSecCode);
        switch (marketType) {
            case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
            case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                break;
            default:
                return false;
        }

        int secType = getSecType(dtSecCode);
        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
                break;
            default:
                return false;
        }

        return true;
    }

    public static boolean supportChipEntrance(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        int marketType = getMarketType(dtSecCode);
        switch (marketType) {
            case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
            case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                break;
            default:
                return false;
        }

        int secType = getSecType(dtSecCode);
        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
                break;
            default:
                return false;
        }

        return true;
    }

    public static boolean supportInvestmentAtlas(final String dtSecCode) {
        return supportFigure(dtSecCode);
    }

    public static boolean supportStockCompare(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        int marketType = getMarketType(dtSecCode);
        switch (marketType) {
            case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
            case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                break;
            default:
                return false;
        }

        int secType = getSecType(dtSecCode);
        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
                break;
            default:
                return false;
        }

        return true;
    }

    public static boolean supportCustomStockCompare(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        int marketType = getMarketType(dtSecCode);
        switch (marketType) {
            case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
            case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
            case E_MARKET_TYPE.E_MT_DT: //灯塔
                break;
            default:
                return false;
        }

        int secType = getSecType(dtSecCode);
        switch (secType) {
            case E_SEC_TYPE.E_ST_INDEX:
            case E_SEC_TYPE.E_ST_STOCK:
            case E_SEC_TYPE.E_ST_PLATE:
                break;
            default:
                return false;
        }

        return true;
    }

    public static boolean supportFigure(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        int marketType = getMarketType(dtSecCode);
        switch (marketType) {
            case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
            case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                break;
            default:
                return false;
        }

        int secType = getSecType(dtSecCode);
        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
                break;
            default:
                return false;
        }

        return true;
    }

    public static boolean isNewThirdBoard(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        int marketType = getMarketType(dtSecCode);

        boolean isNewThirdBoard = false;
        switch (marketType) {
            case E_MARKET_TYPE.E_MT_TB: //新三板
                isNewThirdBoard = true;
                break;
            default:
                break;
        }
        return isNewThirdBoard;
    }

    public static boolean isHongKongOrUSA(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        int marketType = getMarketType(dtSecCode);

        boolean isHongKongOrUsa = false;
        switch (marketType) {
            case E_MARKET_TYPE.E_MT_HK: //港股
            case E_MARKET_TYPE.E_MT_NASDAQ: //纳斯达克
            case E_MARKET_TYPE.E_MT_NYSE: //纽交所
            case E_MARKET_TYPE.E_MT_AMEX: //美交所
            case E_MARKET_TYPE.E_MT_HIS: //港股指数
            case E_MARKET_TYPE.E_MT_USI: //美股指数
                isHongKongOrUsa = true;
                break;
            default:
                break;
        }
        return isHongKongOrUsa;
    }

    public static boolean isChineseStockB(final String dtSecCode) {
        SecCode secCode = convertSecInfo(dtSecCode);
        if(secCode.getESecType() == E_SEC_TYPE.E_ST_STOCK) {
            switch (secCode.getEMarketType()) {
                case E_MARKET_TYPE.E_MT_SZ:
                    if(!TextUtils.isEmpty(secCode.getSSecCode()) && secCode.getSSecCode().startsWith("200")) {
                        return true;
                    }
                    break;
                case E_MARKET_TYPE.E_MT_SH:
                    if(!TextUtils.isEmpty(secCode.getSSecCode()) && secCode.getSSecCode().startsWith("900")) {
                        return true;
                    }
                    break;
                default:
            }
        }
        return false;
    }

    public static boolean isHongKongIndex(final String dtSecCode) {
        int marketType = getMarketType(dtSecCode);
        return marketType == E_MARKET_TYPE.E_MT_HIS;
    }

    public static boolean isUSAIndex(final String dtSecCode) {
        int marketType = getMarketType(dtSecCode);
        return marketType == E_MARKET_TYPE.E_MT_USI;
    }

    public static boolean isHongKongOrUsaIndex(final String dtSecCode) {
        int marketType = getMarketType(dtSecCode);
        return marketType == E_MARKET_TYPE.E_MT_USI || marketType == E_MARKET_TYPE.E_MT_HIS;
    }

    public static boolean isStock(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        return convertSecInfo(dtSecCode).getESecType() == E_SEC_TYPE.E_ST_STOCK;
    }

    public static boolean isFund(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }
        return convertSecInfo(dtSecCode).getESecType() == E_SEC_TYPE.E_ST_FUND;
    }

    /**
     * 是否是指数
     * @param dtSecCode
     * @return
     */
    public static boolean isIndex(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        int secType = convertSecInfo(dtSecCode).getESecType();
        boolean isIndex = false;
        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
                // 股票
                break;
            case E_SEC_TYPE.E_ST_FUND:
                // 基金
                break;
            case E_SEC_TYPE.E_ST_INDEX:                // 指数
            case E_SEC_TYPE.E_ST_PLATE:                // 行业板块
                isIndex = true;
                break;
            case E_SEC_TYPE.E_ST_CONC:
                // 主题
                break;
            case E_SEC_TYPE.E_ST_FUTURES:
                // 股指期货
                break;
            default:
                break;
        }

        return isIndex;
    }

    public static boolean isFuture(final String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        int secType = convertSecInfo(dtSecCode).getESecType();
        boolean isFuture = false;
        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
                // 股票
                break;
            case E_SEC_TYPE.E_ST_FUND:
                // 基金
                break;
            case E_SEC_TYPE.E_ST_INDEX:                // 指数
            case E_SEC_TYPE.E_ST_PLATE:                // 行业板块
                break;
            case E_SEC_TYPE.E_ST_CONC:
                // 主题
                break;
            case E_SEC_TYPE.E_ST_FUTURES:
                isFuture = true;
                // 股指期货
                break;
            default:
                break;
        }

        return isFuture;
    }

    public static boolean isHandfulUnit(String dtSecCode) {
        if (TextUtils.isEmpty(dtSecCode)) {
            return false;
        }

        SecCode secInfo = convertSecInfo(dtSecCode);

        int secType = secInfo.getESecType();
        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
                // 股票
                break;
            case E_SEC_TYPE.E_ST_FUND:
                // 基金
                break;
            case E_SEC_TYPE.E_ST_INDEX:                // 指数
            case E_SEC_TYPE.E_ST_PLATE:                // 行业板块
                return true;
            case E_SEC_TYPE.E_ST_CONC:
                // 主题
                break;
            case E_SEC_TYPE.E_ST_FUTURES:
                // 股指期货
                return true;
            default:
                break;
        }

        int marketType = secInfo.getEMarketType();
        switch (marketType) {
            case E_MARKET_TYPE.E_MT_TB: // 新三板
                return true;
            default:
                break;
        }

        return false;
    }

    public static boolean supportSmartStare(final String dtSecCode) {
        int secType = convertSecInfo(dtSecCode).getESecType();
        int marketType = getMarketType(dtSecCode);
        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
                // 股票
                switch (marketType) {
                    case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
                    case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                        return true;
                    default:
                }
            default:
        }
        return false;
    }

    public static String getTypeString(final Context context, final String dtSecCode) {
        if (!sStrInited) {
            Resources resources = context.getResources();
            sStrIndex = resources.getString(R.string.sec_type_index);
            sStrPlateIndex = resources.getString(R.string.sec_type_plate_index);
            sStrFund = resources.getString(R.string.sec_type_fund);
            sStrIndexFuture = resources.getString(R.string.sec_type_index_future);
            sStrInited = true;
        }

        SecCode secCode = convertSecInfo(dtSecCode);
        int secType = secCode.getESecType();

        String secTypeStr = "";
        switch (secType) {
            case E_SEC_TYPE.E_ST_INDEX:
                secTypeStr = sStrIndex;
                break;
            case E_SEC_TYPE.E_ST_FUND:
                secTypeStr = sStrFund;
                break;
            case E_SEC_TYPE.E_ST_FUTURES:
                secTypeStr = sStrIndexFuture;
                break;
            case E_SEC_TYPE.E_ST_CONC:
                break;
            case E_SEC_TYPE.E_ST_PLATE:
                secTypeStr = sStrPlateIndex;
                break;
            default:
                break;
        }

        return secTypeStr;
    }

    public static SecSimpleQuote getSimpleQuoteFromQuote(SecQuote secQuote) {
        SecSimpleQuote simpleQuote = new SecSimpleQuote();
        simpleQuote.setFNow(secQuote.getFNow());
        simpleQuote.setFClose(secQuote.getFClose());
        simpleQuote.setITpFlag(secQuote.getITpFlag());

        return simpleQuote;
    }

    public static boolean isSuspended(SecQuote quote) {
        return quote != null && quote.getESecStatus() == E_SEC_STATUS.E_SS_SUSPENDED;
    }

    public static String getSearchTagText(final String dtSecCode) {
        final int secType = getSecType(dtSecCode);
        switch (secType) {
            case E_SEC_TYPE.E_ST_INDEX:
                return "指数";
            case E_SEC_TYPE.E_ST_PLATE:
                return "板块";
            case E_SEC_TYPE.E_ST_FUND:
                return "基金";
            case E_SEC_TYPE.E_ST_FUTURES:
                return "期货";
            default:
                break;
        }

        final int marketType = getMarketType(dtSecCode);
        switch (marketType) {
            case E_MARKET_TYPE.E_MT_SH: {
                final String secCode = getSecCode(dtSecCode);
                if (!TextUtils.isEmpty(secCode)) {
                    if (secCode.startsWith("60")) {
                        return "沪A";
                    } else if (secCode.startsWith("90")) {
                        return "沪B";
                    }
                }
                break;
            }
            case E_MARKET_TYPE.E_MT_SZ:
                final String secCode = getSecCode(dtSecCode);
                if (!TextUtils.isEmpty(secCode)) {
                    if (secCode.startsWith("00")) {
                        return "深A";
                    } else if (secCode.startsWith("30")) {
                        return "深A";
                    } else if (secCode.startsWith("20")) {
                        return "深B";
                    }
                }
                break;
            case E_MARKET_TYPE.E_MT_TB:
                return "三板";
            case E_MARKET_TYPE.E_MT_HK:
            case E_MARKET_TYPE.E_MT_HIS:
                return "HK";
            case E_MARKET_TYPE.E_MT_NASDAQ:
            case E_MARKET_TYPE.E_MT_NYSE:
            case E_MARKET_TYPE.E_MT_AMEX:
            case E_MARKET_TYPE.E_MT_USI:
                return "US";
            default:
                break;
        }
        return "其他";
    }

    public static String getStockMarketDtCode(String dtSecCode) {
        String marketDtSecCode = null;
        switch (StockUtil.getMarketType(dtSecCode)) {
            case E_MARKET_TYPE.E_MT_SZ:
                String secCode = StockUtil.getSecCode(dtSecCode);
                if (!TextUtils.isEmpty(secCode)) {
                    if (secCode.startsWith("002")) {
                        marketDtSecCode = DengtaConst.SMALL_AND_MEDIUM_SIZED_BOARD_INDEX_DT_CODE;
                    } else if (secCode.startsWith("300")) {
                        marketDtSecCode = DengtaConst.GROWTH_ENTERPRISE_INDEX_DT_CODE;
                    } else {
                        marketDtSecCode = DengtaConst.SHENZHEN_INDEX_DT_CODE;
                    }
                }
                break;
            case E_MARKET_TYPE.E_MT_SH:
                marketDtSecCode = DengtaConst.SHANGHAI_INDEX_DT_CODE;
                break;
            default:
        }
        return marketDtSecCode;
    }
}
