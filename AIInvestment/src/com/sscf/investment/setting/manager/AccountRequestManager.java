package com.sscf.investment.setting.manager;

import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.sscf.investment.sdk.utils.EncryptUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import BEC.AccountInfo;
import BEC.AccountTicket;
import BEC.DtMemberInfo;
import BEC.E_DT_MEMBER_TYPE;
import BEC.E_LOGIN_TYPE;
import BEC.E_VERIFY_TYPE;
import BEC.FinishRegisterAccountReq;
import BEC.LoginReq;
import BEC.LogoutAccountReq;
import BEC.MODIFY_ACCOUNT_TYPE;
import BEC.ModifyAccountInfoReq;
import BEC.SendPhoneCodeReq;
import BEC.ThirdLoginInfo;
import BEC.UpdateTicketReq;
import BEC.UserInfo;
import BEC.VerfiyThirdAccountReq;
import BEC.VerifyAccountInfoReq;
import BEC.VerifyCode;

/**
 * davidwei
 * 用户信息相关的请求，向后台发送请求的接口统一放这里
 */
public final class AccountRequestManager {
    private static final byte[] KEY = "*&&jl0131$#".getBytes();
    /**
     * 账户存在，任需要发短信
     */
    public static final short STATE_ACCOUNT_EXIST_SEND_CODE = 1;
    /**
     * 账户存在，不需要发短信
     */
    public static final short STATE_ACCOUNT_EXIST_NOT_SEND_CODE = 2;

    /**
     * 更新ticket，防止ticket过期
     * @param accountId
     * @param observer
     */
    public static void updateTicketRequest(final byte[] ticket, final long accountId, final UserInfo userInfo, final DataSourceProxy.IRequestCallback observer) {
        final UpdateTicketReq req = new UpdateTicketReq();
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.iAccountId = accountId;
        req.stUserInfo = userInfo;

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_UPDATE_TICKET, req, observer);
    }

    /**
     * 向后台请求发送短信验证码
     * @param cellphone 手机号
     * @param state 验证手机号是否被使用过
     * @param observer 请求的回调
     */
    public static void sendVerifedCode(final String cellphone, final short state, final DataSourceProxy.IRequestCallback observer) {
        final SendPhoneCodeReq req = new SendPhoneCodeReq();
        req.sPhoneNum = cellphone;
        req.iStatus = state;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_SEND_PHONE_VERIFY_CODE, req, observer);
    }

    /**
     * 发送完成注册的请求，可以绕过验证码，直接调用此接口注册
     * @param cellphone
     * @param password
     * @param observer
     */
    public static void finishRegister(String cellphone, String password, final DataSourceProxy.IRequestCallback observer) {
        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.sPhoneNum = cellphone;
        final byte[] passwordCipher = EncryptUtils.encodeDES(password.getBytes(), KEY);
        accountInfo.vtPassword = passwordCipher;
        accountInfo.vtDupliPassword = passwordCipher;
        final FinishRegisterAccountReq req = new FinishRegisterAccountReq();
        req.stAccountInfo = accountInfo;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_FINISH_RESIGTER, req, observer);
    }

    /**
     * 向后台发送登录请求，验证登录密码也使用此请求
     */
    public static void login(String cellphone, String password, final DataSourceProxy.IRequestCallback observer) {
        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.sPhoneNum = cellphone;
        final byte[] passwordCipher = EncryptUtils.encodeDES(password.getBytes(), KEY);
        accountInfo.vtPassword = passwordCipher;

        final LoginReq req = new LoginReq();
        req.eLoginType = E_LOGIN_TYPE.E_PHONE_LOGIN;
        req.stAccountInfo = accountInfo;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_LOGIN, req, observer);
    }

    /**
     * 帐号登出
     */
    public static void logout(final long accountId, final DataSourceProxy.IRequestCallback observer) {
        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.iAccountId = accountId;

        final LogoutAccountReq req = new LogoutAccountReq();
        req.stAccountInfo = accountInfo;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_LOGOUT, req, observer);
    }

    /**
     * 第三方帐号登录
     */
    public static void thirdPartyLogin(String openId, String accessToken, int loginType, DataSourceProxy.IRequestCallback observer) {
        final ThirdLoginInfo thirdLoginInfo = new ThirdLoginInfo();
        thirdLoginInfo.sOpenId = openId;
        thirdLoginInfo.sAccessToken = accessToken;

        final VerfiyThirdAccountReq req = new VerfiyThirdAccountReq();
        req.eLoginType = loginType;
        req.stThirdLoginInfo = thirdLoginInfo;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_THIRD_PARTY_LOGIN, req, observer);
    }

    /**
     * QQ帐号登录
     */
    public static void qqLogin(String openId, String accessToken, DataSourceProxy.IRequestCallback observer) {
        final ThirdLoginInfo thirdLoginInfo = new ThirdLoginInfo();
        thirdLoginInfo.sOpenId = openId;
        thirdLoginInfo.sAccessToken = accessToken;

        final VerfiyThirdAccountReq req = new VerfiyThirdAccountReq();
        req.eLoginType = E_LOGIN_TYPE.E_QQ_LOGIN;
        req.stThirdLoginInfo = thirdLoginInfo;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_THIRD_PARTY_LOGIN, req, observer);
    }

    /**
     * 微信帐号登录
     */
    public static void wechatLogin(String openId, String accessToken, String unionid, DataSourceProxy.IRequestCallback observer) {
        final ThirdLoginInfo thirdLoginInfo = new ThirdLoginInfo();
        thirdLoginInfo.sOpenId = openId;
        thirdLoginInfo.sAccessToken = accessToken;
        HashMap<String, String> params = new HashMap<String, String>(1);
        params.put(SettingConst.EXTRA_WECHAT_UNIONID, unionid == null ? "" : unionid);
        thirdLoginInfo.mpParam = params;

        final VerfiyThirdAccountReq req = new VerfiyThirdAccountReq();
        req.eLoginType = E_LOGIN_TYPE.E_WEIXIN_LOGIN;
        req.stThirdLoginInfo = thirdLoginInfo;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_THIRD_PARTY_LOGIN, req, observer);
    }

    /**
     * 第三方帐号注册
     *
     */
    public static void thirdPartyRegister(String openId, String accessToken, int loginType, String nickname,
                                          byte[] avatarData, String imageType, DataSourceProxy.IRequestCallback observer) {
        final ThirdLoginInfo thirdLoginInfo = new ThirdLoginInfo();
        thirdLoginInfo.sOpenId = openId;
        thirdLoginInfo.sAccessToken = accessToken;

        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.sUserName = nickname;

        final LoginReq req = new LoginReq();
        req.eLoginType = loginType;
        req.stThirdLoginInfo = thirdLoginInfo;
        req.vtFaceData = avatarData;
        req.sFaceImageFileType = imageType;
        req.stAccountInfo = accountInfo;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_LOGIN, req, observer);
    }

    /**
     * 微信帐号注册
     *
     */
    public static void wechatRegister(String openId, String accessToken, String unionid, String nickname,
                                      byte[] avatarData, String imageType, DataSourceProxy.IRequestCallback observer) {
        final ThirdLoginInfo thirdLoginInfo = new ThirdLoginInfo();
        thirdLoginInfo.sOpenId = openId;
        thirdLoginInfo.sAccessToken = accessToken;
        HashMap<String, String> params = new HashMap<String, String>(1);
        params.put(SettingConst.EXTRA_WECHAT_UNIONID, unionid == null ? "" : unionid);
        thirdLoginInfo.mpParam = params;

        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.sUserName = nickname;

        final LoginReq req = new LoginReq();
        req.eLoginType = E_LOGIN_TYPE.E_WEIXIN_LOGIN;
        req.stThirdLoginInfo = thirdLoginInfo;
        req.vtFaceData = avatarData;
        req.sFaceImageFileType = imageType;
        req.stAccountInfo = accountInfo;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_LOGIN, req, observer);
    }

    /**
     * 发送修改昵称的请求
     */
    public static void modifyNickname(final byte[] ticket, final long accountId, final String nickname, final DataSourceProxy.IRequestCallback observer) {
        final ModifyAccountInfoReq req = new ModifyAccountInfoReq();
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.eModifyType = MODIFY_ACCOUNT_TYPE.E_MODIFY_USER_NAME;

        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.iAccountId = accountId;
        accountInfo.sUserName = nickname;
        req.stAccountInfo = accountInfo;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO, req, observer);
    }

    /**
     * 发送修改密码的请求
     */
    public static void modifyPassword(final byte[] ticket, final long accountId, final String oldPassword, final String newPassword, final DataSourceProxy.IRequestCallback observer) {
        final ModifyAccountInfoReq req = new ModifyAccountInfoReq();
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.eModifyType = MODIFY_ACCOUNT_TYPE.E_MOIDFY_PASSWORD;

        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.iAccountId = accountId;
        accountInfo.vtOldPassword = EncryptUtils.encodeDES(oldPassword.getBytes(), KEY);
        accountInfo.vtPassword = EncryptUtils.encodeDES(newPassword.getBytes(), KEY);
        req.stAccountInfo = accountInfo;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO, req, observer);
    }

    /**
     * 向后台发送验证短信验证码的请求
     * @param cellphone 手机号
     * @param smsCode 短信验证码
     * @param observer 请求的回调
     */
    public static void verifySmsCode(final String cellphone, final String smsCode, final DataSourceProxy.IRequestCallback observer) {
        final AccountInfo account = new AccountInfo();
        account.sPhoneNum = cellphone;

        final VerifyCode verifyCode = new VerifyCode();
        verifyCode.sPhoneVerifyCode = smsCode;

        final VerifyAccountInfoReq req = new VerifyAccountInfoReq();
        req.stAccountInfo = account;
        req.eVerifyType = E_VERIFY_TYPE.E_VERIFY_PHONE_CODE;
        req.stVerifyCode = verifyCode;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_VERIFY_ACCOUNT_INFO, req, observer);
    }

    /**
     * 向后台发送验证短信验证码的请求
     * @param cellphone 手机号
     * @param smsCode 短信验证码
     * @param observer 请求的回调
     */
    public static void modifyCellphoneinfo(final byte[] ticket, final long accountId, final String cellphone, final String password,
                                           final String smsCode, final DataSourceProxy.IRequestCallback observer) {
        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.iAccountId = accountId;
        accountInfo.sPhoneNum = cellphone;
        final byte[] passwordCipher = EncryptUtils.encodeDES(password.getBytes(), KEY);
        accountInfo.vtPassword = passwordCipher;
        accountInfo.vtDupliPassword = passwordCipher;

        final VerifyCode verifyCode = new VerifyCode();
        verifyCode.iType = 1;
        verifyCode.sPhoneVerifyCode = smsCode;

        final ModifyAccountInfoReq req = new ModifyAccountInfoReq();
        req.stAccountInfo = accountInfo;
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.eModifyType = MODIFY_ACCOUNT_TYPE.E_EXCHANGE_PHONE;
        req.stVerifyCode = verifyCode;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO, req, observer);
    }

    /**
     * 向后台发送验证短信验证码的请求
     * @param cellphone 手机号
     * @param smsCode 短信验证码
     * @param observer 请求的回调
     */
    public static void bindCellphoneinfo(final byte[] ticket, final long accountId, final String cellphone, final String password,
                                           final String smsCode, final DataSourceProxy.IRequestCallback observer) {
        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.iAccountId = accountId;
        accountInfo.sPhoneNum = cellphone;
        final byte[] passwordCipher = EncryptUtils.encodeDES(password.getBytes(), KEY);
        accountInfo.vtPassword = passwordCipher;
        accountInfo.vtDupliPassword = passwordCipher;

        final VerifyCode verifyCode = new VerifyCode();
        verifyCode.iType = 1;
        verifyCode.sPhoneVerifyCode = smsCode;

        final ModifyAccountInfoReq req = new ModifyAccountInfoReq();
        req.stAccountInfo = accountInfo;
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.eModifyType = MODIFY_ACCOUNT_TYPE.E_BIND_PHONE;
        req.stVerifyCode = verifyCode;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO, req, observer);
    }

    /**
     * 绑定第三方账户
     * @param ticket
     * @param accountId
     * @param loginType
     * @param openId
     */
    public static void bindThirdPartyAccount(final byte[] ticket, final long accountId, final int loginType, final String openId, final String accessToken,
                                             final DataSourceProxy.IRequestCallback observer) {
        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.iAccountId = accountId;

        ThirdLoginInfo thirdLoginInfo = new ThirdLoginInfo();
        thirdLoginInfo.sOpenId = openId;
        thirdLoginInfo.sAccessToken = accessToken;
        thirdLoginInfo.eType = loginType;

        final ModifyAccountInfoReq req = new ModifyAccountInfoReq();
        req.stAccountInfo = accountInfo;
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.eModifyType = MODIFY_ACCOUNT_TYPE.E_BIND_THIRD;
        req.stThirdLoginInfo = thirdLoginInfo;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO, req, observer);
    }

    /**
     * 绑定微信账户
     */
    public static void bindWechatAccount(final byte[] ticket, final long accountId, final String unionid, final String openId, final String accessToken,
                                     final DataSourceProxy.IRequestCallback observer) {
        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.iAccountId = accountId;

        ThirdLoginInfo thirdLoginInfo = new ThirdLoginInfo();
        thirdLoginInfo.sOpenId = openId;
        thirdLoginInfo.sAccessToken = accessToken;
        HashMap<String, String> params = new HashMap<String, String>(1);
        params.put(SettingConst.EXTRA_WECHAT_UNIONID, unionid == null ? "" : unionid);
        thirdLoginInfo.mpParam = params;
        thirdLoginInfo.eType = E_LOGIN_TYPE.E_WEIXIN_LOGIN;

        final ModifyAccountInfoReq req = new ModifyAccountInfoReq();
        req.stAccountInfo = accountInfo;
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.eModifyType = MODIFY_ACCOUNT_TYPE.E_BIND_THIRD;
        req.stThirdLoginInfo = thirdLoginInfo;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO, req, observer);
    }

    /**
     * 解绑第三方账户
     * @param ticket
     * @param accountId
     * @param loginType
     * @param openId
     */
    public static void unbindThirdPartyAccount(final byte[] ticket, final long accountId, final int loginType, final String openId,
                                               final DataSourceProxy.IRequestCallback observer) {
        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.iAccountId = accountId;

        ThirdLoginInfo thirdLoginInfo = new ThirdLoginInfo();
        thirdLoginInfo.sOpenId = openId;
        thirdLoginInfo.eType = loginType;

        final ModifyAccountInfoReq req = new ModifyAccountInfoReq();
        req.stAccountInfo = accountInfo;
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.eModifyType = MODIFY_ACCOUNT_TYPE.E_UNBIND_THIRD;
        req.stThirdLoginInfo = thirdLoginInfo;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO, req, observer);
    }

    /**
     * 忘记密码后，设置新的密码
     */
    public static void setForgottenPassword(final String cellphone, final String smsCode, final String password,
                                               final DataSourceProxy.IRequestCallback observer) {

        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.sPhoneNum = cellphone;
        final byte[] passwordCipher = EncryptUtils.encodeDES(password.getBytes(), KEY);
        accountInfo.vtPassword = passwordCipher;
        accountInfo.vtDupliPassword = passwordCipher;

        final VerifyCode verifyCode = new VerifyCode();
        verifyCode.iType = 1;
        verifyCode.sPhoneVerifyCode = smsCode;

        final ModifyAccountInfoReq req = new ModifyAccountInfoReq();
        req.stAccountInfo = accountInfo;
        // ticket应该是不需要的信息，传空的ticket
        req.stAccountTicket = new AccountTicket();
        req.eModifyType = MODIFY_ACCOUNT_TYPE.E_MOIDFY_PASSWORD;
        req.stVerifyCode = verifyCode;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO, req, observer);
    }

    /**
     * 修改用户头像
     */
    public static void modifyAvatar(final byte[] ticket, final long id, final byte[] avatarData, final String imageType,
                                    final DataSourceProxy.IRequestCallback observer) {
        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.iAccountId = id;

        final ModifyAccountInfoReq req = new ModifyAccountInfoReq();
        req.stAccountInfo = accountInfo;
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.eModifyType = MODIFY_ACCOUNT_TYPE.E_MODIFY_FACE;

        req.vtFaceData = avatarData;
        req.sFaceImageFileType = imageType;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO, req, observer);
    }

    /**
     * 修改性别
     */
    public static void modifyGender(final int gender, final AccountInfoExt accountInfoExt,
                                    final DataSourceProxy.IRequestCallback observer) {
        final ModifyAccountInfoReq req = new ModifyAccountInfoReq();
        final AccountInfo accountInfo = convertAccountInfo(accountInfoExt);
        accountInfo.iGender = gender;
        req.stAccountInfo = accountInfo;
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfoExt.accountInfo.ticket);
        req.setStAccountTicket(accountTicket);
        req.eModifyType = MODIFY_ACCOUNT_TYPE.E_MODIFY_USER_DESC;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO, req, observer);
    }

    /**
     * 修改地区
     */
    public static void modifyPlace(final String province, final String city, final AccountInfoExt accountInfoExt,
                                    final DataSourceProxy.IRequestCallback observer) {
        final ModifyAccountInfoReq req = new ModifyAccountInfoReq();
        final AccountInfo accountInfo = convertAccountInfo(accountInfoExt);
        accountInfo.sProvince = province;
        accountInfo.sCity = city;
        req.stAccountInfo = accountInfo;
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfoExt.accountInfo.ticket);
        req.setStAccountTicket(accountTicket);
        req.eModifyType = MODIFY_ACCOUNT_TYPE.E_MODIFY_USER_DESC;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO, req, observer);
    }

    /**
     * 修改个人简介
     */
    public static void modifyProfile(final String profile, final AccountInfoExt accountInfoExt,
                                   final DataSourceProxy.IRequestCallback observer) {
        final ModifyAccountInfoReq req = new ModifyAccountInfoReq();
        final AccountInfo accountInfo = convertAccountInfo(accountInfoExt);
        accountInfo.sProfile = profile;
        req.stAccountInfo = accountInfo;
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfoExt.accountInfo.ticket);
        req.setStAccountTicket(accountTicket);
        req.eModifyType = MODIFY_ACCOUNT_TYPE.E_MODIFY_USER_DESC;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO, req, observer);
    }

    static AccountInfo convertAccountInfo(final AccountInfoExt accountInfoExt) {
        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.iAccountId = accountInfoExt.accountInfo.id;
        accountInfo.iGender = accountInfoExt.gender;
        accountInfo.sProvince = accountInfoExt.province;
        accountInfo.sCity = accountInfoExt.city;
        accountInfo.sProfile = accountInfoExt.profile;
        accountInfo.sVerifyDesc = accountInfoExt.verification;
        accountInfo.eUserType = accountInfoExt.type;
        return accountInfo;
    }

    public static AccountInfoExt parseAccountInfo(final byte[] ticket, final AccountInfo accountInfo, final ArrayList<ThirdLoginInfo> thirdLoginInfos) {
        final AccountInfoExt accountInfoExt = new AccountInfoExt();
        final AccountInfoEntity accountInfoEntity = new AccountInfoEntity();
        accountInfoEntity.ticket = ticket;
        accountInfoEntity.id = accountInfo.iAccountId;
        accountInfoEntity.cellphone = accountInfo.sPhoneNum;
        accountInfoEntity.nickname = accountInfo.sUserName;
        accountInfoEntity.iconUrl = accountInfo.sFaceUrl;
        accountInfoExt.accountInfo = accountInfoEntity;

        accountInfoExt.gender = accountInfo.iGender;
        accountInfoExt.province = accountInfo.sProvince;
        accountInfoExt.city = accountInfo.sCity;
        accountInfoExt.profile = accountInfo.sProfile;
        accountInfoExt.verification = accountInfo.sVerifyDesc;
        accountInfoExt.type = accountInfo.eUserType;
        final DtMemberInfo memberInfo = accountInfo.stMember;
        if (memberInfo != null) {
            accountInfoExt.memberType = memberInfo.iMemberType;
            accountInfoExt.memberEndTime = memberInfo.lMemberEndTime;
        } else {
            accountInfoExt.memberType = E_DT_MEMBER_TYPE.E_DT_NO_MEMBER;
        }
        accountInfoExt.idNum = accountInfo.sUserIDNumber;
        accountInfoExt.realName = accountInfo.sUserRealName;

        final Iterator<ThirdLoginInfo> it = thirdLoginInfos.iterator();
        final HashMap<Integer, String> thirdPartyAccountInfos = new HashMap<Integer, String>(3);
        ThirdLoginInfo thirdLoginInfo = null;
        while (it.hasNext()) {
            thirdLoginInfo = it.next();
            thirdPartyAccountInfos.put(thirdLoginInfo.eType, thirdLoginInfo.sOpenId);
        }
        accountInfoEntity.thirdPartyAccountInfos = thirdPartyAccountInfos;

        return accountInfoExt;
    }
}