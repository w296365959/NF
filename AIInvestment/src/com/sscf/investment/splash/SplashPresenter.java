package com.sscf.investment.splash;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.dengtacj.component.managers.IX5WebViewManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.BitmapUtils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import java.io.File;

/**
 * Created by davidwei on 2017/04/13
 */
public final class SplashPresenter {
    private static final int DEFAULT_SPLASH_SECOND = 3;

    private final SplashDialog mView;
    private final SplashDataManager mModel;
    private SplashDbEntity mSplashEntity;

    SplashPresenter(final SplashDialog view) {
        mView = view;
        mModel = new SplashDataManager();
    }

    void init() {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        dengtaApplication.checkOldVersion();
        if (dengtaApplication.isNewInstall()) {
            mView.showNewInstallSplash();
        } else if (dengtaApplication.isOverInstall()) {
            mView.showUpgradeSplash();
        } else {
            SplashDbEntity entity = mModel.findCurrentSplash();
            mSplashEntity = entity;
            if (checkSplashExists(entity)) {
                showSplashFromNetwork(entity);
            } else {
                mView.showDefaultSplash(DEFAULT_SPLASH_SECOND);
            }

            mView.dismissWithAnimation(true);
        }
        new Handler(Looper.getMainLooper()).postDelayed(() ->{
            mModel.checkSplashUpdate();
        }, 2000L);
    }

    void showSplashFromNetwork(SplashDbEntity entity) {
        int attr = entity.getAttr();
        String url = entity.getUrl();

        final int splashSecond = entity != null && entity.getSkipSecond() > 0 ? entity.getSkipSecond() : DEFAULT_SPLASH_SECOND;
        final boolean skipVisible = entity != null && entity.getSkipState() != 0;
        String filePath = FileUtil.getSplashImageFilePathByUrl(url);
        Bitmap bitmap = BitmapUtils.decodeBitmap(filePath);
        if (attr == 0) {
            mView.showHalfScreenSplash(bitmap, skipVisible, splashSecond);
        } else if (attr == 1) {
            mView.showFullScreenSplash(bitmap, skipVisible, splashSecond);
        }
        StatisticsUtil.reportAction(StatisticsConst.DISPLAY_NETWORK_SPLASH);
    }

    private boolean checkSplashExists(SplashDbEntity entity) {
        if (entity == null) {
            return false;
        }

        String url = entity.getUrl();
        return checkImageExists(url);
    }

    private boolean checkImageExists(String url) {
        //根据URL检查图片是否已下载
        final String imageFilePath = FileUtil.getSplashImageFilePathByUrl(url);
        return new File(imageFilePath).exists();
    }

    void clickContent(final Context context) {
        if (mSplashEntity != null) {
            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            if (scheme != null) {
                final IX5WebViewManager x5WebViewManager = (IX5WebViewManager) ComponentManager.getInstance().getManager(IX5WebViewManager.class.getName());
                if (x5WebViewManager != null && x5WebViewManager.isX5Inited()) {
                    scheme.handleUrl(context, mSplashEntity.getSkipUrl());
                }
            }
        }
    }
}
