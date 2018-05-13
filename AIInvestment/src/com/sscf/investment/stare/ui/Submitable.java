package com.sscf.investment.stare.ui;


import com.dengtacj.component.entity.db.StockDbEntity;

/**
 * Created by yorkeehuang on 2017/9/14.
 */

public interface Submitable {

    int RESULT_INVALID = -1;
    int RESULT_NOCHANGE = 0;
    int RESULT_SHOULD_SUBMIT = 1;

    void initValue(StockDbEntity stockEntity);

    int checkInput(StockDbEntity stockEntity);

    int submit(StockDbEntity stockEntity);
}
