package com.dengtacj.component.event;

/**
 * Created by liqf on 2016/10/14.
 */

public class DoLikeByWebEvent {
    public String feedId;
    public boolean isAdd;

    public DoLikeByWebEvent(final String feedId, final boolean isAdd) {
        this.feedId = feedId;
        this.isAdd = isAdd;
    }
}
