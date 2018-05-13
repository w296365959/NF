package com.sscf.investment.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.DotIndicatorView;

/**
 * Created by davidwei on 2015/05/22
 * 多页的RecyclerView
 */
public class RecyclerViewViewPager extends LinearLayout implements ViewPager.OnPageChangeListener {
    private DotIndicatorView mIndicator;

    public RecyclerViewViewPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIndicator = (DotIndicatorView) findViewById(R.id.indicator);
    }

    public void init(final int countPerLine, final RecyclerView.Adapter[] adapters) {
        final int pageCount = adapters == null ? 0 : adapters.length;
        mIndicator.setIndicatorCount(pageCount);

        final View[] pagerViews = new View[pageCount];
        final Context context = getContext();
        RecyclerView recyclerView;
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        for (int i = 0; i < pageCount; i++) {
            recyclerView = (RecyclerView) View.inflate(context, R.layout.recycler_view, null);
            recyclerView.setLayoutManager(new GridLayoutManager(context, countPerLine));
            recyclerView.setAdapter(adapters[i]);
            pagerViews[i] = recyclerView;
        }

        viewPager.setAdapter(new RecyclerViewPagerAdapter(pagerViews));
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.switchIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}

final class RecyclerViewPagerAdapter extends PagerAdapter {
    View[] mPagerViews;

    public RecyclerViewPagerAdapter(View[] mPagerViews) {
        this.mPagerViews = mPagerViews;
    }

    @Override
    public int getCount() {
        return mPagerViews.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View pager = mPagerViews[position];
        container.addView(pager);
        return pager;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mPagerViews[position]);
    }
}