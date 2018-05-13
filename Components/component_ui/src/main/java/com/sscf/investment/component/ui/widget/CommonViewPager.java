package com.sscf.investment.component.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by liqf on 2016/1/4.
 * 避免Android系统的这个问题：java.lang.IllegalArgumentException: pointerIndex out of range pointerIndex=-1 pointerCount=1
 */
public class CommonViewPager extends ViewPager {
    public CommonViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;
        try {
            result = super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return result;
    }
}
