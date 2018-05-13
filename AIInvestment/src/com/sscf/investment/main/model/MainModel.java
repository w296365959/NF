package com.sscf.investment.main.model;

import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.presenter.MainPresenter;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.NetworkConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.dengtacj.thoth.MapProtoLite;
import BEC.AccountTicket;
import BEC.GetBoxLiveReq;
import BEC.GetBoxLiveRsp;

/**
 * Created by davidwei on 2017-08-11.
 */

public final class MainModel implements DataSourceProxy.IRequestCallback {
    private static final String TAG = MainModel.class.getSimpleName();
    private final MainPresenter mPresenter;

    public MainModel(final MainPresenter presenter) {
        mPresenter = presenter;
    }

    public void requestLiveMsg(final int lastLiveMsgTime, final int reqType) {
        DtLog.d(TAG, "requestLiveMsg : lastLiveMsgTime = " + lastLiveMsgTime + " , reqType = " + reqType);
        final GetBoxLiveReq req = new GetBoxLiveReq();

        req.iTime = lastLiveMsgTime;
        req.iType = reqType;

        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo != null) {
            final AccountTicket accountTicket = new AccountTicket();
            accountTicket.vtTicket = accountInfo.ticket;
            req.stAccountTicket = accountTicket;
        }
        req.stUserInfo = accountManager.getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_QUERY_DT_LIVE_MSG, req, this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_QUERY_DT_LIVE_MSG:
                GetBoxLiveRsp rsp = null;
                if (success) {
                    final MapProtoLite packet = (MapProtoLite) data.getEntity();
                    if (packet != null) {
                        int retCode = packet.read("", -1);
                        if (retCode == 0) {
                            rsp = packet.read(NetworkConst.RSP, new GetBoxLiveRsp());
                        }
                    }
                }
                mPresenter.onGetLiveMsg(rsp);
                break;
            default:
                break;
        }
    }
}
