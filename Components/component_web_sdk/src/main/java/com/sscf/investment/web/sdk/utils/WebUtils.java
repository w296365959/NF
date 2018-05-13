package com.sscf.investment.web.sdk.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IThemeManager;
import com.sscf.investment.sdk.utils.Base64Utils;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.web.sdk.WebConst;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import java.net.URLEncoder;

/**
 * Created by davidwei on 2017-09-05
 */

public final class WebUtils {

    public static String addThemeParam(final String url) {
        String urlWithThemeParam;

        boolean isDefaultTheme = true;
        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        if (themeManager != null) {
            isDefaultTheme = themeManager.isDefaultTheme();
        }
        final String themeName = isDefaultTheme ? "default" : "night";

        final int i = url.indexOf("?");
        if (i != -1) { //本身已经带参数，插入一个
            urlWithThemeParam = url.substring(0, i) + "?theme=" + themeName + "&" + url.substring(i + 1);
        } else { //本身不带参数，补充一个
            urlWithThemeParam = url + "?theme=" + themeName;
        }

        return urlWithThemeParam;
    }

    public static void setCookies(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }

        CookieManager cookieManager = null;
        try {
            CookieSyncManager.createInstance(context);
            cookieManager = CookieManager.getInstance();
        } catch (Throwable e) { // android.content.pm.PackageManager$NameNotFoundException: com.google.android.webview
            e.printStackTrace();
            return;
        }

        cookieManager.setAcceptCookie(true);
        final String host = Uri.parse(url).getHost();
        // url使用顶级域名
        url = "http://" + host;
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        long accountId = 0L;
        String ticketBase64 = "";
        String nickname = "";
        String iconUrl = "";
        if (accountInfo != null) {
            accountId = accountInfo.id;
            ticketBase64 = Base64Utils.encodeOnHttp(accountInfo.ticket);
            try {
                nickname = URLEncoder.encode(accountInfo.nickname, CommonConst.CHARTSET_UTF8);
                iconUrl = URLEncoder.encode(accountInfo.iconUrl, CommonConst.CHARTSET_UTF8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cookieManager.setCookie(url, WebConst.ACCOUNT_DT_ID + "=" + accountId);
        cookieManager.setCookie(url, WebConst.ACCOUNT_DT_TICKET + "=" + ticketBase64);
        cookieManager.setCookie(url, WebConst.ACCOUNT_DT_NICKNAME + "=" + nickname);
        cookieManager.setCookie(url, WebConst.ACCOUNT_DT_ICON + "=" + iconUrl);
        CookieSyncManager.getInstance().sync();
    }
}
