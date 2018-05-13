package com.dengtacj.component.managers;

import java.io.File;

/**
 * Created by davidwei on 2017/09/04
 */
public interface ILocalH5ResourceManager {
    void init(final boolean isOverInstall);
    void setUseH5LocalResource(boolean use);
    File getWebResourceFile(String url);
}
