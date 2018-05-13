package com.sscf.investment.web.sdk.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.web.sdk.R;

import java.util.Iterator;
import java.util.LinkedList;

public final class VoiceVolumeAnimView extends View {

    public static final int DIRECTION_LEFT_TO_RIGHT = 1;
    public static final int DIRECTION_RIGHT_TO_LEFT = 2;
    private int mDirection;
    private int mMaxVolumn;
    private int mMaxRectHeight;
    private int mMinRectHeight;
    private final int mRectWidth; // 一个波的宽度
    private int mRectSize; // 波的个数
    private final int mGap;
    private final Rect mRect;

    private LinkedList<Integer> mVolumns;
    private final Paint mPaint;

    public VoiceVolumeAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final Resources res = context.getResources();
        mRectWidth = res.getDimensionPixelSize(R.dimen.voice_volume_anim_view_rect_width);
        mGap = res.getDimensionPixelSize(R.dimen.voice_volume_anim_view_gap);
        mPaint = new Paint();
        mPaint.setColor(res.getColor(R.color.actionbar_bg_color));
        mMaxRectHeight = res.getDimensionPixelSize(R.dimen.voice_volume_anim_view_rect_max_height);
        mMinRectHeight = res.getDimensionPixelSize(R.dimen.voice_volume_anim_view_rect_min_height);
        mRect = new Rect();
    }

    public void setMaxVolumn(final int maxVolumn) {
        mMaxVolumn = maxVolumn;
    }

    /**
     * ui线程调用
     * @param volumn
     */
    public void setVolumn(final int volumn) {
        if (mVolumns != null) {
            mVolumns.addFirst(volumn);
            mVolumns.removeLast();
            invalidate();
        }
    }

    public void setDirection(final int direction) {
        mDirection = direction;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMaxVolumn <= 0 || mDirection <= 0) {
            return;
        }

        initParams();

        switch (mDirection) {
            case DIRECTION_LEFT_TO_RIGHT:
                drawLeftToRight(canvas);
                break;
            case DIRECTION_RIGHT_TO_LEFT:
                drawRightToLeft(canvas);
                break;
            default:
                break;
        }
    }

    private void drawRightToLeft(final Canvas canvas) {
        final int right = getWidth() - getPaddingRight();
        final Iterator<Integer> it = mVolumns.iterator();
        final Rect rect = mRect;
        final int height = getHeight();
        rect.set(right - mRectWidth, 0, right, 0);
        final int offsetX = mGap + mRectWidth;
        while (it.hasNext()) {
            final int volumn = it.next();
            final int rectHeight = volumn * mMaxRectHeight / mMaxVolumn + mMinRectHeight;
            rect.top = (height - rectHeight) / 2;
            rect.bottom = (height + rectHeight) / 2;
            canvas.drawRect(rect, mPaint);
            rect.offset(-offsetX, 0);
        }
    }

    private void drawLeftToRight(final Canvas canvas) {
        final int left = getPaddingLeft();
        final Iterator<Integer> it = mVolumns.iterator();
        final Rect rect = mRect;
        final int height = getHeight();
        rect.set(left, 0, left + mRectWidth, 0);
        final int offsetX = mGap + mRectWidth;
        while (it.hasNext()) {
            int volumn = it.next();
            final int rectHeight = volumn * mMaxRectHeight / mMaxVolumn + mMinRectHeight;
            rect.top = (height - rectHeight) / 2;
            rect.bottom = (height + rectHeight) / 2;
            canvas.drawRect(rect, mPaint);
            rect.offset(offsetX, 0);
        }
    }

    private void initParams() {
        if (mRectSize == 0) {
            final int width = getWidth();
            final int size = width / (mRectWidth + mGap);
            mRectSize = size;
            reset();
        }
    }

    public void reset() {
        if (mVolumns == null) {
            mVolumns = new LinkedList<Integer>();
        } else {
            mVolumns.clear();
        }
        final int size = mRectSize;
        for (int i = 0; i < size; i++) {
            mVolumns.add(0);
        }
    }
}
