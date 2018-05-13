package com.sscf.investment.logic.component.manager;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketWarningManager;
import com.dengtacj.component.managers.ITradingStateManager;
import com.dengtacj.request.MarketRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.CommonConst;
import BEC.GetSecBsInfoRsp;
import BEC.RealMarketQRRsp;

/**
 * Created by davidwei on 2015/8/22.
 */
public final class MarketWarningManager implements IMarketWarningManager, DataSourceProxy.IRequestCallback {
    private RealMarketQRRsp mRsp;
    private OnGetDataCallback<RealMarketQRRsp> mCallback;

    public RealMarketQRRsp getMainBoardWarningInfo() {
        if (mRsp == null) {
            getMainBoardWarningInfoRequest(null);
        }
        return mRsp;
    }

    public void getMainBoardWarningInfoRequest(final OnGetDataCallback<RealMarketQRRsp> callback) {
        long currentTimestamp;
        final ITradingStateManager tradingStateManager = (ITradingStateManager) ComponentManager.getInstance()
                .getManager(ITradingStateManager.class.getName());
        if (tradingStateManager != null) {
            currentTimestamp = CommonConst.MILLIS_FOR_SECOND * tradingStateManager.getServerTime();
        } else {
            currentTimestamp = System.currentTimeMillis();
        }

        MarketRequestManager.getMainBoardWarningInfoRequest(currentTimestamp, this);
        mCallback = callback;
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success && data != null) {
            final RealMarketQRRsp rsp = (RealMarketQRRsp) data.getEntity();
            mRsp = rsp;
            final OnGetDataCallback<RealMarketQRRsp> callback = mCallback;
            mCallback = null;
            if (callback != null) {
                callback.onGetData(rsp);
            }
        }
    }
}
