package com.sscf.investment.setting.manager;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.sscf.investment.common.entity.SecDetailPageViewEntity;
import com.sscf.investment.db.DBHelper;
import com.sscf.investment.main.DataPref;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.DeviceUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import BEC.BEACON_STAT_TYPE;
import BEC.BeaconStat;
import BEC.BeaconStatData;
import BEC.ReportLogReq;

/**
 * davidwei
 *
 */
public final class LogRequestManager {
    private static final String TAG = LogRequestManager.class.getSimpleName();

    private static final int REPORT_INSTALLED_APPS_DURATION = 7 * 24 * 3600; //一周的秒数
    private static final int REPORT_SEC_DETAIL_PAGE_VIEW_DURATION = 24 * 3600; //一天的秒数

    public static void reportLogRequest(final byte[] data, final String type, final String extension, final DataSourceProxy.IRequestCallback callback) {
        final ReportLogReq req = new ReportLogReq();
        req.vData = data;
        req.sBussName = type;
        req.sFileSuffix = extension;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_LOG_REPORT, req, callback);
    }

    public static void reportStatisticRequest(int eStat, String data, final DataSourceProxy.IRequestCallback callback) {
        final BeaconStat req = new BeaconStat();

        final ArrayList<BeaconStatData> beaconStatDatas = new ArrayList<>(1);
        final BeaconStatData beaconStatData = new BeaconStatData();
        beaconStatData.iTime = System.currentTimeMillis() / 1000;
        beaconStatData.eType = eStat;
        beaconStatData.sData = data;
        beaconStatDatas.add(beaconStatData);
        req.vBeaconStatData = beaconStatDatas;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_STAT_REPORT, req, callback);
    }

    public static void reportInstalledAppsToServer() {
        try {
            final int lastReportInstalledAppsTime = DataPref.getLastReportInstalledAppsTime();
            TradingStateManager tradingStateManager = DengtaApplication.getApplication().getTradingStateManager();
            int serverTime = tradingStateManager.getServerTime();
            if (serverTime - lastReportInstalledAppsTime < REPORT_INSTALLED_APPS_DURATION) {
                return;
            }

            DataPref.setLastReportInstalledAppsTime(serverTime);

            PackageManager manager = DengtaApplication.getApplication().getPackageManager();
            List<PackageInfo> installedAppInfos = manager.getInstalledPackages(0);

            if (installedAppInfos != null) {
                DtLog.d(TAG, "init: installedAppInfos.size() = " + installedAppInfos.size());
                int count = 0;
                List<PackageInfo> apps = new ArrayList<>();
                for (PackageInfo installedAppInfo : installedAppInfos) {
                    boolean systemApp = DeviceUtil.isSystemApp(installedAppInfo);
                    if (!systemApp) {
                        count++;
                        apps.add(installedAppInfo);
                    }
                }
                DtLog.d(TAG, "init: installedAppInfos app count = " + count);

                if (apps == null || apps.size() == 0) {
                    return;
                }
                DtLog.d(TAG, "reportInstalledAppsToServer size =  " + apps.size());

                StringBuilder packageNames = new StringBuilder();
                for (int i = 0; i < apps.size(); i++) {
                    PackageInfo packageInfo = apps.get(i);
                    packageNames.append(packageInfo.packageName);
                    if (i != apps.size() - 1) {
                        packageNames.append(":");
                    }
                }

                reportStatisticRequest(BEACON_STAT_TYPE.E_BST_APP_INSTALL, packageNames.toString(), new DataSourceProxy.IRequestCallback() {
                    @Override
                    public void callback(boolean success, EntityObject data) {
                        DtLog.d(TAG, "reportInstalledAppsToServer callback success = " + success);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reportSecDetailPageViewToServer() {
        try {
            final int lastReportSecDetailPageViewTime = DataPref.getLastReportSecDetailPVTime();
            TradingStateManager tradingStateManager = DengtaApplication.getApplication().getTradingStateManager();
            int serverTime = tradingStateManager.getServerTime();
            if (serverTime - lastReportSecDetailPageViewTime < REPORT_SEC_DETAIL_PAGE_VIEW_DURATION) {
                return;
            }

            DataPref.setLastReportSecDetailPVTime(serverTime);
            final List<SecDetailPageViewEntity> pvs = DBHelper.getInstance().findAll(SecDetailPageViewEntity.class);

            final int size = pvs == null ? 0 : pvs.size();
            if (size == 0) {
                return;
            }

            DtLog.d(TAG, "reportSecDetailPageViewToServer size =  " + size);

            final StringBuilder pageViews = new StringBuilder();
            final Iterator<SecDetailPageViewEntity> it = pvs.iterator();
            SecDetailPageViewEntity entity = null;
            while (it.hasNext()) {
                entity = it.next();
                pageViews.append(entity.getDtSecCode());
                pageViews.append(':');
                pageViews.append(entity.getPv());
                pageViews.append('$');
            }
            final int length = pageViews.length();
            if (length > 0) {
                pageViews.deleteCharAt(length - 1);
                DtLog.d(TAG, "reportSecDetailPageViewToServer: pageViews = " + pageViews);

                reportStatisticRequest(BEACON_STAT_TYPE.E_BST_SEC_ACCESS, pageViews.toString(), new DataSourceProxy.IRequestCallback() {
                    @Override
                    public void callback(boolean success, EntityObject data) {
                        DtLog.d(TAG, "reportSecDetailPageViewToServer callback success = " + success);
                        if (success) {
                            DBHelper.getInstance().deleteAll(SecDetailPageViewEntity.class);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
