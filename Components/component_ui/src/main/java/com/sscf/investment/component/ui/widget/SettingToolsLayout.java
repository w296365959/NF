package com.sscf.investment.component.ui.widget;

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
import android.widget.TextView;
import com.sscf.investment.component.ui.R;
import com.sscf.investment.sdk.utils.DeviceUtil;
import java.util.List;

/**
 * Created by davidwei on 2017-03-09.
 *
 */
public final class SettingToolsLayout extends LinearLayout implements ViewPager.OnPageChangeListener {
    private DotIndicatorView mIndicator;

    public SettingToolsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIndicator = (DotIndicatorView) findViewById(R.id.indexIndicator);
    }

    public void init(final int pageCount, final int countPerLine, final List<ToolsItem>[] items) {
        if(pageCount > 1) {
            mIndicator.setVisibility(VISIBLE);
            mIndicator.setIndicatorCount(pageCount, 0);
        } else {
            mIndicator.setVisibility(INVISIBLE);
        }
        final View[] pagerViews = new View[pageCount];
        final Context context = getContext();
        ToolsItemAdapter adapter;
        RecyclerView recyclerView;
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        for (int i = 0; i < pageCount; i++) {
            recyclerView = (RecyclerView) View.inflate(context, R.layout.recycler_view, null);
            recyclerView.setLayoutManager(new GridLayoutManager(context, countPerLine));
            adapter = new ToolsItemAdapter(context, items[i]);
            recyclerView.setAdapter(adapter);
            adapter.setItemClickable(true);
            pagerViews[i] = recyclerView;
            if (i == 0) { // 计算viewpage的高度
                final int itemHeight = DeviceUtil.dip2px(context, 88);
                final LayoutParams params = (LayoutParams) viewPager.getLayoutParams();
                params.height = ((items[0].size() - 1) / countPerLine + 1) * itemHeight;
                viewPager.setLayoutParams(params);
            }
        }

        viewPager.setAdapter(new ToolsPagerAdapter(pagerViews));
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

final class ToolsPagerAdapter extends PagerAdapter {
    View[] mPagerViews;

    public ToolsPagerAdapter(View[] mPagerViews) {
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

final class ToolsItemAdapter extends CommonBaseRecyclerViewAdapter<ToolsItem> {
    public ToolsItemAdapter(Context context, List<ToolsItem> data) {
        super(context, data, R.layout.tab_fragment_setting_tools_item);
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, ToolsItem item, int position) {
        final TextView textView = holder.getView(R.id.textView);
        textView.setText(item.textId);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, item.drawableId, 0, 0);
        holder.getView(R.id.newIcon).setVisibility(item.isNew ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        getItem(position).click(mContext);
        notifyItemChanged(position); // 更新new标签
    }
}
