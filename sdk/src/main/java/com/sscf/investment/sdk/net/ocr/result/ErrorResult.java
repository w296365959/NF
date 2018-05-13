package com.sscf.investment.sdk.net.ocr.result;

/**
 * Created by yorkeehuang on 2017/5/25.
 */

public class ErrorResult extends RequestResult {
    public static int ERROR_IMAGE_LOAD = -1;
    public static int ERROR_HTTP = -2;
    public static int ERROR_USERINFO = -3;

    public final int error;

    public ErrorResult(int error) {
        super(false);
        this.error = error;
    }
}