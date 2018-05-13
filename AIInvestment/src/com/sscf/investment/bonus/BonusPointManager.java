package com.sscf.investment.bonus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.dengtacj.component.managers.IBonusPointManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.ProtoManager;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.socialize.ShareDialog;
import com.dengtacj.thoth.MapProtoLite;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import BEC.AccuPointDesc;
import BEC.AccuPointErrCode;
import BEC.AccuPointPriviInfo;
import BEC.AccuPointTaskItem;
import BEC.AccuPointTaskType;
import BEC.ConfigDetail;
import BEC.E_CONFIG_TYPE;
import BEC.GetConfigReq;
import BEC.GetConfigRsp;
import BEC.GetPointTaskListRsp;
import BEC.GetUserPointInfoRsp;
import BEC.PriviExchangeDesc;

/**
 * Created by yorkeehuang on 2016/12/19.
 */

public class BonusPointManager extends BroadcastReceiver implements DataSourceProxy.IRequestCallback, IBonusPointManager {

    private static final String TAG = BonusPointManager.class.getSimpleName();
    public static final String USER_POINT_CHANGE = "user_point_change";
    public static final String NOTIFY_EXTRA_INT_DATA = "notify_extra_int_data";
    public static final String TASK_LIST_CHANGE = "task_list_change";
    public static final String OPEN_POINT_PRIVI_RESULT = "open_point_privi_result";
    public static final String DESC_CHANGE = "desc_change";

    public static final int INVALID_INT_DATA = -1;

    public static final int OPEN_PRIVILEGE_FAILED = -1000;

    private static final int REFRESH_INTEVAL_TIME = 30 * 60 * 1000;

    private GetUserPointInfoRsp mGetUserPointInfoRsp;

    private Long mLastRefreshTaskSetTime = 0L;

    private String mDescText = null;

    private List<AccuPointTaskItem> mTaskList = new ArrayList<>();
    private Set<Integer> mFnishedTaskSet = new HashSet<>();

    public BonusPointManager() {
        final IntentFilter filter = new IntentFilter(ShareDialog.ACTION_SHARE_SUCCESS);
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).registerReceiver(this, filter);
    }

    public void requestBonusPointDescription() {
        DtLog.d(TAG, "requestBonusPointDescription()");
        final GetConfigReq req = new GetConfigReq();
        final ArrayList<Integer> types = new ArrayList<Integer>(1);
        types.add(E_CONFIG_TYPE.E_CONFIG_ACCU_POINT_DESC);
        req.vType = types;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_CONFIG_ACCU_POINT_DESC, req, this);
    }

    public void clear() {
        mTaskList.clear();
        mFnishedTaskSet.clear();
        mGetUserPointInfoRsp = null;
    }

    public void requestUserPointInfo() {
        if(!BonusPointRequestManager.requestUserPointInfo(this)) {
            mGetUserPointInfoRsp = null;
            notifyDataChange(USER_POINT_CHANGE);
        }
    }

    public void requestTasks() {
        BonusPointRequestManager.requestTasks(this);
    }

    public void reportFinishedTask(int taskType) {
        DtLog.d(TAG, "reportFinishedTask() taskType = " + taskType);
        synchronized (mLastRefreshTaskSetTime) {
            long now = System.currentTimeMillis();
            DtLog.d(TAG, "reportFinishedTask() mLastRefreshTaskSetTime = " + mLastRefreshTaskSetTime + ", now = " + now);
            if(now - mLastRefreshTaskSetTime >= REFRESH_INTEVAL_TIME) {
                mFnishedTaskSet.clear();
                mLastRefreshTaskSetTime = now;
            }
        }

        if(mFnishedTaskSet.contains(taskType)) {
            DtLog.d(TAG, "reportFinishedTask() mFnishedTaskSet contains " + taskType);
            return;
        }

        BonusPointRequestManager.requestReportFinishedTask(taskType, this);
    }

    public void openPointPrivilege(List<PriviExchangeDesc> openPrivilegeList) {
        if(openPrivilegeList == null || openPrivilegeList.isEmpty()) {
            return;
        }
        BonusPointRequestManager.requestOpenPointPrivilege(openPrivilegeList, this);
    }

    public void requestExChangePriviList() {
        BonusPointRequestManager.requestExChangePriviList(this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
                switch (data.getEntityType()) {
                    case EntityObject.ET_GET_USER_POINT_INFO:
                        handleGetUserPointInfo(data, true);
                        break;
                    case EntityObject.ET_GET_POINT_TASK_LIST:
                        handleGetPointTaskList(data, true);
                        break;
                    case EntityObject.ET_REPORT_TASK_FINISHED:
                        handleReportTaskFinished((MapProtoLite)data.getEntity(), (String) data.getExtra());
                        break;
                    case EntityObject.ET_OPEN_POINT_PRIVILEGE:
                        handleOpenPointPrivilege((MapProtoLite)data.getEntity(), true);
                        break;
                    case EntityObject.ET_CONFIG_ACCU_POINT_DESC:
                        handleBonusPointsDesc(data);
                        break;
                    default:
                }
        } else {
            switch (data.getEntityType()) {
                case EntityObject.ET_OPEN_POINT_PRIVILEGE:
                    handleOpenPointPrivilege((MapProtoLite)data.getEntity(), false);
                    break;
                case EntityObject.ET_GET_USER_POINT_INFO:
                    handleGetUserPointInfo(data, false);;
                    break;
                case EntityObject.ET_GET_POINT_TASK_LIST:
                    handleGetPointTaskList(data, false);
                    break;
                default:
            }
        }
    }

    private void handleGetUserPointInfo(EntityObject data, boolean success) {
        if(success) {
            mGetUserPointInfoRsp = (GetUserPointInfoRsp) data.getEntity();
        }
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(new Intent(USER_POINT_CHANGE));
    }

    private void handleGetPointTaskList(EntityObject data, boolean success) {
        if(success) {
            GetPointTaskListRsp rsp = (GetPointTaskListRsp) data.getEntity();
            if(rsp.getVTask() != null && !rsp.getVTask().isEmpty()) {
                ArrayList<AccuPointTaskItem> tasks = rsp.getVTask();
                mTaskList.clear();
                for(AccuPointTaskItem task : tasks) {
                    mTaskList.add(task);
                }

                ArrayList<Integer> finisheds = rsp.getVFinished();
                synchronized (mLastRefreshTaskSetTime) {
                    mLastRefreshTaskSetTime = System.currentTimeMillis();
                    mFnishedTaskSet.clear();
                    for(int finished : finisheds) {
                        mFnishedTaskSet.add(finished);
                    }
                }
            }
        }
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication())
                .sendBroadcast(new Intent(TASK_LIST_CHANGE));
    }

    private void handleReportTaskFinished(MapProtoLite packet, String extra) {
        int retCode = packet.read("", -1);
        DtLog.d(TAG, "handleReportTaskFinished retCode=" + retCode);
        int taskType = -1;
        if(!TextUtils.isEmpty(extra)) {
            try {
                taskType = Integer.valueOf(extra);
                DtLog.d(TAG, "callback() ET_REPORT_TASK_FINISHED: taskType = " + taskType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        switch (retCode) {
            case AccuPointErrCode.E_ACCU_POINT_SUCC:
                break;
            case AccuPointErrCode.E_ACCU_POINT_TASK_LIMIT:
                if(taskType >= 0) {
                    DtLog.d(TAG, "handleReportTaskFinished() cache taskType = " + taskType);
                    mFnishedTaskSet.add(taskType);
                }
                break;
        }
    }

    private void handleOpenPointPrivilege(MapProtoLite packet, boolean success) {
        int retCode;
        if(success) {
            retCode = packet.read("", -1);
        } else {
            retCode = OPEN_PRIVILEGE_FAILED;
        }
        DtLog.d(TAG, "handleOpenPointPrivilege retCode=" + retCode);
        Intent intent = new Intent(OPEN_POINT_PRIVI_RESULT);
        intent.putExtra(NOTIFY_EXTRA_INT_DATA, retCode);
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(intent);
    }

    private void handleBonusPointsDesc(EntityObject data) {
        GetConfigRsp rsp = (GetConfigRsp) data.getEntity();
        ArrayList<ConfigDetail> cfgDetails = rsp.getVList();
        for (ConfigDetail config : cfgDetails) {
            if (config.iType == E_CONFIG_TYPE.E_CONFIG_ACCU_POINT_DESC) {
                AccuPointDesc accuPointDesc = new AccuPointDesc();
                if (ProtoManager.decode(accuPointDesc, config.vData)) {
                    if(!TextUtils.isEmpty(accuPointDesc.getSDesc())) {
                        mDescText = accuPointDesc.getSDesc();
                        String storedDescText = SettingPref.getString(SettingConst.KEY_SETTING_BONUS_DESC, "");
                        DtLog.d(TAG, "handleBonusPointsDesc storedDescText=" + storedDescText + ", accuPointDesc.getSDesc() = " + accuPointDesc.getSDesc());
                        if(!TextUtils.equals(mDescText, storedDescText)) {
                            DengtaApplication.getApplication().getRedDotManager().setNeverEnterBonus(true);
                            notifyDataChange(DESC_CHANGE);
                        }
                    } else {
                         DengtaApplication.getApplication().getRedDotManager().setNeverEnterBonus(false);
                    }
                }
                break;
            }
        }
    }

    /**
     * 获取总积分
     * @return 总积分
     */
    public int getAccuPoints() {
        if(mGetUserPointInfoRsp != null) {
            return mGetUserPointInfoRsp.getIAccuPoints();
        }
        return  INVALID_INT_DATA;
    }

    public Map<Integer, Integer> getPriviPointsMap() {
        if(mGetUserPointInfoRsp != null) {
            return mGetUserPointInfoRsp.getMPriviPoints();
        }
        return null;
    }

    /**
     * 获取今日已领积分
     * @return 今日已领积分
     */
    public int getPointsDaily() {
        if(mGetUserPointInfoRsp != null) {
            return mGetUserPointInfoRsp.getIGetPointsDaily();
        }
        return INVALID_INT_DATA;
    }

    /**
     * 获取今天可做任务
     * @return 今天可做任务
     */
    public int getLeftTaskNum() {
        if(mGetUserPointInfoRsp != null) {
            return mGetUserPointInfoRsp.getILeftTaskNum();
        }
        return INVALID_INT_DATA;
    }

    public boolean isPrivilegeOn(int type) {
        if(mGetUserPointInfoRsp != null) {
            ArrayList<AccuPointPriviInfo> priviInfos = mGetUserPointInfoRsp.getVPrivi();
            if(priviInfos != null && !priviInfos.isEmpty()) {
                for(AccuPointPriviInfo info : priviInfos) {
                    if(info.getIPriviType() == type) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<AccuPointTaskItem> getTaskList() {
        return mTaskList;
    }

    public boolean isTaskFinished(int taskType) {
        if(mFnishedTaskSet != null && !mFnishedTaskSet.isEmpty()) {
            return mFnishedTaskSet.contains(taskType);
        }
        return false;
    }

    public String getDescText() {
        return mDescText;
    }

    private void notifyDataChange(String action) {
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(new Intent(action));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (ShareDialog.ACTION_SHARE_SUCCESS.equals(action)) {
            reportFinishedTask(AccuPointTaskType.E_ACCU_POINT_TASK_SHARE);
        }
    }
}
