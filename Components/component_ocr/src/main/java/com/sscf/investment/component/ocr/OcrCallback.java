package com.sscf.investment.component.ocr;

import com.sscf.investment.sdk.net.ocr.OcrHttpCallback;

/**
 * Created by yorkeehuang on 2017/5/23.
 */

public interface OcrCallback extends OcrHttpCallback {

    void onOcrStart();

    void onCompressSuccess();
}
