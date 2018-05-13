package com.sscf.investment.push;

import android.text.TextUtils;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.IPManager;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.umeng.message.MessageSharedPrefs;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

/**
 * Created by davidwei on 2015/12/07.
 *
 * 友盟push管理
 */
public final class UmengPushManager {
    private static final String TAG = UmengPushManager.class.getSimpleName();
    public static final String PUSH_VERSION = "version_4";

    public static void initUmengTag() {
        DtLog.d(TAG, "TagManager initUmengTag");
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        final PushAgent pushAgent = PushAgent.getInstance(dengtaApplication);

        if (TextUtils.isEmpty(pushAgent.getRegistrationId())) {
            return;
        }

        // 设置友盟的alias
        final long accountId = dengtaApplication.getAccountManager().getAccountId();
        final int VERSION = 4;
        try {
            pushAgent.addAlias(AccountManager.getUmengAlias(accountId, VERSION), DengtaConst.ALIAS_TYPE_ID_ENV, new UTrack.ICallBack() {
                @Override
                public void onMessage(boolean b, String s) {
                    DtLog.d(TAG, "Alias onMessage b : " + b + " s : " + s);
                }
            });
        } catch (Exception e) {
            DtLog.e(TAG, e.getMessage(), e);
        }

        // 设置tag
        final String[] tags = new String[2];
        if (IPManager.getInstance().isTest()) {
            tags[0] = DengtaConst.ALIAS_ENV_LOCAL;
        } else {
            tags[0] = DengtaConst.ALIAS_ENV_FORMAL;
        }
        tags[1] = PUSH_VERSION;

        final TagManager tagManager = pushAgent.getTagManager();
        try {
            tagManager.update(new TagManager.TCallBack() {
                @Override
                public void onMessage(boolean b, ITagManager.Result result) {
                    DtLog.d(TAG, "TagManager onMessage b : " + b + " result : " + result + "tags = " + tags[0] + " " + tags[1]);
                }
            }, tags);
        } catch (Exception e) {
            DtLog.e(TAG, e.getMessage(), e);
        }
    }

    public static void setPushKeyAndSecret() {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        if (!IPManager.getInstance().isTest()) {
            // 正式环境
            MessageSharedPrefs.getInstance(dengtaApplication).setMessageAppKey("5a72baab8f4a9d36d9000138");
            MessageSharedPrefs.getInstance(dengtaApplication).setMessageAppSecret("70e78bbe48f2ca331832b1a059acb871");
            DtLog.d(TAG, "setPushKeyAndSecret REAL");
        } else {
            // 测试环境
            MessageSharedPrefs.getInstance(dengtaApplication).setMessageAppKey("5ac348548f4a9d2e200000a2");
            MessageSharedPrefs.getInstance(dengtaApplication).setMessageAppSecret("65a562a2fffece2a64481b89355e57f6");
            DtLog.d(TAG, "setPushKeyAndSecret DEBUG");
        }
    }
}
