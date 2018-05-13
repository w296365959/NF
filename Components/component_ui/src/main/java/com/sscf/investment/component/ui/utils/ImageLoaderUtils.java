package com.sscf.investment.component.ui.utils;

import android.content.Context;
import android.net.Uri;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IAccountManager;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.utils.Base64Utils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.web.CommonWebConst;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yorkeehuang on 2017/4/19.
 */

public final class ImageLoaderUtils {

    static {
        final Context context = SDKManager.getInstance().getContext();
        final ImageLoader imageLoader = ImageLoaderUtils.getImageLoader();
        if (!imageLoader.isInited()) {
            final DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true).cacheOnDisk(true).build();
            final ImageLoaderConfiguration config = new ImageLoaderConfiguration
                    .Builder(context)
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(new LruMemoryCache(4 * 1024 * 1024))
                    .memoryCacheSize(4 * 1024 * 1024)
                    .diskCacheSize(50 * 1024 * 1024)
                    .diskCacheFileCount(100)
                    .imageDownloader(new AvatarDownloadManager(context))
                    .build();
            imageLoader.init(config);
        }
    }

    public static DisplayImageOptions buildDisplayImageOptions(final int drawableId) {
        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(drawableId)
                .showImageOnFail(drawableId)
                .showImageOnLoading(drawableId).build();
    }

    public static ImageLoader getImageLoader() {
        return ImageLoader.getInstance();
    }
}

final class AvatarDownloadManager extends BaseImageDownloader {
    public static final String TAG = AvatarDownloadManager.class.getSimpleName();

    public AvatarDownloadManager(Context context) {
        super(context);
    }

    @Override
    protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
        DtLog.d(TAG, "createConnection url : " + url);
        String encodedUrl = Uri.encode(url, "@#&=*+-_.,:!?()/~\'%");
        final HttpURLConnection conn = (HttpURLConnection)(new URL(encodedUrl)).openConnection();

        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        final AccountInfoEntity accountInfo = accountManager == null ? null : accountManager.getAccountInfo();
        if (accountInfo != null) {
            final StringBuilder cookies = new StringBuilder();
            final String ticketBase64 = Base64Utils.encodeOnHttp(accountInfo.ticket);
            cookies.append(CommonWebConst.ACCOUNT_DT_ID).append("=").append(accountInfo.id).append(';')
                    .append(CommonWebConst.ACCOUNT_DT_TICKET).append("=").append(ticketBase64);
            DtLog.d(TAG, "createConnection cookies : " + cookies);
            conn.setRequestProperty("Cookie", cookies.toString());
        }

        conn.setConnectTimeout(this.connectTimeout);
        conn.setReadTimeout(this.readTimeout);

        return conn;
    }
}