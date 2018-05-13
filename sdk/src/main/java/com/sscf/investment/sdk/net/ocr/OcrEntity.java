package com.sscf.investment.sdk.net.ocr;

import java.io.File;

/**
 * Created by yorkeehuang on 2017/5/23.
 */

public class OcrEntity {

    public File mFile;

    public OcrEntity(String path) {
        mFile = new File(path);
    }

    public OcrEntity(File file) {
        mFile = file;
    }

    public File getFile() {
        return mFile;
    }
}
