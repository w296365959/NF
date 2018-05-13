package com.sscf.investment.component.ui.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;
import com.sscf.investment.component.ui.R;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import java.lang.reflect.Method;

/**
 * Created by liqf on 2015/8/24.
 */
public class SwipeBackLayout extends LinearLayout {
    private static final String TAG = SwipeBackLayout.class.getSimpleName();
    public static final int SCROLL_TO_FINISH_DURATION = 150;
    private final boolean DEBUG = false; //为true时显示本文件的log
    private final int mScreenWidth;
    private Activity mActivity;
    private View mContentView;

    private int mDragState = STATE_IDLE;

    /**
     * A view is not currently being dragged or animating as a result of a
     * fling/snap.
     */
    public static final int STATE_IDLE = 0;

    /**
     * A view is currently being dragged. The position is currently changing as
     * a result of user input or simulated user input.
     */
    public static final int STATE_DRAGGING = 1;

    /**
     * A view is currently settling into place as a result of a fling or
     * predefined non-interactive motion.
     */
    public static final int STATE_SETTLING = 2;

    //手指向右滑动时的最小速度
    private int mMinSpeedToTriggleFinish;

    //手指向右滑动时的最小距离
    private int mMinDistanceToStartScroll;

    //UP事件时最少要移动了这段距离才会finish
    private int mMinDistanceToFinish;

    //移动距离超过该值才触发对触摸事件的拦截,进入STATE_DRAGGING状态
    private int mMinDistanceToTriggerScroll;

    //垂直方向移动超过了此最大距离水平就不移动
    private int mMaxVerticalDistance;

    //在刚边缘区域内触发down事件则被认为是边缘滑动
    private float mMinDistanceToTriggerEdgeTracking;

  //记录手指按下时的横坐标。
    private float xDown;
    private float yDown;

    //记录手指移动时的横坐标。
    private float xMove;
    private float yMove;

    private float lastXMove;
    private float lastYMove;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    private ViewGroup mDecorView;
    private int initAlpha;
    private boolean mTrackingEdge;

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScreenWidth = DeviceUtil.getScreenWidth(context.getApplicationContext());
        mMinDistanceToFinish = mScreenWidth / 5;

        Resources resources = getResources();
        mMinDistanceToStartScroll = resources.getDimensionPixelSize(R.dimen.min_distance_to_start_scroll);
        mMaxVerticalDistance = resources.getDimensionPixelSize(R.dimen.max_distance_to_lock_scroll);
        mMinSpeedToTriggleFinish = resources.getDimensionPixelSize(R.dimen.min_speed_to_trigger_finish);
        mMinDistanceToTriggerEdgeTracking = resources.getDimensionPixelSize(R.dimen.min_distance_to_trigger_edge_tracking);
        mMinDistanceToTriggerScroll = resources.getDimensionPixelSize(R.dimen.min_distance_to_trigger_scroll);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        if (DEBUG) {
            DtLog.d(TAG, "onInterceptTouchEvent: action is " + action);
        }

        createVelocityTracker(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();

                if (DEBUG) {
                    DtLog.d(TAG, "onInterceptTouchEvent: xDown is " + xDown + ", yDown is " + yDown);
                }

//                if (mTrackingEdge) {
//                    if (xDown < mMinDistanceToTriggerEdgeTracking) {
//                        mDragState = STATE_DRAGGING;
//                    }
//                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                judgetInterception(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }

        if (DEBUG) {
            DtLog.d(TAG, "onInterceptTouchEvent: mDragState is " + mDragState);
        }
        return mDragState == STATE_DRAGGING;
    }

    private void judgetInterception(MotionEvent event) {
        if (mDragState == STATE_DRAGGING) {
            return;
        }

        xMove = event.getRawX();
        yMove = event.getRawY();
        //活动的距离
        int distanceX = (int) (xMove - xDown);
        int distanceY = Math.abs((int) (yMove - yDown));

        //获取瞬时速度
        int xSpeed = getScrollVelocity();
        if (DEBUG) {
            DtLog.d(TAG, "xSpeed = " + xSpeed);
            DtLog.d(TAG, "distanceX = " + distanceX);
        }

        //当检测到向右滑动且Y轴方向并没有开始明显的滑动时，进入DRAGGING状态，拦截掉给子View的触摸事件，后续触摸事件交由自身的onTouchEvent处理
        if (distanceX > mMinDistanceToTriggerScroll && distanceY < mMaxVerticalDistance) {
            if (mTrackingEdge) {
                if (xDown < mMinDistanceToTriggerEdgeTracking) {
                    mDragState = STATE_DRAGGING;
                }
            } else {
                mDragState = STATE_DRAGGING;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        if (DEBUG) {
            DtLog.d(TAG, "onTouchEvent action is " + action);
            DtLog.d(TAG, "onTouchEvent action is x " + event.getRawX());
            DtLog.d(TAG, "onTouchEvent action is y " + event.getRawY());
        }

        createVelocityTracker(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
//                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                judgetInterception(event);

                xMove = event.getRawX();
                yMove = event.getRawY();
                //活动的距离
                int distanceX = (int) (xMove - xDown);
                int distanceY = (int) Math.abs(yMove - yDown);

                //获取瞬时速度
                int xSpeed = getScrollVelocity();
                if (DEBUG) {
                    DtLog.d(TAG, "xSpeed = " + xSpeed);
                }
                //当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity
                if (xSpeed > mMinSpeedToTriggleFinish) {
                    if (mDragState == STATE_IDLE) {
                        if (xMove - lastXMove > 0) {
                            mDragState = STATE_SETTLING;
                            scrollToFinish();
                        } else {
                            mContentView.animate().translationX(0).setDuration(100);
                            mDragState = STATE_IDLE;
                        }
                    }
                } else {
                    if (mDragState == STATE_DRAGGING) {
                        distanceX -= mMinDistanceToStartScroll;
                        distanceX = distanceX >= 0 ? distanceX : 0;
                        getBackground().setAlpha((int) ((float) (mScreenWidth - distanceX) / mScreenWidth * 255));
                        mContentView.setTranslationX(distanceX);
                    }
                }

                lastXMove = xMove;
                lastYMove = yMove;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();

                int xUp = (int) event.getRawX();
                int yUp = (int) event.getRawY();
                int xDistance = (int) (xUp - xDown);
                int yDistance = (int) Math.abs(yUp - yDown);
                if (DEBUG) {
                    DtLog.d(TAG, "xUp = " + xUp);
                }
                if (!(mDragState == STATE_SETTLING)) {
                    if (xDistance > mMinDistanceToFinish) {
                        scrollToFinish();
                        mDragState = STATE_SETTLING;
                    } else {
                        mContentView.animate().translationX(0);
                        mDragState = STATE_IDLE;
                    }
                }
                break;
            default:
                break;
        }

        if (DEBUG) {
            DtLog.d(TAG, "onTouchEvent mDragState is " + mDragState);
        }
        return true;
    }

    private void scrollToFinish() {
        ColorDrawable bgDrawable = (ColorDrawable) getBackground();
        initAlpha = bgDrawable.getAlpha();

        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(initAlpha, 0).setDuration(SCROLL_TO_FINISH_DURATION);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (DEBUG) {
                    DtLog.d(TAG, "alpha value is " + value);
                }
                getBackground().setAlpha((int) value);
            }
        });
        alphaAnimator.start();

        ViewPropertyAnimator animator = mContentView.animate().translationX(mScreenWidth).setDuration(SCROLL_TO_FINISH_DURATION);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mActivity.finish();
                mActivity.overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        /*animator.setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                DtLog.i(TAG, ((Float) animation.getAnimatedValue()).toString());
                float value = (float) animation.getAnimatedValue();
//                getBackground().setAlpha((int) ((1 - value) * initAlpha));
            }
        });*/
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     *
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    public void attachToActivity(Activity activity) {
        mActivity = activity;
        convertActivityToTranslucent(mActivity);

        mDecorView = (ViewGroup) activity.getWindow().getDecorView();
        mDecorView.setBackgroundDrawable(null);
        final View decorChild = mDecorView.getChildAt(0);
        decorChild.setBackgroundColor(Color.TRANSPARENT);
        mDecorView.removeView(decorChild);

        addView(decorChild);
        setContentView(decorChild);
        setBackgroundColor(getResources().getColor(R.color.black_20));
        mDecorView.addView(this);
    }

    public static void attachSwipeLayout(final Activity activity) {
        SwipeBackLayout mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(activity).inflate(
            R.layout.swipeback_layout, null);
        mSwipeBackLayout.attachToActivity(activity);

        DeviceUtil.enableTranslucentStatus(activity, ContextCompat.getColor(activity, R.color.actionbar_bg));
    }

    public static void attachSwipeLayout(final Activity activity, final boolean triggerFromEdge) {
        SwipeBackLayout mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(activity).inflate(
            R.layout.swipeback_layout, null);
        mSwipeBackLayout.attachToActivity(activity);
        mSwipeBackLayout.setTriggerFromEdge(triggerFromEdge);

        DeviceUtil.enableTranslucentStatus(activity, ContextCompat.getColor(activity, R.color.actionbar_bg));
    }

    private void setTriggerFromEdge(boolean triggerFromEdge) {
        mTrackingEdge = triggerFromEdge;
    }

    /**
     * Set up contentView which will be moved by user gesture
     *
     * @param view
     */
    private void setContentView(View view) {
        mContentView = view;
    }

    public static void convertActivityToTranslucent(Activity activity) {
        try {
            Class[] t = Activity.class.getDeclaredClasses();
            Class translucentConversionListenerClazz = null;
            Class[] method = t;
            int len$ = t.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Class clazz = method[i$];
                if(clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                    break;
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Method var8 = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz, ActivityOptions.class);
                var8.setAccessible(true);
                var8.invoke(activity, new Object[]{null, null});
            } else {
                Method var8 = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz);
                var8.setAccessible(true);
                var8.invoke(activity, new Object[]{null});
            }
        } catch (Throwable e) {
        }

    }
}
