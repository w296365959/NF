package com.sscf.investment.detail.entity;

/**
 * Created by LEN on 2018/4/12.
 */

public class BullBear {

    private int bullBear;//0, 为熊； 1，为牛; -1为无效

    public BullBear(int bullBear) {
        this.bullBear = bullBear;
    }

    public int getBullBear() {
        return bullBear;
    }

    public void setBullBear(int bullBear) {
        this.bullBear = bullBear;
    }
}
