package com.sscf.investment.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TextAppearanceSpan;

/**
 * 自定义字体的TextAppearanceSpan
 */
public final class CustomTypefaceTextAppearanceSpan extends TextAppearanceSpan {

    private final Typeface mTypeFace;

    public CustomTypefaceTextAppearanceSpan(Context context, int appearance, Typeface typeface) {
        super(context, appearance);
        mTypeFace = typeface;
    }

    @Override
    public void updateMeasureState(TextPaint ds) {
        super.updateMeasureState(ds);
        ds.setTypeface(mTypeFace);
    }
}