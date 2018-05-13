package com.sscf.investment.comparator;

import BEC.SecSimpleQuote;
import com.sscf.investment.market.view.StockSortTitleView;
import java.util.Comparator;

/**
 * Created by davidwei on 2016-01-11.
 * SecSimpleQuote，按照涨跌幅排序，默认降序
 */
public final class SecSimpleQuoteUpdownComparator implements Comparator<SecSimpleQuote> {
    private final int mSortType;

    public SecSimpleQuoteUpdownComparator() {
        this(StockSortTitleView.STATE_SORT_DESCEND);
    }

    public SecSimpleQuoteUpdownComparator(int sortType) {
        this.mSortType = sortType;
    }

    @Override
    public int compare(SecSimpleQuote o1, SecSimpleQuote o2) {
        int res = 0;
        if (o1 == null && o2 == null) {
            res = 0;
        } else if (o1 == null) {
            res = 1;
        } else if (o2 == null) {
            res = -1;
        } else {
            if (o1.eSecStatus == o2.eSecStatus) { // 停牌状态相同
                // 比较涨跌幅
                final float value1 = o1.fClose * o2.fNow;
                final float value2 = o2.fClose * o1.fNow;
                if (value1 != 0 || value2 != 0) {
                    res = Float.compare(value1, value2);
                    if (mSortType == StockSortTitleView.STATE_SORT_ASCEND) {
                        res = -res;
                    }
                } else {
                    res = o1.sDtSecCode.compareTo(o2.sDtSecCode);
                }
            } else { // 停牌状态不同，停牌的放后面
                res = o1.eSecStatus - o2.eSecStatus;
            }
        }

        return res;
    }
}
