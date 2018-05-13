package com.sscf.investment.detail.entity;

/**
 * Created by LEN on 2018/4/2.
 */

public class BreakPoint {

    private int index;

    private boolean isBreakUp;

    private boolean isBreakDown;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isBreakUp() {
        return isBreakUp;
    }

    public void setBreakUp(boolean breakUp) {
        isBreakUp = breakUp;
    }

    public boolean isBreakDown() {
        return isBreakDown;
    }

    public void setBreakDown(boolean breakDown) {
        isBreakDown = breakDown;
    }

    public BreakPoint(int index, boolean isBreakUp, boolean isBreakDown) {
        this.index = index;
        this.isBreakUp = isBreakUp;
        this.isBreakDown = isBreakDown;
    }
}
