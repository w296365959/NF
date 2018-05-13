package com.dengtacj.component.managers;

import android.content.Context;
import android.net.Uri;

/**
 * Created by davidwei on 2017/09/12
 */
public interface ISchemeManager {
    int handleUrl(Context context, String url);
    int handleBeaconScheme(Context context, Uri url);
    int handleWebViewUrl(Context context, String url);
    String[] getDomainList();
}
