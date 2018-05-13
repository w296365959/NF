package com.sscf.investment.main;

import android.os.Debug;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import com.sscf.investment.BuildConfig;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.sdk.utils.*;
import com.sscf.investment.sdk.utils.DataUtils;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by davidwei on 2015/9/9.
 * 捕获异常的处理
 */
public final class DengtaCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = DengtaCrashHandler.class.getSimpleName();
    private static DengtaCrashHandler instance;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private final long bootTime;

    private long mLastCrash;

    private DengtaCrashHandler() {
        bootTime = SystemClock.elapsedRealtime();
    }

    private void setDefaultHandler() {
        final Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 判断是否是系统的UncaughtExceptionHandler
        final String className = defaultHandler.getClass().getName();
        DtLog.d(TAG, "setDefaultHandler defaultHandler : " + className);
        if (!className.startsWith("com.android") && defaultHandler != this) {
            // 不是系统的UncaughtExceptionHandler就需要保存
            mDefaultHandler = defaultHandler;
        }
        DtLog.d(TAG, "setDefaultHandler mDefaultHandler : " + mDefaultHandler);
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static DengtaCrashHandler getInstance() {
        if (instance == null) {
            instance = new DengtaCrashHandler();
        }
        instance.setDefaultHandler();
        return instance;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        DtLog.d(TAG, "uncaughtException thread : " + thread);
        DtLog.d(TAG, "uncaughtException ex : " + ex);
        final long now = System.currentTimeMillis();
        if (now - mLastCrash < 3000) {
            return;
        }
        mLastCrash = now;

        if (mDefaultHandler != null) {
            DtLog.d(TAG, "uncaughtException mDefaultHandler.uncaughtException start : " + mDefaultHandler);
            try {
                mDefaultHandler.uncaughtException(thread, ex);
            } catch (Throwable e) { // 避免第三方调用出现异常
                e.printStackTrace();
            }
            DtLog.d(TAG, "uncaughtException mDefaultHandler.uncaughtException end");
        }
        handleException(thread, ex);
    }

    private void handleException(final Thread thread, final Throwable ex) {
        DtLog.d(TAG, "handleException");
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();

        final long DELAY = 1000L;
        // 相隔1s以内都算是启动
        final boolean isBoot = SystemClock.elapsedRealtime() - bootTime < DELAY;

        if (isBoot && thread != null && thread == Looper.getMainLooper().getThread()) {
            // 如果是主线程就主动上报
            StatisticsUtil.reportError(ex);
        }

        DtLog.d(TAG, "handleException onKillProcess start");
        try {
            MobclickAgent.onKillProcess(dengtaApplication);
        } catch (Exception e) { // 可能NullPointerException
        }
        DtLog.d(TAG, "handleException onKillProcess end");

        dengtaApplication.defaultExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DtLog.d(TAG, "handleException run");
                ex.printStackTrace();

                if (!TextUtils.equals(BuildConfig.BUILD_TYPE, "publish")) {
                    if (ex instanceof OutOfMemoryError) {
                        DtLog.d(TAG, "handleException dumpHprofData");
                        try {
                            Debug.dumpHprofData(new File(FileUtil.getExternalFilesDir(dengtaApplication, "log"),
                                    PackageUtil.getCurrentProcessName(dengtaApplication) + "_oom.prof").getAbsolutePath());
                            DtLog.d(TAG, "handleException dumpHprofData success");
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }

                saveCrashLog(ex);

                // 防止启动就crash，导致部分逻辑没执行
                if (isBoot) {
                    DtLog.d(TAG, "handleException sleep start");
                    try {
                        Thread.sleep(DELAY);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    DtLog.d(TAG, "handleException sleep end");
                }

                DtLog.d(TAG, "handleException killProcess");
                try {
                    android.os.Process.killProcess(android.os.Process.myPid());
                } catch (Throwable e) {
                    e.printStackTrace();
                    DtLog.d(TAG, "handleException killProcess Exception : " + e);
                }
            }
        });
    }

    private void saveCrashLog(final Throwable ex) {
        DtLog.d(TAG, "saveCrashLog ex");
        final long now = System.currentTimeMillis();

        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        PrintWriter out = null;
        final AccountManager accountManager = dengtaApplication.getAccountManager();
        final byte[] guid = accountManager.getGuid();
        final String guidString = DataUtils.bytesToHexString(guid);
        final String dua = accountManager.getDUA();
        final String time = TimeUtils.getTimeString("yyyy.MM.dd HH:mm:ss", now);
        try {
            final File crashFile = new File(FileUtil.getCrashDir(dengtaApplication),
                    PackageUtil.getCurrentProcessName(dengtaApplication) + '_' + now + ".log");
            out = new PrintWriter(new FileOutputStream(crashFile, true));
            out.print("time:");
            out.println(time);
            out.print("guid:");
            out.println(guidString);
            out.print("dua:");
            out.println(dua);
            out.print("imei:");
            out.println(DeviceUtil.getImei(dengtaApplication));
            out.print("network:");
            out.println(NetUtil.getNetworkType(dengtaApplication));
            out.print("accountId:");
            out.println(accountManager.getAccountId());

            ex.printStackTrace(out);
            out.flush();
            DtLog.d(TAG, "saveCrashLog success");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeStream(out);
        }
    }
}
