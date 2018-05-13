package com.dengtacj.component.entity.db;

import com.dengtacj.component.entity.RemindedMessageItem;
import com.sscf.investment.afinal.annotation.sqlite.Id;
import com.sscf.investment.afinal.annotation.sqlite.Property;
import com.sscf.investment.afinal.annotation.sqlite.Table;
import com.sscf.investment.afinal.annotation.sqlite.Transient;
import java.io.Serializable;

/**
 * Created by davidwei on 2015-08-13.
 */
@Table(name = "setting_favor")
public class FavorItem implements Serializable {
    @Transient
    private static final long serialVersionUID = 3L;
    @Id(column="id")
    private int id;//自增ID
    @Property(column="favor_id")
    protected String favorId;
    @Property(column="favor_type")
    protected int favorType;
    @Property(column="title")
    protected String title;// 标题
    @Property(column="publish_time")
    protected long publishTime;// 发布时间
    @Property(column="info_url")
    protected String infoUrl;// 资讯的网页地址
    @Property(column="third_url")
    protected String thirdUrl;// 第三方的网页地址
    @Property(column="account_id")
    protected long accountId;
    @Property
    protected long favorTime;  // 收藏的时间
    @Property
    @Deprecated
    protected String dtSecCode;

    /**
     * 必须有空构造
     */
    public FavorItem() {
    }

    public FavorItem(long accountId, String favorId, int favorType, String title, String infoUrl, long publishTime) {
        this.favorId = favorId;
        this.favorType = favorType;
        this.title = title;
        this.infoUrl = infoUrl;
        this.publishTime = publishTime;
        this.accountId = accountId;
    }

    public FavorItem(final long accountId, final RemindedMessageItem item) {
        this(accountId, item.newsId, item.newsType, item.getFavorTitle(), item.infoUrl, item.publishTime);
    }

    public long getFavorTime() {
        return favorTime;
    }

    public void setFavorTime(long favorTime) {
        this.favorTime = favorTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setFavorId(String favorId) {
        this.favorId = favorId;
    }

    public String getFavorId() {
        return favorId;
    }

    public void setFavorType(int favorType) {
        this.favorType = favorType;
    }

    public int getFavorType() {
        return favorType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    @Override
    public boolean equals(Object o) {
        final FavorItem favorItem = ((FavorItem) o);
        return favorItem != null && favorItem.accountId == accountId && favorId.equals(favorItem.favorId);
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getAccountId() {
        return accountId;
    }

    @Deprecated
    public void setDtSecCode(String dtSecCode) {
        this.dtSecCode = dtSecCode;
    }

    public String getThirdUrl() {
        return thirdUrl;
    }

    public void setThirdUrl(String thirdUrl) {
        this.thirdUrl = thirdUrl;
    }

    @Deprecated
    public String getDtSecCode() {
        return dtSecCode;
    }

    @Override
    public String toString() {
        return title;
    }
}

