package com.sscf.investment.teacherYan.presenter;

import android.os.Handler;
import android.os.Message;

import com.sscf.investment.information.manager.MarketInfoRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.EntityUtil;

import java.util.ArrayList;

import BEC.BannerInfo;

/**
 * Created by LEN on 2018/4/24.
 */

public class TeacherYanBannerPresent implements Handler.Callback, DataSourceProxy.IRequestCallback{

    private static final String TAG = TeacherYanBannerPresent.class.getSimpleName();

    private OnGetBannerCallback mCallBack;
    private Handler mHandler;
    private int mBannerType;

    private static final int MSG_UPDATE_BANNER = 6;

    public TeacherYanBannerPresent(int bannerType, OnGetBannerCallback callback) {
        this.mCallBack = callback;
        this.mBannerType = bannerType;
        this.mHandler = new Handler(this);
    }

    public void requestData(){
        MarketInfoRequestManager.requestBannerList(this, mBannerType);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_BANNER:
                final ArrayList<BannerInfo> bannerList = (ArrayList<BannerInfo>) msg.obj;
                if (null != mCallBack){
                    mCallBack.onGetBannerCallback(bannerList);
                }
                break;
            default:
                break;
        }
        return true;

    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()){
            case EntityObject.ET_GET_DISCOVER_BANNER:
                onGetBannerList(EntityUtil.entityToBannerList(success, data));
            break;
        }
    }

    private void onGetBannerList(final ArrayList<BannerInfo> bannerList) {
        final int size = bannerList == null ? 0 : bannerList.size();
        if (size >= 0) {
            mHandler.obtainMessage(MSG_UPDATE_BANNER, bannerList).sendToTarget();
        }
    }

    public interface OnGetBannerCallback {
        void onGetBannerCallback(ArrayList<BannerInfo> bannerList);
    }
}
