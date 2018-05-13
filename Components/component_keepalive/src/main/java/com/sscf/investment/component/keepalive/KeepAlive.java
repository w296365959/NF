package com.sscf.investment.component.keepalive;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.marswin89.marsdaemon.DaemonClient;
import com.marswin89.marsdaemon.DaemonConfigurations;

/**
 * Created by xuebinliu on 07/04/2017.
 */
public class KeepAlive {
    public static final String TAG = "KeepAlive";

    private static final int JOB_ID = 20151126;

    private DaemonClient mDaemonClient;

    private static KeepAlive instance;

    private Context context;
    private String action;

    private KeepAlive() {
    }

    public synchronized static KeepAlive getInstance() {
        if (instance == null) {
            instance = new KeepAlive();
        }
        return instance;
    }

    /**
     * 启动服务
     * @param context
     * @param action 定时发送action广播
     *@param isStartDeamon 是否开启native守护
     */
    public void start(Context context, String action, boolean isStartDeamon) {
        Log.d(TAG, "start action=" + action);

        if (context == null) {
            return;
        }

        this.context = context;
        this.action = action;

        // 开启守护进程
        if (mDaemonClient == null && isStartDeamon) {
            mDaemonClient = new DaemonClient(createDaemonConfigurations());
            mDaemonClient.onAttachBaseContext(context);
        }

        // api 21或以上提供了job service服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

                jobScheduler.cancel(JOB_ID);

                JobInfo jobInfo = new JobInfo.Builder(JOB_ID, new ComponentName(context.getPackageName(), KLJobService.class.getName()))
                        .setPeriodic(10000)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .build();

                int result = jobScheduler.schedule(jobInfo);

                Log.d(TAG, "start job result=" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "low version not start job");
        }
    }

    public void sendBroadcast() {
        Log.d(TAG, "sendBroadcast action=" + action);
        if (context == null || TextUtils.isEmpty(action)) {
            return;
        }

        try {
            Intent intent = new Intent(action);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DaemonConfigurations createDaemonConfigurations(){
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                "com.sscf.investment:channel",
                EmptyService1.class.getCanonicalName(),
                EmptyReceiver1.class.getCanonicalName());
        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "com.sscf.investment:daemon",
                EmptyService2.class.getCanonicalName(),
                EmptyReceiver2.class.getCanonicalName());

        return new DaemonConfigurations(configuration1, configuration2, new MyDaemonListener());
    }

    class MyDaemonListener implements DaemonConfigurations.DaemonListener{
        @Override
        public void onPersistentStart(Context context) {
            Log.d(TAG, "onPersistentStart");
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
            Log.d(TAG, "onDaemonAssistantStart");
        }

        @Override
        public void onWatchDaemonDaed() {
            Log.d(TAG, "onWatchDaemonDead");
        }
    }
}
