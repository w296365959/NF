package com.sscf.investment.teacherYan.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.teacherYan.TeacherYanCourseFragment;
import com.sscf.investment.teacherYan.manager.TeacherYanCoureseRequestManager;

import java.util.ArrayList;

import BEC.WxWalkRecord;
import BEC.WxWalkRecordListRsp;

/**
 * Created by LEN on 2018/4/23.
 */

public class TeacherYanCoursePresent implements DataSourceProxy.IRequestCallback, Handler.Callback{

    private static final String TAG = TeacherYanCoursePresent.class.getSimpleName();

    private static final int MSG_UPDATE_LIST = 1;
    private static final int MSG_UPDATE_LIST_FAILED = 2;
    private static final int MSG_UPDATE_LIST_MORE = 4;
    private static final int MSG_UPDATE_LIST_NO_MORE = 5;
    private static final int MSG_UPDATE_LIST_MORE_FAILED = 6;

    private TeacherYanCourseFragment mFragment;
    private Handler mHandler;
    private ArrayList<WxWalkRecord> mVideoList;

    private int mLastID; // 分页使用
    private int totalCount;

    public TeacherYanCoursePresent(TeacherYanCourseFragment mFragment) {
        this.mFragment = mFragment;
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public void requestListData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())){
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            return;
        }
        TeacherYanCoureseRequestManager.requestTeacherYanCurseList(this);
    }

    public void requestMoreData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_MORE_FAILED);
            return;
        }
        TeacherYanCoureseRequestManager.requestTeacherYanWordListMore(mLastID, this);
    }

    public int getTotalCount() {
        return totalCount;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_LIST:
                final ArrayList<WxWalkRecord> records = (ArrayList<WxWalkRecord>) msg.obj;
                final int size = records == null ? 0 : records.size();
                if (size > 0) {
                    mVideoList = records;
                    mFragment.updateList(mVideoList);
                } else {
                    mFragment.showEmptyLayout();
                }
                break;
            case MSG_UPDATE_LIST_FAILED:
                final ArrayList<WxWalkRecord> list = mVideoList;
                if (list != null && list.size() > 0) {
                    DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                } else {
                    mFragment.showRetryLayout();
                }
                break;
            case MSG_UPDATE_LIST_MORE:
                mVideoList.addAll((ArrayList<WxWalkRecord>) msg.obj);
                mFragment.updateList(mVideoList);
                if (mVideoList.size() == totalCount) {
                    mHandler.obtainMessage(MSG_UPDATE_LIST_NO_MORE).sendToTarget();
                }
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
            case EntityObject.ET_TEACHER_YAN_CURSE:
                getCurseCallbak(success, data);
                break;
            default:
                break;
        }
    }

    private void getCurseCallbak(boolean success, EntityObject data) {
        final String endTime = (String) data.getExtra();
        if (TextUtils.isEmpty(endTime)) { // 首页数据
            if (success) {
                final WxWalkRecordListRsp rsp = (WxWalkRecordListRsp) data.getEntity();
                totalCount = rsp.iTotalCount;
                final ArrayList<WxWalkRecord> videos = rsp.vtWxWalkRecord;
                final int size = videos == null ? 0 : videos.size();
                if (size > 0) {
                    mLastID = videos.get(size - 1).getIID();
                }
                mHandler.obtainMessage(MSG_UPDATE_LIST, videos).sendToTarget();
                if (videos.size() == totalCount){
                    mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                }
                DtLog.d(TAG, "getCurseCallbak size : " + size);
            } else {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            }
        } else { // 分页数据
            if (success) {
                if (endTime.equals(String.valueOf(mLastID))) { // 过滤重复数据
                    final WxWalkRecordListRsp rsp = (WxWalkRecordListRsp) data.getEntity();
                    totalCount = rsp.iTotalCount;
                    final ArrayList<WxWalkRecord> videos = rsp.vtWxWalkRecord;
                    final int size = videos == null ? 0 : videos.size();
                    if (size > 0) {
                        mLastID = videos.get(size - 1).getIID();
                        mHandler.obtainMessage(MSG_UPDATE_LIST_MORE, videos).sendToTarget();
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
