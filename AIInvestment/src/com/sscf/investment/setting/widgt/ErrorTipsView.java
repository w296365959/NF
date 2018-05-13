package com.sscf.investment.setting.widgt;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by davidwei on 2015-09-29.
 *
 */
public class ErrorTipsView extends TextView implements Handler.Callback {
    private final Handler mHandler;
    private static final int MSG_SHOW_ERROR_TIPS = 1;
    private static final int MSG_HIDE_ERROR_TIPS = 2;
    public ErrorTipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new Handler(this);
    }

    public void showErrorTips(String tips) {
        mHandler.obtainMessage(MSG_SHOW_ERROR_TIPS, tips).sendToTarget();
    }

    public void showErrorTips(int stringId) {
        mHandler.obtainMessage(MSG_SHOW_ERROR_TIPS, stringId, 0).sendToTarget();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SHOW_ERROR_TIPS:
                mHandler.removeMessages(MSG_HIDE_ERROR_TIPS);
                if (msg.arg1 != 0) {
                    setText(msg.arg1);
                } else if (msg.obj != null) {
                    setText((String) msg.obj);
                }

                setVisibility(View.VISIBLE);
                mHandler.sendEmptyMessageDelayed(MSG_HIDE_ERROR_TIPS, 3000L);
                break;
            case MSG_HIDE_ERROR_TIPS:
                setVisibility(View.INVISIBLE);
                setText("");
                break;
            default:
                break;
        }
        return false;
    }
}
