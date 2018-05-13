package com.sscf.investment.logic.component.entity;

import BEC.E_FAVOR_ACTION;
import com.dengtacj.component.entity.db.FavorItem;
import com.sscf.investment.afinal.annotation.sqlite.Id;
import com.sscf.investment.afinal.annotation.sqlite.Property;
import com.sscf.investment.afinal.annotation.sqlite.Table;
import com.sscf.investment.afinal.annotation.sqlite.Transient;
import java.io.Serializable;

/**
 * Created by davidwei on 2015-08-13.
 * 本地已经添加，或删除操作，还为与后台同步的favor，
 */
@Table(name = "setting_favor_operation")
public final class FavorOperationItem implements Serializable {
    @Transient
    private static final long serialVersionUID = 0L;
    @Id(column="id")
    protected int id;//自增ID
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
    private int action;
    @Property
    @Deprecated
    private String dtSecCode;

    public FavorOperationItem() {
    }

    public FavorOperationItem(final FavorItem favorItem, int action) {
        this.favorId = favorItem.getFavorId();
        this.favorType = favorItem.getFavorType();
        this.title = favorItem.getTitle();
        this.infoUrl = favorItem.getInfoUrl();
        this.thirdUrl = favorItem.getThirdUrl();
        this.publishTime = favorItem.getPublishTime();
        this.accountId = favorItem.getAccountId();
        this.dtSecCode = favorItem.getDtSecCode();
        this.action = action;
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

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    @Deprecated
    public void setDtSecCode(String dtSecCode) {
        this.dtSecCode = dtSecCode;
    }

    @Deprecated
    public String getDtSecCode() {
        return dtSecCode;
    }

    public String getThirdUrl() {
        return thirdUrl;
    }

    public void setThirdUrl(String thirdUrl) {
        this.thirdUrl = thirdUrl;
    }

    @Transient
    public static final int ADD = E_FAVOR_ACTION.E_FA_ADD;
    @Transient
    public static final int DELETE = E_FAVOR_ACTION.E_FA_DEL;
}

