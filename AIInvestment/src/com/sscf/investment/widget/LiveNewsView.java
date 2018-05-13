package com.sscf.investment.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;

import java.util.ArrayList;

import BEC.SecLiveMsg;

/**
 * Created by liqf on 2016/5/4.
 */
public class LiveNewsView extends View {
    private static final String TAG = "LiveNewsView";
    private int mDrawingWidth;
    private int mDrawingHeight;
    private RectF mRectBg = new RectF();
    private int mColorBg;
    private int mTextSize;
    private int mTextColor;
    private int mTextHeight;
    private int mTextBottom;
    private Paint mPaint;
    private Bitmap mBitmapReminder;
    private RectF mRectBitmap = new RectF();
    private TextDrawer mTextDrawer;
    private String mMsgText = "";

    public LiveNewsView(Context context) {
        super(context, null);
    }

    public LiveNewsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initResources(context);
    }

    private void initResources(Context context) {
        Resources resources = context.getResources();

        mColorBg = resources.getColor(R.color.black_80);
        mTextColor = resources.getColor(R.color.white_80);
        mTextSize = resources.getDimensionPixelSize(R.dimen.font_size_12);
        mBitmapReminder = ((BitmapDrawable)resources.getDrawable(R.drawable.capital_flow_reminder)).getBitmap();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);

        mTextDrawer = new TextDrawer();
        mTextHeight = mTextDrawer.measureSingleTextHeight(mTextSize, null);
        mTextBottom = mTextDrawer.measureSingleTextBottom(mTextSize, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();

        mRectBg.set(0, 0, mDrawingWidth, mDrawingHeight);
        int radius = mDrawingHeight / 2;
        mPaint.setColor(mColorBg);
        canvas.drawRoundRect(mRectBg, radius, radius, mPaint);

        float x, y;
        int iconWidth = mBitmapReminder.getWidth();
        mRectBitmap.left = (mDrawingHeight - iconWidth) / 2;
        mRectBitmap.top = (mDrawingHeight - iconWidth) / 2;
        mRectBitmap.right = mRectBitmap.left + iconWidth;
        mRectBitmap.bottom = mRectBitmap.top + iconWidth;

        canvas.drawBitmap(mBitmapReminder, null, mRectBitmap, mPaint);

        x = mDrawingHeight;
        y = mDrawingHeight - (mDrawingHeight - mTextHeight) / 2 - mTextBottom / 2;
        mPaint.setColor(mTextColor);
        canvas.drawText(mMsgText, x, y, mPaint);

        int textWidth = mTextDrawer.measureSingleTextWidth(mMsgText, mTextSize, null);
        x += textWidth + 50;
        canvas.drawText(">", x, y, mPaint);
    }

    public void setData(ArrayList<SecLiveMsg> secLiveMsgs) {
        SecLiveMsg liveMsg = secLiveMsgs.get(0);
        mMsgText = liveMsg.getSMsg();

        if (TextUtils.isEmpty(mMsgText)) {
            return;
        }

        int textWidth = mTextDrawer.measureSingleTextWidth(mMsgText, mTextSize, null);

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = textWidth + 100;
        layoutParams.height = 100;
        setLayoutParams(layoutParams);
        setVisibility(VISIBLE);
    }
}
