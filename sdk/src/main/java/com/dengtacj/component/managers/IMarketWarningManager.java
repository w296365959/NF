package com.dengtacj.component.managers;

import com.dengtacj.component.callback.OnGetDataCallback;

import BEC.GetSecBsInfoRsp;
import BEC.RealMarketQRRsp;

/**
 * Created by davidwei on 2017-09-04.
 */

public interface IMarketWarningManager {
    RealMarketQRRsp getMainBoardWarningInfo();
    void getMainBoardWarningInfoRequest(OnGetDataCallback<RealMarketQRRsp> callback);
}
