package com.sscf.investment.market.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.DeviceUtil;
import BEC.ChangeStatDesc;

/**
 * Created by davidwei on 2017/11/13
 * 涨跌分布
 */
public final class MarketChinaChangeStatView extends FrameLayout implements View.OnClickListener, Runnable {
    private static final int Y_LINE_COUNT = 5;
    private static final int PILLAR_COUNT = 23;
    private final int mBottomTextHeight;
    private final int mYLineSize;
    private final int[] mLineY = new int[Y_LINE_COUNT];
    private final Paint mChartPaint;
    private final Paint mTextPaint;
    private final int mTextYGap;
    private final int mTextMargin;

    private int mLineWidth;
    private int mChartHeight;

    private final int mRightTextWidth;
    private int mRightTextLeft;

    private int mPillarWidth;
    private final int mTextSize;

    private float mLastX;
    private float mLastY;

    private int mMaxLineValue;
    private TextView mTextView;
    private LayoutParams mParams;
    private final int mColorRed;
    private final int mColorGreen;
    private final int mColorGray;
    private final int mColorGray60;
    private final int mYLineColor;

    private final int mTouchSlop;

    private int mSelectIndex;
    private boolean mEditMode = false;

    // data
    private boolean mHasData;
    private final int[] mPillarsX = new int[PILLAR_COUNT];
    private final int[] mValues = new int[PILLAR_COUNT];
    private final String[] mUpdownText = new String[PILLAR_COUNT];
    private final int[] mUpdownNum = new int[PILLAR_COUNT];

    public MarketChinaChangeStatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop() / 2 ;
        mColorRed = ContextCompat.getColor(context, R.color.stock_red_color);
        mColorGreen = ContextCompat.getColor(context, R.color.stock_green_color);
        mColorGray = 0xffccd0d6;
        mColorGray60 = ContextCompat.getColor(context, R.color.default_text_color_60);
        mYLineColor = ContextCompat.getColor(context, R.color.market_change_stat_divider_color);
        mRightTextWidth = DeviceUtil.dip2px(context,24);
        mBottomTextHeight = DeviceUtil.dip2px(context,24);
        mYLineSize = DeviceUtil.dip2px(context, 0.5f);
        mTextMargin = DeviceUtil.dip2px(context, 2);
        mChartPaint = new Paint();

        final int textSize = DeviceUtil.dip2px(context, 10);
        mTextYGap = textSize / 2 - DeviceUtil.dip2px(context, 2);
        mTextSize = textSize;

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);

        mUpdownText[1] = "9% ~ 10%";
        mUpdownText[2] = "8% ~ 9%";
        mUpdownText[3] = "7% ~ 8%";
        mUpdownText[4] = "6% ~ 7%";
        mUpdownText[5] = "5% ~ 6%";
        mUpdownText[6] = "4% ~ 5%";
        mUpdownText[7] = "3% ~ 4%";
        mUpdownText[8] = "2% ~ 3%";
        mUpdownText[9] = "1% ~ 2%";
        mUpdownText[10] = "0% ~ 1%";
        mUpdownText[12] = "0% ~ 1%";
        mUpdownText[13] = "1% ~ 2%";
        mUpdownText[14] = "2% ~ 3%";
        mUpdownText[15] = "3% ~ 4%";
        mUpdownText[16] = "4% ~ 5%";
        mUpdownText[17] = "5% ~ 6%";
        mUpdownText[18] = "6% ~ 7%";
        mUpdownText[19] = "7% ~ 8%";
        mUpdownText[20] = "8% ~ 9%";
        mUpdownText[21] = "9% ~ 10%";
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();
        final float xDiff = Math.abs(x - mLastX);
        final float yDiff = Math.abs(y - mLastY);
        mLastX = x;
        mLastY = y;
        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                final boolean isYScroll = yDiff > mTouchSlop * 0.75f && yDiff > xDiff * 1.5f;
                if (!mEditMode && isYScroll) {
                    removeCallbacks(this);
                    mEditMode = false;
                    setEditMode(false);
                    final ViewGroup viewGroup = (ViewGroup) getParent();
                    viewGroup.requestDisallowInterceptTouchEvent(false);
                }
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                postDelayed(this, 100L);
                mEditMode = false;
                final ViewGroup viewGroup = (ViewGroup) getParent();
                viewGroup.requestDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_UP:
                final ViewGroup viewGroup = (ViewGroup) getParent();
                viewGroup.requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
            default:
                break;
        }
        if (mEditMode) {
            postInvalidateDelayed(30L);
        }

        return true;
    }

    @Override
    public void run() {
        mEditMode = true;
        invalidate();
    }

    public int[] updateData(ChangeStatDesc data) {
        final int[] values = mValues;
        values[0] = data.iChangeMin;
        mUpdownNum[0] = values[0];
        values[1] = data.iChange09;
        values[2] = data.iChange98;
        values[3] = data.iChange87;
        values[4] = data.iChange76;
        values[5] = data.iChange65;
        values[6] = data.iChange54;
        values[7] = data.iChange43;
        values[8] = data.iChange32;
        values[9] = data.iChange21;
        values[10] = data.iChange10;
        values[11] = data.iChange00;
        values[12] = data.iChange01;
        values[13] = data.iChange12;
        values[14] = data.iChange23;
        values[15] = data.iChange34;
        values[16] = data.iChange45;
        values[17] = data.iChange56;
        values[18] = data.iChange67;
        values[19] = data.iChange78;
        values[20] = data.iChange89;
        values[21] = data.iChange90;
        values[22] = data.iChangeMax;
        mUpdownNum[22] = values[22];
        int maxValue = 0;
        for (int i = 0; i < PILLAR_COUNT; i++) {
            maxValue = Math.max(maxValue, values[i]);
            if (i > 0 && i < 11) {
                mUpdownNum[i] = values[i] + mUpdownNum[i - 1];
            }
        }
        for (int i = 21; i > 11; i--) {
            mUpdownNum[i] = values[i] + mUpdownNum[i + 1];
        }

        final int LINE_GAP = 200;
        int maxLineValue = LINE_GAP;
        while (maxLineValue < maxValue) {
            maxLineValue += LINE_GAP;
        }
        mMaxLineValue = maxLineValue;

        mHasData = true;
        invalidate();

        final int[] num = new int[2];
        num[0] = mUpdownNum[10];
        num[1] = mUpdownNum[12];
        return num;
    }

    private void initResource() {
        final int[] lineY = mLineY;
        if (lineY[0] > 0) {
            return;
        }
        final int width = getWidth();
        final int height = getHeight();
        final int chartHeight = height - mBottomTextHeight - mYLineSize * Y_LINE_COUNT - DeviceUtil.dip2px(getContext(), 5);
        mChartHeight = chartHeight;
        final int lineGap = chartHeight / (Y_LINE_COUNT - 1);
        lineY[0] = height - mBottomTextHeight;
        for (int i = 1; i < Y_LINE_COUNT; i++) {
            lineY[i] = lineY[i - 1] - lineGap;
        }

        mRightTextLeft = width - mRightTextWidth;
        mLineWidth = mRightTextLeft - DeviceUtil.dip2px(getContext(), 3);

        final int[] pillarStartX = mPillarsX;
        final int pillarLength = pillarStartX.length;
        final int pillarWidthAndGap = mRightTextLeft / pillarLength;
        final int pillarGap = pillarWidthAndGap * 7 / 15;
        mPillarWidth = pillarWidthAndGap - pillarGap;
        pillarStartX[0] = pillarWidthAndGap / 2;
        for (int i = 1; i < pillarLength; i++) {
            pillarStartX[i] = pillarStartX[i - 1] + pillarWidthAndGap;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mHasData) {
            return;
        }
        initResource();
        drawCoordinates(canvas);
        final int selectedIndex = drawPillar(canvas);
        if (mEditMode && selectedIndex > 0 && selectedIndex < PILLAR_COUNT) {
            final int touchLineX = mPillarsX[selectedIndex];
            drawTouchLine(canvas, touchLineX);
            showPopupInfo(selectedIndex, touchLineX);
            mSelectIndex = selectedIndex;
        }
    }

    private void drawCoordinates(final Canvas canvas) {
        int lineY;
        mChartPaint.setColor(mYLineColor);
        mChartPaint.setStrokeWidth(mYLineSize);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(mColorGray60);
        for (int i = 0; i < Y_LINE_COUNT; i++) {
            lineY = mLineY[i];
            canvas.drawLine(0, lineY, mLineWidth, lineY, mChartPaint);
            canvas.drawText(String.valueOf(mMaxLineValue * i / (Y_LINE_COUNT - 1)), mRightTextLeft, lineY + mTextYGap, mTextPaint);
        }
    }

    private int drawPillar(final Canvas canvas) {
        mChartPaint.setColor(0xff00ff00);
        mChartPaint.setStrokeWidth(mPillarWidth);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        final int[] pillarsX = mPillarsX;
        int startX;
        int selectedIndex = -1;
        final float touchX = mLastX;
        float minGap = Float.MAX_VALUE;
        final int maxLineValue = mMaxLineValue;
        int pillarHeight;
        final int[] values = mValues;
        for (int i = 0; i < PILLAR_COUNT; i++) {
            startX = pillarsX[i];
            pillarHeight = mChartHeight * values[i] / maxLineValue;

            if (mEditMode) {
                final float gap = Math.abs(touchX - startX);
                if (gap < minGap) {
                    minGap = gap;
                    selectedIndex = i;
                }
            }

            switch (i) {
                case 0:
                    mChartPaint.setColor(mColorGreen);
                    mTextPaint.setColor(mColorGreen);
                    canvas.drawText("跌", startX, mLineY[0] + mTextSize, mTextPaint);
                    canvas.drawText("停", startX, mLineY[0] + mTextSize * 2, mTextPaint);
                    break;
                case 6:
                    mTextPaint.setColor(mColorGreen);
                    canvas.drawText("-5%", startX, mLineY[0] + mTextSize, mTextPaint);
                    break;
                case 11:
                    mChartPaint.setColor(mColorGray);
                    mTextPaint.setColor(mColorGray);
                    canvas.drawText("0%", startX, mLineY[0] + mTextSize, mTextPaint);
                    break;
                case 12:
                    mChartPaint.setColor(mColorRed);
                    break;
                case 16:
                    mTextPaint.setColor(mColorRed);
                    canvas.drawText("5%", startX, mLineY[0] + mTextSize, mTextPaint);
                    break;
                case 22:
                    mTextPaint.setColor(mColorRed);
                    canvas.drawText("涨", startX, mLineY[0] + mTextSize, mTextPaint);
                    canvas.drawText("停", startX, mLineY[0] + mTextSize * 2, mTextPaint);
                    break;
                default:
                    break;
            }
            canvas.drawLine(startX,  mLineY[0], startX, mLineY[0] - pillarHeight, mChartPaint);
        }
        return selectedIndex;
    }

    private void drawTouchLine(final Canvas canvas, final int touchLineX) {
        mChartPaint.setColor(0xffffae00);
        mChartPaint.setStrokeWidth(mYLineSize);
        canvas.drawLine(touchLineX, 0, touchLineX, mLineY[0], mChartPaint);
    }

    private TextView getPopupTextView() {
        TextView textView = mTextView;
        if (textView == null) {
            final Context context = getContext();
            textView = new TextView(context);
            textView.setOnClickListener(this);
            textView.setPadding(10, 10, 10, 10);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(12);
            textView.setCompoundDrawablePadding(DeviceUtil.dip2px(context, 3));
            textView.setBackgroundResource(R.drawable.market_china_change_stat_text_bg);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.market_china_change_stat_arrow, 0);
            mParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mParams.topMargin = DeviceUtil.dip2px(context, 5);
            mTextView = textView;
        }
        return textView;
    }

    private void showPopupInfo(final int selectedIndex, final int touchLineX) {
        final TextView textView = getPopupTextView();
        if (touchLineX > getWidth() / 2) {
            mParams.leftMargin = 0;
            mParams.rightMargin = getWidth() - touchLineX + mTextMargin;
            mParams.gravity = Gravity.RIGHT;
        } else {
            mParams.leftMargin = touchLineX + mTextMargin;
            mParams.rightMargin = 0;
            mParams.gravity = Gravity.LEFT;
        }
        if (textView.getParent() == null) {
            addView(textView, mParams);
        } else {
            textView.setLayoutParams(mParams);
        }
        String text = "";
        if (selectedIndex == 0) {
            text = String.format("跌停：共%s只", mValues[selectedIndex]);
        } else if (selectedIndex > 0 && selectedIndex < 11) {
            text = String.format("跌幅：%s 共%s只\n跌幅>%s%% 股票个数%s只", mUpdownText[selectedIndex],
                    mValues[selectedIndex], (10 - selectedIndex), mUpdownNum[selectedIndex]);
        } else if (selectedIndex == 11) {
            text = String.format("平盘：共%s只", mValues[selectedIndex]);
        } else if (selectedIndex > 11 && selectedIndex < 22) {
            text = String.format("涨幅：%s 共%s只\n涨幅>%s%% 股票个数%s只", mUpdownText[selectedIndex],
                    mValues[selectedIndex], (selectedIndex - 12), mUpdownNum[selectedIndex]);
        } else if (selectedIndex == 22) {
            text = String.format("涨停：共%s只", mValues[selectedIndex]);
        }
        textView.setText(text);
    }

    private void hidePopupInfo() {
        final TextView textView = mTextView;
        if (textView != null && textView.getParent() != null) {
            removeView(textView);
        }
    }

    public void setEditMode(final boolean mode) {
        mEditMode = mode;
        if (!mode) {
            hidePopupInfo();
        }
        invalidate();
    }

    @Override
    public void onClick(View v) {
        if (v == mTextView) {
            // 点击跳转
            WebBeaconJump.showMarketChangeStat(getContext(), mSelectIndex == 0 ? 22 : mSelectIndex - 1);
        }
    }
}
