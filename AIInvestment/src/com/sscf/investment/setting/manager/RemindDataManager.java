package com.sscf.investment.setting.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.dengtacj.component.entity.RemindedMessageItem;
import com.sscf.investment.sdk.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * davidwei
 *  管理提醒未读信息
 */
public final class RemindDataManager extends BroadcastReceiver {
    private HashSet<String> mUnreadMsgIds;

    private File mUnreadMsgIdsFile;

    private boolean mRegistered;

    /**
     * 单线程运行
     */
    public final Executor mExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100));

    public RemindDataManager() {
    }

    public void init() {
        registerAccountReceiver();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                getUnreadMsgIdFromLocal();
            }
        });
    }

    private void getUnreadMsgIdFromLocal() {
        final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
        if (accountInfo != null) {
            mUnreadMsgIdsFile = FileUtil.getUnreadRemindIdsFile(DengtaApplication.getApplication(), accountInfo.id);
            mUnreadMsgIds = (HashSet<String>) FileUtil.getObjectFromFile(mUnreadMsgIdsFile);
        }
    }

    public void destory() {
        unregisterAccountReceiver();
    }

    private void registerAccountReceiver() {
        if (!mRegistered) {
            final IntentFilter intentFilter = new IntentFilter(SettingConst.ACTION_LOGIN_SUCCESS);
            intentFilter.addAction(SettingConst.ACTION_LOGOUT_SUCCESS);
            final DengtaApplication app = DengtaApplication.getApplication();
            LocalBroadcastManager.getInstance(app).registerReceiver(this, intentFilter);
            mRegistered = true;
        }
    }

    private void unregisterAccountReceiver() {
        if (mRegistered) {
            final DengtaApplication app = DengtaApplication.getApplication();
            LocalBroadcastManager.getInstance(app).unregisterReceiver(this);
            mRegistered = false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (SettingConst.ACTION_LOGIN_SUCCESS.equals(action)) {
                    getUnreadMsgIdFromLocal();
                } else if (SettingConst.ACTION_LOGOUT_SUCCESS.equals(action)) {
                    mUnreadMsgIdsFile = null;
                    mUnreadMsgIds = null;
                }
            }
        });
    }

    public boolean isUnreadRemind(final String id) {
        return mUnreadMsgIds != null && mUnreadMsgIds.contains(id);
    }

    public void addUnreadRemind(final String id) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // 登录了才需要存储
                if (mUnreadMsgIdsFile == null) {
                    return;
                }
                if (mUnreadMsgIds == null) {
                    mUnreadMsgIds = new HashSet<String>();
                }
                mUnreadMsgIds.add(id);
                FileUtil.saveObjectToFile(mUnreadMsgIds, mUnreadMsgIdsFile);
            }
        });
    }

    public void removeUnreadRemind(final String id) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // 登录了才需要存储
                if (mUnreadMsgIdsFile == null) {
                    return;
                }
                if (mUnreadMsgIds == null) {
                    return;
                }
                mUnreadMsgIds.remove(id);
                FileUtil.saveObjectToFile(mUnreadMsgIds, mUnreadMsgIdsFile);
            }
        });
    }

    /**
     * 去除不在提醒列表的数据
     * @param items
     */
    public void removeInvalidUnreadRemind(final ArrayList<RemindedMessageItem> items) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // 登录了才需要存储
                if (mUnreadMsgIdsFile == null) {
                    return;
                }
                if (mUnreadMsgIds == null || mUnreadMsgIds.size() == 0) {
                    return;
                }
                String id = null;
                boolean exists = false;
                final HashSet<String> ids = new HashSet<String>();
                for (RemindedMessageItem item : items) {
                    id = item.id;
                    if (mUnreadMsgIds.contains(id)) {
                        ids.add(id);
                    }
                }
                mUnreadMsgIds = ids;
                FileUtil.saveObjectToFile(ids, mUnreadMsgIdsFile);
            }
        });
    }

    public void clearAllUnreadRemind() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (mUnreadMsgIdsFile != null && mUnreadMsgIdsFile.exists()) {
                    mUnreadMsgIdsFile.delete();
                }
                mUnreadMsgIds = null;
            }
        });
    }
}