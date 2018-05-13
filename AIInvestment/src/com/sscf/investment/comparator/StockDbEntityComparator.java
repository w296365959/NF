package com.sscf.investment.comparator;

import com.dengtacj.component.entity.db.StockDbEntity;
import java.util.Comparator;

/**
 * Created by davidwei on 2017-10-11
 *
 */
public final class StockDbEntityComparator implements Comparator<StockDbEntity> {
    public enum SORT_TYPE {
        UP_DOWN_PERCENT,    // 涨跌幅
        UP_DOWN_PRICE,      // 涨跌价
        TOTOAL_MARKET_VALUE, // 总市值
        PRICE                // 现价
    }
    private final SORT_TYPE mSortType;
    private final boolean mAsc;      // 是否升序

    public StockDbEntityComparator(SORT_TYPE sortType, boolean asc) {
        mSortType = sortType;
        mAsc = asc;
    }

    @Override
    public int compare(StockDbEntity o1, StockDbEntity o2) {
        switch (mSortType) {
            case PRICE:
                return priceCompare(o1, o2);
            case UP_DOWN_PERCENT:
                return updownPercentCompare(o1, o2);
            case UP_DOWN_PRICE:
                return upDownPriceCompare(o1, o2);
            case TOTOAL_MARKET_VALUE:
                return marketValueCompare(o1, o2);
            default:
                return 0;
        }
    }

    private int priceCompare(StockDbEntity o1, StockDbEntity o2) {
        int res = 0;
        final float value1 = o1.getDisplayNow();
        final float value2 = o2.getDisplayNow();
        if (value1 != 0 || value2 != 0) { // 默认降序
            res = Float.compare(value2, value1);
            if (mAsc && value1 != 0 && value2 != 0) { // 升序排列
                res = -res;
            }
        } else {
            // 当前价格都为0
            res = o1.getDtSecCode().compareTo(o2.getDtSecCode());
        }
        return res;
    }

    private int updownPercentCompare(StockDbEntity o1, StockDbEntity o2) {
        int res = 0;

        final float value1 = o1.getDisplayNow();
        final float value2 = o2.getDisplayNow();
        if (value1 != 0 && value2 != 0) { // 都有有效值的时候
            final int status1 = o1.getIStatus();
            final int status2 = o2.getIStatus();
            if (status1 == status2) { // 停牌状态相同
                // 比较涨跌幅
                final float updown1 = o1.getFClose() * o2.getFNow();
                final float updown2 = o2.getFClose() * o1.getFNow();
                res = Float.compare(updown1, updown2);
                if (mAsc) {
                    res = -res;
                }
            } else { // 停牌状态不同，停牌的放后面
                res = status1 - status2;
            }
        } else {
            if (value1 != value2) { // 有一个没有有效值的时候
                res = Float.compare(value2, value1);
            } else { // 都没有有效值的时候
                res = o1.getDtSecCode().compareTo(o2.getDtSecCode());
            }
        }

        return res;
    }

    private int upDownPriceCompare(StockDbEntity o1, StockDbEntity o2) {
        int res = 0;
        final float value1 = o1.getDisplayNow();
        final float value2 = o2.getDisplayNow();
        if (value1 != 0 && value2 != 0) { // 都有有效值的时候
            final int status1 = o1.getIStatus();
            final int status2 = o2.getIStatus();
            if (status1 == status2) { // 停牌状态相同
                // 默认降序
                final float delta1 = o1.getFNow() - o1.getFClose();
                final float delta2 = o2.getFNow() - o2.getFClose();
                res = Float.compare(delta2, delta1);
                if (mAsc) {
                    res = -res;
                }
            } else { // 停牌状态不同，停牌的放后面
                res = status1 - status2;
            }
        } else {
            if (value1 != value2) { // 有一个没有有效值的时候
                res = Float.compare(value2, value1);
            } else { // 都没有有效值的时候
                res = o1.getDtSecCode().compareTo(o2.getDtSecCode());
            }
        }
        return res;
    }

    private int marketValueCompare(StockDbEntity o1, StockDbEntity o2) {
        int res = 0;
        final float value1 = o1.getTotalmarketvalue();
        final float value2 = o2.getTotalmarketvalue();
        if (value1 != 0 || value2 != 0) { // 默认降序
            res = Float.compare(value2, value1);
            if (mAsc && value1 != 0 && value2 != 0) {
                res = -res;
            }
        } else {
            // 都为0
            res = o1.getDtSecCode().compareTo(o2.getDtSecCode());
        }
        return res;
    }
}
