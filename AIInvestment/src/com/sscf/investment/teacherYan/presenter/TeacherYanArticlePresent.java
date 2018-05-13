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
import com.sscf.investment.teacherYan.TeacherYanArticleFragment;
import com.sscf.investment.teacherYan.manager.TeacherYanArticleRequestManager;

import java.util.ArrayList;

import BEC.WxTeachListRsp;
import BEC.WxTeachTitle;

/**
 * Created by LEN on 2018/4/23.
 */

public class TeacherYanArticlePresent implements DataSourceProxy.IRequestCallback, Handler.Callback {
    private static final String TAG = TeacherYanArticlePresent.class.getSimpleName();

    private static final int MSG_UPDATE_LIST = 1;
    private static final int MSG_UPDATE_LIST_FAILED = 2;
    private static final int MSG_UPDATE_LIST_MORE = 4;
    private static final int MSG_UPDATE_LIST_NO_MORE = 5;
    private static final int MSG_UPDATE_LIST_MORE_FAILED = 6;

    private TeacherYanArticleFragment mFragment;
    private Handler mHandler;
    private ArrayList<WxTeachTitle> mArticleList;

    private int mLastID; // 分页使用
    private int totalCount;

    public TeacherYanArticlePresent(TeacherYanArticleFragment mFragment) {
        this.mFragment = mFragment;
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void requestListData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())){
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            return;
        }
        TeacherYanArticleRequestManager.requestTeacherYanArticleList(this);
    }

    public void requestMoreData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_MORE_FAILED);
            return;
        }
        TeacherYanArticleRequestManager.requestTeacherYanArticleListMore(mLastID, this);
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_LIST:
                final ArrayList<WxTeachTitle> articleList = (ArrayList<WxTeachTitle>) msg.obj;
                final int size = articleList == null ? 0 : articleList.size();
                if (size > 0) {
                    mArticleList = articleList;
                    mFragment.updateList(mArticleList);
                } else {
                    mFragment.showEmptyLayout();
                }
                break;
            case MSG_UPDATE_LIST_FAILED:
                final ArrayList<WxTeachTitle> list = mArticleList;
                if (list != null && list.size() > 0) {
                    DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                } else {
                    mFragment.showRetryLayout();
                }
                break;
            case MSG_UPDATE_LIST_MORE:
                mArticleList.addAll((ArrayList<WxTeachTitle>) msg.obj);
                mFragment.updateList(mArticleList);
                if (mArticleList.size() == totalCount) {
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
            case EntityObject.ET_TEACHER_YAN_ARTICLE:
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
                final WxTeachListRsp rsp = (WxTeachListRsp) data.getEntity();
                totalCount = rsp.iTotalCount;
                final ArrayList<WxTeachTitle> arrayList = rsp.vtWxTeachTitle;
                final int size = arrayList == null ? 0 : arrayList.size();
                if (size > 0) {
                    mLastID = arrayList.get(size - 1).getIID();
                }
                mHandler.obtainMessage(MSG_UPDATE_LIST, arrayList).sendToTarget();
                if (arrayList.size() == rsp.getITotalCount()){
                    mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                }
                DtLog.d(TAG, "getArticleCallbak size : " + size);
            } else {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            }
        } else { // 分页数据
            if (success) {
                if (endTime.equals(String.valueOf(mLastID))) { // 过滤重复数据
                    final WxTeachListRsp rsp = (WxTeachListRsp) data.getEntity();
                    totalCount = rsp.iTotalCount;
                    final ArrayList<WxTeachTitle> videos = rsp.vtWxTeachTitle;
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
