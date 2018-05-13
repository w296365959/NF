package com.dengtacj.component.managers;

import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.entity.AccountInfoExt;

import BEC.UserInfo;

/**
 * Created by davidwei on 2017-09-04.
 */

public interface IAccountManager {
    String getGuidString();
    String getDUA();
    AccountInfoEntity getAccountInfo();
    AccountInfoExt getAccountInfoExt();
    boolean isLogined();
    boolean isMember();
    long getAccountId();
    UserInfo getUserInfo();
    void removeAccountInfo();
    void updateAccountInfoFromWeb();
}
