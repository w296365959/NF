package com.sscf.investment.detail.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.sscf.investment.detail.ArticleListActivity;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.detail.presenter.AStockNoticePresenter;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.TimeLineView;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;
import java.util.ArrayList;
import java.util.List;
import BEC.AnnoucementType;
import BEC.BEACON_STAT_TYPE;
import BEC.E_NEMS_GET_SOURCE;
import BEC.E_NEWS_TYPE;
import BEC.NewsDesc;

/**
 * Created by davidwei on 2017/11/28
 * a股的公告
 */
public final class AStockNoticeFragment extends BaseFragment implements OnReloadDataListener, View.OnClickListener {
    public static final String TAG = AStockNoticeFragment.class.getSimpleName();
    private ListView mListView;
    private NewsListAdapter mAdapter;
    private View mLoadingLayout;
    private View mFailRetryLayout;
    private View mEmptyView;

    private String mName;
    private String mDtSecCode;
    private static final int NEWS_TYPE = E_NEWS_TYPE.NT_ANNOUNCEMENT;
    private static final int GET_SOURCE = E_NEMS_GET_SOURCE._E_STOCK_MARKET_GET;

    private TextView mCategoryButton;
    private PopupWindow mCategoryMenu;

    private AStockNoticePresenter mPresenter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(CommonConst.KEY_SEC_CODE);
            mName = args.getString(CommonConst.KEY_SEC_NAME);
        }
        DtLog.d(TAG, "NewsFragment: onCreateView:" + mName);
        setTimeStatSubType();
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_news_a_stock_notice, container, false);
        mListView = (ListView) contextView.findViewById(R.id.news_listview);
        mListView.setDividerHeight(Math.round(getActivity().getResources().getDimension(R.dimen.list_divider_height)));

        mEmptyView = contextView.findViewById(R.id.empty_view);
        mListView.setEmptyView(mEmptyView);
        mListView.setHeaderDividersEnabled(true);

        mFailRetryLayout = contextView.findViewById(R.id.fail_retry);
        mFailRetryLayout.setOnClickListener(this);

        mLoadingLayout = contextView.findViewById(R.id.loading_layout);

        mCategoryButton = (TextView) contextView.findViewById(R.id.categoryButton);
        mCategoryButton.setOnClickListener(this);

        mPresenter = new AStockNoticePresenter(this, mDtSecCode);
        loadData();
        return contextView;
    }

    private void setTimeStatSubType() {
        switch (NEWS_TYPE) {
            case E_NEWS_TYPE.NT_NEWS:
                mTimeStatHelper.setKey(StatConsts.STOCK_INFO_NEWS);
                break;
            case E_NEWS_TYPE.NT_ANNOUNCEMENT:
                mTimeStatHelper.setKey(StatConsts.STOCK_INFO_NOTICE);
                break;
            case E_NEWS_TYPE.NT_REPORT:
                mTimeStatHelper.setKey(StatConsts.STOCK_INFO_REPORT);
                break;
            default:
        }
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        return new TimeStatHelper(BEACON_STAT_TYPE.E_BST_SEC_NEWS);
    }

    private void loadData() {
        mListView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mFailRetryLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);

        mPresenter.requestData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DtLog.d(TAG, "onDestroyView: mName = " + mName);
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
    }

    @Override
    public void onFirstUserInvisible() {
        super.onFirstUserInvisible();
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
    }

    public void onLoadFailed() {
        mListView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mFailRetryLayout.setVisibility(View.VISIBLE);
    }

    public void onLoadComplete() {
        mLoadingLayout.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    public void updateAnnouncementRemind(final ArrayList<BEC.NewsDesc> newsList) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        final View root = getView();
        if (root == null) {
            return;
        }

        int size = newsList == null ? 0 : newsList.size();
        if (size <= 0) {
            return;
        }
        final View announcementRemindLayout = root.findViewById(R.id.announcementRemindLayout);
        announcementRemindLayout.setOnClickListener(this);
        announcementRemindLayout.setVisibility(View.VISIBLE);
        size = Math.min(size, 3);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final Context context = getContext();
        View itemView = null;
        TimeLineView timeLineView = null;
        TextView titleView = null;
        TextView contentView = null;
        NewsDesc newsInfo = null;
        final String format = "yyyy-MM-dd";
        String date = null;
        final LinearLayout itemContainer = (LinearLayout) root.findViewById(R.id.announcementRemindItemContainer);
        itemContainer.removeAllViews();
        for (int i = 0; i < size; i++) {
            newsInfo = newsList.get(i);

            itemView = View.inflate(context, R.layout.fragment_news_announcement_remind_header_item, null);
            itemContainer.addView(itemView, params);

            titleView = (TextView) itemView.findViewById(R.id.title);
            date = TimeUtils.getTimeString(format, newsInfo.iTime * 1000L);
            titleView.setText(date);
            titleView.append(" ");
            titleView.append(newsInfo.sTitle);
            contentView = (TextView) itemView.findViewById(R.id.content);
            contentView.setText(newsInfo.sContent);

            timeLineView = (TimeLineView) itemView.findViewById(R.id.timeLineView);
            if (i == 0) {
                timeLineView.setType(TimeLineView.TYPE_FIRST_DOT);
            } else {
                timeLineView.setType(TimeLineView.TYPE_NORMAL_DOT);
            }
        }
    }

    private View mMoreLayout;

    public void updateNewsList(List<NewsDesc> newsDesc, int status) {
        View moreLayout = mMoreLayout;
        if (status == 1) { //1时表示有更多数据
            if (moreLayout == null) {
                moreLayout = getActivity().getLayoutInflater().inflate(R.layout.more_text, null);
                moreLayout.setOnClickListener(v -> {
                    final Activity activity = getActivity();
                    final Intent intent = new Intent(activity, ArticleListActivity.class);
                    intent.putExtra(CommonConst.KEY_SEC_CODE, mDtSecCode);
                    intent.putExtra(CommonConst.KEY_SEC_NAME, mName);
                    intent.putExtra(CommonConst.KEY_NEWS_TYPE, NEWS_TYPE);
                    intent.putExtra(CommonConst.KEY_GET_SOURCE, GET_SOURCE);
                    activity.startActivity(intent);
                });
                mMoreLayout = moreLayout;
            }
            if (moreLayout.getParent() == null) {
                mListView.addFooterView(moreLayout);
            }
        } else {
            if (moreLayout != null && moreLayout.getParent() != null) {
                mListView.removeFooterView(moreLayout);
            }
        }

        mListView.setFocusable(false); //这一行可以解决listview抢焦点把自己置于屏幕中央的问题

        // 每次new新的adapter，保证addFooterView不会crash
        final NewsListAdapter adapter = new NewsListAdapter(getActivity(), true, mDtSecCode, NEWS_TYPE);
        adapter.setListData(newsDesc);
        mListView.setAdapter(adapter);


        if (newsDesc.size() > 0) {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onReloadData() {
        if (mPresenter != null) {
            mPresenter.requestData();
        }
    }

    private void showCategoryMenu(View v) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        final ArrayList<AnnoucementType> typeList = mPresenter.getAnnoucementTypeList();
        final int size = typeList == null ? 0 : typeList.size();
        if (typeList == null) {
            return;
        }

        final RecyclerView recyclerView = (RecyclerView) View.inflate(activity, R.layout.recycler_view, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(new MenuAdapter(activity, typeList));

        final int height = DeviceUtil.dip2px(activity, (float) ((30 + 0.5) * size));
        final PopupWindow popupWindow = new PopupWindow(recyclerView,
                DeviceUtil.dip2px(activity, 74), height);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.round_rect_bg_popup_window));
        popupWindow.setFocusable(true);

        try {
            popupWindow.showAsDropDown(v, 0, DeviceUtil.dip2px(activity, 2));
        } catch (Exception e) {
            e.printStackTrace();
            StatisticsUtil.reportError(e);
        }
        mCategoryMenu = popupWindow;
    }

    @Override
    public void onClick(View v) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        final int id = v.getId();
        if (id == R.id.fail_retry) {
            loadData();
        } else if (id == R.id.announcementRemindLayout) {
            WebBeaconJump.showNewBigEvent(activity, mDtSecCode, mName);
        } else if (id == R.id.categoryButton) {
            showCategoryMenu(v);
        }
    }

    final class MenuAdapter extends CommonBaseRecyclerViewAdapter<AnnoucementType> {

        public MenuAdapter(final Context context, final List<AnnoucementType> data) {
            super(context, data, R.layout.popup_notice_category_item);
            setItemClickable(true);
        }

        @Override
        public void convert(CommonRecyclerViewHolder holder, AnnoucementType item, int position) {
            holder.setText(R.id.text, item.sLabel);
        }

        @Override
        protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            final AnnoucementType type = getItem(position);
            mCategoryButton.setText(type.sLabel);
            mPresenter.setCurrentAnnoucementType(type.sId);
            if (mCategoryMenu != null) {
                mCategoryMenu.dismiss();
                mCategoryMenu = null;
            }
            StatisticsUtil.reportAction(StatisticsConst.A_STOCK_NOTICE_CLICK_CATEGORY);
        }
    }
}