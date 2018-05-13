package com.sscf.investment.detail.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.sscf.investment.R;
import com.sscf.investment.detail.manager.SecurityDetailRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.detail.ArticleListActivity;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.widget.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import BEC.BEACON_STAT_TYPE;
import BEC.E_NEMS_GET_SOURCE;
import BEC.E_NEWS_TYPE;
import BEC.NewsDesc;
import BEC.NewsRsp;

/**
 * Created by liqf on 2015/7/15.
 */
public final class NewsFragment extends BaseFragment implements DataSourceProxy.IRequestCallback, OnReloadDataListener {
    public static final String TAG = NewsFragment.class.getSimpleName();
    private ListView mListView;
    private NewsListAdapter mAdapter;
    private View mLoadingLayout;
    private View mFailRetryLayout;
    private View mEmptyView;

    private String mName;
    private String mDtSecCode;
    private int mNewsType = E_NEWS_TYPE.NT_NEWS;
    private int mGetSource = E_NEMS_GET_SOURCE._E_STOCK_MARKET_GET;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(CommonConst.KEY_SEC_CODE);
            mNewsType = args.getInt(CommonConst.KEY_NEWS_TYPE);
            mGetSource = args.getInt(CommonConst.KEY_GET_SOURCE);
            mName = args.getString(CommonConst.KEY_SEC_NAME);
        }
        DtLog.d(TAG, "NewsFragment: onCreateView:" + mName);
        setTimeStatSubType();
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_news, container, false);
        mListView = (ListView) contextView.findViewById(R.id.news_listview);
        mListView.setDividerHeight(Math.round(getActivity().getResources().getDimension(R.dimen.list_divider_height)));

        mEmptyView = contextView.findViewById(R.id.empty_view);
        mListView.setEmptyView(mEmptyView);

        mFailRetryLayout = contextView.findViewById(R.id.fail_retry);
        mFailRetryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        mLoadingLayout = contextView.findViewById(R.id.loading_layout);

        loadData();

        return contextView;
    }

    private void setTimeStatSubType() {
        switch (mNewsType) {
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

        SecurityDetailRequestManager.requestNewsList(mDtSecCode, mNewsType, mGetSource, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DtLog.d(TAG, "onDestroyView: mName = " + mName);
        mAdapter = null;
    }

    @Override
    public void callback(boolean success, EntityObject entity) {
        final NewsRsp newsRsp = EntityUtil.entityToNewsRsp(success, entity);
        ThreadUtils.runOnUiThread(() -> {
            if (isAdded()) {
                if (newsRsp != null) {
                    onLoadComplete();
                    final ArrayList<NewsDesc> newsDesc = newsRsp.stNewsList.vNewsDesc;
                    setData(newsDesc, newsRsp.iStatus);
                } else {
                    onLoadFailed();
                }
            }
        });
    }

    private void onLoadFailed() {
        mListView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mFailRetryLayout.setVisibility(View.VISIBLE);
    }

    private void onLoadComplete() {
        mLoadingLayout.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    private void setData(List<NewsDesc> newsDesc, int status) {
        if (status == 1) { //1时表示有更多数据
            if (mListView.getFooterViewsCount() == 0) {
                final View moreLayout = getActivity().getLayoutInflater().inflate(R.layout.more_text, null);
                moreLayout.setOnClickListener(v -> {
                    final Activity activity = getActivity();
                    Intent intent = new Intent(activity, ArticleListActivity.class);
                    intent.putExtra(CommonConst.KEY_SEC_CODE, mDtSecCode);
                    intent.putExtra(CommonConst.KEY_SEC_NAME, mName);
                    intent.putExtra(CommonConst.KEY_NEWS_TYPE, mNewsType);
                    intent.putExtra(CommonConst.KEY_GET_SOURCE, mGetSource);
                    activity.startActivity(intent);
                });
                mListView.addFooterView(moreLayout);
            }
        }

        mListView.setFocusable(false); //这一行可以解决listview抢焦点把自己置于屏幕中央的问题

        if (mAdapter == null) {
            mAdapter = new NewsListAdapter(getActivity(), true, mDtSecCode, mNewsType);
            mAdapter.setListData(newsDesc);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setListData(newsDesc);
            mAdapter.notifyDataSetChanged();
        }

        if (newsDesc.size() > 0) {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onReloadData() {
        loadData();
    }
}
