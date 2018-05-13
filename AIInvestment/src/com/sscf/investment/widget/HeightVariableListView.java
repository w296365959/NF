package com.sscf.investment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.sscf.investment.sdk.utils.DtLog;

/**
 * Created by liqf on 2015/10/31.
 */
public class HeightVariableListView extends ListView {
    private static final String TAG = HeightVariableListView.class.getSimpleName();

    public HeightVariableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpaec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpaec);
        DtLog.d(TAG, "onMeasure: height = " + getMeasuredHeight());
    }
}
