package com.sscf.investment.comparator;

import BEC.AHPlateDesc;
import com.sscf.investment.market.view.StockSortTitleView;

import java.util.Comparator;

/**
 * Created by davidwei on 2016-04-29.
 * SecSimpleQuote，按照涨跌幅排序，默认降序
 */
public final class AHPremiumComparator implements Comparator<AHPlateDesc> {
    private final int mSortType;

    public AHPremiumComparator() {
        this(StockSortTitleView.STATE_SORT_DESCEND);
    }

    public AHPremiumComparator(int sortType) {
        this.mSortType = sortType;
    }

    @Override
    public int compare(AHPlateDesc lhs, AHPlateDesc rhs) {
        int res = 0;

        final boolean b1 = lhs.fANow > 0;
        final boolean b2 = lhs.fHKNow > 0;
        final boolean b3 = rhs.fANow > 0;
        final boolean b4 = rhs.fHKNow > 0;

        if (lhs == null && rhs == null) {
            res = 0;
        } else if (lhs == null) {
            res = -1;
        } else if (rhs == null) {
            res = 1;
        } else if (b1 && b2 && b3 && b4) { // 两个股票ah都有现价
            final float value1 = lhs.fANow * rhs.fHKNow;
            final float value2 = rhs.fANow * lhs.fHKNow;
            if (value1 > value2) {
                res = 1;
            } else if (value1 < value2) {
                res = -1;
            }

            if (mSortType == StockSortTitleView.STATE_SORT_ASCEND) {
                res = -res;
            }
        } else if (b1 && b2 && b3 && !b4) { // 其中一个股票没有h股现价
            res = -1;
        } else if (b1 && !b2 && b3 && b4) { // 其中一个股票没有h股现价
            res = 1;
        } else if (b1 && b2 && !b3 && b4) { // 其中一个股票没有a股现价
            res = -1;
        } else if (!b1 && b2 && b3 && b4) { // 其中一个股票没有a股现价
            res = 1;
        } else if (b1 && b2 && !b3 && !b4) { // 一个股票有ah股现价,另一个股票没有现价
            res = -1;
        } else if (!b1 && !b2 && b3 && b4) { // 一个股票有ah股现价,另一个股票没有现价
            res = 1;
        } else if (b1 && !b2 && !b3 && b4) { // 其中一个股票没有a股现价,其中一个股票没有h股现价
            res = -1;
        } else if (!b1 && b2 && b3 && !b4) { // 其中一个股票没有a股现价,其中一个股票没有h股现价
            res = 1;
        } else if (b1 && !b2 && b3 && !b4) { // 两只股票有a股现价,没有h股现价
            if (lhs.fANow > rhs.fANow) {
                res = -1;
            } else if (lhs.fANow < rhs.fANow) {
                res = 1;
            }
        } else if (!b1 && b2 && !b3 && b4) { // 两只股票有h股现价,没有a股现价
            if (lhs.fHKNow > rhs.fHKNow) {
                res = -1;
            } else if (lhs.fHKNow < rhs.fHKNow) {
                res = 1;
            }
        } else if (b1 && !b2 && !b3 && !b4) { // 一只股票有a股现价,其余都没有
            res = -1;
        } else if (!b1 && !b2 && b3 && !b4) { // 一只股票有a股现价,其余都没有
            res = 1;
        } else if (!b1 && b2 && !b3 && !b4) { // 一只股票有h股现价,其余都没有
            res = -1;
        } else if (!b1 && !b2 && !b3 && b4) { // 一只股票有h股现价,其余都没有
            res = 1;
        } else { // 两只股票ah股现价都没有
            res = lhs.sHKSecName.compareTo(rhs.sHKSecName);
        }

        return res;
    }
}
