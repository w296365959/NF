package com.sscf.investment.sdk.download;

/**
 * Created by yorkeehuang on 2017/3/7.
 */

public interface InnerDownloadObserver  {

    void onInitialed(DownloadTask task, int progress);

    void onStart(DownloadTask task, int progress);

    void onPause(DownloadTask task, int progress);

    void onProgress(DownloadTask task, int progress);

    void onError(DownloadTask task, int error);

    void onFinish(DownloadTask task, boolean isInitCheck);
}
