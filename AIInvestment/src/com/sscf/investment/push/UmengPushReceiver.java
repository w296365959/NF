package com.sscf.investment.push;

import android.content.Context;

import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by liqf on 2016/1/21.
 */
public class UmengPushReceiver extends UmengMessageHandler {
    private static final String TAG = PushManager.TAG;

    @Override
    public void dealWithNotificationMessage(Context context, UMessage uMessage) {
        DtLog.d(TAG, uMessage.text + " ");
        super.dealWithNotificationMessage(context, uMessage);
    }

    @Override
    public void dealWithCustomMessage(final Context context, final UMessage msg) {
        DtLog.d(TAG, "UmengPushReceiver dealWithCustomMessage UMENG ");
        DengtaApplication.getApplication().getPushManager().dealWithCustomMessage(context, msg.custom, DengtaConst.PUSH_FROM_UMENG, msg.play_sound);
    }
}
