package com.dengtacj.component.entity;

import java.io.Serializable;

/**
 * Created by davidwei on 2017-09-15.
 */

public class PluginInfo implements Serializable {
    public final int type;
    public String name;
    public String packageName;
    public int versionCode;
    public String title;
    public String info;
    public long fileSize;
    public String url;
    public String md5;
    public boolean autoInstall = false;

    public PluginInfo(int type) {
        this.type = type;
    }
}
