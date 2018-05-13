package com.sscf.investment.teacherYan;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.message.manager.RecyclerViewManager;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.teacherYan.adapter.AnalysisMarketAdapter;
import com.sscf.investment.teacherYan.adapter.AnalysisMarketAdapter;
import com.sscf.investment.teacherYan.presenter.AnalysisMarketPresent;
import com.sscf.investment.teacherYan.presenter.AnalysisMarketPresent;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.GapItemDecoration;

import java.util.ArrayList;
import BEC.InformationSpiderNews;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by LEN on 2018/4/23.
 */

public class AnalysisMarketFragment extends BaseFragment implements PtrHandler, RecyclerViewManager.OnLoadMoreListener, View.OnClickListener {

    private static final String TAG = AnalysisMarketFragment.class.getSimpleName();
    private AnalysisMarketPresent mPresenter;
    private PtrFrameLayout mPtrLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerViewManager mRecyclerViewManager;
    private AnalysisMarketAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.common_ptr_recycler_view_with_state, null);
        initViews(root);

        mPresenter = new AnalysisMarketPresent(this);
        return root;
    }

    private void initViews(final View root){
        final Activity activity = getActivity();
        mPtrLayout = (PtrFrameLayout) root.findViewById(R.id.ptr);
        mPtrLayout.setPtrHandler(this);

        mRecyclerView = (RecyclerView) mPtrLayout.findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new GapItemDecoration(
                activity.getResources().getDimensionPixelSize(R.dimen.list_market_divider_height)));
        final AnalysisMarketAdapter adapter = new AnalysisMarketAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;

        final View loadingLayoutCenter = root.findViewById(R.id.loading_layout);
        final View failRetryLayoutCenter = root.findViewById(R.id.fail_retry);
        failRetryLayoutCenter.setOnClickListener(this);
        final TextView emptyView = (TextView) root.findViewById(R.id.emptyView);
        emptyView.setText(R.string.no_content);

        mRecyclerViewManager = new RecyclerViewManager(activity, mRecyclerView, mLinearLayoutManager,
                adapter, loadingLayoutCenter, failRetryLayoutCenter, emptyView, this);
        showLoadingLayout();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        mPresenter.requestListData();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mPresenter.requestListData();
    }

    @Override
    public void onLoadMore() {
        mPresenter.requestListDataMore();
    }

    public void refreshComplete() {
        mPtrLayout.refreshComplete();
    }

    public void showLoadingLayout() {
        DtLog.d(TAG, "showLoadingLayout");
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_LOADING);
    }

    public void showRetryLayout() {
        DtLog.d(TAG, "showRetryLayout");
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
    }

    public void showEmptyLayout() {
        DtLog.d(TAG, "showEmptyLayout");
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NO_CONTENT);
    }

    public void showFooterRetryLayout() {
        DtLog.d(TAG, "showFooterRetryLayout");
        mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
    }

    public void showFooterNoMoreLayout() {
        DtLog.d(TAG, "showFooterNoMoreLayout");
        mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
    }

    public void showFooterNormalLayout() {
        mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_NORMAL);
    }

    @Override
    public void onClick(View v) {
        showLoadingLayout();
        mPresenter.requestListData();
    }

    public void updateList( final ArrayList<InformationSpiderNews> informations) {
        DtLog.d(TAG, "updateList informations.size() : " + informations.size());
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NORMAL);
        final AnalysisMarketAdapter adapter = mAdapter;
        adapter.setListData(informations);
        adapter.notifyDataSetChanged();
    }

}
