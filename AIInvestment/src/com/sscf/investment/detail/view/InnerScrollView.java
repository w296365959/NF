package com.sscf.investment.detail.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.sscf.investment.sdk.utils.DtLog;

/**
 * Created by liqf on 2015/9/16.
 */
public class InnerScrollView extends NestedScrollView {
    int currentY;

    public InnerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        DtLog.d("liqf", "onTouchEvent: action = " + action);

        View child = getChildAt(0);
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
//                return true;
                break;
            case MotionEvent.ACTION_MOVE:
                int height = child.getMeasuredHeight();
                height = height - getMeasuredHeight();

                // System.out.println("height=" + height);
                int scrollY = getScrollY();
                // System.out.println("scrollY" + scrollY);
                int y = (int)ev.getY();

                // 手指向下滑动
                if (currentY < y) {
                    if (scrollY <= 0) {
                        // 如果向下滑动到头，就把滚动交给父Scrollview
//                        setParentScrollable(true);
                        return false;
                    } else {
                        setParentScrollable(false);

                    }
                } else if (currentY > y) {
                    if (scrollY >= height) {
                        // 如果向上滑动到头，就把滚动交给父Scrollview
//                        setParentScrollable(true);
                        return false;
                    } else {
                        setParentScrollable(false);

                    }

                }
                currentY = y;
                break;
            case MotionEvent.ACTION_UP:
                setParentScrollable(true);
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 将父scrollview的滚动事件拦截
            currentY = (int) ev.getY();
            setParentScrollable(false);
            return super.onInterceptTouchEvent(ev);
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            // 把滚动事件恢复给父Scrollview
            setParentScrollable(true);
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
        }

        return super.onInterceptTouchEvent(ev);
    }

    private void setParentScrollable(boolean scrollable) {
        boolean disallowParent = !scrollable;
        getParent().requestDisallowInterceptTouchEvent(disallowParent);
    }

}
