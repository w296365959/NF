package com.sscf.investment.setting;

import BEC.UpgradeInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chenenyu.router.annotation.Route;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.download.DownloadObserver;
import com.sscf.investment.sdk.download.DownloadTask;
import com.sscf.investment.sdk.download.DtDownloadManager;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.PackageUtil;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.widgt.UpgradeProgressButton;
import com.sscf.investment.utils.*;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import java.io.*;

/**
 * davidwei
 * 升级界面
 */
@Route("UpgradeActivity")
public final class UpgradeActivity extends BaseFragmentActivity implements View.OnClickListener, DownloadObserver {
    private static final String TAG = UpgradeActivity.class.getSimpleName();
    private FrameLayout mBtnPanel;
    private UpgradeProgressButton mUpgradeButton;
    private View mCancelButton;
    private String mDefaultButtonText;

    private UpgradeInfo mUpgradeInfo;
    private long mApkFileLength;

    private int mDownloadProgress;

    private DtDownloadManager mDownloadManager;
    private DownloadTask mDownloadTask;

    private File mApkFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUpgradeInfo = DengtaApplication.getApplication().getUpgradeManager().getUpgradeInfo();

//        mUpgradeInfo.sURL = "http://116.211.120.35/dd.myapp.com/16891/27F619DCE0E4332E5CB7EC255F300ACA.apk?mkey=55ffcc849a048ace&f=1224&fsname=com.tencent.mm_6.2.5.53r2565f18_621.apk&asr=02f1&p=.apk";
//        mUpgradeInfo.sMd5 = "27f619dce0e4332e5cb7ec255f300aca";
//        mUpgradeInfo.sFileSize = "32251172";

        // 没有更新信息就直接退出
        if (mUpgradeInfo == null) {
            finish();
            return;
        }

        setContentView(R.layout.activity_setting_upgrade);

        mApkFileLength = StringUtil.parseLong(mUpgradeInfo.sFileSize, 0);

        initViews();
        mDownloadManager = DengtaApplication.getApplication().getDownloadManager();
        String path = FileUtil.getExternalFilesDir(DengtaApplication.getApplication(), "upgrade").getPath();
        String fileName = "upgrade.apk";
        mDownloadTask = mDownloadManager.addTask(mUpgradeInfo.getSURL(), path, fileName, false, mApkFileLength, mUpgradeInfo.getSMd5());

        if(mDownloadTask != null) {
            mApkFile = mDownloadTask.getFile();
            mDownloadTask.addObserver(this);
            mDownloadTask.init();
            DengtaApplication.getApplication().getRedDotManager().setUpgradeState(false);
        } else {
            finish();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDownloadTask != null) {
            mDownloadTask.removeObserver(this);
        }
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.setting_upgrade_title);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        ((TextView) findViewById(R.id.upgradeInfoTitle)).setText(mUpgradeInfo.sTitle);
        ((TextView) findViewById(R.id.upgradeInfoContent)).setText(mUpgradeInfo.sText);


        mBtnPanel = (FrameLayout) findViewById(R.id.btn_panel);
        mUpgradeButton = (UpgradeProgressButton) findViewById(R.id.upgradeButton);
        mUpgradeButton.setOnClickListener(this);

        mCancelButton = findViewById(R.id.upgradeCancel);
        mCancelButton.setOnClickListener(this);

        mDefaultButtonText = getString(R.string.setting_install, StringUtil.sizeToString(mApkFileLength));
        mUpgradeButton.setText(mDefaultButtonText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.upgradeCancel:
                clickCancelButton();
                break;
            case R.id.upgradeButton:
                clickUpgradeButton();
                break;
            default:
                break;
        }
    }

    @Override
    public void onInitialed(int progress) {
        DtLog.d(TAG, "onInitialed()");
        mBtnPanel.setVisibility(View.VISIBLE);
        if (progress > 0) {
            mDownloadProgress = progress;
            mUpgradeButton.setText(getString(R.string.setting_download_pause, String.valueOf(progress)));
            mUpgradeButton.setProgress(progress);
            mCancelButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart(int progress) {
        DtLog.d(TAG, "onStart() progress = " + progress);
        mDownloadProgress = progress;
        mUpgradeButton.setProgress(progress);
        mUpgradeButton.setText(getString(R.string.setting_downloading, String.valueOf(progress)));
    }

    @Override
    public void onPause(int progress) {
        DtLog.d(TAG, "onPause() progress = " + progress);
        mDownloadProgress = progress;
        mUpgradeButton.setProgress(progress);
        mUpgradeButton.setText(getString(R.string.setting_download_pause, String.valueOf(progress)));
    }

    @Override
    public void onProgress(int progress) {
        DtLog.d(TAG, "onProgress() progress = " + progress);
        mDownloadProgress = progress;
        mUpgradeButton.setProgress(progress);
        mUpgradeButton.setText(getString(R.string.setting_downloading, String.valueOf(progress)));
    }

    @Override
    public void onError(int error) {
        DtLog.d(TAG, "onError() error = " + error);
        switch (error) {
            case DownloadObserver.ERROR_NETWORK_UNAVAILABLE:
                DengtaApplication.getApplication().showToast(R.string.error_tips_network_error);
                break;
            case DownloadObserver.ERROR_CHECK_FILE_FAILED:
            case DownloadObserver.ERROR_DOWNLOAD_FAILED:
                mUpgradeButton.setProgress(100);
                mUpgradeButton.setText(R.string.setting_download_failed);
                mCancelButton.setVisibility(View.INVISIBLE);
                mApkFile.delete();
                break;
            default:
        }
    }

    @Override
    public void onFinish(boolean isInitCheck) {
        DtLog.d(TAG, "onFinish()");
        if(!isInitCheck) {
            PackageUtil.installApk(UpgradeActivity.this, mApkFile);
        }
        mUpgradeButton.setText(mDefaultButtonText);
        mCancelButton.setVisibility(View.INVISIBLE);
    }

    private void clickUpgradeButton() {
        if (mApkFile.exists() && mApkFile.isFile()) {
            PackageUtil.installApk(this, mApkFile);
            return;
        }

        switch (mDownloadTask.getData().getState()) {
            case DownloadTask.STATE_DOWNLOADING:
                mDownloadTask.pause();
                mUpgradeButton.setText(getString(R.string.setting_download_pause, String.valueOf(mDownloadProgress)));
                mCancelButton.setVisibility(View.VISIBLE);
                break;
            case DownloadTask.STATE_FAILED:
            case DownloadTask.STATE_INITIALED:
            case DownloadTask.STATE_PAUSE:
            case DownloadTask.STATE_FINISH:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_UPGRADE_CLICK_DOWNLOAD);
                if (!NetUtil.isNetWorkConnected(this)) {
                    DengtaApplication.getApplication().showToast(R.string.error_tips_network_error);
                    return;
                }

                mCancelButton.setVisibility(View.VISIBLE);
                mDownloadTask.start();
                break;
            default:
                break;
        }
    }

    private void clickCancelButton() {
        mCancelButton.setVisibility(View.INVISIBLE);
        mDownloadProgress = 0;
        mUpgradeButton.setProgress(100);
        mUpgradeButton.setText(mDefaultButtonText);
        SettingPref.putInt(SettingConst.KEY_DOWNLOADED_UPGRADE_VERSION_CODE, -1);
    }
}
