package com.sscf.investment.splash;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import com.sscf.investment.R;
import com.sscf.investment.interpolator.SineEaseIn;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.TimeUtils;
import java.util.ArrayList;

/**
 * Created by liqf on 2015/9/24.
 */
public final class SplashDialog extends Dialog implements View.OnClickListener, Runnable {
    private static final String TAG = "SplashDialog";
    private static final long ANIMATION_DURATION = 400;

    final SplashPresenter mPresenter;

//    private TextureVideoView mVideoView;

    private View mContentView;

    private OnSplashDisappearedCallback mOnSplashDisappearedCallback;

    private Handler mHandler;
    private int mSplashSecond;

    public interface OnSplashDisappearedCallback {
        void onSplashDisappearedAnimationStart();
        void onSplashDisappearedAnimationEnd();
    }

    public SplashDialog(Context context, OnSplashDisappearedCallback callback) {
        super(context, R.style.SplashDialogTheme);
        DeviceUtil.enableDialogTranslucentStatus(getWindow(), Color.TRANSPARENT);
        mOnSplashDisappearedCallback = callback;
        mHandler = new Handler();
        mPresenter = new SplashPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final long start = System.currentTimeMillis();
        DtLog.d(TAG, start + " SplashDialog.onCreate start");
        super.onCreate(savedInstanceState);
        mPresenter.init();
        final long end = System.currentTimeMillis();
        DtLog.d(TAG, end + " SplashDialog.onCreate end");
        DtLog.d(TAG, "SplashDialog.onCreate spend " + (end - start));
    }

    void showNewInstallSplash() {
        DtLog.d(TAG, "showNewInstallSplash");
        showViewPagerSplash(new int[] {R.drawable.splash_00, R.drawable.splash_01, R.drawable.splash_02, R.drawable.splash_03});
    }

    void showUpgradeSplash() {
        showViewPagerSplashNew(new int[] { R.drawable.splash_0_0, R.drawable.splash_1_0,
                R.drawable.splash_2_0},
                new int[] { R.drawable.splash_0_1, R.drawable.splash_1_1,
                        R.drawable.splash_2_1});
        DtLog.d(TAG, "showUpgradeSplash");
    }

    private void showViewPagerSplash(final int[] drawableIds) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        mContentView = inflater.inflate(R.layout.main_splash_layout, null);
        setContentView(mContentView);
        // finishButton
        final View finishButton = mContentView.findViewById(R.id.finish_button);
        finishButton.setOnClickListener(this);
        new SplashLoadingTask(inflater, mContentView, finishButton, drawableIds)
                .executeOnExecutor(DengtaApplication.getApplication().defaultExecutor);
    }

    private void showViewPagerSplashNew(final int[] drawableIds, final int[] drawableTextIds) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        mContentView = inflater.inflate(R.layout.main_splash_layout, null);
        setContentView(mContentView);
        // finishButton
        final View finishButton = mContentView.findViewById(R.id.finish_button);
        finishButton.setOnClickListener(this);
        new SplashLoadingTaskNew(inflater, mContentView, finishButton, drawableIds, drawableTextIds)
                .executeOnExecutor(DengtaApplication.getApplication().defaultExecutor);
    }

    void showDefaultSplash(final int splashSecond) {
        DtLog.d(TAG, "showDefaultSplash");
        mSplashSecond = splashSecond;
        mContentView = LayoutInflater.from(getContext()).inflate(R.layout.main_splash_layout_half_screen_default, null);
        setContentView(mContentView);
//        if (DeviceUtil.getScreenWidth(getContext()) <= 480)
        {
            showBottom();
            dismissWithAnimation(true);
            return;
        }
    }

    void showFullScreenSplash(final Bitmap bitmap, final boolean skipVisible, int splashSecond) {
        DtLog.d(TAG, "showFullScreenSplash");
        mSplashSecond = splashSecond;
        mContentView = LayoutInflater.from(getContext()).inflate(R.layout.main_splash_layout_full_screen, null);
        mContentView.setOnClickListener(this);

        final CountDownView countDownView = (CountDownView) mContentView.findViewById(R.id.splashSkipButton);
        if (skipVisible) {
            countDownView.startCountDown(splashSecond);
            countDownView.setOnClickListener(this);
        } else {
            countDownView.setVisibility(View.INVISIBLE);
        }

        final ImageView imageView = (ImageView) mContentView.findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);

        setContentView(mContentView);
        dismissWithAnimation(true);
    }

    void showHalfScreenSplash(final Bitmap bitmap, final boolean skipVisible, int splashSecond) {
        DtLog.d(TAG, "showHalfScreenSplash");
        mSplashSecond = splashSecond;
        mContentView = LayoutInflater.from(getContext()).inflate(R.layout.main_splash_layout_half_screen, null);
        mContentView.setOnClickListener(this);

        final CountDownView countDownView = (CountDownView) mContentView.findViewById(R.id.splashSkipButton);
        if (skipVisible) {
            countDownView.startCountDown(splashSecond);
            countDownView.setOnClickListener(this);
        } else {
            countDownView.setVisibility(View.INVISIBLE);
        }

        final ImageView imageView = (ImageView) mContentView.findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
        final ImageView bottomImage = (ImageView) mContentView.findViewById(R.id.splash_bottom);
        bottomImage.setImageResource(getBottomImageResourceId());

        setContentView(mContentView);
        dismissWithAnimation(true);
    }

    private void showBottom() {
        DtLog.e(TAG, "showBottom");
        ImageView imageView = (ImageView) mContentView.findViewById(R.id.splash_bottom);
        playFadeInAnimation(imageView);
        int resourceId = getBottomImageResourceId();
        imageView.setImageResource(resourceId);
    }

    public void stopDefaultSplash() {
    }

    /**
     * 根据是否在某些渠道首发来取不同的底部图标
     * @return 底部图片的resource ID
     */
    private int getBottomImageResourceId() {
        int resourceId = R.drawable.default_logo_bottom;

        //有首发的时候才配置以下代码
        String channel = DengtaApplication.getChannelIDFromManifest();
        DtLog.e(TAG, "channel = " + channel);
//        if (TextUtils.equals(channel, "tencent")) { //应用宝渠道
//            resourceId = R.drawable.channel_icon_yingyongbao;
//        }
//        else if (TextUtils.equals(channel, "baidu"))
//        { //百度手机助手渠道
//            resourceId = R.drawable.channel_icon_baidu;
//        } else if (TextUtils.equals(channel, "le")) { //乐视应用商店渠道
//            resourceId = R.drawable.channel_icon_letv;
//        }
//        if (TextUtils.equals(channel, "huawei")) { //华为应用商店渠道
//            resourceId = R.drawable.channel_icon_huawei;
//        }
        if (TextUtils.equals(channel, "baidu"))
        { //百度手机助手渠道
            resourceId = R.drawable.channel_icon_baidu;
        }

        return resourceId;
    }

    void dismissWithAnimation(final boolean withDelay) {
        DtLog.d(TAG, "dismissWithAnimation withDelay = " + withDelay + " mSplashSecond = " + mSplashSecond);
        long delay = withDelay ? mSplashSecond * DengtaConst.MILLIS_FOR_SECOND : 0;
        mHandler.postDelayed(this, delay);
    }

    public void run() {
        mHandler.removeCallbacks(this);
        playFadeOutAnimation();
    }

    private void playFadeOutAnimation() {
        ViewPropertyAnimator alpha = mContentView.animate().alpha(0).scaleX(1.2f).scaleY(1.2f).setDuration(500);
        alpha.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mOnSplashDisappearedCallback != null) {
                    mOnSplashDisappearedCallback.onSplashDisappearedAnimationStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (SplashDialog.this.isShowing()) {
                    SplashDialog.this.dismiss();
                }

                if (mOnSplashDisappearedCallback != null) {
                    mOnSplashDisappearedCallback.onSplashDisappearedAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private static void playFadeInAnimation(View view) {
        ValueAnimator animatorExpand = ObjectAnimator.ofFloat(view, "alpha", 0, 1.0f);
        animatorExpand.setDuration(ANIMATION_DURATION);
        animatorExpand.setEvaluator(new SineEaseIn(ANIMATION_DURATION));
        animatorExpand.start();
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.finish_button:
            case R.id.splashSkipButton:
                dismissWithAnimation(false);
                break;
            case R.id.content:
                mPresenter.clickContent(getContext());
                break;
            default:
                break;
        }
    }

    private class SplashLoadingTaskNew extends AsyncTask<Integer, Object, Object> implements View.OnTouchListener {
        private final LayoutInflater mInflater;
        private final int[] mDrawableIds;
        private final int[] mDrawableTextIds;
        private ArrayList<View> mViewList;
        private final SplashPagerAdapter mAdapter;
        private float mDownX;
        private int mMaxOverScrollDistance;

        SplashLoadingTaskNew(final LayoutInflater inflater, final View contentView, final View finishButton,
                             final int[] drawableIds, final int[] drawableTextIds) {
            mInflater = inflater;
            final ViewPager viewPager = (ViewPager) contentView.findViewById(R.id.viewpager);
            mDrawableIds = drawableIds;
            mDrawableTextIds = drawableTextIds;
            final int length = drawableIds.length;
            // indicator
            final PageIndicatorView indicator = (PageIndicatorView) contentView.findViewById(R.id.indicator);
            indicator.setCount(length);
            indicator.setHighlightIndex(0);
            // viewPager
            final ArrayList<View> viewList = new ArrayList<>(length);
            mViewList = viewList;

            final View splash = inflater.inflate(R.layout.splash_wizard_because_designer, null);
            final ImageView image = (ImageView) splash.findViewById(R.id.iv_splash_0);
            image.setImageResource(drawableIds[0]);
            final ImageView image1 = (ImageView) splash.findViewById(R.id.iv_splash_1);
            image1.setImageResource(drawableTextIds[0]);
            mViewList.add(splash);

            final SplashPagerAdapter pagerAdapter = new SplashPagerAdapter(viewList, indicator, finishButton);
            viewPager.setAdapter(pagerAdapter);
            viewPager.addOnPageChangeListener(pagerAdapter);

            viewPager.setOnTouchListener(this);
            mAdapter = pagerAdapter;
            mMaxOverScrollDistance = getContext().getResources().getDimensionPixelSize(R.dimen.max_splash_overscroll_distance);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    float downX = mDownX;
                    float upX = event.getX();
                    if (mAdapter.getCurrentPage() == mViewList.size() - 1 && downX - upX >= (mMaxOverScrollDistance)) {
                        dismissWithAnimation(false);
                    }
                    break;
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Integer... params) {
            final int[] drawableIds = mDrawableIds;
            final int length = drawableIds.length;
            final LayoutInflater inflater = mInflater;
            View splash;
            Bitmap bitmap, bitmap1;
            final Resources res = DengtaApplication.getApplication().getResources();
            for (int i = 1; i < length; i++) {
                splash = inflater.inflate(R.layout.splash_wizard_because_designer, null);
                bitmap = BitmapFactory.decodeResource(res, drawableIds[i]);
                bitmap1 = BitmapFactory.decodeResource(res, mDrawableTextIds[i]);
                publishProgress(splash, bitmap, bitmap1);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            final View splash = (View) values[0];
            final Bitmap bitmap = (Bitmap) values[1];
            final Bitmap bitmap1 = (Bitmap) values[2];
            final ImageView image = (ImageView) splash.findViewById(R.id.iv_splash_0);
            image.setImageBitmap(bitmap);

            final ImageView image1 = (ImageView) splash.findViewById(R.id.iv_splash_1);
            image1.setImageBitmap(bitmap1);
            mViewList.add(splash);
            mAdapter.notifyDataSetChanged();
        }
    }


    private class SplashLoadingTask extends AsyncTask<Integer, Object, Object> implements View.OnTouchListener {
        private final LayoutInflater mInflater;
        private final int[] mDrawableIds;
        private ArrayList<View> mViewList;
        private final SplashPagerAdapter mAdapter;
        private float mDownX;
        private int mMaxOverScrollDistance;

        SplashLoadingTask(final LayoutInflater inflater, final View contentView, final View finishButton, final int[] drawableIds) {
            mInflater = inflater;
            final ViewPager viewPager = (ViewPager) contentView.findViewById(R.id.viewpager);
            mDrawableIds = drawableIds;
            final int length = drawableIds.length;
            // indicator
            final PageIndicatorView indicator = (PageIndicatorView) contentView.findViewById(R.id.indicator);
            indicator.setCount(length);
            indicator.setHighlightIndex(0);
            // viewPager
            final ArrayList<View> viewList = new ArrayList<>(length);
            mViewList = viewList;

            final View splash = inflater.inflate(R.layout.splash_wizard, null);
            final ImageView image = (ImageView) splash.findViewById(R.id.image);
            image.setImageResource(drawableIds[0]);
            mViewList.add(splash);

            final SplashPagerAdapter pagerAdapter = new SplashPagerAdapter(viewList, indicator, finishButton);
            viewPager.setAdapter(pagerAdapter);
            viewPager.addOnPageChangeListener(pagerAdapter);

            viewPager.setOnTouchListener(this);
            mAdapter = pagerAdapter;
            mMaxOverScrollDistance = getContext().getResources().getDimensionPixelSize(R.dimen.max_splash_overscroll_distance);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    float downX = mDownX;
                    float upX = event.getX();
                    if (mAdapter.getCurrentPage() == mViewList.size() - 1 && downX - upX >= (mMaxOverScrollDistance)) {
                        dismissWithAnimation(false);
                    }
                    break;
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Integer... params) {
            final int[] drawableIds = mDrawableIds;
            final int length = drawableIds.length;
            final LayoutInflater inflater = mInflater;
            View splash;
            Bitmap bitmap;
            final Resources res = DengtaApplication.getApplication().getResources();
            for (int i = 1; i < length; i++) {
                splash = inflater.inflate(R.layout.splash_wizard, null);
                bitmap = BitmapFactory.decodeResource(res, drawableIds[i]);
                publishProgress(splash, bitmap);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            final View splash = (View) values[0];
            final Bitmap bitmap = (Bitmap) values[1];
            final ImageView image = (ImageView) splash.findViewById(R.id.image);
            image.setImageBitmap(bitmap);
            mViewList.add(splash);
            mAdapter.notifyDataSetChanged();
        }
    }
}

final class SplashPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private final ArrayList<View> mViewList;
    private final PageIndicatorView mIndicator;
    private final View mFinishButton;
    private int mCurrentPage = 0;

    SplashPagerAdapter(final ArrayList<View> viewList, final PageIndicatorView indicator, final View finishButton) {
        mViewList = viewList;
        mIndicator = indicator;
        mFinishButton = finishButton;
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViewList.get(position);
        container.addView(view);
        return view;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    // ---------- OnPageChangeListener -----------
    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {
        mCurrentPage = i;
        if (i == mViewList.size() - 1) {
            mFinishButton.setVisibility(View.VISIBLE);
        } else {
            mFinishButton.setVisibility(View.INVISIBLE);
        }
        mIndicator.setHighlightIndex(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }
}
