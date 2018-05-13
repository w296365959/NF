package com.sscf.investment.teacherYan.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.sscf.investment.R;
import com.sscf.investment.information.HotSpotFragment;
import com.sscf.investment.information.manager.HotSpotRequestManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.teacherYan.TeacherYanWordFragment;
import com.sscf.investment.teacherYan.manager.TeacherYanWordRequestManager;
import com.sscf.investment.utils.PeriodicHandlerManager;

import java.util.ArrayList;

import BEC.E_MARKET_TYPE;
import BEC.SecSimpleQuote;
import BEC.TopicListItem;
import BEC.TopicListRsp;
import BEC.WxWalkRecord;
import BEC.WxWalkRecordListRsp;
import BEC.WxWalkRecordReq;

/**
 * Created by LEN on 2018/4/19.
 */

public class TeacherYanWordPresent implements DataSourceProxy.IRequestCallback, Handler.Callback{

    private static final String TAG = TeacherYanWordPresent.class.getSimpleName();

    private static final int MSG_UPDATE_LIST = 1;
    private static final int MSG_UPDATE_LIST_FAILED = 2;
    private static final int MSG_UPDATE_LIST_MORE = 4;
    private static final int MSG_UPDATE_LIST_NO_MORE = 5;
    private static final int MSG_UPDATE_LIST_MORE_FAILED = 6;

    private TeacherYanWordFragment mFragment;
    private final Handler mHandler;
    private TeacherYanWordRequestManager mManager;
    private ArrayList<WxWalkRecord> mRecordList;

    private int mLastID; // 分页使用
    private int mTotalCount;

    public TeacherYanWordPresent(final TeacherYanWordFragment fragment) {
        mFragment = fragment;
        mHandler = new Handler(Looper.getMainLooper(), this);
        mManager = new TeacherYanWordRequestManager();
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public void requestListData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            return;
        }

        TeacherYanWordRequestManager.requestTeacherYanWordList(this);
    }

    public void requestListMoreData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_MORE_FAILED);
            return;
        }
        TeacherYanWordRequestManager.requestTeacherYanWordListMore(mLastID,this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_LIST:
                final ArrayList<WxWalkRecord> records = (ArrayList<WxWalkRecord>) msg.obj;
                final int size = records == null ? 0 : records.size();
                if (size > 0) {
                    mRecordList = records;
                    mFragment.updateList(mRecordList);
                } else {
                    mFragment.showEmptyLayout();
                }
                break;
            case MSG_UPDATE_LIST_FAILED:
                final ArrayList<WxWalkRecord> list = mRecordList;
                if (list != null && list.size() > 0) {
                    DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                } else {
                    mFragment.showRetryLayout();
                }
                break;
            case MSG_UPDATE_LIST_MORE:
                mRecordList.addAll((ArrayList<WxWalkRecord>) msg.obj);
                if (mRecordList.size() == mTotalCount) {
                    mHandler.obtainMessage(MSG_UPDATE_LIST_NO_MORE).sendToTarget();
                }
                mFragment.updateList(mRecordList);
                break;
            case MSG_UPDATE_LIST_NO_MORE:
                mFragment.showFooterNoMoreLayout();
                break;
            case MSG_UPDATE_LIST_MORE_FAILED:
                mFragment.showFooterRetryLayout();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_TEACHER_YAN_WORD:
                getWorkRecordCallbak(success, data);
                break;
            default:
                break;
        }
    }

    private void getWorkRecordCallbak(boolean success, EntityObject data) {
        final String endTime = (String) data.getExtra();
        if (TextUtils.isEmpty(endTime)) { // 首页数据
            if (success) {
                final WxWalkRecordListRsp rsp = (WxWalkRecordListRsp) data.getEntity();
                final ArrayList<WxWalkRecord> walkRecords = rsp.vtWxWalkRecord;
                mTotalCount = rsp.iTotalCount;
                final int size = walkRecords == null ? 0 : walkRecords.size();
                if (size > 0) {
                    mLastID = walkRecords.get(size - 1).getIID();
                }
                mHandler.obtainMessage(MSG_UPDATE_LIST, walkRecords).sendToTarget();
                if (walkRecords.size() == mTotalCount){
                    mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                }
                DtLog.d(TAG, "getWorkRecordCallbak size : " + size);
            } else {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            }
        } else { // 分页数据
            if (success) {
                if (endTime.equals(String.valueOf(mLastID))) { // 过滤重复数据
                    final WxWalkRecordListRsp rsp = (WxWalkRecordListRsp) data.getEntity();
                    mTotalCount = rsp.iTotalCount;
                    final ArrayList<WxWalkRecord> walkRecords = rsp.vtWxWalkRecord;
                    final int size = walkRecords == null ? 0 : walkRecords.size();
                    if (size > 0) {
                        mLastID = walkRecords.get(size - 1).getIID();
                        mHandler.obtainMessage(MSG_UPDATE_LIST_MORE, walkRecords).sendToTarget();
                    }
                    if (size == 0) {
                        mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                    }
                }
            } else {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_MORE_FAILED);
            }
        }
    }
}
