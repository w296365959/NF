package com.sscf.investment.component.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by liqf on 2015/8/10.
 */
public class NumberTextView extends TextView {
    private Typeface mTypeface = TextDrawer.getTypeface();

    public NumberTextView(Context context) {
        super(context);
        setTypeface(mTypeface);
    }

    public NumberTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(mTypeface);
    }
}
