package com.sscf.investment.sdk.download;

/**
 * Created by yorkeehuang on 2017/2/28.
 */

public interface DownloadObserver {

    public static int ERROR_NETWORK_UNAVAILABLE = -1;
    public static int ERROR_DOWNLOAD_FAILED = -2;
    public static int ERROR_CHECK_FILE_FAILED = -3;

    void onInitialed(int progress);

    void onStart(int progress);

    void onPause(int progress);

    void onProgress(int progress);

    void onError(int error);

    void onFinish(boolean isInitCheck);
}
