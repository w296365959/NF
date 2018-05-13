package com.sscf.investment.discover.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.dengtacj.component.router.WebBeaconJump;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.sscf.investment.information.view.ImageBannerView;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import BEC.CategoryInfo;
import BEC.DtActivityDetail;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by davidwei on 2016/01/18.
 * 智能选股的header
 */
public final class StockPickHeader extends LinearLayout implements ImageBannerView.OnBannerClickListener {
    static final int STRATEGY_COUNT_PER_LINE = 4;

    @BindView(R.id.image_banner_layout) ImageBannerView mBannerView;
    @BindView(R.id.recyclerview) RecyclerView mStrategyListLayout;
    private StrategyListAdapter mStrategyAdapter;

    private ArrayList<DtActivityDetail> mBannerList;

    public StockPickHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void onUserVisible() {
        mBannerView.startLooperPic();
    }

    public void onUserInvisible() {
        mBannerView.stopLooperPic();
    }

    public void updateStrategyList(final ArrayList<CategoryInfo> strategyList) {
        final int size = strategyList != null ? strategyList.size() : 0;
        if (size == 0) {
            mStrategyListLayout.setVisibility(GONE);
        } else {
            StrategyListAdapter adapter = mStrategyAdapter;
            if (adapter == null) {
                mStrategyListLayout.setLayoutManager(new GridLayoutManager(getContext(), STRATEGY_COUNT_PER_LINE));
                adapter = new StrategyListAdapter(getContext(), strategyList, R.layout.discover_stock_pick_strategy_item, mStrategyListLayout);
                mStrategyListLayout.setAdapter(adapter);
                mStrategyAdapter = adapter;
            } else {
                adapter.setData(strategyList);
                adapter.notifyDataSetChanged();
            }

            mStrategyListLayout.setVisibility(VISIBLE);
        }
    }

    public void updateBannerList(final ArrayList<DtActivityDetail> bannerList) {
        mBannerView.setData(ImageBannerView.getImageUrls(bannerList));
        mBannerView.setOnBannerClickListener(this);
        mBannerList = bannerList;
    }

    @Override
    public void onBannerClick(int position) {
        final ArrayList<DtActivityDetail> bannerList = mBannerList;
        final int size = bannerList == null ? 0 : bannerList.size();
        if (position < size) {
            DtActivityDetail banner = bannerList.get(position);
            if (banner != null) {
                final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                if (scheme != null) {
                    scheme.handleUrl(getContext(), banner.sUrl);
                }
            }
            StatisticsUtil.reportAction(StatisticsConst.STOCK_PICK_CLICK_BANNER);
        }
    }
}

final class StrategyListAdapter extends CommonBaseRecyclerViewAdapter<CategoryInfo> {
    static final int EXPANDED_COUNT = 8;
    final DisplayImageOptions defaultOptions;
    View mList;
    int mStrategyItemHeight;
    boolean mExpanded = false;
    boolean mMoreShow = false;

    StrategyListAdapter(Context context, List<CategoryInfo> data, int itemLayoutId, View list) {
        super(context, data, itemLayoutId);
        defaultOptions = ImageLoaderUtils.buildDisplayImageOptions(R.drawable.default_consultant_face);
        mList = list;
        mStrategyItemHeight = context.getResources().getDimensionPixelSize(R.dimen.discover_stock_pick_strategy_item_height);
        init(data);
        setItemClickable(true);
    }

    private void init(List<CategoryInfo> data) {
        final int count = data == null ? 0 : data.size();
        if (count <= EXPANDED_COUNT) {
            mExpanded = false;
        }
        mMoreShow = count > EXPANDED_COUNT;
        if (mMoreShow) { // 添加一个null占住更多的位置
            data.add(EXPANDED_COUNT - 1, null);
        }
        updateListHeight();
    }

    @Override
    public int getItemCount() {
        final int count = super.getItemCount();
        return (mMoreShow && !mExpanded) ? EXPANDED_COUNT : count ;
    }

    @Override
    public void setData(List<CategoryInfo> data) {
        super.setData(data);
        init(data);
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, CategoryInfo item, int position) {
        if (item == null) { // 更多
            holder.setText(R.id.strategyText, R.string.more);
            final ImageView iconView = holder.getView(R.id.strategyIcon);
            iconView.setRotation(mExpanded ? 0 : 180);
            iconView.setImageResource(R.drawable.stock_pick_header_more_icon);
        } else {
            holder.setText(R.id.strategyText, item.sName);
            final ImageView iconView = holder.getView(R.id.strategyIcon);
            iconView.setRotation(0);
            ImageLoaderUtils.getImageLoader().displayImage(item.sPicUrl, iconView, defaultOptions);
        }
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        StatisticsUtil.reportAction(StatisticsConst.STOCK_PICK_CLICK_STRATEGY_ITEM);
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        final CategoryInfo item = getItem(position);
        if (item == null) { // 点击更多
            final int startHeight = calculateListHeightByCount(getItemCount());
            mExpanded = !mExpanded;
            final int endHeight = calculateListHeightByCount(getItemCount());
            animateListHeight(startHeight, endHeight);
            float startDegree;
            float endDegree;
            if (mExpanded) {
                startDegree = 180f;
                endDegree = 360f;
            } else {
                startDegree = 0f;
                endDegree = 180f;
            }
            animateMoreRotation(holder.getView(R.id.strategyIcon), startDegree, endDegree);
        } else {
            if(position==0){
                StatisticsUtil.reportAction(StatisticsConst.A_MACD_DISCOVERY_IQSELECT_BANNER_SHENGSHI);
            }else if(position==1){
                StatisticsUtil.reportAction(StatisticsConst.A_MACD_DISCOVERY_IQSELECT_BANNER_ZHANGTING);
            }else if(position==2){
                StatisticsUtil.reportAction(StatisticsConst.A_MACD_DISCOVERY_IQSELECT_BANNER_EVENT_DRIVEN);
            }else if(position==3){
                StatisticsUtil.reportAction(StatisticsConst.A_MACD_DISCOVERY_IQSELECT_BANNER_CONDITION);
            }else if(position==4){
                StatisticsUtil.reportAction(StatisticsConst.A_MACD_DISCOVERY_IQSELECT_BANNER_QUANTIZATION);
            }else if(position==5){
                StatisticsUtil.reportAction(StatisticsConst.A_MACD_DISCOVERE_IQSELECT_BANNER_STRATEGY);
            }else if(position==6){
                StatisticsUtil.reportAction(StatisticsConst.A_MACD_DISCOVERY_IQSELECT_BANNER_WORTH);
            }else if(position==7){//更多

            }else if(position==8){
                StatisticsUtil.reportAction(StatisticsConst.A_MACD_DISCOVERY_IQSELECT_BANNER_BIUGU);
            }else if(position==9){
                StatisticsUtil.reportAction(StatisticsConst.A_MACD_DISCOVERY_IQSELECT_BANNER_SET_BY);
            }
            WebBeaconJump.showCommonWebActivity(mContext, item.sUrl);
        }
    }

    void updateListHeight() {
        // 根据返回的个数，设置高度
        final int count = getItemCount();
        final int height = calculateListHeightByCount(count);
        setListHeight(height);
    }

    /**
     * 根据item的个数计算RecyclerView的高度
     * @param count
     * @return
     */
    int calculateListHeightByCount(final int count) {
        return ((count - 1) / StockPickHeader.STRATEGY_COUNT_PER_LINE + 1) * mStrategyItemHeight;
    }

    void setListHeight(final int height) {
        final ViewGroup.LayoutParams params = mList.getLayoutParams();
        if (params.height != height) {
            params.height = height;
            mList.setLayoutParams(params);
        }
    }

    void animateListHeight(final int startHeight, final int endHeight) {
        final ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.addUpdateListener((animation) -> {
            final int value = (Integer) animation.getAnimatedValue();
            setListHeight(value);
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(200L);
        animator.start();
    }

    void animateMoreRotation(final View view, final float startDegree, final float endDegree) {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", startDegree, endDegree);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(200L);
        animator.start();
    }
}
