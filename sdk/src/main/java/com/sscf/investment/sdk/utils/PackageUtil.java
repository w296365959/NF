package com.sscf.investment.sdk.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

/**
 * Created by davidwei on 2015/09/21.
 *
 */
public final class PackageUtil {

    public static void installApk(final Context context, final String filePath) {
        installApk(context, new File(filePath));
    }

    public static void installApk(final Context context, final File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return VersionCode
     */
    public static int getVersionCode(final Context context) {
        PackageInfo info = getPackageInfo(context);

        int versionCode = 0;
        if (info != null) {
            versionCode = info.versionCode;
        }
        return versionCode;
    }

    public static String getVersionName(final Context context) {
        final PackageInfo info = getPackageInfo(context);
        String versionName = "";
        if (info != null) {
            versionName = info.versionName;
        }
        return versionName;
    }

    public static PackageInfo getPackageInfo(Context context) {
        context = context.getApplicationContext();
        final PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * @return appNmae
     */
    public static String getAppName(final Context context) {
        final PackageManager manager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = manager.getApplicationInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String appName = "AI智投";
        if (applicationInfo != null) {
            try {
                appName = manager.getApplicationLabel(applicationInfo).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return appName;
    }

    private static String processName = null;

    public static String getCurrentProcessName(Context context) {
        if (processName == null) {
            final int pid = android.os.Process.myPid();
            final ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            final List<ActivityManager.RunningAppProcessInfo> processes = activityManager
                    .getRunningAppProcesses();
            if (processes != null) {
                for (ActivityManager.RunningAppProcessInfo appProcess : processes) {
                    if (appProcess.pid == pid) {
                        processName = appProcess.processName;
                        processName = processName.replaceAll(":", "_");
                        break;
                    }
                }
            }
        }
        return processName;
    }

    public static void openUrl(Context context, String url) {
        if(context == null || TextUtils.isEmpty(url)) {
            return;
        }

        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
