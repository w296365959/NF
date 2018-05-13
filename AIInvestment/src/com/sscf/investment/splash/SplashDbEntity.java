package com.sscf.investment.splash;

import com.sscf.investment.afinal.annotation.sqlite.Id;
import com.sscf.investment.afinal.annotation.sqlite.Property;
import com.sscf.investment.afinal.annotation.sqlite.Table;
import com.sscf.investment.afinal.annotation.sqlite.Transient;

/**
 * Created by liqf on 2016/1/20.
 */
@Table(name = "splash")
public class SplashDbEntity {
    @Transient
    private static final long serialVersionUID = 0L;

    @Id(column = "id")
    private int _id;            //自增ID

    @Property(column = "splash_id")
    private String splashId;

    @Property
    private String url;
    @Property
    private String skipUrl;
    /**
     * 闪屏上是否显示“跳过”。0-不显示；1-显示
     */
    @Property
    private int skipState;
    /**
     * 闪屏停留秒数
     */
    @Property
    private int skipSecond;

    public int getAttr() {
        return attr;
    }

    public void setAttr(int attr) {
        this.attr = attr;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(int effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public int getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(int effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    @Property(column = "attr")
    private int attr;

    @Property(column = "priority")
    private int priority;

    @Property(column = "effectiveStartTime")
    private int effectiveStartTime;

    @Property(column = "effectiveEndTime")
    private int effectiveEndTime;

    @Property(column = "play")
    private int play = 0;

    public int getPlay() {
        return play;
    }

    public void setPlay(int play) {
        this.play = play;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSplashId() {
        return splashId;
    }

    public void setSplashId(String splashId) {
        this.splashId = splashId;
    }

    public void setSkipUrl(String skipUrl) {
        this.skipUrl = skipUrl;
    }

    public String getSkipUrl() {
        return skipUrl;
    }

    public int getSkipState() {
        return skipState;
    }

    public int getSkipSecond() {
        return skipSecond;
    }

    public void setSkipState(int skipState) {
        this.skipState = skipState;
    }

    public void setSkipSecond(int skipSecond) {
        this.skipSecond = skipSecond;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
