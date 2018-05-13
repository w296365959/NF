package com.sscf.investment.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.sscf.investment.R;

/**
 * Created by davidwei on 2015/11/16.
 * 在内部嵌套滚动使用的viewpager
 */
public final class NestScrollViewPager extends ViewPager {
    private float mDownX;
    private float mDownY;
    private int mMinDistanceToTriggerScroll;

    public NestScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMinDistanceToTriggerScroll = getResources().getDimensionPixelSize(R.dimen.min_distance_to_trigger_scroll);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float rawX = ev.getRawX();
                float rawY = ev.getRawY();
                float deltaX = Math.abs(rawX - mDownX);
                float deltaY = Math.abs(rawY - mDownY);
                if (deltaY > mMinDistanceToTriggerScroll && deltaY > deltaX) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
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
}
