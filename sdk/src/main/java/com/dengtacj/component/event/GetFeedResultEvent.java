package com.dengtacj.component.event;

import BEC.FeedItem;

/**
 * Created by liqf on 2016/10/20.
 */

public class GetFeedResultEvent {
    public FeedItem feedItem;

    public GetFeedResultEvent(FeedItem feedItem) {
        this.feedItem = feedItem;
    }
}
