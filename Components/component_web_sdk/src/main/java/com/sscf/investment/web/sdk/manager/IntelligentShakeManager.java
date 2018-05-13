package com.sscf.investment.web.sdk.manager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.text.TextUtils;
import com.dengtacj.component.managers.IIntelligentShakeManager;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import java.util.LinkedList;

/**
 * Created by davidwei on 2017/09/21
 */
public final class IntelligentShakeManager implements SensorEventListener, IIntelligentShakeManager {
    private static final String TAG = IntelligentShakeManager.class.getSimpleName();

    /**
     * 摇一摇开关
     */
    public static final String KEY_SHAKE_SWITCH = "key_shake_switch";
    public static final String KEY_SHAKE_SENSOR = "key_shake_sensor";
    public static final int DEFAULT_SHAKE_SENSOR = 12;

    private static final int LIST_MAX_SIZE = 8;
    private static final int THRESHOLD_COUNT = 3;

    private SensorManager mSensorManager;

    private final LinkedList<Integer> mCounts = new LinkedList<Integer>();

    private int mSensorThreshold;
    private Context mContext;
    private Vibrator mVibrator;
    private OnGetStockInfoCallback mCallback;
    private boolean mEnable;

    public void init() {
        mEnable = SettingPref.getIBoolean(KEY_SHAKE_SWITCH, true);
        mSensorThreshold = SettingPref.getInt(KEY_SHAKE_SENSOR, DEFAULT_SHAKE_SENSOR);
    }

    @Override
    public void registerShakeListener(Context context, OnGetStockInfoCallback callback) {
        mContext = context;
        mCallback = callback;
        final Context appContext = context.getApplicationContext();
        mSensorManager = (SensorManager) appContext.getSystemService(Context.SENSOR_SERVICE);
        mVibrator = (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (mSensorManager != null) {
            mCounts.clear();
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void unregisterShakeListener() {
        mContext = null;
        mCallback = null;
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final Context context = mContext;
        if (context == null) {
            return;
        }
        final int sensorThreshold = mSensorThreshold;
        if (sensorThreshold <= 0) {
            return;
        }

        // 传感器信息改变时执行该方法
        float[] values = event.values;
        float x = values[0]; // x轴方向的重力加速度，向右为正
        float y = values[1]; // y轴方向的重力加速度，向前为正
        float z = values[2]; // z轴方向的重力加速度，向上为正
//        DtLog.d(TAG, "x : " + x + ", y : " + y + ", z : " + z);

        // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
        int count = 0;
        if (Math.abs(x) >= sensorThreshold) {
            count++;
        }
        if (Math.abs(y) >= sensorThreshold) {
            count++;
        }
        if (Math.abs(z) >= sensorThreshold) {
            count++;
        }

//        DtLog.d(TAG, "sensorThreshold : " + sensorThreshold);

        mCounts.add(count);

        if (mCounts.size() > LIST_MAX_SIZE) {
            mCounts.removeFirst();
        }

        int totalCount = 0;
        for (Integer c : mCounts) {
            totalCount += c;
        }

        if (totalCount >= THRESHOLD_COUNT && NetUtil.isNetWorkConnected(context)) {
            mCounts.clear();
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            mVibrator.vibrate(200);
            showIntelligentAnswer();
        }
    }

    private void showIntelligentAnswer() {
        final Context context = mContext;
        if (context == null) {
            return;
        }

        StatisticsUtil.reportAction(StatisticsConst.SHAKE_TO_DISPLAY_INTELLIGENT_ANSWER);
        String dtCode = null;
        String secName = null;
        if (mCallback != null) {
            dtCode = mCallback.getDtCode();
            secName = mCallback.getSecName();
        }
        if (TextUtils.isEmpty(dtCode) || TextUtils.isEmpty(secName)) {
            WebBeaconJump.showIntelligentAnswer(context, false);
        } else {
            WebBeaconJump.showIntelligentAnswer(context, dtCode, secName);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void setSensorThreshold(int threshold) {
        DtLog.d(TAG, "setSensorThreshold : threshold = " + threshold);
        mSensorThreshold = threshold;
        SettingPref.putInt(KEY_SHAKE_SENSOR, threshold);
    }

    @Override
    public int getSensorThreshold() {
        if (mSensorThreshold <= 0) {
            init();
        }
        return mSensorThreshold;
    }

    @Override
    public void setEnable(boolean enable) {
        mEnable = enable;
        SettingPref.putIBoolean(KEY_SHAKE_SWITCH, enable);
    }

    @Override
    public boolean isEnable() {
        if (mSensorThreshold <= 0) {
            init();
        }
        return mEnable;
    }
}
