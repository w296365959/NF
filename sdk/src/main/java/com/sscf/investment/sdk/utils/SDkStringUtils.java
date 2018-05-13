package com.sscf.investment.sdk.utils;

import android.text.TextUtils;

/**
 * Created by davidwei on 2017/06/29
 */
public final class SDkStringUtils {

    public static String hideSecName(final String secName) {
        final String[] removeStrs = {"*ST", "ST", "S*ST", "S*ST", "SST", "S"};
        String name = secName;
        for (String str : removeStrs) {
            if (secName.startsWith(str)) {
                name = secName.substring(str.length());
                break;
            }
        }
        if (TextUtils.isEmpty(name)) {
            return "***";
        }
        return name.charAt(0) + "***";
    }
}
