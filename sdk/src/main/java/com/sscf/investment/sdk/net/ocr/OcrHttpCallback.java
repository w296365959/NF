package com.sscf.investment.sdk.net.ocr;

/**
 * Created by yorkeehuang on 2017/5/23.
 */

public interface OcrHttpCallback {

    void onRequestStart();

    void onRequestSuccess(String rsp);

    void onError(int error);
}
