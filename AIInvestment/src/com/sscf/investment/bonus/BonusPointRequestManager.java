package com.sscf.investment.bonus;

import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.entity.AccountInfoEntity;

import java.util.ArrayList;
import java.util.List;

import BEC.AccountTicket;
import BEC.GetExChangePriviListReq;
import BEC.GetPointTaskListReq;
import BEC.GetUserPointInfoReq;
import BEC.OpenPointPriviItem;
import BEC.OpenPointPriviReq;
import BEC.PriviExchangeDesc;
import BEC.ReportFinishTaskReq;
import BEC.UserInfo;

/**
 * Created by yorkeehuang on 2017/8/31.
 */

public class BonusPointRequestManager {

    private static final String TAG = BonusPointRequestManager.class.getSimpleName();

    public static boolean requestUserPointInfo(DataSourceProxy.IRequestCallback callback) {
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        UserInfo userInfo = accountManager.getUserInfo();
        AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        AccountTicket accountTicket = null;
        if(userInfo != null && accountInfoEntity != null && accountInfoEntity.ticket != null) {
            accountTicket = new AccountTicket();
            accountTicket.setVtTicket(accountInfoEntity.ticket);
            GetUserPointInfoReq req = new GetUserPointInfoReq(userInfo, accountTicket);

            DtLog.d(TAG, "DataEngine.getInstance().requestBriefInfo(EntityObject.ET_GET_USER_POINT_INFO, req, this);");
            DataEngine.getInstance().request(EntityObject.ET_GET_USER_POINT_INFO, req, callback);
            return true;
        }
        return false;
    }

    public static boolean requestTasks(DataSourceProxy.IRequestCallback callback) {
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        UserInfo userInfo = accountManager.getUserInfo();
        AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        AccountTicket accountTicket = null;
        if(userInfo != null && accountInfoEntity != null && accountInfoEntity.ticket != null) {
            accountTicket = new AccountTicket();
            accountTicket.setVtTicket(accountInfoEntity.ticket);
        }
        GetPointTaskListReq req = new GetPointTaskListReq(userInfo, accountTicket);
        return DataEngine.getInstance().request(EntityObject.ET_GET_POINT_TASK_LIST, req, callback);
    }

    public static boolean requestReportFinishedTask(int taskType, DataSourceProxy.IRequestCallback callback) {
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        UserInfo userInfo = accountManager.getUserInfo();
        AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        if(userInfo != null && accountInfoEntity != null && accountInfoEntity.ticket != null) {
            AccountTicket accountTicket = new AccountTicket();
            accountTicket.setVtTicket(accountInfoEntity.ticket);
            ReportFinishTaskReq req = new ReportFinishTaskReq(userInfo, accountTicket, taskType);
            DtLog.d(TAG, "DataEngine.getInstance().requestBriefInfo(EntityObject.ET_REPORT_TASK_FINISHED, req, this); taskType = " + taskType);
            return DataEngine.getInstance().request(EntityObject.ET_REPORT_TASK_FINISHED, req, callback, String.valueOf(taskType));
        }

        DtLog.d(TAG, "requestReportFinishedTask() has no userInfo or ticket(taskType = " + taskType + ")");
        return false;
    }

    public static boolean requestOpenPointPrivilege(List<PriviExchangeDesc> openList, DataSourceProxy.IRequestCallback callback) {
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        UserInfo userInfo = accountManager.getUserInfo();
        AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        if(userInfo != null && accountInfoEntity != null && accountInfoEntity.ticket != null) {
            AccountTicket accountTicket = new AccountTicket();
            accountTicket.setVtTicket(accountInfoEntity.ticket);
            OpenPointPriviReq req = new OpenPointPriviReq();
            req.setStUserInfo(userInfo);
            req.setStAccountTicket(accountTicket);
            ArrayList<OpenPointPriviItem> items = new ArrayList<>(openList.size());
            for(PriviExchangeDesc openDesc : openList) {
                OpenPointPriviItem item = new OpenPointPriviItem(openDesc.getIPriviType(), openDesc.getIExchangeDays());
                items.add(item);
            }
            req.setVItem(items);
            DtLog.d(TAG, "DataEngine.getInstance().requestBriefInfo(EntityObject.ET_OPEN_POINT_PRIVILEGE, req, this);");
            return DataEngine.getInstance().request(EntityObject.ET_OPEN_POINT_PRIVILEGE, req, callback);
        }

        DtLog.d(TAG, "requestOpenPointPrivilege() has no userInfo or ticket");
        return false;
    }

    public static boolean requestExChangePriviList(DataSourceProxy.IRequestCallback callback) {
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        UserInfo userInfo = accountManager.getUserInfo();
        AccountInfoEntity accountInfoEntity = accountManager.getAccountInfo();
        if(userInfo != null && accountInfoEntity != null && accountInfoEntity.ticket != null) {
            GetExChangePriviListReq req = new GetExChangePriviListReq();
            req.setStUserInfo(userInfo);
            return DataEngine.getInstance().request(EntityObject.ET_GET_EXCHANGE_PRIVILEGE_LIST, req, callback);
        }

        DtLog.d(TAG, "requestExChangePriviList() has no userInfo or ticket");
        return false;
    }
}
