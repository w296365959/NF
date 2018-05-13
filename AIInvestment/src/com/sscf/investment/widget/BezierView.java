package com.sscf.investment.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.sdk.utils.DeviceUtil;

/**
 * Created by LEN on 2018/4/27.
 */

public class BezierView extends View implements Handler.Callback{

    private Paint mPaint;
    private int centerX, centerY;

    private PointF start, medium, end, control11, control12, control21,control22, control31, control32;

    private Handler mHandler;

    private static final int UPDATE_ANIMATION = 1;

    public BezierView(Context context) {
        super(context);
        init();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(60);

        start = new PointF(0,0);
        medium = new PointF();
        end = new PointF(0,0);
        control11 = new PointF(0,0);
        control12 = new PointF();
        mHandler = new Handler(this);

        control21 = new PointF();
        control22 = new PointF();
        control31 = new PointF();
        control32 = new PointF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initAnimaParam();
        centerX = w/2;
        centerY = h/2;

        // 初始化数据点和控制点的位置
        start.x = 0;
        start.y = centerY;

        medium.x = centerX;
        medium.y = centerY;

        end.x = w;
        end.y = centerY;
        control11.x = w * 1 / 4;
        control21.x = w * 1 / 4;
        control31.x = w * 1 / 4;
        control12.x = w * 3 / 4;
        control22.x = w * 3 / 4;
        control32.x = w * 3 / 4;

        control11.y = 0;
        control21.y = h * 1 / 7;
        control31.y = h * 2 / 7;

        control12.y = h;
        control22.y = h * 6 / 7;
        control32.y = h * 5 / 7;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.parseColor("#eeeeee"));
        mPaint.setStrokeWidth(DeviceUtil.dip2px(getContext(), 1));
        mPaint.setAntiAlias(true);



        mPaint.setAlpha(150);
        Path path1 = new Path();
        path1.moveTo(start.x,start.y);
        path1.quadTo(control11.x,control11.y ,medium.x,medium.y);
        path1.quadTo(control12.x, control12.y, end.x, end.y);
        canvas.drawPath(path1, mPaint);

        mPaint.setAlpha(100);
        Path path2 = new Path();
        path2.moveTo(start.x,start.y + 10);
        path2.quadTo(control21.x,control21.y,medium.x,medium.y);
        path2.quadTo(control22.x, control22.y, end.x, end.y - 10);
        canvas.drawPath(path2, mPaint);

        mPaint.setAlpha(50);
        Path path3 = new Path();
        path3.moveTo(start.x,start.y + 20);
        path3.quadTo(control31.x, control31.y, medium.x,medium.y);
        path3.quadTo(control32.x, control32.y, end.x, end.y - 20);
        canvas.drawPath(path3, mPaint);

    }

    public void startAnimation() {
        mHandler.removeMessages(UPDATE_ANIMATION);
        mHandler.sendMessage(mHandler.obtainMessage(UPDATE_ANIMATION));

    }

    public void stopAnimation() {
        mHandler.removeMessages(UPDATE_ANIMATION);
    }

    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        switch (what){
            case UPDATE_ANIMATION:
                if (oritation1 == 0) {
                    control11.y += moveHeight;
                    control12.y -= moveHeight;
                }else {
                    control11.y -= moveHeight;
                    control12.y += moveHeight;
                }

                if (oritation2 == 0) {
                    control21.y += moveHeight;
                    control22.y -= moveHeight;
                }else {
                    control21.y -= moveHeight;
                    control22.y += moveHeight;
                }

                if (oritation3 == 0) {
                    control31.y += moveHeight;
                    control32.y -= moveHeight;
                }else {
                    control31.y -= moveHeight;
                    control32.y += moveHeight;
                }

                if (oritation1 == 0 && control11.y >= getHeight()) {
                    oritation1 = 1;
                }else if (oritation1 == 1 && control11.y <= 0) {
                    oritation1 = 0;
                }

                if (oritation2 == 0 && control21.y >= getHeight()) {
                    oritation2 = 1;
                }else if (oritation2 == 1 && control21.y <= 0) {
                    oritation2 = 0;
                }

                if (oritation3 == 0 && control31.y >= getHeight()) {
                    oritation3 = 1;
                }else if (oritation3 == 1 && control31.y <= 0) {
                    oritation3 = 0;
                }
                invalidate();
                mHandler.sendEmptyMessageDelayed(UPDATE_ANIMATION, 50);
                break;
        }
        return true;
    }

    private int moveHeight;
    private int oritation1;//0 是向下滑动
    private int oritation2;
    private int oritation3;

    private void initAnimaParam() {
        oritation1 = 0;
        moveHeight = (int) (getHeight() / 5000f * 50f);//3s钟从上到下，50ms动一次
    }
}
