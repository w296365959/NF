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
import com.sscf.investment.teacherYan.AnalysisMarketFragment;
import com.sscf.investment.teacherYan.manager.AnalysisMarketRequestManager;

import java.util.ArrayList;

import BEC.InformationSpiderNews;
import BEC.InformationSpiderNewsListRsp;
import BEC.SecSimpleQuote;

/**
 * Created by LEN on 2018/4/24.
 */

public class AnalysisMarketPresent implements Handler.Callback, DataSourceProxy.IRequestCallback{

    private static final String TAG = AnalysisMarketPresent.class.getSimpleName();

    private static final int MSG_UPDATE_LIST = 1;
    private static final int MSG_UPDATE_LIST_FAILED = 2;
    private static final int MSG_UPDATE_QUOTES = 3;
    private static final int MSG_UPDATE_LIST_MORE = 4;
    private static final int MSG_UPDATE_LIST_NO_MORE = 5;
    private static final int MSG_UPDATE_LIST_MORE_FAILED = 6;

    private AnalysisMarketFragment mFragment;
    private final Handler mHandler;
    private int mTotalCount;

    private ArrayList<InformationSpiderNews> mInformations;
    private int mLastId;

    public AnalysisMarketPresent(AnalysisMarketFragment fragment) {
        this.mFragment = fragment;
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public void requestListData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            return;
        }

        AnalysisMarketRequestManager.requestAnalysisMarketList(this);
    }

    public void requestListDataMore() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            return;
        }
        AnalysisMarketRequestManager.requestAnalysisMarketListMore(mLastId, this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_ANALYSIS_MARKET:
                getAnalysisMarketCallback(success, data);
                break;
        }
    }

    private void getAnalysisMarketCallback(boolean success, EntityObject data) {
        final String endId = (String) data.getExtra();
        if (TextUtils.isEmpty(endId)) { // 首页数据
            if (success) {
                final InformationSpiderNewsListRsp rsp = (InformationSpiderNewsListRsp) data.getEntity();
                mTotalCount = rsp.getITotalCount();
                final ArrayList<InformationSpiderNews> informations = rsp.getVtInformationSpiderNews();
                final int size = informations == null ? 0 : informations.size();
                if (size > 0) {
                    mLastId = informations.get(size - 1).getIID();
                }
                mHandler.obtainMessage(MSG_UPDATE_LIST, informations).sendToTarget();
                if (rsp.getITotalCount() == informations.size()) {
                    mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                } else if (size > 0) {
                    mFragment.showFooterNormalLayout();
                }
            } else {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            }
        } else { // 分页数据
            if (success) {
                if (endId.equals(String.valueOf(mLastId))) { // 过滤重复数据
                    final InformationSpiderNewsListRsp rsp = (InformationSpiderNewsListRsp) data.getEntity();
                    mTotalCount = rsp.getITotalCount();
                    final ArrayList<InformationSpiderNews> informations = rsp.getVtInformationSpiderNews();
                    final int size = informations == null ? 0 : informations.size();
                    if (size > 0) {
                        mLastId = informations.get(size - 1).getIID();
                        mHandler.obtainMessage(MSG_UPDATE_LIST_MORE, informations).sendToTarget();
                    }

                    DtLog.d(TAG, "getHotSpotListCallback size : " + size);
                }
            } else {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_MORE_FAILED);
            }
        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_LIST:
                mFragment.refreshComplete();
                final ArrayList<InformationSpiderNews> informations = (ArrayList<InformationSpiderNews>) msg.obj;
                final int size = informations == null ? 0 : informations.size();
                if (size > 0) {
                    mInformations = informations;
                    mFragment.updateList(informations);
                } else {
                    mFragment.showEmptyLayout();
                }
                break;
            case MSG_UPDATE_LIST_FAILED:
                mFragment.refreshComplete();
                final ArrayList<InformationSpiderNews> list = mInformations;
                if (list != null && list.size() > 0) {
                    DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                } else {
                    mFragment.showRetryLayout();
                }
                break;
            case MSG_UPDATE_LIST_MORE:
                mInformations.addAll((ArrayList<InformationSpiderNews>) msg.obj);
                if (mInformations.size() >= mTotalCount) {
                    mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                }
                mFragment.updateList(mInformations);
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
}
