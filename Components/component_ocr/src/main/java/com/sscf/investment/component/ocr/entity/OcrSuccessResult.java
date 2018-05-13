package com.sscf.investment.component.ocr.entity;

import com.sscf.investment.sdk.net.ocr.result.SuccessResult;

/**
 * Created by yorkeehuang on 2017/5/25.
 */

public class OcrSuccessResult extends OcrResult {

    public final String rsp;

    public OcrSuccessResult(String rsp) {
        super(true);
        this.rsp = rsp;
    }

    public OcrSuccessResult(SuccessResult successResult) {
        this(successResult.rsp);
    }
}
