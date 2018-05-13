package com.sscf.investment.component.keepalive;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

/**
 * Created by xuebinliu on 07/04/2017.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class KLJobService extends JobService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * false: 该系统假设任何任务运行不需要很长时间并且到方法返回时已经完成。
     * true: 该系统假设任务是需要一些时间并且当任务完成时需要调用jobFinished()告知系统。
     */
    @Override
    public boolean onStartJob(JobParameters params) {

        Log.d(KeepAlive.TAG, "onStartJob jobId=" + params.getJobId());

        KeepAlive.getInstance().sendBroadcast();

        // 完成任务后，需要重复调用
        jobFinished(params, true);

        return true;
    }

    /**
     * 当收到取消请求时，该方法是系统用来取消挂起的任务的。
     * 如果onStartJob()返回false，则系统会假设没有当前运行的任务，故不会调用该方法。
     */
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.w(KeepAlive.TAG, "onStopJob jobId=" + params.getJobId());
        return true;
    }
}
