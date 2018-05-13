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
import com.sscf.investment.teacherYan.TeacherVideoFragment;
import com.sscf.investment.teacherYan.manager.TeacherVideoRequestManager;

import java.util.ArrayList;

import BEC.InformationSpiderNews;
import BEC.VideoInfo;
import BEC.VideoInfoListRsp;

/**
 * Created by LEN on 2018/4/24.
 */

public class TeacherVideoPresent implements Handler.Callback, DataSourceProxy.IRequestCallback {

    private static final String TAG = TeacherVideoPresent.class.getSimpleName();

    private static final int MSG_UPDATE_LIST = 1;
    private static final int MSG_UPDATE_LIST_FAILED = 2;
    private static final int MSG_UPDATE_LIST_MORE = 3;
    private static final int MSG_UPDATE_LIST_NO_MORE =4;
    private static final int MSG_UPDATE_LIST_MORE_FAILED = 5;

    private final Handler mHandler;

    private ArrayList<VideoInfo> mVideoList;
    private int mLastID;
    private TeacherVideoFragment mFragment;
    private int mTotalCount;

    public TeacherVideoPresent(TeacherVideoFragment mFragment) {
        this.mFragment = mFragment;
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public void requestData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            return;
        }

        TeacherVideoRequestManager.requestTeacherVideoList(this);
    }

    public void requestMoreData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_MORE_FAILED);
            return;
        }
        TeacherVideoRequestManager.requestTeacherVideoListMore(mLastID, this);
    }

    public void onGetVideoCallback(boolean success, EntityObject data) {
        final String endId = (String) data.getExtra();
        if (TextUtils.isEmpty(endId)) { // 首页数据
            if (success) {
                final VideoInfoListRsp rsp = (VideoInfoListRsp) data.getEntity();
                mTotalCount = rsp.getITotalCount();
                final ArrayList<VideoInfo> videoInfos = rsp.getVtVideoInfo();
                final int size = videoInfos == null ? 0 : videoInfos.size();
                if (size > 0) {
                    mLastID = videoInfos.get(size - 1).getIID();
                }
                mHandler.obtainMessage(MSG_UPDATE_LIST, videoInfos).sendToTarget();
                if (rsp.getITotalCount() == videoInfos.size()) {
                    mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                } else if (size > 0) {
                    mFragment.showFooterNormalLayout();
                }
            } else {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            }
        } else { // 分页数据
            if (success) {
                if (endId.equals(String.valueOf(mLastID))) { // 过滤重复数据
                    final VideoInfoListRsp rsp = (VideoInfoListRsp) data.getEntity();
                    mTotalCount = rsp.getITotalCount();
                    final ArrayList<VideoInfo> videoInfos = rsp.getVtVideoInfo();
                    final int size = videoInfos == null ? 0 : videoInfos.size();
                    if (size > 0) {
                        mLastID = videoInfos.get(size - 1).getIID();
                        mHandler.obtainMessage(MSG_UPDATE_LIST_MORE, videoInfos).sendToTarget();
                    }

                    DtLog.d(TAG, "requestTeacherVideoListMore size : " + size);
                }
            } else {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_MORE_FAILED);
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        DtLog.d(TAG, "handleMessage msg.what : " + msg.what);
        switch (msg.what) {
            case MSG_UPDATE_LIST:
                mFragment.refreshComplete();
                final ArrayList<VideoInfo> videoInfos = (ArrayList<VideoInfo>) msg.obj;
                final int size = videoInfos.size();
                if (size > 0) {
                    mVideoList = videoInfos;
                    mFragment.updateList(mVideoList);
                } else {
                    mFragment.showEmptyLayout();
                }
                mHandler.removeMessages(MSG_UPDATE_LIST); // 防止重复刷新数据
                break;
            case MSG_UPDATE_LIST_FAILED:
                mFragment.refreshComplete();
                final ArrayList<VideoInfo> list = mVideoList;
                if (list != null && list.size() > 0) {
                    DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                } else {
                    mFragment.showRetryLayout();
                }
                break;
            case MSG_UPDATE_LIST_MORE:
                mVideoList.addAll((ArrayList<VideoInfo>) msg.obj);
                if (mVideoList.size() >= mTotalCount) {
                    mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                }
                mFragment.updateList(mVideoList);
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
            case EntityObject.ET_REQUEST_TEACHER_VIDEO:
                onGetVideoCallback(success, data);
                break;
            default:
                break;
        }
    }
}
