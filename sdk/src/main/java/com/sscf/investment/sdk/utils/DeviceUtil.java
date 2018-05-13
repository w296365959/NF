package com.sscf.investment.sdk.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Looper;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by xuebinliu on 2015/8/1.
 *
 * 屏幕及设备参数获取工具类
 */
public final class DeviceUtil {

    private static final String TAG = DeviceUtil.class.getSimpleName();

    private static String PHONE_IMEI = null;

    public static boolean isNeedCreateShortcut() {
        return !getNotNeedCreateShortcutDeviceList().contains(android.os.Build.MODEL);
    }

    public static boolean isAfterAndroidO() {
        return Build.VERSION.SDK_INT >= 26;
    }

    public static boolean isAfterAndroidN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * 获取不需要创建快捷方式的手机
     *
     * @return
     */
    private static HashSet<String> getNotNeedCreateShortcutDeviceList() {
        final HashSet<String> set = new HashSet<String>();
        set.add("ZTE N939Sc");
        set.add("Coolpad 9976D");
        return set;
    }

    public static String getTopActivity(Activity context) {
        try {
            final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            final List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

            if (runningTaskInfos != null) {
                return (runningTaskInfos.get(0).topActivity).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @return px
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return px
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取手机imei
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        if (!TextUtils.isEmpty(PHONE_IMEI)) {
            return PHONE_IMEI;
        }

        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            if (mTelephonyManager != null) {
                PHONE_IMEI = mTelephonyManager.getDeviceId();
                if (TextUtils.isEmpty(PHONE_IMEI)) {
                    PHONE_IMEI = getSimImei();
                }
            }

            if (PHONE_IMEI != null) {
                PHONE_IMEI = PHONE_IMEI.toLowerCase();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return PHONE_IMEI;
    }

    private static String getSimImei() {
        Class<?> clazz;
        try {
            clazz = Class.forName("android.telephony.TelephonyManager");
            Method method = clazz.getDeclaredMethod("getSecondary", Object.class);
            method.setAccessible(true);
            TelephonyManager telManager = (TelephonyManager) method.invoke(clazz);
            if (telManager != null) {
                return telManager.getDeviceId();
            }
        } catch (Throwable e) {
            DtLog.w("Util", "getSimImei -> " + e.getMessage());
        }
        return null;
    }

    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }

        int versionCode = 0;
        if (info != null) {
            versionCode = info.versionCode;
        }

        return versionCode;
    }

    public static String getCurrentProcessName(final Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        final int myPid = android.os.Process.myPid();

        String processName = null;
        if (processes != null) {
            for (ActivityManager.RunningAppProcessInfo process : processes) {
                if (myPid == process.pid) {
                    processName = process.processName;
                    break;
                }
            }
        }

        return processName;
    }

    public static boolean isMainProcess(final Context context) {
        final String processName = getCurrentProcessName(context);
        return TextUtils.equals(context.getPackageName(), processName) || TextUtils.isEmpty(processName);
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void putToSystemClipboard(final Context context, final CharSequence content) {
        final ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, content));
    }

    /**
     * 获取剪切板内容
     *
     * @param context
     * @return
     */
    public static CharSequence getClipboardText(final Context context) {
        final ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager.hasPrimaryClip()) {
            final ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                final ClipData.Item item = clipData.getItemAt(0);
                if (item != null) {
                    return item.getText();
                }
            }
        }
        return null;
    }

    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        DtLog.d(TAG, "fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext() + " dest_context=" + destContext);
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void clearPreloadedDrawables() {
        // 只能在主线程调用
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }

        try {
            final Field field = Resources.class.getDeclaredField("sPreloadedDrawables");

            if (field != null) {
                field.setAccessible(true);

                final Object object = field.get(Resources.class);

                if (object != null) {
                    if (object instanceof LongSparseArray) {
                        final LongSparseArray<?> sPreloadedDrawables = (LongSparseArray<?>) object;

                        if (sPreloadedDrawables != null) {
                            sPreloadedDrawables.clear();
                        }
                    } else if (object instanceof LongSparseArray[]) {
                        final LongSparseArray<?>[] sPreloadedDrawables =
                                (LongSparseArray<?>[]) object;

                        if (sPreloadedDrawables != null && sPreloadedDrawables.length > 0) {
                            final int count = sPreloadedDrawables.length;

                            for (int i = 0; i < count; ++i) {
                                sPreloadedDrawables[i].clear();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Nothing to do
        }
    }

    public static void showInputMethod(final View view, final int flags) {
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, flags);
    }

    public static void showInputMethod(final View view) {
        showInputMethod(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideInputMethod(final View view) {
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), /*InputMethodManager.HIDE_NOT_ALWAYS*/0);
    }

    @TargetApi(19)
    public static void setTranslucentStatus(boolean on, Window win) {
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static void enableTranslucentStatus(final Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            enableTranslucentStatusAboveLollipop(activity, color);
        } else {
            enableTranslucentStatusBelowLollipop(activity, color);
        }
    }

    public static void enableTranslucentStatusBelowLollipop(final Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DeviceUtil.setTranslucentStatus(true, activity.getWindow());
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(color);//通知栏所需颜色
        tintManager.setNavigationBarTintEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void enableTranslucentStatusAboveLollipop(final Activity activity, int color) {
        Window window = activity.getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(color);

        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void enableDialogTranslucentStatus(final Window window, int statusBarColor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //TODO Android 5.0以下的暂时没有适配
            return;
        }

        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
//        window.setStatusBarColor(getStatusbarColor(DengtaApplication.getApplication()));
        window.setStatusBarColor(statusBarColor);
    }

    public static void setTaskDescription(final Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.setTaskDescription(new ActivityManager.TaskDescription(null, null, color));
        }
    }

    public static boolean isSystemApp(final PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    /**
     * 检测某个service服务是否在运行
     *
     * @param context
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (serviceList == null || serviceList.isEmpty())
            return false;
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) && TextUtils.equals(
                    serviceList.get(i).service.getPackageName(), context.getPackageName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 获取签名的SHA1
     *
     * @param context
     * @return
     */
    public static String getSHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i]).toUpperCase(Locale.US);
                if (appendString.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(appendString);
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isVivoX20() {
        return android.os.Build.MODEL.contains("vivo X20");
    }

}