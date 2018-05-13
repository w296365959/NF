package com.sscf.investment.detail.entity;

/**
 *
 * 神奇九转指标
 * Created by LEN on 2018/3/30.
 */

public class MagicNine {

    public static final int NINE = 9;

    private int status;//1 为大于4天前的收盘价 -1 为小于4天前的收盘价

    private int number;//数字1-9

    private boolean isEnable = false;

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public MagicNine(int status, int number) {
        this.status = status;
        this.number = number;
    }

    public MagicNine(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public MagicNine() {
    }
}
