package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.StringUtil;

import java.util.ArrayList;

import BEC.TickDesc;

/**
 * Created by liqf on 2015/9/16.
 */
public class TicksView extends View {
    public static final int SHOW_COUNT = 10;
    private NestedScrollView mParentScrollView;
    private ArrayList<TickDesc> mTickList;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private int mItemHeight;

    private Paint mPaint;
    private int mTextSize;

    private int mColorRed;
    private int mColorGreen;
    private int mColorBase;
    private int mTextHeight;

    private Runnable mScrollToBottomRunnable = new Runnable() {
        @Override
        public void run() {
            if (mParentScrollView != null) {
                mParentScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }
    };
    private int mTitleWidth;
    private int mPriceWidth;
    private int mVolumeWidth;
    private TextDrawer mTextDrawer;
    private Typeface mTypeface;
    private float mYesterdayClose = 0;
    private int mTextPadding;
    private int mTpFlag = DengtaConst.DEFAULT_TP_FLAG;

    public TicksView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initResources();
    }

    private void initResources() {
        Resources resources = getResources();
        mColorRed = resources.getColor(R.color.stock_red_color);
        mColorGreen = resources.getColor(R.color.stock_green_color);
        mColorBase = ContextCompat.getColor(getContext(), R.color.default_text_color_80);

        mTextSize = resources.getDimensionPixelSize(R.dimen.font_size_14);
        mTextPadding = resources.getDimensionPixelSize(R.dimen.stock_buy_sell_textPadding);

        mTitleWidth = resources.getDimensionPixelSize(R.dimen.stock_buy_sell_title_width);
        mPriceWidth = resources.getDimensionPixelSize(R.dimen.stock_buy_sell_price_width);
        mVolumeWidth = resources.getDimensionPixelSize(R.dimen.stock_buy_sell_volume_width);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTypeface = TextDrawer.getTypeface();
        mPaint.setColor(mColorBase);
        mPaint.setTextSize(mTextSize);
        mPaint.setTypeface(mTypeface);

        mTextDrawer = new TextDrawer();
        mTextHeight = mTextDrawer.measureSingleTextHeight(mTextSize, mTypeface);
    }

    private void initDrawingSizes() {
        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = mParentScrollView.getMeasuredHeight();
        mVolumeWidth = mDrawingWidth - mTitleWidth - mPriceWidth;

        mItemHeight = mDrawingHeight / SHOW_COUNT;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mParentScrollView == null) {
            mParentScrollView = getNestedScrollView();
        }
        initDrawingSizes();

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mItemHeight * getTickCount());
    }

    private NestedScrollView getNestedScrollView(){
        if (getParent() instanceof NestedScrollView){
            return (NestedScrollView) getParent();
        }else{
            return (NestedScrollView) getParent().getParent();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        postDelayed(mScrollToBottomRunnable, getDelayMillions());
    }

    private int getDelayMillions(){
        if (DeviceUtil.isVivoX20()){
            return 300;
        }
        return 100;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTickList == null || mYesterdayClose == 0) {
            return;
        }

        int textWidth;
        int x = 0, y = mTextHeight;
        for (TickDesc tickDesc : mTickList) {
            x = 0;

            int minute = tickDesc.getIMinute();
            String time = StringUtil.minuteToTime(minute);
            mPaint.setColor(mColorBase);
            mPaint.setTextSize(mTextSize);
            canvas.drawText(time, x, y, mPaint);

            x += mTitleWidth;
            float now = tickDesc.getFNow();
            String price = StringUtil.getFormattedFloat(now, mTpFlag);
            textWidth = mTextDrawer.measureSingleTextWidth(price, mTextSize, mTypeface);
            int startX = x + mPriceWidth - textWidth;
            setPriceColor(now);
            mPaint.setTextSize(mTextSize);
            canvas.drawText(price, startX, y, mPaint);

            x += mPriceWidth;
            long nowVolume = tickDesc.getLNowVolume();
            String volume = StringUtil.getVolumeString(nowVolume, false, true, false);
            textWidth = mTextDrawer.measureSingleTextWidth(volume, mTextSize, mTypeface);
            int textSize = mTextSize;
            while (textWidth > mVolumeWidth - mTextPadding) {
                textWidth = mTextDrawer.measureSingleTextWidth(volume, --textSize, mTypeface);
            }
            mPaint.setTextSize(textSize);
            x += mVolumeWidth - textWidth;
            int inOut = tickDesc.getIInOut();
            setVolumeColor(inOut);
            canvas.drawText(volume, x, y, mPaint);

            y += mItemHeight;
        }
    }

    private void setVolumeColor(int inOut) {
        if (inOut == 0) {
            mPaint.setColor(mColorRed);
        } else {
            mPaint.setColor(mColorGreen);
        }
    }

    private void setPriceColor(float priceValue) {
        if (priceValue > mYesterdayClose) {
            mPaint.setColor(mColorRed);
        } else if (priceValue < mYesterdayClose) {
            mPaint.setColor(mColorGreen);
        } else {
            mPaint.setColor(mColorBase);
        }
    }

    public void setTicksData(ArrayList<TickDesc> tickDescs) {
        mTickList = tickDescs;

        requestLayout();
        postInvalidate();
    }

    public ArrayList<TickDesc> getTickList() {
        return mTickList;
    }

    public void setTpFlag(int tpFlag) {
        if (mTpFlag != tpFlag) {
            mTpFlag = tpFlag;
            invalidate();
        }
    }

    private int getTickCount() {
        if (mTickList == null) {
            return 0;
        }

        return mTickList.size();
    }

    public void setYesterdayClose(float close) {
        mYesterdayClose = close;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionType = event.getAction();
        if (actionType == MotionEvent.ACTION_DOWN){
            mParentScrollView.setNestedScrollingEnabled(false);
        }else if (actionType == MotionEvent.ACTION_UP){
            mParentScrollView.setNestedScrollingEnabled(true);
        }
        return super.onTouchEvent(event);
    }
}
