package com.sscf.investment.common.entity;

import com.sscf.investment.afinal.annotation.sqlite.Id;
import com.sscf.investment.afinal.annotation.sqlite.Property;
import com.sscf.investment.afinal.annotation.sqlite.Table;

/**
 * Created by liqf on 2016/9/24.
 */
@Table(name = "feed_like")
public class FeedLikeEntity {
    @Id
    private int id;
    @Property
    private String feedId;
    @Property
    private long accountId;
    @Property
    private boolean like;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
