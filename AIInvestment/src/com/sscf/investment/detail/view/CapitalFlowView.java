package com.sscf.investment.detail.view;

import BEC.CapitalFlow;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.sdk.utils.DtLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqf on 2015/7/21.
 */
public class CapitalFlowView extends View {
    private static final String TAG = "CapitalFlowView";

    public static final float FULL_CIRCLE = 360.0f;
    public static final float INITIAL_START_ANGLE = -90.0f;
    public static final int DX = 20;
    public static final int DY = 5;
    public static final int OFFSET_Y = 40;
    public static final float MULTIPLE_AREA = 1.2f;
    public static final float MULTIPLE_INDICATOR = 1.0f;

    private int mIndicatorBottomWidth;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private int mInnerRadius;
    private int mOuterRadius;

    private Paint mPaint;
    private RectF mDrawingRect;

    private float mMainInPercent;
    private float mMainOutPercent;
    private float mRetailInPercent;
    private float mRetailOutPercent;

    private int mColorMainIn;
    private int mColorMainOut;
    private int mColorRetailIn;
    private int mColorRetailOut;

    private int mColorInner;

    private boolean mHasValue;

    private String mTodayString;
    private int mTodayStringColor;
    private int mTodayStringSize;
    private int mTodayStringWidth;
    private int mTodayStringHeight;

    private Paint mTextPaint;
    private TextDrawer mTextDrawer = new TextDrawer();
    private String mStrMainIn;
    private String mStrMainOut;
    private String mStrRetailIn;
    private String mStrRetailOut;

    private RectF mPercentBgRectF = new RectF();
    private List<RectF> mAreaRects = new ArrayList<RectF>();
    private List<Point> mIndicatorPoints = new ArrayList<Point>();
    private List<Point> mIndicatorCirclePoints = new ArrayList<Point>();
    private int[] mColors;

    private Path mTrianglePath = new Path();
    private int mOuterRingColor;
    private float mPercentTextSize;
    private Typeface mTypeface;
    private int mTextPadding;
    private int mTitleHeight;
    private int mValueHeight;
    private int mIndicatorGap;

    public static final int TYPE_DETAIL_TAB_PIE = 0;     // 个股详情tab的资金圆环图
    public static final int TYPE_TRANSACTION_PIE = 1;    // 交易五档/明细/资金的圆环图
    private int type = -1;

    private float [] capitalPercent = new float[8];
    private int [] capitalColor;

    public CapitalFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setType(int type) {
        this.type = type;

        Resources resources = getResources();
        if (type == TYPE_DETAIL_TAB_PIE) {
            mRadius = resources.getDimensionPixelSize(R.dimen.capital_flow_radius);
            mInnerRadius = resources.getDimensionPixelSize(R.dimen.capital_flow_radius_inner);
            mOuterRadius = resources.getDimensionPixelSize(R.dimen.capital_flow_radius_outer);
        } else if (type == TYPE_TRANSACTION_PIE) {
            mRadius = (int)resources.getDisplayMetrics().density * 40;
            mInnerRadius = (int)resources.getDisplayMetrics().density * 20;
            mOuterRadius = (int)resources.getDisplayMetrics().density * 40;

            capitalColor = new int[] {
                resources.getColor(R.color.transaction_capital_super_in),
                resources.getColor(R.color.transaction_capital_big_in),
                resources.getColor(R.color.transaction_capital_middle_in),
                resources.getColor(R.color.transaction_capital_small_in),
                resources.getColor(R.color.transaction_capital_small_out),
                resources.getColor(R.color.transaction_capital_middle_out),
                resources.getColor(R.color.transaction_capital_big_out),
                resources.getColor(R.color.transaction_capital_super_out)
            };
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        DtLog.d(TAG, "CapitalFlowView: onFinishInflate");

        Resources resources = getResources();

        mRadius = resources.getDimensionPixelSize(R.dimen.capital_flow_radius);
        mInnerRadius = resources.getDimensionPixelSize(R.dimen.capital_flow_radius_inner);
        mOuterRadius = resources.getDimensionPixelSize(R.dimen.capital_flow_radius_outer);

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);

        mTodayStringColor = ContextCompat.getColor(getContext(), R.color.default_text_color_60);
        mColorInner = ContextCompat.getColor(getContext(), R.color.default_content_bg);
        mOuterRingColor = ContextCompat.getColor(getContext(), R.color.default_background);

        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/myfont.ttf");
        mPercentTextSize = resources.getDimensionPixelSize(R.dimen.capital_flow_percent_textSize);
        mTextPadding = resources.getDimensionPixelSize(R.dimen.capital_flow_text_padding);

        mTodayString = resources.getString(R.string.capital_today);
        TextDrawer textDrawer = new TextDrawer();
        mTodayStringSize = resources.getDimensionPixelSize(R.dimen.capital_today_textSize);
        mTodayStringWidth = textDrawer.measureSingleTextWidth(mTodayString, mTodayStringSize, null);
        mTodayStringHeight = textDrawer.measureSingleTextHeight(mTodayStringSize, null);

        mTitleHeight = mTextDrawer.measureSingleTextHeight(mTodayStringSize, null);
//        mValueHeight = mTextDrawer.measureSingleTextHeight(mPercentTextSize, mTypeface) /*+ mTextPadding*/;
        mValueHeight = resources.getDimensionPixelSize(R.dimen.capital_flow_percent_height);
        mIndicatorGap = resources.getDimensionPixelSize(R.dimen.capital_flow_indicator_gap);

        mTextPaint = new Paint();
        mTextPaint.setTypeface(mTypeface);
        mTextPaint.setTextSize(mPercentTextSize);

        mColorMainIn = resources.getColor(R.color.capital_flow_main_in);
        mColorMainOut = resources.getColor(R.color.capital_flow_main_out);
        mColorRetailIn = resources.getColor(R.color.capital_flow_retail_in);
        mColorRetailOut = resources.getColor(R.color.capital_flow_retail_out);

        mColors = new int[] {mColorMainIn, mColorRetailIn, mColorRetailOut, mColorMainOut};

        mStrMainIn = resources.getString(R.string.capital_main_in);
        mStrMainOut = resources.getString(R.string.capital_main_out);
        mStrRetailIn = resources.getString(R.string.capital_retail_in);
        mStrRetailOut = resources.getString(R.string.capital_retail_out);

        mIndicatorBottomWidth = resources.getDimensionPixelSize(R.dimen.capital_flow_indicator_bottom_width);
    }

    private void initDrawingPosition() {
        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();
        mCenterX = mDrawingWidth / 2;
        mCenterY = mDrawingHeight / 2;
        mDrawingRect = new RectF(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initDrawingPosition();

        mAreaRects.clear();
        mIndicatorPoints.clear();
        mIndicatorCirclePoints.clear();

        //画底板，也即外圈的灰色背景
        mPaint.setColor(mOuterRingColor);
        canvas.drawCircle(mCenterX, mCenterY, mOuterRadius, mPaint);

        if (mHasValue) {
            if (type == TYPE_DETAIL_TAB_PIE) {
                drawSectors(canvas);
            } else if (type == TYPE_TRANSACTION_PIE) {
                draw8Sectors(canvas);
            }

            rerangeRectsIfNeed();
            rerangeRectsIfNeed();

            if (type == TYPE_DETAIL_TAB_PIE) {
                drawIndicatorTriangles(canvas);
                if (mMainInPercent != 0) {
                    drawPercentContent(canvas, mAreaRects.get(0), mMainInPercent, mStrMainIn, mColorMainIn);
                }
                if (mRetailInPercent != 0) {
                    drawPercentContent(canvas, mAreaRects.get(1), mRetailInPercent, mStrRetailIn, mColorRetailIn);
                }
                if (mRetailOutPercent != 0) {
                    drawPercentContent(canvas, mAreaRects.get(2), mRetailOutPercent, mStrRetailOut, mColorRetailOut);
                }
                if (mMainOutPercent != 0) {
                    drawPercentContent(canvas, mAreaRects.get(3), mMainOutPercent, mStrMainOut, mColorMainOut);
                }
            }
        }

        // 画内圈
        mPaint.setColor(mColorInner);
        canvas.drawCircle(mCenterX, mCenterY, mInnerRadius, mPaint);

        if (type == TYPE_DETAIL_TAB_PIE) {
            // 画中心的说明文字
            mPaint.setColor(mTodayStringColor);
            mPaint.setTextSize(mTodayStringSize);
            canvas.drawText(mTodayString, mCenterX - mTodayStringWidth / 2, mCenterY + mTodayStringHeight / 2, mPaint);
        }
    }

    /**
     * 画8个圆环区域
     * @param canvas
     */
    private void draw8Sectors(Canvas canvas) {
        float startAngle = INITIAL_START_ANGLE;
        float sweepAngle;
        for (int i = 0; i < 8; i++) {
            sweepAngle = capitalPercent[i] * FULL_CIRCLE;
            mPaint.setColor(capitalColor[i]);
            canvas.drawArc(mDrawingRect, startAngle, sweepAngle, true, mPaint);
            startAngle += sweepAngle;
        }

        mPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.font_size_10));
        float oneTextWidth = mPaint.measureText("国");
        {
            mPaint.setColor(getResources().getColor(R.color.transaction_capital_super_out));
            float y = getHeight() / 2 - mPaint.measureText("主动卖")/4;
//            float x = 0 + oneTextWidth/2;
            float x = 0;
            canvas.drawText("主", x, y, mPaint);
            canvas.drawText("动", x, y + oneTextWidth, mPaint);
            canvas.drawText("卖", x, y + 2*oneTextWidth, mPaint);
        }

        {
            mPaint.setColor(getResources().getColor(R.color.transaction_capital_super_in));
            float y = getHeight() / 2 - mPaint.measureText("主动买")/4;
//            float x = getWidth() - oneTextWidth - oneTextWidth/2 ;
            float x = getWidth() - oneTextWidth;
            canvas.drawText("主", x, y, mPaint);
            canvas.drawText("动", x, y + oneTextWidth, mPaint);
            canvas.drawText("买", x, y + 2*oneTextWidth, mPaint);
        }

    }

    private void drawSectors(Canvas canvas) {
        float startAngle = INITIAL_START_ANGLE;
        float sweepAngle;
        sweepAngle = mMainInPercent * FULL_CIRCLE;
        mPaint.setColor(mColorMainIn);
        canvas.drawArc(mDrawingRect, startAngle, sweepAngle, true, mPaint);
        Point point = getIndicatorStartPoint(startAngle, sweepAngle, MULTIPLE_AREA);
        Point pointOnCircle = getIndicatorStartPoint(startAngle, sweepAngle, MULTIPLE_INDICATOR);
        mIndicatorPoints.add(point);
        mIndicatorCirclePoints.add(pointOnCircle);
        getPercentArea(point, mMainInPercent, mStrMainIn);
        startAngle += sweepAngle;

        sweepAngle = mRetailInPercent * FULL_CIRCLE;
        mPaint.setColor(mColorRetailIn);
        canvas.drawArc(mDrawingRect, startAngle, sweepAngle, true, mPaint);
        point = getIndicatorStartPoint(startAngle, sweepAngle, MULTIPLE_AREA);
        pointOnCircle = getIndicatorStartPoint(startAngle, sweepAngle, MULTIPLE_INDICATOR);
        mIndicatorPoints.add(point);
        mIndicatorCirclePoints.add(pointOnCircle);
        getPercentArea(point, mRetailInPercent, mStrRetailIn);
        startAngle += sweepAngle;

        sweepAngle = mRetailOutPercent * FULL_CIRCLE;
        mPaint.setColor(mColorRetailOut);
        canvas.drawArc(mDrawingRect, startAngle, sweepAngle, true, mPaint);
        point = getIndicatorStartPoint(startAngle, sweepAngle, MULTIPLE_AREA);
        pointOnCircle = getIndicatorStartPoint(startAngle, sweepAngle, MULTIPLE_INDICATOR);
        mIndicatorPoints.add(point);
        mIndicatorCirclePoints.add(pointOnCircle);
        getPercentArea(point, mRetailOutPercent, mStrRetailOut);
        startAngle += sweepAngle;

        sweepAngle = mMainOutPercent * FULL_CIRCLE;
        mPaint.setColor(mColorMainOut);
        canvas.drawArc(mDrawingRect, startAngle, sweepAngle, true, mPaint);
        point = getIndicatorStartPoint(startAngle, sweepAngle, MULTIPLE_AREA);
        pointOnCircle = getIndicatorStartPoint(startAngle, sweepAngle, MULTIPLE_INDICATOR);
        mIndicatorPoints.add(point);
        mIndicatorCirclePoints.add(pointOnCircle);
        getPercentArea(point, mMainOutPercent, mStrMainOut);
    }

    private void drawIndicatorTriangles(Canvas canvas) {
        float x0 = 0, y0 = 0, x1 = 0, y1 = 0;
        for (int i = 0; i < mAreaRects.size(); i++) {
            if (i == 0 && mMainInPercent == 0 || i == 1 && mRetailInPercent == 0
                || i == 2 && mRetailOutPercent == 0 || i == 3 && mMainOutPercent == 0) {
                continue;
            }

            RectF areaRect = mAreaRects.get(i);
            int color = mColors[i];
            mTrianglePath.reset();

            float centerX = areaRect.centerX();
            float centerY = areaRect.centerY();

            if (centerX > mCenterX) {
                if (centerY < mCenterY) { //右上
                    x0 = areaRect.left;
                    y0 = areaRect.bottom;
                    x1 = x0 + mIndicatorBottomWidth;
                    y1 = y0;
                } else { //右下
                    x0 = areaRect.left;
                    y0 = areaRect.top;
                    x1 = x0 + mIndicatorBottomWidth;
                    y1 = y0;
                }
            } else {
                if (centerY < mCenterY) { //左上
                    x0 = areaRect.right;
                    y0 = areaRect.bottom;
                    x1 = x0 - mIndicatorBottomWidth;
                    y1 = y0;
                } else { //左下
                    x0 = areaRect.right;
                    y0 = areaRect.top;
                    x1 = x0 - mIndicatorBottomWidth;
                    y1 = y0;
                }
            }
            mTrianglePath.moveTo(x0, y0);
            mTrianglePath.lineTo(x1, y1);
            Point dp = getIndicatorDestPoint(new Point((int)((x0+x1)/2), (int)y0), mIndicatorCirclePoints.get(i));
            mTrianglePath.lineTo(dp.x, dp.y);
            mTrianglePath.close();
            mPaint.setColor(color);
            canvas.drawPath(mTrianglePath, mPaint);
        }
    }

    private Point getIndicatorDestPoint(Point startPoint, Point pointOnCircle) {
        Point point = new Point();
        double angle = Math.atan(Math.abs((pointOnCircle.y - startPoint.y)) / (double) Math.abs((-pointOnCircle.x + startPoint.x)));

        double deltaX = Math.cos(angle) * mIndicatorGap;
        double deltaY = Math.sin(angle) * mIndicatorGap;

        if (startPoint.x > pointOnCircle.x) {
            if (startPoint.y < pointOnCircle.y) { //右上
                point.x = (int) (deltaX + pointOnCircle.x);
                point.y = (int) (-deltaY + pointOnCircle.y);
            } else { //右下
                point.x = (int) (pointOnCircle.x + deltaX);
                point.y = (int) (pointOnCircle.y + deltaY);
            }
        } else {
            if (startPoint.y > pointOnCircle.y) { //左下
                point.x = (int) (pointOnCircle.x - deltaX);
                point.y = (int) (pointOnCircle.y + deltaY);
            } else { //左上
                point.x = (int) (pointOnCircle.x - deltaX);
                point.y = (int) (pointOnCircle.y - deltaY);
            }
        }

        return point;
    }

    private void rerangeRectsIfNeed() {
        RectF current, next;
        boolean intersect = false;
        for (int i = 0; i < mAreaRects.size() - 1; i++) {
            current = mAreaRects.get(i);
            next = mAreaRects.get(i + 1);
            do {
                intersect = false;
                if (RectF.intersects(current, next)) {
                    next.offset(DX, DY);
                    current.offset(-DX, -DY);
                    intersect = true;
                }
            } while (intersect);
        }
    }

    private void drawPercentContent(Canvas canvas, RectF rectF, float value, String title, int bgColor) {
        String percentStr = getPercentString(value);
        float titleWidth = mTextDrawer.measureSingleTextWidth(title, mPercentTextSize, null);
        float valueWidth = mTextDrawer.measureSingleTextWidth(percentStr, mPercentTextSize, mTypeface);

        float x = 0, y = 0;
        float titleX = 0, titleY = 0;
        float centerX = rectF.centerX();
        float centerY = rectF.centerY();

        titleX = rectF.left;
        x = titleX + (titleWidth - valueWidth) / 2;

        if (centerX > mCenterX) {
            if (centerY > mCenterY) { //右下
                titleY = rectF.bottom;
                y = titleY - mTitleHeight;
                mPercentBgRectF.set(titleX, y - mValueHeight, titleX + titleWidth, y);
                y -= mTextPadding;
            } else { //右上
                y = rectF.bottom;
                titleY = y - mValueHeight/* - mTextPadding*/;
                mPercentBgRectF.set(titleX, y - mValueHeight, titleX + titleWidth, y);
                y -= mTextPadding;
                titleY -= mTextPadding;
            }
        } else {
            if (centerY > mCenterY) { //左下
                titleY = rectF.bottom;
                y = rectF.top + mValueHeight;
                mPercentBgRectF.set(titleX, y - mValueHeight, titleX + titleWidth, y);
                y -= mTextPadding;
            } else { //左上
                y = rectF.bottom;
                titleY = y - mValueHeight/* - mTextPadding*/;
                mPercentBgRectF.set(titleX, y - mValueHeight, titleX + titleWidth, y);
                y -= mTextPadding;
                titleY -= mTextPadding;
            }
        }

        mPaint.setColor(bgColor);
        canvas.drawRect(mPercentBgRectF, mPaint);
        mTextPaint.setColor(Color.WHITE);
        canvas.drawText(percentStr, x, y, mTextPaint);
        mPaint.setColor(bgColor);
        mPaint.setTextSize(mPercentTextSize);
        canvas.drawText(title, titleX, titleY, mPaint);
    }

    private void getPercentArea(Point point, float value, String title) {
        RectF percentAreaRectF = new RectF();
        float titleWidth = mTextDrawer.measureSingleTextWidth(title, mPercentTextSize, null);

        int textTotalHeight = mTitleHeight + mValueHeight;
        if (point.x > mCenterX) {
            if (point.y > mCenterY) { //右下
                percentAreaRectF.set(point.x, point.y, point.x + titleWidth, point.y + textTotalHeight);
                percentAreaRectF.offset(0, OFFSET_Y);
                findValidRectPosition(percentAreaRectF, -DX, DY);
            } else { //右上
                percentAreaRectF.set(point.x, point.y - textTotalHeight, point.x + titleWidth, point.y);
                percentAreaRectF.offset(0, -OFFSET_Y);
                findValidRectPosition(percentAreaRectF, DX, DY);
            }
        } else {
            if (point.y > mCenterY) { //左下
                percentAreaRectF.set(point.x - titleWidth, point.y, point.x, point.y + textTotalHeight);
                percentAreaRectF.offset(0, OFFSET_Y);
                findValidRectPosition(percentAreaRectF, -DX, -DY);
            } else { //左上
                percentAreaRectF.set(point.x - titleWidth, point.y - textTotalHeight, point.x, point.y);
                percentAreaRectF.offset(0, -OFFSET_Y);
                findValidRectPosition(percentAreaRectF, DX, -DY);
            }
        }

        mAreaRects.add(percentAreaRectF);
    }

    private void findValidRectPosition(RectF percentAreaRectF, float dx, float dy) {
        boolean intersect = false;
        do {
            intersect = false;
            for (RectF areaRect : mAreaRects) {
                if (RectF.intersects(areaRect, percentAreaRectF)) {
                    percentAreaRectF.offset(dx, dy);
                    areaRect.offset(-dx, -dy);
                    intersect = true;
                    break;
                }
            }
        } while (intersect);
    }

    private String getPercentString(float value) {
        int percent = (int) (value * 100.0f);
        return String.valueOf(percent) + "%";
    }

    private Point getIndicatorStartPoint(float startAngle, float sweepAngle, float multiple) {
        Point point = new Point();
        double rAngle = Math.toRadians((double) (90.0F - (startAngle + sweepAngle / 2.0F)));
        double sinValue = Math.sin(rAngle);
        double cosValue = Math.cos(rAngle);
        int x1 = Math.round((float) mCenterX + (float) ((double) mRadius * multiple * sinValue));
        int y1 = Math.round((float) mCenterY + (float) ((double) mRadius * multiple * cosValue));
        point.set(x1, y1);
        return point;
    }

    public void setData(CapitalFlow capitalData) {
        float totalFund = capitalData.getFSuperin() + capitalData.getFBigin() + capitalData.getFMidin() + capitalData.getFSmallin() +
                capitalData.getFSuperout() + capitalData.getFBigout() + capitalData.getFMidout() + capitalData.getFSmallout();

        capitalPercent[0] = getRoundedValue(capitalData.getFSuperin() / totalFund);
        capitalPercent[1] = getRoundedValue(capitalData.getFBigin() / totalFund);
        capitalPercent[2] = getRoundedValue(capitalData.getFMidin() / totalFund);
        capitalPercent[3] = getRoundedValue(capitalData.getFSmallin() / totalFund);

        capitalPercent[4] = getRoundedValue(capitalData.getFSmallout() / totalFund);
        capitalPercent[5] = getRoundedValue(capitalData.getFMidout() / totalFund);
        capitalPercent[6] = getRoundedValue(capitalData.getFBigout() / totalFund);
        capitalPercent[7] = getRoundedValue(capitalData.getFSuperout() / totalFund);

        if (type == TYPE_DETAIL_TAB_PIE) {
            mMainInPercent = capitalPercent[0] + capitalPercent[1];
            mMainOutPercent = capitalPercent[6] + capitalPercent[7];
            mRetailInPercent = capitalPercent[2] + capitalPercent[3];
            mRetailOutPercent = capitalPercent[4] + capitalPercent[5];

            capitalPercent[0] = mMainInPercent;
            capitalPercent[1] = mRetailInPercent;
            capitalPercent[2] = mRetailInPercent;
            capitalPercent[3] = mRetailOutPercent;
        }

        mHasValue = true;
        invalidate();
    }

    private float getRoundedValue(float value) {
        return Math.round(value * 100) / 100f;
    }
}
