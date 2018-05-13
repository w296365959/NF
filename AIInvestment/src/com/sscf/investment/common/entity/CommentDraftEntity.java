package com.sscf.investment.common.entity;

import com.sscf.investment.afinal.annotation.sqlite.Id;
import com.sscf.investment.afinal.annotation.sqlite.Property;
import com.sscf.investment.afinal.annotation.sqlite.Table;

/**
 * Created by liqf on 2016/9/28.
 */
@Table(name = "comment_draft")
public class CommentDraftEntity {
    @Id
    private int id;
    @Property
    private long accountId;
    @Property
    private String feedId;
    @Property
    private long replyAccountId;
    @Property
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public long getReplyAccountId() {
        return replyAccountId;
    }

    public void setReplyAccountId(long replyAccountId) {
        this.replyAccountId = replyAccountId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
