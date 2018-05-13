package com.dengtacj.component.event;

/**
 * Created by liqf on 2016/10/8.
 */

public class PostFeedResultEvent {
    public boolean success;
    public String dtSecCode;

    public PostFeedResultEvent(boolean success, String dtSecCode) {
        this.success = success;
        this.dtSecCode = dtSecCode;
    }
}
