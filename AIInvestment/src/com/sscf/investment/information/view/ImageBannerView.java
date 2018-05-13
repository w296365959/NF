package com.sscf.investment.information.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.hejunlin.superindicatorlibray.CircleIndicator;
import com.hejunlin.superindicatorlibray.LoopViewPager;
import java.util.ArrayList;
import BEC.DtActivityDetail;

/**
 * Created by liqf on 2016/9/6.
 */
public final class ImageBannerView extends FrameLayout {
    private LoopViewPager mViewPager;
    private BannerAdapter mAdapter;
    private CircleIndicator mIndicator;

    private boolean mLooper;

    public ImageBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mViewPager = (LoopViewPager) findViewById(R.id.viewpager);
        mIndicator = (CircleIndicator) findViewById(R.id.indicator);
    }

    public void setData(final String[] imageUrls) {
        final int length = imageUrls == null ? 0 : imageUrls.length;
        setVisibility(length > 0 ? VISIBLE : GONE);
        BannerAdapter adapter = mAdapter;
        if (adapter == null) {
            adapter = new BannerAdapter(getContext(), imageUrls);
            mAdapter = adapter;
        } else {
            adapter.setData(imageUrls);
        }
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
        mLooper = length > 1;
        startLooperPic();//是否设置自动轮播
    }

    public void startLooperPic() {
        mViewPager.setLooperPic(mLooper);
    }

    public void stopLooperPic() {
        mViewPager.setLooperPic(false);
    }

    public static String[] getImageUrls(final ArrayList<DtActivityDetail> bannerInfos) {
        String[] imageUrls = null;
        if (bannerInfos != null) {
            final int size = bannerInfos.size();
            imageUrls = new String[size];
            DtActivityDetail bannerInfo = null;
            for (int i = 0; i < size; i++) {
                bannerInfo = bannerInfos.get(i);
                if (bannerInfo != null) {
                    imageUrls[i] = bannerInfo.sPicUrl;
                }
            }
        }
        return imageUrls;
    }

    public void setOnBannerClickListener(final OnBannerClickListener l) {
        if (mAdapter != null) {
            mAdapter.setOnBannerClickListener(l);
        }
    }

    public interface OnBannerClickListener {
        void onBannerClick(int position);
    }
}

final class BannerAdapter extends PagerAdapter implements View.OnClickListener {
    private String[] mImageUrls;
    private final Context mContext;
    private ImageBannerView.OnBannerClickListener mListener;

    BannerAdapter(final Context context, final String[] imageUrls) {
        mContext = context;
        mImageUrls = imageUrls;
    }

    void setData(final String[] imageUrls) {
        mImageUrls = imageUrls;
    }

    void setOnBannerClickListener(final ImageBannerView.OnBannerClickListener l) {
        mListener = l;
    }

    @Override
    public int getCount() {
        return mImageUrls == null ? 0 : mImageUrls.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(R.color.default_background);
        final int length = mImageUrls == null ? 0 :  mImageUrls.length;
        if (position < length) {
            ImageLoaderUtils.getImageLoader().displayImage(mImageUrls[position], imageView);
        }
        imageView.setTag(position);
        imageView.setOnClickListener(this);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            final Integer position = (Integer) v.getTag();
            if (position != null) {
                mListener.onBannerClick(position);
            }
        }
    }
}
