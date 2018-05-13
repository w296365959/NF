package com.sscf.investment.sdk;

import android.content.Context;

/**
 * Created by yorkeehuang on 2017/3/7.
 */

public class ContextHolder {
    private static Context sCtx = null;


    public static void init(Context ctx) {
        sCtx = ctx;
    }

    public static Context getCtx() {
        return sCtx;
    }
}
