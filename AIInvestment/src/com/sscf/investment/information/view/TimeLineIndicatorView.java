package com.sscf.investment.information.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;

/**
 * Created by liqf on 2016/3/25.
 */
public class TimeLineIndicatorView extends View {
    private static final String TAG = TimeLineIndicatorView.class.getSimpleName();

    private String mText = "";

    private Paint mPaintText;
    private Paint mPaintBg;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private TextDrawer mTextDrawer;
    private int mTextSize;
    private int mTextHeight;
    private int mTextWidth;
    private int mTextBottom;
    private RectF mRectBg = new RectF();
    private int mBgHeight;
    private int mBgColor;

    public TimeLineIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initResources();
    }

    private void initResources() {
        Resources resources = getContext().getResources();

        mTextSize = resources.getDimensionPixelSize(R.dimen.font_size_12);
        mTextDrawer = new TextDrawer();
        mTextHeight = mTextDrawer.measureSingleTextHeight(mTextSize, null);
        mTextBottom = resources.getDimensionPixelSize(R.dimen.capital_flow_reminder_textBottom);
        mBgHeight = resources.getDimensionPixelSize(R.dimen.time_line_indicator_text_bg_height);

        TypedArray a = getContext().obtainStyledAttributes(new int[] {
            R.attr.news_important
        });
        mBgColor = a.getColor(0, Color.GRAY);
        a.recycle();

        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setTextSize(mTextSize);
        mPaintText.setColor(mBgColor);

        mPaintBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBg.setStyle(Paint.Style.STROKE);
        mPaintBg.setColor(mBgColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (TextUtils.isEmpty(mText)) {
            return;
        }

        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();

        int bgWidth = mDrawingWidth - paddingLeft - paddingRight;
        int lineStartX = paddingLeft + bgWidth / 2;
        int textStartX = paddingLeft + (bgWidth - mTextWidth) / 2;

        canvas.drawLine(lineStartX, 0, lineStartX, paddingTop, mPaintText);
        canvas.drawLine(lineStartX, paddingTop + mBgHeight, lineStartX, mDrawingHeight, mPaintText);

        mRectBg.set(paddingLeft, paddingTop, paddingLeft + bgWidth, paddingTop + mBgHeight);
        int radius = mBgHeight / 2;
        canvas.drawRoundRect(mRectBg, radius, radius, mPaintBg);
        canvas.drawText(mText, textStartX, paddingTop + mTextHeight + mTextBottom, mPaintText);
    }

    public void setText(final String text) {
        mText = text;
        mTextWidth = mTextDrawer.measureSingleTextWidth(mText, mTextSize, null);
        invalidate();
    }
}
