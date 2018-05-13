package com.sscf.investment.teacherYan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.message.manager.RecyclerViewManager;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.teacherYan.adapter.TalkFreeAdapter;
import com.sscf.investment.teacherYan.manager.MediaControl;
import com.sscf.investment.teacherYan.presenter.TalkFreePresent;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.widget.BaseFragment;

import java.util.ArrayList;

import BEC.WxTalkFree;
import BEC.WxWalkRecord;

/**
 * Created by LEN on 2018/4/20.
 */

public class TalkFreeFragment extends BaseFragment implements View.OnClickListener,
        RecyclerViewManager.OnLoadMoreListener, TeacherYanFragment.OnPullRefresh {

    private static final String TAG = "TalkFreeFragment";

    private TalkFreePresent mPresenter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerViewManager mRecyclerViewManager;
    private TalkFreeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.normal_recycle_view_without_ptr, null);
        initView(root);
        mPresenter = new TalkFreePresent(this);
        return root;
    }

    private void initView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setFocusable(false);
        mAdapter = new TalkFreeAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        final View loadingLayoutCenter = view.findViewById(R.id.loading_layout);
        final View failRetryLayoutCenter = view.findViewById(R.id.fail_retry);
        failRetryLayoutCenter.setOnClickListener(this);
        final TextView emptyView = (TextView) view.findViewById(R.id.emptyView);
        emptyView.setText(R.string.no_content);

        mRecyclerViewManager = new RecyclerViewManager(getActivity(), mRecyclerView, mLinearLayoutManager,
                mAdapter, loadingLayoutCenter, failRetryLayoutCenter, emptyView, this);
        mRecyclerViewManager.setMovePosition(false);
        showLoadingLayout();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        mPresenter.requestListData();
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
    }

    @Override
    public void onFirstUserInvisible() {
        DtLog.e(TAG, "onFirstUserInvisible");
        super.onFirstUserInvisible();
        mAdapter.reset();//停止音乐
    }

    @Override
    public void onUserInvisible() {
        DtLog.e(TAG, "onUserInvisible");
        super.onUserInvisible();
        mAdapter.reset();//停止音乐
    }

    @Override
    public void onPause() {
        super.onPause();
        DtLog.e(TAG, "onPause");
    }

    @Override
    public void onLoadMore() {
        mPresenter.requestListMoreData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter.release();
    }

    public void showLoadingLayout() {
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_LOADING);
    }

    public void showRetryLayout() {
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
    }

    public void showEmptyLayout() {
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NO_CONTENT);
    }

    public void showFooterRetryLayout() {
        mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
    }

    public void showFooterNoMoreLayout() {
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

    public void updateList( final ArrayList<WxTalkFree> records) {
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NORMAL);
        mAdapter.setListData(records);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPullRefresh() {
        mPresenter.requestListData();
    }

    @Override
    public void onScrollToBottom() {
        if (mAdapter.getItemCount() < mPresenter.getTotalCount()) {
            mPresenter.requestListMoreData();
        }
    }
}
