package com.sscf.investment.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

public final class CommonFragmentPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private final int mCount;
    private final Fragment[] mChildFragments;
    private final FragmentFactoryCallback mCallback;
    private int mCurrentIndex = 0;

    public CommonFragmentPagerAdapter(final FragmentManager fm, final int count, final FragmentFactoryCallback callback) {
        super(fm);
        mCount = count;
        mChildFragments = new Fragment[count];
        mCallback = callback;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mChildFragments[position];
        if (fragment == null) {
            fragment = mCallback.createFragment(position);
            mChildFragments[position] = fragment;
        }
        return fragment;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getCount() {
        return mChildFragments.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 此处不需要调用父类的方法，避免被销毁
//        super.destroyItem(container, position, object);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void setChildUserVisibleHint(boolean isVisibleToUser) {
        if (mCurrentIndex > -1 && mCurrentIndex < mCount) {
            final Fragment currentFragment = mChildFragments[mCurrentIndex];
            if (currentFragment != null) {
                currentFragment.setUserVisibleHint(isVisibleToUser);
            }
        }
    }

    public interface FragmentFactoryCallback {
        Fragment createFragment(int index);
    }
}
