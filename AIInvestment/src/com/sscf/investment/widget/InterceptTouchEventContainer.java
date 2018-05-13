package com.sscf.investment.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.sscf.investment.sdk.utils.DtLog;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class InterceptTouchEventContainer extends LinearLayout {

	public InterceptTouchEventContainer(Context context) {
		super(context);
	}

	public InterceptTouchEventContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		DtLog.d("InterceptTouchEventContainer", "onInterceptTouchEvent ev=" + ev.toString());
		return true;
	}
}
