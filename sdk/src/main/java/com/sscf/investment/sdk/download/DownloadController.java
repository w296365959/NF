package com.sscf.investment.sdk.download;

import android.os.Handler;
import android.os.Looper;

import com.sscf.investment.db.DBHelper;
import com.sscf.investment.sdk.download.entity.DownloadTaskEntity;

import java.util.concurrent.Executor;

/**
 * Created by yorkeehuang on 2017/3/8.
 */

public class DownloadController {

    private static final String TAG = DownloadController.class.getSimpleName();

    private Handler mUIHandler;
    private Executor mExecutor;
    private DBHelper mDBHelper;

    DownloadController(DownloadController controller) {
        attach(controller);
    }

    DownloadController(Handler uiHandler, Executor executor, DBHelper dbHelper) {
        mUIHandler = uiHandler;
        mExecutor = executor;
        mDBHelper = dbHelper;
    }

    synchronized void runOnUIThread(Runnable runnable) {
        if(Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            runnable.run();
        } else {
            if(mUIHandler != null) {
                mUIHandler.post(runnable);
            }
        }
    }

    synchronized void runOnExecutor(Runnable runnable) {
        if(mExecutor != null) {
            mExecutor.execute(runnable);
        }
    }

    synchronized void saveEntity(DownloadTaskEntity entity) {
        if(mDBHelper != null) {
            mDBHelper.update(entity);
        }
    }

    synchronized void add(DownloadTaskEntity entity) {

    }

    synchronized void attach(DownloadController controller) {
        mUIHandler = controller.mUIHandler;
        mExecutor = controller.mExecutor;
        mDBHelper = controller.mDBHelper;
    }

    synchronized void detach() {
        mUIHandler = null;
        mExecutor = null;
        mDBHelper = null;
    }
}
