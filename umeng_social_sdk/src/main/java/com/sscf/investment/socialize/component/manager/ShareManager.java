package com.sscf.investment.socialize.component.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.dengtacj.component.entity.ShareParams;
import com.dengtacj.component.entity.ShareType;
import com.dengtacj.component.managers.IShareManager;
import com.sscf.investment.socialize.ShareDialog;
import com.sscf.investment.socialize.UmengSocialSDKUtils;

import java.util.Map;

/**
 * Created by davidwei on 2017-09-12.
 */

public final class ShareManager implements IShareManager {

    @Override
    public void showShareDialog(Activity activity, Map<String, ShareParams> params, ShareListener listener) {
        final ShareDialog shareDialog = new ShareDialog(activity);
        shareDialog.setShareListener(listener);
        shareDialog.showShareDialog(params);
    }

    @Override
    public void showShareDialog(Activity activity, ShareParams params) {
        final ShareDialog shareDialog = new ShareDialog(activity);
        shareDialog.showShareDialog(params);
    }

    @Override
    public void showShareDialog(Activity activity, ShareParams params, ShareType shareType) {
        final ShareDialog shareDialog = new ShareDialog(activity, shareType);
        shareDialog.showShareDialog(params);
    }

    @Override
    public void release(Context context) {
        UmengSocialSDKUtils.release(context);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        UmengSocialSDKUtils.onActivityResult(activity, requestCode, resultCode, data);
    }

}
