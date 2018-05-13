package com.dengtacj.component.event;

/**
 * Created by liqf on 2016/10/9.
 */

public class DeleteCommentEvent {
    public String feedId;
    public boolean success;

    public DeleteCommentEvent(boolean success, String feedId) {
        this.feedId = feedId;
        this.success = success;
    }
}
