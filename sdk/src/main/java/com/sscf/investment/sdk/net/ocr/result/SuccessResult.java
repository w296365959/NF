package com.sscf.investment.sdk.net.ocr.result;

/**
 * Created by yorkeehuang on 2017/5/25.
 */

public class SuccessResult extends RequestResult {

    public final String rsp;

    public SuccessResult(String rsp) {
        super(true);
        this.rsp = rsp;
    }
}