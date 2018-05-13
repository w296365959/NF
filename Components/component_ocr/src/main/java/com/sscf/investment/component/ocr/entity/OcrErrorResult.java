package com.sscf.investment.component.ocr.entity;

import com.sscf.investment.sdk.net.ocr.result.ErrorResult;

/**
 * Created by yorkeehuang on 2017/5/25.
 */

public class OcrErrorResult extends OcrResult {

    public static final int ERROR_INIT = -3;
    public static final int ERROR_COMPRESS = -4;
    public static final int ERROR_TIMEOUT = -5;
    public static final int ERROR_CANCEL = -6;
    public static final int ERROR_COMMON = -7;

    public int error;

    public OcrErrorResult(int error) {
        super(false);
        this.error = error;
    }

    public OcrErrorResult(ErrorResult errorResult) {
        this(errorResult.error);
    }
}
