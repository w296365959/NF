package com.sscf.investment.teacherYan;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.sscf.investment.R;
import com.sscf.investment.information.view.ImageBannerView;
import com.sscf.investment.message.manager.RecyclerViewManager;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.teacherYan.adapter.TeacherVideoAdapter;
import com.sscf.investment.teacherYan.presenter.TeacherVideoPresent;
import com.sscf.investment.teacherYan.presenter.TeacherYanBannerPresent;
import com.sscf.investment.teacherYan.presenter.TeacherYanBannerPresent.OnGetBannerCallback;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;
import com.sscf.investment.widget.recyclerview.RecyclerViewHelper;

import java.util.ArrayList;

import BEC.BannerInfo;
import BEC.E_SCENE_TYPE;
import BEC.VideoInfo;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by LEN on 2018/4/23.
 */

public class TeacherVideoFragment extends BaseFragment implements View.OnClickListener,
        RecyclerViewManager.OnLoadMoreListener, PtrHandler, ImageBannerView.OnBannerClickListener , OnGetBannerCallback {

    private static final String TAG = TeacherVideoFragment.class.getSimpleName();

    private PtrFrameLayout mPtrLayout;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TeacherVideoAdapter mAdapter;
    private FrameLayout mHeaderView;
    private ImageBannerView mBannerView;
    private TeacherVideoPresent mPresenter;
    private TeacherYanBannerPresent mBannerPresent;
    private RecyclerViewManager mRecyclerViewManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.common_ptr_recycler_view_with_state, container, false);
        initViews(root);
        mPresenter = new TeacherVideoPresent(this);
        mBannerPresent = new TeacherYanBannerPresent(E_SCENE_TYPE.E_ST_YYSP, this);
        return root;
    }

    private void initViews(final View root) {

        final Activity activity = getActivity();
        mPtrLayout = (PtrFrameLayout) root.findViewById(R.id.ptr);
        mPtrLayout.setPtrHandler(this);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new TeacherVideoAdapter(activity);
        mRecyclerView.setAdapter(mAdapter);

        mHeaderView = new FrameLayout(activity);
        mAdapter.setHeaderView(mHeaderView);

        final View loadingLayoutCenter = root.findViewById(R.id.loading_layout);
        final View failRetryLayoutCenter = root.findViewById(R.id.fail_retry);
        failRetryLayoutCenter.setOnClickListener(this);
        final TextView emptyView = (TextView) root.findViewById(R.id.emptyView);
        emptyView.setText(R.string.no_content);

        mRecyclerViewManager = new RecyclerViewManager(activity, mRecyclerView, mLayoutManager,
                mAdapter, loadingLayoutCenter, failRetryLayoutCenter, emptyView, this);
        showLoadingLayout();

    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View content, View header) {
        if (mRecyclerView.getVisibility() == View.VISIBLE){
            return RecyclerViewHelper.isOnTop(mRecyclerView);
        }
        return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        requestData();
    }

    @Override
    public void onLoadMore() {
        mPresenter.requestMoreData();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        requestData();
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (mBannerView != null) {
            mBannerView.startLooperPic();
        }
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        if (mBannerView != null) {
            mBannerView.stopLooperPic();
        }
    }

    private void requestData() {
        mPresenter.requestData();
        mBannerPresent.requestData();
    }

    public void showFooterNormalLayout() {
        mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_NORMAL);
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

    public void refreshComplete() {
        mPtrLayout.refreshComplete();
    }

    public void updateList(final ArrayList<VideoInfo> list) {
        DtLog.d(TAG, "updateList list : " + list);
        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NORMAL);
        mAdapter.setListData(list);
        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<BannerInfo> mBannerInfos;

    public void refreshBannerLayout(ArrayList<BannerInfo> bannerInfos) {
        DtLog.d(TAG, "refreshBannerLayout bannerInfos : " + bannerInfos);
        final Activity activity = getActivity();
        if (activity == null || bannerInfos.size() == 0) {
            return;
        }

        mBannerInfos = bannerInfos;
        ImageBannerView bannerView = mBannerView;
        if (bannerView == null) {
            LayoutInflater.from(activity).inflate(R.layout.image_banner_layout, mHeaderView, true);
            bannerView = (ImageBannerView) mHeaderView.findViewById(R.id.image_banner_layout);
            final ViewGroup.LayoutParams params = bannerView.getLayoutParams();
            params.height = DeviceUtil.dip2px(activity, 144);
            bannerView.setLayoutParams(params);
            mBannerView = bannerView;
        }
        bannerView.setData(getImageUrls(bannerInfos));
        bannerView.setOnBannerClickListener(this);
    }

    String[] getImageUrls(final ArrayList<BannerInfo> bannerInfos) {
        String[] imageUrls = null;
        if (bannerInfos != null) {
            final int size = bannerInfos.size();
            imageUrls = new String[size];
            BannerInfo bannerInfo = null;
            for (int i = 0; i < size; i++) {
                bannerInfo = bannerInfos.get(i);
                if (bannerInfo != null) {
                    imageUrls[i] = bannerInfo.sImgUrl;
                }
            }
        }
        return imageUrls;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mAdapter && mAdapter.getItemCount() > 0) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBannerClick(int position) {
        final int size = mBannerInfos == null ? 0 : mBannerInfos.size();
        if (position < size) {
            final BannerInfo bannerInfo = mBannerInfos.get(position);
            if (bannerInfo != null) {
                final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                if (scheme != null) {
                    scheme.handleUrl(getContext(), bannerInfo.sSkippUrl);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fail_retry:
                showLoadingLayout();
                requestData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetBannerCallback(ArrayList<BannerInfo> bannerInfos) {
        refreshComplete();
        refreshBannerLayout(bannerInfos);
    }
}
