package com.sscf.investment.scan.view;

import android.animation.ValueAnimator;
import android.graphics.*;

import com.sscf.investment.component.scan.R;
import com.sscf.investment.scan.camera.CameraManager;
import com.google.zxing.ResultPoint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

    private static final long ANIMATION_DELAY = 25L;
    private static final int OPAQUE = 0xFF;

    private final Paint paint;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;

    private final Rect mFrameRect;
    private final Rect mLaserSrcRect;
    private final Rect mLaserDistRect;
    private final Bitmap mLaserBitmap;
    private int mLaserVerticalOffset;
    private final int mAngleWidth;
    private final int mAngleHeight;
    private final int mAngleColor;

    private ValueAnimator mValueAnimator;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint();
        final Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);

        mFrameRect = new Rect();
        mLaserSrcRect = new Rect();
        mLaserDistRect = new Rect();
        mLaserBitmap = BitmapFactory.decodeResource(resources, R.drawable.scan_laser);

        mAngleWidth = resources.getDimensionPixelSize(R.dimen.viewfinder_angle_width);
        mAngleHeight = resources.getDimensionPixelSize(R.dimen.viewfinder_angle_height);
        mAngleColor = 0xfffd7736;
    }

    private void init(final int width, final int height) {
        final Rect frameRect = CameraManager.get().getFramingRect();
        mFrameRect.set(frameRect);
        final Rect laserSrcRect = mLaserSrcRect;
        final Rect laserDistRect = mLaserDistRect;

        final Resources resources = getResources();
        final int frameSize = resources.getDimensionPixelSize(R.dimen.viewfinder_view_size);

//        final int frameOffset = 0;
//        frameRect.left = (width - frameSize) / 2;
//        frameRect.right = frameRect.left + frameSize;
//        frameRect.top = (height - frameSize) / 2 - frameOffset;
//        frameRect.bottom = frameRect.top + frameSize - frameOffset;

        final int laserHeight = resources.getDimensionPixelSize(R.dimen.viewfinder_scan_laser_height);
        laserSrcRect.right = frameSize;
        laserSrcRect.bottom = laserHeight;

        laserDistRect.top = frameRect.top + mLaserVerticalOffset;
        laserDistRect.bottom = laserDistRect.top + laserSrcRect.bottom;
        laserDistRect.left = frameRect.left;
        laserDistRect.right = frameRect.right;

        mLaserVerticalOffset = frameRect.top;

        mValueAnimator = ValueAnimator.ofInt(frameRect.top, frameRect.bottom - laserHeight);
        mValueAnimator.setStartDelay(200L);
        mValueAnimator.setDuration(2000L);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLaserVerticalOffset = (Integer) animation.getAnimatedValue();
            }
        });
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.start();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (getVisibility() != VISIBLE) {
            return;
        }

        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        final Rect frameRect = mFrameRect;
        final Rect laserSrcRect = mLaserSrcRect;
        final Rect laserDistRect = mLaserDistRect;
        if (frameRect.left == 0) {
            init(width, height);
        }

        laserDistRect.offsetTo(laserDistRect.left, mLaserVerticalOffset);

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frameRect.top, paint);
        canvas.drawRect(0, frameRect.top, frameRect.left, frameRect.bottom, paint);
        canvas.drawRect(frameRect.right, frameRect.top, width, frameRect.bottom, paint);
        canvas.drawRect(0, frameRect.bottom, width, height, paint);

        draw4Angle(canvas);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frameRect.left, frameRect.top, paint);
        } else {
            canvas.drawBitmap(mLaserBitmap, laserSrcRect, laserDistRect, paint);

            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY, frameRect.left, frameRect.top, frameRect.right, frameRect.bottom);
        }
    }

    private void draw4Angle(Canvas canvas) {
        final Rect frameRect = mFrameRect;
        paint.setColor(mAngleColor);

        // left-top angle
        canvas.drawRect(frameRect.left, frameRect.top, frameRect.left + mAngleWidth, frameRect.top +  + mAngleHeight, paint);
        canvas.drawRect(frameRect.left, frameRect.top, frameRect.left + mAngleHeight, frameRect.top +  + mAngleWidth, paint);

        // right-top angle
        canvas.drawRect(frameRect.right - mAngleWidth, frameRect.top, frameRect.right, frameRect.top + mAngleHeight, paint);
        canvas.drawRect(frameRect.right - mAngleHeight, frameRect.top, frameRect.right, frameRect.top + mAngleWidth, paint);

        // left-bottom angle
        canvas.drawRect(frameRect.left, frameRect.bottom - mAngleHeight, frameRect.left + mAngleWidth, frameRect.bottom, paint);
        canvas.drawRect(frameRect.left, frameRect.bottom - mAngleWidth, frameRect.left + mAngleHeight, frameRect.bottom, paint);

        // right-bottom angle
        canvas.drawRect(frameRect.right - mAngleWidth, frameRect.bottom - mAngleHeight, frameRect.right, frameRect.bottom, paint);
        canvas.drawRect(frameRect.right - mAngleHeight, frameRect.bottom - mAngleWidth, frameRect.right, frameRect.bottom, paint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
    }

}
