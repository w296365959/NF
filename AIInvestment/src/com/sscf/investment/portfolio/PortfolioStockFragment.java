package com.sscf.investment.portfolio;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.portfolio.presenter.PortfolioPresenter;
import com.sscf.investment.portfolio.widget.CommonListDialog;
import com.sscf.investment.sdk.entity.SecListItem;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.BeaconPtrFrameLayout;
import java.util.ArrayList;
import java.util.List;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by xuebinliu on 2015/8/5.
 *
 * 自选股票界面
 */
public final class PortfolioStockFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener, PtrHandler,
        AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener, AbsListView.OnScrollListener, ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = PortfolioStockFragment.class.getSimpleName();

    private View mRoot;
    // 列表
    private BeaconPtrFrameLayout mPtrFrame;
    private ListView mListView;
    private PortfolioStockListAdapter mAdapter;

    // header
    private PortfolioHeaderMarket mPortfolioHeaderMarket;
    private TextView mHeadPrice;
    private TextView mHeadUpDownPercent;
    // 盖楼用的
    private RelativeLayout mStockTitleHeadSticky;
    private TextView mHeadPriceSticky;
    private TextView mHeadUpDownPercentSticky;

    // footer
    // 占位布局，用于item不足一屏时加入占位以便把登录bar布局到页面底部
    private View mListFooterAdd;

    private PortfolioPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DtLog.d(TAG, "onCreateView");
        final View root = inflater.inflate(R.layout.portfolio_stock, null);
        mRoot = root;
        initResources();
        initViews(root, inflater);
        root.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mPresenter = new PortfolioPresenter(this, mPortfolioHeaderMarket);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.release();
        mRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    private int loginLayoutHeight;
    private int marketHeaderHeight;
    private int listHeaderHeight;
    private int marketDividerHeight;
    private int mListFooterAddMiniHeith;
    private int mListItemHeight = 0;        // 单个list item 高度
    private int mListItemRangeHeight = 0;   // list item总共高度
    private int mDividerHeight = 0;         // list item分割线高度

    private void initResources() {
        final Resources resources = getResources();
        loginLayoutHeight = resources.getDimensionPixelSize(R.dimen.portfolio_login_layout_height);
        marketHeaderHeight = resources.getDimensionPixelSize(R.dimen.portfolio_market_height);
        listHeaderHeight = resources.getDimensionPixelSize(R.dimen.list_head_height);
        marketDividerHeight = resources.getDimensionPixelSize(R.dimen.list_horizontal_divider_height);
        mListFooterAddMiniHeith = resources.getDimensionPixelSize(R.dimen.list_item_height);
        mListItemHeight = resources.getDimensionPixelSize(R.dimen.list_item_height);
        mDividerHeight = resources.getDimensionPixelSize(R.dimen.list_divider_height);
    }

    private void initViews(View root, LayoutInflater inflater) {
        mPtrFrame = (BeaconPtrFrameLayout) root.findViewById(R.id.ptr);
        mPtrFrame.setPtrHandler(this);
        mPtrFrame.setSupportDisallowInterceptTouchEvent(true);

        final ListView listView = (ListView) mPtrFrame.findViewById(R.id.list);
        mListView = listView;
        listView.setHeaderDividersEnabled(true);

        // 盖楼，悬浮list head的布局和控件事件监听
        mStockTitleHeadSticky = (RelativeLayout) root.findViewById(R.id.portfolio_stock_list_head_id);
        mStockTitleHeadSticky.setVisibility(View.GONE);
        root.findViewById(R.id.portfolio_stock_list_head_name).setOnClickListener(this);
        mHeadPriceSticky = (TextView) root.findViewById(R.id.portfolio_stock_list_head_price);
        mHeadPriceSticky.setOnClickListener(this);
        mHeadUpDownPercentSticky = (TextView) root.findViewById(R.id.portfolio_stock_list_head_updown);
        mHeadUpDownPercentSticky.setOnClickListener(this);

        // header
        // 指数
        mPortfolioHeaderMarket = (PortfolioHeaderMarket) inflater.inflate(R.layout.portfolio_stock_market, null);
        listView.addHeaderView(mPortfolioHeaderMarket);

        // list head
        final View listTitleView = inflater.inflate(R.layout.portfolio_stock_list_head, null, false);
        listTitleView.findViewById(R.id.portfolio_stock_list_head_name).setOnClickListener(this);
        mHeadPrice = (TextView) listTitleView.findViewById(R.id.portfolio_stock_list_head_price);
        mHeadPrice.setOnClickListener(this);
        mHeadUpDownPercent = (TextView) listTitleView.findViewById(R.id.portfolio_stock_list_head_updown);
        mHeadUpDownPercent.setOnClickListener(this);
        // list stock head
        listView.addHeaderView(listTitleView);

        // footer
        // 底部占位用
        mListFooterAdd = inflater.inflate(R.layout.portfolio_list_footer_add, null);
        mListFooterAdd.findViewById(R.id.add_portfolio_layout_id).setOnClickListener(this);
        listView.addFooterView(mListFooterAdd);

        final LoginStatusLayout loginLayout = (LoginStatusLayout) inflater.inflate(R.layout.portfolio_login_status_layout, null);
        loginLayout.updateUserInfo();
        // 添加底部用户登录状态view
        listView.addFooterView(loginLayout);

        // 特别注意：setAdapter一定要在addHeaderView，addFooterView之后调用
        mAdapter = new PortfolioStockListAdapter(getActivity(), this);
        listView.setAdapter(mAdapter);

        listView.setOnTouchListener(this);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        DtLog.d(TAG, "onResume");
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        mPresenter.init();
    }

    @Override
    public void onUserVisible() {
        DtLog.e(TAG, "onUserVisible");
        super.onUserVisible();
        // TODO 可优化为自选股变化以后，才去重新刷新列表
        mPresenter.updateData();
    }

    @Override
    public void onFirstUserInvisible() {
        super.onFirstUserInvisible();
        if (mPresenter != null) {
            mPresenter.stopRefresh();
        }
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        mPresenter.stopRefresh();
    }

    public int getFirstVisiblePosition() {
        return mListView != null ? mListView.getFirstVisiblePosition() : 0;
    }

    public int getLastVisiblePosition() {
        return mListView != null ? mListView.getLastVisiblePosition() : 0;
    }

    public void updateHeadPriceArrowIcon(final int drawableId) {
        mHeadPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0);
        mHeadPriceSticky.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0);
    }

    public void updateHeadUpDownArrowIcon(final int drawableId) {
        mHeadUpDownPercent.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0);
        mHeadUpDownPercentSticky.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0);
    }

    /**
     * 更新涨跌额列的列名
     */
    public void updateHeadChangeText(final int changeState) {
        if (mHeadUpDownPercent != null) {
            int titleId = 0;
            switch (changeState) {
                case PortfolioPresenter.CHANGE_STATE_PERCENT:
                    titleId = R.string.delta;
                    break;
                case PortfolioPresenter.CHANGE_STATE_PRICE:
                    titleId = R.string.stock_detail_change;
                    break;
                case PortfolioPresenter.CHANGE_STATE_MARKET_VALUE:
                    titleId = R.string.stock_detail_market_value;
                    break;
                default:
                    return;
            }
            mHeadUpDownPercent.setText(titleId);
            mHeadUpDownPercentSticky.setText(titleId);
        }
    }

    public void updatePortfolioList(final List<StockDbEntity> list, final int changeState) {
        mAdapter.setData(list);
        mAdapter.setChangeState(changeState);
        mAdapter.notifyDataSetChanged();
    }

    public void updateItemPositionState() {
        mAdapter.notifyDataSetChanged();
    }

    public void updateQuote() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        DtLog.d(TAG, "onClick v id=" + v.getId());
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.add_portfolio_layout_id://+添加股票
                if (TimeUtils.isFrequentOperation()) {
                    return;
                }
                CommonBeaconJump.showSearch(activity);
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_CLICK_ADD);
                break;
            case R.id.portfolio_stock_list_head_name://编辑
                if (TimeUtils.isFrequentOperation()) {
                    return;
                }
                activity.startActivity(new Intent(activity, PortfolioStockEditActivity.class));
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_CLICK_EDIT);
                break;
            case R.id.portfolio_stock_list_head_price://最新价
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_CLICK_CHANGE_PRICE);
                mPresenter.clickHeaderPrice();
                break;
            case R.id.portfolio_stock_list_head_updown://涨跌幅
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_CLICK_CHANGE_UPDOWN);
                mPresenter.clickHeadUpdown();
                break;
            case R.id.portfolio_stock_listitem_change://item涨跌幅具体值
                mPresenter.clickItemChange();
                break;
            default:
                break;
        }
    }

    @Override
    public void onGlobalLayout() {
//        if (mCurrentStockList != null) {
//            updateFooterAddHeight(mCurrentStockList);
//        }
    }

    /**
     * 计算list区域的高度，界面的高度减去三个header的高度和login bar高度
     * @return
     */
    public int computeListItemRange() {
        if (mRoot == null || mRoot.getHeight() == 0) {
            return 0;
        }

        return mRoot.getHeight() - marketHeaderHeight - listHeaderHeight - loginLayoutHeight - marketDividerHeight;
    }

    private void updateFooterAddHeight(List<StockDbEntity> stockList) {
        mListItemRangeHeight = computeListItemRange();

        if (mListItemRangeHeight != 0) {
            // 因为item填不满list，需要添加占位布局，用List区域总高度减去List Item的总高度和List Divider的总高度
            float stubHeight = mListItemRangeHeight -
                    stockList.size() * mListItemHeight -
                    (stockList.size() + 2) * mDividerHeight;
            AbsListView.LayoutParams params = (AbsListView.LayoutParams) mListFooterAdd.getLayoutParams();
            if (params != null) {
                params.height = stubHeight < mListFooterAddMiniHeith ? mListFooterAddMiniHeith : Math.round(stubHeight - 0.5f);
                mListFooterAdd.setLayoutParams(params);
            }
        }
    }

    // --------------- ptr 事件处理 -------------
    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if (mListView.getVisibility() == View.VISIBLE) {
            boolean notOnTop = mListView.getChildCount() > 0
                    && (mListView.getFirstVisiblePosition() > 0
                    || mListView.getChildAt(0).getTop() < mListView.getPaddingTop());
            return !notOnTop;
        }
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(final PtrFrameLayout frame) {
        DtLog.d(TAG, "onRefreshBegin: onUIRefresh");
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
        }

        mPresenter.refresh();
        StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_PULL_REFRESH);
        frame.refreshComplete(); // 直接结束下拉动画，保证动画连贯
    }
    // --------------- ptr 事件处理 -------------
    // --------------- list view 事件处理 -------------
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mPtrFrame.requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                if (!mPresenter.isSortingList()) {
                    mPresenter.refresh();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem >= 1) {
            if (mStockTitleHeadSticky.getVisibility() == View.GONE) {
                mStockTitleHeadSticky.setVisibility(View.VISIBLE);
            }
        } else {
            if (mStockTitleHeadSticky.getVisibility() == View.VISIBLE) {
                mStockTitleHeadSticky.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DtLog.d(TAG, "onItemClick position=" + position + ", id=" + id);
        if (id == -1) {
            return;
        }

        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        final StockDbEntity currentEntity = mAdapter.getItem((int)id);
        int count = mAdapter.getCount();
        final ArrayList<SecListItem> secItems = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            StockDbEntity entity = mAdapter.getItem(i);
            SecListItem secItem = new SecListItem();
            secItem.setDtSecCode(entity.getDtSecCode());
            secItem.setName(entity.getSzName());
            secItems.add(secItem);
        }
        CommonBeaconJump.showSecurityDetailActivity(activity, currentEntity.getDtSecCode(), currentEntity.getSzName(), secItems);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        DtLog.d(TAG, "onItemLongClick position=" + position + ", id=" + id);
        if (id == -1) {
            return false;
        }

        final Activity activity = getActivity();
        if (activity == null) {
            return true;
        }

        final int currentId = (int) id;
        final StockDbEntity entity = mAdapter.getItem(currentId);

        final ArrayList<String> texts = new ArrayList<String>(3);
        texts.add(activity.getString(R.string.delete));
        texts.add(activity.getString(R.string.portfolio_edit_title_stick));
        texts.add(activity.getString(entity.isPosition ? R.string.portfolio_delete_position : R.string.portfolio_add_position));
        final CommonListDialog editDialog = new CommonListDialog(activity);

        editDialog.showListDialog(texts, itemPosition -> {
            switch (itemPosition) {
                case 0: // 删除
                    DtLog.d(TAG, "onItemLongClick del code=" + entity.getDtSecCode());
                    mPresenter.showDeleteDialog(activity, entity);
                    StatisticsUtil.reportAction(StatisticsConst.A_AD_OPTIONAL_STOCK_DEL_COUNT);
                    break;
                case 1: // 置顶
                    mPresenter.changePosition(currentId, 0);
                    break;
                case 2: // 持仓
                    mPresenter.clickPosition(entity);
                    break;
            }
            editDialog.dismiss();
        });
        editDialog.show();
        return true;
    }
    // --------------- list view 事件处理 -------------
}
