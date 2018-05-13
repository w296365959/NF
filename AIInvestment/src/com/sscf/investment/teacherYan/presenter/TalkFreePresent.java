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
import com.sscf.investment.teacherYan.TalkFreeFragment;
import com.sscf.investment.teacherYan.manager.TalkFreeRequestManager;

import java.util.ArrayList;

import BEC.WxTalkFree;
import BEC.WxTalkFreeListRsp;

/**
 * Created by LEN on 2018/4/20.
 */

public class TalkFreePresent implements DataSourceProxy.IRequestCallback, Handler.Callback{
    private static final String TAG = TalkFreePresent.class.getSimpleName();

    private static final int MSG_UPDATE_LIST = 1;
    private static final int MSG_UPDATE_LIST_FAILED = 2;
    private static final int MSG_UPDATE_LIST_MORE = 4;
    private static final int MSG_UPDATE_LIST_NO_MORE = 5;
    private static final int MSG_UPDATE_LIST_MORE_FAILED = 6;

    private TalkFreeFragment mFragment;
    private final Handler mHandler;
    private TalkFreeRequestManager mManager;
    private ArrayList<WxTalkFree> mTalkFrees;

    private int mLastID; // 分页使用
    private int totalCount;

    public TalkFreePresent(final TalkFreeFragment fragment) {
        mFragment = fragment;
        mHandler = new Handler(Looper.getMainLooper(), this);
        mManager = new TalkFreeRequestManager();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void requestListData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            return;
        }

        TalkFreeRequestManager.requestTalkFreeData(this);
    }

    public void requestListMoreData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_MORE_FAILED);
            return;
        }

        TalkFreeRequestManager.requestTalkFreeDataMore(this, mLastID);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_LIST:
                final ArrayList<WxTalkFree> talkFrees = (ArrayList<WxTalkFree>) msg.obj;
                final int size = talkFrees == null ? 0 : talkFrees.size();
                if (size > 0) {
                    mTalkFrees = talkFrees;
                    mFragment.updateList(talkFrees);
                } else {
                    mFragment.showEmptyLayout();
                }
                break;
            case MSG_UPDATE_LIST_FAILED:
                final ArrayList<WxTalkFree> list = mTalkFrees;
                if (list != null && list.size() > 0) {
                    DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                } else {
                    mFragment.showRetryLayout();
                }
                break;
            case MSG_UPDATE_LIST_MORE:
                mTalkFrees.addAll((ArrayList<WxTalkFree>) msg.obj);
                mFragment.updateList(mTalkFrees);
                if (mTalkFrees.size() == totalCount) {
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
            case EntityObject.ET_TALK_FREE:
                getTalkFreeCallback(success, data);
                break;
            default:
                break;
        }
    }

    private void getTalkFreeCallback(boolean success, EntityObject data) {
        final String endTime = (String) data.getExtra();
        if (TextUtils.isEmpty(endTime)) { // 首页数据
            if (success) {
                final WxTalkFreeListRsp rsp = (WxTalkFreeListRsp) data.getEntity();
                totalCount = rsp.iTotalCount;
                final ArrayList<WxTalkFree> talkFrees = rsp.vtWxTalkFree;
                final int size = talkFrees == null ? 0 : talkFrees.size();
                if (size > 0) {
                    mLastID = talkFrees.get(size - 1).getIID();
                }
                mHandler.obtainMessage(MSG_UPDATE_LIST, talkFrees).sendToTarget();
                if (talkFrees.size() == totalCount){
                    mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                }
                DtLog.d(TAG, "getTalkFreeCallback size : " + size);
            } else {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            }
        } else { // 分页数据
            if (success) {
                if (endTime.equals(String.valueOf(mLastID))) { // 过滤重复数据
                    final WxTalkFreeListRsp rsp = (WxTalkFreeListRsp) data.getEntity();
                    totalCount = rsp.iTotalCount;
                    final ArrayList<WxTalkFree> talkFrees = rsp.vtWxTalkFree;
                    final int size = talkFrees == null ? 0 : talkFrees.size();
                    if (size > 0) {
                        mLastID = talkFrees.get(size - 1).getIID();
                        mHandler.obtainMessage(MSG_UPDATE_LIST_MORE, talkFrees).sendToTarget();
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
