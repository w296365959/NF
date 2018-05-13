package com.sscf.investment.setting.entity;

import java.io.Serializable;

import BEC.E_DT_MEMBER_TYPE;
import BEC.E_FEED_USER_TYPE;

/**
 * Created by davidwei on 2016-10-20
 */
public final class AccountInfoExt implements Serializable {
    private static final long serialVersionUID = 1L;
    public AccountInfoEntity accountInfo;
    public int gender;
    public String province;
    public String city;
    public String profile;
    public String verification;
    public int type;
    public int memberType;
    public long memberEndTime;
    public String idNum;
    public String realName;

    public boolean isV() {
        return type == E_FEED_USER_TYPE.E_FEED_USER_INVEST_V;
    }

    public boolean isMember() {
        return memberType == E_DT_MEMBER_TYPE.E_DT_MEMBER;
    }

    public boolean isExpired() {
        return memberType == E_DT_MEMBER_TYPE.E_DT_MEMBER_EXPIRED;
    }
}
