package com.sscf.investment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by liqf on 2015/8/10.
 */
public class BoldTextView extends TextView {

    public BoldTextView(Context context) {
        super(context);
        getPaint().setFakeBoldText(true);
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getPaint().setFakeBoldText(true);
    }
}
