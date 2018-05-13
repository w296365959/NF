package com.sscf.investment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import java.lang.reflect.Field;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by davidwei on 2017/03/22
 */
public class BeaconPtrFrameLayout extends PtrFrameLayout {
    private boolean disallowInterceptTouchEvent;
    private boolean supportDisallowInterceptTouchEvent;

    public BeaconPtrFrameLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        disallowInterceptTouchEvent = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (supportDisallowInterceptTouchEvent && disallowInterceptTouchEvent) {
            return dispatchTouchEventSupper(e);
        }
        boolean handled = false;
        try {
            handled = super.dispatchTouchEvent(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return handled;
    }

    public void setSupportDisallowInterceptTouchEvent(boolean supportDisallowInterceptTouchEvent) {
        this.supportDisallowInterceptTouchEvent = supportDisallowInterceptTouchEvent;
    }

    public PtrIndicator getIndicator() {
        try {
            Field field = PtrFrameLayout.class.getDeclaredField("mPtrIndicator");
            if(field != null) {
                field.setAccessible(true);
                Object obj = field.get(this);
                if(obj != null) {
                    return (PtrIndicator) obj;
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
