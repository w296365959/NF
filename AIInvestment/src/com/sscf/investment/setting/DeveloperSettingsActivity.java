package com.sscf.investment.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.*;
import android.text.TextUtils;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ILocalH5ResourceManager;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.MainActivity;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.IPManager;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.setting.manager.LogRequestManager;
import com.sscf.investment.setting.widgt.DeveloperSettingPref;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.ZipUtils;
import com.sscf.investment.component.ui.widget.LoadingDialog;
import com.dengtacj.component.router.WebBeaconJump;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.PrintStream;
import BEC.ELogReportRetCode;
import BEC.ReportLogRsp;

public final class DeveloperSettingsActivity extends Activity implements Handler.Callback{
    public static final int MSG_SHOW_LOADING_DIALOG = 1;
    private Handler mHandler;
    private boolean mDestroy;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragement()).commit();
        mHandler = new Handler(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDestroy = true;
    }

    public static final class PrefsFragement extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        private static final String TEST_JUMP_URL = "http://192.168.9.114:55554/demo/beacon/protocol.html";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
            getPreferenceManager().setSharedPreferencesName(DeveloperSettingPref.DEVELOPER_SETTINGS);

            addPreferencesFromResource(R.xml.develeper_settings);

            refreshServerTypePreference();

            Preference serverIP = findPreference(DeveloperSettingPref.KEY_SERVER_IP);
            serverIP.setSummary(IPManager.getInstance().getIpString(true));

            Preference guid = findPreference(DeveloperSettingPref.KEY_GUID);
            guid.setSummary(SettingPref.getString(SettingConst.SETTING_APP_GUID, ""));

            Preference testJump = findPreference(DeveloperSettingPref.KEY_TEST_JUMP);
            testJump.setSummary(TEST_JUMP_URL);

            AccountManager am = dengtaApplication.getAccountManager();

            Preference imei = findPreference(DeveloperSettingPref.KEY_IMEI);
            String imeiStr = DeviceUtil.getImei(getActivity());
            if (imeiStr == null) {
                imeiStr = "";
            }
            imei.setSummary(imeiStr);

            Preference dtid = findPreference(DeveloperSettingPref.KEY_DTID);
            dtid.setSummary(String.valueOf(am.getAccountId()));

            Preference dua = findPreference(DeveloperSettingPref.KEY_DUA);
            dua.setSummary(am.getDUA());

            Preference deviceToken = findPreference(DeveloperSettingPref.KEY_DEVICE_TOKEN);

            // umeng device token
            final String token = PushAgent.getInstance(dengtaApplication).getRegistrationId();
            deviceToken.setSummary(token);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            final Activity activity = getActivity();
            if (activity == null) {
                return;
            }
            if (DeveloperSettingPref.KEY_SERVER_TYPE.equals(key)) {
                //清除数据重启
                new AsynClearDataTask().executeOnExecutor(DengtaApplication.getApplication().defaultExecutor, activity);
            } else if (DeveloperSettingPref.KEY_USE_H5_LOCAL_CACHE.equals(key)) {
                CheckBoxPreference useH5CahcePreference = (CheckBoxPreference) findPreference(key);
                boolean checked = useH5CahcePreference.isChecked();
                final ILocalH5ResourceManager localH5ResourceManager = (ILocalH5ResourceManager) ComponentManager.getInstance().getManager(ILocalH5ResourceManager.class.getName());
                if (localH5ResourceManager != null) {
                    localH5ResourceManager.setUseH5LocalResource(checked);
                }
            } else if (DeveloperSettingPref.KEY_SAVE_LOG_TO_FILE.equals(key)) {
                DtLog.setFileLogEnable(((CheckBoxPreference) findPreference(key)).isChecked());
            }
        }

        private void refreshServerTypePreference() {
            ListPreference serverTypePreference = (ListPreference) findPreference(DeveloperSettingPref.KEY_SERVER_TYPE);
            String serverType = DeveloperSettingPref.getServerType();
            String[] array = DengtaApplication.getApplication().getResources().getStringArray(R.array.entries_list_server_type);
            serverTypePreference.setSummary(array[Integer.valueOf(serverType)]);
        }

        @Override
        public void onResume() {
            super.onResume();
            DeveloperSettingPref.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            DeveloperSettingPref.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            final String key = preference.getKey();

            final DeveloperSettingsActivity activity = (DeveloperSettingsActivity) getActivity();

            if (DeveloperSettingPref.KEY_UPLOAD_LOG.equals(key)) {
                if (activity != null) {
                    if (NetUtil.isNetWorkConnected(activity)) {
                        activity.showLoadingDialog();
                        new UploadLogsTask().executeOnExecutor(DengtaApplication.getApplication().defaultExecutor, activity);
                    } else {
                        DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                    }
                }
            } else if (DeveloperSettingPref.KEY_UPLOAD_ANR.equals(key)) {
                if (activity != null) {
                    if (NetUtil.isNetWorkConnected(activity)) {
                        activity.showLoadingDialog();
                        new UploadANRTask().executeOnExecutor(DengtaApplication.getApplication().defaultExecutor, activity);
                    } else {
                        DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                    }
                }
            } else if (DeveloperSettingPref.KEY_TEST_JUMP.equals(key)) {
                if (activity != null) {
                    WebBeaconJump.showCommonWebActivity(activity, TEST_JUMP_URL);
                }
            } else if (!(preference instanceof ListPreference
                || preference instanceof CheckBoxPreference)) {
                final CharSequence summary = preference.getSummary();
                if (!TextUtils.isEmpty(summary)) {
                    DeviceUtil.putToSystemClipboard(getActivity(), preference.getSummary());
                    DengtaApplication.getApplication().showToast(preference.getTitle()
                            + ":\n已复制到系统剪贴板\n在任意输入框长按粘贴");
                }
            }

            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
    }

    public boolean isDestroy() {
        return mDestroy || isFinishing();
    }

    public void showLoadingDialog() {
        if (isDestroy()) {
            return;
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            showLoadingDialogOnUI();
        } else {
            mHandler.sendEmptyMessage(MSG_SHOW_LOADING_DIALOG);
        }
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
        mHandler.removeMessages(MSG_SHOW_LOADING_DIALOG);
        if (isDestroy()) {
            return;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        showLoadingDialogOnUI();
        return true;
    }
}

/**
 * 清除数据的异步任务
 * 覆盖安装清除数据后，再次回到主界面
 */
final class AsynClearDataTask extends AsyncTask<Activity, Void, Void> {
    private Activity mActivity;

    @Override
    protected Void doInBackground(Activity... params) {
        mActivity = params[0];
        doClearCacheData();

        MobclickAgent.onKillProcess(DengtaApplication.getApplication());
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        DengtaApplication.getApplication().showToast("数据清理完成");

        final Activity activity = mActivity;
        if (activity != null) {
            Intent intent = new Intent(DengtaApplication.getApplication(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
        }

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void doClearCacheData() {
        deleteDir(new File(FileUtil.getPackageDataPath()));
        deleteDir(new File(FileUtil.getSDCardPackageDataPath()));
    }

    private void deleteDir(File mBaseDir) {
        try {
            String[] files = mBaseDir.list();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File f = new File(mBaseDir, files[i]);
                    if (f.isDirectory()) {
                        deleteDir(f);
                    } else {
                        deleteFile(f);
                    }
                }
                deleteFile(mBaseDir);
            } else { //mBaseDir是文件而不是文件夹
                deleteFile(mBaseDir);
            }
        } catch (SecurityException e) {}
    }

    private void deleteFile(File f) {
        //不删除记录开发者选项的preference文件
        if (TextUtils.equals(f.getAbsolutePath(), DeveloperSettingPref.getDeveloperSettingsPrefPath())) {
            return;
        }

        f.delete();
    }


}

final class UploadLogsTask extends AsyncTask<DeveloperSettingsActivity, Object, Boolean> implements FileFilter, DataSourceProxy.IRequestCallback {
    DeveloperSettingsActivity mActivity;
    File[] mLogFiles;

    @Override
    protected Boolean doInBackground(DeveloperSettingsActivity[] params) {
        mActivity = params[0];

        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        final File logDir = FileUtil.getExternalFilesDir(dengtaApplication, "log");
        if (logDir.exists() && logDir.isDirectory()) {
            final File[] files = logDir.listFiles(this);
            final int length = files == null ? 0 : files.length;
            if (length > 0) {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                if (ZipUtils.zipOutput(files, out)) {
                    final byte[] data = out.toByteArray();
                    if (data != null && data.length > 0) {
                        mLogFiles = files;
                        LogRequestManager.reportLogRequest(data, "android_log", "zip", this);
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        if (b) {
            DengtaApplication.getApplication().showToast("没有log文件");
            mActivity.dismissLoadingDialog();
        }
    }

    @Override
    public boolean accept(File file) {
        return file.isFile() && file.getName().endsWith(".log");
    }

    @Override
    public void callback(final boolean success, final EntityObject data) {
        final DeveloperSettingsActivity activity = mActivity;
        boolean uploadSuccess = false;
        if (success && data != null) {
            final ReportLogRsp rsp = (ReportLogRsp) data.getEntity();
            if (rsp != null && rsp.iRetCode == ELogReportRetCode.E_REPORT_SUCC) {
                final File[] logFiles = mLogFiles;
                if (logFiles != null) {
                    for (File file : logFiles) {
                        file.delete();
                    }
                }
                uploadSuccess = true;
            }
        }

        final String tips = uploadSuccess ? "上报成功" : "上报失败，请稍后重试";
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.dismissLoadingDialog();
                DengtaApplication.getApplication().showToast(tips);
            }
        });
    }
}

final class UploadANRTask extends AsyncTask<DeveloperSettingsActivity, Object, Boolean> implements DataSourceProxy.IRequestCallback {
    DeveloperSettingsActivity mActivity;

    @Override
    protected Boolean doInBackground(DeveloperSettingsActivity[] params) {
        mActivity = params[0];

        final File anrFile = new File("/data/anr/traces.txt");
        if (anrFile.exists() && anrFile.length() > 0) {
            final byte[] data = getANRData(anrFile);
            final int length = data == null ? 0 : data.length;
            if (length > 0) {
                LogRequestManager.reportLogRequest(data, "android_anr", "txt", this);
                return false;
            }
        }
        return true;
    }

    private byte[] getANRData(final File anrFile) {
        BufferedReader reader = null;
        byte[] data = null;
        final int READ_LINES = 500;
        PrintStream out = null;
        String line = null;
        try {
            reader = new BufferedReader(new FileReader(anrFile));
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(READ_LINES * 50);
            out = new PrintStream(baos);
            for (int i = 0; i < READ_LINES && (line = reader.readLine()) != null; i++) {
                out.println(line);
            }
            out.flush();
            data = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeStream(reader);
            FileUtil.closeStream(out);
        }
        return data;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        if (b) {
            DengtaApplication.getApplication().showToast("没有anr文件");
            mActivity.dismissLoadingDialog();
        }
    }

    @Override
    public void callback(final boolean success, final EntityObject data) {
        final DeveloperSettingsActivity activity = mActivity;
        boolean uploadSuccess = false;
        if (success && data != null) {
            final ReportLogRsp rsp = (ReportLogRsp) data.getEntity();
            if (rsp != null && rsp.iRetCode == ELogReportRetCode.E_REPORT_SUCC) {
                uploadSuccess = true;
            }
        }

        final String tips = uploadSuccess ? "上报成功" : "上报失败，请稍后重试";
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.dismissLoadingDialog();
                DengtaApplication.getApplication().showToast(tips);
            }
        });
    }
}
