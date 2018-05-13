package com.sscf.investment.utils;

import com.sscf.investment.BuildConfig;

/**
 * Created by davidwei on 2017-09-02.
 */
public final class BuildUtils {

    public static boolean isDebug() {
        return "debug".equals(BuildConfig.BUILD_TYPE);
    }

    public static boolean isPublish() {
        return "publish".equals(BuildConfig.BUILD_TYPE);
    }
}
