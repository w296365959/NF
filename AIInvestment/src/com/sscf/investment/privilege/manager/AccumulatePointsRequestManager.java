package com.sscf.investment.privilege.manager;


import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.entity.AccountInfoEntity;

import BEC.AccountTicket;
import BEC.AccuPointCodeType;
import BEC.CommitAccuPointCodeReq;

/**
 * Created by davidwei on 2016/12/20.
 */
public final class AccumulatePointsRequestManager {

    public static void commitInviteCodeRequest(final String code, final DataSourceProxy.IRequestCallback observer) {
        commitCodeRequest(code, AccuPointCodeType.E_ACCU_POINT_CODE_INVITE, observer);
    }

    public static void commitExchangeMemberCodeRequest(final String code, final DataSourceProxy.IRequestCallback observer) {
        commitCodeRequest(code, AccuPointCodeType.E_ACCU_POINT_CODE_ACTIVATE, observer);
    }

    private static void commitCodeRequest(final String code, final int codeType, final DataSourceProxy.IRequestCallback observer) {
        final CommitAccuPointCodeReq req = new CommitAccuPointCodeReq();
        req.sCode = code;
        req.iCodeType = codeType;

        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfo != null ? accountInfo.ticket : null);
        req.setStAccountTicket(accountTicket);;
        req.stUserInfo = accountManager.getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCUMULATE_POINTS_COMMIT_CODE, req, observer);
    }
}
