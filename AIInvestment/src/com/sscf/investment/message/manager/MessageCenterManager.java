package com.sscf.investment.message.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import BEC.AlertMsgClassDesc;
import BEC.AlertMsgClassListRsp;

/**
 * davidwei
 * 消息中心，管理消息分类的数据
 */
public final class MessageCenterManager extends BroadcastReceiver implements DataSourceProxy.IRequestCallback, Handler.Callback {
    private HashMap<Integer, Integer> mEndTimeMap;
    private HashMap<Integer, Integer> mUnreadMessageCountMap;
    private ArrayList<AlertMsgClassDesc> mMessageClassList;

    private final File mEndTimeFile;

    private long mAccountId = -1;

    private OnGetMessageClassListListener mOnGetMessageClassListListener;

    private final Handler mHandler;

    public MessageCenterManager() {
        mEndTimeFile = FileUtil.getMessageEndTimeFile(DengtaApplication.getApplication());
        mUnreadMessageCountMap = new HashMap<Integer, Integer>();
        mHandler = new Handler(Looper.getMainLooper(), this);

        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).registerReceiver(
                this, new IntentFilter(DengtaConst.ACTION_MESSAGE_TOTAL_UNREAD_COUNT_CHANGED));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (DengtaConst.ACTION_MESSAGE_TOTAL_UNREAD_COUNT_CHANGED.equals(action)) {
            DengtaApplication.getApplication().getRedDotManager().setMessageCenterState(getTotalUnreadMessageCount() > 0);
        }
    }

    public void initAsync() {
        mEndTimeMap = (HashMap<Integer, Integer>) FileUtil.getObjectFromFile(mEndTimeFile);
        requestMessageCenter();
    }

    public void requestMessageCenter() {
        final long accountId = DengtaApplication.getApplication().getAccountManager().getAccountId();
        if (accountId != mAccountId) {
            mAccountId = accountId;
            loadDataFromLocal();
        } else if (mMessageClassList == null) {
            loadDataFromLocal();
        }
        MessageRequestManager.getMsgClassListRequest(mEndTimeMap, this);
    }

    public void reset() {
        mEndTimeMap = new HashMap<Integer, Integer>();
        mUnreadMessageCountMap = new HashMap<Integer, Integer>();
    }

    public HashMap<Integer, Integer> getEndTimeMap() {
        if (mEndTimeMap == null) {
            mEndTimeMap = new HashMap<Integer, Integer>();
        }
        return mEndTimeMap;
    }

    public void updateEndTime(final int classId, final int pushTime) {
        final HashMap<Integer, Integer> map = getEndTimeMap();
        final Integer time = map.get(classId);
        if (time == null || time.intValue() != pushTime) {
            map.put(classId, pushTime);
            DengtaApplication.getApplication().defaultExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    FileUtil.saveObjectToFile(map, mEndTimeFile);
                }
            });
        }
    }

    public ArrayList<AlertMsgClassDesc> getMessageClassList() {
        return mMessageClassList;
    }

    public synchronized void updateMessageClassList(ArrayList<AlertMsgClassDesc> classList) {
        mMessageClassList = classList;
        updateUnreadMessageCount(classList);
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(
                new Intent(DengtaConst.ACTION_MESSAGE_TOTAL_UNREAD_COUNT_CHANGED));
    }

    private void updateUnreadMessageCount(ArrayList<AlertMsgClassDesc> classList) {
        if (classList != null) {
            for (AlertMsgClassDesc messageClassItem : classList) {
                setUnreadMessageCount(messageClassItem.iClassID, messageClassItem.iNewsPushCount);
            }
        }
    }

    public int getTotalUnreadMessageCount() {
        int totalCount = 0;
        final HashMap<Integer, Integer> map = mUnreadMessageCountMap;
        for (Integer classId : map.keySet()) {
            final Integer count = map.get(classId);
            totalCount += (count == null ? 0 : count);
        }
        return totalCount;
    }

    public int getUnreadMessageCount(final int classId) {
        final Integer count = mUnreadMessageCountMap.get(classId);
        return count == null ? 0 : count;
    }

    public void setUnreadMessageCount(final int classId, final int count) {
        mUnreadMessageCountMap.put(classId, count);
    }

    public void clearUnreadMessageCount(final int classId) {
        setUnreadMessageCount(classId, 0);
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(
                new Intent(DengtaConst.ACTION_MESSAGE_TOTAL_UNREAD_COUNT_CHANGED));
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success) {
            final AlertMsgClassListRsp rsp = (AlertMsgClassListRsp) data.getEntity();
            final ArrayList<AlertMsgClassDesc> classList = rsp.vAlertMsgClassDesc;
            updateMessageClassList(classList);
            if (mOnGetMessageClassListListener != null) {
                mHandler.obtainMessage(0, classList).sendToTarget();
            }
            saveDataToLocal(classList);
        }
    }

    public void saveDataToLocal(final ArrayList<AlertMsgClassDesc> classList) {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        final long accountId = dengtaApplication.getAccountManager().getAccountId();
        final File file = FileUtil.getMessageCenterDataFile(dengtaApplication, accountId);
        FileUtil.saveObjectToFile(classList, file);
    }

    private void loadDataFromLocal() {
        DengtaApplication.getApplication().defaultExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
                final long accountId = dengtaApplication.getAccountManager().getAccountId();
                final File file = FileUtil.getMessageCenterDataFile(dengtaApplication, accountId);
                final ArrayList<AlertMsgClassDesc> classList = (ArrayList<AlertMsgClassDesc>) FileUtil.getObjectFromFile(file);
                if (classList != null) {
                    updateMessageClassList(classList);
                }
            }
        });
    }

    public void setOnGetMessageClassListListener(OnGetMessageClassListListener l) {
        mOnGetMessageClassListListener = l;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (mOnGetMessageClassListListener != null) {
            mOnGetMessageClassListListener.onGetMessageClassList((ArrayList<AlertMsgClassDesc>) msg.obj);
        }
        return true;
    }

    public interface OnGetMessageClassListListener {
        void onGetMessageClassList(final ArrayList<AlertMsgClassDesc> classList);
    }
}
