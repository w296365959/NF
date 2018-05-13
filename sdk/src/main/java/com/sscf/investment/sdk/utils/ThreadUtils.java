package com.sscf.investment.sdk.utils;

import android.os.Handler;
import android.os.Looper;
import java.lang.ref.WeakReference;

/**
 * Created by davidwei on 2016/10/11
 */
public final class ThreadUtils {
    private static WeakReference<Handler> mHandlerRef;

    private static Handler getHanlder() {
        final WeakReference<Handler> handlerRef = mHandlerRef;
        return handlerRef == null ? null : handlerRef.get();
    }

    private static Handler getOrCreateHanlder() {
        Handler handler = getHanlder();
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
            mHandlerRef = new WeakReference<>(handler);
        }
        return handler;
    }

    public static void runOnUiThread(final Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            getOrCreateHanlder().post(runnable);
        }
    }

    public static void runOnUiThreadDelay(final Runnable runnable, final long delay) {
        getOrCreateHanlder().postDelayed(runnable, delay);
    }

    public static void removeCallbacksOnUiThread(final Runnable runnable) {
        final Handler handler = getHanlder();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    public static boolean isMainThread() {
        return isMainThread(Looper.myLooper());
    }

    public static boolean isMainThread(Looper looper) {
        return looper == Looper.getMainLooper();
    }
}
