package com.sscf.investment.sdk.main.manager;

import android.content.Context;
import android.text.TextUtils;

import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import BEC.ShareUrl;
import BEC.ShareUrlReq;
import BEC.ShareUrlRsp;

/**
 * Created by davidwei on 2017/09/30
 */
public abstract class BaseUrlManager implements DataSourceProxy.IRequestCallback {

    public static final String TAG = BaseUrlManager.class.getSimpleName();

    private final Map<Integer, String> mDefaultUrls;
    protected Map<Integer, String> mUrls;
    protected File mUrlFile;

    public BaseUrlManager() {
        mDefaultUrls = getDefaultUrls();
    }

    public void init(final Context context) {
        final File urlFile = getUrlFile(context);
        if(urlFile != null && urlFile.exists()) {
            // 读取本地文件
            Object storeUrl = FileUtil.getObjectFromFile(urlFile);
            if(storeUrl != null && storeUrl instanceof HashMap) {
                mUrls = (HashMap<Integer, String>) storeUrl;
            } else {
                urlFile.delete();
                DtLog.w(TAG, "storeUrl instance is not HashMap");
            }
        }
        mUrlFile = urlFile;

        if (mDefaultUrls != null) {
            final ArrayList<Integer> reqTypeList = new ArrayList<Integer>(mDefaultUrls.keySet());
            getUrlsRequest(reqTypeList, BaseUrlManager.this);
        }
    }

    protected String getUrl(int key) {
        String url = "";
        if (mUrls != null) {
            url = mUrls.get(key);
        }

        if (TextUtils.isEmpty(url)) {
            if (mDefaultUrls != null) {
                url = mDefaultUrls.get(key);
            }
        }

        if (url == null) {
            url = "";
        }
        return url;
    }

    private static String getParams(final String paramsFormat, final Object... params) {
        if (params == null) {
            return paramsFormat;
        }

        final int length = params.length;
        for (int i = 0; i < length; i++) {
            try {
                params[i] = URLEncoder.encode(params[i].toString(), "UTF8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return String.format(paramsFormat, params);
    }

    public static String appendParams(final String url, final String paramsFormat, final Object... params) {
        String destUrl = null;
        if (url.indexOf('?') > -1) {
            destUrl = url + '&' + getParams(paramsFormat, params);
        } else {
            destUrl =  url + '?' + getParams(paramsFormat, params);
        }
        DtLog.d(TAG, "getUrl : " + destUrl);
        return destUrl;
    }

    protected static void getUrlsRequest(final ArrayList<Integer> reqTypeList, final DataSourceProxy.IRequestCallback callback) {
        DtLog.d(TAG, "getUrlsRequest");
        final ShareUrlReq req = new ShareUrlReq();
        req.vtShareType = reqTypeList;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_QUERY_SHARE_URL, req, callback);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        DtLog.d(TAG, "callback success : " + success);
        if (success) {
            if (data.getEntityType() == EntityObject.ET_QUERY_SHARE_URL) {
                // 股票详情分享URL获取
                final ShareUrlRsp shareUrlRsp = (ShareUrlRsp) data.getEntity();
                ArrayList<ShareUrl> shareUrls = shareUrlRsp.vtShareUrl;
                final HashMap<Integer, String> urls = new HashMap<Integer, String>();
                for (ShareUrl shareUrl : shareUrls) {
                    urls.put(shareUrl.eShareType, shareUrl.sUrl);
                }

                if (mUrlFile != null) {
                    FileUtil.saveObjectToFile(urls, mUrlFile);
                }

                mUrls = urls;
            }
        }
    }

    protected abstract Map<Integer, String> getDefaultUrls();

    protected abstract File getUrlFile(Context context);
}
