package com.sscf.investment.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.sscf.investment.R;

/**
 * Created by liqf on 2015/11/16
 *
 */
public class AvoidClickViewPager extends ViewPager {
    private float mDownX;
    private float mDownY;
    private int mMinDistanceToTriggerScroll;
    private boolean mDisallowIntercept;

    public AvoidClickViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMinDistanceToTriggerScroll = getResources().getDimensionPixelSize(R.dimen.min_distance_to_trigger_scroll);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float rawX = ev.getRawX();
                float rawY = ev.getRawY();

                if (!mDisallowIntercept && Math.abs(rawX - mDownX) > mMinDistanceToTriggerScroll && Math.abs(rawY - mDownY) < mMinDistanceToTriggerScroll) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mDisallowIntercept = false;
                break;
        }

        boolean result = false;
        try {
            result = super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = false;

        try {
            result = super.onTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        mDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }
}
