package com.sscf.investment.sdk.core;

import BEC.GenGuidReq;
import BEC.GenGuidRsp;
import BEC.ReportUserInfoReq;
import BEC.UserInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.SDKUtil;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DataUtils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.PackageUtil;
import com.sscf.investment.sdk.utils.PrefUtil;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by free on 11/2/16.
 *
 * 授权管理模块
 *
 * 第三方APP的guid获取和上报
 * 第三方APP的授权情况
 */
public class AuthManager {
    private static final String TAG = AuthManager.class.getSimpleName();

    public static final String BEACON_APP_NAME = "com.sscf.investment";

    // guid，16个字节的字节串
    private byte[] guid = null;

    private UserInfo userInfo;

    private String dua = "";

    private int accountId = 0;

    // 是否是灯塔APP在使用SDK
    private boolean isBeaconApp = false;

    private IAuthComplete authComplete;

    private static AuthManager instance;

    private AuthManager() {
    }

    public static synchronized AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public void init(IAuthComplete authComplete) {
        DtLog.d(TAG, "init start");

        this.authComplete = authComplete;

        this.isBeaconApp = SDKManager.getInstance().getContext().getPackageName().equalsIgnoreCase(BEACON_APP_NAME);

        if (!this.isBeaconApp) {
            // 初始化GUID
            String guidString = PrefUtil.getString(PrefUtil.KEY_GUID, "");
            if (!TextUtils.isEmpty(guidString)) {
                guid = DataUtils.hexStringToBytes(guidString);
            }
            // 拉取和上报GUID
            reportGuid();

            // TODO 授权检测

        } else {
            this.authComplete.callback(true);
        }

        DtLog.d(TAG, "init end, isBeaconApp=" + this.isBeaconApp);
    }

    /**
     * 授权回调接口，用于鉴权完成后通知APP
     * 只有回调这个接口，外部APP才可以调用API，否则所有接口会返回鉴权失败错误码
     */
    public interface IAuthComplete {
        /**
         * 授权完成回调
         * @param isAuth 是否获得了授权
         */
        void callback(boolean isAuth);
    }

    public boolean isBeaconApp() {
//        return isBeaconApp;
        return true;
    }

    /**
     * 设置账户id 灯塔APP需要设置
     * @param accountId
     */
    public void setAccountId(int accountId) {
        getUserInfo().setIAccountId(accountId);
    }


    /**
     * guid上报或拉取
     * @return
     */
    private void reportGuid() {
        final GenGuidReq req = new GenGuidReq();
        req.stUserInfo = getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_GUID, req, new DataSourceProxy.IRequestCallback() {
            @Override
            public void callback(boolean success, EntityObject data) {
                if (success) {
                    DtLog.d(TAG, "initGuid callback, data=" + data.toString());
                    GenGuidRsp genGuidRsp = (GenGuidRsp) data.getEntity();
                    setGuid(genGuidRsp.getVGuid());

                    // 上报获取guid成功
                    final ReportUserInfoReq req = new ReportUserInfoReq();
                    req.stUserInfo = userInfo;
                    DataEngine.getInstance().request(EntityObject.ET_REPORT_USER_INFO, req, new DataSourceProxy.IRequestCallback() {
                        @Override
                        public void callback(boolean success, EntityObject data) {}
                    });
                } else {
                    DtLog.d(TAG, "get guid request failed");
                }
            }
        });
    }

    public byte[] getGuid() {
        return guid;
    }

    /**
     * 设置guid
     * @param guid
     */
    public void setGuid(byte[] guid) {
        if (guid != null && guid.length == 16) {
            this.guid = guid;
            String guidString = DataUtils.bytesToHexString(guid);
            DtLog.d(TAG, "save guid string=" + guidString);
            if (userInfo != null) {
                userInfo.setVGUID(guid);
            }
            PrefUtil.putString(PrefUtil.KEY_GUID, guidString);
        }
    }

    public UserInfo getUserInfo() {
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setSDUA(getDua());
            userInfo.setSIMEI(SDKUtil.getImei(SDKManager.getInstance().getContext()));
            userInfo.setSPackageName(SDKManager.getInstance().getContext().getPackageName());
            if (getGuid() == null) {
                // 避免guid为null的时候会crash的问题
                userInfo.vGUID = new byte[0];
            } else {
                userInfo.vGUID = getGuid();
            }
            userInfo.iAccountId = accountId;
        }

        return userInfo;
    }

    public String getDua() {
        if (dua == null) {
            StringBuffer stringBuffer = new StringBuffer();
            final String versionCode = String.valueOf(PackageUtil.getVersionCode(SDKManager.getInstance().getContext()));
            String duaVersionMain = null;
            if (versionCode.length() >= 2) {
                duaVersionMain = versionCode.substring(0,2);
            } else {
                duaVersionMain = versionCode;
            }
            stringBuffer.append("SN=").append("ADR" + "CJ" + "PH").append(duaVersionMain).append("_GA");
            stringBuffer.append("&VN=").append(versionCode);
            stringBuffer.append("&BN=").append("0");              // db编号
            stringBuffer.append("&VC=").append(android.os.Build.MANUFACTURER);  // 厂商名
            stringBuffer.append("&MO=").append(android.os.Build.MODEL);         // 设备名
            stringBuffer.append("&RL=").append(SDKUtil.getScreenWidth()).append("_").append(SDKUtil.getScreenHeight());  // 屏幕分辨率
            stringBuffer.append("&CHID=").append("sdk");    // 渠道号
            stringBuffer.append("&LCID=").append("");               // LCID
            stringBuffer.append("&RV=").append("");                 // 保留
            stringBuffer.append("&OS=").append("Android" + android.os.Build.VERSION.RELEASE);      // 系统版本
            stringBuffer.append("&DV=").append("V1");

            dua = stringBuffer.toString();
            DtLog.d(TAG, "DUA=" + dua);
        }
        return dua;
    }

    /**
     * 检测当前用户是否获得授权
     * @param type 授权类型
     * @return 是否授权
     */
    public boolean isAuth(int type) {

        return true;
    }

    /**
     * 获取指定包名程序的SHA1
     */
    public String getCurrentSHA1() {
        try {
            String pkg = SDKManager.getInstance().getContext().getPackageName();
            PackageInfo packageInfo = SDKManager.getInstance().getContext().getPackageManager().getPackageInfo(pkg, PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            return parseSignature(sign.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String parseSignature(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
            String sha1 = DataUtils.bytesToHexString(MessageDigest.getInstance("SHA-1").digest(cert.getEncoded()));
            return sha1;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
