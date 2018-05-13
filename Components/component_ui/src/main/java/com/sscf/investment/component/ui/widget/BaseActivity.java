package com.sscf.investment.component.ui.widget;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.sscf.investment.sdk.stat.TotalFrontTimeStatHelper;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.ThreadUtils;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected TimeStatHelper mTimeStatHelper;
    private boolean mDestroy;

    private LoadingDialog mLoadingDialog;
    private Runnable mLoadingRunnable;
    protected Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mHandler = new Handler();
        super.onCreate(savedInstanceState);
        mTimeStatHelper = createTimeStatHelper();
    }

    protected TimeStatHelper createTimeStatHelper() {
        return new TotalFrontTimeStatHelper();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mTimeStatHelper != null) {
            mTimeStatHelper.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mTimeStatHelper != null) {
            mTimeStatHelper.end();
        }
    }

    @Override
    protected void onDestroy() {
        DtLog.d(TAG, "onDestroy");
        dismissLoadingDialog();
        mDestroy = true;
        try {
            super.onDestroy();
        } catch (Exception e) { // 处理java.lang.RuntimeException: Error during detachFromGLContext
            DtLog.w(TAG, "onDestroy Exception, msg" + e.getMessage());
        }
        mLoadingDialog = null;
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
        } catch (IllegalStateException e) {
            //防止出现IllegalStateException: Can not perform this action after onSaveInstanceState
            DtLog.w(TAG, "onBackPressed Exception, msg" + e.getMessage());
        }
    }

    public boolean isDestroy() {
        return mDestroy || isFinishing();
    }

    public void showLoadingDialog() {
        if (isDestroy()) {
            return;
        }

        if (ThreadUtils.isMainThread()) {
            showLoadingDialogOnUI();
        } else {
            Runnable runnable = mLoadingRunnable;
            if (runnable == null) {
                runnable = () -> showLoadingDialogOnUI();
                mLoadingRunnable = runnable;
            }
            mHandler.post(runnable);
        }
    }

    public void showLoadingDialogDelay(long delay) {
        if (isDestroy()) {
            return;
        }

        mHandler.postDelayed(mLoadingRunnable, delay);
    }

    private void showLoadingDialogOnUI() {
        if (isDestroy()) {
            return;
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        mHandler.removeCallbacks(mLoadingRunnable);
        if (isDestroy()) {
            return;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
