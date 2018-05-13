package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.detail.entity.PillarChangeEntity;
import com.sscf.investment.sdk.utils.DtLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import BEC.E_SEASON_TYPE;
import BEC.SeasonOperatingRevenue;

/**
 * Created by liqf on 2015/7/23.
 */
public class PillarChangeView extends View {
    public static final int NOT_INITED = -1;
    public static final float MAX_PILLAR_HEIGHT_RATIO = 0.9f;
    private final int mPolylineColor;
    private int mDrawingWidth;
    private int mDrawingHeight;
    private int mContentStartOffset;
    private int mPillarBottomOffset;
    private int mContentWidth;
    private int mPillarBottomLineHeight;
    private Paint mPaint;
    private List<PillarChangeEntity> mEntityList;
    private int mPillarWidth;
    private int mPillarColorPlus;
    private int mPillarColorMinus;
    private TextDrawer mTextDrawer;
    private int mTimeTextSize;
    private int mTimeTextColor;
    private int mDeltaCircleRadius;
    private Paint mDeltaPaint;
    private Paint mDeltaTextPaint;
    private int mDeltaTextSize;
    private int mDeltaColor;
    private int mDeltaStrokeWidth;
    private int mDividerColor;
    private int mBgColor;
    private int mPillarGapWidth;
    private Typeface mTypeface;
    private int mTextMargin = 6;

    //反映柱体穿过的最大最小值
    private float mMinValue;
    private float mMaxValue;
    private float mMinDelta;
    private float mMaxDelta;

    private float mSegmentDividerHeight;
    private float mMinDrawingHeight;

    public PillarChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.PillarChangeView);
        mPillarColorPlus = array.getColor(R.styleable.PillarChangeView_pillarColorPlus, Color.GRAY);
        mPillarColorMinus = array.getColor(R.styleable.PillarChangeView_pillarColorMinus, Color.GRAY);
        mPolylineColor = array.getColor(R.styleable.PillarChangeView_polylineColor, Color.GRAY);
        array.recycle();

        initResources();
    }

    private void initResources() {
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/myfont.ttf");

        Resources resources = getResources();
        mTextMargin = resources.getDimensionPixelSize(R.dimen.pillar_text_margin);
        mPillarBottomOffset = resources.getDimensionPixelSize(R.dimen.pillar_bottom_offset);
        mContentStartOffset = resources.getDimensionPixelSize(R.dimen.pillar_start_offset);
        mPillarBottomLineHeight = resources.getDimensionPixelSize(R.dimen.pillar_bottom_line_height);
        mPillarWidth = resources.getDimensionPixelSize(R.dimen.capital_flow_change_histogram_width);

        mSegmentDividerHeight = resources.getDimensionPixelSize(R.dimen.pillar_segment_divider_height);
        mMinDrawingHeight = resources.getDimensionPixelSize(R.dimen.pillar_min_drawing_height);

        mTimeTextSize = resources.getDimensionPixelSize(R.dimen.font_size_10);
        TypedArray a = getContext().obtainStyledAttributes(new int[] {
            R.attr.stock_detail_finance_incoming_polyline_color,
        });
        mDeltaColor = a.getColor(0, Color.GRAY);
        a.recycle();
        mTimeTextColor = ContextCompat.getColor(getContext(), R.color.default_text_color_60);
        mBgColor = ContextCompat.getColor(getContext(), R.color.default_content_bg);
        mDividerColor = ContextCompat.getColor(getContext(), R.color.list_divider);

        mPaint = new Paint();
        mPaint.setStrokeWidth(mPillarBottomLineHeight);
        mPaint.setTypeface(mTypeface);

        mDeltaStrokeWidth = resources.getDimensionPixelSize(R.dimen.pillar_delta_stroke_width);
        mDeltaPaint = new Paint();
        mDeltaPaint.setColor(mDeltaColor);
        mDeltaPaint.setStyle(Paint.Style.STROKE);
        mDeltaPaint.setStrokeWidth(mDeltaStrokeWidth);
        mDeltaCircleRadius = resources.getDimensionPixelSize(R.dimen.pillar_delta_circle_radius);

        mDeltaTextSize = resources.getDimensionPixelSize(R.dimen.pillar_delta_value_textSize);
        mDeltaTextPaint = new Paint();
        mDeltaTextPaint.setColor(mPolylineColor);
        mDeltaTextPaint.setTextSize(mDeltaTextSize);
        mDeltaTextPaint.setTypeface(mTypeface);

        mTextDrawer = new TextDrawer();
    }

    private void getDrawingPoints(float startX, float startY, float endX, float endY, PointF startPoint, PointF endPoint) {
        float dx0 = endX - startX;
        float dy0 = endY - startY;
        double angle = Math.atan2(dy0, dx0);

        double dx1 = mDeltaCircleRadius * Math.cos(angle);
        double dy1 = mDeltaCircleRadius * Math.sin(angle);

        startPoint.set((float) (startX + dx1), (float) (startY + dy1));
        endPoint.set((float) (endX - dx1), (float) (endY - dy1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();
        mContentWidth = mDrawingWidth;

        //两边留出相等的边距，且柱子宽度固定，剩下的gap宽度由计算得出
        mPillarGapWidth = (mDrawingWidth - 2 * mContentStartOffset - 5 * mPillarWidth) / 4;

        drawBaseLine(canvas);

        if (mEntityList == null || mEntityList.size() == 0) {
            return;
        }

        drawPillars(canvas);
        drawPillarTimes(canvas);

        drawDeltaLines(canvas);
    }

    private void drawDeltaLines(Canvas canvas) {
        int itemWidth = mPillarWidth + mPillarGapWidth;
        int itemStartX = mContentStartOffset;

        List<Float> originValues = new ArrayList<Float>();
        for (PillarChangeEntity entity : mEntityList) {
            float delta = entity.getDelta();
            originValues.add(delta);
        }
        float max = Collections.max(originValues);
        float min = Collections.min(originValues);
        float range = max - min;

        String minDeltaStr = getPercentString(max - range / 0.4f * (0.3f + 0.4f));
        String maxDeltaStr = getPercentString(min + range / 0.4f * (0.3f + 0.4f));
        float textHeight = mTextDrawer.measureSingleTextHeight(mDeltaTextSize, mTypeface);

        if (mEntityList.size() > 1) {
            //画最顶上的值
            float textWidth = mTextDrawer.measureSingleTextWidth(minDeltaStr, mDeltaTextSize, mTypeface);
            float textStartX = mDrawingWidth - textWidth;
            canvas.drawText(minDeltaStr, textStartX, mPillarBottomOffset, mDeltaTextPaint);

            //画最下面的值
            textWidth = mTextDrawer.measureSingleTextWidth(maxDeltaStr, mDeltaTextSize, mTypeface);
            textStartX = mDrawingWidth - textWidth;
            canvas.drawText(maxDeltaStr, textStartX, textHeight, mDeltaTextPaint);

            //画中间的值
            String middleDeltaStr = getPercentString((min + max) / 2);
            textWidth = mTextDrawer.measureSingleTextWidth(middleDeltaStr, mDeltaTextSize, mTypeface);
            textStartX = mDrawingWidth - textWidth;
            canvas.drawText(middleDeltaStr, textStartX, mPillarBottomOffset / 2 + textHeight / 2, mDeltaTextPaint);
        } else {
            //画当前唯一的一个值
            String middleDeltaStr = getPercentString(min);
            float textWidth = mTextDrawer.measureSingleTextWidth(middleDeltaStr, mDeltaTextSize, mTypeface);
            float textStartX = mDrawingWidth - textWidth;
            //把归一化的值从0到1映射到0.3到0.7的区间
            float normalizedValue = 0.4f * 0 + 0.3f;
            float drawingHeight = normalizedValue * mPillarBottomOffset;
            canvas.drawText(middleDeltaStr, textStartX, mPillarBottomOffset - drawingHeight + textHeight / 2, mDeltaTextPaint);
        }

        //画变化的折线
        float lastX = NOT_INITED;
        float lastY = NOT_INITED;
        PointF startPoint = new PointF();
        PointF endPoint = new PointF();
        for (Float originValue : originValues) {
            float normalizedValue = range != 0 ? (originValue - min) / range : 0.0f;
            //把归一化的值从0到1映射到0.3到0.7的区间
            normalizedValue = 0.4f * normalizedValue + 0.3f;
            float drawingHeight = normalizedValue * mPillarBottomOffset;
//            DtLog.d("liqf", "drawingHeight=" + drawingHeight);

            //画大圆圈
            float cx = itemStartX + mPillarWidth / 2;
            float cy = mPillarBottomOffset - drawingHeight;
            mDeltaPaint.setColor(mDeltaColor);
            canvas.drawCircle(cx, cy, mDeltaCircleRadius, mDeltaPaint);

            //画大圆圈内部的小圆圈
            mDeltaPaint.setColor(mBgColor);
            mDeltaPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(cx, cy, mDeltaCircleRadius - mDeltaStrokeWidth, mDeltaPaint);

            //画从上一个圈到当前这个圈的连接线
            if (lastX != NOT_INITED && lastY != NOT_INITED) {
                getDrawingPoints(lastX, lastY, cx, cy, startPoint, endPoint);
                mDeltaPaint.setStyle(Paint.Style.STROKE);
                mDeltaPaint.setColor(mDeltaColor);
                canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, mDeltaPaint);
            }

            lastX = cx;
            lastY = cy;

            itemStartX += itemWidth;
        }
    }

    private String getPercentString(float value) {
        return (int)value + "%";
    }

    private void drawBaseLine(Canvas canvas) {
        float startX = 0;
        float startY = mPillarBottomOffset;
        float stopX = startX + mContentWidth;
        float stopY = mPillarBottomOffset;

        mPaint.setColor(mDividerColor);
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
    }

    private void drawPillars(Canvas canvas) {
        float maxHeight = mPillarBottomOffset;
        int itemWidth = mPillarWidth + mPillarGapWidth;
        int itemStartX = mContentStartOffset;
        mPaint.setColor(mPillarColorPlus);
        mPaint.setTextSize(mTimeTextSize);
        float textHeight = mTextDrawer.measureSingleTextHeight(mTimeTextSize, mTypeface);
        for (PillarChangeEntity entity : mEntityList) {
            float value = entity.getValue();
            float drawingHeight = getPillarDrawingHeight(maxHeight, value);
//            DtLog.d("liqf", "drawingHeight=" + drawingHeight);

            ArrayList<SeasonOperatingRevenue> seasonIncomeList = entity.getSeasonIncomeList();

            //画中线
            float dividerHeight = getPillarDrawingHeight(maxHeight, 0);
            mPaint.setColor(mDividerColor);
            canvas.drawLine(0, dividerHeight, mContentWidth, dividerHeight, mPaint);

            //画柱子
            float start1, end1, start2, end2, start3, end3, start4, end4 = 0;
            if (value >= 0) { //全部为正值的情况
                mPaint.setColor(mPillarColorPlus);
                final int seasonIncomeSize = seasonIncomeList == null ? 0 : seasonIncomeList.size();
                if (seasonIncomeSize > 0) { //画按照季报分段的柱子
                    float income1 = 0, income2 = 0, income3 = 0, income4 = 0;
                    for (SeasonOperatingRevenue seasonIncome : seasonIncomeList) {
                        int seasonType = seasonIncome.getESeasonType();
                        float income = seasonIncome.getFIncome();
                        switch (seasonType) {
                            case E_SEASON_TYPE.E_ST_FIRST:
                                income1 = income;
                                break;
                            case E_SEASON_TYPE.E_ST_SECOND:
                                income2 = income;
                                break;
                            case E_SEASON_TYPE.E_ST_THIRD:
                                income3 = income;
                                break;
                            case E_SEASON_TYPE.E_ST_FORTH:
                                income4 = income;
                                break;
                            default:
                                break;
                        }
                    }

                    if (Math.abs(income1 + income2 + income3 + income4 - value) < 2) {

//                    income1 = value / 4;
//                    income2 = value / 2;
//                    income3 = /*value / 6*/0.5f;
//                    income4 = value - income1 - income2 - income3;

                        float pillarHeight = mPillarBottomOffset - drawingHeight;
                        start1 = mPillarBottomOffset;
                        end1 = start1 - (income1 / value) * pillarHeight;
                        if (start1 - end1 < mMinDrawingHeight) {
                            end1 = start1 - mMinDrawingHeight;
                        }
                        mPaint.setAlpha(255);
                        canvas.drawRect(itemStartX, end1, itemStartX + mPillarWidth, start1, mPaint);
                        drawingHeight = end1;

                        if (income2 != 0) {
                            start2 = end1 - mSegmentDividerHeight;
                            end2 = start2 - (income2 / value) * pillarHeight;
                            if (start2 - end2 < mMinDrawingHeight) {
                                end2 = start2 - mMinDrawingHeight;
                            }
                            mPaint.setAlpha((int) (255 * 0.8f));
                            canvas.drawRect(itemStartX, end2, itemStartX + mPillarWidth, start2, mPaint);
                            drawingHeight = end2;

                            if (income3 != 0) {
                                start3 = end2 - mSegmentDividerHeight;
                                end3 = start3 - (income3 / value) * pillarHeight;
                                if (start3 - end3 < mMinDrawingHeight) {
                                    end3 = start3 - mMinDrawingHeight;
                                }
                                mPaint.setAlpha((int) (255 * 0.6f));
                                canvas.drawRect(itemStartX, end3, itemStartX + mPillarWidth, start3, mPaint);
                                drawingHeight = end3;

                                if (income4 != 0) {
                                    start4 = end3 - mSegmentDividerHeight;
                                    end4 = start4 - (income4 / value) * pillarHeight;
                                    if (start4 - end4 < mMinDrawingHeight) {
                                        end4 = start4 - mMinDrawingHeight;
                                    }
                                    mPaint.setAlpha((int) (255 * 0.4f));
                                    canvas.drawRect(itemStartX, end4, itemStartX + mPillarWidth, start4, mPaint);
                                    drawingHeight = end4;
                                }
                            }
                        }

                        mPaint.setAlpha(255);
                    } else {  //以年为单位，不按照季度分段的情况
                        if (Math.abs(dividerHeight - drawingHeight) < mMinDrawingHeight) {
                            if (drawingHeight <= dividerHeight) {
                                drawingHeight = dividerHeight - mMinDrawingHeight;
                            } else {
                                drawingHeight = dividerHeight + mMinDrawingHeight;
                            }
                        }

                        canvas.drawRect(itemStartX, drawingHeight, itemStartX + mPillarWidth, dividerHeight, mPaint);
                    }
                } else { //以年为单位，不按照季度分段的情况
                    if (Math.abs(dividerHeight - drawingHeight) < mMinDrawingHeight) {
                        if (drawingHeight <= dividerHeight) {
                            drawingHeight = dividerHeight - mMinDrawingHeight;
                        } else {
                            drawingHeight = dividerHeight + mMinDrawingHeight;
                        }
                    }

                    canvas.drawRect(itemStartX, drawingHeight, itemStartX + mPillarWidth, dividerHeight, mPaint);
                }
            } else { //有正有负的情况，目前用来画利润图
                if (Math.abs(dividerHeight - drawingHeight) < mMinDrawingHeight) {
                    if (drawingHeight <= dividerHeight) {
                        drawingHeight = dividerHeight - mMinDrawingHeight;
                    } else {
                        drawingHeight = dividerHeight + mMinDrawingHeight;
                    }
                }

                mPaint.setColor(mPillarColorMinus);
                canvas.drawRect(itemStartX, dividerHeight, itemStartX + mPillarWidth, drawingHeight, mPaint);
            }

            //画柱子上的值
            String originValueStr = String.valueOf(value);
            int textWidth = mTextDrawer.measureSingleTextWidth(originValueStr, mTimeTextSize, mTypeface);
            int textStartOffset = itemStartX - (textWidth - mPillarWidth) / 2;
            float y = value >= 0 ? drawingHeight - mTextMargin : drawingHeight + textHeight;
            canvas.drawText(originValueStr, textStartOffset, y, mPaint);

            itemStartX += itemWidth;
        }
        DtLog.d("liqf", "maxValue = " + mMaxValue);
    }

    private float getPillarDrawingHeight(float maxHeight, float value) {
        float y = 0;
        if (mMinValue > -0.00001f) { //全部是正值时，顶上留一段空白
            y = (value - mMinValue) / (mMaxValue - mMinValue) * maxHeight * MAX_PILLAR_HEIGHT_RATIO;
            return mPillarBottomOffset - y;
        } else if (mMaxValue < 0.00001f) { //全部是负值时，底下留一段空白
            y = MAX_PILLAR_HEIGHT_RATIO * maxHeight * value / mMinValue;
            return y;
        } else { //有正有负时，上下都要留一段空白(以下是解一元一次方程的结果)
            y = MAX_PILLAR_HEIGHT_RATIO * maxHeight + (1 - 2 * MAX_PILLAR_HEIGHT_RATIO) * maxHeight * (value - mMinValue) / (mMaxValue - mMinValue);
        }
        return y;
    }

    private void drawPillarTimes(Canvas canvas) {
        int itemWidth = mPillarWidth + mPillarGapWidth;

        int itemStartX = mContentStartOffset;
        int itemHeight = mTextDrawer.measureSingleTextHeight(mTimeTextSize, mTypeface);
        mPaint.setColor(mTimeTextColor);
        mPaint.setTextSize(mTimeTextSize);
        for (PillarChangeEntity entity : mEntityList) {
            String time = entity.getTime();
            int textWidth = mTextDrawer.measureSingleTextWidth(time, mTimeTextSize, mTypeface);
            int textStartOffset = itemStartX - (textWidth - mPillarWidth) / 2;
            canvas.drawText(time, textStartOffset, mPillarBottomOffset + itemHeight, mPaint);
            itemStartX += itemWidth;
        }
    }

    public void setData(List<PillarChangeEntity> entitys) {
        mEntityList = entitys;
        updateMinMaxValue(mEntityList);
        invalidate();
    }

    private void updateMinMaxValue(List<PillarChangeEntity> entityList) {
        for (PillarChangeEntity entity : entityList) {
            float value = entity.getValue();
            float delta = entity.getDelta();

            if (value > mMaxValue) {
                mMaxValue = value;
            } else if (value < mMinValue) {
                mMinValue = value;
            }
            if (mMinValue > 0) {
                mMinValue = 0;
            }
            if (mMaxValue < 0) {
                mMaxValue = 0;
            }

            if (delta > mMaxDelta) {
                mMaxDelta = delta;
            } else if (delta < mMinDelta) {
                mMinDelta = delta;
            }
        }
    }
}
