package com.sscf.investment.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.sdk.utils.DtLog;

/**
 * Created by liqf on 2015/10/20.
 */
public class VariableTextView extends TextView {
    private static final String TAG = VariableTextView.class.getSimpleName();
    private int mDrawingWidth;
    private String mText;
    private TextDrawer mTextDrawer;
    private Typeface mTypeface;

    /**
     * 原始布局文件里面设置的文字大小
     */
    private float mTextSize = 0;

    public VariableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initResources();

        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);

        mTextSize = getTextSize();

//        measure(0, 0);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (TextUtils.isEmpty(text)) {
            super.setText(text, type);
            return;
        }

//        resetTextSize(text);
        String trimmedText = text.toString().trim();

        int count = getCount(trimmedText);

        setTextSizeByCount(count);

        super.setText(trimmedText, type);
    }

    private void setTextSizeByCount(int count) {
        if (mTextSize == 0) {
            return;
        }

        float textSize = mTextSize;

        if (count > 14) {
            textSize = mTextSize * 12 / 18;
        } else if (count > 12) {
            textSize = mTextSize * 14 / 18;
        } else if (count > 10) {
            textSize = mTextSize * 16 / 18;
        } else {
            textSize = mTextSize;
        }

        if (textSize != getTextSize()) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    private int getCount(String trimmedText) {
        int count = 0;
        for (int i = 0; i < trimmedText.length(); i++) {
            char c = trimmedText.charAt(i);
            if (isAlphabet(c)) {
                count++;
            } else {
                count += 2;
            }
        }
        return count;
    }

    private boolean isAlphabet(char c) {
        if ((c <= 'Z' && c >= 'A') || (c <= 'z' && c >= 'a')
            || c == ' '
            || (c >= '0' && c <= '9')) {
            return true;
        }
        return false;
    }

    private void resetTextSize(CharSequence newText) {
//        mDrawingWidth = getMeasuredWidth();
        if (mDrawingWidth == 0) {
            DtLog.d(TAG, "mDrawingWidth == 0");
            return;
        }

        mText = newText.toString();
        mTypeface = getTypeface();

        float prevTextSize = getTextSize();

        float textSize = mTextSize;
        DtLog.d(TAG, "textSize = " + textSize);

        String text = mText.split("\n")[0];
        DtLog.d(TAG, "text = " + text);

        int textWidth = mTextDrawer.measureSingleTextWidth(text, textSize, mTypeface);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        while (textWidth > mDrawingWidth - paddingLeft - paddingRight) {
            textWidth = mTextDrawer.measureSingleTextWidth(text, --textSize, mTypeface);
        }
        DtLog.d(TAG, "after textSize = " + textSize);

        if (textSize != prevTextSize) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            DtLog.d(TAG, "after set textSize = " + textSize);
        }
    }

    private void initResources() {
        mTextDrawer = new TextDrawer();
    }
}
