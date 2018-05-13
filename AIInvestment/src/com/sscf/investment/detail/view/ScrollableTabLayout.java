package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.DeviceUtil;

/**
 * Created by liqf on 2016/7/14.
 */
public class ScrollableTabLayout extends LinearLayout implements View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = ScrollableTabLayout.class.getSimpleName();
    public static final int SCROLL_DURATION = 300;
    public static final int TEXT_SCALE_DURATION = 100;
    private int[] mTitles;
    private String[] mTags;
    private Context mContext;
    private int mItemWidth;
    private int mIndicatorExtraWidth;

    private float mDownX;
    private float mLastX;
    private boolean mIntercept;

    private int mTotalWidth;
    private int mDisplayWidth;

    private int mTouchSlop;
    private final Scroller mScroller;

    private VelocityTracker mVelocityTracker;

    private int mCurrentTabIndex;
    private OnTabSelectionListener mTabSelectionListener;
    private int mTabHeight;

    private ScrollView mParentScrollView;

    public ScrollableTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOrientation(HORIZONTAL);

        setBackgroundResource(R.color.default_background);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout);
        mTabHeight = a.getDimensionPixelSize(R.styleable.TabLayout_tabHeight, 0);
        a.recycle();

        initResources();

        mScroller = new Scroller(context);

        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        mDisplayWidth = getMeasuredWidth();

    }

    private void initResources() {
        Resources resources = mContext.getResources();

        if (mTabHeight == 0) {
            mTabHeight = resources.getDimensionPixelSize(R.dimen.default_tab_height);
        }

        mIndicatorExtraWidth = resources.getDimensionPixelSize(R.dimen.tab_title_indicator_extraWidth);
//        mItemWidth = resources.getDimensionPixelSize(R.dimen.default_tab_item_width);
        mItemWidth = (int) (DeviceUtil.getScreenWidth(getContext()) / 4.5f);

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    public void initWithTitles(int[] titles, String[] tags) {
        mTitles = titles;
        mTags = tags;
        mTotalWidth = mItemWidth * mTitles.length;

        initChildren();
    }

    private void initChildren() {
        for (int i = 0; i < mTitles.length; i++) {
            final RelativeLayout relativeLayout = (RelativeLayout) View.inflate(mContext, R.layout.tab_layout_item, null);
            final TextView textView = (TextView) relativeLayout.findViewById(R.id.tabTitle);

            textView.setText(mTitles[i]);

            LayoutParams lp = new LayoutParams(mItemWidth, mTabHeight);
            addView(relativeLayout, lp);

            relativeLayout.setTag(mTags[i]);
            relativeLayout.setOnClickListener(this);
        }
    }

    public void setParentScrollView(ScrollView scrollView) {
        mParentScrollView = scrollView;
    }

    private void setParentScrollable(boolean scrollable) {
        if (mParentScrollView != null) {
            boolean disallowParent = !scrollable;
            mParentScrollView.requestDisallowInterceptTouchEvent(disallowParent);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        Log.d(TAG, "dispatchTouchEvent action = " + action);

        createVelocityTracker(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                setParentScrollable(false);
                mDownX = ev.getRawX();
                Log.d(TAG, "dispatchTouchEvent mDownX = " + mDownX);
                mLastX = mDownX;
                mIntercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = mLastX - ev.getRawX();
                int newScrollX = (int) (getScrollX() + dx);
                mLastX = ev.getRawX();
                Log.d(TAG, "dispatchTouchEvent move newScrollX = " + newScrollX + ", dx = " + dx);
                if (newScrollX < 0) {
                    newScrollX = 0;
                } else if (newScrollX > mTotalWidth - mDisplayWidth) {
                    newScrollX = mTotalWidth - mDisplayWidth;
                }

                setScrollX(newScrollX);

                if (!mIntercept) {
                    mIntercept = Math.abs(mDownX - mLastX) > mTouchSlop / 3;//水平移动了足够的距离才拦截事件
                    if (mIntercept) {
//                        setParentScrollable(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //获取瞬时速度
                int xSpeed = getScrollVelocity();
                recycleVelocityTracker();

                int scrollDistance = (int) (-xSpeed / 8f);
                int scrollX = getScrollX();
                int targetScrollX = scrollX + scrollDistance;
                if (targetScrollX < 0) {
                    targetScrollX = 0;
                } else if (targetScrollX > mTotalWidth - mDisplayWidth) {
                    targetScrollX = mTotalWidth - mDisplayWidth;
                }
                Log.d(TAG, "dispatchTouchEvent xSpeed = " + xSpeed + ", targetScrollX = " + targetScrollX);

                scrollDistance = targetScrollX - scrollX;
                mScroller.startScroll(scrollX, 0, scrollDistance, 0, SCROLL_DURATION);
                invalidate();

                setParentScrollable(false);
                break;
            default:
                break;
        }

        if (mIntercept) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
//        DtLog.d(TAG, "dispatchTouchEvent computeScroll");
        if (mScroller.computeScrollOffset()) {
            int newScrollX = mScroller.getCurrX();
//            DtLog.d(TAG, "dispatchTouchEvent computeScroll: newScrollX = " + newScrollX + ", getScrollX() = " + getScrollX());
            if (getScrollX() != newScrollX) {
                scrollTo(newScrollX, 0);
            }
            postInvalidate();
        }
    }

    @Override
    public void onClick(View v) {
        scrollToCenter(v, true);

        setParentScrollable(true);

        int index = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == v) {
                index = i;
                break;
            }
        }

        switchTab(index);
    }

    public void setCenterByTag(String tag) {
        if (getVisibility() != VISIBLE) {
            return;
        }

        View child = findViewWithTag(tag);
        if (child != null) {
            scrollToCenter(child, false);
        }
    }

    private void scrollToCenter(View v, boolean withAnimation) {
        int scrollX = getScrollX();

        int targetScrollX = (int) (v.getX() - (mDisplayWidth - mItemWidth) / 2);//滚动到Tab栏的正中央位置
        if (targetScrollX < 0) {
            targetScrollX = 0;
        } else if (targetScrollX > mTotalWidth - mDisplayWidth) {
            targetScrollX = mTotalWidth - mDisplayWidth;
        }
        Log.d(TAG, "scrollToCenter: scrollX = " + scrollX + ", targetScrollX = " + targetScrollX);

        if (scrollX == targetScrollX) {
            return;
        }

        if (withAnimation) {
            mScroller.startScroll(scrollX, 0, targetScrollX - scrollX, 0, SCROLL_DURATION);
            postInvalidate();
        } else {
            setScrollX(targetScrollX);
        }
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
        return /*Math.abs*/(velocity);
    }

    public void setOnTabSelectionListener(OnTabSelectionListener listener) {
        mTabSelectionListener = listener;
    }

    public interface OnTabSelectionListener {
        void onTabSelected(final int index);
    }

    public String getTagByIndex(final int index) {
        return mTags[index];
    }

    public String getCurrentTag() {
        if (mTags == null || mTags.length == 0) {
            return "";
        }

        return mTags[mCurrentTabIndex];
    }

    public void refreshSelectedTab(int selectedIndex) {
        float textScale = 16.0f / 14.0f;

        for (int i = 0; i < mTitles.length; i++) {
            final RelativeLayout frameLayout = (RelativeLayout) getChildAt(i);
            TextView textView = (TextView) frameLayout.getChildAt(0);
            View indicator = frameLayout.getChildAt(1);
            if (selectedIndex == i) {
                textView.setSelected(true);
                textView.animate().scaleX(textScale).scaleY(textScale).setDuration(TEXT_SCALE_DURATION);
                indicator.setVisibility(VISIBLE);
            } else {
                textView.setSelected(false);
//                textView.setScaleX(1.0f);
//                textView.setScaleY(1.0f);
                textView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(TEXT_SCALE_DURATION);
                indicator.setVisibility(GONE);
            }

        }
    }

    private void refreshIndicatorByText(TextView textView, View indicator) {
        int width = (int) textView.getPaint().measureText(textView.getText().toString());
        int textWidth = width + mIndicatorExtraWidth;
        ViewGroup.LayoutParams lp = indicator.getLayoutParams();
        lp.width = textWidth;
        indicator.setLayoutParams(lp);
    }

    public void switchTab(final int index) {
        int finalIndex = 0;
        if (index < mTitles.length) {
            finalIndex = index;
        }

        refreshSelectedTab(finalIndex);

        if (mTabSelectionListener != null) {
            mTabSelectionListener.onTabSelected(finalIndex);
        }

        mCurrentTabIndex = finalIndex;
    }

    public void switchTabNoCallback(final int index) {
        int finalIndex = 0;
        if (index < mTitles.length) {
            finalIndex = index;
        }

        refreshSelectedTab(finalIndex);

        mCurrentTabIndex = finalIndex;
    }

    public void switchTab(final String tag) {
        if (mTags == null) {
            return;
        }

        if (TextUtils.isEmpty(tag)) {
            switchTab(0);
            return;
        }

        for (int i = 0; i < mTags.length; i++) {
            if (TextUtils.equals(tag, mTags[i])) {
                switchTab(i);
                return;
            }
        }
        switchTab(0);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
