package com.dengtacj.component.event;

/**
 * Created by liqf on 2016/10/14.
 */

public class DeleteFeedEvent {
    public boolean success;
    public String feedId;

    public DeleteFeedEvent(final boolean success, final String feedId) {
        this.success = success;
        this.feedId = feedId;
    }
}
