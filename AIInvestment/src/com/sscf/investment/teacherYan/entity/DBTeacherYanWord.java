package com.sscf.investment.teacherYan.entity;

import com.sscf.investment.afinal.annotation.sqlite.Id;
import com.sscf.investment.afinal.annotation.sqlite.Property;
import com.sscf.investment.afinal.annotation.sqlite.Table;
import com.sscf.investment.afinal.annotation.sqlite.Transient;

import java.io.Serializable;

/**
 * Created by LEN on 2018/4/26.
 */
@Table(name = "teacher_yan_word")
public class DBTeacherYanWord implements Serializable {

    @Transient
    private static final long serialVersionUID = 0L;

    @Id
    private int id;

    @Property(column = "audioId")
    private int audioId;

    @Property(column="title")
    private String title;

    @Property(column = "updatetime")
    private String updateTime;

    @Property(column = "listenernum")
    private String listenerNum;

    @Property(column = "url")
    private String url;

    public int getAudioId() {
        return audioId;
    }

    public String getTitle() {
        return title;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getListenerNum() {
        return listenerNum;
    }

    public String getUrl() {
        return url;
    }

    public void setAudioId(int audioId) {
        this.audioId = audioId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setListenerNum(String listenerNum) {
        this.listenerNum = listenerNum;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
