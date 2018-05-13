package com.dengtacj.component.managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.dengtacj.component.entity.ShareParams;
import com.dengtacj.component.entity.ShareType;

import java.util.Map;

/**
 * Created by davidwei on 2017-09-04.
 */

public interface IShareManager {
    /**
     * 不要改变，需要兼容以前shareSDK的常量值
     */
    String PLAT_QQ = "QQ";
    String PLAT_WECHAT = "Wechat";
    String PLAT_WECHAT_MOMENTS = "WechatMoments";
    String PLAT_SINA_WEIBO = "SinaWeibo";
    String PLAT_INTENT_MOMENTS = "intentMoments";

    int STATE_SUCCESS = 1;
    int STATE_ERROR = 2;
    int STATE_CANCEL = 3;

    void showShareDialog(Activity activity, Map<String, ShareParams> params, ShareListener listener);
    void showShareDialog(Activity activity, ShareParams params);
    void showShareDialog(Activity activity, ShareParams params, ShareType shareType);

    void release(Context context);
    void onActivityResult(final Activity activity, final int requestCode, final int resultCode, final Intent data);

    interface ShareListener {
        void onShareListener(int state, String plat);
    }
}
