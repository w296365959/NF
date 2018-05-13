package com.sscf.investment.splash;

import BEC.*;
import android.text.TextUtils;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.db.DBHelper;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.ProtoManager;
import com.sscf.investment.sdk.utils.DownloadUtils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by liqf on 2016/1/20.
 */
public final class SplashDataManager implements DataSourceProxy.IRequestCallback {
    private static final String TAG = SplashDataManager.class.getSimpleName();
    private static final String QUERY_EFFECTIVE_ITEMS = "effectiveStartTime <= '%d' and effectiveEndTime >= '%d'";
    private static final String ORDER_BY_PLAY_PRIORITY = "play asc, priority desc";
    private static final String QUERY_OUTDATED_ITEMS = "effectiveEndTime < '%d'";
    private static final String QUERY_BY_SPLASH_ID = "splash_id = '%s'";
    private List<SplashDbEntity> mSplashList;

    public void checkSplashUpdate() {
        getSplashUpdateRequest(this);
    }

    public static void getSplashUpdateRequest(final DataSourceProxy.IRequestCallback observer) {
        final GetConfigReq req = new GetConfigReq();
        final ArrayList<Integer> types = new ArrayList<Integer>(1);
        types.add(E_CONFIG_TYPE.E_CONFIG_SPLASH_SCREEN);
        req.vType = types;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GET_SPLASH_UPDATE, req, observer);
    }

    public SplashDbEntity findCurrentSplash() {
        int nowTime = (int) (System.currentTimeMillis() / 1000);
        String where = String.format(Locale.getDefault(), QUERY_EFFECTIVE_ITEMS, nowTime, nowTime);
        DtLog.d(TAG, "findCurrentSplash where=" + where);

        List<SplashDbEntity> splashList = DBHelper.getInstance().findAllByWhere(SplashDbEntity.class, where, ORDER_BY_PLAY_PRIORITY);
        if (splashList == null || splashList.size() == 0) {
            return null;
        }
        mSplashList = splashList;
        final SplashDbEntity currentSplash = splashList.get(0);
        currentSplash.setPlay(1);
        return currentSplash;
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        DengtaApplication.getApplication().defaultExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DtLog.e(TAG, data.getJSONString());
                processCallback(success, data);
            }
        });
    }

    private void processCallback(boolean success, EntityObject data) {
        if (success) {
            final ArrayList<ConfigDetail> configs = ((GetConfigRsp) data.getEntity()).getVList();
            for (ConfigDetail config : configs) {
                if (config.iType == E_CONFIG_TYPE.E_CONFIG_SPLASH_SCREEN) {
                    final SplashScreenList splashScreenList = new SplashScreenList();

                    if (ProtoManager.decode(splashScreenList, config.vData)) {
                        ArrayList<SplashScreenInfo> splashScreenInfos = splashScreenList.getVList();
                        if (splashScreenInfos != null) {
                            for (int i = 0 ; i < splashScreenInfos.size() ; i++){
                                DtLog.e(TAG, "splashScreenInfos = " + splashScreenInfos.get(i).getSUrl());
                            }

                            updateData(splashScreenInfos);
                            downloadImagesIfNeeded();
                            return;
                        }
                    }
                }
            }
        }
        updatePlayStatus();
    }

    private void updatePlayStatus() {
        if (mSplashList != null) {
            boolean playAll = true;
            for (SplashDbEntity currentSplashScreenInfo : mSplashList) {
                if (currentSplashScreenInfo.getPlay() == 0) {
                    playAll = false;
                }
            }
            for (SplashDbEntity currentSplashScreenInfo : mSplashList) {
                if (playAll) {// // 如果全部都播放过，全部设置为没播放
                    currentSplashScreenInfo.setPlay(0);
                }
                DBHelper.getInstance().update(currentSplashScreenInfo);
            }
        }
    }

    private synchronized void updateData(ArrayList<SplashScreenInfo> splashScreenInfos) {
        removeOutdatedInfos();
        removeNotExistInfos(splashScreenInfos);
        saveToDB(splashScreenInfos);
    }

    private void removeOutdatedInfos() {
        int nowTime = (int) (System.currentTimeMillis() / 1000);
        String where = String.format(Locale.getDefault(), QUERY_OUTDATED_ITEMS, nowTime);
        DtLog.d(TAG, "findCurrentSplash where=" + where);

        List<SplashDbEntity> splashList = DBHelper.getInstance().findAllByWhere(SplashDbEntity.class, where);
        if (splashList == null || splashList.size() == 0) {
            return;
        }

        removeOutdatedImageFiles(splashList);

        removeOutdatedDBItems(nowTime);
    }

    private void removeOutdatedDBItems(int nowTime) {
        String where = String.format(Locale.getDefault(), QUERY_OUTDATED_ITEMS, nowTime);
        DBHelper.getInstance().deleteByWhere(SplashDbEntity.class, where);
    }

    private void removeOutdatedImageFiles(List<SplashDbEntity> splashList) {
        for (SplashDbEntity dbEntity : splashList) {
            String url = dbEntity.getUrl();
            String imageFilePath = FileUtil.getSplashImageFilePathByUrl(url);
            new File(imageFilePath).delete();
        }
    }

    private void removeNotExistInfos(ArrayList<SplashScreenInfo> splashScreenInfos) {
        List<String> newIds = new ArrayList<>();
        for (SplashScreenInfo splashScreenInfo : splashScreenInfos) {
            String id = splashScreenInfo.getSID();
            newIds.add(id);
        }

        List<SplashDbEntity> oldSplashList = DBHelper.getInstance().findAll(SplashDbEntity.class);
        if (oldSplashList == null) {
            return;
        }
        List<SplashDbEntity> notExistSplashList = new ArrayList<>();
        for (SplashDbEntity oldDbEntity : oldSplashList) {
            String oldId = oldDbEntity.getSplashId();
            if (!newIds.contains(oldId)) {
                String where = String.format(Locale.getDefault(), QUERY_BY_SPLASH_ID, oldId);
                DBHelper.getInstance().deleteByWhere(SplashDbEntity.class, where);

                notExistSplashList.add(oldDbEntity);
            }
        }

        removeOutdatedImageFiles(notExistSplashList);
    }

    private void downloadImagesIfNeeded() {
        List<SplashDbEntity> splashList = DBHelper.getInstance().findAll(SplashDbEntity.class);
        if (splashList == null || splashList.size() == 0) {
            return;
        }

        removeUnNeededImageFiles(splashList);

        for (SplashDbEntity dbEntity : splashList) {
            final String url = dbEntity.getUrl();
            final String imageFilePath = FileUtil.getSplashImageFilePathByUrl(url);
            if (new File(imageFilePath).exists()) {
                continue;
            }

            DengtaApplication.getApplication().defaultExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    String tmpDownloadFile = imageFilePath + ".download";
                    boolean success = DownloadUtils.httpDownload(url, tmpDownloadFile);
                    if (success) {
                        new File(tmpDownloadFile).renameTo(new File(imageFilePath));
                    }
                }
            });
        }
    }

    private void removeUnNeededImageFiles(List<SplashDbEntity> splashList) {
        String dir = FileUtil.getSplashImageFileDir();
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            return;
        }

        List<String> filePaths = new ArrayList<>();
        for (SplashDbEntity dbEntity : splashList) {
            String url = dbEntity.getUrl();
            String pathByUrl = FileUtil.getSplashImageFilePathByUrl(url);
            filePaths.add(pathByUrl);
        }

        String[] files = dirFile.list();
        if (files != null) {
            for (String file : files) {
                if (!filePaths.contains(file)) {
                    new File(file).delete();
                }
            }
        }
    }

    private void saveToDB(ArrayList<SplashScreenInfo> splashScreenInfos) {
        final ArrayList<SplashDbEntity> newSplashScreenInfos = new ArrayList<SplashDbEntity>();
        if (mSplashList != null) {
            boolean playAll = true;
            for (SplashScreenInfo splashScreenInfo : splashScreenInfos) {
                final SplashDbEntity splashDbEntity = buildSplashDBEntity(splashScreenInfo);
                for (SplashDbEntity currentSplashScreenInfo : mSplashList) {
                    if (TextUtils.equals(splashDbEntity.getSplashId(), currentSplashScreenInfo.getSplashId())) {
                        splashDbEntity.set_id(currentSplashScreenInfo.get_id());
                        splashDbEntity.setPlay(currentSplashScreenInfo.getPlay());
                    }
                }
                newSplashScreenInfos.add(splashDbEntity);
                if (splashDbEntity.getPlay() == 0) {
                    playAll = false;
                }
            }
            final DBHelper dbHelper = DBHelper.getInstance();
            for (SplashDbEntity currentSplashScreenInfo : newSplashScreenInfos) {
                if (playAll) {// 如果全部都播放过，全部设置为没播放
                    currentSplashScreenInfo.setPlay(0);
                }
                if (!alreadyExits(currentSplashScreenInfo.getSplashId())) {
                    dbHelper.add(currentSplashScreenInfo);
                } else {
                    dbHelper.update(currentSplashScreenInfo);
                }
            }
        } else {
            for (SplashScreenInfo splashScreenInfo : splashScreenInfos) {
                String splashId = splashScreenInfo.getSID();
                if (!alreadyExits(splashId)) {
                    doAddEntity(splashScreenInfo);
                } else {
                    doUpdateEntity(splashScreenInfo);
                }
            }
        }
    }

    private boolean alreadyExits(String splashId) {
        String where = String.format(Locale.getDefault(), QUERY_BY_SPLASH_ID, splashId);
        List<SplashDbEntity> splashList = DBHelper.getInstance().findAllByWhere(SplashDbEntity.class, where);
        if (splashList == null || splashList.size() == 0) {
            return false;
        }
        return true;
    }

    private void doAddEntity(SplashScreenInfo splashScreenInfo) {
        SplashDbEntity splashDbEntity = buildSplashDBEntity(splashScreenInfo);

        DBHelper.getInstance().add(splashDbEntity);
    }

    // TODO 可以更新为update
    private void doUpdateEntity(SplashScreenInfo splashScreenInfo) {
        SplashDbEntity splashDbEntity = buildSplashDBEntity(splashScreenInfo);
        String where = String.format(Locale.getDefault(), QUERY_BY_SPLASH_ID, splashScreenInfo.getSID());

        DBHelper.getInstance().deleteByWhere(SplashDbEntity.class, where);
        DBHelper.getInstance().add(splashDbEntity);
    }

    private SplashDbEntity buildSplashDBEntity(SplashScreenInfo splashScreenInfo) {
        SplashDbEntity splashDbEntity = new SplashDbEntity();

        splashDbEntity.setSplashId(splashScreenInfo.getSID());
        splashDbEntity.setAttr(splashScreenInfo.getIAttr());
        splashDbEntity.setPriority(splashScreenInfo.getIPriority());
        splashDbEntity.setUrl(splashScreenInfo.getSUrl());
        splashDbEntity.setEffectiveStartTime(splashScreenInfo.getIEffectiveStartTime());
        splashDbEntity.setEffectiveEndTime(splashScreenInfo.getIEffectiveEndTime());
        splashDbEntity.setSkipUrl(splashScreenInfo.getSSkipUrl());
        splashDbEntity.setSkipState(splashScreenInfo.iSkip);
        splashDbEntity.setSkipSecond(splashScreenInfo.iStaySecond);

        return splashDbEntity;
    }
}
