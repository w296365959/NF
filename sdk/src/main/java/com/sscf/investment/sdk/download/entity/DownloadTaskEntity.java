package com.sscf.investment.sdk.download.entity;

import com.sscf.investment.afinal.annotation.sqlite.Id;
import com.sscf.investment.afinal.annotation.sqlite.Property;
import com.sscf.investment.afinal.annotation.sqlite.Table;

/**
 * Created by yorkeehuang on 2017/2/28.
 */
@Table(name = "download_task")
public class DownloadTaskEntity {
    @Id
    private int taskId;
    @Property
    private String url;
    @Property
    private String path;
    @Property
    private String fileName;
    @Property
    private long totalSize;
    @Property
    private long downloadedSize;
    @Property
    private int state;
    @Property
    private boolean isUnique;
    @Property
    private String md5;

    public int getTaskId() {
        return taskId;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }

    public long getDownloadedSize() {
        return downloadedSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public int getState() {
        return state;
    }

    public String getMd5() {
        return md5;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public void setDownloadedSize(long downloadedSize) {
        this.downloadedSize = downloadedSize;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
