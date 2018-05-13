package com.sscf.investment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    private Checkable mCheckableChild;

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mCheckableChild = getCheckableChild(this);
    }

    private static Checkable getCheckableChild(ViewGroup viewGroup) {
        final int childCount = viewGroup.getChildCount();
        View child = null;
        Checkable checkableChild = null;
        for (int i = 0; i < childCount; i++) {
            child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                checkableChild = getCheckableChild((ViewGroup) child);
                if (checkableChild != null) {
                    return checkableChild;
                }
            } else if (child instanceof Checkable) {
                return (Checkable) child;
            }
        }
        return null;
    }

    @Override
    public boolean isChecked() {
        if (mCheckableChild != null) {
            return mCheckableChild.isChecked();
        }
        return false;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mCheckableChild != null) {
            mCheckableChild.setChecked(checked);
        }
    }

    @Override
    public void toggle() {
        if (mCheckableChild != null) {
            mCheckableChild.toggle();
        }
    }
}
