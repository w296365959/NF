package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.detail.entity.PillarChangeEntity;
import com.sscf.investment.utils.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liqf on 2015/8/26.
 */
public class PolylineChangeView extends View {
    public static final int NOT_INITED = -1;
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

    private int mDeltaTextMarginBottom;

    public PolylineChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.PillarChangeView);
        mPolylineColor = array.getColor(R.styleable.PillarChangeView_polylineColor, Color.GRAY);
        array.recycle();

        initResources();
    }

    private void initResources() {
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/myfont.ttf");

        Resources resources = getResources();
        mTextMargin = resources.getDimensionPixelSize(R.dimen.pillar_text_margin);
        mPillarBottomOffset = resources.getDimensionPixelSize(R.dimen.pillar_bottom_offset);
        mContentWidth = mDrawingWidth;
        mContentStartOffset = resources.getDimensionPixelSize(R.dimen.pillar_start_offset);
        mPillarBottomLineHeight = resources.getDimensionPixelSize(R.dimen.pillar_bottom_line_height);
        mPillarWidth = resources.getDimensionPixelSize(R.dimen.capital_flow_change_histogram_width);
        mDeltaTextMarginBottom = resources.getDimensionPixelSize(R.dimen.delta_text_marginBottom);

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

        //两边留出相等的边距，且柱子宽度固定，剩下的gap宽度由计算得出
        mPillarGapWidth = (mDrawingWidth - 2 * mContentStartOffset - 5 * mPillarWidth) / 4;

        drawBaseLine(canvas);

        if (mEntityList == null || mEntityList.size() == 0) {
            return;
        }

        drawDeltaLines(canvas);
        drawPillarTimes(canvas);
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

            //画大圆圈上面的数字值
            String valueStr = StringUtil.getFormatedFloat(originValue);
            float textWidth = mTextDrawer.measureSingleTextWidth(valueStr, mDeltaTextSize, mTypeface);
            canvas.drawText(valueStr, cx - textWidth / 2, cy - mDeltaTextMarginBottom, mDeltaTextPaint);

            //画大圆圈内部的小圆圈
            mDeltaPaint.setColor(mBgColor);
            canvas.drawCircle(cx, cy, mDeltaCircleRadius - mDeltaStrokeWidth, mDeltaPaint);

            //画从上一个圈到当前这个圈的连接线
            if (lastX != NOT_INITED && lastY != NOT_INITED) {
                getDrawingPoints(lastX, lastY, cx, cy, startPoint, endPoint);
                mDeltaPaint.setColor(mDeltaColor);
                canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, mDeltaPaint);
            }

            lastX = cx;
            lastY = cy;

            itemStartX += itemWidth;
        }
    }

    private String getPercentString(float value) {
        return StringUtil.getFormatedFloat(value) + "%";
    }

    private void drawBaseLine(Canvas canvas) {
        float startX = 0;
        float startY = mPillarBottomOffset;
        float stopX = startX + mContentWidth;
        float stopY = mPillarBottomOffset;

        mPaint.setColor(mDividerColor);
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
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
