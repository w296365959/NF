package com.sscf.investment.sdk.net.ocr.result;

/**
 * Created by yorkeehuang on 2017/5/25.
 */

public abstract class RequestResult {
    public final boolean success;
    public RequestResult(boolean success) {
        this.success = success;
    }
}
