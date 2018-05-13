package com.sscf.investment.sdk.stat;

import android.text.TextUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by yorkeehuang on 2017/6/13.
 */

public class TimeStatHelper {
    private long mVisibleTime;

    final private int mType;
    private int mKey = 0;
    private List<String> mExtras = Collections.EMPTY_LIST;
    private boolean mIncTotal = false;

    public TimeStatHelper(int type) {
        mType = type;
    }

    public TimeStatHelper(int type, boolean incTotal) {
        mType = type;
        mIncTotal = incTotal;
    }

    public void setKey(int key) {
        if(key >= 0) {
            mKey = key;
        }
    }

    public void setExtras(List extras) {
        if(extras != null) {
            mExtras = extras;
        }
    }

    public void start() {
        mVisibleTime = System.currentTimeMillis();
        String extra;
        if(!mExtras.isEmpty()) {
            extra = StatConsts.Extra.EXTRA_START + StatManager.EXTRA_SEPERATOR + TextUtils.join(StatManager.EXTRA_SEPERATOR, mExtras);
        } else {
            extra = StatConsts.Extra.EXTRA_START;
        }
        StatManager.getInstance().log(mType, mKey, extra);
    }

    public void end() {
        long now = System.currentTimeMillis();
        if(mVisibleTime > 0 && now > mVisibleTime) {
            int frontTime = (int) (now - mVisibleTime);
            String extra;
            if(!mExtras.isEmpty()) {
                extra = StatConsts.Extra.EXTRA_END + StatManager.EXTRA_SEPERATOR + String.valueOf(frontTime) + StatManager.EXTRA_SEPERATOR + TextUtils.join(StatManager.EXTRA_SEPERATOR, mExtras);
            } else {
                extra = StatConsts.Extra.EXTRA_END + StatManager.EXTRA_SEPERATOR + String.valueOf(frontTime);
            }
            StatManager.getInstance().log(mType, mKey, extra);
            if(mIncTotal) {
                StatManager.getInstance().incTotalFrontTime(frontTime);
            }
        }
        mVisibleTime = 0;
    }
}
