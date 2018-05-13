package com.sscf.investment.logic.component.manager;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.ShareParams;
import com.dengtacj.component.managers.IScreenShotManager;
import com.dengtacj.component.managers.IShareManager;
import com.sscf.investment.component.ui.widget.CommonToast;
import com.sscf.investment.logic.R;
import com.sscf.investment.logic.widget.ScreenShotShareDialog;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yorkeehuang on 2017/8/17.
 */

public final class ScreenShotManager implements IScreenShotManager {
    private static final String TAG = "ScreenShotListenManager";

    /** 读取媒体数据库时需要读取的列, 其中 WIDTH 和 HEIGHT 字段在 API 16 以后才有 */
    private static final String[] MEDIA_PROJECTIONS = {
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.WIDTH,
            MediaStore.Images.ImageColumns.HEIGHT,
    };

    /** 截屏依据中的路径判断关键字 */
    private static final String[] KEYWORDS = {
            "screenshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap"
    };

    private static Point sScreenRealSize;

    /** 已回调过的路径 */
    private final List<String> sHasCallbackPaths = new ArrayList<String>();

    private long mStartListenTime;

    /** 内部存储器内容观察者 */
    private MediaContentObserver mInternalObserver;

    /** 外部存储器内容观察者 */
    private MediaContentObserver mExternalObserver;

    /** 运行在 UI 线程的 Handler, 用于运行监听器回调 */
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

    private Activity mActivity;

    @Override
    public void init() {
        // 获取屏幕真实的分辨率
        if (sScreenRealSize == null) {
            sScreenRealSize = getRealScreenSize();
            if (sScreenRealSize != null) {
                DtLog.d(TAG, "Screen Real Size: " + sScreenRealSize.x + " * " + sScreenRealSize.y);
            } else {
                DtLog.w(TAG, "Get screen real size failed.");
            }
        }
    }

    /**
     * 启动监听
     */
    public void startListen(Activity activity) {
        mActivity = activity;
        assertInMainThread();

        sHasCallbackPaths.clear();

        // 记录开始监听的时间戳
        mStartListenTime = System.currentTimeMillis();

        // 创建内容观察者
        mInternalObserver = new MediaContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, mUiHandler);
        mExternalObserver = new MediaContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mUiHandler);

        final Context context = SDKManager.getInstance().getContext();
        // 注册内容观察者
        context.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                false,
                mInternalObserver
        );
        context.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                false,
                mExternalObserver
        );
    }

    /**
     * 停止监听
     */
    public void stopListen() {
        mActivity = null;
        assertInMainThread();

        final Context context = SDKManager.getInstance().getContext();
        // 注销内容观察者
        if (mInternalObserver != null) {
            try {
                context.getContentResolver().unregisterContentObserver(mInternalObserver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mInternalObserver = null;
        }
        if (mExternalObserver != null) {
            try {
                context.getContentResolver().unregisterContentObserver(mExternalObserver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mExternalObserver = null;
        }

        // 清空数据
        mStartListenTime = 0;
        sHasCallbackPaths.clear();
    }

    private String mLastPath;

    /**
     * 处理媒体数据库的内容改变
     */
    private void handleMediaContentChange(Uri contentUri) {
        Cursor cursor = null;
        if(TextUtils.equals(mLastPath, contentUri.getPath())) {
            return;
        }
        mLastPath = contentUri.getPath();
        DtLog.d(TAG, "handleMediaContentChange() mLastPath = " + mLastPath);
        final Context context = SDKManager.getInstance().getContext();
        try {
            // 数据改变时查询数据库中最后加入的一条数据
            cursor = context.getContentResolver().query(
                    contentUri,
                    MEDIA_PROJECTIONS,
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1"
            );

            if (cursor == null) {
                DtLog.e(TAG, "Deviant logic.");
                return;
            }
            if (!cursor.moveToFirst()) {
                DtLog.d(TAG, "Cursor no data.");
                return;
            }

            // 获取各列的索引
            int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);
            int widthIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH);
            int heightIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT);

            // 获取行数据
            String data = cursor.getString(dataIndex);

            if(TextUtils.equals(mLastPath, data)) {
                return;
            }
            mLastPath = data;

            long dateTaken = cursor.getLong(dateTakenIndex);
            int width = 0;
            int height = 0;
            if (widthIndex >= 0 && heightIndex >= 0) {
                width = cursor.getInt(widthIndex);
                height = cursor.getInt(heightIndex);
            } else {
                // API 16 之前, 宽高要手动获取
                Point size = getImageSize(data);
                width = size.x;
                height = size.y;
            }

            // 处理获取到的第一行数据
            handleMediaRowData(data, dateTaken, width, height);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private Point getImageSize(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return new Point(options.outWidth, options.outHeight);
    }

    /**
     * 处理获取到的一行数据
     */
    private void handleMediaRowData(final String data, long dateTaken, int width, int height) {
        if (checkScreenShot(data, dateTaken, width, height)) {
            DtLog.d(TAG, "ScreenShot: path = " + data + "; size = " + width + " * " + height
                    + "; date = " + dateTaken);
            if (!checkCallback(data)) {
                if(!TextUtils.isEmpty(data)) {
                    new Handler().post(() -> {
                                StatisticsUtil.reportAction(StatisticsConst.CAPTURE_SYSTEM_SCREEN_SHOT);
                                showScreenShotFloatView(data);
                            }
                    );
                }
            }
        } else {
            // 如果在观察区间媒体数据库有数据改变，又不符合截屏规则，则输出到 log 待分析
            DtLog.w(TAG, "Media content changed, but not screenshot: path = " + data
                    + "; size = " + width + " * " + height + "; date = " + dateTaken);
        }
    }

    private void showScreenShotFloatView(String imagePath) {
        final Activity activity = mActivity;
        if (activity == null) {
            return;
        }
        final File file = new File(imagePath);
        final ScreenShotShareDialog screenShotShareDialog = new ScreenShotShareDialog(activity);
        screenShotShareDialog.setCanCancelOnTouchOutside(true);
        screenShotShareDialog.setButtonClickListener((dialog, view, position) -> {
            if(position == 0) {
                if(file.exists()) {
                    final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
                            .getManager(IShareManager.class.getName());
                    if (shareManager == null) {
                        return;
                    }
                    final ShareParams params = ShareParams.createShareParams("", "#股票灯塔#", "", "");
                    params.putFile(file);
                    shareManager.showShareDialog(activity, params);
                } else {
                    CommonToast.showToast(R.string.screen_shot_no_data);
                }
            }
        });
        screenShotShareDialog.show();
    }

    /**
     * 判断指定的数据行是否符合截屏条件
     */
    private boolean checkScreenShot(String data, long dateTaken, int width, int height) {
        /*
         * 判断依据一: 时间判断
         */
        // 如果加入数据库的时间在开始监听之前, 或者与当前时间相差大于10秒, 则认为当前没有截屏
        if (dateTaken < mStartListenTime || (System.currentTimeMillis() - dateTaken) > 10 * 1000) {
            return false;
        }

        /*
         * 判断依据二: 尺寸判断
         */
        if (sScreenRealSize != null) {
            // 如果图片尺寸超出屏幕, 则认为当前没有截屏
            if (!((width <= sScreenRealSize.x && height <= sScreenRealSize.y) ||
                            (height <= sScreenRealSize.x && width <= sScreenRealSize.y))) {
                return false;
            }
        }

        /*
         * 判断依据三: 路径判断
         */
        if (TextUtils.isEmpty(data)) {
            return false;
        }
        data = data.toLowerCase();
        // 判断图片路径是否含有指定的关键字之一, 如果有, 则认为当前截屏了
        for (String keyWork : KEYWORDS) {
            if (data.contains(keyWork)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否已回调过, 某些手机ROM截屏一次会发出多次内容改变的通知; <br/>
     * 删除一个图片也会发通知, 同时防止删除图片时误将上一张符合截屏规则的图片当做是当前截屏.
     */
    private boolean checkCallback(String imagePath) {
        if (sHasCallbackPaths.contains(imagePath)) {
            return true;
        }
        // 大概缓存15~20条记录便可
        if (sHasCallbackPaths.size() >= 20) {
            for (int i = 0; i < 5; i++) {
                sHasCallbackPaths.remove(0);
            }
        }
        sHasCallbackPaths.add(imagePath);
        return false;
    }

    /**
     * 获取屏幕分辨率
     */
    private Point getRealScreenSize() {
        Point screenSize = null;
        final Context context = SDKManager.getInstance().getContext();
        try {
            screenSize = new Point();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display defaultDisplay = windowManager.getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                defaultDisplay.getRealSize(screenSize);
            } else {
                try {
                    Method mGetRawW = Display.class.getMethod("getRawWidth");
                    Method mGetRawH = Display.class.getMethod("getRawHeight");
                    screenSize.set(
                            (Integer) mGetRawW.invoke(defaultDisplay),
                            (Integer) mGetRawH.invoke(defaultDisplay)
                    );
                } catch (Exception e) {
                    screenSize.set(defaultDisplay.getWidth(), defaultDisplay.getHeight());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenSize;
    }

    private static void assertInMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            String methodMsg = null;
            if (elements != null && elements.length >= 4) {
                methodMsg = elements[3].toString();
            }
            throw new IllegalStateException("Call the method must be in main thread: " + methodMsg);
        }
    }

    /**
     * 媒体内容观察者(观察媒体数据库的改变)
     */
    private class MediaContentObserver extends ContentObserver {

        private static final int NOTIFY_INTERVAL = 1000;

        private Uri mContentUri;

        private long mLastChangeTime = System.currentTimeMillis();

        public MediaContentObserver(Uri contentUri, Handler handler) {
            super(handler);
            mContentUri = contentUri;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            long now = System.currentTimeMillis();
            if(now - mLastChangeTime > NOTIFY_INTERVAL) {
                mLastChangeTime = now;
                handleMediaContentChange(mContentUri);
            }
        }
    }
}
