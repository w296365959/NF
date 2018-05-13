package com.sscf.investment.component.ocr;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.sscf.investment.component.ocr.entity.OcrResult;
import com.sscf.investment.component.ocr.entity.OcrErrorResult;
import com.sscf.investment.component.ocr.entity.OcrSuccessResult;
import com.sscf.investment.component.ocr.utils.CompressUtils;
import com.sscf.investment.sdk.ContextHolder;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.ocr.OcrEntity;
import com.sscf.investment.sdk.net.ocr.OcrRequestManager;
import com.sscf.investment.sdk.net.ocr.result.RequestResult;
import com.sscf.investment.sdk.net.ocr.result.SuccessResult;
import com.sscf.investment.sdk.net.ocr.result.ErrorResult;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by yorkeehuang on 2017/5/23.
 */

public class OcrManager {

    private static final String TAG = OcrManager.class.getSimpleName();

    private static OcrManager sInstance = null;

    private WeakReference<OcrCallback> mCallbackRef;
    private OcrRequestManager mOcrRequestManager;

    private OcrManager() {
        mOcrRequestManager = new OcrRequestManager();
    }

    public static OcrManager get() {
        if(sInstance == null) {
            sInstance = new OcrManager();
        }
        return sInstance;
    }

    public void setCallback(OcrCallback callback) {
        mCallbackRef = new WeakReference<>(callback);
    }

    public void release() {
        if(mCallbackRef != null) {
            mCallbackRef.clear();
        }
    }

    public OcrCallback getCallback() {
        if(mCallbackRef != null) {
            mCallbackRef.get();
        }
        return null;
    }

    public void startOcrTask(final Uri uri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final OcrCallback callback = mCallbackRef.get();
                if(callback != null) {
                    DtLog.d(TAG, "FutureTask<OcrResult> task = new FutureTask(new OcrTask(uri));");
                    FutureTask<OcrResult> task = new FutureTask(new OcrTask(uri));
                    DtLog.d(TAG, "SDKManager.getInstance().getDefaultExecutor().execute(task);");
                    SDKManager.getInstance().getDefaultExecutor().execute(task);
                    OcrResult result = null;
                    try {
                        DtLog.d(TAG, "result = task.get(10, TimeUnit.SECONDS);");
                        result = task.get(10, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        DtLog.d(TAG, "InterruptedException e");
                        e.printStackTrace();
                        task.cancel(true);
                        result = new OcrErrorResult(OcrErrorResult.ERROR_CANCEL);
                    } catch (ExecutionException e) {
                        DtLog.d(TAG, "ExecutionException e");
                        e.printStackTrace();
                        task.cancel(true);
                        result = new OcrErrorResult(OcrErrorResult.ERROR_COMMON);
                    } catch (TimeoutException e) {
                        DtLog.d(TAG, "TimeoutException e");
                        e.printStackTrace();
                        task.cancel(true);
                        result = new OcrErrorResult(OcrErrorResult.ERROR_TIMEOUT);
                    }

                    if(result != null) {
                        if(result.success) {
                            OcrSuccessResult success = (OcrSuccessResult) result;
                            DtLog.d(TAG, "callback.onRequestSuccess(success.rsp)");
                            callback.onRequestSuccess(success.rsp);
                        } else {
                            OcrErrorResult error = (OcrErrorResult) result;
                            DtLog.d(TAG, "callback.onError(error.error); error = " + error.error);
                            callback.onError(error.error);
                        }
                    }
                }
            }
        }).start();
    }

    private class OcrTask implements Callable<OcrResult> {

        private Uri mUri;

        public OcrTask(@NonNull Uri uri) {
            mUri = uri;
        }

        @Override
        public OcrResult call() {
            if(mUri != null) {
                Context context = ContextHolder.getCtx();
                OcrCallback callback = mCallbackRef.get();
                if(context == null || callback == null) {
                    return new OcrErrorResult(OcrErrorResult.ERROR_INIT);
                }
                ContentResolver cr = context.getContentResolver();
                callback.onOcrStart();
                File file = createTmpFile();
                if(CompressUtils.compressFromUri(cr, createCompressOptions(file, mUri))) {
                    callback.onCompressSuccess();
                    OcrEntity ocrEntity = new OcrEntity(file);
                    callback.onRequestStart();
                    RequestResult result = mOcrRequestManager.request(ocrEntity);
                    if(result.success) {
                        SuccessResult success = (SuccessResult) result;
                        return new OcrSuccessResult(success);
                    } else {
                        ErrorResult error = (ErrorResult) result;
                        return new OcrErrorResult(error);
                    }
                } else {
                    return new OcrErrorResult(OcrErrorResult.ERROR_COMPRESS);
                }
            } else {
                return new OcrErrorResult(OcrErrorResult.ERROR_INIT);
            }
        }

        private File createTmpFile() {
            return new File(FileUtil.getScanFileDir() + File.separator + "pic.tmp");
        }

        private CompressUtils.CompressOptions createCompressOptions(File file, Uri uri) {
            CompressUtils.CompressOptions opt = new CompressUtils.CompressOptions();
            opt.destFile = file;
            opt.imgFormat = Bitmap.CompressFormat.JPEG;
            opt.uri = uri;
            return opt;
        }
    }
}
