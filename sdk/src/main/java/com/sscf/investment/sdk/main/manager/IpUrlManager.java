package com.sscf.investment.sdk.main.manager;

import android.content.Context;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import BEC.E_SHARE_TYPE;

/**
 * Created by davidwei on 2015/9/2.
 *
 * 管理各种ip的url
 */
public final class IpUrlManager extends BaseUrlManager {

    public static final String TAG = IpUrlManager.class.getSimpleName();

    private static IpUrlManager instance;

    private IpUrlManager() {
    }

    public static IpUrlManager getInstance() {
        if (instance == null) {
            instance = new IpUrlManager();
        }
        return instance;
    }

    public String getUpHosts() {
        return getUrl(E_SHARE_TYPE.E_WUP_FORWARD_URL);
    }

    @Override
    protected Map<Integer, String> getDefaultUrls() {
        final HashMap<Integer, String> urls = new HashMap<Integer, String>(1);
        urls.put(E_SHARE_TYPE.E_WUP_FORWARD_URL, "https://dtpay.uptougu.com/");
        return urls;
    }

    @Override
    protected File getUrlFile(Context context) {
        return new File(context.getFilesDir(), "ip_urls.bat");
    }
}
