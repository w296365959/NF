package com.dengtacj.component.event;

/**
 * Created by liqf on 2016/10/8.
 */

public class PostCommentResultEvent {
    public String feedId;
    public boolean success;

    public PostCommentResultEvent(boolean success, String feedId) {
        this.success = success;
        this.feedId = feedId;
    }
}
