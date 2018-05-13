package com.sscf.investment.detail.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.sscf.investment.R;

import java.util.ArrayList;

import BEC.SecLiveMsg;

/**
 * Created by liqf on 2016/3/1.
 */
public class CapitalFlowReminderLayout extends FrameLayout {
    CapitalFlowReminderView mReminder;
    public CapitalFlowReminderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClipChildren(false);
        LayoutInflater.from(getContext()).inflate(R.layout.capital_reminder_layout, this, true);

        mReminder = (CapitalFlowReminderView) findViewById(R.id.capital_flow_reminder);
    }

    public void setData(ArrayList<SecLiveMsg> secLiveMsgs, int showType) {
//        setWidth(LayoutParams.MATCH_PARENT);
        mReminder.setData(secLiveMsgs, showType);
    }

    public void stopAnimation() {
        mReminder.stopAnimation();
    }

    public void setWidth(int width) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        layoutParams.width = width;
        setLayoutParams(layoutParams);
    }

    public int getFullWidth() {
        ViewParent viewParent = getParent();
        if (viewParent == null) {
            return 0;
        }

        return ((View) viewParent).getMeasuredWidth();
    }
}
