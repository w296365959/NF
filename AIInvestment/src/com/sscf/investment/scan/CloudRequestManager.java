package com.sscf.investment.scan;

import BEC.AccountTicket;
import BEC.ReportAckLoginReq;
import BEC.ReportScanReq;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;

public final class CloudRequestManager {

    public static void reportScan(final byte[] ticket, final String sessionId, final int type, final DataSourceProxy.IRequestCallback observer) {
        final ReportScanReq req = new ReportScanReq();
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.sSessionId = sessionId;
        req.iTargetType = type;
        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_CLOUD_REPORT_SCAN, req, observer);
    }

    public static void confirmLogin(final byte[] ticket, final String rspTicket, final int type, final DataSourceProxy.IRequestCallback observer) {
        final ReportAckLoginReq req = new ReportAckLoginReq();
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.sTicket = rspTicket;
        req.iTargetType = type;
        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_CLOUD_CONFIRM_LOGIN, req, observer);
    }
}