package com.sscf.investment.setting.entity;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.HashMap;

import BEC.E_LOGIN_TYPE;

/**
 * Created by davidwei on 2015-08-06.
 */
public final class AccountInfoEntity implements Serializable {
    public long id;
    public String cellphone;
    public String nickname;
    public String iconUrl;
    public byte[] ticket;
    // TODO 使用SparseArray不能序列化
    public HashMap<Integer, String> thirdPartyAccountInfos;

    private boolean isBinded(int type) {
        return !TextUtils.isEmpty(getOpenId(type));
    }

    public boolean isQqBinded() {
        return isBinded(E_LOGIN_TYPE.E_QQ_LOGIN);
    }

    public boolean isWechatBinded() {
        return isBinded(E_LOGIN_TYPE.E_WEIXIN_LOGIN);
    }

    public boolean isSinaWeiboBinded() {
        return isBinded(E_LOGIN_TYPE.E_WEIBO_LOGIN);
    }

    public boolean isUniqueThirdPartyBinded() {
        return TextUtils.isEmpty(cellphone) && thirdPartyAccountInfos != null &&  thirdPartyAccountInfos.size() == 1;
    }

    private String getOpenId(final int type) {
        if (thirdPartyAccountInfos != null) {
            return thirdPartyAccountInfos.get(type);
        }
        return null;
    }

    public String getQqOpenId() {
        return getOpenId(E_LOGIN_TYPE.E_QQ_LOGIN);
    }

    public String getWechatOpenId() {
        return getOpenId(E_LOGIN_TYPE.E_WEIXIN_LOGIN);
    }

    public String getSinaWeiboOpenId() {
        return getOpenId(E_LOGIN_TYPE.E_WEIBO_LOGIN);
    }

    public void addThirdPartyInfo(final int type, final String openId) {
        if (thirdPartyAccountInfos == null) {
            thirdPartyAccountInfos = new HashMap<Integer, String>(3);
        }
        thirdPartyAccountInfos.put(type, openId);
    }

    public void removeThirdPartyInfo(final int type) {
        if (thirdPartyAccountInfos != null) {
            thirdPartyAccountInfos.remove(type);
        }
    }
}
