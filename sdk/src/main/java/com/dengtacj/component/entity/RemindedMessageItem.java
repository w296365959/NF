package com.dengtacj.component.entity;

import android.text.TextUtils;
import com.sscf.investment.sdk.utils.TimeUtils;
import java.io.Serializable;
import java.util.ArrayList;
import BEC.TagInfo;

/**
 * Created by davidwei on 2015-08-13.
 */
public final class RemindedMessageItem implements Serializable, Comparable<RemindedMessageItem> {
    private static final long serialVersionUID = 2L;
    public String id;
    public String title;
    public String content;
    public int messageType;
    public long publishTime;// 发布时间

    public String publishDateString;
    public String publishTimeString;

    // 股价提醒相关
    public String dtCode;

    // 新闻资讯相关，公告，研报，重大新闻
    public int newsType;
    public String newsId;
    public String infoUrl;// 资讯的网页地址

    // 投顾，互动消息使用
    public long accountId;
    public String nickname;
    public boolean userType; // true是大v，false不是大v
    public String userIconUrl;
    public String message;
    public ArrayList<TagInfo> tagInfos;
    public int feedType;

    public static final int TYPE_SEC = 1;
    public static final int TYPE_ANNOUNCEMENT = 2;
    public static final int TYPE_RESEARCH = 3;
    public static final int TYPE_IMPORTANT_NEWS = 4;
    public static final int TYPE_ACTIVITY = 5;
    public static final int TYPE_NEW_SHARE = 6;
    public static final int TYPE_DAILY_REPORT = 7;
    public static final int TYPE_INVESTMENT_ADVISER_EVENTS = 8;
    public static final int TYPE_INTERACT_MESSAGE = 9;
    public static final int TYPE_VALUEADDED_SERVICES = 10;

    public RemindedMessageItem() {
    }

    public RemindedMessageItem(String id, String title, String content, long publishTime, int messageType) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.publishTime = publishTime;
        this.messageType = messageType;
    }

    /**
     * 股票提醒使用
     */
    public RemindedMessageItem(String id, String title, String content, long publishTime, int messageType, String dtCode) {
        this(id, title, content, publishTime, messageType);
        this.dtCode = dtCode;
    }

    /**
     * 公告研报提醒使用
     */
    public RemindedMessageItem(String id, String title, String content, long publishTime, int messageType, String dtCode,
                               String infoUrl, String newsId, int newsType) {
        this(id, title, content, publishTime, messageType);
        this.dtCode = dtCode;
        this.infoUrl = infoUrl;
        this.newsId = newsId;
        this.newsType = newsType;
    }

    public String getFavorTitle() {
        String favorTitle = title;
        switch (messageType) {
            case RemindedMessageItem.TYPE_ANNOUNCEMENT:
            case RemindedMessageItem.TYPE_RESEARCH:
                favorTitle = content;
                break;
            default:
                break;
        }
        return favorTitle;
    }

    public String getPublishDateString() {
        if (TextUtils.isEmpty(publishDateString)) {
            if (publishTime > 0) {
                publishDateString = TimeUtils.timeStamp2SimpleDate(publishTime);
            } else {
                publishDateString = "";
            }
        }
        return publishDateString;
    }

    public String getPublishTimeString() {
        if (TextUtils.isEmpty(publishTimeString)) {
            if (publishTime > 0) {
                publishTimeString = TimeUtils.getTimeString("HH:mm", publishTime);
            } else {
                publishTimeString = "";
            }
        }
        return publishTimeString;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o.getClass().equals(this.getClass()) && TextUtils.equals(((RemindedMessageItem) o).id, id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public int compareTo(RemindedMessageItem item) {
        final long time = item != null ? item.publishTime : 0L;
        int res = 0;
        if (publishTime < time) {
            res = 1;
        } else if (publishTime > time) {
            res = -1;
        }
        return res;
    }
}
