package com.sscf.investment.web.sdk.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.dengtacj.component.managers.ILocalH5ResourceManager;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DownloadUtils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.ZipUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import BEC.ZipDiffRes;
import BEC.ZipRequest;
import BEC.ZipResponse;
import BEC.ZipStoreReq;
import BEC.ZipStoreRsp;

/**
 * Created by davidwei on 2017/02/27.
 */
public final class LocalH5ResourceManager implements DataSourceProxy.IRequestCallback, ILocalH5ResourceManager {
    private static final String TAG = LocalH5ResourceManager.class.getSimpleName();
    /**
     * 默认值为0，代表用assets目录里面内置的资源包
     */
    private static final int DEFAULT_H5_RESOURCE_VERSION_CODE = 0;
    /**
     * 当前H5资源包的版本号
     */
    private static final String KEY_H5_RESOURCE_VERSION_CODE = "h5_resouce_version_code";

    private static final String H5_RES_DIR_NAME = "h5_res";
    private static final String H5_RES_ZIP_NAME = "h5_res.zip";
    private static final String H5_RES_CONFIG_FILE_NAME = "app.config";

    private ArrayList<Pattern> mH5ResourcePatterns;
    private ArrayList<String> mH5ResourceHostList;
    private String mH5ResourceExclude;
    private final File mH5Dir;

    private boolean mUseH5LocalResource = true; //for test

    public LocalH5ResourceManager() {
        mH5Dir = getH5Dir();
    }

    public void setUseH5LocalResource(boolean useH5LocalResource) {
        mUseH5LocalResource = useH5LocalResource;
    }

    private static void requestQueryH5Upgrade(final int versionCode, final DataSourceProxy.IRequestCallback observer) {
        final ZipStoreReq req = new ZipStoreReq();

        final ZipRequest zipReq = new ZipRequest();
        zipReq.iVersion = versionCode;
        final ArrayList<ZipRequest> zipRequests = new ArrayList<>(1);
        zipRequests.add(zipReq);
        req.vZipRequest = zipRequests;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_QUERY_H5_RES_ZIP_UPGRADE, req, observer);
    }

    /**
     * 有io操作，请在非ui线程执行
     */
    public void init(final boolean isOverInstall) {
        DtLog.e(TAG, "init isOverInstall = " + isOverInstall);
        if (isOverInstall) { // 覆盖安装，重置版本号，重新解压内置资源包
            setH5ResourceVersionCode(DEFAULT_H5_RESOURCE_VERSION_CODE);
        }
        upzipResoucesFromAssets();
        initResourceRules();
        requestQueryH5Upgrade(getH5ResourceVersionCode(), this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success && data != null) {
            final ZipStoreRsp rsp = (ZipStoreRsp) data.getEntity();
            if (rsp != null) {
                SDKManager.getInstance().getDefaultExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        handleUpgradeCallback(rsp);
                    }
                });
            }
        }
    }

    private void handleUpgradeCallback(final ZipStoreRsp rsp) {
        final ArrayList<ZipResponse> zipResponses = rsp.vZipResponse;
        final int size = zipResponses == null ? 0 :  zipResponses.size();
        if (size == 0) {
            return;
        }

        final ZipResponse zipResponse = zipResponses.get(0);
        if (zipResponse == null || zipResponse.bLast) {
            return;
        }

        final ArrayList<ZipDiffRes> zipDiffResList = zipResponse.getVZipDiffRes();
        final int diffSize = zipDiffResList == null ? 0 : zipDiffResList.size();

        DtLog.d(TAG, "handleUpgradeCallback : callback() from server diffSize = " + diffSize);
        boolean success;
        if (diffSize > 0) { //diff查分升级模式过程
            success = diffUpgrade(zipDiffResList);
        } else { //完整的zip包升级模式
            success = wholeZipUpgrade(zipResponse);
        }

        if (success) {
            initResourceRules();
        } else { // 更新失败就使用assets里的资源
            setH5ResourceVersionCode(DEFAULT_H5_RESOURCE_VERSION_CODE);
            upzipResoucesFromAssets();
            initResourceRules();
        }
    }

    private boolean diffUpgrade(final ArrayList<ZipDiffRes> zipDiffResList) {
        final File h5Dir = mH5Dir;
        for (ZipDiffRes diffRes : zipDiffResList) {
            final String fileName = diffRes.sFileName;
            final File tempFile = new File(h5Dir, fileName + ".tmp");
            if (DownloadUtils.httpDownload(diffRes.sUrl, tempFile)) {
                final String fileMd5 = FileUtil.getFileMD5(tempFile);
                if (!TextUtils.isEmpty(fileMd5) && TextUtils.equals(fileMd5, diffRes.sMd5)) {
                    final File destFile = new File(h5Dir, fileName);
                    if (destFile.exists() && destFile.isFile()) {
                        destFile.delete();
                    }
                    if (!tempFile.renameTo(destFile)) {
                        tempFile.delete();
                        return false;
                    }
                } else {
                    tempFile.delete();
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean wholeZipUpgrade(final ZipResponse zipResponse) {
        final File zipFile = getZipFile();
        if (DownloadUtils.httpDownload(zipResponse.sUrl, zipFile)) {
            DtLog.d(TAG, "wholeZipUpgrade : httpDownload success");
            final String fileMd5 = FileUtil.getFileMD5(zipFile);
            DtLog.d(TAG, "wholeZipUpgrade : fileMd5 = " + fileMd5);
            if (!TextUtils.isEmpty(fileMd5) && TextUtils.equals(fileMd5, zipResponse.sMd5)) {
                return unzipResource();
            } else {
                zipFile.delete();
            }
        }
        DtLog.d(TAG, "wholeZipUpgrade failed");
        return false;
    }

    private void upzipResoucesFromAssets() {
        final int versionCode = getH5ResourceVersionCode();
        DtLog.d(TAG, "upzipResoucesFromAssets: versionCode = " + versionCode);
        if (versionCode == DEFAULT_H5_RESOURCE_VERSION_CODE) { //首次启动或覆盖安装，先解压assets目录的内置资源包
            DtLog.d(TAG, "upzipResoucesFromAssets : copyAssertFile");
            if (FileUtil.copyAssertFile(SDKManager.getInstance().getContext(), H5_RES_ZIP_NAME, getZipFile())) {
                DtLog.d(TAG, "copyAssertFile success ");
                unzipResource();
            }
        }
    }

    /**
     * 把h5_res.zip解压
     */
    private boolean unzipResource() {
        DtLog.d(TAG, "unzipResource");
        final File zipFile = getZipFile();
        if (zipFile.exists()) {
            final File h5Dir = mH5Dir;
            FileUtil.cleanDirectory(h5Dir);
            if (ZipUtils.unzipFiles(zipFile, h5Dir)) {
                zipFile.delete();
                DtLog.d(TAG, "unzipResource success");
                return true;
            } else {
                FileUtil.cleanDirectory(h5Dir);
                DtLog.d(TAG, "unzipResource failed");
                return false;
            }
        }
        return false;
    }

    /**
     * 从h5_res目录下读取规则信息
     */
    private void initResourceRules() {
        DtLog.d(TAG, "initResourceRules");
        final File h5Dir = mH5Dir;
        final File configFile = new File(h5Dir, H5_RES_CONFIG_FILE_NAME);
        final byte[] bytes = FileUtil.getByteArrayFromFile(configFile);
        if (bytes == null) {
            return;
        }

        final String jsonStr = new String(bytes);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        final JSONObject rules = jsonObject.optJSONObject("release");
        if (rules == null) {
            return;
        }

        // 走缓存的文件列表
        final JSONArray fileList = rules.optJSONArray("fileList");
        DtLog.d(TAG, "initResourceRules fileList = " + fileList);
        final ArrayList<Pattern> patterns = new ArrayList<>();
        final int patternsSize = fileList == null ? 0 : fileList.length();
        for (int i = 0; i < patternsSize; i++) {
            final String rule = (String) fileList.opt(i);
            if(!TextUtils.isEmpty(rule)) {
                final Pattern pattern = Pattern.compile(rule);
                patterns.add(pattern);
            }
        }

        // 走缓存的host列表
        final JSONArray hostList = rules.optJSONArray("hostList");
        DtLog.d(TAG, "initResourceRules hostList = " + hostList);
        final ArrayList<String> hosts = new ArrayList<>();
        final int hostsSize = hostList == null ? 0 : hostList.length();
        for (int i = 0; i < hostsSize; i++) {
            String host = (String) hostList.opt(i);
            hosts.add(host);
        }

        mH5ResourcePatterns = patterns;
        mH5ResourceHostList = hosts;
        mH5ResourceExclude = rules.optString("passThrough");
        DtLog.d(TAG, "initResourceRules mH5ResourceExclude = " + mH5ResourceExclude);

        setH5ResourceVersionCode(rules.optInt("version"));
        DtLog.d(TAG, "initResourceRules rules.optInt(version) = " + rules.optInt("version"));
    }

    @Override
    public File getWebResourceFile(final String url) {
        DtLog.d(TAG, "getWebFilePath url = " + url);
        if (TextUtils.isEmpty(url) || !mUseH5LocalResource) {
            return null;
        }

        final ArrayList<Pattern> patterns = mH5ResourcePatterns;
        final ArrayList<String> hostList = mH5ResourceHostList;
        DtLog.d(TAG, "getWebFilePath hostList = " + hostList);

        if (patterns == null || hostList == null) {
            return null;
        }

        final String exclude = mH5ResourceExclude;
        if (!TextUtils.isEmpty(exclude) && url.contains(exclude)) {
            return null;
        }

        for (String host : hostList) {
            if (!url.contains(host)) {
                continue;
            }

            for (Pattern pattern : patterns) {
                if (pattern == null) {
                    continue;
                }

                final Matcher matcher = pattern.matcher(url);

                if (!matcher.find()) {
                    continue;
                }

                return new File(mH5Dir.getAbsolutePath(), matcher.group());
            }
        }

        DtLog.d(TAG, "getWebFilePath not find");
        return null;
    }

    private static File getH5Dir() {
        return FileUtil.getExternalFilesDir(SDKManager.getInstance().getContext(), H5_RES_DIR_NAME);
    }

    private static File getZipFile() {
        return FileUtil.getExternalFile(SDKManager.getInstance().getContext(), H5_RES_ZIP_NAME);
    }

    private static int getH5ResourceVersionCode() {
        return getInt(KEY_H5_RESOURCE_VERSION_CODE, DEFAULT_H5_RESOURCE_VERSION_CODE);
    }

    private static void setH5ResourceVersionCode(final int version) {
        putInt(KEY_H5_RESOURCE_VERSION_CODE, version);
    }

    private static void putInt(String key, int value) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private static int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    private static SharedPreferences getSharedPreferences() {
        return SDKManager.getInstance().getContext().getSharedPreferences("dt_web_sdk", Context.MODE_PRIVATE);
    }
}
