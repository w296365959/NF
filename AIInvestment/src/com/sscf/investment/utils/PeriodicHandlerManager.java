package com.sscf.investment.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.SettingPref;

/**
 * Created by davidwei
 * 循环调用接口
 */
public final class PeriodicHandlerManager implements Handler.Callback {
    private static final String TAG = PeriodicHandlerManager.class.getSimpleName();
	private final Handler mHandler;
    private final Runnable mCallback;
    private int mDelay = SettingPref.DEFAULT_DELAY_TIME;

    public PeriodicHandlerManager(final Runnable callback) {
        this(callback, Looper.getMainLooper());
    }

    public PeriodicHandlerManager(final Runnable callback, final Looper looper) {
        mHandler = new Handler(looper, this);
        mCallback = callback;
    }

    public void setDelay(int delay) {
        mDelay = delay;
    }

    public void runPeriodic() {
        DtLog.d(TAG, "PeriodicHandlerManager.runPeriodic Looper.myLooper() : " + Looper.myLooper());
        if (mHandler.getLooper() == Looper.myLooper()) {
            run();
        } else {
            mHandler.sendEmptyMessage(0);
        }
    }

    public void runPeriodicDelay(final int delay) {
        mHandler.sendEmptyMessageDelayed(0, delay);
    }

    public void stop() {
        mHandler.removeMessages(0);
        DtLog.d(TAG, "PeriodicHandlerManager.stop mHandler.hasMessages(0) : " + mHandler.hasMessages(0));
    }

    @Override
    public boolean handleMessage(Message msg) {
        run();
        return true;
    }

    private void run() {
        DtLog.d(TAG, "PeriodicHandlerManager.run mCallback : " + mCallback);
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, mDelay);
        mCallback.run();
    }
}
