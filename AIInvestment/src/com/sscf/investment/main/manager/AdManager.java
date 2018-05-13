package com.sscf.investment.main.manager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DownloadUtils;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.sdk.utils.SettingPref;
import java.io.File;
import java.io.FileFilter;
import java.util.Calendar;
import BEC.DT_ACTIVITY_TYPE;
import BEC.DtActivityDetail;

/**
 * Created by yorkeehuang on 2017/3/21.
 */
public final class AdManager implements DataSourceProxy.IRequestCallback {

    private File mTempFile;
    private DtActivityDetail mAdDetail;

    public AdManager() {
        mTempFile = new File(FileUtil.getAdImageFileDir(), "download.temp");
        resetTemp();
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_DT_ACTIVITY_LIST:
                final DtActivityDetail detail = EntityUtil.entityToDtActivityDetail(success, data);
                if (detail != null) {
                    if (!checkAdAlreadyShow(detail, System.currentTimeMillis())) {
                        mAdDetail = detail;
                        downloadImagesIfNeeded(detail.getSPicUrl());
                        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(new Intent(SettingConst.ACTION_FULL_SCREEN_AD));
                    }
                }
                break;
            default:
        }
    }

    private void resetTemp() {
        if(mTempFile.exists()) {
            mTempFile.delete();
        }
    }

    public void reqAd() {
        AdRequestManager.requestDtActivityList(DT_ACTIVITY_TYPE.T_ACTIVITY_FULL_SCREEN_ADS, this);
    }

    private void downloadImagesIfNeeded(String url) {
        File dir = new File(FileUtil.getAdImageFileDir());
        File downloadFile = new File(FileUtil.getAdImageFilePathByUrl(url));
        if(dir.isDirectory()) {
            try {
                File[] oldFiles = dir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        return !TextUtils.equals(downloadFile.getPath(), file.getPath());
                    }
                });

                for (File oldFile : oldFiles) {
                    oldFile.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(!downloadFile.exists()) {
            DengtaApplication.getApplication().defaultExecutor.execute(() -> {
                boolean success = DownloadUtils.httpDownload(url, mTempFile);
                if (success) {
                    try {
                        mTempFile.renameTo(downloadFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public Bitmap getAdBitmap() {
        final DtActivityDetail detail = mAdDetail;
        if (detail != null) {
            String url = detail.getSPicUrl();
            if(!TextUtils.isEmpty(url)) {
                File bmpFile = new File(FileUtil.getAdImageFilePathByUrl(url));
                if(bmpFile.exists()) {
                    return BitmapFactory.decodeFile(bmpFile.getPath());
                }
            }
        }
        return null;
    }

    public DtActivityDetail getAdDetail() {
        return mAdDetail;
    }

    public void saveOnShow(DtActivityDetail detail) {
        if(detail != null) {
            setLastFullScreenAdPicUrl(detail.getSPicUrl());
            setLastFullScreenAdUrl(detail.getSUrl());
            setLastFullScreenAdTime(System.currentTimeMillis());
        }
    }

    private boolean checkAdAlreadyShow(DtActivityDetail detail, long time) {
        String storedPicUrl = getLastFullScreenAdPicUrl();
        if(!TextUtils.equals(storedPicUrl, detail.getSPicUrl())) {
            return false;
        }

        String storedUrl = getLastFullScreenAdUrl();
        if(!TextUtils.equals(storedUrl, detail.getSUrl())) {
            return false;
        }

        long storedTime = getLastFullScreenAdTime();
        if(!compareDate(storedTime, time)) {
            return false;
        }

        return true;
    }

    public static boolean compareDate(long src, long target) {
        if(src == 0L || target == 0L) {
            return false;
        }

        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(src);
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(target);
        if(c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) {
            return false;
        } else if(c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)) {
            return false;
        } else if(c1.get(Calendar.DAY_OF_MONTH) != c2.get(Calendar.DAY_OF_MONTH)) {
            return false;
        }
        return true;
    }

    private void setLastFullScreenAdPicUrl(String picUrl) {
        SettingPref.putString(SettingConst.KEY_FULL_SCREEN_AD_PIC_URL, picUrl);
    }

    private String getLastFullScreenAdPicUrl() {
        return SettingPref.getString(SettingConst.KEY_FULL_SCREEN_AD_PIC_URL, "");
    }

    private void setLastFullScreenAdUrl(String url) {
        SettingPref.putString(SettingConst.KEY_FULL_SCREEN_AD_URL, url);
    }

    private String getLastFullScreenAdUrl() {
        return SettingPref.getString(SettingConst.KEY_FULL_SCREEN_AD_URL, "");
    }

    private void setLastFullScreenAdTime(long time) {
        SettingPref.putLong(SettingConst.KEY_FULL_SCREEN_AD_TIME, time);
    }

    private long getLastFullScreenAdTime() {
        return SettingPref.getLong(SettingConst.KEY_FULL_SCREEN_AD_TIME, 0L);
    }
}
